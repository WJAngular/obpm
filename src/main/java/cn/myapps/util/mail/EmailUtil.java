package cn.myapps.util.mail;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;


public class EmailUtil {

	private static final Logger log = Logger.getLogger(EmailUtil.class);
	
	public static boolean _DEBUG = true;
	private EmailSender _sender;
	private String applicationid;

	public EmailUtil() {
		this(null);
	}

	public EmailUtil(String applicationid) {
		setApplicationid(applicationid);
		_sender = EmailSender.getInstance();
	}

	public void setEmail(String from, String to, String subject, String body, String host, String user,
			String password, String bcc, boolean validate) {
		_sender.addEmail(from, to, subject, body, host, user, password, bcc, validate);
	}
	
	public void setEmail(String from, String to, String subject, String body,String[] attachFiles, String host, String user,
			String password, String bcc, boolean validate) {
		_sender.addEmail(from, to, subject, body,attachFiles,host, user, password,null, bcc, validate);
	}

	/**
	 * Send the e-mail.
	 * 
	 * @param email
	 *            The emal object.
	 * @throws Exception
	 */
	public void send() throws Exception {
		_sender.sendEmail();
	}

	public void sendMailToAllUser(String from, String subject, String content, String host, String account, String password,
			String bbc, boolean validate) throws Exception {

		try {

			HttpSession session = ServletActionContext.getRequest().getSession();
			WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
			UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			Collection<UserVO> colls = up.doQueryHasMail(webUser.getDomainid());
			for (Iterator<UserVO> iter = colls.iterator(); iter.hasNext();) {
				UserVO user = iter.next();
				String to = user.getEmail();
				if (isEmailAddress(to) && user.getStatus() == 1) {
					Email email = new Email(from, to, subject, content, host, account, password, bbc, true);
					_sender.addEmail(email);
					_sender.sendEmail();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("---------sendMailToAllUser error--------------");
		} finally {
			PersistenceUtils.closeSession();
		}
	}
	
	public void sendMailToAllUser(String from, String subject, String content,String[] attachFiles, String host, String account, String password,
			String bbc, boolean validate) throws Exception {

		try {

			HttpSession session = ServletActionContext.getRequest().getSession();
			WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
			UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			Collection<UserVO> colls = up.doQueryHasMail(webUser.getDomainid());
			for (Iterator<UserVO> iter = colls.iterator(); iter.hasNext();) {
				UserVO user = iter.next();
				String to = user.getEmail();
				if (isEmailAddress(to) && user.getStatus() == 1) {
					Email email = new Email(from, to, subject, content, host, account, password, null,bbc,attachFiles, true);
					_sender.addEmail(email);
					_sender.sendEmail();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("---------sendMailToAllUser error--------------");
		} finally {
			PersistenceUtils.closeSession();
		}
	}

	public String getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}
	
	/**
	 * 用系统配置用户发送邮件
	 * @param to 邮件接收人
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param attachFiles 附件路径
	 */
	public void sendEmailBySystemUser(String to, String subject, String content,String[] attachFiles) {
		if (isEmailAddress(to) && !StringUtil.isBlank(content)) {
			String from = PropertyUtil.getByPropName("email", "from");
			String host = PropertyUtil.getByPropName("email", "host");
			String user = PropertyUtil.getByPropName("email", "user");
			String password = PropertyUtil.getByPropName("email", "password");
			Email email = new Email(from, to, subject, content, host, user, password, null, true);
			_sender.addEmail(email);
			_sender.sendEmail();
		} else {
			log.warn("Sent email by system user error: to=" + to);
		}
	}
	
	/**
	 * 用系统配置用户发送邮件
	 * @param to 邮件接收人
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 */
	public void sendEmailBySystemUser(String to, String subject, String content) {
		if (isEmailAddress(to) && !StringUtil.isBlank(content)) {
			String from = PropertyUtil.getByPropName("email", "from");
			String host = PropertyUtil.getByPropName("email", "host");
			String user = PropertyUtil.getByPropName("email", "user");
			String password = PropertyUtil.getByPropName("email", "password");
			Email email = new Email(from, to, subject, content, host, user, password, null, true);
			_sender.addEmail(email);
			_sender.sendEmail();
		} else {
			log.warn("Sent email by system user error: to=" + to);
		}
	}
	
	public boolean sendEmailBySystemUserForTranspond(String to, String subject, String content) {
		if (isEmailAddress(to) && !StringUtil.isBlank(content)) {
			String from = PropertyUtil.getByPropName("email", "from");
			String host = PropertyUtil.getByPropName("email", "host");
			String user = PropertyUtil.getByPropName("email", "user");
			String password = PropertyUtil.getByPropName("email", "password");
			Email email = new Email(from, to, subject, content, host, user, password, null, true);
			SimpleEmailSender sender = new SimpleEmailSender(email, true);
			boolean success = sender.sendHtmlEmail();
			if (success) {
				log.info("Sent email to: " + email.getTo() + " success");
			}
			return success;
		} else {
			log.warn("Sent email by system user error: to=" + to);
			return false;
		}
	}
	
	public boolean isEmailAddress(String address) {
		if (address != null && address.trim().length() > 0) {
			Pattern p = Pattern.compile("(.*)@(.*)\\.(.*)"); // 检验Email地址
			Matcher m = p.matcher(address);
			return m.matches();
		}
		return false;
	}

	/**
	 * 示例
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		EmailUtil eUtil = new EmailUtil();
		String to = "xiuwei168@qq.com";
		String subject = "主题註冊";
		String body = "内容";
		String from = "happy@teemlink.com";
		String host = "smtp.exmail.qq.com";
		String user = "happy@teemlink.com";
		String password = "";
		String bbc = "";
		boolean validate = true;

		eUtil.setEmail(from, to, subject, body,new String[]{"D:\\附件.txt"}, host, user, password, bbc, validate);
		eUtil.send();
	}
}
