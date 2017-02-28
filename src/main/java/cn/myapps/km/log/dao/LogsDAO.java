package cn.myapps.km.log.dao;

import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.log.ejb.Logs;
import cn.myapps.km.org.ejb.NUser;

public interface LogsDAO extends NRuntimeDAO {
	
	/**
	 * 通过用户ID查询用户浏览、下载、分享、收藏的次数
	 * @param operationType 操作类型（浏览、下载、分享、收藏）
	 * @param userId 用户ID
	 * @return 统计量
	 * @throws Exception
	 */
	public long getTypeCountByUser(String operationType,String userId) throws Exception;
	
	/**
	 * 通过文件ID查询文件被浏览、下载、分享、收藏的次数
	 * @param operationType 操作类型（浏览、下载、分享、收藏）
	 * @param fileId  文件ID
	 * @return 统计量
	 * @throws Exception
	 */
	public long getTypeCountByFile(String operationType,String fileId) throws Exception;
	
	/**
	 * 通过用户ID查询用户浏览、下载、分享、收藏的日记（分页）
	 * @param operationtype 操作类型（浏览、下载、分享、收藏）
	 * @param userid 用户ID
	 * @return Logs
	 * @throws Exception
	 */
	public DataPackage<Logs> query(int page, int lines,String userid,String operationtype,String filename) throws Exception;
	
	public DataPackage<Logs> view(int page, int lines,String userid,String operationtype,String filename) throws Exception;
	public DataPackage<Logs> queryByFile(int page, int lines,String userid,String operationtype,String filename) throws Exception;
	public DataPackage<Logs> viewByFile(int page, int lines,String userid,String operationtype,String filename) throws Exception;
	public DataPackage<Logs> managerQuery(int page, int lines,ParamsTable params) throws Exception;
	
	/**
	 * 查询客户端同步任务内容
	 * @param lastSynchTime
	 * 	上一次同步时间
	 * @param diskType
	 * 网盘类型
	 * @param user
	 * 	用户
	 * @return
	 * 	json格式的数据
	 * @throws Exception
	 */
	public String querySynchronizeTasks(long lastSynchTime, int diskType,NUser user) throws Exception;
}
