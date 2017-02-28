package cn.myapps.core.dynaform.form.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseHelper;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.form.ejb.AbstractUploadField;
import cn.myapps.core.dynaform.form.ejb.AttachmentUploadField;
import cn.myapps.core.dynaform.form.ejb.AttachmentUploadToDataBaseField;
import cn.myapps.core.dynaform.form.ejb.BaseFormProcess;
import cn.myapps.core.dynaform.form.ejb.CalctextField;
import cn.myapps.core.dynaform.form.ejb.CheckboxField;
import cn.myapps.core.dynaform.form.ejb.DateField;
import cn.myapps.core.dynaform.form.ejb.DepartmentField;
import cn.myapps.core.dynaform.form.ejb.FileManagerField;
import cn.myapps.core.dynaform.form.ejb.FlowHistoryField;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.HTMLEditorField;
import cn.myapps.core.dynaform.form.ejb.HandwritingField;
import cn.myapps.core.dynaform.form.ejb.ImageUploadField;
import cn.myapps.core.dynaform.form.ejb.ImageUploadToDataBaseField;
import cn.myapps.core.dynaform.form.ejb.IncludeField;
import cn.myapps.core.dynaform.form.ejb.InputField;
import cn.myapps.core.dynaform.form.ejb.MapField;
import cn.myapps.core.dynaform.form.ejb.OnLineTakePhotoField;
import cn.myapps.core.dynaform.form.ejb.QRCodeField;
import cn.myapps.core.dynaform.form.ejb.Question;
import cn.myapps.core.dynaform.form.ejb.RadioField;
import cn.myapps.core.dynaform.form.ejb.ReminderField;
import cn.myapps.core.dynaform.form.ejb.SelectAboutField;
import cn.myapps.core.dynaform.form.ejb.SelectField;
import cn.myapps.core.dynaform.form.ejb.SuggestField;
import cn.myapps.core.dynaform.form.ejb.SurveyField;
import cn.myapps.core.dynaform.form.ejb.TabField;
import cn.myapps.core.dynaform.form.ejb.TextareaField;
import cn.myapps.core.dynaform.form.ejb.TreeDepartmentField;
import cn.myapps.core.dynaform.form.ejb.UserField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.form.ejb.ViewDialogField;
import cn.myapps.core.dynaform.form.ejb.WordField;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.table.model.Column;
import cn.myapps.core.table.model.Table;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.user.ejb.UserDefinedProcess;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.km.util.FileUtils;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;
import cn.myapps.util.web.DWRHtmlUtils;

public class FormHelper extends BaseHelper<Form> {
	private static final Logger log = Logger.getLogger(FormHelper.class);

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @see cn.myapps.base.action.BaseHelper#BaseHelper(BaseProcess)
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public FormHelper() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(FormProcess.class));
	}

	/**
	 * @SuppressWarnings 这里不能使用泛型
	 * @see cn.myapps.base.action.BaseHelper#BaseHelper(BaseProcess)
	 * @param process
	 */
	@SuppressWarnings("unchecked")
	public FormHelper(BaseFormProcess process) {
		super(process);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟侥ｏ拷锟接︼拷帽锟绞讹拷锟窖�锟斤拷锟斤拷锟斤拷应锟�锟斤拷锟斤拷.
	 * 
	 * @param application
	 *            应锟矫憋拷识
	 * @return 锟�锟斤拷锟斤拷
	 * @throws Exception
	 */
	public Map<String, String> get_formList(String application) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		try {
			Collection<Form> forms = fp.getFormsByModule(this.getModuleid(), application);
			for (Iterator<Form> ite = forms.iterator(); ite.hasNext();) {
				Form form = (Form) ite.next();
				map.put(form.getId(), form.getName());
			}

			return map;

		} catch (Exception e) {
			e.printStackTrace();
			// return new ArrayList();
		}
		return map;
	}

	public Map<String, String> getFormToMap(String formid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);
		if (form != null) {
			map.put(form.getId(), form.getName());
		}
		return map;
	}

	public Collection<Form> get_forms(String application) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		try {
			return fp.getFormsByModule(this.getModuleid(), application);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Form>();
		}
	}

	public Collection<Form> get_form(String formid) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> formList = new ArrayList<Form>();
		try {
			formList.add((Form) fp.doView(formid));
		} catch (Exception e) {
			e.printStackTrace();
			return formList;
		}
		return formList;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟侥ｏ拷锟接︼拷帽锟绞讹拷锟窖�锟斤拷锟斤拷锟斤拷应锟斤拷询锟�锟斤拷锟斤拷.
	 * 
	 * @param application
	 *            应锟矫憋拷识
	 * @return 锟斤拷询锟�锟斤拷锟斤拷
	 * @throws Exception
	 */
	public Collection<Form> get_searchFormList(String application) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		return fp.getFormsByModule(this.getModuleid(), application);

	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟侥ｏ拷锟斤拷应锟矫憋拷识锟斤拷询,锟斤拷锟斤拷锟斤拷应锟斤拷询锟�锟斤拷锟斤拷.
	 * 
	 * @param application
	 *            应锟矫憋拷识
	 * @return 锟斤拷询锟�锟斤拷锟斤拷
	 * @throws Exception
	 */
	public Collection<Form> get_searchFormListByApplicationOrModule(String application) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		if (moduleid != null && moduleid.length() > 0)
			return fp.getFormsByModule(moduleid, application);
		else
			return fp.getSearchFormsByApplication(applicationid, application);

	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟侥ｏ拷锟斤拷应锟矫憋拷识锟斤拷询,锟斤拷取锟斤拷应锟接�锟斤拷锟斤拷.
	 * 
	 * @param application
	 *            应锟矫憋拷识
	 * @return 锟接�锟斤拷锟斤拷
	 * @throws Exception
	 */
	public Collection<Form> get_subFormList(String application) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		return fp.getFormsByModule(this.getModuleid(), application);

	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟侥ｏ拷锟斤拷应锟矫憋拷识,锟斤拷取锟斤拷应锟�锟斤拷锟斤拷.
	 * 
	 * @param application
	 *            应锟矫憋拷识
	 * @return 锟�锟斤拷锟斤拷
	 * @throws Exception
	 */
	public Collection<Form> get_normalFormList(String application) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		
		if (this.getModuleid() == null) {
			return fp.get_formList(application);
		} else {
			return fp.getFormsByModule(this.getModuleid(), application);
		}
	}
	
	/**
	 * 获取普通(含映射)表单的集合
	 * @param application
	 * @return
	 * @throws Exception
	 */
	public Collection<Form> getNormalFormList(String application) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		return fp.getNormalFormsByModule(getModuleid(), application);
	}
	
	public Collection<Form> getTemplateFormList(String application) throws Exception {
		Collection<Form> rtn = new ArrayList<Form>();
		try {
			FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Collection<Form> r = fp.getTemplateFormsByModule(this.getModuleid(), application);
			if(r!=null && !r.isEmpty()){
				rtn.addAll(r);
			}
		} catch (Exception e) {
		}
		return rtn;
	}
	
	

	/**
	 * 锟斤拷荼?锟斤拷锟�锟斤拷取锟斤拷应锟�值锟斤拷锟斤拷.
	 * 
	 * @param formid
	 *            锟�锟斤拷锟�
	 * @return 锟�值锟斤拷锟斤拷
	 * @throws Exception
	 */
	public static Form get_FormById(String formid) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);
		if (form == null) {
			UserDefinedProcess hpp = (UserDefinedProcess) ProcessFactory.createProcess(UserDefinedProcess.class);
			UserDefined homepage = (UserDefined) hpp.doView(formid);
			if (homepage != null)
				form = homepage.getPage();
		}
		return form;
	}

	public Collection<Form> get_relateFormById(String formid) throws Exception {
		Collection<Form> formlist = null;
		Form form = get_FormById(formid);
		if (formlist == null) {
			formlist = new ArrayList<Form>();
		}
		formlist.add(form);
		return formlist;
	}

	/**
	 * 锟斤拷荼?锟斤拷锟�锟斤拷取锟斤拷应锟斤拷锟斤拷模锟斤拷锟斤拷锟斤拷.
	 * 
	 * @param formid
	 *            锟�锟斤拷锟�
	 * @return 模锟斤拷锟斤拷锟斤拷
	 * @throws Exception
	 */
	public Map<String, String> get_FormMod(String formid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();

		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);
		if (form != null) {
			map.put("modId", form.getModule().getId());
		}

		return map;
	}

	/**
	 * 锟斤拷锟斤拷锟街凤拷为锟截讹拷锟斤拷锟斤拷javascript.实锟斤拷锟斤拷-j锟斤拷锟斤拷态锟斤拷锟斤拷Formfield锟叫憋拷锟�
	 * 锟斤拷实锟斤拷锟角革拷锟斤拷锟斤拷锟
	 * �锟斤拷取锟侥�锟街段硷拷锟斤拷,锟斤拷锟斤拷一锟斤拷锟斤拷-锟斤拷j锟斤拷锟侥讹拷态锟斤拷锟斤拷Formfield锟叫憋拷锟�
	 * 
	 * @param selectFieldName
	 *            Formfield锟叫憋拷锟�锟街讹拷锟斤拷
	 * @param formid
	 *            锟斤拷锟斤拷?
	 * @param def
	 *            锟斤拷选锟叫碉拷Formfield锟叫憋拷锟街�
	 * @return 锟街凤拷为锟截讹拷锟斤拷锟斤拷javascript.
	 * @throws Exception
	 */
	public String creatFormfieldOptions(String selectFieldName, String formid, String def) throws Exception {
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		Form form = get_FormById(formid);
		map.put("", "{*[Select]*}");
		if (form != null && form.getAllFields() != null) {
			for (Iterator<FormField> iter = form.getAllFields().iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();

				map.put(field.getName(), field.getName());
			}
		}

		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}

	/**
	 * 锟斤拷锟斤拷锟街凤拷为锟截讹拷锟斤拷锟斤拷javascript.实锟斤拷锟斤拷-j锟斤拷锟斤拷态锟斤拷锟斤拷锟叫憋拷锟�
	 * 锟斤拷实锟斤拷锟角革拷锟斤拷锟斤拷锟�锟斤拷取锟侥�锟街段硷拷锟较硷拷锟斤拷锟侥碉拷锟斤拷锟斤拷锟街讹拷,锟斤拷锟斤拷一锟斤拷锟斤拷-
	 * j锟斤拷锟侥讹拷态锟斤拷锟斤拷Formfield锟叫憋拷锟�
	 * 
	 * @param selectFieldName
	 *            Formfield锟叫憋拷锟斤拷锟�
	 * @param formid
	 *            锟斤拷锟斤拷?
	 * @param def
	 *            Formfield锟叫憋拷锟街�
	 * @return
	 * @throws Exception
	 */
	public String creatFormfieldOptionsWithDocProp(String selectFieldName, String formid, String def) throws Exception {
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		Form form = get_FormById(formid);
		map.put("", "{*[Select]*}");
		if (form != null && form.getAllFields() != null) {
			for (Iterator<FormField> iter = form.getAllFields().iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				
					map.put(field.getName(), field.getName());
			}
			map.putAll(defaultSystemFormField4Flow());
		}
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}

	//获取下拉联动表单字段数据并过滤掉Reminder和htmleditorfield字段
	public String creatFormfieldOptionsWithDocPropFilterReminderfieldAndHtmleditorfield(String selectFieldName, String formid, String def) throws Exception {
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		Form form = get_FormById(formid);
		map.put("", "{*[Select]*}");
		if (form != null && form.getAllFields() != null) {
			for (Iterator<FormField> iter = form.getAllFields().iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if(field instanceof HTMLEditorField || field instanceof ReminderField || field instanceof TabField ||
						field instanceof ViewDialogField || field instanceof IncludeField){
				}else{
					map.put(field.getName(), field.getName());
				}
			}
			map.putAll(defaultSystemFormField4Flow());
		}
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}
	
	/**
	 * 为视图列创建表单字段下拉选项
	 * @param selectFieldName
	 * @param formid
	 * @param def
	 * @return
	 * @throws Exception
	 */
	public String creatFormfield4Column(String selectFieldName, String formid, String def) throws Exception {
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		Form form = get_FormById(formid);
		map.put("", "{*[Select]*}");
		if (form != null && form.getAllFields() != null) {
			for (Iterator<FormField> iter = form.getAllFields().iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if (field instanceof IncludeField
						|| (field instanceof HTMLEditorField) 
						|| (field instanceof TabField) 
						|| (field instanceof FlowHistoryField)
						|| (field instanceof FileManagerField)
						|| (field instanceof SurveyField)
						|| (field instanceof ImageUploadToDataBaseField)
						|| (field instanceof ViewDialogField)
						|| (field instanceof ReminderField)
						|| (field instanceof QRCodeField)){
					continue;
				}
				else {
					map.put(field.getName(), field.getName());
				}
			}
			map.putAll(defaultSystemFormField4Flow());
			map.put("$PrevAuditNode", "${*[core.dynaform.form.formfield.prev_audit_node]*}"); // 上一步流程状态
			map.put("$PrevAuditUser", "${*[core.dynaform.form.formfield.prev_audit_user]*}"); // 上一步流程提交人
			
		}
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}
	
	public static HashMap<String, String> defaultSystemFormField = new LinkedHashMap<String, String>();

	static {
		defaultSystemFormField.put("$Id", "{*[Form]*}ID"); // 表单ID
		defaultSystemFormField.put("$StateLabel", "{*[Form]*}{*[StateLabel]*}"); // 状态标签�
		defaultSystemFormField.put("$Created", "{*[Form]*}{*[Created]*}"); // 创建日期
		defaultSystemFormField.put("$AuditDate", "{*[Form]*}{*[AuditDate]*}"); // 审批日期
		defaultSystemFormField.put("$LastModified", "{*[Form]*}{*[LastModified]*}"); // 最后修改日期�
		defaultSystemFormField.put("$Author", "{*[Form]*}{*[Author]*}"); // 文档作者
		defaultSystemFormField.put("$AuditorNames", "{*[Form]*}{*[AuditorNames]*}"); // 审批名称�
		defaultSystemFormField.put("$LastFlowOperation", "{*[Form]*}{*[LastFlowOperation]*}"); // 最后操作�
		defaultSystemFormField.put("$FormName", "{*[Form]*}{*[Name]*}"); // 表单名称�
	}

	public Map<String, String> getDocumentFieldMap() {
		return defaultSystemFormField;
	}
	
	
	public static HashMap<String, String> defaultSystemFormField4Flow = new LinkedHashMap<String, String>();
		
	static{
		defaultSystemFormField4Flow.put("$Id", "${*[Form]*}ID"); // 表单ID
		defaultSystemFormField4Flow.put("$StateLabel", "${*[Form]*}{*[StateLabel]*}"); // 状态标签�
		defaultSystemFormField4Flow.put("$Created", "${*[Form]*}{*[Created]*}"); // 创建日期
		defaultSystemFormField4Flow.put("$AuditDate", "${*[Form]*}{*[AuditDate]*}"); // 审批日期
		defaultSystemFormField4Flow.put("$LastModified", "${*[Form]*}{*[LastModified]*}"); // 最后修改日期�
		defaultSystemFormField4Flow.put("$Author", "${*[Form]*}{*[Author]*}"); // 文档作者
		defaultSystemFormField4Flow.put("$AuditorNames", "${*[Form]*}{*[AuditorNames]*}"); // 审批名称�
		defaultSystemFormField4Flow.put("$LastFlowOperation", "${*[Form]*}{*[LastFlowOperation]*}"); // 最后操作�
		defaultSystemFormField4Flow.put("$FormName", "${*[Form]*}{*[Name]*}"); // 表单名称�
	}
		
	public static Map<String, String> defaultSystemFormField4Flow(){
		return defaultSystemFormField4Flow;
	}
	
	
	public static HashMap<String, String> defaultSystemFormField4FrontFlow = new LinkedHashMap<String, String>();
	static{
		//简体中文
		defaultSystemFormField4FrontFlow.put("$表单ID", "$Id"); // 表单ID
		defaultSystemFormField4FrontFlow.put("$表单状态标签", "$StateLabel"); // 状态标签�
		defaultSystemFormField4FrontFlow.put("$表单创建时间", "$Created"); // 创建日期
		defaultSystemFormField4FrontFlow.put("$表单审批日期", "$AuditDate"); // 审批日期
		defaultSystemFormField4FrontFlow.put("$表单最后修改时间", "$LastModified"); // 最后修改日期�
		defaultSystemFormField4FrontFlow.put("$表单作者", "$Author"); // 文档作者
		defaultSystemFormField4FrontFlow.put("$表单审批人名称", "$AuditorNames"); // 审批名称�
		defaultSystemFormField4FrontFlow.put("$表单最后流程操作", "$LastFlowOperation"); // 最后操作�
		defaultSystemFormField4FrontFlow.put("$表单名称", "$FormName"); // 表单名称�
		//繁体中文
		defaultSystemFormField4FrontFlow.put("$表單ID", "$Id"); // 表单ID
		defaultSystemFormField4FrontFlow.put("$表單狀態標籤", "$StateLabel"); // 状态标签�
		defaultSystemFormField4FrontFlow.put("$表單創建時間", "$Created"); // 创建日期
		defaultSystemFormField4FrontFlow.put("$表單審批日期", "$AuditDate"); // 审批日期
		defaultSystemFormField4FrontFlow.put("$表單最後修改時間", "$LastModified"); // 最后修改日期�
		defaultSystemFormField4FrontFlow.put("$表單作者", "$Author"); // 文档作者
		defaultSystemFormField4FrontFlow.put("$表單審批人名稱", "$AuditorNames"); // 审批名称�
		defaultSystemFormField4FrontFlow.put("$表單最後流程操作", "$LastFlowOperation"); // 最后操作�
		defaultSystemFormField4FrontFlow.put("$表單名稱", "$FormName"); // 表单名称�
	}
		
	public static String getdefaultSystemFormFieldByName(String name){
		return defaultSystemFormField4FrontFlow.get(name);
	}
	
	public String creatFormfieldOptionsWithDocProp4Flow(String selectFieldName, String formid, String def) throws Exception {
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		Form form = get_FormById(formid);
		map.put("", "{*[Select]*}");
		if (form != null && form.getAllFields() != null) {
			for (Iterator<FormField> iter = form.getAllFields().iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if(field instanceof CheckboxField || field instanceof RadioField || field instanceof InputField
						|| field instanceof TextareaField || field instanceof SelectField || field instanceof DateField
						|| field instanceof DepartmentField || field instanceof TreeDepartmentField || field instanceof UserField
						|| field instanceof SelectAboutField || field instanceof SuggestField || field instanceof HTMLEditorField){
					map.put(field.getName(), field.getName());
				}
			}
			map.putAll(defaultSystemFormField4Flow());
		}
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}
	
	/**
	 * 跨流程主子表单传参子表单字段
	 * @param selectFieldName
	 * @param formid
	 * @param def
	 * @return
	 * @throws Exception
	 */
	public String creatFormfieldOptionsWithDocProp4subFlow(String selectFieldName, String formid, String def) throws Exception {
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		Form form = get_FormById(formid);
		map.put("", "{*[Select]*}");
		if (form != null && form.getAllFields() != null) {
			for (Iterator<FormField> iter = form.getAllFields().iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if(field instanceof CheckboxField || field instanceof RadioField || field instanceof InputField
						|| field instanceof TextareaField || field instanceof SelectField || field instanceof DateField
						|| field instanceof DepartmentField || field instanceof TreeDepartmentField || field instanceof UserField
						|| field instanceof SelectAboutField || field instanceof SuggestField || field instanceof HTMLEditorField){
					map.put(field.getName(), field.getName());
				}
			}
		}
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}

	public static String getDisplayFieldNameByName(String name) {
		Set<Entry<String, String>> entrys = defaultSystemFormField.entrySet();
		for (Iterator<Entry<String, String>> it = entrys.iterator(); it.hasNext();) {
			Entry<String, String> entry = it.next();
			if (entry.getKey().equals(name)) {
				return entry.getValue();
			}
		}
		return name;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟侥ｏ拷锟斤拷约锟斤拷?,锟斤拷锟斤拷锟街凤拷为锟截讹拷锟斤拷锟斤拷javascript.实锟斤拷锟斤拷-
	 * j锟斤拷锟斤拷态锟斤拷锟斤拷锟叫憋拷锟�
	 * 
	 * @param selectFieldNamem
	 *            锟街讹拷锟斤拷
	 * @param appid
	 *            应锟矫憋拷识
	 * @param moduleid
	 *            锟斤拷锟斤拷模锟斤拷锟斤拷锟�
	 * @param formid
	 *            锟斤拷锟斤拷?锟斤拷锟�
	 * @param def
	 *            锟街讹拷值
	 * @return
	 * @throws Exception
	 */
	public String creatFormfieldOptionsByApplicationAndModule(String selectFieldName, String appid, String moduleid,
			String formid, String def) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();

		map.put("none", "{*[Select]*}");
		if (appid != null && moduleid != null && appid.length() > 0 && moduleid.length() > 0 && !appid.equals("none")
				&& !moduleid.equals("none")) {
			Form form = get_FormById(formid);
			if (form != null && form.getFields() != null) {
				for (Iterator<FormField> iter = form.getFields().iterator(); iter.hasNext();) {
					FormField field = (FormField) iter.next();
					map.put(field.getName(), field.getName());
				}
			}
		}
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟侥ｏ拷锟斤拷约锟斤拷?,锟斤拷锟斤拷锟街凤拷为锟截讹拷锟斤拷锟斤拷javascript,实锟斤拷锟斤拷-
	 * j锟斤拷锟斤拷态锟斤拷锟斤拷锟叫憋拷锟�
	 * 
	 * @param selectFieldNamem
	 *            锟街讹拷锟斤拷
	 * @param appid
	 *            应锟矫憋拷识
	 * @param moduleid
	 *            锟斤拷锟斤拷模锟斤拷锟斤拷锟�
	 * @param formid
	 *            锟斤拷锟斤拷?锟斤拷锟�
	 * @param def
	 *            锟街讹拷值
	 * @return
	 * @throws Exception
	 */
	public String creatFormfieldsByApplicationAndModule(String selectFieldName, String appid, String moduleid,
			String formid, String def) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();

		if (appid != null && moduleid != null && appid.length() > 0 && moduleid.length() > 0 && !appid.equals("none")
				&& !moduleid.equals("none")) {
			Form form = get_FormById(formid);
			if (form != null && form.getFields() != null) {
				for (Iterator<FormField> iter = form.getFields().iterator(); iter.hasNext();) {
					FormField field = (FormField) iter.next();
					map.put(field.getName(), field.getName());
				}
			}
		}
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}

	/**
	 * 获取表单字段集合
	 * 
	 * @param formid
	 *            表单ID
	 * @return 表单字段集合（包括Document基本属性）
	 * @throws Exception
	 */
	public Map<String, String> getFields(String formid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		// map.put("", "{*[Select]*}");

		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);

		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if (field instanceof IncludeField || field instanceof FileManagerField
						|| field instanceof ImageUploadField || field instanceof ImageUploadToDataBaseField
						|| field instanceof TabField || field instanceof ViewDialogField
						|| field instanceof ReminderField) {
				} else
					map.put(field.getName(), field.getName());
			}
		}

		map.putAll(defaultSystemFormField4Flow());
		return map;
	}
	
	public Map<String, String> getFields4SearchForm(String formid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		// map.put("", "{*[Select]*}");

		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);

		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if (field instanceof IncludeField || field instanceof FileManagerField
						|| field instanceof ImageUploadField || field instanceof ImageUploadToDataBaseField
						|| field instanceof TabField || field instanceof ViewDialogField
						|| field instanceof ReminderField) {
				} else
					map.put(field.getName(), field.getName());
			}
		}

		return map;
	}
	
	/**
	 * 获取动态打印时需要显示的表单字段集合
	 * 
	 * @param formid
	 *            表单ID
	 * @return 表单字段集合（包括Document基本属性）
	 * @throws Exception
	 */
	public Map<String, String> getFields4Print(String formid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		// map.put("", "{*[Select]*}");

		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);

		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if (field instanceof IncludeField || field instanceof FileManagerField
						|| field instanceof ImageUploadField || field instanceof ImageUploadToDataBaseField
						|| field instanceof TabField || field instanceof ViewDialogField 
						|| field instanceof ReminderField || field instanceof OnLineTakePhotoField
						|| field instanceof HTMLEditorField || field instanceof WordField) {
				} else
					map.put(field.getName(), field.getName());
			}
		}

		map.putAll(defaultSystemFormField4Flow());
		return map;
	}

	/**
	 * @param formid
	 * @return ValueStoreFields
	 * @throws Exception
	 */
	public Map<String, String> getValueStoreFields(String formid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");

		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);

		if (form != null) {
			Collection<FormField> colls = form.getValueStoreFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				map.put(field.getName(), field.getName());
			}
		}

		map.putAll(defaultSystemFormField4Flow());
		return map;
	}

	/**
	 * 
	 * get all fields
	 */
	public Map<String, String> getAllFields(String formid, String condition) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (condition.equals("true")) {
			map.put("", "{*[Select]*}");
		}
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);

		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				map.put(field.getName(), field.getName());
			}
		}

		return map;
	}

	/**
	 * get field type
	 */
	public Map<String, String> getFieldType(String formid, String fieldname) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);
		
		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if (field.getName().equals(fieldname)) {
					map.put(field.getFieldtype(), field.getFieldtype());
				}

			}
		}

		return map;
	}

	/**
	 * 
	 * 锟斤拷锟斤拷锟斤拷锟�锟皆硷拷锟斤拷锟剿猴拷,锟斤拷锟截�锟斤拷锟斤拷锟街讹拷.
	 * 
	 * @param selectFieldNamem
	 *            锟街讹拷锟斤拷
	 * @param appid
	 *            应锟矫憋拷识
	 * @param moduleid
	 *            锟斤拷锟斤拷模锟斤拷锟斤拷锟�
	 * @param formid
	 *            锟斤拷锟斤拷?锟斤拷锟�
	 * @param def
	 *            锟街讹拷值
	 * @return 锟�锟斤拷锟斤拷锟街讹拷
	 * @throws Exception
	 */
	public Map<String, String> getFilterScriptFields(String formid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");

		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);

		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				map.put(field.getName(), field.getName());
			}
		}
		return map;
	}

	
	public String refreshFlowPanelHTML(String formid, String docid, String userid, Map<String, Object> valuesMap,
			String flowid, String actionType, HttpServletRequest request) throws Exception {
		// PersistenceUtils.currentSession().clear();
		WebUser user = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user == null) {
			UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			user = up.getWebUserInstance(userid);
		}
		
		if (user != null) {
			try {
//				synchronized (user) {
					// PersistenceUtils.beginTransaction();
					long start = System.currentTimeMillis();
					// try {
					// //PersistenceUtils.getSessionSignal().sessionSignal++;

					// if (StringUtil.isBlank(user.getApplicationid())) {
					// String application = (String)
					// valuesMap.get("application");
					// user.setApplicationid(application);
					// }

					Form form = get_FormById(formid);

					ParamsTable params = ParamsTable.convertHTTP(request);
					params.setSessionid(request.getSession().getId());

					params.putAll(valuesMap);

					DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,form.getApplicationid());
					// 从Session中获取Document
					Document doc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, user);
					// 从数据库中获取Document
					if (doc == null) {
						doc = (Document) dp.doView(docid);
					}
					if (doc == null) {
						doc = form.createDocument(params, user);
						
						//docid不为空时,设为原来的id,解决因浏览器页签切换后包含元素内容丢失
						if(!StringUtil.isBlank(docid))
							doc.setId(docid);
					}
					form.addItems(doc, params);
					form.recalculateDocument(doc, params, false, user);

					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), form.getApplicationid());
					runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());

					if (user != null && doc != null && doc.getId() != null) {
						MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
					}

					//当文档为新建时，没有state则需要添加一个
					if (doc.getIstmp() || StringUtil.isBlank(doc.getStateid())) {
						if (!StringUtil.isBlank(flowid) && !"null".equals(flowid)) {
							//doc = (Document) doc.clone();
							FlowStateRTProcess stateProcess = (FlowStateRTProcess) ProcessFactory
									.createRuntimeProcess(FlowStateRTProcess.class, doc
											.getApplicationid());
							
							BillDefiProcess billDefiProcess = (BillDefiProcess) ProcessFactory
									.createProcess(BillDefiProcess.class);
							
							BillDefiVO flowVO = (BillDefiVO) billDefiProcess.doView(flowid);
							Node firstNode = StateMachine.getFirstNode(doc, flowVO, user,
									params);

							if (firstNode != null) {
								FlowStateRT state = stateProcess.createTransientFlowStateRT(doc,
										flowid, user);
								ArrayList<NodeRT> noderts = new ArrayList<NodeRT>();
								NodeRT nodert = new NodeRT(state, firstNode, FlowType.START2RUNNING);
								noderts.add(nodert);
								state.setNoderts(noderts);

								doc.setState(state);// 创建瞬态流程实例
								doc.setInitiator(user.getId());// 设置流程发起人
								Node startNode = StateMachine.getStartNodeByFirstNode(flowVO,
										firstNode);
								if (startNode != null) {
									// 设置上下文当前处理节点对象
									request.setAttribute("_targetNode",  firstNode.id);
									request.setAttribute("_targetNodeRT", nodert);
									
//									stateProcess.doApprove(params, doc.getState(), currNodeId,
//											nextNodeIds, FlowType.START2RUNNING, "",
//											Environment.getInstance(), user);
								}
							} 
						}
					}
					
					StateMachineHelper helper = new StateMachineHelper(
							doc);
					String htmlText = null;
					
					if ("init".equals(actionType)) {
						htmlText = helper.toFlowHtmlTextForInit(doc, user,
								"",request);
					}
					else if ("commitTo".equals(actionType)) {
						htmlText = helper.toFlowHtmlTextForCommitTo(doc, user,
								"",request);
					} 
					else if ("returnTo".equals(actionType)) {
						htmlText = helper.toFlowHtmlTextForReturnTo(doc, user,
								"",request);
					}
					log.info("Refresh FlowProcessPanel total times->" + (System.currentTimeMillis() - start) + "(ms)");
					return htmlText;
//				}
			} catch (Exception e) {
				log.error("FormHelper Error", e);
				throw e;
			}

		} else {
			throw new OBPMValidateException("无法从Session中获取User");
		}

	}
	/**
	 * 表单刷新
	 * 
	 * @param formid
	 * @param actfield
	 * @param docid
	 * @param userid
	 * @param valuesMap
	 * @param flowid
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String refresh(String _tabid, String formid, String actfield, String docid, String userid, Map<String, Object> valuesMap,
			String flowid, HttpServletRequest request) throws Exception {

		// PersistenceUtils.currentSession().clear();
		StringBuffer text = new StringBuffer();
		WebUser user = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user == null) {
			UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			user = up.getWebUserInstance(userid);
		}
		
		if (user != null) {
			try {
//				synchronized (user) {
					// PersistenceUtils.beginTransaction();
					long start = System.currentTimeMillis();
					// try {
					// //PersistenceUtils.getSessionSignal().sessionSignal++;

					// if (StringUtil.isBlank(user.getApplicationid())) {
					// String application = (String)
					// valuesMap.get("application");
					// user.setApplicationid(application);
					// }

					Form form = get_FormById(formid);
					log.debug("Refresh Form SETP-3.0 times->" + (System.currentTimeMillis() - start) + "(ms)");

					log.debug("Refresh Form SETP-3.1 times->" + (System.currentTimeMillis() - start) + "(ms)");

					ParamsTable params = ParamsTable.convertHTTP(request);
					params.setParameter("_tabid", _tabid);
					params.setParameter("actfield", actfield);
					params.setSessionid(request.getSession().getId());

					params.putAll(valuesMap);

					DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,form.getApplicationid());
					// 从Session中获取Document
					Document doc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, user);

					// 从数据库中获取Document
					if (doc == null) {
						doc = (Document) dp.doView(docid);
					}
					if (doc == null) {
						doc = form.createDocument(params, user);
						//docid不为空时,设为原来的id,解决因浏览器页签切换后包含元素内容丢失
						if(!StringUtil.isBlank(docid))
							doc.setId(docid);
					}
					form.addItems(doc, params);
					form.recalculateDocument(doc, params, false, user);

					// doc.setEditAble(StateMachineHelper.isDocEditUser(doc,
					// user));

					log.debug("Refresh Form SETP-4.0 times->" + (System.currentTimeMillis() - start) + "(ms)");

					log.debug("Refresh Form SETP-4.1 times->" + (System.currentTimeMillis() - start) + "(ms)");

					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), form.getApplicationid());
					runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());

					log.debug("Refresh Form SETP-4.2 times->" + (System.currentTimeMillis() - start) + "(ms)");

					// 获取所有字段
					Collection<FormField> fields = form.getFields();
					if (fields != null) {
						for (Iterator<FormField> iter = fields.iterator(); iter.hasNext();) {
							FormField field = (FormField) iter.next();

							if (field.isCalculateOnRefresh() && !field.getName().equals(actfield)) {
								String destVal = doc.getItemValueAsString(field.getName(),field);
								String origVal = (String) valuesMap.get(field.getName());
								if (field.isRender(destVal, origVal)) {
									text.append(field.getRefreshScript(runner, doc, user));
								}
							}
						}
					}

					if (user != null && doc != null && doc.getId() != null) {
						MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
					}
					log.info("Refresh Form total times->" + (System.currentTimeMillis() - start) + "(ms)");
//				}
			} catch (Exception e) {
				log.error("FormHelper Error", e);
				throw e;
			}

		} else {
			throw new OBPMValidateException("无法从Session中获取User");
		}
		return text.toString();
	}

	/**
	 * 
	 * @param selectFieldName
	 *            Formfield
	 * @param formid
	 *            �
	 * @param def
	 *            �
	 * @return javascript.
	 * @throws Exception
	 */
	public String creatFormfieldOptionsByguide(String selectFieldName, String formid, String def) throws Exception {
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		Form form = get_FormById(formid);

		if (form != null && form.getAllFields() != null) {
			for (Iterator<FormField> iter = form.getAllFields().iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if (field instanceof IncludeField
						|| (field instanceof HTMLEditorField) 
						|| (field instanceof TabField) 
						|| (field instanceof FlowHistoryField)
						|| (field instanceof FileManagerField)
						|| (field instanceof SurveyField)
						|| (field instanceof ImageUploadToDataBaseField)
						|| (field instanceof ViewDialogField)
						|| (field instanceof ReminderField)
						|| (field instanceof QRCodeField)) {
					continue;
				} else {
					map.put(field.getName(), field.getName());
				}

			}
		}

		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟侥ｏ拷锟接︼拷帽锟绞讹拷锟窖�锟斤拷锟斤拷锟斤拷应锟�锟斤拷锟斤拷.
	 * 
	 * @param moduleId
	 *            锟斤拷锟斤拷模锟斤拷锟绞�
	 * @return 锟�锟斤拷锟斤拷
	 * @throws Exception
	 */
	public Map<String, String> get_formListByModule(String moduleId) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> col = fp.getFormsByModule(moduleId, this.getApplicationid());
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		Iterator<Form> it = col.iterator();
		while (it.hasNext()) {
			Form form = (Form) it.next();
			log.debug("formname-->" + form.getName());
			// 判断是否有流程,没有的流程的不列出来
			Collection<Activity> activitys = form.getActivitys();
			for (Iterator<Activity> iterator = activitys.iterator(); iterator.hasNext();) {
				Activity activity = (Activity) iterator.next();
				if (activity != null && activity.getType() == ActivityType.WORKFLOW_PROCESS)
					map.put(form.getId(), form.getName());
			}
		}

		return map;
	}

	/**
	 * 根据模块Id查出该模块下的所有表单
	 * 
	 * @param moduleId
	 * @return map
	 * @throws Exception
	 */
	public Map<String, String> get_formListByModules(String moduleId) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> col = fp.getFormsByModule(moduleId, this.getApplicationid());
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		Iterator<Form> it = col.iterator();
		while (it.hasNext()) {
			Form form = (Form) it.next();
			map.put(form.getId(), form.getName());
		}
		return map;
	}
	
	/**
	 * 根据模块Id查出该模块下的所有片段表单
	 * 
	 * @param moduleId
	 * @return map
	 * @throws Exception
	 */
	public Map<String, String> getFragmentformListByModules(String moduleId) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> col = fp.getFragmentFormsByModule(moduleId, this.getApplicationid());
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		Iterator<Form> it = col.iterator();
		while (it.hasNext()) {
			Form form = (Form) it.next();
			map.put(form.getId(), form.getName());
		}
		return map;
	}
	
	/**
	 * 根据模块Id查出该模块下的普通和映射表单
	 * 
	 * @param moduleId
	 * @return map
	 * @throws Exception
	 */
	public Map<String, String> get_NormalAndMappingFormListByModules(String moduleId) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> col = fp.getFormsByModule(moduleId, this.getApplicationid());
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		Iterator<Form> it = col.iterator();
		while (it.hasNext()) {
			Form form = (Form) it.next();
			if(form.getType() == Form.FORM_TYPE_NORMAL
					|| form.getType() == Form.FORM_TYPE_NORMAL_MAPPING){
				log.debug("formname-->" + form.getName());
				map.put(form.getId(), form.getName());
			}
		}
		return map;
	}
	
	/**
	 * 根据模块Id查出该模块下的所有表单，并过滤掉表单(查询表单和模板表单)
	 * 
	 * @param moduleId
	 * @return （过滤查询表单后的）map
	 * @throws Exception
	 */
	public Map<String, String> get_formListByModuleType(String moduleId) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> col = fp.getFormsByModule(moduleId, this.getApplicationid());
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		Iterator<Form> it = col.iterator();
		while (it.hasNext()) {
			Form form = (Form) it.next();	
			if (form.getType() != Form.FORM_TYPE_SEARCHFORM && form.getType() != Form.FORM_TYPE_TEMPLATEFORM && form.getType() != Form.FORM_TYPE_FRAGMENT) {
				log.debug("formname-->" + form.getName());
				map.put(form.getId(), form.getName());
			}
		}
		return map;
	}

	public String getFieldAndType(String formid) throws Exception {
		Form form = get_FormById(formid);
		JSONArray array = new JSONArray();
		JSONObject obj;

		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if (field != null && !(field instanceof IncludeField)) {
					obj = new JSONObject();
					obj.put("text", field.getName());
					obj.put("value", field.getFieldtype());
					array.add(obj);
				}
			}
		}
		return array.toString();
	}

	/**
	 * 锟斤拷锟斤拷薷锟斤拷锟絀D锟斤拷取锟睫革拷锟斤拷
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	/**
	 * 锟斤拷取锟睫改猴拷锟斤拷侄锟街碉拷锟街�
	 * 
	 * @param docId
	 * @param applicationid
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public String getFieldModifiedLog(String docId, String applicationid, String fieldName) throws Exception {
		JSONArray array = new JSONArray();
		JSONObject obj;
		DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationid);
		Collection<Document> col = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (docId != null && docId.trim().length() > 0) {
			Document doc = (Document) process.doView(docId);
			col = process.queryModifiedDocuments(doc);// 锟斤拷锟揭硷拷录
			if (col != null && col.size() > 0) {
				Iterator<Document> iter = col.iterator();
				List<String> doclist = new ArrayList<String>();
				while (iter.hasNext()) {
					Document logDoc = (Document) iter.next();
					if (logDoc != null) {
						String fieldValue = logDoc.getItemValueAsString(fieldName);
						fieldValue = getFieldValue(logDoc, fieldName, fieldValue);
						String modifier = getModifieder(logDoc.getLastmodifier());
						String modified_time = "";
						String statelabel = logDoc.getStateLabel();
						try {
							modified_time = format.format(logDoc.getLastmodified());
						} catch (Exception ex) {
							format = new SimpleDateFormat("yyyy-MM-dd");
							modified_time = format.format(logDoc.getLastmodified());
						}
						if (doclist != null && doclist.contains(fieldValue)) {
						} else {
							doclist.add(fieldValue);
							if (fieldValue == null || fieldValue.length() <= 0) {
								fieldValue = "null";
							}
							obj = new JSONObject();
							obj.put("modifier", modifier);
							obj.put("time", modified_time);
							obj.put("fieldValue", fieldValue);
							obj.put("statelabel", statelabel);
							array.add(obj);
						}
					}
				}
				if (doclist.size() > 0) {
					String currentfieldvalue = doc.getItemValueAsString(fieldName);
					currentfieldvalue = getFieldValue(doc, fieldName, currentfieldvalue);
					if (doclist.get(doclist.size() - 1) != null
							&& !(doclist.get(doclist.size() - 1).equals(currentfieldvalue))) {
						obj = new JSONObject();
						if (currentfieldvalue == null || currentfieldvalue.length() <= 0) {
							currentfieldvalue = "null";
						}
						obj.put("modifier", getModifieder(doc.getLastmodifier()));
						obj.put("time", format.format(doc.getLastmodified()));
						obj.put("fieldValue", currentfieldvalue);
						obj.put("statelabel", doc.getStateLabel());
						array.add(obj);
					} else if (doclist.get(doclist.size() - 1) == null && currentfieldvalue != null
							&& !"".equals(currentfieldvalue)) {
						obj = new JSONObject();
						obj.put("modifier", getModifieder(doc.getLastmodifier()));
						obj.put("time", format.format(doc.getLastmodified()));
						obj.put("fieldValue", currentfieldvalue);
						obj.put("statelabel", doc.getStateLabel());
						array.add(obj);
					}
				}

			}

		}
		return array.toString();
	}

	private String getFieldValue(Document logDoc, String fieldName, String fieldValue) {
		String _fieldValue = "";
		try {
			if(!StringUtil.isBlank(fieldValue)){
				Form form = logDoc.getForm();
				Collection<FormField> fields = form.getAllFields();
				Iterator<FormField> it = fields.iterator();
				while(it.hasNext()){
					FormField field = it.next();
					if(field.getName().equals(fieldName)){
						if(field instanceof DepartmentField || field instanceof TreeDepartmentField){
							DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
							String[] ids = fieldValue.split(";");
							for(int i=0; i<ids.length; i++){
								DepartmentVO department = (DepartmentVO) process.doView(ids[i]);
								if(department != null){
									_fieldValue = _fieldValue + department.getName() + ";";
								}
							}
							break;
						}else if(field instanceof UserField){
							UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
							String[] ids = fieldValue.split(";");
							for(int i=0; i<ids.length; i++){
								UserVO user = (UserVO) process.doView(ids[i]);
								if(user != null){
									_fieldValue = _fieldValue + user.getName() + ";";
								}
							}
							break;
						}else{
							_fieldValue = fieldValue;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _fieldValue;
	}

	public String getModifieder(String userId) throws Exception {
		String name = "";
		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		if (userId != null && userId.trim().length() > 0) {
			UserVO user = (UserVO) userProcess.doView(userId);
			if (user != null) {
				name = user.getName();
			}
			log.warn("User not fonud, ID: " + userId);
		}
		return name;
	}

	/**
	 * 权限字段
	 * 
	 * @param formid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getAllAuthorityFields(String formid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);
		if (form != null) {
			Collection<FormField> fields = form.getAllFields();
			for (Iterator<FormField> iterator = fields.iterator(); iterator.hasNext();) {
				FormField f = (FormField) iterator.next();
				if (f.isAuthority()) {
					map.put(f.getName(), f.getName());
				}
			}
		}
		return map;
	}

	public Collection<Form> get_FormByModule() throws Exception {
		Collection<Form> forms = new HashSet<Form>();
		if (moduleid == null || moduleid.trim().length() <= 0) {
			return forms;
		}
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		forms = fp.getFormsByModule(moduleid, getApplicationid());

		return forms;
	}

	/**
	 * 锟斤拷取锟街段诧拷锟斤拷checkbox锟斤拷示
	 * 
	 * @param formid
	 *            锟�ID
	 * @return 锟斤拷锟斤拷权锟斤拷锟街段的硷拷锟斤拷
	 * @throws Exception
	 */
	public String getFiledsCheckBox(String formid, String divid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);

		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if (isShouldIncludeFiled(field))
					map.put(field.getName(), field.getName());
			}
		}
		String[] str = new String[10];
		return DWRHtmlUtils.createFiledCheckbox(map, divid, str);
	}

	/**
	 * 
	 * @param formid
	 * @param divid
	 * @return 显示的filed应该是只包括input,select,date,department等formfield
	 * @throws Exception
	 */
	public boolean isShouldIncludeFiled(FormField field) throws Exception {

		if (!field.getName().startsWith(("$")) && field.getTextType() != null
				&& (field.getTextType().equalsIgnoreCase("text") || field.getTextType().equalsIgnoreCase("readonly"))) {
			if (field instanceof InputField)
				return true;

			if (field instanceof CheckboxField)
				return true;

			if (field instanceof DateField)
				return true;

			if (field instanceof DepartmentField)
				return true;

			if (field instanceof RadioField)
				return true;

			if (field instanceof SelectField)
				return true;
		}
		return false;
	}

	/**
	 * 生成一个form的Checkbox
	 * 
	 * @param moduleId
	 * @param divid
	 * @return
	 * @throws Exception
	 */
	public String getFormNameCheckBox(String moduleId, String divid) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> col = fp.getFormsByModule(moduleId, this.getApplicationid());
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (col != null) {
			for (Iterator<Form> iter = col.iterator(); iter.hasNext();) {
				Form form = (Form) iter.next();
				map.put(form.getId(), form.getName());
			}
		}
		String[] str = new String[10];
		return DWRHtmlUtils.createFiledCheckbox(map, divid, str);
	}

	public Form findFormByRelationName(String relationName) throws Exception {
		return ((BaseFormProcess<?>) process).findFormByRelationName(relationName, null);
	}

	/**
	 * 获取数据表名称映射
	 * 
	 * @param applicationId
	 * @return
	 */
	public Map<String, String> getDataBaseTableMap(String applicationId) {
		Map<String, String> rtn = new LinkedHashMap<String, String>();
		rtn.put("", "{*[Select]*}");

		Collection<String> tables = DbTypeUtil.getTableNames(applicationId);
		if (tables != null) {
			for (Iterator<String> iterator = tables.iterator(); iterator.hasNext();) {
				String tableName = (String) iterator.next();
				if (tableName.equalsIgnoreCase("t_document") || tableName.split("_")[0].equalsIgnoreCase("auth") || tableName.split("_")[0].equalsIgnoreCase("tlk") || tableName.split("_")[0].equalsIgnoreCase("log")) {

				} else {
					rtn.put(tableName, tableName);
				}
			}
		}
		return rtn;
	}

	public Map<String, String> getDataBaseColumnMap(String tableName, String applicationId) {
		Map<String, String> rtn = new LinkedHashMap<String, String>();
		rtn.put("", "{*[Select]*}");
		Table table = DbTypeUtil.getTable(tableName, applicationId);
		if (table != null) {
			Collection<Column> columns = table.getColumns();
			for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext();) {
				Column column = (Column) iterator.next();
				rtn.put(column.getName(), column.getName());
			}
		}

		return rtn;
	}

	public Map<String, String> getDateFields(String formid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");

		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);

		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if (Item.VALUE_TYPE_DATE.equals(field.getFieldtype())) {
					map.put(field.getName(), field.getName());
				}
			}
		}
		map.put("$Created", "{*[Form]*}{*[Created]*}");
		map.put("$LastModified", "{*[Form]*}{*[LastModified]*}");
		return map;
	}
	/**
	 * 表单字段是否是可排序控件 是返回true， 否则false。
	 * 
	 @param formid
	 * @return
	 * @throws Exception
	 * */
	public String isNoOrderField(String formid,String value) throws Exception {
		String isNoOrder="false";
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);
		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if ((field instanceof AbstractUploadField
						||field instanceof AttachmentUploadField
						||field instanceof AttachmentUploadToDataBaseField
						||field instanceof CalctextField
						||field instanceof FileManagerField
						||field instanceof HandwritingField
						||field instanceof HTMLEditorField
						||field instanceof ImageUploadField
						||field instanceof ImageUploadToDataBaseField
						||field instanceof MapField
						||field instanceof TextareaField
						||field instanceof WordField) && field.getName().equals(value)){
					isNoOrder = "true";
					}				
		        }
			}
		return isNoOrder;
		}
	
	/**
	 * 表单字段是否为地图控件，是返回true，否则false。
	 * 
	 * @param formid
	 * @return
	 * @throws Exception
	 */
	public boolean isMapField(String formid,String value) throws Exception {
		boolean isMap=false;
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);
		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if (field.getType()!=null && field.getName()!=null){
					if(field.getType().equals("map") && field.getName().equals(value)){
						isMap=true;
					}
				}
			}
		}
		return isMap;
	}

	/**
	 * 根据流程ID获取表单选项
	 * 
	 * @param flowid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getFormOptionsByFlow(String flowid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");

		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);

		ParamsTable params = new ParamsTable();
		params.setParameter("s_activityXML", "%" + flowid + "%"); // 查询操作定义XML
		Collection<?> forms = fp.doSimpleQuery(params);

		if (forms != null && !forms.isEmpty()) {
			for (Iterator<?> iterator = forms.iterator(); iterator.hasNext();) {
				Form form = (Form) iterator.next();
				map.put(form.getId(), form.getName());
			}
		}

		return map;
	}

	/**
	 * 根据软件编号获得关联的域
	 * 
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getDomainByApplication(String applicationid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		DomainProcess dp = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
		Collection<DomainVO> cdomain = dp.getAllDomain();
		if (cdomain.size() > 0) {
			for (Iterator<DomainVO> iterator = cdomain.iterator(); iterator.hasNext();) {
				DomainVO domain = (DomainVO) iterator.next();
				Collection<ApplicationVO> capplication = domain.getApplications();
				if (capplication.size() > 0) {
					for (Iterator<ApplicationVO> iterator1 = capplication.iterator(); iterator1.hasNext();) {
						ApplicationVO applicationVO = (ApplicationVO) iterator1.next();
						if (applicationVO != null && applicationid != null) {
							if (applicationVO.getId().equals(applicationid)) {
								map.put(domain.getId(), domain.getName());
							}
						}
					}
				}
			}
		}
		return map;
	}

	/**
	 * 根据操作id、formId得到动态打印配置（PRINTER）ID
	 * 
	 * @param activityId
	 * @param formId
	 * @return
	 * @throws Exception
	 */
	public String getPrinterId(String activityId, String formId) throws Exception {
		String rnt = "";
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formId);
		Set<Activity> acts = form.getActivitys();
		for (Iterator<Activity> it = acts.iterator(); it.hasNext();) {
			Activity act = (Activity) it.next();
			if (act.getId().equals(activityId)) {
				rnt = act.getOnActionPrint();
				break;
			}
		}
		return rnt;
	}
	
	/**
	 * 文件上传控件/文件上传到数据库控件获取限制的文件类型(前台调用)
	 * @param customizeType 
	 * 		自定义文件类型
	 * @return
	 */
	public String getFileDesc(String customizeType){
		StringBuffer fileDescStr = new StringBuffer();
		String[] fileDesc = getFileTypeArray(customizeType);
		if(fileDesc != null && fileDesc.length > 0){
			for(int i = 0; i < fileDesc.length; i++){
				if(! StringUtil.isBlank(fileDesc[i])){
					fileDescStr.append(fileDesc[i]).append("/");
				}
			}
		}
		if(fileDescStr.length() > 0){
			return fileDescStr.substring(0, fileDescStr.lastIndexOf("/"));
		}
		return fileDescStr.toString();
	}
	
	/**
	 * 文件上传控件/文件上传到数据库控件获取限制的文件类型(前台调用)
	 * @param customizeType 
	 * 		自定义文件类型
	 * @return
	 */
	public String getFileExt(String customizeType){
		StringBuffer fileExtStr = new StringBuffer();
		String[] fileExt = getFileTypeArray(customizeType);
		if(fileExt != null && fileExt.length > 0){
			for(int i = 0; i < fileExt.length; i++){
				if(! StringUtil.isBlank(fileExt[i])){
					fileExtStr.append("*.").append(fileExt[i]).append(";");
				}
			}
		}
		if(fileExtStr.length() > 0){
			return fileExtStr.substring(0, fileExtStr.lastIndexOf(";"));
		}
		return fileExtStr.toString();
	}
	
	public String[] getFileTypeArray(String customizeType){
		if(! StringUtil.isBlank(customizeType)){
			String[] fileExt = customizeType.split(";");
			return fileExt;
		}
		return null;
	}
	
	/**
	 * 获取软件下的所有普通类型和映射类型的表单
	 * @param application
	 * 		软件Id
	 * @return
	 * 		Map对象 (key=表单id  value=表单全名)
	 * @throws Exception
	 */
	public Map<String, String> getRelatedFormsByApplication(String application) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		
		FormProcess proc = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> col = proc.getFormsByApplication(application);

		for (Iterator<Form> iterator = col.iterator(); iterator.hasNext();) {
			Form form = iterator.next();
			if(Form.FORM_TYPE_SEARCHFORM != form.getType() && Form.FORM_TYPE_TEMPLATEFORM != form.getType() && Form.FORM_TYPE_FRAGMENT != form.getType()){
				map.put(form.getId(), form.getName());
			}
		}
		PersistenceUtils.closeSession();
		return map;
	}
	
	/**
	 * 通过表单Id获取所在模块的Id
	 * 
	 * @return ModuleId
	 * 
	 * @throws Exception
	 */
	public String getModuleById(String formId) throws Exception {
		FormProcess process = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) process.doView(formId);
		
		if(form != null){
			return form.getModule().getId();
		}
		return null;
	}
	
	/**
	 * 通过参数传入的文件路径和文件名判断此文件是否存在与之相关的SWF格式文件(用于在线预览)
	 * @param path
	 * 		文件路径
	 * @param fileName
	 * 		文件名
	 * @return
	 * 		如果存在SWF格式文件返回true 否则返回false 
	 */
	public boolean hasSwfFile(String path,String fileName){
		String fullPath = Environment.getInstance().getRealPath(path);
		
		String swfFullPath = fullPath.substring(0, fullPath.lastIndexOf("/"))+File.separator+FileUtils.SWF_PATH+File.separator;
		swfFullPath += fileName.substring(0,fileName.lastIndexOf("."))+".swf";
		if(new File(swfFullPath).exists()) return true;
		
		return false;
	}
	

	/**
	 * 表单字段是否为有真实值的控件 是返回true， 否则false。
	 * 
	 @param formid
	 * @return
	 * @throws Exception
	 * */
	public String isHasTrueValue(String formid,String value) throws Exception {
		String isHasTrue="false";
		FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) fp.doView(formid);
		if (form != null) {
			Collection<FormField> colls = form.getAllFields();
			for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
				FormField field = (FormField) iter.next();
				if ((field instanceof RadioField
						||field instanceof CheckboxField
						||field instanceof SelectField
						||field instanceof UserField
						||field instanceof DepartmentField
						||field instanceof TreeDepartmentField) && field.getName().equals(value)){
					isHasTrue = "true";
					}				
		        }
			}
		return isHasTrue;
		}
}
