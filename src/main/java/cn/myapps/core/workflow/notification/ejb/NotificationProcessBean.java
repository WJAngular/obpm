package cn.myapps.core.workflow.notification.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.notification.dao.NotificationDAO;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcessBean;
import cn.myapps.core.workflow.storage.runtime.proxy.ejb.WorkflowProxyProcess;
import cn.myapps.core.workflow.storage.runtime.proxy.ejb.WorkflowProxyProcessBean;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.RuntimeDaoManager;

public class NotificationProcessBean extends AbstractRunTimeProcessBean<Notification> implements NotificationProcess {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9137619464810557702L;

	private BillDefiProcess billDefiProcess;

	private ActorRTProcess actorRTProcess;

	private DocumentProcess documentProcess;

//	private ReminderProcess reminderProcess;
	
//	private SummaryCfgProcess summaryCfgProcess;

	private Map<String, BillDefiVO> flowTemp;

	public NotificationProcessBean(String applicationId) {
		super(applicationId);
		try {
			flowTemp = new HashMap<String, BillDefiVO>();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected IRuntimeDAO getDAO() throws Exception {
		return new RuntimeDaoManager().getNotificationDAO(getConnection(), getApplicationId());
	}

	/**
	 * 通知流程送出人
	 * 
	 * @param doc
	 * @param pending
	 * @param flow
	 * @param user
	 * @throws Exception
	 */
	public void notifySender(Document doc, BillDefiVO flow,NodeRT currNodeRT, WebUser user,String FlowTypeMessage) throws Exception {

		if (currNodeRT != null) {
			WorkflowProxyProcess proxyProcess = new WorkflowProxyProcessBean(flow.getApplicationid());
			Collection<BaseUser> userList = currNodeRT.getUserList();

			if (userList.isEmpty()) {
				return;
			}
			userList.addAll(proxyProcess.getAgentsByOwners(userList));

			// 创建发送通知
			Node el = flow.toFlowDiagram().getNodeByID(currNodeRT.getNodeid());
			NotificationCreator creator = new NotificationCreator(el);
			Notification notification = creator.createSendNotification(user, getApplicationId());
			notification.setFlowTypeMessage(FlowTypeMessage);
			notification.setResponsibles(userList);
			notification.setDocument(doc);
			notification.send();
		}
	}

	/**
	 * 流程审批后通知当前审批者
	 */
	public void notifyCurrentAuditors(Document doc, BillDefiVO flow,WebUser user, String currNodeId, String[] nextNodeIds,String flowTypeMessage) throws Exception {
		FlowStateRT stateRT = doc.getState();

		if (stateRT != null) {
			Collection<NodeRT> nodeRTs = stateRT.getNoderts();
			WorkflowProxyProcess proxyProcess = new WorkflowProxyProcessBean(doc.getApplicationid());
			for (Iterator<NodeRT> iterator = nodeRTs.iterator(); iterator.hasNext();) {
				NodeRT nodeRT = (NodeRT) iterator.next();
				boolean isNeedToNotify = false;
				if(currNodeId.equals(nodeRT.getNodeid())){
					isNeedToNotify = true;
				}else {
					for(int i=0; i<nextNodeIds.length; i++){
						if(nextNodeIds[i].equals(nodeRT.getNodeid())){
							isNeedToNotify = true;
							break;
						}
					}
				}
				
				if(isNeedToNotify){
					Collection<BaseUser> userList = nodeRT.getUserList();
	
					if (userList.isEmpty()) {
						continue;
					}
					userList.addAll(proxyProcess.getAgentsByOwners(userList));
					// 创建到达通知
					Node el = flow.toFlowDiagram().getNodeByID(nodeRT.getNodeid());
					NotificationCreator creator = new NotificationCreator(el);
					Notification notification = creator.createArriveNotification(getApplicationId());
					
					notification.setResponsibles(userList);
					notification.setDocument(doc);
					notification.setWebUser(user);
					notification.setFlowTypeMessage(flowTypeMessage);
					notification.send();
				}
			}
		}
	}
	
	/**
	 * 编辑审批人流程审批后通知当前审批者
	 */
	public void notifyCurrentAuditors(Document doc,BillDefiVO flow,Collection<BaseUser> userList,WebUser user) throws Exception {
		FlowStateRT stateRT = doc.getState();

		if (stateRT != null) {
			Collection<NodeRT> nodeRTs = stateRT.getNoderts();
			for (Iterator<NodeRT> iterator = nodeRTs.iterator(); iterator.hasNext();) {
				NodeRT nodeRT = (NodeRT) iterator.next();

				if (userList.isEmpty()) {
					continue;
				}
				// 创建到达通知
				Node el = flow.toFlowDiagram().getNodeByID(nodeRT.getNodeid());
				NotificationCreator creator = new NotificationCreator(el);
				Notification notification = creator.createArriveNotification(getApplicationId());
				
				notification.setResponsibles(userList);
				notification.setDocument(doc);
				notification.setWebUser(user);
				notification.send();
			}
		}
	}

	/**
	 * 通知被回退者
	 * 
	 * @param doc
	 * @param flow
	 * @throws Exception
	 */
	public void notifyRejectees(Document doc,BillDefiVO flow,ParamsTable params,WebUser user,String flowTypeMessage) throws Exception {
		FlowStateRT stateRT = doc.getState();

		if (stateRT != null) {
			Collection<NodeRT> nodeRTs = stateRT.getNoderts();
			WorkflowProxyProcess proxyProcess = new WorkflowProxyProcessBean(doc.getApplicationid());
			for (Iterator<NodeRT> iterator = nodeRTs.iterator(); iterator.hasNext();) {
				NodeRT nodeRT = (NodeRT) iterator.next();
				Collection<BaseUser> userList = new ArrayList<BaseUser>();
				for (Iterator<ActorRT> iterator2 = nodeRT.getPendingActorRTList().iterator(); iterator2.hasNext();) {
					ActorRT actorRT = (ActorRT) iterator2.next();
					userList.addAll(actorRT.getAllUser());
				}

				if (userList.isEmpty()) {
					continue;
				}
				userList.addAll(proxyProcess.getAgentsByOwners(userList));

				// 创建回退通知
				Node el = flow.toFlowDiagram().getNodeByID(nodeRT.getNodeid());
				NotificationCreator creator = new NotificationCreator(el);

				Notification notification = creator.createRejectNotification((UserVO) doc.getAuthor(),
						getApplicationId());
				notification.setResponsibles(userList);
				notification.setDocument(doc);
				notification.setWebUser(user);
				notification.set_params(params);
				notification.setFlowTypeMessage(flowTypeMessage);
				notification.send();
			}
		}
	}
	
	/**
	 * 发送流程催办消息通知
	 * @param doc
	 * 		文档对象
	 * @param nodertIds
	 * 		催办的状态节点id
	 * @param reminderContent
	 * 		提醒内容
	 * @param params
	 * 		参数表
	 * @param user
	 * 		当前用户
	 * @throws Exception
	 */
	public void sendFlowReminderNotification(Document doc,String[] nodertIds,String title,String reminderContent,ParamsTable params,WebUser user) throws Exception {
		FlowStateRT instance = doc.getState();
		Collection<NodeRT> noderts = instance.getNoderts();
		FlowDiagram fd = instance.getFlowVO().toFlowDiagram();
		for (Iterator<NodeRT> iterator = noderts.iterator(); iterator.hasNext();) {
			NodeRT nodeRT = iterator.next();
			for (int i = 0; i < nodertIds.length; i++) {
				if(nodeRT.getId().equals(nodertIds[i])){
					Node el = fd.getNodeByID(nodeRT.getNodeid());
					NotificationCreator creator = new NotificationCreator(el);
					Notification notification = creator.createFlowReminderNotification(title,reminderContent,getApplicationId());
					notification.setResponsibles(nodeRT.getUserList());
					notification.setDocument(doc);
					notification.setWebUser(user);
					notification.send();
					break;
				}
			}
		}
				
	}

	public void notifyOverDueAuditors() throws Exception {
		Collection<Notification> notifications = getOverDueNotifications();
		for (Iterator<Notification> iterator = notifications.iterator(); iterator.hasNext();) {
			Notification notification = (Notification) iterator.next();
			notification.send();

//			if (notification.getDocument() != null) {
				// 对于同一个文档和流程，提醒次数只增加一次
//				String flowId = notification.getDocument().getFlowid();
//				String key = docId + flowId;
				
//				String actorid = notification.getWebUser().getId();
				
				
				
//				if (notification.isSended() && unique.add(key)) {
				
				if (notification instanceof OverDueNotification) {
					String actorrtId = ((OverDueNotification)notification).getActorrt().getId();
					 ((NotificationDAO) getDAO()).updateLastOverDueReminder(actorrtId);					
				}
					
					
//					addReminderCount(docId, flowId);
//				}
//			}
		}
	}

	/**
	 * 在最后一条历史记录中增加提醒次数，以标记当前节点超时提醒次数
	 * 
	 * @throws Exception
	 */
	public void addReminderCount(String docid, String flowid) throws Exception {
		RelationHISProcess relationProcess = new RelationHISProcessBean(getApplicationId());
		RelationHIS relationHIS = relationProcess.doViewLast(docid, flowid);
		relationHIS.setReminderCount(relationHIS.getReminderCount() + 1);
		relationProcess.doUpdate(relationHIS);
	}

	/**
	 * 获取超时的通知
	 * 
	 * @return 超时通知集合
	 * @throws Exception
	 */
	private Collection<Notification> getOverDueNotifications() throws Exception {
		List<Notification> notifications = new ArrayList<Notification>();

		Date curDate = new Date(); // 当前日期

		// 测试用的伪代码
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(DateUtil.parseDateTime("2009-05-05 13:00:44"));
		// curDate = calendar.getTime();

		Collection<Map<String, Object>> pendingInfo = ((NotificationDAO) getDAO()).queryOverDuePending(
				new java.sql.Timestamp(curDate.getTime()), getApplicationId());
		for (Iterator<Map<String, Object>> iterator = pendingInfo.iterator(); iterator.hasNext();) {
			Map<String, Object> info = iterator.next();
			Collection<BaseUser> userList = getUserList(info);
			
			String id = (String) info.get("actorrtid");
			ActorRT actorrt = (ActorRT) getActorRTProcess().doView(id);
			// 创建超期通知
			Notification notification = createOverDueNotification(info, curDate);
			notification.setResponsibles(userList);
			if(notification instanceof OverDueNotification){
				((OverDueNotification)notification).setActorrt(actorrt);
			}
			notifications.add(notification);
		}

		return notifications;
	}

	/**
	 * 获取当前审批用户列表
	 * 
	 * @param info
	 *            待办信息
	 * @return 文档
	 * @throws Exception
	 */
	private List<BaseUser> getUserList(Map<String, Object> info) throws Exception {
		String id = (String) info.get("actorrtid");
		ActorRT actorrt = (ActorRT) getActorRTProcess().doView(id);
		if (actorrt != null) {
			return actorrt.getAllUser();
		}

		return new ArrayList<BaseUser>();
	}

	/**
	 * 创建超时通知
	 * 
	 * @param info
	 *            待办信息
	 * @param curDate
	 *            当前日期
	 * @return
	 * @throws Exception
	 */
	private Notification createOverDueNotification(Map<String, Object> info, Date curDate) throws Exception {
		String nodeid = (String) info.get("nodeid");
		String flowid = (String) info.get("flowid");
		String docid = (String) info.get("docid");
		Date deadline = (Date) info.get("deadline");

		BillDefiVO flow = (BillDefiVO) flowTemp.get(flowid);
		if (!flowTemp.containsKey(flowid)) {
			flow = (BillDefiVO) getBillDefiProcess().doView(flowid);
			//统一从document里获取，可能有种情况是前台手动调整流程
			/*DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,flow.getApplicationid());
			Document doc = (Document)dp.doView(docid);
			if(doc != null && flow ==null){
				flow = doc.getFlowVO();
			}*/
		}
		
		return createOverDueNotification(flow, docid, nodeid, curDate, deadline);
	}

	/**
	 * 创建超期通知
	 * 
	 * @param flow
	 *            流程
	 * @param docid
	 *            文档ID
	 * @param nodeid
	 *            节点ID
	 * @param curDate
	 *            当前日期
	 * @param deadline
	 *            最后限期
	 * @return
	 * @throws Exception
	 */
	private Notification createOverDueNotification(BillDefiVO flow, String docid, String nodeid, Date curDate,
			Date deadline) throws Exception {
		DocumentProcess dp = getDocumentProcess();

		if (flow != null) {
			Node el = flow.toFlowDiagram().getNodeByID(nodeid);
			NotificationCreator creator = new NotificationCreator(el);
			Notification notification = creator.createOverDueNotification(curDate, deadline, getApplicationId());
			Document document = (Document) dp.doView(docid); // 性能问题
			notification.setDocument(document);

			return notification;
		}

		return new NullNotification();
	}

	public DocumentProcess getDocumentProcess() {
		if (documentProcess == null) {
			try {
				documentProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,getApplicationId());
			} catch (CreateProcessException e) {
				e.printStackTrace();
			}
		}
		return documentProcess;
	}
/*
	public ReminderProcess getReminderProcess() {
		if (reminderProcess == null) {
			reminderProcess = new ReminderProcessBean();
		}
		return reminderProcess;
	}
*/
	public BillDefiProcess getBillDefiProcess() throws Exception {
		if (billDefiProcess == null) {
			billDefiProcess = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		}
		return billDefiProcess;
	}

	public ActorRTProcess getActorRTProcess() {
		if (actorRTProcess == null) {
			actorRTProcess = new ActorRTProcessBean(getApplicationId());
		}
		return actorRTProcess;
	}

	@Override
	public void notifycarbonCopy2Circulator(Document doc, Node curNode, Collection<BaseUser> userList,String flowTypeMessage, WebUser user)
			throws Exception {

		   if (curNode != null && !userList.isEmpty()) {
					// 创建抄送通知
					NotificationCreator creator = new NotificationCreator(curNode);
					Notification notification = creator.createCarbonCopyNotification(getApplicationId());
					
					notification.setResponsibles(userList);
					notification.setDocument(doc);
					notification.setFlowTypeMessage(flowTypeMessage);
					notification.setWebUser(user);
					notification.send();
				}
			}
		
}
