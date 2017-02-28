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

public class OracleNDiskDAO extends AbstractNDiskDAO implements NDiskDAO {

	public OracleNDiskDAO(Connection conn) throws Exception {
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
		
		String sql = "SELECT f.id,f.name,f.createdate,f.filesize,'2' as filetype, favorites, downloads, views FROM " + getFullTableName("KM_NFILE") + " f WHERE DOMAIN_ID=? AND ORIGIN='1' AND STATE='1'";
		
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
			return sql;
		}

		int from = (page - 1) * lines;
		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect
				.append("select *  FROM  ( select row_.*, rownum rownum_  FROM  ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ where rownum <= ");
		pagingSelect.append(to);
		pagingSelect.append(") where rownum_ > ");
		pagingSelect.append(from);
		// if (orderby != null && !orderby.trim().equals(""))
		// pagingSelect.append(orderby);

		return pagingSelect.toString();
	}

}
