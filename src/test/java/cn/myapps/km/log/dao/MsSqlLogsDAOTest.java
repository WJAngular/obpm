package cn.myapps.km.log.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.log.ejb.Logs;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

public class MsSqlLogsDAOTest {
	
	Connection conn = null;

	LogsDAO dao = null;

	@Before
	public void setUp() throws Exception {
		conn = NDataSource.getConnection();
		dao = new MsSqlLogsDAO(conn);
	}

	@After
	public void tearDown() throws Exception {
		if(conn!=null && !conn.isClosed()){
			conn.close();
		}
	}

	//@Test
	public void testGetConnection() throws Exception {

		Connection conn = NDataSource.getConnection();
		assert (!conn.isClosed());

	}

	//@Test
	public void testCreate() throws Exception {

		Logs logs = new Logs();
		
		logs.setId(Sequence.getSequence());
		
		dao.create(logs);
	}

	@Test
	public void testCFU() throws Exception {
		Logs logs = new Logs();
		String id = Sequence.getSequence();

		logs.setId(id);
		
		dao.create(logs);
		
		logs = (Logs) dao.find(id);
		
		
		dao.update(logs);
		
		logs = (Logs) dao.find(id);
		assert(id.equals(logs.getId()));
		
		dao.remove(id);
		
		logs = (Logs) dao.find(id);
		assert(logs==null);
		
	}

	//@Test
	public void testFind() {
		fail("Not yet implemented");
	}

	//@Test
	public void testRemove() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQuery() {
		fail("Not yet implemented");
	}

}
