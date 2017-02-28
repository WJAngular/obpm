package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateSQLUtils;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.AbstractRunner;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.resource.ejb.ResourceType;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.PreviewUser;
import cn.myapps.util.Debug;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 引入视图组件
 * 
 * @author nicholas
 */
public class IncludedView extends IncludedElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1517041188945769061L;

	public View view;

	public IncludedView(IncludeField field) {
		super(field);
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		String html = "";
		try {

			view = (View) getValueObject(runner);

			// if (view != null && field.is()) {
			// html = toIntegrateHtml(doc, runner);
			if (view != null) {
				html = toFrameHtml(doc, runner, webUser);
			}

			// "isEnabled=ture" means that hidden the view when data not exit
			if (field.isEnabled()) {
				if (isEmpty(doc, runner)) {
					html = "";
				}
			}
		} catch (Exception e) {
			Debug.println(e.getMessage());
			e.printStackTrace();
		}

		return html;
	}

	private String toFrameHtml(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return toFrameXml(doc, runner, webUser, "HTML");
	}

	private String toFrameXml(Document doc, IRunner runner, WebUser webUser, String type) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = field.getDisplayType(doc, runner, webUser);
		if (type.equals("HTML")) {
			
			html.append("<input type='hidden' moduleType='IncludedView' id='" + view.getId() +"'");
			html.append(" userType = '" + (webUser instanceof PreviewUser)+"'");
			if(webUser instanceof PreviewUser)
				html.append(" skinType='"+((PreviewUser)webUser).getSkinType()+"'");
			html.append(" action='displayView.action'");
			html.append(" application='" + view.getApplicationid() + "'");
			html.append(" _viewid='" + view.getId() + "'");
			html.append(" _fieldid='" + field.getId() + "'");
			html.append(" _opentype='" + view.getOpenType() + "'");
			html.append(" parentid='");
			if(StringUtil.isBlank(doc.getId())){
				html.append("@");
			}else if("HOME_PAGE_ID".equals(doc.getId())){
				html.append("null");
				field.setRelate(false);
			}else{
				html.append(doc.getId());
			}
			html.append("' isRelate='" + field.isRelate() + "'");
			html.append(" divid='" + field.getId() + "_divid" + "'");
			html.append(" getEditMode='" + field.getEditMode(runner, doc, webUser).equals("false") + "'");
			html.append(" isRefreshOnChanged='" + field.isRefreshOnChanged() + "'");
			html.append(" getName='" + field.getName()+ "'");
			html.append(" fixation='" + field.isFixation() + "'");
			html.append(" fixationHeight='" + field.getFixationHeight() + "'");
			html.append(" />");
		} else if (type.equals("XML")) {
			html.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE).append(
					"='" + ResourceType.RESOURCE_TYPE_MOBILE + "'").append(" ").append(MobileConstant.ATT_NAME).append("='"+field.name+"'");

			if (displayType == PermissionType.READONLY  || displayType == PermissionType.DISABLED) {
				html.append(" ").append(MobileConstant.ATT_READONLY + "='true' ");
			}
			if (displayType == PermissionType.HIDDEN) {
				html.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
			}
			html.append(">");
			html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
					"='_viewid'>" + HtmlEncoder.encode(view.getId()) + "</").append(MobileConstant.TAG_PARAMETER)
					.append(">");

			if (doc.getId() != null && doc.getId().trim().length() > 0) {
				html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME)
						.append("='");
				if (field.isRelate()) { // 是否有父子关系
					html.append("isRelate'>" + field.isRelate() + "</").append(MobileConstant.TAG_PARAMETER).append(
					">");
					html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME)
					.append("='");
					html.append("parentid");
				} else {
					html.append("parentid");
				}
				html.append("'>" + HtmlEncoder.encode(doc.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(
						">");
			}
			if (field.isRefreshOnChanged()) {
				html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME)
						.append("='refresh'>true</").append(MobileConstant.TAG_PARAMETER).append(">");
			}
			html.append("</").append(MobileConstant.TAG_ACTION).append(">");
		}else if(type.equals("XML2")){
			html.append("<").append(MobileConstant2.TAG_INCLUDEFIELD);
			html.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(field.getName()).append("'");
//			html.append("LABEL").append("='").append(field.getDiscript()).append("'").append(" ");

			if (displayType == PermissionType.READONLY  || displayType == PermissionType.DISABLED) {
				html.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
			}
			if (displayType == PermissionType.HIDDEN) {
				html.append(" ").append(MobileConstant2.ATT_HIDDEN).append(" ='true'");
				if(!field.getHiddenValue().equals("") && !field.getHiddenValue().equals(null) && !field.getHiddenValue().equals("&nbsp;")){
					html.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(field.getHiddenValue()).append("'");
				}
			}
			
			html.append(" ").append(MobileConstant2.ATT_VIEWID).append("='").append(view.getId()).append("'");
			if (field.isRefreshOnChanged()) {
				html.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
			}
			if (field.isRelate()) {// 是否有父子关系
				html.append(" ").append(MobileConstant2.ATT_RELATE).append("='true'");
				html.append(" ").append(MobileConstant2.ATT_PARENTID).append("='").append(doc.getId()).append("'");
			}
			
			html.append(">");
			
//			if (doc.getId() != null && doc.getId().trim().length() > 0) {
//				html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME)
//						.append("='");
//				if (field.isRelate()) { // 是否有父子关系
//					html.append("isRelate'>" + field.isRelate() + "</").append(MobileConstant.TAG_PARAMETER).append(
//					">");
//					html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME)
//					.append("='");
//					html.append("parentid");
//				} else {
//					html.append("parentid");
//				}
//				html.append("'>" + HtmlEncoder.encode(doc.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(
//						">");
//			}
//			if (field.isRefreshOnChanged()) {
//				html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME)
//						.append("='refresh'>true</").append(MobileConstant.TAG_PARAMETER).append(">");
//			}
			html.append("</").append(MobileConstant2.TAG_INCLUDEFIELD).append(">");
		}
		return html.toString();
	}

	private String toIntegrateHtml(Document doc, IRunner runner, WebUser webUser, int pagelines) throws Exception {
		StringBuffer html = new StringBuffer();
		StringBuffer labelBuffer = new StringBuffer();
		labelBuffer.append(field.getScriptLable("toHtmlTxt"));

		if (view != null) {
			html.append("<table width='100%' class='display_view-table' style='border-top:1px solid #b5b8c8;border-left:1px solid #b5b8c8;position:relative; z-index:1' border='0' cellpadding='0' cellspacing='0' pageid='" + field.getId() + "'>");
			html.append("<tr class='ptable-header'>");

			appendHeadTD(html, runner);///////////

			html.append("</tr>");

			try {
				DataPackage<Document> datas = getDatas(doc, webUser, pagelines);
				if (datas != null) {
					if (datas.datas == null || datas.datas.size() == 0) {
						return "";
					} else {
						Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
						
						//序列号
						int rownum = 1; 
						for (Iterator<Document> iter = datas.datas.iterator(); iter.hasNext();) {
							Document data = (Document) iter.next();
							// 内部注册
							runner.initBSFManager(data, doc.get_params(), webUser, errors);
							html.append("<tr class=\"table-tr\">");
							appendDataTD(html, labelBuffer, data, runner, webUser, rownum++);//////////
							html.append("</tr>");
						}
						//汇总信息
						if(view.isSum()){
							html.append("<tr class=\"table-tr\">");
							appendTotalTD(html, labelBuffer, datas, runner, webUser);//////////
							html.append("</tr>");
						}
					}
				}
				html.append("</table>");
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				// 执行完内部注册后，重新注册外部文档
				runner.initBSFManager(doc, doc.get_params(), webUser, new ArrayList<ValidateMessage>());
			}
		}
		return html.toString();
	}

	private DataPackage<Document> getDatas(Document doc, WebUser user, int pagelines) throws Exception {
		DocumentProcess dp = new DocumentProcessBean(view.getApplicationid());
		ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);

		DataPackage<Document> datas = null;
		String parentid = (String) field.getDocParameter(doc, "_docid");
		boolean isRelate = field.isRelate();

		if (StringUtil.isBlank(parentid)) {
			parentid = doc.getId();
		}

		ParamsTable params = doc.get_params();
		String[] fields = view.getDefaultOrderFieldArr();
		params.setParameter("_sortCol", fields);
		HibernateSQLUtils sqlUtils = new HibernateSQLUtils();
		if (view.getEditMode().equals(View.EDIT_MODE_DESIGN)) {
			String sql = viewProcess.getQueryString(view, params, user, doc);
			if (!StringUtil.isBlank(parentid) && isRelate) {
				sql = sqlUtils.appendCondition(sql, "PARENT = '" + parentid + "'");
			}
			datas = dp.queryBySQLPage(sql, params, 1, pagelines, doc.getDomainid());
		} else if (view.getEditMode().equals(View.EDIT_MODE_CODE_DQL)) {
			String dql = viewProcess.getQueryString(view, params, user, doc);
			if (!StringUtil.isBlank(parentid) && isRelate) {
				dql += " and $parent.$id = '" + parentid + "'";
			}
			datas = dp.queryByDQLPage(dql, params, 1, pagelines, doc.getDomainid());

		} else if (view.getEditMode().equals(View.EDIT_MODE_CODE_SQL)) {
			String sql = viewProcess.getQueryString(view, params, user, doc);
			if (!StringUtil.isBlank(parentid) && isRelate) {
				sql = sqlUtils.appendCondition(sql, "PARENT = '" + parentid + "'");
			}
			datas = dp.queryBySQLPage(sql, params, 1, pagelines, doc.getDomainid());
		}

		return datas;
	}

	private void appendHeadTD(StringBuffer html, IRunner runner) {
		Collection<Column> columns = view.getColumns();
		for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
			Column clm = (Column) iter.next();
			if(!clm.isHiddenColumn(runner)){
				html.append("<td class='column-head'").append(
						clm.getWidth() != null && clm.getWidth().trim().length() > 0 ? " width='" + clm.getWidth() + "'"
								: "");
				html.append(">");
				html.append(clm.getName());
				html.append("</td>");
			}
		}
	}

	private void appendDataTD(StringBuffer html, StringBuffer label, Document doc, IRunner runner, WebUser webUser, int rownum)
			throws Exception {
		Collection<Column> columns = view.getColumns();
		
		for (Iterator<Column> iter2 = columns.iterator(); iter2.hasNext();) {
			Column col = (Column) iter2.next();
			if(!col.isHiddenColumn(runner)){
				html.append("<td class='column-td'>");
				if(col.getType().equals(Column.COLUMN_TYPE_ROWNUM)){
					html.append(rownum);
				}else{
					html.append(col.getText(doc, runner, webUser));
				}
				html.append("</td>");
			}
		}
	}
	
	private void appendTotalTD(StringBuffer html, StringBuffer labelBuffer,
			DataPackage<Document> datas, IRunner runner, WebUser webUser) {
		Collection<Column> columns = view.getColumns();
		for (Iterator<Column> iter2 = columns.iterator(); iter2.hasNext();) {
			Column col = (Column) iter2.next();
			if(!col.isHiddenColumn(runner)){
				html.append("<td class='column-td'>");
				if(col.isSum() || col.isTotal()){
					html.append(col.getName()).append(":");
					if(col.isSum()){
						html.append(col.getSumByDatas(datas, runner, webUser));
					}
					if(col.isTotal()){
						ParamsTable params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
						params.setParameter("_viewid", view.getId());
						if(field.isRelate()){
							params.setParameter("parentid", params.getParameter("_docid"));
							params.setParameter("isRelate", true);
							html.append(col.getSumTotal(webUser, webUser.getDomainid(), params));
						}else{
							html.append(col.getSumTotal(webUser, webUser.getDomainid(), params));
						}
					}
				}
				html.append("</td>");
			}
		}
	}

	/**
	 * 
	 * Form模版的includeField组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的打印html文本.
	 * 
	 * @see cn.myapps.core.dynaform.form.ejb.IncludeField#toHtmlTxt(ParamsTable,
	 *      WebUser, AbstractRunner, int)
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * @param runner
	 *            AbstractRunner(执行脚本的接口类)
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @param doc
	 *            Document
	 * @return Form模版的includeField组件内容结合Document中的ITEM存放的值为重定义后的打印html
	 * @throws Exception
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		view = (View) getValueObject(runner);
		
		if (doc != null) {
			int displayType = field.getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.field.getPrintHiddenValue();
			}

			if (view != null) {
				html.append("<link rel=\"stylesheet\"");
				html.append(" href=\"" + field.getContextPath(doc) + "/resource/css/style.jsp?styleid=" + getStyleId(view)
						+ "\"/>");
				html.append(toIntegrateHtml(doc, runner, webUser, Integer.MAX_VALUE));
			}
		}
		return html.toString();
	}
	
	

	public String getStyleId(View view) {
		if (view.getStyle() != null) {
			return view.getStyle().getId();
		}
		return "";
	}

	public boolean isEmpty(Document doc, IRunner runner) throws Exception {
		DocumentProcess dp = new DocumentProcessBean(doc.getApplicationid());
		String filterScript = view.getFilterScript();

		StringBuffer label = new StringBuffer();
		label.append("View(").append(view.getId()).append(")." + view.getName()).append(".filterScript");

		Object result = runner.run(label.toString(), filterScript);
		if (result != null && result instanceof String) {
			String dql = (String) result;
			String parentid = (String) field.getDocParameter(doc, "_docid");

			if (parentid != null && parentid.trim().length() > 0 && field.isRelate()) {
				dql = "(" + dql + ") and ($parent.$id='" + parentid + "')";
			}

			DataPackage<?> datas = dp.queryByDQL(dql, doc.getDomainid());

			return datas.datas.isEmpty();
		}

		return false;
	}

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public IDesignTimeProcess getProcess() throws Exception {
		return ProcessFactory.createProcess(ViewProcess.class);
	}

	public String toXMLTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		String html = "";
		try {

			view = (View) getValueObject(runner);
			html = toFrameXml(doc, runner, webUser, "XML");
			if (field.isEnabled()) {
				if (isEmpty(doc, runner)) {
					html = "";
				}
			}
		} catch (Exception e) {
			Debug.println(e.getMessage());
			e.printStackTrace();
		}

		return html;
	}
	
	public String toXMLTxt2(Document doc, IRunner runner, WebUser webUser) throws Exception {
		String html = "";
		try {
			view = (View) getValueObject(runner);
			html = toFrameXml(doc, runner, webUser, "XML2");
			if (field.isEnabled()) {
				if (isEmpty(doc, runner)) {
					html = "";
				}
			}
		} catch (Exception e) {
			Debug.println(e.getMessage());
			e.printStackTrace();
		}

		return html;
	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
		view = (View) getValueObject(runner);
		int pagelines = Integer.parseInt(this.view.getPagelines());

		if (view != null) {
			html.append("<link rel=\"stylesheet\"");
			html.append(" href=\"" + field.getContextPath(doc) + "/resource/css/style.jsp?styleid=" + getStyleId(view)
					+ "\"/>");
			html.append(toIntegrateHtml(doc, runner, webUser, pagelines));
		}

		return html.toString();
	}
}
