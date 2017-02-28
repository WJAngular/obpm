package cn.myapps.core.workflow.notification.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;

public interface NotificationProcess extends IRunTimeProcess<Notification> {

	/**
	 * 通知当前审批者
	 * 
	 * @param doc
	 *            文档(Document)对象
	 * @param pending
	 *            待办对象
	 * @param flow
	 *            流程对象
	 * @throws Exception
	 */
	public void notifyCurrentAuditors(Document doc, BillDefiVO flow ,WebUser user, String currNodeId, String[] nextNodeIds,String FlowTypeMessage) throws Exception;
	
	/**
	 * 编辑审批人通知当前审批者
	 * 
	 * @param doc
	 *            文档(Document)对象
	 * @param pending
	 *            待办对象
	 * @param flow
	 *            流程对象
	 * @throws Exception
	 */
	public void notifyCurrentAuditors(Document doc, BillDefiVO flow,Collection<BaseUser> userList,WebUser user) throws Exception;

	/**
	 * 通知超期审批者
	 * 
	 * @throws Exception
	 */
	public void notifyOverDueAuditors() throws Exception;

	/**
	 * 通知被回退者
	 * 
	 * @param doc
	 *            文档(Document)对象
	 * @param flow
	 *            流程对象
	 * @throws Exception
	 */
	public void notifyRejectees(Document doc,BillDefiVO flow,ParamsTable params, WebUser user,String FlowTypeMessage) throws Exception;

	/**
	 * 通知流程送出人
	 * 
	 * @param doc
	 *            文档(Document)对象
	 * @param pending
	 *            待办对象
	 * @param flow
	 *            流程对象
	 * @param currNodeRT
	 * 		当前节点对象
	 * @param user
	 *            web用户
	 * @throws Exception
	 */
	public void notifySender(Document doc, BillDefiVO flow,NodeRT currNodeRT, WebUser user,String FlowTypeMessage) throws Exception;
	
	/**
	 * 发送流程催办消息通知
	 * @param doc
	 * 		文档对象
	 * @param nodertIds
	 * 		催办的状态节点id
	 * @param title
	 * 		催办标题
	 * @param reminderContent
	 * 		提醒内容
	 * @param params
	 * 		参数表
	 * @param user
	 * 		当前用户
	 * @throws Exception
	 */
	public void sendFlowReminderNotification(Document doc,String[] nodertIds,String title,String reminderContent,ParamsTable params,WebUser user) throws Exception;
	
	/**
	 * 发送抄送通知
	 * @param doc
	 *          文档对象
	 * @param flow
     *          流程对象
     * @param nodertId
     *            节点id
	 * @param userList
	 *            通知对象
	 * @param user
	 *            web用户
	 * @throws Exception
	 */
	public void notifycarbonCopy2Circulator(Document doc,Node curNode,Collection<BaseUser> userList,String FlowType,WebUser user) throws Exception;
}
