package cn.myapps.core.dynaform.view.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.VersionSupport;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.Options;
import cn.myapps.core.dynaform.view.ejb.editmode.DQLEditMode;
import cn.myapps.core.dynaform.view.ejb.editmode.DesignEditMode;
import cn.myapps.core.dynaform.view.ejb.editmode.NullEditMode;
import cn.myapps.core.dynaform.view.ejb.editmode.ProcedureEditMode;
import cn.myapps.core.dynaform.view.ejb.editmode.SQLEditMode;
import cn.myapps.core.dynaform.view.ejb.type.CalendarType;
import cn.myapps.core.dynaform.view.ejb.type.CollapsibleType;
import cn.myapps.core.dynaform.view.ejb.type.GanttType;
import cn.myapps.core.dynaform.view.ejb.type.MapType;
import cn.myapps.core.dynaform.view.ejb.type.NormalType;
import cn.myapps.core.dynaform.view.ejb.type.NullType;
import cn.myapps.core.dynaform.view.ejb.type.TreeType;
import cn.myapps.core.links.ejb.LinkProcess;
import cn.myapps.core.links.ejb.LinkVO;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.style.repository.ejb.StyleRepositoryVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.xml.XmlUtil;

/**
 * @hibernate.class table="T_VIEW"
 * @author nicholas
 */
public class View extends VersionSupport implements ActivityParent {
	private static final Logger LOG = Logger.getLogger(View.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -3484102375494272629L;
	/**
	 * 窗口打开类型 为普通模式打开新的窗口
	 */
	public static final int OPEN_TYPE_NORMAL = 0x0000001;
	/**
	 * 窗口打开类型 弹出框显示
	 * @deprecated
	 */
	public static final int OPEN_TYPE_POP = 0x0000010;
	/**
	 * 窗口打开类型 在父窗口区域显示
	 */
	public static final int OPEN_TYPE_PARENT = 0x0000100;
	/**
	 * 窗口打开类型 当前区域显示
	 * @deprecated
	 */
	public static final int OPEN_TYPE_OWN = 0x0000110;
	/**
	 * 窗口打开类型 网格显示
	 */
	public static final int OPEN_TYPE_GRID = 0x0000120;
	/**
	 * 窗口打开类型 弹出层显示
	 */
	public static final int OPEN_TYPE_DIV = 0x0000115;
	/**
	 * 视图编辑模式 为设计的编辑模式
	 */
	public final static String EDIT_MODE_DESIGN = "00";
	/**
	 * 视图编辑模式 为DQL语句的编辑模式
	 */
	public final static String EDIT_MODE_CODE_DQL = "01";
	/**
	 * 视图编辑模式 为SQL语句的编辑模式
	 */
	public final static String EDIT_MODE_CODE_SQL = "02";
	/**
	 * 视图编辑模式为存储过程的编辑模式 2.6版本新增调用存储过程的编辑模式常量
	 */
	public final static String EDIT_MODE_CODE_PROCEDURE = "03";
	/**
	 * 鼠标移动的类样式
	 */
	protected final static String ONMOUSEOVER_CLASS = "table-tr-onchange";
	/**
	 * 鼠标移动的类样式
	 */
	protected final static String ONMOUSEOUT_CLASS = "table-tr";
	
	/**
	 * 数据来源表单呈现方式
	 */
	public static final String DISPLAY_TYPE_RELATEDFORM = "relatedForm";
	
	/**
	 * 模板表单呈现方式
	 */
	public static final String DISPLAY_TYPE_TEMPLATEFORM = "templateForm";

	/**
	 * 普通视图类型常量
	 */
	public static final int VIEW_TYPE_NORMAL = 0x0000001;

	/**
	 * 日历视图类型常量
	 */

	public static final int VIEW_TYPE_CALENDAR = 0x0000010;

	/**
	 * 树形视图类型常量
	 */

	public static final int VIEW_TYPE_TREE = 0x0000011;

	/**
	 * 地图视图类型常量
	 */
	public static final int VIEW_TYPE_MAP = 0x0000012;

	/**
	 * 甘特视图类型常量
	 */
	public static final int VIEW_TYPE_GANTT = 0x0000013;
	
	/**
	 * 折叠视图类型常量collapsible
	 */
	public static final int VIEW_TYPE_COLLAPSIBLE = 0x0000014;
	

	/**
	 * 树节点链接(当前视图)
	 */
	public static final String TREENODE_HREF_VIEW = "VIEW";

	/**
	 * 树节点链接(当前关联表单)
	 */
	public static final String TREENODE_HREF_FORM = "FORM";

	/**
	 * 树节点链接(根据定义链接生成)
	 */
	public static final String TREENODE_HREF_LINK = "LINK";
	
	/**
	 * 权限字段-作者
	 */
	public static final String AUTHFIELD_AUTHOR = "author";
	
	/**
	 * 权限字段-作者默认部门
	 */
	public static final String AUTHFIELD_AUTHOR_DEFAULT_DEPT = "authorDefaultDept";
	
	/**
	 * 权限字段-处理人
	 */
	public static final String AUTHFIELD_AUDITOR = "auditor";
	
	/**
	 * 权限字段-处理过
	 */
	public static final String AUTHFIELD_PROCESSED = "processed";
	
	/**
	 * 作者自身
	 */
	public static final String AUTHFIELD_SCOPE_ITSELF = "itself";
	
	/**
	 * 直属上级用户
	 */
	public static final String AUTHFIELD_SCOPE_AUTHOR_SUPERIOR = "superior";
	
	/**
	 * 所有上级用户
	 */
	public static final String AUTHFIELD_SCOPE_AUTHOR_ALL_SUPERIOR = "allSuperior";
	
	/**
	 * 直属下级用户
	 */
	public static final String AUTHFIELD_SCOPE_AUTHOR_LOWER = "lower";
	
	/**
	 * 所有下级用户
	 */
	public static final String AUTHFIELD_SCOPE_AUTHOR_ALL_LOWER = "allLower";
	
	/**
	 * 本级默认部门
	 */
	public static final String AUTHFIELD_SCOPE_AUTHOR_DEPT_DEFAULT = "default";
	
	/**
	 * 直属上级部门
	 */
	public static final String AUTHFIELD_SCOPE_AUTHOR_DEPT_LINE_SUPERIOR = "lineSuperior";

	/**
	 * 直属下级部门
	 */
	public static final String AUTHFIELD_SCOPE_AUTHOR_DEPT_LINE_LOWER = "lineLower";
	/**
	 * 所有上级部门
	 */
	public static final String AUTHFIELD_SCOPE_AUTHOR_DEPT_ALL_SUPERIOR = "allSuperior";

	/**
	 * 所有下级部门
	 */
	public static final String AUTHFIELD_SCOPE_AUTHOR_DEPT_ALL_LOWER = "allLower";
	
	/**
	 * 手机视图类型:默认视图
	 */
	public static final String MOBILENORMALVIEW = "00";
	
	/**
	 * 手机视图类型:表格视图
	 */
	public static final String MOBILETABLEVIEW = "01";
	
	/*授权方式-私有*/
	public static final String PERMISSION_TYPE_PRIVATE = "private";
	/*授权方式-公共*/
	public static final String PERMISSION_TYPE_PUBLIC = "public";
	
	
	/**
	 * 标识
	 */
	private String id;
	/**
	 * 视图名
	 */
	private String name;

	/**
	 * 列表描述
	 * 
	 * @uml.property name="description"
	 */
	private String description;
	/**
	 * DQL过滤条件
	 */
	private String filterScript;
	/**
	 * SQL过滤条件
	 */
	private String sqlFilterScript;
	/**
	 * procedure过滤条件(存储过程)
	 * 
	 * 2.6版本新增调用存储过程的脚本字段
	 */
	private String procedureFilterScript;
	/**
	 * 列集合
	 */
	private Set<Column> columns;
	/**
	 * 查询表单
	 */
	private Form searchForm;
	/**
	 * 按钮集合
	 */
	private Set<Activity> activitys;
	/**
	 * 关联的模块
	 */
	private ModuleVO module;
	/**
	 * 关联样式
	 */
	public StyleRepositoryVO style;
	/**
	 * 关联菜单
	 */
	private String relatedResourceid;
	/**
	 * 打开类型
	 */
	private int openType;
	/**
	 * 是否分页
	 */
	private boolean pagination;
	/**
	 * 每页显示记录
	 */
	private String pagelines;
	/**
	 * Filter Script的编辑模式
	 */
	private String editMode = "00"; // Filter Script的编辑模式
	/**
	 * 当为设计模式时的过滤条件
	 */
	private String filterCondition;
	/**
	 * 常用过滤条件
	 */
	private String commonFilterCondition ;
	/**
	 * 是否显示总计数
	 */
	private boolean showTotalRow;
	/**
	 * 关联表单,当为设计模式时,关联表单必填
	 */
	private String relatedForm;
	
	/**
	 * 模板表单 用于以特定的模板展现数据
	 */
	private String templateForm;

	 /**
	 * 排序的Field
	 */
	 private String orderField; // 排序的Field
	 /**
	 * 排序类型 ASC|DESC
	 */
	 private String orderType; // 排序类型 ASC|DESC
		

	/**
	 * 是否刷新
	 */
	private boolean refresh;
	/**
	 * 视图的最后修改时间
	 */
	private Date lastmodifytime; // 最后修改时间

	/**
	 * 用户权限字段,为视图为设计时使用
	 */
	private String auth_user;
	/**
	 * 角色权限字段,为视图为设计时使用
	 */
	private String auth_role;
	/**
	 * 权限字段,为视图为设计时使用
	 */
	private String auth_fields;
	
	/**
	 * 权限字段允许范围,为视图为设计时使用
	 */
	private String authFieldScope;
	/**
	 * 部门权限字段,为视图为设计时使用
	 */
	private String departments;
	/**
	 * 保存按钮的xml
	 */
	private String activityXML;
	/**
	 * 保存列的xml
	 */
	private String columnXML;
	/**
	 * 视图关联的菜单描述
	 */
	private String resourcedesc;
	/**
	 * 是否只读(如果为只读,就只能读不能修改)
	 */
	private Boolean readonly;

	/**
	 * 视图类型描述
	 */
	private int viewType = 1;

	/**
	 * 日期关联字段
	 */
	private String relationDateColum;

	/**
	 * 视图Column映射
	 */
	private String relatedMap;

	/**
	 * 树形节点点击打开类型
	 */
	private String innerType;

	/**
	 * 树形节点链接ID
	 */
	private String nodeLinkId;
	
	
	/**
	 * 是否签出
	 */
	private  boolean checkout = false;
	
	/**
	 * 签出者
	 */
	private String checkoutHandler;
	
	
	/**
	 * 数据呈现类型
	 */
	private String displayType;
	
	/**
	 * 地图视图默认地址国家
	 */
	private String country;

	/**
	 * 地图视图默认地址省份
	 */
	private String province;
	
	/**
	 * 地图视图默认地址城市
	 */
	private String city;
	
	/**
	 * 地图视图默认地址镇
	 */
	private String town;
	
	/**
	 * 地图视图打开地图默认等级
	 */
	private String level;
	
	/**
	 * 地图视图打开地图默认路线
	 */
	private Boolean isroute = false;

	/**
	 * 权限过滤条件
	 */
	private String authorityCondition;
	
	/**
	 * 手机视图类型
	 */
	private String mobileViewType;
	
	/**
	 * 授权方式
	 */
	private String permissionType = PERMISSION_TYPE_PUBLIC;
	
	public String getCommonFilterCondition() {
		return commonFilterCondition;
	}

	public void setCommonFilterCondition(String commonFilterCondition) {
		this.commonFilterCondition = commonFilterCondition;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		if(StringUtil.isBlank(permissionType)) permissionType = PERMISSION_TYPE_PRIVATE;
		this.permissionType = permissionType;
	}
	
	public String getMobileViewType() {
		return mobileViewType;
	}

	public void setMobileViewType(String mobileViewType) {
		this.mobileViewType = mobileViewType;
	}

	public String getAuthorityCondition() {
		return authorityCondition;
	}

	public void setAuthorityCondition(String authorityCondition) {
		this.authorityCondition = authorityCondition;
	}

	public String getLevel() {
		return !StringUtil.isBlank(level)?level:"4";
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public Boolean getIsroute() {
		return isroute;
	}

	public void setIsroute(Boolean isroute) {
		this.isroute = isroute;
	}
	
	public String getCountry() {
		return !StringUtil.isBlank(country)?country:"";
	}

	public void setCountry(String country) {
		if(!StringUtil.isBlank(country) && !country.equals("请选择")){
			this.country = country;
		}else{
			this.country = "";
		}
	}

	public String getProvince() {
		return !StringUtil.isBlank(province)?province:"";
	}

	public void setProvince(String province) {
		if(!StringUtil.isBlank(province) && !province.equals("请选择")){
			this.province = province;
		}else{
			this.province = "";
		}
	}

	public String getCity() {
		return !StringUtil.isBlank(city)?city:"";
	}

	public void setCity(String city) {
		if(!StringUtil.isBlank(city) && !city.equals("请选择")){
			this.city = city;
		}else{
			this.city = "";
		}
	}

	public String getTown() {
		return !StringUtil.isBlank(town)?town:"";
	}

	public void setTown(String town) {
		if(!StringUtil.isBlank(town) && !town.equals("请选择")){
			this.town = town;
		}else{
			this.town = "";
		}
	}

	/**
	 * 获取数据呈现类型
	 * @return
	 */
	public String getDisplayType() {
		return displayType;
	}

	/**
	 * 设置数据呈现类型
	 * @param displayType
	 */
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	/**
	 * 获取模板表单
	 * @return
	 */
	public String getTemplateForm() {
		return templateForm;
	}

	/**
	 * 设置模板表单
	 * @param templateForm
	 */
	public void setTemplateForm(String templateForm) {
		this.templateForm = templateForm;
	}

	/**
	 * 是否被签出
	 * @return
	 */
	public boolean isCheckout() {
		return checkout;
	}

	/**
	 * 设置是否签出
	 * @param checkout
	 */
	public void setCheckout(boolean checkout) {
		this.checkout = checkout;
	}

	/**
	 * 获取签出者
	 * @return
	 */
	public String getCheckoutHandler() {
		return checkoutHandler;
	}

	/**
	 * 设置签出者
	 * @param checkoutHandler
	 */
	public void setCheckoutHandler(String checkoutHandler) {
		this.checkoutHandler = checkoutHandler;
	}

	public String getRelatedMap() {
		return relatedMap;
	}

	public void setRelatedMap(String relatedMap) {
		this.relatedMap = relatedMap;
	}

	public String getInnerType() {
		return innerType;
	}

	public void setInnerType(String innerType) {
		this.innerType = innerType;
	}

	/**
	 * 是否汇总
	 * 
	 * @return
	 */
	public boolean isSum() {
		for (Iterator<Column> iterator = this.getColumns().iterator(); iterator.hasNext();) {
			Column col = iterator.next();
			if (col.isSum() || col.isTotal()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 获取用户权限字段
	 * 
	 * @hibernate.property column="AUTH_USER"
	 * @return
	 */
	public String getAuth_user() {
		return auth_user;
	}

	/**
	 * 设置用户权限字段
	 * 
	 * @param auth_user
	 */
	public void setAuth_user(String auth_user) {
		this.auth_user = auth_user;
	}

	/**
	 * 获取角色权限字段
	 * 
	 * @hibernate.property column="AUTH_ROLE"
	 * @return
	 */
	public String getAuth_role() {
		return auth_role;
	}

	/**
	 * 设置角色权限字段
	 * 
	 * @param auth_role
	 */
	public void setAuth_role(String auth_role) {
		this.auth_role = auth_role;
	}

	/**
	 * @hibernate.property column="AUTH_FIELDS"
	 * @return
	 */
	public String getAuth_fields() {
		return auth_fields;
	}

	/**
	 * 设置表单的权限字段
	 * 
	 * @param auth_fields
	 */
	public void setAuth_fields(String auth_fields) {
		this.auth_fields = auth_fields;
	}

	/**
	 * 获取过滤条件
	 * 
	 * @hibernate.property column="FILTERCONDITION" length = "2000"
	 * @return 过滤条件
	 */
	public String getFilterCondition() {
		return filterCondition;
	}

	/**
	 * 设置视图为设计模式时的过滤条件
	 * 
	 * @param filterCondition
	 */
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

	/**
	 * 获取sql过滤脚本
	 * 
	 * @hibernate.property column="SQLFILTER_SCRIPT" type="text"
	 * @return
	 */
	public String getSqlFilterScript() {
		return sqlFilterScript;
	}

	/**
	 * 设置 sql过滤脚本
	 * 
	 * @param sqlFilterScript
	 */
	public void setSqlFilterScript(String sqlFilterScript) {
		this.sqlFilterScript = sqlFilterScript;
	}

	public String getProcedureFilterScript() {
		return procedureFilterScript;
	}

	public void setProcedureFilterScript(String procedureFilterScript) {
		this.procedureFilterScript = procedureFilterScript;
	}

	/**
	 * 获取编辑模式
	 * 
	 * @hibernate.property column="EDITMODE"
	 * @return 编辑模式
	 */
	public String getEditMode() {
		return editMode;
	}

	/**
	 * 设置编辑模式
	 * 
	 * @param editMode
	 *            编辑模式
	 */
	public void setEditMode(String editMode) {
		if (editMode != null && editMode.trim().length() > 0) {
			this.editMode = editMode;
		}
	}

	/**
	 * 获取关联模块对象
	 * 
	 * @return cn.myapps.core.deploy.module.ejb.ModuleVO
	 * @hibernate.many-to-one class="cn.myapps.core.deploy.module.ejb.ModuleVO"
	 *                        column="MODULE"
	 */
	public ModuleVO getModule() {
		return module;
	}

	/**
	 * 设置关联模块对象
	 * 
	 * @param module
	 */
	public void setModule(ModuleVO module) {
		this.module = module;
	}

	/**
	 * 主键
	 * 
	 * @hibernate.id column="ID" generator-class="assigned"
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置标识
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取相关联字段集合
	 * 
	 * @return
	 */
	public Set<Column> getColumns() {
		if (this.columns == null)
			this.columns = new TreeSet<Column>();
		return columns;
	}

	/**
	 * 设置相关联列集合
	 * 
	 * @param columns
	 */
	public void setColumns(Set<Column> columns) {
		this.columns = columns;
	}

	/**
	 * 获取显示的列
	 * 
	 * @return
	 */
	public Set<Column> getDisplayColumns(IRunner runner) {
		Set<Column> rtn = new TreeSet<Column>();

		Set<Column> columns = getColumns();
		if (columns.size() > 0) {
			for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext();) {
				Column column = iterator.next();
//				String width = column.getWidth() + "";
//				if (!"0".equals(width.trim()) && !column.isHiddenColumn(runner)) {
				if (!column.isHiddenColumn(runner)) {
					rtn.add(column);
				}
			}
		}

		return rtn;
	}

	/**
	 * 获取过滤脚本
	 * 
	 * @hibernate.property column="FILTER_SCRIPT" type="text"
	 * @return
	 */
	public String getFilterScript() {
		return filterScript;
	}

	/**
	 * 设置过滤脚本
	 * 
	 * @param filterScript
	 */
	public void setFilterScript(String filterScript) {
		this.filterScript = filterScript;
	}

	/**
	 * 获取名称
	 * 
	 * @hibernate.property column="NAME"
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取关联查询表单(Form)
	 * 
	 * @hibernate.many-to-one name="searchForm" cascade="none" outer-join="true"
	 *                        class="cn.myapps.core.dynaform.form.ejb.Form"
	 *                        column="FORM_ID"
	 * 
	 * @return 查询表单
	 */
	public Form getSearchForm() {
		return searchForm;
	}

	/**
	 * 设置关联查询表单(Form)
	 * 
	 * @param searchForm
	 *            关联查询表单
	 */
	public void setSearchForm(Form searchForm) {
		this.searchForm = searchForm;
	}

	/**
	 * 设置相关联按钮集合
	 */
	public Set<Activity> getActivitys() {
		if (this.activitys == null)
			this.activitys = new TreeSet<Activity>();
		return activitys;
	}

	/**
	 * 设置Activity
	 * 
	 * @param activitys
	 *            按钮集合
	 */
	public void setActivitys(Set<Activity> activitys) {
		this.activitys = activitys;
	}

	/**
	 * 获取风格(Style)对象
	 * 
	 * @return cn.myapps.core.style.repository.ejb.StyleRepositoryVO
	 * @hibernate.many-to-one 
	 *                        class="cn.myapps.core.style.repository.ejb.StyleRepositoryVO"
	 *                        column="STYLE"
	 */
	public StyleRepositoryVO getStyle() {
		return style;
	}

	/**
	 * 设置样式库(Style)对象
	 * 
	 * @param style
	 *            样式库(Style)对象
	 */
	public void setStyle(StyleRepositoryVO style) {
		this.style = style;
	}

	/**
	 * 获取相关resource主键
	 * 
	 * @hibernate.property column="RELATEDRESOURCEID"
	 */
	public String getRelatedResourceid() {
		return relatedResourceid;
	}

	/**
	 * 设置相关resource主键
	 * 
	 * @param relatedResourceid
	 */
	public void setRelatedResourceid(String relatedResourceid) {
		this.relatedResourceid = relatedResourceid;
	}

	/**
	 * 获取是否分页
	 * 
	 * @hibernate.property column="ISPAGINATION"
	 * @return true为分页,false不分页
	 */
	public boolean isPagination() {
		return pagination;
	}

	/**
	 * 设置是否分页
	 * 
	 * @param isPagination
	 */
	public void setPagination(boolean isPagination) {
		this.pagination = isPagination;
	}

	/**
	 * 获取一页显示行数
	 * 
	 * @hibernate.property column="PAGELINES"
	 * @return 一页显示行数
	 */
	public String getPagelines() {
		return pagelines;
	}

	/**
	 * 设置一页显示行数
	 * 
	 * @param pagelines
	 */
	public void setPagelines(String pagelines) {
		this.pagelines = pagelines;
	}

	/**
	 * 打开类型
	 * 
	 * @hibernate.property column="OPENTYPE"
	 */
	public int getOpenType() {
		//去除当前区域显示,普通改为当前页显示,旧数据兼容 2013-5-8
		if(openType == View.OPEN_TYPE_OWN){
			openType = View.OPEN_TYPE_NORMAL;
		}
		if(openType == View.OPEN_TYPE_POP){
			openType = View.OPEN_TYPE_DIV;
		}
		return openType;
	}

	/**
	 * 设置打开类型
	 * 
	 * @param openType
	 */
	public void setOpenType(int openType) {
		//去除当前区域显示,普通改为当前页显示,旧数据兼容 2013-5-8
		if(openType == View.OPEN_TYPE_OWN){
			openType = View.OPEN_TYPE_NORMAL;
		}
		if(openType == View.OPEN_TYPE_POP){
			openType = View.OPEN_TYPE_DIV;
		}
		this.openType = openType;
	}

	/**
	 * 获取是否显示总行数
	 * 
	 * @hibernate.property column="SHOWTOTALROW"
	 * @return true为显示，false为不显示
	 */
	public boolean isShowTotalRow() {
		return showTotalRow;
	}

	/**
	 * 设置是否显示总行数
	 * 
	 * @param showTotalRow
	 */
	public void setShowTotalRow(boolean showTotalRow) {
		this.showTotalRow = showTotalRow;
	}

	 /**
	 * 获取排序的字列
	 *
	 * @hibernate.property column="ORDERFIELD"
	 */
	 public String getOrderField() {
	 return orderField;
	 }
	
	 /**
	 * 设置排序的字列
	 *
	 * @param orderField
	 * 排序的字列
	 */
	 public void setOrderField(String orderField) {
	 this.orderField = orderField;
	 }
	 /**
	 * 排序类型 ASC|DESC
	 *
	 * @hibernate.property column="ORDERTYPE"
	 */
	 public String getOrderType() {
	 return orderType;
	 }
	
	 /**
	 * 设置排序类型
	 *
	 * @param orderType
	 */
	 public void setOrderType(String orderType) {
	 this.orderType = orderType;
	 }

	/**
	 * 获取相关表单
	 * 
	 * @hibernate.property column="RELATEDFORM"
	 */
	public String getRelatedForm() {
		return relatedForm;
	}

	/**
	 * 
	 * 设置相关联的表单
	 * 
	 * @param relatedForm
	 */
	public void setRelatedForm(String relatedForm) {
		this.relatedForm = relatedForm;
	}

	/**
	 * 获取最后修改日期. 此为记录表单修改的最后日期.
	 * 
	 * @hibernate.property column="LASTMODIFYTIME"
	 * @return 最后的修改日期
	 */
	public Date getLastmodifytime() {
		return lastmodifytime;
	}

	/**
	 * 设置最后的修改日期. 此为记录表单修改的最后日期.
	 * 
	 * @param lastmodifytime
	 *            最后的修改日期
	 */
	public void setLastmodifytime(Date lastmodifytime) {
		this.lastmodifytime = lastmodifytime;
	}

	/**
	 * 是否刷新
	 * 
	 * @hibernate.property column="REFRESH"
	 * @return 是否刷新
	 */
	public boolean isRefresh() {
		return refresh;
	}

	/**
	 * 设置是否可是刷新
	 * 
	 * @param refresh
	 */
	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	/**
	 * 获取部门字段值
	 * 
	 * @hibernate.property column="FILTERSCRIPTONDESIGN"
	 * @return
	 */
	public String getDepartments() {
		return departments;
	}

	/**
	 * 设置相关联部门值,用于权限字段
	 * 
	 * @param departments
	 */
	public void setDepartments(String departments) {
		this.departments = departments;
	}

	/**
	 * 获取列表描述.
	 * 
	 * @hibernate.property column="DESCRIPTION"
	 * @return 列表描述
	 * @uml.property name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置列表描述.
	 * 
	 * @param description
	 *            列表描述
	 * @uml.property name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取按钮XML,通过映射的对象生成
	 * 
	 * @hibernate.property column="ACTIVITYXML" type="text"
	 * @return
	 */
	public String getActivityXML() {
		if (!getActivitys().isEmpty()) {
			return XmlUtil.toXml(getActivitys());
		}
		return activityXML;
	}

	/**
	 * @SuppressWarnings XmlUtil.toOjbect返回值类型为Object不定类型 设置相关联按钮XML,通过映射的对象生成
	 */
	@SuppressWarnings("unchecked")
	public void setActivityXML(String activityXML) {
		getActivitys().clear();
		if (!StringUtil.isBlank(activityXML)) {
			getActivitys().addAll((Set<Activity>) XmlUtil.toOjbect(activityXML));
		}
		this.activityXML = activityXML;
	}

	/**
	 * 获取相关表的XML
	 * 
	 * @hibernate.property column="COLUMNXML" type="text"
	 * @return
	 */
	public String getColumnXML() {
		if (!getColumns().isEmpty()) {
			return XmlUtil.toXml(getColumns());
		}
		return columnXML;
	}

	/**
	 * 设置关联列的XML
	 * 
	 * @param columnXML
	 */
	@SuppressWarnings("unchecked")
	public void setColumnXML(String columnXML) {
		getColumns().clear();
		if (!StringUtil.isBlank(columnXML)) {
			getColumns().addAll((Set<Column>) XmlUtil.toOjbect(columnXML));
		}
		this.columnXML = columnXML;
	}

	public Column findColumnByName(String name) {
		if (!StringUtil.isBlank(name)) {
			Set<Column> set = getColumns();
			if (set != null && !set.isEmpty()) {
				for (Iterator<Column> iterator = set.iterator(); iterator.hasNext();) {
					Column column = (Column) iterator.next();
					if (column.getName().equals(name)) {
						return column;
					}
				}
			}
		}

		return null;
	}

	/**
	 * 根据表单字段名称查找视图列
	 * 
	 * @param fieldName
	 *            表单字段名称
	 * @return
	 */
	public Column findColumnByFieldName(String fieldName) {
		if (!StringUtil.isBlank(fieldName)) {
			Set<Column> set = getColumns();
			if (set != null && !set.isEmpty()) {
				for (Iterator<Column> iterator = set.iterator(); iterator.hasNext();) {
					Column column = iterator.next();
					if (column.getFieldName().equals(fieldName)) {
						return column;
					}
				}
			}
		}

		return null;
	}

	/**
	 * 查询按钮,获取按钮对象
	 * 
	 * @param id
	 *            按钮标识
	 * @return 按钮对象
	 */
	public Activity findActivity(String id) {
		Set<Activity> activitySet = getActivitys();
		for (Iterator<Activity> iterator = activitySet.iterator(); iterator.hasNext();) {
			Activity activity = iterator.next();
			if (activity.getId().equals(id)) {
				activity.setParentView(getId());
				activity.setApplicationid(getApplicationid());
				return activity;
			}
		}
		Form form = getSearchForm();
		if (form != null)
			return form.findActivity(id);
		return null;
	}

	/**
	 * 获取视图关联的菜单描述
	 * 
	 * @hibernate.property column="RESOURCEDESC" type="text"
	 * @return
	 */
	public String getResourcedesc() {
		return resourcedesc;
	}

	/**
	 * 设置视图关联的菜单描述
	 * 
	 * @param resourcedesc
	 */
	public void setResourcedesc(String resourcedesc) {
		this.resourcedesc = resourcedesc;
	}

	/**
	 * 获得视图名
	 * 
	 * @return
	 */
	public String getLocation() {
		return "View." + getName();
	}

	/**
	 * 生成表格行的HTML,前台显示
	 * 
	 * @param doc
	 *            文档
	 * @param runner
	 *            脚本执行器
	 * @param webUser
	 *            当前用户
	 * @param columnOptionsCache    
	 *            缓存控件选项(选项设计模式构建的Options)的Map
	 * @return
	 */
	public String toRowHtml(Document doc, IRunner runner, WebUser webUser, boolean isEdit, Map<String,Options> columnOptionsCache) {
		StringBuffer htmlBuilder = new StringBuffer();
		if (isEdit) {
			isEdit = !this.getReadonly().booleanValue();
		}
		// 添加表格行开始
		htmlBuilder.append("<tr id='" + doc.getId() + "' trType='dataTr'");
		htmlBuilder.append(" class='" + ONMOUSEOUT_CLASS + "'");
		htmlBuilder.append(" onmouseover='jQuery(this).addClass(\"" + ONMOUSEOVER_CLASS + "\")'");
		htmlBuilder.append(" onmouseout='jQuery(this).removeClass(\"" + ONMOUSEOVER_CLASS + "\")'");
		htmlBuilder.append(">\n");

		// 添加CheckBox表格列
		htmlBuilder.append(toCheckBoxColumnHtml(doc));
		// 添加自定义表格列
		Iterator<Column> iter = columns.iterator();
		while (iter.hasNext()) {
			Column col = iter.next();
			if (!col.isHiddenColumn(runner) && col.isVisible())
				htmlBuilder.append(col.toGridColumnHtml(doc, runner, webUser, isEdit, columnOptionsCache));
		}

		// 添加操作列
		htmlBuilder.append(toActionColumnHtml(doc));

		// 添加表格行结束
		htmlBuilder.append("</tr>");

		return htmlBuilder.toString();
	}

	/**
	 * 视图打印时，生成表格行的HTML
	 */
	public String printToRowHtml(Document doc, IRunner runner, WebUser webUser, boolean isEdit, Map<String,Options> columnOptionsCache) {
		StringBuffer htmlBuilder = new StringBuffer();
		if (isEdit) {
			isEdit = !this.getReadonly().booleanValue();
		}
		// 添加表格行开始
		htmlBuilder.append("<tr id='" + doc.getId() + "'");
		htmlBuilder.append(" class='" + ONMOUSEOUT_CLASS + "'");
		htmlBuilder.append(" onmouseover='jQuery(this).addClass(\"" + ONMOUSEOVER_CLASS + "\")'");
		htmlBuilder.append(" onmouseout='jQuery(this).removeClass(\"" + ONMOUSEOVER_CLASS + "\")'");
		htmlBuilder.append(">\n");

		// 添加自定义表格列
		Iterator<Column> iter = columns.iterator();
		while (iter.hasNext()) {
			Column col = iter.next();
			if (!col.isHiddenColumn(runner) && col.isVisible())
				htmlBuilder.append(col.toGridColumnHtml(doc, runner, webUser, isEdit, columnOptionsCache));
		}

		// 添加操作列
		htmlBuilder.append(toActionColumnHtml(doc));

		// 添加表格行结束
		htmlBuilder.append("</tr>");

		return htmlBuilder.toString();
	}
	
	/**
	 * 在列显示前生成多选的HTML
	 * 
	 * @param doc
	 *            文档
	 * @return 以html的形式返回
	 */
	public String toCheckBoxColumnHtml(Document doc) {
		StringBuffer htmlBuilder = new StringBuffer();

		htmlBuilder.append("<td class='table-td'>");
		htmlBuilder.append(getCheckBoxColumnContent(doc));
		htmlBuilder.append("</td>\n");

		return htmlBuilder.toString();
	}

	/**
	 * 生成操作行
	 * 
	 * @param doc
	 *            文档
	 * @return 以html的形式返回
	 */
	public String toActionColumnHtml(Document doc) {
		StringBuffer htmlBuilder = new StringBuffer();

		htmlBuilder.append("<td class='table_th_td' style='text-align: left'>");
		htmlBuilder.append(getActionColumnContent(doc));
		htmlBuilder.append("</td>\n");

		return htmlBuilder.toString();
	}

	private String getActionColumnContent(Document doc) {
		StringBuffer htmlBuilder = new StringBuffer();
		String id = doc.getId() + "_" + "actions";
		String showId = id + "_show";
		String editId = id + "_edit";

		htmlBuilder.append("<div");
		htmlBuilder.append(" id=").append("'").append(showId).append("'");
		htmlBuilder.append(">");
		htmlBuilder.append("&nbsp;");
		htmlBuilder.append("</div>");

		htmlBuilder.append("<div style='display:none'");
		htmlBuilder.append(" id=").append("'").append(editId).append("'");
		htmlBuilder.append(">");
//		// 保存按钮
//		htmlBuilder.append("<input type='button' onclick='doSave(\"" + doc.getId()
//				+ "\")' class='grid-button-confirm' value='{*[Save]*}'/>");
		// 空项
		htmlBuilder.append("<input type='hidden' id='" + id + "' />&nbsp;");
		// 取消按钮
		htmlBuilder.append("<input type='button' value='{*[Cancel]*}' onclick='doCancel(\"" + doc.getId()
				+ "\")' class='grid-button-cancel'/>");

		htmlBuilder.append("</div>");

		return htmlBuilder.toString();
	}

	/**
	 * 获得视图关联的列,关生成html,在列前加多选
	 * 
	 * @param doc
	 *            文档
	 * @return 以html的形式返回
	 */
	public String getCheckBoxColumnContent(Document doc) {
		StringBuffer htmlBuilder = new StringBuffer();

		String id = doc.getId() + "_" + "_selects";
		String showId = id + "_show";
		// String editId = id + "_edit";

		htmlBuilder.append("<div");
		htmlBuilder.append(" id=").append("'").append(showId).append("'");
		htmlBuilder.append(">");
		htmlBuilder.append("<input type='checkbox' id='" + id + "' name='_selects' value='" + doc.getId() + "'>");
		htmlBuilder.append("</div>");

		// htmlBuilder.append("<div style='display:none'");
		// htmlBuilder.append(" id=").append("'").append(editId).append("'");
		// htmlBuilder.append(">");
		//
		// htmlBuilder.append("<input type='button' onclick='doSave(\"" +
		// doc.getId()
		// + "\")' class='grid-button-confirm'/><br>");
		//
		// htmlBuilder.append("<input type='button' onclick='doCancel(\"" +
		// doc.getId()
		// + "\")' class='grid-button-cancel'/>");
		// htmlBuilder.append("</div>");

		return htmlBuilder.toString();
	}

	/**
	 * 生成视图刷新方法,
	 * 
	 * @return
	 */
	public String toRefreshFunction() {
		StringBuffer scriptBuilder = new StringBuffer();

		scriptBuilder.append("<script>");
		scriptBuilder.append("var columnModel = [");
		if (columns != null && !columns.isEmpty()) {
			for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext();) {
				Column col = iterator.next();
				String fieldName = col.getFieldName();
				scriptBuilder.append("'" + fieldName + "'");
				scriptBuilder.append(",");
			}
			// scriptBuilder.deleteCharAt(scriptBuilder.lastIndexOf(","));
			scriptBuilder.append("'actions'");
		}
		scriptBuilder.append("];\n");
		scriptBuilder.append("function dy_view_refresh(id){\n");
		scriptBuilder.append("doRefresh(id);\n");
		scriptBuilder.append("}");
		scriptBuilder.append("</script>");
		return scriptBuilder.toString();
	}

	/**
	 * 获取表格行的后并创建脚本, 在编辑视图的列时使用,
	 * 
	 * @param doc
	 *            文档
	 * @param runner
	 *            宏脚本运行类
	 * @param webUser
	 *            用户
	 * @return script脚本形式(function method)
	 * @throws Exception
	 */
	public String getRowCreateScript(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer refreshScript = new StringBuffer();

		refreshScript.append("var func = function(){");
		refreshScript.append("var oTR = createRow({id:'" + doc.getId() + "', 'cssClass':'" + ONMOUSEOUT_CLASS + "'");
		refreshScript.append("});");
		refreshScript.append("oTR.mouseover(function(){jQuery(this).addClass('" + ONMOUSEOVER_CLASS + "')});");
		refreshScript.append("oTR.mouseout(function(){jQuery(this).removeClass('" + ONMOUSEOVER_CLASS + "')});");

		// 添加CheckBox Column
		refreshScript.append("oTR.append(");
		refreshScript.append("createColumn({'cssClass': 'table-td'}, \"");
		refreshScript.append(replaceContent(getCheckBoxColumnContent(doc)));
		refreshScript.append("\"));");

		// 添加Custom Column
		for (Iterator<Column> iterator = this.getColumns().iterator(); iterator.hasNext();) {
			Column col = iterator.next();
			refreshScript.append("oTR.append(");
			refreshScript.append(col.getCellCreateScript(doc, runner, webUser));
			refreshScript.append(");");
		}

		// 添加Action Column
		refreshScript.append("oTR.append(");
		refreshScript.append("createColumn({'style': 'text-align: left'}, \"");
		refreshScript.append(replaceContent(getActionColumnContent(doc)));
		refreshScript.append("\"));");

		refreshScript.append("return oTR;");
		refreshScript.append("};");

		refreshScript.append("func.call();");

		return refreshScript.toString();
	}

	private String replaceContent(String content) {
		content = content.replaceAll("\\\"", "\\\\\"");
		content = content.replaceAll("\\\'", "\\\\\'");
		return content;
	}

	/**
	 * 获取表格行的刷新脚本,在编辑视图的列时使用,
	 * 
	 * @param doc
	 *            文档
	 * @param runner
	 *            宏脚本运行类
	 * @param webUser
	 *            用户
	 * @return script脚本形式(functio method)
	 * @throws Exception
	 */
	public String getRowRefreshScript(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer refreshScript = new StringBuffer();
		refreshScript.append("var func = function(){");
		for (Iterator<Column> iterator = this.getColumns().iterator(); iterator.hasNext();) {
			Column col = iterator.next();
			refreshScript.append(col.getCellRefreshScript(doc, runner, webUser));
		}
		refreshScript.append("};func.call();");

		return refreshScript.toString();
	}

	/**
	 * 获取是否只读
	 * 
	 * @hibernate.property column="READONLY"
	 * @return
	 */
	public Boolean getReadonly() {
		if (this.readonly == null) {
			readonly = false;
		}

		return readonly;
	}

	/**
	 * 设置只读
	 * 
	 * @param readonly
	 *            是否只读
	 */
	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	/**
	 * 设置视图类型
	 * 
	 * @param viewType
	 *            视图类型
	 * 
	 */
	public void setViewType(int viewType) {
		this.viewType = viewType;
	}

	/**
	 * 获取关联日期字段名
	 * 
	 * @hibernate.property column="RELATIONDATECOLUM"
	 * @return
	 */
	public String getRelationDateColum() {
		return relationDateColum;
	}

	/**
	 * 设置关联日期字段名
	 * 
	 * @param viewType
	 *            关联日期字段名
	 * 
	 */
	public void setRelationDateColum(String relationDateColum) {
		this.relationDateColum = relationDateColum;
	}

	/**
	 * 获取编辑类型(Design、DQL、SQL)
	 * 
	 * @return 编辑类型
	 */
	public EditMode getEditModeType() {
		if (EDIT_MODE_CODE_DQL.equals(getEditMode())) {
			return new DQLEditMode(this);
		} else if (EDIT_MODE_CODE_SQL.equals(getEditMode())) {
			return new SQLEditMode(this);
		} else if (EDIT_MODE_DESIGN.equals(getEditMode())) {
			return new DesignEditMode(this);
		} else if (EDIT_MODE_CODE_PROCEDURE.equals(getEditMode())) {
			return new ProcedureEditMode(this);
		}

		return new NullEditMode(this);
	}

	/**
	 * 获取视图类型
	 * 
	 * @return 视图类型
	 */
	public ViewType getViewTypeImpl() {
		switch (viewType) {
		case VIEW_TYPE_CALENDAR:
			return new CalendarType(this);
		case VIEW_TYPE_NORMAL:
			return new NormalType(this);
		case VIEW_TYPE_TREE:
			return new TreeType(this);
		case VIEW_TYPE_MAP:
			return new MapType(this);
		case VIEW_TYPE_GANTT:
			return new GanttType(this);
		case VIEW_TYPE_COLLAPSIBLE:
			return new CollapsibleType(this);
		default:
			break;
		}

		return new NullType(this);
	}

	public String getTotalRowText(DataPackage<Document> datas) {
		String totalRowText = "0";
		if (this.isShowTotalRow()) {
			if (datas != null && datas.rowCount > 0) {
				totalRowText = String.valueOf(datas.rowCount);
			}
		}
		return totalRowText;
	}

	public int getViewType() {
		return viewType;
	}

	public String getFullName() {
		String viewname = this.name;

		ModuleVO mv = this.getModule();
		if (mv != null) {
			viewname = mv.getName() + "/" + viewname;
			while (mv.getSuperior() != null) {
				mv = mv.getSuperior();
				viewname = mv.getName() + "/" + viewname;
			}
			viewname = mv.getApplication().getName() + "/" + viewname;
		}
		return viewname;
	}

	public String getNodeLinkId() {
		return nodeLinkId;
	}

	public void setNodeLinkId(String nodeLinkId) {
		this.nodeLinkId = nodeLinkId;
	}
	
	/**
	 * 获取权限字段的允许范围
	 * @return
	 */
	public String getAuthFieldScope() {
		return authFieldScope;
	}

	/**
	 * 设置权限字段允许范围
	 * @param authFieldScope
	 */
	public void setAuthFieldScope(String authFieldScope) {
		this.authFieldScope = authFieldScope;
	}

	public LinkVO getLink() {
		try {
			LinkProcess linkProcess = (LinkProcess) ProcessFactory.createProcess(LinkProcess.class);
			LinkVO linkVO = (LinkVO) linkProcess.doView(getNodeLinkId());

			return linkVO;
		} catch (Exception e) {
			LOG.warn("getLink", e);
		}

		return null;
	}

	/**
	 * 根据关联表单获取子表单视图
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<View> getSubViewList() throws Exception {
		FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<View> rtn = new ArrayList<View>();
		String formid = getRelatedForm();
		if (!StringUtil.isBlank(formid)) {
			Form form = (Form) formProcess.doView(formid);
			if (form != null) {
				rtn.addAll(form.getIncludeViewList());
			} else {
				LOG.warn("RelatedForm not exist, formid is '" + formid + "'.");
			}
		}

		return rtn;
	}

	/**
	 * 获取默认排序列以及其属性
	 * @return
	 */
	public String[] getDefaultOrderFieldArr(){
		Collection<Column> cols = this.getOrderByColumns();
		return getOrderFieldAndOrderTypeArr(cols);
	}
	
	/**
	 * 获取单击列头排序列以及其属性
	 * @return
	 */
	public String[] getClickSortingFieldArr(){
		Collection<Column> cols = this.getClickSortingColumns();
		return getOrderFieldAndOrderTypeArr(cols);
	}
	
	private String[] getOrderFieldAndOrderTypeArr(Collection<Column> cols) {
		String[] orderfields = new String[cols.size()];
		int i = 0;
		for (Iterator<Column> iter = cols.iterator(); iter.hasNext();) {
			Column vo = iter.next();
			String orderField = this.getFormFieldNameByColsName(vo.getFieldName());
			String orderType = vo.getOrderType();
			if(orderType == null){
				orderType = "ASC";
			}
			if(! StringUtil.isBlank(vo.getSortStandard())){
				orderfields[i++] = orderField + " " + orderType + vo.getSortStandard();
			}else{
				orderfields[i++] = orderField + " " + orderType;
			}
		}
		return orderfields;
	}

	private Collection<Column> getOrderByColumns() {
		Collection<Column> rtn = new ArrayList<Column>();
		Collection<Column> cols = this.getColumns();
		for (Iterator<Column> iter = cols.iterator(); iter.hasNext();) {
			Column vo = iter.next();
			if (vo.getFieldName() != null && !vo.getFieldName().equals("")) {
				if (vo.getIsOrderByField() != null) {
					if (vo.getType().equals(Column.COLUMN_TYPE_FIELD) && vo.getIsOrderByField().equals("true")) {
						rtn.add(vo);
					}
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 获取单击列头排序的Columns
	 * @return
	 */
	private Collection<Column> getClickSortingColumns() {
		Collection<Column> rtn = new ArrayList<Column>();
		Collection<Column> cols = this.getColumns();
		for (Iterator<Column> iter = cols.iterator(); iter.hasNext();) {
			Column vo = iter.next();
			if (vo.getFieldName() != null && !vo.getFieldName().equals("")) {
				if (vo.getType().equals(Column.COLUMN_TYPE_FIELD) && vo.isClickSorting()) {
					rtn.add(vo);
				}
			}
		}
		return rtn;
	}

	public String getFormFieldNameByColsName(String field) {
		if (field != null && field.trim().length() > 0) {
			if (field.startsWith("$")) {
				return field.substring(1, field.length());
			} else {
				try {
					FormProcess fp = (FormProcess) ProcessFactory.createProcess((FormProcess.class));
					Form form = (Form) fp.doView(this.getRelatedForm());
					if (form != null && null != form.getTableMapping()) {
						String columnName = form.getTableMapping().getColumnName(field);
						return columnName;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "";
				}
			}
		}
		return field;
	}

	public String getSimpleClassName() {
		return this.getClass().getSimpleName();
	}
}
