package cn.myapps.core.email.email.action;

import javax.mail.AuthenticationFailedException;

import org.apache.log4j.Logger;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.constans.Web;
import cn.myapps.core.email.email.ejb.EmailUser;
import cn.myapps.core.email.email.ejb.EmailUserProcess;
import cn.myapps.core.email.util.EmailConfig;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class EmailUserAction extends BaseAction<EmailUser> {

	private static final long serialVersionUID = 4943705676604508099L;
	private static final Logger log = Logger.getLogger(EmailUserAction.class);
	
	private String _password;
	
	/**
	 * 默认构造方法
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public EmailUserAction() throws Exception {
		super(ProcessFactory.createProcess(EmailUserProcess.class), new EmailUser());
	}
	
	@Override
	public String doView() {
		try {
//			String ownerid = getParams().getParameterAsString("ownerid");
			WebUser webUser = getUser();
			EmailUser emailUser = null;
			if (webUser.getEmailUser() != null) {
				emailUser = webUser.getEmailUser();
			} else {
				emailUser = ((EmailUserProcess)process).getEmailUserByOwner(webUser.getId(), webUser.getDomainid());
			}
			super.setContent(emailUser);
			String error = getParams().getParameterAsString("error");
			if (!StringUtil.isBlank(error)) {
				throw new OBPMValidateException(error);
			}
//			if (StringUtil.isBlank(ownerid)) {
//				throw new Exception("Can't find e-mail user");
//			}
			
			return SUCCESS;
		} catch (OBPMValidateException e) {
			log.warn(e);
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		} catch (Exception e) {
			log.warn(e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	@Override
	public String doSave() {
		//HttpServletResponse response = ServletActionContext.getResponse();
		try {
			EmailUser emailUser = (EmailUser) getContent();
			if (StringUtil.isBlank(emailUser.getAccount())) {
				throw new OBPMValidateException("{*[page.name.notexist]*}");
			}
			String[] strs = emailUser.getAccount().split("@");
			if (strs.length > 2) {
				throw new OBPMValidateException("{*[core.name.illegal]*}");
			} else if (strs.length == 2) {
				if (!EmailConfig.getEmailDomain().equals(strs[1])) {
					throw new OBPMValidateException("{*[core.email.domain.error]*}");
				}
				emailUser.setAccount(strs[0]);
			}
			WebUser webUser = getUser();
			emailUser.setDomainid(webUser.getDomainid());
			if (StringUtil.isBlank(emailUser.getId())) {
				process.doCreate(emailUser);
			} else {
				process.doUpdate(emailUser);
			}
			//emailUser.setPassword(Security.decodeBASE64(emailUser.getPassword()));
			EmailUserHelper.loginEmailSystem(webUser, emailUser);
			if (!EmailConfig.isInternalEmail()) {
				if (webUser.getConnectionMetaHandler() == null) {
					throw new Exception();
				}
			}
			return SUCCESS;
			//ResponseUtil.setTextToResponse(response, SUCCESS);
		} catch (AuthenticationFailedException e) { 
			this.addFieldError("1", "{*[core.user.password.error]*}: " + e.getMessage());
		}catch (OBPMValidateException e) { 
			this.addFieldError("1", "{*[core.user.password.error]*}: " + e.getValidateMessage());
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return INPUT;
	}
	
	@Override
	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}

	/**
	 * @return the _password
	 */
	public String get_password() {
		//EmailUser emailUser = (EmailUser) getContent();
		//if (emailUser != null && 
		//		!StringUtil.isBlank(emailUser.getPassword())) {
		//	return Web.DEFAULT_SHOWPASSWORD;
		//} 
		return "";
	}

	/**
	 * @param password the _password to set
	 */
	public void set_password(String password) {
		_password = password;
		EmailUser emailUser = (EmailUser) getContent();
		if (emailUser != null) {
			emailUser.setPassword(_password);
		}
	}
	
}
