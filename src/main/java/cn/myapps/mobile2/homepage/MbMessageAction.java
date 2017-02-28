package cn.myapps.mobile2.homepage;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

import com.opensymphony.xwork2.ActionSupport;

public class MbMessageAction extends ActionSupport {

	private static final long serialVersionUID = 1048201371360443459L;
	
	private String result;
	
	public MbMessageAction(){
		super();
	}
	
	public String doMessageData() throws Exception{
		try{
			WebUser user = getUser();
			String result = MbMessageHelper.getMessageData(user);
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
	
	public String doMessageList() throws Exception{
		try{
			ParamsTable params = getParams();
			WebUser user = getUser();
			String result = MbMessageXMLBuilder.toMessageXML(user,params);
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
	
	//修改已读状态
	public String doReadMessage() throws Exception{
		ParamsTable params = getParams();
		MbMessageHelper.doReadMessage(params);
		return SUCCESS;
	}
	
	/**
     * 手机端，站内短信回复ACTION
     * @return
     */
    public String doReplyMessage() throws Exception{
        try {
            ParamsTable params = getParams();
            String result = MbMessageHelper.doReplyMessage(params);
            if(result.equals(SUCCESS)){
            }else{
            	if(!StringUtil.isBlank(result)){
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
