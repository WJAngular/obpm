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
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.Config;

/**
 * 
 * @author xiuwei
 *
 */
public class BaikeSecurityFilter extends HttpServlet implements Filter{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6523580237182401917L;


	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String uri = hreq.getRequestURI();
		
		
			HttpSession session = hreq.getSession();
			BUser user = (BUser) session.getAttribute(BUser.SESSION_ATTRIBUTE_FRONT_USER);
			if (user != null) {//通过
				if(uri.contains("baike/") ){
					
					chain.doFilter(request, response);
				}
				return;
			}else{
				WebUser webUser = (WebUser)session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
				if(webUser !=null){
					try {
						BUser.login(hreq, webUser);
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


	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
