package cn.myapps.km.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.Config;

/**
 * 
 * @author xiuwei
 *
 */
public class KMSecurityFilter extends HttpServlet implements Filter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1008045328822901315L;

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String uri = hreq.getRequestURI();
		
		if (isExcludeURI(uri)) {
			chain.doFilter(request, response);
			return;
		} else {//符合拦截逻辑
			HttpSession session = hreq.getSession();
			NUser user = (NUser) session.getAttribute(NUser.SESSION_ATTRIBUTE_FRONT_USER);
			if (user != null) {//通过
//				if(uri.contains("km/") &&!Config.kmEnabled()){
//					resp.sendRedirect(hreq.getContextPath()
//							+ "/km/setup.jsp?returnUrl=" + uri);
//				}else{
					chain.doFilter(request, response);
//				}
				return;
			}else{
				WebUser webUser = (WebUser)session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
				if(webUser !=null){
					try {
						NUser.login(hreq, webUser);
					} catch (Exception e) {
						e.printStackTrace();
					}
					chain.doFilter(request, response);
					return;
				}else{
					resp.sendRedirect(hreq.getContextPath()
							+ "/saas/timeOut.jsp?page=" + uri);
				}
			}
			
			
			
		}
		
		
	}
	
	/**
	 * 是否不作检验的URI
	 * 
	 * @param uri
	 * @return 是返回true,否则返回false
	 */
	private boolean isExcludeURI(String uri) {
		return uri.indexOf("km/index.jsp") >= 0 || uri.indexOf("km/error.jsp") >=0 || uri.indexOf("km/servlet/") >=0 || uri.indexOf("/km/desktop/service") >=0 || uri.indexOf("km/setup.jsp")>=0 ;
	}
		

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
