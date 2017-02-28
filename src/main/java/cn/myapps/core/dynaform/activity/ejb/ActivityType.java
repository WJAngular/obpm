package cn.myapps.core.dynaform.activity.ejb;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.activity.ejb.type.WorkFlowProcess;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public abstract class ActivityType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3210615361376221094L;

	public static final Logger log = Logger.getLogger(ActivityType.class);

	public ActivityType(Activity act) {
		this.act = act;
	}

	/**
	 * 按钮类型为查询Document.
	 */
	public static final int DOCUMENT_QUERY = 1;

	/**
	 * 按钮类型为创建Document.
	 */
	public static final int DOCUMENT_CREATE = 2;

	/**
	 * 按钮类型为删除Document.
	 */
	public static final int DOCUMENT_DELETE = 3;

	/**
	 * 按钮类型为更新Document.
	 */
	public static final int DOCUMENT_UPDATE = 34;

	/**
	 * 按钮类型为流程处理
	 */
	public static final int WORKFLOW_PROCESS = 5;

	/**
	 * 按钮类型为SCRIPT处理
	 */
	public static final int SCRIPT_PROCESS = 6;

	/**
	 * 按钮类型为修改Document
	 */
	public static final int DOCUMENT_MODIFY = 7;

	/**
	 * 按钮类型为关闭窗口
	 */
	public static final int CLOSE_WINDOW = 8;

	/**
	 * 按钮类型为保存并关闭窗口
	 */
	public static final int SAVE_CLOSE_WINDOW = 9;

	/**
	 * 按钮类型为返回
	 */
	public static final int DOCUMENT_BACK = 10;

	/**
	 * 按钮类型为保存并返回
	 */
	public static final int SAVE_BACK = 11;

	/**
	 * 按钮类型保存并新建(新建有一条有旧数据Document)
	 */
	public static final int SAVE_NEW_WITH_OLD = 12;

	/**
	 * 没任何操作
	 */
	public static final int NOTHING = 13;

	/**
	 * 按钮类型为打印
	 */
	public static final int PRINT = 14;

	/**
	 * 按钮类型为连同流程图一起打印
	 */
	public static final int PRINT_WITHFLOWHIS = 15;

	/**
	 * 按钮类型为导出Excel
	 */
	public static final int EXPTOEXCEL = 16;

	/**
	 * 按钮类型为保存并新建(新建一条空Document)
	 */
	public static final int SAVE_NEW_WITHOUT_OLD = 17;

	/**
	 * 按钮类型为清掉所有这个form的数据
	 */

	public static final int CLEAR_ALL = 18;

	/**
	 * 按钮类型为保存但不执行校验
	 */
	public static final int SAVE_WITHOUT_VALIDATE = 19;

	/**
	 * 按钮类型为批量审批按钮
	 */
	public static final int BATCH_APPROVE = 20;

	/**
	 * 按钮类型为复制按钮
	 */
	public static final int DOCUMENT_COPY = 21;

	/**
	 * 按钮类型为查看流程图
	 */
	public static final int DOCUEMNT_VIEWFLOWIMAGE = 22;

	/**
	 * 按钮类型为PDF导出
	 */
	public static final int EXPTOPDF = 25;

	/**
	 * 按钮类型为文件下载
	 */
	public static final int FILE_DOWNLOAD = 26;

	/**
	 * 按钮类型为 Excel导入
	 */
	public static final int EXCEL_IMPORT = 27;
	/**
	 * 按钮类型为 电子签章
	 */
	public static final int SIGNATURE = 28;
	/**
	 * 按钮类型为 批量电子签章
	 */
	public static final int BATCHSIGNATURE = 29;

	/**
	 * 按钮类型为FLEX打印
	 */
	public static final int FLEX_PRINT = 30;

	/**
	 * 按钮类型为FLEX带流程历史打印
	 */
	public static final int FLEX_PRINT_WITHFLOWHIS = 31;

	/**
	 * 按钮类型为跳转操作
	 */
	public static final int JUMP = 32;

	/**
	 * start workflow Button; 2010-9-26
	 */
	public static final int START_WORKFLOW = 33;
	/*
	 * save and start workflow button
	 */
	public static final int SAVE_SARTWORKFLOW = 4;

	/**
	 * 按钮类型为视图打印
	 */
	public static final int PRINT_VIEW = 36;

	/**
	 * 通过邮件转发按钮
	 */
	public static final int EMAIL_TRANSPOND = 37;

	/**
	 * 按钮类型为Dispatcher
	 */
	public static final int DISPATCHER = 39;
	
	/**
	 * 保存并新建
	 */
	public static final int SAVE_NEW = 42;

	/**
	 * 跳转
	 */
	public static final int JUMP_TO = 43;

	/**
	 * 按钮类型-归档
	 */
	public static final int ARCHIVE = 45;

	private static final Map<String, String> formActivityTypeMap;

	private static final Map<String, String> viewActivityTypeMap;

	static {
		formActivityTypeMap = new LinkedHashMap<String, String>();
		formActivityTypeMap.put(NOTHING + "",
				"cn.myapps.core.dynaform.activity.type.nothing");
		// formActivityTypeMap.put(DOCUMENT_QUERY + "", "Query");
		formActivityTypeMap.put(DOCUMENT_UPDATE + "",
				"cn.myapps.core.dynaform.activity.type.save");
		formActivityTypeMap.put(SAVE_SARTWORKFLOW + "",
				"cn.myapps.core.dynaform.activity.type.save_and_start_flow");
		formActivityTypeMap.put(SAVE_BACK + "", "Save_Back");
		formActivityTypeMap.put(SAVE_NEW_WITH_OLD + "", "Save_New_With_Old");
		formActivityTypeMap.put(SAVE_NEW_WITHOUT_OLD + "",
				"Save_New_WithOut_Old");
		formActivityTypeMap.put(SAVE_NEW + "", "Save&New");
		formActivityTypeMap.put(SAVE_WITHOUT_VALIDATE + "",
				"Save_WithOut_Validate");
		formActivityTypeMap.put(DOCUMENT_COPY + "", "Save_Copy");
		formActivityTypeMap.put(WORKFLOW_PROCESS + "",
				"cn.myapps.core.dynaform.activity.type.flow_processing");
		formActivityTypeMap.put(START_WORKFLOW + "",
				"cn.myapps.core.dynaform.activity.type.start_flow");
		formActivityTypeMap.put(DOCUMENT_BACK + "", "Back");
		formActivityTypeMap.put(SAVE_CLOSE_WINDOW + "", "Save_Close_Window");
		formActivityTypeMap.put(CLOSE_WINDOW + "", "Close_Window");
		formActivityTypeMap.put(PRINT + "",
				"cn.myapps.core.dynaform.activity.print");
		formActivityTypeMap.put(PRINT_WITHFLOWHIS + "", "Print_With_FlowHis");
		formActivityTypeMap.put(FLEX_PRINT + "",
				"cn.myapps.core.dynaform.activity.type.flexPrint");

		formActivityTypeMap.put(ARCHIVE + "",
				"cn.myapps.core.dynaform.activity.archive.name");
		formActivityTypeMap.put(EXPTOPDF + "",
				"cn.myapps.core.dynaform.activity.type.pdfExport");
		formActivityTypeMap.put(FILE_DOWNLOAD + "", "File_Download");
		formActivityTypeMap.put(SIGNATURE + "",
				"cn.myapps.core.dynaform.activity.type.signature");

		formActivityTypeMap.put(JUMP + "",
				"cn.myapps.core.dynaform.activity.type.jump");
		formActivityTypeMap.put(EMAIL_TRANSPOND + "", "core.email.transport");
		formActivityTypeMap.put(DISPATCHER + "", "Dispatcher");
		formActivityTypeMap.put(JUMP_TO + "",
				"cn.myapps.core.dynaform.activity.type.jump");

		viewActivityTypeMap = new LinkedHashMap<String, String>();
		viewActivityTypeMap.put(DOCUMENT_CREATE + "", "Create");
		viewActivityTypeMap.put(DOCUMENT_DELETE + "", "Delete");
		viewActivityTypeMap.put(CLEAR_ALL + "", "Clear_All_Datas");
		viewActivityTypeMap.put(DOCUMENT_QUERY + "",
				"cn.myapps.core.dynaform.activity.type.document_query");
		viewActivityTypeMap.put(BATCH_APPROVE + "", "Batch_Approve");
		viewActivityTypeMap.put(BATCHSIGNATURE + "",
				"cn.myapps.core.dynaform.activity.type.batchSignature");
		viewActivityTypeMap.put(EXPTOEXCEL + "",
				"cn.myapps.core.dynaform.activity.type.excelExport");
		viewActivityTypeMap.put(EXCEL_IMPORT + "",
				"cn.myapps.core.dynaform.activity.type.excelImport");
		viewActivityTypeMap.put(FILE_DOWNLOAD + "", "File_Download");
		viewActivityTypeMap.put(PRINT_VIEW + "",
				"cn.myapps.core.dynaform.activity.print");
		viewActivityTypeMap.put(DISPATCHER + "", "Dispatcher");
		viewActivityTypeMap.put(JUMP_TO + "",
				"cn.myapps.core.dynaform.activity.type.jump");
	}

	// View
	protected final static String VIEW_NAMESPACE = "/portal/dynaform/view";

	protected final static String VIEW_JSP_NAMESPACE = "/portal/dispatch/dynaform/view";

	protected final static String VIEW_SHARE_JSP_NAMESPACE = "/portal/share/dynaform/view";

	protected final static String VIEW_BUTTON_ID = "button_act";

	protected final static String VIEW_BUTTON_CLASS = "button-dis";

	// Document
	protected final static String DOCUMENT_NAMESPACE = "/portal/dynaform/document";

	protected final static String ACTIVITY_RUNTIME_NAMESPACE = "/portal/dynaform/activity";

	protected final static String DOCUMENT_JSP_NAMESPACE = "/portal/dispatch/dynaform/document";

	// Document共用jsp的namespace
	protected final static String DOCUMENT_SHARE_JSP_NAMESPACE = "/portal/share/dynaform/document";

	protected final static String DOCUMENT_BUTTON_ID = "button_act";

	protected final static String DOCUMENT_BUTTON_CLASS = "button-document";

	// Icon
	protected final static String ICON_BUTTON_CLASS = "button-image";

	protected final static String DOCUMENT_BUTTON_ON_CLASS = "button-onchange";

	protected final static String ACTIVITY_NAMESPACE = "/portal/dynaform/activity";

	protected final static String ACTIVITY_JSP_NAMESPACE = "/portal/dispatch/dynaform/activity";

	protected final static String ACTIVITY_SHARE_JSP_NAMESPACE = "/portal/share/dynaform/activity";

	protected final static String BASE_ACTION = "/";

	// Dispatcher
	protected final static String DISPATCHER_SHARE_JSP_NAMESPACE = "/portal/share/dynaform/dispatcher";

	protected final static String ACTIVITY_EMAIL_NAMESPACE = "/portal/share/dynaform/view/activity";

	protected StringBuffer htmlBuilder;

	protected Activity act;

	protected Document doc;

	protected WebUser user;

	/**
	 * 获取按钮执行后脚本
	 * 
	 * @return
	 */
	public abstract String getAfterAction();

	/**
	 * 获取按钮执行前脚本
	 * 
	 * @return 脚本
	 */
	public abstract String getBeforeAction();

	/**
	 * 获取按钮执行返回脚本
	 * 
	 * @return 脚本
	 */
	public abstract String getBackAction();

	/**
	 * 获取发生FieldErrors级别的异常时跳转的页面URL
	 * 
	 * @return
	 */
	public String getFieldErrorsAction() {
		return getAfterAction();
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public WebUser getUser() {
		return user;
	}

	public void setUser(WebUser user) {
		this.user = user;
	}

	/**
	 * 返回重定义后的html
	 * 
	 * @param permissionType
	 * @return
	 */
	public String toHtml(int permissionType) {
		htmlBuilder = new StringBuffer();
		addDefaultButton(permissionType);
		return htmlBuilder.toString();
	}
	
	/**
	 * 返回重定义后的xml
	 * 
	 * @param permissionType
	 * @return
	 */
	public String toXml(int permissionType){
		htmlBuilder = new StringBuffer();
		
		// 生成Button XML
		htmlBuilder.append("<ACTION").append(" ACTIONID='").append(act.getId()).append("'");
		htmlBuilder.append(" TYPE='").append(act.getType()).append("'");	
		// 有多语言标签时，将代替name属性在前台显示。since:2.5sp4
		if (act.getMultiLanguageLabel() == null
				|| act.getMultiLanguageLabel().trim().length() <= 0) {
			htmlBuilder.append(" NAME='").append(act.getName()).append("'");
		} else {
			htmlBuilder.append(" NAME='").append(getInnerText()).append("'");
		}


		if (PermissionType.DISABLED == permissionType) {
			htmlBuilder.append(" READONLY='true'");
		}
		htmlBuilder.append(">");
		htmlBuilder.append("</ACTION>");
		return htmlBuilder.toString();
	}

	public String toButtonHtml(Document doc, IRunner runner, WebUser webUser,
			int permissionType) {
		this.doc = doc;
		htmlBuilder = new StringBuffer();
		addButton(getInnerText(), getOnClickFunction(), "button-cmd",
				permissionType);
		return htmlBuilder.toString();
	}

	public String toButtonHtml(int permissionType) {
		htmlBuilder = new StringBuffer();
		addButton(getInnerText(), getOnClickFunction(), "button-cmd",
				permissionType);
		return htmlBuilder.toString();
	}

	/**
	 * 按钮的基本函数,在onclick时调用
	 * 
	 * @return 函数的执行语句
	 */
	public abstract String getOnClickFunction();

	/**
	 * 获取按钮的标识
	 * 
	 * @return 按钮的标识
	 */
	public abstract String getButtonId();

	/**
	 * 获取按钮的默认样式
	 * 
	 * @return 默认样式
	 */
	public abstract String getDefaultClass();

	/**
	 * 获取点击按钮的默认样式
	 * 
	 * @return 默认样式
	 */
	public abstract String getDefaultOnClass();

	/**
	 * 执行操作按钮的动作
	 * 
	 * @param action
	 *            action对象
	 * @param doc
	 *            文档
	 * @param user
	 *            前台用户
	 * @param params
	 *            参数集
	 * @return 返回处理结果(success|input|error等)
	 * @throws Exception
	 */
	public abstract String doExecute(AbstractRunTimeAction action,
			Document doc, WebUser user, ParamsTable params) throws Exception;
	
	/**
	 * 执行按钮的业务操作
	 * @param action
	 * @param doc
	 * @param user
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String doProcess(AbstractRunTimeAction action,
			Document doc, WebUser user, ParamsTable params) throws Exception{
		return "none";
	}

	/**
	 * 移动客户端执行操作按钮的动作
	 * 
	 * @param doc
	 *            文档
	 * @param user
	 *            前台用户
	 * @param params
	 *            参数
	 * @throws Exception
	 */
	public abstract ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception;

	/**
	 * 获取按钮页面的样式
	 * 
	 * @return 样式
	 */
	public String getButtonClass() {
		if (!StringUtil.isBlank(act.getIconurl())) {
			return ICON_BUTTON_CLASS;
		} else {
			return getDefaultClass();
		}
	}

	/**
	 * 给按钮增加图片显示
	 * 
	 * @return 图片地址<img src=.......>按钮名</img>
	 */
	public String getInnerText() {
		StringBuffer textBuilder = new StringBuffer();
		try {
			if (!StringUtil.isBlank(act.getIconurl())) {

				textBuilder.append("<img ");
				textBuilder.append(" src='");
				String context = Environment.getInstance().getContext(
						act.getIconurl());
				textBuilder.append(context);
				textBuilder.append("'");
				textBuilder.append("/>");
			} else {
				String name = act.getName();
				// 有多语言标签时，将代替name属性在前台显示。since:2.5sp4
				if (act.getMultiLanguageLabel() != null
						&& act.getMultiLanguageLabel() != "") {
					name = act.getMultiLanguageLabel();
				}
				// if(name != null && name.length() > 4){
				// name = name.substring(0, 4) + "..";
				// }
				textBuilder.append("{*[" + name + "]*}");
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return textBuilder.toString();
	}

	protected void addDefaultButton() {
		addButton(getInnerText(), getOnClickFunction());
	}

	protected void addDefaultButton(int permissionType) {
		addButton(getInnerText(), getOnClickFunction(), getDefaultClass(),
				permissionType);
	}

	protected void addButton(String innerText, String function) {
		addButton(innerText, function, getDefaultClass(), PermissionType.MODIFY);
	}

	protected void addButton(String innerText, String function,
			String className, int permissionType) {

		String contextPath = Environment.getInstance().getContextPath();
		// TODO 注释掉 PortletActionContext.isRender()问题 稍后研究
		/*
		 * if (PortletActionContext.isRender()) { contextPath =
		 * PortletActionContext.getRenderRequest() .getContextPath(); } else if
		 * (PortletActionContext.isEvent()) { try { contextPath =
		 * PortletActionContext.getActionRequest() .getContextPath(); } catch
		 * (Exception e) { contextPath =
		 * Environment.getInstance().getContextPath(); } }
		 */
		String skinType = null;
		
		try {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			skinType = (String) session.getAttribute("SKINTYPE");
		} catch (Exception e) {
			// ignore
		}

		// 生成Button HTML
		htmlBuilder
				.append("<input moduleType='activityButton' type='hidden' ")
				.append(" actid='" + act.getId() + "'")
				.append(" applicationid='"+act.getApplicationid()+"'")
				.append(" icon='")
				.append(act.getIcon() != null
						&& act.getIcon().trim().length() > 0 ? act.getIcon()
						: act.getType()).append("'")
				.append(" name='" + getButtonId() + "'")
				.append(" class='activity' ")
				.append("activityType='" + act.getType() + "'");

		
		
		
		if(this instanceof WorkFlowProcess){
			htmlBuilder
				.append(" flowShowType='" + act.getFlowShowType() + "'");
		}
		
		// 有多语言标签时，将代替name属性在前台显示。since:2.5sp4
		if (act.getMultiLanguageLabel() == null
				|| act.getMultiLanguageLabel().trim().length() <= 0) {
			htmlBuilder.append(" title='" + act.getName() + "'");
			htmlBuilder.append(" value='" + act.getName() + "'");
		} else {
			htmlBuilder.append(" title='{*[" + act.getMultiLanguageLabel()
					+ "]*}'");
			htmlBuilder.append(" value='" + innerText + "'");
		}

		if (act.getIcon() != null && !act.getIcon().equals("")) {
			htmlBuilder.append(" iconType='custom'");
		} else {
			htmlBuilder.append(" iconType='default'");
		}
		if (PermissionType.MODIFY == permissionType) {
			htmlBuilder.append(" onclick=\"" + function + "\"");
		}
		if (PermissionType.DISABLED == permissionType) {
			htmlBuilder.append(" disabled='disabled'");
		}
		htmlBuilder.append(" style='display:none;' />");
	}

	protected String getButtonStyle() {
		// return "background-image: url(../../../resource/imgnew/act/act_" +
		// act.getType() + ".gif)";
		return "";
	}

	/**
	 * 获取点击按钮页面的样式
	 * 
	 * @return 页面的样式
	 */
	protected String getButtonOnClass() {
		if (!StringUtil.isBlank(act.getIconurl())) {
			return ICON_BUTTON_CLASS;
		} else {
			return getDefaultOnClass();
		}
	}

	public Activity getActivity() {
		return act;
	}

	/**
	 * 获取表单的按钮类型{code: name}映射
	 * 
	 * @return 表单按钮类型映射
	 */
	public static Map<String, String> getFormActivityTypeMap() {
		return formActivityTypeMap;
	}

	/**
	 * 获取表单的按钮类型{code: name}映射，加上多语言标签
	 * 
	 * @return 表单按钮类型映射
	 */
	public static Map<String, String> getFormActTypeMapWithMulti() {
		Map<String, String> rtn = new LinkedHashMap<String, String>();
		Map<String, String> formActMap = ActivityType.getFormActivityTypeMap();
		for (Iterator<?> iterator = formActMap.entrySet().iterator(); iterator
				.hasNext();) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			// 加上多语言标签
			rtn.put((String) entry.getKey(), "{*[" + entry.getValue() + "]*}");
		}

		return rtn;
	}

	/**
	 * 获取视图的按钮类型{code: name}映射
	 * 
	 * @return 视图按钮类型映射
	 */
	public static Map<String, String> getViewActivityTypeMap() {
		return viewActivityTypeMap;
	}

	/**
	 * 获取视图的按钮类型{code: name}映射，加上多语言标签
	 * 
	 * @return 视图按钮类型映射
	 */
	public static Map<String, String> getViewActTypeMapWithMulti() {
		Map<String, String> rtn = new LinkedHashMap<String, String>();
		Map<String, String> viewActMap = ActivityType.getViewActivityTypeMap();
		for (Iterator<?> iterator = viewActMap.entrySet().iterator(); iterator
				.hasNext();) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			// 加上多语言标签
			rtn.put((String) entry.getKey(), "{*[" + entry.getValue() + "]*}");
		}

		return rtn;
	}

	/**
	 * 获取所有的按钮类型{code: name}映射
	 * 
	 * @return 按钮类型映射
	 */
	public static Map<Object, String> getAllActivityTypeMap() {
		LinkedHashMap<Object, String> map = new LinkedHashMap<Object, String>();
		map.putAll(formActivityTypeMap);
		map.putAll(viewActivityTypeMap);

		return map;
	}
}
