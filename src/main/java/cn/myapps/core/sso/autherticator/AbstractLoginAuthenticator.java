package cn.myapps.core.sso.autherticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.constans.Web;
import cn.myapps.core.sysconfig.ejb.AuthConfig;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.http.CookieUtil;
import cn.myapps.util.property.PropertyUtil;

public class AbstractLoginAuthenticator  {
	protected UserProcess userProcess;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected String defaultApplication;

	public UserProcess getUserProcess() {
		return userProcess;
	}

	public String getDefaultApplication() {
		return defaultApplication;
	}

	public void setDefaultApplication(String defaultApplication) {
		this.defaultApplication = defaultApplication;
	}
	
	public AbstractLoginAuthenticator() {
	}

	public AbstractLoginAuthenticator(HttpServletRequest request, HttpServletResponse response) {
		try {
			this.userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			this.request = request;
			this.response = response;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置信息到cookie或session中
	 * 
	 * @param userVO
	 * @param domainName
	 */
	protected void saveInfo(UserVO userVO, String domainName) throws Exception {
		try {
			String ssoDataEncryption = PropertyUtil.get(AuthConfig.SSO_INFO_DATA_ENCRYPTION);
			if(ssoDataEncryption.equals("base64")){
				if ("cookie".equals(PropertyUtil.get(Web.SSO_INFO_SAVE_TYEP))) {
					CookieUtil.setCookie(PropertyUtil.get(Web.SSO_INFO_KEY_LGOINACCOUNT), Security
							.encodeToBASE64(userVO.getLoginno()), response);
					CookieUtil.setCookie(PropertyUtil.get(Web.SSO_INFO_KEY_DOMAINNAME), Security
							.encodeToBASE64(domainName), response);
					CookieUtil.setCookie(PropertyUtil.get(AuthConfig.SSO_INFO_KEY_EMAIL), Security.
							encodeToBASE64(userVO.getEmail()), response);
				} else if ("session".equals(PropertyUtil.get(Web.SSO_INFO_SAVE_TYEP))) {
					request.getSession().setAttribute(PropertyUtil.get(Web.SSO_INFO_KEY_LGOINACCOUNT),
							Security.encodeToBASE64(userVO.getLoginno()));
					request.getSession().setAttribute(PropertyUtil.get(Web.SSO_INFO_KEY_DOMAINNAME),
							Security.encodeToBASE64(domainName));
					request.getSession().setAttribute(PropertyUtil.get(Web.SSO_INFO_KEY_EMAIL),  
							Security.encodeToBASE64(userVO.getEmail()));
				}
			}else if(ssoDataEncryption.equals("none")){
				if ("cookie".equals(PropertyUtil.get(Web.SSO_INFO_SAVE_TYEP))) {
					CookieUtil.setCookie(PropertyUtil.get(Web.SSO_INFO_KEY_LGOINACCOUNT), userVO.getLoginno(), response);
					CookieUtil.setCookie(PropertyUtil.get(Web.SSO_INFO_KEY_DOMAINNAME), domainName, response);
					CookieUtil.setCookie(PropertyUtil.get(AuthConfig.SSO_INFO_KEY_EMAIL), userVO.getEmail(), response);
				} else if ("session".equals(PropertyUtil.get(Web.SSO_INFO_SAVE_TYEP))) {
					request.getSession().setAttribute(PropertyUtil.get(Web.SSO_INFO_KEY_LGOINACCOUNT),userVO.getLoginno());
					request.getSession().setAttribute(PropertyUtil.get(Web.SSO_INFO_KEY_DOMAINNAME),domainName);
					request.getSession().setAttribute(PropertyUtil.get(AuthConfig.SSO_INFO_KEY_EMAIL), userVO.getEmail());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
