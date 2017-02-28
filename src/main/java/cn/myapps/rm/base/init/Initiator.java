package cn.myapps.rm.base.init;

import java.sql.Connection;

import cn.myapps.rm.base.init.dao.AbstractInitDAO;
import cn.myapps.rm.base.init.dao.DB2InitDAO;
import cn.myapps.rm.base.init.dao.MssqlInitDAO;
import cn.myapps.rm.base.init.dao.MysqlInitDAO;
import cn.myapps.rm.base.init.dao.OraclelInitDAO;
import cn.myapps.rm.util.IDataSource;

public class Initiator {
	
	private AbstractInitDAO initDAO;
	
	private Connection conn;
	
	
	public void init() throws Exception {
		try {
			IDataSource.reLoadDataSource();
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

	public AbstractInitDAO getInitDAO() {
		if(initDAO ==null){
			try {
				String dbType = IDataSource._dbTye;
				if("MSSQL".equals(dbType)){  
					initDAO = new MssqlInitDAO(getConn());
				}else if("ORACLE".equals(dbType)){
					initDAO = new OraclelInitDAO(getConn());
				}else if("DB2".equals(dbType)){
					initDAO = new DB2InitDAO(getConn());
				}else if("MYSQL".equals(dbType)){
					initDAO = new MysqlInitDAO(getConn());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return initDAO;
	}

	public Connection getConn() {
		if(conn ==null){
			try {
				conn = IDataSource.getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}
	
	public void initSchema() throws Exception {
		
		try {
			getInitDAO().initTables();
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
			new Initiator().init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
