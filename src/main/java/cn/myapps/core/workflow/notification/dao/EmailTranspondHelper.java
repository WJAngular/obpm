package cn.myapps.core.workflow.notification.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.myapps.core.security.OBPMParameter;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class EmailTranspondHelper {
	
	public static void initJump(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		String handleUrl = (String) request.getAttribute("myHandleUrl");
		if(StringUtil.isBlank(handleUrl)){
			handleUrl = (String) request.getAttribute("handleUrl");
		}
		//去除mode=email,避免造成死循环
		if(!StringUtil.isBlank(handleUrl)){
			if(handleUrl.contains("&mode=email")){
				handleUrl = handleUrl.substring(0, handleUrl.indexOf("&mode=email"));
			}
		}
		
		String application = request.getParameter("application");
		if(StringUtil.isBlank(application)){
			application = (String) request.getAttribute("application");
		}
		
		String uuid = Sequence.getUUID();
		
		OBPMParameter oParameter = new OBPMParameter();
		oParameter.setPage(handleUrl);

		session.setAttribute(uuid, oParameter);
		request.setAttribute("emailTranspondUrl", handleUrl);
		String urlParameter = "?returnUrl=&application=" + application + "&" + "uuid=" + uuid;
		String dispatchToUrl = "/portal/share/emailTranspond.jsp" + urlParameter;
		request.getRequestDispatcher(dispatchToUrl).forward(request, response);
	}
}
