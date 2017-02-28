package cn.myapps.util.notification;

/**
 * 发送消息通知工具类库
 * <p>封装消息的使用</p>
 * @author Happy
 *
 */
public class NotificationUtil {
	
	/**
	 * 发送图文消息
	 * @param touser
	 * 		用户账号列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 * @param title
	 * 		消息标题
	 * @param description
	 * 		消息描述
	 * @param url
	 * 		消息url
	 * @param picUrl
	 * 		图片url
	 * @param domainId
	 * 		企业域id
	 * @param applicationid
	 * 		发送目标软件id（null 表示发送到企业小助手）
	 * @return
	 * @throws Exception
	 */
	public static void sendRichWeixinMessage(String touser,String title,String description,String url,String picUrl,String domainId,String applicationid) {
		if(applicationid==null) applicationid ="";
		RichWeixinMessage message = new RichWeixinMessage(touser, title, description, url, picUrl, domainId, applicationid);
		send(message);
	}
	
	/**
	 * 发送文本消息
	 * @param touser
	 * 		用户账号列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 * @param content
	 * 		消息内容
	 * @param domainId
	 * 		企业域id
	 * @param applicationid
	 * 		发送目标软件id（null 表示发送到企业小助手）
	 * @return
	 * @throws Exception
	 */
	public static void sendTextWeixinMessage(String touser,String content, String domainId,String applicationid) {
		if(applicationid==null) applicationid ="";
		TextWeixinMessage message = new TextWeixinMessage(touser, content, domainId, applicationid);
		send(message);
	}
	
	
	private static void send(Message message){
		NotificationSender sender = NotificationSenderFactory.getQueueNotificationSender();
		sender.putMessage(message);
		sender.start();
	}
	
	

}
