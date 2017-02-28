package cn.myapps.km.kmap.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.kmap.ejb.KMap;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

public class MySqlKMapTest {
	Connection conn = null;
	KMapDAO dao = null;

	@Before
	public void setUp() throws Exception {
		conn = NDataSource.getConnection();
		dao = new MySqlKMapDAO(conn);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
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
		
		kmap.setFileid(Sequence.getSequence()+id);
		
		dao.update(kmap);
		
		kmap = dao.find(id);
		
		assert(("update_Fileid"+id).equals(kmap.getFileid()));
	}

	@Test
	public void testFind() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		fail("Not yet implemented");
	}

	@Test
	public void testQuery() {
		fail("Not yet implemented");
	}

}
