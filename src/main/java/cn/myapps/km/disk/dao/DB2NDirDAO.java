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

public class DB2NDirDAO extends AbstractNDirDAO implements NDirDAO {

	public DB2NDirDAO(Connection conn) throws Exception {
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
		System.out.println(parentid);
		String sql = "SELECT d.id,d.name,d.createdate, nullif(1,1) as filesize , '1'  filetype, '' as creator,0 as origin  FROM " + getFullTableName("KM_NDIR") + " d WHERE PARENT_ID=?" +
					 " UNION " +
					 "SELECT f.id,f.name,f.createdate,f.filesize, '2'  filetype,f.creator,f.origin  FROM " + getFullTableName("KM_NFILE") + " f WHERE NDIRID=?";
		
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
			if(sql.toUpperCase().indexOf("WITH UR")>0){
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
		//if (orderby != null && !orderby.trim().equals(""))
			//pagingSelect.append(orderby);
		pagingSelect.append(" ) AS rown from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) AS row_) AS rows_ where rows_.rown BETWEEN ");
		pagingSelect.append(from);
		pagingSelect.append(" AND ");
		pagingSelect.append(to);
		if(pagingSelect.toString().toUpperCase().indexOf("WITH UR")==-1){
			 pagingSelect.append(" WITH UR");
		}
		return pagingSelect.toString();
	}

}
