package com.teemlink.saas.weioa.security.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.constans.Web;
import cn.myapps.core.security.action.LoginAction;
import cn.myapps.core.security.action.LoginHelper;
import cn.myapps.core.sso.LoginAuthenticator;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import cn.myapps.util.WebCookies;

public class WeioaLoginAction extends LoginAction {

	public WeioaLoginAction() throws Exception {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5186853202587656989L;
	
	public String doLogin() {
		// file and to also use the specified CallbackHandler.
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		int count=0;
		/**
		Cookie pwdErrorTimes = getErrorTimes();
		if(pwdErrorTimes!=null){
		String i = pwdErrorTimes.getValue();
		 count = Integer.parseInt(i);
		}
		**/

		try {
			HttpSession session = request.getSession();
			/**
			if (pwdErrorTimes != null && isExceedTimes(pwdErrorTimes)) {
				String code = (String) session.getAttribute(Web.SESSION_ATTRIBUTE_CHECKCODE);
				if (this.checkcode == null || !this.checkcode.equalsIgnoreCase(code)) {
					throw new Exception("{*[core.security.character.error]*}");
				}
			}
			**/

			//LoginAuthenticator loginAuthenticator = getLoginAuthenticator(request, response);
			//UserVO user = loginAuthenticator.validateLogin(this.getDomainName(),this.getUsername(), this.getPassword(),count);
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = userProcess.login(this.getUsername());
			if (user != null) {
				if(!Security.decryptPassword(user.getLoginpwd()).equals(this.getPassword())){
					throw new OBPMValidateException("{*[core.user.password.error]*}");
				}
				if(user.getLockFlag()==0){
					throw new OBPMValidateException("{*[LockedAccount]*}");
				}
				if(user.getDimission()==0){
					throw new OBPMValidateException("{*[cn.myapps.core.user.isdimissioned]*}");
				}
				
				LoginHelper.initWebUser(request, user, "", user.getDomain().getName());
				
				if (!StringUtil.isBlank(this.getKeepinfo())) {
					saveLoginInfo(response, request);
				} else {
					destroyLoginInfo(response, request);
				}
				getWebCooikes().destroyCookie("pwdErrorTimes");
				
				//把企业域名和账号保存到cookie 用于超时登录窗口获取企业域与账号信息
				WebCookies webCookies = getWebCooikes();
				webCookies.addCookie("_dn", user.getDomain().getName());
				webCookies.addCookie("_ac", this.getUsername());
				
				//选中“记住登陆信息”时，把登陆信息存放到Cookie中。
				String remenberMe =request.getParameter("remenberMe");
				if("1".equals(remenberMe)){
					String verify_login = toEncryption(this.getUsername(),this.getPassword());
					webCookies.addCookie("verify_login", verify_login);
				}
				
				if(this.isAgent(user)){
					if (this.getMyHandleUrl() != null && !this.getMyHandleUrl().equals("null")){
						request.setAttribute("myHandleUrl", this.getMyHandleUrl());
					}
					return "proxy";
				}
				initApplication();
				
				if("true".equals(user.getDomainUser())){
					return "controlPanel";
				}
			}else{
				throw new OBPMValidateException("{*[core.user.notexist]*}");
			}
			
		} catch (Exception e) {
			this.addFieldError("1", e.getMessage());
			/**
			increaseErrorTimes(); // cookie中增加出错次数
			if (isExceedTimes(pwdErrorTimes))
				ServletActionContext.getRequest().setAttribute("showCode", Boolean.valueOf(true));
//			if(!(e instanceof AuthenticationException)){
				e.printStackTrace();
//			}
			destroyLoginPassword(response, request);
			**/
			request.setAttribute("failtoLogin", "failtoLogin");
			return ERROR;
		} finally {
		}
		if (this.getMyHandleUrl() != null && !this.getMyHandleUrl().equals("null")){
			request.setAttribute("myHandleUrl", this.getMyHandleUrl());
			return "success1";
		}
		return SUCCESS;

	}

	//把登陆信息加密存放到Cookie中
	public String toEncryption(String username,String password){
		String str = "{username:\""+username+"\",password:\""+password+"\"}";
		String str_base64 = new sun.misc.BASE64Encoder().encode(str.getBytes());
		int len = str_base64.length();
		String result = str_base64.substring(18,len).trim()+str_base64.substring(0,18).trim();
		return result;
	}
}
