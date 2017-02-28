package cn.myapps.mr.base.schema;

import java.sql.Connection;

import cn.myapps.mr.base.schema.SchemaHelper;
import cn.myapps.mr.base.schema.dao.AbstractSchemaDAO;
import cn.myapps.mr.base.schema.dao.DB2SchemaDAO;
import cn.myapps.mr.base.schema.dao.MssqlSchemaDAO;
import cn.myapps.mr.base.schema.dao.MysqlSchemaDAO;
import cn.myapps.mr.base.schema.dao.OraclelSchemaDAO;
import cn.myapps.mr.util.ConnectionManager;

public class SchemaHelper {
private AbstractSchemaDAO schemaDAO;
	
	private Connection conn;
	
	
	public void init() throws Exception {
		try {
			conn = ConnectionManager.getConnection();
			initSchema();
			initBaseData();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(conn !=null && !conn.isClosed()){
				conn.close();
			}
		}
	}

	public AbstractSchemaDAO getSchemaDAO() {
		if(schemaDAO ==null){
			try {
				String dbType = ConnectionManager.dbType;
				if("MSSQL".equals(dbType)){  
					schemaDAO = new MssqlSchemaDAO(getConn());
				}else if("ORACLE".equals(dbType)){
					schemaDAO = new OraclelSchemaDAO(getConn());
				}else if("DB2".equals(dbType)){
					schemaDAO = new DB2SchemaDAO(getConn());
				}else if("MYSQL".equals(dbType)){
					schemaDAO = new MysqlSchemaDAO(getConn());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return schemaDAO;
	}
	

	public Connection getConn() {
		if(conn ==null){
			try {
				conn = ConnectionManager.getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}
	
	public void initSchema() throws Exception {
		
		try {
			getSchemaDAO().initTables();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	public void initBaseData() throws Exception {
		try {
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static void main(String[] args) {
		try {
			new SchemaHelper().init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}