package cn.myapps.webservice.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import cn.myapps.base.dao.ValueObject;

/**
 * Simple流程历史路线
 * 
 */
public class SimpleRelationHIS extends ValueObject {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4498219846885073545L;

	private String id;

	public String startnodeid;

	public String endnodeid;

	private String startnodename;

	private String endnodename;

	private String flowid;
	
	private String flowname; //流程名称

	private String docid;

	private Collection<SimpleActorHIS> actorhiss;

	private boolean ispassed;

	private String auditor; // 审批人

	/**
	 * 到达此节点的时间
	 */
	private Date actiontime;

	/**
	 * 送下一个节点的时间
	 */
	private Date processtime;

	private String attitude; // 审批意见

//	private BillDefiVO flow; // 流程

	private String flowOperation;// 操作类型

	private int ReminderCount;// 提醒次数
	
	/**
	 * 流程实例ID
	 */
	private String flowStateId;

	/**
	 * 获取提醒次数
	 * 
	 * @return 提醒次数
	 */
	public int getReminderCount() {
		return ReminderCount;
	}

	/**
	 * 设置提醒次数
	 * 
	 * @param reminderCount
	 *            提醒次数
	 */
	public void setReminderCount(int reminderCount) {
		ReminderCount = reminderCount;
	}

	/**
	 * 设置流程操作类型
	 * 
	 * @return 流程操作类型
	 */
	public String getFlowOperation() {
		return flowOperation;
	}

	/**
	 * 设置流程操作类型
	 * 
	 * @param flowOperation
	 *            流程操作类型
	 */
	public void setFlowOperation(String flowOperation) {
		this.flowOperation = flowOperation;
	}

	public SimpleRelationHIS() {

	}

//	/**
//	 * 构造方法
//	 * 
//	 * @param start
//	 *            开始节点对象
//	 * @throws SequenceException
//	 */
//	public SimpleRelationHIS(Node start) throws SequenceException {
//		id = Sequence.getSequence();
//		setStartNode(start);
//	}
//
//	/**
//	 * 构造方法
//	 * 
//	 * @param start
//	 *            开始节点对象
//	 * @param end
//	 *            结束节点对象
//	 * @throws SequenceException
//	 */
//	public SimpleRelationHIS(Node start, Node end) throws SequenceException {
//		id = Sequence.getSequence();
//		setStartNode(start);
//		setEndNode(end);
//	}

//	/**
//	 * 设置开始节点对象
//	 * 
//	 * @param start
//	 *            开始节点对象
//	 */
//	public void setStartNode(Node start) {
//		startnodeid = start.id;
//		startnodename = start.name;
//	}
//
//	/**
//	 * 设置结束节点对象
//	 * 
//	 * @param end
//	 *            结束节点对象
//	 */
//	public void setEndNode(Node end) {
//		endnodeid = end.id;
//		endnodename = end.name;
//	}

//	/**
//	 * 获取开始节点对象
//	 * 
//	 * @return 开始节点对象
//	 */
//	public Node getStartNode() {
//		if (getFlow() != null) {
//			return (Node) getFlow().toFlowDiagram().getElementByID(startnodeid);
//		}
//		return null;
//	}
//
//	/**
//	 * 获取结束节点对象
//	 * 
//	 * @return 结束节点对象
//	 */
//	public Node getEndNode() {
//		if (getFlow() != null) {
//			return (Node) getFlow().toFlowDiagram().getElementByID(endnodeid);
//		}
//		return null;
//	}
//
//	public void addActorhiss(WebUser user) throws SequenceException {
//		SimpleActorHIS acthis = new SimpleActorHIS(user);
//		this.getActorhiss().add(acthis);
//	}

	/**
	 * @hibernate.property column="ACTIONTIME"
	 * @return
	 */
	public Date getActiontime() {
		return actiontime;
	}

	public void setActiontime(Date actiontime) {
		this.actiontime = actiontime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartnodename() {
		return startnodename;
	}

	public void setStartnodename(String startnodename) {
		this.startnodename = startnodename;
	}

	public String getFlowid() {
		return flowid;
	}

	public void setFlowid(String flowid) {
		this.flowid = flowid;
	}

	public Collection<SimpleActorHIS> getActorhiss() {
		if (actorhiss == null)
			actorhiss = new ArrayList<SimpleActorHIS>();
		return actorhiss;
	}

	public void setActorhiss(Collection<SimpleActorHIS> actorhiss) {
		this.actorhiss = actorhiss;
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getEndnodeid() {
		return endnodeid;
	}

	public void setEndnodeid(String endnodeid) {
		this.endnodeid = endnodeid;
	}

	public String getEndnodename() {
		return endnodename;
	}

	public void setEndnodename(String endnodename) {
		this.endnodename = endnodename;
	}

	public String getStartnodeid() {
		return startnodeid;
	}

	public void setStartnodeid(String startnodeid) {
		this.startnodeid = startnodeid;
	}

	public boolean getIspassed() {
		return ispassed;
	}

	public void setIspassed(boolean ispassed) {
		this.ispassed = ispassed;
	}

	public String getAttitude() {
		return attitude;
	}

	public void setAttitude(String attitude) {
		this.attitude = attitude;
	}

//	private BillDefiVO getFlow() {
//		if (flow == null) {
//			BillDefiProcess process;
//			try {
//				process = (BillDefiProcess) ProcessFactory
//						.createProcess(BillDefiProcess.class);
//				flow = (BillDefiVO) process.doView(flowid);
//				//统一从document里获取，可能有种情况是前台手动调整流程
//				DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,flow.getApplicationid());
//				Document doc = (Document)dp.doView(docid);
//				flow = doc.getFlowVO();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return flow;
//	}

	public Date getProcesstime() {
		return processtime;
	}

	public void setProcesstime(Date processtime) {
		this.processtime = processtime;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	
	public String getFlowname() {
		return flowname;
	}

	public void setFlowname(String flowname) {
		this.flowname = flowname;
	}
	
	

	/**
	 * 获取流程实例ID
	 * @return
	 */
	public String getFlowStateId() {
		return flowStateId;
	}

	/**
	 * 设置流程实例ID
	 * @param flowStateId
	 */
	public void setFlowStateId(String flowStateId) {
		this.flowStateId = flowStateId;
	}

	public Collection<String> getUserIdList() throws Exception {
		Collection<String> rtn = new ArrayList<String>();

		if (getActorhiss() != null && !getActorhiss().isEmpty()) {
			for (Iterator<SimpleActorHIS> iterator = getActorhiss().iterator(); iterator
					.hasNext();) {
				SimpleActorHIS actorHIS = (SimpleActorHIS) iterator.next();
				rtn.add(actorHIS.getActorid());
			}
		}

		return rtn;
	}

//	public Collection<SimpleUser> getUserList() throws Exception {
//		Collection<SimpleUser> rtnList = new ArrayList<SimpleUser>();
//		Collection<UserVO> rtn = new ArrayList<UserVO>();
//
//		UserProcess userProcess = (UserProcess) ProcessFactory
//				.createProcess(UserProcess.class);
//		for (Iterator<String> iterator = getUserIdList().iterator(); iterator
//				.hasNext();) {
//			String userid = (String) iterator.next();
//			UserVO userVO = (UserVO) userProcess.doView(userid);
//			rtn.add(userVO);
//		}
//		for (Iterator<UserVO> iterator = rtn.iterator(); iterator.hasNext();) {
//			UserVO userVO = (UserVO) iterator.next();
//			SimpleUser sUser = new UserService().convertToSimple(userVO);
//			rtnList.add(sUser);
//		}
//		return rtnList;
//	}

}
