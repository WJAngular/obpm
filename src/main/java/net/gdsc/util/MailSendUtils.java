//package net.gdsc.util;
//
//import java.util.Properties;
//
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.Message.RecipientType;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//import cn.myapps.util.property.PropertyUtil;
//
//public class MailSendUtils {
//
//	public static void sendHtmlMail(MailInfo info)throws Exception{
//		Message message = getMessage(info);
//		message.setContent(info.getContent(), "text/html;charset=utf-8");
//		Transport.send(message);
//	}
//	
//	public static void sendTextMail(MailInfo info)throws Exception{
//		Message message = getMessage(info);
//		message.setText(info.getContent());
//		Transport.send(message);
//	}
//	/**
//	 * 使用默认账号 发送邮箱 
//	 * @param toAddress   收件人
//	 * @param title		   标题
//	 * @param content	   内容
//	 * @throws Exception
//	 */
//	public static void sendTextMail1(String toAddress,String title,String content)throws Exception{
//		MailInfo info = new MailInfo();
//		info.setToAddress(toAddress);
//		info.setSubject(title);
//		info.setContent(content);
//		Message message = getMessage(info);
//		message.setText(info.getContent());
//		Transport.send(message);
//	}
//	
//	private static Message getMessage(MailInfo info) throws Exception{
//		final Properties  p = System.getProperties();
//		PropertyUtil.reload("e-mail");
//		String host = PropertyUtil.get("mail.smtp.host");
//		String auth = PropertyUtil.get("mail.smtp.auth");
//		String user = PropertyUtil.get("mail.smtp.user");
//		String pass = PropertyUtil.get("mail.smtp.pass");
//		String repy = PropertyUtil.get("mail.smtp.repy");
//		boolean debug = PropertyUtil.get("mail.smtp.debug").equals("true");
//		p.setProperty("mail.smtp.host", host);
//		p.setProperty("mail.smtp.auth", auth);//需要验证身份
//		p.setProperty("mail.smtp.user", user);
//		p.setProperty("mail.smtp.pass", pass);
//		
//		Session session = Session.getInstance(p, new Authenticator(){
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(p.getProperty("mail.smtp.user"),p.getProperty("mail.smtp.pass"));
//			}
//		});
//		session.setDebug(debug);//调试模式(打印模式)
//		Message message = new MimeMessage(session);
//		message.setSubject(info.getSubject());
//		message.setReplyTo(InternetAddress.parse(repy));
//		message.setFrom(new InternetAddress(p.getProperty("mail.smtp.user"),"惠州市海洋与渔业局"));
//		message.setRecipient(RecipientType.TO,new InternetAddress(info.getToAddress()));
//		
//		return message ;
//	}
//	
//	//测试
//	/*public static void main(String[] args) throws Exception{
//		MailInfo info = new MailInfo();
//		info.setHost("smtp.163.com");
//		info.setFormName("yazisama_mz@163.com");
//		info.setFormPassword("zmz!@12");
//		info.setReplayAddress("yazisama_mz@163.com");
//		info.setToAddress("2398590854@qq.com");
//		info.setSubject("bbs 测试邮件");
//		info.setContent("这是一封测试邮件");
//		sendTextMail(info);
//	}*/
//	
//	public static void main(String[] args) throws Exception {
//		StringBuffer str = new StringBuffer();
//		str.append("尊敬的\n");
//		str.append("  你好！这封信是由 惠州市海洋与项目管理系统发送的。\n");
//		str.append("您收到这封邮件，是由于在 惠州市海洋与项目管理系统 进行了新用户注册。\n");
//		str.append("你的注册资料为 单位：xxxx，账户:xxxx。\n");
//		str.append("由于新注册的账号需要等待主管单位审核通过后才能正式登陆系统，请耐心等候，如审核通过或者不通过，都将会收到邮件通知。也可以在项目管理系统首页的账号查询了解账号状态。\n");
//		str.append("如急需审核，请电话联系当地主管单位进行审批。谢谢！\n");
//		str.append("如果您并没有访问过 惠州市海洋与项目管理系统，或没有进行上述操作，请忽略这封邮件。您不需要退订或进行其他进一步的操作。\n");
//		str.append("感谢您的访问，祝您使用愉快！\n");
//		str.append("此致\n");
//		str.append("惠州市海洋与渔业局。\n");
//		str.append("http://hyxmgl.huizhou.gov.cn/");
//		System.out.println(str);
//		/*MailInfo info = new MailInfo();
//		info.setToAddress("2398590854@qq.com");
//		info.setSubject("bbs 测试邮件");
//		info.setContent("这是一封测试邮件");
//		sendTextMail(info);*/
//	}
//}
