package cn.myapps.core.security.action;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.sso.LoginAuthenticator;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import cn.myapps.util.UsbKeyUtil;
import cn.myapps.util.WebCookies;
import cn.myapps.util.http.ResponseUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.property.PropertyUtil;


public class LoginAction extends ActionSupport {
	private static final Logger LOG = Logger.getLogger(LoginAction.class);

	private String username;

	private String password;

	private String domainName;

	private String checkcode;

	private String keepinfo;

	private String defaultApplication;

	private String _proxyUser;

	private String _userType;

	private String returnUrl;

	private WebCookies webCookies;

	private String application;
	
	/**
	 * 手机验证码
	 */
	private String smsCheckCode;
	
	/**
	 * 验证码超时时间
	 */
	private String timeout;
	
	private String myHandleUrl;
	
	/**
	 * USBKey签名数据的模
	 */
	private String signText;

	public String getSignText() {
		return signText;
	}

	public void setSignText(String signText) {
		this.signText = signText;
	}

	/**
	 * 处理的url
	 * @return
	 */
	public String getMyHandleUrl() {
		return myHandleUrl;
	}

	public void setMyHandleUrl(String myHandleUrl) {
		this.myHandleUrl = myHandleUrl;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getReturnUrl() {
		if (StringUtil.isBlank(returnUrl)) return "";
		try {
			return URLEncoder.encode(returnUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.warn(e);
			return "";
		}
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	private static final long serialVersionUID = -4277772173056045618L;

	public LoginAction() throws Exception {
	}
	
	/**
	 * 单点登录调用方法, 此方法会被SecurityFilter拦截
	 * @return
	 * @throws Exception 
	 */
	public String doSSO() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user != null) {
			if(this.isAgent(user)){
				if (myHandleUrl != null && !myHandleUrl.equals("null")){
					request.setAttribute("myHandleUrl", myHandleUrl);
				}
				if ("cn.myapps.core.sso.ADUserSSO".equals(PropertyUtil
						.get(Web.SSO_IMPLEMENTATION))){
					return "adssoProxy";
				}else{
					return "proxy";
				}
			}
			// 不设置application会导致异常
			initApplication();
			if("true".equals(DefaultProperty.getProperty("saas")) && "true".equals(user.getDomainUser())){
				return "controlPanel";
			}
			if(getApplication() == null){
				//addFieldError("1", "{*[core.domain.user.noapp]*}");
				request.setAttribute("errorMsg", "{*[core.domain.user.noapp]*}");
				return INPUT;
			}
			return SUCCESS;
		}
		
		return INPUT;
	}
	
	public String doUrlLogin() {
		try {
			Cookie pwdErrorTimes = getErrorTimes();
			int count = Integer.parseInt(pwdErrorTimes.getValue());
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			LoginAuthenticator loginAuthenticator = getLoginAuthenticator(request, response);
			loginAuthenticator.validateLogin(domainName, username, password,count);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseUtil.setTextToResponse(ServletActionContext.getResponse(), e.getMessage());
		}

		ResponseUtil.setTextToResponse(ServletActionContext.getResponse(), SUCCESS);
		return NONE;
	}

	/**
	 * 登录
	 * @SuppressWarnings webwork不支持泛型
	 * @return "SUCCESS","ERROR"
	 * @throws Exception
	 */
	public String doLogin() {
		// file and to also use the specified CallbackHandler.
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		Cookie pwdErrorTimes = getErrorTimes();
		int count=0;
		if(pwdErrorTimes!=null){
		String i = pwdErrorTimes.getValue();
		 count = Integer.parseInt(i);
		}

		try {
			HttpSession session = request.getSession();

			if (pwdErrorTimes != null && isExceedTimes(pwdErrorTimes)) {
				String code = (String) session.getAttribute(Web.SESSION_ATTRIBUTE_CHECKCODE);
				if(code !=null && !code.equalsIgnoreCase(this.checkcode)){
					throw new Exception("{*[core.security.character.error]*}");
				}
			}
			
			LoginAuthenticator loginAuthenticator = getLoginAuthenticator(request, response);
			UserVO user = loginAuthenticator.validateLogin(domainName, username, password,count);
			if (user != null) {
				if (!StringUtil.isBlank(keepinfo)) {
					saveLoginInfo(response, request);
				} else {
					destroyLoginInfo(response, request);
				}
				getWebCooikes().destroyCookie("pwdErrorTimes");
				
				//把企业域名和账号保存到cookie 用于超时登录窗口获取企业域与账号信息
				WebCookies webCookies = getWebCooikes();
				webCookies.addCookie("_dn", domainName);
				webCookies.addCookie("_ac", username);
				
				if(LoginHelper.smsVerificationRequired(LoginHelper.getIpAddr(request))){
					session.setAttribute(Web.SESSION_ATTRIBUTE_TEMP_FRONT_USER, session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER));
					session.removeAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
					return "smsauth";
				}
				
				if(LoginHelper.usbKeyVerificationRequired(LoginHelper.getIpAddr(request))){//是否需要UK身份验证
					session.setAttribute(Web.SESSION_ATTRIBUTE_TEMP_FRONT_USER, session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER));
					session.removeAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
					return "usbkeyAuth";
				}

				if(this.isAgent(user)){
					if (myHandleUrl != null && !myHandleUrl.equals("null")){
						request.setAttribute("myHandleUrl", myHandleUrl);
					}
					return "proxy";
				}
				initApplication();
				
				if(getApplication() == null){
					String uri = request.getRequestURI();
					
					NUser nUser = (NUser)request.getSession().getAttribute(NUser.SESSION_ATTRIBUTE_FRONT_USER);
					
					if(uri.contains("/kmlogin.action")){
						if (nUser==null || nUser.getKmRoles()==null || nUser.getKmRoles().size()<=0) {
							//当没有默认软件，且不是KM用户
							addFieldError("1", "{*[core.domain.user.noapp]*}");
							return INPUT;
	
						}
					}else {
						addFieldError("1", "{*[core.domain.user.noapp]*}");
						return INPUT;
					}
				}
			}
			
		} catch (Exception e) {
			this.addFieldError("1", e.getMessage());
			increaseErrorTimes(); // cookie中增加出错次数
			if (isExceedTimes(pwdErrorTimes))
				ServletActionContext.getRequest().setAttribute("showCode", Boolean.valueOf(true));
//			if(!(e instanceof AuthenticationException)){
				e.printStackTrace();
//			}
			destroyLoginPassword(response, request);
			request.setAttribute("failtoLogin", "failtoLogin");
			return ERROR;
		} finally {
		}
		// 判断是否是已debug形式登录
		String debug = ServletActionContext.getRequest().getParameter("debug");
		if("true".equals(debug)){
			ServletActionContext.getContext().getSession().put(Web.SESSION_ATTRIBUTE_DEBUG, debug);
			return "debug";
		}
		if (myHandleUrl != null && !myHandleUrl.equals("null")){
			request.setAttribute("myHandleUrl", myHandleUrl);
			return "success1";
		}
		return SUCCESS;

	}
	
	
	public String doLoginWithCiphertext() {
		// file and to also use the specified CallbackHandler.
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		Cookie pwdErrorTimes = getErrorTimes();
		int count=0;
		if(pwdErrorTimes!=null){
		String i = pwdErrorTimes.getValue();
		 count = Integer.parseInt(i);
		}

		try {
			HttpSession session = request.getSession();

			if (pwdErrorTimes != null && isExceedTimes(pwdErrorTimes)) {
				String code = (String) session.getAttribute(Web.SESSION_ATTRIBUTE_CHECKCODE);
				if(code !=null && !code.equalsIgnoreCase(this.checkcode)){
					throw new Exception("{*[core.security.character.error]*}");
				}
			}
			
			if(password!=null && password.length()>2){
				String lp = password.substring(0, password.length()-2);
				String rp = password.substring(password.length()-2,password.length()); 
				password = Security.decodeBASE64(rp+lp);
			}

			LoginAuthenticator loginAuthenticator = getLoginAuthenticator(request, response);
			UserVO user = loginAuthenticator.validateLogin(domainName, username, password,count);
			if (user != null) {
				if (!StringUtil.isBlank(keepinfo)) {
					saveLoginInfo(response, request);
				} else {
					destroyLoginInfo(response, request);
				}
				getWebCooikes().destroyCookie("pwdErrorTimes");
				
				//把企业域名和账号保存到cookie 用于超时登录窗口获取企业域与账号信息
				WebCookies webCookies = getWebCooikes();
				webCookies.addCookie("_dn", domainName);
				webCookies.addCookie("_ac", username);
				
				if(LoginHelper.smsVerificationRequired(LoginHelper.getIpAddr(request))){
					session.setAttribute(Web.SESSION_ATTRIBUTE_TEMP_FRONT_USER, session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER));
					session.removeAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
					return "smsauth";
				}
				
				if(LoginHelper.usbKeyVerificationRequired(LoginHelper.getIpAddr(request))){//是否需要UK身份验证
					session.setAttribute(Web.SESSION_ATTRIBUTE_TEMP_FRONT_USER, session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER));
					session.removeAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
					return "usbkeyAuth";
				}

				if(this.isAgent(user)){
					if (myHandleUrl != null && !myHandleUrl.equals("null")){
						request.setAttribute("myHandleUrl", myHandleUrl);
					}
					return "proxy";
				}
				initApplication();
				
				if(getApplication() == null){
					String uri = request.getRequestURI();
					
					NUser nUser = (NUser)request.getSession().getAttribute(NUser.SESSION_ATTRIBUTE_FRONT_USER);
					
					if(uri.contains("/kmlogin.action")){
						if (nUser==null || nUser.getKmRoles()==null || nUser.getKmRoles().size()<=0) {
							//当没有默认软件，且不是KM用户
							addFieldError("1", "{*[core.domain.user.noapp]*}");
							return INPUT;
	
						}
					}else {
						addFieldError("1", "{*[core.domain.user.noapp]*}");
						return INPUT;
					}
				}
			}
			
		} catch (Exception e) {
			this.addFieldError("1", e.getMessage());
			increaseErrorTimes(); // cookie中增加出错次数
			if (isExceedTimes(pwdErrorTimes))
				ServletActionContext.getRequest().setAttribute("showCode", Boolean.valueOf(true));
//			if(!(e instanceof AuthenticationException)){
				e.printStackTrace();
//			}
			destroyLoginPassword(response, request);
			request.setAttribute("failtoLogin", "failtoLogin");
			return ERROR;
		} finally {
		}
		// 判断是否是已debug形式登录
		String debug = ServletActionContext.getRequest().getParameter("debug");
		if("true".equals(debug)){
			ServletActionContext.getContext().getSession().put(Web.SESSION_ATTRIBUTE_DEBUG, debug);
			return "debug";
		}
		if (myHandleUrl != null && !myHandleUrl.equals("null")){
			request.setAttribute("myHandleUrl", myHandleUrl);
			return "success1";
		}
		return SUCCESS;

	}
	
	/**
	 * 手机短信验证
	 * @return
	 * @throws Exception
	 */
	public String doSMSAuthLogin() throws Exception {
		Cookie pwdErrorTimes = getErrorTimes();
		HttpServletRequest request = ServletActionContext.getRequest();
//		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		try{
			String code = (String) session.getAttribute(Web.SESSION_ATTRIBUTE_SMSCHECKCODE);
			if (this.smsCheckCode == null || !this.smsCheckCode.equalsIgnoreCase(code)) {
				session.removeAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
				throw new Exception("{*[core.security.character.error]*}");
			}
			session.removeAttribute(Web.SESSION_ATTRIBUTE_SMSCHECKCODE);
			if(this.isAgent(this.getUser())){
				return "proxy";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("1", e.getMessage());
			increaseErrorTimes(); // cookie中增加出错次数
			if (isExceedTimes(pwdErrorTimes))
				ServletActionContext.getRequest().setAttribute("showCode", Boolean.valueOf(true));

			return ERROR;
		} finally {
		}
		// 判断是否是已debug形式登录
		String debug = ServletActionContext.getRequest().getParameter("debug");
		ServletActionContext.getContext().getSession().put(Web.SESSION_ATTRIBUTE_DEBUG, debug);
		return SUCCESS;
	}
	
	/**
	 * 判断当前用户是否为其他用户的合法代理人
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean isAgent(BaseUser user) throws Exception{
		for (Iterator<UserVO> iterator = get_proxyUsers(user).iterator(); iterator.hasNext();) {
			UserVO userVO = (UserVO) iterator.next();
			//判断代理人是否代理日期过期
			if(userVO.getStartProxyTime()!=null && userVO.getEndProxyTime()!=null){
				Date now = new Date();
				//过滤代理日期过期代理人
				if(now.after(userVO.getStartProxyTime()) && now.before(userVO.getEndProxyTime()))
				if(user.getId().equals(userVO.getProxyUser().getId()))
					return true;
			}else{
				if(user.getId().equals(userVO.getProxyUser().getId()))
					return true;
			}
		}
		return false;
	}

	private Cookie getErrorTimes() {
		WebCookies webCookies = getWebCooikes();
		Cookie pwdErrorTimes = webCookies.getCookie("pwdErrorTimes");

		return pwdErrorTimes;
	}

	private void increaseErrorTimes() {
		WebCookies webCookies = getWebCooikes();
		Cookie pwdErrorTimes = webCookies.getCookie("pwdErrorTimes");

		try {
			if (pwdErrorTimes != null) {
				int times = Integer.valueOf(webCookies.getValue("pwdErrorTimes"));
				webCookies.addCookie("pwdErrorTimes", (times + 1) + "", 60 * 60 * 24);
			} else {
				webCookies.addCookie("pwdErrorTimes", "1", 60 * 60 * 24);
			}
		} catch (NumberFormatException e) {
			LOG.warn("increaseErrorTimes", e);
		}

	}

	/**
	 * 保存登录信息
	 * 
	 * @param response
	 * @param request
	 */
	public void saveLoginInfo(HttpServletResponse response, HttpServletRequest request) {
		WebCookies webCookies = getWebCooikes();
		int maxAge = 60 * 60 * 24 * 15;
		if (!StringUtil.isBlank(getDomainName())) {
			webCookies.addCookie("domainName", getDomainName(), maxAge);
		}
		if (!StringUtil.isBlank(getUsername())) {
			webCookies.addCookie("account", getUsername(), maxAge);
		}
		if (!StringUtil.isBlank(getPassword())) {
			webCookies.addCookie("password", getPassword(), maxAge);
		}
		if (!StringUtil.isBlank(getKeepinfo())) {
			webCookies.addCookie("keepinfo", getKeepinfo(), maxAge);
		}
	}

	/**
	 * 销毁登录信息
	 * 
	 * @param response
	 * @param request
	 */
	public void destroyLoginInfo(HttpServletResponse response, HttpServletRequest request) {
		WebCookies webCookies = getWebCooikes();
		webCookies.destroyCookie("domainName");
		webCookies.destroyCookie("account");
		webCookies.destroyCookie("password");
		webCookies.destroyCookie("keepinfo");
	}

	/**
	 * 销毁登陆密码
	 * @param response
	 * @param request
	 */
	private void destroyLoginPassword(HttpServletResponse response, HttpServletRequest request){
		WebCookies webCookies = getWebCooikes();
		webCookies.destroyCookie("password");
	}
	
	/**
	 * @SuppressWarnings webwork不支持泛型
	 * @return
	 */
	public String doLoginProxy() {
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			// 判断是否是已debug形式登录
			String debug = ServletActionContext.getRequest().getParameter("debug");
			if (debug != null && debug.trim().length() > 0)
				ServletActionContext.getContext().getSession().put(Web.SESSION_ATTRIBUTE_DEBUG, debug);
			if (_userType.equals("0")) {
				initApplication();
				if (myHandleUrl != null && !myHandleUrl.equals("null")){
					request.setAttribute("myHandleUrl", myHandleUrl);
					return "success1";
				}
				return SUCCESS;
			}
			if (StringUtil.isBlank(_proxyUser)) {
				throw new Exception("{*[page.user.notChoose]*}");
			}
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = (UserVO) process.doView(_proxyUser);
			WebUser oldUser = getUser();
			String oldName = oldUser.getName();
			if (user.getStatus() != 0) {
				if (user.getProxyUser() != null && user.getProxyUser().getId().equals(oldUser.getId())) {
					LoginHelper.initWebUser(ServletActionContext.getRequest(), user, defaultApplication, "");
					WebUser webUser = (WebUser) ServletActionContext.getRequest().getSession().getAttribute(
							Web.SESSION_ATTRIBUTE_FRONT_USER);
					webUser.setName(user.getName() + "[" + oldName + "]");
					initApplication();
				}
			} else {
				this.addFieldError("1", "{*[BeProxyUser]*}{*[Disable]*}");
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("1", e.getMessage());
			return ERROR;
		}
		if (!StringUtil.isBlank(myHandleUrl) && !myHandleUrl.equals("null")){
			request.setAttribute("myHandleUrl", myHandleUrl);
			return "success1";
		}
		return SUCCESS;
	}
	
	public String doSMSAuth() throws Exception {
		try {
			timeout = String.valueOf(LoginHelper.sendSMSCheckCode(this.getTempUser(), ServletActionContext.getRequest()));
		} catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("1", e.getMessage());
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 手机短信验证
	 * @return
	 * @throws Exception
	 */
	public String doLoginSMS() throws Exception {
		WebUser user = this.getTempUser();
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			String sCheckCode = String.valueOf(session.getAttribute(Web.SESSION_ATTRIBUTE_SMSCHECKCODE));
			if(user ==null || StringUtil.isBlank(smsCheckCode) || StringUtil.isBlank(sCheckCode) || !smsCheckCode.equals(sCheckCode)){
				session.removeAttribute(Web.SESSION_ATTRIBUTE_TEMP_FRONT_USER);
				throw new Exception("手机验证码校验失败！");
			}
			session.removeAttribute(Web.SESSION_ATTRIBUTE_SMSCHECKCODE);
			session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, user);
			session.removeAttribute(Web.SESSION_ATTRIBUTE_TEMP_FRONT_USER);
			initApplication();
		} catch (Exception e) {
			this.addFieldError("1", e.getMessage());
			return INPUT;
		}
		if(this.isAgent(user)){
			return "proxy";
		}
		
		return SUCCESS;
		
		
	}
	
	public String doUsbKeyAuth() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * USBKey身份验证
	 * @return
	 * @throws Exception
	 */
	public String doLoginUsbKey() throws Exception {
		WebUser user = this.getTempUser();
		HttpSession session = ServletActionContext.getRequest().getSession();
		try {
			String plainText = (String)session.getAttribute(Web.UK_AUTH_RANDOM_CODE);
			if(user ==null || StringUtil.isBlank(plainText)){
				throw new Exception("非法访问");
			}
			if(StringUtil.isBlank(user.getPublicKey())){
				throw new Exception("您的账号还没有绑定USBKey");
			}
			
			if(UsbKeyUtil.verify(user.getPublicKey(), plainText, getSignText())){
				session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, user);
				session.removeAttribute(Web.SESSION_ATTRIBUTE_TEMP_FRONT_USER);
				session.removeAttribute(Web.UK_AUTH_RANDOM_CODE);
				initApplication();
			}else {
				throw new Exception("USBKey验证失败");
			}
			
			
		} catch (Exception e) {
			session.removeAttribute(Web.SESSION_ATTRIBUTE_TEMP_FRONT_USER);
			session.removeAttribute(Web.UK_AUTH_RANDOM_CODE);
			this.addFieldError("1", e.getMessage());
			return INPUT;
		}
		if(this.isAgent(user)){
			return "proxy";
		}
		
		return SUCCESS;
	}
	
	public void initApplication() throws Exception {
		WebUser user = getUser();
		 Collection<ApplicationVO> apps = new ApplicationHelper().getListByWebUser(user);
		 for (Iterator<ApplicationVO> iterator = apps.iterator(); iterator.hasNext();) {
			ApplicationVO applicationVO = (ApplicationVO) iterator
					.next();
			if(applicationVO.getId().equals(user.getDefaultApplication())){
				setApplication(user.getDefaultApplication());
				break;
			}
		}
		 
		if(StringUtil.isBlank(this.getApplication()) && !apps.isEmpty()){
			setApplication(apps.iterator().next().getId());
		}
	}

	@SuppressWarnings("unchecked")
	public LoginAuthenticator getLoginAuthenticator(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 获取登录验证器实例

		Constructor<LoginAuthenticator> cons;
		try {
			cons = (Constructor<LoginAuthenticator>) Class.forName(PropertyUtil.get(Web.LOGIN_AUTHENTICATOR))
					.getConstructor(HttpServletRequest.class, HttpServletResponse.class);
			LoginAuthenticator loginAuthenticator = cons.newInstance(request, response);
			return loginAuthenticator;
		} catch (SecurityException e) {
			throw new Exception("Get login authenticator failed: " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new Exception("Get login authenticator failed: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new Exception("Get login authenticator failed: " + e.getMessage());
		}
	}

	public Map<String, String> get_userTypes() {
		Map<String, String> tree = new LinkedHashMap<String, String>();
		tree.put("1", "{*[Proxy]*}");
		tree.put("0", "{*[Personal]*}");
		return tree;
	}
	
	/**
	 * 获取代理人
	 * @param baseUser
	 * @return
	 * @throws Exception
	 */
	public Collection<UserVO> get_proxyUsers(BaseUser baseUser) throws Exception {
		Collection<UserVO> proxUser = new ArrayList<UserVO>();
		UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		Collection<UserVO> cols = process.queryByProxyUserId(baseUser.getId());
		if (cols != null && cols.size() > 0) {
			for (Iterator<UserVO> iterator = cols.iterator(); iterator.hasNext();) {
				UserVO user = iterator.next();
				if(user.getStartProxyTime()!=null && user.getEndProxyTime()!=null){
					Date now = new Date();
					//过滤代理日期过期代理人
					if(now.after(user.getStartProxyTime()) && now.before(user.getEndProxyTime())){
						proxUser.add(user);
					}
				}else{
					proxUser.add(user);
				}
			}
		}
		return proxUser;
	}
	
	/**
	 * 获取代理人
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> get_proxyUsers() throws Exception {
		Map<String, String> tree = new LinkedHashMap<String, String>();
		UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		Collection<UserVO> cols = process.queryByProxyUserId(getUser().getId());
		if (cols != null && cols.size() > 0) {
			for (Iterator<UserVO> iterator = cols.iterator(); iterator.hasNext();) {
				UserVO user = iterator.next();
				if(user.getStartProxyTime()!=null && user.getEndProxyTime()!=null){
					Date now = new Date();
					//过滤代理日期过期代理人
					if(now.after(user.getStartProxyTime()) && now.before(user.getEndProxyTime())){
						tree.put(user.getId(), user.getName());
					}
				}else{
					tree.put(user.getId(), user.getName());
				}
			}
		}
		return tree;
	}

	public void set_userType(String _userType) {
		this._userType = _userType;
	}

	public String get_userType() {
		if (_userType == null)
			_userType = "0";
		return _userType;
	}

	public void set_proxyUser(String proxyId) {
		_proxyUser = proxyId;
	}

	public String get_proxyUser() {
		if (_proxyUser == null)
			_proxyUser = "";
		return _proxyUser;
	}

	/**
	 * 检查错误次数
	 * 
	 * @param cookie
	 * @return
	 */
	public boolean isExceedTimes(Cookie cookie) {
		if (cookie != null) {
			try {
				String val = webCookies.getValue(cookie.getName());
				int errorTime = Integer.parseInt(val);
				if (errorTime >= 3) {
					return true;
				}
			} catch (NumberFormatException e) {
				LOG.warn("isExceedTimes", e);
			}
		}
		return false;
	}

	/**
	 * 注销
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	public String doLogout() {
		return SUCCESS;
	}

	/**
	 * 返回密码
	 * 
	 * @return 返回密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 *            密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 返回用户帐号
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户帐号
	 * 
	 * @param username
	 *            用户帐号
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String getDomainName() {
		return domainName;
	}

	public String getCheckcode() {
		return checkcode;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	public void setKeepinfo(String keepinfo) {
		this.keepinfo = keepinfo;
	}

	public String getKeepinfo() {
		return keepinfo;
	}

	public String getDefaultApplication() {
		return defaultApplication;
	}

	public void setDefaultApplication(String defaultApplication) {
		this.defaultApplication = defaultApplication;
	}

	public static ActionContext getContext() {
		return ActionContext.getContext();
	}
	
	public String getSmsCheckCode() {
		return smsCheckCode;
	}

	public void setSmsCheckCode(String smsCheckCode) {
		this.smsCheckCode = smsCheckCode;
	}

	/**
	 * Get WebUser Object.
	 * 
	 * @return WebUser Object user
	 * @throws Exception
	 */
	public WebUser getUser() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		WebUser user = null;
		if (session == null || session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER) == null) {
			UserVO vo = new UserVO();
			vo.getId();
			vo.setName("GUEST");
			vo.setLoginno("guest");
			vo.setLoginpwd("");
			vo.setRoles(null);
			vo.setEmail("");
			// vo.setLanguageType(1);
			user = new WebUser(vo);
		} else {
			user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		}
		return user;
	}

	public WebCookies getWebCooikes() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		if (webCookies == null) {
			webCookies = new WebCookies(request, response, WebCookies.ENCRYPTION_URL);
		}

		return webCookies;
	}
	
	private WebUser getTempUser(){
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_TEMP_FRONT_USER);
		return user;
	}
}
