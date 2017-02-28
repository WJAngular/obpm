package cn.myapps.core.dynaform.activity.ejb.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.dynaform.form.action.FormHelper;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.OBPMDispatcher;
import cn.myapps.util.cache.MemoryCacheUtil;
import cn.myapps.util.pdf.ConvertHTML2Pdf;

public class ExportToPdf extends ActivityType {

	public ExportToPdf(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getAfterAction() {
		return BASE_ACTION;
	}

	public String getBackAction() {
		return BASE_ACTION;
	}

	public String getBeforeAction() {
		return BASE_ACTION;
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getDefaultOnClass() {
		return DOCUMENT_BUTTON_ON_CLASS;
	}

	public String getOnClickFunction() {
		//return "doExportToPDF('" + act.getId() + "')";
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+")";
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,WebUser user, ParamsTable params) throws Exception {
		return null;
	}
	
	public String doProcess(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		try {
			HttpServletRequest request = action.getRequest();
			HttpServletResponse response = action.getResponse();
			
			String pTotalLable = "";
			String cTotalLable = "";
			String language = (String) request.getSession().getAttribute("USERLANGUAGE");
			if("CN".equals(language)){
				pTotalLable = "当前页小计";
				cTotalLable = "总计";
			}else if("TW".equals(language)){
				pTotalLable = "當前頁小計";
				cTotalLable = "總計";
			}else if("EN".equals(language)){
				pTotalLable = "Current page total";
				cTotalLable = "Grand total";
			}
			
			String formid = request.getParameter("_formid");
			Form form = FormHelper.get_FormById(formid);
			
			Collection<Activity> acts = form.getActivitys();
			Iterator<Activity> it = acts.iterator();
			String afterActionScript = "";
			String actId = "";
			while(it.hasNext()){
				Activity act = it.next();
				if(ActivityType.EXPTOPDF == act.getType()){
					actId = act.getId();
					afterActionScript = act.getAfterActionScript();
				}
			}
			
			String id = request.getParameter("content.id");
			DocumentProcess docProcess = new DocumentProcessBean(form.getApplicationid());
			doc = (Document) MemoryCacheUtil.getFromPrivateSpace(id, user);
			if (doc == null) {
				 doc = (Document)docProcess.doView(id);
			}
			
			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), form.getApplicationid());
			runner.initBSFManager(doc,  params,	user, new ArrayList<ValidateMessage>());
			
			String agent = request.getHeader("USER-AGENT");
		
			if(null != agent && -1 != agent.indexOf("Firefox")){
				response.setContentType("application/pdf; charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment; filename=" + MimeUtility.encodeText(form.getName(), "UTF-8", "B") + ".pdf");
			}else{
				response.setContentType("application/pdf; charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(form.getName(), "UTF-8") + ".pdf");
			}
			//PdfUtil.htmlToPDF(html, response.getOutputStream());
			StringBuffer html = new StringBuffer();
			//String html = form.toPrintHtml(doc, params, user, new ArrayList<ValidateMessage>());
			html.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
			html.append("<html>\n<head>\n");
			html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
			
			html.append(getCssContent("/portal/share/css/pdf-a4.css", request, response));
			html.append("</head>\n");
			html.append("<body>\n");
			html.append("<div id=\"container\">\n");
			html.append("<table id=\"toAll\">");
			html.append("<tr>\n");
			html.append("<td valign=\"top\" colspan=\"2\">\n");
			html.append(form.toPdfHtml(doc, params, user, new ArrayList<ValidateMessage>())).append("\n");
			html.append("</td></tr></table>");
			html.append("</div>\n");
			html.append("</body>\n");
			html.append("</html>\n");
			String pdfHtml = html.toString().replace("{*[cn.myapps.core.dynaform.view.current_page_total]*}", pTotalLable)
					.replace("{*[cn.myapps.core.dynaform.view.Grant_Total]*}", cTotalLable)
					.replaceAll("&nbsp;", " ");
			new ConvertHTML2Pdf().generatePDFByFlyingSaucer(pdfHtml, response.getOutputStream());
			response.flushBuffer();
			
			//执行运行后脚本
			if(afterActionScript != null && !afterActionScript.equals("")){
				StringBuffer afterLabel = new StringBuffer();
				afterLabel.append("Activity Action:").append(actId).append(" afterActionScript");
				runner.run(afterLabel.toString(), afterActionScript);
			}
		}  catch (IOException e) {
			action.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
			e.printStackTrace();
		}catch (OBPMValidateException e) {
			action.addFieldError("1", e.getValidateMessage());
			e.printStackTrace();
		} catch (Exception e) {
			action.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
			e.printStackTrace();
		}
	
		return null;
	}
	
	/**
	 * 根据传入的css路径地址获取css文件内容
	 * @param url
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private String getCssContent(String url,HttpServletRequest request,HttpServletResponse response) throws Exception {
		StringBuffer content = new StringBuffer();
		content.append("<style>\n");
		String filePath = request.getRealPath(new OBPMDispatcher().getDispatchURL(url,request,response));
		content.append(cn.myapps.util.file.FileOperate.getFileContentAsString(filePath));
		content.append("</style>\n");
		return content.toString();
	}
	
	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}

}
