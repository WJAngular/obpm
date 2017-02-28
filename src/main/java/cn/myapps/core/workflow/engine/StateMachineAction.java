package cn.myapps.core.workflow.engine;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.action.DocumentAction;
import cn.myapps.core.dynaform.document.action.DocumentHelper;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.OGraphics;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowHistoryVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;

public class StateMachineAction extends DocumentAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7970496758364995341L;

	/**
	 * 下一个节点数组
	 */
	private String[] _nextids;

	/**
	 * 当前节点id
	 */
	private String _currid;

	private String _flowType;

	private String _attitude;

	/*
	 * 用户所选择地提交对象{node0:u1;u2,node1:u1;u3}
	 */
	private String submitTo;

	public String getSubmitTo() {
		return submitTo;
	}

	public void setSubmitTo(String submitTo) {
		this.submitTo = submitTo;
	}

	public String get_attitude() {
		return _attitude;
	}

	public void set_attitude(String _attitude) {
		this._attitude = _attitude;
	}

	public String get_flowType() {
		return _flowType;
	}

	public void set_flowType(String type) {
		_flowType = type;
	}

	public String get_currid() {
		return _currid;
	}

	public void set_currid(String _currid) {
		this._currid = _currid;
	}

	public String[] get_nextids() {
		return _nextids;
	}

	public void set_nextids(String[] _nextids) {
		this._nextids = _nextids;
	}

	public StateMachineAction() throws Exception {

	}

	public String doViewFlow() throws Exception {
		return SUCCESS;
	}
	
	public String doRetracement() throws Exception{
		ParamsTable params = getParams();
		WebUser user = getUser();
		DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, getApplication());
		Document doc = (Document) docProcess.doView(params.getParameterAsString("_docid"));
		if (user.getStatus() == 1) {
			try {
				doc = DocumentHelper.rebuildDocument(doc, params,getUser());
				DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,doc.getApplicationid());
				BillDefiVO flowVO = doc.getState().getFlowVO();
				FlowDiagram fd = flowVO.toFlowDiagram();
				
				
				Node currNode = null;
				if(doc.getState().isComplete()){
					RelationHISProcess procss = (RelationHISProcess)ProcessFactory.createRuntimeProcess(RelationHISProcess.class, doc.getApplicationid());
					RelationHIS his = procss.getCompleteRelationHIS(doc.getId(), doc.getState().getId());
					currNode = (Node) fd.getElementByID(his.getEndnodeid());
					
				}else if(doc.getState().getNoderts()!=null && doc.getState().getNoderts().size()>0){
					NodeRT nodert = (NodeRT) doc.getState().getNoderts().iterator().next();
					currNode = (Node) fd.getElementByID(nodert.getNodeid());
				}
				
				Node nextNode = StateMachine.getBackNodeByHis(doc, flowVO, currNode.id, user, FlowState.RUNNING);
				if (nextNode != null) {

					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), getApplication());
					runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());

					boolean allowRetracement = false;
					if (((ManualNode) nextNode).retracementEditMode == 0 && ((ManualNode) nextNode).cRetracement) {
						allowRetracement = true;
					} else if (((ManualNode) nextNode).retracementEditMode == 1
							&& ((ManualNode) nextNode).retracementScript != null
							&& (((ManualNode) nextNode).retracementScript).trim().length() > 0) {
						StringBuffer label = new StringBuffer();
						label.append(doc.getFormname()).append(".Activity(").append(params.getParameter("_activityid"))
								.append("流程回撤").append(".retracementScript").append(")");
						String script = StringUtil.dencodeHTML(((ManualNode) nextNode).retracementScript);
						Object result = runner.run(label.toString(), script);
						if (result != null && result instanceof Boolean) {
							if (((Boolean) result).booleanValue())
								allowRetracement = true;
						}
					}

					if (allowRetracement) {
						Collection<Node> fromNode = new ArrayList<Node>();
						if(((ManualNode) nextNode).issplit){
							
							Collection<NodeRT> noderts = doc.getState().getNoderts();
							Collection<Node> nextNodeList = fd.getNextNodeList(nextNode.id,doc,params,user);
							if(!nextNodeList.isEmpty() && !noderts.isEmpty()){
								for (Iterator<Node> iterator = nextNodeList.iterator(); iterator
										.hasNext();) {
									Node node = (Node) iterator.next();
									for (Iterator<NodeRT> iter = noderts
											.iterator(); iter.hasNext();) {
										NodeRT rt = iter.next();
										if(node.id.equals(rt.getNodeid())){
											fromNode.add(node);
											break;
										}
										
									}
									
								}
							}
							
						}else{
							fromNode.add(currNode);
						}
						
					for (Iterator<Node> iterator = fromNode.iterator(); iterator
						.hasNext();) {
						Node node = (Node) iterator.next();
						
						// 指的审批人
						String submitTo = "[{\"nodeid\":'" + nextNode.id + "',\"isToPerson\":'true',\"userids\":\"['"
								+ user.getId() + "']\"},]";
						params.setParameter("submitTo", submitTo);

						String[] nextids = { nextNode.id };
						doc = ((DocumentProcess) proxy).doFlow(doc, params, node.id, nextids,
								FlowType.RUNNING2RUNNING_RETRACEMENT, params.getParameterAsString("_attitude"), user);
						
						String _targetNode = params.getParameterAsString("_targetNode");
						
						//设置审批节点
						if(!StringUtil.isBlank(doc.getStateid())){
							String defaultNodeId = null;
							if(!StringUtil.isBlank(_targetNode)){
								defaultNodeId = _targetNode;
							}
							NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user,defaultNodeId);
							if(nodert !=null) {
								set_targetNode(nodert.getNodeid());
								ServletActionContext.getRequest().setAttribute("_targetNodeRT", nodert);
							}
						}
						setContent(doc);
						MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
					}
					} else {
						this.addFieldError("System Error", "此流程状态下不允许回撤");
						return INPUT;
					}
				} else {
					this.addFieldError("System Error", "您没有回撤的权限");
					return INPUT;
				}
			} catch (OBPMValidateException e) {
				this.addFieldError("System Error", e.getValidateMessage());
				return INPUT;
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException(e.getMessage(), e));
				e.printStackTrace();
				return INPUT;
			}
			return SUCCESS;
		} else {
			this.addFieldError("System Error", "{*[core.user.noeffectived]*}");
			return INPUT;
		}
	}
	
	/**
	 * 流程加签
	 * @return
	 * @throws Exception
	 */
	public String doAddAuditor() throws Exception{
		
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, getApplication());
			
			String formid = params.getParameterAsString("_formid");
			String docid = (String) ServletActionContext.getRequest().getAttribute("content.id");
			String parentId = params.getParameterAsString("parentid");
			
			if (!StringUtil.isBlank(formid)) {
				FormProcess formPross = (FormProcess) ProcessFactory
						.createProcess(FormProcess.class);
				Form form = (Form) formPross.doView(formid);
				
				Document doc = new Document();
				if (!StringUtil.isBlank(docid)) {
					doc.setId(docid);
					doc = form
							.createDocumentWithSystemField(params, doc, getUser());
				} else if (!StringUtil.isBlank(parentId)) {
					doc = (Document) getUser().getFromTmpspace(parentId);
				}
			
			
			if(doc != null){
				doc = DocumentHelper.rebuildDocument(doc, params,user);
				doc.setLastmodifier(user.getId());
				docProcess.doChangeAuditor(doc, params, user);
				doc.setEditAbleLoaded(false);
				this.setContent(doc);
				MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
				this.addActionMessage("{*[cn.myapps.core.dynaform.document.success_to_edit_auditor]*}");
			}
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			if((e.getCause() instanceof ImpropriateException)){
				//加载数据库中最新的文档到上下文环境
				this.setContent(getProcess().doView(this.getContent().getId()));
				MemoryCacheUtil.putToPrivateSpace(this.getContent().getId(), this.getContent(), getUser());
			}else{
				e.printStackTrace();
				}
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException(e.getMessage(), e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * 编辑审批人
	 * @return
	 * @throws Exception
	 */
	public String doEditAuditor() throws Exception{
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, getApplication());
			
			String formid = params.getParameterAsString("_formid");
			String docid = (String) ServletActionContext.getRequest().getAttribute("content.id");
			String parentId = params.getParameterAsString("parentid");
			
			if (!StringUtil.isBlank(formid)) {
				FormProcess formPross = (FormProcess) ProcessFactory
						.createProcess(FormProcess.class);
				Form form = (Form) formPross.doView(formid);
				
				Document doc = (Document) docProcess.doView(docid);
				if(doc == null) doc = new Document();
				if (!StringUtil.isBlank(docid)) {
					doc.setId(docid);
					doc = form
							.createDocumentWithSystemField(params, doc, getUser());
				} else if (!StringUtil.isBlank(parentId)) {
					doc = (Document) getUser().getFromTmpspace(parentId);
				}
			
			
				if(doc != null){
					doc = DocumentHelper.rebuildDocument(doc, params,user);
					doc.setLastmodifier(user.getId());
					doc.setDomainid(this.getDomain());
					docProcess.doChangeAuditor(doc, params, user);
					doc.setEditAbleLoaded(false);
					this.setContent(doc);
					MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
					this.addActionMessage("{*[cn.myapps.core.dynaform.document.success_to_edit_auditor]*}");
				}
			}
			} catch (OBPMValidateException e) {
				this.addFieldError("1", e.getValidateMessage());
				if((e.getCause() instanceof ImpropriateException)){
					//加载数据库中最新的文档到上下文环境
					this.setContent(getProcess().doView(this.getContent().getId()));
					MemoryCacheUtil.putToPrivateSpace(this.getContent().getId(), this.getContent(), getUser());
				}else{
					e.printStackTrace();
					}
				return INPUT;
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException(e.getMessage(), e));
				e.printStackTrace();
				return INPUT;
			}
			return SUCCESS;
			
	}
	
	/**
	 * 终止流程
	 * @return
	 * @throws Exception
	 */
	public String doTerminateFlow() throws Exception {
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String formid = params.getParameterAsString("_formid");
			String docid = (String) ServletActionContext.getRequest().getAttribute("content.id");
			String parentId = params.getParameterAsString("parentid");
			
			if (!StringUtil.isBlank(formid)) {
				FormProcess formPross = (FormProcess) ProcessFactory
						.createProcess(FormProcess.class);
				Form form = (Form) formPross.doView(formid);
				
				Document doc = new Document();
				if (!StringUtil.isBlank(docid)) {
					doc.setId(docid);
					doc = form
							.createDocumentWithSystemField(params, doc, getUser());
				} else if (!StringUtil.isBlank(parentId)) {
					doc = (Document) getUser().getFromTmpspace(parentId);
				}
			
			
				if(doc != null){
					doc = DocumentHelper.rebuildDocument(doc, params,getUser());
					FlowStateRT instance = doc.getState();
					FlowDiagram fd = instance.getFlowVO().toFlowDiagram();
					
					boolean update = false;
					Node currNode = (Node) fd.getElementByID(params.getParameterAsString("_currid"));
					Node endNode = new Node(fd) {
						
						public void paintMobile(OGraphics g) {
						}
						public void showTips(Graphics g) {
						}
						public void paint(OGraphics g) {
						}
					};
					String label = "终止";
					endNode.name = label;
					endNode.id= "terminate_id";
					NodeRTProcess nodeRTProcess = (NodeRTProcess) ProcessFactory
							.createRuntimeProcess(NodeRTProcess.class, doc.getApplicationid());
					
					if(!instance.isComplete() && !instance.isTerminated()){
						if(instance.getNoderts()!=null && instance.getNoderts().size()>0){
							for (Iterator<?> iterator = instance.getNoderts().iterator(); iterator
									.hasNext();) {
								NodeRT nodert = (NodeRT) iterator.next();
								nodeRTProcess.doRemove(nodert.getId());
							}
							instance.getNoderts().clear();
							update = true;
						}
					}
					
					if(update){
						Date actionTime = new Date();
						String attitude = params.getParameterAsString("_attitude");
						//更新流程实例
						this.updateFlowStateRT(doc,instance,user,label,actionTime);
						//更新流程历史
						StateMachine.updatePreviousRelationHIS(instance, currNode, user, actionTime);
						StateMachine.createOrUpdateRelationHIS(instance, currNode, endNode, user, actionTime, attitude,params.getParameterAsString("_signature"), FlowType.RUNNING2TERMIATE);
						//更新文档
						this.updateDocument(doc, user, label, actionTime);
						//更新图片
						StateMachine.updateImage(instance);
					}
					this.setContent(doc);
					MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
					this.addActionMessage("{*[成功终止流程]*}");
				}
			}
			} catch (OBPMValidateException e) {
				this.addFieldError("1", e.getValidateMessage());
				if((e.getCause() instanceof ImpropriateException)){
					//加载数据库中最新的文档到上下文环境
					this.setContent(getProcess().doView(this.getContent().getId()));
					MemoryCacheUtil.putToPrivateSpace(this.getContent().getId(), this.getContent(), getUser());
				}else{
					e.printStackTrace();
					}
				return INPUT;
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException(e.getMessage(), e));
				e.printStackTrace();
				return INPUT;
			}
			return SUCCESS;
		
	}
	
	/**
	 * 显示流程历史
	 * @return
	 */
	public String showFlowHistory(){
		ParamsTable params = getParams();
		
		String flowStateId = params.getParameterAsString("flowStateId");
		String flowName = "";
		boolean isComplete = false;
		List<FlowHistoryVO> historys = Collections.EMPTY_LIST;
		try {
			FlowStateRTProcess flowStateRTProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, getApplication());
			RelationHISProcess hisProcess = (RelationHISProcess) ProcessFactory.createRuntimeProcess(RelationHISProcess.class,getApplication());
			
			FlowStateRT flowInstance = (FlowStateRT)flowStateRTProcess.doView(flowStateId);
			
			if(flowInstance != null){
				flowName = flowInstance.getFlowName();
				historys = (List)hisProcess.getFlowHistorysByFolowStateId(flowStateId);
				Collections.reverse(historys);
				if(flowInstance.isComplete()){
					isComplete = true;
				}
			}
			
			ServletActionContext.getRequest().setAttribute("flowName", flowName);
			ServletActionContext.getRequest().setAttribute("historys", historys);
			ServletActionContext.getRequest().setAttribute("isComplete", isComplete);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String doRefreshFlowPanelHTML(){
		try {
			String htmlText = "";
			ParamsTable params = getParams();
			HttpServletRequest request = ServletActionContext.getRequest();
			String formid = params.getParameterAsString("formid");
			String docid = params.getParameterAsString("docid");
			String stateid = params.getParameterAsString("stateid");
			String userid = params.getParameterAsString("userid");
			String flowid = params.getParameterAsString("flowid");
			String actionType = params.getParameterAsString("actionType");
			
			WebUser user = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
			if (user == null) {
				UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				user = up.getWebUserInstance(userid);
			}
			
			if (user != null) {
				try {
						FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
						Form form = (Form) fp.doView(formid);

						DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,form.getApplicationid());
						
						// 从数据库中获取Document
						Document doc = (Document) dp.doView(docid);
						
						if (doc == null) {
							// 从Session中获取Document
							doc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, user);
						}
						if (doc == null) {
							doc = form.createDocument(params, user);
							
							//docid不为空时,设为原来的id,解决因浏览器页签切换后包含元素内容丢失
							if(!StringUtil.isBlank(docid))
								doc.setId(docid);
						}
						doc.setState(stateid);
						form.addItems(doc, params);
						//form.recalculateDocument(doc, params, false, user);//出于性能优化考虑，不执行重计算

						IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), form.getApplicationid());
						runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());

						if (user != null && doc != null && doc.getId() != null) {
							MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
						}

						//当文档为新建时，没有state则需要添加一个
						if (doc.getIstmp() || StringUtil.isBlank(doc.getStateid())) {
							if (!StringUtil.isBlank(flowid) && !"null".equals(flowid)) {
								FlowStateRTProcess stateProcess = (FlowStateRTProcess) ProcessFactory
										.createRuntimeProcess(FlowStateRTProcess.class, doc
												.getApplicationid());
								
								BillDefiProcess billDefiProcess = (BillDefiProcess) ProcessFactory
										.createProcess(BillDefiProcess.class);
								
								BillDefiVO flowVO = (BillDefiVO) billDefiProcess.doView(flowid);
								Node firstNode = StateMachine.getFirstNode(doc, flowVO, user,
										params);

								if (firstNode != null) {
									FlowStateRT state = stateProcess.createTransientFlowStateRT(doc,
											flowid, user);
									ArrayList<NodeRT> noderts = new ArrayList<NodeRT>();
									NodeRT nodert = new NodeRT(state, firstNode, FlowType.START2RUNNING);
									noderts.add(nodert);
									state.setNoderts(noderts);

									doc.setState(state);// 创建瞬态流程实例
									doc.setInitiator(user.getId());// 设置流程发起人
									Node startNode = StateMachine.getStartNodeByFirstNode(flowVO,
											firstNode);
									if (startNode != null) {
										// 设置上下文当前处理节点对象
										request.setAttribute("_targetNode",  firstNode.id);
										request.setAttribute("_targetNodeRT", nodert);
									}
								} 
							}
						}
						
						StateMachineHelper helper = new StateMachineHelper(
								doc);
						
						if ("init".equals(actionType)) {
							htmlText = helper.toFlowHtmlTextForInit(doc, user,
									"",request);
						}
						else if ("commitTo".equals(actionType)) {
							htmlText = helper.toFlowHtmlTextForCommitTo(doc, user,
									"",request);
						} 
						else if ("returnTo".equals(actionType)) {
							htmlText = helper.toFlowHtmlTextForReturnTo(doc, user,
									"",request);
						}
						ServletActionContext.getRequest().setAttribute("result",htmlText);
//					}
				} catch (Exception e) {
					throw e;
				}

			} else {
				throw new OBPMValidateException("无法从Session中获取User");
			}
			
		} catch (Exception e) {
		}
		
		return SUCCESS;
	}
	
	private void updateFlowStateRT(Document doc, FlowStateRT instance,
			WebUser user, String stateLabel, Date actionTime) throws Exception {
		instance.setAuditdate(actionTime);
		instance.setAuditorList("");
		instance.setAuditorNames("");
		instance.setAudituser(user.getId());
		instance.setTerminated(true);
		instance.setLastFlowOperation(FlowType.RUNNING2TERMIATE);
		instance.setLastModified(actionTime);
		instance.setLastModifierId(user.getId());
		instance.setNoderts(new ArrayList<NodeRT>());
		instance.setStateLabel(stateLabel);
		StateMachine.getFlowStateRTProcess(doc.getApplicationid()).doUpdate(instance);
	}
	
	private void updateDocument(Document doc, WebUser user, String stateLabel,
			Date actionTime) throws Exception {
		DocumentProcess proxy = (DocumentProcess) ProcessFactory
				.createRuntimeProcess(DocumentProcess.class, doc.getApplicationid());
		doc.setAuditdate(actionTime);
		doc.setAuditorList("");
		doc.setAuditorNames("");
		doc.setAudituser(user.getId());
		doc.setLastFlowOperation(FlowType.RUNNING2TERMIATE);
		doc.setLastmodified(actionTime);
		doc.setLastmodifier(user.getId());
		doc.setStateLabel(stateLabel);
		proxy.doCreateOrUpdate(doc, user);
	}

}
