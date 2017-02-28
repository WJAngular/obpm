package cn.myapps.util.report;

import org.jfree.chart.JFreeChart;

import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.user.action.WebUser;

public class NullChartBuilder extends AbstractChartBuilder implements ChartBuilder {

	public String bulidOpenFlashChart(CrossReportVO reportVO, WebUser user)
			throws Exception {
		return "";
	}

	public JFreeChart bulidJFreeChart(CrossReportVO reportVO, WebUser user)
			throws Exception {
		return null;
	}

	public String bulidEChart(CrossReportVO reportVO, WebUser user)
			throws Exception {
		// TODO Auto-generated method stub
		return "";
	}

}
