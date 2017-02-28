package cn.myapps.base.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.macro.runner.JsMessage;
import cn.myapps.core.security.CookieAuthValidator;
import cn.myapps.core.security.Firewall;
import cn.myapps.core.sso.SSOUtil;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.support.weixin.WeixinServiceProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.CookieUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.property.PropertyUtil;

/**
 * 
 * @author Nicholas
 * 
 */
public class SecurityFilter implements Filter {

	private final static Logger LOG = Logger.getLogger(SecurityFilter.class);

	private static boolean ACCESS_ADMIN = false;
	
	private Firewall firewall = null;
	
	static {
		try {
			ACCESS_ADMIN = Boolean.parseBoolean(DefaultProperty
					.getProperty("ACCESS_ADMIN"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		WebUser user = null;
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Environment evt = Environment.getInstance();
		if (StringUtil.isBlank(evt.getBaseUrl())) {
			String scheme = hreq.getScheme();
//			String serverAddr = hreq.getLocalAddr();
			
			String serverAddr = hreq.getServerName();
			int port = hreq.getServerPort();
			evt.setBaseUrl(scheme + "://" + serverAddr + ":" + port);
		}

		String queryString = hreq.getQueryString();

		try {
			if(firewall !=null && !firewall.excute(hreq,resp)) return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		String uri = hreq.getRequestURI();
		String mode = hreq.getParameter("mode");
		String handleUrl = "";
		
		if(mode != null && mode.equals("email")){
			handleUrl = hreq.getScheme() + "://" + hreq.getServerName() + ":" + hreq.getServerPort() + uri + "?" + queryString;
			hreq.setAttribute("handleUrl", handleUrl);
		}

		String ldaperrorMsg = "";
		
		if(uri.equals(hreq.getContextPath()+"/") || uri.equals(hreq.getContextPath()+"/index.jsp")){
			user = CookieAuthValidator.valid(hreq, resp);
			if(user != null){
				resp.sendRedirect("portal/share/index.jsp?application="+user.getDefaultApplication());
				return;
			}
		}
		
		if (isExcludeURI(uri,hreq)) {
			chain.doFilter(request, response);
			return;
		} else {
			HttpSession session = hreq.getSession();
			//修正重定向request中message被清除,导致保存返回无弹出提示信息问题
			String content = request.getParameter("message.content");
			if (content != null) {
				content = new String(Security.hexStringToByte(content));
				hreq.setAttribute("message", new JsMessage(
							Integer.parseInt(request.getParameter("message.type")), 
							content));
			}
			user = (WebUser) session
					.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
			WebUser admin = (WebUser) session
					.getAttribute(Web.SESSION_ATTRIBUTE_USER);

			if (isForegroundURI(uri)) { // 是否为前台
				try {
					SSOUtil ssoUtil = new SSOUtil();
					// 前台SSO模式
					if (Web.AUTHENTICATION_TYPE_SSO.equals(PropertyUtil
							.get(Web.AUTHENTICATION_TYPE)) && user == null) {
						user = ssoUtil.checkSSO(hreq, resp);
					}
					
					if(user == null){
						user = CookieAuthValidator.valid(hreq, resp);
					}
				} catch (Exception e1) {
					//e1.printStackTrace();
					ldaperrorMsg = e1.getMessage();
					e1.printStackTrace();
					LOG.warn(e1);
				}

				// 检查URI权限
				if (user != null) {
					if(mode != null && mode.equals("email")){
						hreq.getRequestDispatcher("/portal/share/security/handle.jsp").forward(request, response);
					}else{
						try {
							chain.doFilter(request, response);
							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} else { // 用户不存在
					LOG.warn(uri);
					/**
					 * 如果是单点登录,则直接跳转到登陆页面, 避免用户配置的跳转页面有误导致timeOut.jsp页面无限循环
					 */
					if (Web.AUTHENTICATION_TYPE_SSO.equals(PropertyUtil
							.get(Web.AUTHENTICATION_TYPE))) {
						String msg = hreq.getHeader("Authorization");
						/**
						 * 如果Authorization的信息不为null或者不是AD单点登录,则重定向
						 */
						if (!"cn.myapps.core.sso.ADUserSSO".equals(PropertyUtil
								.get(Web.SSO_IMPLEMENTATION))
								|| msg != null) {
							if(!StringUtil.isBlank(ldaperrorMsg)){
								request.setAttribute("errorMsg", ldaperrorMsg);
							}
							if("cn.myapps.core.sso.CookieUserSSO".equals(PropertyUtil
								.get(Web.SSO_IMPLEMENTATION))){
								hreq.getRequestDispatcher("/portal/share/security/login.jsp").forward(request, response);
							}else {
								//hreq.getRequestDispatcher("/portal/share/security/ssoerror.jsp").forward(request, response);
							}
						}
					}else if(mode != null && mode.equals("email")){
						hreq.getRequestDispatcher("/portal/share/security/login.jsp").forward(request, response);
					}else if(isRequireWeixinAuth(hreq)){//微信OAUTH2 
						try {
							WeixinServiceProxy.auth(hreq, resp, chain);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						
						hreq.setAttribute("_ActionUrl", hreq.getRequestURI());
						
						hreq.getRequestDispatcher("/portal/share/security/timeOut.jsp").forward(request, response);
					}
				}
			} else {
				Map<String, String> info = CookieUtil.getSSO(hreq);
				// 根据URL参数登录
				String account = info.get("loginno");
				String password = info.get("password");

				if (ACCESS_ADMIN) {
					if (admin == null) {
						admin = loginAdmin(account, password, hreq);
					}

					if (admin != null) {
						try {
							chain.doFilter(request, response);
							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					} else {
						LOG.warn(uri);
						resp.sendRedirect(hreq.getContextPath()
								+ "/admin/timeOut.jsp");
					}
				} else {
					LOG.warn(uri);
					resp.sendRedirect(hreq.getContextPath()
							+ "/admin/timeOut.jsp");
				}
			}
		}
	}
	
	/**
     * 是否需要微信认证
     * @param request
     * @return
     */
    private boolean isRequireWeixinAuth(HttpServletRequest request) {
    	//从微信客户端中访问，则不需要CAS验证
    	String userAgent = request.getHeader("user-agent");
    	String queryString = request.getQueryString();
    	if(userAgent.contains("MicroMessenger") && queryString !=null && queryString.indexOf("code")>=0 && queryString.indexOf("state")>=0) return true;
    	return false;
    }

	/**
	 * 是否不作检验的URI
	 * 
	 * @param uri
	 * @return 是返回true,否则返回false
	 */
	private boolean isExcludeURI(String uri,HttpServletRequest request) {
		return uri.indexOf("mobile") >= 0 || uri.indexOf("help") >= 0
				|| uri.startsWith(request.getContextPath()+"/extend/")
				|| uri.indexOf("login.action") >= 0
				|| uri.indexOf("loginWithCiphertext.action") >= 0
				|| uri.indexOf("loginInDialog.action") >= 0
				|| uri.indexOf("loginInDialog.jsp") >= 0
				|| uri.indexOf("loginSMS.action") >= 0
				|| uri.indexOf("urlLogin.action") >= 0
				|| uri.indexOf("logout.jsp") >= 0 || uri.equals("")
				|| uri.equals("/")
				|| (ACCESS_ADMIN && uri.endsWith("/admin/login.jsp"))
				|| (uri.indexOf("timeOut.jsp") >= 0)
				|| (uri.indexOf("login_error.jsp") >= 0)
				|| (uri.indexOf("frame.jsp") >= 0)
				|| ((uri.indexOf(".action") < 0 && uri.indexOf(".jsp") < 0 && uri.indexOf("/portal/LinkForScript") < 0 && uri.indexOf("/portal/document/qrcodefield/handle") < 0) )
				|| uri.indexOf("step1_login.jsp") >= 0
				|| uri.indexOf("login.jsp") >= 0
				|| uri.indexOf("login_debug.jsp") >= 0
				|| uri.startsWith(request.getContextPath()+"/index.jsp")
				|| uri.indexOf("core/multilanguage/change") >= 0
				|| uri.indexOf("sysinfo.jsp") >= 0
				|| uri.indexOf("desktop") >= 0
				|| uri.endsWith("/checkcookie.jsp")
				|| uri.indexOf("/core/macro/debuger/") >= 0
				|| uri.indexOf("loginUsbKey.action") >= 0
//				|| uri.indexOf("sso.action") >= 0
				|| uri.indexOf("smsauth.action") >= 0
				|| uri.indexOf("resource/css/style.jsp") >= 0
				|| uri.indexOf("/saas/register") >= 0
				|| uri.indexOf("/portal/share/server-info.jsp") >= 0
				|| uri.indexOf("/km/") >= 0
				|| uri.indexOf("saas/multilanguage/change") >= 0
				|| uri.indexOf("saas/changeLanguageKm") >= 0
				|| uri.indexOf("/weioa365/services") >= 0
				|| uri.indexOf("/gdsc/") >= 0
				|| uri.indexOf("addClient.action") >= 0
				|| uri.indexOf("clientDefault.action") >= 0
				|| uri.indexOf("jumpClient.action") >= 0
				|| uri.indexOf("billExcel.action") >= 0
				|| uri.indexOf("billTemplate.action") >= 0
				|| uri.indexOf("prostageJsp.action") >= 0
				|| uri.indexOf("prostage.action") >= 0
				|| uri.indexOf("checkNo.action") >= 0
				|| uri.indexOf("fundExcelTem.action") >= 0
				|| uri.indexOf("importSCExcel.action") >= 0
				|| uri.indexOf("lottery.action") >= 0
				|| uri.indexOf("importExcel.jsp") >= 0
				|| uri.indexOf("/portal/document/qrcodefield/ready") >= 0
				|| uri.indexOf("/portal/dynaform/document/fileDownload") >= 0	//微信端文件下载超时
				|| uri.indexOf("/weixin/") >= 0 || uri.indexOf("/portal/component/user/") >= 0;
				
	}

	/**
	 * 是否前台URI
	 * 
	 * @param uri
	 * @return 是返回true,否则返回false
	 */
	private boolean isForegroundURI(String uri) {
		// 检查以"/portal"或"/mobile"开始的URI(*.action、*.jsp、*.html);
		return uri.indexOf("/portal/") >= 0 || uri.indexOf("/mobile/") >= 0 || uri.indexOf("/pm/")>= 0 || uri.indexOf("/rm/") >= 0 
				|| uri.indexOf("/attendance/")>=0
				|| uri.indexOf("/saas/weioa/")>=0
				|| uri.indexOf("/contacts/")>=0 || uri.indexOf("/qm/") >=0
				|| uri.indexOf("/extendedReport/") >= 0;
	}

	/**
	 * 登录后台管理
	 * 
	 * @return
	 */
	private WebUser loginAdmin(String username, String password,
			HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			SuperUserProcess sprocess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);

			SuperUserVO user = (SuperUserVO) sprocess.login(username, password);

			if (user != null && user.getStatus() == 1) {
				WebUser webUser = new WebUser(user);
				session.setAttribute(Web.SESSION_ATTRIBUTE_USER, webUser);
				return webUser;
			}
		} catch (Exception e) {
			LOG.warn(request.getRequestURI(), e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 桌面应用例外URI
	 * 
	 * @param uri
	 * @return
	 */
	/*
	 * private boolean isExcludeURIByDesktop(String uri) { if
	 * (uri.indexOf("mainFrame.jsp") >= 0 || uri.indexOf("welcome.jsp") >= 0 ||
	 * uri.endsWith("newword.action") || uri.indexOf("main.jsp") >= 0 ||
	 * uri.indexOf("error.jsp") >= 0) { return true; } return false; }
	 */


	public void destroy() {

	}
	
	public void init(FilterConfig arg0) throws ServletException {
		firewall = Firewall.getInstance();
	}
}
