/*
 * Copyright (c) JForum Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following  disclaimer.
 * 2)  Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 * 
 * Created on Jun 2, 2005 6:56:25 PM
 * The JForum Project
 * http://www.jforum.net
 */
package cn.myapps.core.sso;

import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.constans.Web;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.security.action.LoginHelper;
import cn.myapps.core.sysconfig.ejb.LdapConfig;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.CookieUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.property.PropertyUtil;

/**
 * General utilities to use with SSO.
 * 
 * @author Rafael Steil
 * @version $Id: SSOUtils.java,v 1.6 2006/08/20 22:47:43 rafaelsteil Exp $
 */
public class SSOUtil {
	private boolean exists = true;
	private UserVO user;
	private UserProcess process;
	private DomainProcess domainProcess;

	public SSOUtil() {
		try {
			this.process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			this.domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if an user exists in the database
	 * 
	 * @param username
	 *            The username to check
	 * @return <code>true</code> if the user exists. If <code>false</code> is
	 *         returned, then you can insert the user by calling
	 *         {@link #register(String, String)}
	 * @see #register(String, String)
	 * @see #getUser()
	 */
	public boolean userExists(String loginno, String domainName) {
		try {
			if("true".equals(DefaultProperty.getProperty("saas"))){
				this.user = process.login(loginno);
				this.exists = user != null;
			}
			else if (!StringUtil.isBlank(loginno) && !StringUtil.isBlank(domainName)) {
				this.user = process.getUserByLoginnoAndDoaminName(loginno, domainName);
				this.exists = user != null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.exists;
	}

	/**
	 * Registers a new user. This method should be used together with
	 * {@link #userExists(String)}.
	 * 
	 * @param password
	 *            the user's password. It <em>should</em> be the real / final
	 *            password. In other words, the data passed as password is the
	 *            data that'll be written to the database
	 * @param email
	 *            the user's email
	 * @throws SSOException
	 * @see #getUser()
	 */
	public void register(String loginno, String password, String domainName) throws SSOException {
		if (this.exists) {
			return;
		}

		try {
			if (StringUtil.isBlank(loginno)) {
				throw new SSOException("{*[sso.account.registration.failed]*}: account name is empty");
			}
			if (StringUtil.isBlank(domainName)) {
				throw new SSOException("{*[sso.account.registration.failed]*}: domain name is empty");
			}

			// Is a new user for us. Register him
			this.user = new UserVO();
			DomainVO domainVO = (DomainVO) domainProcess.getDomainByName(domainName);
			user.setName(loginno);
			user.setLoginno(loginno);
			user.setLoginpwd(password);
			user.setStatus(1);
			user.setDomainid(domainVO.getId());

			process.doCreate(user);
		} catch (Exception e) {
			throw new SSOException("{*[sso.account.registration.failed]*}: " + e);
		}

	}

	/**
	 * Gets the user associated to this class instance.
	 * 
	 * @return the user
	 */
	public UserVO getUser() {
		return this.user;
	}

	public WebUser checkSSO(PortletRequest request, PortletResponse response) throws SSOException {
		String ssoImplementation = "";
		try {
			ssoImplementation = PropertyUtil.get(Web.SSO_IMPLEMENTATION);

			WebUser webUser = (WebUser) request.getPortletSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER,
					PortletSession.APPLICATION_SCOPE);

			SSO sso = (SSO) Class.forName(ssoImplementation).newInstance();
			Map<String, String> userInfo = sso.authenticateUser(request);
			String loginno = userInfo.get(Web.SSO_LOGINACCOUNT_ATTRIBUTE);
			String domainName = PropertyUtil.get(LdapConfig.DOMAIN);

			if (StringUtil.isBlank(loginno)) {
				throw new Exception("{*[login.account.is.empty]*}");
			}

			// 判断登录账号是否发生改变
			if (webUser == null || !loginno.equals(webUser.getLoginno())) {
				if (!userExists(loginno, domainName)) {
					// 不存在则注册一个用户
					//register(loginno, loginno, domainName);
				}

				LoginHelper.initWebUser(request, getUser(), getUser().getDefaultApplication(), domainName);
				// if
				// ("cookie".equals(PropertyUtil.get(Web.SSO_INFO_SAVE_TYEP))) {
				// CookieUtil.setCookie(PropertyUtil.get("sso.info.key.loginAccount"),
				// Security
				// .encodeToBASE64(loginno), response);
				// CookieUtil.setCookie(PropertyUtil.get("sso.info.key.domainName"),
				// Security
				// .encodeToBASE64(domainName), response);
				// }

				return new WebUser(getUser());
			}

			return webUser;
		} catch (InstantiationException e) {
			throw new SSOException("{*[invalid.sso.implementation]*}: " + ssoImplementation);
		} catch (IllegalAccessException e) {
			throw new SSOException("{*[invalid.sso.implementation]*}: " + ssoImplementation);
		} catch (ClassNotFoundException e) {
			throw new SSOException("{*[invalid.sso.implementation]*}: " + ssoImplementation);
		} catch (Exception e) {
			throw new SSOException("{*[error.while.check.sso]*}: " + e.getMessage());
		}
	}

	public WebUser checkSSO(HttpServletRequest request, HttpServletResponse response) throws SSOException {
		String ssoImplementation = "";
		try {
			ssoImplementation = PropertyUtil.get(Web.SSO_IMPLEMENTATION);

			WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
			if(webUser != null) return webUser;
			SSO sso = (SSO) Class.forName(ssoImplementation).newInstance();
			Map<String, String> userInfo = sso.authenticateUser(request, response);
			String loginno = userInfo.get(Web.SSO_LOGINACCOUNT_ATTRIBUTE);
			String domainName = userInfo.get(Web.SSO_DOMAINNAME_ATTRIBUTE);
			if(StringUtil.isBlank(domainName)){
				domainName = PropertyUtil.get(LdapConfig.DOMAIN);
			}
			if (StringUtil.isBlank(loginno) && !(sso instanceof ADUserSSO)) {
				throw new Exception("{*[login.account.is.empty]*}");
			}

			// 判断登录账号是否发生改变
			if(!StringUtil.isBlank(loginno)){
				if (webUser == null || !loginno.equals(webUser.getLoginno())) {
					if (!userExists(loginno, domainName)) {
						// 不存在则注册一个用户
						//register(loginno, loginno, domainName);
					}

					LoginHelper.initWebUser(request, getUser(), getUser().getDefaultApplication(), domainName);
					if ("cookie".equals(PropertyUtil.get(Web.SSO_INFO_SAVE_TYEP))) {
						CookieUtil.setCookie(PropertyUtil.get("sso.info.key.loginAccount"), Security
								.encodeToBASE64(loginno), response);
						CookieUtil.setCookie(PropertyUtil.get("sso.info.key.domainName"), Security
								.encodeToBASE64(domainName), response);
					}

					return new WebUser(getUser());
				}
			}
			

			return webUser;
		} catch (InstantiationException e) {
			throw new SSOException("{*[invalid.sso.implementation]*}: " + ssoImplementation);
		} catch (IllegalAccessException e) {
			throw new SSOException("{*[invalid.sso.implementation]*}: " + ssoImplementation);
		} catch (ClassNotFoundException e) {
			throw new SSOException("{*[invalid.sso.implementation]*}: " + ssoImplementation);
		} catch (Exception e) {
			throw new SSOException(e.getMessage());
		}
	}

	/**
	 * 获取注销重定向链接
	 * 
	 * @return 注销重定向链接
	 */
	public static String getLogoutRedirect() {
		String url = "";
		if (Web.AUTHENTICATION_TYPE_SSO.equals(PropertyUtil.get(Web.AUTHENTICATION_TYPE))
				&& !StringUtil.isBlank(PropertyUtil.get(Web.SSO_LOGOUT_REDIRECT))) {
			url = PropertyUtil.get(Web.SSO_LOGOUT_REDIRECT);
		}

		return url;
	}

	/**
	 * 获取登录重定向链接
	 * 
	 * @return 登录重定向链接
	 */
	public static String getLoginRedirect() {
		String url = "";
		if (Web.AUTHENTICATION_TYPE_SSO.equals(PropertyUtil.get(Web.AUTHENTICATION_TYPE))
				&& !StringUtil.isBlank(PropertyUtil.get(Web.SSO_REDIRECT))) {
			url = PropertyUtil.get(Web.SSO_REDIRECT);
		}

		return url;
	}
	
	public static void doRedirect(HttpServletRequest request, HttpServletResponse response) {
		try {
			String returnUrl = request.getParameter("returnUrl");
			// 存在returnUrl则重定向，SSO使用
			if (!StringUtil.isBlank(returnUrl)){
				System.out.println("returnUrl: " + returnUrl);
				PropertyUtil.reload("security");
				if(returnUrl.toLowerCase().startsWith("http://") && "true".equals(PropertyUtil.get("cfg.security.url.intercept.external.address"))){
					String[] excludeAddress = PropertyUtil.get("cfg.security.url.exclude.address").split(";");
					String url = returnUrl.toLowerCase().substring(7);
					boolean falg = false;
					
					for(int i=0;i<excludeAddress.length;i++){
						if(url.startsWith(excludeAddress[i])){
							falg = true;
							break;
						}
					}
					if(!falg){
						url = request.getContextPath()+"/portal/share/security/redirect.jsp?_link="+returnUrl+"&application="+request.getParameter("application");
						response.sendRedirect(url);
						return;
					}
				}
				response.sendRedirect(returnUrl);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
