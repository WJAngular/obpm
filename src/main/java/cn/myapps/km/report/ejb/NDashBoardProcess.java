package cn.myapps.km.report.ejb;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.log.ejb.Logs;
import cn.myapps.km.org.ejb.NUser;

public interface NDashBoardProcess extends NRunTimeProcess<NDashBoardVO> {
	
	/**
	 * 各部门月、年上传量统计表
	 * @return
	 * @throws Exception
	 */
	public String getSumDepartUpload(String startDate,String endDate,String columns) throws Exception;
	
	/**
	 * 人员上传量排名统计表
	 * @return
	 * @throws Exception
	 */
	public String getSumUserUpload(String startDate,String endDate,String columns) throws Exception;
	
	/**
	 * 各专业上传量排名统计表
	 * @return
	 * @throws Exception
	 */
	public String getCategoryUpload(String startDate,String endDate,String columns) throws Exception;
	
	/**
	 * 文档览排名统计表
	 * @return
	 * @throws Exception
	 */
	public String getFilePreview(String startDate,String endDate,String columns) throws Exception;
	
	/**
	 * 文档下载排名统计表
	 * @return
	 * @throws Exception
	 */
	public String getSumFileDownLoad(String startDate,String endDate,String columns) throws Exception;
	
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
	public DataPackage<ReportItem> doQuery(int page, int pageLines,String domainId,String rootCategoryId,String subCategoryId, String operationType,String departmentId,String userId,String startDate,String endDate,NUser user)throws Exception;
}
