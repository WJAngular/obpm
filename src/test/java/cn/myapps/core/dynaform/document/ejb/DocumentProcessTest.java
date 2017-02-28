package cn.myapps.core.dynaform.document.ejb;

import junit.framework.TestCase;

public class DocumentProcessTest extends TestCase {
	private static final String APPLICATION_ID = "01b98ff4-8d8c-b3c0-8d30-ece2aa60d534";

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDoRemoveByFormName() throws Exception{
		DocumentProcessBean process = new DocumentProcessBean(APPLICATION_ID);
		process.doRemoveByFormName(null);
	}

	public void testDoUpdateValueObject() throws Exception {
		DocumentProcessBean process = new DocumentProcessBean(APPLICATION_ID);
		Document doc = (Document) process
				.doView("193f0927-5140-432e-9c7f-134875ac9f52");
		assertNotNull(doc.getAuditorNames());
		assertNotNull(doc.getAuthor());
		// process.doUpdate(doc);
	}

	public void testQueryBySQLPage() throws Exception {
	}
	
	public void testFindBySQL() throws Exception {
		DocumentProcessBean process = new DocumentProcessBean(APPLICATION_ID);
		String sql = "SELECT * FROM T_DOCUMENT WHERE ID='01b2b853-ed61-5500-b328-0ba2e17a1628'";
		Document doc = (Document)process.findBySQL(sql, APPLICATION_ID);	
		assertNotNull(doc.getFormname());
		assertNotNull(doc.getVersions());
	}
	
	public void testFindByDQL() throws Exception {
	}

}
