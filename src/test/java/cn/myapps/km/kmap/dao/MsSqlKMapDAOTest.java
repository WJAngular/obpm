package cn.myapps.km.kmap.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.kmap.ejb.KMap;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

public class MsSqlKMapDAOTest {
	
	Connection conn = null;

	KMapDAO dao = null;

	@Before
	public void setUp() throws Exception {
		conn = NDataSource.getConnection();
		dao = new MsSqlKMapDAO(conn);
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

		KMap kmap = new KMap();
		
		kmap.setId(Sequence.getSequence());
		kmap.setFileid(Sequence.getSequence());
		
		dao.create(kmap);
	}

	@Test
	public void testCFU() throws Exception {
		KMap kmap = new KMap();
		String id = Sequence.getSequence();

		kmap.setId(id);
		kmap.setFileid(Sequence.getSequence());
		
		dao.create(kmap);
		
		kmap = dao.find(id);
		
		kmap.setFileid("update_Fileid"+id);
		
		dao.update(kmap);
		
		kmap = dao.find(id);
		assert(("update_Fileid"+id).equals(kmap.getFileid()));
		
		dao.remove(id);
		
		kmap = dao.find(id);
		assert(kmap==null);
		
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
