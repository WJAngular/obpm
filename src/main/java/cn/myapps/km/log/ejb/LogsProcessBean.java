package cn.myapps.km.log.ejb;

import java.util.Date;

import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.disk.dao.NDirDAO;
import cn.myapps.km.disk.dao.NFileDAO;
import cn.myapps.km.disk.ejb.NDir;
import cn.myapps.km.disk.ejb.NDirProcess;
import cn.myapps.km.disk.ejb.NDirProcessBean;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.disk.ejb.NFileProcess;
import cn.myapps.km.disk.ejb.NFileProcessBean;
import cn.myapps.km.log.dao.LogsDAO;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.Sequence;
import cn.myapps.km.util.UserUtil;


public class LogsProcessBean extends AbstractBaseProcessBean<Logs>
		implements LogsProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7113254731805614957L;
	
	@Override
	public NRuntimeDAO getDAO() throws Exception {
		// TODO Auto-generated method stub
		return DaoManager.getLogsDAO(getConnection());
	}
	
	@Override
	public void doCreate(NObject no) throws Exception {
		// TODO Auto-generated method stub
		Logs logs = (Logs) no;
		try {
			// 1.设置id
			if (logs.getId() == null) {
				logs.setId(Sequence.getSequence());
			}
			beginTransaction();

			getDAO().create(logs);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
		}
	}

	@Override
	public void doRemove(String pk) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doUpdate(NObject no) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NObject doView(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	/**
	 * 通过文件ID查询文件被浏览、下载、分享、收藏的次数
	 * @param operationType 操作类型（浏览、下载、分享、收藏）
	 * @param fileId  文件ID
	 * @return 统计量
	 * @throws Exception
	 */
	public long getTypeCountByFile(String operationType, String fileId)
			throws Exception {
		return ((LogsDAO)getDAO()).getTypeCountByFile(operationType, fileId);
	}
	
	/**
	 * 通过用户ID查询用户浏览、下载、分享、收藏的次数
	 * @param operationType 操作类型（浏览、下载、分享、收藏）
	 * @param userId 用户ID
	 * @return 统计量
	 * @throws Exception
	 */
	public long getTypeCountByUser(String operationType, String userId)
			throws Exception {
		return ((LogsDAO)getDAO()).getTypeCountByUser(operationType, userId);
	}

	public DataPackage<Logs> doQuery(int page, int lines,String userid, String operationtype,String filename)
			throws Exception {
		DataPackage<Logs> result = ((LogsDAO) getDAO()).query(page,lines,userid, operationtype,filename);
		return result;
	}

	public DataPackage<Logs> queryByFile(int page, int lines,String userid, String operationtype,
			String filename) throws Exception {
		DataPackage<Logs> result = ((LogsDAO) getDAO()).queryByFile(page,lines,userid, operationtype,filename);
		return result;
	}

	public DataPackage<Logs> doView(int page, int lines, String userid,String operationtype,String filename)
			throws Exception {
		DataPackage<Logs> result = ((LogsDAO) getDAO()).view(page,lines,userid,operationtype,filename);
		return result;
	}

	public DataPackage<Logs> viewByFile(int page, int lines, String userid,
			String operationtype, String filename) throws Exception {
		DataPackage<Logs> result = ((LogsDAO) getDAO()).viewByFile(page,lines,userid,operationtype,filename);
		return result;
	}

	public DataPackage<Logs> managerQuery(int page, int lines,
			ParamsTable params) throws Exception {
		DataPackage<Logs> result = ((LogsDAO) getDAO()).managerQuery(page,lines,params);
		return result;
	}

	public String doQuerySynchronizeTasks(long lastSynchTime, int diskType,
			NUser user) throws Exception {
		return ((LogsDAO) getDAO()).querySynchronizeTasks(lastSynchTime, diskType, user);
	}
	
	public void doLog(String logType,int operationFileOrDirectory,NDir dir,String parentid,String rename,String[] dirids, String[] fileids, NFile file, ParamsTable params, NUser user)
	throws Exception {
		LogsProcess logsProcess = new LogsProcessBean();
		NFileProcess nFileProcess = new NFileProcessBean();
		if(Logs.OPERATION_FILE ==(operationFileOrDirectory)){
			if(Logs.OPERATION_TYPE_MOVE.equals(logType)){
				NDirProcess nDirProcess = new NDirProcessBean();
				NDir d = (NDir) nDirProcess.doView(parentid);
				if(fileids.length > 0){
					for(int i=0; i<fileids.length; i++){
						Logs logs = new Logs();
						NFile f = (NFile) nFileProcess.doView(fileids[i]);
						if(f!=null){
							logs.setFileId(f.getId());
							logs.setFileName(f.getName());
						}
						logs.setOperationContent(d.getName());
						logs.setUserId(user.getId());
						logs.setUserName(user.getName());
						logs.setOperationType(logType);
						logs.setOperationFileOrDirectory(operationFileOrDirectory);
						logs.setOperationDate(new Date());
						logs.setUserIp(UserUtil.getIpAddr(params.getHttpRequest()));
						logs.setDepartmentId(user.getDefaultDepartment());
						logs.setDepartmentName((user.getDepartmentById(user.getDefaultDepartment())).getName());
						
						logsProcess.doCreate(logs);
					}
				}
			}else if(Logs.OPERATION_TYPE_DELETE.equals(logType)){
				 if(fileids.length > 0){
						for(int i=0; i<fileids.length; i++){
							Logs logs = new Logs();
								NFile f = (NFile) nFileProcess.doView(fileids[i]);
								if(f!=null){
									logs.setFileId(f.getId());
									logs.setFileName(f.getName());
								}
							logs.setUserId(user.getId());
							logs.setUserName(user.getName());
							logs.setOperationType(logType);
							logs.setOperationFileOrDirectory(operationFileOrDirectory);
							logs.setOperationDate(new Date());
							logs.setUserIp(UserUtil.getIpAddr(params.getHttpRequest()));
							logs.setDepartmentId(user.getDefaultDepartment());
							logs.setDepartmentName((user.getDepartmentById(user.getDefaultDepartment())).getName());
							
							logsProcess.doCreate(logs);
						}
					}
			}else if(Logs.OPERATION_TYPE_RENAME.equals(logType)){
				Logs logs = new Logs();
				if(file!=null){
					logs.setFileId(file.getId());
					logs.setFileName(file.getName());
				}
				logs.setOperationContent(rename);
				logs.setUserId(user.getId());
				logs.setUserName(user.getName());
				logs.setOperationType(logType);
				logs.setOperationFileOrDirectory(operationFileOrDirectory);
				logs.setOperationDate(new Date());
				logs.setUserIp(UserUtil.getIpAddr(params.getHttpRequest()));
				logs.setDepartmentId(user.getDefaultDepartment());
				logs.setDepartmentName((user.getDepartmentById(user.getDefaultDepartment())).getName());
				
				logsProcess.doCreate(logs);
			}else{
			try {
				Logs logs = new Logs();
				if(file!=null){
					logs.setFileId(file.getId());
					logs.setFileName(file.getName());
				}
				
				logs.setUserId(user.getId());
				logs.setUserName(user.getName());
				logs.setOperationType(logType);
				logs.setOperationDate(new Date());
				logs.setUserIp(UserUtil.getIpAddr(params.getHttpRequest()));
				logs.setDepartmentId(user.getDefaultDepartment());
				logs.setDepartmentName((user.getDepartmentById(user.getDefaultDepartment())).getName());
				logs.setOperationFileOrDirectory(Logs.OPERATION_FILE);
				beginTransaction();
				if (Logs.OPERATION_TYPE_VIEW.equals(logType)) {
					//logs.setOperationContent("阅读");
					file.setViews(file.getViews() + 1);
					nFileProcess.doUpdate(file);
				} else if (Logs.OPERATION_TYPE_DOWNLOAD.equals(logType)) {
				//	logs.setOperationContent("下载");
					file.setDownloads(file.getDownloads() + 1);
					nFileProcess.doUpdate(file);
				} else if (Logs.OPERATION_TYPE_FAVORITE.equals(logType)) {
					//logs.setOperationContent("收藏");
					file.setFavorites(file.getFavorites() + 1);
					nFileProcess.doUpdate(file);
				} else if (Logs.OPERATION_TYPE_SHARE.equals(logType)) {
					String usernames = (String) params.getParameter("用户选择");
					if(usernames.equals("")){
						//logs.setOperationContent("公共分享");
					}else{
						logs.setOperationContent(usernames);
					}
				}else if(Logs.OPERATION_TYPE_RECOMMEND.equals(logType)){
					String usernames = (String) params.getParameter("用户选择");
					logs.setOperationContent(usernames);
				}else if(Logs.OPERATION_TYPE_SELECT.equals(logType)){
					String usernames = (String) params.getParameterAsString("queryString");
					logs.setOperationContent(usernames);
				}
				
				logsProcess.doCreate(logs);
				commitTransaction();
			} catch (Exception e) {
				rollbackTransaction();
				e.printStackTrace();
				throw e;
			}
			}
		}else if(Logs.OPERATION_DIRECTORY ==(operationFileOrDirectory)){
			if(Logs.OPERATION_TYPE_RENAME.equals(logType)){
				Logs logs = new Logs();
				if(dir!=null){
					logs.setFileId(dir.getId());
					logs.setFileName(dir.getName());
				}
				logs.setOperationContent(rename);
				logs.setUserId(user.getId());
				logs.setUserName(user.getName());
				logs.setOperationType(logType);
				logs.setOperationFileOrDirectory(operationFileOrDirectory);
				logs.setOperationDate(new Date());
				logs.setUserIp(UserUtil.getIpAddr(params.getHttpRequest()));
				logs.setDepartmentId(user.getDefaultDepartment());
				logs.setDepartmentName((user.getDepartmentById(user.getDefaultDepartment())).getName());
				
				logsProcess.doCreate(logs);
			}else if(Logs.OPERATION_TYPE_MOVE.equals(logType)){
				NDirProcess nDirProcess = new NDirProcessBean();
				NDir d = (NDir) nDirProcess.doView(parentid);
				if(dirids.length > 0){
					for(int i=0; i<dirids.length; i++){
						Logs logs = new Logs();
						NDir dirs = (NDir) nDirProcess.doView(dirids[i]);
						if(dirs!=null){
							logs.setFileId(dirs.getId());
							logs.setFileName(dirs.getName());
						}
						logs.setOperationContent(d.getName());
						logs.setUserId(user.getId());
						logs.setUserName(user.getName());
						logs.setOperationType(logType);
						logs.setOperationFileOrDirectory(operationFileOrDirectory);
						logs.setOperationDate(new Date());
						logs.setUserIp(UserUtil.getIpAddr(params.getHttpRequest()));
						logs.setDepartmentId(user.getDefaultDepartment());
						logs.setDepartmentName((user.getDepartmentById(user.getDefaultDepartment())).getName());
						logs.setOperationFileOrDirectory(operationFileOrDirectory);
						
						logsProcess.doCreate(logs);
					}
				}
			}else if(Logs.OPERATION_TYPE_CREATE.equals(logType)){
				Logs logs = new Logs();
				logs.setFileId(dir.getId());
				logs.setFileName(dir.getName());
				logs.setUserId(user.getId());
				logs.setUserName(user.getName());
				logs.setOperationType(logType);
				logs.setOperationFileOrDirectory(operationFileOrDirectory);
				logs.setOperationDate(new Date());
				logs.setUserIp(UserUtil.getIpAddr(params.getHttpRequest()));
				logs.setDepartmentId(user.getDefaultDepartment());
				logs.setDepartmentName((user.getDepartmentById(user.getDefaultDepartment())).getName());
				
				logsProcess.doCreate(logs);
			}else if(Logs.OPERATION_TYPE_DELETE.equals(logType)){
				NDirProcess nDirProcess = new NDirProcessBean();
				if(dirids.length > 0){
					for(int i=0; i<dirids.length; i++){
						Logs logs = new Logs();
							NDir dirs = (NDir) nDirProcess.doView(dirids[i]);
							if(dirs!=null){
								logs.setFileId(dirs.getId());
								logs.setFileName(dirs.getName());
							}
						logs.setUserId(user.getId());
						logs.setUserName(user.getName());
						logs.setOperationType(logType);
						logs.setOperationFileOrDirectory(operationFileOrDirectory);
						logs.setOperationDate(new Date());
						logs.setUserIp(UserUtil.getIpAddr(params.getHttpRequest()));
						logs.setDepartmentId(user.getDefaultDepartment());
						logs.setDepartmentName((user.getDepartmentById(user.getDefaultDepartment())).getName());
						
						logsProcess.doCreate(logs);
					}
				}
			}
		}
	}

}
