package cn.myapps.core.sso;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.authentication.AttributePrincipal;

import cn.myapps.constans.Web;

public class CasUserSSO implements SSO {

	/* (non-Javadoc)
	 * @see cn.myapps.core.sso.SSO#authenticateUser(javax.servlet.http.HttpServletRequest)
	 *@SuppressWarnings 第三方包不支持泛型
	 */
	public Map<String, String> authenticateUser(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, String> userInfo = new HashMap<String, String>();
		AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal(); 
		if(principal != null){
	    	String username = principal.getName(); //登录账号
	    	Map<String, Object> attr = principal.getAttributes();//其他属性
	    	String domain = (String) attr.get("domain");
//	    	try {
//				domain = new String(domain.getBytes("GBK"),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
	    	userInfo.put(Web.SSO_LOGINACCOUNT_ATTRIBUTE, username);
	    	userInfo.put(Web.SSO_DOMAINNAME_ATTRIBUTE, domain);
		}
		return userInfo;
	}

	public Map<String, String> authenticateUser(PortletRequest request) {
		Map<String, String> userInfo = new HashMap<String, String>();
		AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal(); 
		if(principal != null){
	    	String username = principal.getName(); //登录账号
	    	Map<String, Object> attr = principal.getAttributes();//其他属性
	    	String domain = (String) attr.get("domain");
	    	userInfo.put(Web.SSO_LOGINACCOUNT_ATTRIBUTE, username);
	    	userInfo.put(Web.SSO_DOMAINNAME_ATTRIBUTE, domain);
		}
		return userInfo;
	}

}
