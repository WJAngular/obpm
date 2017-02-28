package cn.myapps.core.sso;

import java.util.Map;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SSO {
	/**
	 * 验证用户
	 * 
	 * @param request
	 * @return 验证信息
	 */
	public Map<String, String> authenticateUser(HttpServletRequest request, HttpServletResponse response);
	
	public Map<String, String> authenticateUser(PortletRequest request);
}
