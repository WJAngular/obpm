package cn.myapps.core.email.email.action;

import java.util.Date;

import javax.mail.AuthenticationFailedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.constans.Web;
import cn.myapps.core.email.email.ejb.EmailProcess;
import cn.myapps.core.email.email.ejb.EmailUser;
import cn.myapps.core.email.email.ejb.EmailUserProcess;
import cn.myapps.core.email.runtime.mail.AuthProfile;
import cn.myapps.core.email.runtime.mail.ConnectionMetaHandler;
import cn.myapps.core.email.runtime.mail.ImapProtocolImpl;
import cn.myapps.core.email.runtime.mail.ProtocolFactory;
import cn.myapps.core.email.util.Constants;
import cn.myapps.core.email.util.EmailConfig;
import cn.myapps.core.email.util.EmailProcessUtil;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;

public class EmailUserHelper {

	private static final Logger LOG = Logger.getLogger(EmailUserHelper.class);
	
	/**
	 * 登录邮件系统
	 * @param webUser
	 * @throws Exception
	 * @throws SecurityException
	 */
	public static void loginEmailSystem(WebUser webUser) throws Exception, SecurityException {
		try {
			if (!EmailConfig.isUserEmail()) {
				return;
			}
			EmailUserProcess emailUserProcess = (EmailUserProcess) ProcessFactory.createProcess(EmailUserProcess.class);
			EmailUser emailUser = emailUserProcess.getEmailUserByOwner(webUser.getId(), webUser.getDomainid());
			if (emailUser == null && EmailConfig.isInternalEmail()) {
				emailUser = new EmailUser();
				emailUser.setAccount(webUser.getLoginno());
				emailUser.setName(webUser.getName());
				emailUser.setCreateDate(new Date());
				emailUser.setDomainid(webUser.getDomainid());
				emailUser.setOwnerid(webUser.getId());
				emailUserProcess.doCreate(emailUser);
			}
			EmailUserHelper.loginEmailSystem(webUser, emailUser);
		} catch (Exception e) {
			LOG.error("#########邮件系统登录失败！<Login failed mail system!>#########");
			if (webUser != null) {
				webUser.setEmailUser(null);
			}
			throw new OBPMValidateException("Login failed mail system");
		}
	}
	
	/**
	 * 登录邮件系统
	 * @param webUser
	 * @throws Exception
	 * @throws SecurityException
	 */
	public static void loginEmailSystem(WebUser webUser, EmailUser emailUser) throws Exception, SecurityException {
		try {
			if (!EmailConfig.isUserEmail()) {
				return;
			}
			if (webUser == null) {
				throw new SecurityException("{*[page.timeout]*}");
			}
//			if (emailUser != null && !EmailConfig.isInternalEmail()) {
//				emailUser.setPassword(Security.decodeBASE64(emailUser.getPassword()));
//			}
			webUser.setEmailUser(emailUser);
		} catch (Exception e) {
			LOG.error("#########邮件系统登录失败！<Login failed mail system!>#########");
			if (webUser != null) {
				webUser.setEmailUser(null);
			}
			throw new OBPMValidateException("Login failed mail system");
		}
	}
	
	public static void initConnectionHandler(WebUser webUser) throws Exception {
		if (webUser == null || webUser.getEmailUser() == null) {
			throw new SecurityException("{*[page.timeout]*}");
		}
		/*if (EmailConfig.isInternalEmail()) {
			throw new Exception("{*[core.email.internal.error]*}");
		}*/
		AuthProfile authProfile = new AuthProfile();
		authProfile.setUserName(webUser.getEmailUser().getAccount());
		authProfile.setPassword(webUser.getEmailUser().getPassword());
		ConnectionMetaHandler handler = new ConnectionMetaHandler();
		ProtocolFactory factory = new ProtocolFactory(EmailConfig.getConnectionProfile(), authProfile, handler);
		ImapProtocolImpl protocol = (ImapProtocolImpl) factory.getImapProtocol(Constants.DEFAULT_FOLDER_INBOX);
		handler = protocol.connect(Constants.CONNECT_TYPE_READ_WRITE);
		webUser.setConnectionMetaHandler(handler);
		// 但邮件文件夹不存在是，是否创建
		//EmailFolderProcess folderProcess = (EmailFolderProcess) EmailProcessUtil.createProcess(EmailFolderProcess.class, webUser);
		//folderProcess.initCreatDefaultMailFolder();
	}
	
	/**
	 * 退出邮件系统
	 * @param session
	 */
	public static void logoutEmailSystem(HttpSession session) {
		WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (webUser != null) {
			webUser.disconnectOfEmail();
		}
	}
	
	/**
	 * 检查和创建邮件用户
	 * @param user
	 * @param request
	 * @throws Exception
	 */
	public static void checkAndCreateEmailUser(UserVO user, HttpServletRequest request) throws Exception {
		if (EmailConfig.isUserEmail()) {
			EmailUserProcess eup = (EmailUserProcess) EmailProcessUtil.createProcess(EmailUserProcess.class, request);
			if (EmailConfig.isInternalEmail()) {
				EmailUser emailUser = eup.getEmailUserByOwner(user.getId(), user.getDomainid());
				if (emailUser == null) {
					emailUser = new EmailUser();
					emailUser.setAccount(user.getLoginno());
					emailUser.setName(user.getName());
					emailUser.setPassword(user.getLoginpwd());
					emailUser.setOwnerid(user.getId());
					emailUser.setDomainid(user.getDomainid());
					emailUser.setDefaultDepartment(user.getDefaultDepartment());
					eup.doCreateEmailUser(emailUser);
				} else {
					if (!emailUser.getOwnerid().equals(user.getId())) {
						// 该内部邮件账号已经被其他用户创建
						throw new OBPMValidateException("{*[core.email.account.create.error]*}: " + user.getLoginno() + "@" + EmailConfig.getEmailDomain());
					} else {
						emailUser.setName(user.getName());
						emailUser.setDefaultDepartment(user.getDefaultDepartment());
						eup.doUpdate(emailUser);
					}
				}
			} else {
				
			}
		}
	}
	
	/**
	 * 删除邮件用户，必须在删除用户之前删除
	 * @param ids 用户id
	 * @param request
	 * @throws Exception
	 */
	public static void removeEmailUsers(String[] ids, HttpServletRequest request) throws Exception {
		if (EmailConfig.isUserEmail() && ids != null) {
			//HttpSession session = request.getSession();
			EmailUserProcess eup = (EmailUserProcess) EmailProcessUtil.createProcess(EmailUserProcess.class, request);
			EmailProcess ep = (EmailProcess) ProcessFactory.createProcess(EmailProcess.class);
			UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			if (EmailConfig.isInternalEmail()) {
				for (int i = 0; i < ids.length; i++) {
					UserVO user = (UserVO) up.doView(ids[i]);
					if (user != null) {
						EmailUser euser = eup.getEmailUserByOwner(user.getId(), user.getDomainid());
						if (euser != null) {
							ep.doRemoveEmailByEmailUser(euser.getId());
							eup.doRemove(euser);
						}
					}
				}
			} else {
			}
		}
	}
	
	public static String checkLogin(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (webUser == null || webUser.getEmailUser() == null) {
			String errorUrl = "/portal/email/user/view.action";
			if (!EmailConfig.isUserEmail()) {
				errorUrl = "/portal/share/error.jsp?error=" + Constants.getText(session, "core.email.system.use.email.error");
			} else if (EmailConfig.isInternalEmail()) {
				errorUrl = "/portal/share/error.jsp?error=" + Constants.getText(session, "core.email.cannot.find.user");
			}
			return errorUrl;
		}
		try {
			if (!EmailConfig.isInternalEmail()) {
				if (webUser.getConnectionMetaHandler() == null) {
					throw new OBPMValidateException("Can't connection email server");
				}
			}
		} catch (AuthenticationFailedException e) { 
			return "/portal/email/user/view.action?error={*[core.user.password.error]*}: " + e.getMessage();
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append(Constants.getText(session, "core.email.function.init.error"));
			if (e.getMessage() != null) {
				sb.append(": ").append(e.getMessage());
			}
			sb.append("！");
			return "/portal/email/user/view.action?error=" + sb.toString();
		}
		return null;
	}
	
}
