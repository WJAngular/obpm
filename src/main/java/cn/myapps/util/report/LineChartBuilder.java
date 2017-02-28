package cn.myapps.util.report;

import java.util.Iterator;

import jofc2.model.Chart;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.LineChart;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jfree.chart.JFreeChart;

import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

/**
 * 折线图生成器
 * @author Happy
 *
 */
public class LineChartBuilder extends AbstractChartBuilder implements ChartBuilder {

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
			LineChart lineChart = new LineChart();//折线图
			//lineChart.setFontSize(12);// 设置字体
			
			XAxis x = new XAxis(); // X 轴
			YAxis y = new YAxis();  //y 轴   
			
			JSONArray datas = JSONArray.fromObject(dataSet);
			if (datas != null && datas.size() > 0) {
				for (Iterator<JSONObject> iterator = datas.iterator(); iterator
						.hasNext();) {
					JSONObject d = iterator.next();
					String label = d.getString("xAxis");
					double value = d.getDouble("yAxis0");
					if(value>yMax) yMax = value;
					lineChart.addDots(new LineChart.Dot(value));
					x.addLabels(label);
				}
				y.setMax(yMax); //y 轴最大值   
				y.setSteps(Math.ceil(yMax/10)); // 步进   

				chart.addElements(lineChart); // 把折线图加入到图表   
				chart.setYAxis(y);  
				chart.setXAxis(x);   
			}else{
				lineChart.addDots(new LineChart.Dot(0));
				x.addLabels("");
				y.setMax(yMax); //y 轴最大值   
				chart.addElements(lineChart);
				chart.setYAxis(y);  
				chart.setXAxis(x);  
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
				echart.put("sign","line");
			return echart.toString();
		}
		return "";
	}

}
