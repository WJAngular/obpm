package cn.myapps.core.deploy.application.ejb;

import junit.framework.TestCase;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.RuntimeDaoManager;

public class ApplicationProcessTest extends TestCase {
	ApplicationProcess process;

	protected void setUp() throws Exception {
		process = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetApplicationByDomainName() {
		fail("Not yet implemented");
	}

	public void testGetAppDomain_Cache() {
		fail("Not yet implemented");
	}

	public void testIsEmpty() {
		fail("Not yet implemented");
	}

	public void testQueryApplicationsStringIntInt() {
		fail("Not yet implemented");
	}

	public void testQueryAppsByDomain() {
		fail("Not yet implemented");
	}

	public void testQueryApplicationsString() {
		fail("Not yet implemented");
	}

	public void testQueryByDomain() {
		fail("Not yet implemented");
	}

	public void testFindByWebUser() {
		fail("Not yet implemented");
	}

	public void testFindBySIPAppKey() {
		fail("Not yet implemented");
	}

	public void testDoViewByName() {
		fail("Not yet implemented");
	}

	public void testAddDevelopersToApplication() {
		fail("Not yet implemented");
	}

	public void testRemoveDevelopersFromApplication() {
		fail("Not yet implemented");
	}

	public void testGetApplicationsByDeveloper() {
		fail("Not yet implemented");
	}

	public void testGetApplicationsByDoaminAdmin() throws Exception {
		ApplicationVO app = (ApplicationVO) process.doView("01b98ff4-8d8c-b3c0-8d30-ece2aa60d534");
		DataSource ds = app.getDataSourceDefine();
		new RuntimeDaoManager().getApplicationInitDAO(app.getConnection(), app.getApplicationid(), ds.getDbTypeName())
				.initTables();
	}

}
