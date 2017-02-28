package cn.myapps.core.workflow.storage.definition.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.VersionSupport;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.element.AutoNode;
import cn.myapps.core.workflow.element.Element;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.Relation;
import cn.myapps.core.workflow.element.StartNode;
import cn.myapps.core.workflow.element.SubFlow;
import cn.myapps.core.workflow.engine.State;
import cn.myapps.core.workflow.engine.WFRunner;
import cn.myapps.core.workflow.engine.state.StateCreator;
import cn.myapps.util.StringUtil;

/**
 * @hibernate.class table="T_BILLDEFI"
 */
public class BillDefiVO extends VersionSupport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 728924410235215069L;

	private String authorno;

	private String authorname;

	private Date lastmodify;

	private String subject;

	private String flow;

	private String id;

	private ModuleVO module;

	private transient FlowDiagram _flowDiagram;
	
	/**
	 * 是否签出
	 */
	private  boolean checkout = false;
	
	/**
	 * 签出者
	 */
	private String checkoutHandler;
	
	/**
	 * 流程常用意见
	 */
	private String opinion;
	
	/**
	 * 获取流程常用意见
	 * @return
	 */
	public String getOpinion() {
		return opinion;
	}
	/**
	 * 设置流程常用意见
	 * @param opinion
	 */
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	/**
	 * 是否被签出
	 * @return
	 */
	public boolean isCheckout() {
		return checkout;
	}

	/**
	 * 设置是否签出
	 * @param checkout
	 */
	public void setCheckout(boolean checkout) {
		this.checkout = checkout;
	}

	/**
	 * 获取签出者
	 * @return
	 */
	public String getCheckoutHandler() {
		return checkoutHandler;
	}

	/**
	 * 设置签出者
	 * @param checkoutHandler
	 */
	public void setCheckoutHandler(String checkoutHandler) {
		this.checkoutHandler = checkoutHandler;
	}

	/**
	 * 获取模块
	 * 
	 * @return cn.myapps.core.deploy.module.ejb.ModuleVO
	 * @hibernate.many-to-one class="cn.myapps.core.deploy.module.ejb.ModuleVO"
	 *                        column="MODULE"
	 */
	public ModuleVO getModule() {
		return module;
	}

	/**
	 * 设置模块
	 * 
	 * @param module
	 *            模块对象
	 */
	public void setModule(ModuleVO module) {
		this.module = module;
	}

	/**
	 * 获取作者
	 * 
	 * @hibernate.property column="AUTHORNAME"
	 * @return 作者
	 */
	public String getAuthorname() {
		return authorname;
	}

	/**
	 * 设置作者
	 * 
	 * @param authorname
	 *            作者
	 */
	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	/**
	 * @hibernate.property column="AUTHORNO"
	 * @return
	 */
	public String getAuthorno() {
		return authorno;
	}

	public void setAuthorno(String authorno) {
		this.authorno = authorno;
	}

	/**
	 * @hibernate.property column="FLOW" type="text" length = "100000"
	 * @return
	 */
	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	/**
	 * @hibernate.property column="LASTMODIFY"
	 * @return
	 */
	public Date getLastmodify() {
		return lastmodify;
	}

	public void setLastmodify(Date lastmodify) {
		this.lastmodify = lastmodify;
	}

	/**
	 * @hibernate.property column="SUBJECT"
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @hibernate.id column="ID" generator-class="assigned"
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FlowDiagram toFlowDiagram() {
		if (_flowDiagram == null) {
			WFRunner wfr = new WFRunner(this.getFlow(), getApplicationid());
			_flowDiagram = wfr.getFlowDiagram();
			_flowDiagram.setId(this.id);

			// 加载子流程图表元素
			//addElementsToSubFlow(_flowDiagram);
		}

		return _flowDiagram;
	}

	private void addElementsToSubFlow(FlowDiagram flowDiagram) {
		Collection<SubFlow> subFlowList = flowDiagram.getSubFlowNodeList();
		BillDefiProcess defiProcess = new BillDefiProcessBean();
		try {
			for (Iterator<SubFlow> iterator = subFlowList.iterator(); iterator.hasNext();) {
				SubFlow subFlow = (SubFlow) iterator.next();
				if (!SubFlow.PARAM_PASSING_SHARE.equals(subFlow.paramPassingType)) {
					BillDefiVO flow = (BillDefiVO) defiProcess.doView(subFlow.subflowid);
					FlowDiagram subFlowDiagram = flow.toFlowDiagram();
					subFlow._subelems = subFlowDiagram.getAllElements();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearDiagram() {
		_flowDiagram = null;
	}

	public Node findNodeById(String nodeid) throws Exception {
		FlowDiagram fd = this.toFlowDiagram();
		return fd.getNodeByID(nodeid);
	}

	public Node getFirstNode(Document doc,ParamsTable params,WebUser user) {
		FlowDiagram fd = this.toFlowDiagram();
		// 获取所有开始结点
		Collection<StartNode> startNodeList = fd.getStartNodeList();
		for (Iterator<StartNode> iter = startNodeList.iterator(); iter.hasNext();) {
			StartNode startNode = (StartNode) iter.next();
			Collection<Node> nextNodeList = fd.getNextNodeList(startNode.id,doc,params,user);

			for (Iterator<Node> iter2 = nextNodeList.iterator(); iter2.hasNext();) {
				Node nextNode = (Node) iter2.next();
				return nextNode;
			}
		}
		return null;
	}

	/**
	 * 根据用户获取流程的第一个节点
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Node getFirstNode(Document doc,WebUser user,ParamsTable params) throws Exception {
		Collection<Node> firstNodeList = getFirstNodeList(doc,user,params);
		if (firstNodeList != null && firstNodeList.size() > 0) {
			return (Node) firstNodeList.iterator().next();
		}
		return null;
	}

	/**
	 * 获取用户有审批权限的第一个节点列表
	 * 
	 * @param user
	 *            用户
	 * @return
	 * @throws Exception
	 */
	public Collection<Node> getFirstNodeList(Document doc,WebUser user,ParamsTable params) throws Exception {
		ArrayList<Node> rtn = new ArrayList<Node>();
		FlowDiagram fd = this.toFlowDiagram();

		// 获取所有开始结点
		Collection<StartNode> startNodeList = fd.getStartNodeList();
		for (Iterator<StartNode> iter = startNodeList.iterator(); iter.hasNext();) {
			StartNode startNode = (StartNode) iter.next();
			Collection<Node> nextNodeList = fd.getNextNodeList(startNode.id,doc,params,user);

			for (Iterator<Node> iter2 = nextNodeList.iterator(); iter2.hasNext();) {
				Node nextNode = (Node) iter2.next();
				State state = StateCreator.getNodeState(nextNode);
//				ParamsTable parmas = new ParamsTable();
				params.setParameter("_flowId", this.getId());
				Collection<String> prinspalIdList = state.getPrincipalIdList(doc,params, user.getDomainid(),
						this.getApplicationid(),user);
				// 当前节点负责人或管理员可获取此节点
				if (prinspalIdList.contains(user.getId()) || "admin".equals(user.getLoginno().toLowerCase())
						|| isAgent(prinspalIdList, this.getId(), user)) {
					rtn.add(nextNode);
				}
			}
		}
		return rtn;
	}

	public static boolean isAgent(Collection<String> prinspalIdList, String flowId, WebUser user) throws Exception {
		for (String userId : prinspalIdList) {
			if (user.isAgent(userId, flowId)) {
				return true;
			}
		}
		return false;

	}

	public Node getStartNodeByFirstNode(Node firstNode) {
		return (Node) getStartNodeListByFirstNode(firstNode).iterator().next();
	}

	public Collection<Element> getStartNodeListByFirstNode(Node firstNode) {
		ArrayList<Element> rtn = new ArrayList<Element>();
		// String flow = flowVO.getFlow();
		// WFRunner wfr = new WFRunner(flow);
		// FlowDiagram fd = wfr.getFlowDiagram();
		FlowDiagram fd = this.toFlowDiagram();

		// 获取所有endnodeid为firstNode的开始结点列表
		Collection<Element> ems = fd.getAllElements();
		for (Iterator<Element> iter = ems.iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			if (element instanceof Relation) {
				Relation r = (Relation) element;
				if (r.endnodeid != null && r.endnodeid.equals(firstNode.id)) {
					Element em = fd.getElementByID(r.startnodeid);
					if (em != null && em instanceof StartNode) {
						rtn.add(em);
					}
				}
			}
		}
		return rtn;
	}

	public Collection<Node> getNextNodeList(String currnodeid,Document doc,ParamsTable params,WebUser user) {
		FlowDiagram fd = this.toFlowDiagram();
		return fd.getNextNodeList(currnodeid,doc,params,user);
	}

	public String[] getNextNodeids(String currnodeid,Document doc,ParamsTable params,WebUser user) {
		FlowDiagram fd = this.toFlowDiagram();
		Collection<Node> nextNodeList = fd.getNextNodeList(currnodeid,doc,params,user);
		String[] nodeids = new String[nextNodeList.size()];
		int index = 0;
		for (Iterator<Node> iterator = nextNodeList.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			nodeids[index] = node.id;
			index++;
		}

		return nodeids;
	}

	/**
	 * 获取下一个节点
	 * 
	 * @param currnodeid
	 *            当前节点ID
	 * @param nextNodeName
	 *            下一个节点名称
	 * @return 有匹配名称的则返回匹配的节点，没有则返回第一个节点
	 */
	public Node getNextNode(String currnodeid, String nextNodeName,Document doc,ParamsTable params,WebUser user) {
		FlowDiagram fd = this.toFlowDiagram();
		Collection<Node> nextNodeList = fd.getNextNodeList(currnodeid,doc,params,user);
		for (Iterator<Node> iterator = nextNodeList.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			if (!StringUtil.isBlank(nextNodeName) && nextNodeName.equals(node.name)) {
				return node;
			}
		}
		return (Node) nextNodeList.iterator().next();
	}
	
	/**
	 * 获取流程图表中所有可执行的节点（包含人工节点、自动节点、子流程节点）
	 * @return
	 */
	public Collection<Node> getAllExecuteableNodes(){
		Collection<Node> list = new ArrayList<Node>();
		FlowDiagram fd = this.toFlowDiagram();
		for (Iterator<Node> iterator = fd.getAllNodes().iterator(); iterator.hasNext();) {
			Node node = iterator.next();
			if(node instanceof ManualNode || node instanceof AutoNode || node instanceof SubFlow){
				list.add(node);
			}
		}
		
		return list;
	}
}
