package cn.myapps.km.report.dao;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.report.ejb.NDashBoardVO;
import cn.myapps.km.report.ejb.ReportItem;

public interface NDashBoardDAO extends NRuntimeDAO{
	
	public DataPackage<NDashBoardVO> getSumFileDownLoad(String startDate,String endDate,String columns) throws Exception;
	
	public DataPackage<NDashBoardVO> getSumFilePreview(String startDate,String endDate,String columns) throws Exception;
	
	public DataPackage<NDashBoardVO> getSumUserUpLoad(String startDate,String endDate,String columns) throws Exception;
	
	public DataPackage<NDashBoardVO> getSumDepartmentUpLoad(String startDate,String endDate,String columns) throws Exception;
	
	public DataPackage<NDashBoardVO> getSumCategoryUpload(String startDate,String endDate,String columns) throws Exception;
	
	/**
	 * 检索报表
	 * @param page
	 * 		当前页码
	 * @param pageLines
	 * 		每页数据量
	 * @param rootCategoryId
	 * 		根类别
	 * @param subCategoryId
	 * 		子类别
	 * @param operationType
	 * 		行为类型
	 * @param departmentId
	 * 		部门id
	 * @param userId
	 * 		用户id
	 * @return
	 * 		ReportItem集合
	 * @throws Exception
	 */
	public DataPackage<ReportItem> query(int page, int pageLines,String domainId,String rootCategoryId,String subCategoryId, String operationType,String departmentId,String userId,String startDate,String endDate,NUser user)throws Exception;
}
