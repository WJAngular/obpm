package cn.myapps.km.disk.dao;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

public class MsSqlNFileDAOTest {
	
	Connection conn = null;

	NFileDAO dao = null;

	@Before
	public void setUp() throws Exception {
		conn = NDataSource.getConnection();
		dao = new MsSqlNFileDAO(conn);
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

		NFile nFile = new NFile();

		nFile.setId(Sequence.getSequence());
		nFile.setName("name");
		nFile.setMemo("good time: See http://www.slf4j.org/codes.html#StaticLoggerBinder f" +
				"or further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder " +
				"for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.");

		nFile.setLastmodify(new Date());
		dao.create(nFile);
	}

	@Test
	public void testCFU() throws Exception {
		NFile nFile = new NFile();
		String id = Sequence.getSequence();

		nFile.setId(id);
		nFile.setName("name");
		nFile.setMemo("good time: See http://www.slf4j.org/codes.html#StaticLoggerBinder f" +
				"or further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder " +
				"for further details.: See http://www.slf4j.org/codes.html#StaticLoggerBinder" +
				" for further details.");

		nFile.setLastmodify(new Date());
		nFile.setNDirId(Sequence.getSequence());
		nFile.setTitle("测试文档");
		dao.create(nFile);
		
		nFile = dao.find(id);
		
		nFile.setName("update_name"+id);
		
		dao.update(nFile);
		
		nFile = dao.find(id);
		assert(("update_name"+id).equals(nFile.getName()));
		
		dao.remove(id);
		
		nFile = dao.find(id);
		assert(nFile==null);
		
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
