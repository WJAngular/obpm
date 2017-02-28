package cn.myapps.core.dynaform.view.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.form.ejb.FileManagerField;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.ImageUploadField;
import cn.myapps.core.dynaform.form.ejb.ImageUploadToDataBaseField;
import cn.myapps.core.dynaform.form.ejb.MapField;
import cn.myapps.core.dynaform.form.ejb.OnLineTakePhotoField;
import cn.myapps.core.dynaform.form.ejb.Options;
import cn.myapps.core.dynaform.form.ejb.TreeDepartmentField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.form.ejb.WordField;
import cn.myapps.core.dynaform.signature.action.SignatureHelper;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.PreviewUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class ViewHtmlBean {
	private static final Logger LOG = Logger.getLogger(ViewHtmlBean.class);

	// 地图视图只读
	protected boolean mapReadonly = false;

	protected View view;

	protected WebUser webUser;

	protected ParamsTable params;

	protected IRunner runner;

	protected HttpServletRequest request;

	protected Map<String, Object> columnHiddenMap = new HashMap<String, Object>();

	public WebUser getWebUser() {
		return webUser;
	}

	public void setWebUser(WebUser webUser) {
		this.webUser = webUser;
	}

	public HttpServletRequest getHttpRequest() {
		return request;
	}

	public void setHttpRequest(HttpServletRequest httpRequest) {
		this.request = httpRequest;
		params = ParamsTable.convertHTTP(httpRequest);
		Object content = request.getAttribute("content");
		if (content instanceof View) {
			this.view = (View) content;
		} else {
			try {
				String viewid = params.getParameterAsString("_viewid");
				ViewProcess process = (ViewProcess) ProcessFactory
						.createProcess(ViewProcess.class);
				this.view = (View) process.doView(viewid);
			} catch (Exception e) {
				LOG.warn(e.toString());
			}
		}
		if (view == null) {
			view = new View();
		}
		// Excel导出文件名,存在时加入request.原因:execAndWait拦截器用不了request
		String excelFileName = (String) request.getSession().getAttribute(
				"excelFileName");
		if (excelFileName != null && excelFileName.trim().length() > 0) {
			request.setAttribute("excelFileName", excelFileName);
			request.getSession().removeAttribute("excelFileName");
		}
	}

	public String getActBtnView4Preview() {
		Object content = request.getAttribute("content");
		this.view = (View) content;
		return toViewButtonsHTML4Preview(view);
	}

	public String toViewButtonsHTML4Preview(View view) {
		StringBuffer html = new StringBuffer();
		try {
			if (view.getActivitys() != null && !view.getActivitys().isEmpty()) {
				for (Iterator<Activity> aiter = view.getActivitys().iterator(); aiter
						.hasNext();) {
					Activity act = (Activity) aiter.next();
					act.setApplicationid(view.getApplicationid());
					int permissionType = PermissionType.MODIFY;
					html.append(act.toHtml(permissionType));
				}
			}
		} catch (Exception e) {
			LOG.error("getActBtnHTML", e);
		}

		return html.toString();
	}

	/**
	 * 输出操作HTML
	 * 
	 * @return
	 */
	public String toActHtml() {
		if (webUser instanceof PreviewUser) {
			return getActBtnView4Preview();
		}
		return toActHtml(PermissionType.MODIFY);
	}

	/**
	 * 输出操作XML
	 * 
	 * @return
	 */
	public String toActXml(int pType) {
		boolean isEdit = true;
		StringBuffer xmlBuffer = new StringBuffer();
		try {
			Document parent = (Document) request.getAttribute("parent");
			Document tdoc = parent != null ? parent : new Document();

			if (parent != null
					|| !StringUtil.isBlank(params
							.getParameterAsString("parentid"))) {
				isEdit = !StringUtil.isBlank(request.getParameter("isedit")) ? Boolean
						.parseBoolean(request.getParameter("isedit")) : true;
			}

			IRunner runner = getRunner();
			Iterator<Activity> aiter = view.getActivitys().iterator();
			// 标志含有批量审批按钮
			while (aiter.hasNext()) {
				Activity act = (Activity) aiter.next();
				act.setApplicationid(view.getApplicationid());

				// 如果有批量提交按钮添加审批备注属性
				if (act.getType() == 20) {
					continue;
				}
				int permissionType = pType;
				if (act.isReadonly(this.getRunner(), view.getName())) {
					permissionType = PermissionType.DISABLED;
				}

				if (!act.isHidden(runner, view, tdoc, webUser, ResVO.VIEW_TYPE)
						&& isEdit) {
					if (act.isShowInToolbar()) {
						xmlBuffer.append(act.toXml(permissionType));
					}
				} else {
					mapReadonly = true;
				}

			}
		} catch (Exception e) {
			LOG.warn("toActHtml", e);
		}

		return xmlBuffer.toString();
	}

	/**
	 * 输出操作HTML
	 * 
	 * @return
	 */
	public String toActHtml(int pType) {
		boolean isEdit = true;
		StringBuffer htmlBuffer = new StringBuffer();
		try {
			Document parent = (Document) request.getAttribute("parent");
			Document tdoc = parent != null ? parent : new Document();

			if (parent != null
					|| !StringUtil.isBlank(params
							.getParameterAsString("parentid"))) {
				isEdit = !StringUtil.isBlank(request.getParameter("isedit")) ? Boolean
						.parseBoolean(request.getParameter("isedit")) : true;
			}
			String parentid = params.getParameterAsString("parentid");
			if(parent != null && !StringUtil.isBlank(parentid) ){
					//把父文档写入缓存，因为父文档执行更新操作后会清空缓存，导致包含元素无法通过缓存的方式获取父文档
					this.webUser.putToTmpspace(parent.getId(), parent);
			}

			IRunner runner = getRunner();
			Iterator<Activity> aiter = view.getActivitys().iterator();
			// 标志含有批量审批按钮
			boolean isBATCH_APPROVEAct = false;
			Activity batch_approve_act = null;
			while (aiter.hasNext()) {
				Activity act = (Activity) aiter.next();
				act.setView(view);
				act.setApplicationid(view.getApplicationid());
				isBATCH_APPROVEAct = false;

				// 如果有批量提交按钮添加审批备注属性
				if (act.getType() == 20) {
					isBATCH_APPROVEAct = true;
					batch_approve_act = act;
				}
				int permissionType = pType;
				if (act.isReadonly(this.getRunner(), view.getName())) {
					permissionType = PermissionType.DISABLED;
				}

				if (!act.isHidden(runner, view, tdoc, webUser, ResVO.VIEW_TYPE)
						&& isEdit) {
					if (act.isShowInToolbar()) {
						htmlBuffer.append(act.toHtml(permissionType));
					}
				} else {
					mapReadonly = true;
				}

				if (isBATCH_APPROVEAct) {
					htmlBuffer
							.append("<textarea id='_attitude"
									+ batch_approve_act.getId()
									+ "' type='text' style='display:none;' name='_attitude"
									+ batch_approve_act.getId()
									+ "'></textarea>");
					htmlBuffer
							.append("<div id='inputAuditRemarkDiv"
									+ batch_approve_act.getId()
									+ "' style='display:none;width:280;' title='{*[cn.myapps.core.dynaform.view.input_audit_remark]*}'><textarea id='temp_attitude"
									+ batch_approve_act.getId()
									+ "' rows='12' cols='35' name='temp_attitude"
									+ batch_approve_act.getId()
									+ "' style='width:97%;'></textarea></div>");
				}
			}
			// grid视图添加保存和取消按钮
			if (view.getOpenType() == View.OPEN_TYPE_GRID) {
				htmlBuffer
						.append("<input moduleType='activityButton' class='activity' type='hidden' id='doSave_btn' icon='4' value='{*[Save]*}'  onclick=\"doSave()\" />");
				htmlBuffer
						.append("<input moduleType='activityButton' class='activity' type='hidden' id='doCancelAll_btn' name='button_act' icon='4' value='{*[Cancel]*}{*[All]*}'  onclick=\"doCancelAll()\" />");
			}

		} catch (Exception e) {
			LOG.warn("toActHtml", e);
		}

		return htmlBuffer.toString();
	}

	/**
	 * 是否显示查询表单
	 * 
	 * @return 是否显示查询表单
	 */
	public boolean isShowSearchForm() {
		if (!StringUtil.isBlank(request.getParameter("isprint"))) {
			return false;
		}
		return view.getSearchForm() != null;
	}

	/**
	 * 是否显示查询表单常规按钮(查询、重置)
	 * 
	 * @return 是否显示查询表单
	 */
	public boolean isShowSearchFormButton() {
		try {
			Form searchForm = view.getSearchForm();
			if (searchForm != null && searchForm.getFields().size() > 0) {
				return searchForm.checkDisplayType();
			}
		} catch (Exception e) {
			LOG.warn("isShowSearchFormButton", e);
		}

		return false;
	}

	/**
	 * 输出查询表单HTML
	 * 
	 * @return
	 */
	public String toSearchFormHtml() {
		try {
			Form searchForm = view.getSearchForm();
			Collection<FormField> fields = searchForm.getAllFields();// 当查询表单有数字类型字段时
			Iterator<FormField> it = fields.iterator(); // 而此字段没有值时
			while (it.hasNext()) { // 从params中去除
				FormField field = it.next();
				if (field.getFieldtype() != null) {
					if (field.getFieldtype().equals(Item.VALUE_TYPE_NUMBER)) {
						String value = params.getParameterAsString(field
								.getName());
						if (StringUtil.isBlank(value)) {
							params.removeParameter(field.getName());
						}
					}
				}
			}
			Document searchDoc = searchForm.createDocument(params, webUser);
			// searchDoc = searchDoc == null ? new Document() : searchDoc;
			String ehtml = searchForm.toHtml(searchDoc, params, webUser,
					new ArrayList<ValidateMessage>());
			
			
			//设置常用查询头
			String commonFilterCondition = view.getCommonFilterCondition();
			//commonFilterCondition= "[{field:'标题',isCommonFilter:'true'},{field:'编号',isCommonFilter:'true'}]";
			if(!StringUtil.isBlank(commonFilterCondition)){
				JSONArray commonFilters = JSONArray.fromObject(commonFilterCondition);
				if(commonFilters.size()>0){
					ehtml = toSetCommonFilterCondition(commonFilters,ehtml);
				}
			}
			return ehtml;
		} catch (Exception e) {
			LOG.warn("toSearchFormHtml", e);
		}

		return "";
	}

	private String toSetCommonFilterCondition(JSONArray commonFilters,String ehtml) {
		
		JSONObject commonFilter ;
		
		for(int index = 0 ; index < commonFilters.size() ; index ++){
			commonFilter = commonFilters.getJSONObject(index);
			String name = commonFilter.getString("field");
			String flag1 = "_"+name+"\"";
			String flag2 = "_"+name+"\'";
			int pos1 = ehtml.indexOf(flag1);
			int pos2 = ehtml.indexOf(flag2);
			if(pos1 > 0){
				String replaceStr = flag1 + " _isCommonFilter = 'true' ";
				ehtml = ehtml.replace(flag1, replaceStr);
			}else if(pos2 > 0){
				String replaceStr = flag2 + " _isCommonFilter = 'true' ";
				ehtml = ehtml.replace(flag2, replaceStr);
			}
		}
		
		return ehtml;
	}

	/**
	 * 是否隐藏列
	 * 
	 * @param column
	 *            当前列
	 * @return
	 */
	public boolean isHiddenColumn(Column column) {
		try {
			if (columnHiddenMap.containsKey(column.getId())) {
				return ((Boolean) columnHiddenMap.get(column.getId()))
						.booleanValue();
			}
			if (column.getHiddenScript() != null
					&& column.getHiddenScript().trim().length() > 0) {
				StringBuffer label = new StringBuffer();
				label.append("View").append("." + view.getName())
						.append(".Activity(").append(column.getId())
						.append(")." + column.getName())
						.append(".runHiddenScript");

				IRunner runner = getRunner();
				Object result = runner.run(label.toString(),
						column.getHiddenScript());// 运行脚本
				if (result != null && result instanceof Boolean) {
					columnHiddenMap.put(column.getId(), result);
					return ((Boolean) result).booleanValue();
				} else {
					columnHiddenMap.put(column.getId(), Boolean.valueOf(false));
				}
			} else {
				columnHiddenMap.put(column.getId(), false);
			}
		} catch (Exception e) {
			LOG.warn("isHiddenColumn", e);
		}

		return false;
	}

	/**
	 * 是否存在电子签章
	 * 
	 * @return
	 */
	public boolean isSignatureExist() {
		Boolean signatureExist = SignatureHelper.signatureExistMethod(null,
				view.getActivitys());
		return signatureExist != null ? signatureExist.booleanValue() : false;
	}

	/**
	 * 获取电子签章信息
	 * 
	 * @param dataPackage
	 *            数据包
	 * @return
	 */
	public Map<String, String> getSignatureInfo(
			DataPackage<Document> dataPackage) {
		if (dataPackage.rowCount > 0) {
			Document doc = (Document) dataPackage.datas.iterator().next();
			return getSignatureInfo(doc);
		}

		return new HashMap<String, String>();
	}

	/**
	 * 获取电子签章信息
	 * 
	 * @param doc
	 *            文档
	 * @return
	 */
	public Map<String, String> getSignatureInfo(Document doc) {
		Map<String, String> rtn = new HashMap<String, String>();

		String GetBatchDocument = "/portal/dynaform/mysignature/getBatchDocument.action";
		String mDoCommand = "/portal/dynaform/mysignature/doCommand.action";
		String mHttpUrlName = request.getRequestURI();
		String mGetBatchDocumentUrl = "http://" + request.getServerName() + ":"
				+ request.getServerPort()
				+ mHttpUrlName.substring(0, mHttpUrlName.indexOf("/", 2))
				+ GetBatchDocument;
		String mDoCommandUrl = "http://" + request.getServerName() + ":"
				+ request.getServerPort()
				+ mHttpUrlName.substring(0, mHttpUrlName.indexOf("/", 2))
				+ mDoCommand;

		String FormID = doc.getFormid();
		rtn.put("FormID", FormID);
		rtn.put("DocumentID", doc.getId());
		rtn.put("DomainID", doc.getDomainid());
		rtn.put("ApplicationID", doc.getApplicationid());
		rtn.put("mGetBatchDocumentUrl", mGetBatchDocumentUrl);
		rtn.put("mDoCommandUrl", mDoCommandUrl);

		return rtn;
	}

	/**
	 * 根据表单文档输出表格行XML
	 * 
	 * @param doc
	 *            表单文档
	 * @param columnOptionsCache
	 *            缓存控件选项(选项设计模式构建的Options)的Map
	 * @return
	 * @throws Exception
	 */
	public String toRowXml(Document doc, Map<String, Options> columnOptionsCache)
			throws Exception {
		StringBuffer xmlBuffer = new StringBuffer();
		try {
			IRunner runner = getRunner();
			runner.initBSFManager(doc, params, webUser,
					new ArrayList<ValidateMessage>());
			Iterator<Column> iter = view.getColumns().iterator();
			Iterator<Column> _iter = view.getColumns().iterator();

			// 先使用map保存列的值,可供操作列的跳转按钮使用
			Map<String, String> resultMap = new HashMap<String, String>();
			while (_iter.hasNext()) {
				Column _col = _iter.next();
				FormField _field = _col.getFormField();
				String _result = "";
				if (_field instanceof ImageUploadField
						|| _field instanceof ImageUploadToDataBaseField
						|| _field instanceof OnLineTakePhotoField
						|| _field instanceof FileManagerField) {
					doc.get_params().setParameter("viewReadType",
							view.getReadonly());
					_result = _col.getText(doc, runner, webUser,
							columnOptionsCache);
				} else {
					_result = _col.getText(doc, runner, webUser,
							columnOptionsCache);
				}

				resultMap.put(_col.getId(), _result);
			}
			while (iter.hasNext()) {
				Column col = iter.next();
				String result = resultMap.get(col.getId());

				if (col.getFlowReturnCss())
					result = col.getFlowReturnOperation(doc, result);

				xmlBuffer.append("<TD");
				xmlBuffer.append(" DOCID='").append(doc.getId()).append("'");
				xmlBuffer.append(" HIDDEN='").append(isHiddenColumn(col))
						.append("'");
				xmlBuffer.append(" TYPE='").append(col.getType()).append("'");
				xmlBuffer.append(" FIELDNAME='").append(col.getFieldName())
						.append("'");
				xmlBuffer.append(" ID='").append(col.getId()).append("'");
				xmlBuffer
						.append(" JUMPMAPPING='")
						.append(getJumpMapping(col.getJumpMapping(), resultMap))
						.append("'");
				xmlBuffer.append(">");

				xmlBuffer.append(HtmlEncoder.encode(replaceHTML(result)));

				xmlBuffer.append("</TD>");
			}
		} catch (Exception e) {
			LOG.warn("toRowXML", e);
			throw new Exception(e);
		}

		return xmlBuffer.toString();
	}

	/**
	 * 根据表单文档输出表格行HTML
	 * 
	 * @param doc
	 *            表单文档
	 * @param columnOptionsCache
	 *            缓存控件选项(选项设计模式构建的Options)的Map
	 * @return
	 * @throws Exception
	 */
	public String toRowHtml(Document doc,
			Map<String, Options> columnOptionsCache) throws Exception {
		StringBuffer htmlBuffer = new StringBuffer();
		boolean isEdit = true;
		isEdit = !StringUtil.isBlank(request.getParameter("isedit")) ? Boolean
				.parseBoolean(request.getParameter("isedit")) : true;
		try {
			IRunner runner = getRunner();
			runner.initBSFManager(doc, params, webUser,
					new ArrayList<ValidateMessage>());
			Iterator<Column> iter = view.getColumns().iterator();
			Iterator<Column> _iter = view.getColumns().iterator();

			// 先使用map保存列的值,可供操作列的跳转按钮使用
			Map<String, String> resultMap = new HashMap<String, String>();
			while (_iter.hasNext()) {
				Column _col = _iter.next();
				FormField _field = _col.getFormField();
				String _result = "";
				if (_field instanceof ImageUploadField
						|| _field instanceof ImageUploadToDataBaseField
						|| _field instanceof OnLineTakePhotoField
						|| _field instanceof FileManagerField) {
					doc.get_params().setParameter("viewReadType",
							view.getReadonly());
					_result = _col.getText(doc, runner, webUser,
							columnOptionsCache);
				} else {
					_result = _col.getText(doc, runner, webUser,
							columnOptionsCache);
				}

				resultMap.put(_col.getId(), _result);
			}
			while (iter.hasNext()) {
				Column col = iter.next();
				FormField field = col.getFormField();
				String result = resultMap.get(col.getId());

				if (col.getFlowReturnCss())
					result = col.getFlowReturnOperation(doc, result);


				htmlBuffer.append("<td");
				htmlBuffer.append(" fieldType='" + (field.getType() != null ? field.getType() : "") + "'");
				htmlBuffer.append(" showType='" + (col.getShowType() != null ? col.getShowType() : "") + "'");
//				htmlBuffer.append(" isHidden='" + isHiddenColumn(col) + "'");	//由前台js根据列头处理
				htmlBuffer.append(" isVisible='" + col.isVisible() + "'");
				htmlBuffer.append(" isReadonly='" + view.getReadonly() + "'");
				htmlBuffer.append(" colType='" + (col.getType() != null ? col.getType() : "") + "'");
				htmlBuffer.append(" fieldInstanceOfWordField='"
						+ (field instanceof WordField) + "'");
				htmlBuffer.append(" fieldInstanceOfMapField='"
						+ (field instanceof MapField) + "'");
				htmlBuffer.append(" isTreeDepartmentField='"
						+ (field instanceof TreeDepartmentField) + "'");
				htmlBuffer
						.append(" displayType='" + (col.getDisplayType() != null ? col.getDisplayType() : "") + "'");
				htmlBuffer.append(" isShowTitle='" + col.isShowTitle() + "'");
				htmlBuffer.append(" colDisplayLength='"
						+ (col.getDisplayLength() != null ? col.getDisplayLength() : "") + "'");
				htmlBuffer.append(" colFieldName='" + (col.getFieldName() != null ? col.getFieldName() : "") + "'");
				htmlBuffer.append(" colFlowReturnCss='"
						+ col.getFlowReturnCss() + "'");
				htmlBuffer.append(" docId='" + (doc.getId() != null ? doc.getId() : "") + "'");
				htmlBuffer.append(" docFormid='" + (doc.getFormid() != null ? doc.getFormid() : "") + "'");
				htmlBuffer.append(" isSignatureExist='" + isSignatureExist()
						+ "'");
				htmlBuffer.append(" isEdit='" + isEdit + "'");
				htmlBuffer.append(" viewDisplayType='" + (view.getDisplayType() != null ? view.getDisplayType() : "")
						+ "'");
				htmlBuffer.append(" viewTemplateForm='"
						+ (view.getTemplateForm() != null ? view.getTemplateForm() : "") + "'");
				htmlBuffer.append(" colTemplateForm='" + (col.getTemplateForm() != null ? col.getTemplateForm() : "")
						+ "'");
				htmlBuffer.append(" colWidth='" + (col.getWidth() != null ? col.getWidth() : "") + "'");
				htmlBuffer.append(" colGroundColor='" + (col.getGroundColor() != null ? col.getGroundColor() : "")
						+ "'");
				htmlBuffer.append(" colColor='" + (col.getColor() != null ? col.getColor() : "") + "'");
				htmlBuffer.append(" colFontSize='" + (col.getFontSize() != null ? col.getFontSize() : "") + "'");
				htmlBuffer.append(" colButtonType='" + (col.getButtonType() != null ? col.getButtonType() : "")
						+ "'");
				htmlBuffer.append(" colApproveLimit='" + (col.getApproveLimit() != null ? col.getApproveLimit() : "")
						+ "'");
				htmlBuffer.append(" colButtonName='" + (col.getButtonName() != null ? col.getButtonName() : "")
						+ "'");
				htmlBuffer.append(" colMappingform='" + (col.getMappingform() != null ? col.getMappingform() : "")
						+ "'");
				htmlBuffer.append(" colIcon='" + (col.getIcon() != null ? col.getIcon() : "") + "'");
				htmlBuffer.append(" colId='" + (col.getId() != null ? col.getId() : "") + "'");
				htmlBuffer.append(" showWord='{*[Show]*} {*[Word]*}'");
				
				if (col.isShowIcon()) {
					String[] iconMappings = col.getIconMapping().split(";");
					for (int i = 0; i < iconMappings.length; i++) {
						String[] iconMapping = iconMappings[i].split(":");
						if (result.equals(iconMapping[0])) {
							htmlBuffer.append(" showIcon='")
									.append(iconMapping[1]).append("'");
							;
						}
					}
				}

				htmlBuffer.append(" ><div name='result'>" + result
						+ "</div><div style='display:none' name='jumpMapping'>"
						+ getJumpMapping(col.getJumpMapping(), resultMap)
						+ "</div></td>");
			}
		} catch (Exception e) {
			LOG.warn("toRowHtml", e);
			throw new Exception(e);
		}

		return htmlBuffer.toString();
	}

	/**
	 * 获取宏脚本执行器
	 * 
	 * @return
	 * @throws Exception
	 */
	public IRunner getRunner() throws Exception {
		if (runner == null) {
			Document parent = (Document) request.getAttribute("parent");
			Document tdoc = parent != null ? parent : new Document();
			runner = JavaScriptFactory.getInstance(
					request.getSession().getId(), view.getApplicationid());
			runner.initBSFManager(tdoc, params, webUser,
					new ArrayList<ValidateMessage>());
		}

		return runner;
	}

	public String getViewStyle() {
		if (view == null || view.getStyle() == null) {
			return null;
		}
		return view.getStyle().getId();
	}

	public String getApplicationid() throws Exception {
		if (view == null) {
			String viewid = params.getParameterAsString("_viewid");
			ViewProcess process = (ViewProcess) ProcessFactory
					.createProcess(ViewProcess.class);
			view = (View) process.doView(viewid);
		}
		return view.getApplicationid();
	}

	/**
	 * 输出右键菜单脚本
	 */
	public String toContextMenuHtml() {
		StringBuffer htmlBuffer = new StringBuffer();
		boolean flag = false;
		boolean isReadOnly = false;
		try {
			IRunner runner = getRunner();
			Document parent = (Document) request.getAttribute("parent");
			Document tdoc = parent != null ? parent : new Document();

			StringBuffer MenuData = new StringBuffer();
			MenuData.append("[[");
			htmlBuffer.append("<script lanaguage=\"javaScript\"> ");
			htmlBuffer.append("function initContextMenu(){ ");
			Iterator<Activity> aiter = view.getActivitys().iterator();
			while (aiter.hasNext()) {
				Activity act = aiter.next();
				isReadOnly = act.isReadonly(runner, view.getName());
				String actName = "";
				if (StringUtil.isBlank(act.getMultiLanguageLabel())) {
					actName = act.getName();
				} else {
					actName = "{*[" + act.getMultiLanguageLabel() + "]*}";
				}
				if (act.getType() == 2 || act.getType() == 3
						|| act.getType() == 20 || act.getType() == 29) {
					/**
					 * 添加右键菜单--新建
					 */
					if (act.getType() == 2
							&& act.isContextMenu()
							&& !act.isHidden(runner, view, tdoc, webUser,
									ResVO.VIEW_TYPE)) {
						htmlBuffer.append("var objNew = {text: " + "\""
								+ actName + "\"," + "isReadOnly:" + "\""
								+ isReadOnly + "\"," + "func: function() {"
								+ "doNew('" + act.getId() + "');}};");
						MenuData.append("objNew,");
						flag = true;
					}

					/**
					 * 添加右键菜单--删除
					 */
					if (act.getType() == 3
							&& act.isContextMenu()
							&& !act.isHidden(runner, view, tdoc, webUser,
									ResVO.VIEW_TYPE)) {
						htmlBuffer
								.append("var objDelete = {text: "
										+ "\""
										+ actName
										+ "\","
										+ "isReadOnly:"
										+ "\""
										+ isReadOnly
										+ "\","
										+ "func: function() {jQuery(this).find(\"input\").attr(\"checked\", \"checked\");"
										+ "doRemove('" + act.getId() + "');}};");
						MenuData.append("objDelete,");
						flag = true;
					}

					/**
					 * 添加右键菜单--批量审批
					 */
					if (act.getType() == 20
							&& act.isContextMenu()
							&& !act.isHidden(runner, view, tdoc, webUser,
									ResVO.VIEW_TYPE)) {
						htmlBuffer
								.append("var objDoFlow = {text: "
										+ "\""
										+ actName
										+ "\","
										+ "isReadOnly:"
										+ "\""
										+ isReadOnly
										+ "\","
										+ "func: function() {jQuery(this).find(\"input\").attr(\"checked\", \"checked\");"
										+ "doBatchApprove('" + act.getId()
										+ "', " + "true);}};");
						MenuData.append("objDoFlow,");
						flag = true;
					}

					/**
					 * 添加右键菜单--批量签章
					 */
					if (act.getType() == 29
							&& act.isContextMenu()
							&& !act.isHidden(runner, view, tdoc, webUser,
									ResVO.VIEW_TYPE)) {
						htmlBuffer
								.append("var objSignature = {text: "
										+ "\""
										+ actName
										+ "\","
										+ "isReadOnly:"
										+ "\""
										+ isReadOnly
										+ "\","
										+ "func: function() {jQuery(this).find(\"input\").attr(\"checked\", \"checked\");"
										+ "DoBatchSignature();}};");
						MenuData.append("objSignature,");
						flag = true;
					}
				}
			}
			if (MenuData.length() > 2) {
				MenuData.setLength(MenuData.length() - 1);
			}
			MenuData.append("]]");
			htmlBuffer.append("var MenuData = " + MenuData.toString() + ";");
			if (flag) {
				htmlBuffer
						.append("jQuery(\"tr\").smartMenu(MenuData, {name: \"mail\"});");
			}
			htmlBuffer.append("} ");
			htmlBuffer.append("</script>");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlBuffer.toString();
	}

	// 根据跳转操作列的映射生成 参数名称:列值 映射
	private String getJumpMapping(String jumpMapping,
			Map<String, String> resultMap) {
		String _jumpMapping = "[";
		if (!StringUtil.isBlank(jumpMapping)) {
			String[] maps = jumpMapping.split(";");
			for (int i = 0; i < maps.length; i++) {
				String[] map = maps[i].split(":");
				String agr = map[0];
				String col = map[1];
				String value = (String) resultMap.get(col);
				String colValue = value.replaceAll("\'", "\\\\'");
				_jumpMapping += "{name:\'" + agr + "'," + "value:'" + colValue
						+ "'},";
			}

			if (_jumpMapping.length() > 1) {
				_jumpMapping = _jumpMapping.substring(0,
						_jumpMapping.length() - 1);
			}
		}
		_jumpMapping += "]";

		return _jumpMapping;
	}

	/**
	 * 输出列表视图（手机客户端客户端）
	 */
	public String toXMLText(DataPackage<Document> datas) {
		StringBuilder xml = new StringBuilder();

		// 首行（列头）----start
		xml.append("<TH>");// 首列
		for (Iterator<Column> iterator = this.view.getColumns().iterator(); iterator
				.hasNext();) {
			Column column = iterator.next();
			String colName = column.getName();
			IRunner runner = null;
			try {
				runner = getRunner();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String colText = "";
			String multiLanguageLabel = column.getMultiLanguageLabel();
			if (!StringUtil.isBlank(multiLanguageLabel)
					&& multiLanguageLabel != null) {
				colText = "{*[" + multiLanguageLabel + "]*}";
			} else {
				colText = colName;
			}
			xml.append("<TD");
			xml.append(" HIDDEN").append("='")
					.append(column.isHiddenColumn(runner)).append("'");
			xml.append(">");
			xml.append(HtmlEncoder.encode(replaceHTML(colText)));
			xml.append("</TD>");
		}
		xml.append("</TH>");
		// 首行（列头）----end

		Collection<Document> docs = datas.datas;
		// 数据行
		/**
		 * 缓存控件选项(选项设计模式构建的Options)的Map key--fieldId value--Options
		 */
		Map<String, Options> columnOptionsCache = new HashMap<String, Options>();
		for (Iterator<Document> iterator = docs.iterator(); iterator.hasNext();) {
			Document doc = iterator.next();
			xml.append("<TR");
			xml.append(" DOCID='").append(doc.getId()).append("'");
			xml.append(">");
			try {
				xml.append(toRowXml(doc, columnOptionsCache));// 其他列
			} catch (Exception e) {
				e.printStackTrace();
			}
			xml.append("</TR>");
		}
		return xml.toString();
	}

	/**
	 * 输出列表视图
	 * 
	 * @return
	 */
	public String toHTMLText() {
		StringBuilder html = new StringBuilder();
		html.append("<table class=\"viewTable\" moduleType=\"viewList\" style=\"display:none;\"");
		String _sortCol = (String) request.getAttribute("_sortCol");//
		// 可排序图标
		String _sortStatus = (String) request.getAttribute("_sortStatus");
		html.append(" _sortCol=\"" + (_sortCol != null ? _sortCol : "") + "\"");
		html.append(" _sortStatus=\"" + (_sortStatus != null ? _sortStatus : "") + "\"");
		html.append(" isSum=\"" + view.isSum() + "\"");
		html.append(" >");

		// 首行（列头）----start
		html.append("<tr class='header'><td></td>");// 首列
		for (Iterator<Column> iterator = this.view.getColumns().iterator(); iterator
				.hasNext();) {
			Column column = iterator.next();
			String colName = column.getName();
			IRunner runner = null;
			try {
				runner = getRunner();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String colText = "";
			String multiLanguageLabel = column.getMultiLanguageLabel();
			if (!StringUtil.isBlank(multiLanguageLabel)
					&& multiLanguageLabel != null) {
				colText = "{*[" + multiLanguageLabel + "]*}";
			} else {
				colText = colName;
			}
			html.append("<td");// 其他列
			html.append(" colName=\"" + (column.getName() !=null? column.getName():"") + "\"");
			html.append(" colText=\"" + (colText != null ? colText : "") + "\"");
			html.append(" isVisible=\"" + column.isVisible() + "\"");
			html.append(" isHiddenColumn=\"" + column.isHiddenColumn(runner)
					+ "\"");
			html.append(" colWidth=\"" + (column.getWidth() != null ? column.getWidth() : "") + "\"");
			html.append(" colType=\"" + (column.getType() != null ? column.getType() : "") + "\"");
			html.append(" colFieldName=\"" + (column.getFieldName() != null ? column.getFieldName() : "") + "\"");
			html.append(" isOrderByField=\"" + column.isClickSorting() + "\" >");
			html.append(colText + "</td>");
		}
		html.append("</tr>");// 首行（列头）----end

		DataPackage<Document> datas = (DataPackage<Document>) request
				.getAttribute("datas");
		Collection<Document> docs = datas.datas;
		// 数据行
		/**
		 * 缓存控件选项(选项设计模式构建的Options)的Map key--fieldId value--Options
		 */
		Map<String, Options> columnOptionsCache = new HashMap<String, Options>();
		for (Iterator<Document> iterator = docs.iterator(); iterator.hasNext();) {
			Document doc = iterator.next();
			html.append("<tr trType='dataTr'><td colId=\"" + doc.getId()
					+ "\"></td>");// 首列
			try {
				html.append(toRowHtml(doc, columnOptionsCache));// 其他列
			} catch (Exception e) {
				e.printStackTrace();
			}
			html.append("</tr>");
		}

		// 末行（字段值汇总）
		if (this.view.isSum()) {
			ParamsTable params = ParamsTable.convertHTTP(request);
			html.append("<tr id=\"sumTrId\"><td>&nbsp;</td>");// 首列
			for (Iterator<Column> iterator = this.view.getColumns().iterator(); iterator
					.hasNext();) {
				Column column = iterator.next();
				if (column.isVisible() && !column.isHiddenColumn(runner)) {
					html.append("<td");// 其他列
					html.append(" isVisible=\"" + column.isVisible() + "\"");
					html.append(" isHiddenColumn=\""
							+ column.isHiddenColumn(runner) + "\"");
					html.append(" isSum=\"" + column.isSum() + "\"");
					html.append(" isTotal=\"" + column.isTotal() + "\"");
					html.append(" colName=\"" + (column.getName() != null ? column.getName() : "") + "\"");
					html.append(" sumByDatas=\""
							+ column.getSumByDatas(datas, runner, webUser)
							+ "\"");
					html.append(" sumTotal=\""
							+ column.getSumTotal(webUser,
									webUser.getDomainid(), params) + "\"");
					html.append(" >");
					if (column.isSum() || column.isTotal())
						html.append(column.getName());
					if (column.isSum())
						html.append(column
								.getSumByDatas(datas, runner, webUser)
								+ "&nbsp;");
					if (column.isTotal())
						html.append(column.getSumTotal(webUser,
								webUser.getDomainid(), params)
								+ "&nbsp;");
					html.append("</td>");
				}
			}
			html.append("</tr>");
		}

		html.append("</table>");
		return html.toString();
	}

	// 输出网格视图
	@SuppressWarnings("unchecked")
	public String toHTMLText4Grid() {
		StringBuffer html = new StringBuffer();

		// 排序的参数
		String _sortCol = (String) request.getAttribute("_sortCol");
		String _sortStatus = (String) request.getAttribute("_sortStatus");

		html.append("<table class=\"viewTable\" moduleType=\"subGridView\" style=\"display:none;\"");
		html.append(" isSum=\"" + view.isSum() + "\"");
		html.append(" _sortCol=\"" + (_sortCol != null ? _sortCol : "") + "\"");
		html.append(" _sortStatus=\"" + (_sortStatus != null ? _sortStatus : "") + "\"");
		html.append(">");
		// table-header
		html.append("<tr>");
		html.append("<td>");
		html.append("<input type=\"checkbox\" onclick=\"selectAll(this.checked)\">");
		html.append("</td>");
		Collection<Column> cols = view.getColumns();
		for (Iterator<Column> it = cols.iterator(); it.hasNext();) {
			Column col = it.next();
			String colName = col.getName();
			String colText = "";
			String multiLanguageLabel = col.getMultiLanguageLabel();
			if (!StringUtil.isBlank(multiLanguageLabel)
					&& multiLanguageLabel != null) {
				colText = "{*[" + multiLanguageLabel + "]*}";
			} else {
				colText = "{*[" + colName + "]*}";
			}
			html.append("<td");
			html.append(" colText=\"" + (colText != null ? colText : "") + "\"");
			html.append(" isVisible = '" + col.isVisible() + "'");
			html.append(" isHiddenColumn = '" + isHiddenColumn(col) + "'");
			html.append(" isBlank = '" + StringUtil.isBlank(col.getWidth())
					+ "'");
			html.append(" id = '" + (col.getName() != null ? col.getName() : "") + "_showType'");
			html.append(" value = '" + (col.getShowType() != null ? col.getShowType() : "") + "'");
			html.append(" colWidth = '" + (col.getWidth() != null ? col.getWidth() : "") + "'");
			html.append(" colFieldName ='" + (col.getFieldName() != null ? col.getFieldName() : "") + "'");
			html.append(" isEmpty = '" + cols.isEmpty() + "'");
			html.append(" colType ='" + (col.getType() != null ? col.getType() : "") + "'");
			html.append(" isOrderByField=\"" + (col.getIsOrderByField() != null ? col.getIsOrderByField() : "") + "\"");
			html.append(" >");
			html.append("<input id='" + (col.getName() != null ? col.getName() : "") + "_showType'");
			html.append(" value='" + (col.getShowType() != null ? col.getShowType() : "") + "' type=\"hidden\">");
			html.append(colText + "</td>");
		}
		if (!cols.isEmpty()) {
			html.append("<td");
			html.append(" actions = '{*[Actions]*}'");
			html.append(" >");
			html.append("{*[Actions]*}");
			html.append("</td>");
		}
		html.append("</tr>");
		// end of table-header

		WebUser user = (WebUser) request.getSession().getAttribute(
				Web.SESSION_ATTRIBUTE_FRONT_USER);
		boolean isEdit = !StringUtil.isBlank(request.getParameter("isedit")) ? Boolean
				.parseBoolean(request.getParameter("isedit")) : true;
		IRunner jsrun = JavaScriptFactory.getInstance(params.getSessionid(),
				view.getApplicationid());
		DataPackage<Document> datas = (DataPackage<Document>) request
				.getAttribute("datas");
		Collection<Document> docs = datas.datas;

		// data iterator
		html.append("<tbody id=\"dataBody\">");
		try {
			/**
			 * 缓存控件选项(选项设计模式构建的Options)的Map key--fieldId value--Options
			 */
			Map<String, Options> columnOptionsCache = new HashMap<String, Options>();
			for (Iterator<Document> it = docs.iterator(); it.hasNext();) {
				Document doc = it.next();
				jsrun.initBSFManager(doc, params, user,
						new ArrayList<ValidateMessage>());
				html.append(view.toRowHtml(doc, jsrun, user, isEdit,
						columnOptionsCache));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 字段值汇总
		if (view.isSum()) {
			ParamsTable params = ParamsTable.convertHTTP(request);
			html.append("<tr id=\"sumTrId\">");
			html.append("<td>&nbsp;</td>");

			for (Iterator<Column> iterator = this.view.getColumns().iterator(); iterator
					.hasNext();) {
				Column column = iterator.next();
				if (column.isVisible() && !column.isHiddenColumn(runner)) {
					html.append("<td");
					html.append(" isVisible=\"" + column.isVisible() + "\"");
					html.append(" isHiddenColumn=\""
							+ column.isHiddenColumn(runner) + "\"");
					html.append(" isSum=\"" + column.isSum() + "\"");
					html.append(" isTotal=\"" + column.isTotal() + "\"");
					html.append(" colName=\"" + (column.getName() != null ? column.getName() : "") + "\"");
					html.append(" sumByDatas=\""
							+ column.getSumByDatas(datas, runner, webUser)
							+ "\"");
					html.append(" sumTotal=\""
							+ column.getSumTotal(webUser,
									webUser.getDomainid(), params) + "\"");
					html.append(" >");
					if (column.isSum() || column.isTotal()) {
						html.append(column.getName() + ":");
					}
					if (column.isSum()) {
						html.append(column.getSumByDatas(datas, runner, user));
					}
					if (column.isTotal()) {
						html.append(column.getSumTotal(user,
								user.getDomainid(), params));
					}
					html.append("</td>");
				}
			}

			html.append("<td>&nbsp;</td>");
			html.append("</tr>");
		}
		html.append("</tbody>");
		html.append("</table>");
		html.append(view.toRefreshFunction());
		return html.toString();
	}

	/**
	 * 正则表达式去掉html标签
	 * 
	 * @param htmlString
	 * @return
	 */
	public String replaceHTML(String htmlString) {
		String noHTMLString = htmlString.replaceAll("</?[^>]+>", "");
		noHTMLString = noHTMLString.replaceAll("\\&nbsp;", " ");
		return noHTMLString;
	}

	public String getViewTitle() {
		String title = null;
		if (view != null) {
			title = view.getDescription() == null
					|| view.getDescription().isEmpty() ? view.getName() : view
					.getDescription();
		}
		return title;
	}
	
	public View getView(){
		return this.view;
	}
}
