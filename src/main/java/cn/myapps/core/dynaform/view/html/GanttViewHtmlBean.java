package cn.myapps.core.dynaform.view.html;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewType;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ObjectUtil;
import cn.myapps.util.StringUtil;

public class GanttViewHtmlBean extends ViewHtmlBean {
	private static final Logger LOG = Logger.getLogger(ViewHtmlBean.class);

	/**
	 * 输出甘特视图数据
	 * @return
	 */
	public String toHtml() {
		StringBuffer html = new StringBuffer();
		try {
			View _view = new View();
			ObjectUtil.copyProperties(_view, view);
			_view.setPagelines(String.valueOf(Integer.MAX_VALUE));//甘特视图不分页
			ViewType viewType = _view.getViewTypeImpl();
			
			Map<String, Column> columnMapping = viewType.getColumnMapping();

			if(params.getParameter("_sortCol") != null){
				params.setParameter("_sortCol", "");
			}
			
			DataPackage<Document> datas = viewType.getViewDatas(params, webUser, getSearchDocument(params, webUser));

			html.append("<table moduleType=\"ganttView\" style=\"display:none;\"");
			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
			// 循环添加Task
			if (datas.rowCount > 0) {
				html.append(">");
				for (Iterator<Document> iterator = datas.datas.iterator(); iterator.hasNext();) {
					Document doc = iterator.next();

					// 必须字段
					Column column1 = columnMapping.get("name");
					Column column2 = columnMapping.get("start");
					Column column3 = columnMapping.get("end");
					Column column6 = columnMapping.get("complete");
					Column column7 = columnMapping.get("group");
//					Column column8 = columnMapping.get("milestone");
//					Column column9 = columnMapping.get("resource");
					Column column10 = columnMapping.get("parent");
//					Column column11 = columnMapping.get("open");
					Column column12 = columnMapping.get("dependency");

					// 非必须字段
					Column column4 = columnMapping.get("color");
//					Column column5 = columnMapping.get("link");
					Column column13 = columnMapping.get("caption");

					String id = doc.getId(); // 任务ID
					String name = column1 != null ? column1.getTextString(doc, runner, webUser) : ""; // 名称
					String start = column2 != null ? column2.getTextString(doc, runner, webUser) : ""; // 开始日期
					String end = column3 != null ? column3.getTextString(doc, runner, webUser) : ""; // 结束日期
					String color = column4 != null ? column4.getTextString(doc, runner, webUser) : ""; // 颜色
//					String link = column5 != null ? column5.getTextString(doc, runner, webUser) : ""; // 链接
					String complete = column6 != null ? column6.getTextString(doc, runner, webUser) : "0"; // 完成度
					String group = column7 != null ? column7.getTextString(doc, runner, webUser) : "0"; // 分组
//					String mileStone = column8 != null ? column8.getTextString(doc, runner, webUser) : "0"; // 里程碑
//					String resource = column9 != null ? column9.getTextString(doc, runner, webUser) : ""; // 资源
					String parent = column10 != null ? column10.getTextString(doc, runner, webUser) : ""; // 上级任务ID
//					String open = column11 != null ? column11.getTextString(doc, runner, webUser) : "0"; // 是否展开
					String dependency = column12 != null ? column12.getTextString(doc, runner, webUser) : ""; // 依赖任务
					String caption = column13 != null ? column13.getTextString(doc, runner, webUser) : ""; // 标题

					String isGroup = StringUtil.isBlank(group) ? "0" : group; // 是否分组
//					String isMileStone = StringUtil.isBlank(mileStone) ? "0" : mileStone; // 是否里程碑
					
					/*DateFormat format = new SimpleDateFormat("mm/dd/yyyy HH:mm");
					start = format.format(format.parse(start));
					end = format.format(format.parse(end));*/

					// 'id', 'name', 'start', 'end', 'color', 'link',
					// 'complete','group', 'milestone', 'resource', 'parent',
					// 'open', 'dependency', 'caption', 'formid'

					// 设置隐藏的列
					html.append("<tr");

					html.append(" startTimeIsHidden=\"" + isHiddenColumn(column2) + "\"");
					html.append(" endTimeIsHidden=\"" + isHiddenColumn(column3) + "\"");
					html.append(" completeIsHidden=\"" + isHiddenColumn(column6) + "\"");
					//html.append(" resourceIsHidden=\"" + isHiddenColumn(column9) + "\"");
					html.append(" id=\"" + id + "\"");
					html.append(" name=\"" + name + "\"");
					html.append(" start=\"" + start + "\"");
					html.append(" end=\"" + end + "\"");
					html.append(" color=\"" + color + "\"");
//					html.append(" link=\"" + link + "\"");
//					html.append(" isMileStone=\"" + isMileStone + "\"");
//					html.append(" resource=\"" + resource + "\"");
					html.append(" complete=\"" + complete + "\"");
					html.append(" isGroup=\"" + isGroup + "\"");
					html.append(" parentid=\"" + parent + "\"");
//					html.append(" isOpen=\"" + open + "\"");
					html.append(" dependency=\"" + dependency + "\"");
					html.append(" caption=\"" + caption + "\"");
					html.append(" formId=\"" + doc.getFormid() + "\">");
					
					html.append("<td>id : " + id + "</td>");
					html.append("<td>name : " + name + "</td>");
					html.append("<td>start : " + start + "</td>");
					html.append("<td>end : " + end + "</td>");
					html.append("<td>color : " + color + "</td>");
//					html.append("<td>link : " + link + "</td>");
//					html.append("<td>isMileStone : " + isMileStone + "</td>");
//					html.append("<td>resource : " + resource + "</td>");
					html.append("<td>complete : " + complete + "</td>");
					html.append("<td>isGroup : " + isGroup + "</td>");
					html.append("<td>parent : " + parent + "</td>");
//					html.append("<td>isOpen : " + open + "</td>");
					html.append("<td>dependency : " + dependency + "</td>");
					html.append("<td>caption : " + caption + "</td>");
					html.append("<td>formid : " + doc.getFormid() + "</td>");
					html.append("</tr>");
					
				}
			} else {
				// 空任务
				html.append(" isEmpty=\"true\"><tr><td></td><td></td><td></td><td></td><td></td>" +
						"<td></td><td></td><td></td><td></td><td></td>" +
						"<td></td><td></td><td></td><td></td><td></td></tr>");
			}
			html.append("</table>");
		} catch (Exception e) {
			LOG.error("toHtml", e);
		}

		return html.toString();
	}

	protected Document getSearchDocument(ParamsTable params, WebUser user) {
		if (view.getSearchForm() != null) {
			try {
				return view.getSearchForm().createDocument(params, user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new Document();
	}

}
