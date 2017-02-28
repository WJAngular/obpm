package cn.myapps.core.dynaform.view.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public class MapViewHtmlBean extends ViewHtmlBean {
	private static final Logger LOG = Logger.getLogger(MapViewHtmlBean.class);

	public String getMapColumnName() {
		if (view.getViewTypeImpl().getColumnMapping() != null) {
			if((Column) view.getViewTypeImpl().getColumnMapping().get("mapcolumn")!=null){
				return ((Column) view.getViewTypeImpl().getColumnMapping().get("mapcolumn")).getName();
			}
		}

		return "";
	}
	
	public String getMapTitleColumnName() {
		if (view.getViewTypeImpl().getColumnMapping() != null) {
			if((Column) view.getViewTypeImpl().getColumnMapping().get("titlecolumn")!=null){
				return ((Column) view.getViewTypeImpl().getColumnMapping().get("titlecolumn")).getName();
			}
		}
		return "";
	}
	
	public String getMapAddressColumnName() {
		if (view.getViewTypeImpl().getColumnMapping() != null) {
			if((Column) view.getViewTypeImpl().getColumnMapping().get("addresscolumn")!=null){
				return ((Column) view.getViewTypeImpl().getColumnMapping().get("addresscolumn")).getName();
			}
		}
		return "";
	}
	
	public String getMapDetailColumnName() {
		if (view.getViewTypeImpl().getColumnMapping() != null) {
			return ((Column) view.getViewTypeImpl().getColumnMapping().get("detailcolumn")).getName();
		}
		return "";
	}

	public String toLocationString(DataPackage<Document> dataPackage) {
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		try {
			IRunner runner = getRunner();
			if (dataPackage.rowCount > 0) {
				sb.append("{");
				sb.append("\"defaultLevel\":\""+view.getLevel()+"\",");
				sb.append("\"defaultLine\":\""+view.getIsroute()+"\",");
				sb.append("\"defaultAddress\":\""+view.getCountry());
				sb.append(view.getProvince());
				sb.append(view.getCity());
				sb.append(view.getTown());
				sb.append("\",\"deleteReadOnly\":"+(view.getReadonly() || mapReadonly)+",\"editReadOnly\":"+view.getReadonly()+",\"mapInfo\":[");
				for (Iterator<Document> iterator = dataPackage.datas.iterator(); iterator.hasNext();) {
					Document doc = (Document) iterator.next();
					runner.initBSFManager(doc, params, webUser, new ArrayList<ValidateMessage>());
					
					Column column = view.findColumnByName(getMapColumnName());
					if (column != null) {
						String text = column.getText(doc, runner, webUser);
						if (!StringUtil.isBlank(text) && !text.equals("&nbsp")) {
							sb.append(text);
							sb.append(",");
							flag =true;
						}

					}else{
						Column titleColumn = view.findColumnByName(getMapTitleColumnName());
						Column addressColumn=view.findColumnByName(getMapAddressColumnName());
						Column detailColumn=view.findColumnByName(getMapDetailColumnName());
						Column feildColumn=titleColumn == null ? (addressColumn==null?detailColumn : addressColumn) : titleColumn;
						
						sb.append("{\"fieldid\":\""+ doc.getId() + "_" + feildColumn.getFormField().getName() +"\",");
						if (titleColumn != null) {
							String text = titleColumn.getText(doc, runner, webUser);
							if (text != null) {
								sb.append("\"title\":\""+text+"\",");
							}else{
								sb.append("\"title\":\"\",");
							}
						}else{
							sb.append("\"title\":\"&nbsp\",");
						}
						
						if(addressColumn!=null){
							String text = addressColumn.getText(doc, runner, webUser);
							if (text != null) {
								sb.append("\"address\":\""+text+"\",");
							}else{
								sb.append("\"address\":\"\",");
							}
						}else{
							sb.append("\"address\":\"&nbsp\",");
						}
						
						if(detailColumn!=null){
							String text = detailColumn.getText(doc, runner, webUser);
							if (text != null) {
								sb.append("\"detail\":\""+text+"\"}");
							}else{
								sb.append("\"detail\":\"\",");
							}
						}else{
							sb.append("\"detail\":\"&nbsp\"}");
						}
						sb.append(",");
						flag =true;
					}
				}

				if (sb.indexOf(",") != -1 && flag) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("]}");
			}
			else{
				sb.append("{");
				sb.append("\"defaultLevel\":\""+view.getLevel()+"\",");
				sb.append("\"defaultLine\":\""+view.getIsroute()+"\",");
				sb.append("\"defaultAddress\":\""+view.getCountry());
				sb.append(view.getProvince());
				sb.append(view.getCity());
				sb.append(view.getTown());
				sb.append("\",\"deleteReadOnly\":"+(view.getReadonly() || mapReadonly)+",\"editReadOnly\":"+view.getReadonly()+"}");
			}
		} catch (Exception e) {
			LOG.warn("toLocationString", e);
		}

		return sb.toString();
	}

	public String toBaiduMapUrl(DataPackage<Document> dataPackage) {
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer.append(request.getContextPath() + "/portal/share/component/map/view/baiduMap.jsp");
		urlBuffer.append("?formid=" + view.getRelatedForm());
		urlBuffer.append("&_viewid=" + view.getId());
		return urlBuffer.toString();
	}
	

	//输出地图视图
	public String toHTMLText4Map(){
		StringBuffer html = new StringBuffer();
	
		html.append("<table class=\"viewTable\" moduleType=\"mapView\" style=\"display:none;\"");
		html.append(" isSum=\"" + view.isSum() + "\"");
		html.append(" >");
		//table-header
		html.append("<tr>");
		html.append("<td ");
		html.append(" columnName = " + this.getMapColumnName() + "");
		html.append(" >");
		html.append("<input type=\"checkbox\" onclick=\"selectAll(this.checked)\">");
		html.append(this.getMapColumnName());
		html.append("</td>");
		html.append("</tr>");
		WebUser user = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		DataPackage<Document> datas = (DataPackage<Document>) request.getAttribute("datas");
		Collection<Document> docs = datas.datas;
		//显示地图
		html.append("<tr>");
		html.append("<td ");
		html.append(" formid=" + view.getRelatedForm() + "");
		html.append(" _viewid =" + view.getId()+ " ");
		html.append(" toLocationString='" + toLocationString(datas) + "'");
		html.append(" >");
		html.append("<div id=\"baiduMapData\" style=\"display:none\">");
		html.append(toLocationString(datas));
		html.append("</div>");
		html.append("<div style=\"display:none\">");
		// data iterator
		for(Iterator<Document> it = docs.iterator(); it.hasNext();){
			Document doc = it.next();
			html.append("<input type=\"checkbox\" name=\"_selects\" ");
			html.append(" id='"+ doc.getId() + "'");
			html.append(" value='" + doc.getId()+ "'");
			html.append(" >");
		}
		html.append("</div>");
		html.append("</td>");
		html.append("</tr>");
		
		//字段值汇总
		if(view.isSum()){
			ParamsTable params = ParamsTable.convertHTTP(request);
			html.append("<tr><td>&nbsp;</td>");//首列
			for (Iterator<Column> iterator = this.view.getColumns().iterator(); iterator.hasNext();) {
				Column column =  iterator.next();
				if(column.isVisible() && !column.isHiddenColumn(runner)){
					html.append("<td ");
					html.append(" isVisible=\"" + column.isVisible() + "\"");
					html.append(" isHiddenColumn=\"" + column.isHiddenColumn(runner) + "\"");
					html.append(" isSum=\"" + column.isSum() + "\"");
					html.append(" isTotal=\"" + column.isTotal() + "\"");
					html.append(" colName=\"" + column.getName() + "\"");
					html.append(" sumByDatas=\"" + column.getSumByDatas(datas, runner, webUser) + "\"");
					html.append(" sumTotal=\"" + column.getSumTotal(webUser, webUser.getDomainid(), params) + "\"");
					html.append(" >");
					if(column.isSum() || column.isTotal()){
						html.append(column.getName() + ":");
					}
					if(column.isSum()){
						html.append(column.getSumByDatas(datas, runner, user));
					}
					if(column.isTotal()){
						html.append(column.getSumTotal(user, user.getDomainid(), params));
					}
					html.append("</td>");
				}
			}
			html.append("</tr>");
		}
		html.append("</table>");
		
		return html.toString();
	}
}
