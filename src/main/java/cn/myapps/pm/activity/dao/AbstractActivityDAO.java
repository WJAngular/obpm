package cn.myapps.pm.activity.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.activity.ejb.Activity;
import cn.myapps.pm.base.dao.AbstractBaseDAO;
import cn.myapps.pm.task.ejb.Task;
import cn.myapps.pm.util.ConnectionManager;
import cn.myapps.util.StringUtil;

public abstract class AbstractActivityDAO extends AbstractBaseDAO {

	private static final Logger log = Logger
			.getLogger(AbstractActivityDAO.class);

	public AbstractActivityDAO(Connection conn) throws Exception {
		super(conn);
		this.tableName = "PM_ACTIVITY";
	}

	public ValueObject create(ValueObject vo) throws Exception {
		Activity activity = (Activity) vo;

		PreparedStatement stmt = null;

		String sql = "INSERT INTO " + getFullTableName(tableName)
				+ " (ID,TASK_ID,USER_ID,USER_NAME,OPERATION_TYPE,OPERATION_DATE,DOMAIN_ID) values (?,?,?,?,?,?,?)";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, activity.getId());
			stmt.setString(2, activity.getTaskId());
			stmt.setString(3, activity.getUserId());
			stmt.setString(4, activity.getUserName());
			stmt.setInt(5,activity.getOperationType());
			if (activity.getOperationDate() == null) {
				stmt.setNull(6, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(activity.getOperationDate().getTime());
				stmt.setTimestamp(6, ts);
			}
			stmt.setString(7, activity.getDomainid());
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

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			Activity activity = null;
			if (rs.next()) {
				activity = new Activity();
				setProperties(activity, rs);
			}
			rs.close();
			return activity;
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

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			log.debug(sql);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}

	}

	public ValueObject update(ValueObject vo) throws Exception {
		Activity activity = (Activity) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE " + getFullTableName(tableName)
				+ " SET TASK_ID=?,USER_ID=?,USER_NAME=?,OPERATION_TYPE=?,OPERATION_DATE=?,DOMAIN_ID=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, activity.getTaskId());
			stmt.setString(2, activity.getUserId());
			stmt.setString(3, activity.getUserName());
			stmt.setInt(4,activity.getOperationType());
			if (activity.getOperationDate() == null) {
				stmt.setNull(5, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(activity.getOperationDate().getTime());
				stmt.setTimestamp(5, ts);
			}
			stmt.setString(6, activity.getDomainid());
			stmt.setString(7, activity.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;
	}

	public Collection<?> simpleQuery(ParamsTable params, WebUser user)
			throws Exception {
				return null;
	}
	
	public Collection<Activity> query(ParamsTable params, WebUser user)throws Exception {
		Collection<Activity> activitys = new ArrayList<Activity>();
		
		String range = params.getParameterAsString("range");
		int page = 1;
		int lines = 50;
		
		String sql = "";
		if("all".equalsIgnoreCase(range)){
			sql = "SELECT a.*,t.NAME FROM "
				+ getFullTableName(tableName)+" a,"
				+ getFullTableName("PM_TASK")+" t "+
				" WHERE a.TASK_ID=t.ID AND (t.EXECUTER_ID='"+user.getId()+"' OR t.ID in" +
				"(SELECT TASK_ID FROM "+getFullTableName("PM_TASK_FOLLOWER_SET")+" WHERE USER_ID='"+user.getId()+"'))";
		}else if("my".equalsIgnoreCase(range)){
			sql = "SELECT a.*,t.NAME FROM "
				+ getFullTableName(tableName)+" a,"
				+ getFullTableName("PM_TASK")+" t "+
				" WHERE a.TASK_ID=t.ID AND t.EXECUTER_ID='"+user.getId()+"'";
		}else if("follow".equalsIgnoreCase(range)){
			sql = "SELECT a.*,t.NAME FROM "
				+ getFullTableName(tableName)+" a,"
				+ getFullTableName("PM_TASK")+" t,"+getFullTableName("PM_TASK_FOLLOWER_SET")+" f "+
				" WHERE a.TASK_ID=t.ID AND f.TASK_ID=t.id AND f.USER_ID='"+user.getId()+"'";
		}
		
		
		sql = buildLimitString(sql, page, lines,"OPERATION_DATE","DESC");
		
		log.debug(sql);
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Activity activity = new Activity();
				setProperties(activity, rs);
				if(!StringUtil.isBlank(rs.getString("NAME"))){
					activity.setTaskName(rs.getString("NAME"));
				}
				activitys.add(activity);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		
		return activitys;
	}
	
	
	public void removeByTask(String taskId) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName(tableName)
				+ " WHERE TASK_ID=?";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, taskId);
			log.debug(sql);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	void setProperties(Activity activity, ResultSet rs) throws Exception {
		try {
			activity.setId(rs.getString("ID"));
			activity.setTaskId(rs.getString("TASK_ID"));
			activity.setUserId(rs.getString("USER_ID"));
			activity.setUserName(rs.getString("USER_NAME"));
			activity.setOperationType(rs.getInt("OPERATION_TYPE"));
			activity.setOperationDate(rs.getTimestamp("OPERATION_DATE"));
			activity.setDomainid(rs.getString("DOMAIN_ID"));
		} catch (SQLException e) {
			throw e;
		}

	}

}
