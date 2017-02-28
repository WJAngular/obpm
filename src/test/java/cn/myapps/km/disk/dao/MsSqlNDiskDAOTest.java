package cn.myapps.km.disk.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.disk.ejb.NDisk;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

public class MsSqlNDiskDAOTest {
	
	Connection conn = null;

	NDiskDAO dao = null;

	@Before
	public void setUp() throws Exception {
		conn = NDataSource.getConnection();
		dao = new MsSqlNDiskDAO(conn);
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

		NDisk nDisk = new NDisk();

		nDisk.setId(Sequence.getSequence());
		dao.create(nDisk);
	}

	@Test
	public void testCFU() throws Exception {
		NDisk nDisk = new NDisk();
		String id = Sequence.getSequence();
		String departmentId = Sequence.getSequence();

		nDisk.setId(id);
		nDisk.setName("name");

		dao.create(nDisk);
		
		nDisk = dao.find(id);
		
		nDisk.setName("update_name"+id);
		
		dao.update(nDisk);
		
		nDisk = dao.find(id);
		
		assert(("update_name"+id).equals(nDisk.getName()));
		
		dao.remove(id);
		nDisk = dao.find(id);
		
		assert(nDisk == null);
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
