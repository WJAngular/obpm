package cn.myapps.km.report.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import jofc2.model.Chart;
import jofc2.model.Text;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.Element;
import jofc2.model.elements.StackedBarChart;
import jofc2.model.elements.StackedBarChart.Key;
import jofc2.model.elements.StackedBarChart.Stack;
import jofc2.model.elements.StackedBarChart.StackValue;
import cn.myapps.core.workflow.analyzer.Colors;
import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.report.dao.NDashBoardDAO;
import cn.myapps.km.util.DateUtil;

public class NDashBoardProessBean extends AbstractBaseProcessBean<NDashBoardVO>
		implements NDashBoardProcess {

	private static final long serialVersionUID = 1L;

	@Override
	public NRuntimeDAO getDAO() throws Exception {
		return DaoManager.getNDashBoardDAO(getConnection());
	}

	@Override
	public void doCreate(NObject no) throws Exception {
	}

	@Override
	public void doUpdate(NObject no) throws Exception {

	}

	@Override
	public void doRemove(String pk) throws Exception {

	}

	@Override
	public NObject doView(String id) throws Exception {
		return null;
	}

	/**
	 * 各部门月、年上传量统计表
	 */
	public String getSumDepartUpload(String startDate, String endDate,
			String columns) throws Exception {
		try {
			if (startDate == null || startDate.equals("null"))
				startDate = "";
			if (endDate == null || endDate.equals("null") || endDate.equals(""))
				endDate = DateUtil.format(new Date(), "yyyy-mm-dd");
			if (columns == null || columns == "")
				columns = "10";
			DataPackage<NDashBoardVO> boards = ((NDashBoardDAO) getDAO())
					.getSumDepartmentUpLoad(startDate, endDate, columns);
			String title = "部门上传量排名统计表";
			String result = createReportStr(boards, title);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 人员上传量排名统计表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getSumUserUpload(String startDate, String endDate,
			String columns) throws Exception {
		try {
			if (startDate == null || startDate.equals("null"))
				startDate = "";
			if (endDate == null || endDate.equals("null") || endDate.equals(""))
				endDate = DateUtil.format(new Date(), "yyyy-mm-dd");
			if (columns == null || columns == "")
				columns = "10";
			DataPackage<NDashBoardVO> boards = ((NDashBoardDAO) getDAO())
					.getSumUserUpLoad(startDate, endDate, columns);
			String title = "人员上传量排名统计表";
			String result = createReportStr(boards, title);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 文件浏览上传排名统计表
	 */
	public String getFilePreview(String startDate, String endDate,
			String columns) throws Exception {
		// TODO Auto-generated method stub
		try {
			if (startDate == null || startDate.equals("null"))
				startDate = "";
			if (endDate == null || endDate.equals("null") || endDate.equals(""))
				endDate = DateUtil.format(new Date(), "yyyy-mm-dd");
			if (columns == null || columns == "")
				columns = "10";
			DataPackage<NDashBoardVO> boards = ((NDashBoardDAO) getDAO())
					.getSumFilePreview(startDate, endDate,columns);
			String title = "文件浏览排名统计表";
			return createReportStr(boards, title);
		} catch (Exception e) {

		}
		return "";
	}

	/**
	 * 各专业上传量排名统计表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCategoryUpload(String startDate, String endDate,
			String columns) throws Exception {
		try {
			if (startDate == null || startDate.equals("null"))
				startDate = "";
			if (endDate == null || endDate.equals("null") || endDate.equals(""))
				endDate = DateUtil.format(new Date(), "yyyy-mm-dd");
			if (columns == null || columns == "")
				columns = "10";
			DataPackage<NDashBoardVO> boards = ((NDashBoardDAO) getDAO())
					.getSumCategoryUpload(startDate, endDate, columns);
			String title = "专业上传量排名统计表";
			return createReportStr(boards, title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 文档下载排名统计表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getSumFileDownLoad(String startDate,String endDate,String columns) throws Exception {
		try {
			if (startDate == null || startDate.equals("null"))
				startDate = "";
			if (endDate == null || endDate.equals("null") || endDate.equals(""))
				endDate = DateUtil.format(new Date(), "yyyy-mm-dd");
			if (columns == null || columns == "")
				columns = "10";
			DataPackage<NDashBoardVO> datas = ((NDashBoardDAO) getDAO())
					.getSumFileDownLoad(startDate, endDate, columns);
			// TODO Auto-generated method stub
			String title = "文档下载排名统计表";
			return createReportStr(datas, title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private ArrayList<NDashBoardVO> createData(String[] name, double[] value) {
		ArrayList<NDashBoardVO> list = new ArrayList<NDashBoardVO>();
		for (int i = 0; i < name.length; i++) {
			NDashBoardVO vo = new NDashBoardVO();
			vo.setName(name[i]);
			vo.setValue(value[i]);
			list.add(vo);
		}
		return list;
	}

	private String createReportStr(DataPackage<NDashBoardVO> boards,
			String titleStr) {
		double max = 0;
		StackedBarChart bar = new StackedBarChart();
		String AXISCOLOR = "#ff00ff";
		String GRIDCOLOR = "#888888";
		Colors color = new Colors();
		XAxis x_Axis = new XAxis();
		YAxis y_axis = new YAxis(); // y 轴
		int i = 0;
		StackValue stackZero = null;
		stackZero = new StackValue(0, color.getColor(i));
		bar.addStack((new Stack()).addStackValues(stackZero));
		x_Axis.addLabels("");
		if (boards.rowCount > 0) {
			for (Iterator<NDashBoardVO> its = boards.datas.iterator(); its
					.hasNext();) {
				NDashBoardVO vo = its.next();
				Key key = new Key(color.getColor(i), vo.getName(), 10);
				bar.addKeys(key);
				StackValue stack = null;
				stack = new StackValue(vo.getValue(), color.getColor(i));
				bar.addStack((new Stack()).addStackValues(stack));
				x_Axis.addLabels(vo.getName());
				if (vo.getValue() > max) {
					max = vo.getValue();
				}
				i++;
			}
		}
		// 设置X坐标
		x_Axis.setColour(AXISCOLOR);// 设置x轴的颜色
		x_Axis.setGridColour(GRIDCOLOR);// 设置x轴平行的网格的颜色
		x_Axis.setStroke(1); // 描点
		x_Axis.setTickHeight(4);// 点的高度
		x_Axis.setOffset(false);
		x_Axis.setMax(boards.rowCount + 1);
		x_Axis.setSteps(1);
		x_Axis.getLabels().setSize(12);// 设置显示的文本大小

		// 设置Y轴
		int step = ((int) max / 10) + 1;

		y_axis.setMax(step * 10);
		y_axis.setColour(AXISCOLOR);
		y_axis.setGridColour(GRIDCOLOR);
		y_axis.setStroke(2);// 设置y轴的的线的宽度

		y_axis.setSteps(step); // 间距
		y_axis.getLabels().setSize(10);// 设置显示的文本大小
		// 实例一个图表标题，并且设置标题内容和样式
		Chart flashChart = new Chart();
		Text title = new Text();
		title.setText(titleStr);
		flashChart.setTitle(title);
		// 把图放入flash
		flashChart.setXAxis(x_Axis);
		flashChart.setYAxis(y_axis);
		flashChart.addElements(new Element[] { bar });
		return flashChart.toString();
	}

	public DataPackage<ReportItem> doQuery(int page, int pageLines,String domainId,
			String rootCategoryId, String subCategoryId, String operationType,
			String departmentId, String userId,String startDate,String endDate,NUser user) throws Exception {
		return ((NDashBoardDAO) getDAO()).query(page, pageLines,domainId, rootCategoryId, subCategoryId, operationType, departmentId, userId, startDate, endDate,user);
	}
}
