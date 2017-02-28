/**
 * 
 */
package cn.myapps.base.web.ServletResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import com.opensymphony.xwork2.ActionInvocation;

import cn.myapps.util.OBPMDispatcher;


/**
 * @author Chris Xu
 * @version 2010-11-14 下午03:49:35
 */
/**
 * @author Chris Xu
 * 
 */
public class OBPMServletDispatcherResult extends ServletDispatcherResult {
	
	private static final long serialVersionUID = -320550508450507715L;

	@Override
	public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		finalLocation = new OBPMDispatcher().getDispatchURL(finalLocation, request, response);
		super.doExecute(finalLocation, invocation);
	}

}
