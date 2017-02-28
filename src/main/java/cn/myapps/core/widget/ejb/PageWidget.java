package cn.myapps.core.widget.ejb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.http.UrlUtil;

/**
 * 小工具、首页插件、首页元素
 * @author Happy
 *
 */
/**
 * @author Happy
 * 
 */
public class PageWidget extends ValueObject {

	private static final long serialVersionUID = -9089448969042443024L;

	/**
	 * 摘要类型
	 */
	public static final String TYPE_SUMMARY = "summary";
	/**
	 * 快速入口（内部系统链接类型）
	 */
	public static final String TYPE_LINK = "link";
	/**
	 * 链接内容（显示指定URL的页面内容）
	 */
	public static final String TYPE_PAGE = "page";
	/**
	 * 视图类型
	 */
	public static final String TYPE_VIEW = "view";
	/**
	 * 按钮操作类型
	 */
	public static final String TYPE_ACTIVITY = "activity";
	/**
	 * 报表类型
	 */
	public static final String TYPE_REPORT = "report";
	
	/**
	 * 自定义报表
	 */
	public static final String TYPE_CUSTOMIZE_REPORT = "customizeReport";
	/**
	 * 润乾类型
	 */
	public static final String TYPE_RUNQUAN_REPORT = "runquanReport";
	
	/**通知公告类型的系统Widget**/
	public static final String TYPE_SYSTEM_ANNOUNCEMENT = "system_announcement";
	/**流程处理类型的系统Widget**/
	public static final String TYPE_SYSTEM_WORKFLOW = "system_workflow";
	/**天气类型的系统Widget**/
	public static final String TYPE_SYSTEM_WEATHER = "system_weather";
	/**流程仪表分析类型**/
	public static final String TYPE_WORKFLOW_ANALYZER = "workflow_analyzer";
	
	/**IScript计算值类型**/
	public static final String TYPE_ISCRIPT = "iscript";
	
	public static final int AUTH_MODE_ALL_ROLES = 0;
	public static final int AUTH_MODE_SOME_ROLES = 1;
	
	

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 所属模块
	 */
	private String moduleid;

	/**
	 * 内容
	 */
	private String actionContent;
	/**
	 * 请求参数
	 */
	private String queryString;

	/**
	 * 宽
	 */
	private String width;

	/**
	 * 高
	 */
	private String height;

	/**
	 *授权方式(授权给所有角色|授权给选定角色)
	 */
	private int authMode;

	/**
	 * 获得授权使用的角色Id
	 */
	private String authRolesId;
	
	/**
	 * 获得授权使用的角色名称
	 */
	private String authRolesName;

	/**
	 * 是否发布
	 */
	private boolean published;
	
	/**
	 * 图标
	 */
	private String icon;
	
	/**
	 * 标题颜色
	 */
	private String titleColor;

	/**
	 * 标题背景颜色
	 */
	private String titleBColor;
	
	/**
	 * 图标形式显示
	 */
	private boolean iconShow;
	
	/**
	 * 排序号
	 */
	private Integer orderno;

	public boolean getIconShow() {
		return iconShow;
	}

	public void setIconShow(boolean iconShow) {
		this.iconShow = iconShow;
	}

	public String getTitleBColor() {
		return titleBColor;
	}

	public void setTitleBColor(String titleBColor) {
		this.titleBColor = titleBColor;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModuleid() {
		return moduleid;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	public String getActionContent() {
		return actionContent;
	}

	public void setActionContent(String actionContent) {
		this.actionContent = actionContent;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public int getAuthMode() {
		return authMode;
	}

	public void setAuthMode(int authMode) {
		this.authMode = authMode;
	}


	public boolean isPublished() {
		return published;
	}
	
	public boolean getPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getAuthRolesId() {
		return authRolesId;
	}

	public void setAuthRolesId(String authRolesId) {
		this.authRolesId = authRolesId;
	}

	public String getAuthRolesName() {
		return authRolesName;
	}

	public void setAuthRolesName(String authRolesName) {
		this.authRolesName = authRolesName;
	}
	
	public static PageWidget newSystemWidget(String type){
		PageWidget widget = new PageWidget();
		widget.setId(type);
		widget.setType(type);
		if(TYPE_SYSTEM_ANNOUNCEMENT.equals(type)){
			widget.setName("{*[cn.myapps.core.widget.type.announcement]*}");
		}
		if(TYPE_SYSTEM_WORKFLOW.equals(type)){
			widget.setName("{*[cn.myapps.core.widget.type.workflow]*}");
		}
		if(TYPE_SYSTEM_WEATHER.equals(type)){
			widget.setName("{*[cn.myapps.core.widget.type.weather]*}");
		}
		
		return widget;
	}

	public String toUrl(ParamsTable params, WebUser user) {
		if (TYPE_SUMMARY.equals(type)) {
			return toSummaryUrl(params.getContextPath());
		} else if (TYPE_LINK.equals(type)) {
			return toLinkUrl(params, user);
		} else if (TYPE_VIEW.equals(type)) {
			return toViewUrl(params, user);
		} else if (TYPE_REPORT.equals(type)) {
			return toReportUrl(params, user);
		} else if (TYPE_RUNQUAN_REPORT.equals(type)) {
			return toRunquanReportUrl(params, user);
		}

		return "";

	}

	private String toSummaryUrl(String contentPath) {
		StringBuffer url = new StringBuffer();
		try {
			SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory
					.createProcess(SummaryCfgProcess.class);
			SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess
					.doView(getActionContent());
			if (SummaryCfgVO.SCOPE_PENDING == summaryCfg.getScope()) {// 代办
				url.append("").append(
						"/portal/dynaform/document/pendinglist.action?")
						.append("summaryCfgId=").append(getActionContent())
						.append("&application=").append(getApplicationid())
						.append("&datatime=").append(new Date().getTime());
			} else if (SummaryCfgVO.SCOPE_CIRCULATOR == summaryCfg.getScope()) {// 代阅
				url
						.append(contentPath)
						.append(
								"/portal/workflow/storage/runtime/circulatorlist.action?")
						.append("summaryCfgId=").append(getActionContent())
						.append("&application=").append(getApplicationid())
						.append("&datatime=").append(new Date().getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url.toString();
	}

	private String toLinkUrl(ParamsTable params, WebUser user) {
		StringBuffer url = new StringBuffer();
		String contentPath = params.getContextPath();
		url.append(contentPath + "/portal/"
				+ params.getParameterAsString("skinType")
				+ "/widget.jsp?_widgetId=" + id + "&_title="
				+ Security.bytesToHexString(name.getBytes()) + "&_height="
				+ height + "&_width=" + width + "&_targetUrl=");
		url.append(contentPath);
		if (!actionContent.startsWith("/")) {
			url.append("/");
		}
		url.append(UrlUtil.parameterize(actionContent, "application",
				this.applicationid));
		System.out.println(url.toString());
		return url.toString();
	}

	private String toExtLinkUrl(ParamsTable params, WebUser user) {
		StringBuffer url = new StringBuffer();
		if (!actionContent.toLowerCase().startsWith("http://")) {
			actionContent = "http://" + actionContent;
		}
		String contentPath = params.getContextPath();
		url.append(contentPath + "/portal/"
				+ params.getParameterAsString("skinType")
				+ "/widget.jsp?_widgetId=" + id + "&_title="
				+ Security.bytesToHexString(name.getBytes()) + "&_height="
				+ height + "&_width=" + width + "&_targetUrl=");
		url.append(actionContent);
		System.out.println(url.toString());
		return url.toString();
	}

	private String toViewUrl(ParamsTable params, WebUser user) {
		StringBuffer url = new StringBuffer();
		String contentPath = params.getContextPath();
		url.append(contentPath + "/portal/"
				+ params.getParameterAsString("skinType")
				+ "/widget.jsp?_widgetId=" + id + "&_title="
				+ Security.bytesToHexString(name.getBytes()) + "&_height="
				+ height + "&_width=" + width + "&_targetUrl=");
		String tempUrl = contentPath
				+ "/portal/dynaform/view/displayViewWithPermission.action";
		tempUrl = UrlUtil.parameterize(tempUrl, "_viewid", actionContent);
		tempUrl = UrlUtil.parameterize(tempUrl, "clearTemp", "true");
		tempUrl = UrlUtil.parameterize(tempUrl, "application", applicationid);
		try {
			url.append(URLEncoder.encode(tempUrl, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url.toString();
	}

	private String toReportUrl(ParamsTable params, WebUser user) {
		StringBuffer url = new StringBuffer();
		String contentPath = params.getContextPath();
		url.append(contentPath + "/portal/"
				+ params.getParameterAsString("skinType")
				+ "/widget.jsp?_widgetId=" + id + "&_title="
				+ Security.bytesToHexString(name.getBytes()) + "&_height="
				+ height + "&_width=" + width + "&_targetUrl=");
		if (actionContent.startsWith("$/")) {
			url.append(contentPath + "/");
			url.append(UrlUtil.parameterize(actionContent.substring(2,
					actionContent.length()), "application", applicationid));
		} else {
			String tempUrl = contentPath
					+ "/portal/report/crossreport/runtime/runreport.action";
			tempUrl = UrlUtil.parameterize(tempUrl, "reportId", actionContent);
			tempUrl = UrlUtil.parameterize(tempUrl, "application",
					applicationid);
			url.append(tempUrl);
		}
		return url.toString();
	}

	private String toRunquanReportUrl(ParamsTable params, WebUser user) {
		StringBuffer url = new StringBuffer();
		String contentPath = params.getContextPath();
		url.append(contentPath + "/portal/"
				+ params.getParameterAsString("skinType")
				+ "/widget.jsp?_widgetId=" + id + "&_title="
				+ Security.bytesToHexString(name.getBytes()) + "&_height="
				+ height + "&_width=" + width + "&_targetUrl=");
		String tempUrl = contentPath
				+ "/portal/share/report/runqianreport/content4homepage.jsp?_widgetId="
				+ id;
		url.append(tempUrl);
		return url.toString();
	}

	public Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}

}
