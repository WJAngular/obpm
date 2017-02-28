package cn.myapps.attendance.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.myapps.attendance.util.ConnectionManager;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;


public abstract class AbstractBaseDAO {
	
	private static final Logger log = Logger.getLogger(AbstractBaseDAO.class);
	
	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";
	
	protected String tableName = "";

	protected Connection connection;

	public AbstractBaseDAO(Connection conn) throws Exception {
		this.connection = conn;
	}
	
	protected abstract String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException ;
	
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
	
	/**
	 * 更新字段
	 * @param items
	 * 		更新的字段集合（key表示字段名，value表示字段值）
	 * @param pk
	 * 		主键ID的值
	 * @throws Exception
	 */
	public void update(Map<String, Object> items,String pk) throws Exception{
		PreparedStatement stmt = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(getFullTableName(tableName)).append(" SET ");


		Set<String> keys = items.keySet();
		for(String key : keys){
			sql.append(key).append("=?,");
		}
		sql.setLength(sql.length()-1);
		sql.append(" WHERE ID=?");
		
		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql.toString());
			
			int index = 1;
			for(String key : keys){
				Object value = items.get(key);
				if(value instanceof Date){
					Timestamp ts = new Timestamp(((Date)value).getTime());
					stmt.setTimestamp(index, ts);
				}else{
					stmt.setObject(index, value);
				}
				index++;
			}
			
			stmt.setString(index, pk);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}
	
	public DataPackage<?> query(ParamsTable params,WebUser user) throws Exception{
		return null;
	}
	
}
