package cn.myapps.util.report;

import org.jfree.chart.JFreeChart;

import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.user.action.WebUser;

/**
 * 报表图形生成器
 * @author Happy
 *
 */
public interface ChartBuilder {
	
	/**折线图**/
	public static String LINE_CHART = "LineChart";
	/**条形图**/
	public static String BAR_CHART = "BarChart";
	/**面积图**/
	public static String AREA_CHART = "AreaChart";
	/**柱状图**/
	public static String HISTOGRAM_CHART = "ColumnChart";
	/**散点图**/
	public static String SCATTERPLOT_CHART = "PlotChart";
	/**馅饼图**/
	public static String PIE_CHART = "PieChart";
	
	/**图表背景颜色**/
	public String backgroundColour = "#FFFFFF";
	/**图表元素颜色**/
	public String itemColour = "#3A90DF";
	
	
	/**
	 * 生成OpenFlashChart格式的图形报表
	 * @param reportVO
	 * 		报表对象
	 * @param user
	 * 		用户
	 * @return
	 * 		返回JSON格式的报表数据
	 * @throws Exception
	 */
	public String bulidOpenFlashChart(CrossReportVO reportVO,WebUser user) throws Exception;
	
	/**
	 * 生成JFreeChart格式的图形报表对象
	 * @param reportVO
	 * 		报表对象
	 * @param user
	 * 		用户
	 * @return
	 * 		JFreeChart对象
	 * @throws Exception
	 */
	public JFreeChart bulidJFreeChart(CrossReportVO reportVO,WebUser user) throws Exception;
	
	/**
	 * 生成EChart格式的图形报表
	 * @param reportVO
	 * 		报表对象
	 * @param user
	 * 		用户
	 * @return
	 * 		返回JSON格式的报表数据
	 * @throws Exception
	 */
	public String bulidEChart(CrossReportVO reportVO,WebUser user) throws Exception;
	

}
