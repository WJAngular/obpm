package cn.myapps.util.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jfree.chart.JFreeChart;

import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

/**
 * 散点图生成器
 * @author Happy
 *
 */
public class ScatterPlotChartBuilder extends AbstractChartBuilder implements ChartBuilder {

	@SuppressWarnings("unchecked")
	public String bulidOpenFlashChart(CrossReportVO reportVO, WebUser user)
			throws Exception {
		String reportJson = reportVO.getJson();
		String title = "";
		String chartType = "scatter";//散点图
		if (!StringUtil.isBlank(reportJson)) {
			JSONObject cfg = JSONObject.fromObject(reportJson);
			title = cfg.getString("viewLabel");
		}
		
		String dataSet = getDataSet(reportVO, user);
		if (dataSet != null) {
			JSONArray datas = JSONArray.fromObject(dataSet);
			if (datas != null && datas.size() > 0) {
				double yMax = 1.0;//x轴最大值
				JSONObject chart = new JSONObject();
				JSONObject chartTitle = new JSONObject();
				JSONArray elements = new JSONArray();
				JSONObject element = new JSONObject();
				element.put("type", chartType);
				JSONArray values = new JSONArray();
				
				JSONObject yAxis = new JSONObject();
				JSONArray xAxis_labels = new JSONArray();
				List<String> tempXAxisLabels = new ArrayList<String>();
				
				JSONObject xAxis = new JSONObject();
				
				for (Iterator<JSONObject> iterator = datas.iterator(); iterator.hasNext();) {
					JSONObject d = iterator.next();
					String label = d.getString("xAxis");
					double value = d.getDouble("yAxis0");
					if(value>yMax) yMax = value;
					
					JSONObject item = new JSONObject();
					item.put("value", value);
					values.add(item);
					
					tempXAxisLabels.add(label);
				}
				
				if(!tempXAxisLabels.isEmpty()){
						xAxis_labels.addAll(tempXAxisLabels);
				}
				JSONObject xLable = new JSONObject();
				xLable.put("labels", xAxis_labels);
				
				yAxis.put("max", yMax);
				yAxis.put("steps",Math.ceil(yMax/10));
				//xAxis.put("offset", 1);
				
				
				element.put("values", values);
				element.put("colour", itemColour);
				
				JSONObject dotStyle = new JSONObject();//锚点样式
				dotStyle.put("type", "anchor");//点的形状-多边形
				dotStyle.put("sides", 20);//边的个数
				dotStyle.put("hollow", false);//是否空心
				dotStyle.put("width", 2);//边的粗细
				element.put("dot-style", dotStyle);
				
				
				
				elements.add(element);
				chart.put("elements", elements);
				xAxis.put("labels", xLable);
				xAxis.put("offset", 1);
				chart.put("y_axis", yAxis);
				chart.put("x_axis", xAxis);
				chartTitle.put("text", title);
				chart.put("title", chartTitle);
				chart.put("bg_colour", backgroundColour);
				
				return chart.toString();
				
			}
			/**
			 *JSON格式参考
			return "{\"y_axis\":{\"max\":168,\"steps\":17}," +
					"\"title\":{\"text\":\"自定义报表一\"}," +
					"\"bg_colour\":\"#FFFFFF\"," +
					"\"elements\":[{\"colour\":\"0x336699\"," +
					"\"values\":[{\"value\":40},{\"value\":100},{\"value\":168}]," +
					"\"type\":\"scatter\",\"key-on-click\":\"toggle-visibility\"}]," +
					"\"x_axis\":{\"offset\":1,\"labels\":{\"labels\":[\"happy\",\"denny\",\"tom\"]}}}";
			**/
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
			JSONArray item = new JSONArray();
			JSONArray targetDatas = new JSONArray();
			JSONObject echart = new JSONObject();

				for (Iterator<JSONObject> iterator = datas.iterator(); iterator
						.hasNext();) {
					JSONObject d = iterator.next();
					targetDatas.add(d.get("xAxis"));
					targetDatas.add(d.get("yAxis0"));
					item.add(targetDatas);
					targetDatas.removeAll(targetDatas);
				}
				echart.put("data",item);
				echart.put("sign","scatter");
			return echart.toString();
		}
		return "";
	}

}
