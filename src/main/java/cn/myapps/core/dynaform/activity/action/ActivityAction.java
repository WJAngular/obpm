package cn.myapps.core.dynaform.activity.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.action.DocumentHelper;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.html.DocumentHtmlBean;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.dynaform.view.html.ViewHtmlBean;
import cn.myapps.core.logger.action.LogHelper;
import cn.myapps.core.macro.runner.JsMessage;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;


public class ActivityAction extends AbstractRunTimeAction<Document> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7573513417090816740L;
	protected static final String ACTION_RESULT_KEY = "status";
	protected static final int ACTION_RESULT_VALUE_SUCCESS = 1;
	protected static final int ACTION_RESULT_VALUE_FAULT = 0;
	
	protected static final String ACTION_MESSAGE_KEY = "message";
	protected static final String ACTION_DATA_KEY = "data";
	
	
	private String _formid;

	private String _viewid;

	private String _activityid;

	private String[] _selects;

	private String _attitude;
	
	/**当前审批节点ID**/
	private String _targetNode;
	//将会被Struts2序列化为JSON字符串的对象,用来作为控制层返回结果的容器
	protected Map<String, Object> dataMap = new HashMap<String, Object>();
	private boolean isAction = true;
	
	public ActionResult result = new ActionResult();

	public ActivityAction() {
		super();
		setContent(new Document());
	}
	
	public String doExecute(){
		boolean isContinue = true;
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String _activityid = params.getParameterAsString("_activityid");
			String formid = params.getParameterAsString("_formid");
			String _templateFormId = params.getParameterAsString("_templateForm");
			String parentId = params.getParameterAsString("parentid");
			String docid =  params.getParameterAsString("content.id");
			if(docid !=null && docid.equals(parentId)){
				parentId = null;
				params.removeParameter("parentid");
			}
			Activity act = null;
			if (_activityid != null && _activityid.trim().length() > 0) {
				act = findActivityById(_activityid);
				ServletActionContext.getRequest().setAttribute(
						Web.REQUEST_ATTRIBUTE_ACTIVITY, act);
			}

			DocumentProcess process = (DocumentProcess) getProcess();
			Document po = (Document) process.doView(docid);
			Document doc = po!=null? po : null;

			// 修改BUG 新建Document时重复执行2遍的问题 --Jarod 2011.5.15
			if (!StringUtil.isBlank(formid)) {
				FormProcess formPross = (FormProcess) ProcessFactory
						.createProcess(FormProcess.class);
				Form form = (Form) formPross.doView(formid);
				if (!StringUtil.isBlank(_templateFormId)) {// 模板表单模式
					Form _templateForm = (Form) formPross.doView(_templateFormId);
					if (_templateForm != null) {
						Form warpForm = new Form();
						warpForm.setId(form.getId());
						warpForm.setName(form.getName());
						warpForm.setApplicationid(form.getApplicationid());
						warpForm.setSortId(form.getSortId());
						warpForm.setSubFormMap(form.getSubFormMap());
						warpForm.setDiscription(form.getDiscription());
						warpForm.setType(form.getType());
						warpForm.setModule(form.getModule());
						warpForm.setShowLog(_templateForm.isShowLog());
						warpForm.setLastmodifier(form.getLastmodifier());
						warpForm.setLastmodifytime(form.getLastmodifytime());
						warpForm.setStyle(form.getStyle());
						warpForm.setActivityXML(form.getActivityXML());
						warpForm.setVersion(form.getVersion());
						warpForm.setIsopenablescript(form.getIsopenablescript());
						warpForm.setIseditablescript(form.getIseditablescript());
						warpForm.setRelationName(form.getRelationName());
						warpForm.setRelationText(form.getRelationText());
						warpForm.setOnSaveStartFlow(form.isOnSaveStartFlow());
						warpForm.setMappingStr(form.getMappingStr());
						warpForm
								.setDocumentSummaryXML(form.getDocumentSummaryXML());
						warpForm.setSummaryCfg(form.getSummaryCfg());
						warpForm.setCheckout(form.isCheckout());
						warpForm.setCheckoutHandler(form.getCheckoutHandler());
						warpForm.setTemplatecontext(_templateForm
								.getTemplatecontext());
						warpForm.setPermissionType(form.getPermissionType());
						form = warpForm;
					}

				}
				
				if(StringUtil.isBlank(docid)){
					DocumentProcess proxy = (DocumentProcess) getProcess();
					Document newDoc = proxy.doNew(form, user, getParams());
					docid = newDoc.getId();
				}
				if (!StringUtil.isBlank(docid)) {
					if(doc ==null){
						doc = (Document) user.getFromTmpspace(docid);
					}
					
					if(doc ==null){
						doc = form.createDocument(params, user);
						doc.setId(docid);
						//doc = form.createDocumentWithSystemField(params, doc, user);
//						if(params.getParameter("content.versions") !=null){
//							doc.setVersions(params.getParameterAsInteger("content.versions"));
//						}
					}else{
						doc.setId(docid);
						doc = form.createDocumentWithSystemField(params, doc, user);
					}
				} else if (!StringUtil.isBlank(parentId)) {
					doc = (Document) getUser().getFromTmpspace(parentId);
				}
			} 
			if(doc !=null){
				setContent(doc);
			}
			

			// 保存操作日志
			if (isAction) {
				LogHelper.saveLogByDynaform(act, doc, getUser());
			}
			
			if (act != null) {
				result.setActivityId(act.getId());
				result.setActivityType(act.getType());
				if (doc !=null && (
						act.getType() == ActivityType.DOCUMENT_UPDATE
						|| act.getType() == ActivityType.START_WORKFLOW
						|| act.getType() == ActivityType.SAVE_SARTWORKFLOW
						|| act.getType() == ActivityType.WORKFLOW_PROCESS
						|| act.getType() == ActivityType.SAVE_NEW_WITH_OLD
						|| act.getType() == ActivityType.SAVE_NEW_WITHOUT_OLD
						|| act.getType() == ActivityType.SAVE_BACK
						|| act.getType() == ActivityType.SAVE_CLOSE_WINDOW
						|| act.getType() == ActivityType.DOCUMENT_COPY
						|| act.getType() == ActivityType.DISPATCHER
						|| act.getType() == ActivityType.DOCUMENT_UPDATE
						|| act.getType() == ActivityType.SAVE_NEW)) {

					validateDocument();
					if(this.hasFieldErrors()){
						isContinue = false;
						result.setResultType(ActionResult.TYPE_VALIDATE);
						result.setResultData(this.getFieldErrors());
					}
					if(this.getRuntimeException() !=null){
						isContinue = false;
						result.setResultType(ActionResult.TYPE_EXCEPTION);
						result.setResultData(this.getRuntimeException().getNativeMessage());
					}
				}
				
				// 执行Activity的动作
				if(isContinue){
					String resultType = act.execute(this, doc,
							getUser(), params);
					if(resultType == null) return null;
					result.setResultType(resultType);
					if(this.hasActionErrors() || this.hasFieldErrors()){
						result.setResultType(ActionResult.TYPE_VALIDATE);
						result.setResultData(this.getFieldErrors());
						isContinue = false;
					}
				}
				//运行动作执行后脚本
				if(isContinue){
					if ((act.getAfterActionScript()) != null && (act.getAfterActionScript()).trim().length() > 0) {
						Object message = null;
						try {
							message = act.runAfterActionScript((Document) getContent(),params,user);
						} catch (Throwable e) {
							result.setResultType(ActionResult.TYPE_EXCEPTION);
							result.setResultData(e.getMessage());
							e.printStackTrace();
						}	
						if (message != null) {
							if (message instanceof String && ((String)message).trim().length() > 0) {
								message = new JsMessage(JsMessage.TYPE_ALERT,(String)message);
							}
							if(message instanceof JsMessage){
								ServletActionContext.getRequest().setAttribute("message", message);
								result.setResultType(ActionResult.TYPE_MESSAGE);
								result.setResultData(message);
							}
						}else{
							
						}
					}
				}
				
				if(getContent()!=null){
					if(!ActionResult.TYPE_EXCEPTION.equals(result.getResultType()) && !ActionResult.TYPE_VALIDATE.equals(result.getResultType()) &&!ActionResult.TYPE_MESSAGE.equals(result.getResultType())){
						user.putToTmpspace(getContent().getId(), getContent());
					}
					if(result.getResultData()==null){
						result.setResultData(getContent().getId());
					}
				}
				//result.setActivityAferAction(act.getAfterAction());
				addActionResult(true, this.getActionMessages().toString(), result);
			}else{
				throw new OBPMRuntimeException("找不到对象");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultType(ActionResult.TYPE_EXCEPTION);
			result.setResultData(e.getMessage());
			addActionResult(false, e.getMessage(), result);
		}
		ServletActionContext.getRequest().setAttribute("result", JSONObject.fromObject(dataMap).toString());
		return SUCCESS;
	}
	
	/**
	 * 执行按钮业务操作
	 * @return
	 */
	public String doProcess(){
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			HttpServletRequest request = ServletActionContext.getRequest();
			String _activityid = params.getParameterAsString("_activityid");
			String docid = params.getParameterAsString("content.id");

			Activity act = null;

			if (request.getAttribute(Web.REQUEST_ATTRIBUTE_ACTIVITY) != null) {
				// 从Servlet上下文获取Activity对象
				act = (Activity) request
						.getAttribute(Web.REQUEST_ATTRIBUTE_ACTIVITY);
			} else if (_activityid != null && _activityid.trim().length() > 0) {
				// 根据参数传入的Activity ID 从视图或表单载体中获取Activity对象，并设置到Servlet上下文中
				act = findActivityById(_activityid);
				ServletActionContext.getRequest().setAttribute(
						Web.REQUEST_ATTRIBUTE_ACTIVITY, act);
			}

			if (act != null) {
				// 执行Activity的业务操作
				return act.process(this, (Document) user.getFromTmpspace(docid),
						getUser(), params);
			}

			// 运行时异常处理
			if (getRuntimeException() != null) {
				// TODO: 待完善异常输出和上传收集功能
			}
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
		
	}
	
	
	public void doRunbeforeactionscript() {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		ParamsTable params = ParamsTable.convertHTTP(request);
		WebUser user = (WebUser) request.getSession().getAttribute(
				Web.SESSION_ATTRIBUTE_FRONT_USER);
		try {
			json = ActivityUtil.excuteBeforeActionScription4Ajax(null, params, user);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(json);
			return;
		} catch (IOException e) {
			if (out != null) {
				out.close();
			}
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 刷新表单
	 * @return
	 */
	public String doRefreshForm(){
		try {
			Map<String,Object> datas = new HashMap<String, Object>();
			WebUser user = getUser();
			ParamsTable params = getParams();
			
			DocumentHtmlBean dochtmlBean = new DocumentHtmlBean();
			dochtmlBean.setHttpRequest(ServletActionContext.getRequest());
			dochtmlBean.setHttpResponse(ServletActionContext.getResponse());
			dochtmlBean.setWebUser(user);
			
			DocumentProcess process = (DocumentProcess) getProcess();
			String docid = params.getParameterAsString("content.id");
			Document doc = (Document) process.doView(docid);
			ServletActionContext.getRequest().setAttribute("content", doc);
			dochtmlBean.setDoc(doc);
			
			//1.输出操作按钮
			datas.put("activityHtml", dochtmlBean.getActBtnHTML());
			
			//2.输出表单
			datas.put("formHtml", dochtmlBean.getFormHTML());
			
			//3.重新填充系统字段
			Map<String, String> systemFields = new HashMap<String, String>();//存放系统字段
			systemFields.put("content.versions", String.valueOf(doc.getVersions()));
			systemFields.put("content.mappingId", doc.getMappingId());
			systemFields.put("content.istmp", String.valueOf(doc.getIstmp()));
			if(!StringUtil.isBlank(doc.getStateid())){
				//把当前审批节点设置到上下文
				NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user,null);
				if(nodert != null){
					ServletActionContext.getRequest().setAttribute("_targetNodeRT", nodert);
					systemFields.put("_currid", nodert.getNodeid());
				}
				systemFields.put("_flowid", doc.getState().getFlowid());
				systemFields.put("flowid", doc.getState().getFlowid());
				systemFields.put("content.auditorList", doc.getAuditorList());
				systemFields.put("content.stateid", doc.getStateid());
				systemFields.put("content.stateInt", String.valueOf(doc.getStateInt()));
			}
			datas.put("systemFields", systemFields);
			
			//4.输出流程处理人列表、流程状态标签、流程历史按钮
			if(doc.getState()!=null){
				String processorHtml = null;
				String skin = (String) ServletActionContext.getRequest().getSession().getAttribute(Web.SKIN_TYPE);
				if("cool".equals(skin) || "dwz".equals(skin)){
					processorHtml = StateMachineHelper.toProcessorHtml4OldSkin(doc, user);
				}else{
					processorHtml = StateMachineHelper.toProcessorHtml(doc, user);
				}
				datas.put("processorHtml", processorHtml);
				datas.put("flowStateHtml", StateMachineHelper.toFlowStateHtml(doc));
			}
			
			addActionResult(true, "", datas);
			
			//System.out.println(JSONObject.fromObject(dataMap).toString());
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		ServletActionContext.getRequest().setAttribute("result", JSONObject.fromObject(dataMap).toString());
		return SUCCESS;
	}
	
	/**
	 * 刷新视图
	 * @return
	 */
	public String doRefreshView(){
		try {
			Map<String,Object> datas = new HashMap<String, Object>();
			WebUser user = getUser();
			ParamsTable params = getParams();
			
			ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			String viewId = params.getParameterAsString("_viewid");
			View view = (View)viewProcess.doView(viewId);
				
			Document searchDocument = view.getSearchForm() !=null? view.getSearchForm().createDocument(params, user) : new Document();

			// 改变排序参数
			//changeOrderBy(params);


			DataPackage dataPackage = view.getViewTypeImpl().getViewDatas(params, getUser(), searchDocument);
			ServletActionContext.getRequest().setAttribute("content", view);
			ServletActionContext.getRequest().setAttribute("datas", dataPackage);
			ServletActionContext.getRequest().setAttribute("totalRowText", view.getTotalRowText(dataPackage));
			
			ViewHtmlBean htmlBean = new ViewHtmlBean();
			htmlBean.setHttpRequest(ServletActionContext.getRequest());
			htmlBean.setWebUser(user);
			
			//1.输出操作按钮
			//datas.put("activityHtml", dochtmlBean.getActBtnHTML());
			
			//2.输出视图列表
			datas.put("viewHtml", htmlBean.toHTMLText());
			
			addActionResult(true, "", datas);
			
			//System.out.println(JSONObject.fromObject(dataMap).toString());
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		ServletActionContext.getRequest().setAttribute("result", JSONObject.fromObject(dataMap).toString());
		return SUCCESS;
	}
	
	public String doNewDocument() {
		
		try {
			Map<String,Object> datas = new HashMap<String, Object>();
			WebUser user = getUser();
			ParamsTable params = getParams();
			DocumentProcess process = (DocumentProcess) getProcess();
			
			DocumentHtmlBean dochtmlBean = new DocumentHtmlBean();
			dochtmlBean.setHttpRequest(ServletActionContext.getRequest());
			dochtmlBean.setHttpResponse(ServletActionContext.getResponse());
			dochtmlBean.setWebUser(user);
			
			boolean withOld = params.getParameterAsBoolean("_withOld");
			
			Document doc = null;
			if(withOld){
				doc = process.doNew(dochtmlBean.getForm(), user, params);
			}else{
				doc = process.doNewWithOutItems(dochtmlBean.getForm(), user, params);
			}
			ServletActionContext.getRequest().setAttribute("content", doc);
			
			//1.输出操作按钮
			datas.put("activityHtml", dochtmlBean.getActBtnHTML());
			
			//2.输出表单
			datas.put("formHtml", dochtmlBean.getFormHTML());
			
			//3.重新填充系统字段
			Map<String, String> systemFields = new HashMap<String, String>();//存放系统字段
			systemFields.put("content.id", doc.getId());
			systemFields.put("content.authorId", doc.getAuthorId());
			systemFields.put("content.authorDeptIndex", doc.getAuthorDeptIndex());
			systemFields.put("content.versions", String.valueOf(doc.getVersions()));
			systemFields.put("content.mappingId", doc.getMappingId());
			systemFields.put("content.stateInt", "0");
			systemFields.put("_currid", "");
//			systemFields.put("_flowid", "");
//			systemFields.put("flowid", "");
			systemFields.put("content.auditorList", "");
			systemFields.put("content.stateid", "");
			datas.put("systemFields", systemFields);
			
			//4.输出流程处理人列表、流程状态标签、流程历史按钮
			if(StringUtil.isBlank(doc.getStateid())){
				datas.put("processorHtml", StateMachineHelper.toProcessorHtml(doc, user));
				datas.put("flowStateHtml", StateMachineHelper.toFlowStateHtml(doc));
			}
			
			addActionResult(true, "", datas);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		ServletActionContext.getRequest().setAttribute("result", JSONObject.fromObject(dataMap).toString());
		return SUCCESS;
	}
	
	public String doPrint(){
		try {
			ParamsTable params = getParams();
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, params.getParameterAsString("application"));
			
			String docid = params.getParameterAsString("_docid");
			Document doc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, getUser());
			//Document doc = (Document) process.doView(docid);
			setContent(doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	

	public IRunTimeProcess<Document> getProcess() {
		try {
			this.process = ProcessFactory.createRuntimeProcess(DocumentProcess.class, getApplication());
		} catch (CreateProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.process;
	}
	
	 /**
     * Struts2序列化指定属性时，必须有该属性的getter方法
     * @return
     */
    public Map<String, Object> getDataMap() {
        return dataMap;
    }
    
    /**
     * 添加Action处理结果
     * @param isSuccess
     * 		是否成功处理
     * @param message
     * 		返回消息
     * @param data
     * 		返回数据
     */
    public void addActionResult(boolean isSuccess,String message,Object data){
    	dataMap.put(ACTION_RESULT_KEY, isSuccess?ACTION_RESULT_VALUE_SUCCESS : ACTION_RESULT_VALUE_FAULT );
    	dataMap.put(ACTION_MESSAGE_KEY, message);
    	if(data != null){
    		dataMap.put(ACTION_DATA_KEY, data);
    	}
    }
    
    private Activity findActivityById(String actid) throws Exception {
    	ParamsTable params = getParams();
    	String _formid = params.getParameterAsString("_formid");
    	String _viewid = params.getParameterAsString("_viewid");
		FormProcess formProcess = (FormProcess) ProcessFactory
				.createProcess(FormProcess.class);
		ViewProcess viewProcess = (ViewProcess) ProcessFactory
				.createProcess(ViewProcess.class);

		ActivityParent actParent = null;
		Activity act = null;
		
		if (!StringUtil.isBlank(_formid)) {
			actParent =  (ActivityParent) formProcess.doView(_formid);
			act = actParent.findActivity(actid);
		}


		if (act == null && !StringUtil.isBlank(_viewid)) {
			actParent = (ActivityParent) viewProcess.doView(_viewid);
			act = actParent.findActivity(actid);
			if(((View)actParent).getSearchForm()!=null){
				setContent(((View)actParent).getSearchForm().createDocument(params, getUser()));
			}
		} 
		
		if (act == null && !StringUtil.isBlank(params.getParameterAsString(
				"_jumpForm"))) {
			actParent = (ActivityParent) formProcess.doView(params
					.getParameterAsString("_jumpForm"));
			act = actParent.findActivity(actid);
		} 

		if (act == null && !StringUtil.isBlank(params.getParameterAsString(
				"_templateForm"))) {
			actParent =  (ActivityParent) formProcess.doView(params
					.getParameterAsString("_templateForm"));
			act = actParent.findActivity(actid);
		}

		return act;
	}
    
    /**
	 * 校验当前用户是否可以保存文档.
	 * 根据当前Document是否有子Document并且是否可以编辑,若有子Document并且可以编辑,返回true,
	 * 此时可以保存当前Document. 并根据Document id 、 flow(流程)id 与当前用户作为参数条件来判断.
	 */
	private void validateDocument() {
		Document doc = (Document) getContent();

		try {
			final ParamsTable params = getParams();
			doc.setDomainid(getDomain());
			doc = DocumentHelper.rebuildDocument(doc, params, getUser());
			final WebUser webUser = getUser();
			String _flowType = params.getParameterAsString("_flowType");
			boolean isWithoutValidate = params
					.getParameterAsBoolean("isWithoutValidate");
			if (isWithoutValidate)
				return;
			if ("retracement".equals(_flowType)) {
				_flowType = "85";
			}
			DocumentProcess proxy = (DocumentProcess) ProcessFactory
					.createRuntimeProcess(DocumentProcess.class,
							getApplication());
			if ((_flowType != null && _flowType
					.equals(FlowType.RUNNING2RUNNING_RETRACEMENT))
					|| proxy.isDocSaveUser(doc, params, webUser)) {
				Collection<ValidateMessage> errors = proxy.doValidate(doc,
						params, webUser);
				if (errors != null && errors.size() > 0) {
					for (Iterator<ValidateMessage> iter = errors.iterator(); iter
							.hasNext();) {
						ValidateMessage err = (ValidateMessage) iter.next();
						this.addFieldError(err.getFieldname(), err
								.getErrmessage());
					}
				}
			} else {
				addFieldError("isDocSaveUser", "{*[core.document.cannotsave]*}");
			}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
			e.printStackTrace();
		}
		setContent(doc);
	}
	
	public String get_formid() {
		return _formid;
	}

	public void set_formid(String _formid) {
		this._formid = _formid;
	}

	public String get_viewid() {
		return _viewid;
	}

	public void set_viewid(String _viewid) {
		this._viewid = _viewid;
	}

	public String get_activityid() {
		return _activityid;
	}

	public void set_activityid(String _activityid) {
		this._activityid = _activityid;
	}

	public String[] get_selects() {
		return _selects;
	}

	public void set_selects(String[] _selects) {
		this._selects = _selects;
	}

	public String get_attitude() {
		return _attitude;
	}

	public void set_attitude(String _attitude) {
		this._attitude = _attitude;
	}

	public String get_targetNode() {
		return _targetNode;
	}

	public void set_targetNode(String _targetNode) {
		this._targetNode = _targetNode;
	}


	public class ActionResult{
		
		public static final String TYPE_FORM = "form";
		public static final String TYPE_VIEW = "view";
		public static final String TYPE_BACK = "back";
		public static final String TYPE_CLOSE = "close";
		public static final String TYPE_MESSAGE = "message";
		public static final String TYPE_VALIDATE = "validate";
		public static final String TYPE_EXCEPTION = "exception";
		
		
		private String resultType;
		
		private Object resultData;
		
		private String activityId;
		
		private int activityType;
		
		private String activityAferAction;

		public String getResultType() {
			return resultType;
		}

		public void setResultType(String resultType) {
			this.resultType = resultType;
		}

		public Object getResultData() {
			return resultData;
		}

		public void setResultData(Object resultData) {
			this.resultData = resultData;
		}

		public String getActivityId() {
			return activityId;
		}

		public void setActivityId(String activityId) {
			this.activityId = activityId;
		}

		public int getActivityType() {
			return activityType;
		}

		public void setActivityType(int activityType) {
			this.activityType = activityType;
		}

		public String getActivityAferAction() {
			return activityAferAction;
		}

		public void setActivityAferAction(String activityAferAction) {
			this.activityAferAction = activityAferAction;
		}
		
		
	}
	

}
