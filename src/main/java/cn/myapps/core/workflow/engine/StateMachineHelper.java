package cn.myapps.core.workflow.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.AutoNode;
import cn.myapps.core.workflow.element.CompleteNode;
import cn.myapps.core.workflow.element.Element;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.Relation;
import cn.myapps.core.workflow.element.StartNode;
import cn.myapps.core.workflow.element.SubFlow;
import cn.myapps.core.workflow.element.SuspendNode;
import cn.myapps.core.workflow.engine.state.StateCreator;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowHistory;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.Type;
import cn.myapps.core.workflow.utility.NameList;
import cn.myapps.core.workflow.utility.NameNode;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.DefaultProperty;

/**
 * 
 * @author Marky
 * 
 */
public class StateMachineHelper {
	private static Logger LOG = Logger.getLogger(StateMachineHelper.class);

	public boolean isDisplySubmit = false; // 是否显示提交按钮

	public boolean isDisplyFlow = true; // 是否显示流程

	public boolean isFrontEdit;// 是否显示前台手动调整流程

	public boolean isOnlyShowRetracementButton = false;// 只显示回撤按钮

	private static BillDefiProcess getBillDefiProcess() throws Exception {
		return StateMachine.getBillDefiProcess();
	}

	private static RelationHISProcess getRelationHISProcess(String applicationId)
			throws Exception {
		return StateMachine.getRelationHISProcess(applicationId);
	}

	public StateMachineHelper() {
	}

	public StateMachineHelper(Document doc) {
		initFlowImage(doc);
	}

	/**
	 * 初始化流程图
	 * 
	 * @param flowid
	 * @param docid
	 * @param request
	 */
	private void initFlowImage(Document doc) {
		try {
			FlowStateRT instance = doc.getState();
			if (instance == null || doc.getIstmp())//新的文档无需产生流程图片
				return;
			String path = DefaultProperty.getProperty("BILLFLOW_DIAGRAMPATH");
			Environment env = Environment.getInstance();
			String imgPath = env.getRealPath(path + "/" + instance.getId()
					+ ".jpg");

			File file = new File(imgPath);
			if (!file.exists()) {
				if (instance != null) {
					StateMachine.updateImage(instance);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据当前用户获取当前流程所有开始节点的下一个手动节点(第一个节点)的列表
	 * 
	 * @param flowid
	 * @param user
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<Node> getFirstNodeList(Document doc, String flowid,
			WebUser user, ParamsTable params) throws Exception {
		BillDefiVO flowVO = (BillDefiVO) getBillDefiProcess().doView(flowid);
		return flowVO.getFirstNodeList(doc, user, params);
	}

	/**
	 * 根据第一个节点获取开始节点
	 * 
	 * @param flowid
	 * @param firstNode
	 * @return
	 * @throws Exception
	 */
	public Collection<StartNode> getStartNodeListByFirstNode(String flowid,
			Node firstNode) throws Exception {
		BillDefiVO flowVO = (BillDefiVO) getBillDefiProcess().doView(flowid);
		return StateMachine.getStartNodeListByFirstNode(flowVO, firstNode);
	}

	/**
	 * 根据当前节点id获取回退节点列表
	 * 
	 * @param currid
	 * @param user
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<Node> getBackToNodeList(Document doc, NodeRT currNodeRT,
			WebUser user) throws Exception {
		return getBackToNodeList(doc, currNodeRT, user, 0);
	}

	/**
	 * 获取回退节点（按历史痕迹回退|定制回退）
	 * 
	 * @param doc
	 * @param currid
	 * @param user
	 * @param flowState
	 * @return
	 * @throws Exception
	 */
	public Collection<Node> getBackToNodeList(Document doc, NodeRT currNodeRT,
			WebUser user, int flowState) throws Exception {
		BillDefiVO flowVO = null;
		Collection<Node> backToNodeList = new ArrayList<Node>();
		flowVO = doc.getState().getFlowVO();
		FlowDiagram fd = flowVO.toFlowDiagram();
		Node node = (Node) fd.getElementByID(currNodeRT.getNodeid());
		// 手工节点回退
		if (node instanceof ManualNode) {
			if (((ManualNode) node).backType == 1 && ((ManualNode) node).cBack) {
				backToNodeList = StateMachine.getBackToNodeList(flowVO,
						currNodeRT, user, 0);
			} else if (((ManualNode) node).backType == 0
					&& ((ManualNode) node).cBack) {
				backToNodeList = StateMachine.getBackToNodeList(doc, flowVO,
						currNodeRT, user, flowState);
			} else if (((ManualNode) node).retracementScript == null) {// 兼容旧版本
				backToNodeList = StateMachine.getBackToNodeList(doc, flowVO,
						currNodeRT, user, flowState);
			}
		} else {// 非手工节点回退(如：挂起)
			backToNodeList = StateMachine.getBackToNodeList(doc, flowVO,
					currNodeRT, user, flowState);
		}

		return backToNodeList;

	}
	
	/**
	 * 获取当前流程状态下允许催办的人工节点
	 * @return
	 * @throws Exception
	 */
	public List<ManualNode> getAllowUrge2ApprovalNodes(FlowStateRT instance,ParamsTable params,WebUser user,FlowDiagram fd) throws Exception {
		List<ManualNode> list = new ArrayList<ManualNode>();
		// 获取当前流程实例的节点(nodeRT)
		Collection<NodeRT> noderts = instance.getNoderts();
		//获取当前NodeRT对应的ManualNode对象
		for (Iterator<NodeRT> iterator = noderts.iterator(); iterator.hasNext();) {
			NodeRT nodeRT = iterator.next();
			Node node =fd.getNodeByID(nodeRT.getNodeid());
			if(node instanceof ManualNode){
				if(((ManualNode)node).isAllowUrge2Approval(instance.getDocument(), params, user)){
					//把符合允许催办的ManualNode对象加入list
					list.add((ManualNode) node);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 获取当前流程状态下允许催办的状态节点
	 * @return
	 * @throws Exception
	 */
	public List<NodeRT> getAllowUrge2ApprovalNodeRTs(FlowStateRT instance,ParamsTable params,WebUser user,FlowDiagram fd) throws Exception {
		List<NodeRT> list = new ArrayList<NodeRT>();
		// 获取当前流程实例的节点(nodeRT)
		Collection<NodeRT> noderts = instance.getNoderts();
		//获取当前NodeRT对应的ManualNode对象
		for (Iterator<NodeRT> iterator = noderts.iterator(); iterator.hasNext();) {
			NodeRT nodeRT = iterator.next();
			Node node =fd.getNodeByID(nodeRT.getNodeid());
			if(node instanceof ManualNode){
				if(((ManualNode)node).isAllowUrge2Approval(instance.getDocument(), params, user)){
					list.add(nodeRT);
				}
			}
		}
		
		return list;
	}

	/**
	 * 获取当前结点
	 * 
	 * @param flowid
	 * @param nodeid
	 * @return
	 * @throws Exception
	 */
	public static Node getCurrNode(String flowid, String nodeid)
			throws Exception {
		BillDefiProcess process = (BillDefiProcess) ProcessFactory
				.createProcess(BillDefiProcess.class);
		BillDefiVO flowVO = (BillDefiVO) process.doView(flowid);
		return getCurrNode(flowVO, nodeid);
	}

	/**
	 * 获取当前结点
	 * 
	 * @param flowid
	 * @param nodeid
	 * @return
	 * @throws Exception
	 */
	public static Node getCurrNode(BillDefiVO flowVO, String nodeid)
			throws Exception {
		return StateMachine.getCurrNode(flowVO, nodeid);
	}

	/**
	 * 获取所有运行时节点
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Collection<NodeRT> getAllNodeRT(Document doc)
			throws Exception {
		if (doc != null && doc.getState() != null) {
			return doc.getState().getNoderts();
		}
		return new ArrayList<NodeRT>();
	}

	/**
	 * 获取当前流程所有历史记录
	 * 
	 * @param docid
	 * @param flowid
	 * @return
	 * @throws Exception
	 */
	public static Collection<RelationHIS> getAllRelationHIS(String docid,
			String flowid, String applicationId) throws Exception {
		return getRelationHISProcess(applicationId).doQuery(docid, flowid);

	}

	/**
	 * 根据进行挂起操作的用户获取运行时节点
	 * 
	 * @return 根据进行挂起操作的用户,运行时的节点
	 * @throws Exception
	 */
	public NodeRT getSuspendNodeRT(Document doc, String applicationId)
			throws Exception {
		// 获取最后一条历史记录
		RelationHIS relationHIS = getRelationHISProcess(applicationId)
				.doViewLast(doc.getId(), doc.getFlowid());
		// 此历史记录只有唯一用户历史记录
		Collection<ActorHIS> actorhissList = relationHIS.getActorhiss();
		ActorHIS actorhis = (ActorHIS) actorhissList.iterator().next();

		// 获取所有NodeRT
		Collection<NodeRT> nodertList = getAllNodeRT(doc);

		// 获取只有一个处理者的NodeRT列表
		Collection<NodeRT> newNodertList = new ArrayList<NodeRT>();
		for (Iterator<NodeRT> iter = nodertList.iterator(); iter.hasNext();) {
			NodeRT nodert = (NodeRT) iter.next();
			Collection<ActorRT> actorrtList = nodert.getActorrts();
			if (actorrtList.size() == 1) {
				newNodertList.add(nodert);
			}
		}

		// 根据进行挂起操作的用户获取NodeRT
		if (newNodertList != null) {
			for (Iterator<NodeRT> iter = newNodertList.iterator(); iter
					.hasNext();) {
				NodeRT nodert = (NodeRT) iter.next();
				ActorRT actort = (ActorRT) nodert.getActorrts().iterator()
						.next();
				if ((actorhis.getActorid()).equals(actort.getActorid())) {
					return nodert;
				}
			}
		}
		return null;
	}

	/**
	 * 判断当前用户是否可编辑文档. 此实现为通过当前用户、此Document与相应的流程获取当前用户节点是否为空 或根据此Document id
	 * 与相应的流程id来获取当前流程状态是否为0(FlowState.START)作为判断. 若返回true代表用户可以对文档编辑,否则不可以.
	 * 
	 * @param doc
	 * 
	 * @param webUser
	 *            当前用户
	 * 
	 * @return 如果是流程处理者则返回true,否则返回false
	 * @throws Exception
	 */
	public static boolean isDocEditUser(final Document doc, WebUser webUser)
			throws Exception {
		Document flowDoc = doc;
		if (doc == null)
			return false;
		if (doc.getParent() != null) {
			flowDoc = doc.getParent();
			if (!StringUtil.isBlank(flowDoc.getId())
					&& !StringUtil.isBlank(flowDoc.getStateid())) {
				FlowStateRTProcess stateProcess = (FlowStateRTProcess) ProcessFactory
						.createRuntimeProcess(FlowStateRTProcess.class,
								flowDoc.getApplicationid());
				if (stateProcess.isMultiFlowState(flowDoc)) {// 有多个流程实例
					FlowStateRT currInstance = stateProcess.getCurrFlowStateRT(
							flowDoc, webUser, null);// 绑定一个用户可审批的文档实例
					if (currInstance == null) {
						currInstance = stateProcess
								.getParentFlowStateRT(flowDoc);// 将文档加载到主流程实例
					}
					if (currInstance != null) {
						flowDoc.setState(currInstance);
					}
				}
			}
		}
		NodeRT nodert = StateMachine.getCurrUserNodeRT(flowDoc, webUser, null);

		// 流程尚未启动或当前用户为审批人时可编辑
		return flowDoc.getStateInt()==0 ||StringUtil.isBlank(flowDoc.getStateid()) || nodert != null;
	}

	/**
	 * 返回的字符串为重定义后的XML，表达显示当前用户运行时节点
	 * 
	 * @param flowid
	 *            flow id
	 * @param docid
	 *            Document id
	 * @param webUser
	 *            webuser
	 * @return 字符串为显示当前用户运行时节点
	 * @throws Exception
	 */
	public String toFlowXMLText(Document doc, WebUser webUser,ParamsTable params) throws Exception {
		StringBuffer buffer = new StringBuffer();
		BillDefiVO flowVO = doc.getFlowVO();
		NodeRT nodert = null;
		FlowDiagram fd = null;
		if (flowVO != null) {
			fd = flowVO.toFlowDiagram();
			nodert = StateMachine.getCurrUserNodeRT(doc, webUser, null);
		}
		// 获取当前结点
		Node currnode = null;
		State state = null; // 当前节点状态
		if (nodert != null) {
			String currnodeid = nodert.getNodeid();
			if (currnodeid != null) {
				currnode = (Node) fd.getElementByID(currnodeid);
				state = StateCreator.getNodeState(currnode);
			}
			buffer.append("<").append(MobileConstant.TAG_FORM).append(" ");
			buffer.append(" ").append(MobileConstant.ATT_TITLE).append("='")
					.append("{*[Workflow]*}").append("'>");
			buffer.append("<").append(MobileConstant.TAG_WORKFLOW).append(">");
			buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD)
					.append(" ").append(MobileConstant.ATT_NAME)
					.append("='_currid' >" + currnodeid + "</")
					.append(MobileConstant.TAG_HIDDENFIELD).append(">");

			if (state != null && state.toInt() == FlowState.RUNNING) {// 送下一步
				isDisplySubmit = true;

				Collection<Node> nextNodeList = fd.getNextNodeList(currnodeid,doc,params,webUser);
				boolean isToPerson = false;
				if (nextNodeList != null && nextNodeList.size() > 0) {
					Iterator<Node> it3 = nextNodeList.iterator();
					StringBuffer buf1 = new StringBuffer();
					StringBuffer buf2 = new StringBuffer();
					StringBuffer userBuf = new StringBuffer(); // 审批用户列表
					while (it3.hasNext()) {
						Node nextNode = (Node) it3.next();
						boolean needToPersion = false; // 是否显示指定审批人
						Collection<BaseUser> userList = null;
						if ((nextNode instanceof ManualNode)
								&& ((ManualNode) currnode).isToPerson) {// 手动节点指定审批人
							userList = StateMachineUtil.getPrincipalList(doc,
									new ParamsTable(), nextNode,
									doc.getDomainid(), doc.getApplicationid(),
									webUser);

							// 会签模式时,最后一个审批者才可以去指定审批人
							boolean isAllowToPerson = this.isAllowToPerson(
									(ManualNode) currnode, nodert);

							if (userList != null && userList.size() > 1
									&& isAllowToPerson) {// 当下一步审批人超过1人时才出现指定审批人操作
								needToPersion = true;
							}
						}
						Node node = getCurrNode(flowVO, nodert.getNodeid());
						boolean issplit = false;
						boolean isgather = false;
						if (node != null && node instanceof ManualNode) {
							issplit = ((ManualNode) node).issplit;
							isgather = ((ManualNode) node).isgather;
						}

						boolean flag = false;
						if (isgather) {// 如果为聚合
							Collection<NodeRT> nodertList = getAllNodeRT(doc);
							if (nodertList != null) {
								for (Iterator<NodeRT> iter = nodertList
										.iterator(); iter.hasNext();) {
									NodeRT nrt = (NodeRT) iter.next();

									// 判断所有运行时节点是否为当前节点
									if (!nrt.getNodeid().equals(
											nodert.getNodeid())) {
										Node nd = (Node) fd.getElementByID(nrt
												.getNodeid());

										Node currNode = (Node) fd
												.getElementByID(nodert
														.getNodeid());

										Collection<Node> followTo = fd
												.getAllFollowNodeOnPath(nd.id,doc,params,webUser);
										if (followTo != null
												&& followTo.contains(currNode)) {
											flag = true;
											break;
										}
									}
								}
							}
						}

						if (!flag) {
							@SuppressWarnings("unused")
							boolean isOthers = false;
							@SuppressWarnings("unused")
							String id = "next";
							@SuppressWarnings("unused")
							String flowOperation = FlowType.RUNNING2RUNNING_NEXT;
							if (!(nextNode instanceof ManualNode)) {// 下一个节点中是否存在suspend
								isOthers = true;
								id = "other";
								if (nextNode instanceof SuspendNode) {
									flowOperation = FlowType.RUNNING2SUSPEND;
								} else if (nextNode instanceof CompleteNode) {
									flowOperation = FlowType.RUNNING2COMPLETE;
								}
							}
							if (issplit) {
								buf1.append("<")
										.append(MobileConstant.TAG_OPTION)
										.append("");
								buf1.append(" ")
										.append(MobileConstant.ATT_SELECTED)
										.append("='").append(0);
								buf1.append("' ")
										.append(MobileConstant.ATT_VALUE)
										.append("='" + nextNode.id + "' >");
								buf1.append(StringUtil
										.dencodeHTML(nextNode.name));
								buf1.append("</")
										.append(MobileConstant.TAG_OPTION)
										.append(">");
							} else {
								buf2.append("<")
										.append(MobileConstant.TAG_OPTION)
										.append("");
								buf2.append(" ")
										.append(MobileConstant.ATT_VALUE)
										.append("='" + nextNode.id + "' >");
								buf2.append(StringUtil
										.dencodeHTML(nextNode.name));
								buf2.append("</")
										.append(MobileConstant.TAG_OPTION)
										.append(">");
							}
							if (needToPersion) {
								userBuf.append("<").append(
										MobileConstant.TAG_USERFIELD);
								userBuf.append(" ").append(
										MobileConstant.ATT_ID + "='"
												+ nextNode.id + "'");
								userBuf.append(" ").append(
										MobileConstant.ATT_NAME + "='"
												+ nextNode.id + "'>");
								Iterator<BaseUser> ituser = userList.iterator();
								while (ituser.hasNext()) {
									BaseUser user = ituser.next();
									userBuf.append("<")
											.append(MobileConstant.TAG_OPTION)
											.append("");
									userBuf.append(" ")
											.append(MobileConstant.ATT_VALUE)
											.append("='");
									userBuf.append(
											HtmlEncoder.encode(user.getId()))
											.append("'>");
									userBuf.append(user.getName());
									userBuf.append("</")
											.append(MobileConstant.TAG_OPTION)
											.append(">");
								}
								userBuf.append("</").append(
										MobileConstant.TAG_USERFIELD + ">");
							}
						}
					}
					if (buf1.toString().trim().length() > 0) {
						buffer.append("<")
								.append(MobileConstant.TAG_CHECKBOXFIELD)
								.append("  ")
								.append(MobileConstant.ATT_NAME)
								.append("='_nextids'>" + buf1.toString() + "</")
								.append(MobileConstant.TAG_CHECKBOXFIELD)
								.append(">");
					}
					if (buf2.toString().trim().length() > 0) {
						buffer.append("<")
								.append(MobileConstant.TAG_RADIOFIELD)
								.append("  ").append(MobileConstant.ATT_NAME)
								.append("='_nextids'>");
						buffer.append(buf2);
						buffer.append("</")
								.append(MobileConstant.TAG_RADIOFIELD)
								.append(">");
					}
					if (userBuf.toString().trim().length() > 0) {
						buffer.append(userBuf);
					}
				}

				// 设置流程抄送
				Collection<BaseUser> circulatorList = null;
				if ((currnode instanceof ManualNode)
						&& ((ManualNode) currnode).isCarbonCopy
						&& ((ManualNode) currnode).isSelectCirculator) {
					circulatorList = StateMachineUtil.getCirculatorList(
							new ParamsTable(), doc, currnode,
							doc.getDomainid(), doc.getApplicationid());
				}
				if (circulatorList != null && circulatorList.size() > 1) {
					buffer.append("<").append(MobileConstant.TAG_USERFIELD);
					buffer.append(" ").append(
							MobileConstant.ATT_ID + "='_circulatorInfo'");
					buffer.append(" ").append(
							MobileConstant.ATT_NAME + "='_circulatorInfo'");
					buffer.append(" ").append(MobileConstant.ATT_LABEL)
							.append("='").append("{*[copy.for]*}").append("'>");
					Iterator<BaseUser> its = circulatorList.iterator();
					while (its.hasNext()) {
						BaseUser user = its.next();
						buffer.append("<").append(MobileConstant.TAG_OPTION)
								.append("");
						buffer.append(" ").append(MobileConstant.ATT_VALUE)
								.append("='");
						buffer.append(HtmlEncoder.encode(user.getId())).append(
								"'>");
						buffer.append(user.getName());
						buffer.append("</").append(MobileConstant.TAG_OPTION)
								.append(">");
					}
					buffer.append("</").append(
							MobileConstant.TAG_USERFIELD + ">");
				}

				Collection<Node> backNodeList = getBackToNodeList(doc, nodert,
						webUser);
				if (backNodeList != null && backNodeList.size() > 0) {
					// buffer.append("<RETURNTO ")
					// .append(MobileConstant.ATT_LABEL).append(
					// " = '{*[Return To]*}:'>");
					buffer.append("<")
							.append(MobileConstant.TAG_SELECTFIELD)
							.append(" ")
							.append(MobileConstant.ATT_LABEL)
							.append("='{*[cn.myapps.core.workflow.return_to]*}'")
							.append(" ").append(MobileConstant.ATT_NAME)
							.append("='_nextids'>");

					buffer.append("<").append(MobileConstant.TAG_OPTION)
							.append(" ").append(MobileConstant.ATT_VALUE)
							.append("=''>");
					buffer.append("{*[cn.myapps.core.workflow.please_choose]*}");
					buffer.append("</").append(MobileConstant.TAG_OPTION)
							.append(">");
					for (Iterator<Node> iter = backNodeList.iterator(); iter
							.hasNext();) {
						Node backNode = (Node) iter.next();
						buffer.append("<").append(MobileConstant.TAG_OPTION)
								.append(" ").append(MobileConstant.ATT_VALUE)
								.append("='" + backNode.id + "'>");
						buffer.append(StringUtil.dencodeHTML(backNode.name));
						buffer.append("</").append(MobileConstant.TAG_OPTION)
								.append(">");
					}
					buffer.append("</").append(MobileConstant.TAG_SELECTFIELD)
							.append(">");
					// .append("></RETURNTO>");
				}
			} else if (state != null && state.toInt() == FlowState.SUSPEND) {
				Collection<Node> backNodeList = this.getBackToNodeList(doc,
						nodert, webUser, FlowState.SUSPEND);
				backNodeList = StateMachine.removeDuplicateNode(backNodeList);
				isDisplySubmit = true;

				if (backNodeList != null) {
					Iterator<Node> it4 = backNodeList.iterator();
					buffer.append("<")
							.append(MobileConstant.TAG_RADIOFIELD)
							.append(" ")
							.append(MobileConstant.ATT_LABEL)
							.append(" = '{*[cn.myapps.core.workflow.resume_flow]*}'  ")
							.append(MobileConstant.ATT_NAME)
							.append("='_nextids'>");
					while (it4.hasNext()) {
						Node backNode = (Node) it4.next();
						buffer.append("<").append(MobileConstant.TAG_OPTION)
								.append(" ").append(MobileConstant.ATT_VALUE)
								.append("='" + backNode.id + "' ");
						buffer.append(">");
						buffer.append(StringUtil.dencodeHTML(backNode.name));
						buffer.append("</").append(MobileConstant.TAG_OPTION)
								.append(">");
					}
					buffer.append("</").append(MobileConstant.TAG_RADIOFIELD)
							.append(">");
				}
			}
			if (doc.getState() != null) {
				buffer.append("<")
						.append(MobileConstant.TAG_TEXTAREAFIELD)
						.append(" ")
						.append(MobileConstant.ATT_LABEL)
						.append(" = '{*[cn.myapps.core.workflow.approve_remarks]*}' ")
						.append(MobileConstant.ATT_NAME)
						.append("='_attitude'></")
						.append(MobileConstant.TAG_TEXTAREAFIELD).append(">");
			}
			buffer.append("</").append(MobileConstant.TAG_WORKFLOW).append(">");
			buffer.append("</").append(MobileConstant.TAG_FORM).append(">");
		}

		return buffer.toString();
	} // 流程处理者

	/**
	 * 返回的字符串为重定义后的XML，表达显示当前用户运行时节点
	 * 
	 * @param flowid
	 *            flow id
	 * @param docid
	 *            Document id
	 * @param webUser
	 *            webuser
	 * @return 字符串为显示当前用户运行时节点
	 * @throws Exception
	 */
	public String toFlowXMLText2(Document doc, WebUser webUser,
			String flowShowType, HttpServletRequest request) throws Exception {
		StringBuffer buffer = new StringBuffer();
		BillDefiVO flowVO = doc.getState().getFlowVO();
		NodeRT nodert = null;
		FlowDiagram fd = null;
		if (flowVO != null) {
			fd = flowVO.toFlowDiagram();
			nodert = StateMachine.getCurrUserNodeRT(doc, webUser, null);
		}
		// 获取当前结点
		Node currnode = null;
		State state = null; // 当前节点状态
		if (nodert != null) {
			String currnodeid = nodert.getNodeid();
			if (currnodeid != null) {
				currnode = (Node) fd.getElementByID(currnodeid);
				state = StateCreator.getNodeState(currnode);
			}
			
			//下一步节点的默认选中状态
			int nextNodeCheckedStatus = ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED;
			nextNodeCheckedStatus = ((ManualNode) currnode).nextNodeCheckedStatus;
			
			buffer.append("<").append(MobileConstant2.TAG_LABEL);
			buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='")
					.append("{*[Workflow_Info]*}").append("'/>");
			buffer.append("<").append(MobileConstant2.TAG_PARAMS);
			buffer.append(" ").append(MobileConstant2.ATT_KEY).append("='")
					.append("_currid").append("'");
			buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='")
					.append(currnodeid).append("'/>");

			if (state != null && state.toInt() == FlowState.RUNNING) {// 送下一步
				isDisplySubmit = true;

				Collection<Node> nextNodeList = fd.getNextNodeList(currnodeid,doc,ParamsTable.convertHTTP(request),webUser);
				boolean first = true;
				boolean isToPerson = false;
				if (nextNodeList != null && nextNodeList.size() > 0) {
					Iterator<Node> it3 = nextNodeList.iterator();
					StringBuffer buf1 = new StringBuffer();
					StringBuffer buf2 = new StringBuffer();
					StringBuffer userBuf = new StringBuffer(); // 审批用户列表
					while (it3.hasNext()) {
						Node nextNode = (Node) it3.next();
						boolean needToPersion = false; // 是否显示指定审批人
						Collection<BaseUser> userList = null;
						if ((nextNode instanceof ManualNode)
								&& ((ManualNode) currnode).isToPerson) {// 手动节点指定审批人
							userList = StateMachineUtil.getPrincipalList(doc,
									new ParamsTable(), nextNode,
									doc.getDomainid(), doc.getApplicationid(),
									webUser);

							// 会签模式时,最后一个审批者才可以去指定审批人
							boolean isAllowToPerson = this.isAllowToPerson(
									(ManualNode) currnode, nodert);

							if (userList != null && userList.size() > 1
									&& isAllowToPerson) {// 当下一步审批人超过1人时才出现指定审批人操作
								needToPersion = true;
							}
						}
						Node node = getCurrNode(flowVO, nodert.getNodeid());
						boolean issplit = false;
						boolean isgather = false;
						
						if (node != null && node instanceof ManualNode) {
							issplit = ((ManualNode) node).issplit;
							isgather = ((ManualNode) node).isgather;
							
						}

						boolean flag = false;
						if (isgather) {// 如果为聚合
							Collection<NodeRT> nodertList = getAllNodeRT(doc);
							if (nodertList != null) {
								for (Iterator<NodeRT> iter = nodertList
										.iterator(); iter.hasNext();) {
									NodeRT nrt = (NodeRT) iter.next();

									// 判断所有运行时节点是否为当前节点
									if (!nrt.getNodeid().equals(
											nodert.getNodeid())) {
										Node nd = (Node) fd.getElementByID(nrt
												.getNodeid());

										Node currNode = (Node) fd
												.getElementByID(nodert
														.getNodeid());

										Collection<Node> followTo = fd
												.getAllFollowNodeOnPath(nd.id,doc,ParamsTable.convertHTTP(request),webUser);
										if (followTo != null
												&& followTo.contains(currNode)) {
											flag = true;
											break;
										}
									}
								}
							}
						}

						if (!flag) {
							@SuppressWarnings("unused")
							boolean isOthers = false;
							@SuppressWarnings("unused")
							String id = "next";
							@SuppressWarnings("unused")
							String flowOperation = FlowType.RUNNING2RUNNING_NEXT;
							if (!(nextNode instanceof ManualNode)) {// 下一个节点中是否存在suspend
								isOthers = true;
								id = "other";
								if (nextNode instanceof SuspendNode) {
									flowOperation = FlowType.RUNNING2SUSPEND;
								} else if (nextNode instanceof CompleteNode) {
									flowOperation = FlowType.RUNNING2COMPLETE;
								}
							}
							buffer.append("<").append(
									MobileConstant2.TAG_PARAMS);
							buffer.append(" ").append(MobileConstant2.ATT_KEY)
									.append("='").append("_flowType")
									.append("'");
							buffer.append(" ")
									.append(MobileConstant2.ATT_VALUE)
									.append("='").append(flowOperation)
									.append("'").append(">");
							buffer.append("</")
									.append(MobileConstant2.TAG_PARAMS)
									.append(">");
							if (issplit) {
								buf1.append("<").append(
										MobileConstant2.TAG_OPTION);
								
								if (nextNodeCheckedStatus == ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED || nextNodeCheckedStatus==ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED_AND_LOCKED){
									buf1.append(" ")
									.append(MobileConstant2.ATT_SELECTED)
									.append("='").append("true")
									.append("'");
								}
								
								buf1.append(" ")
										.append(MobileConstant2.ATT_VALUE)
										.append("='").append(nextNode.id)
										.append("'>");
								buf1.append(StringUtil
										.dencodeHTML(nextNode.name));
								buf1.append("</")
										.append(MobileConstant2.TAG_OPTION)
										.append(">");
							} else {
								buf2.append("<").append(
										MobileConstant2.TAG_OPTION);
								if (first) {
									first = false;
									buf2.append(" ")
											.append(MobileConstant2.ATT_SELECTED)
											.append("='").append("true")
											.append("'");
								}
								buf2.append(" ")
										.append(MobileConstant2.ATT_VALUE)
										.append("='").append(nextNode.id)
										.append("'>");
								buf2.append(StringUtil
										.dencodeHTML(nextNode.name));
								buf2.append("</")
										.append(MobileConstant2.TAG_OPTION)
										.append(">");
							}
							if (needToPersion) {
								userBuf.append("<").append(
										MobileConstant2.TAG_USERFIELD);
								userBuf.append(" ")
										.append(MobileConstant2.ATT_ID)
										.append("='").append(nextNode.id)
										.append("'");
								userBuf.append(" ")
										.append(MobileConstant2.ATT_LABEL)
										.append("='")
										.append(StringUtil
												.dencodeHTML(nextNode.name))
										.append("{*[cn.myapps.core.workflow.specify_auditor]*}")
										.append("'");
								userBuf.append(" ")
										.append(MobileConstant2.ATT_MODE)
										.append("='S'");
								userBuf.append(" ")
										.append(MobileConstant2.ATT_NAME)
										.append("='").append(nextNode.id)
										.append("'>");
								Iterator<BaseUser> ituser = userList.iterator();
								while (ituser.hasNext()) {
									BaseUser user = ituser.next();
									userBuf.append("<").append(
											MobileConstant2.TAG_OPTION);
									userBuf.append(" ")
											.append(MobileConstant2.ATT_VALUE)
											.append("='")
											.append(HtmlEncoder.encode(user
													.getId())).append("'>");
									userBuf.append(user.getName());
									userBuf.append("</")
											.append(MobileConstant2.TAG_OPTION)
											.append(">");
								}
								userBuf.append("</")
										.append(MobileConstant2.TAG_USERFIELD)
										.append(">");
							}
						}
					}
					if (buf1.toString().trim().length() > 0) {
						buffer.append("<")
								.append(MobileConstant2.TAG_CHECKBOXFIELD)
								.append(" ");
						buffer.append(" ")
								.append(MobileConstant2.ATT_LABEL)
								.append("='")
								.append("{*[cn.myapps.core.workflow.commit_to]*}")
								.append("'");
						if(nextNodeCheckedStatus == ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED_AND_LOCKED){
							buffer.append(" ")
							.append(MobileConstant2.ATT_READONLY)
							.append("='").append("true")
							.append("'");
							
						}
						
						
						buffer.append(" ").append(MobileConstant2.ATT_NAME)
								.append("='").append("_nextids").append("'>");
						buffer.append(buf1.toString());
						buffer.append("</")
								.append(MobileConstant2.TAG_CHECKBOXFIELD)
								.append(">");
					}
					if (buf2.toString().trim().length() > 0) {
						buffer.append("<").append(
								MobileConstant2.TAG_RADIOFIELD);
						buffer.append(" ")
								.append(MobileConstant2.ATT_LABEL)
								.append("='")
								.append("{*[cn.myapps.core.workflow.commit_to]*}")
								.append("'");
						buffer.append(" ").append(MobileConstant2.ATT_NAME)
								.append("='").append("_nextids").append("'>");
						buffer.append(buf2.toString());
						buffer.append("</")
								.append(MobileConstant2.TAG_RADIOFIELD)
								.append(">");
					}
					if (userBuf.toString().trim().length() > 0) {
						buffer.append(userBuf);
					}
				}

				// 设置流程抄送
				Collection<BaseUser> circulatorList = null;
				if ((currnode instanceof ManualNode)
						&& ((ManualNode) currnode).isCarbonCopy
						&& ((ManualNode) currnode).isSelectCirculator) {
					circulatorList = StateMachineUtil.getCirculatorList(
							new ParamsTable(), doc, currnode,
							doc.getDomainid(), doc.getApplicationid());
				}
				if (circulatorList != null && circulatorList.size() > 1) {
					buffer.append("<").append(MobileConstant2.TAG_USERFIELD);
					buffer.append(" ").append(MobileConstant2.ATT_ID)
							.append("='").append("_circulatorInfo").append("'");
					buffer.append(" ").append(MobileConstant2.ATT_NAME)
							.append("='").append("_circulatorInfo").append("'");
					buffer.append(" ").append(MobileConstant2.ATT_MODE)
							.append("='S'");
					buffer.append(" ").append(MobileConstant2.ATT_LABEL)
							.append("='").append("{*[copy.for]*}").append("'>");
					Iterator<BaseUser> its = circulatorList.iterator();
					while (its.hasNext()) {
						BaseUser user = its.next();
						buffer.append("<").append(MobileConstant2.TAG_OPTION);
						buffer.append(" ").append(MobileConstant2.ATT_VALUE)
								.append("='")
								.append(HtmlEncoder.encode(user.getId()))
								.append("'>");
						buffer.append(user.getName());
						buffer.append("</").append(MobileConstant2.TAG_OPTION)
								.append(">");
					}
					buffer.append("</").append(MobileConstant2.TAG_USERFIELD)
							.append(">");
				}

				Collection<Node> backNodeList = getBackToNodeList(doc, nodert,
						webUser);
				if (backNodeList != null && backNodeList.size() > 0) {
					buffer.append("<").append(MobileConstant2.TAG_SELECTFIELD);
					buffer.append(" ").append(MobileConstant2.ATT_LABEL)
							.append("='")
							.append("{*[cn.myapps.core.workflow.reject]*}")
							.append("'");
					buffer.append(" ").append(MobileConstant2.ATT_NAME)
							.append("='").append("_nextids").append("'>");

					buffer.append("<").append(MobileConstant2.TAG_OPTION);
					buffer.append(" ").append(MobileConstant2.ATT_VALUE)
							.append("=''").append(">");
					buffer.append("{*[please.select]*}");
					buffer.append("</").append(MobileConstant2.TAG_OPTION)
							.append(">");
					for (Iterator<Node> iter = backNodeList.iterator(); iter
							.hasNext();) {
						Node backNode = (Node) iter.next();
						buffer.append("<").append(MobileConstant2.TAG_OPTION);
						buffer.append(" ").append(MobileConstant2.ATT_VALUE)
								.append("='").append(backNode.id).append("'>");
						buffer.append(StringUtil.dencodeHTML(backNode.name));
						buffer.append("</").append(MobileConstant2.TAG_OPTION)
								.append(">");
					}
					buffer.append("</").append(MobileConstant2.TAG_SELECTFIELD)
							.append(">");
				}
			} else if (state != null && state.toInt() == FlowState.SUSPEND) {
				Collection<Node> backNodeList = this.getBackToNodeList(doc,
						nodert, webUser, FlowState.SUSPEND);
				backNodeList = StateMachine.removeDuplicateNode(backNodeList);
				isDisplySubmit = true;

				if (backNodeList != null) {
					Iterator<Node> it4 = backNodeList.iterator();
					buffer.append("<").append(MobileConstant2.TAG_RADIOFIELD);
					buffer.append(" ")
							.append(MobileConstant2.ATT_LABEL)
							.append("='")
							.append("{*[cn.myapps.core.workflow.resume_flow]*}")
							.append("'");
					buffer.append(" ").append(MobileConstant2.ATT_NAME)
							.append("='").append("_nextids").append("'>");
					while (it4.hasNext()) {
						Node backNode = (Node) it4.next();
						buffer.append("<").append(MobileConstant2.TAG_OPTION);
						buffer.append(" ").append(MobileConstant2.ATT_VALUE)
								.append("='").append(backNode.id).append("'")
								.append(">");
						buffer.append(StringUtil.dencodeHTML(backNode.name));
						buffer.append("</").append(MobileConstant2.TAG_OPTION)
								.append(">");
					}
					buffer.append("</").append(MobileConstant2.TAG_RADIOFIELD)
							.append(">");
				}
			}
			if (doc.getState() != null) {
				buffer.append("<").append(MobileConstant2.TAG_TEXTAREAFIELD);
				buffer.append(" ").append(MobileConstant2.ATT_LABEL)
						.append("='").append("{*[Remark]*}").append("'");
				buffer.append(" ").append(MobileConstant2.ATT_NAME)
						.append("='").append("_attitude").append("'>");
				buffer.append("</").append(MobileConstant2.TAG_TEXTAREAFIELD)
						.append(">");
			}
		}

		return buffer.toString();
	} // 流程处理者
	
	public String toFlowXMLTextForInit(Document doc, WebUser webUser, HttpServletRequest request, String actid)
			throws Exception {
		StringBuffer buffer = new StringBuffer();
		if (doc.getState() != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			NodeRT nodert = null;
			String currNodeId = null;
			FlowDiagram fd = null;
			if (flowVO != null) {
				fd = flowVO.toFlowDiagram();
				currNodeId = (String) request.getAttribute("_targetNode");
				// 从上下文获取当前处理节点对象
				nodert = (NodeRT) request.getAttribute("_targetNodeRT");
				if (currNodeId == null && nodert == null) {
					nodert = StateMachine.getCurrUserNodeRT(doc, webUser, currNodeId);
					if (nodert != null)
						currNodeId = nodert.getNodeid();
				}
				
				buffer.append("<").append(MobileConstant2.TAG_PARAMS);
				buffer.append(" ").append("KEY").append("='").append("_currid").append("'");
				buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(currNodeId).append("'")
						.append(">");
				buffer.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
			}

			if (nodert == null) {
				// 流程回撤
				isDisplyFlow = false;
				if (allowRetracement(doc, webUser, request)) {
					buffer.append("<ACTION TYPE='retracement' ID='btn_retracement' NAME='{*[Retracement]*}'/>");
					isOnlyShowRetracementButton = true;
					isDisplyFlow = true;
				}
				return buffer.toString();
			}

			// 获取当前结点
			Node currnode = null;
			if (currNodeId != null) {
				currnode = (Node) fd.getElementByID(currNodeId);
			}

			// 流程提交操作按钮
			if (fd.getNextNodeList(currnode.id,doc,ParamsTable.convertHTTP(request),webUser).size() > 0) {
				buffer.append("<ACTION TYPE='commit' ACTIONID='" + actid + "' NAME='{*[Commit]*}'/>");
			}

			// 流程回退操作按钮
			if (currnode instanceof ManualNode && ((ManualNode) currnode).cBack) {
				Collection<Node> backNodeList = getBackToNodeList(doc, nodert, webUser);
				if (backNodeList != null && !backNodeList.isEmpty()) {
					buffer.append("<ACTION TYPE='back' ACTIONID='" + actid
							+ "' NAME='{*[cn.myapps.core.workflow.reject]*}'/>");
				}
			}

			// 流程终止操作按钮
			if (currnode instanceof ManualNode && ((ManualNode) currnode).isAllowTermination) {
				buffer.append("<ACTION TYPE='termination' ID='btn_termination' NAME='{*[cn.myapps.core.dynaform.activity.type.flow_terminate]*}'/>");
			}

			// 流程回撤操作按钮
			if (allowRetracement(doc, webUser, request)) {
				buffer.append("<ACTION TYPE='retracement' ID='btn_retracement' NAME='{*[Retracement]*}'/>");
			}

			// 挂起;恢复 操作
			if (currnode != null) {
				if ((currnode instanceof ManualNode) && !doc.getIstmp() // 新建文档，不显示挂起按钮
				) {

					boolean flag = false;
					if (((ManualNode) currnode).handupEditMode == 0) {
						flag = ((ManualNode) currnode).isHandup;
					} else if (((ManualNode) currnode).handupEditMode == 1) {
						ParamsTable params = ParamsTable.convertHTTP(request);
						IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), doc.getApplicationid());
						runner.initBSFManager(doc, params, webUser, new ArrayList<ValidateMessage>());
						StringBuffer label = new StringBuffer();
						label.append("FlowName:" + fd.getName() + " handupScript(").append(fd.getId())
								.append(")." + fd.getName()).append(".handupScript");
						Object result = runner.run(label.toString(), ((ManualNode) currnode).handupScript);
						if (result != null && result instanceof Boolean) {
							flag = ((Boolean) result).booleanValue();
						}
					}
					if (flag) {
						if (nodert.getState() == 0) {
							buffer.append("<ACTION TYPE='handup' NAME='{*[cn.myapps.core.workflow.suspend]*}' ID='_handup'>");
							buffer.append("<PARAMS KEY='nodertId' VALUE='" + nodert.getId() + "'>");
							buffer.append("</PARAMS>");
							buffer.append("</ACTION>");
						} else if (nodert.getState() == 1) {
							buffer.append("<ACTION TYPE='recover' NAME='{*[cn.myapps.core.workflow.recover]*}' ID='_recover'>");
							buffer.append("<PARAMS KEY='nodertId' VALUE='" + nodert.getId() + "'>");
							buffer.append("</PARAMS>");
							buffer.append("</ACTION>");
						}
					}
				}
			}

			// 流程加签操作按钮
			if (currnode instanceof ManualNode
					&& Integer.parseInt(((ManualNode) currnode).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND
					&& ((ManualNode) currnode).isApproverEdit) {
				buffer.append("<ACTION TYPE='addAuditor' ID='btn_addAuditor' NAME='{*[cn.myapps.core.workflow.add_auditor]*}'/>");
			}
			// 编辑流程审批人操作按钮
			if (currnode instanceof ManualNode && ((ManualNode) currnode).isAllowEditAuditor) {
				buffer.append("<ACTION TYPE='editAuditor' ID='btn_editAuditor' NAME='{*[Edit_Auditor]*}'/>");
			}

			// 返回节点PermissionList
			// String fieldpermlist = ((ManualNode) currnode).fieldpermlist;
			// if (currnode instanceof ManualNode
			// && fieldpermlist != null && !fieldpermlist.isEmpty()) {
			// fieldpermlist = StringUtil.dencodeHTML(fieldpermlist);
			// buffer.append("<textarea id='fieldpermlist' name='fieldpermlist' style='display:none;'>")
			// .append(fieldpermlist)
			// .append("</textarea>");
			// }

		}

		return buffer.toString();

	}

	/**
	 * 返回的字符串为重定义后的XML，表达显示当前用户运行时节点
	 * 
	 * @param flowid
	 *            flow id
	 * @param docid
	 *            Document id
	 * @param webUser
	 *            webuser
	 * @return 字符串为显示当前用户运行时节点
	 * @throws Exception
	 */
	public String toFlowXMLTextForCommitTo(Document doc, WebUser webUser, String flowShowType,
			HttpServletRequest request) throws Exception {
		StringBuffer buffer = new StringBuffer();
		Environment evn = Environment.getInstance();
		FlowStateRT instance = doc.getState();
		if (instance != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			NodeRT nodert = null;
			String currNodeId = null;
			FlowDiagram fd = null;
			boolean isToPerson = false;
			if (flowVO != null) {
				fd = flowVO.toFlowDiagram();
				currNodeId = (String) request.getAttribute("_targetNode");
				// 从上下文获取当前处理节点对象
				nodert = (NodeRT) request.getAttribute("_targetNodeRT");

				if (instance.isTemp()) {
					nodert = ((List<NodeRT>) instance.getNoderts()).get(0);
					currNodeId = nodert.getNodeid();
				}

				if (currNodeId == null && nodert == null) {
					nodert = StateMachine.getCurrUserNodeRT(doc, webUser, currNodeId);
					if (nodert != null)
						currNodeId = nodert.getNodeid();
				}
			}
			// 获取当前结点
			Node currnode = null;
			State state = null;
			if (currNodeId != null) {
				currnode = (Node) fd.getElementByID(currNodeId);
				state = StateCreator.getNodeState(currnode);
				if (currnode instanceof ManualNode) {
					if (((ManualNode) currnode).isFrontEdit) {
						this.isFrontEdit = true;
					}
				}
				buffer.append("<").append(MobileConstant2.TAG_PARAMS);
				buffer.append(" ").append("KEY").append("='").append("_currentNodeId").append("'");
				buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(currNodeId)
						.append("'").append(">");
				buffer.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
				
				buffer.append("<").append(MobileConstant2.TAG_PARAMS);
				buffer.append(" ").append(MobileConstant2.ATT_KEY).append("='").append("_flowType").append("'");
				buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(FlowType.RUNNING2RUNNING_NEXT)
						.append("'").append(">");
				buffer.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
			}

			if (state != null && state.toInt() == FlowState.RUNNING) {// 送下一步
				isDisplySubmit = true;
				Collection<Node> nextNodeList = fd.getNextNodeList(currnode.id,doc,ParamsTable.convertHTTP(request),webUser);

				// 判断是否能通过
				boolean issplit = false;
				boolean needToPersion = false;// 是否需要选择审批人操作
				boolean isAppentCirculator = false;
				// 下一步节点的默认选中状态
				int nextNodeCheckedStatus = ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED;

				// checkedState判断在html拼接的时候是否需要为checkbox 和 radio 添加"默认选中"标识
				boolean checkedState = true;
				if (currnode != null) {
					if (currnode instanceof ManualNode) {
						issplit = ((ManualNode) currnode).issplit;
						nextNodeCheckedStatus = ((ManualNode) currnode).nextNodeCheckedStatus;
					} else if (currnode instanceof AutoNode) {
						issplit = ((AutoNode) currnode).issplit;
					}
				}

				// 分散的情况下，为每个分支节点增加令牌
				if (issplit) {
					buffer.append("<").append(MobileConstant2.TAG_PARAMS);
					buffer.append(" ").append("KEY").append("='").append("splitToken").append("'");
					buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(currNodeId)
							.append("'").append(">");
					buffer.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
				}

				if (nextNodeList != null && nextNodeList.size() > 0) {
					Iterator<Node> it3 = nextNodeList.iterator();
					
					int imgid = 0;
					StringBuffer buf1 = new StringBuffer();
					
					buffer.append("<");
					buffer.append(issplit?MobileConstant2.TAG_CHECKBOXFIELD:MobileConstant2.TAG_RADIOFIELD);
					buffer.append(" ").append(MobileConstant2.ATT_LABEL).append("='")
							.append("{*[cn.myapps.core.workflow.commit_to]*}").append("'");
					
					if (issplit && nextNodeCheckedStatus == ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED_AND_LOCKED) {
						buffer.append(" ").append(MobileConstant2.ATT_READONLY).append("='").append("true").append("'");

					}
					
					buffer.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("_nextids");
					buffer.append("'>");
					
					while (it3.hasNext()) {
						Node nextNode = (Node) it3.next();

						boolean isOthers = false;
						needToPersion = false;
						String id = "next";
						String flowOperation = FlowType.RUNNING2RUNNING_NEXT;
						if (!(nextNode instanceof ManualNode)) {
							isOthers = true;
							id = "other";
							if (nextNode instanceof SuspendNode) {
								flowOperation = FlowType.RUNNING2SUSPEND;
							} else if (nextNode instanceof CompleteNode) {
								flowOperation = FlowType.RUNNING2COMPLETE;
							} else if (nextNode instanceof AutoNode) {
								flowOperation = FlowType.RUNNING2AUTO;
							}
						}
						// TD1
						
						buffer.append("<").append(MobileConstant2.TAG_OPTION);
						if(!issplit && checkedState){
							buffer.append(" ").append(MobileConstant2.ATT_SELECTED).append("='").append(true).append("'");
							checkedState = false;
						}
						buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(nextNode.id).append("'>");
						buffer.append(StringUtil.dencodeHTML(nextNode.name));
						buffer.append("</").append(MobileConstant2.TAG_OPTION).append(">");
						if (issplit) {// 并行
							switch (nextNodeCheckedStatus) {
							case ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED:// 默认选中
								buffer.append(" ").append(MobileConstant2.ATT_SELECTED).append("='").append(true).append("'");
								break;
							case ManualNode.NEXT_NODE_CHECKED_STATUS_UNCHECKED:// 默认不选中
								buffer.append(" ").append(MobileConstant2.ATT_SELECTED).append("='").append(true).append("'");
								break;
							case ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED_AND_LOCKED:// 默认选中且锁定
								buffer.append(" ").append(MobileConstant2.ATT_SELECTED).append("='").append(true).append("'");
								break;
							default:
								break;
							}
						}

						if ((nextNode instanceof ManualNode) && ((ManualNode) currnode).isToPerson) {// 手动节点指定审批人
							Collection<BaseUser> userList = StateMachineUtil.getPrincipalList(doc, new ParamsTable(),
									nextNode, doc.getDomainid(), doc.getApplicationid(), webUser);

							// 会签模式时,最后一个审批者才可以去指定审批人
							boolean isAllowToPerson = this.isAllowToPerson((ManualNode) currnode, nodert);

							if (userList != null && userList.size() > 1 && isAllowToPerson) {// 当下一步审批人超过1人时才出现指定审批人操作
								needToPersion = true;
							}
						}

						// 循环生成isToPerson，以判断每个节点是否有指定审批人
						if (currnode != null) {
							if ((currnode instanceof ManualNode) && (nextNode instanceof ManualNode)) {// 编辑审批人
								buffer.append("<").append(MobileConstant2.TAG_PARAMS);
								buffer.append(" ").append("KEY").append("='").append("isToPerson_" + nextNode.id).append("'");
								buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(needToPersion)
										.append("'").append(">");
								buffer.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
							}
						}

						if ((nextNode instanceof ManualNode) && ((ManualNode) currnode).isToPerson && needToPersion) {// 手动节点指定审批人
							isToPerson = true;
							buffer.append("<").append(MobileConstant2.TAG_USERFIELD);
							buffer.append(" ").append(MobileConstant2.ATT_ID).append("='").append(nextNode.id)
									.append("'");
							buffer.append(" ").append(MobileConstant2.ATT_LABEL).append("='")
									.append(StringUtil.dencodeHTML(nextNode.name))
									.append("{*[cn.myapps.core.workflow.specify_auditor]*}").append("'");
							buffer.append(" ").append(MobileConstant2.ATT_MODE).append("='S'");
							buffer.append(" ").append(MobileConstant2.ATT_NAME).append("='")
									.append(nextNode.id).append("'>");
							Collection<BaseUser> userList = StateMachineUtil.getPrincipalList(doc, new ParamsTable(), nextNode,
									doc.getDomainid(), doc.getApplicationid(), webUser);
							Iterator<BaseUser> ituser = userList.iterator();
							while (ituser.hasNext()) {
								BaseUser user = ituser.next();
								buffer.append("<").append(MobileConstant2.TAG_OPTION);
								buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='")
										.append(HtmlEncoder.encode(user.getId())).append("'>");
								buffer.append(user.getName());
								buffer.append("</").append(MobileConstant2.TAG_OPTION).append(">");
							}
							buffer.append("</").append(MobileConstant2.TAG_USERFIELD).append(">");
							
						} else if ((nextNode instanceof SubFlow) && ((SubFlow) nextNode).isToPerson) {// 子流程节点指定审批人
							isToPerson = true;
//							buf1.append("<").append(MobileConstant2.TAG_USERFIELD);
//							buf1.append(" ").append(MobileConstant2.ATT_ID).append("='").append(nextNode.id)
//									.append("'");
//							buf1.append(" ").append(MobileConstant2.ATT_LABEL).append("='")
//									.append(StringUtil.dencodeHTML(nextNode.name))
//									.append("{*[cn.myapps.core.workflow.specify_auditor]*}").append("'");
//							buf1.append(" ").append(MobileConstant2.ATT_MODE).append("='S'");
//							buf1.append(" ").append(MobileConstant2.ATT_NAME).append("='")
//									.append(nextNode.id).append("'>");
//							
//							ParamsTable subParams = new ParamsTable();
//							subParams.setParameter("_isGetApprover2SubFlow", "true");
//							subParams.setParameter("_flowId", flowVO.getId());
//							subParams.setParameter("_docId", doc.getId());
//							subParams.setParameter("_nodeId", nextNode.id);
//							subParams.setParameter("_pagelines", String.valueOf(Integer.MAX_VALUE));
//							
//							UserProcess proces  = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
//							DataPackage<UserVO> users = proces.queryForUserDialog(subParams, webUser, ServletActionContext
//									.getRequest(), null);
//							
//							Iterator<UserVO> ituser = users.datas.iterator();
//							while (ituser.hasNext()) {
//								BaseUser user = ituser.next();
//								buf1.append("<").append(MobileConstant2.TAG_OPTION);
//								buf1.append(" ").append(MobileConstant2.ATT_VALUE).append("='")
//										.append(HtmlEncoder.encode(user.getId())).append("'>");
//								buf1.append(user.getName());
//								buf1.append("</").append(MobileConstant2.TAG_OPTION).append(">");
//							}
//							buf1.append("</").append(MobileConstant2.TAG_USERFIELD).append(">");
//							
//							
//							
//							String numberSetingType = ((SubFlow) nextNode).numberSetingType;
//							int instanceTotal = getSubFlowInstanceTotal((SubFlow) nextNode, doc, webUser);
//							buffer.append("&nbsp&nbsp{*[cn.myapps.core.workflow.specify_auditor]*}:&nbsp");
//							buffer.append("<span id='opra_" + imgid + "' style='display:inline;'>");
//							buffer.append("<input id='input_"
//									+ imgid
//									+ "' name='input_"
//									+ imgid
//									+ "' readonly='true' type='text'  size='10'  value='"
//									+ (request.getParameter("input_" + imgid) != null ? request.getParameter("input_"
//											+ imgid) : "") + "'/>");
//							buffer.append("<img id='selectUserImg_"
//									+ imgid
//									+ "' style='cursor:pointer;display:inline;' onclick=\"showUserSelectOnSubFlow('actionName', {nextNodeId:'"
//									+ nextNode.id + "',instanceId:'" + doc.getStateid() + "',numberSetingType:'"
//									+ numberSetingType + "',instanceTotal:'" + instanceTotal + "', docid:'"
//									+ doc.getId() + "',flowid:'" + flowVO.getId() + "', textField:'input_" + imgid
//									+ "',valueField: 'input_" + imgid + "', readonly: false})\" src='"
//									+ evn.getContextPath()
//									+ "/portal/share/component/dialog/images/userselect.gif' /> ");
//							buffer.append("<input type=\"hidden\" id=\"input_hidden_id_"
//									+ imgid
//									+ "\" name=\"input_hidden_id_"
//									+ imgid
//									+ "\" value=\""
//									+ (request.getParameter("input_hidden_id_" + imgid) != null ? request
//											.getParameter("input_hidden_id_" + imgid) : "") + "\"/>");
//							buffer.append("</span>");
						}

						// 指定流程抄送人-------------------------------------------start

						if ((currnode instanceof ManualNode) && ((ManualNode) currnode).isCarbonCopy
								&& ((ManualNode) currnode).isSelectCirculator && !isAppentCirculator) {// 手动节点指定抄送人
							isToPerson = true;
							isAppentCirculator = true;
							
							Collection<BaseUser> circulatorList = StateMachineUtil.getCirculatorList(new ParamsTable(), doc, currnode,
									doc.getDomainid(), doc.getApplicationid());
							
							buffer.append("<").append(MobileConstant2.TAG_USERFIELD);
							buffer.append(" ").append(MobileConstant2.ATT_ID).append("='").append("_circulatorInfo")
									.append("'");
							buffer.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("_circulatorInfo")
									.append("'");
							buffer.append(" ").append(MobileConstant2.ATT_MODE).append("='S'");
							buffer.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append("{*[copy.for]*}")
									.append("'>");
							Iterator<BaseUser> its = circulatorList.iterator();
							while (its.hasNext()) {
								BaseUser user = its.next();
								buffer.append("<").append(MobileConstant2.TAG_OPTION);
								buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='")
										.append(HtmlEncoder.encode(user.getId())).append("'>");
								buffer.append(user.getName());
								buffer.append("</").append(MobileConstant2.TAG_OPTION).append(">");
							}
							buffer.append("</").append(MobileConstant2.TAG_USERFIELD).append(">");
							
						}

						// 指定流程抄送人-------------------------------------------end
						imgid++;
					}
					buffer.append("</");
					buffer.append(issplit?MobileConstant2.TAG_CHECKBOXFIELD:MobileConstant2.TAG_RADIOFIELD);
					buffer.append(">");

				}

			}

			if (nodert != null) {
				if ((currnode instanceof ManualNode)) {// 编辑审批人
					buffer.append("<").append(MobileConstant2.TAG_PARAMS);
					buffer.append(" ").append("KEY").append("='").append("isToPerson").append("'");
					buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(isToPerson)
							.append("'").append(">");
					buffer.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
				}
			}
			
			buffer.append("<").append(MobileConstant2.TAG_TEXTAREAFIELD);
			buffer.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append("{*[Remark]*}")
					.append("'");
			buffer.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("_attitude").append("'>");
			buffer.append("</").append(MobileConstant2.TAG_TEXTAREAFIELD).append(">");
		}

		return buffer.toString();
		
		
		
		
		
	}

	/**
	 * 返回的字符串为重定义后的XML，表达显示当前用户运行时回退节点
	 * 
	 * @param flowid
	 *            flow id
	 * @param docid
	 *            Document id
	 * @param webUser
	 *            webuser
	 * @return 字符串为显示当前用户运行时节点
	 * @throws Exception
	 */
	public String toFlowXMLTextForReturnTo(Document doc, WebUser webUser, String flowShowType,
			HttpServletRequest request) throws Exception {
		
		StringBuffer buffer = new StringBuffer();
		Environment evn = Environment.getInstance();

		if (doc.getState() != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			NodeRT nodert = null;
			String currNodeId = null;
			FlowDiagram fd = null;
			boolean isToPerson = false;
			boolean isAllowBack = false;// 是否允许回退
			if (flowVO != null) {
				fd = flowVO.toFlowDiagram();
				currNodeId = (String) request.getAttribute("_targetNode");
				// 从上下文获取当前处理节点对象
				nodert = (NodeRT) request.getAttribute("_targetNodeRT");
				if (currNodeId == null && nodert == null) {
					nodert = StateMachine.getCurrUserNodeRT(doc, webUser, currNodeId);
					if (nodert != null)
						currNodeId = nodert.getNodeid();
				}
			}
			if (nodert == null) {
				return buffer.toString();
			}
			// 获取当前结点
			Node currnode = null;
			State state = null;
			if (currNodeId != null) {
				currnode = (Node) fd.getElementByID(currNodeId);
				state = StateCreator.getNodeState(currnode);
				if (currnode instanceof ManualNode) {
					if (((ManualNode) currnode).isFrontEdit) {
						this.isFrontEdit = true;
					}
				}
				
				buffer.append("<").append(MobileConstant2.TAG_PARAMS);
				buffer.append(" ").append("KEY").append("='").append("_currentNodeId").append("'");
				buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(currNodeId)
						.append("'").append(">");
				buffer.append("</").append(MobileConstant2.TAG_PARAMS).append(">");

				buffer.append("<").append(MobileConstant2.TAG_PARAMS);
				buffer.append(" ").append(MobileConstant2.ATT_KEY).append("='").append("_flowType").append("'");
				buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(FlowType.RUNNING2RUNNING_BACK)
						.append("'").append(">");
				buffer.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
				
			}

			if (state != null && state.toInt() == FlowState.RUNNING) {// 送下一步
				isDisplySubmit = true;

				// 判断是否能通过
				boolean issplit = false;
				boolean needToPersion = false;// 是否需要选择审批人操作
				boolean isAppentCirculator = false;

				// checkedState判断在html拼接的时候是否需要为checkbox 和 radio 添加"默认选中"标识
				boolean checkedState = true;
				if (currnode != null) {
					if (currnode instanceof ManualNode) {
						issplit = ((ManualNode) currnode).issplit;
					} else if (currnode instanceof AutoNode) {
						issplit = ((AutoNode) currnode).issplit;
					}
				}

				// 分散的情况下，为每个分支节点增加令牌
				if (issplit) {
					buffer.append("<").append(MobileConstant2.TAG_PARAMS);
					buffer.append(" ").append("KEY").append("='").append("splitToken").append("'");
					buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(currNodeId)
							.append("'").append(">");
					buffer.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
				}

				Collection<Node> backNodeList = getBackToNodeList(doc, nodert, webUser);
				if (backNodeList != null && backNodeList.size() > 0) {
					isAllowBack = true;
					
					buffer.append("<").append(MobileConstant2.TAG_SELECTFIELD);
					buffer.append(" ").append(MobileConstant2.ATT_LABEL).append("='")
							.append("{*[cn.myapps.core.workflow.return_to]*}").append("'");
					buffer.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("_nextids").append("'>");

					buffer.append("<").append(MobileConstant2.TAG_OPTION);
					buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("=''").append(">");
					buffer.append("{*[please.select]*}");
					buffer.append("</").append(MobileConstant2.TAG_OPTION).append(">");
					for (Iterator<Node> iter = backNodeList.iterator(); iter.hasNext();) {
						Node backNode = (Node) iter.next();
						buffer.append("<").append(MobileConstant2.TAG_OPTION);
						buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(backNode.id)
								.append("'>");
						buffer.append(StringUtil.dencodeHTML(backNode.name));
						buffer.append("(").append(StringUtil.dencodeHTML(backNode.statelabel)).append(")");
						buffer.append("</").append(MobileConstant2.TAG_OPTION).append(">");
					}
					buffer.append("</").append(MobileConstant2.TAG_SELECTFIELD).append(">");
				}
			}

			if (nodert != null) {
				if ((currnode instanceof ManualNode)) {// 编辑审批人
					buffer.append("<").append(MobileConstant2.TAG_PARAMS);
					buffer.append(" ").append("KEY").append("='").append("isToPerson").append("'");
					buffer.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(isToPerson)
							.append("'").append(">");
					buffer.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
				}
			}
		}
		
		if (doc.getState() != null) {
			buffer.append("<").append(MobileConstant2.TAG_TEXTAREAFIELD);
			buffer.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append("{*[Remark]*}").append("'");
			buffer.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("_attitude").append("'>");
			buffer.append("</").append(MobileConstant2.TAG_TEXTAREAFIELD).append(">");
		}
		
		return buffer.toString();
	}// 流程处理者
	

	public String biuldUserIdStr(WebUser webUser, ParamsTable params,
			boolean isToperson) throws Exception {
		StringBuffer w = new StringBuffer();
		// String getApprover2SubFlow =
		// params.getParameterAsString("_isGetApprover2SubFlow");//子流程节点选择审批人
		// String getCirculator =
		// params.getParameterAsString("_isGetCirculator");//抄送人
		if (isToperson) {
			// ||(!StringUtil.isBlank(getApprover2SubFlow) &&
			// "true".equals(getApprover2SubFlow))) {
			String _flowId = params.getParameterAsString("_flowId");
			String _docId = params.getParameterAsString("_docid");
			String _nodeId = params.getParameterAsString("_nodeId");
			Collection<BaseUser> users = StateMachineHelper.getPrincipalList(
					_docId, webUser, _nodeId,
					ServletActionContext.getRequest(), _flowId);
			if (users != null && !users.isEmpty()) {
				w.append(" AND id in(");
				for (Iterator<BaseUser> iterator = users.iterator(); iterator
						.hasNext();) {
					BaseUser user = (BaseUser) iterator.next();
					w.append("'").append(user.getId()).append("',");
				}
				w.setLength(w.length() - 1);
				w.append(")");
			}
		}
		// else if(!StringUtil.isBlank(getCirculator)
		// && "true".equals(getCirculator)){
		// String _flowId = params.getParameterAsString("_flowId");
		// String _docId = params.getParameterAsString("_docId");
		// String _nodeId = params.getParameterAsString("_nodeId");
		// Collection<BaseUser> users = StateMachineHelper.getCirculatorList(
		// _docId, webUser, _nodeId, ServletActionContext
		// .getRequest(), _flowId);
		// if (users != null && !users.isEmpty()) {
		// w.append(" AND id in(");
		// for (Iterator<BaseUser> iterator = users.iterator(); iterator
		// .hasNext();) {
		// BaseUser user = (BaseUser) iterator.next();
		// w.append("'").append(user.getId()).append("',");
		// }
		// w.setLength(w.length() - 1);
		// w.append(")");
		// }
		// }
		return w.toString();
	}

	public String toFlowDialogHtmlText(Document doc,ParamsTable params, WebUser webUser,
			String flowShowType) throws Exception {
		StringBuffer buffer = new StringBuffer();
		Environment evn = Environment.getInstance();
		FlowStateRTProcess stateProcess = new FlowStateRTProcessBean(
				doc.getApplicationid());
		FlowStateRT instance = doc.getState();
		BillDefiVO flowVO = instance.getFlowVO();
		NodeRT nodert = null;
		FlowDiagram fd = null;
		boolean isToPerson = false;
		boolean needToPersion = false;
		boolean isAppentCirculator = false;
		if (flowVO != null) {
			fd = flowVO.toFlowDiagram();
			// String defaultNodeId =
			// (String)request.getAttribute("_targetNode");
			nodert = StateMachine.getCurrUserNodeRT(doc, webUser, null);
		}
		// 获取当前结点
		Node currnode = null;
		State state = null;
		if (nodert != null) {
			String currnodeid = nodert.getNodeid();
			if (currnodeid != null) {
				currnode = (Node) fd.getElementByID(currnodeid);
				state = StateCreator.getNodeState(currnode);
				if (currnode instanceof ManualNode) {
					if (((ManualNode) currnode).isFrontEdit) {
						this.isFrontEdit = true;
					}
				}
			}

			buffer.append("<input type='hidden' name='_currid' value='"
					+ currnodeid + "'>");
		}

		buffer.append("");

		if (state != null && state.toInt() == FlowState.RUNNING) {// 送下一步
			isDisplySubmit = true;
			Collection<Node> nextNodeList = fd.getNextNodeList(currnode.id,doc,params,webUser);

			// 判断是否能通过
			Node node = (Node) fd.getElementByID(nodert.getNodeid());
			boolean issplit = false;

			// checkedState判断在html拼接的时候是否需要为checkbox 和 radio 添加"默认选中"标识
			boolean checkedState = true;

			if (node != null && node instanceof ManualNode) {
				issplit = ((ManualNode) node).issplit;
			}

			if (nextNodeList != null && nextNodeList.size() > 0) {
				Iterator<Node> it3 = nextNodeList.iterator();
				int imgid = 0;
				while (it3.hasNext()) {
					buffer.append("<tr>");
					buffer.append("<td class='flow-next' style='width:20%;white-space:nowrap;word-break:keep-all'>{*[cn.myapps.core.workflow.commit_to]*}:</td>");
					Node nextNode = (Node) it3.next();

					boolean isOthers = false;
					needToPersion = false;
					String id = "next";
					String flowOperation = FlowType.RUNNING2RUNNING_NEXT;
					if (!(nextNode instanceof ManualNode)) {// 下一个节点中是否存在suspend
						isOthers = true;
						id = "other";
						if (nextNode instanceof SuspendNode) {
							flowOperation = FlowType.RUNNING2SUSPEND;
						} else if (nextNode instanceof CompleteNode) {
							flowOperation = FlowType.RUNNING2COMPLETE;
						} else if (nextNode instanceof AutoNode) {
							flowOperation = FlowType.RUNNING2AUTO;
						}
					}
					// TD1
					buffer.append("<td>");
					buffer.append("<input id='" + id + "' type='");
					buffer.append(issplit ? "checkbox' checked" : "radio'");
					// 如果是单选按钮
					if (!issplit && checkedState) {
						buffer.append(" checked");
						checkedState = false;
					}
					buffer.append(" name='_nextids' value='" + nextNode.id);
					buffer.append("' onclick='ev_setFlowType(" + isOthers);
					buffer.append(", this, " + flowOperation + "," + imgid
							+ ")' />" + StringUtil.dencodeHTML(nextNode.name));

					if ((nextNode instanceof ManualNode)
							&& ((ManualNode) currnode).isToPerson) {// 手动节点指定审批人
						Collection<BaseUser> userList = StateMachineUtil
								.getPrincipalList(doc, new ParamsTable(),
										nextNode, doc.getDomainid(),
										doc.getApplicationid(), webUser);

						// 会签模式时,最后一个审批者才可以去指定审批人
						boolean isAllowToPerson = this.isAllowToPerson(
								(ManualNode) currnode, nodert);

						if (userList != null && userList.size() > 1
								&& isAllowToPerson) {// 当下一步审批人超过1人时才出现指定审批人操作
							needToPersion = true;
						}
					}
					if ((nextNode instanceof ManualNode)
							&& ((ManualNode) currnode).isToPerson
							&& needToPersion) {// 编辑审批人
						isToPerson = true;
						buffer.append("&nbsp");
						buffer.append("{*[cn.myapps.core.workflow.specify_auditor]*}:&nbsp");
						buffer.append("<span id='opra_" + imgid
								+ "' style='display:inline;'>");
						buffer.append("<input id='input_" + imgid
								+ "' name='input_" + imgid
								+ "' readonly='true' type='text' />");

						buffer.append("<img id='selectUserImg_"
								+ imgid
								+ "' style='cursor:pointer;display:inline;' onclick=\"showUserSelect('actionName', {nextNodeId:'"
								+ nextNode.id
								+ "', docid:'"
								+ doc.getId()
								+ "',flowid:'"
								+ flowVO.getId()
								+ "', textField:'input_"
								+ imgid
								+ "',valueField: 'input_"
								+ imgid
								+ "',hiddenIds:'input_hidden_id_"
								+ imgid
								+ "', readonly: false})\" src='"
								+ evn.getContextPath()
								+ "/portal/share/component/dialog/images/userselect.gif' /> ");
						buffer.append("<input type=\"hidden\" id=\"input_hidden_id_"
								+ imgid + "\"/>");
						buffer.append("</span>");
					} else if ((nextNode instanceof SubFlow)
							&& ((SubFlow) nextNode).isToPerson) {// 子流程节点指定审批人
						isToPerson = true;
						String numberSetingType = ((SubFlow) nextNode).numberSetingType;
						int instanceTotal = getSubFlowInstanceTotal(
								(SubFlow) nextNode, doc, webUser);
						buffer.append("&nbsp");
						buffer.append("{*[cn.myapps.core.workflow.specify_auditor]*}:&nbsp");
						buffer.append("<span id='opra_" + imgid
								+ "' style='display:inline;'>");
						buffer.append("<input id='input_" + imgid
								+ "' name='input_" + imgid
								+ "' readonly='true' type='text' />");

						buffer.append("<img id='selectUserImg_"
								+ imgid
								+ "' style='cursor:pointer;display:inline;' onclick=\"showUserSelectOnSubFlow('actionName', {nextNodeId:'"
								+ nextNode.id
								+ "',instanceId:'"
								+ doc.getStateid()
								+ "',numberSetingType:'"
								+ numberSetingType
								+ "',instanceTotal:'"
								+ instanceTotal
								+ "', docid:'"
								+ doc.getId()
								+ "',flowid:'"
								+ flowVO.getId()
								+ "', textField:'input_"
								+ imgid
								+ "',valueField: 'input_"
								+ imgid
								+ "', readonly: false})\" src='"
								+ evn.getContextPath()
								+ "/portal/share/component/dialog/images/userselect.gif' /> ");
						buffer.append("</span>");
					}
					buffer.append("</td>");

					// 指定流程抄送人-------------------------------------------start

					buffer.append("<td >");
					if ((currnode instanceof ManualNode)
							&& ((ManualNode) currnode).isCarbonCopy
							&& ((ManualNode) currnode).isSelectCirculator
							&& !isAppentCirculator) {// 手动节点指定抄送人
						isToPerson = true;
						isAppentCirculator = true;
						buffer.append("{*[cn.myapps.core.workflow.label.copy_for]*}:&nbsp");
						buffer.append("<span id='opra_" + imgid
								+ "' style='display:inline;'>");
						buffer.append("<input id='_circulator"
								+ "' name='_circulator"
								+ "' readonly='true'  type='text'  size='10' />");

						buffer.append("<img id='selectUserImg_"
								+ imgid
								+ "' style='cursor:pointer;display:inline;' onclick=\"selectCirculator('actionName', {nextNodeId:'"
								+ currnode.id
								+ "', docid:'"
								+ doc.getId()
								+ "',flowid:'"
								+ flowVO.getId()
								+ "', textField:'_circulator"
								+ "',valueField: '_circulator"
								+ "', readonly: false})\" src='"
								+ evn.getContextPath()
								+ "/portal/share/component/dialog/images/userselect.gif' /> ");
						buffer.append("</span>");
					}
					buffer.append("</td>");

					// 指定流程抄送人-------------------------------------------end

					buffer.append("</tr>");

					imgid++;
				}
			}

			Collection<Node> backNodeList = getBackToNodeList(doc, nodert,
					webUser);
			if (backNodeList != null && backNodeList.size() > 0) {
				buffer.append("<td style='white-space:nowrap;word-break:keep-all' class='commFont flow-back'>{*[cn.myapps.core.workflow.return_to]*}:");
				buffer.append("</td>");
				buffer.append("<td>");
				buffer.append("<select class='flow-back' id='back' name='_nextids'");
				buffer.append(" onchange='ev_setFlowType(false, this, "
						+ FlowType.RUNNING2RUNNING_BACK + ")'>");
				buffer.append("<option value=''>");
				buffer.append("{*[cn.myapps.core.workflow.please_choose]*}");
				buffer.append("</option>");
				for (Iterator<Node> iter = backNodeList.iterator(); iter
						.hasNext();) {
					Node backNode = (Node) iter.next();
					buffer.append("<option value='" + backNode.id + "'>");
					buffer.append(StringUtil.dencodeHTML(backNode.name));
					buffer.append("(")
							.append(StringUtil.dencodeHTML(backNode.statelabel))
							.append(")");
					buffer.append("</option>");
				}
				buffer.append("</select>");
				buffer.append("</td>");
			}
		} else if (state != null && state.toInt() == FlowState.SUSPEND) {
			Collection<Node> backNodeList = this.getBackToNodeList(doc, nodert,
					webUser, FlowState.SUSPEND);
			backNodeList = StateMachine.removeDuplicateNode(backNodeList);
			isDisplySubmit = true;

			if (backNodeList != null) {
				Iterator<Node> it4 = backNodeList.iterator();
				buffer.append("<td class='flow-next'>{*[cn.myapps.core.workflow.resume_flow]*}: </td>");
				buffer.append("<td>");
				while (it4.hasNext()) {
					Node backNode = (Node) it4.next();
					buffer.append("<input id='suspend' type='radio' name='_nextids' value='"
							+ backNode.id + "' ");
					buffer.append("' onclick='ev_setFlowType(false, this, ");
					buffer.append(FlowType.SUSPEND2RUNNING + ")' />");
					buffer.append(StringUtil.dencodeHTML(backNode.name));
				}
				buffer.append("</td>");
			}
		} else {
			isDisplySubmit = false;
			if (state != null && state.toInt() != FlowState.RUNNING) {
				if (state.toInt() == FlowState.START) {
					isDisplyFlow = false;
				} else {
					buffer.append("<td style='font-size:12px;font-family: Arial, Helvetica;color:#FF0000'>"
							+ doc.getStateLabel() + "</td>");
				}
			} else {
				buffer.append("<td style='font-size:12px;font-family: Arial, Helvetica;'>");
				buffer.append(doc.getStateLabel());
				buffer.append("</td>");
			}
		}

		// 挂起;恢复 操作
		if (nodert != null) {
			if ((currnode instanceof ManualNode)) {
				if (((ManualNode) currnode).isHandup) {
					if (nodert.getState() == 0) {
						buffer.append("<tr><td><input type='button' name='handup' value='{*[cn.myapps.core.workflow.suspend]*}' onclick=\"ev_flowHandup('"
								+ nodert.getId() + "')\"/></td></tr>");
					} else if (nodert.getState() == 1) {
						buffer.append("<tr><td><input type='button' name='handup' value='{*[cn.myapps.core.workflow.recover]*}' onclick=\"ev_flowRecover('"
								+ nodert.getId() + "')\"/></td></tr>");
						buffer.append("<input type='hidden' name='isHandup'/>");
					}
				}
			}
		}

		if (nodert != null) {
			if ((currnode instanceof ManualNode)) {// 编辑审批人
				buffer.append("<input id='isToPerson' type='hidden' name='isToPerson' value='"
						+ isToPerson + "' />");
			}
		}

		return buffer.toString();
	}

	/**
	 * 获取子流程实例创建总数
	 * 
	 * @param node
	 * @param doc
	 * @param webUser
	 * @return
	 * @throws Exception
	 */
	public int getSubFlowInstanceTotal(SubFlow subFlowNode, Document doc,
			WebUser webUser) throws Exception {
		int count = 0;
		IRunner runner = JavaScriptFactory.getInstance("",
				doc.getApplicationid());
		runner.initBSFManager(doc, new ParamsTable(), webUser,
				new ArrayList<ValidateMessage>());

		if (SubFlow.NUMBER_SETING_CUSTOM.equals(subFlowNode.numberSetingType)) {
			count = Integer.parseInt(subFlowNode.numberSetingContent);
		} else if (SubFlow.NUMBER_SETING_FIEDL
				.equals(subFlowNode.numberSetingType)) {
			count = Integer.parseInt(doc
					.getItemValueAsString(subFlowNode.numberSetingContent));
		} else if (SubFlow.NUMBER_SETING_SCRIPT
				.equals(subFlowNode.numberSetingType)) {
			Object obj = runner.run("subFlow:" + subFlowNode.name
					+ " numberSetingScript",
					StringUtil.dencodeHTML(subFlowNode.numberSetingContent));
			if (obj != null) {
				count = Integer.parseInt(String.valueOf(obj));
			}
		}

		return count;
	}
	
	/**
	 * 返回的字符串为重定义后的HTML，表达显示当前用户运行时节点
	 * 
	 * @param flowid
	 *            flow id
	 * @param docid
	 *            Document id
	 * @param webUser
	 *            webuser
	 * @return 字符串为显示当前用户运行时节点
	 * @throws Exception
	 */
	public String toFlowHtmlTextForInit(Document doc, WebUser webUser,
			String flowShowType, HttpServletRequest request) throws Exception {
		StringBuffer buffer = new StringBuffer();
		ParamsTable params = ParamsTable.convertHTTP(request);
		FlowStateRT instance = doc.getState();
		if (instance != null) {
			BillDefiVO flowVO = instance.getFlowVO();
			NodeRT nodert = null;
			String currNodeId = null;
			FlowDiagram fd = null;
			boolean isToPerson = false;
			boolean isAllowBack = false;// 是否允许回退
			if (flowVO != null) {
				fd = flowVO.toFlowDiagram();
				currNodeId = (String) request.getAttribute("_targetNode");
				// 从上下文获取当前处理节点对象
				nodert = (NodeRT) request.getAttribute("_targetNodeRT");
				if (currNodeId == null && nodert == null) {
					nodert = StateMachine.getCurrUserNodeRT(doc, webUser,
							currNodeId);
					if (nodert != null)
						currNodeId = nodert.getNodeid();
				}
			}
			//流程催办操作按钮
			List<NodeRT> allowUrge2ApprovalNodeRTs = getAllowUrge2ApprovalNodeRTs(instance, params, webUser, fd);
			if (nodert == null) {
				// 流程回撤
				isDisplyFlow = false;
				if (allowRetracement(doc, webUser, request)) {
					buffer.append("<input type='hidden' moduleType='retracement' id='btn_retracement' buttonname='{*[Retracement]*}'/>");
					isOnlyShowRetracementButton = true;
					isDisplyFlow = true;
				}
				if(!allowUrge2ApprovalNodeRTs.isEmpty()){
					buffer.append("<input type='hidden' moduleType='flowReminder' id='btn_flow_reminder' buttonname='{*[cn.myapps.core.workflow.activity.flowreminder.name]*}' ");
					buffer.append(" data-nodes='[");
					for (Iterator<NodeRT> iterator = allowUrge2ApprovalNodeRTs.iterator(); iterator
							.hasNext();) {
						NodeRT nodeRT2 = iterator.next();
						buffer.append("{\"nodertId\":\""+nodeRT2.getId()+"\",\"nodeName\":\""+nodeRT2.getName()+"\"},");
					}
					buffer.setLength(buffer.length()-1);
					buffer.append("]' />");
				}
				return buffer.toString();
			}
			// 获取当前结点
			Node currnode = null;
			if (currNodeId != null) {
				currnode = (Node) fd.getElementByID(currNodeId);
			}

			// 挂起;恢复 操作
			if (currnode != null) {
				if ((currnode instanceof ManualNode) 
						&& !doc.getIstmp()  //新建文档，不显示挂起按钮
						) {
					
					boolean flag = false;
					if (((ManualNode) currnode).handupEditMode == 0) {
						flag = ((ManualNode) currnode).isHandup;
					} else if (((ManualNode) currnode).handupEditMode == 1) {
						IRunner runner = JavaScriptFactory.getInstance(
								params.getSessionid(), doc.getApplicationid());
						runner.initBSFManager(doc, params, webUser,
								new ArrayList<ValidateMessage>());
						StringBuffer label = new StringBuffer();
						label.append(
								"FlowName:" + fd.getName() + " handupScript(")
								.append(fd.getId()).append(")." + fd.getName())
								.append(".handupScript");
						Object result = runner.run(label.toString(),
								((ManualNode) currnode).handupScript);
						if (result != null && result instanceof Boolean) {
							flag = ((Boolean) result).booleanValue();
						}
					}
					if (flag) {
						if (nodert.getState() == 0) {
							buffer.append("<tr><td><input type='hidden' moduleType='handup' id='_handup' buttonname='{*[cn.myapps.core.workflow.suspend]*}' nodertId='"
									+ nodert.getId() + "'/></td></tr>");
						} else if (nodert.getState() == 1) {
							buffer.append("<tr><td><input type='hidden' moduleType='recover' id='_recover' buttonname='{*[cn.myapps.core.workflow.recover]*}' nodertId='"
									+ nodert.getId() + "'/></td></tr>");
						}
					}
				}
			}

			// 流程提交操作按钮
			//if(fd.getNextNodeList(currnode.id).size()>0){
				buffer.append("<input type='hidden' moduleType='commit' id='btn_commit' />");
			//}
				
			if(!allowUrge2ApprovalNodeRTs.isEmpty()){
				buffer.append("<input type='hidden' moduleType='flowReminder' id='btn_flow_reminder' buttonname='{*[cn.myapps.core.workflow.activity.flowreminder.name]*}' ");
				buffer.append(" data-nodes='[");
				for (Iterator<NodeRT> iterator = allowUrge2ApprovalNodeRTs.iterator(); iterator
						.hasNext();) {
					NodeRT nodeRT2 = iterator.next();
					buffer.append("{\"nodertId\":\""+nodeRT2.getId()+"\",\"nodeName\":\""+nodeRT2.getName()+"\"},");
				}
				buffer.setLength(buffer.length()-1);
				buffer.append("]' />");
			}

			// 流程回撤操作按钮
			if (allowRetracement(doc, webUser, request)) {
				buffer.append("<input type='hidden' moduleType='retracement' id='btn_retracement' buttonname='{*[Retracement]*}' />");
			}
			// 流程加签操作按钮
			if (currnode instanceof ManualNode
					&& Integer.parseInt(((ManualNode) currnode).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND
					&& ((ManualNode) currnode).isApproverEdit) {
				buffer.append("<input type='hidden' moduleType='addAuditor' id='btn_addAuditor' />");
			}
			// 编辑流程审批人操作按钮
			if (currnode instanceof ManualNode
					&& ((ManualNode) currnode).isAllowEditAuditor) {
				buffer.append("<input type='hidden' moduleType='editAuditor' id='btn_editAuditor' />");
			}
			// 调整流程操作按钮
			if (currnode instanceof ManualNode
					&& ((ManualNode) currnode).isFrontEdit) {
				buffer.append("<input type='hidden' moduleType='editFlow' id='btn_editFlow' />");
			}
			// 流程终止操作按钮
			if (currnode instanceof ManualNode
					&& ((ManualNode) currnode).isAllowTermination) {
				buffer.append("<input type='hidden' moduleType='termination' id='btn_termination' />");
			}
			// 流程回退操作按钮
			if (currnode instanceof ManualNode
					&& ((ManualNode) currnode).cBack) {
				Collection<Node> backNodeList = getBackToNodeList(doc, nodert,
							webUser);
				if(backNodeList !=null && !backNodeList.isEmpty()){
					isAllowBack = true;
					buffer.append("<input type='hidden' moduleType='back' id='btn_back' />");
				}
			}

			if (nodert != null) {
				if ((currnode instanceof ManualNode)) {// 编辑审批人
					buffer.append("<input id='isToPerson' type='hidden' name='isToPerson' value='"
							+ isToPerson + "' />");
				}
			}
			
			//返回节点PermissionList
//			String fieldpermlist = ((ManualNode) currnode).fieldpermlist;
//			if (currnode instanceof ManualNode
//					&&  fieldpermlist != null && !fieldpermlist.isEmpty()) {
//				fieldpermlist = StringUtil.dencodeHTML(fieldpermlist);
//				buffer.append("<textarea id='fieldpermlist' name='fieldpermlist' style='display:none;'>")
//					.append(fieldpermlist)
//					.append("</textarea>");
//			}

		}

		return buffer.toString();
	}
	
	/**
	 * 返回的字符串为重定义后的HTML，表达显示当前用户运行时节点
	 * 
	 * @param flowid
	 *            flow id
	 * @param docid
	 *            Document id
	 * @param webUser
	 *            webuser
	 * @return 字符串为显示当前用户运行时节点
	 * @throws Exception
	 */
	public String toFlowHtmlTextForCommitTo(Document doc, WebUser webUser,
			String flowShowType, HttpServletRequest request) throws Exception {
		StringBuffer buffer = new StringBuffer();
		Environment evn = Environment.getInstance();
		FlowStateRT instance = doc.getState();
		if (instance != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			NodeRT nodert = null;
			String currNodeId = null;
			FlowDiagram fd = null;
			boolean isToPerson = false;
			if (flowVO != null) {
				fd = flowVO.toFlowDiagram();
				currNodeId = (String) request.getAttribute("_targetNode");
				// 从上下文获取当前处理节点对象
				nodert = (NodeRT) request.getAttribute("_targetNodeRT");
				
				if(instance.isTemp()){
					nodert = ((List<NodeRT>)instance.getNoderts()).get(0);
					currNodeId = nodert.getNodeid();
				}
				
				if (currNodeId == null && nodert == null) {
					nodert = StateMachine.getCurrUserNodeRT(doc, webUser,
							currNodeId);
					if (nodert != null)
						currNodeId = nodert.getNodeid();
				}
			}
			// 获取当前结点
			Node currnode = null;
			State state = null;
			if (currNodeId != null) {
				currnode = (Node) fd.getElementByID(currNodeId);
				state = StateCreator.getNodeState(currnode);
				if (currnode instanceof ManualNode) {
					if (((ManualNode) currnode).isFrontEdit) {
						this.isFrontEdit = true;
					}
				}
				buffer.append("<input type='hidden' id='_currentNodeId' name='_currentNodeId' value='"+currNodeId+"'>");
			}

			if (state != null && state.toInt() == FlowState.RUNNING) {// 送下一步
				isDisplySubmit = true;
				Collection<Node> nextNodeList = fd.getNextNodeList(currnode.id,doc,ParamsTable.convertHTTP(request),webUser);

				// 判断是否能通过
				boolean issplit = false;
				boolean needToPersion = false;// 是否需要选择审批人操作
				boolean isAppentCirculator = false;
				//下一步节点的默认选中状态
				int nextNodeCheckedStatus = ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED;

				// checkedState判断在html拼接的时候是否需要为checkbox 和 radio 添加"默认选中"标识
				boolean checkedState = true;
				if (currnode != null) {
					if (currnode instanceof ManualNode) {
						issplit = ((ManualNode) currnode).issplit;
						nextNodeCheckedStatus = ((ManualNode) currnode).nextNodeCheckedStatus;
					} else if (currnode instanceof AutoNode) {
						issplit = ((AutoNode) currnode).issplit;
					}
				}

				// 分散的情况下，为每个分支节点增加令牌
				if (issplit) {
					buffer.append("<input name='splitToken' type='hidden' value='"
							+ currNodeId + "' /> ");
				}
				
				buffer.append("<fieldset id='fieldset_commit_to'><legend>{*[cn.myapps.core.workflow.commit_to]*}</legend>");

				if (nextNodeList != null && nextNodeList.size() > 0) {
					Iterator<Node> it3 = nextNodeList.iterator();

					int imgid = 0;
					while (it3.hasNext()) {
						Node nextNode = (Node) it3.next();

						boolean isOthers = false;
						needToPersion = false;
						String id = "next";
						String flowOperation = FlowType.RUNNING2RUNNING_NEXT;
						if (!(nextNode instanceof ManualNode)) {
							isOthers = true;
							id = "other";
							if (nextNode instanceof SuspendNode) {
								flowOperation = FlowType.RUNNING2SUSPEND;
							} else if (nextNode instanceof CompleteNode) {
								flowOperation = FlowType.RUNNING2COMPLETE;
							} else if (nextNode instanceof AutoNode) {
								flowOperation = FlowType.RUNNING2AUTO;
							}
						}
						// TD1
						buffer.append("<div>");
						buffer.append("<label ><input id='" + id + "' type='");
						buffer.append(issplit ? "checkbox'" : "radio'");
						if (!issplit && checkedState) {
							buffer.append(" checked");
							checkedState = false;
						}
						buffer.append(" name='_nextids' value='").append(nextNode.id).append("'");
						if(issplit){//并行
							switch (nextNodeCheckedStatus) {
							case ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED://默认选中
								buffer.append(" checked");
								buffer.append(" onclick='ev_setFlowType(").append(isOthers);
								buffer.append(", this, ").append(flowOperation).append(")' />");
								break;
							case ManualNode.NEXT_NODE_CHECKED_STATUS_UNCHECKED://默认不选中
								buffer.append(" onclick='ev_setFlowType(").append(isOthers);
								buffer.append(", this, ").append(flowOperation).append(")' />");
								break;
							case ManualNode.NEXT_NODE_CHECKED_STATUS_CHECKED_AND_LOCKED://默认选中且锁定
								buffer.append(" checked");
								buffer.append(" onclick='ev_setFlowType(").append(isOthers);
								buffer.append(", this, ").append(flowOperation).append(");return false;' />");
								break;
							default:
								break;
							}
						}else{//串行
							buffer.append(" onclick='ev_setFlowType(").append(isOthers);
							buffer.append(", this, ").append(flowOperation).append(")' />");
						}
						buffer.append(StringUtil.dencodeHTML(nextNode.name)).append("</label>");

						if ((nextNode instanceof ManualNode)
								&& ((ManualNode) currnode).isToPerson) {// 手动节点指定审批人
							Collection<BaseUser> userList = StateMachineUtil
									.getPrincipalList(doc, new ParamsTable(),
											nextNode, doc.getDomainid(),
											doc.getApplicationid(), webUser);

							// 会签模式时,最后一个审批者才可以去指定审批人
							boolean isAllowToPerson = this.isAllowToPerson(
									(ManualNode) currnode, nodert);

							if (userList != null && userList.size() > 1
									&& isAllowToPerson) {// 当下一步审批人超过1人时才出现指定审批人操作
								needToPersion = true;
							}
						}

						// 循环生成isToPerson，以判断每个节点是否有指定审批人
						if (currnode != null) {
							if ((currnode instanceof ManualNode)
									&& (nextNode instanceof ManualNode)) {// 编辑审批人
								buffer.append("<input id='isToPerson_"
										+ nextNode.id
										+ "' type='hidden' name='isToPerson_"
										+ nextNode.id + "' value='"
										+ needToPersion + "' />");
							}
						}

						if ((nextNode instanceof ManualNode)
								&& ((ManualNode) currnode).isToPerson
								&& needToPersion) {// 手动节点指定审批人
							isToPerson = true;
							buffer.append("&nbsp&nbsp{*[cn.myapps.core.workflow.specify_auditor]*}:&nbsp");
							buffer.append("<span class='flowToPerson' id='opra_" + imgid
									+ "' style='display:inline;'>");
							buffer.append("<input class='flowToPerson-Input' id='input_"
									+ imgid
									+ "' name='input_"
									+ imgid
									+ "' readonly='true' type='text' size='10' value='"
									+ (request.getParameter("input_" + imgid) != null ? request
											.getParameter("input_" + imgid)
											: "") + "'/>");

							buffer.append("<img id='selectUserImg_"
									+ imgid
									+ "' style='cursor:pointer;display:inline;' onclick=\"showUserSelect('actionName', {nextNodeId:'"
									+ nextNode.id
									+ "', docid:'"
									+ doc.getId()
									+ "',flowid:'"
									+ flowVO.getId()
									+ "', textField:'input_"
									+ imgid
									+ "',valueField: 'input_"
									+ imgid
									+ "',hiddenIds:'input_hidden_id_"
									+ imgid
									+ "', readonly: false})\" src='"
									+ evn.getContextPath()
									+ "/portal/share/component/dialog/images/userselect.gif' /> ");
							buffer.append("<input type=\"hidden\" id=\"input_hidden_id_"
									+ imgid
									+ "\" name=\"input_hidden_id_"
									+ imgid
									+ "\" value=\""
									+ (request.getParameter("input_hidden_id_"
											+ imgid) != null ? request
											.getParameter("input_hidden_id_"
													+ imgid) : "") + "\"/>");
							buffer.append("</span>");
						} else if ((nextNode instanceof SubFlow)
								&& ((SubFlow) nextNode).isToPerson) {// 子流程节点指定审批人
							isToPerson = true;
							String numberSetingType = ((SubFlow) nextNode).numberSetingType;
							int instanceTotal = getSubFlowInstanceTotal(
									(SubFlow) nextNode, doc, webUser);
							buffer.append("&nbsp&nbsp{*[cn.myapps.core.workflow.specify_auditor]*}:&nbsp");
							buffer.append("<span id='opra_" + imgid
									+ "' style='display:inline;'>");
							buffer.append("<input id='input_"
									+ imgid
									+ "' name='input_"
									+ imgid
									+ "' readonly='true' type='text'  size='10'  value='"
									+ (request.getParameter("input_" + imgid) != null ? request
											.getParameter("input_" + imgid)
											: "") + "'/>");
							buffer.append("<img id='selectUserImg_"
									+ imgid
									+ "' style='cursor:pointer;display:inline;' onclick=\"showUserSelectOnSubFlow('actionName', {nextNodeId:'"
									+ nextNode.id
									+ "',instanceId:'"
									+ doc.getStateid()
									+ "',numberSetingType:'"
									+ numberSetingType
									+ "',instanceTotal:'"
									+ instanceTotal
									+ "', docid:'"
									+ doc.getId()
									+ "',flowid:'"
									+ flowVO.getId()
									+ "', textField:'input_"
									+ imgid
									+ "',valueField: 'input_"
									+ imgid
									+ "', readonly: false})\" src='"
									+ evn.getContextPath()
									+ "/portal/share/component/dialog/images/userselect.gif' /> ");
							buffer.append("<input type=\"hidden\" id=\"input_hidden_id_"
									+ imgid
									+ "\" name=\"input_hidden_id_"
									+ imgid
									+ "\" value=\""
									+ (request.getParameter("input_hidden_id_"
											+ imgid) != null ? request
											.getParameter("input_hidden_id_"
													+ imgid) : "") + "\"/>");
							buffer.append("</span>");
						}

						// 指定流程抄送人-------------------------------------------start

						if ((currnode instanceof ManualNode)
								&& ((ManualNode) currnode).isCarbonCopy
								&& ((ManualNode) currnode).isSelectCirculator
								&& !isAppentCirculator) {// 手动节点指定抄送人
							isToPerson = true;
							isAppentCirculator = true;
							buffer.append("{*[cn.myapps.core.workflow.label.copy_for]*}:&nbsp");
							buffer.append("<span id='opra_" + imgid
									+ "' style='display:inline;'>");
							buffer.append("<input id='_circulator"
									+ "' name='_circulator"
									+ "' readonly='true'  type='text'  size='10' />");

							buffer.append("<img id='selectUserImg_"
									+ imgid
									+ "' style='cursor:pointer;display:inline;' onclick=\"selectCirculator('actionName', {nextNodeId:'"
									+ currnode.id
									+ "', docid:'"
									+ doc.getId()
									+ "',flowid:'"
									+ flowVO.getId()
									+ "', textField:'_circulator"
									+ "',valueField: '_circulator"
									+ "', readonly: false})\" src='"
									+ evn.getContextPath()
									+ "/portal/share/component/dialog/images/userselect.gif' /> ");
							buffer.append("</span>");
						}

						// 指定流程抄送人-------------------------------------------end
						imgid++;
						buffer.append("</div>");
					}

					buffer.append("</fieldset>");

				}

			} else {
				isDisplySubmit = false;
				if (state != null && state.toInt() != FlowState.RUNNING) {
					if (state.toInt() == FlowState.START) {
						isDisplyFlow = false;
					} else {
						buffer.append("<td style='font-size:12px;font-family: Arial, Helvetica;color:#FF0000'>"
								+ doc.getStateLabel() + "</td>");
					}
				} else {
					buffer.append("<td style='font-size:12px;font-family: Arial, Helvetica;'>");
					buffer.append(doc.getStateLabel());
					buffer.append("</td>");
				}
			}

			if (nodert != null) {
				if ((currnode instanceof ManualNode)) {// 编辑审批人
					buffer.append("<input id='isToPerson' type='hidden' name='isToPerson' value='"
							+ isToPerson + "' />");
				}
			}
		}

		return buffer.toString();
	}	
	/**
	 * 返回的字符串为重定义后的HTML，表达显示当前用户运行时节点
	 * 
	 * @param flowid
	 *            flow id
	 * @param docid
	 *            Document id
	 * @param webUser
	 *            webuser
	 * @return 字符串为显示当前用户运行时节点
	 * @throws Exception
	 */
	public String toFlowHtmlTextForReturnTo(Document doc, WebUser webUser,
			String flowShowType, HttpServletRequest request) throws Exception {
		StringBuffer buffer = new StringBuffer();
		Environment evn = Environment.getInstance();

		if (doc.getState() != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			NodeRT nodert = null;
			String currNodeId = null;
			FlowDiagram fd = null;
			boolean isToPerson = false;
			boolean isAllowBack = false;// 是否允许回退
			if (flowVO != null) {
				fd = flowVO.toFlowDiagram();
				currNodeId = (String) request.getAttribute("_targetNode");
				// 从上下文获取当前处理节点对象
				nodert = (NodeRT) request.getAttribute("_targetNodeRT");
				if (currNodeId == null && nodert == null) {
					nodert = StateMachine.getCurrUserNodeRT(doc, webUser,
							currNodeId);
					if (nodert != null)
						currNodeId = nodert.getNodeid();
				}
			}
			if (nodert == null) {
				return buffer.toString();
			}
			// 获取当前结点
			Node currnode = null;
			State state = null;
			if (currNodeId != null) {
				currnode = (Node) fd.getElementByID(currNodeId);
				state = StateCreator.getNodeState(currnode);
				if (currnode instanceof ManualNode) {
					if (((ManualNode) currnode).isFrontEdit) {
						this.isFrontEdit = true;
					}
				}
				buffer.append("<input type='hidden' id='_currentNodeId' name='_currentNodeId' value='"+currNodeId+"'>");
			}

			if (state != null && state.toInt() == FlowState.RUNNING) {// 送下一步
				isDisplySubmit = true;

				// 判断是否能通过
				boolean issplit = false;
				boolean needToPersion = false;// 是否需要选择审批人操作
				boolean isAppentCirculator = false;

				// checkedState判断在html拼接的时候是否需要为checkbox 和 radio 添加"默认选中"标识
				boolean checkedState = true;
				if (currnode != null) {
					if (currnode instanceof ManualNode) {
						issplit = ((ManualNode) currnode).issplit;
					} else if (currnode instanceof AutoNode) {
						issplit = ((AutoNode) currnode).issplit;
					}
				}

				// 分散的情况下，为每个分支节点增加令牌
				if (issplit) {
					buffer.append("<input name='splitToken' type='hidden' value='"
							+ currNodeId + "' /> ");
				}

				Collection<Node> backNodeList = getBackToNodeList(doc, nodert,
						webUser);
				if (backNodeList != null && backNodeList.size() > 0) {
					isAllowBack = true;
					buffer.append("<fieldset id='fieldset_return_to'><legend>{*[cn.myapps.core.workflow.return_to]*}</legend>");

					buffer.append("<select class='flow-back' id='back' name='_nextids'");
					buffer.append(" onchange='ev_setFlowType(false, this, "
							+ FlowType.RUNNING2RUNNING_BACK + ")'>");
					buffer.append("<option value=''>");
					buffer.append("{*[cn.myapps.core.workflow.please_choose]*}");
					buffer.append("</option>");
					for (Iterator<Node> iter = backNodeList.iterator(); iter
							.hasNext();) {
						String _nextids = request.getParameter("_nextids");
						Node backNode = (Node) iter.next();
						if (_nextids != null) {
							if (_nextids.equals(backNode.id)) {
								buffer.append("<option value='" + backNode.id
										+ "' selected='selected'>");
							} else {
								buffer.append("<option value='" + backNode.id
										+ "'>");
							}
						} else {
							buffer.append("<option value='" + backNode.id
									+ "'>");
						}
						buffer.append(StringUtil.dencodeHTML(backNode.name));
						buffer.append("(")
								.append(StringUtil
										.dencodeHTML(backNode.statelabel))
								.append(")");
						buffer.append("</option>");

					}
					buffer.append("<input type='hidden' moduleType='back' id='btn_commit' />");
					buffer.append("</fieldset>");
				}
			} else {
				isDisplySubmit = false;
				if (state != null && state.toInt() != FlowState.RUNNING) {
					if (state.toInt() == FlowState.START) {
						isDisplyFlow = false;
					} else {
						buffer.append("<td style='font-size:12px;font-family: Arial, Helvetica;color:#FF0000'>"
								+ doc.getStateLabel() + "</td>");
					}
				} else {
					buffer.append("<td style='font-size:12px;font-family: Arial, Helvetica;'>");
					buffer.append(doc.getStateLabel());
					buffer.append("</td>");
				}
			}
			
			if (nodert != null) {
				if ((currnode instanceof ManualNode)) {// 编辑审批人
					buffer.append("<input id='isToPerson' type='hidden' name='isToPerson' value='"
							+ isToPerson + "' />");
				}
			}
		}

		return buffer.toString();
	}
	
	/**
	 * 返回的字符串为重定义后的HTML，表达显示当前用户运行时节点
	 * 
	 * @param flowid
	 *            flow id
	 * @param docid
	 *            Document id
	 * @param webUser
	 *            webuser
	 * @return 字符串为显示当前用户运行时节点
	 * @throws Exception
	 */
	public String toFlowHtmlText(Document doc, WebUser webUser,
			String flowShowType, HttpServletRequest request) throws Exception {
		StringBuffer buffer = new StringBuffer();
		Environment evn = Environment.getInstance();

		if (doc.getState() != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			NodeRT nodert = null;
			String currNodeId = null;
			FlowDiagram fd = null;
			boolean isToPerson = false;
			boolean isAllowBack = false;// 是否允许回退
			if (flowVO != null) {
				fd = flowVO.toFlowDiagram();
				currNodeId = (String) request.getAttribute("_targetNode");
				// 从上下文获取当前处理节点对象
				nodert = (NodeRT) request.getAttribute("_targetNodeRT");
				if (currNodeId == null && nodert == null) {
					nodert = StateMachine.getCurrUserNodeRT(doc, webUser,
							currNodeId);
					if (nodert != null)
						currNodeId = nodert.getNodeid();
				}
			}
			if (nodert == null) {
				// 流程回撤
				isDisplyFlow = false;
				if (allowRetracement(doc, webUser, request)) {
					buffer.append("<input type='hidden' moduleType='retracement' id='btn_retracement' buttonname='{*[Retracement]*}'/>");
					isOnlyShowRetracementButton = true;
					isDisplyFlow = true;
				}
				return buffer.toString();
			}
			// 获取当前结点
			Node currnode = null;
			State state = null;
			if (currNodeId != null) {
				currnode = (Node) fd.getElementByID(currNodeId);
				state = StateCreator.getNodeState(currnode);
				if (currnode instanceof ManualNode) {
					if (((ManualNode) currnode).isFrontEdit) {
						this.isFrontEdit = true;
					}
				}
				buffer.append("<input type='hidden' id='_currentNodeId' name='_currentNodeId' value='"+currNodeId+"'>");
			}

			if (state != null && state.toInt() == FlowState.RUNNING) {// 送下一步
				isDisplySubmit = true;
				Collection<Node> nextNodeList = fd.getNextNodeList(currnode.id,doc,ParamsTable.convertHTTP(request),webUser);

				// 判断是否能通过
				boolean issplit = false;
				boolean needToPersion = false;// 是否需要选择审批人操作
				boolean isAppentCirculator = false;

				// checkedState判断在html拼接的时候是否需要为checkbox 和 radio 添加"默认选中"标识
				boolean checkedState = true;
				if (currnode != null) {
					if (currnode instanceof ManualNode) {
						issplit = ((ManualNode) currnode).issplit;
					} else if (currnode instanceof AutoNode) {
						issplit = ((AutoNode) currnode).issplit;
					}
				}

				// 分散的情况下，为每个分支节点增加令牌
				if (issplit) {
					buffer.append("<input name='splitToken' type='hidden' value='"
							+ currNodeId + "' /> ");
				}

				if (nextNodeList != null && nextNodeList.size() > 0) {
					Iterator<Node> it3 = nextNodeList.iterator();

					buffer.append("<fieldset id='fieldset_commit_to'><legend>{*[cn.myapps.core.workflow.commit_to]*}</legend>");

					int imgid = 0;
					while (it3.hasNext()) {
						Node nextNode = (Node) it3.next();

						boolean isOthers = false;
						needToPersion = false;
						String id = "next";
						String flowOperation = FlowType.RUNNING2RUNNING_NEXT;
						if (!(nextNode instanceof ManualNode)) {
							isOthers = true;
							id = "other";
							if (nextNode instanceof SuspendNode) {
								flowOperation = FlowType.RUNNING2SUSPEND;
							} else if (nextNode instanceof CompleteNode) {
								flowOperation = FlowType.RUNNING2COMPLETE;
							} else if (nextNode instanceof AutoNode) {
								flowOperation = FlowType.RUNNING2AUTO;
							}
						}
						// TD1
						buffer.append("<div>");
						buffer.append("<input id='" + id + "' type='");
						buffer.append(issplit ? "checkbox' checked" : "radio'");
						if (!issplit && checkedState) {
							buffer.append(" checked");
							checkedState = false;
						}
						buffer.append(" name='_nextids' value='" + nextNode.id);
						buffer.append("' onclick='ev_setFlowType(" + isOthers);
						buffer.append(", this, " + flowOperation + ")' />"
								+ StringUtil.dencodeHTML(nextNode.name));

						if ((nextNode instanceof ManualNode)
								&& ((ManualNode) currnode).isToPerson) {// 手动节点指定审批人
							Collection<BaseUser> userList = StateMachineUtil
									.getPrincipalList(doc, new ParamsTable(),
											nextNode, doc.getDomainid(),
											doc.getApplicationid(), webUser);

							// 会签模式时,最后一个审批者才可以去指定审批人
							boolean isAllowToPerson = this.isAllowToPerson(
									(ManualNode) currnode, nodert);

							if (userList != null && userList.size() > 1
									&& isAllowToPerson) {// 当下一步审批人超过1人时才出现指定审批人操作
								needToPersion = true;
							}
						}

						// 循环生成isToPerson，以判断每个节点是否有指定审批人
						if (currnode != null) {
							if ((currnode instanceof ManualNode)
									&& (nextNode instanceof ManualNode)) {// 编辑审批人
								buffer.append("<input id='isToPerson_"
										+ nextNode.id
										+ "' type='hidden' name='isToPerson_"
										+ nextNode.id + "' value='"
										+ needToPersion + "' />");
							}
						}

						if ((nextNode instanceof ManualNode)
								&& ((ManualNode) currnode).isToPerson
								&& needToPersion) {// 手动节点指定审批人
							isToPerson = true;
							buffer.append("&nbsp&nbsp{*[cn.myapps.core.workflow.specify_auditor]*}:&nbsp");
							buffer.append("<span id='opra_" + imgid
									+ "' style='display:inline;'>");
							buffer.append("<input id='input_"
									+ imgid
									+ "' name='input_"
									+ imgid
									+ "' readonly='true' type='text' size='10' value='"
									+ (request.getParameter("input_" + imgid) != null ? request
											.getParameter("input_" + imgid)
											: "") + "'/>");

							buffer.append("<img id='selectUserImg_"
									+ imgid
									+ "' style='cursor:pointer;display:inline;' onclick=\"showUserSelect('actionName', {nextNodeId:'"
									+ nextNode.id
									+ "', docid:'"
									+ doc.getId()
									+ "',flowid:'"
									+ flowVO.getId()
									+ "', textField:'input_"
									+ imgid
									+ "',valueField: 'input_"
									+ imgid
									+ "',hiddenIds:'input_hidden_id_"
									+ imgid
									+ "', readonly: false})\" src='"
									+ evn.getContextPath()
									+ "/portal/share/component/dialog/images/userselect.gif' /> ");
							buffer.append("<input type=\"hidden\" id=\"input_hidden_id_"
									+ imgid
									+ "\" name=\"input_hidden_id_"
									+ imgid
									+ "\" value=\""
									+ (request.getParameter("input_hidden_id_"
											+ imgid) != null ? request
											.getParameter("input_hidden_id_"
													+ imgid) : "") + "\"/>");
							buffer.append("</span>");
						} else if ((nextNode instanceof SubFlow)
								&& ((SubFlow) nextNode).isToPerson) {// 子流程节点指定审批人
							isToPerson = true;
							String numberSetingType = ((SubFlow) nextNode).numberSetingType;
							int instanceTotal = getSubFlowInstanceTotal(
									(SubFlow) nextNode, doc, webUser);
							buffer.append("&nbsp&nbsp{*[cn.myapps.core.workflow.specify_auditor]*}:&nbsp");
							buffer.append("<span id='opra_" + imgid
									+ "' style='display:inline;'>");
							buffer.append("<input id='input_"
									+ imgid
									+ "' name='input_"
									+ imgid
									+ "' readonly='true' type='text'  size='10'  value='"
									+ (request.getParameter("input_" + imgid) != null ? request
											.getParameter("input_" + imgid)
											: "") + "'/>");
							buffer.append("<img id='selectUserImg_"
									+ imgid
									+ "' style='cursor:pointer;display:inline;' onclick=\"showUserSelectOnSubFlow('actionName', {nextNodeId:'"
									+ nextNode.id
									+ "',instanceId:'"
									+ doc.getStateid()
									+ "',numberSetingType:'"
									+ numberSetingType
									+ "',instanceTotal:'"
									+ instanceTotal
									+ "', docid:'"
									+ doc.getId()
									+ "',flowid:'"
									+ flowVO.getId()
									+ "', textField:'input_"
									+ imgid
									+ "',valueField: 'input_"
									+ imgid
									+ "', readonly: false})\" src='"
									+ evn.getContextPath()
									+ "/portal/share/component/dialog/images/userselect.gif' /> ");
							buffer.append("<input type=\"hidden\" id=\"input_hidden_id_"
									+ imgid
									+ "\" name=\"input_hidden_id_"
									+ imgid
									+ "\" value=\""
									+ (request.getParameter("input_hidden_id_"
											+ imgid) != null ? request
											.getParameter("input_hidden_id_"
													+ imgid) : "") + "\"/>");
							buffer.append("</span>");
						}

						// 指定流程抄送人-------------------------------------------start

						if ((currnode instanceof ManualNode)
								&& ((ManualNode) currnode).isCarbonCopy
								&& ((ManualNode) currnode).isSelectCirculator
								&& !isAppentCirculator) {// 手动节点指定抄送人
							isToPerson = true;
							isAppentCirculator = true;
							buffer.append("{*[cn.myapps.core.workflow.label.copy_for]*}:&nbsp");
							buffer.append("<span id='opra_" + imgid
									+ "' style='display:inline;'>");
							buffer.append("<input id='_circulator"
									+ "' name='_circulator"
									+ "' readonly='true'  type='text'  size='10' />");

							buffer.append("<img id='selectUserImg_"
									+ imgid
									+ "' style='cursor:pointer;display:inline;' onclick=\"selectCirculator('actionName', {nextNodeId:'"
									+ currnode.id
									+ "', docid:'"
									+ doc.getId()
									+ "',flowid:'"
									+ flowVO.getId()
									+ "', textField:'_circulator"
									+ "',valueField: '_circulator"
									+ "', readonly: false})\" src='"
									+ evn.getContextPath()
									+ "/portal/share/component/dialog/images/userselect.gif' /> ");
							buffer.append("</span>");
						}

						// 指定流程抄送人-------------------------------------------end
						imgid++;
						buffer.append("</div>");
					}

					buffer.append("</fieldset>");

				}

				Collection<Node> backNodeList = getBackToNodeList(doc, nodert,
						webUser);
				if (backNodeList != null && backNodeList.size() > 0) {
					isAllowBack = true;
					buffer.append("<fieldset id='fieldset_return_to'><legend>{*[cn.myapps.core.workflow.return_to]*}</legend>");

					buffer.append("<select class='flow-back' id='back' name='_nextids'");
					buffer.append(" onchange='ev_setFlowType(false, this, "
							+ FlowType.RUNNING2RUNNING_BACK + ")'>");
					buffer.append("<option value=''>");
					buffer.append("{*[cn.myapps.core.workflow.please_choose]*}");
					buffer.append("</option>");
					for (Iterator<Node> iter = backNodeList.iterator(); iter
							.hasNext();) {
						String _nextids = request.getParameter("_nextids");
						Node backNode = (Node) iter.next();
						if (_nextids != null) {
							if (_nextids.equals(backNode.id)) {
								buffer.append("<option value='" + backNode.id
										+ "' selected='selected'>");
							} else {
								buffer.append("<option value='" + backNode.id
										+ "'>");
							}
						} else {
							buffer.append("<option value='" + backNode.id
									+ "'>");
						}
						buffer.append(StringUtil.dencodeHTML(backNode.name));
						buffer.append("(")
								.append(StringUtil
										.dencodeHTML(backNode.statelabel))
								.append(")");
						buffer.append("</option>");

					}
					buffer.append("<input type='hidden' moduleType='back' id='btn_commit' />");
					buffer.append("</fieldset>");
				}
			} else {
				isDisplySubmit = false;
				if (state != null && state.toInt() != FlowState.RUNNING) {
					if (state.toInt() == FlowState.START) {
						isDisplyFlow = false;
					} else {
						buffer.append("<td style='font-size:12px;font-family: Arial, Helvetica;color:#FF0000'>"
								+ doc.getStateLabel() + "</td>");
					}
				} else {
					buffer.append("<td style='font-size:12px;font-family: Arial, Helvetica;'>");
					buffer.append(doc.getStateLabel());
					buffer.append("</td>");
				}
			}

			// 挂起;恢复 操作
			if (currnode != null) {
				if ((currnode instanceof ManualNode) 
						&& !doc.getIstmp()  //新建文档，不显示挂起按钮
						) {
					
					boolean flag = false;
					if (((ManualNode) currnode).handupEditMode == 0) {
						flag = ((ManualNode) currnode).isHandup;
					} else if (((ManualNode) currnode).handupEditMode == 1) {
						ParamsTable params = ParamsTable.convertHTTP(request);
						IRunner runner = JavaScriptFactory.getInstance(
								params.getSessionid(), doc.getApplicationid());
						runner.initBSFManager(doc, params, webUser,
								new ArrayList<ValidateMessage>());
						StringBuffer label = new StringBuffer();
						label.append(
								"FlowName:" + fd.getName() + " handupScript(")
								.append(fd.getId()).append(")." + fd.getName())
								.append(".handupScript");
						Object result = runner.run(label.toString(),
								((ManualNode) currnode).handupScript);
						if (result != null && result instanceof Boolean) {
							flag = ((Boolean) result).booleanValue();
						}
					}
					if (flag) {
						if (nodert.getState() == 0) {
							buffer.append("<tr><td><input type='hidden' moduleType='handup' id='_handup' buttonname='{*[cn.myapps.core.workflow.suspend]*}' nodertId='"
									+ nodert.getId() + "'/></td></tr>");
						} else if (nodert.getState() == 1) {
							buffer.append("<tr><td><input type='hidden' moduleType='recover' id='_recover' buttonname='{*[cn.myapps.core.workflow.recover]*}' nodertId='"
									+ nodert.getId() + "'/></td></tr>");
						}
					}
				}
			}

			// 流程提交操作按钮
			buffer.append("<input type='hidden' moduleType='commit' id='btn_commit' />");

			// 流程回撤操作按钮
			if (allowRetracement(doc, webUser, request)) {
				buffer.append("<input type='hidden' moduleType='retracement' id='btn_retracement' buttonname='{*[Retracement]*}' />");
			}
			// 流程加签操作按钮
			if (currnode instanceof ManualNode
					&& Integer.parseInt(((ManualNode) currnode).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND
					&& ((ManualNode) currnode).isApproverEdit) {
				buffer.append("<input type='hidden' moduleType='addAuditor' id='btn_addAuditor' />");
			}
			// 编辑流程审批人操作按钮
			if (currnode instanceof ManualNode
					&& ((ManualNode) currnode).isAllowEditAuditor) {
				buffer.append("<input type='hidden' moduleType='editAuditor' id='btn_editAuditor' />");
			}
			// 调整流程操作按钮
			if (currnode instanceof ManualNode
					&& ((ManualNode) currnode).isFrontEdit) {
				buffer.append("<input type='hidden' moduleType='editFlow' id='btn_editFlow' />");
			}
			// 流程终止操作按钮
			if (currnode instanceof ManualNode
					&& ((ManualNode) currnode).isAllowTermination) {
				buffer.append("<input type='hidden' moduleType='termination' id='btn_termination' />");
			}
			// 流程回退操作按钮
			if (isAllowBack) {
				buffer.append("<input type='hidden' moduleType='back' id='btn_back' />");
			}

			if (nodert != null) {
				if ((currnode instanceof ManualNode)) {// 编辑审批人
					buffer.append("<input id='isToPerson' type='hidden' name='isToPerson' value='"
							+ isToPerson + "' />");
				}
			}
		}
		else {//没有流程状态，代表没有启动流程，需要自动去到流程的第一个环节。
			LOG.debug("start flow...");
			
			
		}

		return buffer.toString();
	}
	
	

	/**
	 * 是否允许流程回撤
	 * 
	 * @param doc
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private boolean allowRetracement(Document doc, WebUser user,
			HttpServletRequest request) throws Exception {
		boolean isAllow = false;
		if (!StringUtil.isBlank(doc.getParentid())) {// 子表单没有回撤操作权限单
			return false;
		}
		BillDefiVO flowVO = doc.getState().getFlowVO();
		FlowDiagram fd = flowVO.toFlowDiagram();
		Node currNode = null;
		if (doc.getState().isComplete()) {
			RelationHISProcess procss = (RelationHISProcess) ProcessFactory
					.createRuntimeProcess(RelationHISProcess.class,
							doc.getApplicationid());
			RelationHIS his = procss.getCompleteRelationHIS(doc.getId(), doc
					.getState().getId());
			if (his != null) {
				currNode = (Node) fd.getElementByID(his != null ? his
						.getEndnodeid() : null);
			}

		} else if (doc.getState().getNoderts() != null
				&& doc.getState().getNoderts().size() > 0) {
			NodeRT nodert = (NodeRT) doc.getState().getNoderts().iterator()
					.next();
			currNode = (Node) fd.getElementByID(nodert.getNodeid());
		}
		
		if (currNode != null) {
			if (currNode instanceof SubFlow) {
				return false;
			}
			if (currNode instanceof CompleteNode) {
				return false;
			}
			if (currNode instanceof AutoNode) {
				return false;
			}
			
			Node nextNode = StateMachine.getBackNodeByHis(doc, flowVO,
					currNode.id, user, FlowState.RUNNING);
			if (nextNode != null) {
				if (nextNode instanceof SubFlow) {
					return false;
				}
				if (((ManualNode) nextNode).retracementEditMode == 0
						&& ((ManualNode) nextNode).cRetracement) {
					isAllow = true;
				} else if (((ManualNode) nextNode).retracementEditMode == 1
						&& ((ManualNode) nextNode).retracementScript != null
						&& (((ManualNode) nextNode).retracementScript).trim()
								.length() > 0) {
					ParamsTable params = ParamsTable.convertHTTP(request);
					IRunner runner = JavaScriptFactory.getInstance(
							params.getSessionid(), doc.getApplicationid());
					runner.initBSFManager(doc, params, user,
							new ArrayList<ValidateMessage>());
					StringBuffer label = new StringBuffer();
					label.append(currNode.name).append("[" + currNode.id + "]")
							.append(".retracementScript");
					Object result = runner
							.run(label.toString(),
									StringUtil
											.dencodeHTML(((ManualNode) nextNode).retracementScript));
					if (result != null && result instanceof Boolean) {
						if (((Boolean) result).booleanValue())
							isAllow = true;
					}
				}
			}
		}
		return isAllow;

	}

	/**
	 * 返回字符串内容为显示当前处理人。
	 * 
	 * @param doc
	 * 
	 * @return 字符串内容为显示当前处理人
	 * @throws Exception
	 */
	public String toCurrProcessorHtml(Document doc) throws Exception {
		StringBuffer buffer = new StringBuffer();

		Collection<String> nameList = toCurrProcessorList(doc);
		if (nameList != null && !nameList.isEmpty()) {
			buffer.append("(");
			for (Iterator<String> iterator = nameList.iterator(); iterator
					.hasNext();) {
				String name = (String) iterator.next();
				buffer.append(name + ",");
			}
			if (buffer.lastIndexOf(",") != -1) {
				buffer.deleteCharAt(buffer.lastIndexOf(","));
			}
			buffer.append(")");
		}

		return buffer.toString();
	}

	public static String toProcessorHtml(Document doc, WebUser webUser)
			throws Exception {

		JSONArray result = new JSONArray();
		if(!StringUtil.isBlank(doc.getStateid()) && !StringUtil.isBlank(doc.getStateLabel())){
			String stateLabelInfo = doc.getStateLabelInfo();
			JSONArray stateLabelInfoArray = JSONArray.fromObject(stateLabelInfo);
			
			for( Iterator<JSONObject> stateLabelInfo_iterator = stateLabelInfoArray.iterator(); stateLabelInfo_iterator.hasNext();){
				JSONObject stateLabelInfoJSON = stateLabelInfo_iterator.next();
				JSONArray nodes = stateLabelInfoJSON.getJSONArray("nodes");
				
				for( Iterator<JSONObject> nodes_iterator = nodes.iterator(); nodes_iterator.hasNext();){
					JSONObject node = nodes_iterator.next();
					
					/*兼容旧数据*/
					 if(!node.containsKey("auditors")){
						 doc.setStateLabelInfo("");
						 result.add(doc.getStateLabelInfo());
						 return result.toString() ;
					 }else{
						 result.add(doc.getStateLabelInfo());
						 return result.toString() ;
					 }
				}
			}
		}
		return result.toString() ;
	}
	
	@Deprecated
	public static String toProcessorHtml4OldSkin(Document doc, WebUser webUser)
			throws Exception {
		StringBuffer buffer = new StringBuffer();
		Collection<String> processorList = toCurrProcessorList(doc);
		String displayProcessor = "";
		StringBuffer displayPocessorStr = new StringBuffer();
		if (processorList.size() > 1) {
			if (processorList.contains(webUser.getName())) {
				for (Iterator<String> iter = processorList.iterator(); iter
						.hasNext();) {
					String name = (String) iter.next();
					if (!webUser.getName().equals(name)) {
						displayPocessorStr.append(name).append(", ");
					}
				}
				if (displayPocessorStr.toString().endsWith(" ")) {
					displayPocessorStr
							.setLength(displayPocessorStr.length() - 2);
				}
				displayProcessor = webUser.getName();
			} else {
				displayProcessor = (String) processorList.toArray()[0];
				Object[] processorArray = processorList.toArray();
				for (int i = 1; i < processorArray.length; i++) {
					String name = (String) processorArray[i];
					displayPocessorStr.append(name).append(", ");
				}
				if (displayPocessorStr.toString().endsWith(" ")) {
					displayPocessorStr
							.setLength(displayPocessorStr.length() - 2);
				}
			}
		} else if (processorList.size() == 1) {
			displayProcessor = (String) processorList.toArray()[0];
		}
		if (processorList != null && !processorList.isEmpty()) {
			buffer.append("<div class='divFormFlowCls' "
					+ " title='{*[cn.myapps.core.workflow.current_processor]*}:("
					+ displayProcessor
					+ ")'><span class='formFlowCls'>"
					+ "{*[cn.myapps.core.workflow.current_processor]*}:(<span onmouseover='displayProcessor()' onmouseout='displayProcessor()'>"
					+ displayProcessor + "</span>)</span></div>");
			buffer.append("<div id='processorDiv' style='display:none; position: absolute;width:260px;z-index:1100;'><span>{*[More]*}:("
					+ displayPocessorStr.toString() + ")</span></div>");
		}
		return buffer.toString();
	}

	public static Collection<String> toCurrProcessorList(Document doc)
			throws Exception {

		Collection<NodeRT> nodertList = getAllNodeRT(doc);
		Collection<String> processorList = new ArrayList<String>();
		for (Iterator<NodeRT> iter = nodertList.iterator(); iter.hasNext();) {
			NodeRT nodert = (NodeRT) iter.next();
			Collection<ActorRT> colls = nodert.getPendingActorRTList();
			Object[] actorrts = colls.toArray();
			if (actorrts.length > 0) {
				for (int i = 0; i < actorrts.length; i++) {
					String actorrtName = ((ActorRT) actorrts[i]).getName();
					processorList.add(actorrtName);
				}

			}
		}
		return processorList;
	}

	// 流程历史
	public static String toHistoryHtml(Document doc, int cellCount)
			throws Exception {
		String docid = doc.getId();
		String flowid = doc.getFlowid();
		String applicationid = doc.getApplicationid();
		FlowHistory his = new FlowHistory();
		if (!StringUtil.isBlank(docid) && !StringUtil.isBlank(flowid)
				&& !StringUtil.isBlank(applicationid)) {
			Collection<RelationHIS> colls = getAllRelationHIS(docid, flowid,
					applicationid);
			his.addAllHis(colls);
		}

		return his.toTextHtml();
	}

	// 流程历史
	public static String toHistoryXml(Document doc, int cellCount)
			throws Exception {
		Collection<RelationHIS> colls = getAllRelationHIS(doc.getId(),
				doc.getFlowid(), doc.getApplicationid());
		FlowHistory his = new FlowHistory();
		his.addAllHis(colls);

		return his.toTextXml();
	}

	/**
	 * 返回字符串为显示的流程状态标识。 此实现为通过Document id 与流程(flow) id
	 * 查询当前Document流程状态(flowStateRT)，并通过流程状态获取当前节点的状态标识（State label)
	 * 
	 * @returnf 字符串为显示的流程状态。
	 * @param doc
	 * @throws Exception
	 */
	public static String toFlowStateHtml(Document doc) throws Exception {
		StringBuffer buffer = new StringBuffer();
		FlowStateRT flowStateRT = doc.getState();
		Collection<String> flowStateList = toCurrProcessorList(doc);
		String displayflowState = "";
		StringBuffer displayflowStateStr = new StringBuffer();
		if (flowStateRT != null) {
			buffer.append("<span class='formFlowCls'>{*[cn.myapps.core.workflow.flow_state]*}:(<span>");

			if (!StringUtil.isBlank(flowStateRT.getStateLabel())) {
				buffer.append(StringUtil.dencodeHTML(flowStateRT
						.getStateLabel()));
			} else if (!StringUtil.isBlank(doc.getStateLabel())) {
				buffer.append(doc.getStateLabel());
			} else if (isDraft(doc)) {
				buffer.append("{*[" + FlowState.getName(FlowState.DRAFT)
						+ "]*}");
			} else {
				buffer.append("{*[" + FlowState.getName(flowStateRT.getState())
						+ "]*}");
			}
			buffer.append("</span>)</span>");
		}
		/*
		 * if (flowStateList != null && !flowStateList.isEmpty()) {
		 * buffer.append("{*[Current_flowState]*}:"); buffer .append(
		 * "<div style='cursor: pointer; position: relative; display: inline;' onMouseOver='displayProcessor()' onMouseOut='displayProcessor()'>(<span class='redColor'>"
		 * + displayflowState + "</span>)"); buffer .append(
		 * "<div id='processorDiv' style='display: none; width: 250px; padding-top: 16px; z-index: 99; position: absolute; left: 0px; top: 0px;'>{*[More]*}:(<span class='redColor'>"
		 * + displayflowStateStr.toString() + "</span>)</div>");
		 * buffer.append("</div>"); }
		 */
		LOG.debug("FlowState HTML: " + buffer.toString());
		return buffer.toString();
	}

	/**
	 * 判断是否为草稿状态
	 * 
	 * @param doc
	 * @param webUser
	 * 
	 * @return
	 * @throws Exception
	 */
	private static boolean isDraft(Document doc) throws Exception {
		Collection<Node> firstNodelist = StateMachine.getFirstNodeList(doc);

		if (firstNodelist != null && firstNodelist.size() > 0) {
			Collection<NodeRT> nodertlist = StateMachine.getAllNodeRT(
					doc.getId(), doc.getStateid(), doc.getApplicationid());

			if (nodertlist != null && nodertlist.size() > 0) {
				for (Iterator<NodeRT> iter = nodertlist.iterator(); iter
						.hasNext();) {
					NodeRT nodert = (NodeRT) iter.next();
					if (isContains(firstNodelist, nodert)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean isContains(Collection<Node> firstNodes, NodeRT nodert) {
		for (Iterator<Node> iter = firstNodes.iterator(); iter.hasNext();) {
			Node firstNode = (Node) iter.next();
			if (firstNode.id.equals(nodert.getNodeid())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取当前节点负责人列表
	 * 
	 * @param docid
	 *            文档ID
	 * @param flowid
	 *            流程ID
	 * @param nodeid
	 *            节点ID
	 * @return
	 * @throws Exception
	 */
	public static Collection<BaseUser> getPrincipalList(String docid,
			WebUser user, String nodeid, HttpServletRequest request,
			String flowid) throws Exception {

		BillDefiProcess process = (BillDefiProcess) ProcessFactory
				.createProcess(BillDefiProcess.class);
		BillDefiVO flowVO = (BillDefiVO) process.doView(flowid);

		DocumentProcess docProcess = (DocumentProcess) ProcessFactory
				.createRuntimeProcess(DocumentProcess.class,
						flowVO.getApplicationid());
		Document doc = (Document) user.getFromTmpspace(docid);
		if (doc == null) {
			doc = (Document) docProcess.doView(docid);
		}

		ParamsTable params = ParamsTable.convertHTTP(request);
		params.setParameter("docid", docid);
		request.setAttribute("content", doc);
		params.setHttpRequest(request);

		if (doc != null && doc.getStateid() != null) {
			flowVO = doc.getState().getFlowVO();
			Node nextNode = flowVO.findNodeById(nodeid);
			if (nextNode instanceof SubFlow) {
				SubFlow subFlowNode = ((SubFlow) nextNode);
				String subFlowFlowId = null;
				IRunner runner = JavaScriptFactory.getInstance(
						params.getSessionid(), doc.getApplicationid());
				runner.initBSFManager(doc, params, user,
						new ArrayList<ValidateMessage>());
				if (SubFlow.SUBFLOW_DEFINITION_CUSTOM
						.equals(subFlowNode.subFlowDefiType)) {
					subFlowFlowId = subFlowNode.subflowid;
				} else if (SubFlow.SUBFLOW_DEFINITION_SCRIPT
						.equals(subFlowNode.subFlowDefiType)
						&& !StringUtil.isBlank(subFlowNode.subflowScript)) {
					Object result = runner.run("subFlow:" + subFlowNode.name
							+ " subFlowScript",
							StringUtil.dencodeHTML(subFlowNode.subflowScript));
					if (result != null)
						subFlowFlowId = String.valueOf(result);

				}
				flowVO = (BillDefiVO) process.doView(subFlowFlowId);
				nextNode = flowVO.getFirstNode(doc,params,user);
			}
			return StateMachineUtil.getPrincipalList(doc, params, nextNode,
					user.getDomainid(), flowVO.getApplicationid(), user);
		}

		return new ArrayList<BaseUser>();
	}

	/**
	 * 获取当前节点负责人列表
	 * 
	 * @param docid
	 *            文档ID
	 * @param flowid
	 *            流程ID
	 * @param nodeid
	 *            节点ID
	 * @return
	 * @throws Exception
	 */
	public static String getPrincipalString(String docid, WebUser user,
			String nodeid, HttpServletRequest request, String flowid, int type,
			String id) throws Exception {

		StringBuffer html = new StringBuffer();
		RoleProcess roleProcess = (RoleProcess) ProcessFactory
				.createProcess(RoleProcess.class);
		DepartmentProcess deptProcess = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);
		try {
			Collection<BaseUser> users = getPrincipalList(docid, user, nodeid,
					request, flowid);

			int total = 0;

			switch (type) {
			case Type.TYPE_USER:
				for (Iterator<BaseUser> iter2 = users.iterator(); iter2
						.hasNext();) {
					UserVO tempUser = (UserVO) iter2.next();
					if (id != null && !"".equals(id)) {
						if (!tempUser.getName().contains(id)) {
							continue;
						}
					}
					toUserDiv(html, tempUser);
					total++;
				}
				break;
			case Type.TYPE_ROLE:
				Collection<NameNode> nameNodeList = getNodeList(docid, user,
						nodeid, request, flowid);
				if (nameNodeList == null) {
					RoleVO tempRole = (RoleVO) roleProcess.doView(id);
					if(tempRole != null && RoleVO.STATUS_VALID == tempRole.getStatus()){
						// 查找对应角色的用户
						for (Iterator<BaseUser> iter2 = users.iterator(); iter2
								.hasNext();) {
							UserVO tempUser = (UserVO) iter2.next();
							if (tempRole.getUsers().contains(tempUser)) {
								toUserDiv(html, tempUser);
								total++;
							}
						}
					}
				} else {
					Collection<UserVO> filterUser = new ArrayList<UserVO>();
					for (Iterator iter = nameNodeList.iterator(); iter
							.hasNext();) {
						NameNode nameNode = (NameNode) iter.next();
						if (nameNode.getType() == Type.TYPE_ROLE) {
							String actorId = nameNode.getId();
							if (!actorId.equals(id)) {
								continue;
							}
						}
						RoleVO tempRole = (RoleVO) roleProcess.doView(id);
						// 查找对应角色的用户
						for (Iterator<BaseUser> iter2 = users.iterator(); iter2
								.hasNext();) {
							UserVO tempUser = (UserVO) iter2.next();
							if (tempRole.getUsers().contains(tempUser)) {
								filterUser.add(tempUser);
							}
						}
					}
					for (Iterator iter3 = filterUser.iterator(); iter3
							.hasNext();) {
						UserVO tempUser = (UserVO) iter3.next();
						toUserDiv(html, tempUser);
						total++;
					}
				}
				break;
			case Type.TYPE_DEPARTMENT:
				Collection<NameNode> nameNodeList2 = getNodeList(docid, user,
						nodeid, request, flowid);
				if (nameNodeList2 == null) {
					DepartmentVO dept = (DepartmentVO) deptProcess.doView(id);
					for (Iterator<BaseUser> iter2 = users.iterator(); iter2
							.hasNext();) {
						UserVO tempUser = (UserVO) iter2.next();
						if (dept.getUsers().contains(tempUser)) {
							toUserDiv(html, tempUser);
							total++;
						}
					}
				} else {
					Set<UserVO> filterUser = new HashSet<UserVO>();
					for (Iterator iter = nameNodeList2.iterator(); iter
							.hasNext();) {
						NameNode nameNode = (NameNode) iter.next();
						if (nameNode.getType() == Type.TYPE_DEPARTMENT) {
							String actorId = nameNode.getId();
							if (!actorId.equals(id)) {
								continue;
							}
						}
						DepartmentVO dept = (DepartmentVO) deptProcess
								.doView(id);
						for (Iterator<BaseUser> iter2 = users.iterator(); iter2
								.hasNext();) {
							UserVO tempUser = (UserVO) iter2.next();
							if (dept.getUsers().contains(tempUser)) {
								filterUser.add(tempUser);
							}
						}
					}
					for (Iterator iter3 = filterUser.iterator(); iter3
							.hasNext();) {
						UserVO tempUser = (UserVO) iter3.next();
						toUserDiv(html, tempUser);
						total++;
					}
				}
				break;
			default:
				break;
			}
			if (total > 0) {
				getPageDiv(html, total);
			}
		} catch (Exception e) {
		}

		String data = StringUtil.dencodeHTML(html.toString());

		return data;
	}

	private static Collection<NameNode> getNodeList(String docid, WebUser user,
			String nodeid, HttpServletRequest request, String flowid)
			throws Exception {
		BillDefiProcess process = (BillDefiProcess) ProcessFactory
				.createProcess(BillDefiProcess.class);
		BillDefiVO flowVO = (BillDefiVO) process.doView(flowid);

		DocumentProcess docProcess = (DocumentProcess) ProcessFactory
				.createRuntimeProcess(DocumentProcess.class,
						flowVO.getApplicationid());
		Document doc = (Document) user.getFromTmpspace(docid);
		if (doc == null) {
			doc = (Document) docProcess.doView(docid);
		}

		ParamsTable params = ParamsTable.convertHTTP(request);
		params.setParameter("docid", docid);
		request.setAttribute("content", doc);
		params.setHttpRequest(request);
		Node nextNode = null;
		if (doc != null && doc.getStateid() != null) {
			flowVO = doc.getState().getFlowVO();
			nextNode = flowVO.findNodeById(nodeid);
			if (nextNode instanceof SubFlow) {
				SubFlow subFlowNode = ((SubFlow) nextNode);
				String subFlowFlowId = null;
				IRunner runner = JavaScriptFactory.getInstance(
						params.getSessionid(), doc.getApplicationid());
				runner.initBSFManager(doc, params, user,
						new ArrayList<ValidateMessage>());
				if (SubFlow.SUBFLOW_DEFINITION_CUSTOM
						.equals(subFlowNode.subFlowDefiType)) {
					subFlowFlowId = subFlowNode.subflowid;
				} else if (SubFlow.SUBFLOW_DEFINITION_SCRIPT
						.equals(subFlowNode.subFlowDefiType)
						&& !StringUtil.isBlank(subFlowNode.subflowScript)) {
					Object result = runner.run("subFlow:" + subFlowNode.name
							+ " subFlowScript",
							StringUtil.dencodeHTML(subFlowNode.subflowScript));
					if (result != null)
						subFlowFlowId = String.valueOf(result);

				}
				flowVO = (BillDefiVO) process.doView(subFlowFlowId);
				nextNode = flowVO.getFirstNode(doc,params,user);
			}
		}
		ManualNode mNode = (ManualNode) nextNode;
		Collection<NameNode> nameNodeList = null;
		switch (mNode.actorEditMode) {
		case ManualNode.ACTOR_EDIT_MODE_CODE:
			break;
		case ManualNode.ACTOR_EDIT_MODE_DESIGN:
			NameList nameList = NameList.parser(mNode.namelist);
			nameNodeList = nameList.toNameNodeCollection();
			break;
		case ManualNode.ACTOR_EDIT_MODE_USER_DESIGN:
			NameList userList = NameList.parser(mNode.userList);
			nameNodeList = userList.toNameNodeCollection();
			break;
		case ManualNode.ACTOR_EDIT_MODE_ORGANIZATION_DESIGN:
			break;
		default:
			break;
		}
		return nameNodeList;
	}

	private static void toUserDiv(StringBuffer html, BaseUser user) {
		
		String avatar = "";
		if(!StringUtil.isBlank(user.getAvatar())){
			JSONObject json = JSONObject.fromObject(user.getAvatar());
			avatar = json.getString("url");
		}
		
		html.append("<div class='list_div_user' title='" + user.getName()
				+ "'>");

		html.append("<input class='list_div_click' type='checkbox' name='"
					+ user.getName()
					+ "' id='"
					+ user.getId()
					+ "' avatar='"
					+ avatar
					+ "' telephone='"
					+ user.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
		html.append("<span onclick='"
				+ "jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);'>"
				+ user.getName() + "</span>");
		html.append("</div>");
	}

	private static void getPageDiv(StringBuffer html, int total) {
		long pagelines = 10;

		int pageCount = (int) Math.ceil((double) total / pagelines);

		html.append("<div style='padding:5px;border-bottom:1px solid gray;'>");

		html.append("<a id='first_page' style='cursor: pointer;color:#316AC5;' onclick='doPageNav("
				+ "1)'>{*[FirstPage]*}</a>&nbsp;");
		html.append("<a id='prev_page' style='cursor: pointer;color:#316AC5;' onclick='doPageNav("
				+ "1)'>{*[PrevPage]*}</a>&nbsp;");

		html.append("<a id='next_page' style='cursor: pointer;color:#316AC5;' onclick='doPageNav("
				+ "2)'>{*[NextPage]*}</a>&nbsp;");
		html.append("<a id='end_page' style='cursor: pointer;color:#316AC5;' onclick='doPageNav("
				+ pageCount + ")'>{*[EndPage]*}</a>&nbsp;");

		if (pageCount != 1) {
			html.append("<a id='all_page' style='cursor: pointer;color:#0000E3;' onclick='doAllPageNav("
					+ "0)'> {*[all]*}</a>&nbsp;");

			html.append("<span id='all_page_hide'>{*[InPage]*}1")
					.append("{*[Page]*}/{*[Total]*}1")
					.append("{*[Pages]*}</span>&nbsp;");
		}

		html.append("<span id='all_page_show'>{*[InPage]*}<span id='in_page'>1")
				.append("</span>{*[Page]*}/{*[Total]*}<span id='total_page'>")
				.append(pageCount).append("</span>{*[Pages]*}</span>&nbsp;");

		html.append("</div>");
	}

	/**
	 * 获取节点的流程抄送人
	 * 
	 * @param docid
	 * @param user
	 * @param nodeid
	 * @param request
	 * @param flowid
	 * @return
	 * @throws Exception
	 */
	public static Collection<BaseUser> getCirculatorList(String docid,
			WebUser user, String nodeid, HttpServletRequest request,
			String flowid) throws Exception {
		BillDefiProcess process = (BillDefiProcess) ProcessFactory
				.createProcess(BillDefiProcess.class);
		BillDefiVO flowVO = (BillDefiVO) process.doView(flowid);

		DocumentProcess docProcess = (DocumentProcess) ProcessFactory
				.createRuntimeProcess(DocumentProcess.class,
						flowVO.getApplicationid());
		Document doc = (Document) user.getFromTmpspace(docid);
		if (doc == null) {
			doc = (Document) docProcess.doView(docid);
		}
		flowVO = doc.getState().getFlowVO();

		ParamsTable params = ParamsTable.convertHTTP(request);
		params.setParameter("docid", docid);
		request.setAttribute("content", doc);
		params.setHttpRequest(request);

		if (doc != null && flowVO != null) {
			Node nextNode = flowVO.findNodeById(nodeid);
			// 注：判断流程如果有前台手动调整过的，则使用调整后的节点去获取抄送人列表
			if (!StringUtil.isBlank(doc.getState().getFlowXML())) {
				BillDefiVO fv = doc.getState().getFlowVO();
				FlowDiagram fd = fv.toFlowDiagram();
				nextNode = (Node) fd.getElementByID(nodeid);
			}
			return StateMachineUtil.getCirculatorList(params, doc, nextNode,
					doc.getDomainid(), doc.getApplicationid());
		}
		return new ArrayList<BaseUser>();
	}

	/**
	 * 获取文档的其他节点(除当前节点和开始节点)
	 * 
	 * @param docId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getOtherNodeMap(String docId,
			String applicationid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "{*[Select]*}");
		if (docId == null || docId.trim().length() <= 0)
			return map;

		DocumentProcess process = (DocumentProcess) ProcessFactory
				.createRuntimeProcess(DocumentProcess.class, applicationid);
		Document doc = (Document) process.doView(docId);
		if (doc != null) {
			Collection<Node> nodes = getOtherNodeList(doc);
			for (Node node : nodes) {
				map.put(node.id, node.statelabel);
			}
		}

		return map;

	}

	/**
	 * 获取文档的其他节点(除当前节点和开始节点)
	 * 
	 * @param docId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Collection<Node> getOtherNodeList(String docId, String applicationid)
			throws Exception {
		if (docId == null || docId.trim().length() <= 0)
			return new ArrayList<Node>();

		DocumentProcess process = (DocumentProcess) ProcessFactory
				.createRuntimeProcess(DocumentProcess.class, applicationid);
		Document doc = (Document) process.doView(docId);
		if (doc != null) {
			return getOtherNodeList(doc);
		}
		return new ArrayList<Node>();
	}

	/**
	 * 获取文档的其他节点(除当前节点和开始节点)
	 * 
	 * @param doc
	 *            文档
	 * @param user
	 *            用户
	 * @return
	 * @throws Exception
	 * @author Happy
	 */
	public Collection<Node> getOtherNodeList(Document doc) throws Exception {
		Collection<Node> rtn = new ArrayList<Node>();
		// rtn.add(null);
		if (doc != null) {
			BillDefiVO flowVO = doc.getFlowVO();
			Collection<NodeRT> nodert = null;
			FlowDiagram fd = null;
			if (flowVO != null) {
				fd = flowVO.toFlowDiagram();
				nodert = doc.getState().getNoderts();
			}

			if (nodert != null) {
				StringBuffer currnodeids = new StringBuffer();
				for (NodeRT rt : nodert) {
					currnodeids.append(rt.getNodeid()).append(",");
				}
				Vector<Element> allElemets = fd.getAllElements();

				for (Enumeration<Element> e = allElemets.elements(); e
						.hasMoreElements();) {
					Element elem = (Element) e.nextElement();
					if (elem instanceof Relation
							|| currnodeids.toString().indexOf(elem.id) > -1
							|| elem instanceof StartNode) {
						// allElemets.remove(elem);// delete Relation,StartNode
						// and currentNode
					} else {
						rtn.add((Node) elem);
					}
				}

			}
		}

		return rtn;
	}

	/**
	 * 会签时,判断是否允许指定审批人
	 * 
	 * @param currnode
	 * @param nodert
	 * @return
	 * @throws Exception
	 */
	private boolean isAllowToPerson(ManualNode currnode, NodeRT nodert)
			throws Exception {
		if (Integer.parseInt(currnode.passcondition) == ManualNode.PASS_CONDITION_AND
				|| Integer.parseInt(currnode.passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND) {
			if (nodert.getAllActorrts().size() > 1) {
				return false;// 会签模式,当前节点处理人不是最后一个处理人时,不允许指定审批人
			}
		}
		return true;
	}

}
