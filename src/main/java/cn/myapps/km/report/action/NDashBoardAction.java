package cn.myapps.km.report.action;


import java.util.Collection;
import java.util.Map;

import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.report.ejb.NDashBoardProessBean;
import cn.myapps.km.report.ejb.NDashBoardVO;
import cn.myapps.km.report.ejb.ReportItem;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class NDashBoardAction extends AbstractRunTimeAction<NDashBoardVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String chartstr;
	
	private DataPackage<ReportItem> reportItemDataPackage = null;
	
	public String getChartstr() {
		return chartstr;
	}

	public void setChartstr(String chartstr) {
		this.chartstr = chartstr;
	}

	public DataPackage<ReportItem> getReportItemDataPackage() {
		return reportItemDataPackage;
	}

	public void setReportItemDataPackage(
			DataPackage<ReportItem> reportItemDataPackage) {
		this.reportItemDataPackage = reportItemDataPackage;
	}
	
	/**
	 * 返回树形Department集合
	 * 
	 * @return Department集合
	 * @throws Exception
	 */
	public Map<String, String> get_departmentTree() throws Exception {
		DepartmentProcess da = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);
		Collection<DepartmentVO> dc = da.queryByDomain(getUser().getDomainid());
		Map<String, String> dm = ((DepartmentProcess) da)
				.deepSearchDepartmentTree(dc, null, null, 0);

		return dm;
	}

	@Override
	public NRunTimeProcess<NDashBoardVO> getProcess() {
		// TODO Auto-generated method stub
		return new NDashBoardProessBean();
	}
	
	/**
	 * 各部门月、年上传量统计表
	 * @return
	 */
	public String doSumDepartmentupload(){
		  //定义柱状图
		try {
			ParamsTable params = getParams();
			String startDate = params.getParameterAsString("startDate");
			String endDate = params.getParameterAsString("endDate");
			String columns = params.getParameterAsString("columns");
			setChartstr(((NDashBoardProessBean)getProcess()).getSumDepartUpload(startDate,endDate,columns));
		} catch (Exception e) {
			e.printStackTrace();
		}// 转成 json 格式
		return SUCCESS;
	}
	
	/**
	 * 人员上传量排名统计表
	 * @return
	 */
	public String doSumUserupLoad(){
		try {
			ParamsTable params = getParams();
			String startDate = params.getParameterAsString("startDate");
			String endDate = params.getParameterAsString("endDate");
			String columns = params.getParameterAsString("columns");
			setChartstr(((NDashBoardProessBean)getProcess()).getSumUserUpload(startDate,endDate,columns));
		} catch (Exception e) {
			e.printStackTrace();
		}// 转成 json 格式
		return SUCCESS;
	}
	
	/**
	 * 各专业上传量排名统计表
	 * @return
	 */
	public String doCategoryuploadUpload(){
		try {
			ParamsTable params = getParams();
			String startDate = params.getParameterAsString("startDate");
			String endDate = params.getParameterAsString("endDate");
			String columns = params.getParameterAsString("columns");
			setChartstr(((NDashBoardProessBean)getProcess()).getCategoryUpload(startDate,endDate,columns));
		} catch (Exception e) {
			e.printStackTrace();
		}// 转成 json 格式
		return SUCCESS;
	}
	
	/**
	 * 文档览排名统计表
	 * @return
	 */
	public String doFilePreview(){
		try {
			ParamsTable params = getParams();
			String startDate = params.getParameterAsString("startDate");
			String endDate = params.getParameterAsString("endDate");
			String columns = params.getParameterAsString("columns");
			setChartstr(((NDashBoardProessBean)getProcess()).getFilePreview(startDate,endDate,columns));
		} catch (Exception e) {
			e.printStackTrace();
		}// 转成 json 格式
		return SUCCESS;
	}
	
	/**
	 * 文档下载排名统计表
	 */
	public String doFileDownLoad(){
		try {
			ParamsTable params = getParams();
			String startDate = params.getParameterAsString("startDate");
			String endDate = params.getParameterAsString("endDate");
			String columns = params.getParameterAsString("columns");
			setChartstr(((NDashBoardProessBean)getProcess()).getSumFileDownLoad(startDate,endDate,columns));
		} catch (Exception e) {
			e.printStackTrace();
		}// 转成 json 格式
		return SUCCESS;
	}
	
	/**
	 * 报表筛选器查询
	 * @return
	 */
	public String doQuery(){
		try {
			ParamsTable params = getParams();
			NUser user = getUser();
			String startDate = params.getParameterAsString("startDate");
			String endDate = params.getParameterAsString("endDate");
			int page =  1;//params.getParameterAsInteger("page");
			int pageLines = Integer.MAX_VALUE;//params.getParameterAsInteger("endDate");
			String domainId = getUser().getDomainid();
			String rootCategoryId= params.getParameterAsString("rootCategoryId");
			String subCategoryId= params.getParameterAsString("subCategoryId");
			String operationType= params.getParameterAsString("operationType");
			String departmentId= params.getParameterAsString("departmentId");
			String userId= params.getParameterAsString("userId");
			String _isQuery = params.getParameterAsString("_isQuery");
			if(!StringUtil.isBlank(_isQuery)){
				setReportItemDataPackage(new DataPackage<ReportItem>());
			}else{
				setReportItemDataPackage(((NDashBoardProessBean)getProcess()).doQuery(page, pageLines, domainId, rootCategoryId, subCategoryId, operationType, departmentId, userId, startDate, endDate,user));
			}
		} catch (Exception e) {
			this.addFieldError("", e.getMessage());
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
}
