package cn.myapps.util.report;

import java.util.Iterator;

import jofc2.model.Chart;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.BarChart;
import jofc2.model.elements.BarChart.Bar;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jfree.chart.JFreeChart;

import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

/**
 * 柱状图
 * @author Happy
 *
 */
public class HistogramChartBuilder extends AbstractChartBuilder implements ChartBuilder {

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
			double yMax = 0;//Y 轴最大值
			
			Chart chart = new Chart(title);
			chart.setBackgroundColour(backgroundColour);
			BarChart barChart = new BarChart(BarChart.Style. GLASS);// 设置柱状图样式
			XAxis x = new XAxis(); // X 轴

			JSONArray datas = JSONArray.fromObject(dataSet);
			if (datas != null && datas.size() > 0) {
				for (Iterator<JSONObject> iterator = datas.iterator(); iterator
						.hasNext();) {
					JSONObject d = iterator.next();
					x.addLabels(d.getString("xAxis"));
					double yValue = d.getDouble("yAxis0");
					if(yValue>yMax) yMax = yValue;
					Bar bar = new Bar(yValue);
					bar.setTooltip(String.valueOf(yValue));
					bar.setColour(itemColour); // 颜色
					barChart.addBars(bar); // 条标题，显示在 x 轴上
				}
				chart.addElements(barChart); // 把柱图加入到图表
				
				YAxis y = new YAxis();  //y 轴   
				y.setMax(yMax+10);//y 轴最大值   
				y.setSteps(Math.ceil(yMax/10)); // 步进   
				
				chart.setYAxis(y);  
				chart.setXAxis(x);  
			} else {
				barChart.addBars(new Bar(0));
				chart.addElements(barChart); 
			}
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
				echart.put("sign","histogram");
			return echart.toString();
		}
		return "";
	}

}
