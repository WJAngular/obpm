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
import cn.myapps.util.StringUtil;

public class DB2NDiskDAO extends AbstractNDiskDAO implements NDiskDAO {

	public DB2NDiskDAO(Connection conn) throws Exception {
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

	public DataPackage<IFile> getHotestFiles(int page, int lines,
			String domainid, String categoryID) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DataPackage<IFile> datas = new DataPackage<IFile>();
		Collection<IFile> iFiles = new ArrayList<IFile>();
		
		String sql = "SELECT f.id,f.name,f.createdate,f.filesize,'2' filetype, favorites, downloads, views FROM " + getFullTableName("KM_NFILE") + " f WHERE DOMAIN_ID=? AND ORIGIN=1 AND STATE='1'";
		
		if(!StringUtil.isBlank(categoryID)){
			sql = sql + " AND ROOT_CATEGORY_ID=?";
		}
		
		//拼接MsSql的分页语句
		String querySql = buildLimitString(sql, page, lines, "favorites,downloads,views", "DESC");
		
		log.info(querySql);
		
		try {
			stmt = connection.prepareStatement(querySql);
			stmt.setString(1, domainid);
			if(!StringUtil.isBlank(categoryID)){
				stmt.setString(2, categoryID);
			}
			rs = stmt.executeQuery();
			
			while(rs.next()){
				IFile iFile = new IFile();
				iFile.setId(rs.getString("id"));
				iFile.setName(rs.getString("name"));
				iFile.setFileType(rs.getString("filetype"));
				iFile.setCreateDate(rs.getTimestamp("createdate"));
				iFile.setSize(rs.getLong("filesize"));
				
				iFiles.add(iFile);
			}
			
			datas.datas = iFiles;
			datas.linesPerPage = lines;
			datas.rowCount = (int) countBySQL(sql, domainid, categoryID);
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
	@Override
	protected String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			if (sql.toUpperCase().indexOf("WITH UR") > 0) {
				return sql;
			}
			return sql + " WITH UR";
		}
		// Modify by James:2010-01-03, fixed page divide error.
		// int from = (page - 1) * lines;
		int from = (page - 1) * lines + 1;

		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect.append("Select * from (select row_.*, rownumber() over(");
		// if (orderby != null && !orderby.trim().equals(""))
		// pagingSelect.append(orderby);
		pagingSelect.append(" ) AS rown from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) AS row_) AS rows_ where rows_.rown BETWEEN ");
		pagingSelect.append(from);
		pagingSelect.append(" AND ");
		pagingSelect.append(to);
		if (pagingSelect.toString().toUpperCase().indexOf("WITH UR") == -1) {
			pagingSelect.append(" WITH UR");
		}
		return pagingSelect.toString();
	}

}
