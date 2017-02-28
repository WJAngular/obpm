package cn.myapps.qm.base.schema;

import java.sql.Connection;

import cn.myapps.qm.base.schema.dao.*;
import cn.myapps.qm.util.ConnectionManager;

public class SchemaHelper {
	private Connection conn;
	
	private AbstractSchemaDAO schemaDAO;
	
	public void init() throws Exception{
		try{
			//创建一个链接用于初始化调查问卷数据表
			conn = ConnectionManager.getConnection();
			initSchema();
		}catch(Exception e){
//			e.printStackTrace();
//			throw e;
		}finally{
			if(conn != null && !conn.isClosed()){
				conn.close();
			}
		}
	}
	
	public void initSchema() throws Exception{
		try{
			getSchemaDAO().initTables();
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	public AbstractSchemaDAO getSchemaDAO(){
		if(schemaDAO == null){
			try{
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
			}catch(Exception e){
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
	
	public static void main(String[] args) {
		try {
			new SchemaHelper().init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
