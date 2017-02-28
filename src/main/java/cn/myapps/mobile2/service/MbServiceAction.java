package cn.myapps.mobile2.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.myapps.base.OBPMValidateException;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.macro.runner.JsMessage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mobile2.document.MbDocumentXMLBuilder;
import cn.myapps.mobile2.view.MbViewXMLBuilder;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;

/**
 * @author nicholas
 */
public class MbServiceAction extends ActionSupport {

	private static final long serialVersionUID = -3253948249606151825L;

	private static final Logger LOG = Logger.getLogger(MbServiceAction.class);
	
	/**
	 * 输出值
	 */
	private String result;
	
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
	 *                              <action name="reply"
	 * @return result.
	 * @throws Exception 
	 * @throws Exception
	 */
	public String doAction() throws Exception {
		try {
			ParamsTable params = getParams();
			Activity act = null;
			WebUser user = getUser();
			String activityid = params.getParameterAsString("_activityid");
			ActivityParent activityParent = MbServiceHelper.dogetParentById(params);
			act = activityParent.findActivity(activityid);
			if(act != null){
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
						|| act.getType() == ActivityType.DOCUMENT_UPDATE) {
					if(!doValidate()) {
						if(this.fieldErrors.size() >0){
							StringBuffer xml = new StringBuffer();
							xml.append("[ERROR]*");
				            xml.append(this.fieldErrors.get("SystemError"));
				            this.fieldErrors = new HashMap<String, List<String>>();
				            this.result = xml.toString();
				            return SUCCESS;
						}
					}
				}
				ServletActionContext.getRequest().setAttribute("ACTIVITY_INSTNACE", act);
				ValueObject obj = act.mbExecte(user, params);
				String result = "";
				if(obj instanceof Document){
					result = MbDocumentXMLBuilder.toMobileXML((Document) obj, user, params);
				}else if(obj instanceof View){
					result = MbViewXMLBuilder.toMobileXML((View)obj, user, params);
				}else if(obj == null){
					if(act.getType() == ActivityType.JUMP_TO){
						result = MbServiceHelper.doDispathUrl(act, params, user);
					}
				}
				if(!StringUtil.isBlank(result)){
					this.result = result;
				}
			}else{
				String actType = params.getParameterAsString("_activityType");
				if("retracement".equals(actType)){
					String result = MbServiceHelper.doRetracement(user, params);
					this.result = result;
				}else if("termination".equals(actType)){
					String result = MbServiceHelper.doTerminateFlow(user, params);
					this.result = result;
				}else if("handup".equals(actType)){
					String result = MbServiceHelper.doFlowHandup(user, params);
					this.result = result;
				}else if("recover".equals(actType)){
					String result = MbServiceHelper.doFlowRecover(user, params);
					this.result = result;
				}
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
            if (e instanceof OBPMValidateException) {
                xml.append("{*[core.document.cannotsave]*}");
            } else {
                xml.append(e.getMessage());
            }
            this.result = xml.toString();
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String doBefore() throws Exception{
		try{
			ParamsTable params = getParams();
			Activity act = null;
			System.out.print("ios run after iscript!");
			String activityid = params.getParameterAsString("_activityid");
			if (!StringUtil.isBlank(activityid) && !activityid.equals("null")) {
				ActivityParent activityParent = MbServiceHelper.dogetParentById(params);
				act = activityParent.findActivity(activityid);
				Document doc = MbServiceHelper.getDocument(act, params, getUser());
				if(doc == null){
					return SUCCESS;
				}
				if ((act.getBeforeActionScript()) != null && (act.getBeforeActionScript()).trim().length() > 0) {
					Object result = act.runBeforeActionScript(doc, params, getUser());
					if (result instanceof JsMessage) {
//						if(((JsMessage)result).getType() == JsMessage.TYPE_ALERT){
//							return SUCCESS;
//						}
						this.addFieldError("SystemError", ((JsMessage) result).getContent());
						StringBuffer xml = new StringBuffer();
						xml.append("[ERROR]*");
						xml.append(((JsMessage) result).getContent());
						if(!StringUtil.isBlank(xml.toString())){
							this.result = xml.toString();
						}
					} else if (result instanceof String && ((String) result).trim().length() > 0) {
						this.addFieldError("SystemError", ((String) result));
						StringBuffer xml = new StringBuffer();
						xml.append("[ERROR]*");
						xml.append(((String) result));
						if(!StringUtil.isBlank(xml.toString())){
							this.result = xml.toString();
						}
					}
				}
				
			}
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append( e.getMessage());
			if(!StringUtil.isBlank(xml.toString())){
				this.result = xml.toString();
			}
		}catch (Throwable e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append( e.getMessage());
			if(!StringUtil.isBlank(xml.toString())){
				this.result = xml.toString();
			}
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
		
	}
	
	public String doAfter() throws Exception{
		try{
			ParamsTable params = getParams();
			Activity act = null;
			System.out.print("ios run before iscript!");
			String activityid = params.getParameterAsString("_activityid");
			if (!StringUtil.isBlank(activityid) && !activityid.equals("null")) {
				ActivityParent activityParent = MbServiceHelper.dogetParentById(params);
				act = activityParent.findActivity(activityid);
				Document doc = MbServiceHelper.getDocument(act, params, getUser());
				if ((act.getAfterActionScript()) != null && (act.getAfterActionScript()).trim().length() > 0) {
					Object result = act.runAfterActionScript(doc, params, getUser());
					if (result instanceof JsMessage) {
//						if(((JsMessage)result).getType() == JsMessage.TYPE_ALERT){
//							this.result =  MbJsMessageXMLBuilder.toMobileXML((JsMessage)result);
//						}else{
							this.addFieldError("SystemError", ((JsMessage) result).getContent());
							StringBuffer xml = new StringBuffer();
							xml.append("[ERROR]*");
							xml.append(((JsMessage) result).getContent());
							if(!StringUtil.isBlank(xml.toString())){
								this.result = xml.toString();
							}
//						}
					} else if (result instanceof String && ((String) result).trim().length() > 0) {
						this.addFieldError("SystemError", ((String) result));
						StringBuffer xml = new StringBuffer();
						xml.append("[ERROR]*");
						xml.append(((String) result));
						if(!StringUtil.isBlank(xml.toString())){
							this.result = xml.toString();
						}
					}
				}
				
			}
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			if(!StringUtil.isBlank(xml.toString())){
				this.result = xml.toString();
			}
			LOG.warn(e);
		}catch (Throwable e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			if(!StringUtil.isBlank(xml.toString())){
				this.result = xml.toString();
			}
			LOG.warn(e);
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}

	private static DocumentProcess createDocumentProcess(String applicationid) throws CreateProcessException {
		DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
				applicationid);

		return process;
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
	
	public String doFlowHistory() throws Exception {
		try{
			WebUser user = getUser();
			ParamsTable params = getParams();
			String result = MbFlowHistoryXMLBuilder.toHistoryXML(user, params);
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
			LOG.warn(e);
		}
		return SUCCESS;
	}
	
	@Deprecated
	public String doShowFlowHis() throws Exception {
		try{
			WebUser user = getUser();
			ParamsTable params = getParams();
			String result = MbFlowHistoryXMLBuilder.toMobileXML(user, params);
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
			LOG.warn(e);
		}
		return SUCCESS;
	}
	
	/**
	 * 视图选择框确认脚本
	 * @return
	 */
	public String doViewDialogConfirm(){
		try{
			ParamsTable params = getParams();
			WebUser user = getUser();
			this.result = MbServiceHelper.ViewDialogConfirmRunScript(params, user);
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			if(!StringUtil.isBlank(xml.toString())){
				this.result = xml.toString();
			}
			LOG.warn(e);
		}
		return SUCCESS;
	}


	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
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
	 * @throws Exception 
	 */
	public boolean doValidate() throws Exception {
		boolean flag = true;
		try {
			ParamsTable params = getParams();
			WebUser webUser = getUser();
			String formid = params.getParameterAsString("_formid");
			String application = params.getParameterAsString("_application");
			DocumentProcess proxy = createDocumentProcess(application);
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = (Form) formProcess.doView(formid);
			String docid = params.getParameterAsString("_docid");
            //重新构建document的时候，需要从缓存中获取docid来构建
			Document doc = null;
            Document olddoc = null;
            if (olddoc == null) {
                olddoc = (Document) proxy.doView(docid);
            }
            if(olddoc == null){
                olddoc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, webUser);
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
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
			LOG.warn(e);
		}
		return flag;
	}
	
	/**
	 * 流程启动获取数据
	 * @author kharry
	 * @return
	 */
	public String doStartWorkFlow(){
		try{
			ParamsTable params = getParams();
			
			String result = MbStartWorkFlowXMLBuilder.toMobileXML(new Document(), getUser(), params);
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		}catch(Exception e){
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
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
	
	/**
	 * Get the Parameters table
	 * 
	 * @return ParamsTable
	 */
	public ParamsTable getParams() {
		ParamsTable params = null;
		if (params == null) {
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
			params.setSessionid(ServletActionContext.getRequest().getSession().getId());

			if (params.getParameter("_pagelines") == null)
				params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
		}

		return params;
	}
	
	public WebUser getUser() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user == null) {
			throw new Exception("[*TIMEOUT*]{*[page.timeout]*}");
		}
		return user;
	}

	public String getResult() {
		return result;
	}
}