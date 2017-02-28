package cn.myapps.km.comments.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.comments.ejb.Comments;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

public class MsSqlCommentsDAOTest {
	
	Connection conn = null;

	CommentsDAO dao = null;

	@Before
	public void setUp() throws Exception {
		conn = NDataSource.getConnection();
		dao = new MsSqlCommentsDAO(conn);
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

		Comments comments = new Comments();
		
		comments.setId(Sequence.getSequence());
		comments.setContent("good book");
		
		dao.create(comments);
	}

	@Test
	public void testCFU() throws Exception {
		Comments comments = new Comments();
		String id = Sequence.getSequence();

		comments.setId(id);
		comments.setContent("good book");
		
		dao.create(comments);
		
		comments = dao.find(id);
		
		comments.setContent("update_content"+id);
		
		dao.update(comments);
		
		comments = dao.find(id);
		assert(("update_content"+id).equals(comments.getContent()));
		
		dao.remove(id);
		
		comments = dao.find(id);
		assert(comments==null);
		
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
