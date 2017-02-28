package cn.myapps.mobile2.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.WebCookies;
import cn.myapps.util.property.MultiLanguageProperty;

public class MbLoginAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7152882025902624095L;
	private WebCookies webCookies;
	private String username;
	
	/**
	 * 输出值
	 */
	private String result;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomainname() {
		return domainname;
	}

	public void setDomainname(String domainname) {
		this.domainname = domainname;
	}

	public WebCookies getWebCookies() {
		return webCookies;
	}

	public void setWebCookies(WebCookies webCookies) {
		this.webCookies = webCookies;
	}

	private String password;
	private String domainname;

	public MbLoginAction() throws Exception {

	}

	private Cookie getErrorTimes() {
		WebCookies webCookies = getWebCooikes();
		Cookie pwdErrorTimes = webCookies.getCookie("pwdErrorTimes");

		return pwdErrorTimes;
	}

	private WebCookies getWebCooikes() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		if (webCookies == null) {
			webCookies = new WebCookies(request, response,
					WebCookies.ENCRYPTION_URL);
		}

		return webCookies;
	}

	private void increaseErrorTimes() {
		WebCookies webCookies = getWebCooikes();
		Cookie pwdErrorTimes = webCookies.getCookie("pwdErrorTimes");

		try {
			if (pwdErrorTimes != null) {
				int times = Integer.valueOf(webCookies
						.getValue("pwdErrorTimes"));
				webCookies.addCookie("pwdErrorTimes", (times + 1) + "",
						60 * 60 * 24);
			} else {
				webCookies.addCookie("pwdErrorTimes", "1", 60 * 60 * 24);
			}
		} catch (NumberFormatException e) {
			LOG.warn("increaseErrorTimes", e);
		}

	}

	public String getResult() {
		return result;
	}

	/**
	 * 登录
	 * 
	 * @return "SUCCESS","ERROR"
	 * @throws Exception
	 */
	public String doLogin() throws Exception {

		Cookie pwdErrorTimes = getErrorTimes();
		int count = 0;
		if (pwdErrorTimes != null) {
			String i = pwdErrorTimes.getValue();
			count = Integer.parseInt(i);
		}
		// file and to also use the specified CallbackHandler.
		try {
			// String loginFailTimesString =
			// PropertyUtil.get(LoginConfig.LOGIN_FAIL_TIMES);
			// int loginPasswordErrortimes =
			// Integer.parseInt(loginFailTimesString);
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			UserProcess process = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);

			UserVO user = process.login(username, password, domainname, count);
			String result = "";
			if (user != null && user.getStatus() == 1) {
				WebUser webUser = new WebUser(user);
				UserVO vo = (UserVO) user.clone();
				vo.setLoginpwd(null);
				process.doUpdateWithCache(vo);
				getWebCooikes().destroyCookie("pwdErrorTimes");
				session.setAttribute(Web.SESSION_ATTRIBUTE_DOMAIN,
						webUser.getDomainid());
				String language = MultiLanguageProperty.getName(2);
				session.setAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE,
						language);
				session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);
				result = MbLoginXMLBuilder.toMobileXML(true, "",vo);
			} else {
				result = MbLoginXMLBuilder.toMobileXML(false, "{*[登录失败]*}",null);
			}
			this.result = result;
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			increaseErrorTimes();
			String result = "";
			if (e instanceof OBPMValidateException) {
				OBPMValidateException myException = (OBPMValidateException) e;
				result = MbLoginXMLBuilder.toMobileXML(false,
						myException.getValidateMessage(),null);
			}else{
				result = MbLoginXMLBuilder.toMobileXML(false,
						e.getMessage(),null);
			}
			this.result = result;
		}
		return SUCCESS;
	}

	/**
	 * 注销
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	public String doLogout() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.invalidate();
		return null;
	}

}
