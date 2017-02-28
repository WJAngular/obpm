package cn.myapps.km.disk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.disk.ejb.IFile;
import cn.myapps.km.util.PersistenceUtils;

public class OracleNDirDAO extends AbstractNDirDAO implements NDirDAO {

	public OracleNDirDAO(Connection conn) throws Exception {
		super(conn);
		if (conn != null) {
			try {
				this.schema = conn.getMetaData().getUserName().trim()
						.toUpperCase();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}
	
	public long countBySQL(String sql, String parentid) throws Exception {
		if (sql == null || sql.trim().equals(""))
			return 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		sql = "SELECT COUNT(*)  FROM (" + sql + ")  T";

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, parentid);
			statement.setString(2, parentid);
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
	 * 分页查找下级目录及文件
	 * @param page
	 * @param lines
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	public DataPackage<IFile> getUnderNDirAndNFile(int page, int lines, String parentid,String orderbyFile,String orderbyMode) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DataPackage<IFile> datas = new DataPackage<IFile>();
		Collection<IFile> iFiles = new ArrayList<IFile>();
		
		String sql = "SELECT d.id,d.name,d.createdate, null as filesize ,'1' as filetype, null as creator,0 as origin FROM " + getFullTableName("KM_NDIR") + " d WHERE PARENT_ID=?" +
					 " UNION " +
					 "SELECT f.id,f.name,f.createdate,f.filesize, '2' as filetype,f.creator,f.origin FROM " + getFullTableName("KM_NFILE") + " f WHERE NDIRID=?";
		
		//拼接MsSql的分页语句
		String querySql = buildLimitString(sql, page, lines, orderbyFile, orderbyMode);
		
		log.info(querySql);
		
		try {
			stmt = connection.prepareStatement(querySql);
			stmt.setString(1, parentid);
			stmt.setString(2, parentid);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				IFile iFile = new IFile();
				iFile.setId(rs.getString("id"));
				iFile.setName(rs.getString("name"));
				iFile.setFileType(rs.getString("filetype"));
				iFile.setCreateDate(rs.getDate("createdate"));
				iFile.setSize(rs.getLong("filesize"));
				iFile.setCreator(rs.getString("creator"));
				iFile.setOrigin(rs.getInt("origin"));
				iFiles.add(iFile);
			}
			
			datas.datas = iFiles;
			datas.linesPerPage = lines;
			datas.rowCount = (int) countBySQL(sql, parentid);
			datas.pageNo = page;
			return datas;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		
		return null;
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
	public String buildLimitString(String sql, int page, int lines, String orderbyFile, String orderbyMode)
			throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		int from = (page - 1) * lines;
		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect.append("select *  FROM  ( select row_.*, rownum rownum_  FROM  ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ where rownum <= ");
		pagingSelect.append(to);
		pagingSelect.append(") where rownum_ > ");
		pagingSelect.append(from);
		//if (orderby != null && !orderby.trim().equals(""))
			//pagingSelect.append(orderby);

		return pagingSelect.toString();
	}

}
