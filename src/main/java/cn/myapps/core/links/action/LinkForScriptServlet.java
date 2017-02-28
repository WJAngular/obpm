package cn.myapps.core.links.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.OBPMDispatcher;
import cn.myapps.util.ProcessFactory;

/**
 * Servlet implementation class LinkForScript
 */
public class LinkForScriptServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LinkForScriptServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String linkUrl = "";
		try {
			HttpSession session = request.getSession();
			WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
			String resourceId = request.getParameter("_resourceid");
			ResourceProcess resourceProcess = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
			ResourceVO res = (ResourceVO)resourceProcess.doView(resourceId);
			ParamsTable params = ParamsTable.convertHTTP(request);
			if(res != null){
				linkUrl = res.toScriptUrl(new Document(), params, webUser);
			}else {
				linkUrl = request.getContextPath() + "/portal/share/error.jsp";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		new OBPMDispatcher().sendRedirect(linkUrl, request, response);
	}

}
