package cn.myapps.core.dynaform.view.action;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.log4j.Logger;
import org.apache.struts2.portlet.context.PortletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.property.MultiLanguageProperty;


public class ViewPortletAction extends ViewAction {
	private static final Logger LOG = Logger.getLogger(ViewPortletAction.class);

	public ViewPortletAction() throws ClassNotFoundException {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4794418883215417622L;

	/**
	 * 显示视图view数据列表(前台调用)
	 * 
	 * @return result.
	 * @throws Exception
	 */
	public String doDisplayView() throws Exception {
		try {
			return getSuccessResult(view, DO_DISPLAY_VIEW);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return getInputResult(view);
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return getInputResult(view);
		}
	}

	@SuppressWarnings("unchecked")
	protected void putRequestParameters() {
		PortletRequest request = getPortletRequest();
		Map m = request.getParameterMap();
		// realPath 如何获取
		// String realPath = getSession().getServletContext().getRealPath("/");
		// params.setParameter("realPath", realPath);
		params.setContextPath(request.getContextPath());
		Iterator<Entry<String, Object>> iter = m.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			String name = entry.getKey();
			Object value = entry.getValue();
			try {
				// If there is only one string in the string array, the put the
				// string only, not array.
				if (value instanceof String[])
					if (((String[]) value).length > 1)
						params.setParameter(name, value);
					else
						params.setParameter(name, ((String[]) value)[0]);
				else
					params.setParameter(name, value);
			} catch (Exception e) {
				LOG.warn("Set parameter: " + name + " failed, the value is: " + value);
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}
		// params.setHttpRequest(request);
	}

	private PortletSession getPortletSession() {
		return PortletActionContext.getRequest().getPortletSession();
	}

	private PortletRequest getPortletRequest() {
		return PortletActionContext.getRequest();
	}

	/**
	 * 从Session中获取用户
	 */
	public WebUser getUser() throws Exception {
		PortletSession session = getPortletSession();
		WebUser user = (WebUser) session.getAttribute(getWebUserSessionKey(), PortletSession.APPLICATION_SCOPE);

		if (user == null){
			user = getAnonymousUser();
		}
		
		return user;
	}

	/**
	 * 获取Session ID
	 */
	public String getSessionid() {
		return getPortletSession().getId();
	}

	/**
	 * 从Session中获取多语言设置
	 */
	public String getMultiLanguage(String key, String defaultValue) {
		try {
			PortletSession session = getPortletSession();
			String language = (String) session.getAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE);
			return MultiLanguageProperty.getProperty(language, key, defaultValue);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			LOG.warn("Load multilanguage " + key + "error: " + e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			LOG.warn("Load multilanguage " + key + "error: " + e.getMessage());
		}
		return defaultValue;
	}
}
