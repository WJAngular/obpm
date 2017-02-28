package cn.myapps.core.user.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Environment;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.homepage.ejb.Reminder;
import cn.myapps.core.page.ejb.Page;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.style.repository.ejb.StyleRepositoryVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.core.widget.ejb.PageWidgetProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

public class UserDefined extends ValueObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * REGULAR_MODE为常规模式 CUSTOMIZE_MODE为自定义模式
	 */
	public final static int REGULAR_MODE = 16, CUSTOMIZE_MODE = 256;

	public final static int IS_DEFINED = 1, ISNOT_DEFINED = 0;

	private String layoutType;

	private String description;

	private Collection<Reminder> reminders;

	// private String roles;

	/**
	 * REGULAR_MODE为常规模式 CUSTOMIZE_MODE为自定义模式
	 */
	private String id;

	private String name;

	private String type;

	private String displayTo;

	private String roleIds;

	private String roleNames;

	private String userId;

	private String creator;

	private String applicationid;

	private String templateStyle;

	private String templateElement;

	private String homepageId;

	private Set<SummaryCfgVO> summaryCfgs;
	/**
	 * 定制模式
	 */
	private Integer defineMode = REGULAR_MODE;

	private String templateContext;

	private StyleRepositoryVO style;

	private boolean published;

	private Integer usedDefined = ISNOT_DEFINED;

	public String getTemplateContext() {
		return templateContext;
	}

	public void setTemplateContext(String templateContext) {
		this.templateContext = templateContext;
	}

	public String getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	public String getTemplateElement() {
		return templateElement;
	}

	public void setTemplateElement(String templateElement) {
		this.templateElement = templateElement;
	}

	public String getTemplateStyle() {
		return templateStyle;
	}

	public void setTemplateStyle(String templateStyle) {
		this.templateStyle = templateStyle;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHomepageId() {
		return homepageId;
	}

	public void setHomepageId(String homepageId) {
		this.homepageId = homepageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDisplayTo() {
		return displayTo;
	}

	public void setDisplayTo(String displayTo) {
		this.displayTo = displayTo;
	}

	public StyleRepositoryVO getStyle() {
		return style;
	}

	public void setStyle(StyleRepositoryVO style) {
		this.style = style;
	}

	public static int getREGULAR_MODE() {
		return REGULAR_MODE;
	}

	public static int getCUSTOMIZE_MODE() {
		return CUSTOMIZE_MODE;
	}

	public Integer getDefineMode() {
		if (this.defineMode == null)
			this.defineMode = REGULAR_MODE;
		return this.defineMode;
	}

	public void setDefineMode(Integer defineMode) {
		this.defineMode = defineMode;
	}

	public boolean getPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public Set<SummaryCfgVO> getSummaryCfgs() {
		return summaryCfgs;
	}

	public void setSummaryCfgs(Set<SummaryCfgVO> summaryCfgs) {
		this.summaryCfgs = summaryCfgs;
	}

	public Page getPage() {
		Page page = new Page();
		page.setId(this.getId());
		page.setSortId(this.getSortId());
		page.setName(this.getName());
		page.setApplicationid(this.getApplicationid());
		// page.setDiscription(this.getDescription());
		page.setRoles(this.getRoleIds());
		page.setRoleNames(this.getRoleNames());
		page.setTemplatecontext(this.getTemplateContext());
		page.setStyle(this.getStyle());

		return page;
	}

	public Page initPage(UserDefined userDefined) {
		if (userDefined != null) {
			Page page = new Page();
			page.setId(userDefined.getId());
			page.setSortId(userDefined.getSortId());
			page.setName(userDefined.getName());
			page.setApplicationid(userDefined.getApplicationid());
			page.setDiscription(userDefined.getDescription());
			page.setRoles(userDefined.getRoleIds());
			page.setRoleNames(userDefined.getRoleNames());
			page.setTemplatecontext(userDefined.getTemplateContext());
			page.setStyle(userDefined.getStyle());
			return page;
		}
		return null;
	}

	/**
	 * 先判断是否有前台自定义首页， 否，则根据角色判断后台是否己定制默认首页 是，则调用方法生成html
	 * 
	 * @author jack
	 * @param params
	 * @param webUser
	 * @return String
	 * @throws Exception
	 */
	public String toHtml(ParamsTable params, WebUser webUser) throws Exception {
		String htmlBuilder = "";
		try {
			String applicationid = params.getParameterAsString("application");
			String userid = webUser.getId();
			UserDefinedProcess udprocss = (UserDefinedProcess) ProcessFactory
					.createProcess(UserDefinedProcess.class);

			ParamsTable params1 = new ParamsTable();
			// 获取当前用户自定义首页
			params1.setParameter("t_applicationid", applicationid);
			params1.setParameter("t_userId", userid);
			params1.setParameter("i_useddefined", IS_DEFINED);
			params1.setParameter("_orderby", "id");
			DataPackage<UserDefined> dataPackage = udprocss.doQuery(params1);
			if (dataPackage.rowCount > 0) {
				UserDefined userDefined = new UserDefined();
				for (Iterator<UserDefined> ite1 = dataPackage.datas.iterator(); ite1
						.hasNext();) {
					userDefined = (UserDefined) ite1.next();
				}
				return createTemplateElement(applicationid, userDefined,
						params, webUser);
			} else {
				// 无自定义首页时,获取后台定制的默认首页
				Collection<RoleVO> userRoles = webUser.getRoles();
				RoleVO roleVO = new RoleVO();
				params1 = new ParamsTable();
				params1.setParameter("t_applicationid", applicationid);
				params1.setParameter("n_published", true);
				params1.setParameter("_orderby", "id");
				DataPackage<UserDefined> dataPackage1 = udprocss
						.doQuery(params1);
				if (dataPackage1.rowCount > 0) {
					for (Iterator<UserDefined> ite1 = dataPackage1.datas
							.iterator(); ite1.hasNext();) {
						UserDefined userDefined = (UserDefined) ite1.next();
						// 判断是否适用于所有角色
						if ("1".equals(userDefined.getDisplayTo())) {
							return createTemplateElement(applicationid,
									userDefined, params, webUser);
						} else {
							// 获取某一首页的角色
							String roleIds = userDefined.getRoleIds();
							if (!StringUtil.isBlank(roleIds)) {
								String[] userRoleIds = roleIds.split(",");
								for (int i = 0; i < userRoleIds.length; i++) {
									if (userRoles.size() > 0) {
										for (Iterator<RoleVO> ite2 = userRoles
												.iterator(); ite2.hasNext();) {
											roleVO = (RoleVO) ite2.next();
											if (userRoleIds[i].equals(roleVO
													.getId())) {
												// 当前角色与 后台首页待办设置的角色
												// 相同时，返回此后台定制的首页待办信息
												return createTemplateElement(
														applicationid,
														userDefined, params,
														webUser);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return htmlBuilder;
	}

	/**
	 * 高级模式
	 * 
	 * @param params
	 * @param webUser
	 * @return String
	 * @throws Exception
	 */
	public String toHtml(Document doc, ParamsTable params, WebUser webUser)
			throws Exception {
		Page page = initPage(this);
		if (page != null) {
			return page.toHtml(doc, params, webUser);
		}
		return "";
	}

	/**
	 * 生成摘要元素
	 * 
	 * @param params
	 * @param userDefined
	 * @param applicationid
	 * @param htmlBuilder1
	 * @author jack
	 * @throws Exception
	 */
	private String createTemplateElement(String applicationid,
			UserDefined userDefined, ParamsTable params, WebUser user)
			throws Exception {
		StringBuilder htmlBuilder1 = new StringBuilder();
		if (userDefined.getDefineMode() == CUSTOMIZE_MODE) {
			// 高级模式
			if (!this.id.equals(userDefined.getId())) {
				this.id = userDefined.getId();
				this.name = userDefined.getName();
				this.templateContext = userDefined.getTemplateContext();
			}
			Page page = initPage(this);
			if (page != null) {
				return page.toHtml(params, user);
			}
		} else {
			// 基本模式
			String templateElement;
			String contextPath = Environment.getInstance().getContextPath();
			templateElement = userDefined.getTemplateElement();
			String skinType = "";

			// 获取皮肤参数
			if (user.getUserSetup() != null) {
				skinType = user.getUserSetup().getUserSkin();
			} else {
				DomainProcess domPro = (DomainProcess) ProcessFactory
						.createProcess(DomainProcess.class);
				DomainVO domainVO = (DomainVO) domPro
						.doView(user.getDomainid());
				skinType = domainVO.getSkinType();
			}

			if (skinType != null && !skinType.equals("")) {
				htmlBuilder1
						.append("<input id='userDefinedId' name='userDefinedId' type='hidden' value='"
								+ userDefined.getId() + "' />");
				htmlBuilder1
						.append("<input id='templateStyle' name='templateStyle' type='hidden' value='"
								+ userDefined.getTemplateStyle() + "' />");
				htmlBuilder1
						.append(
								"<textarea id='templateElement' name='templateElement' style='display:none'>")
						.append(userDefined.getTemplateElement()).append(
								"</textarea>");
			}

			if (!StringUtil.isBlank(templateElement)
					&& templateElement.length() > 1) {
				if (templateElement.startsWith("[")) {
					toWidgetsHTML(htmlBuilder1, skinType, params, user);
				} else {// 旧模板数据解析
					toOldWidgetsHTML(htmlBuilder1, skinType, params, user);
				}
			}
		}
		return htmlBuilder1.toString();
	}

	/**
	 * 构建首页小工具页面HTML代码
	 * 
	 * @param htmlBuilder1
	 * @param skinType
	 * @param params
	 * @param user
	 * @throws Exception
	 */
	private void toWidgetsHTML(StringBuilder htmlBuilder1, String skinType,
			ParamsTable params, WebUser user) throws Exception {
		Map<String, Collection<Map<String, String>>> widgetMap = new HashMap<String, Collection<Map<String, String>>>();
		Collection<Object> widgets = JsonUtil.toCollection(templateElement);
		for (Iterator iterator = widgets.iterator(); iterator.hasNext();) {
			Map<String, String> _widget = (Map<String, String>) iterator.next();
			String id = _widget.get("id");
			String title = _widget.get("title");
			String position = _widget.get("position");
			String color = _widget.get("color");

			Collection<Map<String, String>> list = widgetMap.get(position);
			if (list == null)
				list = new ArrayList<Map<String, String>>();
			list.add(_widget);
			widgetMap.put(position, list);
		}

		for (Iterator<String> iterator2 = widgetMap.keySet().iterator(); iterator2
				.hasNext();) {
			String position = iterator2.next();
			Collection<Map<String, String>> list = widgetMap.get(position);

			// 根据不同皮肤构建html
			if (skinType != null && !skinType.equals("")) {
				params.setParameter("skinType", skinType);
				htmlBuilder1.append("<div class='" + position + "'>");
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map<String, String> map = (Map<String, String>) iterator
							.next();
					String id = map.get("id");
					String title = map.get("title");
					String color = map.get("color");

					PageWidget widget = getWidget(id);
					if (widget == null) {
						continue;
					}
					htmlBuilder1.append("<div class='ElementDiv' value='" + id
							+ "'");

					// color
					if (!StringUtil.isBlank(color)) {
						htmlBuilder1.append(" color='").append(color).append(
								"'");
					} else {
						htmlBuilder1.append(" color='color-green'");
					}

					htmlBuilder1.append("src='").append(
							widget.toUrl(params, user));
					htmlBuilder1.append("' ></div>");
				}
				htmlBuilder1.append("</div>");
			}
		}
	}

	/**
	 *构建首页小工具页面HTML代码（旧模式，支持2.5sp10及以下版本）
	 * 
	 * @param htmlBuilder1
	 * @param skinType
	 * @param params
	 * @param user
	 * @throws Exception
	 * @deprecated
	 */
	private void toOldWidgetsHTML(StringBuilder htmlBuilder1, String skinType,
			ParamsTable params, WebUser user) throws Exception {

		templateElement = templateElement.substring(1,
				templateElement.length() - 1);
		// 获取各布局单元格和对应的元素
		String[] templateElements = (String[]) templateElement.split(",");
		for (int i = 0; i < templateElements.length; i++) {
			String[] templateElementSubs = (String[]) templateElements[i]
					.split(":");
			if (!StringUtil.isBlank(templateElementSubs[0])
					&& templateElementSubs[0].length() > 1) {
				// 模板单元格id
				String templateTdId = templateElementSubs[0].substring(1,
						templateElementSubs[0].length() - 1);
				// 单元格摘要Id和title
				String[] td_id_title_colors = templateElementSubs[1].split(";");

				String templateTdEle = td_id_title_colors[0].substring(1,
						td_id_title_colors[0].length() - 1);
				// 摘要id数组
				String[] widgetIds = templateTdEle.split("\\|");

				String[] widgetColors = td_id_title_colors.length > 2 ? td_id_title_colors[2]
						.substring(1, td_id_title_colors[2].length() - 1)
						.split("\\|")
						: new String[100];

				// 根据不同皮肤构建html
				if (skinType != null && !skinType.equals("")) {
					params.setParameter("skinType", skinType);
					htmlBuilder1.append("<div class='" + templateTdId + "'>");
					for (int j = 0; j < widgetIds.length; j++) {
						PageWidget widget = getWidget(widgetIds[j]);
						if (widget == null) {
							continue;
						}
						if (!"".equals(widgetIds[j])) {
							htmlBuilder1
									.append("<div class='ElementDiv' value='"
											+ widgetIds[j] + "'");

							// color
							if (widgetColors[j] != null) {
								htmlBuilder1.append(" color='").append(
										widgetColors[j]).append("'");
							} else {
								htmlBuilder1.append(" color='color-green'");
							}

							htmlBuilder1.append("src='").append(
									widget.toUrl(params, user));
							htmlBuilder1.append("' ></div>");

						}
					}
					htmlBuilder1.append("</div>");
				}
			}
		}
	}

	/**
	 * 查询摘要 有摘要则返回此摘要对象 否则返回null
	 * 
	 * @param summaryid
	 * @author jack
	 * @return summaryCfg
	 * @throws Exception
	 */
	public SummaryCfgVO summaryIdCheck(String summaryid)
			throws ClassNotFoundException {
		SummaryCfgVO summaryCfg = null;
		try {
			SummaryCfgProcess summaryCfgPro = (SummaryCfgProcess) ProcessFactory
					.createProcess(SummaryCfgProcess.class);
			if (!StringUtil.isBlank(summaryid))
				summaryCfg = (SummaryCfgVO) summaryCfgPro.doView(summaryid);
		} catch (Exception e) {
			System.err.println(e);
		}
		return summaryCfg;
	}

	private PageWidget getWidget(String id) throws Exception {
		if (StringUtil.isBlank(id))
			return null;
		PageWidget widget = null;
		SummaryCfgProcess summaryCfgPro = (SummaryCfgProcess) ProcessFactory
				.createProcess(SummaryCfgProcess.class);
		PageWidgetProcess process = (PageWidgetProcess) ProcessFactory
				.createProcess(PageWidgetProcess.class);
		widget = (PageWidget) process.doView(id);
		if (widget == null) {// 兼容旧数据
			SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgPro.doView(id);
			if (summaryCfg != null) {
				widget = new PageWidget();
				widget.setType(PageWidget.TYPE_SUMMARY);
				widget.setActionContent(id);
				widget.setApplicationid(summaryCfg.getApplicationid());
				widget.setName(summaryCfg.getTitle());
			}
		}
		return widget;
	}

	public Collection<SummaryCfgVO> getSummaryCfgByTemplateElement()
			throws Exception {
		Collection<SummaryCfgVO> result = new ArrayList<SummaryCfgVO>();
		String templateElement = this.getTemplateElement();
		if (!StringUtil.isBlank(templateElement)) {
			templateElement = templateElement.substring(1, templateElement
					.length() - 1);
			String[] templateElements = (String[]) templateElement.split(",");
			for (int i = 0; i < templateElements.length; i++) {
				String[] templateElementSubs = (String[]) templateElements[i]
						.split(":");

				if (!StringUtil.isBlank(templateElementSubs[0])) {
					// 摘要Id和title
					String[] summaryIds = templateElementSubs[1].split(";");
					String templateTdEle = summaryIds[0].substring(1,
							summaryIds[0].length() - 1);
					// 摘要id数组
					summaryIds = templateTdEle.split("\\|");
					if (summaryIds.length >= 1) {
						SummaryCfgProcess process = (SummaryCfgProcess) ProcessFactory
								.createProcess(SummaryCfgProcess.class);
						for (int j = 0; j < summaryIds.length; j++) {
							String summaryCfgid = summaryIds[j];
							if (!StringUtil.isBlank(summaryCfgid)) {
								SummaryCfgVO summaryCfg = (SummaryCfgVO) process
										.doView(summaryCfgid);
								if (summaryCfg != null) {
									result.add(summaryCfg);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 自定模式下生成html
	 * 
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String toHtmlOnCustomize_Mode(Page page, ParamsTable params,
			WebUser user) throws Exception {
		if (page != null)
			return page.toHtml(params, user);
		return "";
	}

	public String getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<Reminder> getReminders() {
		return reminders;
	}

	public void setReminders(Collection<Reminder> reminders) {
		this.reminders = reminders;
	}

	public static int getIS_DEFINED() {
		return IS_DEFINED;
	}

	public static int getISNOT_DEFINED() {
		return ISNOT_DEFINED;
	}

	public Integer getUsedDefined() {
		return usedDefined;
	}

	public void setUsedDefined(Integer usedDefined) {
		this.usedDefined = usedDefined;
	}

}
