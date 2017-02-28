package cn.myapps.pm.task.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.base.dao.AbstractBaseDAO;
import cn.myapps.pm.task.ejb.Follower;
import cn.myapps.pm.task.ejb.Task;
import cn.myapps.pm.util.ConnectionManager;
import cn.myapps.util.DateUtil;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

public abstract class AbstractTaskDAO extends AbstractBaseDAO {

	
	private static final Logger log = Logger.getLogger(AbstractTaskDAO.class);
	
	/**任务默认排序Map
	 *       
	 * **/ 
	public static final LinkedHashMap<String,String> TASK_ORDER_CONDITION_MAP ;
	
	
	static{
		TASK_ORDER_CONDITION_MAP = new LinkedHashMap<String,String>();
		//TASK_ORDER_CONDITION.put("START_DATE", "DESC");
		//伪劣，用于解决状态顺序不一致的情况
		TASK_ORDER_CONDITION_MAP.put("STATUSORDER", "ASC");
		TASK_ORDER_CONDITION_MAP.put("CREATE_DATE", "DESC");
		TASK_ORDER_CONDITION_MAP.put("LEVELS", "DESC");
		TASK_ORDER_CONDITION_MAP.put("NAME", "DESC");
		TASK_ORDER_CONDITION_MAP.put("TAGS", "DESC");
	}

	public AbstractTaskDAO(Connection conn) throws Exception {
		super(conn);
		this.tableName = "PM_TASK";
	}

	public ValueObject create(ValueObject vo) throws Exception {
		Task task = (Task)vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName(tableName)
				+ " (ID,NAME,DESCRIPTION,LEVELS,STATUS,CREATOR,CREATOR_ID,CREATE_DATE,START_DATE,END_DATE,REMIND_MODE,TAGS,EXECUTER,EXECUTER_ID,SUB_TASKS,REMARK,LOGS,PROJECT_ID,PROJECT_NAME,DOMAIN_ID,ATTACHMENT) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, task.getId());
			stmt.setString(2, task.getName());
			stmt.setString(3, task.getDescription());
			stmt.setInt(4,task.getLevel());
			stmt.setInt(5,task.getStatus());
			stmt.setString(6, task.getCreator());
			stmt.setString(7, task.getCreatorId());
			if (task.getCreateDate() == null) {
				stmt.setNull(8, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(task.getCreateDate().getTime());
				stmt.setTimestamp(8, ts);
			}
			if (task.getStartDate() == null) {
				stmt.setNull(9, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(task.getStartDate().getTime());
				stmt.setTimestamp(9, ts);
			}
			if (task.getEndDate() == null) {
				stmt.setNull(10, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(task.getEndDate().getTime());
				stmt.setTimestamp(10, ts);
			}
			stmt.setInt(11,task.getRemindMode());
			stmt.setString(12, task.getTags());
			stmt.setString(13, task.getExecutor());
			stmt.setString(14, task.getExecutorId());
			stmt.setString(15, task.getSubTasks());
			stmt.setString(16, task.getRemark());
			stmt.setString(17, task.getLogs());
			stmt.setString(18, task.getProjectId());
			stmt.setString(19, task.getProjectName());
			stmt.setString(20, task.getDomainid());
			stmt.setString(21, task.getAttachment());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		
		return vo;
	}

	public ValueObject find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName(tableName) + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			Task task = null;
			if (rs.next()) {
				task = new Task();
				setProperties(task, rs);
			}
			rs.close();
			return task;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public void remove(String pk) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";
		
		String sql2 = "DELETE FROM " + getFullTableName("PM_TASK_FOLLOWER_SET")
				+ " WHERE TASK_ID=?";
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			log.debug(sql);
			stmt.execute();
			stmt = connection.prepareStatement(sql2);
			stmt.setString(1, pk);
			log.debug(sql2);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		
	}

	public ValueObject update(ValueObject vo) throws Exception {
		Task task = (Task) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName(tableName)
				+ " SET NAME=?,DESCRIPTION=?,LEVELS=?,STATUS=?,CREATOR=?,CREATOR_ID=?,CREATE_DATE=?,START_DATE=?,END_DATE=?,REMIND_MODE=?,TAGS=?,EXECUTER=?,EXECUTER_ID=?,SUB_TASKS=?,REMARK=?,LOGS=?,PROJECT_ID=?,PROJECT_NAME=?,DOMAIN_ID=?,ATTACHMENT=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, task.getName());
			stmt.setString(2, task.getDescription());
			stmt.setInt(3,task.getLevel());
			stmt.setInt(4,task.getStatus());
			stmt.setString(5, task.getCreator());
			stmt.setString(6, task.getCreatorId());
			if (task.getCreateDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(task.getCreateDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			if (task.getStartDate() == null) {
				stmt.setNull(8, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(task.getStartDate().getTime());
				stmt.setTimestamp(8, ts);
			}
			if (task.getEndDate() == null) {
				stmt.setNull(9, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(task.getEndDate().getTime());
				stmt.setTimestamp(9, ts);
			}
			stmt.setInt(10,task.getRemindMode());
			stmt.setString(11, task.getTags());
			stmt.setString(12, task.getExecutor());
			stmt.setString(13, task.getExecutorId());
			stmt.setString(14, task.getSubTasks());
			stmt.setString(15, task.getRemark());
			stmt.setString(16, task.getLogs());
			stmt.setString(17, task.getProjectId());
			stmt.setString(18, task.getProjectName());
			stmt.setString(19, task.getDomainid());
			stmt.setString(20, task.getAttachment());
			stmt.setString(21, task.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;
		
	}
	
	/**
	 * 添加关注人
	 * @param taskId
	 * 		任务主键
	 * @param followers
	 * 		关注人集合
	 * @return
	 */
	public void addFollowers(String taskId,Collection<Follower> followers) throws Exception{
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("PM_TASK_FOLLOWER_SET")
				+ " (TASK_ID,USER_ID,USER_NAME,DOMAIN_ID) values (?,?,?,?)";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			for(Follower follower : followers){
				stmt.setString(1, taskId);
				stmt.setString(2, follower.getUserId());
				stmt.setString(3, follower.getUserName());
				stmt.setString(4, follower.getDomainId());
				stmt.addBatch();
			}
			stmt.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}
	
	/**
	 * 删除关注人
	 * @param userId
	 * 		用户主键
	 * @param taskId
	 * 		任务主键
	 * @throws Exception
	 */
	public void deleteFollower(String userId,String taskId) throws Exception{
		PreparedStatement stmt = null;
		String sql = "DELETE FROM "
			+getFullTableName("PM_TASK_FOLLOWER_SET")
			+" WHERE TASK_ID=? AND USER_ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, taskId);
			stmt.setString(2, userId);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
	}
	
	public Collection<?> simpleQuery(ParamsTable params,WebUser user) throws Exception {
		return null;
	}
	
	public Collection<Task> queryMyTasks(String name,int status,String currDate,String dateRangeType,WebUser user) throws Exception {
		
		Collection<Task> tasks = new ArrayList<Task>();
		PreparedStatement stmt = null;
		
		String sql = "SELECT ts.*,CASE STATUS WHEN 0 THEN '0'  WHEN 1 THEN '3' WHEN 2 THEN '1' WHEN 3 THEN '2' WHEN -1 THEN '4' ELSE '9' END  AS statusOrder  FROM "
				+ getFullTableName(tableName) + " ts WHERE EXECUTER_ID=?";
		
		if(status != Task.STATUS_NULL){    
			if(status != Task.STATUS_ON){
				sql+=" AND STATUS=?";
			}else{
				sql+=" AND STATUS IN (?,?,?)";
			}
			
		}
		sql+=" AND (( START_DATE>=? AND START_DATE<=?) OR ( END_DATE>=? AND END_DATE<=?) OR (START_DATE IS NULL AND END_DATE IS NULL) OR( END_DATE IS NULL ) )";
		
		if(!StringUtil.isBlank(name)){
			sql+=" AND NAME like '%?%'";
		}
		
		sql+=" ORDER BY statusOrder ASC,CREATE_DATE Desc ";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			int index =1;
			stmt.setString(index++, user.getId());

			if(status != Task.STATUS_NULL){    
				
				if(status != Task.STATUS_ON){
					stmt.setInt(index++,status);
				}else{
					stmt.setInt(index++,Task.STATUS_NEW);
					stmt.setInt(index++,Task.STATUS_IN_PROGRESS);
					stmt.setInt(index++,Task.STATUS_RESOLVED);
				}
				
			}
			stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			stmt.setTimestamp(index++, getDateRangeEnd(currDate, dateRangeType));
			stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			stmt.setTimestamp(index++, getDateRangeEnd(currDate, dateRangeType));
			
			if(!StringUtil.isBlank(name)){
				stmt.setString(index++, name);
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Task task = new Task();
				setProperties(task, rs);
				tasks.add(task);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return tasks;
	}
	
	public Collection<Task> queryMyTasks4CalendarView(String name,int status,String startDate,String endDate,WebUser user) throws Exception {
		
		Collection<Task> tasks = new ArrayList<Task>();
		PreparedStatement stmt = null;
		
		String sql = "SELECT ts.*,CASE STATUS  WHEN 0 THEN '0'  WHEN 1 THEN '3' WHEN 2 THEN '1' WHEN 3 THEN '2' WHEN -1 THEN '4' ELSE '9' END  AS statusOrder  FROM "
				+ getFullTableName(tableName) + " ts  WHERE EXECUTER_ID=?";
		
		if(status != Task.STATUS_NULL){    
			if(status != Task.STATUS_ON){
				sql+=" AND STATUS=?";
			}else{
				sql+=" AND STATUS IN (?,?,?)";
			}
			
		}
		sql+=" AND ((START_DATE<=? AND END_DATE>=?) OR (START_DATE>=? AND START_DATE<?))";
		
		if(!StringUtil.isBlank(name)){
			sql+=" AND NAME like '%?%'";
		}
		sql+=" ORDER BY statusOrder ASC , CREATE_DATE Desc ";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			int index =1;
			stmt.setString(index++, user.getId());
             if(status != Task.STATUS_NULL){    
				if(status != Task.STATUS_ON){
					stmt.setInt(index++,status);
				}else{
					stmt.setInt(index++,Task.STATUS_NEW);
					stmt.setInt(index++,Task.STATUS_IN_PROGRESS);
					stmt.setInt(index++,Task.STATUS_RESOLVED);
				}
			}
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp s = new Timestamp(format.parse(startDate).getTime());
			Timestamp e = new Timestamp(format.parse(endDate).getTime());
			
			stmt.setTimestamp(index++, s);
			stmt.setTimestamp(index++,s);
			stmt.setTimestamp(index++, s);
			stmt.setTimestamp(index++, e);
			
			if(!StringUtil.isBlank(name)){
				stmt.setString(index++, name);
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Task task = new Task();
				setProperties(task, rs);
				tasks.add(task);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return tasks;
	}
	
	/**
	 * 
	 * @param projectId
	 * 		项目主键
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		任务完成状态
	 * @param level
	 *      优先级
	 * @param executor
	 *      执行人名称
	 * @param currDate
	 * 		当前日期
	 * @param dateRangeType
	 * 		日期范围
	 * @param page
	 * 		当前页码
	 * @param lines
	 * 		单页数据条数	 
	 * @param overdueStatus 
	 * 		过期状态（"overdue"为选定已过期状态）
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Task> queryByProject(String projectId,String name,int status,int level,String executerId,String createrId ,String currDate,String dateRangeType,String tag ,String orderName,String orderBy,int page,int lines,String overdueStatus,WebUser user) throws Exception{
		
		
		DataPackage<Task> dp = new DataPackage<Task>();
		dp.setPageNo(page);
		dp.setLinesPerPage(lines);
		
		Collection<Task> tasks = new ArrayList<Task>();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		
		String sql = "SELECT t.*,CASE t.STATUS  WHEN 0 THEN '0'  WHEN 1 THEN '3' WHEN 2 THEN '1' WHEN 3 THEN '2' WHEN -1 THEN '4' ELSE '9' END  AS statusOrder ,f.USER_ID as IS_FOLLOW FROM "
				+ getFullTableName(tableName) + " t LEFT JOIN "
				+ getFullTableName("PM_TASK_FOLLOWER_SET")+" f ON t.ID=f.TASK_ID AND f.USER_ID=? WHERE t.PROJECT_ID=?";
		
		String countSql = "SELECT COUNT(*) FROM "
				+ getFullTableName(tableName) + " t LEFT JOIN "
				+getFullTableName("PM_TASK_FOLLOWER_SET")+" f ON t.ID=f.TASK_ID AND f.USER_ID=? WHERE t.PROJECT_ID=?";
		
		if(status != Task.STATUS_NULL){    
			if(status != Task.STATUS_ON){
				sql+=" AND t.STATUS=?";
				countSql+=" AND t.STATUS=?";
			}else{
				sql+=" AND t.STATUS IN (?,?,?)";
				countSql+=" AND t.STATUS IN(?,?,?)";
			}
			
		}
		
		if(!StringUtil.isBlank(dateRangeType)){
			
		sql+=" AND ((t.START_DATE  >= ? AND t.START_DATE <= ?) OR (t.END_DATE >= ? AND t.END_DATE <= ? ))";
		countSql+=" AND ((t.START_DATE  >= ? AND t.START_DATE <= ?) OR (t.END_DATE >= ? AND t.END_DATE <= ? ))";
		}
		
		if (Task.OVERDUE_STATUS_OVERDUE.equalsIgnoreCase(overdueStatus)) {
			sql += " AND t.END_DATE IS NOT NULL AND t.END_DATE<? AND STATUS!=1 AND STATUS!=-1";
			countSql += " AND t.END_DATE IS NOT NULL AND t.END_DATE<? AND STATUS!=1 AND STATUS!=-1";
		} 
		
		if(!StringUtil.isBlank(name)){
			sql+=" AND t.NAME like ?";
			countSql+=" AND t.NAME like ?";
		}
		
		if(level != Task.LEVEL_NULL){   
			sql+=" AND t.LEVELS=?";
			countSql+=" AND t.LEVELS=?";
		}
		
		if(!StringUtil.isBlank(executerId)){
			sql+=" AND t.EXECUTER_ID = ?";
			countSql+=" AND t.EXECUTER_ID = ?";
		}
		
		if(!StringUtil.isBlank(createrId)){
			sql+=" AND t.CREATOR_ID = ?";
			countSql+=" AND t.CREATOR_ID = ?";
		}
	
		if(!StringUtil.isBlank(tag)){
			sql+=" AND t.tags like ?";
			countSql+=" AND t.tags like ?";
		}
		
		LinkedHashMap<String,String> orderMap = new LinkedHashMap<String,String>();
		
		
		if(StringUtil.isBlank(orderName)){
			orderMap = TASK_ORDER_CONDITION_MAP;
		}else{
			orderMap.put(orderName, orderBy);
		}
		sql = buildLimitString(sql, page, lines,orderMap);
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt2 = connection.prepareStatement(countSql);
			int index =1;
			stmt2.setString(index, user.getId());
			stmt.setString(index++, user.getId());
			stmt2.setString(index, projectId);
			stmt.setString(index++, projectId);
			
			if(status != Task.STATUS_NULL){    
				
				if(status != Task.STATUS_ON){
					stmt2.setInt(index,status);
					stmt.setInt(index++,status);
				}else{
					stmt2.setInt(index,Task.STATUS_NEW);
					stmt.setInt(index++,Task.STATUS_NEW);
					stmt2.setInt(index,Task.STATUS_IN_PROGRESS);
					stmt.setInt(index++,Task.STATUS_IN_PROGRESS);
					stmt2.setInt(index,Task.STATUS_RESOLVED);
					stmt.setInt(index++,Task.STATUS_RESOLVED);
				}
				
			}
			
			if(!StringUtil.isBlank(dateRangeType)){
				stmt.setTimestamp(index, getDateRangeBegin(currDate, dateRangeType));
				stmt2.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			    stmt.setTimestamp(index, getDateRangeEnd(currDate, dateRangeType));
			    stmt2.setTimestamp(index++, getDateRangeEnd(currDate, dateRangeType));
			    stmt.setTimestamp(index, getDateRangeBegin(currDate, dateRangeType));
			    stmt2.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			    stmt.setTimestamp(index, getDateRangeEnd(currDate, dateRangeType));
			    stmt2.setTimestamp(index++, getDateRangeEnd(currDate, dateRangeType));
			    
			}
			if (Task.OVERDUE_STATUS_OVERDUE.equalsIgnoreCase(overdueStatus)) {
				Date date = DateUtil.parseDate(DateUtil.getCurDateStr("yyyy-MM-dd"));
				Timestamp now = new Timestamp(date.getTime());
				stmt.setTimestamp(index, now);
				stmt2.setTimestamp(index++, now);
			} 
			if(!StringUtil.isBlank(name)){
				stmt2.setString(index, "%"+name+"%");
				stmt.setString(index++, "%"+name+"%");
			}
			
			if(level != Task.LEVEL_NULL){
				stmt2.setInt(index, level);
				stmt.setInt(index++, level);
			}
			
			if(!StringUtil.isBlank(executerId)){
				stmt2.setString(index, executerId);
				stmt.setString(index++, executerId);
			}
			if(!StringUtil.isBlank(createrId)){
				stmt2.setString(index, createrId);
				stmt.setString(index++, createrId);
			}
			
			if(!StringUtil.isBlank(tag)){
				stmt2.setString(index, "%"+tag+"%");
				stmt.setString(index++, "%"+tag+"%");
			}
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Task task = new Task();
				setProperties(task, rs);
				if(!StringUtil.isBlank(rs.getString("IS_FOLLOW"))){
					task.setHasFollow(true);
				}
				tasks.add(task);
			}
			rs.close();
			dp.setDatas(tasks);
			ResultSet rs2 = stmt2.executeQuery();
			if(rs2.next()){
				dp.setRowCount(rs2.getInt(1));
			}
			rs2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeStatement(stmt2);
		}
		
		return dp;
	}
	
	
	
	/**
	 * 生成限制条件sql.
	 * 
	 * @param sql
	 *            sql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @param taskOrderConditionSql
	 *            从AbstractTaskDAO中获取排序条件
	 *            
	 * @return 生成限制条件sql语句字符串
	 * @throws SQLException
	 */
	public abstract String buildLimitString(String sql, int page, int lines,
			LinkedHashMap<String, String> taskOrderConditionMap) throws SQLException; 

	@Deprecated
	public Collection<Task> queryByProject2(String projectId,String name,int status,String currDate,String dateRangeType,int page,int lines,WebUser user) throws Exception {
		
		Collection<Task> tasks = new ArrayList<Task>();
		PreparedStatement stmt = null;
		
		String sql = "SELECT t.*,f.USER_ID as IS_FOLLOW FROM "
				+ getFullTableName(tableName) + " t LEFT JOIN "
				+getFullTableName("PM_TASK_FOLLOWER_SET")+" f ON t.ID=f.TASK_ID AND f.USER_ID=? WHERE t.PROJECT_ID=?";
		
		if(status !=Task.STATUS_NULL){
			sql+=" AND t.STATUS=?";
		}
		
		//sql+=" AND ((t.START_DATE<=? AND t.END_DATE>=?) OR (t.START_DATE>=? AND t.START_DATE<?))";
		
		if(!StringUtil.isBlank(name)){
			sql+=" AND t.NAME like '%?%'";
		}
		
		sql = buildLimitString(sql, page, lines,"CREATE_DATE","DESC");
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			int index =1;
			stmt.setString(index++, user.getId());
			stmt.setString(index++, projectId);
			if(status !=Task.STATUS_NULL){
				stmt.setInt(index++,status);
			}
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeEnd(currDate, dateRangeType));
			
			if(!StringUtil.isBlank(name)){
				stmt.setString(index++, name);
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Task task = new Task();
				setProperties(task, rs);
				if(!StringUtil.isBlank(rs.getString("IS_FOLLOW"))){
					task.setHasFollow(true);
				}
				tasks.add(task);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return tasks;
	}
	
	public Collection<Task> queryTasksByTag(String tagName,String name,int status,String currDate,String dateRangeType,int page,int lines,WebUser user) throws Exception{
		Collection<Task> tasks = new ArrayList<Task>();
		PreparedStatement stmt = null;
		
		String sql = "SELECT t.*,CASE t.STATUS  WHEN 0 THEN '0'  WHEN 1 THEN '3' WHEN 2 THEN '1' WHEN 3 THEN '2' WHEN -1 THEN '4' ELSE '9' END  AS statusOrder ,f.USER_ID as IS_FOLLOW FROM "
				+ getFullTableName(tableName) + " t LEFT JOIN "
				+getFullTableName("PM_TASK_FOLLOWER_SET")+" f ON t.ID=f.TASK_ID AND f.USER_ID=? WHERE t.TAGS like '%"+tagName+"%' and (t.CREATOR_ID=? or t.EXECUTER_ID=?) ";
		
		if(status !=Task.STATUS_NULL){
			sql+=" AND t.STATUS=?";
		}
		
		//sql+=" AND ((t.START_DATE<=? AND t.END_DATE>=?) OR (t.START_DATE>=? AND t.START_DATE<?))";
		
		if(!StringUtil.isBlank(name)){
			sql+=" AND t.NAME like '%?%'";
		}
		
		sql = buildLimitString(sql, page, lines,TASK_ORDER_CONDITION_MAP);
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			int index =1;
			stmt.setString(index++, user.getId());
			stmt.setString(index++, user.getId());
			stmt.setString(index++, user.getId());
			if(status !=Task.STATUS_NULL){
				stmt.setInt(index++,status);
			}
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeEnd(currDate, dateRangeType));
			
			if(!StringUtil.isBlank(name)){
				stmt.setString(index++, name);
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Task task = new Task();
				setProperties(task, rs);
				if(!StringUtil.isBlank(rs.getString("IS_FOLLOW"))){
					task.setHasFollow(true);
				}
				tasks.add(task);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return tasks;
	}
	
	public Collection<Task> queryMyFollowTasks(String name,int status,String currDate,String dateRangeType,int page, int lines,WebUser user) throws Exception {
		
		Collection<Task> tasks = new ArrayList<Task>();
		PreparedStatement stmt = null;
		
		String sql = "SELECT t.*,CASE t.STATUS  WHEN 0 THEN '0'  WHEN 1 THEN '3' WHEN 2 THEN '1' WHEN 3 THEN '2' WHEN -1 THEN '4' ELSE '9' END  AS statusOrder  FROM "
				+ getFullTableName(tableName) + " t, "
				+ getFullTableName("PM_TASK_FOLLOWER_SET")+" f WHERE t.ID=f.TASK_ID AND f.USER_ID=? or (t.CREATOR_ID=? and t.EXECUTER_ID!=?)";
		
		if(status !=Task.STATUS_NULL){
			sql+=" AND t.STATUS=?";
		}
		
		//sql+=" AND ((t.START_DATE<=? AND t.END_DATE>=?) OR (t.START_DATE>=? AND t.START_DATE<?))";
		
		if(!StringUtil.isBlank(name)){
			sql+=" AND t.NAME like '%?%'";
		}
		sql = buildLimitString(sql, page, lines,TASK_ORDER_CONDITION_MAP);
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			int index =1;
			stmt.setString(index++, user.getId());
			stmt.setString(index++, user.getId());
			stmt.setString(index++, user.getId());
			if(status !=Task.STATUS_NULL){
				stmt.setInt(index++,status);
			}
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeEnd(currDate, dateRangeType));
			
			if(!StringUtil.isBlank(name)){
				stmt.setString(index++, name);
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Task task = new Task();
				setProperties(task, rs);
				tasks.add(task);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return tasks;
	}
	
public Collection<Task> queryMyEntrustTasks(String name,int status,String currDate,String dateRangeType,int page, int lines,WebUser user) throws Exception {
		
		Collection<Task> tasks = new ArrayList<Task>();
		PreparedStatement stmt = null;
		
		String sql = "SELECT t.* ,CASE t.STATUS  WHEN 0 THEN '0'  WHEN 1 THEN '3' WHEN 2 THEN '1' WHEN 3 THEN '2' WHEN -1 THEN '4' ELSE '9' END  AS statusOrder  FROM "
				+ getFullTableName(tableName) + " t WHERE t.CREATOR_ID=? AND t.EXECUTER_ID !=?";
		
		if(status != Task.STATUS_NULL){    
			if(status != Task.STATUS_ON){
				sql+=" AND t.STATUS=?";
			}else{
				sql+=" AND t.STATUS IN (?,?,?)";
			}
			
		}
		
		//sql+=" AND ((t.START_DATE<=? AND t.END_DATE>=?) OR (t.START_DATE>=? AND t.START_DATE<?))";
		
		if(!StringUtil.isBlank(name)){
			sql+=" AND t.NAME like '%?%'";
		}
		
		sql = buildLimitString(sql, page, lines,TASK_ORDER_CONDITION_MAP);
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			int index =1;
			stmt.setString(index++, user.getId());
			stmt.setString(index++, user.getId());
			if(status != Task.STATUS_NULL){    
				
				if(status != Task.STATUS_ON){
					stmt.setInt(index++,status);
				}else{
					stmt.setInt(index++,Task.STATUS_NEW);
					stmt.setInt(index++,Task.STATUS_IN_PROGRESS);
					stmt.setInt(index++,Task.STATUS_RESOLVED);
				}
				
			}
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			//stmt.setTimestamp(index++, getDateRangeEnd(currDate, dateRangeType));
			
			if(!StringUtil.isBlank(name)){
				stmt.setString(index, name);
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Task task = new Task();
				setProperties(task, rs);
				tasks.add(task);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return tasks;
	}
 	
	void setProperties(Task task, ResultSet rs) throws Exception {
		try {
			task.setId(rs.getString("ID"));
			task.setName(rs.getString("NAME"));
			task.setDescription(rs.getString("DESCRIPTION"));
			task.setLevel(rs.getInt("LEVELS"));
			task.setStatus(rs.getInt("STATUS"));
			task.setCreator(rs.getString("CREATOR"));
			task.setCreatorId(rs.getString("CREATOR_ID"));
			task.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			task.setStartDate(rs.getTimestamp("START_DATE"));
			task.setEndDate(rs.getTimestamp("END_DATE"));
			task.setRemindMode(rs.getInt("REMIND_MODE"));
			task.setTags(rs.getString("TAGS"));
			task.setExecutor(rs.getString("EXECUTER"));
			task.setExecutorId(rs.getString("EXECUTER_ID"));
			task.setSubTasks(rs.getString("SUB_TASKS"));
			task.setRemark(rs.getString("REMARK"));
			task.setLogs(rs.getString("LOGS"));
			task.setProjectId(rs.getString("PROJECT_ID"));
			task.setProjectName(rs.getString("PROJECT_NAME"));
			task.setDomainid(rs.getString("DOMAIN_ID"));
			task.setAttachment(rs.getString("ATTACHMENT"));
		} catch (SQLException e) {
			throw e;
		}

	}
	
	protected Timestamp getDateRangeBegin(String currDate,String dateRangeType) {
		
		    if (dateRangeType != null) {
			Calendar cld = Calendar.getInstance();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date cd = format.parse(currDate);
				cld.setTime(cd);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if (dateRangeType.equalsIgnoreCase("TODAY")) {
				
			}else if (dateRangeType.equalsIgnoreCase("LASTMONTH")) {
				if(cld.get(Calendar.MONTH)==0){
					cld.roll(Calendar.YEAR, -1);
				}
				cld.roll(Calendar.MONTH, -1);
				cld.set(Calendar.DAY_OF_MONTH, 1);
				
			}else if (dateRangeType.equalsIgnoreCase("LASTWEEK")){
				cld.add(Calendar.DATE, -1*7);
				cld.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
				
			}else if( dateRangeType.equalsIgnoreCase("lastThreeday")){
				 int days = cld.get(Calendar.DAY_OF_YEAR) - 3;
			     cld.set(Calendar.DAY_OF_YEAR, days);
			     
			}else if( dateRangeType.equalsIgnoreCase("nextThreeday")){	
				 int days = cld.get(Calendar.DAY_OF_YEAR) + 1;
			     cld.set(Calendar.DAY_OF_YEAR, days);
			}else if (dateRangeType.equalsIgnoreCase("THISWEEK")) {
				cld.set(Calendar.DAY_OF_WEEK, 1);
				
			}else if (dateRangeType.equalsIgnoreCase("THISMONTH")) {
				cld.set(Calendar.DAY_OF_MONTH, 1);
				
			}else if(dateRangeType.equalsIgnoreCase("NEXTWEEK")){
				cld.add(Calendar.DATE, 1*7);
				cld.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
				
			}else if(dateRangeType.equalsIgnoreCase("NEXTMONTH")){
				if(cld.get(Calendar.MONTH)==11){
					cld.roll(Calendar.YEAR, 1);
				}
				cld.roll(Calendar.MONTH, 1);
				cld.set(Calendar.DAY_OF_MONTH, 1);
				
			}else if (dateRangeType.equalsIgnoreCase("THISYEAR")) {
				cld.set(Calendar.DAY_OF_YEAR, 1);
			}
			cld.set(Calendar.HOUR_OF_DAY, 0);
			cld.set(Calendar.MINUTE, 0);
			cld.set(Calendar.SECOND, 0);
			cld.set(Calendar.MILLISECOND, 0);
			
			System.out.println(cld.getTime());

			return new Timestamp(cld.getTimeInMillis());
		}
		return null;
	}

	

	protected Timestamp getDateRangeEnd(String currDate,String dateRangeType) {
		
	     
		if (dateRangeType != null) {
			Calendar cld = Calendar.getInstance();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date cd = format.parse(currDate);
				cld.setTime(cd);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (dateRangeType.equalsIgnoreCase("TODAY")) {
				//cld.roll(Calendar.DATE, 1);
			}else if (dateRangeType.equalsIgnoreCase("LASTMONTH")) {
				
				if(cld.get(Calendar.MONTH)==0){
					cld.roll(Calendar.YEAR, -1);
				}
				cld.roll(Calendar.MONTH, -1);
				cld.set(Calendar.DAY_OF_MONTH, cld.getActualMaximum(Calendar.DAY_OF_MONTH));
				
			}else if (dateRangeType.equalsIgnoreCase("LASTWEEK")){
				cld.add(Calendar.DATE, -1*7);
				cld.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
				
			}else if( dateRangeType.equalsIgnoreCase("LASTTHREEDAY")){
				int days = cld.get(Calendar.DAY_OF_YEAR) - 1;
			       cld.set(Calendar.DAY_OF_YEAR, days);
			}else if( dateRangeType.equalsIgnoreCase("nextThreeday")){
				 int days = cld.get(Calendar.DAY_OF_YEAR) + 3;
			       cld.set(Calendar.DAY_OF_YEAR, days);
			       
			} else if (dateRangeType.equalsIgnoreCase("THISWEEK")) {
				cld.roll(Calendar.WEEK_OF_YEAR, 1);
				cld.set(Calendar.DAY_OF_WEEK, 1);
			} else if (dateRangeType.equalsIgnoreCase("THISMONTH")) {
				cld.set(Calendar.DAY_OF_MONTH, cld.getActualMaximum(Calendar.DAY_OF_MONTH));
			}else if(dateRangeType.equalsIgnoreCase("NEXTWEEK")){
				cld.add(Calendar.DATE, 1*7);
				cld.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
				
			}else if(dateRangeType.equalsIgnoreCase("NEXTMONTH")){
				if(cld.get(Calendar.MONTH)==11){
					cld.roll(Calendar.YEAR, 1);
				}
				cld.roll(Calendar.MONTH, 1);
				cld.set(Calendar.DAY_OF_MONTH, cld.getActualMaximum(Calendar.DAY_OF_MONTH));
				
			}else if (dateRangeType.equalsIgnoreCase("THISYEAR")) {
				cld.roll(Calendar.YEAR, 1);
				cld.set(Calendar.DAY_OF_YEAR, 1);
			}

			cld.set(Calendar.HOUR_OF_DAY,23);
			cld.set(Calendar.MINUTE, 59);
			cld.set(Calendar.SECOND, 59);
			cld.set(Calendar.MILLISECOND, 0);
			
			System.out.println(cld.getTime());

			return new Timestamp(cld.getTimeInMillis());
		}
		return null;
	}
	
	/**
	 * 获取上/下周周一和周日的时间
	 * @param n 
	 *          n为推迟的周数，-1为上一星期，1为下一星期
	 * 
	 * @param cld 设置日期
	 */
	protected Calendar getWeekDay(int n,Calendar cld, int CalendarDay) {
		
		cld.add(Calendar.DATE, n*7);
		cld.set(Calendar.DAY_OF_WEEK,CalendarDay);
		return cld;
	}
	/**
	 * 获取上/下月 第一天和最后一天
	 * @param n 
	 *          n为推迟月数，-1为上一月，1为下一月
	 * 
	 * @param cld 设置日期
	 */
	protected Calendar getMonthDay(int n,Calendar cld , String flag) {
		
		int Month = cld.get(Calendar.MONTH) + n;
		cld.set(Calendar.MONTH,Month);
		cld.set(Calendar.DAY_OF_MONTH, cld.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return cld;
	}
	/**
	 * 获取前后N天时间
	 * @param n 
	 *          n为推迟日数，-1为上一天，1为下一天
	 * 
	 * @param cld 设置日期
	 */
   protected Calendar getNextDay(Calendar cld , int day) {
		
	   int days = cld.get(Calendar.DAY_OF_YEAR) + day;
       cld.set(Calendar.DAY_OF_YEAR, days);
	   return cld;
	}
	
	/**
	 * 查询任务的关注人集合
	 * @param taskId
	 * 		任务主键
	 * @return
	 * @throws Exception
	 */
	public Collection<Follower> queryFollowersByTask(String taskId) throws Exception {
		Collection<Follower> rtn = new ArrayList<Follower>();
		
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
			+ getFullTableName("PM_TASK_FOLLOWER_SET") +" WHERE TASK_ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, taskId);
			ResultSet rs = stmt.executeQuery();
			Follower follower = null;
			while(rs.next()){
				follower = new Follower();
				setFollowerProperties(follower, rs);
				rtn.add(follower);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rtn;
	}
	
	void setFollowerProperties(Follower follower, ResultSet rs) throws Exception {
		try {
			follower.setUserName(rs.getString("USER_NAME"));
			follower.setUserId(rs.getString("USER_ID"));
		} catch (SQLException e) {
			throw e;
		}

	}
	
	public void addAttachment(String id,String attachjson) throws Exception{
		PreparedStatement stmt1 = null;
		String attachString = null;
		
		String sql1 = "SELECT * FROM "
			+ getFullTableName(tableName)
			+ " WHERE ID=?";
		try {
			stmt1 = connection.prepareStatement(sql1);
			stmt1.setString(1, id);
		
			ResultSet rs = stmt1.executeQuery();
			while(rs.next()){
				attachString = rs.getString("attachment");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			ConnectionManager.closeStatement(stmt1);
		}
		
		if(attachString==null||attachString.equals(""))
			attachString = "{}";
		Map<String, Object> map = JsonUtil.toMap(attachString);
		Map<String, Object> map1 = JsonUtil.toMap(attachjson);
		map.putAll(map1);
		String string = JsonUtil.toJson(map);
		
		PreparedStatement stmt = null;
		String sql = "UPDATE "
				+ getFullTableName(tableName)
				+ " SET ATTACHMENT=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, string);
			stmt.setString(2, id);
		
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}
	
	public Task deleteAttachment(String id,String key) throws Exception{
		PreparedStatement stmt = null;
		String attachString = null;
		Task task=new Task();
		
		String sql1 = "SELECT * FROM "
			+ getFullTableName(tableName)
			+ " WHERE ID=?";
		try {
			stmt = connection.prepareStatement(sql1);
			stmt.setString(1, id);
		
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				setProperties(task, rs);
				attachString = rs.getString("attachment");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		//删除附件文件操作
		
		Map<String, Object> map = JsonUtil.toMap(attachString);
		map.remove(key);
		String string = JsonUtil.toJson(map);
		String sql = "UPDATE "
				+ getFullTableName(tableName)
				+ " SET ATTACHMENT=? WHERE ID=?";
		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, string);
			stmt.setString(2, id);
		
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return task;
	}
	
	/**
	 * 
	 * @param projectId
	 * 		项目主键
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		任务完成状态
	 * @param level
	 *      优先级
	 * @param executor
	 *      执行人名称
	 * @param currDate
	 * 		当前日期
	 * @param dateRangeType
	 * 		日期范围
	 * @param page
	 * 		当前页码
	 * @param lines
	 * 		单页数据条数	 
	 * @param overdueStatus 
	 * 		过期状态（"overdue"为选定已过期状态）
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Task> queryTasks(String projectId, String taskName,Integer status, Integer level, String executerId, String createrId,String currDate, String dateRangeType, String tag,String orderName, String orderBy, Integer page, Integer lines,String overdueStatus, WebUser user)throws Exception{
		
		DataPackage<Task> dp = new DataPackage<Task>();
		dp.setPageNo(page);
		dp.setLinesPerPage(lines);
		
		Collection<Task> tasks = new ArrayList<Task>();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		
		String sql = "SELECT t.*,CASE t.STATUS  WHEN 0 THEN '0'  WHEN 1 THEN '3' WHEN 2 THEN '1' WHEN 3 THEN '2' WHEN -1 THEN '4' ELSE '9' END  AS statusOrder ,f.USER_ID as IS_FOLLOW FROM "
				+ getFullTableName(tableName) + " t LEFT JOIN "
				+ getFullTableName("PM_TASK_FOLLOWER_SET")+" f ON t.ID=f.TASK_ID AND f.USER_ID=? WHERE 1=1 ";
		
		String countSql = "SELECT COUNT(*) FROM "
				+ getFullTableName(tableName) + " t LEFT JOIN "
				+getFullTableName("PM_TASK_FOLLOWER_SET")+" f ON t.ID=f.TASK_ID AND f.USER_ID=? WHERE 1=1 ";
		
		if(!StringUtil.isBlank(projectId) && projectId != ""){
			sql+=" AND t.PROJECT_ID=?";
			countSql+=" AND t.PROJECT_ID=?";
		}
		
		if(status != null){
			sql+=" AND t.STATUS=?";
			countSql+=" AND t.STATUS=?";
		}
		
		
		if(!StringUtil.isBlank(dateRangeType)){
			
		sql+=" AND ((t.START_DATE  >= ? AND t.START_DATE <= ?) OR (t.END_DATE >= ? AND t.END_DATE <= ? ))";
		countSql+=" AND ((t.START_DATE  >= ? AND t.START_DATE <= ?) OR (t.END_DATE >= ? AND t.END_DATE <= ? ))";
		}
		
		if (Task.OVERDUE_STATUS_OVERDUE.equalsIgnoreCase(overdueStatus)) {
			sql += " AND t.END_DATE IS NOT NULL AND t.END_DATE<? AND STATUS!=1 AND STATUS!=-1";
			countSql += " AND t.END_DATE IS NOT NULL AND t.END_DATE<? AND STATUS!=1 AND STATUS!=-1";
		} 
		
		if(!StringUtil.isBlank(taskName)){
			sql+=" AND t.NAME like ?";
			countSql+=" AND t.NAME like ?";
		}
		
		if(level != null){   
			sql+=" AND t.LEVELS=?";
			countSql+=" AND t.LEVELS=?";
		}
		
		if(!StringUtil.isBlank(executerId)){
			sql+=" AND t.EXECUTER_ID = ?";
			countSql+=" AND t.EXECUTER_ID = ?";
		}
		
		if(!StringUtil.isBlank(createrId)){
			sql+=" AND t.CREATOR_ID = ?";
			countSql+=" AND t.CREATOR_ID = ?";
		}
	
		if(!StringUtil.isBlank(tag)){
			sql+=" AND t.tags like ?";
			countSql+=" AND t.tags like ?";
		}
		
		LinkedHashMap<String,String> orderMap = new LinkedHashMap<String,String>();
		
		
		if(StringUtil.isBlank(orderName)){
			orderMap = TASK_ORDER_CONDITION_MAP;
		}else{
			orderMap.put(orderName, orderBy);
		}
		sql = buildLimitString(sql, page, lines,orderMap);
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt2 = connection.prepareStatement(countSql);
			int index =1;
			stmt2.setString(index, user.getId());
			stmt.setString(index++, user.getId());
			
			if(!StringUtil.isBlank(projectId) && projectId != ""){
				stmt2.setString(index, projectId);
				stmt.setString(index++, projectId);
			}
			
			if(status != null){
				stmt2.setInt(index,status);
				stmt.setInt(index++,status);
			}
			
			
			if(!StringUtil.isBlank(dateRangeType)){
				stmt.setTimestamp(index, getDateRangeBegin(currDate, dateRangeType));
				stmt2.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			    stmt.setTimestamp(index, getDateRangeEnd(currDate, dateRangeType));
			    stmt2.setTimestamp(index++, getDateRangeEnd(currDate, dateRangeType));
			    stmt.setTimestamp(index, getDateRangeBegin(currDate, dateRangeType));
			    stmt2.setTimestamp(index++, getDateRangeBegin(currDate, dateRangeType));
			    stmt.setTimestamp(index, getDateRangeEnd(currDate, dateRangeType));
			    stmt2.setTimestamp(index++, getDateRangeEnd(currDate, dateRangeType));
			    
			}
			if (Task.OVERDUE_STATUS_OVERDUE.equalsIgnoreCase(overdueStatus)) {
				Date date = DateUtil.parseDate(DateUtil.getCurDateStr("yyyy-MM-dd"));
				Timestamp now = new Timestamp(date.getTime());
				stmt.setTimestamp(index, now);
				stmt2.setTimestamp(index++, now);
			} 
			if(!StringUtil.isBlank(taskName)){
				stmt2.setString(index, "%"+taskName+"%");
				stmt.setString(index++, "%"+taskName+"%");
			}
			
			if(level != null){
				stmt2.setInt(index, level);
				stmt.setInt(index++, level);
			}
			
			if(!StringUtil.isBlank(executerId)){
				stmt2.setString(index, executerId);
				stmt.setString(index++, executerId);
			}
			if(!StringUtil.isBlank(createrId)){
				stmt2.setString(index, createrId);
				stmt.setString(index++, createrId);
			}
			
			if(!StringUtil.isBlank(tag)){
				stmt2.setString(index, "%"+tag+"%");
				stmt.setString(index++, "%"+tag+"%");
			}
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Task task = new Task();
				setProperties(task, rs);
				if(!StringUtil.isBlank(rs.getString("IS_FOLLOW"))){
					task.setHasFollow(true);
				}
				tasks.add(task);
			}
			rs.close();
			dp.setDatas(tasks);
			ResultSet rs2 = stmt2.executeQuery();
			if(rs2.next()){
				dp.setRowCount(rs2.getInt(1));
			}
			rs2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeStatement(stmt2);
		}
		
		return dp;
	}
}
