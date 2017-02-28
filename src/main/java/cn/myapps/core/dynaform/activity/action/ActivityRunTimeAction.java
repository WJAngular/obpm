package cn.myapps.core.dynaform.activity.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.action.DocumentHelper;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.logger.action.LogHelper;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class ActivityRunTimeAction extends AbstractRunTimeAction<Document> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4421551010061913544L;

	private String _formid;

	private String _viewid;

	private String _activityid;

	private String[] _selects;

	private String _attitude;
	
	/**当前审批节点ID**/
	private String _targetNode;

	public String get_attitude() {
		return _attitude;
	}

	public void set_attitude(String attitude) {
		_attitude = attitude;
	}

	public String get_formid() {
		return _formid;
	}

	public void set_formid(String formid) {
		_formid = formid;
	}

	public String get_viewid() {
		return _viewid;
	}

	public void set_viewid(String viewid) {
		_viewid = viewid;
	}

	public String get_activityid() {
		return _activityid;
	}

	public void set_activityid(String activityid) {
		_activityid = activityid;
	}

	public String get_targetNode() {
		return _targetNode;
	}

	public void set_targetNode(String targetNode) {
		_targetNode = targetNode;
	}

	/**
	 * 返回存放Document id 数组
	 * 
	 * @return 存放Document id 数组
	 * @uml.property name="_selects"
	 */
	public String[] get_selects() {
		return _selects;
	}

	/**
	 * 设置存放Document id 数组
	 * 
	 * @param selects
	 * @uml.property name="_selects"
	 */
	public void set_selects(String[] selects) {
		this._selects = selects;
	}

	public ActivityRunTimeAction() throws ClassNotFoundException {
		setContent(new Document());
	}

	public IRunTimeProcess<Document> getProcess() {
		IRunTimeProcess<Document> process = null;
		try {
			process = ProcessFactory.createRuntimeProcess(
					DocumentProcess.class, getApplication());
		} catch (CreateProcessException e) {
			this.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
			e.printStackTrace();
		}
		return process;
	}

	private boolean isAction = true;

	/**
	 * 操作执行前触发
	 * <p>
	 * Activity(按钮)类型常量分别为:
	 * <p>
	 * 1:"ACTIVITY_TYPE_DOCUMENT_QUERY"(查询Document);
	 * 2:"ACTIVITY_TYPE_DOCUMENT_CREATE"(创建Document);
	 * <p>
	 * 3:"ACTIVITY_TYPE_DOCUMENT_DELETE"(删除Document);
	 * 4:"ACTIVITY_TYPE_DOCUMENT_UPDATE"(更新Document);
	 * <p>
	 * 5:"ACTIVITY_TYPE_WORKFLOW_PROCESS"(流程处理);
	 * 6:"ACTIVITY_TYPE_SCRIPT_PROCESS"(SCRIPT);
	 * <p>
	 * 7:"ACTIVITY_TYPE_DOCUMENT_MODIFY"(回退);
	 * 8:"ACTIVITY_TYPE_CLOSE_WINDOW"(关闭窗口);
	 * <p>
	 * 9:"ACTIVITY_TYPE_SAVE_CLOSE_WINDOW"(保存Document并关闭窗口);
	 * 10:"ACTIVITY_TYPE_DOCUMENT_BACK"(回退);
	 * <p>
	 * 11:"ACTIVITY_TYPE_SAVE_BACK"(保存Document并回退);
	 * 12:"ACTIVITY_TYPE_SAVE_NEW_WITH_OLD"(保存并新建保留有旧数据的Document);
	 * <p>
	 * 13:"ACTIVITY_TYPE_Nothing"; 14:"ACTIVITY_TYPE_PRINT"(普通打印);
	 * <p>
	 * 15:"ACTIVITY_TYPE_PRINT_WITHFLOWHIS"(打印包含有流程);
	 * 16:"ACTIVITY_TYPE_EXPTOEXCEL"(将数据导出到EXCEL);
	 * <p>
	 * 17:"ACTIVITY_TYPE_SAVE_NEW_WITHOUT_OLD"((保存并新建一条空的Document));
	 * 
	 * 
	 * @return result.
	 * @throws Exception
	 */
	public String doBefore() throws Exception {
		ParamsTable params = getParams();
		String _activityid = params.getParameterAsString("_activityid");
		String formid = params.getParameterAsString("_formid");
		String _templateFormId = params.getParameterAsString("_templateForm");
		String parentId = params.getParameterAsString("parentid");
		String docid = (String) ServletActionContext.getRequest().getAttribute(
				"content.id");
		Activity act = null;
		if (_activityid != null && _activityid.trim().length() > 0) {
			act = findActivityById(get_activityid());
			ServletActionContext.getRequest().setAttribute(
					Web.REQUEST_ATTRIBUTE_ACTIVITY, act);
		}

		Document po = (Document) getProcess().doView(docid);
		Document doc = po!=null? po : new Document();

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

			if (!StringUtil.isBlank(docid)) {
				doc.setId(docid);
				doc = form
						.createDocumentWithSystemField(params, doc, getUser());
			} else if (!StringUtil.isBlank(parentId)) {
				doc = (Document) getUser().getFromTmpspace(parentId);
			}
		} 
		setContent(doc);

		// 保存操作日志
		if (isAction) {
			LogHelper.saveLogByDynaform(act, doc, getUser());
		}

		return SUCCESS;
	}

	/**
	 * 操作执行时促发
	 * 
	 * @return
	 */
	public String doAction() {
		try {
			ParamsTable params = getParams();
			HttpServletRequest request = ServletActionContext.getRequest();
			String _activityid = params.getParameterAsString("_activityid");

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
				if (act.getType() == ActivityType.NOTHING
						|| act.getType() == ActivityType.DOCUMENT_UPDATE
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
						|| act.getType() == ActivityType.SAVE_NEW) {

//					prepareDocument();  2013-12-06 在dobefore里去重新构建doc系统字段,这里不需要再次构建
					validateDocument();
					if(this.hasFieldErrors()){
						return "form";
					}
				}

				// 执行Activity的动作
				return act.execute(this, (Document) this.getContent(),
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

	/**
	 * 操作执行后触发
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doAfter() throws Exception {
		ParamsTable params = getParams();
		String _activityid = params.getParameterAsString("_activityid");

		Activity act = null;
		if (_activityid != null && _activityid.trim().length() > 0) {
			act = findActivityById(_activityid);
			ServletActionContext.getRequest().setAttribute(
					Web.REQUEST_ATTRIBUTE_ACTIVITY, act);
		}

		Document doc = (Document) getContent();
		setContent(doc);

		return SUCCESS;
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

	/**
	 * 执行保存或提交流程等相关操作,必须加入此拦截以确保数据完整
	 */
	public void prepareDocument() throws Exception {
		WebUser user = getUser();
		FormProcess formPross = (FormProcess) ProcessFactory
				.createProcess(FormProcess.class);

		DocumentProcess proxy = (DocumentProcess) getProcess();
		// 先与po合并
		Document mo = proxy.mergePO((Document) getContent(), user);
		if (_formid != null) {
			// 重新计算
			Document content = ((Form) formPross.doView(_formid))
					.recalculateDocument(mo, getParams(), user);
			content.setAuditorList(getParams().getParameterAsString(
					"content.auditorList"));
			setContent(content);
		}
	}

	private Activity findActivityById(String actid) throws Exception {
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
		} 
		
		if (act == null && !StringUtil.isBlank(getParams().getParameterAsString(
				"_jumpForm"))) {
			actParent = (ActivityParent) formProcess.doView(getParams()
					.getParameterAsString("_jumpForm"));
			act = actParent.findActivity(actid);
		} 

		if (act == null && !StringUtil.isBlank(getParams().getParameterAsString(
				"_templateForm"))) {
			actParent =  (ActivityParent) formProcess.doView(getParams()
					.getParameterAsString("_templateForm"));
			act = actParent.findActivity(actid);
		}

		return act;
	}

}
