//Source file: C:\\Java\\workspace\\MyApps\\src\\cn\\myapps\\core\\resource\\ejb\\ResourceVO.java

package cn.myapps.core.resource.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.UrlUtil;
import cn.myapps.util.json.JsonUtil;
import net.sf.json.JSONObject;

/**
 * @hibernate.class table="T_RESOURCE" lazy="false"
 */
public class ResourceVO extends ValueObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7671765820640298103L;

	private static final Logger LOG = Logger.getLogger(ResourceVO.class);
	public static final String ACTIONTYPE_NONE = "00";

	public static final String ACTIONTYPE_VIEW = "01";

	public static final String ACTIONTYPE_ACTIONClASS = "02";

	public static final String ACTIONTYPE_OTHEROUR = "03";

	public static final String ACTIONTYPE_REPORT = "04";

	/*授权方式-私有*/
	public static final String PERMISSION_TYPE_PRIVATE = "private";
	/*授权方式-公共*/
	public static final String PERMISSION_TYPE_PUBLIC = "public";
	
	public static final int SHOW_TYPE_BOTH = 0;
	public static final int SHOW_TYPE_MENU = 1;
	public static final int SHOW_TYPE_FLOW_CENTER = 2;

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 资源描述
	 */
	private String description;
	
	/**
	 * 菜单多语言标签
	 */
	private String multiLanguageLabel;


	/**
	 * 资源类型： 00=菜单 01=页面 10=Mobile 菜单
	 */
	private String type;

	/**
	 * 打开页面位置
	 */
	private String opentarget;

	public String getOpentarget() {
		return opentarget;
	}

	public void setOpentarget(String opentarget) {
		this.opentarget = opentarget;
	}

	/**
	 * 排序字段
	 * worning:把String类型的排序字段改为Integer类型的字段
	 *    排序字段在配置文件里已改成SORTNO，注意查看
	 */
	private Integer orderno;

	/**
	 * 是否显示记录总数
	 */
	private String showtotalrow;

	public String getShowtotalrow() {
		return showtotalrow;
	}

	public void setShowtotalrow(String showtotalrow) {
		this.showtotalrow = showtotalrow;
	}

	/**
	 * 菜单图标
	 */
	private String ico;

	/**
	 * 菜单图标类型 001=目录菜单 010=待办列表 011=超期列表 100=常规列表
	 */
	private String mobileIco;

	private ResourceVO superior;

	private String application;

	private String reportAppliction;

	private String reportModule;

	private String report;
	
	/**
	 * 授权方式
	 */
	private String permissionType = PERMISSION_TYPE_PUBLIC;
	
	/**
	 * 菜单显示方式（仅菜单显示、仅流程中心显示、菜单与流程中心显示）
	 */
	private int showType = SHOW_TYPE_BOTH;


	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		if(StringUtil.isBlank(permissionType)) permissionType = PERMISSION_TYPE_PRIVATE;
		this.permissionType = permissionType;
	}

	/**
	 * @return java.lang.String
	 * @hibernate.property column="DESCRIPTION"
	 * @roseuid 44C5FC6C0026
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @return java.lang.String
	 * @hibernate.property column = "ICO"
	 * @roseuid 44C5FC6C0289
	 */
	public String getIco() {
		return ico;
	}

	/**
	 * @return the current value of the id property
	 * @hibernate.id column="ID" generator-class = "assigned"
	 * @roseuid 44C5FC6B03D2
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return java.lang.String
	 * @hibernate.property column = "ORDERNO"
	 * @roseuid 44C5FC6C01FD
	 */
	public Integer getOrderno() {
		return orderno;
	}

	/**
	 * @hibernate.many-to-one class = "cn.myapps.core.resource.ejb.ResourceVO"
	 *                        column = "SUPERIOR" outer-join = "true"
	 * @return Returns the superior.
	 * @roseuid 44C7A18A029F
	 */
	public ResourceVO getSuperior() {
		return superior;
	}

	/**
	 * @return java.lang.String
	 * @hibernate.property column = "TYPE"
	 * @roseuid 44C5FC6C017B
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param aDescription
	 * @roseuid 44C5FC6C003B
	 */
	public void setDescription(String aDescription) {
		description = aDescription;
	}

	/**
	 * @param aIco
	 * @roseuid 44C5FC6C029D
	 */
	public void setIco(String aIco) {
		ico = aIco;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param aId
	 *            the new value of the id property
	 * 
	 * @param aId
	 * @roseuid 44C5FC6B03DC
	 */
	public void setId(String aId) {
		id = aId;
	}

	/**
	 * @param aOrderno
	 * @roseuid 44C5FC6C0211
	 */
	public void setOrderno(Integer aOrderno) {
		orderno = aOrderno;
	}

	/**
	 * @param superior
	 * @roseuid 44C7A18A02A8
	 */
	public void setSuperior(ResourceVO superior) {
		this.superior = superior;
	}

	/**
	 * @param aType
	 * @roseuid 44C5FC6C018F
	 */
	public void setType(String aType) {
		type = aType;
	}

	/**
	 * @return java.lang.String
	 * @hibernate.property column = "APPLICATION"
	 * @roseuid 44C5FC6C012B
	 */
	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	/**
	 * @hibernate.property column="REPORT"
	 */
	public String getReport() {
		return report;
	}

	/**
	 * @param report
	 *            The report to set.
	 */
	public void setReport(String report) {
		this.report = report;
	}

	/**
	 * @hibernate.property column="REPORTAPPLICTION"
	 */
	public String getReportAppliction() {
		return reportAppliction;
	}

	/**
	 * @param reportAppliction
	 *            The reportAppliction to set.
	 */
	public void setReportAppliction(String reportAppliction) {
		this.reportAppliction = reportAppliction;
	}

	/**
	 * @hibernate.property column="REPORTMODULE"
	 */
	public String getReportModule() {
		return reportModule;
	}

	/**
	 * @param reportModule
	 *            The reportModule to set.
	 */
	public void setReportModule(String reportModule) {
		this.reportModule = reportModule;
	}

	/**
	 * @hibernate.property column="MOBILEICO"
	 */
	public String getMobileIco() {
		return mobileIco;
	}

	/**
	 * @param mobileIco
	 *            The mobileIco to set.
	 */
	public void setMobileIco(String mobileIco) {
		this.mobileIco = mobileIco;
	}

	public String getMultiLanguageLabel() {
		return multiLanguageLabel;
	}

	public void setMultiLanguageLabel(String multiLanguageLabel) {
		this.multiLanguageLabel = multiLanguageLabel;
	}

	public String toUrlString() {
		return toUrlString(null, new ParamsTable());
	}

	/**
	 * 返回此菜单的超链接
	 * 
	 * @return
	 */
	public String toUrlString(WebUser user, ParamsTable params) {
		Environment env = Environment.getInstance();
		StringBuffer html = new StringBuffer();
		String linkUrl = this.toLinkUrl(user, params);
		if(linkUrl.equals("")){
			return linkUrl;
		}
		if ("01".equals(this.getType())) {
			html.append(linkUrl);
			html.insert(linkUrl.indexOf("?") + 1, "_resourceid=" + this.getId() + "&");
			linkUrl = html.toString();
		}else if("07".equals(this.getType())){
			linkUrl += "?_resourceid=" + this.getId();
			linkUrl += "&application=" + params.getParameterAsString("application");
		}
		if (linkUrl.toLowerCase().startsWith("http")
				|| linkUrl.toLowerCase().startsWith(env.getContextPath().toLowerCase())) {
			return linkUrl;
		} else {
			if(linkUrl.indexOf("_resourceid") == -1){
				linkUrl += "&_resourceid=" + this.getId();
			}
			if (linkUrl.startsWith("/")) {
				return env.getContextPath() + linkUrl;
			}
			return env.getContextPath() + "/" + linkUrl;
		}

	}

	/**
	 * 返回此菜单的超链接
	 * 
	 * @return
	 */
	public String toUrlString(WebUser user, HttpServletRequest request) {
		ParamsTable params = ParamsTable.convertHTTP(request);
		return toUrlString(user, params);
	}

	/**
	 * 获取菜单全名
	 * 
	 * @return String
	 */
	public String getFullName() {
		ResourceVO resVO = this;

		String resName = resVO.getDescription();
		while (resVO.getSuperior() != null) {
			resVO = resVO.getSuperior();
			resName = resVO.getDescription() + "/" + resName;
		}

		if (!StringUtil.isBlank(resVO.getApplicationid())) {
			try {
				ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
						.createProcess(ApplicationProcess.class);
				ApplicationVO applicationVO = (ApplicationVO) applicationProcess.doView(resVO.getApplicationid());
				resName = applicationVO.getName() + "/" + resName;
			} catch (Exception e) {
				LOG.warn(e.getMessage());
			}
		}

		return resName;
	}
	
	//----------------------------以下为原LinkVO对象 Start----------------------------------
	public enum LinkType {
		FORM("00"), // 表单类型
		VIEW("01"), // 视图类型
		REPORT("02"), // 报表类型
		CUSTOMIZE_REPORT("09"), // 自定义报表类型
		RUNQIAN_REPORT("12"),//润乾报表
		EXCELIMPORT("03"), // excel导入类型
		ACTION("04"), // 平台控制器类型
		MANUAL_INTERNAL("05"), // 内部手动链接类型
		MANUAL_EXTERNAL("06"), // 外部手动链接类型
		SCRIPT("07"), // 脚本类型
		EMAIL("08"), // 邮件链接
		BBS("10"), // 论坛
		NetworkDisk("11"), // 论坛
		NONE("@@");

		private String code;

		private LinkType(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public static LinkType getByCode(String code) {
			LinkType[] tyeps = values();
			for (int i = 0; i < tyeps.length; i++) {
				if (tyeps[i].getCode().equals(code)) {
					return tyeps[i];
				}
			}
			return NONE;
		}
	}

	/**
	 * 名称
	 */
	private String linkName;

	/**
	 * 链接类型
	 */
	private String linkType;
	
	/**
	 * 所属模块
	 */
	private String moduleid;
	
	/**
	 * 链接内容指定的目录(如:portal/下)
	 */
	private String directory;

	/**
	 * 链接内容
	 */
	private String actionContent;

	/**
	 * 请求参数
	 */
	private String queryString;

	/**
	 * 获取链接名称
	 * 
	 * @return
	 */
	public String getLinkName() {
		return linkName;
	}

	/**
	 * 设置链接名称
	 * 
	 * @param linkName
	 */
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	
	/**
	 * 获取链接类型
	 * 
	 * @return
	 */
	public String getLinkType() {
		return linkType;
	}

	/**
	 * 设置链接类型
	 * 
	 * @param linkType
	 */
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	
	/**
	 * 获取模块ID
	 * 
	 * @return
	 */
	public String getModuleid() {
		return moduleid;
	}

	/**
	 * 设置模块ID
	 * 
	 * @param moduleid
	 */
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	/**
	 * 获取链接内容
	 * 
	 * @return
	 */
	public String getActionContent() {
		return actionContent;
	}

	/**
	 * 设置链接内容
	 * 
	 * @param actionContent
	 */
	public void setActionContent(String actionContent) {
		this.actionContent = actionContent;
	}

	/**
	 * 获取请求参数(Json 格式)
	 * 
	 * @return
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * 设置请求参数(Json 格式)
	 * 
	 * @param queryString
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * 设置链接内容指定的目录
	 * 
	 * @param directory
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * 获取链接内容指定的目录
	 * 
	 * @return directory(example: portal)
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * 根据链接对象的配置生成URL
	 * 
	 * @param doc
	 * @param params
	 * @param user
	 * @return
	 */
	public String toLinkUrl(Document doc, ParamsTable params, WebUser user) {
		LinkType type = LinkType.getByCode(this.getLinkType());
		String tempUrl = "";
		StringBuffer url = new StringBuffer();
		switch (type) {
		case FORM:
			tempUrl = "/portal/dynaform/document/newWithPermission.action";
			tempUrl = UrlUtil.parameterize(tempUrl, "_formid", actionContent);
			tempUrl = UrlUtil.parameterize(tempUrl, "_isJump", "1");
			tempUrl = UrlUtil.parameterize(tempUrl, "application", applicationid);
			url.append(tempUrl);
			break;
		case VIEW:
			tempUrl = "/portal/dynaform/view/displayViewWithPermission.action";
			tempUrl = UrlUtil.parameterize(tempUrl, "_viewid", actionContent);
			tempUrl = UrlUtil.parameterize(tempUrl, "clearTemp", "true");
			tempUrl = UrlUtil.parameterize(tempUrl, "application", applicationid);
			url.append(tempUrl);
			break;
		case REPORT:
			if (actionContent.startsWith("$/")) {
				url.append(UrlUtil.parameterize(actionContent.substring(2, actionContent.length()), "application", applicationid));
			} else {
				tempUrl = "/portal/report/crossreport/runtime/runreport.action";
				tempUrl = UrlUtil.parameterize(tempUrl, "reportId", actionContent);
				tempUrl = UrlUtil.parameterize(tempUrl, "application", applicationid);
				url.append(tempUrl);
			}
			break;
		case EXCELIMPORT:
			tempUrl = "/portal/share/dynaform/dts/excelimport/importbyid.jsp";
			tempUrl = UrlUtil.parameterize(tempUrl, "id", actionContent);
			tempUrl = UrlUtil.parameterize(tempUrl, "applicationid", applicationid);
			url.append(tempUrl);
			break;
		case MANUAL_INTERNAL:
			url.append(UrlUtil.parameterize(this.toManual_InternalUrl(), "application", this.applicationid));
			break;
		case EMAIL:
			url.append(UrlUtil.parameterize(actionContent, "application", this.applicationid));
			break;
		case MANUAL_EXTERNAL:
			if (!actionContent.toLowerCase().startsWith("http://")) {
				actionContent = "http://" + actionContent;
			}
			url.append(actionContent);
			break;
		case SCRIPT: // 脚本模式
			try {
				url.append(toScriptUrl4transit());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case CUSTOMIZE_REPORT:
			url.append(UrlUtil.parameterize(actionContent, "application", this.applicationid));
			break;
		case RUNQIAN_REPORT:
			tempUrl = actionContent;
			tempUrl = UrlUtil.parameterize(actionContent, "_linkid", this.id);
			tempUrl = UrlUtil.parameterize(tempUrl, "applicationid", applicationid);
			url.append(tempUrl);
			break;
		case BBS:
			HttpServletRequest request = params.getHttpRequest();
			String requestUrl = request.getRequestURL().toString();
			String contextPath = request.getContextPath();
			String prex = "";
			int index = requestUrl.indexOf(contextPath);
			if (index > -1) {
				prex = requestUrl.substring(0, index);
			}
			url.append(actionContent != null && "".equals(actionContent) ? prex + actionContent : prex
					+ "/bbs/index.htm");
			break;
		case NetworkDisk:
			url.append(UrlUtil.parameterize("/portal/share/component/networkdisk/networkdisk.jsp", "application", this.applicationid));
			break;
		}
		if (getQueryString() !=null && getQueryString().trim().length() > 0 && !"07".equals(getLinkType())&& !"12".equals(getLinkType())) {
			appendQueryString(url);
		}
		return url.toString();
	}

	/**
	 * 根据链接对象的配置生成URL
	 * 
	 * @return
	 */
	public String toLinkUrl() {
		try {
			return toLinkUrl(new Document(), new ParamsTable(), new WebUser(new BaseUser()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 根据链接对象的配置生成URL
	 * 
	 * @param user
	 * @return
	 */
	public String toLinkUrl(WebUser user, ParamsTable params) {
		return toLinkUrl(new Document(), params, user);
	}
	
	/**
	 * 根据链接对象的配置生成URL
	 * 
	 * @param user
	 * @return
	 */
	public String toLinkUrl(WebUser user, HttpServletRequest rquest) {
		ParamsTable params = ParamsTable.convertHTTP(rquest);
		return toLinkUrl(new Document(), params, user);
	}

	/**
	 * 在链接对象生成的URL基础上拼接请求参数 返回带请求参数的URL
	 * 
	 * @param url
	 * @param link
	 * @return
	 */
	public String appendQueryString(StringBuffer url) {
		Collection<Object> qs = JsonUtil.toCollection(getQueryString());
		Iterator<Object> iterator = qs.iterator();
		while (iterator.hasNext()) {
			JSONObject object = JSONObject.fromObject(iterator.next());
			if (url.toString().indexOf("?") > -1) {
				url.append("&");
			} else {
				url.append("?");
			}
			url.append(object.get("paramKey")).append("=").append(object.get("paramValue"));
		}

		return url.toString();
	}

	/**
	 * 生成链接类型为脚本类型的URL
	 * 
	 * @param doc
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String toScriptUrl(Document doc, ParamsTable params, WebUser user) throws Exception {
		String application = params.getParameterAsString(Web.REQUEST_ATTRIBUTE_APPLICATION);

		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), application);
		runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());

		int type = Integer.parseInt(this.getLinkType());
		StringBuffer url = new StringBuffer();
		switch (type) {
		case 7:
			StringBuffer label = new StringBuffer();
			label.append("Link(").append(this.getId()).append(").").append(this.getLinkName()).append(".Sript");
			Object result = runner.run(label.toString(), this.getActionContent());
			if (String.valueOf(result).trim().length() > 0) {
				url.append(String.valueOf(result));
			}
			break;
		}
		if (this.getQueryString().trim().length() > 0) {
			appendQueryString(url);
		}

		return url.toString();
	}
	
	public String toScriptUrl4transit() throws Exception {
		return "/portal/LinkForScript?_resourceid=" + this.id; //如果链接类型为脚本链接,生成一个servlet链接,在servlet去运行脚本生成URL
	}
	
	/**
	 * 获取完整的自定义链接(内部)，即(directory + actionContent)
	 * @return 自定义链接url(内部)
	 */
	public String toManual_InternalUrl(){
		try {
			if(StringUtil.isBlank(directory)){
				directory = "portal";//自定义链接(内部),默认链接portal目录下文件
				if(!actionContent.startsWith("/portal") && !actionContent.startsWith("portal")){
					if (actionContent.startsWith("/")){
						actionContent = directory + actionContent;
					}else{
						actionContent = directory + "/" + actionContent;
					}
				}
			}else{
				if("portal".equals(directory)){
					if(!actionContent.startsWith("/portal") && !actionContent.startsWith("portal")){
						if (actionContent.startsWith("/")){
							actionContent = directory + actionContent;
						}else{
							actionContent = directory + "/" + actionContent;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actionContent;
	}
	//----------------------------------原LinkVO对象End------------------------------------
	
	/**
	 * 判断是否pc端菜单
	 */
	public boolean isHtmlTerminal(){
		String[] types = type.split(",");
		for(String str:types){
			if(str.trim().equals(ResourceType.RESOURCE_TYPE_MENU));
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否移动客户端
	 */
	public boolean isMobileTerminal(){
		String[] types = type.split(",");
		for(String str:types){
			if(str.trim().equals(ResourceType.RESOURCE_TYPE_MOBILE));
			return true;
		}
		return false;
	}
}
