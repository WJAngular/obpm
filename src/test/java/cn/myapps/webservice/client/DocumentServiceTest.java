package cn.myapps.webservice.client;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;
import cn.myapps.webservice.fault.DocumentServiceFault;
import cn.myapps.webservice.model.SimpleDocument;

public class DocumentServiceTest extends TestCase {
	DocumentService service;

	protected void setUp() throws Exception {
		super.setUp();
		DocumentServiceServiceLocator locator = new DocumentServiceServiceLocator();
		service = locator.getDocumentService("testuser", "123456", "demo");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateDocumentByDomainUser() throws ServiceException {
		DocumentServiceServiceLocator locator = new DocumentServiceServiceLocator();
		// 根据用户信息获取DocumentService
		DocumentService service = locator.getDocumentService("testuser", "123456", "demo");
		
		// 表单名称
		String formName = "分散聚合类型节点";
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("字段1", "字段1的值");
		parameters.put("字段2", "字段2的值");
		// 流程名称
		parameters.put("flowname", "聚合分散"); 
		
		// myApps功能示例应用
		String applicationId = "11de-ef9e-c010eee1-860c-e1cadb714510";
		// 企业域用户ID, 这里使用testuser用户的ID
		String domainUserId = "11de-c13a-0cf76f8b-a3db-1bc87eaaad4c"; 

		try {
			service.createDocumentByDomainUser(formName, parameters, domainUserId, applicationId);
		} catch (DocumentServiceFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void testCreateDocumentByGuest() {
		String formName = "fm_dayoff_copy27";
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("姓名", "nicholas0001");
		String applicationId = "01b98ff4-8d8c-b3c0-8d30-ece2aa60d534"; // Default应用

		try {
			service.createDocumentByGuest(formName, parameters, applicationId);
		} catch (DocumentServiceFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void testUpdateDocumentByGuest() {
		fail("Not yet implemented");
	}

	public void testUpdateDocumentByDomainUser() {
		fail("Not yet implemented");
	}

	public void testSearchDocumentsByFilter() {
		String formName = "fm_dayoff_copy27";
		HashMap<String, String> parameters = new HashMap<String, String>();
		String applicationId = "01b98ff4-8d8c-b3c0-8d30-ece2aa60d534"; // Default应用
		try {
			Object[] documents = service.searchDocumentsByFilter(formName, parameters, applicationId);
			for (int i = 0; i < documents.length; i++) {
				SimpleDocument doc = (SimpleDocument) documents[i];
				assertNotNull(doc);
			}
		} catch (DocumentServiceFault e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (RemoteException e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}

	public void testSearchDocumentByFilter() {
		fail("Not yet implemented");
	}

}
