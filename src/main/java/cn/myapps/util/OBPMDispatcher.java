/**
 * 
 */
package cn.myapps.util;

import java.io.IOException;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.myapps.base.web.filter.OBPMFilterDispatcher;
import cn.myapps.constans.Web;
import cn.myapps.core.user.ejb.PreviewUser;
import cn.myapps.util.property.DefaultProperty;

/**
 * @author Chris Xu
 * @version 2010-11-14 下午02:32:05
 */
/**
 * @author Chris Xu
 * 
 */
public class OBPMDispatcher {
	private static final Logger LOG = Logger.getLogger(OBPMFilterDispatcher.class);

	private static String DEFAULT_SKINTYPE = null;
	static {
		try {
			DEFAULT_SKINTYPE = DefaultProperty.getProperty("DEFAULT_SKINTYPE");
			if (DEFAULT_SKINTYPE == null)
				DEFAULT_SKINTYPE = "cool";
		} catch (Exception e) {
			DEFAULT_SKINTYPE = "cool";
		}
	}

	public void forward(String url, ServletRequest req, ServletResponse res) throws ServletException, IOException {
		if (url.indexOf("/portal/dispatch/") != -1) {
			url = getDispatchURL(url, req, res);
			url = url.replace(((HttpServletRequest) req).getContextPath(), "");
		}
		LOG.debug(" [FORWARD] " + url);
		req.getRequestDispatcher(url).forward(req, res);
		return;
	}

	public void sendRedirect(String url, ServletRequest req, ServletResponse res) throws IOException {
		url = getDispatchURL(url, req, res);
		LOG.debug(" [REDIRECT] " + url);
		((HttpServletResponse) res).sendRedirect(url);
	}

	/**
	 * 
	 * @param url
	 * @param req
	 * @param res
	 * @return
	 */
	public String getDispatchURL(String url, ServletRequest req, ServletResponse res) {
		if (url.indexOf("/portal/dispatch/") != -1) {
			HttpServletRequest hreq = (HttpServletRequest) req;
			HttpSession session = hreq.getSession();
			String skinType = (String) session.getAttribute("SKINTYPE");
			if (skinType == null)
				skinType = DEFAULT_SKINTYPE;
			if("true".equals(req.getAttribute("_isPreview"))){
				skinType = (String) req.getAttribute("_skinType");
				if(StringUtil.isBlank(skinType)){
					if(session.getAttribute(Web.SESSION_ATTRIBUTE_PREVIEW_USER) !=null){
						skinType = ((PreviewUser)session.getAttribute(Web.SESSION_ATTRIBUTE_PREVIEW_USER)).getSkinType();
					}
				}
			}
			url = url.replace("dispatch", skinType);
		}
		return url;
	}

	/**
	 * Get dispatch url by portlet request
	 * 
	 * @param url
	 * @param req
	 * @param res
	 * @return
	 */
	public String getDispatchURL(String url, PortletRequest req, PortletResponse res) {
		PortletSession session = req.getPortletSession();
		String skinType = (String) session.getAttribute("SKINTYPE");

		return getDispatchURL(url, skinType);
	}

	/**
	 * 
	 * @param url
	 * @param skinType
	 * @return
	 */
	public String getDispatchURL(String url, String skinType) {
		if (url.indexOf("/portal/dispatch/") != -1) {
			if (skinType == null)
				skinType = DEFAULT_SKINTYPE;
			url = url.replace("dispatch", skinType);
		}

		return url;
	}
}
