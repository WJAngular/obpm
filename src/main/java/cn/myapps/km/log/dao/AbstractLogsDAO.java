package cn.myapps.km.log.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.log.ejb.Logs;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

/**
 * @author Happy
 * 
 */
public abstract class AbstractLogsDAO {

	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractLogsDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		Logs logs = (Logs) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("KM_LOGS")
				+ " (ID,OPERATION_TYPE,FILE_ID,FILE_NAME,USER_ID,USER_NAME,OPERATIONDATE,USER_IP,OPERATION_CONTENT,DEPARTMENT_ID,DEPARTMENT_NAME,OPERATION_FILE_OR_DIRECTORY) values (?,?,?,?,?,?,?,?,?,?,?,?)";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, logs.getId());
			stmt.setString(2, logs.getOperationType());
			stmt.setString(3, logs.getFileId());
			stmt.setString(4, logs.getFileName());
			stmt.setString(5, logs.getUserId());
			stmt.setString(6, logs.getUserName());
			if (logs.getOperationDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(logs.getOperationDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			stmt.setString(8, logs.getUserIp());
			stmt.setString(9, logs.getOperationContent());
			stmt.setString(10, logs.getDepartmentId());
			stmt.setString(11, logs.getDepartmentName());
			stmt.setInt(12, logs.getOperationFileOrDirectory());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(NObject vo) throws Exception {
		Logs logs = (Logs) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("KM_LOGS")
				+ " SET ID=?,OPERATION_TYPE=? ,FILE_ID=?,FILE_NAME=?,USER_ID=?,USER_NAME=?,OPERATIONDATE=?,USER_IP=?,OPERATION_CONTENT=?,DEPARTMENT_ID=?,DEPARTMENT_NAME=? WHERE id=?";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, logs.getId());
			stmt.setString(2, logs.getOperationType());
			stmt.setString(3, logs.getFileId());
			stmt.setString(4, logs.getFileName());
			stmt.setString(5, logs.getUserId());
			stmt.setString(6, logs.getUserName());
			if (logs.getOperationDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(logs.getOperationDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			stmt.setString(8, logs.getUserIp());
			stmt.setString(9, logs.getOperationContent());
			stmt.setString(10, logs.getDepartmentId());
			stmt.setString(11, logs.getDepartmentName());
			stmt.setString(12, logs.getId());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}

	}

	public Logs find(String id) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM " + getFullTableName("KM_LOGS")
				+ " WHERE ID=?";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			rs = stmt.executeQuery();

			if (rs.next()) {
				Logs logs = this.setLogsProperty(rs);
				rs.close();
				return logs;
			}

			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		String sql = "DELETE FROM " + getFullTableName("KM_LOGS")
				+ " WHERE ID = ?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public DataPackage<Logs> view(int page, int lines, String userid,
			String operationtype, String filename) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DataPackage<Logs> datas = new DataPackage<Logs>();
		Collection<Logs> Logss = new ArrayList<Logs>();
		String sql = "select  lf.FILE_NAME, lf.creator, lf.OPERATION_CONTENT, lf.OPERATIONDATE  ,  lf.views, lf.downloads, lf.favorites  from  (select  l.FILE_NAME, f.creator, l.OPERATION_CONTENT, l.OPERATIONDATE  ,  f.views, f.downloads, f.favorites ,l.USER_ID ,l.OPERATION_TYPE FROM "
				+ getFullTableName(" KM_LOGS")
				+ "  l  JOIN "
				+ getFullTableName("KM_NFILE")
				+ "  f   on  l.FILE_ID=f.ID) lf where lf.USER_ID=?";

		// 拼接MySql的分页语句
		String querySql = buildLimitString(sql, page, lines);

		log.info(querySql);
		try {
			stmt = connection.prepareStatement(querySql);
			stmt.setString(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Logs logs = new Logs();
				logs.setFileName(rs.getString("file_Name"));
				logs.setOperationContent(rs.getString("operation_Content"));
				logs.setOperationDate(rs.getTimestamp("operationDate"));
				logs.setCreator(rs.getString("creator"));
				logs.setViews(rs.getInt("views"));
				logs.setDownloads(rs.getInt("downloads"));
				logs.setFavorites(rs.getInt("favorites"));
				Logss.add(logs);
			}
			datas.datas = Logss;
			datas.linesPerPage = lines;
			datas.rowCount = (int) countBySQL(sql, filename, operationtype,
					userid);
			datas.pageNo = page;
			return datas;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			PersistenceUtils.closeStatement(stmt);
		}

	}

	public DataPackage<Logs> query(int page, int lines, String userid,
			String operationtype, String filename) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DataPackage<Logs> datas = new DataPackage<Logs>();
		Collection<Logs> Logss = new ArrayList<Logs>();
		String sql = "select  lf.FILE_NAME, lf.creator, lf.OPERATION_CONTENT, lf.OPERATIONDATE  ,  lf.views, lf.downloads, lf.favorites  from  (select  l.FILE_NAME, f.creator, l.OPERATION_CONTENT, l.OPERATIONDATE  ,  f.views, f.downloads, f.favorites ,l.USER_ID ,l.OPERATION_TYPE FROM "
				+ getFullTableName(" KM_LOGS")
				+ "  l  JOIN "
				+ getFullTableName("KM_NFILE")
				+ "  f   on  l.FILE_ID=f.ID) lf where lf.OPERATION_TYPE= ?   AND  lf.USER_ID=?";

		// 拼接MySql的分页语句
		String querySql = buildLimitString(sql, page, lines);

		log.info(querySql);
		try {
			stmt = connection.prepareStatement(querySql);
			stmt.setString(1, operationtype);
			stmt.setString(2, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Logs logs = new Logs();
				logs.setFileName(rs.getString("file_Name"));
				logs.setOperationContent(rs.getString("operation_Content"));
				logs.setOperationDate(rs.getTimestamp("operationDate"));
				logs.setCreator(rs.getString("creator"));
				logs.setViews(rs.getInt("views"));
				logs.setDownloads(rs.getInt("downloads"));
				logs.setFavorites(rs.getInt("favorites"));
				Logss.add(logs);
			}
			datas.datas = Logss;
			datas.linesPerPage = lines;
			datas.rowCount = (int) countBySQL(sql, filename, operationtype,
					userid);
			datas.pageNo = page;
			return datas;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			PersistenceUtils.closeStatement(stmt);
		}

	}

	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}

	private Logs setLogsProperty(ResultSet rs) throws Exception {
		Logs logs = new Logs();
		try {
			logs.setId(rs.getString("id"));
			logs.setOperationType(rs.getString("operation_type"));
			logs.setFileId(rs.getString("file_id"));
			logs.setFileName(rs.getString("file_name"));
			logs.setUserId(rs.getString("user_id"));
			logs.setUserName(rs.getString("user_name"));
			logs.setOperationDate(rs.getDate("operationdate"));
			logs.setUserIp(rs.getString("user_ip"));
			logs.setOperationContent(rs.getString("operation_content"));
			logs.setDepartmentId(rs.getString("department_id"));
			logs.setDepartmentName(rs.getString("department_name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logs;
	}

	public long getTypeCountByFile(String operationType, String fileId)
			throws Exception {
		String sql = "";
		PreparedStatement statement = null;
		ResultSet rs = null;

		sql = "SELECT COUNT(*)  FROM " + getFullTableName("KM_LOGS")
				+ " WHERE FILE_ID=? and OPERATION_TYPE=? ";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, fileId);
			statement.setString(2, operationType);
			rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
		return 0;
	}

	public long getTypeCountByUser(String operationType, String userId)
			throws Exception {
		String sql = "";
		PreparedStatement statement = null;
		ResultSet rs = null;

		sql = "SELECT COUNT(*)  FROM " + getFullTableName("KM_LOGS")
				+ " WHERE USER_ID=? and OPERATION_TYPE=? ";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setString(2, operationType);
			rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
		return 0;
	}

	/**
	 * 分页查找用户操作文件日记
	 * 
	 * @param page
	 * @param lines
	 * @param userid
	 * @param operationtype
	 * @param fileid
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Logs> queryByFile(int page, int lines, String userid,
			String operationtype, String filename) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer buf = new StringBuffer();
		String file = buf.append("%").append(filename).append("%.%").toString();
		DataPackage<Logs> datas = new DataPackage<Logs>();
		Collection<Logs> Logss = new ArrayList<Logs>();
		String sql = "select  lf.FILE_NAME, lf.creator, lf.OPERATION_CONTENT, lf.OPERATIONDATE  ,  lf.views, lf.downloads, lf.favorites  from  (select  l.FILE_NAME, f.creator, l.OPERATION_CONTENT, l.OPERATIONDATE  ,  f.views, f.downloads, f.favorites ,l.USER_ID ,l.OPERATION_TYPE FROM "
				+ getFullTableName(" KM_LOGS")
				+ "  l  JOIN "
				+ getFullTableName("KM_NFILE")
				+ "  f   on  l.FILE_ID=f.ID) lf where  lf.FILE_NAME LIKE ?   AND  lf.OPERATION_TYPE= ?  AND  lf.USER_ID=?";

		// 拼接MySql的分页语句
		String querySql = buildLimitString(sql, page, lines);

		log.info(querySql);
		try {
			stmt = connection.prepareStatement(querySql);
			stmt.setString(1, file);
			stmt.setString(2, operationtype);
			stmt.setString(3, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Logs logs = new Logs();
				logs.setFileName(rs.getString("file_Name"));
				logs.setOperationContent(rs.getString("operation_Content"));
				logs.setOperationDate(rs.getTimestamp("operationDate"));
				logs.setCreator(rs.getString("creator"));
				logs.setViews(rs.getInt("views"));
				logs.setDownloads(rs.getInt("downloads"));
				logs.setFavorites(rs.getInt("favorites"));
				Logss.add(logs);
			}
			datas.datas = Logss;
			datas.linesPerPage = lines;
			datas.rowCount = (int) countBySQL(sql, file, operationtype, userid);
			datas.pageNo = page;
			return datas;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			PersistenceUtils.closeStatement(stmt);
		}

	}

	public DataPackage<Logs> viewByFile(int page, int lines, String userid,
			String operationtype, String filename) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer buf = new StringBuffer();
		String file = buf.append("%").append(filename).append("%.%").toString();
		DataPackage<Logs> datas = new DataPackage<Logs>();
		Collection<Logs> Logss = new ArrayList<Logs>();
		String sql = "select  lf.FILE_NAME, lf.creator, lf.OPERATION_CONTENT, lf.OPERATIONDATE  ,  lf.views, lf.downloads, lf.favorites  from  (select  l.FILE_NAME, f.creator, l.OPERATION_CONTENT, l.OPERATIONDATE  ,  f.views, f.downloads, f.favorites ,l.USER_ID ,l.OPERATION_TYPE FROM "
				+ getFullTableName(" KM_LOGS")
				+ "  l  JOIN "
				+ getFullTableName("KM_NFILE")
				+ "  f   on  l.FILE_ID=f.ID) lf where  lf.FILE_NAME LIKE ? AND  lf.USER_ID=?";

		// 拼接MySql的分页语句
		String querySql = buildLimitString(sql, page, lines);

		log.info(querySql);
		try {
			stmt = connection.prepareStatement(querySql);
			stmt.setString(1, file);
			stmt.setString(2, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Logs logs = new Logs();
				logs.setFileName(rs.getString("file_Name"));
				logs.setOperationContent(rs.getString("operation_Content"));
				logs.setOperationDate(rs.getTimestamp("operationDate"));
				logs.setCreator(rs.getString("creator"));
				logs.setViews(rs.getInt("views"));
				logs.setDownloads(rs.getInt("downloads"));
				logs.setFavorites(rs.getInt("favorites"));
				Logss.add(logs);
			}
			datas.datas = Logss;
			datas.linesPerPage = lines;
			datas.rowCount = (int) countBySQL(sql, file, operationtype, userid);
			datas.pageNo = page;
			return datas;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			PersistenceUtils.closeStatement(stmt);
		}

	}

	public DataPackage<Logs> managerQuery(int page, int lines,
			ParamsTable params) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer s = new StringBuffer();
		DataPackage<Logs> datas = new DataPackage<Logs>();
		Collection<Logs> Logss = new ArrayList<Logs>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(getFullTableName("KM_LOGS "));
		String operationtype = params.getParameterAsString("operationType");
		if (!StringUtil.isBlank(operationtype)) {
			sql.append(" WHERE operation_type = '").append(operationtype)
					.append("' ");
		}
		// sql.append(" order by OPERATIONDATE desc");
		log.info(sql.toString());
		// 拼接MySql的分页语句
		String querySql = buildLimitString(sql.toString(), page, lines);

		log.info(querySql);
		try {
			stmt = connection.prepareStatement(querySql);
			// stmt.setString(1, file);
			// stmt.setString(2, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Logs logs = new Logs();
				logs.setFileName(rs.getString("file_Name"));
				logs.setOperationContent(rs.getString("operation_Content"));
				logs.setOperationDate(rs.getTimestamp("OPERATIONDATE"));
				logs.setOperationType(rs.getString("operation_type"));
				logs.setUserName(rs.getString("user_Name"));
				logs.setDepartmentName(rs.getString("department_Name"));
				logs.setUserIp(rs.getString("user_Ip"));
				Logss.add(logs);
			}
			datas.datas = Logss;
			datas.linesPerPage = lines;
			datas.rowCount = (int) countBy(sql.toString());
			datas.pageNo = page;
			return datas;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			PersistenceUtils.closeStatement(stmt);
		}

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
	 * @return 生成限制条件sql语句字符串
	 * @throws SQLException
	 */
	public String buildLimitString(String sql, int page, int lines)
			throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect.append("SELECT * FROM (");
		pagingSelect.append(sql).append(" order by OPERATIONDATE desc");
		;
		pagingSelect.append(" )AS TB LIMIT " + to + "," + lines);

		return pagingSelect.toString();
	}

	public long countBySQL(String sql, String filename, String operationtype,
			String userid) throws Exception {
		if (sql == null || sql.trim().equals(""))
			return 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		sql = "SELECT COUNT(*)  FROM (" + sql + ") AS T";

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			if ((filename == null || filename == "")
					&& (operationtype == null || operationtype == "")) {
				statement.setString(1, userid);
			} else if (filename == null || filename == "") {
				statement.setString(1, operationtype);
				statement.setString(2, userid);
			} else if (operationtype == null || operationtype == "") {
				statement.setString(1, filename);
				statement.setString(2, userid);
			} else {
				statement.setString(1, filename);
				statement.setString(2, operationtype);
				statement.setString(3, userid);
			}
			rs = statement.executeQuery();

			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
	}

	public long countBy(String sql) throws Exception {
		if (sql == null || sql.trim().equals(""))
			return 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		sql = "SELECT COUNT(*)  FROM (" + sql + ") AS T";

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
	}
	
	public String querySynchronizeTasks(long lastSynchTime, int diskType,NUser user) throws Exception{
		Collection<Map<String, String>> taskList = new ArrayList<Map<String,String>>();
		try {
			taskList.addAll(queryDirCreateSynchronizeTasks(lastSynchTime, user));
			taskList.addAll(queryDirDeleteSynchronizeTasks(lastSynchTime, diskType, user));
			taskList.addAll(queryFileUploadSynchronizeTasks(lastSynchTime, diskType, user));
			taskList.addAll(queryFileDeleteSynchronizeTasks(lastSynchTime, diskType, user));
			if(!taskList.isEmpty()){
				return JsonUtil.collection2Json(taskList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
		
		
	}

	public Collection<Map<String, String>> queryFileUploadSynchronizeTasks(long lastSynchTime, int diskType,
			NUser user) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<Map<String, String>> taskList = new ArrayList<Map<String,String>>();
		String sql = "SELECT f.ID,f.NAME,f.FILESIZE,f.NDIRID,l.OPERATION_TYPE,l.OPERATIONDATE "
				+ "FROM "
				+ getFullTableName("KM_NFILE")
				+ " f,"
				+ getFullTableName("KM_LOGS ")
				+ " l "
				+ "WHERE f.ID=l.FILE_ID and f.STATE =? and f.OWNERID=? and l.OPERATION_FILE_OR_DIRECTORY="+Logs.OPERATION_FILE+" and l.OPERATIONDATE > ? and "
				+ "l.OPERATION_TYPE='UPLOAD' "
				+ "ORDER BY l.OPERATIONDATE";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, diskType);
			stmt.setString(2, user.getId());
			stmt.setTimestamp(3, new Timestamp(lastSynchTime));
			rs = stmt.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("ID"));
				map.put("name", rs.getString("NAME"));
				map.put("type", "1");
				map.put("size", rs.getString("FILESIZE"));
				map.put("nDirId", rs.getString("NDIRID"));
				map.put("taskType", "DOWNLOAD");
				map.put("operation_date", rs.getString("OPERATIONDATE"));
				taskList.add(map);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		return taskList;
	}
	

	public Collection<Map<String, String>> queryFileDeleteSynchronizeTasks(long lastSynchTime, int diskType,
			NUser user) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<Map<String, String>> taskList = new ArrayList<Map<String,String>>();
		String sql = "SELECT l.FILE_ID,l.FILE_NAME,l.OPERATION_TYPE,l.OPERATIONDATE "
				+ "FROM "
				+ getFullTableName("KM_LOGS ")
				+ " l "
				+ "WHERE l.USER_ID=? and l.OPERATION_FILE_OR_DIRECTORY="+Logs.OPERATION_FILE+" and l.OPERATIONDATE > ? and "
				+ "l.OPERATION_TYPE='DELETE' "
				+ "ORDER BY l.OPERATIONDATE";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			//stmt.setInt(1, diskType);
			stmt.setString(1, user.getId());
			stmt.setTimestamp(2, new Timestamp(lastSynchTime));
			rs = stmt.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("FILE_ID"));
				map.put("name", rs.getString("FILE_NAME"));
				map.put("type", "1");
				map.put("size", "0");
				map.put("nDirId", "");
				map.put("taskType", "DELETE");
				map.put("operation_date", rs.getString("OPERATIONDATE"));
				taskList.add(map);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		return taskList;
	}
	
	public Collection<Map<String, String>> queryDirCreateSynchronizeTasks(long lastSynchTime,
			NUser user) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<Map<String, String>> taskList = new ArrayList<Map<String,String>>();
		String sql = "SELECT d.ID,d.NAME,d.PARENT_ID,l.OPERATION_TYPE,l.OPERATIONDATE "
				+ "FROM "
				+ getFullTableName("KM_NDir")
				+ " d,"
				+ getFullTableName("KM_LOGS ")
				+ " l "
				+ "WHERE d.ID=l.FILE_ID and d.OWNER_ID=? and l.OPERATION_FILE_OR_DIRECTORY="+Logs.OPERATION_DIRECTORY+" and l.OPERATIONDATE > ? and "
				+ "l.OPERATION_TYPE='CREATE' "
				+ "ORDER BY l.OPERATIONDATE";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getId());
			stmt.setTimestamp(2, new Timestamp(lastSynchTime));
			rs = stmt.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("ID"));
				map.put("name", rs.getString("NAME"));
				map.put("type", "2");
				map.put("size", "0");
				map.put("nDirId", rs.getString("PARENT_ID"));
				map.put("taskType", "DOWNLOAD");
				map.put("operation_date", rs.getString("OPERATIONDATE"));
				taskList.add(map);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		return taskList;
	}
	
	public Collection<Map<String, String>> queryDirDeleteSynchronizeTasks(long lastSynchTime, int diskType,
			NUser user) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<Map<String, String>> taskList = new ArrayList<Map<String,String>>();
		String sql = "SELECT l.FILE_ID,l.FILE_NAME,l.OPERATION_TYPE,l.OPERATIONDATE "
				+ "FROM "
				+ getFullTableName("KM_LOGS ")
				+ " l "
				+ "WHERE l.USER_ID=? and l.OPERATION_FILE_OR_DIRECTORY="+Logs.OPERATION_DIRECTORY+" and l.OPERATIONDATE > ? and "
				+ "l.OPERATION_TYPE='DELETE' "
				+ "ORDER BY l.OPERATIONDATE";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			//stmt.setInt(1, diskType);
			stmt.setString(1, user.getId());
			stmt.setTimestamp(2, new Timestamp(lastSynchTime));
			rs = stmt.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("FILE_ID"));
				map.put("name", rs.getString("FILE_NAME"));
				map.put("type", "2");
				map.put("size", "0");
				map.put("nDirId", "");
				map.put("taskType", "DELETE");
				map.put("operation_date", rs.getString("OPERATIONDATE"));
				taskList.add(map);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		return taskList;
	}
}
