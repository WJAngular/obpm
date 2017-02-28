package cn.myapps.mobile2.homepage;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;


public class MbHomePageAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String application;
	
	private String result;
	
	/**
	 * 获取手机端首页内容  
	 */
	public String doView() throws Exception{
		try{
			WebUser user = getUser();
			String result = MbHomePageXMLBuilder.toHomePageXML(user);
			this.result = result;
		} catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}
		return SUCCESS;
	}
	
	/**
	 * 刷新widget
	 */
	public String doRefreshWidget() throws Exception{
		try{
			WebUser user = getUser();
			ParamsTable params = getParams();
			String id = params.getParameterAsString("_widgetid");
			String result = MbHomePageHelper.refreshWidget(id, user);
			this.result = result;
		} catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}
		return SUCCESS;
	}
	
	/**
	 * 待办列表
	 * @return
	 */
	public String doPendingList(){
		try{
			ParamsTable params = getParams();
			WebUser user = getUser();
			String result = MbHomePageXMLBuilder.toPendingXML(user,params);
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}
		return  SUCCESS;
	}
	
	/**
	 * 旧接口  兼容低版本手机客户端  已废弃
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String doHomePage() throws Exception {
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String result = MbHomePageXMLBuilder.toMobileXML(user,params);
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}
		return SUCCESS;
	}
	
	/**
	 * 旧接口  兼容低版本手机客户端  已废弃
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String doHomePage2() throws Exception {
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String result = MbHomePageXMLBuilder.toHomePageMobileXML(user,params);
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}
		return SUCCESS;
	}
	
	/**
	 * 获取短信内容
	 * @return
	 * @throws Exception
	 */
	public String doQueryMessage() throws Exception{
		try{
			ParamsTable params = getParams();
			WebUser user = getUser();
			String result = MbHomePageXMLBuilder.toMessageMobileXML(user,params);
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}
		return  SUCCESS;
	}
	
	@Deprecated
	public String doGetWidgetList() throws Exception{
		try{
			ParamsTable params = getParams();
			WebUser user = getUser();
			String result = MbHomePageXMLBuilder.toWidgetMobileXML(user,params);
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}
		return  SUCCESS;
	}
	
	
	public WebUser getUser() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user == null) {
			throw new Exception("[*TIMEOUT*]{*[page.timeout]*}");
		}
		return user;
	}
	
	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}
	
	public String getResult() {
		return result;
	}

	/**
	 * Get the Parameters table
	 * 
	 * @return ParamsTable
	 */
	public ParamsTable getParams() {
		ParamsTable params = null;
		if (params == null) {
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
			params.setSessionid(ServletActionContext.getRequest().getSession().getId());

			if (params.getParameter("_pagelines") == null)
				params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
		}

		return params;
	}
}
