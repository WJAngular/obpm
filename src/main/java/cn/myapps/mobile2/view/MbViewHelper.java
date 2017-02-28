package cn.myapps.mobile2.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.ImageUploadField;
import cn.myapps.core.dynaform.form.ejb.OnLineTakePhotoField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewType;
import cn.myapps.core.dynaform.view.html.ViewHtmlBean;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class MbViewHelper {

	private static String ATT_TH = "TH";
	private static int rootCount = 0;
	
	@Deprecated
	public static ArrayList<Activity> doViewActivity(View view, HttpServletRequest request, WebUser user)
			throws Exception {
		ArrayList<Activity> activityList = new ArrayList<Activity>();
		try {
			HttpSession session = request.getSession();
			IRunner runner = JavaScriptFactory.getInstance(session.getId(), view.getApplicationid());
			Collection<Activity> activities = view.getActivitys();
			Iterator<Activity> aiter = activities.iterator();
			while (aiter.hasNext()) {
				Activity act = (Activity) aiter.next();
				boolean isHidden = false;
				String hiddenScript = act.getHiddenScript();
				if (!StringUtil.isBlank(hiddenScript)) {

					String script = StringUtil.dencodeHTML(hiddenScript);

					StringBuffer label = new StringBuffer();
					label.append("View").append("." + view.getName());
					label.append(".Activity(").append(act.getId());
					label.append(act.getName() + ")").append(".runHiddenScript");
					Object javaResult = runner.run(label.toString(), script);// Run
					if (javaResult instanceof Boolean) {
						isHidden = ((Boolean) javaResult).booleanValue();
						if (isHidden)
							continue;
					}
				}
				activityList.add(act);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return activityList;
	}
	
	public static DataPackage<Document> doData(View view, Document document, ParamsTable params, WebUser user)
			throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			int page = 1;
			String _currpage = params.getParameterAsString("_currpage");
			String _pagelines = params.getParameterAsString("_pagelines");
			if (!StringUtil.isBlank(_currpage)) {
				try {
					page = Integer.parseInt(_currpage);
				} catch (Exception e) {
				}
			}
			int lines = 0;
			if (view.isPagination()) {
				lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 99;
			} else {
				lines = Integer.MAX_VALUE;
			}

			ViewType viewType = view.getViewTypeImpl();
			DataPackage<Document> datas = viewType.getViewDatasPage(params, page, lines, user, document);
			return datas;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PersistenceUtils.closeSessionAndConnection();
		}
		return null;
	}
	
	@Deprecated
	public static Map<String, Collection<String>> doDataXML(View view, Document document, HttpServletRequest request,
			ParamsTable params, WebUser user, DataPackage<Document> datas) {
		try {
			Map<String, Collection<String>> trs = new LinkedHashMap<String, Collection<String>>();
			Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
			HttpSession session = request.getSession();
			IRunner jsrun = JavaScriptFactory.getInstance(session.getId(), view.getApplicationid());
			IRunner runner = JavaScriptFactory.getInstance(session.getId(), view.getApplicationid());

			Collection<Column> columns = view.getColumns();
			Collection<Document> col = datas.getDatas();
			if (col != null) {
				Iterator<Document> its = col.iterator();
				Iterator<Column> itths = columns.iterator();
				ArrayList<Column> columnList = new ArrayList<Column>();
				Collection<String> ths = new ArrayList<String>();
				Collection<String> images = new ArrayList<String>();

				// 循环th
				while (itths.hasNext()) {
					Column column = (Column) itths.next();
					if (column.getType().equals("COLUMN_TYPE_OPERATE")) {
						continue;
					}
					columnList.add(column);
				}
				Iterator<Column> it = columnList.iterator();
				// 循环th
				while (it.hasNext()) {
					Column column = (Column) it.next();
					if (columnHidden(column, runner)) {
						continue;
					}
					ths.add(column.getName());
				}
				trs.put(ATT_TH, ths);
				int i = 0;
				while (its.hasNext()) {
					i++;
					Document doc = (Document) its.next();

					// 先使用map保存列的值,可供操作列的跳转按钮使用
					jsrun.initBSFManager(doc, params, user, errors);

					if (doc == null)
						continue;
					Iterator<Column> iter = columnList.iterator();
					Collection<String> tds = new ArrayList<String>();
					try {
						boolean first = true;
						while (iter.hasNext()) {
							StringBuffer xml = new StringBuffer();
							Column column = (Column) iter.next();
							if (column.getFormField() != null && column.getShowType() != null
									&& column.getShowType().equals(Column.SHOW_TYPE_TEXT)) {
								if (column.getFormField() instanceof OnLineTakePhotoField
										|| column.getFormField() instanceof ImageUploadField) {
									String result = column.getFormField().getValue(doc, runner, user);
									xml.append("<").append("TD");
									xml.append(" ").append("COLUMNID").append("='").append(column.getId()).append("'");
									xml.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='")
											.append(column.isVisible()).append("'");
									xml.append(" ").append("TYPE").append("='").append("IMAGE").append("'").append(">");
									xml.append(replacSpecial(HtmlEncoder.encode((String) result)));
									xml.append("</").append("TD").append(">");
									tds.add(xml.toString());
									continue;
								}
							}
							boolean isHidden = columnHidden(column, runner);
							String result = column.getText(doc, runner, user);

							if ("$StateLabel".equals(column.getFieldName())
									|| "$PrevAuditNode".equals(column.getFieldName())) {
								result = parserStateLabel(column, result);
							}

							xml.append(toTdXML(first, replaceHTML((String) result), column.getId(), isHidden));
							// 默认第一列为主题 当主题为隐藏时 则由下一列为主题
							if (first && !isHidden) {
								first = false;
							}
							tds.add(xml.toString());
						}
						if(doc.getId() == null){
							trs.put(String.valueOf(i), tds);
						}else{
							trs.put(doc.getId(), tds);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return trs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Deprecated
	protected static String parserStateLabel(Column column, String result) {
		try {
			String value = "";
			if ("$StateLabel".equals(column.getFieldName()) && (result.startsWith("[") || result.startsWith("<img"))) {
				StringBuffer stateLabelSB = new StringBuffer();
				JSONArray instances = null;
				if (result.startsWith("[")) {
					instances = new JSONArray(result);
				} else if (result.startsWith("<img")) {
					int jsonStartIndex = result.indexOf("[{");
					int jsonEndIndex = result.lastIndexOf("}]");
					String imgHtml = result.substring(0, result.indexOf("<font"));
					String fontStart = result.substring(result.indexOf("<font"), jsonStartIndex);
					String fontEnd = result.substring(jsonEndIndex + 2, result.length());
					instances = new JSONArray(result.substring(jsonStartIndex, jsonEndIndex + 2));
				}
				for (int i = 0; i < instances.length(); i++) {
					JSONObject instance = instances.getJSONObject(i);
					JSONArray nodes = instance.getJSONArray("nodes");
					for (int j = 0; j < nodes.length(); j++) {
						JSONObject node = nodes.getJSONObject(j);
						String stateLabel = node.getString("stateLabel");
						stateLabelSB.append(stateLabel).append(";");
					}
				}
				value = stateLabelSB.toString();
			} else if ("$PrevAuditNode" == column.getFieldName() && result.indexOf("[") == 0) {
				StringBuffer auditNodeSB = new StringBuffer();
				JSONArray instances = new JSONArray(result);
				for (int i = 0; i < instances.length(); i++) {
					JSONObject instance = instances.getJSONObject(i);
					String prevAuditNode = instance.getString("prevAuditNode");
					auditNodeSB.append(prevAuditNode).append(";");
				}
				value = auditNodeSB.toString();
			}
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 处理修改的数据
	 * 
	 * @param view
	 * @param user
	 * @param params
	 * @return
	 */
	@Deprecated
	public static String doViewChangeData(View view, WebUser user, ParamsTable params) throws Exception {
		StringBuffer sb = new StringBuffer();
		String docid = params.getParameterAsString("_docid");
		DocumentProcess documentProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
				view.getApplicationid());
		Document doc = (Document) documentProcess.doView(docid);
		if (doc != null && doc.getForm() != null) {
			if (view.getRelatedForm().equals(doc.getForm().getId())) {
				sb.append("<").append("VIEWCHANGEDATA").append(">");

				HttpServletRequest request = ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				IRunner runner = JavaScriptFactory.getInstance(session.getId(), view.getApplicationid());

				Iterator<Column> iter = view.getColumns().iterator();
				try {
					boolean first = true;
					sb.append("<").append(MobileConstant2.ATT_TR).append(" ").append(MobileConstant2.ATT_DOCID)
							.append("='").append(doc.getId()).append("'>");
					while (iter.hasNext()) {
						StringBuffer xml = new StringBuffer();
						Column column = (Column) iter.next();
						if (column.getFormField() != null && column.getShowType() != null
								&& column.getShowType().equals(Column.SHOW_TYPE_TEXT)) {
							if (column.getFormField() instanceof OnLineTakePhotoField
									|| column.getFormField() instanceof ImageUploadField) {
								String result = column.getFormField().getValue(doc, runner, user);

								xml.append("<").append("TD");
								xml.append(" ").append("COLUMNID").append("='").append(column.getId()).append("'");
								xml.append(" ").append("TYPE").append("='").append("IMAGE").append("'").append(">");
								xml.append(replacSpecial(HtmlEncoder.encode((String) result)));
								xml.append("</").append("TD").append(">");
								continue;
							}
						}

						String result = column.getText(doc, runner, user);

						if ("$StateLabel".equals(column.getFieldName())
								|| "$PrevAuditNode".equals(column.getFieldName())) {
							result = parserStateLabel(column, result);
						}

						boolean isHidden = columnHidden(column, runner);
						xml.append(toTdXML(first, replaceHTML((String) result), column.getId(), isHidden));
						if (first && !isHidden) {
							first = false;
						}
						sb.append(xml);
					}
					sb.append("</").append(MobileConstant2.ATT_TR).append(">");
				} catch (Exception e) {
					e.printStackTrace();
				}
				sb.append("</").append("VIEWCHANGEDATA").append(">");
			}
		}
		return sb.toString();
	}

	/**
	 * 拼接视图列数据xml
	 * 
	 * @param first
	 * @param result
	 * @param columnId
	 * @param docid
	 * @return
	 */
	@Deprecated
	public static String toTdXML(boolean first, String result, String id, boolean isHidden) {
		StringBuffer xml = new StringBuffer();
		if (first && !isHidden) {
			xml.append("<").append("SUMMARY");
			xml.append(" ").append("COLUMNID").append("='").append(id).append("'");
			xml.append(">");
			xml.append(replacSpecial(replacSpecial(replaceHTML((String) result))));
			xml.append("</").append("SUMMARY").append(">");
		} else {
			xml.append("<").append("TD");
			xml.append(" ").append("COLUMNID").append("='").append(id).append("'");
			xml.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='").append(isHidden).append("'");
			xml.append(">");
			xml.append(replacSpecial(replacSpecial(replaceHTML((String) result))));
			xml.append("</").append("TD").append(">");
		}
		return xml.toString();
	}
	
	@Deprecated
	public static int getRootCount() {
		return rootCount;
	}
	
	@Deprecated
	public static String replaceHTML(String htmlString) {
		String noHTMLString = htmlString.replaceAll("</?[^>]+>", "");
		noHTMLString = noHTMLString.replaceAll("\\&nbsp;", " ");
		return noHTMLString;
	}

	/**
	 * 检查字符串是否包含特殊符号：<br>
	 * 
	 * @param value
	 * @return
	 */
	@Deprecated
	public static String replacSpecial(String value) {
		if (value != null) {
			value = value.replace("&", "&amp;");
			value = value.replace(">", "&gt;");
			value = value.replace("<", "&lt;");
			value = value.replace("'", "&apos;");
			value = value.replace("\"", "&quot;");
		}
		return value;
	}

	public static boolean columnHidden(Column column, IRunner runner) {
		boolean isHidden = column.isHiddenColumn(runner);
		if (!column.isVisible()) {
			return true;
		}
		if (isHidden) {
			return true;
		}
		return false;
	}
}
