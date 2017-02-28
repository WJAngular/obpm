package cn.myapps.mobile2.menu;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public class MbMenuAction extends ActionSupport {

	private static final long serialVersionUID = -634311789681454408L;
	
	/**
	 * 输出值
	 */
	private String result;
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doLaunchMenu2() throws Exception {
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			String id = params.getParameterAsString("_menuid");
			String applicationid = params.getParameterAsString("_applicationid");
			
			String result = "";
			if(StringUtil.isBlank(id)){
				result = MbMenuXMLBuilder.toMobileMenu(user, null, ResourceVO.LinkType.FORM.getCode());
			}else{
				result = MbMenuXMLBuilder.toSecondaryMenu(user, applicationid, ResourceVO.LinkType.FORM.getCode(), id);
			}
			if(!StringUtil.isBlank(result)){
				if (result != null) {
					this.result = result;
				}
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
	
	public String doApplicationMenu() throws Exception {
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			String id = params.getParameterAsString("_menuid");
			String applicationid = params.getParameterAsString("_applicationid");
			
			String result = "";
			if(StringUtil.isBlank(id)){
				result = MbMenuXMLBuilder.toMobileMenu(user, null, null);
			}else{
				result = MbMenuXMLBuilder.toSecondaryMenu(user, applicationid, null, id);
			}
			if(!StringUtil.isBlank(result)){
				if (result != null) {
					this.result = result;
				}
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

	public String doLaunchMenu() throws Exception {
		try {
			WebUser user = getUser();
			String result = MbMenuXMLBuilder.toMobileXML(null, user, null, ResourceVO.LinkType.FORM.getCode());
			if(!StringUtil.isBlank(result)){
				if (result != null) {
					this.result = result;
				}
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

	public String doSearchMenu() throws Exception {
		try {
			WebUser user = getUser();
			String result = MbMenuXMLBuilder.toMobileXML(null, user, null, ResourceVO.LinkType.VIEW.getCode());
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
	
	public String doPending(){
		try {
			WebUser user = getUser();
			String result = MbWorkXMLBuilder.toPendingMobileXML(user);
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String doProcessing(){
		try {
			WebUser user = getUser();
			String result = MbWorkXMLBuilder.toProcessingMobileXML(user);
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String doFinished(){
		try {
			WebUser user = getUser();
			String result = MbWorkXMLBuilder.toFinishedMobileXML(user, getParams());
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
	
	public String doFinished2(){
		try {
			WebUser user = getUser();
			String result = MbWorkXMLBuilder.toFinishedMobileXML(user, getParams());
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
	
	public String doApplicationList(){
		try {
			WebUser user = getUser();
			String result = MbApplicationXMLBuilder.toMobileXML(user, getParams());
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
	
	public String doApplicationList2(){
		try {
			WebUser user = getUser();
			String result = MbWorkHelper.getApplicationList(user);
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
	
	public WebUser getUser() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user == null) {
			throw new Exception("[*TIMEOUT*]{*[page.timeout]*}");
		}
		return user;
	}

	public String getResult() {
		return result;
	}
}
