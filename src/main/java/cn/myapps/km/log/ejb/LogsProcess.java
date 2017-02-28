package cn.myapps.km.log.ejb;

import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.disk.ejb.NDir;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.org.ejb.NUser;


public  interface  LogsProcess  extends NRunTimeProcess<Logs>{
	
	public DataPackage<Logs> managerQuery(int page, int lines,ParamsTable params) throws Exception;
	public DataPackage<Logs> doQuery(int page, int lines,String userid,String operationtype,String filename) throws Exception;
	public DataPackage<Logs> doView(int page, int lines,String userid,String operationtype,String filename) throws Exception;
	public DataPackage<Logs> queryByFile(int page, int lines,String userid,String operationtype,String filename) throws Exception;
	public DataPackage<Logs> viewByFile(int page, int lines,String userid,String operationtype,String filename) throws Exception;
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
	public String doQuerySynchronizeTasks(long lastSynchTime, int diskType,NUser user) throws Exception;
	
	
	/**
	 * 记录日志
	 * @param logType  操作类型（浏览、下载、分享、收藏、删除、移动、重命名、创建）
	 * @param operationFileOrDirectory 操作类型（文件、目录）
	 * @param dir （创建、重命名）目录对象
	 * @param parentid	移动到的目录ID
	 * @param rename	重命名的名称
	 * @param dirids	(批量)删除，移动的目录id
	 * @param fileids	(批量)删除，移动的文件id
	 * @param file		（浏览、下载、分享、收藏、重命名）文件对象
	 * @param params
	 * @param user
	 * @throws Exception
	 */
	public void doLog(String logType,int operationFileOrDirectory,NDir dir,String parentid,String rename,String[] dirids, String[] fileids, NFile file, ParamsTable params, NUser user)
	throws Exception ;
}
