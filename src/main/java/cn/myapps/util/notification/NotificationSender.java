package cn.myapps.util.notification;

/**
 * 通知提醒发送器
 * @author Happy
 *
 */
public interface NotificationSender {
	
	
	/**
	 * 添加消息
	 * @param message
	 */
	public void putMessage(Message message);
	
	/**
	 * 启动发送器
	 */
	public void start();
	
	/**
	 * 停止发送器
	 */
	public void stop();

}
