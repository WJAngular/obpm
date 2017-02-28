package cn.myapps.km.permission.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.permission.ejb.DirAccess;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

public class MsSqlDirAccessDAOTest {
	
	Connection conn = null;

	DirAccessDAO dao = null;

	@Before
	public void setUp() throws Exception {
		conn = NDataSource.getConnection();
		dao = new MsSqlDirAccessDAO(conn);
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

		DirAccess dirAccess = new DirAccess();

		dirAccess.setId(Sequence.getSequence());
		dirAccess.setScope(DirAccess.SCOPE_USER);
		dirAccess.setOwnerId(Sequence.getSequence());
		dirAccess.setReadMode(1);
		dirAccess.setWriteMode(2);
		dirAccess.setDownloadMode(3);
		dirAccess.setPrintMode(4);
		dao.create(dirAccess);
	}

	@Test
	public void testCFU() throws Exception {
		DirAccess dirAccess = new DirAccess();
		String id = Sequence.getSequence();

		dirAccess.setId(id);
		dirAccess.setScope(DirAccess.SCOPE_USER);
		dirAccess.setOwnerId(Sequence.getSequence());
		dirAccess.setFileId(Sequence.getSequence());
		dirAccess.setReadMode(1);
		dirAccess.setWriteMode(2);
		dirAccess.setDownloadMode(3);
		dirAccess.setPrintMode(4);
		
		dao.create(dirAccess);
		
		dirAccess = dao.find(id);
		
		dirAccess.setReadMode(5);
		
		dao.update(dirAccess);
		
		dirAccess = dao.find(id);
		assert(5 == dirAccess.getReadMode());
		
		dao.remove(id);
		
		dirAccess = dao.find(id);
		assert(dirAccess==null);
		
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
