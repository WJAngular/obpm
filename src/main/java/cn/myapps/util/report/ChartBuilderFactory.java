package cn.myapps.util.report;

/**
 * 报表图形生成器工厂
 * @author Happy
 *
 */
public class ChartBuilderFactory {

	public static ChartBuilder getChartBuilder(String chartType) {

		if (ChartBuilder.LINE_CHART.equalsIgnoreCase(chartType)) {
			return new LineChartBuilder();
		} else if (ChartBuilder.BAR_CHART.equalsIgnoreCase(chartType)) {
			return new BarChartBuilder();
		} else if (ChartBuilder.AREA_CHART.equalsIgnoreCase(chartType)) {
			return new AreaChartBuilder();
		} else if (ChartBuilder.HISTOGRAM_CHART.equalsIgnoreCase(chartType)) {
			return new HistogramChartBuilder();
		} else if (ChartBuilder.PIE_CHART.equalsIgnoreCase(chartType)) {
			return new PieChartBuilder();
		} else if (ChartBuilder.SCATTERPLOT_CHART.equalsIgnoreCase(chartType)) {
			return new ScatterPlotChartBuilder();
		}else if (ChartBuilder.BAR_CHART.equalsIgnoreCase(chartType)) {
			return new BarChartBuilder();
		}

		return new NullChartBuilder();
	}

}
