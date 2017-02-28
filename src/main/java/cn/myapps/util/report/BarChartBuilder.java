package cn.myapps.util.report;

import java.util.Iterator;
import java.util.Stack;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jfree.chart.JFreeChart;

import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

/**
 * 条形图生成器
 * @author Happy
 *
 */
public class BarChartBuilder extends AbstractChartBuilder implements ChartBuilder {

	public String bulidOpenFlashChart(CrossReportVO reportVO, WebUser user)
			throws Exception {
		
		
		String reportJson = reportVO.getJson();
		String title = "";
		String chartType = "hbar";//条形图
		if (!StringUtil.isBlank(reportJson)) {
			JSONObject cfg = JSONObject.fromObject(reportJson);
			title = cfg.getString("viewLabel");
		}
		
		String dataSet = getDataSet(reportVO, user);
		if (dataSet != null) {
			JSONArray datas = JSONArray.fromObject(dataSet);
			if (datas != null && datas.size() > 0) {
				double xMax = 1.0;//x轴最大值
				JSONObject chart = new JSONObject();
				JSONObject chartTitle = new JSONObject();
				JSONArray elements = new JSONArray();
				JSONObject element = new JSONObject();
				element.put("type", chartType);
				JSONArray values = new JSONArray();
				
				JSONObject yAxis = new JSONObject();
				JSONArray yAxis_labels = new JSONArray();
				Stack<String> tempYAxisLabels = new Stack<String>();
				
				JSONObject xAxis = new JSONObject();
				
				for (Iterator<JSONObject> iterator = datas.iterator(); iterator.hasNext();) {
					JSONObject d = iterator.next();
					String label = d.getString("xAxis");
					double value = d.getDouble("yAxis0");
					if(value>xMax) xMax = value;
					
					JSONObject item = new JSONObject();
					item.put("left", 0);
					item.put("right", value);
					values.add(item);
					
					tempYAxisLabels.push(label);
				}
				
				while(!tempYAxisLabels.isEmpty()){
					yAxis_labels.add(tempYAxisLabels.pop());
				}
				
				xAxis.put("max", xMax);
				xAxis.put("steps",Math.ceil(xMax/10));
				//xAxis.put("offset", 1);
				
				
				element.put("values", values);
				element.put("colour", itemColour);
				
				elements.add(element);
				chart.put("elements", elements);
				yAxis.put("labels", yAxis_labels);
				yAxis.put("offset", 1);
				chart.put("y_axis", yAxis);
				chart.put("x_axis", xAxis);
				chartTitle.put("text", title);
				chart.put("title", chartTitle);
				chart.put("bg_colour", backgroundColour);
				
				return chart.toString();
				
			}

			
			//return "{ \"elements\": [ { \"type\": \"hbar\", \"values\": [ { \"left\": 0, \"right\": 4 }, { \"left\": 0, \"right\": 8 }, { \"left\": 0, \"right\": 11, \"tip\": \"#left# to #right#Sep to Dec (#val# months)\" } ], \"colour\": \"#86BBEF\", \"tip\": \"Months: #val#\" } ], \"title\": { \"text\": \"Our New House Schedule\" }, \"x_axis\": { \"offset\": false, \"labels\": { \"labels\": [ \"Jan\", \"Feb\", \"Mar\", \"Apr\", \"May\", \"Jun\", \"Jul\", \"Aug\", \"Sep\", \"Oct\", \"Nov\", \"Dec\" ] } }, \"y_axis\": { \"offset\": 1, \"labels\": [ \"Make garden look sexy\", \"Paint house\", \"Move into house\" ] } }";
			
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
			JSONArray nameDatas = new JSONArray();
			JSONArray valueDatas = new JSONArray();
			JSONObject echart = new JSONObject();

				for (Iterator<JSONObject> iterator = datas.iterator(); iterator
						.hasNext();) {
					JSONObject d = iterator.next();
					nameDatas.add(d.get("xAxis"));
					valueDatas.add(d.get("yAxis0"));
				}
				echart.put("xAxis",nameDatas);
				echart.put("yAxis",valueDatas);
				echart.put("sign","bar");
			return echart.toString();
		}
		return "";
	}

}
