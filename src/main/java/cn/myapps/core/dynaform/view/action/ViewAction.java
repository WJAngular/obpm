package cn.myapps.core.dynaform.view.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.style.repository.ejb.StyleRepositoryProcess;
import cn.myapps.core.style.repository.ejb.StyleRepositoryVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;


/**
 * 
 * @author nicholas
 * 
 */
public class ViewAction extends BaseAction<View> {
	private static final Logger LOG = Logger.getLogger(ViewAction.class);

	public static final int DO_DISPLAY_VIEW = 1;

	public static final int DO_DIALOG_VIEW = 2;

	protected String _viewid;

	protected String _superiorid;

	protected String _resourceid;

	protected String _resourcedesc;

	protected String totalRowText;

	protected String _sortCol;

	protected String _sortStatus;

	protected String _orderby;

	protected String domain;

	protected Document parent;

	protected Document currentDocument;

	protected int year;

	protected int month;

	protected int week;

	protected int day;

	protected String viewMode;

	protected boolean isPreview = false;
	

	public boolean isPreview() {
		return isPreview;
	}

	public void setPreview(boolean isPreview) {
		this.isPreview = isPreview;
	}

	public String getViewMode() {
		return viewMode;
	}

	public void setViewMode(String viewMode) {
		this.viewMode = viewMode;
	}

	public int getWeek() {
		return week;
	}

	public int getDay() {
		return day;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public void setDay(int day) {
		this.day = day;
	}

	private static final long serialVersionUID = 1L;

	private static Map<Integer, String> _OPENTYPE = new TreeMap<Integer, String>();

	protected View view;

	/**
	 * View类型. 两种类型： 1:列表视图(LISTVIEW),2:日历视图(CALENDARVIEW), 3:树形视图(TREEVIEW),
	 * 4:地图视图(TREEVIEW), 5:折叠视图(COLLAPSIBLEVIEW).
	 */
	private static Map<Integer, String> _VIEWTYPE = new TreeMap<Integer, String>();

	public Map<Integer, String> get_VIEWTYPE() {
		return _VIEWTYPE;
	}

	static {
		_VIEWTYPE.put(View.VIEW_TYPE_NORMAL, "{*[cn.myapps.core.dynaform.view.list_view]*}");
		_VIEWTYPE.put(View.VIEW_TYPE_CALENDAR, "{*[cn.myapps.core.dynaform.view.calendar_view]*}");
		_VIEWTYPE.put(View.VIEW_TYPE_TREE, "{*[cn.myapps.core.dynaform.view.tree_view]*}");
		_VIEWTYPE.put(View.VIEW_TYPE_MAP, "{*[cn.myapps.core.dynaform.view.map_view]*}");
		_VIEWTYPE.put(View.VIEW_TYPE_GANTT, "{*[cn.myapps.core.dynaform.view.gantt_view]*}");
		_VIEWTYPE.put(View.VIEW_TYPE_COLLAPSIBLE, "{*[cn.myapps.core.dynaform.view.collapsible_view]*}");
	}
	static {
		_OPENTYPE.put(View.OPEN_TYPE_NORMAL, "{*[Open.in.working.currpage]*}");
		//_OPENTYPE.put(View.OPEN_TYPE_OWN, "{*[Open.in.working.own]*}");
		_OPENTYPE.put(View.OPEN_TYPE_DIV, "{*[Open.in.working.div]*}");
		_OPENTYPE.put(View.OPEN_TYPE_GRID, "{*[Open.in.grid]*}");
	}

	/**
	 * 获取所引用的查询Form主键
	 * 
	 * @return 查询Form主键
	 */
	public String get_searchformid() {
		View view = (View) getContent();
		if (view.getSearchForm() != null) {
			return view.getSearchForm().getId();
		}
		return null;
	}

	/**
	 * Set所引用的查询Form主键
	 * 
	 * @param _formid
	 *            查询Form主键
	 */
	public void set_searchformid(String _formid) {
		View view = (View) getContent();
		if (!StringUtil.isBlank(_formid)) {
			Form form = new Form();
			form.setId(_formid);
			view.setSearchForm(form);
		}
	}

	/**
	 * ViewAction 构造函数
	 * 
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @see cn.myapps.base.action.BaseAction#BaseAction(BaseProcess,
	 *      ValueObject)
	 * @see cn.myapps.util.ProcessFactory#createProcess(Class)
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public ViewAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(ViewProcess.class), new View());
	}

	public String doInnerPage() {
		try {
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,view.getApplicationid());
			String docid = getParams().getParameterAsString("_docid");
			if (!StringUtil.isBlank(docid) && !Web.TREEVIEW_ROOT_NODEID.equals(docid)) {
				// 父节点文档
				Document doc = (Document) process.doView(docid);
				if (doc != null) {
					setCurrentDocument(doc);
					MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
				}
			}

			setContent(view);
			
			if ( !"root".equals(docid)){
				String innerType=(String) this.getParams().getParameter("innerType");
				if(innerType!=null && innerType.equals("FORM")){
					return "successForm";
				}else if(innerType!=null && innerType.equals("VIEW")){
					return "successView";
				}else{
					if (View.TREENODE_HREF_FORM.equals(view.getInnerType()) && !"root".equals(docid)) {
						return "successForm";
					} else if (View.TREENODE_HREF_VIEW.equals(view.getInnerType())) {
						return "successView";
					} else {
						return SUCCESS;
					}
				}
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		
		return "successView";
	}

	protected String getSuccessResult(View view, int cldType) throws Exception {
		ParamsTable params = getParams();
		String clearTemp = params.getParameterAsString("clearTemp");
		String parentid = params.getParameterAsString("parentid");
		String isinner = params.getParameterAsString("isinner");
		String innerType = params.getParameterAsString("innerType");
		
		if (clearTemp != null && clearTemp.equals("true")) {
			getUser().clearTmpspace(); // 清空用户缓存
		}
		

		//设置Parent Document
		if(!StringUtil.isBlank(params.getParameterAsString("parentid"))){
			setParent(null);
		}

		if (StringUtil.isBlank(isinner) || !isinner.equals("true") || View.TREENODE_HREF_LINK.equals(innerType)) {
			switch (view.getOpenType()) {
			case View.OPEN_TYPE_GRID:
				displayByView(view);
				return "successGrid";
			default:
				break;
			}

			if (cldType == DO_DIALOG_VIEW) {
				switch (view.getViewType()) {
				case View.VIEW_TYPE_NORMAL:
					displayByView(view);
					break;
				case View.VIEW_TYPE_TREE:
					setContent(view);
					return "successTree";
				case View.VIEW_TYPE_CALENDAR:
					doShowCldView(cldType);
					return "successCld";
				case View.VIEW_TYPE_GANTT:
					setContent(view);
					return "successGantt";
				case View.VIEW_TYPE_MAP:
					displayByView(view);
					return "successMap";
				default:
					displayByView(view);
					break;
				}
			} else {
				switch (view.getViewType()) {
				case View.VIEW_TYPE_CALENDAR:
					doShowCldView(cldType);
					return "successCld";
				case View.VIEW_TYPE_NORMAL:
					displayByView(view);
					if (!StringUtil.isBlank(parentid)) {
						return "successSub";
					}
					break;
				case View.VIEW_TYPE_COLLAPSIBLE:
					displayByView(view);
					return "successCollapsible";
				case View.VIEW_TYPE_TREE:
					setContent(view);
					return "successTree";
				case View.VIEW_TYPE_GANTT:
					setCurrentDocument(getSearchDocument(view));
					setContent(view);
					return "successGantt";
				case View.VIEW_TYPE_MAP:
					displayByView(view);
					return "successMap";
				default:
					break;
				}
			}
		} else if(!StringUtil.isBlank(parentid)){
			displayByView((View) getContent());
			return "successSub";
		}else{
			displayByView((View) getContent());
		}
//		if(false)
//			return "successcheck";
//		else
			return SUCCESS; // 普通且不为子表单
	}

	protected String getInputResult(View view) throws Exception {
		String parentid = getParams().getParameterAsString("parentid");
		String isinner = getParams().getParameterAsString("isinner");

		if (StringUtil.isBlank(isinner) || !isinner.equals("true")) {
			switch (view.getOpenType()) {
			case View.OPEN_TYPE_GRID:
				return "inputGrid";
			default:
				break;
			}

			switch (view.getViewType()) {
			case View.VIEW_TYPE_NORMAL:
				if (!StringUtil.isBlank(parentid)) {
					return "inputSub";
				}
				break;
			case View.VIEW_TYPE_TREE:
				return "inputTree";
			case View.VIEW_TYPE_CALENDAR:
				return "inputCld";
			case View.VIEW_TYPE_GANTT:
				return "inputGantt";
			case View.VIEW_TYPE_MAP:
				return "inputMap";
			default:
				break;
			}
		}

		return INPUT; // 普通且不为子表单
	}


	/**
	 * 显示日历视图
	 * 
	 * @param displayType
	 *            显示类型（普通、弹出）
	 * @return
	 * @throws Exception
	 */
	private boolean doShowCldView(int displayType) throws Exception {
		ParamsTable params = getParams();
		if (view.getViewTypeImpl().intValue() != View.VIEW_TYPE_CALENDAR) {
			return false;
		}

		// part.1
		String viewMode = params.getParameterAsString("viewMode");
		addByType(params);
		ViewHelper hp = new ViewHelper();
		hp.setDisplayType(displayType);

		String toHtml = "";
		if (getDomain() != null)
			params.setParameter("domainid", getDomain());
		changeOrderBy(params);

		// part.2
		if ("WEEKVIEW".equals(viewMode)) {
			toHtml = hp.toWeekHtml(view, params, getUser(), getApplication(), year, month, day, isPreview);
		} else if ("DAYVIEW".equals(viewMode)) {
			toHtml = hp.toDayHtml(view, params, getUser(), getApplication(), year, month, day, isPreview);
		} else {
			viewMode = viewMode != null && viewMode.length() > 0 ? viewMode : "MONTHVIEW";
			params.setParameter("viewMode", viewMode);
			toHtml = hp.toMonthHtml(view, params, getUser(), getApplication(), year, month, isPreview);
		}

		// part.3
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("toHtml", toHtml);
		setViewMode(viewMode);
		setCurrentDocument(getSearchDocument(view));
		setContent(view);
		
//		//4.包含元素 设置Parent Document
//		if(!StringUtil.isBlank(params.getParameterAsString("parentid"))){
//			setParent(null);
//		}

		return true;
	}

	private void addByType(ParamsTable params) {
		String year = params.getParameterAsString("year");
		String month = params.getParameterAsString("month");
		String addType = params.getParameterAsString("addType");
		Calendar dfCld = Calendar.getInstance();
		int yearIndex = year != null && year.trim().length() > 0 ? Integer.parseInt(year) : dfCld.get(Calendar.YEAR);
		int monthIndex = month != null && month.trim().length() > 0 ? Integer.parseInt(month) : dfCld
				.get(Calendar.MONTH) + 1;
		String week = params.getParameterAsString("week");
		int weekIndex = week != null && week.trim().length() > 0 ? Integer.parseInt(week) : 0;
		String day = params.getParameterAsString("day");
		int dayIndex = day != null && day.trim().length() > 0 ? Integer.parseInt(day) : dfCld
				.get(Calendar.DAY_OF_MONTH);
		Calendar cld = CalendarVO.getThisMonth(yearIndex, monthIndex);
		cld.set(Calendar.YEAR, yearIndex);
		cld.set(Calendar.MONTH, monthIndex - 1);
		cld.set(Calendar.DAY_OF_MONTH, dayIndex);
		if ("previousYear".equals(addType)) {
			yearIndex--;
		} else if ("previousMonth".equals(addType)) {
			cld.add(Calendar.MONTH, -1);
			yearIndex = cld.get(Calendar.YEAR);
			monthIndex = cld.get(Calendar.MONTH) + 1;
		} else if ("previousWeek".equals(addType)) {
			weekIndex--;
			cld.add(Calendar.DAY_OF_MONTH, -7);
			yearIndex = cld.get(Calendar.YEAR);
			monthIndex = cld.get(Calendar.MONTH) + 1;
			dayIndex = cld.get(Calendar.DAY_OF_MONTH);
		} else if ("previousDay".equals(addType)) {
			cld.add(Calendar.DAY_OF_MONTH, -1);
			yearIndex = cld.get(Calendar.YEAR);
			monthIndex = cld.get(Calendar.MONTH) + 1;
			dayIndex = cld.get(Calendar.DAY_OF_MONTH);
		} else if ("nextYear".equals(addType)) {
			yearIndex++;
		} else if ("nextMonth".equals(addType)) {
			cld.add(Calendar.MONTH, 1);
			yearIndex = cld.get(Calendar.YEAR);
			monthIndex = cld.get(Calendar.MONTH) + 1;
		} else if ("nextWeek".equals(addType)) {
			weekIndex++;
			cld.add(Calendar.DAY_OF_MONTH, 7);
			yearIndex = cld.get(Calendar.YEAR);
			monthIndex = cld.get(Calendar.MONTH) + 1;
			dayIndex = cld.get(Calendar.DAY_OF_MONTH);
		} else if ("nextDay".equals(addType)) {
			cld.add(Calendar.DAY_OF_MONTH, 1);
			yearIndex = cld.get(Calendar.YEAR);
			monthIndex = cld.get(Calendar.MONTH) + 1;
			dayIndex = cld.get(Calendar.DAY_OF_MONTH);
		}
		setYear(yearIndex);
		setMonth(monthIndex);
		setWeek(weekIndex);
		setDay(dayIndex);
	}

	/**
	 * @SuppressWarnings setDatas方法设置了View以外的对象，存在类型转换风险
	 */
	@SuppressWarnings("unchecked")
	protected void displayByView(View view) throws Exception {
		ParamsTable params = getParams(); // 获取并设置参数
		if (view != null) {
			
			try {
				Document searchDocument = getSearchDocument(view);
				// 设置Action属性
				setCurrentDocument(searchDocument);
				setContent(view);
				setParent(null);

				
				/**
				 * 进入视图时，先根据默认查询表单的值来查询一遍。
				 * @date 2014-12-19
				 * @author Alvin
				 */
				if(params.getParameter("isQueryButton")==null && view.getSearchForm()!=null){
					Collection<FormField> fields = view.getSearchForm().getAllFields();
					// 创建Field
					Iterator<FormField> fieldIter = fields.iterator();
					while (fieldIter.hasNext()) {
						FormField field = (FormField) fieldIter.next();
						//如果查询表单有默认值，设置到params里面。
						Object par =params.getParameter(field.getName());
						if(par ==null){
							params.setParameter(field.getName(), searchDocument.getItemValueAsString(field.getName()));
						}
					}
				}
				
				// 改变排序参数
				changeOrderBy(params);

				this.validateDocumentValue(searchDocument);

				DataPackage datas = view.getViewTypeImpl().getViewDatas(params, getUser(), searchDocument);
				
				setDatas(datas);
				setTotalRowText(view.getTotalRowText(datas));
			} catch (OBPMValidateException e) {
				addFieldError("1", e.getValidateMessage());
				if(this.getDatas() == null) {
					DataPackage datas = view.getViewTypeImpl().getViewDatas(new ParamsTable(), getUser(), new Document());
					setDatas(datas);
				}
			}catch (Exception e) {
				if(this.getDatas() == null && !(e instanceof SQLException)) {
					DataPackage datas = view.getViewTypeImpl().getViewDatas(new ParamsTable(), getUser(), new Document());
					setDatas(datas);
				}
				this.setRuntimeException(new OBPMRuntimeException(e.getCause() !=null ? e.getCause().getMessage():"发生未知异常",e));
				e.printStackTrace();
			}
			
		}
	}

	protected void changeOrderBy(ParamsTable params) {
		if (params.getParameter("_sortCol") == null || params.getParameter("_sortCol").equals("")) {
			setOrder(params, view.getDefaultOrderFieldArr());
			// params.setParameter("_sortStatus",
			// view.getOrderFieldAndOrderTypeArr());
		} else {
			String[] colFields = view.getClickSortingFieldArr();
			String fieldName = view.getFormFieldNameByColsName(params.getParameterAsString("_sortCol"));
			boolean contains = false;
			for(int i=0; i<colFields.length; i++){
				if(colFields[i].contains(fieldName)){
					colFields[i] = colFields[0];
					contains = true;
					break;
				}
			}
			Collection<String> colFieldsList = new ArrayList<String>();
			String sortStatus = params.getParameterAsString("_sortStatus");
			String sortStandard = this.getSortStandardValue(params.getParameterAsString("_sortCol"));
			if(colFields.length > 0 && contains){//存在排序字段时才去修改排序的状态
				if(! StringUtil.isBlank(sortStandard)){
					//colFields[0] = fieldName + " " + sortStatus + sortStandard; //2013-12-10 更改排序逻辑,需把默认排序字段加上排序
					colFieldsList.add(fieldName + " " + sortStatus + sortStandard);
				}else{
					//colFields[0] = fieldName + " " + sortStatus;
					colFieldsList.add(fieldName + " " + sortStatus);
				}
			}
			String[] defaultOrderFieldArr = view.getDefaultOrderFieldArr();
			for(int j=0; j<defaultOrderFieldArr.length; j++){
				if(!defaultOrderFieldArr[j].contains(fieldName)){
					colFieldsList.add(defaultOrderFieldArr[j]);
				}
			}
			
			setOrder(params, colFieldsList.toArray(new String[colFieldsList.size()]));
		}
	}

	/**
	 * 获取视图排序列的排序标准(00或01)
	 * @param colsName 列名
	 * @return 00|01|null
	 */
	private String getSortStandardValue(String colsName){
		Collection<Column> cols = view.getColumns();
		for (Iterator<Column> iter = cols.iterator(); iter.hasNext();) {
			Column vo = iter.next();
			if(vo.getFieldName().equalsIgnoreCase(colsName)){
				if(! StringUtil.isBlank(vo.getSortStandard())){
					return vo.getSortStandard();
				}
			}
		}
		return null;
	}
	
	private void setOrder(ParamsTable params, String[] orderFields) {
		params.setParameter("_sortCol", orderFields);
		if(orderFields.length>0 && orderFields[0] != null && orderFields[0].startsWith("ITEM_")){
			set_sortCol(orderFields[0].substring(5,orderFields[0].indexOf(" ")));
			set_sortStatus((orderFields[0].indexOf("ASC") > 0)? "ASC" : "DESC");
		}
	}
	
	protected Document getSearchDocument(View view) {
		if (view.getSearchForm() != null) {
			try {
				return view.getSearchForm().createDocument(getParams(), getUser());
			} catch (OBPMValidateException e) {
				addFieldError("1", e.getValidateMessage());
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}

		return new Document();
	}

	/**
	 * 返回 view主键
	 * 
	 * @return view主键
	 */
	public String get_viewid() {
		return _viewid;
	}

	/**
	 * Set view主键
	 * 
	 * @param _viewid
	 *            view主键
	 */
	public void set_viewid(String _viewid) {
		try {
			if (!StringUtil.isBlank(_viewid)) {
				String[] viewids = _viewid.split(",");
				View view = (View) process.doView(viewids[0].trim());
				if (view == null) {
					throw new OBPMValidateException("视图不存在");
				}
				setContent(view);
				this.view = view;
				this._viewid = viewids[0].trim();
			}

		} catch (OBPMValidateException e) {
			LOG.warn("set_viewid", e);
			addFieldError("1", e.getValidateMessage());
		} catch (Exception e) {
			LOG.warn("set_viewid", e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

	}

	/**
	 * 返回所属模块主键(module id)
	 * 
	 * @return 所属模块主键(module id)
	 */
	public String get_moduleid() {
		View view = (View) getContent();
		if (view.getModule() != null) {
			return view.getModule().getId();
		}
		return null;
	}

	/**
	 * Set模块主键(module id)
	 * 
	 * @param _moduleid
	 *            模块主键
	 */
	public void set_moduleid(String _moduleid) {
		View view = (View) getContent();
		if (_moduleid != null) {
			try {
				ModuleProcess mp = (ModuleProcess) ProcessFactory.createProcess((ModuleProcess.class));
				ModuleVO module = (ModuleVO) mp.doView(_moduleid);
				view.setModule(module);

			} catch (OBPMValidateException e) {
				addFieldError("1", e.getValidateMessage());
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}
	}

	/**
	 * 返回相关的样式主键(Style id)
	 * 
	 * @return 样式主键(Style id)
	 */
	public String get_styleid() {
		if (((View) getContent()).getStyle() != null)
			return ((View) getContent()).getStyle().getId();
		else
			return null;

	}

	/**
	 * Set相关样式主键(Style id)
	 * 
	 * @param _styleid
	 *            视图风格主键
	 * @throws Exception
	 */
	public void set_styleid(String _styleid) throws Exception {
		View view = (View) getContent();
		if (_styleid != null) {
			StyleRepositoryProcess sp = (StyleRepositoryProcess) ProcessFactory
					.createProcess(StyleRepositoryProcess.class);
			StyleRepositoryVO sty = (StyleRepositoryVO) sp.doView(_styleid);
			view.setStyle(sty);
		}
	}

	/**
	 * Gets view相关resource主键
	 * 
	 * @return
	 */
	public String get_resourceid() {
		View view = (View) getContent();
		return view.getRelatedResourceid();
	}

	/**
	 * Set view相关resource主键
	 * 
	 * @param _resourceid
	 *            resource主键
	 * @throws Exception
	 */
	public void set_resourceid(String _resourceid) throws Exception {
		this._resourceid = _resourceid;
	}

	/**
	 * 获取上级resource主键
	 * 
	 * @return 上级resource主键
	 * @throws Exception
	 */
	public String get_superiorid() throws Exception {
		View view = (View) getContent();
		String rid = view.getRelatedResourceid();
		if (rid != null) {
			ResourceProcess rp = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
			ResourceVO rvo = (ResourceVO) rp.doView(rid);
			if (rvo != null && rvo.getSuperior() != null) {
				return rvo.getSuperior().getId();
			}
		}
		return null;
	}

	/**
	 * Set 上级resource主键
	 * 
	 * @param _superiorid
	 *            上级resource主键
	 */
	public void set_superiorid(String _superiorid) {
		this._superiorid = _superiorid;
	}


	/**
	 * 返回资源描述
	 * 
	 * @return 资源描述
	 */
	public String get_resourcedesc() {
		View view = (View) getContent();
		String rtn = null;
		if (view != null) {
			if (!StringUtil.isBlank(view.getDescription())) {
				rtn = view.getDescription();
			} else {
				String rid = view.getRelatedResourceid();
				if (rid != null) {
					try {
						ResourceProcess rp = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
						ResourceVO rvo = (ResourceVO) rp.doView(rid);
						if (rvo != null) {
							rtn = rvo.getDescription();
						}
					} catch (OBPMValidateException e) {
						addFieldError("1", e.getValidateMessage());
					}catch (Exception e) {
						this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
						e.printStackTrace();
					}
				}
			}
		}
		return rtn;
	}

	/**
	 * Set 资源描述
	 * 
	 * @param _resourcedesc
	 *            资源描述
	 */
	public void set_resourcedesc(String _resourcedesc) {
		this._resourcedesc = _resourcedesc;
	}

	/**
	 * 返回是否分页 ,true分页,false不分页
	 * 
	 * @return true或false
	 */
	public String get_isPagination() {
		View content = (View) getContent();
		if (content.isPagination()) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 设置View是否分页 . true为分页,false 为不分页
	 * 
	 * @param _isPagination
	 *            是否分页标识
	 */
	public void set_isPagination(String _isPagination) {
		View content = (View) getContent();
		if (_isPagination != null) {
			if (_isPagination.trim().equalsIgnoreCase("true")) {
				content.setPagination(true);
				return;
			}
		}
		content.setPagination(false);
	}

	/**
	 * 返回是否显示数据总行. true为显示,false为不显示.
	 * 
	 * @return true或false
	 */
	public String get_isShowTotalRow() {
		View content = (View) getContent();
		if (content.isShowTotalRow()) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * Set 是否显示数据总行. true为显示,false为不显示.
	 * 
	 * @param _isShowTotalRow
	 *            是否显示数据总行
	 */
	public void set_isShowTotalRow(String _isShowTotalRow) {
		View content = (View) getContent();
		if (_isShowTotalRow != null) {
			if (_isShowTotalRow.trim().equalsIgnoreCase("true")) {
				content.setShowTotalRow(true);
				return;
			}
		}
		content.setShowTotalRow(false);
	}

	public void set_readOnly(String _readOnly) {
		View content = (View) getContent();
		if (_readOnly != null) {
			if (_readOnly.trim().equalsIgnoreCase("true")) {
				content.setReadonly(Boolean.valueOf(true));
				return;
			}
		}
		content.setReadonly(false);
	}

	/**
	 * 
	 * 设置view是否只读
	 * 
	 * @return
	 */
	public String get_readOnly() {
		View content = (View) getContent();
		if (content.getReadonly().booleanValue()) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 获取打开View 类型. 三种类型:1.普通, "Normal";
	 * <p>
	 * 2.在子表单 "Open in pop window";
	 * <p>
	 * 3.父窗口"Open in working area";
	 * 
	 * @return 打开View 类型
	 */
	public Map<Integer, String> get_OPENTYPE() {
		return _OPENTYPE;
	}

	/**
	 * 显示总行数文本
	 * 
	 * @return 显示总行数文本
	 */
	public String getTotalRowText() {
		return totalRowText;
	}

	/**
	 * Set 显示总行数文本
	 * 
	 * @param totalRowText
	 *            显示总行数文本
	 * 
	 */
	public void setTotalRowText(String totalRowText) {
		this.totalRowText = totalRowText;
	}



	public String get_sortCol() {
		return _sortCol;
	}

	public void set_sortCol(String col) {
		_sortCol = col;
	}

	/**
	 * 返回排序状态 ASC(按升)，DESC(按降)
	 * 
	 * @return ASC(按升)，DESC(按降)
	 */
	public String get_sortStatus() {
		return _sortStatus;
	}

	/**
	 * 设置排序状态 ASC(按升)，DESC(按降)
	 * 
	 * @param status
	 */

	public void set_sortStatus(String status) {
		_sortStatus = status;
	}

	/**
	 * 
	 * @return
	 */
	public String get_orderby() {
		return _orderby;
	}

	/**
	 * 设置排序
	 * 
	 * @param _orderby
	 */
	public void set_orderby(String _orderby) {
		this._orderby = _orderby;
	}

	/**
	 * 获取父Document
	 * 
	 * @return Document对象
	 */
	public Document getParent() {
		return parent;
	}

	/**
	 * Set 父Document
	 * 
	 * @param parent
	 *            父Document对象
	 * @throws Exception
	 */
	public void setParent(Document parent) throws Exception {
		if (parent == null) {
			String parentid = (String) getParams().getParameterAsString("parentid");
			if (parentid != null && parentid.trim().length() > 0) {
				DocumentProcess dp = createDocumentProcess(getContent().getApplicationid());
				this.parent = (Document) dp.doView(parentid);
			}
		} else {
			this.parent = parent;
		}
	}

	/**
	 * 获取当前Document
	 * 
	 * @return
	 */
	public Document getCurrentDocument() {
		return currentDocument;
	}

	/**
	 * Set 当前Document
	 * 
	 * @param currentDocument
	 * @throws Exception
	 */
	public void setCurrentDocument(Document currentDocument) throws Exception {
		this.currentDocument = currentDocument;
	}



	private static DocumentProcess createDocumentProcess(String applicationid) throws CreateProcessException {
		DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationid);
		return process;
	}

	public String get_isRefresh() {
		View content = (View) getContent();
		if (content.isRefresh()) {
			return "true";
		} else {
			return "false";
		}
	}

	public void set_isRefresh(String refresh) {
		View content = (View) getContent();
		if (refresh != null) {
			if (refresh.trim().equalsIgnoreCase("true")) {
				content.setRefresh(true);
				return;
			}
		}
		content.setRefresh(false);
	}

	public String getDomain() {
		if (domain != null && domain.trim().length() > 0) {
			return domain;
		} else {
			return (String) getContext().getSession().get(Web.SESSION_ATTRIBUTE_DOMAIN);
		}
	}

	public void setDomain(String domain) {
		this.domain = domain;
		getContent().setDomainid(domain);
	}

	public String getFlieName() throws Exception {
		return (String) getParams().getParameter("filename");
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	
	/**
	 * 验证查找文档参数，判断是否存在非法参数，防止sql注入。
	 */
	private void validateDocumentValue(Document document) throws Exception {
		if (document != null) {
			for (Iterator<Item> it = document.getItems().iterator(); it.hasNext();) {
				Item item = it.next();
				if (item != null) {
					Object value = item.getValue();
					if (value instanceof String && specialSymbols(value.toString())) {
						throw new OBPMValidateException("{*[core.special.symbols.error]*}: " + value.toString());
					}
				}
			}
			
			Form form = document.getForm();
			ParamsTable params = getParams();
			boolean isQueryButton = params.getParameterAsBoolean("isQueryButton");
			if(form != null && form.getAllFields() != null && isQueryButton){
				StringBuffer errorMsg = new StringBuffer();
				for(Iterator<FormField> it = form.getAllFields().iterator(); it.hasNext();){
					FormField field = it.next();
					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), application); 
					runner.initBSFManager(getCurrentDocument(), params, getUser(), new java.util.ArrayList<ValidateMessage>());
					ValidateMessage msg = field.validate(runner, document);
					if(msg != null){
						errorMsg.append(msg.getErrmessage() + " ");
					}
				}
				if(!StringUtil.isBlank(errorMsg.toString())){
					throw new OBPMValidateException(errorMsg.toString());
				}
			}
		}
	}
	
	/**
	 * 修改授权类型
	 */
	public void doChangePermisionType(){
		ParamsTable params = getParams();
		String id = params.getParameterAsString("id");
		String permissionType = params.getParameterAsString("permissionType");
		
		try {
			View view = (View) process.doView(id);
			if(view !=null){
				view.setPermissionType(permissionType);
				process.doUpdate(view);
				ServletActionContext.getResponse().getWriter().print("success");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
