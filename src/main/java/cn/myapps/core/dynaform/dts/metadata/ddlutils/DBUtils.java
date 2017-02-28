package cn.myapps.core.dynaform.dts.metadata.ddlutils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;

import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.metadata.ejb.IColumn;
import cn.myapps.core.dynaform.dts.metadata.ejb.IIndex;
import cn.myapps.core.dynaform.dts.metadata.ejb.ITable;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
public class DBUtils {
	
	private static Logger LOG = Logger.getLogger("");
	
	public static final String DBTYPE_ORACLE = "ORACLE";

	public static final String DBTYPE_MSSQL = "MSSQL";

	public static final String DBTYPE_MYSQL = "MYSQL";

	public static final String DBTYPE_HSQLDB = "HSQLDB";

	public static final String DBTYPE_DB2 = "DB2";
	
	
	/**
	 * 获取数据源下的所有表
	 * @param dataSource
	 * @return
	 */
	public static Map<String,ITable> getTables(DataSource dataSource) throws Exception{
		if(dataSource != null){
			try {
				return getTables(null, dataSource.getDbTypeName(), dataSource.getConnection());
			} catch (Exception e) {
				throw e;
			} 
		}
		return new HashMap<String, ITable>();
	
	}
	
	/**
	 * 
	 * @param tableName
	 *            数据表名称
	 * @param applicationId
	 *            应用ID
	 * @return 表集合
	 */
	public static Map<String,ITable> getTables(String tableName, String dbType,
			Connection conn) throws Exception{
		Map<String,ITable> map = new HashMap<String, ITable>();

		ResultSet tableSet = null;
		try {

			String catalog = null;
			String schemaPattern = null;

			String schema = getSchema(conn, dbType);

			if (dbType.equals(DBTYPE_ORACLE)) {
				schemaPattern = schema;
			} else if (dbType.equals(DBTYPE_MSSQL)) {
				schemaPattern = "DBO";
			} else if (dbType.equals(DBTYPE_MYSQL)) {
				catalog = schema;
			} else if (dbType.equals(DBTYPE_HSQLDB)) {
				schemaPattern = schema;
			} else if (dbType.equals(DBTYPE_DB2)) {
				schemaPattern = schema;
			}

			DatabaseMetaData metaData = conn.getMetaData();
			tableSet = metaData.getTables(catalog, schemaPattern, tableName,
					new String[] { "TABLE" });

			while (tableSet.next()) {
				tableName = tableSet.getString(3);
				if(tableName.toUpperCase().startsWith("AUTH_") || tableName.toUpperCase().startsWith("T_")){
					ITable table = new ITable(tableName);
					ResultSet columnSet = null;
					ResultSet primaryKeys =null;
					ResultSet rsIndex =null;
//					ResultSet foreignKeys =null;
					Collection<String> pks = new ArrayList<String>();
					try {
						columnSet = metaData.getColumns(catalog, schemaPattern,
								tableName, null);
						try {
						rsIndex = metaData.getIndexInfo(catalog, schema, tableName, false, false);
						} catch (Exception e) {
//							e.printStackTrace();
						}
						primaryKeys = metaData.getPrimaryKeys(catalog, schemaPattern, tableName);
						while (primaryKeys !=null && primaryKeys.next()) {
							String COLUMN_NAME  = primaryKeys.getString(4);
							pks.add(COLUMN_NAME);
						}
						
//						foreignKeys = metaData.getExportedKeys(catalog, schemaPattern, tableName);
//						while (foreignKeys.next()) {
//							String PKTABLE_NAME   = foreignKeys.getString("PKTABLE_NAME");
//							String PKCOLUMN_NAME   = foreignKeys.getString("PKCOLUMN_NAME");
//							String FKTABLE_NAME   = foreignKeys.getString("FKTABLE_NAME");
//							String FKCOLUMN_NAME   = foreignKeys.getString("FKCOLUMN_NAME");
//							String FK_NAME   = foreignKeys.getString("FK_NAME");
//							String PK_NAME   = foreignKeys.getString("PK_NAME");
//							int UPDATE_RULE = foreignKeys.getInt("UPDATE_RULE");
//							int DELETE_RULE = foreignKeys.getInt("DELETE_RULE");
//							
//							IForeignKey foreignkey = new IForeignKey(PKTABLE_NAME, PKCOLUMN_NAME, FKTABLE_NAME, FKCOLUMN_NAME, FK_NAME, PK_NAME,UPDATE_RULE,DELETE_RULE);
//							table.getForeignKeys().add(foreignkey);
//						}
						
						while (columnSet.next()) {
							String name = columnSet.getString(4);
							int dataType = columnSet.getInt(5);
							int COLUMN_SIZE = columnSet.getInt("COLUMN_SIZE");
							boolean nullable = columnSet.getBoolean("NULLABLE");
							boolean isPK = false;
							if(pks.contains(name)){
								isPK = true;
							}
							IColumn column = new IColumn(name, dataType, isPK, nullable, COLUMN_SIZE);
	
							table.getColumns().add(column);
						}
						
						while (rsIndex != null && rsIndex.next()) {
							String name =rsIndex.getString("INDEX_NAME");
							String tbName = rsIndex.getString("TABLE_NAME");
							int type = rsIndex.getInt("TYPE");
							boolean unique = rsIndex.getBoolean("NON_UNIQUE");
							String columnName = rsIndex.getString("COLUMN_NAME");
							String sort = rsIndex.getString("ASC_OR_DESC");
							if(!StringUtil.isBlank(columnName)){
								IIndex index = new IIndex(name, tbName, columnName, unique, type, sort);
								table.getIndexs().add(index);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					} finally {
						if (columnSet != null) {
							columnSet.close();
						}
						if (primaryKeys != null) {
							primaryKeys.close();
						}
//						if (foreignKeys != null) {
//							foreignKeys.close();
//						}
					}
	
					map.put(table.getName().toUpperCase(),table);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				tableSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;
	}
	
	public static String getSchema(DataSource dataSource) throws Exception{
		try {
			return getSchema(dataSource.getConnection(), dataSource.getDbTypeName());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getSchema(Connection conn, String dbType) {
		if (dbType.equals(DBTYPE_ORACLE) || dbType.equals(DBTYPE_DB2)) {
			try {
				return conn.getMetaData().getUserName().trim().toUpperCase();
			} catch (SQLException sqle) {
				return "";
			}
		} else if (dbType.equals(DBTYPE_MYSQL)) {
			try {
				return conn.getCatalog();
			} catch (SQLException sqle) {
				return "";
			}

		} else if (dbType.equals(DBTYPE_MSSQL)) {
			try {
				ResultSet rs = conn.getMetaData().getSchemas();
				if (rs != null) {
					if (rs.next())
						return rs.getString(1).trim().toUpperCase();
				}
			} catch (SQLException sqle) {
				return "";
			}
		} else if (dbType.equals(DBTYPE_HSQLDB)) {
			return "public".toUpperCase();
		}
		return "";
	}
	
	public static void main(String[] args) {
		
	}
	
	public static String getFullTableName(String tableName,Connection conn,String dbType) {
		String schema = getSchema(conn, dbType);
		if (schema != null && !schema.trim().equals("")) {
			return schema.trim().toUpperCase() + "." + tableName.trim().toUpperCase();
		}
		return tableName.trim().toUpperCase();
	}
	
	public static void executeUpdate(String sql ,DataSource dataSource) throws Exception{
		Statement stmt = null;
		try {
			Connection conn = dataSource.getConnection();
			stmt = conn.createStatement();
			LOG.debug(sql);
			stmt.executeUpdate(sql);
			
		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
		}finally{
			if(stmt !=null) stmt.close();
		}
	}
	
	public static int countBySQL(String sql ,DataSource dataSource) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Connection conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(stmt !=null) stmt.close();
			if(rs != null) rs.close();
		}
		return 0;
	}
	
	/**
	 * 执行查询并返回一个collection
	 * @param sql 查询语句
	 * @param  connection 数据库链接
	 * @return collection 
	 * 集合。Map中的key为查询的表的列名称，value为该列的值。
	 * @throws Exception
	 */
	public static Collection<Map<String,Object>> doQuery(String sql ,Connection conn) throws Exception{
		Collection<Map<String,Object>> collection = new ArrayList<Map<String,Object>>();
		Statement stmt = null;
		ResultSet rs = null;
		try {			
			stmt = conn.createStatement();
			LOG.debug(sql);
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int counts = rsmd.getColumnCount();
			while(rs.next()){
				Map<String,Object> map = new CaseInsensitiveMap();
				for(int i = 1; i <= counts; i ++){
					int dataType = rsmd.getColumnType(i);
					String columnName = rsmd.getColumnName(i);
					Object value = null;
					
					switch (dataType) {
					
					case Types.LONGVARCHAR:
					case Types.CLOB:
						value = rs.getString(columnName);
						break;
						
					case Types.VARBINARY:
					case Types.BLOB:
						value = getColumnByteString(rs,columnName);
						break;
						
					case Types.CHAR:
					case Types.VARCHAR:
						value = rs.getString(columnName);
						break;
						
					case Types.NUMERIC:
					case Types.DECIMAL:
						value = rs.getDouble(columnName);
						break;
						
					case Types.INTEGER:
					case Types.BOOLEAN:
						value = rs.getInt(columnName);
						break;
						
					case Types.DOUBLE:
					case Types.FLOAT:
					case Types.REAL:
						value = rs.getDouble(columnName);
						break;

					case Types.DATE:
					case Types.TIME:
					case Types.TIMESTAMP:
						value = rs.getTimestamp(columnName);
						break;

					default:
						value = rs.getString(columnName);
						break;
					}
					map.put(columnName, value);
				}
				collection.add(map);
			}
			return collection;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(stmt !=null) stmt.close();
			if(rs != null) rs.close();
		}
	}

	private static String getColumnByteString(ResultSet rs,String columnName){
		String rtn = "";
		InputStream in = null;
		ByteArrayOutputStream baos = null;
		try {
			in = rs.getBinaryStream(columnName);
			baos = new ByteArrayOutputStream();
			int nNumber;
			byte[] buffer = new byte[1024];
			while ((nNumber = in.read(buffer)) != -1) {
				baos.write(buffer, 0, nNumber);
			}
			byte[] data = baos.toByteArray();
			rtn = Security.encodeToBASE64(data);
			in.close();
			baos.close();
		} catch (Exception e) {
			
		}finally{
			try {
				if(in !=null) in.close();
				if(baos !=null) baos.close();
			} catch (Exception e2) {
			}
		}
		
		return rtn;
	}

}
