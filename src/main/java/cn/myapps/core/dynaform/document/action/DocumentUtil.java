package cn.myapps.core.dynaform.document.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.macro.runner.JsMessage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.property.DefaultProperty;

public class DocumentUtil {

	/**
	 * 执行新建
	 * 
	 * @param activityId
	 *            操作ID
	 * @param parameters
	 *            参数
	 * @param request
	 *            请求
	 * @return
	 */
	public String doNew(String activityId, Map<String, Object> parameters, HttpServletRequest request) {
		try {
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);

			ParamsTable params = ParamsTable.convertHTTP(request);
			params.putAll(parameters);

			View view = getView(params);

			Activity act = view.findActivity(activityId);
			IRunner runner = getRunner(view, params, webUser);

			// 1.Execute before action script
			doBeforeScript(act, runner);
			
			
			// 2.Create Document
			String formId = view.getRelatedForm();
			Form form = (Form) formProcess.doView(formId);
			Document doc = form.createDocument(params, webUser);


			// 3. Save Document, get create script
			// dp.doCreate(doc);
			String refreshScript = view.getRowCreateScript(doc, runner, webUser);

			// 4.Execute after action script
			doAfterScript(act, runner);
			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, webUser);
			return refreshScript;
		} catch (Exception e) {
			e.printStackTrace();
			return "showError('" + e.getMessage() + "');";
		}
	}

	/**
	 * 运行操作前脚本
	 * 
	 * @param act
	 *            操作
	 * @param runner
	 *            脚本执行器
	 * @throws Exception
	 */
	public void doBeforeScript(Activity act, IRunner runner) throws Exception {
		if (act == null) {
			return;
		}

		StringBuffer label = new StringBuffer();
		label.append("Activity Action(").append(act.getId()).append(")." + act.getName()).append("beforeActionScript");
		Object result = runner.run(label.toString(), act.getBeforeActionScript());
		if (result instanceof String && !StringUtil.isBlank((String) result)) {
			throw new OBPMValidateException((String) result);
		}
	}

	/**
	 * 运行操作后脚本
	 * 
	 * @param act
	 *            操作
	 * @param runner
	 *            脚本执行器
	 * @throws Exception
	 */
	public void doAfterScript(Activity act, IRunner runner) throws Exception {
		if (act == null) {
			return;
		}

		StringBuffer label = new StringBuffer();
		label.append("Activity Action(").append(act.getId()).append(")." + act.getName()).append("afterActionScript");
		Object result = runner.run(label.toString(), act.getAfterActionScript());
		if (result instanceof String && !StringUtil.isBlank((String) result)) {
			result = new JsMessage(JsMessage.TYPE_ALERT, (String)result);
		}
		if(result instanceof JsMessage){
			throw new OBPMValidateException("{\"funName\":\"" + ((JsMessage)result).getTypeName() + "\",\"content\":\"" + ((JsMessage)result).getContent() + "\"}");
		}
	}

	/**
	 * 执行删除
	 * 
	 * @param activityId
	 * @param selects
	 * @param parameters
	 * @param request
	 * @return
	 */
	public String doRemove(String activityId, String[] selects, Map<String, Object> parameters,
			HttpServletRequest request) {
		try {
			WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);

			ParamsTable params = ParamsTable.convertHTTP(request);
			params.putAll(parameters);
			params.setParameter("_selects", selects);

			View view = getView(params);
			DocumentProcess dp =(DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,view.getApplicationid());

			Activity act = view.findActivity(activityId);
			IRunner runner = getRunner(view, params, webUser);

//			doBeforeScript(act, runner);在js用ajax执行动作前脚本

			dp.doRemove(selects);

			doAfterScript(act, runner);
		} catch (Exception e) {
			//e.printStackTrace();
			return e.getMessage();
		}
		return "";
	}

	/**
	 * 获取脚本执行器
	 * 
	 * @param view
	 * @param params
	 * @param webUser
	 * @return
	 * @throws Exception
	 */
	private IRunner getRunner(View view, ParamsTable params, WebUser webUser) throws Exception {
		Form sForm = view.getSearchForm();
		Document sfDoc = new Document();
		if (sForm != null) {
			sForm.createDocument(params, webUser);
		}

		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
		runner.initBSFManager(sfDoc, params, webUser, new ArrayList<ValidateMessage>());

		return runner;
	}

	private View getView(ParamsTable params) throws Exception {
		ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		View view = (View) viewProcess.doView(params.getParameterAsString("_viewid"));

		return view;
	}

	public String doSingleRemove(String id, String application) {
		try {
			DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,application);
			Document doc = (Document) dp.doView(id);
			if (doc != null) {
				dp.doRemove(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "showError('" + e.getMessage() + "');";
		}
		return "";

	}

	public String doRefresh(String dataJSON, Map<String, Object> parameters, HttpServletRequest request) {
		try {
			WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);

			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);

			ParamsTable params = ParamsTable.convertHTTP(request);
			params.putAll(parameters);
			if (dataJSON != null) {
				params.putAll(JsonUtil.toMap(dataJSON));
			}

			View view = getView(params);
			DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,view.getApplicationid());

			String formId = view.getRelatedForm();
			Form form = (Form) formProcess.doView(formId);
			String id = params.getParameterAsString("id"); // Document id
			// 从Session中获取Document
			Document doc = (Document) MemoryCacheUtil.getFromPrivateSpace(id, webUser);

			// 从数据库中获取Document
			if (doc == null) {
				doc = (Document) dp.doView(id);
			}
			form.addItems(doc, params);
			form.recalculateDocument(doc, params, false, webUser);

			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
			runner.initBSFManager(doc, params, webUser, new ArrayList<ValidateMessage>());

			// 1.Generate row script
			String refreshScript = view.getRowRefreshScript(doc, runner, webUser);

			return refreshScript;
		} catch (Exception e) {
			e.printStackTrace();
			return "obpm:" + e.getMessage();
		}
	}
	
	/**
	 * 前台网格视图,点击行编辑时,重计算本文档
	 * @param docid
	 * @param parameters
	 * @param request
	 * @return
	 */
	public String doRefreshRow4subGridView(String docid, Map<String, Object> parameters, HttpServletRequest request){
		String rowScript = "";
		try {
			ParamsTable params = ParamsTable.convertHTTP(request);
			params.putAll(parameters);
			ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			View view = (View) viewProcess.doView(params.getParameterAsString("_viewid"));
			String applicationid = view.getApplicationid();
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationid);
			Document doc = (Document) process.doView(docid);
			Form form = doc.getForm();
			WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
			form.recalculateDocument(doc, params, webUser);
			IRunner runner = getRunner(view, params, webUser);
			rowScript = view.getRowCreateScript(doc, runner, webUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowScript;
	}

	/**
	 * @SuppressWarnings JsonUtil.toCollection返回的对象集类型不定
	 * @param activityId
	 * @param datasJSON
	 * @param parameters
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String doSave(String activityId, String datasJSON, Map<String, Object> parameters, HttpServletRequest request) {
		try {
			WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);

			ParamsTable params = ParamsTable.convertHTTP(request);
			params.putAll(parameters);

			View view = getView(params);
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,view.getApplicationid());
			String formId = view.getRelatedForm();
			Form form = (Form) formProcess.doView(formId);
			Activity act = view.findActivity(activityId);

			Collection<Object> datas = JsonUtil.toCollection(datasJSON);
			try {
				process.beginTransaction();
				for (Iterator<Object> iterator = datas.iterator(); iterator.hasNext();) {
					Map<String, Object> data = (Map<String, Object>) iterator.next();
					params.putAll(data);
					String id = params.getParameterAsString("id");

					// PO, VO整合
					Document doc = (Document) MemoryCacheUtil.getFromPrivateSpace(id, webUser);
					// 从数据库中获取Document
					if (doc == null) {
						doc = (Document) process.doView(id);
					}
					if (doc != null) {
						doc.setLastmodifier(webUser.getId());
						doc = form.createDocument(doc, params, webUser);
					} else {
						doc = form.createDocument(params, webUser);
					}

					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
					runner.initBSFManager(doc, params, webUser, new ArrayList<ValidateMessage>());

					doBeforeScript(act, runner);

					// 对文档进行校验
					Collection<ValidateMessage> errors = process.doValidate(doc, params, webUser);
					StringBuffer error = new StringBuffer();
					if (errors != null && errors.size() > 0) {
						for (Iterator<ValidateMessage> iter = errors.iterator(); iter.hasNext();) {
							ValidateMessage err = iter.next();
							error.append(err.getErrmessage());
							error.append(";");
						}
						error.deleteCharAt(error.lastIndexOf(";"));
						throw new OBPMValidateException(error.toString());
					}
					
					process.doStartFlowOrUpdate(doc, params, webUser);

					doAfterScript(act, runner);

					// MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, webUser);
					// 放置到缓存中
				}
				process.commitTransaction();
			} catch (Exception e) {
				process.rollbackTransaction();
				throw e;
			}
			
			return "";
		} catch (OBPMValidateException e) {
			e.printStackTrace();
			return "obpm:" + e.getValidateMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "obpm:" + e.getMessage();
		}
	}

	public Map<String, String> getFileList(int index, HttpServletRequest request) throws Exception {
		WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		String[] path = { "SECSIGN_PATH", "REDHEAD_DOCPATH", "TEMPLATE_DOCPATH" };
		String[] defaultOption = { "请选择服务器印章", "请选择模板套红", "请选择模板打开" };
		String dir = DefaultProperty.getProperty(path[index]);
		String domainDir = DefaultProperty.getProperty(path[index]) + webUser.getDomainid() + "/";
		Environment evt = Environment.getInstance();
		evt.setContextPath(request.getContextPath());
		String realPath = evt.getRealPath(dir);
		String realDomainPath = evt.getRealPath(domainDir);
		File files = new File(realPath);
		File domainFiles = new File(realDomainPath);
		if (!files.exists()) {
			if (!files.mkdir())
				throw new OBPMValidateException("Folder create failure");
		}
		if (!domainFiles.exists()) {
			if (!domainFiles.mkdir())
				throw new OBPMValidateException("Folder create failure");
		}
		Map<String, String> options = new LinkedHashMap<String, String>();
		options.put("", defaultOption[index]);
		for (File file:files.listFiles()) {
			if(file.isDirectory())
				continue;
			String fileName = file.getName();
			String filePath = dir + fileName;
			options.put(filePath, fileName);
		}
		for (File file:domainFiles.listFiles()) {
			if(file.isDirectory())
				continue;
			String fileName = file.getName();
			String filePath = domainDir + fileName;
			options.put(filePath, fileName);
		}
		return options;
	}

	public ArrayList<ArrayList<String>> getSecFileList(HttpServletRequest request) throws Exception {
		String dir = DefaultProperty.getProperty("SECSIGN_PATH");
		Environment evt = Environment.getInstance();
		evt.setContextPath(request.getContextPath());
		String realPath = evt.getRealPath(dir);
		File file = new File(realPath);
		if (!file.exists()) {
			if (!file.mkdir())
				throw new OBPMValidateException("Folder create failure");
		}
		File[] fileList = file.listFiles();
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		if (fileList.length > 0) {
			for (int i = 0; i < fileList.length; i++) {
				ArrayList<String> tr = new ArrayList<String>();
				tr.add(fileList[i].getName());
				// tr.add(new
				// Date(fileList[i].lastModified()).toLocaleString());
				tr.add(new SimpleDateFormat().format(new Date(fileList[i].lastModified())));
				tr.add(String.valueOf(fileList[i].length()));
				table.add(tr);
			}
		}
		return table;
	}
	
	public String compareAndUpdateItemWord(String filename, String fieldname, String formname, String docid, String applicationid)throws Exception{
		DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationid);
		return dp.compareAndUpdateItemWord(filename, fieldname, formname, docid);
	}
	
	/**
	 * 获取信息反馈内容
	 * @param docid
	 * @param fieldname
	 * @param application
	 * @return
	 * @throws Exception
	 */
	public String getInformationfeedbackInfo(String docid, String fieldname, String application) throws Exception {
		try {
			DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,application);
			Document doc = (Document) dp.doView(docid);
			if (doc!=null) {
				String info = doc.getItemValueAsString(fieldname);
				return null==info? "" : info;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "showError('" + e.getMessage() + "');";
		}
		return "";	
	}
	
	/**
	 * 更新信息反馈信息
	 * @param docid
	 * @param fieldname
	 * @param filedValue
	 * @param application
	 * @return
	 * @throws Exception
	 */
	public String doUpdateInformationfeedbackInfo(String docid, String fieldname, String filedValue, String application) throws Exception {
		try {
			DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,application);
			Document doc = (Document) dp.doView(docid);
			if (doc!=null) {
				doc.addTextItem(fieldname, filedValue);
				dp.doUpdate(doc, true, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "showError('" + e.getMessage() + "');";
		}
		return "";
	}

}
