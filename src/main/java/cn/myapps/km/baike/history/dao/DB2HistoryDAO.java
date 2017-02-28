package cn.myapps.km.baike.history.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.myapps.km.baike.history.ejb.History;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.util.BaikeUtils;

/**
 * @author Able
 * SQLSERVER实现类
 * 
 */
public class DB2HistoryDAO extends AbstractHistoryDAO implements HistoryDao {

	public DB2HistoryDAO(Connection conn) throws Exception {
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
	 * 通过entryId查找浏览历史
	 */
	public History queryByEntryId(String entryId,String userId) throws Exception {
		PreparedStatement stmt = null;
		String sql = "SELECT * FROM " + getFullTableName("BAIKE_READ_HISTORY") + " WHERE ENTRYID = ? AND AUTHOR = ?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			stmt.setString(2, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				History history  = setProperty(rs);
				rs.close();
				return history;
			}else{
				return null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	

	
	/**
	 * 生成分页sql.
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
	public String buildPagingString(String sql, int page, int lines, String orderbyColumn, String orderbyMode)
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
	
	/**
	 * 通过USERID查询浏览记录
	 */
	public DataPackage<History> queryHistoryByUserId(int page, int lines,String userId) throws Exception {
	
			PreparedStatement stmt = null;
			DataPackage<History> result = new DataPackage<History>();
			//设置分页信息
			String rowCounthql="SELECT HISTORY.* FROM "+getFullTableName("BAIKE_ENTRY") +" ENTRY, "+ getFullTableName("BAIKE_READ_HISTORY") +" HISTORY WHERE ENTRY.ID = HISTORY.ENTRYID AND HISTORY.AUTHOR = '"+userId+"' "; 
			result.rowCount = getTotalLines(rowCounthql);
			result.pageNo = page;
			result.linesPerPage = lines;
			//当页码大于总页数
			if (result.pageNo > result.getPageCount()) {
				result.pageNo = 1;
				page = 1;
			}
			//设置每页数据集     没有排序的时候参数设置两遍
			String sql="SELECT HISTORY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY, "+ getFullTableName("BAIKE_READ_HISTORY") + " HISTORY WHERE ENTRY.ID = HISTORY.ENTRYID AND HISTORY.AUTHOR = ? ";
			sql = buildPagingString(sql, page, lines, "READTIME", "DESC");
			log.info(sql);
			try {
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, userId);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					result.getDatas().add(setProperty(rs));
				}
				rs.close();
				return result;
			} catch (Exception e) {
				throw e;
			} finally {
				log.info(sql);
				BaikeUtils.closeStatement(stmt);
			}
}
}