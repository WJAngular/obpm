package cn.myapps.core.workflow.notification.ejb;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.util.ObjectUtil;

public class NotificationCreator {
	private final static Logger LOG = Logger
			.getLogger(NotificationCreator.class);

	private Map<String, Object> notificationStrategyMap = new HashMap<String, Object>();
	private Node node = null;

	public NotificationCreator(Node node) {
		if (node != null && node instanceof ManualNode) {
			notificationStrategyMap = ((ManualNode) node)
					.getNotificationStrategyMap();
			this.node = node;
		}
	}

	/**
	 * 创建发送通知
	 * @SuppressWarnings notificationStrategyMapde value值不确定
	 * @param applicationid
	 *            应用标识
	 * @return 发送对象
	 * @throws NotificationException
	 */
	@SuppressWarnings("unchecked")
	public Notification createSendNotification(BaseUser submitter,
			String applicationid) throws NotificationException {
		Map<Object, Object> notificationStrategy = (Map<Object, Object>) notificationStrategyMap
				.get(NotificationConstant.STRTAGERY_SEND);
		if (notificationStrategy == null) {
			LOG.debug("No send notification strategy defined");
			return new NullNotification();
		}

		try {
			SendNotification notification = new SendNotification(applicationid);
			ObjectUtil.copyProperties(notification, notificationStrategy);

			notification.setSubmitter(submitter);
			notification.setSubject("已送出");
			notification.setApplicationid(applicationid);
			notification.setWebUser((WebUser)submitter);
			notification.setNode(node);

			return notification;
		} catch (IllegalAccessException e) {
			throw new NotificationException(e);
		} catch (InvocationTargetException e) {
			throw new NotificationException(e);
		}
	}

	/**
	 * 创建到达提醒
	 * @SuppressWarnings notificationStrategyMapde value值不确定
	 * @param applicationid
	 *            应用标识
	 * @return 发送对象
	 * @throws NotificationException
	 */
	@SuppressWarnings("unchecked")
	public Notification createArriveNotification(String applicationid)
			throws NotificationException {
		Map<Object, Object> notificationStrategy = (Map<Object, Object>) notificationStrategyMap
				.get(NotificationConstant.STRTAGERY_ARRIVE);
		if (notificationStrategy == null) {
			LOG.debug("No arrive notification strategy defined");
			return new NullNotification();
		}

		try {
			Notification notification = new ArriveNotification(applicationid);
			ObjectUtil.copyProperties(notification, notificationStrategy);
			notification.setSubject("待办");
			notification.setApplicationid(applicationid);
			notification.setNode(node);

			return notification;
		} catch (IllegalAccessException e) {
			throw new NotificationException(e);
		} catch (InvocationTargetException e) {
			throw new NotificationException(e);
		}
	}

	/**
	 * 创建超期提醒
	 * @SuppressWarnings notificationStrategyMapde value值不确定
	 * @param curDate
	 *            创建日期
	 * @param deadline
	 *            过期日期
	 * @param applicationid
	 *            应用标识
	 * @return 发送对象
	 * @throws NotificationException
	 */
	@SuppressWarnings("unchecked")
	public Notification createOverDueNotification(Date curDate, Date deadline,
			String applicationid) throws NotificationException {
		Map<Object, Object> notificationStrategy = (Map<Object, Object>) notificationStrategyMap
				.get(NotificationConstant.STRTAGERY_OVERDUE);
		if (notificationStrategy == null) {
			LOG.debug("No overdue notification strategy defined");
			return new NullNotification();
		}

		try {
			OverDueNotification notification = new OverDueNotification(
					applicationid);
			notification.setSubject("待办超期");
			// notification.setSubject("Pending has been expired");
			ObjectUtil.copyProperties(notification, notificationStrategy);

			// 设置超限属性
			notification.setCurDate(curDate);
			notification.setDeadline(deadline);
			notification.setApplicationid(applicationid);
			notification.setNode(node);
			notification.setFlowTypeMessage("待办超期");
			return notification;
		} catch (IllegalAccessException e) {
			throw new NotificationException(e);
		} catch (InvocationTargetException e) {
			throw new NotificationException(e);
		}
	}

	/**
	 * 创建回退提醒
	 * @SuppressWarnings notificationStrategyMapde value值不确定
	 * @param applicationid
	 *            应用标识
	 * @return 发送对象
	 * @throws NotificationException
	 */
	@SuppressWarnings("unchecked")
	public Notification createRejectNotification(UserVO author,
			String applicationid) throws NotificationException {
		Map<Object, Object> notificationStrategy = (Map<Object, Object>) notificationStrategyMap
				.get(NotificationConstant.STRTAGERY_REJECT);
		if (notificationStrategy == null) {
			LOG.debug("No reject notification strategy defined");
			return new NullNotification();
		}

		try {
			RejectNotification notification = new RejectNotification(
					applicationid);
			notification.setSubject("回退");
			// notification.setSubject("Submission has been rejected");
			notification.setApplicationid(applicationid);
			ObjectUtil.copyProperties(notification, notificationStrategy);
			notification.setNode(node);
			return notification;
		} catch (IllegalAccessException e) {
			throw new NotificationException(e);
		} catch (InvocationTargetException e) {
			throw new NotificationException(e);
		}
	}
	
	
	

	public static void main(String[] args) throws NotificationException {
		ManualNode node = new ManualNode(new FlowDiagram());
		// Mock Datas
		node.notificationStrategyJSON = "{"
				+ "arrive: {sendModeCodes:[0, 1]}, "
				+ "overdue: {sendModeCodes:[0, 1], limittimecount:12, timeunit:0, isnotifysuperior:true},"
				+ "reject: {sendModeCodes:[0, 1], responsibleType: 1}" + "}";
		// node.notificationStrategyJSON = "";
		// NotificationCreator creator = new NotificationCreator(node);
		// Notification notification =
		// creator.createArriveNotification("app001");
	}

	/**
	 * 创建流程催办提醒
	 * @param reminderContent
	 * 		催办消息内容
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws NotificationException
	 */
	public Notification createFlowReminderNotification(String title,String reminderContent,String applicationId) throws NotificationException {
		Map<Object, Object> notificationStrategy = (Map<Object, Object>) notificationStrategyMap
				.get(NotificationConstant.STRTAGERY_REMINDER);
		if (notificationStrategy == null) {
			return new NullNotification();
		}

		FlowReminderNotification notification = new FlowReminderNotification();
		notification.setSubject(title);
		notification.setReminderContent(reminderContent);
		notification.setApplicationid(applicationId);
		try {
			ObjectUtil.copyProperties(notification, notificationStrategy);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return notification;
	}
	
	
	/**
	 * 创建抄送消息提醒
	 * @SuppressWarnings notificationStrategyMapde value值不确定
	 * @param applicationid
	 *            应用标识
	 * @return 发送对象
	 * @throws NotificationException
	 */
	@SuppressWarnings("unchecked")
	public Notification createCarbonCopyNotification(String applicationid)
			throws NotificationException {
		Map<Object, Object> notificationStrategy = (Map<Object, Object>) notificationStrategyMap
				.get(NotificationConstant.STRTAGERY_CARBONCOPY);
		if (notificationStrategy == null) {
			LOG.debug("No carbonCopy notification strategy defined");
			return new NullNotification();
		}

		try {
			Notification notification = new CarbonCopyNotification(applicationid);
			ObjectUtil.copyProperties(notification, notificationStrategy);
			notification.setSubject("抄送");
			notification.setApplicationid(applicationid);
			notification.setNode(node);

			return notification;
		} catch (IllegalAccessException e) {
			throw new NotificationException(e);
		} catch (InvocationTargetException e) {
			throw new NotificationException(e);
		}
	}
}
