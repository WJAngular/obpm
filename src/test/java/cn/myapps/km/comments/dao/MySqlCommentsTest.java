package cn.myapps.km.comments.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.comments.ejb.Comments;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

public class MySqlCommentsTest {
	Connection conn = null;
	MySqlCommentsDAO dao = null;

	@Before
	public void setUp() throws Exception {
		conn = NDataSource.getConnection();
		dao = new MySqlCommentsDAO(conn);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() throws Exception {

		Comments comments = new Comments();

		comments.setId(Sequence.getSequence());
		comments.setContent("goodbook");
		dao.create(comments);
	}

	@Test
	public void testCFU() throws Exception {
		Comments comments = new Comments();
		String id = Sequence.getSequence();

		comments.setId(id);
		comments.setContent("name");
		dao.create(comments);
		
		comments = dao.find(id);
		
		comments.setContent("update_content"+id);
		
		dao.update(comments);
		
		comments = dao.find(id);
		
		assert(("update_content"+id).equals(comments.getContent()));
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
