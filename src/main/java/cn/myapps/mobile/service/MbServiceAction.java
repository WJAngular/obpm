package cn.myapps.mobile.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jfree.util.Log;

import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.activity.action.ActivityUtil;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.activity.ejb.ActivityProcess;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.action.DocumentHelper;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentBuilder;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.form.action.FormHelper;
import cn.myapps.core.dynaform.form.ejb.FileManagerField;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.dynaform.view.ejb.ViewType;
import cn.myapps.core.filemanager.FileOperate;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.macro.runner.JsMessage;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.upload.ejb.UploadProcess;
import cn.myapps.core.upload.ejb.UploadVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.CompleteNode;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.SuspendNode;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.core.workflow.engine.StateMachineUtil;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.mobile.view.MbViewHelper;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;


import flex.messaging.util.URLDecoder;

/**
 * @author nicholas
 */
public class MbServiceAction extends BaseAction<Activity> {

	private static final long serialVersionUID = -5649138986770418640L;
	private static final Logger LOG = Logger.getLogger(MbServiceAction.class);

	private String _activityid;
	private View view;
	private String _currpage;

	/**
	 * 默认构造方法
	 * 
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public MbServiceAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(ActivityProcess.class), new Activity());
	}

	/**
	 * <p>
	 * 触发对Document、流程与view的等有关操作. 根据Activity(按钮)类型实现对Document,流程与VIEW等不同的操作.
	 * </p>
	 * 
	 * <ul>
	 * Activity(按钮)类型常量分别为:
	 * <li>1:ACTIVITY_TYPE_DOCUMENT_QUERY"(查询Document);</li>
	 * <li>2:ACTIVITY_TYPE_DOCUMENT_CREATE"(创建Document);</li>
	 * <li>3:ACTIVITY_TYPE_DOCUMENT_DELETE"(删除Document);</li>
	 * <li>4:ACTIVITY_TYPE_DOCUMENT_UPDATE"(更新Document);</li>
	 * <li>5:ACTIVITY_TYPE_WORKFLOW_PROCESS"(流程处理);</li>
	 * <li>6:ACTIVITY_TYPE_SCRIPT_PROCESS"(SCRIPT);</li>
	 * <li>7:ACTIVITY_TYPE_DOCUMENT_MODIFY"(回退);</li>
	 * <li>8:ACTIVITY_TYPE_CLOSE_WINDOW"(关闭窗口);</li>
	 * <li>9:ACTIVITY_TYPE_SAVE_CLOSE_WINDOW"(保存Document并关闭窗口);</li>
	 * <li>10:ACTIVITY_TYPE_DOCUMENT_BACK"(回退);</li>
	 * <li>11:ACTIVITY_TYPE_SAVE_BACK"(保存Document并回退);</li>
	 * <li>12:ACTIVITY_TYPE_SAVE_NEW_WITH_OLD"(保存并新建保留有旧数据的Document);</li>
	 * <li>13:ACTIVITY_TYPE_Nothing";</li>
	 * <li>14:ACTIVITY_TYPE_PRINT"(普通打印);</li>
	 * <li>15:ACTIVITY_TYPE_PRINT_WITHFLOWHIS"(打印包含有流程);</li>
	 * <li>16:ACTIVITY_TYPE_EXPTOEXCEL"(将数据导出到EXCEL);</li>
	 * <li>17:ACTIVITY_TYPE_SAVE_NEW_WITHOUT_OLD"((保存并新建一条空的Document));</li>
	 * </ul>
	 * 
	 * @return result.
	 * @throws Exception
	 */
	public String doAction() {
		try {
			ParamsTable params = getParams();
			String result = null;
			Activity act = null;
			String afterResult = "";
			if (!StringUtil.isBlank(_activityid) && !_activityid.equals("null")) {
				String formid = params.getParameterAsString("_formid");
				String viewid = params.getParameterAsString("_viewid");
				if (!StringUtil.isBlank(formid)) {
					FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
					Form form = (Form) formPross.doView(formid);
					act = form.findActivity(_activityid);
				} else {
					ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
				    view = (View) viewProcess.doView(viewid);
					act = view.findActivity(_activityid);
				}

				ServletActionContext.getRequest().setAttribute("ACTIVITY_INSTNACE", act);

				Document doc = createDocument();
				String beforeResult = doBefore(act,doc);
				if (beforeResult.trim().equalsIgnoreCase("error")) {
					return ERROR;
				}else if(beforeResult.trim().equalsIgnoreCase("success")){
					if(!StringUtil.isBlank(formid) || !StringUtil.isBlank(viewid)){
						return SUCCESS;
					}
				}
				switch (act.getType()) {
				case ActivityType.BATCH_APPROVE:
					result = doBattchApprove();
					break;
				case ActivityType.CLOSE_WINDOW:
					result = "viewList";
					break;
				case ActivityType.DOCUMENT_BACK:
					result = "viewList";
					break;
				case ActivityType.DOCUMENT_CREATE:
					result = doNewDocument(act,doc);
					break;
				case ActivityType.DOCUMENT_DELETE:
					result = doDelete(act);
					break;
				case ActivityType.DOCUMENT_MODIFY:
					result = doViewDocument();
					break;
				case ActivityType.DOCUMENT_QUERY:
					result = doJumpView();
					break;
				case ActivityType.DOCUMENT_UPDATE:
					result = doSaveDocument(act,doc);
					break;
				case ActivityType.SAVE_SARTWORKFLOW:
					result = doSaveDocumentStartFlow(act,doc);
					break;
				case ActivityType.DOCUMENT_COPY:
					result = doSaveAndCopy(act,doc);
					break;
				case ActivityType.EXPTOEXCEL:

					break;
				case ActivityType.NOTHING:

					break;
				case ActivityType.PRINT:

					break;
				case ActivityType.PRINT_WITHFLOWHIS:
					break;
				case ActivityType.SAVE_BACK:
					result = doSaveBack(act,doc);
					break;
				case ActivityType.SAVE_CLOSE_WINDOW:
					result = doSaveClose(act,doc);
					break;
				case ActivityType.SAVE_NEW_WITH_OLD:
					result = doSaveNewWithOld(act,doc);
					break;
				case ActivityType.SAVE_NEW_WITHOUT_OLD:
					result = doSaveNewWithOutOld(act,doc);
					break;
				case ActivityType.SAVE_WITHOUT_VALIDATE:
					result = doSaveWithOutValidate(act,doc);
					break;
				case ActivityType.SCRIPT_PROCESS:
					break;
				case ActivityType.WORKFLOW_PROCESS:
					result = doSubmitDocument(doc);
					break;
//				case ActivityType.WORKFLOW_RETRACEMENT:
//					result = doWorkFlowRetracemend(doc);
//					break;
				case ActivityType.START_WORKFLOW://启动流程
					result = doSaveDocumentStartFlow(act,doc);
					break;
				case ActivityType.CLEAR_ALL://视图清空数据
					result = doDeleteAll();
					break;
				case ActivityType.JUMP: //跳转按钮
					result = doJumpDocument(act);
					break;
				case ActivityType.FILE_DOWNLOAD: //下载按钮
					result = doFileDownload(act);
					break;
				case ActivityType.SAVE_NEW:
					result = doSaveNew(act,doc);
					break;
				default:
					break;
				}
				if (result == null) {
					throw new Exception("Unsupport activity type!");
				} else {
					if (getContent() instanceof Document) {
						afterResult = doAfter(act, (Document) getContent());
						if (afterResult.trim().equalsIgnoreCase("error")) {
							return ERROR;
						}
					}else if(getContent() instanceof View) {
						afterResult = doAfter(act, new Document());
						if (afterResult.trim().equalsIgnoreCase("error")) {
							return ERROR;
						}
					}
				}
			} else {
				if(params.getParameter("activityType")!=null){
					//android菜单跳转到表单
					int activityType = params.getParameterAsInteger("activityType");
					if(activityType == ActivityType.DOCUMENT_CREATE){
						Activity activity = new Activity();
						String formid = params.getParameterAsString("_formid");
						activity.setOnActionForm(formid);
						result = doNewDocument(activity,new Document());
					}else{
						result = doViewDocument();
					}
				}else{
					result = doViewDocument();
				}
			}
			if(getContent() instanceof Document){
				Document doc = (Document) getContent();
				if(doc != null){
					MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
					toFormXml(doc, new ArrayList<String>(), false);
				}
			}
			if(!StringUtil.isBlank(afterResult) && afterResult.indexOf(MobileConstant.TAG_JSMESSAGE)>0){
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				String toXml = (String) session.getAttribute("toXml");
				session.setAttribute("toXml", toXml + afterResult);
			}
			return result;
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			LOG.warn(e);
			return ERROR;
		}
	}
	
	
	/**
	 * 查出这个formid的所有document,然后一条条删除，如果是审批中或者是审批完成不能删除的，则放到errorfiel中显示出来
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	public String doDeleteAll() throws Exception {
		try {
			FormProcess fb = (FormProcess) (ProcessFactory.createProcess(FormProcess.class));
			String formid = view.getRelatedForm();
			Form form = (Form) fb.doView(formid);

			DocumentProcess proxy = createDocumentProcess(getApplication());
			Collection<Document> docs = proxy.queryByDQL("$formname = '" + form.getFullName() + "'", getDomain()).datas;

			for (Iterator<Document> iter = docs.iterator(); iter.hasNext();) {
				Document doc = (Document) iter.next();
				proxy.doRemove(doc.getId());

			}
			addActionMessage("{*[delete.successful]*}");
		} catch (Exception e) {
			addFieldError("", e.getMessage());
			return ERROR;
		}

		return "viewlist";
	}
	
	
	/**
	 * 批量审批流程
	 * 
	 * @return SUCCESS
	 */
	public String doBattchApprove() {
		ParamsTable params = getParams();
		String limistStrList = params.getParameterAsString("_approveLimit");
		//String limistStrList = (String) ServletActionContext.getRequest().getAttribute("ApproveLimist");
		Collection<String> limistList = new ArrayList<String>();

		if (!StringUtil.isBlank(limistStrList)) {
			limistList = Arrays.asList(limistStrList.split(","));
		}

		try {
			if (_selects != null) {
				if(_selects.length==1 && _selects[0].indexOf(";")!=-1){
					_selects =  _selects[0].split(";");
				}
			}
			DocumentProcess proxy = createDocumentProcess(getApplication());
			proxy.doBatchApprove(_selects, getUser(), getEnvironment(), getParams(), limistList);
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}

		return "viewList";
	}
	
	/**
	 * 回撤
	 * @return
	 * @throws Exception
	 */
	private String doWorkFlowRetracemend(Document doc) throws Exception{
		WebUser user = this.getUser();

		if (user.getStatus() == 1) {
			try {
				ParamsTable params = getParams();
				DocumentProcess proxy = createDocumentProcess(getApplication());
//				String formid = params.getParameterAsString("_formid");
//				FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
//				Form form = (Form) formProcess.doView(formid);
//				String docid = params.getParameterAsString("_docid");
//				Document olddoc = (Document) proxy.doView(docid);
//				Document doc = form.createDocument(olddoc, params, user);
//				if (docid != null && docid.length() > 0) {
//					doc.setId(docid);
//				}
				BillDefiVO flowVO = doc.getState().getFlowVO();
				FlowDiagram fd = flowVO.toFlowDiagram();
				NodeRT nodert = (NodeRT) doc.getState().getNoderts().iterator().next();
				Node currNode = (Node) fd.getElementByID(nodert.getNodeid());
				Node nextNode = StateMachine.getBackNodeByHis(doc, flowVO, currNode.id, user, FlowState.RUNNING);
				if (nextNode != null) {

					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), application);
					runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());

					boolean allowRetracement = false;
					if (((ManualNode) nextNode).retracementEditMode == 0 && ((ManualNode) nextNode).cRetracement) {
						allowRetracement = true;
					} else if (((ManualNode) nextNode).retracementEditMode == 1
							&& ((ManualNode) nextNode).retracementScript != null
							&& (((ManualNode) nextNode).retracementScript).trim().length() > 0) {
						StringBuffer label = new StringBuffer();
						label.append(doc.getFormname()).append(".Activity(").append(params.getParameter("_activityId"))
								.append("流程回撤").append(".retracementScript");
						Object result = runner.run(label.toString(), ((ManualNode) nextNode).retracementScript);
						if (result != null && result instanceof Boolean) {
							if (((Boolean) result).booleanValue())
								allowRetracement = true;
						}
					}

					if (allowRetracement) {
						// 指的审批人
						String submitTo = "[{\"nodeid\":'" + nextNode.id + "',\"isToPerson\":'true',\"userids\":\"["
								+ user.getId() + "]\"},]";
						params.setParameter("submitTo", submitTo);
						params.setParameter("doRetracement", "true");

						String[] nextids = { nextNode.id };
						((DocumentProcess) proxy).doFlow(doc, params, currNode.id, nextids,
								FlowType.RUNNING2RUNNING_RETRACEMENT, get_attitude(), user);
						// doc.setReadusers("");
//						setContent(doc);
						proxy.doUpdate(doc, true,true);
						MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
						set_attitude("");// 将remarks清空
					} else {
						this.addFieldError("System Error", "此流程状态下不允许回撤");
						return ERROR;
					}
				} else {
					this.addFieldError("System Error", "您没有回撤的权限");
					return ERROR;
				}
			} catch (Exception e) {
				this.addFieldError("System Error", e.getMessage());
				e.printStackTrace();
				LOG.warn(e);
				return ERROR;
			}
			return SUCCESS;
		} else {
			this.addFieldError("System Error", "{*[core.user.noeffectived]*}");
			return ERROR;
		}

	}
				
	/**
	 * 流程提交
	 * 
	 * @return
	 * @throws Exception
	 */
	private String doSubmitDocument(Document doc) throws Exception {
		if (!doValidate(doc)) return ERROR;
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			DocumentProcess proxy = createDocumentProcess(getApplication());
//			String formid = params.getParameterAsString("_formid");
//			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
//			Form form = (Form) formProcess.doView(formid);
//			String docid = params.getParameterAsString("_docid");
//			Document olddoc = (Document) proxy.doView(docid);
//			Document doc = form.createDocument(olddoc, params, user);
//			if (docid != null && docid.length() > 0) {
//				doc.setId(docid);
//			}
			((DocumentProcess) proxy).doFlow(doc, params, get_currid(), get_nextids(), get_flowType(doc.getState().getFlowVO()),
					get_attitude(), user);

			setContent(doc);
			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			LOG.warn(e);
			return ERROR;
		}
		ViewProcess viewProcess = (ViewProcess)ProcessFactory.createProcess(ViewProcess.class);
		if(params.getParameterAsString("_viewid")!=null && viewProcess.doView(params.getParameterAsString("_viewid"))!=null){
			return "viewList";
		}else{
			return SUCCESS;
		}
	}
	
	private Document createDocument() throws Exception{
		Document doc = null;
		try{
			ParamsTable params = getParams();
			WebUser webUser = getUser();
			DocumentProcess proxy = createDocumentProcess(getApplication());
			String formid = params.getParameterAsString("_formid");
			String parentId = params.getParameterAsString("parentid");
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = (Form) formProcess.doView(formid);
			if(StringUtil.isBlank(parentId) || parentId.equals("null")){
				params.removeParameter("parentid");
			}
			String docid = getParams().getParameterAsString("_docid");
			Document olddoc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, webUser);
			if (olddoc == null) {
				olddoc = (Document) proxy.doView(docid);
			}
			if(form != null){
				if (olddoc != null) {
					doc = form.createDocument(olddoc, params, webUser);
				} else {
					doc = form.createDocument(params, webUser);
				}
			}else{
				doc = new Document();
			}
			if (docid != null && docid.length() > 0) {
				doc.setId(docid);
			}
			if(doc != null){
				doc.setDomainid(getDomain());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		if(doc == null){
			doc = new Document();
		}
		return doc;
	}
	
	private String doBefore(Activity act,Document doc) throws Exception {
		ParamsTable params = getParams();
//		String formid = params.getParameterAsString("_formid");
//		String parentId = params.getParameterAsString("parentid");
//		String docid = params.getParameterAsString("_docid");
		WebUser webUser = getUser();
//		Document doc = null;
//		if(StringUtil.isBlank(parentId) || parentId.equals("null")){
//			params.removeParameter("parentid");
//		}
//		if (!StringUtil.isBlank(docid)) {
//			doc = (Document) webUser.getFromTmpspace(docid);
//		} else if (!StringUtil.isBlank(parentId) && !"null".equals(parentId)) {
//			doc = (Document) webUser.getFromTmpspace(parentId);
//		}
//		if (!StringUtil.isBlank(formid)) {
//			FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
//			Form form = (Form) formPross.doView(formid);
//			doc = form.createDocument(doc, params, webUser);
//		} else {
//			doc = new Document();
//		}
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), act.getApplicationid());
		runner.initBSFManager(doc, params, webUser, new ArrayList<ValidateMessage>());
		if ((act.getBeforeActionScript()) != null && (act.getBeforeActionScript()).trim().length() > 0) {

			StringBuffer label = new StringBuffer();
			label.append("Activity Action(").append(act.getId()).append(")." + act.getName()).append(
					".runIsEditAbleScript");
			Object result = runner.run(label.toString(), act.getBeforeActionScript());

			if (result != null) {
				if (result instanceof JsMessage) {
					if(((JsMessage)result).getType() == JsMessage.TYPE_ALERT){
						StringBuffer sb = new StringBuffer();
						sb.append("<").append(MobileConstant.TAG_JSMESSAGE).append(" ");
						sb.append(MobileConstant.ATT_TYPE).append(" = \"").append(((JsMessage)result).getType()).append("\">");
						sb.append(((JsMessage)result).getContent());
						sb.append("</").append(MobileConstant.TAG_JSMESSAGE).append(">");
						HttpServletRequest request = ServletActionContext.getRequest();
						HttpSession session = request.getSession();
						session.setAttribute("toXml", sb.toString());
						return SUCCESS;
					}
					this.addFieldError("SystemError", ((JsMessage) result).getContent());
					return ERROR;
				} else if (result instanceof String && ((String) result).trim().length() > 0) {
					this.addFieldError("SystemError", result.toString());
					return ERROR;
				}
			}
		}
		return "";
	}

	private String doAfter(Activity act, Document doc) throws Exception {
		ParamsTable params = getParams();
		WebUser webUser = getUser();
		if (doc == null) {
			doc = new Document();
		}
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), act.getApplicationid());
		runner.initBSFManager(doc, params, webUser, new ArrayList<ValidateMessage>());
		if ((act.getAfterActionScript()) != null && (act.getAfterActionScript()).trim().length() > 0) {

			StringBuffer label = new StringBuffer();
			label.append("Activity Action").append(act.getId()).append("." + act.getName()).append("afterActionScript");
			Object result = runner.run(label.toString(), act.getAfterActionScript());

			if (result != null) {
				if (result instanceof JsMessage) {
					if(((JsMessage)result).getType() == JsMessage.TYPE_ALERT){
						StringBuffer sb = new StringBuffer();
						sb.append("<").append(MobileConstant.TAG_JSMESSAGE).append(" ");
						sb.append(MobileConstant.ATT_TYPE).append(" = \"").append(((JsMessage)result).getType()).append("\">");
						sb.append(((JsMessage)result).getContent());
						sb.append("</").append(MobileConstant.TAG_JSMESSAGE).append(">");
						return sb.toString();
					}
					this.addFieldError("SystemError", ((JsMessage) result).getContent());
					return ERROR;
				} else if (result instanceof String && ((String) result).trim().length() > 0) {
					this.addFieldError("SystemError", result.toString());
					return ERROR;
				}
			}
		}
		return "";
	}

	public String doViewFlow() throws Exception {
		return "viewDocument";
	}

	public String doShowFlowHis() throws Exception {
		if (!toFlowHisXml()) return ERROR;
		return SUCCESS;
	}

	/**
	 * 根据文档主键，查找文档.
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	private String doViewDocument() {
		try {
			WebUser user = this.getUser();
			DocumentProcess proxy = createDocumentProcess(getApplication());
			String _docid = getParams().getParameterAsString("_docid");
			Document doc = (Document) proxy.doView(_docid);
			setContent(doc);
//			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
//			toFormXml(doc, new ArrayList<String>(), false);
		} catch (Exception e) {
			LOG.warn(e);
		}

		return "viewDocument";
	}

	public String doRefresh() throws Exception {
		// synchronized (user) {
		try {
			WebUser user = this.getUser();

			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);

			ParamsTable params = getParams();
			String formid = params.getParameterAsString("_formid");

			Form form = (Form) formProcess.doView(formid);

			String docid = params.getParameterAsString("_docid");
			DocumentProcess proxy = createDocumentProcess(getApplication());
			Document olddoc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, user);
			if (olddoc == null) {
				olddoc = (Document) proxy.doView(docid);
			}

//			Document doc = (Document) proxy.doView(docid);
//			doc = form.createDocument(doc, params, user);
			Document doc = form.createDocument(olddoc, params, user);

			if (docid != null && docid.length() > 0) {
				doc.setId(docid);
			}
			setContent(doc);
			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
//			toFormXml(doc, doc.compareTo(olddoc), true);
			toFormXml(doc,new ArrayList<String>(),false);
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			LOG.warn(e);
			return ERROR;
		}
		return "refreshDocument";
	}

	public String doAfter() throws Exception {
		return doAction();
	}

	/**
	 * 保存并创建一条有数据的记录文档. 如果成功处理，返回"SUCCESS",将再创建一条有旧数据的记录文档。否则返回"INPUT",创建失败。
	 * 
	 * @return "SUCCESS" or "ERROR"
	 * @throws Exception
	 */
	public String doSaveNewWithOld(Activity act,Document doc) {
		try {
			if (doSaveDocument(act,doc).equals("viewDocument")) {
				return doNewDocument(act,doc);
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			addFieldError("SystemError", e.getMessage());
			LOG.warn(e);
			return ERROR;
		}

	}

	/**
	 * 保存并创建一条空的记录文档,如果成功处理. 返回"SUCCESS",将再创建一条空的记录文档。否则返回"INPUT",创建失败。
	 * 
	 * @return "SUCCESS" or "ERROR"
	 * @throws Exception
	 */
	public String doSaveNewWithOutOld(Activity act,Document doc) {
		try {
			if (doSaveDocument(act,doc).equals("viewDocument")) {
				WebUser user = getUser();
				FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				DocumentProcess proxy = createDocumentProcess(getApplication());
				ParamsTable params = getParams();
				String formid = params.getParameterAsString("_formid");

				Form form = (Form) formPross.doView(formid);
				if (form != null) {
					Document newDoc = proxy.doNewWithOutItems(form, user, params);
					setContent(newDoc);
//					MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
//					toFormXml(newDoc, new ArrayList<String>(), false);
				}

				return "viewDocument";
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * 保存并复制
	 * @param act
	 * @return
	 */
	public String doSaveAndCopy(Activity act,Document doc) {
		try {
			if (doSaveDocument(act,doc).equals("viewDocument")) {
				WebUser user = getUser();
				Document oldDoc = (Document) getContent();
				ParamsTable params = getParams();
				String formid = params.getParameterAsString("_formid");
				FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				DocumentProcess proxy = createDocumentProcess(getApplication());
				oldDoc.setDomainid(getDomain());
				Form form = (Form) formPross.doView(formid);
				Document newDoc = proxy.doNewWithChildren(form, user, params, oldDoc.getChilds());
				setContent(newDoc);
//				MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
//				toFormXml(newDoc, new ArrayList<String>(), false);
				return "viewDocument";
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 重新构建文档
	 * @param doc
	 * @param params
	 * @return
	 */
	protected Document rebuildDocument(Document doc, ParamsTable params) {
		String formid = params.getParameterAsString("_formid");
		try {
			if(!StringUtil.isBlank(params.getParameterAsString("_refreshDocument")) && !StringUtil.isBlank(doc.getId()) && !StringUtil.isBlank(formid)){
				doc = (Document) MemoryCacheUtil.getFromPrivateSpace(doc.getId(), getUser());
				FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				Form form = (Form) formPross.doView(formid);
				doc = form.createDocument(doc, params, getUser());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 保存文档并关闭
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doSaveClose(Activity act,Document doc) {
		ParamsTable params = getParams();
		String _viewid = params.getParameterAsString("");
		String xml = doSaveDocument(act,doc);
		if (xml.equals("viewDocument") && !StringUtil.isBlank(_viewid)){
			return "viewList";
		}else if(xml.equals(ERROR)){
			return ERROR;
		}else{
			return SUCCESS;
		}
	}

	private String doNewDocument(Activity act,Document doc) throws Exception {
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			// DocumentProcess proxy =
			// createDocumentProcess(user.getApplicationid());
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = (Form) formProcess.doView(act.getOnActionForm());
			if (form == null) {
				String formid = params.getParameterAsString("_formid");
				form = (Form) formProcess.doView(formid);
			}
			DocumentBuilder builder = new DocumentBuilder(doc, params);
			builder.setForm(form);
			Document newDoc = builder.getNewDocument(user);
			// proxy.doCreate(newDoc);
			setContent(newDoc);

//			MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
//			toFormXml(doc, new ArrayList<String>(), false);
			return "viewDocument";
		} catch (Exception e) {
			addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
	}

	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}

	public String doSaveWithOutValidate(Activity act,Document doc) {
		try {
			WebUser user = this.getUser();
			ParamsTable params = getParams();
//			String formid = params.getParameterAsString("_formid");
			DocumentProcess proxy = createDocumentProcess(getApplication());
//			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
//			Form form = (Form) formProcess.doView(formid);
//			Document doc = null;
//			String _docid = params.getParameterAsString("_docid");
//			if(_docid!=null && !_docid.equals("")){
//				doc = (Document)proxy.doView(_docid);
//			}
//			doc = form.createDocument(doc,params, user);
			doc.setLastmodifier(user.getId());
//			doc.setDomainid(getDomain());
			proxy.doCreateOrUpdate(doc, user);

			setContent(doc);
//			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
//			toFormXml(doc, new ArrayList<String>(), false);
		} catch (Exception e) {
			this.addFieldError("1", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}

		return SUCCESS;
	}

	/**
	 * 保存文档并返回.
	 * 
	 * @return SUCCESS OR ERROR
	 * @throws Exception
	 */
	public String doSaveBack(Activity act,Document doc) {
		ParamsTable params = getParams();
		String _viewid = params.getParameterAsString("");
		String xml = doSaveDocument(act,doc);
		if (xml.equals("viewDocument") && !StringUtil.isBlank(_viewid)){
			return "viewList";
		}else if(xml.equals(ERROR)){
			return ERROR;
		}else{
			return SUCCESS;
		}
	}

	/**
	 * 保存文档. 文档无状态并有流程时开启流程.
	 * 
	 * @return SUCCESS，ERROR
	 * @throws Exception
	 */
	private String doSaveDocument(Activity act,Document doc){
		if (!doValidate(doc))
			return ERROR;
		try {
			WebUser user = this.getUser();
			ParamsTable params = getParams();
//			String formid = params.getParameterAsString("_formid");
			DocumentProcess proxy = createDocumentProcess(getApplication());
//			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
//			Form form = (Form) formProcess.doView(formid);
//			String docid = getParams().getParameterAsString("_docid");
//			Document olddoc = (Document) proxy.doView(docid);
//			Document doc = null;
//			if (olddoc != null) {
//				doc = form.createDocument(olddoc, params, user);
//			} else {
//				doc = form.createDocument(params, user);
//			}
//			if (docid != null && docid.length() > 0) {
//				doc.setId(docid);
//			}
			doc.setDomainid(getDomain());
			proxy.doCreateOrUpdate(doc, user);
			//proxy.doStartFlowOrUpdate(doc, params, user);
			setContent(doc);
//			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
//			toFormXml(doc, new ArrayList<String>(), false);
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		// }
		return "viewDocument";
	}
	
	/**
	 * 文件下载.
	 * 
	 * @return SUCCESS，ERROR
	 * @throws Exception
	 */
	private String doFileDownload(Activity act){
		try {
			WebUser user = this.getUser();
			ParamsTable params = getParams();
			String formid = params.getParameterAsString("_formid");
			DocumentProcess proxy = createDocumentProcess(getApplication());
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = (Form) formProcess.doView(formid);
			String docid = getParams().getParameterAsString("_docid");
			Document olddoc = (Document) proxy.doView(docid);
			Document doc = null;
			if (olddoc != null) {
				doc = form.createDocument(olddoc, params, user);
			} else {
				doc = form.createDocument(params, user);
			}
			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),
					doc.getApplicationid());
			runner.initBSFManager(doc, params, getUser(),
					new java.util.ArrayList<ValidateMessage>());

			// 处理文件下载按钮
			if (act.getType() == ActivityType.FILE_DOWNLOAD
					&& !StringUtil.isBlank(act.getFileNameScript())) {
				StringBuffer label = new StringBuffer();
				label.append("Activity(").append(act.getId()).append(
						")." + act.getName()).append("fileNameScript");
				String result = (String) runner.run(label.toString(), act
						.getFileNameScript());
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				session.setAttribute("toXml", result);
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		// }
		return SUCCESS;
	}
	
	public String doFileDownload(){
		OutputStream os = null;
		BufferedInputStream reader = null;
		try {
			ParamsTable params = this.getParams();
			String encoding = "utf-8";
			String filename = params.getParameterAsString("filename");
			filename = URLDecoder.decode(filename, encoding);
			String filepath = params.getParameterAsString("filepath");
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String realPath = request.getSession().getServletContext().getRealPath("/");
			File file = new File(realPath + filepath);
			if(!file.exists()){
				return "找不到指定文件";
			}
			
			String agent = request.getHeader("USER-AGENT");
			String address = file.toString();
			if(null != agent && -1 != agent.indexOf("Firefox")){
				response.setContentType("application/x-download; charset=" + encoding + "");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + MimeUtility.encodeText(filename, encoding, "B") + "\"");
			}else{
				if(address.indexOf("pdf") == -1){
					response.setContentType("application/x-download; charset=" + encoding + "");
					response.setHeader("Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode(filename, encoding) + "\"");
				}
			}
			os = response.getOutputStream();

			reader = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[4096];
			int i = -1;
			while ((i = reader.read(buffer)) != -1) {
				os.write(buffer, 0, i);
			}
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return "下载失败";
		} finally {
			try{
				if (os != null) {
					reader.close();
				}
				if ( reader != null) {
					reader.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return NONE;
	}
	
	
	/**
	 * 保存并创建一条空的记录文档,如果成功处理. 返回"SUCCESS",将再创建一条空的记录文档。否则返回"INPUT",创建失败。
	 * 
	 * @return "SUCCESS" or "ERROR"
	 * @throws Exception
	 */
	public String doSaveNew(Activity act,Document doc) {
		try {
			if (doSaveDocument(act,doc).equals("viewDocument")) {
				if(act.isWithOld()){
					return doNewDocument(act, doc);
				}else{
					WebUser user = getUser();
					FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
					DocumentProcess proxy = createDocumentProcess(getApplication());
					ParamsTable params = getParams();
					String formid = params.getParameterAsString("_formid");
	
					Form form = (Form) formPross.doView(formid);
					if (form != null) {
						Document newDoc = proxy.doNewWithOutItems(form, user, params);
						setContent(newDoc);
	//					MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
	//					toFormXml(newDoc, new ArrayList<String>(), false);
					}
					return "viewDocument";
				}
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
	}
	
	
	/**
	 * 保存文档. 文档无状态并有流程时开启流程.
	 * 
	 * @return SUCCESS，ERROR
	 * @throws Exception
	 */
	private String doSaveDocumentStartFlow(Activity act,Document doc) {
		if (!doValidate(doc))
			return ERROR;
		try {
			WebUser user = this.getUser();
			ParamsTable params = getParams();
//			String formid = params.getParameterAsString("_formid");
			DocumentProcess proxy = createDocumentProcess(getApplication());
//			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
//			Form form = (Form) formProcess.doView(formid);
//			String docid = getParams().getParameterAsString("_docid");
//			Document olddoc = (Document) proxy.doView(docid);
//			Document doc = null;
			//add by peter
			if(act.getEditMode() == 1){//判断是否需要执行脚本
				IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),doc.getApplicationid());
				runner.initBSFManager(doc, params, user, null);
				StringBuffer start = new StringBuffer();
				start.append("Activity(").append(act.getId()).append(")." + act.getName()).append(".startFlowScript");
				String resultStart = (String)runner.run(start.toString(), act.getStartFlowScript());
				params.setParameter("selectFlow",resultStart);
			}
			String selectFlow = params.getParameterAsString("selectFlow");
//			if (olddoc != null) {
//				doc = form.createDocument(olddoc, params, user);
//			} else {
//				doc = form.createDocument(params, user);
//			}
//			if (docid != null && docid.length() > 0) {
//				doc.setId(docid);
//			}
//			doc.setDomainid(getDomain());
			if (selectFlow != null && selectFlow != "") {
//				BillDefiProcess flowProcss = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
//				BillDefiVO flowVO = null;
//				doc.setFlowid(selectFlow);
//				flowVO = (BillDefiVO) flowProcss.doView(selectFlow);
//				doc.setFlowVO(flowVO);
				params.setParameter("_flowid", selectFlow);
			}
			proxy.doStartFlowOrUpdate(doc, params, user);
			setContent(doc);
//			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
//			toFormXml(doc, new ArrayList<String>(), false);
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		// }
		return "viewDocument";
	}
	
	/**
	 * 保存文档. 文档无状态并有流程时开启流程.
	 * 
	 * @return SUCCESS，ERROR
	 * @throws Exception
	 */
	private String doFlow_Process(Activity act,Document doc) {
		if (!doValidate(doc))
			return ERROR;
		try {
			WebUser user = this.getUser();
			ParamsTable params = getParams();
			DocumentProcess proxy = createDocumentProcess(getApplication());
			String selectFlow = params.getParameterAsString("selectFlow");
			if (selectFlow != null && selectFlow != "") {
//				BillDefiProcess flowProcss = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
//				BillDefiVO flowVO = null;
//				doc.setFlowid(selectFlow);
//				flowVO = (BillDefiVO) flowProcss.doView(selectFlow);
//				doc.setFlowVO(flowVO);
				params.setParameter("_flowid", selectFlow);
			}
			if(doc.getState() != null){
				((DocumentProcess) proxy).doFlow(doc, params, get_currid(), get_nextids(), get_flowType(doc.getState().getFlowVO()),
						get_attitude(), user);
			}else{
				proxy.doStartFlowOrUpdate(doc, params, user);
			}
			setContent(doc);
//			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
//			toFormXml(doc, new ArrayList<String>(), false);
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		// }
		return "viewDocument";
	}
	
	public String doNewDocument() {
		try {
			String _formid = getParams().getParameterAsString("_formid");
			if (StringUtil.isBlank(_formid)) {
				this.addFieldError("SystemError", "Can't find Form");
				return ERROR;
			}
			FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			DocumentProcess proxy = (DocumentProcess) getDocumentProcess();
			WebUser user = getUser();
			Form form = (Form) formPross.doView(_formid);
			if (form != null) {
				Document newDoc = proxy.doNew(form, user, getParams());
				setContent(newDoc);
				// 放入Session中
				ServletActionContext.getRequest().setAttribute("content.id", newDoc.getId());
//				MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
//				
//				toFormXml(newDoc, new ArrayList<String>(), false);
				return SUCCESS;
			} else {
				this.addFieldError("SystemError", "Can't find Form");
				return ERROR;
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", "Can't open Form");
			e.printStackTrace();
			return ERROR;
		}
	}
	
	private DocumentProcess getDocumentProcess() {
		try {
			return createDocumentProcess(getApplication());
		} catch (CreateProcessException e) {
			Log.error(e);
		}
		return null;
	}

	private boolean toFlowHisXml() {
		try {
			ParamsTable params = getParams();
			String docid = params.getParameterAsString("_docid");
			Document doc = null;
			if (!StringUtil.isBlank(docid)) {
				doc = DocumentHelper.getDocumentById(docid, getApplication());
			}
			if (doc != null  && !doc.getIstmp()) {
				String xmlText = StateMachineHelper.toHistoryXml(doc, 4);
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				session.setAttribute("toXml", xmlText);
				return true;
			}
		} catch (Exception e) {
			LOG.warn(e);
			addFieldError("SystemError", e.getMessage());
		}
		return false;
	}

	private void toFormXml(Document doc, Collection<String> columnNames, boolean isRefresh) throws Exception {
		boolean isEdit = true;
		WebUser webUser = getUser();
		ParamsTable params = getParams();

		String formid = doc.getFormid();

		if (formid == null || formid.length() <= 0) {
			formid = params.getParameterAsString("formid");
		}

		Form form = FormHelper.get_FormById(formid);
		if (form == null) {
			throw new Exception("Form {*[does not exist or deleted]*}!");
		}
		
		if (form != null) {
			form.recalculateDocument(doc, getParams(), webUser);
		}
		
		// -------------------------文档已阅未阅功能
		if (doc.getState() != null) {
			Collection<ActorRT> actors = doc.getState().getActors();
			for (Iterator<ActorRT> iter = actors.iterator(); iter.hasNext();) {
				ActorRT actor = iter.next();
				if (webUser.getId().equals(actor.getActorid())) {
					if (!actor.getIsread()) {
						actor.setIsread(true);
						ActorRTProcess process = new ActorRTProcessBean(getApplication());
						process.doUpdate(actor);
					}
					break;
				}
			}
			//查找抄送人为当前用户
			CirculatorProcess cProcess = (CirculatorProcess) ProcessFactory.createRuntimeProcess(CirculatorProcess.class, getApplication());
			Circulator circulator = cProcess.findByCurrDoc(doc.getId(), doc.getState().getId(), false, webUser);
			if(circulator !=null){
				circulator.setRead(true);
				circulator.setReadTime(new Date());
				cProcess.doUpdate(circulator);//更新为已阅
			}
			
			// -------------------------------- 选择可执行的流程实例
			if(!StringUtil.isBlank(doc.getId())){
				FlowStateRTProcess stateProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, getApplication());
				if(stateProcess.isMultiFlowState(doc)){//有多个没完成是流程实例
					doc.setState(stateProcess.getCurrFlowStateRT(doc, webUser, null));//绑定一个可执行的文档实例
					doc.setMulitFlowState(stateProcess.isMultiFlowState(doc, webUser));//是否存在多个可执行实例
				}
			}
		}
		
		
		isEdit = StateMachineHelper.isDocEditUser(doc, webUser);
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), form.getApplicationid());
		runner.initBSFManager(doc, params, webUser, new ArrayList<ValidateMessage>());

		String docid = params.getParameterAsString("_docid");
		if (docid == null) {
			docid = "";
		}
		if (form.getIseditablescript() != null && form.getIseditablescript().trim().length() > 0) {
			StringBuffer label = new StringBuffer();
			label.append("DocumentContent.Form(").append(form.getId()).append(")." + form.getName()).append(
					".runBeforeopenScript");

			Object result = runner.run(label.toString(), form.getIseditablescript());
			if (result != null && result instanceof Boolean) {
				boolean isEdit_bs = (((Boolean) result).booleanValue());
				if (isEdit_bs != isEdit) {
					isEdit = false;
				}
			}
		}
		doc.setEditAble(isEdit);

		if (form != null) {
			String xmlText = form.toMbXML(doc, params, columnNames, webUser, new ArrayList<ValidateMessage>(),
					getEnvironment(), isRefresh);
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("toXml", "");
			session.setAttribute("toXml", xmlText);
			//System.out.println(xmlText);
		}
	}

	private static DocumentProcess createDocumentProcess(String applicationid) throws CreateProcessException {
		DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
				applicationid);

		return process;
	}

	private String _viewid;
	/**
	 * 下一个节点数组
	 */
	private String[] _nextids;

	/**
	 * 当前节点id
	 */
	private String _currid;

	private String _flowType;

	private String _attitude;

	public String get_attitude() {
		return _attitude;
	}

	public void set_attitude(String _attitude) {
		this._attitude = _attitude;
	}

	public String get_flowType(BillDefiVO flowVO) throws Exception {
		String[] nextids = get_nextids();
		String currNodeid = null;
		for (int i = 0; i < nextids.length; i++) {
			if (nextids[i] != null && !nextids[i].trim().equals("")) {
				currNodeid = nextids[i];
				break;
			}
		}
		Node nextNode = StateMachine.getCurrNode(flowVO, currNodeid);

		_flowType = FlowType.RUNNING2RUNNING_NEXT;
		if (!(nextNode instanceof ManualNode)) {// 下一个节点中是否存在suspend
			if (nextNode instanceof SuspendNode) {
				_flowType = FlowType.RUNNING2SUSPEND;
			} else if (nextNode instanceof CompleteNode) {
				_flowType = FlowType.RUNNING2COMPLETE;
			}
		}
		return _flowType;
	}

	public void set_flowType(String type) {
		_flowType = type;
	}

	public String get_currid() {
		return _currid;
	}

	public void set_currid(String _currid) {
		this._currid = _currid;
	}

	public String[] get_nextids() {
		if (_nextids != null && _nextids.length == 1) {
			String tmp = _nextids[0].endsWith(";") ? _nextids[0].substring(0, _nextids[0].length() - 1) : _nextids[0];
			String[] spl = tmp.split(";");
			_nextids = new String[spl.length];
			for (int i = 0; i < spl.length; i++) {
				_nextids[i] = spl[i];
			}
		}
		return _nextids;
	}

	public void set_nextids(String[] _nextids) {
		this._nextids = _nextids;
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

	// private Collection _columnNames;

	private String domain = null;

	/**
	 * 删除Document
	 * 
	 * @return SUCCESS OR ERROR
	 * @throws Exception
	 */
	public String doDelete(Activity act) throws Exception {
		
		if (_selects != null) {
			if(_selects.length==1 && _selects[0].indexOf(";")!=-1){
				_selects =  _selects[0].split(";");
			}
			DocumentProcess proxy = createDocumentProcess(getApplication());
			proxy.doRemove(_selects);
		}

		return "viewList";
	}

	// public Collection get_columnNames() {
	// return _columnNames;
	// }
	//
	// public void set_columnNames(Collection names) {
	// _columnNames = names;
	// }

	public String getDomain() throws Exception {
		if (!StringUtil.isBlank(domain)) return domain;
		return getUser().getDomainid();
	}

	public void setDomain(String domain) {
		this.domain = domain;
		getContent().setDomainid(domain);
	}

	public String getApplication() {
		return application;
	}

	private Map<String, List<String>> fieldErrors;

	public void addFieldError(String fieldname, String message) {
		List<String> thisFieldErrors = getFieldErrors().get(fieldname);

		if (thisFieldErrors == null) {
			thisFieldErrors = new ArrayList<String>();
			this.fieldErrors.put(fieldname, thisFieldErrors);
		}
		thisFieldErrors.add(message);
	}

	public Map<String, List<String>> getFieldErrors() {
		if (fieldErrors == null)
			fieldErrors = new HashMap<String, List<String>>();
		return fieldErrors;
	}

	/**
	 * @SuppressWarnings API不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public void setFieldErrors(Map fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	/**
	 * 校验当前用户是否可以保存文档.
	 * 根据当前Document是否有子Document并且是否可以编辑,若有子Document并且可以编辑,返回true,
	 * 此时可以保存当前Document. 并根据Document id 、 flow(流程)id 与当前用户作为参数条件来判断.
	 */
	public boolean doValidate(Document doc) {
		boolean flag = true;
		try {
			ParamsTable params = getParams();
			WebUser webUser = getUser();
//			String formid = params.getParameterAsString("_formid");
			DocumentProcess proxy = createDocumentProcess(getApplication());
//			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
//			Form form = (Form) formProcess.doView(formid);
//			String docid = params.getParameterAsString("_docid");
//			Document olddoc = (Document) proxy.doView(docid);
//			Document doc = null;
//			if (olddoc != null) {
//				doc = form.createDocument(olddoc, params, webUser);
//			} else {
//				doc = form.createDocument(params, webUser);
//			}
			if (proxy.isDocSaveUser(doc, params, webUser)) {
				Collection<ValidateMessage> errors = proxy.doValidate(doc, params, webUser);
				if (errors != null && errors.size() > 0) {
					for (Iterator<ValidateMessage> iter = errors.iterator(); iter.hasNext();) {
						ValidateMessage err = (ValidateMessage) iter.next();
						addFieldError("SystemError", err.getErrmessage());
					}
					flag = false;
				}
			} else {
				flag = false;
				addFieldError("SystemError", "{*[core.document.cannotsave]*}");
			}
//			setContent(doc);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			addFieldError("SystemError", e.getMessage());
		}
		return flag;
	}
	
	
	/**
	 * 删除列表中所有文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doDeleteFile() {
		try {
			ParamsTable params = getParams();
			String fileFullName = params.getParameterAsString("fileFullName");
			String type = params.getParameterAsString("type");
			if (!StringUtil.isBlank(fileFullName)) {
				fileFullName = URLDecoder.decode(fileFullName,"UTF-8");
				UploadProcess uploadProcess = (UploadProcess) ProcessFactory.createRuntimeProcess(UploadProcess.class, application);
				String[] fileFullNameArry = fileFullName.split(";");
				for (int i = 0; i < fileFullNameArry.length; i++) {
					if(type!=null && (type.equals("MbImageUploadToDataBaseField") || type.equals("MbAttachmentUploadToDataBaseField"))){
						uploadProcess.doRemove(fileFullNameArry[i].split("_")[0]);
					}else{
						UploadVO uploadVO = (UploadVO) uploadProcess.findByColumnName1("PATH", fileFullNameArry[i]);
						if (uploadVO != null) {
							String fileRealPath = getEnvironment().getRealPath(uploadVO.getPath());
							File file = new File(fileRealPath);
							if (file.exists()) {
								if (!file.delete()) {
									Log.warn("File(" + fileRealPath + ") delete failed");
									throw new Exception("File(" + fileRealPath + ") delete failed");
								}
							}
							uploadProcess.doRemove(uploadVO.getId());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 获取文件管理uploads下文件夹及文件
	 * @return
	 */
	public String doFileList(){
		try{
			ParamsTable params = getParams();
			String filePattern = params.getParameterAsString("filePattern");
			String rootpath = params.getParameterAsString("fileCatalog");
			String path = params.getParameterAsString("path");
			
			if (filePattern.equals(FileManagerField.DEFINITION)) {
				rootpath = "uploads" + rootpath;
			}else{
				rootpath = "uploads";
			}
			rootpath =  Environment.getInstance().getRealPath(rootpath);
			rootpath = rootpath.replace("/", "\\");
			if(StringUtil.isBlank(path)){
				path = rootpath;
			}
			//获得角色列表
			String rolelist = getUser().getRolelist(getApplication());
			String xmlText = new FileOperate().toMbXMLText(rootpath,path,rolelist,getApplication());
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("toXml", xmlText);
		}catch(Exception e){
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 获取所有文件夹列表
	 * @return
	 */
	public String doFolderList(){
		try{
			ParamsTable params = getParams();
			String filePattern = params.getParameterAsString("filePattern");
			String rootpath = params.getParameterAsString("fileCatalog");
			
			if (filePattern.equals(FileManagerField.DEFINITION)) {
				rootpath = "uploads" + rootpath;
			}else{
				rootpath = "uploads";
			}
			rootpath =  Environment.getInstance().getRealPath(rootpath);
			rootpath = rootpath.replace("/", "\\");
			String xmlText = new FileOperate().toMbFolderListXMLText(rootpath);
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("toXml", xmlText);
		}catch(Exception e){
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 执行列操作按钮
	 * @throws Exception 
	 */
	public String doColumnAction(){
		try{
			ParamsTable params = getParams();
			String type = params.getParameterAsString("_buttonType");
			if(type.equals(MobileConstant.BUTTON_TYPE_JUMP)){
				return doViewJumpFrom();
			}else if(type.equals(MobileConstant.BUTTON_TYPE_SCRIPT)){
				return runScript();
			}else if(type.equals(MobileConstant.BUTTON_TYPE_DOFLOW)){
				return doFlowState();
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			LOG.warn(e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doViewJumpFrom(){
		try{
			doNewDocument(new Activity(),new Document());
			if(getContent() instanceof Document){
				Document doc = (Document) getContent();
				if(doc != null){
					MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
					toFormXml(doc, new ArrayList<String>(), false);
				}
			}
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			LOG.warn(e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String runScript(){
		String actionScript = "";
		String colName = "";
		ParamsTable param = getParams();
		String docid = param.getParameterAsString("_docid");
		String columnId = param.getParameterAsString("_columnId");
		String viewId= param.getParameterAsString("_viewid");
		ParamsTable params = this.getParams();
		try{
			DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, application);
			Document doc = (Document) dp.doView(docid);
			ViewProcess vp = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			View view = (View) vp.doView(viewId);
			
			Iterator<Column> it = view.getColumns().iterator();
			while(it.hasNext()){
				Column col = it.next();
				if(Column.BUTTON_TYPE_SCRIPT.equals(col.getButtonType()) && col.getId().equals(columnId)){
					actionScript = col.getActionScript();
					colName = col.getName();
				}
			}
			
			if(!StringUtil.isBlank(actionScript)){
				IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), application); 
				runner.initBSFManager(doc, params, getUser(), new java.util.ArrayList<ValidateMessage>());
				StringBuffer label = new StringBuffer();
				label.append("Button Action:[viewName:").append(view.getName()).append("][colName:").append(colName).append("]").append(columnId).append("ActionScript");
				runner.run(label.toString(), actionScript);
			}
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			LOG.warn(e);
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String doFlowState(){
		ParamsTable param = getParams();
		String docid = param.getParameterAsString("_docid");
		String approveLimit = param.getParameterAsString("_approveLimit");
		
		Collection<String> allowedList = new ArrayList<String>();
		if(!StringUtil.isBlank(approveLimit)) {
			allowedList = Arrays.asList(approveLimit.split(","));
		}
		try{
			DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, application);
			FlowStateRTProcess flowProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, application);
			Document doc = (Document) dp.doView(docid);
			FlowStateRT instance = null;
			if (doc != null) {
				instance = doc.getState();
	
				if (instance != null) {
					try {
						IRunner runner = JavaScriptFactory.getInstance(params
								.getSessionid(), instance.getFlowVO()
								.getApplicationid());
						runner.initBSFManager(doc, params, getUser(),
								new ArrayList<ValidateMessage>());
	
						String currNodeId = "";
						// FlowStateRT rt = doc.getState();
						if (instance != null) {
							NodeRT node = instance.getNodeRT(getUser());
							if (node != null)
								currNodeId = node.getNodeid();
						}
							
						Collection<Node> nextNodes = StateMachine.getNextAllowedNode(
								allowedList, instance.getFlowVO(), currNodeId);							
						
						String flowOption="";
						String[] nextNodeIds=new String[nextNodes.size()];						
						int index=0;
						for (Iterator<Node> iter=nextNodes.iterator(); iter.hasNext();index++) {
							
						Node nextNode=iter.next();	
						
						nextNodeIds[index]=nextNode.id;	
						
						if(nextNode != null){
							doc.setLastFlowOperation(FlowType.getActionCode(nextNode));											

						}	
						flowOption=FlowType.getActionCode(nextNode);	
						}
						flowProcess.doApprove(params, instance, currNodeId,nextNodeIds,flowOption, "", getUser());						
						
						return "displayView";
					} catch (Exception e) {
						LOG.info("Approve 1 document(s) with 1 fail(s)");
						return "displayView";
					}
				}
			}
		}catch(Exception e1){
			e1.printStackTrace();
			return ERROR;
		}
		return "displayView";
	}
	
	/**
	 * 跳转表单
	 * @return
	 * @throws Exception 
	 */
	public String doJumpDocument(Activity act) throws Exception{
		String torgetList = act.getTargetList();
		String olist = torgetList.split(";")[0];
		String formid = StringUtil.split(olist, "|")[0];
		ParamsTable params = getParams();
		params.setParameter("_formid", formid);
		params.setParameter("_isJump", 1);
		act.setOnActionForm(null);
		return doNewDocument(act,new Document());
		
	}
	
	/**
	 * 文件操作
	 * @return
	 */
	public String doFileOperate(){
		try{
			ParamsTable params = getParams();
			String operateType = params.getParameterAsString("operateType");
			//新建文件夹
			if(operateType.equals("NewFolder")){
				String newPath = params.getParameterAsString("newPath");
				new FileOperate().newFolder(newPath);
			}else if(operateType.equals("ReNameFolder")){//重命名
				String newPath = params.getParameterAsString("newPath");
				String oldPath = params.getParameterAsString("oldPath");
				new FileOperate().reNameFolderOrFile(oldPath, newPath);
			}else if(operateType.equals("DeleteFolder")){//删除文件夹
				String newPath = params.getParameterAsString("newPath");
				new FileOperate().delFolder(newPath);
			}else if(operateType.equals("DeleteFile")){//删除文件
				String newPath = params.getParameterAsString("newPath");
				if(!StringUtil.isBlank(newPath)){
					String[] newPaths = newPath.split(";");
					new FileOperate().delMoreFile(newPaths);
				}
			}else if(operateType.equals("CopyFile")){//复制文件
				String selectPaths = params.getParameterAsString("selectPaths");
				String newPath = params.getParameterAsString("newPath");
				if(!StringUtil.isBlank(selectPaths) && !StringUtil.isBlank(newPath)){
					String[] selectPaths1 = selectPaths.split(";");
					new FileOperate().copyMoreFile(selectPaths1,newPath);
				}
			}else if(operateType.equals("RemoveFile")){//移动文件
				String selectPaths = params.getParameterAsString("selectPaths");
				String newPath = params.getParameterAsString("newPath");
				if(!StringUtil.isBlank(selectPaths) && !StringUtil.isBlank(newPath)){
					String[] selectPaths1 = selectPaths.split(";");
					new FileOperate().moveMoreFile(selectPaths1,newPath);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 流程启动获取数据
	 * @author kharry
	 * @return
	 */
	public String doStartWorkFlow(){
		try{
			ParamsTable params = getParams();
			String formid = params.getParameterAsString("_formid");
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = (Form) formProcess.doView(formid);
			String docid = params.getParameterAsString("_docid");
			if(form!=null ){
				String module = form.getModule().getId();
				ActivityUtil actUtil = new ActivityUtil();
				StateMachineUtil stateUtil = new StateMachineUtil();
				Map<String, String> flowMap = actUtil.getFlowByModule(module);
				Iterator<Entry<String, String>> iter = flowMap.entrySet().iterator();
				StringBuffer flowSb = new StringBuffer();
				flowSb.append("<"+MobileConstant.TAG_SELECTFIELD).append(" ").append(MobileConstant.ATT_NAME).append("='selectFlow'>");
				StringBuffer nodeSb = new StringBuffer();
				while (iter != null && iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					flowSb.append("<").append(MobileConstant.TAG_OPTION)
							.append(" ").append(MobileConstant.ATT_VALUE)
							.append("='").append(entry.getKey()).append("'>");
					flowSb.append(entry.getValue());
					flowSb.append("</").append(MobileConstant.TAG_OPTION).append(">");
					if(!StringUtil.isBlank(entry.getKey())){
						LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
						Collection<Node> nodes = stateUtil.getFirstNodeList(docid,entry.getKey());
						if (nodes != null) {
							for (Iterator<Node> iters = nodes.iterator(); iters.hasNext();) {
								ManualNode startNode = (ManualNode) iters.next();
								map.put(startNode.id, startNode.name);
							}
						}
						nodeSb.append("<").append(MobileConstant.TAG_RADIOFIELD).append(" ").append(MobileConstant.ATT_NAME).append("='").append(entry.getKey()).append("'>");
						for (Iterator<Entry<String, String>> iter2 = map.entrySet().iterator(); iter2.hasNext();) {
							Map.Entry<String, String> entry2 = iter2.next();
							nodeSb.append("<").append(MobileConstant.TAG_OPTION).append(" ").append(MobileConstant.ATT_VALUE).append("='").append(entry2.getKey()).append("'>");
							nodeSb.append(entry2.getValue());
							nodeSb.append("</").append(MobileConstant.TAG_OPTION).append(">");
						}
						nodeSb.append("</").append(MobileConstant.TAG_RADIOFIELD).append(">");
					}
				}
				flowSb.append("</"+MobileConstant.TAG_SELECTFIELD).append(">");
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				session.setAttribute("toXml", flowSb.toString() + nodeSb.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doJumpView() throws Exception{
		try{
			ParamsTable params = getParams();
			String _viewid=(String) params.getParameter("_viewid");
			String _activityid = (String) params.getParameter("_activityid");
			ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			ActivityParent actParent = (ActivityParent) viewProcess.doView(_viewid);
			Activity act = actParent.findActivity(_activityid);
			String viewid = act.getOnActionView();
			view = (View) viewProcess.doView(viewid);
			Document searchDocument = null;
			if (view.getSearchForm() != null) {
				try {
					searchDocument =  view.getSearchForm().createDocument(getParams(), getUser());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				searchDocument = new Document();
			}

			setContent(view);
			// 分页参数
			int page = 1;
			if (!StringUtil.isBlank(_currpage)) {
				try {
					page = Integer.parseInt(_currpage);
				} catch (Exception e) {
					_currpage = "1";
					LOG.warn(e);
				}
			} else {
				_currpage = "1";
			}
			ViewType viewType = view.getViewTypeImpl();
			String _pagelines = view.getPagelines();
			int lines = 0;
			if(view.isPagination()){
				lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
			}else{
				lines = Integer.MAX_VALUE;
			}HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			MbViewHelper helper = new MbViewHelper(getApplication());
			DataPackage<Document> datas = viewType.getViewDatasPage(params, page, lines, getUser(), searchDocument);
			if (datas == null) datas = new DataPackage<Document>();
			String xml = helper.toViewListXml(false, datas,searchDocument,view,request,params,getUser(),new Document(),get_currpage(),lines);
			session.setAttribute("toXml", xml);
			return SUCCESS;
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
	}
	
	
	/**
	 * 获取设置环境
	 * 
	 * @return Environment
	 */
	public Environment getEnvironment() {
		String ctxPath = ServletActionContext.getRequest().getContextPath();
		Environment evt = Environment.getInstance();
		evt.setContextPath(ctxPath);
		return evt;
	}

	public String get_currpage() {
		return _currpage;
	}

	public void set_currpage(String _currpage) {
		this._currpage = _currpage;
	}
	
	
}
