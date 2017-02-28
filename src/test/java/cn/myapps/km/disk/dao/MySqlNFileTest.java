package cn.myapps.km.disk.dao;


public class MySqlNFileTest {

	MySqlNFileDAO dao = null;

/*	@Before
	public void setUp() throws Exception {
		dao = new MySqlNFileDAO();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetConnection() throws Exception {

		Connection conn = dao.getConnection();
		assert (!conn.isClosed());

	}

	@Test
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
		dao.create(nFile);
		
		nFile = dao.find(id);
		
		nFile.setName("update_name"+id);
		
		dao.update(nFile);
		
		nFile = dao.find(id);
		
		assert(("update_name"+id).equals(nFile.getName()));
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
*/
}
