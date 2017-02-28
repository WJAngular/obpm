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
 * Created on Jun 2, 2005 5:41:11 PM
 * The JForum Project
 * http://www.jforum.net
 */
package cn.myapps.core.sso.autherticator;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.security.action.LoginHelper;
import cn.myapps.core.sso.AuthenticationException;
import cn.myapps.core.sso.LoginAuthenticator;
import cn.myapps.core.sysconfig.ejb.LdapConfig;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;

/**
 * Authenticate users against a LDAP server.
 * 
 * @author Rafael Steil
 * @version $Id: LDAPAuthenticator.java,v 1.8 2006/08/20 22:47:43 rafaelsteil
 *          Exp $
 */
public class ADLoginAuthenticator extends AbstractLoginAuthenticator
		implements LoginAuthenticator {

	static {
		PropertyUtil.load("sso");
	}

	public ADLoginAuthenticator() {
	}

	public ADLoginAuthenticator(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public LdapContext getLdapContext(String userDn, String password)
			throws NamingException {
		Control[] ctl = null;
		Hashtable<String, String> env = new Hashtable<String, String>();

		String url = PropertyUtil.get(LdapConfig.LDAP_URL);
		String pooled = PropertyUtil.get(LdapConfig.LDAP_POOLED);
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		// url,ldap的地址
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, userDn);
		env.put(Context.SECURITY_CREDENTIALS, password);
		// ldap连接池
		if ("true".equals(pooled) || "false".equals(pooled))
			env.put("com.sun.jndi.ldap.connect.pool", pooled);
		return new InitialLdapContext(env, ctl);
	}

	/**
	 * @throws NamingException
	 * @see net.jforum.sso.LoginAuthenticator#validateLogin(java.lang.String,
	 *      java.lang.String, java.util.Map)
	 */
	public UserVO validateLogin(String domain, String loginno, String password,int pwdErrorTimes)
			throws AuthenticationException {
		String userDn = getUserDn(loginno);
		LdapContext ctx = null;
		try {
			ctx = getLdapContext(userDn, password);
			UserVO user = converToUser(loginno, domain);
			if (user != null && user.isActive()) {
				LoginHelper.initWebUser(request, user, defaultApplication,
						domain);
				saveInfo(user, domain);
				return user;
			}
		} catch (Exception e) {
			throw new AuthenticationException(e.getMessage());
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private UserVO converToUser(String loginno, String domainName) throws Exception {
		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
		DomainVO domain = (DomainVO) domainProcess.getDomainByDomainName(domainName);
		
		if(!StringUtil.isBlank(loginno) && domain != null){
			UserVO user = userProcess.getUserByLoginno(loginno, domain.getId());
			return user;
		}
		return null;
	}

	private String getUserDn(String loginno) {
		StringBuffer dn = new StringBuffer();
		String baseDN = PropertyUtil.get(LdapConfig.LDAP_BASEDN);
		dn = dn.append(loginno).append("@");
		if (loginno == null || baseDN == null || "".equals(baseDN)){
			return "";
		}else{
			String[] baseDNs = baseDN.split(",");
			for(int i=0; i<baseDNs.length; i++){
				dn = dn.append(baseDNs[i].substring(baseDNs[i].indexOf("=")+1)).append(".");
			}
			if(dn.length()>0){
				dn.setLength(dn.length()-1);
			}
		}
		return dn.toString();
	}

	public static void main(String[] args) throws AuthenticationException {
		ADLoginAuthenticator la = new ADLoginAuthenticator();
		UserVO user = la.validateLogin("teemlink", "keezzm", "435465",1);
		System.out.println(user.getName());
	}

}
