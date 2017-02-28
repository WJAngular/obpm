package cn.myapps.base.web.portlet;

import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.struts2.portlet.PortletPhase;
import org.apache.struts2.portlet.dispatcher.Jsr168Dispatcher;

import cn.myapps.constans.Web;
import cn.myapps.core.sso.SSOException;
import cn.myapps.core.sso.SSOUtil;
import cn.myapps.util.property.PropertyUtil;

/**
 * @author Happy
 *
 */
public class OBPMJsr168Dispatcher extends Jsr168Dispatcher {
	

	@Override
	public void serviceAction(PortletRequest request, PortletResponse response,
			Map<String, Object> arg2, Map<String, String[]> arg3,
			Map<String, Object> arg4, Map<String, Object> arg5, String arg6,
			PortletPhase arg7) throws PortletException {
		try {
			// 单点登录实现
			SSOUtil ssoUtil = new SSOUtil();
			if (Web.AUTHENTICATION_TYPE_SSO.equals(PropertyUtil.get(Web.AUTHENTICATION_TYPE))) {
				ssoUtil.checkSSO(request, response);
			}
		} catch (SSOException e) {
			throw new PortletException(e.getMessage());
		}
		
		super.serviceAction(request, response, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	
	
}
