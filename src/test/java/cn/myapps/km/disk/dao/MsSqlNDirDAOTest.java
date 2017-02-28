package cn.myapps.km.disk.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.disk.ejb.NDir;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

public class MsSqlNDirDAOTest {
	
	Connection conn = null;

	NDirDAO dao = null;

	@Before
	public void setUp() throws Exception {
		conn = NDataSource.getConnection();
		dao = new MsSqlNDirDAO(conn);
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

		NDir nDir = new NDir();

		nDir.setId(Sequence.getSequence());
		nDir.setOwnerId(Sequence.getSequence());
		dao.create(nDir);
	}

	@Test
	public void testCFU() throws Exception {
		NDir nDir = new NDir();
		String id = Sequence.getSequence();
		String ownerId = Sequence.getSequence();

		nDir.setId(id);
		nDir.setOwnerId(ownerId);
		nDir.setName("name");

		dao.create(nDir);
		
		nDir = dao.find(id);
		
		nDir.setName("update_name"+id);
		
		dao.update(nDir);
		
		nDir = dao.find(id);
		
		assert(("update_name"+id).equals(nDir.getName()));
		
		dao.remove(id);
		nDir = dao.find(id);
		
		assert(nDir == null);
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
