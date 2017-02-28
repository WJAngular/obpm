package cn.myapps.core.dynaform.view.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.opensymphony.xwork2.ActionContext;

import cn.myapps.base.action.BaseHelper;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.form.ejb.DepartmentField;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.TreeDepartmentField;
import cn.myapps.core.dynaform.form.ejb.UserField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.form.ejb.ViewDialogField;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.dynaform.view.ejb.condition.FilterConditionParser;
import cn.myapps.core.dynaform.view.ejb.type.CalendarType;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.macro.runner.JsMessage;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;
import cn.myapps.util.web.DWRHtmlUtils;


public class ViewHelper extends BaseHelper<View> {
	private int displayType;

	// private static final Logger log = Logger.getLogger(ViewHelper.class);

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public ViewHelper() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(ViewProcess.class));
	}

	/**
	 * 返回全部符号
	 * 
	 * @return
	 */
	public static Map<String, String> getALL_SYMBOL() {
		return ALL_SYMBOL;
	}

	public static void setALL_SYMBOL(Map<String, String> all_symbol) {
		if (all_symbol != null)
			ALL_SYMBOL = all_symbol;
	}

	public static Map<String, String> getCOMPARE_SYMBOL() {
		return COMPARE_SYMBOL;
	}

	public static void setCOMPARE_SYMBOL(Map<String, String> compare_symbol) {
		if (compare_symbol != null)
			COMPARE_SYMBOL = compare_symbol;
	}

	public static Map<String, String> getOPERATOR_SYMBOL() {
		return OPERATOR_SYMBOL;
	}

	public static void setOPERATOR_SYMBOL(Map<String, String> operator_symbol) {
		if (operator_symbol != null)
			OPERATOR_SYMBOL = operator_symbol;
	}

	public static Map<String, String> getRELATION_SYMBOL() {
		return RELATION_SYMBOL;
	}

	public static void setRELATION_SYMBOL(Map<String, String> relation_symbol) {
		if (relation_symbol != null)
			RELATION_SYMBOL = relation_symbol;
	}

	private static Map<String, String> RELATION_SYMBOL = new LinkedHashMap<String, String>();

	private static Map<String, String> OPERATOR_SYMBOL = new LinkedHashMap<String, String>();

	private static Map<String, String> COMPARE_SYMBOL = new LinkedHashMap<String, String>();

	private static Map<String, String> ALL_SYMBOL = new LinkedHashMap<String, String>();

	static {
		RELATION_SYMBOL.put("AND", "AND");
		RELATION_SYMBOL.put("OR", "OR");

		OPERATOR_SYMBOL.put("+", "+");
		OPERATOR_SYMBOL.put("-", "-");
		OPERATOR_SYMBOL.put("*", "*");
		OPERATOR_SYMBOL.put("/", "/");

		COMPARE_SYMBOL.put("LIKE", "LIKE");
		COMPARE_SYMBOL.put(">", ">");
		COMPARE_SYMBOL.put(">=", ">=");
		COMPARE_SYMBOL.put("<", "<");
		COMPARE_SYMBOL.put("<=", "<=");
		COMPARE_SYMBOL.put("=", "=");
		COMPARE_SYMBOL.put("IN", "IN");
		COMPARE_SYMBOL.put("NOT IN", "NOT IN");

		ALL_SYMBOL.putAll(RELATION_SYMBOL);
		ALL_SYMBOL.putAll(OPERATOR_SYMBOL);
		ALL_SYMBOL.putAll(COMPARE_SYMBOL);
	}

	/**
	 * 根据所属模块以及应用标识查询,返回视图集合
	 * 
	 * @param application
	 *            应用标识
	 * @return 视图集合
	 * @throws Exception
	 */
	public Collection<View> get_viewList(String application) throws Exception {
		ViewProcess vp = (ViewProcess) ProcessFactory
				.createProcess((ViewProcess.class));
		return vp.getViewsByModule(this.getModuleid(), application);
	}

	/**
	 * 根据相关视图主键查询,返回样式 id
	 * 
	 * @param viewid
	 *            视图主键
	 * @return 样式 id
	 * @throws Exception
	 */
	public static String get_Styleid(String viewid) throws Exception {
		ViewProcess vp = (ViewProcess) ProcessFactory
				.createProcess((ViewProcess.class));
		View vw = (View) vp.doView(viewid);
		if (vw != null && vw.getStyle() != null) {
			return vw.getStyle().getId();
		} else
			return null;
	}

	/**
	 * 根据应用标识查询,返回树型菜单(Resource)集合
	 * 
	 * @param application
	 *            应用标识
	 * @return 树型菜单集合
	 * @throws Exception
	 */
	public Map<String, String> get_MenuTree(String application)
			throws Exception {
		ResourceProcess rp = (ResourceProcess) ProcessFactory
				.createProcess((ResourceProcess.class));
		Collection<ResourceVO> dc = rp.doSimpleQuery(null, application);
		Map<String, String> dm = rp.deepSearchMenuTree(dc, null, null, 0);
		return dm;
	}

	/**
	 * 根据所属模块以及应用标识查询,返回查询表单
	 * 
	 * @param application
	 *            应用标识
	 * @return 查询表单
	 * @throws Exception
	 */
	public Collection<Form> get_searchForm(String application) throws Exception {
		FormProcess fp = (FormProcess) ProcessFactory
				.createProcess((FormProcess.class));
		Collection<Form> searchForms = fp.getSearchFormsByModule(moduleid,
				application);
		return searchForms;
	}

	public String convertValuesMapToPage(Map<String, String> valuesMap)
			throws Exception {
		StringBuffer html = new StringBuffer();
		for (Iterator<Entry<String, String>> iter = valuesMap.entrySet()
				.iterator(); iter.hasNext();) {
			Entry<String, String> entry = iter.next();
			html.append("<input type='hidden' name='").append(entry.getKey())
					.append("' value='").append(entry.getValue()).append("'")
					.append(" />");
		}
		return html.toString();
	}

	/**
	 * 返回字符串为重定义后的html,以html显示视图
	 * 
	 * @param formid
	 *            表单
	 * @param viewid
	 *            视图
	 * @param actfield
	 *            按钮
	 * @param userid
	 *            user
	 * @param valuesMap
	 *            值
	 * @param application
	 *            应用标识
	 * @return 字符串为重定义后的html,以html显示视图
	 * @throws Exception
	 */
	public String displayViewHtml(String formid, String viewid,
			String actfield, String userid, Map<String, String> valuesMap,
			String application) throws Exception {
		try {
			// //PersistenceUtils.getSessionSignal().sessionSignal++;
			DocumentProcess dp = (DocumentProcess) ProcessFactory
					.createProcess(DocumentProcess.class);

			ViewProcess vp = (ViewProcess) ProcessFactory
					.createProcess(ViewProcess.class);

			View view = (View) vp.doView(viewid);

			FormProcess fp = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);

			Form form = (Form) fp.doView(formid);

			// 显示Column
			StringBuffer html = new StringBuffer();
			html
					.append("<table width='100%' style='position:relative; z-index:1'>");
			html.append("<tr>");
			Collection<Column> columns = view.getColumns();
			for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
				Column clm = iter.next();
				html
						.append("<td")
						.append(
								clm.getWidth() != null
										&& clm.getWidth().trim().length() > 0 ? " width='"
										+ clm.getWidth() + "'"
										: "").append(">").append(clm.getName())
						.append("</td>");
			}

			String js = view.getFilterScript();
			if (js != null && js.trim().length() > 0) {
				ParamsTable params = new ParamsTable();
				params.putAll(valuesMap);

				UserProcess up = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				UserVO uservo = (UserVO) up.doView(userid);
				WebUser user = new WebUser(uservo);
				ArrayList<ValidateMessage> errors = new ArrayList<ValidateMessage>();

				Document currdoc = new Document();
				currdoc = form.createDocument(currdoc, params, false, user);

				IRunner runner = JavaScriptFactory.getInstance(params
						.getSessionid(), application);
				runner.initBSFManager(currdoc, params, user, errors);

				StringBuffer label = new StringBuffer();
				label.append("VIEW(").append(view.getId()).append(
						")." + view.getName()).append(".FilterScript");

				Object result = runner.run(label.toString(), js);

				if (result != null && result instanceof String) {
					String dql = (String) result;
					DataPackage<Document> datas = dp.queryByDQL(dql, user
							.getDomainid());
					// 显示数据
					if (datas != null && datas.datas != null)
						for (Iterator<Document> iter = datas.datas.iterator(); iter
								.hasNext();) {
							Document doc = iter.next();
							runner.initBSFManager(doc, params, user, errors);
							html
									.append("<tr bgcolor='#999999' style='cursor:hand' onclick='document.getElementById(\""
											+ actfield
											+ "\").value=\""
											+ doc.getId()
											+ "\";dy_refresh(\""
											+ actfield + "\");cClick();'>");
							for (Iterator<Column> iter2 = columns.iterator(); iter2
									.hasNext();) {
								Column col = iter2.next();
								if (col.getType() != null
										&& col.getType().equals(
												Column.COLUMN_TYPE_SCRIPT)) {

									StringBuffer clabel = new StringBuffer();
									clabel.append("VIEW").append(".").append(
											view.getName()).append(".COLUMN(")
											.append(col.getId()).append(
													")." + col.getName())
											.append("ValueScript");

									html.append("<td nowarp>").append(
											runner.run(clabel.toString(), col
													.getValueScript())).append(
											"</td>");
								} else if (col.getType() != null
										&& col.getType().equals(
												Column.COLUMN_TYPE_FIELD)) {
									html.append("<td nowarp>").append(
											doc.getItemValueAsString(col
													.getFieldName())).append(
											"</td>");
								}
							}
							html.append("</tr>");
						}
				}
			}

			html.append("</table>");
			return html.toString();

		} finally {
			// //PersistenceUtils.getSessionSignal().sessionSignal--;
			PersistenceUtils.closeSession();
		}
	}

	public Map<String, String> get_viewListByModules(String moduleId)
			throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		
		ViewProcess vp = (ViewProcess) ProcessFactory
				.createProcess((ViewProcess.class));
		Collection<View> viewList = vp.getViewsByModule(moduleId, null);
		if (viewList != null) {
			for (Iterator<View> iterator = viewList.iterator(); iterator
					.hasNext();) {
				View view = iterator.next();
				map.put(view.getId(), view.getName());
			}
		}
		return map;
	}

	/**
	 * 给view生成的排序的checkbox
	 * 
	 * @param moduleId
	 * @param divid
	 * @return
	 * @throws Exception
	 */
	public String getViewNameCheckBox(String moduleId, String divid,
			String application) throws Exception {
		ViewProcess fp = (ViewProcess) ProcessFactory
				.createProcess(ViewProcess.class);
		Collection<View> col = fp.getViewsByModule(moduleId, application);
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (col != null) {
			for (Iterator<View> iter = col.iterator(); iter.hasNext();) {
				View view = iter.next();
				map.put(view.getId(), view.getName());
			}
		}
		String[] str = new String[10];
		return DWRHtmlUtils.createFiledCheckbox(map, divid, str);
	}

	public Map<String, String> getSystemVariable() throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");

		map.put(FilterConditionParser.SYSVAR_CURRACC,
				"{*[cn.myapps.core.dynaform.view.current_user_account]*}");
		map.put(FilterConditionParser.SYSVAR_CURRNAME,
				"{*[cn.myapps.core.dynaform.view.current_user_name]*}");
		map.put(FilterConditionParser.SYSVAR_CURRUSERID,
				"{*[cn.myapps.core.dynaform.view.current_user_id]*}");
		map.put(FilterConditionParser.SYSVAR_CURRROLEID,
				"{*[cn.myapps.core.dynaform.view.current_user_role_id]*}");
		map.put(FilterConditionParser.SYSVAR_CURRDEPTID,
				"{*[cn.myapps.core.dynaform.view.current_user_department_id]*}");
		map.put(FilterConditionParser.SYSVAR_SESSION, "{*[cn.myapps.core.dynaform.view.session]*}");

		return map;
	}

	/**
	 * 返回月视图信息组成的HTML字串
	 * 
	 * @param view
	 *            视图对象
	 * @param params
	 *            参数体对象
	 * @param user
	 *            当前用户对象
	 * @param applicationid
	 *            软件ID
	 * @param yearIndex
	 *            年
	 * @param monthIndex
	 *            月
	 * @return 返回月视图信息组成的HTML字串
	 * @throws Exception
	 */
	public String toMonthHtml(View view, ParamsTable params, WebUser user,
			String applicationid, int yearIndex, int monthIndex,
			boolean isPreview) throws Exception {
		StringBuffer buf = new StringBuffer();
		String rootPath = params.getContextPath();
		buf
				.append("<table moduleType=\"calendarView\" style=\"display:none;\" calendarViewType=\"month\" dayTitle=\"{*[Day]*}{*[View]*}\" dayAlt=\"{*[Day]*}{*[View]*}\""
						+ "weekTitle=\"{*[Week]*}{*[View]*}\" weekAlt=\"{*[Week]*}{*[View]*}\" monthTitle=\"{*[Month]*}{*[View]*}\">");
		buf.append("<tr>");
		buf.append("<td>");
		buf.append(tHead(params.getParameterAsString("viewMode"), yearIndex,
				monthIndex, yearIndex + "-" + monthIndex, rootPath));
		buf.append(tMonthBodyHtml(view, params, user, applicationid, yearIndex,
				monthIndex, displayType, isPreview ? false : true));
		buf.append("</td>");
		buf.append("</tr>");
		buf.append("</table>");
		return buf.toString();
	}

	private String tHead(String viewMode, int year, int month,
			String headString, String rootPath) {
		StringBuffer buf = new StringBuffer();
		String llab1 = "previousYear";
		String lab1 = "previousMonth";
		String llab2 = "nextYear";
		String lab2 = "nextMonth";
		if ("WEEKVIEW".equals(viewMode)) {
			llab1 = "previousMonth";
			lab1 = "previousWeek";
			llab2 = "nextMonth";
			lab2 = "nextWeek";
		} else if ("DAYVIEW".equals(viewMode)) {
			llab1 = "previousMonth";
			lab1 = "previousDay";
			llab2 = "nextMonth";
			lab2 = "nextDay";
		}

		buf.append("<table subTable=\"tHead\" viewMode=\"" ).append( viewMode
		).append( "\" llab1=\"" ).append( llab1 ).append( "\" viewMode=\"" ).append( viewMode ).append( "\" "
		).append( "lab1=\"" ).append( lab1 ).append( "\" headString=\"" ).append( headString
		).append( "\" lab2=\"" ).append( lab2 ).append( "\" llab2=\"" ).append( llab2 ).append( "\""
		).append( " title1=\"{*[" ).append( llab1 ).append( "]*}\" alt1=\"{*[" ).append( llab1
		).append( "]*}\" title2=\"{*[" ).append( lab1 ).append( "]*}\" " ).append( "alt2=\"{*[" ).append( lab1
		).append( "]*}\" title3=\"{*[" ).append( lab2 ).append( "]*}\" alt3=\"{*[" ).append( lab2
		).append( "]*}\" title4=\"{*[" ).append( llab2 ).append( "]*}\" alt4=\"{*[" ).append( llab2
		).append( "]*}\">");
		buf.append("<tr><td></td></tr>");
		buf.append("</table>");
		return buf.toString();
	}

	/**
	 * 
	 * @param yearIndex
	 * @param monthIndex
	 * @param href
	 * @param dayInfo
	 * @return
	 * @throws Exception
	 */
	public String tMonthBodyHtml(View view, ParamsTable params, WebUser user,
			String applicationid, int yearIndex, int monthIndex, int viewType,
			boolean isShowDayInfo) throws Exception {
		Calendar thisMonth = CalendarVO.getThisMonth(yearIndex, monthIndex - 1);
		StringBuffer htmlBuilder = new StringBuffer();
		thisMonth.set(Calendar.DAY_OF_MONTH, 1);
		int firstIndex = thisMonth.get(Calendar.DAY_OF_WEEK) - 1;
		int maxIndex = thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		final Calendar cld = thisMonth;
		cld.set(Calendar.HOUR_OF_DAY, 0);
		cld.set(Calendar.MINUTE, 0);
		cld.set(Calendar.SECOND, 0);
		Date stDate = cld.getTime();
		cld.set(Calendar.DAY_OF_MONTH, cld
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		cld.set(Calendar.HOUR_OF_DAY, 23);
		cld.set(Calendar.MINUTE, 59);
		cld.set(Calendar.SECOND, 59);
		Collection<Document> datas = getDatas(view, params, user,
				applicationid, stDate, cld.getTime());
		IRunner jsrun = JavaScriptFactory.getInstance(params.getSessionid(),
				view.getApplicationid());
		htmlBuilder
				.append("<table subTable=\"tMonthBody\" Sunday=\"{*[Sunday]*}\" Monday=\"{*[Monday]*}\""
					).append( " Tuesday=\"{*[Tuesday]*}\" Wednesday=\"{*[Wednesday]*}\" Thursday=\"{*[Thursday]*}\" "
					).append( "Friday=\"{*[Friday]*}\" Saturday=\"{*[Saturday]*}\""
					).append( " viewType=\""
					).append( viewType
					).append( "\" viewAction=\""
					).append( ViewAction.DO_DIALOG_VIEW ).append("\"><tr>");
		int day;// 日
		for (int i = 0; i < 6; i++) {
			htmlBuilder.append("<tr subTable=\"trBody\">");
			for (int j = 0; j < 7; j++) {
				day = (i * 7 + j) - firstIndex + 1;
				htmlBuilder.append("<td subTable=\"tdBody\" day=" ).append( day
				).append( " maxIndex=\"" ).append( maxIndex ).append( "\" viewType=\""
				).append( viewType ).append( "\" viewAction=\""
				).append( ViewAction.DO_DIALOG_VIEW + "\" yearIndex=\""
				).append( yearIndex ).append( "\" monthIndex=\"" ).append( monthIndex
				).append( "\" isShowDayInfo=\"" ).append( isShowDayInfo ).append( "\"");
				if (day > 0 && day <= maxIndex) {
					String dayInfo = getDayViewList(jsrun, view, datas, params,
							user, thisMonth, day, viewType, yearIndex,
							monthIndex);
					String taskInfo = getTaskViewList(jsrun, view, datas,
							params, user, thisMonth, day, viewType, yearIndex,
							monthIndex);
					htmlBuilder.append("><div>"
							+ (isShowDayInfo ? dayInfo : "") + "</div><div>"
							+ (isShowDayInfo ? taskInfo : "") + "</div></td>");
				} else {
					htmlBuilder.append("></td>");
				}
			}
			htmlBuilder.append("</tr>");
		}
		htmlBuilder.append("</table>");
		return htmlBuilder.toString();
	}

	public String tWeekBodyHtml(View view, ParamsTable params, WebUser user,
			String applicationid, final Calendar cld, int yearIndex,
			int monthIndex, int viewType, boolean isShowInfo) throws Exception {
		StringBuffer htmlBuilder = new StringBuffer();
		Date startDate = cld.getTime();
		cld.add(Calendar.DAY_OF_MONTH, 7);
		Collection<Document> datas = getDatas(view, params, user,
				applicationid, startDate, cld.getTime());
		cld.add(Calendar.DAY_OF_MONTH, -7);
		IRunner jsrun = JavaScriptFactory.getInstance(params.getSessionid(),
				view.getApplicationid());

		htmlBuilder
				.append("<table subTable=\"tWeekBody\" week=\"{*[Week]*}\" content=\"{*[Content]*}\" Sunday=\"{*[Sunday]*}\""
						+ "Monday=\"{*[Monday]*}\" Tuesday=\"{*[Tuesday]*}\" Wednesday=\"{*[Wednesday]*}\""
						+ "Thursday=\"{*[Thursday]*}\" Friday=\"{*[Friday]*}\" Saturday=\"{*[Saturday]*}\"><tr>");
		for (int i = 0; i < 7; i++) {
			switch (i) {
			case 0:
				htmlBuilder.append("<td timeFormat1=\""
					).append( DateUtil.format(cld.getTime(), "yyyy-MM-dd")
					).append( "\" dateUtil=\"Sd\"");
				break;
			case 1:
				htmlBuilder.append("<td timeFormat2=\""
					).append( DateUtil.format(cld.getTime(), "yyyy-MM-dd")
					).append( "\" dateUtil=\"Md\"");
				break;
			case 2:
				htmlBuilder.append("<td timeFormat3=\""
					).append( DateUtil.format(cld.getTime(), "yyyy-MM-dd")
					).append( "\" dateUtil=\"Td\"");
				break;
			case 3:
				htmlBuilder.append("<td timeFormat4=\""
					).append( DateUtil.format(cld.getTime(), "yyyy-MM-dd")
					).append("\" dateUtil=\"Wd\"");
				break;
			case 4:
				htmlBuilder.append("<td timeFormat5=\""
					).append( DateUtil.format(cld.getTime(), "yyyy-MM-dd")
					).append( "\" dateUtil=\"T4d\"");
				break;
			case 5:
				htmlBuilder.append("<td timeFormat6=\""
					).append( DateUtil.format(cld.getTime(), "yyyy-MM-dd")
					).append( "\" dateUtil=\"Fd\"");
				break;
			case 6:
				htmlBuilder.append("<td timeFormat7=\""
					).append( DateUtil.format(cld.getTime(), "yyyy-MM-dd")
					).append( "\" dateUtil=\"S6d\"");
				break;
			}
			htmlBuilder.append(" isShowInfo=\"" ).append( isShowInfo ).append( "\">");
			String info = getDayViewList(jsrun, view, datas, params, user, cld,
					cld.get(Calendar.DAY_OF_MONTH), viewType, yearIndex,
					monthIndex);
			htmlBuilder.append(isShowInfo ? info : "");

			cld.add(Calendar.DAY_OF_MONTH, 1);
		}
		htmlBuilder.append("</td></tr></table>");
		return htmlBuilder.toString();
	}

	/**
	 * 返回周视图信息组成的HTML字串
	 * 
	 * @param view
	 *            视图对象
	 * @param params
	 *            参数体对象
	 * @param user
	 *            当前用户对象
	 * @param applicationid
	 *            软件ID
	 * @param yearIndex
	 *            年
	 * @param monthIndex
	 *            月
	 * @param dayIndex
	 *            日（基准日）
	 * @return 返回周视图信息组成的HTML字串
	 * @throws Exception
	 */
	public String toWeekHtml(View view, ParamsTable params, WebUser user,
			String applicationid, int yearIndex, int monthIndex, int dayIndex,
			boolean isPreview) throws Exception {
		StringBuffer buf = new StringBuffer();
		final Calendar cld = Calendar.getInstance();
		cld.set(Calendar.YEAR, yearIndex);
		cld.set(Calendar.MONTH, monthIndex - 1);
		cld.set(Calendar.DAY_OF_MONTH, dayIndex);
		int firstIndex = cld.get(Calendar.DAY_OF_WEEK) - 1;
		cld.add(Calendar.DAY_OF_MONTH, -firstIndex);
		cld.set(Calendar.HOUR_OF_DAY, 0);
		cld.set(Calendar.MINUTE, 0);
		cld.set(Calendar.SECOND, 0);
		Date stDate = cld.getTime();
		String body = tWeekBodyHtml(view, params, user, applicationid, cld,
				yearIndex, monthIndex, displayType, isPreview ? false : true);
		cld.add(Calendar.DAY_OF_MONTH, -1);

		String rootPath = params.getContextPath();
		buf
				.append("<table moduleType=\"calendarView\" style=\"display:none;\" calendarViewType=\"week\" dayTitle=\"{*[Day]*}{*[View]*}\" dayAlt=\"{*[Day]*}{*[View]*}\""
						+ "weekTitle=\"{*[Week]*}{*[View]*}\" weekAlt=\"{*[Week]*}{*[View]*}\" monthTitle=\"{*[Month]*}{*[View]*}\">");
		buf.append("<tr>");
		buf.append("<td>");
		buf.append(tHead(params.getParameterAsString("viewMode"), yearIndex,
				monthIndex, DateUtil.format(stDate, "yyyy-MM-dd") + "~"
						+ DateUtil.format(cld.getTime(), "yyyy-MM-dd"),
				rootPath));
		buf.append("</td>");
		buf.append("<td>");
		buf.append(body);
		buf.append("</td>");
		buf.append("</tr>");
		buf.append("</table>");
		return buf.toString();
	}

	/**
	 * 返回日视图信息组成的HTML字串
	 * 
	 * @param view
	 *            视图对象
	 * @param params
	 *            参数体对象
	 * @param user
	 *            当前用户对象
	 * @param applicationid
	 *            软件ID
	 * @param yearIndex
	 *            年
	 * @param monthIndex
	 *            月
	 * @param dayIndex
	 *            日
	 * @return 返回日视图信息组成的HTML字串
	 * @throws Exception
	 */
	public String toDayHtml(View view, ParamsTable params, WebUser user,
			String applicationid, int yearIndex, int monthIndex, int dayIndex,
			boolean isPreview) throws Exception {
		StringBuffer buf = new StringBuffer();
		String rootPath = params.getContextPath();
		buf
				.append("<table moduleType=\"calendarView\" style=\"display:none;\" calendarViewType=\"day\" dayTitle=\"{*[Day]*}{*[View]*}\" dayAlt=\"{*[Day]*}{*[View]*}\""
						+ "weekTitle=\"{*[Week]*}{*[View]*}\" weekAlt=\"{*[Week]*}{*[View]*}\" monthTitle=\"{*[Month]*}{*[View]*}\">");
		buf.append("<tr>");
		buf.append("<td>");
		buf.append(tHead(params.getParameterAsString("viewMode"), yearIndex,
				monthIndex, yearIndex + "-" + monthIndex + "-" + dayIndex,
				rootPath));
		buf.append("</td>");
		buf.append("<td>");
		buf.append(tDayBodyHtml(view, params, user, applicationid, yearIndex,
				monthIndex, dayIndex, displayType, isPreview ? false : true));
		buf.append("</td>");
		buf.append("</tr>");
		buf.append("</table>");
		return buf.toString();
	}

	public String tDayBodyHtml(View view, ParamsTable params, WebUser user,
			String applicationid, int yearIndex, int monthIndex, int dayIndex,
			int viewType, boolean isShowInfo) throws Exception {
		Calendar cld = Calendar.getInstance();
		StringBuffer htmlBuilder = new StringBuffer();
		cld.set(Calendar.YEAR, yearIndex);
		cld.set(Calendar.MONTH, monthIndex - 1);
		cld.set(Calendar.DAY_OF_MONTH, dayIndex);
		cld.set(Calendar.HOUR_OF_DAY, 0);
		cld.set(Calendar.MINUTE, 0);
		cld.set(Calendar.SECOND, 0);
		Date stDate = cld.getTime();
		cld.add(Calendar.DAY_OF_MONTH, 1);
		Collection<Document> datas = getDatas(view, params, user,
				applicationid, stDate, cld.getTime());
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),
				view.getApplicationid());
		cld.add(Calendar.DAY_OF_MONTH, -1);
		int sTime = 8;
		htmlBuilder.append("<table sTime=\"" ).append( sTime
			).append( "\" subTable=\"tDayBody\" isShowInfo=\"" ).append( isShowInfo
			).append( "\" time=\"{*[Time]*}\" content=\"{*[Content]*}\">");
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td subTable=\"tdView1\">");
		htmlBuilder.append(isShowInfo ? getDayTimeList(runner, view, datas,
				params, user, cld, 8, viewType) : "");
		htmlBuilder.append("</td>");
		htmlBuilder.append("</tr>");
		cld.set(Calendar.HOUR_OF_DAY, 8);

		for (int i = 0; i < 11; i++) {
			htmlBuilder.append("<tr>");
			htmlBuilder.append("<td>"+ (sTime++) +":00 -- "+ sTime + ":00</td><td subTable=\"tdView2\">");
			htmlBuilder.append(isShowInfo ? getDayTimeList(runner, view, datas,
					params, user, cld, 1, viewType) : "");
			htmlBuilder.append("</td>");
			htmlBuilder.append("</tr>");
			cld.set(Calendar.HOUR_OF_DAY, sTime);
		}
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td subTable=\"tdView3\">");
		htmlBuilder.append(isShowInfo ? getDayTimeList(runner, view, datas,
				params, user, cld, 8, viewType) : "");
		htmlBuilder.append("</td>");
		htmlBuilder.append("</tr>");
		htmlBuilder.append("</table>");
		return htmlBuilder.toString();
	}

	private String getDayTimeList(IRunner runner, View view,
			Collection<Document> datas, ParamsTable params, WebUser user,
			final Calendar cld, int step, int viewType) {
		StringBuffer buf = new StringBuffer();
		String templateForm = "";
		if (View.DISPLAY_TYPE_TEMPLATEFORM.equals(view.getDisplayType())) {
			templateForm = view.getTemplateForm();
		}
		if (datas != null) {
			Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
			Iterator<Document> it = datas.iterator();
			try {
				StringBuffer colBuf = new StringBuffer();
				// String fieldName = view.getRelationDateColum();
				Object column = view.getViewTypeImpl().getColumnMapping().get(
						CalendarType.DEFAULT_KEY_FIELDS[0]);
				String fieldName = "";
				boolean showTitle1 = true;
				if (column instanceof String)
					fieldName = column.toString();
				else if (column instanceof Column){
					fieldName = ((Column) column).getFieldName();
					showTitle1 = ((Column) column).isShowTitle();
				}
					

				Date stDate = cld.getTime();
				cld.add(Calendar.HOUR, step);
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String filterValue1 = format.format(stDate);
				String filterValue2 = format.format(cld.getTime());
				int i = 0;
				while (it.hasNext()) {
					Document doc = it.next();
					String value = getDocDateTimeValueAsString(doc, fieldName);
					if (filterValue1.compareTo(value) <= 0
							&& filterValue2.compareTo(value) > 0) {
						runner.initBSFManager(doc, params, user, errors);
						Iterator<Column> iter = view.getColumns().iterator();
						String list = "";
						while (iter.hasNext()) {
							Column col = (Column) iter.next();
							Object result = col.getText(doc, runner, user);
							if (!(col.getWidth() != null && col.getWidth()
									.equals("0"))) {
								list += result + "&nbsp;";
							}
						}
						colBuf.append("<div moduleType=\"div1\" order=\"")
								.append(i).append("\" isReadonly=\"").append(
										view.getReadonly().booleanValue())
								.append("\" list='").append(list).append(
										"' viewType=\"").append(viewType)
								.append("\" showTitle=\"" + showTitle1 + "\"");
						if (view.getReadonly().booleanValue()) {
							colBuf.append(">");
						} else {

							switch (viewType) {
							case 1: {
								colBuf.append(" getId=\"" ).append( doc.getId()
								).append("\" getFormid=\"" ).append( doc.getFormid()
								).append( "\" templateForm=\"" ).append( templateForm
								).append( "\">");

							}
								break;
							case 2: {
								DocumentProcess proxy = (DocumentProcess) getProcess();
								Document doc2 = (Document) proxy.doView(doc
										.getId());
								IRunner jsrun = JavaScriptFactory.getInstance(
										params.getSessionid(), view
												.getApplicationid());
								jsrun
										.initBSFManager(doc2, params, user,
												errors);
								Collection<Column> columns = view.getColumns();
								StringBuffer valuesMap = new StringBuffer("{");
								Iterator<Column> it2 = columns.iterator();
								while (it2.hasNext()) {
									Column key = it2.next();
									Object value1 = key.getText(doc, jsrun,
											user);
									valuesMap
											.append("'")
											.append(key.getId())
											.append("':'")
											.append(
													StringUtil
															.encodeHTML(value1
																	.toString()))
											.append("',");
								}
								valuesMap.setLength(valuesMap.length() - 1);
								valuesMap.append("}");
								colBuf.append(" valuesMap=\""
								).append( valuesMap.toString() ).append( "\">");
							}
								break;
							default:
								;
							}

						}
						colBuf.append("</div>");
						i++;
						if (i >= 3) {
							colBuf
									.append("<div moduleType=\"div2\" filterValue1=\""
										).append( filterValue1.substring(0, 10)
										).append( "\"></div>");
							break;
						}
					}
				}
				if (colBuf.length() > 0)
					buf.append(colBuf);
			} catch (Exception e) {
				e.printStackTrace();
				buf.append("&nbsp;");
			}
		} else {
			buf.append("&nbsp;");
		}
		return buf.toString();
	}

	private String getDayViewList(IRunner runner, View view,
			Collection<Document> datas, ParamsTable params, WebUser user,
			final Calendar cld, int day, int viewType, int yearIndex,
			int monthIndex) {
		StringBuffer buf = new StringBuffer();
		String templateForm = "";
		if (View.DISPLAY_TYPE_TEMPLATEFORM.equals(view.getDisplayType())) {
			templateForm = view.getTemplateForm();
		}
		if (datas != null) {
			Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
			Iterator<Document> it = datas.iterator();
			try {
				StringBuffer colBuf = new StringBuffer();
				// String fieldName = view.getRelationDateColum();
				Object column = view.getViewTypeImpl().getColumnMapping().get(
						CalendarType.DEFAULT_KEY_FIELDS[0]);
				String fieldName = "";
				boolean showTitle = true;
				if (column instanceof String)
					fieldName = column.toString();
				else if (column instanceof Column){
					fieldName = ((Column) column).getFieldName();
					showTitle = ((Column) column).isShowTitle();
				}

				cld.set(Calendar.DAY_OF_MONTH, day);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String filterValue = format.format(cld.getTime());
				int i = 0;
				while (it.hasNext()) {
					Document doc = it.next();
					String value;
					value = doc.getValueByField(fieldName);
					Date dateTemp = format.parse(value);
					value = format.format(dateTemp);
					if (filterValue.equals(value)) {
						runner.initBSFManager(doc, params, user, errors);
						Iterator<Column> iter = view.getColumns().iterator();

						String list = "";
						while (iter.hasNext()) {
							Column col = iter.next();
							if (!isHiddenColumn(col, runner)) {
								Object result = col.getText(doc, runner, user);
								if (!(col.getWidth() != null && col.getWidth()
										.equals("0"))) {
									list += result + "&nbsp;";
								}
							}
						}
						colBuf.append("<div moduleType=\"div1\" isReadonly=\""
								+ view.getReadonly().booleanValue()
								+ "\" viewAction=\""
								+ ViewAction.DO_DIALOG_VIEW + "\" order=\"" + i
								+ "\" viewType=\"" + viewType + "\" list='"
								+ list + "' showTitle='" + showTitle + "'");
						colBuf.append(" yearIndex='" + yearIndex
								+ "'  monthIndex='" + monthIndex
								+ "' day='" + day + "'");
						if (view.getReadonly().booleanValue()) {
							colBuf.append(">");
						} else {
							switch (viewType) {
							case 1: {
								colBuf.append(" docGetid='" + doc.getId()
										+ "' docGetFormid='" + doc.getFormid()
										+ "' templateForm='" + templateForm
										+ "'>");
							}
								break;
							case 2: {
								DocumentProcess proxy = createDocumentProcess(view
										.getApplicationid());
								Document doc2 = (Document) proxy.doView(doc
										.getId());
								IRunner jsrun = JavaScriptFactory.getInstance(
										params.getSessionid(), view
												.getApplicationid());
								jsrun
										.initBSFManager(doc2, params, user,
												errors);
								Collection<Column> columns = view.getColumns();
								StringBuffer valuesMap = new StringBuffer("{");
								Iterator<Column> it2 = columns.iterator();
								while (it2.hasNext()) {
									Column key = it2.next();
									Object value1 = key.getText(doc, jsrun,
											user);
									valuesMap
											.append("'")
											.append(key.getId())
											.append("':'")
											.append(
													StringUtil
															.encodeHTML(value1
																	.toString()))
											.append("',");
								}
								valuesMap.setLength(valuesMap.length() - 1);
								valuesMap.append("}");
								colBuf.append("valuesMap='"
										+ valuesMap.toString() + "'>");

							}
								break;
							default:
								;
							}
						}
						colBuf.append("</div>");
						i++;
						if (i >= 4) {
							if (viewType != ViewAction.DO_DIALOG_VIEW) {
								colBuf
										.append("<div moduleType=\"div2\" filterValue='"
												+ filterValue + "'></div>");
							}
							break;
						}
					}
				}
				if (colBuf.length() > 0)
					buf.append(colBuf);
			} catch (Exception e) {
				e.printStackTrace();
				buf.append("&nbsp;");
			}
		} else {
			buf.append("&nbsp;");
		}
		return buf.toString();
	}

	private String getTaskViewList(IRunner runner, View view,
			Collection<Document> datas, ParamsTable params, WebUser user,
			final Calendar cld, int day, int viewType, int yearIndex,
			int monthIndex) {
		StringBuffer buf = new StringBuffer();
		String templateForm = "";
		if (View.DISPLAY_TYPE_TEMPLATEFORM.equals(view.getDisplayType())) {
			templateForm = view.getTemplateForm();
		}
		if (datas != null) {
			Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
			Iterator<Document> it = datas.iterator();
			try {
				StringBuffer colBuf = new StringBuffer();
				// String fieldName = view.getRelationDateColum();
				Object column = view.getViewTypeImpl().getColumnMapping().get(
						CalendarType.DEFAULT_KEY_FIELDS[0]);
				String fieldName = "";
				boolean showTitle2 = true;
				if (column instanceof String)
					fieldName = column.toString();
				else if (column instanceof Column){
					fieldName = ((Column) column).getFieldName();
					showTitle2 = ((Column) column).isShowTitle();
				}

				cld.set(Calendar.DAY_OF_MONTH, day);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String filterValue = format.format(cld.getTime());
				int i = 0;
				while (it.hasNext()) {
					Document doc = it.next();
					String value;
					value = doc.getValueByField(fieldName);
					if (filterValue.equals(value)) {
						runner.initBSFManager(doc, params, user, errors);
						Iterator<Column> iter = view.getColumns().iterator();

						String list = "";
						while (iter.hasNext()) {
							Column col = iter.next();
							if (!isHiddenColumn(col, runner)) {
								Object result = col.getText(doc, runner, user);
								if (!(col.getWidth() != null && col.getWidth()
										.equals("0"))) {
									list += result + "&nbsp;&nbsp;";
								}
							}
						}
						colBuf
								.append("<div moduleType=\"divType\" isReadonly=\""
									).append( view.getReadonly().booleanValue()
									).append( "\" yearIndex=\""
									).append( yearIndex
									).append( "\" monthIndex=\""
									).append( monthIndex
									).append( "\" day=\""
									).append( day
									).append( "\" list=\""
									).append( list
									).append( "\" getLoginno=\""
									).append( user.getLoginno()
									).append( "\" viewType=\""
									).append( viewType
									).append( "\" getId=\""
									).append( doc.getId()
									).append( "\" showTitle=\""
									).append( showTitle2
									).append( "\" getFormid=\""
									).append( doc.getFormid()
									).append( "\" templateForm=\""
									).append( templateForm
									).append( "\"");
						if (view.getReadonly().booleanValue()) {
							colBuf.append(">");
							colBuf.append("</div>");
						} else {
							switch (viewType) {
							case 1: {
								colBuf.append(">");
								colBuf.append("</div>");
							}
								break;
							case 2: {
								DocumentProcess proxy = createDocumentProcess(view
										.getApplicationid());
								Document doc2 = (Document) proxy.doView(doc
										.getId());
								IRunner jsrun = JavaScriptFactory.getInstance(
										params.getSessionid(), view
												.getApplicationid());
								jsrun
										.initBSFManager(doc2, params, user,
												errors);
								Collection<Column> columns = view.getColumns();
								StringBuffer valuesMap = new StringBuffer("{");
								Iterator<Column> it2 = columns.iterator();
								while (it2.hasNext()) {
									Column key = it2.next();
									Object value1 = key.getText(doc, jsrun,
											user);
									valuesMap
											.append("'")
											.append(key.getId())
											.append("':'")
											.append(
													StringUtil
															.encodeHTML(value1
																	.toString()))
											.append("',");
								}
								valuesMap.setLength(valuesMap.length() - 1);
								valuesMap.append("}");
								colBuf.append(" valuesMap=\""
									).append( valuesMap.toString() ).append( "\">");
								colBuf.append("</div>");
							}
								break;
							default:
								break;
							}
						}
						i++;
					}
				}
				if (colBuf.length() > 0)
					buf.append(colBuf);
			} catch (Exception e) {
				e.printStackTrace();
				buf.append("&nbsp;");
			}
		} else {
			buf.append("&nbsp;");
		}
		return buf.toString();
	}

	private Collection<Document> getDatas(View view, ParamsTable params,
			WebUser user, String applicationid, Date stDate, Date endDate)
			throws Exception {
		ViewProcess viewProcess = (ViewProcess) ProcessFactory
				.createProcess(ViewProcess.class);
		DataPackage<Document> datas = viewProcess.getDataPackage(view, params,
				user, applicationid, stDate, endDate, Integer.MAX_VALUE);
		return datas != null ? datas.datas : null;
	}

	/**
	 * 获取视图的列
	 * 
	 * @param viewid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getColumns(String viewid) throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		ViewProcess viewProcess = (ViewProcess) ProcessFactory
				.createProcess(ViewProcess.class);
		View view = (View) viewProcess.doView(viewid);
		Collection<Column> collection = view.getColumns();
		map.put("", "{*[please.select]*}");
		for (Iterator<Column> ite = collection.iterator(); ite.hasNext();) {
			Column column = ite.next();
			map.put(column.getName(), column.getName());
		}
		return map;
	}

	/**
	 * 根据日期时间字段标识，查找对应日期时间字段值，以字符串返回
	 * 
	 * @param doc
	 *            数据文档
	 * @param fieldName
	 *            日期时间字段标识
	 * @return 根据日期时间字段标识，查找对应日期时间字段值，以字符串返回
	 * @throws Exception
	 */
	public String getDocDateTimeValueAsString(Document doc, String fieldName)
			throws Exception {
		String value = "";
		if (fieldName.toUpperCase().trim().startsWith("$")) {
			String propName = fieldName.substring(1);
			if (propName.equalsIgnoreCase("AuditDate")) {
				if (doc.getAuditdate() != null) {
					try {
						value = DateUtil.format(doc.getAuditdate(),
								"yyyy-MM-dd HH:mm:ss");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (propName.equalsIgnoreCase("LastModified")) {
				if (doc.getLastmodified() != null) {
					try {
						value = DateUtil.format(doc.getLastmodified(),
								"yyyy-MM-dd HH:mm:ss");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (propName.equalsIgnoreCase("Created")) {
				if (doc.getCreated() != null) {
					try {
						value = DateUtil.format(doc.getCreated(),
								"yyyy-MM-dd HH:mm:ss");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			Item item = doc.findItem(fieldName);
			if (item != null && item.getType().equals(Item.VALUE_TYPE_DATE)) {
				if (item.getDatevalue() != null) {
					value = DateUtil.getDateTimeStr(item.getDatevalue());
				}
			}
		}
		return value;
	}

	public static View get_ViewById(String viewid) throws Exception {
		ViewProcess vp = (ViewProcess) ProcessFactory
				.createProcess(ViewProcess.class);
		return (View) vp.doView(viewid);
	}

	private static DocumentProcess createDocumentProcess(String applicationid)
			throws CreateProcessException {
		DocumentProcess process = (DocumentProcess) ProcessFactory
				.createRuntimeProcess(DocumentProcess.class, applicationid);
		return process;
	}

	public IRunTimeProcess<Document> getProcess() throws CreateProcessException {
		return createDocumentProcess(getApplication());
	}

	public String getApplication() {
		if (application != null && application.trim().length() > 0)
			return application;

		return (String) getContext().getSession().get("APPLICATION");
	}

	public static ActionContext getContext() {
		return ActionContext.getContext();
	}

	protected String application;

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	/**
	 * 执行脚本
	 * 
	 * @param parameters
	 *            页面参数
	 * @param request
	 *            HTTP请求
	 * @return
	 */
	public String runScript(Map<String, String> parameters,
			HttpServletRequest request) {
		try {
			FormProcess formProcess = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);

			String viewid = parameters.get("_viewid");
			String formid = parameters.get("formid");
			String fieldid = parameters.get("fieldid");

			WebUser user = (WebUser) request.getSession().getAttribute(
					Web.SESSION_ATTRIBUTE_FRONT_USER);

			Form form = (Form) formProcess.doView(formid);
			if (form == null) {
				return "";
			}

			FormField field = form.findField(fieldid);
			if (field != null && field instanceof ViewDialogField) {
				String script = ((ViewDialogField) field).getOkScript();
				if (!StringUtil.isBlank(script)) {
					script = StringUtil.dencodeHTML(script);
					ParamsTable params = ParamsTable.convertHTTP(request);
					params.putAll(parameters);

					Document searchDocument = getSearchDocument(viewid, params,
							user);
					IRunner runner = JavaScriptFactory.getInstance(params
							.getSessionid(), form.getApplicationid());
					runner.initBSFManager(searchDocument, params, user,
							new ArrayList<ValidateMessage>());

					Object result = runner.run(
							field.getScriptLable("OkScript"), script);
					if (result instanceof org.mozilla.javascript.Undefined) {
						return "";
					} else if (result instanceof JsMessage) {
						JsMessage js = (JsMessage) result;
						if (js.getTypeName().equals("Alert")) {
							return "do" + js.getTypeName() + "('"
									+ js.getContent() + "');";
						} else {
							return "do" + js.getTypeName() + "('"
							+ js.getContent() + "', result);";
						}
					}
					if(result!=null){
						return  String.valueOf(result);
					}else{
						return "";
					}
					
				}
			}
		} catch (Exception e) {
			return e.getMessage();
		}

		return "";
	}

	/**
	 * 获取视图查询文档
	 * 
	 * @param viewid
	 *            视图ID
	 * @param params
	 *            参数
	 * @param user
	 *            当前用户
	 * @return 查询文档
	 * @throws Exception
	 */
	private Document getSearchDocument(String viewid, ParamsTable params,
			WebUser user) throws Exception {
		ViewProcess viewProcess = (ViewProcess) ProcessFactory
				.createProcess(ViewProcess.class);
		View view = (View) viewProcess.doView(viewid);
		if (view != null) {
			Form searchForm = view.getSearchForm();
			if (searchForm != null) {
				return searchForm.createDocument(params, user);
			}
		}

		return new Document();
	}

	public boolean isHiddenColumn(Column column, IRunner runner) {
		try {
			if (column.getHiddenScript() != null
					&& column.getHiddenScript().trim().length() > 0) {
				StringBuffer label = new StringBuffer();
				label.append("View").append(".Activity(")
						.append(column.getId()).append(")." + column.getName())
						.append(".runHiddenScript");

				Object result = runner.run(label.toString(), column
						.getHiddenScript());// 运行脚本
				if (result != null && result instanceof Boolean) {
					return ((Boolean) result).booleanValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 创建权限字段范围下拉选项
	 * 
	 * @param selectFieldName
	 *            下拉框名称
	 * @param def
	 *            默认值
	 * @return 添加选项的JS脚本
	 */
	public Map<String, String> createAuthFieldOptions(String selectFieldName,
			String authField, String def) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (View.AUTHFIELD_AUTHOR.equals(authField)) {
			map.put(View.AUTHFIELD_SCOPE_ITSELF,
					"{*[core.dynaform.view.authfield.scope.itself]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_SUPERIOR,
					"{*[core.dynaform.view.authfield.scope.superior]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_ALL_SUPERIOR,
			"{*[core.dynaform.view.authfield.scope.all_superior]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_LOWER,
					"{*[core.dynaform.view.authfield.scope.lower]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_ALL_LOWER,
			"{*[core.dynaform.view.authfield.scope.all_lower]*}");
		} else if (View.AUTHFIELD_AUTHOR_DEFAULT_DEPT.equals(authField)) {
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_DEFAULT,
					"{*[core.dynaform.view.authfield.scope.author.dept.default]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_ALL_SUPERIOR,
					"{*[core.dynaform.view.authfield.scope.author.dept.allsuperior]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_ALL_LOWER,
					"{*[core.dynaform.view.authfield.scope.author.dept.alllower]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_LINE_SUPERIOR,
					"{*[core.dynaform.view.authfield.scope.author.dept.linesuperior]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_LINE_LOWER,
					"{*[core.dynaform.view.authfield.scope.author.dept.linelower]*}");
		} else if (View.AUTHFIELD_AUDITOR.equals(authField)) {
			map.put(View.AUTHFIELD_SCOPE_ITSELF,
					"{*[core.dynaform.view.authfield.scope.itself]*}");
		}else if(View.AUTHFIELD_PROCESSED.equals(authField)){
			map.put(View.AUTHFIELD_SCOPE_ITSELF, "{*[core.dynaform.view.authfield.scope.itself]*}");
		}else if (authField.contains("departmentField")) {
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_DEFAULT,
					"{*[core.dynaform.view.authfield.scope.author.dept.default]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_ALL_SUPERIOR,
					"{*[core.dynaform.view.authfield.scope.author.dept.allsuperior]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_ALL_LOWER,
					"{*[core.dynaform.view.authfield.scope.author.dept.alllower]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_LINE_SUPERIOR,
					"{*[core.dynaform.view.authfield.scope.author.dept.linesuperior]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_LINE_LOWER,
					"{*[core.dynaform.view.authfield.scope.author.dept.linelower]*}");
		} else if (authField.contains("treeTepartmentField")) {
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_DEFAULT,
					"{*[core.dynaform.view.authfield.scope.author.dept.default]*}");
		} else if (authField.contains("userField")) {
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_SUPERIOR,
					"{*[core.dynaform.view.authfield.scope.superior]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_ALL_SUPERIOR,
			"{*[core.dynaform.view.authfield.scope.all_superior]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_LOWER,
					"{*[core.dynaform.view.authfield.scope.lower]*}");
			map.put(View.AUTHFIELD_SCOPE_AUTHOR_ALL_LOWER,
			"{*[core.dynaform.view.authfield.scope.all_lower]*}");
			map.put(View.AUTHFIELD_SCOPE_ITSELF,
					"{*[core.dynaform.view.authfield.scope.itself]*}");
		} else if (authField.contains("multiUserField")) {
			map.put(View.AUTHFIELD_SCOPE_ITSELF,
					"{*[core.dynaform.view.authfield.scope.itself]*}");
		}

		// return DWRHtmlUtils.createOptions(map, selectFieldName, def);
		return map;
	}

	public Map<String, String> createFieldOptions(String formid) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(" ", " ");
		map.put(View.AUTHFIELD_AUTHOR, "{*[Author]*}");
		map.put(View.AUTHFIELD_AUTHOR_DEFAULT_DEPT,
				"{*[Author]*}{*[Default]*}{*[Department]*}");
		map.put(View.AUTHFIELD_AUDITOR, "{*[Auditor]*}");
		map.put(View.AUTHFIELD_PROCESSED, "{*[Processed]*}");
		try {
			FormProcess formProcess = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);
			if (!StringUtil.isBlank(formid)) {
				if (!StringUtil.isBlank(formid)) {
					Form form = (Form) formProcess.doView(formid);
					if (form != null) {
						Collection<FormField> fields = form.getAllFields();
						for (Iterator<FormField> it = fields.iterator(); it
								.hasNext();) {
							FormField field = it.next();
							if (field instanceof DepartmentField) {
								map.put("departmentField_" + field.getName(),
										field.getName());
							} else if (field instanceof TreeDepartmentField) {
								map.put("treeTepartmentField_"
										+ field.getName(), field.getName());
							} else if (field instanceof UserField) {
								if ("multiSelect".equals(((UserField) field)
										.getSelectMode()))
									map
											.put("multiUserField_"
													+ field.getName(), field
													.getName());
								else if ("selectOne".equals(((UserField) field)
										.getSelectMode()))
									map.put("userField_" + field.getName(),
											field.getName());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	/**
	 * 获取软件下的所有视图
	 * @param application
	 * 		软件Id
	 * @return
	 * 		Map对象 (key=视图id  value=视图全名)
	 * @throws Exception
	 */
	public Map<String, String> getViewsByApplication(String application) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		if (StringUtil.isBlank(application)) {
			return map;
		}
		ViewProcess vp = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		Collection<View> col = vp.getViewsByApplication(application);

		for (Iterator<View> iterator = col.iterator(); iterator.hasNext();) {
			View view = iterator.next();
			map.put(view.getId(), view.getName());
			
		}
		PersistenceUtils.closeSession();
		return map;

	}
	
	/**
	 * 通过视图Id获取所在模块的Id
	 * 
	 * @return ModuleId
	 * 
	 * @throws Exception
	 */
	public String getModuleById(String viewId) throws Exception {
		
		ViewProcess process = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		View view = (View) process.doView(viewId);
		
		if(view != null){
			return view.getModule().getId();
		}
		return null;
	}
	
	/**
	 * 执行视图选择框确定回调脚本
	 * 
	 * @param parameters
	 *            页面参数
	 * @param request
	 *            HTTP请求
	 * @return
	 */
	public String runViewDialogCallbackscript(Map<String, String> parameters,
			HttpServletRequest request) {
		try {
			FormProcess formProcess = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);

			String formid = parameters.get("formid");
			String fieldid = parameters.get("fieldid");
			String docid = parameters.get("content.id");

			WebUser user = (WebUser) request.getSession().getAttribute(
					Web.SESSION_ATTRIBUTE_FRONT_USER);

			Form form = (Form) formProcess.doView(formid);
			if (form == null) {
				return "";
			}

			FormField field = form.findField(fieldid);
			if (field != null && field instanceof ViewDialogField) {
				String script = ((ViewDialogField) field).getCallbackScript();
				if (!StringUtil.isBlank(script)) {
					script = StringUtil.dencodeHTML(script);
					ParamsTable params = ParamsTable.convertHTTP(request);
					params.putAll(parameters);

					Document doc = null;
					if (!StringUtil.isBlank(docid)) {
						doc = (Document) MemoryCacheUtil
								.getFromPrivateSpace(docid, user);
						if (doc != null) {
							doc.setId(docid);
						}
					}
					doc = form.createDocument(doc, params,
							user);
					
					IRunner runner = JavaScriptFactory.getInstance(params
							.getSessionid(), form.getApplicationid());
					runner.initBSFManager(doc, params, user,
							new ArrayList<ValidateMessage>());

					Object result = runner.run(
							field.getScriptLable("callbackScript"), script);
					if (result instanceof org.mozilla.javascript.Undefined) {
						return "";
					} 
					if(result!=null){
						return  String.valueOf(result);
					}else{
						return "";
					}
					
				}
			}
		} catch (Exception e) {
			return e.getMessage();
		}

		return "";
	}
}
