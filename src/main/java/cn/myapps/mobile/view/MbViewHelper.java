package cn.myapps.mobile.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.resource.ejb.ResourceType;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.StringUtil;

public class MbViewHelper {

	private static final Logger log = Logger.getLogger(MbViewHelper.class);
	
	private String application;
	
	public MbViewHelper(String application) {
		this.application = application;
	}
	
	public String toViewListMobileXml(boolean isDialogView, View view, Document parent, DataPackage<Document> datas, WebUser user, HttpServletRequest request, ParamsTable params) throws Exception {
		HttpSession session = request.getSession();
		String _viewid = view.getId();
		
		Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
		String refresh = params.getParameterAsString("refresh");
		boolean isRefresh = refresh != null && refresh.trim().equals("true");
		IRunner jsrun = JavaScriptFactory.getInstance(session.getId(), getApplication());

		boolean isEdit = true;
		String editString = params.getParameterAsString("isedit");
		isEdit = editString == null ? true : Boolean.parseBoolean(editString);
		
		Collection<Column> columns = view.getColumns();
		
		StringBuffer buffer = new StringBuffer();
		
		if (isRefresh) {
			buffer.append(getHiddenFieldXml("refresh", "true"));
		}

		buffer.append(getHiddenFieldXml("_viewid", _viewid));
		buffer.append(getHiddenFieldXml("application", getApplication()));
		
		if (isDialogView) {
			String mapStr = params.getParameterAsString("_mapStr");
			buffer.append(getHiddenFieldXml("_mapStr", mapStr));
		}
		if (parent != null && parent.getId() != null) {
			buffer.append(getHiddenFieldXml("parentid", parent.getId()));
		}else{
			buffer.append(getHiddenFieldXml("parentid", params.getParameterAsString("parentid")));
		}
		String isRelate = params.getParameterAsString("isRelate");
		if (isRelate != null && isRelate.trim().equals("true")) {
			buffer.append(getHiddenFieldXml("isRelate", "true"));
		}
		Document tdoc = parent != null ? parent : new Document();
		
		boolean flag = false;
		IRunner runner = JavaScriptFactory.getInstance(session.getId(), view.getApplicationid());
		
		runner.initBSFManager(tdoc, params, user, new ArrayList<ValidateMessage>());

		Collection<Activity> activities = view.getActivitys();
		if (activities == null) {
			activities = new ArrayList<Activity>();
		}

		Iterator<Activity> aiter = activities.iterator();
		while (aiter.hasNext()) {
			Activity act = (Activity) aiter.next();
			boolean isHidden = false;
			String hiddenScript = act.getHiddenScript();
			if (!StringUtil.isBlank(hiddenScript)) {
				StringBuffer label = new StringBuffer();
				label.append("View").append("." + view.getName());
				label.append(".Activity(").append(act.getId());
				label.append(act.getName() + ")").append(".runHiddenScript");
				Object result = runner.run(label.toString(), act.getHiddenScript());// Run
				if (result instanceof Boolean) {
					isHidden = ((Boolean) result).booleanValue();
				}
			}

			boolean isStateToHidden = false;
			if (parent != null) {
				isStateToHidden = act.isStateToHidden(parent);
			}

			flag = (isHidden || isStateToHidden);
			if (flag) continue;
			
			if (act.getType() == ActivityType.EXPTOEXCEL) continue;
			
			if (!act.isHidden(runner, view, tdoc, user, ResVO.VIEW_TYPE)
					&& isEdit) {
				buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ");
				buffer.append(MobileConstant.ATT_TYPE).append("='");
				buffer.append(act.getType());
				buffer.append("' ").append(MobileConstant.ATT_NAME);
				buffer.append("='{*[" + HtmlEncoder.encode(act.getName()) + "]*}' ");
				buffer.append("").append(MobileConstant.ATT_ID).append("='");
				buffer.append(act.getId());
				buffer.append("'>");
				if(act.getType() == ActivityType.BATCH_APPROVE){//批量提交
					buffer.append("<").append(MobileConstant.TAG_PARAMETER)
					.append(" ").append(MobileConstant.ATT_NAME)
					.append("='_approveLimit'>" + act.getApproveLimit() + "</")
					.append(MobileConstant.TAG_PARAMETER)
					.append(">");
					buffer.append("<").append(MobileConstant.TAG_PARAMETER)
					.append(" ").append(MobileConstant.ATT_NAME)
					.append("='application'>" + act.getApplicationid() + "</")
					.append(MobileConstant.TAG_PARAMETER)
					.append(">");
				}
				buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
			}
		}
		if (isDialogView) {
			buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ");
			buffer.append(MobileConstant.ATT_TYPE).append("='link' ");
			buffer.append(MobileConstant.ATT_NAME).append("='{*[OK]*}' ");
			buffer.append(">");
			buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
		}

		buffer.append("<").append(MobileConstant.TAG_TH).append(">");

		if (view != null) {
			if (view.getSearchForm() != null) {
				buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ");
				buffer.append(MobileConstant.ATT_TYPE).append("='23'");
				buffer.append(" ").append(MobileConstant.ATT_NAME).append("='{*[Search]*}' ");
				buffer.append(MobileConstant.ATT_ID).append("=''>");
				buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
			}
			Collection<Column> col = view.getColumns();
			if (col != null) {
				Iterator<Column> its = col.iterator();
				while (its.hasNext()) {
					Column column = (Column) its.next();
					//列隐藏值
					if (column.getHiddenScript() != null && column.getHiddenScript().trim().length() > 0) {
						StringBuffer label = new StringBuffer();
						label.append("View").append("." + view.getName()).append(
								".Activity(").append(column.getId()).append(
								")." + column.getName()).append(".runHiddenScript");

						Object result = runner.run(label.toString(), column
								.getHiddenScript());// 运行脚本
						if (result != null && result instanceof Boolean) {
							continue;
						}
					}
					if(column.getType().equals("COLUMN_TYPE_OPERATE")){
						continue;
					}
					buffer.append("<").append(MobileConstant.TAG_TD).append(" ");
					buffer.append(MobileConstant.ATT_NAME).append(" = '' ");
					buffer.append(MobileConstant.ATT_WIDTH).append(" = '" + column.getWidth() + "'>");
					buffer.append(replaceHTML(column.getName()));
					buffer.append("</").append(MobileConstant.TAG_TD).append(">");
				}
			}
		}
		buffer.append("</").append(MobileConstant.TAG_TH).append(">");

		if (datas == null) return buffer.toString();
		
		buffer.append(getPageActionXml(view, parent, datas,isDialogView));
		
		Collection<Document> col = datas.getDatas();
		if (col != null) {
			Iterator<Document> its = col.iterator();
			while (its.hasNext()) {
				Document doc = (Document) its.next();
				Iterator<Column> _iter = view.getColumns().iterator();
				
				//先使用map保存列的值,可供操作列的跳转按钮使用
				jsrun.initBSFManager(doc, params, user, errors);
				Map<String, String> resultMap = new HashMap<String, String>();
//				while(_iter.hasNext()){
//					Column _col = _iter.next();
//					String _result = _col.getText(doc, runner, user);
//					resultMap.put(_col.getId(), _result);
//				}
				
				if (doc == null) continue;
				Iterator<Column> iter = columns.iterator();
				try {
					buffer.append("<").append(MobileConstant.TAG_TR).append(" ");
					buffer.append(MobileConstant.ATT_ID).append(" = '" + doc.getId() + "'>");
					ArrayList<Column> columnList = new ArrayList<Column>();
					while (iter.hasNext()) {
						Column column = (Column) iter.next();
						if (column.getHiddenScript() != null && column.getHiddenScript().trim().length() > 0) {
							StringBuffer label = new StringBuffer();
							label.append("View").append("." + view.getName()).append(
									".Activity(").append(column.getId()).append(
									")." + column.getName()).append(".runHiddenScript");

							Object result = runner.run(label.toString(), column
									.getHiddenScript());// 运行脚本
							if (result != null && result instanceof Boolean) {
								String results = column.getText(doc, runner, user);
								resultMap.put(column.getId(), HtmlEncoder.encode((String) results));
								continue;
							}
						}
						if(column.getType().equals("COLUMN_TYPE_OPERATE")){
							columnList.add(column);
						}else{
							buffer.append("<").append(MobileConstant.TAG_TD).append(" ");
							String result = column.getText(doc, runner, user);
							if (column != null && column.getId() != null) {
								buffer.append(" ").append(MobileConstant.ATT_NAME).append(" = '" + HtmlEncoder.encode(column.getId()) + "' >");
							} else {
								buffer.append(" ").append(MobileConstant.ATT_NAME).append(" ='' >");
							}
	
							// DO NOT display null
							if (result == null || result.equals("&nbsp")) result = "";
                            //如果列的值是映射的表单的流程状态
                            if ("$StateLabel".equals(column.getFieldName()) && result.startsWith("[")) {
                                JSONArray instance = JSONArray.fromObject(result);
                                result = "";
                                for (int i = 0; i < instance.size(); i++) {
                                    JSONObject obj = instance.getJSONObject(i);
                                    String nodesStr = obj.getString("nodes");
                                    JSONArray nodes = JSONArray.fromObject(nodesStr);
                                    for (int j = 0; j < nodes.size(); j++) {
                                        JSONObject object = nodes.getJSONObject(j);
                                        result += object.getString("stateLabel") + ";";
                                    }
                                }
                                //如果列的值是映射的上一节点
                            } else if ("$PrevAuditNode".equals(column.getFieldName()) && result.startsWith("[")) {
                                JSONArray instance = JSONArray.fromObject(result);
                                result = "";
                                for (int i = 0; i < instance.size(); i++) {
                                    JSONObject obj = instance.getJSONObject(i);
                                    result += obj.getString("prevAuditNode") + ";";
                                }
                                //如果列的值是映射的上一节点审批人
                            } else if ("$PrevAuditUser".equals(column.getFieldName()) && result.startsWith("[")) {
                                JSONArray instance = JSONArray.fromObject(result);
                                result = "";
                                for (int i = 0; i < instance.size(); i++) {
                                    JSONObject obj = instance.getJSONObject(i);
                                    result += obj.getString("prevAuditUser") + ";";
                                }
                            }

                            //去掉最后的;字符
                            if (result.length() > 0 && result.endsWith(";")) {
                                result = result.substring(0, result.length() - 1);
                            }
                            result = replaceHTML(result);
							buffer.append(HtmlEncoder.encode((String) result));
							resultMap.put(column.getId(), HtmlEncoder.encode((String) result));
							buffer.append("</").append(MobileConstant.TAG_TD).append(">");
						}
						
					}
					if(columnList.size()>0){
						for(int j = 0;j<columnList.size();j++){
							Column column = columnList.get(j);
							if(column.getButtonType().equals(Column.BUTTON_TYPE_JUMP)){
								buffer.append("<").append(MobileConstant.TAG_TD).append(" ");
								String jumpMapping = getJumpMapping(column.getJumpMapping(), resultMap);
								buffer.append(" ").append(MobileConstant.ATT_NAME).append(" = '" + HtmlEncoder.encode(column.getId())).append("' ").append(MobileConstant.ATT_TYPE).append(" = '" + MobileConstant.BUTTON_TYPE_JUMP+ "'");
								buffer.append(" ").append(MobileConstant.ATT_ID).append("= '" + HtmlEncoder.encode(column.getMappingform())).append("' ").append(MobileConstant.ATT_VALUE).append(" = '" + HtmlEncoder.encode(jumpMapping) + "' >" );
								buffer.append(HtmlEncoder.encode(column.getButtonName()));
								buffer.append("</").append(MobileConstant.TAG_TD).append(">");
							}else if(column.getButtonType().equals(Column.BUTTON_TYPE_SCRIPT)){
								buffer.append("<").append(MobileConstant.TAG_TD).append(" ");
								buffer.append(" ").append(MobileConstant.ATT_NAME).append(" = '" + HtmlEncoder.encode(column.getId())).append("' ").append(MobileConstant.ATT_TYPE).append(" = '" + MobileConstant.BUTTON_TYPE_SCRIPT+ "'");
								buffer.append(" ").append(MobileConstant.ATT_ID).append("= '" + HtmlEncoder.encode(doc.getId()) + "' >" );
								buffer.append(HtmlEncoder.encode(column.getButtonName()));
								buffer.append("</").append(MobileConstant.TAG_TD).append(">");
							}else if(column.getButtonType().equals(Column.BUTTON_TYPE_DOFLOW)){
								buffer.append("<").append(MobileConstant.TAG_TD).append(" ");
								buffer.append(" ").append(MobileConstant.ATT_NAME).append(" = '" + HtmlEncoder.encode(column.getId())).append("' ").append(MobileConstant.ATT_TYPE).append(" = '" + MobileConstant.BUTTON_TYPE_DOFLOW+ "'");
								buffer.append(" ").append(MobileConstant.ATT_ID).append("= '" + HtmlEncoder.encode(doc.getId())).append("' ").append(MobileConstant.ATT_VALUE).append(" = '" + HtmlEncoder.encode(column.getApproveLimit()) + "' >" );
								buffer.append(HtmlEncoder.encode(column.getButtonName()));
								buffer.append("</").append(MobileConstant.TAG_TD).append(">");
							}else{
								continue;
							}
						}
					}

					buffer.append("</").append(MobileConstant.TAG_TR).append(">");
				} catch (Exception e) {
					log.warn(e.toString());
					throw e;
				}
			}
		}
		
		return buffer.toString();
	}
	
	public String toViewListXml(boolean isDialogView, DataPackage<Document> datas,Document searchDocument,View view,HttpServletRequest request,ParamsTable params,WebUser user,Document parent,String _currpage,int _pagelines) throws Exception {
		StringBuffer buffer = new StringBuffer();
		String title = view.getDescription();
		if (StringUtil.isBlank(title) || title.trim().equals("null")) title = view.getName();
		title = " ({*[InPage]*}" + datas.pageNo + "{*[page]*}/{*[core.email.tatol.one]*}" + datas.getPageCount() + "{*[page]*})  " + title;
		buffer.append("<").append(MobileConstant.TAG_VIEW).append(" ");
		buffer.append(MobileConstant.ATT_TITLE).append("='" + HtmlEncoder.encode(title) + "' ");
		buffer.append(MobileConstant.ATT_MOBILEVIEWTYPE).append("='" + HtmlEncoder.encode(view.getMobileViewType()) +"' ");
		buffer.append(MobileConstant.ATT_TYPE).append("='" + view.getViewType() + "' ");
		
		String refresh = params.getParameterAsString("refresh");
		boolean isRefresh = refresh != null && refresh.trim().equals("true");
		if (isRefresh) {
			buffer.append(MobileConstant.ATT_REFRESH).append("='true' ");
		}
		if (view.getReadonly().booleanValue()) {
			buffer.append(MobileConstant.ATT_READONLY).append("='true' ");
		}
		buffer.append(">");
		
		buffer.append(getHiddenFieldXml("_currpage", _currpage));
		buffer.append(getHiddenFieldXml("_pagelines", String.valueOf(_pagelines)));
		buffer.append(toViewListMobileXml(isDialogView, view, parent, datas, user, request, params));
		
		compareSearchParams(buffer, view, params);
		buffer.append("</").append(MobileConstant.TAG_VIEW).append(">");
		return buffer.toString();
	}
	
	/**
	 * 比较和添加查询字段值
	 * @param xml
	 * @param view
	 * @param params
	 */
	private void compareSearchParams(StringBuffer xml, View view, ParamsTable params) {
		if (view == null) return;
		Form searchForm = view.getSearchForm();
		if (searchForm == null) return;
		//String xmlString = xml.toString();
		for (Iterator<String> it = params.getParameterNames(); it.hasNext(); ) {
			String key = (String) it.next();
			FormField field = searchForm.findFieldByName(key);
			if (field == null) continue;
			String value = params.getParameterAsString(key);
			if (!StringUtil.isBlank(value) 
					/*&& xmlString.indexOf(key) < 0*/
					&& value.indexOf(">") < 0 
					&& value.indexOf("<") < 0) {
				xml.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ");
				xml.append(MobileConstant.ATT_NAME).append("='" + key + "'>");
				xml.append(value);
				xml.append("</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
			}
		}
	}
	
	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}
	
	public String getHiddenFieldXml(String name, String value) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ");
		buffer.append(MobileConstant.ATT_NAME).append("='").append(name);
		buffer.append("'>").append(value);
		buffer.append("</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
		return buffer.toString();
	}
	
	public String getParameterXml(String name, String value) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ");
		buffer.append(MobileConstant.ATT_NAME).append(" ='").append(name + "'>");
		buffer.append(HtmlEncoder.encode(value));
		buffer.append("</").append(MobileConstant.TAG_PARAMETER).append(">");
		return buffer.toString();
	}
	
	private String getPageActionXml(View view, Document parent, DataPackage<Document> datas,boolean isDialogView) {
		StringBuffer buffer = new StringBuffer();
		int pages = datas.rowCount / datas.linesPerPage;
		
		if (datas.rowCount % datas.linesPerPage > 0) pages++;
		
		if (datas.pageNo < pages || datas.pageNo > 1) {
			buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ");
			buffer.append(MobileConstant.ATT_VALUE).append("='Pagination' ");
			buffer.append(MobileConstant.ATT_NAME).append("='{*[Pagination]*}'>");
		}
		if (datas.pageNo < (pages)) {
			buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ");
			if(isDialogView){
				buffer.append(MobileConstant.ATT_TYPE).append("='"+MobileConstant.CMD_DIALOGVIEW+ "' ");
			}else{
				buffer.append(MobileConstant.ATT_TYPE).append("='" + ResourceType.RESOURCE_TYPE_MOBILE + "' ");
			}
			buffer.append(MobileConstant.ATT_VALUE).append("='NextPage'");
			buffer.append(" ").append(MobileConstant.ATT_NAME).append("='{*[NextPage]*}'>");
			buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ");
			buffer.append(MobileConstant.ATT_NAME).append("='_viewid'>" + HtmlEncoder.encode(view.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
			if (parent != null && !StringUtil.isBlank(parent.getId())) {
				buffer.append(getParameterXml("parentid", parent.getId()));
			}
			buffer.append(getParameterXml("_currpage", String.valueOf(datas.pageNo + 1)));
			buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
			///////////////////////////////////////
			buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ");
			if(isDialogView){
				buffer.append(MobileConstant.ATT_TYPE).append("='"+MobileConstant.CMD_DIALOGVIEW+ "' ");
			}else{
				buffer.append(MobileConstant.ATT_TYPE).append("='" + ResourceType.RESOURCE_TYPE_MOBILE + "' ");
			}
			buffer.append(MobileConstant.ATT_VALUE).append("='EndPage'");
			buffer.append(" ").append(MobileConstant.ATT_NAME).append("='{*[EndPage]*}'>");
			buffer.append(getParameterXml("_viewid", view.getId()));
			if (parent != null && !StringUtil.isBlank(parent.getId())) {
				buffer.append(getParameterXml("parentid", parent.getId()));
			}
			buffer.append(getParameterXml("_currpage", String.valueOf(pages)));
			buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
		}
		if (datas.pageNo > 1) {
			buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ");
			if(isDialogView){
				buffer.append(MobileConstant.ATT_TYPE).append("='"+MobileConstant.CMD_DIALOGVIEW+ "' ");
			}else{
				buffer.append(MobileConstant.ATT_TYPE).append("='" + ResourceType.RESOURCE_TYPE_MOBILE + "' ");
			}
			buffer.append(MobileConstant.ATT_VALUE).append("='FirstPage'");
			buffer.append(" ").append(MobileConstant.ATT_NAME).append("='{*[FirstPage]*}'>");
			buffer.append(getParameterXml("_viewid", view.getId()));
			if (parent != null && !StringUtil.isBlank(parent.getId())) {
				buffer.append(getParameterXml("parentid", parent.getId()));
			}
			buffer.append(getParameterXml("_currpage", String.valueOf(1)));
			buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
			/////////////////////////////////////////////
			buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ");
			if(isDialogView){
				buffer.append(MobileConstant.ATT_TYPE).append("='"+MobileConstant.CMD_DIALOGVIEW+ "' ");
			}else{
				buffer.append(MobileConstant.ATT_TYPE).append("='" + ResourceType.RESOURCE_TYPE_MOBILE + "' ");
			}
			buffer.append(MobileConstant.ATT_VALUE).append("='PrevPage'");
			buffer.append(" ").append(MobileConstant.ATT_NAME).append("='{*[PrevPage]*}'>");
			buffer.append(getParameterXml("_viewid", view.getId()));
			if (parent != null && !StringUtil.isBlank(parent.getId())) {
				buffer.append(getParameterXml("parentid", parent.getId()));
			}
			buffer.append(getParameterXml("_currpage", String.valueOf(datas.pageNo - 1)));
			buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
		}
		if (datas.pageNo < pages || datas.pageNo > 1) {
			buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
		}
		return buffer.toString();
	}
	
	//根据跳转操作列的映射生成  参数名称:列值  映射
	private String getJumpMapping(String jumpMapping, Map<String, String> resultMap){
		String _jumpMapping = "";
		if(!StringUtil.isBlank(jumpMapping)){
			String[] maps = jumpMapping.split(";");
			for(int i=0; i<maps.length; i++){
				String[] map = maps[i].split(":");
				String agr = map[0];
				String col = map[1];
				String colValue = resultMap.get(col);
				if(colValue == "&nbsp") colValue = "";
				_jumpMapping +=  agr + ":" + colValue + ";";
			} 
			if(_jumpMapping.length() > 1){
				_jumpMapping = _jumpMapping.substring(0, _jumpMapping.length()-1);
			}
		}
		
		return _jumpMapping;
	}
	
	//使用正则表达式替换html元素
	public static String replaceHTML(String htmlString){
        String noHTMLString = htmlString.replaceAll("</?[^>]+>", "");
        noHTMLString = noHTMLString.replaceAll("\\&nbsp;", " ");
        noHTMLString = noHTMLString.replaceAll("\\&nbsp", " ");
        return noHTMLString;
    }
	
}
