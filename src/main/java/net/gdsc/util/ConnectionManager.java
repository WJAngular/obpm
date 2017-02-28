/**
 * 
 */
package net.gdsc.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * 采用ThreadLocal封装Connection
 * 只要线程是活动的，没有结束，ThreadLocal是可访问的，就可以访问本线程的connection
 * @author zhangmz
 */
public class ConnectionManager  {
	private static final Logger logger = Logger.getLogger(ConnectionManager.class);
	//使用ThreadLocal保存Connection变量
	private static ThreadLocal<Connection> connectionHolder =  new ThreadLocal<Connection>();
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public static Connection getConnection() {
		//ThreadLocal取得当前线程的connection
		Connection threadLocal = connectionHolder.get();
		Connection conn = null;
		//如果ThreadLocal没有绑定相应的Connection，创建一个新的Connection，
        //并将其保存到本地线程变量中。
		if(threadLocal != null){
			conn = threadLocal;
		}
		try {
			if (conn == null || conn.isClosed()) {
				conn = OADataSource.getConnection();
				//将当前线程的Connection设置到ThreadLocal
				if(conn!=null)
					connectionHolder.set(conn);
				else{
					conn = newConnection();
					connectionHolder.set(conn);
				}
			}
		} catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
        	e.printStackTrace();
		}
		return conn;
	}
	
	 /**
     * 关闭Connection，清除集合中的Connection
	 * @throws Exception 
     */
    public static void closeConnection() throws Exception{
    	try {
			Connection threadLocal = connectionHolder.get();
			if (threadLocal != null) {
					if (!threadLocal.isClosed()) {
						threadLocal.close();
					}
				threadLocal = null;
				
				connectionHolder.set(null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }
    
	/**
	 * 关闭Statement
	 * @param stmt
	 * @throws Exception
	 */
	public static void closeStatement(Statement stmt) throws Exception {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException se) {
			throw new Exception("SQL Exception while closing " + "Statement : \n" + se);
		}
	}
	
	private static Connection newConnection() throws Exception{
		OADataSource.reloadDataSource();
		return OADataSource.getConnection();
	}

	public static void main(String[] args){
//		PreparedStatement stmt = null;
		try {
//			Connection conn = ConnectionManager.getConnection();
//			Connection conn = OADataSource.getConnection();
//			stmt = conn.prepareStatement(sql);
//			ResultSet rs = stmt.executeQuery();
//			if(rs!=null){
//				while(rs.next()){
//					System.out.println(rs.getString("ITEM_新闻标题"));
//				}
//			}
//			stmt.close();
//			NewsDao dao = new NewsDao();
//			NewsVO o = dao.find("cms20141203142513");
//			System.out.println(
//					o.getNewsTitle()
//					+" "+o.getApplicationid()+" "+o.getAuditdate()+" "+o.getAuditorlist()+" "+o.getCreated());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
