package cn.myapps.core.dynaform.document.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.action.FormHelper;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.signature.action.SignatureHelper;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.macro.runner.JsMessage;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.PreviewUser;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.util.OBPMDispatcher;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class DocumentHtmlBean {
	private static final Logger LOG = Logger.getLogger(DocumentHtmlBean.class);

	private HttpServletRequest httpRequest;
	private HttpServletResponse httpResponse;
	private Document doc = null;
	private WebUser webUser = null;
	private IRunner runner = null;
	private ParamsTable params = null;
	private Form form = null;
	private Form templateForm = null;

	private Collection<Activity> activities = null;
	// 当前表单的操作按钮
	private Activity flowAct = null;
	private Activity flexPrintAct = null;
	private Activity flexPrintWFHAct = null;

	// url
	private String mScriptName = "/content.jsp";
	private String mGetDocument = "/portal/dynaform/mysignature/getDocument.action";
	private String mDoCommand = "/portal/dynaform/mysignature/doCommand.action";
	// private String mHttpUrlName = "";
	private String mGetDocumentUrl = "";
	private String mDoCommandUrl = "";

	/**
	 * 产生阅读表单按钮HTML
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toTemplateFormButtonsHTML() throws Exception {
		return toFormButtonsHTML(getTemplateForm());
	}

	/**
	 * 获取表单按钮HTML
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActBtnHTML() throws Exception {
		if (this.getTemplateForm() != null) {
			return toTemplateFormButtonsHTML();
		}

		return toDefaultFormButtonsHTML();
	}
	
	/**
	 * 获取表单按钮HTML(后台预览)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActBtnHTML4Preview() throws Exception {

		return toFormButtonsHTML4Preview(getForm());
	}

	/**
	 * 产生默认表单按钮HTML
	 */
	public String toDefaultFormButtonsHTML() throws Exception {
		if(webUser instanceof PreviewUser){
			return getActBtnHTML4Preview();
		}else {
			return toFormButtonsHTML(getForm());
		}
		
	}

	/**
	 * 产生表单按钮HTML
	 * 
	 * @param form
	 * @return
	 */
	public String toFormButtonsHTML(Form form) {
		if(webUser instanceof PreviewUser){
			return toFormButtonsHTML4Preview(form);
		}
		StringBuffer html = new StringBuffer();
		String showAct = httpRequest.getParameter("show_act");

		try {
			if ((showAct == null || showAct.equals("true"))
					&& form.getActivitys() != null
					&& !form.getActivitys().isEmpty()) {
				
				boolean isFlowComplate = false;
				Document doc = getDoc();
				if(doc !=null && doc.getState()!=null){
					isFlowComplate = doc.getState().isComplete();
				}
				
				for (Iterator<Activity> aiter = form.getActivitys().iterator(); aiter
						.hasNext();) {
					Activity act = (Activity) aiter.next();
					
					if(isFlowComplate && (act.getType()==ActivityType.DOCUMENT_UPDATE || act.getType()==ActivityType.SAVE_BACK ||
							act.getType()==ActivityType.SAVE_CLOSE_WINDOW ||act.getType()==ActivityType.SAVE_NEW ||
							act.getType()==ActivityType.SAVE_NEW_WITH_OLD ||act.getType()==ActivityType.SAVE_NEW_WITHOUT_OLD ||
							act.getType()==ActivityType.SAVE_SARTWORKFLOW ||act.getType()==ActivityType.SAVE_WITHOUT_VALIDATE ||
							act.getType()==ActivityType.SIGNATURE ||act.getType()==ActivityType.WORKFLOW_PROCESS ||
							act.getType()==ActivityType.START_WORKFLOW ||act.getType()==ActivityType.DOCUMENT_COPY
							||act.getType()==ActivityType.ARCHIVE)){
						continue;
					}
					
					act.setApplicationid(form.getApplicationid());
					int permissionType = PermissionType.MODIFY;
					if (act.isReadonly(this.getRunner(), form.getFullName())) {
						permissionType = PermissionType.DISABLED;
					}
					if (!act.isHidden(this.getRunner(), form, this.getDoc(),
							webUser, ResVO.FORM_TYPE)) {
						if (act.getType() == ActivityType.WORKFLOW_PROCESS) {
							// 是否审批者
							//提交操作改成前端渲染，默认输出的都是hidden的，因此，屏蔽掉此处判断 --Jarod
//							if (StateMachineHelper.isDocEditUser(
//									this.getDoc(), webUser)) {
								html.append(act.toHtml(doc,webUser,permissionType));
//							}

						}else if (act.getType() == ActivityType.START_WORKFLOW) {
							if (StringUtil.isBlank(this.getDoc().getStateid()) && StringUtil.isBlank(this.getDoc().getStateLabel())) {
								
									html.append(act.toHtml(doc,webUser,permissionType));
							} 
						} else if (act.getType() == ActivityType.EMAIL_TRANSPOND) {
							if (this.doc.getLastmodified() != null) {
									html.append(act.toHtml(doc,webUser,permissionType));
							} 
						} else {
								html.append(act.toHtml(doc,webUser,permissionType));
						}
					}
				}
			}
		} catch (Exception e) {
			html.setLength(0);
			html.append("<span style='color:red;'>按钮出错！出错信息：").append(e.getMessage()).append("</span>");
			LOG.error("getActBtnHTML", e);
		}

		return html.toString();
	}
	
	/**
	 * 产生表单按钮HTML(后台预览)
	 * 
	 * @param form
	 * @return
	 */
	public String toFormButtonsHTML4Preview(Form form) {
		StringBuffer html = new StringBuffer();
		try {
			if (form.getActivitys() != null
					&& !form.getActivitys().isEmpty()) {
				for (Iterator<Activity> aiter = form.getActivitys().iterator(); aiter
						.hasNext();) {
					Activity act = (Activity) aiter.next();
					act.setApplicationid(form.getApplicationid());
					int permissionType = PermissionType.MODIFY;
								html.append(act.toHtml(permissionType));
				}
			}
		} catch (Exception e) {
			LOG.error("getActBtnHTML", e);
		}

		return html.toString();
	}

	// 如果没有错误提示，就执行事件后脚本
	public String doActAfterActionScript() throws Exception {
		StringBuffer html = new StringBuffer();
		Activity activity = (Activity) this.httpRequest
				.getAttribute("ACTIVITY_INSTNACE");
		int errorMegCount = ((LinkedHashMap<?, ?>) this.httpRequest
				.getAttribute("fieldErrors")).size();
		if (errorMegCount == 0 && activity != null
				&& activity.getAfterActionScript() != null
				&& activity.getAfterActionScript().trim().length() > 0) {

			StringBuffer label = new StringBuffer();
			label.append("Activity Action(").append(activity.getId())
					.append(")." + activity.getName())
					.append("afterActionScript");
			Object result = this.getRunner().run(label.toString(),
					activity.getAfterActionScript());

			if (result != null) {
				if (result instanceof JsMessage) {
					this.httpRequest.setAttribute("message", result);
					RequestDispatcher rd = this.httpRequest
							.getRequestDispatcher(activity.getBackAction());
					rd.forward(this.httpRequest, this.httpResponse);
					return null;
				} else if (result instanceof String
						&& ((String) result).trim().length() > 0) {
					html.append("<textarea name='msg' id='msg' style='display:none'>"
							+ result + "</textarea>");
					html.append("<script>doAlert(document.getElementById('msg').value);</script>");
					return html.toString();
				}
			}
		}
		return html.toString();
	}

	/*
	 * 获取流程信息
	 */
	public String getFlowMsgHTML() throws Exception {
		StringBuffer html = new StringBuffer();
		flowAct = this.getForm().getActivityByType(
				ActivityType.WORKFLOW_PROCESS);
		if (flowAct != null) {
			String fshowtype = flowAct.getFlowShowType();
			if (fshowtype == null || fshowtype == ""
					|| fshowtype.equals("ST01")) {

			}
		}
		return html.toString();
	}

	// 获取提交按钮的打开方法
	public String getFlowShowType() {
		try {
			if (this.getFlowAct() != null)
				return this.getFlowAct().getFlowShowType().toString();
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	public String getFlowId() throws Exception {
		try {
			if (this.getDoc() != null
					&& !StringUtil.isBlank(this.getDoc().getStateid())) {
				return this.getDoc().getState().getFlowid();
			} else {
				if (this.getTemplateForm() != null) {
					return this.getTemplateForm().getOnActionFlow();
				}else{
					return this.getForm().getOnActionFlow();
				}
			}
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * 获取表单HTML
	 */
	public String getFormHTML() throws Exception {
		if (this.getTemplateForm() != null) {
			return this.toTemplateFormHTML();
		}
		return this.toDefaultFormHTML();

	}

	/**
	 * 生成普通表单HTML
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toDefaultFormHTML() throws Exception {
		StringBuffer html = new StringBuffer();
		Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
		if (this.getForm() != null) {
			try {
				html.append(this
						.getForm()
						.toHtml(this.getDoc(), this.getParams(), webUser,
								errors).toString());
			} catch (Exception e) {
				LOG.warn(e);
				throw e;
			}
		}
		return html.toString();
	}

	/**
	 * 生成模板表单HTML
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toTemplateFormHTML() throws Exception {
		StringBuffer html = new StringBuffer();
		Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
		if (this.getTemplateForm() != null) {
			try {
				html.append(this
						.getTemplateForm()
						.toHtml(this.getDoc(), this.getParams(), webUser,
								errors).toString());
			} catch (Exception e) {
				LOG.warn(e);
				throw e;
			}
		}
		return html.toString();
	}

	public String getTemplateFormPrintHTML() throws Exception{
		StringBuffer html = new StringBuffer();
		Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
		if (this.getForm() != null) {
			try {
				html.append(this
						.getTemplateForm()
						.toPrintHtml(this.getDoc(), this.getParams(), webUser,
								errors).toString());
			} catch (Exception e) {
				LOG.warn(e);
				throw e;
			}
		}
		return html.toString();
	}
	
	public String getFormPrintHTML() throws Exception {
		StringBuffer html = new StringBuffer();
		Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
		if (this.getForm() != null) {
			try {
				html.append(this
						.getForm()
						.toPrintHtml(this.getDoc(), this.getParams(), webUser,
								errors).toString());
			} catch (Exception e) {
				LOG.warn(e);
				throw e;
			}
		}
		return html.toString();
	}

	// 检查是否安有电子签章操作
	public boolean isSignatureExist() throws Exception {
		Boolean signatureExist = false;
		String sb = httpRequest.getParameter("signatureExist");
		if ("false".equals(sb) || sb == null) {
			signatureExist = SignatureHelper.signatureExistMethod(
					this.getActivities(), null);
		} else {
			signatureExist = true;
		}
		return signatureExist.booleanValue();
	}

	public boolean isEditCurrDoc() {
		Boolean isEdit = true;
		String currURL = httpRequest.getRequestURL().toString();
		if (currURL.indexOf("new.action") > 0
				|| currURL.indexOf("edit.action") > 0
				|| currURL.indexOf("save.action") > 0) {
			isEdit = true;
		}
		return isEdit;
	}

	// 如果没有错误提示，就执行事件后脚本
	public String checkDocument() throws Exception {
		StringBuffer html = new StringBuffer();
		Activity activity = (Activity) this.httpRequest
				.getAttribute("ACTIVITY_INSTNACE");
		int errorMegCount = ((LinkedHashMap<?, ?>) this.httpRequest
				.getAttribute("fieldErrors")).size();
		if (errorMegCount == 0 && activity != null
				&& activity.getAfterActionScript() != null
				&& activity.getAfterActionScript().trim().length() > 0) {
			StringBuffer label = new StringBuffer();
			label.append("Activity Action(").append(activity.getId())
					.append(")." + activity.getName())
					.append("afterActionScript");
			Object result = this.getRunner().run(label.toString(),
					activity.getAfterActionScript());
			if (result != null) {
				if (result instanceof JsMessage) {
					// httpRequest.setAttribute("message", result);
					// RequestDispatcher rd =
					// httpRequest.getRequestDispatcher(activity.getBackAction());
					// rd.forward(httpRequest, response);
					// return null;
				} else if (result instanceof String
						&& ((String) result).trim().length() > 0) {
					html.append("<textarea name='msg' id='msg' style='display:none'>"
							+ result + "</textarea>");
					html.append("<script>doAlert(document.getElementById('msg').value);</script>");
				}
			}
		}
		return html.toString();
	}

	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public Document getDoc() {
		if (doc == null) {
			this.doc = (Document) httpRequest.getAttribute("content");
		}
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public WebUser getWebUser() {
		return webUser;
	}

	public void setWebUser(WebUser webUser) {
		this.webUser = webUser;
	}

	public IRunner getRunner() throws Exception {
		if (this.runner == null) {
			IRunner r = JavaScriptFactory.getInstance(this.getParams()
					.getSessionid(), getForm().getApplicationid());
			r.initBSFManager(this.getDoc(), this.getParams(),
					this.getWebUser(), new ArrayList<ValidateMessage>());
			this.runner = r;
		}
		return this.runner;
	}

	public void setRunner(IRunner runner) {
		this.runner = runner;
	}

	public ParamsTable getParams() {
		if (this.params == null) {
			this.params = ParamsTable.convertHTTP(httpRequest);
		}
		return this.params;
	}

	public void setParams(ParamsTable params) {
		this.params = params;
	}

	public Form getForm() throws Exception {
		if (this.form == null) {
			String formid = null;
			if (httpRequest.getParameter("_formid") != null) {
				formid = httpRequest.getParameter("_formid");
			} else {
				formid = this.getDoc().getFormid();
			}
			this.form = FormHelper.get_FormById(formid);
		}
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Collection<Activity> getActivities() throws Exception {
		if (this.activities == null && this.getForm() != null)
			this.activities = this.getForm().getActivitys();
		return this.activities;
	}

	public void setActivities(Collection<Activity> activities) {
		this.activities = activities;
	}

	public int getActivitiesSize() throws Exception {
		if (this.activities == null && this.getForm() != null)
			this.activities = this.getForm().getActivitys();
		return this.activities.size();
	}

	public Activity getFlowAct() throws Exception {
		Activity act = this.getForm().getActivityByType(ActivityType.WORKFLOW_PROCESS);
		return act;
	}

	public void setFlowAct(Activity flowAct) {
		this.flowAct = flowAct;
	}

	public Activity getFlexPrintAct() throws Exception {
		if (this.flexPrintAct == null && this.getForm() != null) {
			this.flexPrintAct = this.getForm().getActivityByType(
					ActivityType.FLEX_PRINT);
		}
		return this.flexPrintAct;
	}

	public void setFlexPrintAct(Activity flexPrintAct) {
		this.flexPrintAct = flexPrintAct;
	}

	public Activity getFlexPrintWFHAct() throws Exception {
		if (this.flexPrintWFHAct == null && this.getForm() != null) {
			this.flexPrintWFHAct = this.getForm().getActivityByType(
					ActivityType.FLEX_PRINT);
		}
		return this.flexPrintWFHAct;
	}

	// 运行表单的是否可打开脚本
	public void isOpenAble(String view_id, String contextPath) throws Exception {
		boolean isopenable = true;
		Document doc = this.getDoc();
		if (doc != null && !doc.getIstmp()) {
			Form form = getTemplateForm()==null? this.getForm() : getTemplateForm();
			if (form.getIsopenablescript() != null
					&& form.getIsopenablescript().trim().length() > 0) {
				StringBuffer label = new StringBuffer();
				label.append("Document Print.Form(")
						.append(this.getForm().getId())
						.append(")." + this.getForm().getName())
						.append(".runIsOpenAbleScript");

				Object result = this.getRunner().run(label.toString(),
						form.getIsopenablescript());
				if (result != null && result instanceof Boolean) {
					isopenable = (((Boolean) result).booleanValue());
				}
			}
			if (!isopenable) {
				ViewProcess vp = (ViewProcess) ProcessFactory
						.createProcess(ViewProcess.class);
				View view = (View) vp.doView(view_id);
				int openType = view.getOpenType();
				if (openType == view.OPEN_TYPE_DIV) {
					new OBPMDispatcher().sendRedirect(contextPath
							+ "/portal/share/openType.jsp?view_id=" + view_id,
							this.getHttpRequest(), this.getHttpResponse());
				} else {
					String url = this.getParams().getParameterAsString("_backURL");
					url = url.replaceAll("#", "%23");
					url = url + "&isopenablescript=" + isopenable + ";";
					new OBPMDispatcher().sendRedirect(url,
							this.getHttpRequest(), this.getHttpResponse());
				}
			}
		}

	}

	public void setFlexPrintWFHAct(Activity flexPrintWFHAct) {
		this.flexPrintWFHAct = flexPrintWFHAct;
	}

	public String getMHttpUrlName() {

		return httpRequest.getRequestURI();
	}

	// public void setMHttpUrlName(String httpUrlName) {
	// mHttpUrlName = httpUrlName;
	// }

	public String getMGetDocumentUrl() {
		mGetDocumentUrl = "http://"
				+ httpRequest.getServerName()
				+ ":"
				+ httpRequest.getServerPort()
				+ this.getMHttpUrlName().substring(0,
						this.getMHttpUrlName().indexOf("/", 2)) + mGetDocument;
		return mGetDocumentUrl;
	}

	public void setMGetDocumentUrl(String getDocumentUrl) {
		mGetDocumentUrl = getDocumentUrl;
	}

	public String getMDoCommandUrl() {
		
		mDoCommandUrl = "http://"
				+ httpRequest.getServerName()
				+ ":"
				+ httpRequest.getServerPort()
				+ httpRequest.getContextPath() + mDoCommand;
//				+ this.getMHttpUrlName().substring(0,
//						this.getMHttpUrlName().indexOf("/", 2)) + mDoCommand;
//		String aa = this.getMHttpUrlName();
//		System.out.println(aa+"  "+mDoCommand);
		return mDoCommandUrl;
	}

	public void setMDoCommandUrl(String doCommandUrl) {
		mDoCommandUrl = doCommandUrl;
	}

	public String getMScriptName() {
		return mScriptName;
	}

	public void setMScriptName(String scriptName) {
		mScriptName = scriptName;
	}

	public String getMGetDocument() {
		return mGetDocument;
	}

	public void setMGetDocument(String getDocument) {
		mGetDocument = getDocument;
	}

	public String getMDoCommand() {
		return mDoCommand;
	}

	public void setMDoCommand(String doCommand) {
		mDoCommand = doCommand;
	}

	public HttpServletResponse getHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(HttpServletResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

	/**
	 * 获取阅读表单
	 * 
	 * @return
	 * @throws Exception
	 */
	public Form getTemplateForm() throws Exception {
		if (templateForm == null
				&& !StringUtil.isBlank(this.getHttpRequest().getParameter(
						"_templateForm"))) {
			templateForm = FormHelper.get_FormById(this.getHttpRequest()
					.getParameter("_templateForm"));
		}

		return templateForm;
	}

	/**
	 * 设置阅读表单
	 * 
	 * @param templateForm
	 */
	public void setTemplateForm(Form templateForm) {
		this.templateForm = templateForm;
	}
	
	/**
	 * 获得表单样式库ID
	 * @return
	 * @throws Exception
	 */
	public String getStyleRepositoryId() throws Exception{
		Form form = getTemplateForm();
		if(form ==null){
			form = getForm();
		}
		if(form !=null){
			if(form.getStyle()!=null){
				return form.getStyle().getId();
			}
		}
		
		return null;
		
	}

}
