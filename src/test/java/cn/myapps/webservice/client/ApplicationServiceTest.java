package cn.myapps.webservice.client;

import java.rmi.RemoteException;
import java.util.Collection;

import cn.myapps.webservice.fault.ApplicationServiceFault;
import cn.myapps.webservice.model.SimpleApplication;

import junit.framework.TestCase;

public class ApplicationServiceTest extends TestCase {
	ApplicationService service;

	protected void setUp() throws Exception {
		super.setUp();
		ApplicationServiceServiceLocator locator = new ApplicationServiceServiceLocator();
		service = locator.getApplicationService("testuser", "123456", "demo");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSearchApplicationsByName() {
		try {
			Collection<?> appList = service.searchApplicationsByName("Default");
			assertEquals(1, appList.size());
		} catch (ApplicationServiceFault e) {
			e.printStackTrace();
			assertFalse(true);
		} catch (RemoteException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

	public void testSearchApplicationByName() {
		String name = "Default";
		try {
			SimpleApplication sApp = service.searchApplicationByName(name);
			assertNotNull(sApp);
			assertEquals(sApp.getName(), name);
		} catch (ApplicationServiceFault e) {
			e.printStackTrace();
			assertFalse(true);
		} catch (RemoteException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

	public void testSearchApplicationsByDeveloper() {
		String developerId = "11de-6490-fb8f8a71-a277-4d6795586527";
		try {
			Collection<?> appList = service.searchApplicationsByDeveloper(developerId);
			assertEquals(1, appList.size());
		} catch (ApplicationServiceFault e) {
			e.printStackTrace();
			assertFalse(true);
		} catch (RemoteException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

	public void testSearchApplicationsByDomainAdmin() {
		String id = "11de-7025-7d13e487-97f9-19f066c2a931";
		try {
			Collection<?> appList = service.searchApplicationsByDomainAdmin(id);
			assertEquals(1, appList.size());
		} catch (ApplicationServiceFault e) {
			e.printStackTrace();
			assertFalse(true);
		} catch (RemoteException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

	public void testAddApplication() {
		String userAccount = "admin";
		String domainName = "d1";
		String applicationId = "11de-6496-712b04af-a277-4d6795586527";
		try {
			boolean isVaild = service.addApplication(userAccount, domainName, applicationId);
			assertTrue(isVaild);
		} catch (ApplicationServiceFault e) {
			e.printStackTrace();
			assertFalse(true);
		} catch (RemoteException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
}
