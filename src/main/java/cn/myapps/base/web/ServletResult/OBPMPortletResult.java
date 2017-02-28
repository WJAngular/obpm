package cn.myapps.base.web.ServletResult;

import java.io.IOException;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.portlet.context.PortletActionContext;
import org.apache.struts2.portlet.result.PortletResult;

import com.opensymphony.xwork2.ActionInvocation;

import cn.myapps.util.OBPMDispatcher;

public class OBPMPortletResult extends PortletResult {

	private static final Log LOG = LogFactory.getLog(OBPMPortletResult.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -2464833521213903747L;

	public void doExecute(String finalLocation, ActionInvocation actionInvocation) throws Exception {
		//TODO 注释掉 PortletActionContext.isRender()问题 稍后研究
		/*if (PortletActionContext.isRender()) {
			PortletRequest req = PortletActionContext.getRequest();
			PortletResponse res = PortletActionContext.getResponse();
			finalLocation = new OBPMDispatcher().getDispatchURL(finalLocation, req, res);
			executeRenderResult(finalLocation);
		} else if (PortletActionContext.isEvent()) {
			PortletRequest req = PortletActionContext.getRequest();
			PortletResponse res = PortletActionContext.getResponse();
			finalLocation = new OBPMDispatcher().getDispatchURL(finalLocation, req, res);
			executeActionResult(finalLocation, actionInvocation);
		} else {
			HttpServletRequest req = ServletActionContext.getRequest();
			HttpServletResponse res = ServletActionContext.getResponse();
			finalLocation = new OBPMDispatcher().getDispatchURL(finalLocation, req, res);
			executeRegularServletResult(finalLocation, actionInvocation);
		}*/
		
		//TODO 下面是临时解决方案
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse res = ServletActionContext.getResponse();
		finalLocation = new OBPMDispatcher().getDispatchURL(finalLocation, req, res);
		executeRegularServletResult(finalLocation, actionInvocation);
	}

	private void executeRegularServletResult(String finalLocation, ActionInvocation actionInvocation)
			throws ServletException, IOException {
		ServletContext ctx = ServletActionContext.getServletContext();
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse res = ServletActionContext.getResponse();
		try {
			ctx.getRequestDispatcher(finalLocation).include(req, res);
		} catch (ServletException e) {
			LOG.error("ServletException including " + finalLocation, e);
			throw e;
		} catch (IOException e) {
			LOG.error("IOException while including result '" + finalLocation + "'", e);
			throw e;
		}
	}

}
