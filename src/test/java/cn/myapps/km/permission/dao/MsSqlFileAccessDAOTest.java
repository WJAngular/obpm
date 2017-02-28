package cn.myapps.km.permission.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.permission.ejb.FileAccess;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

public class MsSqlFileAccessDAOTest {
	
	Connection conn = null;

	FileAccessDAO dao = null;

	@Before
	public void setUp() throws Exception {
		conn = NDataSource.getConnection();
		dao = new MsSqlFileAccessDAO(conn);
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

		FileAccess fileAccess = new FileAccess();

		fileAccess.setId(Sequence.getSequence());
		fileAccess.setScope(FileAccess.SCOPE_USER);
		fileAccess.setOwnerId(Sequence.getSequence());
		fileAccess.setReadMode(1);
		fileAccess.setWriteMode(2);
		fileAccess.setDownloadMode(3);
		fileAccess.setPrintMode(4);
		dao.create(fileAccess);
	}

	@Test
	public void testCFU() throws Exception {
		FileAccess fileAccess = new FileAccess();
		String id = Sequence.getSequence();

		fileAccess.setId(id);
		fileAccess.setScope(FileAccess.SCOPE_USER);
		fileAccess.setOwnerId(Sequence.getSequence());
		fileAccess.setFileId(Sequence.getSequence());
		fileAccess.setReadMode(1);
		fileAccess.setWriteMode(2);
		fileAccess.setDownloadMode(3);
		fileAccess.setPrintMode(4);
		
		dao.create(fileAccess);
		
		fileAccess = dao.find(id);
		
		fileAccess.setReadMode(5);
		
		dao.update(fileAccess);
		
		fileAccess = dao.find(id);
		assert(5 == fileAccess.getReadMode());
		
		dao.remove(id);
		
		fileAccess = dao.find(id);
		assert(fileAccess==null);
		
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
