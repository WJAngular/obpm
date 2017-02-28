package cn.myapps.util.report;

import java.util.ArrayList;
import java.util.Iterator;

import jofc2.model.Chart;
import jofc2.model.elements.Element;
import jofc2.model.elements.PieChart;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jfree.chart.JFreeChart;

import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.analyzer.Colors;
import cn.myapps.util.StringUtil;

/**
 * 馅饼图生成器
 * 
 * @author Happy
 * 
 */
public class PieChartBuilder extends AbstractChartBuilder implements
		ChartBuilder {

	public String bulidOpenFlashChart(CrossReportVO reportVO, WebUser user)
			throws Exception {

		String reportJson = reportVO.getJson();
		String title = "";
		if (!StringUtil.isBlank(reportJson)) {
			JSONObject cfg = JSONObject.fromObject(reportJson);
			title = cfg.getString("viewLabel");
		}
		
		String dataSet = getDataSet(reportVO, user);
		if (dataSet != null) {
			Chart chart = new Chart(title);
			chart.setBackgroundColour(backgroundColour);
			ArrayList<String> colors = new ArrayList<String>();
			Colors color = new Colors();
			int index = 0;
			PieChart pie = new PieChart();

			JSONArray datas = JSONArray.fromObject(dataSet);

			if (datas != null && datas.size() > 0) {
				for (Iterator<JSONObject> iterator = datas.iterator(); iterator
						.hasNext();) {
					JSONObject d = iterator.next();
					pie.addSlice(d.getDouble("yAxis0"), d.getString("xAxis"));
					colors.add(color.getColor(index++));
				}
			} else {
				pie.addSlice(Integer.valueOf(100), "");
				colors.add("#ff0000");
			}
			pie.setColours(colors);// 饼图每块的颜色
			pie.setBorder(Integer.valueOf(1));
			chart.addElements(new Element[] { pie });

			return chart.toString();
		}
		return "";
	}

	public JFreeChart bulidJFreeChart(CrossReportVO reportVO, WebUser user)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String bulidEChart(CrossReportVO reportVO, WebUser user)
			throws Exception {
		
		String dataSet = getDataSet(reportVO, user);
		if (dataSet != null) {

			JSONArray datas = JSONArray.fromObject(dataSet);
			JSONArray targetDatas = new JSONArray();
			JSONObject echart = new JSONObject();

				for (Iterator<JSONObject> iterator = datas.iterator(); iterator
						.hasNext();) {
					JSONObject d = iterator.next();
					JSONObject item = new JSONObject();
					item.put("value",d.get("yAxis0"));
					item.put("name", d.get("xAxis"));
					targetDatas.add(item);
				}
				echart.put("data",targetDatas);
				echart.put("sign","pie");
			return echart.toString();
		}
		return "";
	}

}
