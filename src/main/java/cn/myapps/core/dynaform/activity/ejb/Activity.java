package cn.myapps.core.dynaform.activity.ejb;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.activity.ejb.type.Archive;
import cn.myapps.core.dynaform.activity.ejb.type.BatchApprove;
import cn.myapps.core.dynaform.activity.ejb.type.BatchSignature;
import cn.myapps.core.dynaform.activity.ejb.type.ClearAll;
import cn.myapps.core.dynaform.activity.ejb.type.CloseWindow;
import cn.myapps.core.dynaform.activity.ejb.type.Copy;
import cn.myapps.core.dynaform.activity.ejb.type.Dispatcher;
import cn.myapps.core.dynaform.activity.ejb.type.DocumentBack;
import cn.myapps.core.dynaform.activity.ejb.type.DocumentCreate;
import cn.myapps.core.dynaform.activity.ejb.type.DocumentDelete;
import cn.myapps.core.dynaform.activity.ejb.type.DocumentQuery;
import cn.myapps.core.dynaform.activity.ejb.type.DocumentUpdate;
import cn.myapps.core.dynaform.activity.ejb.type.EmailTranspond;
import cn.myapps.core.dynaform.activity.ejb.type.ExcelImport;
import cn.myapps.core.dynaform.activity.ejb.type.ExportToExcel;
import cn.myapps.core.dynaform.activity.ejb.type.ExportToPdf;
import cn.myapps.core.dynaform.activity.ejb.type.FileDownload;
import cn.myapps.core.dynaform.activity.ejb.type.FlexPrint;
import cn.myapps.core.dynaform.activity.ejb.type.Jump;
import cn.myapps.core.dynaform.activity.ejb.type.JumpTo;
import cn.myapps.core.dynaform.activity.ejb.type.Nothing;
import cn.myapps.core.dynaform.activity.ejb.type.NullType;
import cn.myapps.core.dynaform.activity.ejb.type.Print;
import cn.myapps.core.dynaform.activity.ejb.type.PrintView;
import cn.myapps.core.dynaform.activity.ejb.type.PrintWithFlowHis;
import cn.myapps.core.dynaform.activity.ejb.type.SaveBack;
import cn.myapps.core.dynaform.activity.ejb.type.SaveCloseWindow;
import cn.myapps.core.dynaform.activity.ejb.type.SaveNew;
import cn.myapps.core.dynaform.activity.ejb.type.SaveNewWithOld;
import cn.myapps.core.dynaform.activity.ejb.type.SaveNewWithOutOld;
import cn.myapps.core.dynaform.activity.ejb.type.SaveStartWorkFlow;
import cn.myapps.core.dynaform.activity.ejb.type.SaveWithOutValidate;
import cn.myapps.core.dynaform.activity.ejb.type.Siganure;
import cn.myapps.core.dynaform.activity.ejb.type.StartWorkFlow;
import cn.myapps.core.dynaform.activity.ejb.type.WorkFlowProcess;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcessBean;
import cn.myapps.core.workflow.utility.ActivityPermission;
import cn.myapps.core.workflow.utility.ActivityPermissionList;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * @hibernate.class table="T_ACTIVITY"
 * @author Marky
 */
public class Activity extends ValueObject implements Comparable<Activity> {
	public final static Logger LOG = Logger.getLogger(Activity.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 3021001094085548730L;
	/**
	 * 按钮关联的表单标识
	 */
	public static final String ACTIVITY_BELONGTO_FORM = "form";
	/**
	 * 按钮关联的视图标识
	 */
	public static final String ACTIVITY_BELONGTO_VIEW = "view";
	
	/**
	 * 跳转打开类型，当前页
	 */
	public static final int JUMPOPENTYPE_CURRENTPAGE = 0;
	
	/**
	 * 跳转打开类型，弹出层
	 */
	public static final int JUMPOPENTYPE_POPLAYER = 1;
	
	/**
	 * 跳转打开类型，页签
	 */
	public static final int JUMPOPENTYPE_TAB = 2;
	
	/**
	 * 跳转打开类型，新窗口
	 */
	public static final int JUMPOPENTYPE_NEWPAGE = 3;
	
	/**
	 * 自定义按钮 ：执行后的操作类型   :无
	 */
	public static final int AFTER_ACTION_TYPE_NONE = 0 ; 
	/**
	 * 自定义按钮 ：执行后的操作类型   :返回 
	 */
	public static final int AFTER_ACTION_TYPE_BACK = 1 ; 
	/**
	 *  自定义按钮 ：执行后的操作类型   :关闭 
	 */
	public static final int AFTER_ACTION_TYPE_CLOSE = 2 ; 
	/**
	 * 自定义按钮 ：执行后的操作类型   :跳转
	 */
	public static final int AFTER_ACTION_TYPE_URL = 3 ; 
	
	
	private String id;

	private String name;

	private int type;

	private String onActionForm;

	private String onActionView;

	private String onActionFlow;

	private String onActionPrint;

	private String excelName;

	private String beforeActionScript;

	private String afterActionScript;

	private String hiddenScript;

	private String readonlyScript;

	/**
	 * 流程启动脚本
	 */
	private String startFlowScript;

	/**
	 * 编辑模式
	 */
	private int editMode;

	// 流程操作显示方式
	private String flowShowType;

	/**
	 * 文件名称脚本
	 */
	private String fileNameScript;

	private String iconurl;

	private String approveLimit;

	private int orderno; // 排序

	private String stateToShow; // 在某状态下显示此按钮

	private ActivityType actType;

	private String parentView;

	private String parentForm;

	private String impmappingconfigid;

	private int jumpType;

	private boolean expSub;
	
	private String icon;
	
	/**
	 * 禁止添加和删除流程节点(true or false)
	 */
	private boolean disableFlowNode;
	
	/**
	 * 仅允许修改流程节点操作人
	 */
	private boolean changeFlowOperator;
	
	/**
	 * 仅允许修改流程抄送人
	 */
	private boolean changeFlowCc;
	
	public boolean isDisableFlowNode() {
		return disableFlowNode;
	}
	
	private String multiLanguageLabel;//多语言标签
	
	/**
	 * Dispatcher的模式(0:转发，1:重定向)
	 */
	private int dispatcherMode;
	
	/**
	 * Dispatcher的Url
	 */
	private String dispatcherUrl;
	
	/**
	 * Dispatcher的参数
	 */
	private String dispatcherParams;
	
	/**
	 * 跳转按钮模式（0:跳转到动态表单  1：跳转到指定URL）
	 */
	private int jumpMode;
	
	/**
	 * 跳转按钮打开类型（0:当前页  1：弹出层  2：页签  3：新窗口）
	 */
	private int jumpActOpenType = JUMPOPENTYPE_CURRENTPAGE;
	
	/**
	 * 是否带旧数据
	 */
	private boolean withOld;
	
	private transient View view;
	
	/**
	 * 自定义按钮 ：执行后的操作类型 (0:无  1:返回  2:关闭 3:跳转)
	 */
	private int afterActionType;
	
	/**
	 * 自定义按钮 ：执行后的跳转动作类型的地址脚本 
	 */
	private String afterActionDispatcherUrlScript;
	
	public int getAfterActionType() {
		return afterActionType;
	}

	public void setAfterActionType(int afterActionType) {
		this.afterActionType = afterActionType;
	}

	public String getAfterActionDispatcherUrlScript() {
		return afterActionDispatcherUrlScript;
	}

	public void setAfterActionDispatcherUrlScript(
			String afterActionDispatcherUrlScript) {
		this.afterActionDispatcherUrlScript = afterActionDispatcherUrlScript;
	}

	public void setJumpMode(int jumpMode) {
		this.jumpMode = jumpMode;
	}

	public int getJumpMode() {
		return jumpMode;
	}

	public int getJumpActOpenType() {
		return jumpActOpenType;
	}

	public void setJumpActOpenType(int jumpActOpenType) {
		this.jumpActOpenType = jumpActOpenType;
	}

	public void setDisableFlowNode(boolean disableFlowNode) {
		this.disableFlowNode = disableFlowNode;
	}
	
	public int getDispatcherMode() {
		return dispatcherMode;
	}

	public void setDispatcherMode(int dispatcherMode) {
		this.dispatcherMode = dispatcherMode;
	}

	public boolean isChangeFlowOperator() {
		return changeFlowOperator;
	}

	public void setChangeFlowOperator(boolean changeFlowOperator) {
		this.changeFlowOperator = changeFlowOperator;
	}

	public boolean isChangeFlowCc() {
		return changeFlowCc;
	}

	public void setChangeFlowCc(boolean changeFlowCc) {
		this.changeFlowCc = changeFlowCc;
	}

	/**
	 * 右键菜单操作
	 */
	private boolean contextMenu;
	
	private boolean showInToolbar = true;
	
	/**
	 * 转发操作
	 */
	private String transpond;
	
	public String getTranspond() {
		return transpond;
	}

	public void setTranspond(String transpond) {
		this.transpond = transpond;
	}

	public boolean isContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(boolean contextMenu) {
		this.contextMenu = contextMenu;
	}

	public boolean isShowInToolbar() {
		return showInToolbar;
	}

	public void setShowInToolbar(boolean showInToolbar) {
		this.showInToolbar = showInToolbar;
	}

	public String getMultiLanguageLabel() {
		return multiLanguageLabel;
	}

	public void setMultiLanguageLabel(String multiLanguageLabel) {
		this.multiLanguageLabel = multiLanguageLabel;
	}

	public String getDispatcherUrl() {
		return dispatcherUrl;
	}

	public void setDispatcherUrl(String dispatcherUrl) {
		this.dispatcherUrl = dispatcherUrl;
	}

	public String getDispatcherParams() {
		return dispatcherParams;
	}

	public void setDispatcherParams(String dispatcherParams) {
		this.dispatcherParams = dispatcherParams;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isExpSub() {
		return expSub;
	}

	public void setExpSub(boolean expSub) {
		this.expSub = expSub;
	}

	public static final int JUMPTYPE_NEW = 0;
	
	/**
	 * 转发
	 */
	public static final int FORWARD = 0;
	
	/**
	 * 重定向
	 */
	public static final int REDIRECT = 1;
	
	/**
	 * 跳转到动态表单
	 */
	public static final int JUMP_TO_FORM = 0;
	
	/**
	 * 跳转到指定URL
	 */
	public static final int JUMP_TO_URL = 1;

	private String targetList;

	public int getJumpType() {
		return jumpType;
	}

	public void setJumpType(int jumpType) {
		this.jumpType = jumpType;
	}

	public String getTargetList() {
		return targetList;
	}

	public void setTargetList(String targetList) {
		this.targetList = targetList;
	}

	/**
	 * 获取按钮活动所关联的导入配置映射标识
	 * 
	 * @return 导入配置映射标识
	 */
	public String getImpmappingconfigid() {
		return impmappingconfigid;
	}

	/**
	 * 设置按钮活动所关联的导入配置映射标识
	 * 
	 * @param impmappingconfigid
	 *            配置映射标识
	 */
	public void setImpmappingconfigid(String impmappingconfigid) {
		this.impmappingconfigid = impmappingconfigid;
	}

	/**
	 * 获取排序号
	 * 
	 * @hibernate.property column="ORDERNO"
	 * @return 排序号
	 * @uml.property name="orderno"
	 */
	public int getOrderno() {
		return orderno;
	}

	/**
	 * 设置按钮排序号
	 * 
	 * @param orderno
	 *            排序
	 * @uml.property name="orderno"
	 */
	public void setOrderno(int orderno) {
		this.orderno = orderno;
	}

	/**
	 * 获取 按钮主键,主键为UUID,用来标识按钮的唯一性
	 * 
	 * @see cn.myapps.util.sequence.Sequence#getSequence()
	 * @hibernate.id column="ID" generator-class="assigned"
	 * @uml.property name="id"
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置按钮主键,主键为UUID,用来标识按钮的唯一性.
	 * 
	 * @param id
	 *            Acvitity主键
	 * @uml.property name="id"
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取按钮名字
	 * 
	 * @hibernate.property column="NAME"
	 * @return 按钮名字
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置按钮名字
	 * 
	 * @param name
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取执行按钮之前执行脚本
	 * 
	 * @hibernate.property column="BEFOREACTIONSCRIPT" type = "text"
	 * @return 执行按钮之前执行的脚本
	 */
	public String getBeforeActionScript() {
		return beforeActionScript;
	}

	/**
	 * 设置执行按钮之前执行脚本
	 * 
	 * @param beforeActionScript
	 *            执行按钮之前执行脚本
	 * @uml.property name="beforeActionScript"
	 */
	public void setBeforeActionScript(String beforeActionScript) {
		this.beforeActionScript = beforeActionScript;
	}

	/**
	 * 获取隐藏脚本
	 * 
	 * @return 隐藏脚本
	 * @hibernate.property column="HIDDENSCRIPT" type = "text"
	 * @uml.property name="hiddenScript"
	 */
	public String getHiddenScript() {
		return hiddenScript;
	}

	/**
	 * 设置隐藏脚本
	 * 
	 * @param hiddenScript
	 *            隐藏脚本
	 * @uml.property name="hiddenScript"
	 */
	public void setHiddenScript(String hiddenScript) {
		this.hiddenScript = hiddenScript;
	}

	/**
	 * 获取只读脚本
	 * 
	 * @return
	 */
	public String getReadonlyScript() {
		return readonlyScript;
	}

	/**
	 * 设置只读脚本
	 * 
	 * @param readonlyScript
	 */
	public void setReadonlyScript(String readonlyScript) {
		this.readonlyScript = readonlyScript;
	}

	/**
	 * 获取流程启动脚本
	 * 
	 * @return
	 */
	public String getStartFlowScript() {
		return startFlowScript;
	}

	/**
	 * 
	 * @param startFlowScript
	 */
	public void setStartFlowScript(String startFlowScript) {
		this.startFlowScript = startFlowScript;
	}

	/**
	 * 获取编辑模式
	 * 
	 * @return
	 */
	public int getEditMode() {
		return editMode;
	}

	/**
	 * 设置编辑模式
	 * 
	 * @param editMode
	 */
	public void setEditMode(int editMode) {
		this.editMode = editMode;
	}

	/**
	 * 获取按钮活动所关联的表单
	 * 
	 * @return FORM_ID 表单标识
	 * @hibernate.property column="ONACTIONFORM_ID"
	 */
	public String getOnActionForm() {
		return onActionForm;
	}

	/**
	 * 设置按钮活动所关联的表单
	 * 
	 * @param onActionForm
	 *            表单标识
	 */
	public void setOnActionForm(String onActionForm) {
		this.onActionForm = onActionForm;
	}

	/**
	 * 获取按钮活动所关联的视图
	 * 
	 * @return 视图标识
	 * @hibernate.property column="ONACTIONVIEW_ID"
	 */
	public String getOnActionView() {
		return onActionView;
	}

	/**
	 * 设置按钮活动所关联的视图
	 * 
	 * @param onActionView
	 *            视力标识
	 */
	public void setOnActionView(String onActionView) {
		this.onActionView = onActionView;
	}

	/**
	 * 获取按钮活动所关联的流程
	 * 
	 * @return 流程标识
	 * @hibernate.property column="ONACTIONFLOW_ID"
	 */
	public String getOnActionFlow() {
		return onActionFlow;
	}

	/**
	 * 设置按钮活动所关联的流程
	 * 
	 * @param onActionFlow
	 *            流程标识
	 */
	public void setOnActionFlow(String onActionFlow) {
		this.onActionFlow = onActionFlow;
	}

	/**
	 * 获取按钮活动所关联的动态打印
	 * 
	 * @return
	 */
	public String getOnActionPrint() {
		return onActionPrint;
	}

	/**
	 * 设置按钮活动所关联的动态打印
	 * 
	 * @param onActionPrint
	 */
	public void setOnActionPrint(String onActionPrint) {
		this.onActionPrint = onActionPrint;
	}

	/**
	 * 获取按钮活动所关联的EXCEL名称
	 * 
	 * @return EXCEL名称
	 * @hibernate.property column="EXCELNAME_ID"
	 */
	public String getExcelName() {
		return excelName;
	}

	/**
	 * 设置按钮活动所关联的EXCEL名称
	 * 
	 * @param excelName
	 *            EXCEL名称
	 */
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

	public boolean isWithOld() {
		return withOld;
	}

	public void setWithOld(boolean withOld) {
		this.withOld = withOld;
	}

	/**
	 * 获取Activity类型. 分别: 1:文档查询(DOCUMENT_QUERY)
	 * <p>
	 * 2:文档创建(DOCUMENT_CREATE) 3:文档删除(DOCUMENT_DELETE)
	 * <p>
	 * 4:文档更新(DOCUMENT_UPDATE)
	 * <p>
	 * 5:流程处理(WORKFLOW_PROCESS) 6:SCRIPT处理(SCRIPT_PROCESS)
	 * <p>
	 * 7:文档修改(DOCUMENT_MODIFY) 8:关闭窗口(CLOSE_WINDOW);
	 * <p>
	 * 9:保存并关闭窗口(SAVE_CLOSE_WINDOW) 10:回退(DOCUMENT_BACK);
	 * <p>
	 * 11:保存并返回(SAVE_BACK); 12:保存并新建(新建时还保留之前旧的内容)SAVE_NEW_WITH_OLD
	 * <p>
	 * 13:Nothing 14:打印(PRINT)
	 * <p>
	 * 15:与流程图一起打印 (PRINT_WITHFLOWHIS) 16:导出Excel(EXPTOEXCEL)
	 * <p>
	 * 17:保存并新建(新建时不保留之前旧的内容)SAVE_NEW_WITHOUT_OLD
	 * 
	 * @hibernate.property column="TYPE"
	 * @return int
	 * @uml.property name="type"
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置Activity类型. 分别: 1:文档查询(DOCUMENT_QUERY)
	 * <p>
	 * 2:文档创建(DOCUMENT_CREATE) 3:文档删除(DOCUMENT_DELETE)
	 * <p>
	 * 4:文档更新(DOCUMENT_UPDATE)
	 * <p>
	 * 5:流程处理(WORKFLOW_PROCESS) 6:SCRIPT处理(SCRIPT_PROCESS)
	 * <p>
	 * 7:文档修改(DOCUMENT_MODIFY) 8:关闭窗口(CLOSE_WINDOW);
	 * <p>
	 * 9:保存并关闭窗口(SAVE_CLOSE_WINDOW) 10:回退(DOCUMENT_BACK);
	 * <p>
	 * 11:保存并返回(SAVE_BACK); 12:保存并新建(新建时还保留之前旧的内容)SAVE_NEW_WITH_OLD
	 * <p>
	 * 13:Nothing 14:打印(PRINT)
	 * <p>
	 * 15:和流程一起打印 (PRINT_WITHFLOWHIS) 16:导出Excel(EXPTOEXCEL)
	 * <p>
	 * 17:保存并新建(新建时不保留之前旧的内容)SAVE_NEW_WITHOUT_OLD
	 * 
	 * @param type
	 *            Activity类型.
	 */
	public void setType(int type) {
		this.type = type;

		switch (type) {
		case ActivityType.BATCH_APPROVE:
			actType = new BatchApprove(this);
			break;
		case ActivityType.CLEAR_ALL:
			actType = new ClearAll(this);
			break;
		case ActivityType.CLOSE_WINDOW:
			actType = new CloseWindow(this);
			break;
		case ActivityType.DOCUMENT_CREATE:
			actType = new DocumentCreate(this);
			break;
		case ActivityType.DOCUMENT_BACK:
			actType = new DocumentBack(this);
			break;
		case ActivityType.DOCUMENT_DELETE:
			actType = new DocumentDelete(this);
			break;
		case ActivityType.DOCUMENT_MODIFY:
			actType = new NullType(this);
			break;
		case ActivityType.DOCUMENT_QUERY:
			actType = new DocumentQuery(this);
			break;
		case ActivityType.SAVE_SARTWORKFLOW:
			actType = new SaveStartWorkFlow(this);
			break;
		case ActivityType.DOCUMENT_UPDATE:
			actType = new DocumentUpdate(this);
			break;
		case ActivityType.EXPTOEXCEL:
			actType = new ExportToExcel(this);
			break;
		case ActivityType.NOTHING:
			actType = new Nothing(this);
			break;
		case ActivityType.PRINT:
			actType = new Print(this);
			break;
		case ActivityType.PRINT_WITHFLOWHIS:
			actType = new PrintWithFlowHis(this);
			break;
		case ActivityType.SAVE_BACK:
			actType = new SaveBack(this);
			break;
		case ActivityType.SAVE_CLOSE_WINDOW:
			actType = new SaveCloseWindow(this);
			break;
		case ActivityType.SAVE_NEW_WITH_OLD:
			actType = new SaveNewWithOld(this);
			break;
		case ActivityType.SAVE_NEW_WITHOUT_OLD:
			actType = new SaveNewWithOutOld(this);
			break;
		case ActivityType.SAVE_NEW:
			actType = new SaveNew(this);
			break;
		case ActivityType.SAVE_WITHOUT_VALIDATE:
			actType = new SaveWithOutValidate(this);
			break;
		case ActivityType.WORKFLOW_PROCESS:
			actType = new WorkFlowProcess(this);
			break;
		case ActivityType.DOCUMENT_COPY:
			actType = new Copy(this);
			break;
		case ActivityType.EXPTOPDF:
			actType = new ExportToPdf(this);
			break;
		case ActivityType.FILE_DOWNLOAD:
			actType = new FileDownload(this);
			break;
		case ActivityType.EXCEL_IMPORT:
			actType = new ExcelImport(this);
			break;
		case ActivityType.SIGNATURE:
			actType = new Siganure(this);
			break;
		case ActivityType.BATCHSIGNATURE:
			actType = new BatchSignature(this);
			break;
		case ActivityType.FLEX_PRINT:
			actType = new FlexPrint(this);
			break;
		case ActivityType.JUMP:
			actType = new Jump(this);
			break;
		case ActivityType.START_WORKFLOW:
			actType = new StartWorkFlow(this);
			break;
		case ActivityType.PRINT_VIEW:
			actType = new PrintView(this);
			break;
		case ActivityType.EMAIL_TRANSPOND:
			actType = new EmailTranspond(this);
			break;
		case ActivityType.DISPATCHER:
			actType = new Dispatcher(this);
			break;
		case ActivityType.JUMP_TO:
			actType = new JumpTo(this);
			break;
		case ActivityType.ARCHIVE:
			actType = new Archive(this);
			break;
		default:
			actType = new NullType(this);
			break;
		}
	}

	/**
	 * 图标地址url
	 * 
	 * @hibernate.property column="ICONURL"
	 */
	public String getIconurl() {
		return iconurl;
	}

	/**
	 * 设置图标URL
	 * 
	 * @param iconurl
	 */
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}

	/**
	 * 获取流程状态显示
	 * 
	 * @hibernate.property column="STATETOSHOW"
	 * @return 流程状态显示
	 * @uml.property name="stateToShow"
	 */
	public String getStateToShow() {
		return stateToShow;
	}

	/**
	 * 设置流程状态显示
	 * 
	 * @param stateToShow
	 *            流程状态显示
	 * @uml.property name="stateToShow"
	 */
	public void setStateToShow(String stateToShow) {
		this.stateToShow = stateToShow;
	}

	/**
	 * 根据文档与流程状态判断是否隐藏Activity
	 * 
	 * @param doc
	 * 
	 * @return true or false true为隐藏.
	 * @throws Exception
	 */
	public boolean isStateToHidden(Document doc) throws Exception {
		if (getStateToShow() != null && getStateToShow().trim().length() > 0) {
			String[] showStates = getStateToShow().split(",");
			for (int i = 0; i < showStates.length; i++) {
				Collection<String> labelList = doc.getStateLableList();
				if (labelList.size() > 0) {
					if (labelList.contains(showStates[i])) {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 判断按钮是否隐藏
	 * 
	 * @param runner
	 *            脚本运行器
	 * @param parent
	 *            按钮所属于的父元素
	 * @param doc
	 *            Document对象
	 * @return boolean true or false true为隐藏.
	 * @throws Exception
	 */
	public boolean isHidden(IRunner runner, ActivityParent parent, Document doc) throws Exception {
		boolean isHidden = false;

		if ((getHiddenScript()) != null && (getHiddenScript()).trim().length() > 0) {
			StringBuffer label = new StringBuffer();
			label.append(parent.getFullName()).append(".Activity(").append(getId()).append(")." + getName()).append(
					".runHiddenScript");
			Object result = runner.run(label.toString(), getHiddenScript());
			if (result != null && result instanceof Boolean) {
				isHidden = ((Boolean) result).booleanValue();
			}
		}

		if (getType() == ActivityType.WORKFLOW_PROCESS) {
			// 存在流程才显示流程相关按钮
			isHidden = isHidden || !StringUtil.isBlank(doc.getParentid()) || doc.getState() == null;
		}
		return isHidden || isStateToHidden(doc);
	}

	public boolean isHidden(IRunner runner, ActivityParent parent, Document doc, WebUser user, int resType)
			throws Exception {
		
		boolean isHidden = false;
		String parentFullName = parent.getFullName();

		// 1.隐藏脚本
		if ((getHiddenScript()) != null && (getHiddenScript()).trim().length() > 0) {
			StringBuffer label = new StringBuffer();
			label.append(parentFullName).append(".Activity(").append(getId()).append(")." + getName()).append(
					".runHiddenScript");
			Object result = runner.run(label.toString(), getHiddenScript());
			if (result != null && result instanceof Boolean) {
				isHidden = ((Boolean) result).booleanValue();
			}
		}

/**  流程处理按钮都是隐藏，但仅限于前端js不做显示渲染  ***		
		if (getType() == ActivityType.WORKFLOW_PROCESS ) {
			// 存在流程才显示流程相关按钮
			isHidden = isHidden || !StringUtil.isBlank(doc.getParentid()) || doc.getState() == null;
		}
		//如果流程被挂起,且流程处理按钮为非弹出层的时候,隐藏按钮
		if(getType() == ActivityType.WORKFLOW_PROCESS){
			NodeRT currNodeRT = StateMachine.getCurrUserNodeRT(doc, user,null);
			if(currNodeRT != null && currNodeRT.getState() == 1){
				if("ST02".equals(getFlowShowType())){
					//nothing to do
				}else {
					isHidden = true;
				}
			}
		}
*/
		if(!isHidden && getType() == ActivityType.ARCHIVE){
			FlowStateRTProcess stateProcess = new FlowStateRTProcessBean(doc.getApplicationid());
			FlowStateRT instance = (FlowStateRT) stateProcess.doView(doc.getStateid());
			if(instance == null){
				isHidden = true;
			}
			if(instance != null && (!instance.isComplete() || instance.isArchived())){
				isHidden = true;
			}
		}
		if (!isHidden && getType() == ActivityType.START_WORKFLOW) { // 启动流程
			if (!StringUtil.isBlank(doc.getParentid()))
				isHidden = true;
		}
		
		// 2.权限校验
		PermissionProcess process = (PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
		boolean isAllow = true;
		if(parent instanceof Form || parent instanceof View){
			String permissionType = "";
			if(parent instanceof Form){
				permissionType = ((Form)parent).getPermissionType();
			}else if(parent instanceof View){
				permissionType = ((View)parent).getPermissionType();
			}
			if(!Form.PERMISSION_TYPE_PUBLIC.equals(permissionType)){
				isAllow = process.check(user.getRolesByApplication(parent.getApplicationid()), parent.getId(), this.getId(),this.getType(),resType,parent.getApplicationid());
			}
		}
		
		//3.表单操作按钮权限校验
		boolean activityPerssiom = false; 
		ActivityPermissionList permissionList = doc.getActivityPermList(user);
		if(permissionList!=null){
			String permissionType = permissionList.checkPermission(this.getId());
			if( ActivityPermission.ACTIVITY_PERMESSION_HIDE.equalsIgnoreCase(permissionType)){
				activityPerssiom = true;
			}
		}
		
		return activityPerssiom || isHidden || isStateToHidden(doc) || !isAllow ;
	}

	/**
	 * 判断按钮是否只读
	 * 
	 * @param runner
	 *            脚本运行器
	 * @param parentFullName
	 *            按钮所属于的父元素
	 * @return boolean true or false true为只读.
	 * @throws Exception
	 */
	public boolean isReadonly(IRunner runner, String parentFullName) throws Exception {
		boolean isReadonly = false;
		if ((getReadonlyScript()) != null && (getReadonlyScript()).trim().length() > 0) {
			StringBuffer label = new StringBuffer();
			label.append(parentFullName).append(".Activity(").append(getId()).append(")." + getName()).append(
					".runReadonlyScript");
			Object result = runner.run(label.toString(), getReadonlyScript());
			if (result != null && result instanceof Boolean) {
				isReadonly = ((Boolean) result).booleanValue();
			}
		}

		return isReadonly;
	}

	/**
	 * 获取执行按钮动作之后的脚本
	 * 
	 * @hibernate.property column="AFTERACTIONSCRIPT" type = "text"
	 * @return 执行按钮动作之后的脚本
	 */
	public String getAfterActionScript() {
		return afterActionScript;
	}

	/**
	 * 设置执行按钮动作之后的脚本
	 * 
	 * @param afterActionScript
	 *            脚本
	 */
	public void setAfterActionScript(String afterActionScript) {
		this.afterActionScript = afterActionScript;
	}

	/**
	 * 获取审批限制, 当按钮类型为批量审批时, 限制下一步可提交的节点(可多选,以","分隔)
	 * 
	 * @hibernate.property column="APPROVELIMIT"
	 * @return 节点列表
	 */
	public String getApproveLimit() {
		return approveLimit;
	}

	/**
	 * 设置审批限制, 当按钮类型为批量审批时, 限制下一步可提交的节点(可多选,以","分隔)
	 * 
	 * @param approveLimit
	 *            审批限制
	 */
	public void setApproveLimit(String approveLimit) {
		this.approveLimit = approveLimit;
	}

	/**
	 * 将按钮以html的形式输出到页面,形成一个按钮
	 * 
	 * @return
	 */
	public String toHtml() {
		return toHtml(PermissionType.MODIFY);
	}

	public String toHtml(int permissionType) {
		return actType.toHtml(permissionType);
	}
	
	public String toXml(int permissionType) {
		return actType.toXml(permissionType);
	}
	
	public String toHtml(Document doc,WebUser user, int permissionType){
		actType.setDoc(doc);
		actType.setUser(user);
		return actType.toHtml(permissionType);
	}

	
	public String toButtonHtml(int permissionType) {
		return actType.toButtonHtml(permissionType);
	}
	
	public String toButtonHtml(Document doc, IRunner runner, WebUser webUser,int permissionType) {
		return actType.toButtonHtml(doc,runner,webUser,permissionType);
	}

	public String toButtonHtml() {
		return toButtonHtml(PermissionType.MODIFY);
	}

	/**
	 * 获取执行按钮回返脚本
	 * 
	 * @return 脚本
	 */
	public String getBackAction() {
		return actType.getBackAction();
	}

	/**
	 * 获取执行按钮执行前脚本
	 * 
	 * @return 脚本
	 */
	public String getActionUrl() {
		return actType.getBeforeAction();
	}

	/**
	 * 获取执行按钮执行后脚本
	 * 
	 * @return 脚本
	 */
	public String getAfterAction() {
		return actType.getAfterAction();
	}
	
	/**
	 * 获取发生FieldErrors级别的异常时跳转的页面URL
	 * @return
	 */
	public String getFieldErrorsAction(){
		return actType.getFieldErrorsAction();
	}

	/**
	 * 获取关联的视图标识
	 * 
	 * @return 视图标识
	 */
	public String getParentView() {
		return parentView;
	}

	/**
	 * 设置关联的视图标识
	 * 
	 * @param parentView
	 *            视图标识
	 */
	public void setParentView(String parentView) {
		this.parentView = parentView;
	}

	/**
	 * 获取关联的表单标识
	 * 
	 * @return 表单标识
	 */
	public String getParentForm() {
		return parentForm;
	}

	/**
	 * 设置关联的表单标识
	 * 
	 * @param parentForm
	 *            表单标识
	 */
	public void setParentForm(String parentForm) {
		this.parentForm = parentForm;
	}

	/**
	 * 根据orderno比较大小
	 */
	public int compareTo(Activity activity) {
		if (activity != null) {
			int thisOrderno = this.orderno;
			int otherOrderno = activity.orderno;
			return (thisOrderno - otherOrderno);
			// return (this.orderno - ((Activity) o).orderno);
		}
		return -1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		/*
		 * Activity that = (Activity) obj;
		 * 
		 * if (this.getName() != null ? !this.getName().equals(that.getName()) :
		 * that.getName() != null) return false; if (this.getActionUrl() != null
		 * ? !this.getActionUrl().equals(that.getActionUrl()) :
		 * that.getActionUrl() != null) return false; if (this.getAfterAction()
		 * != null ? !this.getAfterAction().equals(that.getAfterAction()) :
		 * that.getAfterAction() != null) return false; if
		 * (this.getAfterActionScript() != null ?
		 * !this.getAfterActionScript().equals(that.getAfterActionScript()) :
		 * that.getAfterActionScript() != null) return false; if
		 * (this.getApplicationid() != null ?
		 * !this.getApplicationid().equals(that.getApplicationid()) :
		 * that.getApplicationid() != null) return false; if
		 * (this.getApproveLimit() != null ?
		 * !this.getApproveLimit().equals(that.getApproveLimit()) :
		 * that.getApproveLimit() != null) return false; if
		 * (this.getBackAction() != null ?
		 * !this.getBackAction().equals(that.getBackAction()) :
		 * that.getBackAction() != null) return false; if
		 * (this.getBeforeActionScript() != null ?
		 * !this.getBeforeActionScript().equals(that.getBeforeActionScript()) :
		 * that.getBeforeActionScript() != null) return false; if
		 * (this.getDomainid() != null ?
		 * !this.getDomainid().equals(that.getDomainid()) : that.getDomainid()
		 * != null) return false;
		 */
		super.equals(obj);
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * 获取文件名称脚本
	 * 
	 * @return 文件名
	 */
	public String getFileNameScript() {
		return fileNameScript;
	}

	/**
	 * 设置文件名称脚本
	 * 
	 * @param fileNameScript
	 *            文件名称脚本
	 */
	public void setFileNameScript(String fileNameScript) {
		this.fileNameScript = fileNameScript;
	}

	public String getFlowShowType() {
		return flowShowType;
	}

	public void setFlowShowType(String flowShowType) {
		this.flowShowType = flowShowType;
	}

	public String getParentFullName() {
		try {
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			if (!StringUtil.isBlank(getParentForm())) {
				Form form = (Form) formProcess.doView(getParentForm());
				return form.getFullName();
			} else if (!StringUtil.isBlank(getParentView())) {
				View view = (View) viewProcess.doView(getParentView());
				return view.getFullName();
			}
		} catch (Exception e) {
			LOG.warn("getParentFullName", e);
		}

		return "";

	}

	public ActivityParent getParent() throws Exception {
		if(view !=null) return view;
		String viewid = getParentView();
		String formid = getParentForm();

		if (!StringUtil.isBlank(viewid)) {
			ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			view =  (View) viewProcess.doView(viewid);
			return view;
		} else if (!StringUtil.isBlank(formid)) {
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			return (ActivityParent) formProcess.doView(formid);
		}
		
		return null;

	}
	
	public void setView(View view){
		this.view = view;
	}
	
	/**
	 * 执行动作
	 */
	public String execute(AbstractRunTimeAction action,Document doc,WebUser user,ParamsTable params) throws Exception {
		try {
			return actType.doExecute(action, doc, user, params);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/**
	 * 触发按钮执行业务操作
	 */
	public String process(AbstractRunTimeAction action,Document doc,WebUser user,ParamsTable params) throws Exception {
		try {
			return actType.doProcess(action, doc, user, params);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/**
	 * 手机客户端执行动作
	 */
	public ValueObject mbExecte(WebUser user,ParamsTable params) throws Exception{
		try {
			return actType.doMbExecte(user, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	/**
	 * 运行执行前脚本
	 * @param doc
	 * 		文档
	 * @param params
	 * 		参数表
	 * @param user
	 * 		用户
	 * @return
	 * 		运算结果
	 * @throws Throwable
	 */
	public Object runBeforeActionScript(Document doc,ParamsTable params,WebUser user) throws Throwable{
		Object result = null;
		try {
			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), params.getParameterAsString("application"));
			runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
			StringBuffer label = new StringBuffer();
			label.append("FormName:"+doc.getFormname()+" Activity(").append(this.getId()).append(")." + this.getName()).append(".beforeActionScript");
			result = runner.run(label.toString(), this.getBeforeActionScript());
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBPMRuntimeException(e);
		}
		
		return result;
		
	}
	
	/**
	 * 运行执行后脚本
	 * @param doc
	 * 		文档
	 * @param params
	 * 		参数表
	 * @param user
	 * 		用户
	 * @return
	 * 		运算结果
	 * @throws Throwable
	 */
	public Object runAfterActionScript(Document doc,ParamsTable params,WebUser user) throws Throwable{
		Object result = null;
		try {
			if(doc !=null && (this.getType() == ActivityType.PRINT || this.getType() == ActivityType.PRINT_WITHFLOWHIS || this.getType() == ActivityType.FLEX_PRINT)){
				DocumentProcess process = (DocumentProcess)ProcessFactory.createRuntimeProcess(DocumentProcess.class,params.getParameterAsString("application"));
				doc = (Document) process.doView(doc.getId());
			}
			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), params.getParameterAsString("application"));
			runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
			StringBuffer label = new StringBuffer();
			label.append("FormName:"+doc.getFormname()+" Activity(").append(this.getId()).append(")." + this.getName()).append(".afterActionScript");
			result = runner.run(label.toString(), this.getAfterActionScript());
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBPMRuntimeException(e);
		}
		
		return result;
		
	}
	
	/**
	 * 运行开启流程脚本
	 * @param doc
	 * 		文档
	 * @param params
	 * 		参数表
	 * @param user
	 * 		用户
	 * @return
	 * 		运算结果
	 * @throws Throwable
	 */
	public Object runStartFlowScript(Document doc,ParamsTable params,WebUser user) throws Throwable{
		Object result = null;
		try {
			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), params.getParameterAsString("application"));
			runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
			StringBuffer label = new StringBuffer();
			label.append("Activity(").append(this.getId()).append(")." + this.getName()).append(".startFlowScript");
			result = runner.run(label.toString(), this.getStartFlowScript());
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBPMRuntimeException(e);
		}
		
		return result;
		
	}
	
	public void setActvityTypeDoc(Document doc){
		this.actType.setDoc(doc);
	}
	
}
