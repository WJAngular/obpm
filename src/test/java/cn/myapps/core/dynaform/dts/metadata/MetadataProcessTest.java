package cn.myapps.core.dynaform.dts.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;

import org.junit.Test;

import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.metadata.ejb.ITable;
import cn.myapps.core.dynaform.dts.metadata.ejb.MetadataProcessBean;

public class MetadataProcessTest {
	
	DataSource datasource = null;
	
	MetadataProcessBean process = new MetadataProcessBean();
	
	@Test
	public void indexOptimizationTest() {
		try {
//			process.doIndexOptimization(getDataSource());
			Collection<ITable>  tables = process.getAllTables(getDataSource());
			System.out.println(tables.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void test2(){
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			metaData.getIndexInfo(null, "SYSTEM", "AUTH_DD", true, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MetadataProcessTest test = new MetadataProcessTest();
		test.test2();
	}
	
	private DataSource getDataSource() {
		if(datasource != null) return datasource;
		String username = "system";
		String password = "teemlink";
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@192.168.0.4:1521:sophietest";
		String poolsize = "50";
		String timeout = "3600";
		datasource = new DataSource();
		datasource.setDbType(DataSource.DB_ORACLE);
		datasource.setDriverClass(driver);
		datasource.setUrl(url);
		datasource.setUsername(username);
		datasource.setPassword(password);
		datasource.setPoolsize(poolsize);
		datasource.setTimeout(timeout);
		
		return datasource;
		
		

	}

}
