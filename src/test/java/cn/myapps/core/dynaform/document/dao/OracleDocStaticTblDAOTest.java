package cn.myapps.core.dynaform.document.dao;

import javax.sql.DataSource;

import junit.framework.TestCase;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.document.ejb.Document;

public class OracleDocStaticTblDAOTest extends TestCase {
	DataSource ds;

	protected void setUp() throws Exception {
		super.setUp();
		String username = "gb4rt";
		String password = "helloworld";
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@192.168.0.123:1527:orcl";
		ds = PersistenceUtils.getC3P0DataSource(username, password, driver, url, "10", "3600");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testFind() throws Exception {
		Document doc = (Document) new OracleDocStaticTblDAO(ds.getConnection(), "")
				.find("8d610935-9f60-46ed-a985-1dc105ae8b12");
		assertNotNull(doc.getAuthor());
	}

}
