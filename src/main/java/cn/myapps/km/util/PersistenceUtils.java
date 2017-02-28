package cn.myapps.km.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PersistenceUtils {
	
	public static final ThreadLocal<Connection> runtimeDBConn = new ThreadLocal<Connection>();
	
	public static void closeStatement(Statement stmt) throws Exception {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException se) {
			throw new Exception("SQL Exception while closing " + "Statement : \n" + se);
		}
	}
	
	public static void closeConnection() throws Exception {
		try {
			Connection threadLocal = PersistenceUtils.runtimeDBConn.get();
			if (threadLocal != null) {
					if (!threadLocal.isClosed()) {
						threadLocal.close();
					}
				threadLocal = null;
				
				PersistenceUtils.runtimeDBConn.set(null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


}
