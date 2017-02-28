package cn.myapps.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;


public class RuntimeDatabaseLoacTest {

	private BasicDataSource datasource;

	private boolean isStop = false;
	
	static final int maxThread = 20;
	static Random rnd = new Random(10000);

	@Test
	public void test() throws InterruptedException {

		for (int i = 0; i < maxThread; i++) {
			new ThreadA().start();
		}
		// new ThreadB().start();

		Thread.sleep(3 * 60 * 1000);
		isStop = true;

	}

	public BasicDataSource getDataSource() {
		if (datasource != null)
			return datasource;

		String username = "sa";
		String password = "teemlink";
		String driver = "net.sourceforge.jtds.jdbc.Driver";
		String url = "jdbc:jtds:sqlserver://192.168.0.38:1433/obpm";
		int poolsize = maxThread;
		int timeout = 3600;

		// dataSource = PersistenceUtils.getC3P0DataSource(username, password,
		// driver, url, poolsize, timeout);
		BasicDataSource datasource = new BasicDataSource();

		datasource.setUsername(username);
		datasource.setPassword(password);
		datasource.setDriverClassName(driver);
		datasource.setUrl(url);
		// ds.setPoolPreparedStatements(true);
		// ds.setMaxOpenPreparedStatements(10);
		// ds.setInitialSize(10);
		datasource.setMaxIdle(5);
		datasource.setMaxActive(poolsize);

		datasource.setMaxWait(timeout);
		datasource.setDefaultAutoCommit(true);

		return datasource;
	}

	public class ThreadA extends Thread {

		public void run() {
			this.setName("ThreadA");

			while (!isStop) {
				Connection conn = null;
				try {
					conn = getDataSource().getConnection();
					String id = rnd.nextLong() + UUID.randomUUID().toString();
					//String id = UUID.randomUUID().toString();
					conn.setAutoCommit(false);
					// with (nolock)
					String selectA = "select top 10 * from (select top 100 * from lock_a order by id desc) as ta";
					String insertA ="insert lock_a (nickname,addr,id) values ('jarod','guangzhou','"+ id + "')";
					String updateA = "update lock_a set nickname='jarod' where id = '" + id+ "'";
					String deleteA = "delete lock_a where id='" + id + "'";

					String selectB = "select top 10 * from (select top 100 * from lock_b order by id desc) as tb";
					String insertB = "insert lock_b (nickname,addr,id) values ('jarod','guangzhou','"+ id + "')";
					String updateB = "update lock_b set nickname='jarod' where id = '" + id+ "'";
					String deleteB = "delete lock_b where id='" + id + "'";

					executeUpdate(insertA, conn);
					executeUpdate(updateA, conn);
					executeUpdate(deleteA, conn);
					executeQuery(selectA, conn);

					executeUpdate(insertB, conn);
					executeUpdate(updateB, conn);
					executeUpdate(deleteB, conn);
					executeQuery(selectB, conn);

					conn.commit();
					conn.setAutoCommit(true);
				} catch (Exception e) {
					try {
						conn.rollback();
						conn.setAutoCommit(true);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

				} finally {
					if (conn != null)
						try {
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				}
				// Thread.sleep(500);

			}

		}

	}

	public static void executeUpdate(String sql, Connection conn)
			throws Exception {

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			System.out.println("Thread(" + Thread.currentThread().getId()
					+ " sql:" + sql);
			stmt.executeUpdate(sql);

		} catch (Exception e) {
			System.err.println("出错的sql：" + sql);
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
		}
		Thread.sleep(100);
	}

	public static ResultSet executeQuery(String sql, Connection conn)
			throws Exception {

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			System.out.println("Thread(" + Thread.currentThread().getId()
					+ " sql:" + sql);
			ResultSet res = stmt.executeQuery(sql);
			Thread.sleep(100);
			return res;
		} catch (Exception e) {
			System.err.println("出错的sql：" + sql);
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new RuntimeDatabaseLoacTest().test();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
