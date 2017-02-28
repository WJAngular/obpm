package cn.myapps.base.web.ServletResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.ServletRedirectResult;

import com.opensymphony.xwork2.ActionInvocation;

import cn.myapps.util.OBPMDispatcher;


public class OBPMServletRedirectResult extends ServletRedirectResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7182913458562750604L;

	public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		finalLocation = new OBPMDispatcher().getDispatchURL(finalLocation, request, response);
		super.doExecute(finalLocation, invocation);
	}
}
