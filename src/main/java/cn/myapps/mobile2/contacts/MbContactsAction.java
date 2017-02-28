package cn.myapps.mobile2.contacts;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public class MbContactsAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 输出值
	 */
	private String result;
	
	public String doContactGroup() throws Exception {
		try{
			WebUser user = getUser();
			ParamsTable params = getParams();
			String result = MbContactsXMLBuilder.toContactGroupXML(user, params);
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
		return SUCCESS;
	}
	
	public String doContact() throws Exception {
		try{
			WebUser user = getUser();
			ParamsTable params = getParams();
			String result = MbContactsXMLBuilder.toContactXML(user, params);
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
		return SUCCESS;
	}
	
	@Deprecated
	public String doContactData() throws Exception {
		try{
			String result = MbContactsXMLBuilder.toMobileXML(null, getUser(), null);
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
		return SUCCESS;
	}
	
	public String doContactMenu() throws Exception {
		try{
			String result = MbContactsXMLBuilder.toMobileXML(null, getUser(), null);
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
		return SUCCESS;
	}
	
	public WebUser getUser() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user == null) {
			throw new Exception("[*TIMEOUT*]{*[page.timeout]*}");
		}
		return user;
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
	
	public String getResult() {
		return result;
	}
}
