package cn.myapps.util.notification;


/**
 * 通知消息发送器工厂
 * <p>职责：复杂创建不同种类的通知消息发送器实例</p>
 * @author Happy
 *
 */
public class NotificationSenderFactory {
	
	private static NotificationSender queueNotificationSender = null;
	
	/**
	 * 获取单例模式的队列通知消息发送器实例
	 * @return
	 */
	public static NotificationSender getQueueNotificationSender(){
		if(queueNotificationSender==null){
			synchronized (NotificationSenderFactory.class) {
				queueNotificationSender = new QueueNotificationSender();
			}
		}
		
		return queueNotificationSender;
	}
	

}
