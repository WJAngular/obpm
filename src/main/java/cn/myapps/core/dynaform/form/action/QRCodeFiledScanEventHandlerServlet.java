package cn.myapps.core.dynaform.form.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.QRCodeField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.macro.runner.JsMessage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 二维码控件扫码回调事件处理
 * @author Happy
 *
 */
public class QRCodeFiledScanEventHandlerServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5644106523329927589L;
	
	/**
	 * 回调事件执行前
	 */
	private static final String ACTION_BEFORE = "before";
	/**
	 * 执行回调事件
	 */
	private static final String ACTION_EXCUTE = "excute";
	/**
	 * 回调事件执行后
	 */
	private static final String ACTION_AFTER = "after";
	
	/**
	 * 回调时间执行结果跟踪
	 */
	private static final String ACTION_EXCUTE_TRACK = "track";
	
	public static Map<String, Boolean> successExcuteSessionPool = new java.util.concurrent.ConcurrentHashMap<String, Boolean>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String action = req.getParameter("action");
		String formId = req.getParameter("formId");
		String docId = req.getParameter("docId");
		String fieldId = req.getParameter("fieldId");
		String applicationId = req.getParameter("applicationId");
		String _randomCode = req.getParameter("_randomCode");
		
		try {
			if(ACTION_EXCUTE_TRACK.equals(action)){
				if(successExcuteSessionPool.get(_randomCode)!=null){
					resp.getWriter().print("success");
					successExcuteSessionPool.remove(_randomCode);
				}else{
					resp.getWriter().print("fault");
				}
				return;
			}
			
			if(isFromWeixin(req)){//通过微信客户端访问
				FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				DocumentProcess documentProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationId);
				ParamsTable params = ParamsTable.convertHTTP(req);
				params.setParameter("application", applicationId);
				
				WebUser user =  (WebUser) req.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
				Form form = (Form) formProcess.doView(formId);
				QRCodeField qrcodeField = (QRCodeField) form.findField(fieldId);
				
				Document doc = (Document) documentProcess.doView(docId);
				
				
				IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), applicationId);
				runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
				
				if(ACTION_BEFORE.equalsIgnoreCase(action)){//执行前
					
				}else if(ACTION_EXCUTE.equalsIgnoreCase(action)){//执行
					StringBuffer label = new StringBuffer();
					label.append("FormName:"+doc.getFormname()+" QRCodeField(").append(fieldId).append(")." + qrcodeField.getName()).append(".callbackScript");
					Object result = null;
					try {
						result = runner.run(label.toString(), StringUtil.dencodeHTML(qrcodeField.getCallbackScript()));
					} catch (Exception e) {
						e.printStackTrace();
						result = new JsMessage(JsMessage.TYPE_DANGER, "系统发生异常，请联系管理员！");
					}		
					
					if(result!=null){
						if(result instanceof String) {
							result = new JsMessage(JsMessage.TYPE_SUCCESS, result.toString());
						}
						
					}else{
						result = new JsMessage(JsMessage.TYPE_SUCCESS, "SUCCESS");
					}
					
					if(result instanceof JsMessage) {
						req.setAttribute("message", result);
						if(((JsMessage) result).getType()==JsMessage.TYPE_SUCCESS && qrcodeField.isRefreshOnChanged()){
							if(!StringUtil.isBlank(_randomCode)){
								successExcuteSessionPool.put(_randomCode, true);
							}
						}
					}
					
					
					
					req.getRequestDispatcher("/portal/phone/resource/component/qrcodeFiled/callback-result.jsp").forward(req, resp);
					
				}else if(ACTION_AFTER.equalsIgnoreCase(action)){//执行后
					
				}else{
					
				}
				
				
			}else{
			}
			
			
		} catch (CreateProcessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
		
		
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	/**
     * 判断请求是否来自微信客户端
     * @param request
     * @return
     */
    private boolean isFromWeixin(HttpServletRequest request) {
    	String userAgent = request.getHeader("user-agent");
    	if(userAgent.contains("MicroMessenger")) return true;
    	return false;
    }
	
	

}
