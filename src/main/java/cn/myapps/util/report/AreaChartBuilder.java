package cn.myapps.util.report;


import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jfree.chart.JFreeChart;

import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

/**
 * 面积图生成器
 * @author Happy
 *
 */
public class AreaChartBuilder extends AbstractChartBuilder implements ChartBuilder {

	public String bulidOpenFlashChart(CrossReportVO reportVO, WebUser user)
			throws Exception {
		
		String reportJson = reportVO.getJson();
		String title = "";
		String chartType = "area";//面积图
		if (!StringUtil.isBlank(reportJson)) {
			JSONObject cfg = JSONObject.fromObject(reportJson);
			title = cfg.getString("viewLabel");
		}
		
		String dataSet = getDataSet(reportVO, user);
		if (dataSet != null) {
			JSONArray datas = JSONArray.fromObject(dataSet);
			if (datas != null && datas.size() > 0) {
				double yMax = 1.0;//Y轴最大值
				double yMin = 0.0;//Y轴最小值
				JSONObject chart = new JSONObject();
				JSONObject chartTitle = new JSONObject();
				JSONArray elements = new JSONArray();
				JSONObject element = new JSONObject();
				element.put("type", chartType);
				element.put("width", 2);
				element.put("colour", itemColour);
				element.put("fill", "#C4B86A");
				element.put("fill-alpha", 0.7);
				
				
				JSONArray values = new JSONArray();
				
				JSONObject yAxis = new JSONObject();
				
				JSONObject xAxis = new JSONObject();
				
				for (Iterator<JSONObject> iterator = datas.iterator(); iterator.hasNext();) {
					JSONObject d = iterator.next();
					double value = d.getDouble("yAxis0");
					if(value>yMax) yMax = value;
					if(value<yMin) yMin = value;
					
					values.add(value);
					
				}
				
				JSONObject dotStyle = new JSONObject();//圆点样式
				dotStyle.put("type", "dot");
				dotStyle.put("colour", "#000000");
				dotStyle.put("dot-size", 7);
				element.put("dot-style", dotStyle);
				
				element.put("values", values);
				element.put("colour", itemColour);
				element.put("width", 2);
				
				elements.add(element);
				chart.put("elements", elements);
				
				yAxis.put("offset", 0);
				yAxis.put("max", yMax);
				yAxis.put("min", yMin);
				yAxis.put("steps",Math.ceil(yMax/10));
				
				xAxis.put("steps",Math.ceil(datas.size()/10));
				
				chart.put("y_axis", yAxis);
				chart.put("x_axis", xAxis);
				chartTitle.put("text", title);
				chart.put("title", chartTitle);
				chart.put("bg_colour", backgroundColour);
				
				return chart.toString();
				
			}
		}
	
		
		return "";
		
		//{ "elements": [ { "type": "area", "width": 2, "dot-style": { "type": "dot", "colour": "#9C0E57", "dot-size": 7 },
		//"colour": "#C4B86A", "fill": "#C4B86A", "fill-alpha": 0.7, "values": [ 0, 0.37747172851062, 0.73989485038644, 1.0728206994506, 1.3629765727091, 1.598794871135, 1.7708742633377, 1.8723544869781, 1.8991898457789, 1.8503104986686, 1.7276651109688, 1.5361431672572, 1.2833800430472, 0.97945260646078, 0.63647748529622, 0.26812801531375, -0.1109108725124, -0.48552809385098, -0.84078884226022, -1.1625299927912, -1.4379247410851, -1.6559939675858, -1.8080439403901, -1.8880129069036, -1.8927127567881, -1.82195612186, -1.6785638458683, -1.4682525263564, -1.1994066119574, -0.88274414088613, -0.53088944657795 ] } ],
		//"title": { "text": "Area Chart" }, "y_axis": { "min": -2, "max": 2, "steps": 2, "labels": null, "offset": 0 },
		//"x_axis": { "labels": { "steps": 4, "rotate": 270 }, "steps": 2 } }
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
				echart.put("sign","area");
			return echart.toString();
		}
		return "";
	}

}
