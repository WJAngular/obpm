package cn.myapps.km.permission.ejb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.util.PersistenceUtils;

public class ManagePermissionProcessBeanTest {
	
	ManagePermissionProcess process = null;
	
	@Before
	public void setUp() throws Exception {
		process =new ManagePermissionProcessBean();
	}

	@After
	public void tearDown() throws Exception {
		PersistenceUtils.closeConnection();
	}
	
	@Test
	public void test() throws Exception {
		
		String id = "1111-2222-abcd-5678-aaaa";
		String domainId = "1211-2222-abcd-5678-aaaa";
		
		try {
			ManagePermission p = new ManagePermission();
			p.setId(id);
			p.setName("insert");
			p.setDomainId(domainId);
			p.setResourceType(ManagePermission.RESOURCE_TYPE_DIR);
			p.setResource("123123-123123-123132-123");
			p.setScope(ManagePermission.SCOPE_USER);
			p.setOwnerIds("123-123-123-123-sdda");
			p.setOwnerNames("Willie Lau");
			
			process.doCreate(p);
			ManagePermission p2 = (ManagePermission) process.doView(id);
			assert(p2.getId().equals(id));
			
			p2.setName("update");
			process.doUpdate(p2);
			
			ManagePermission p3 = (ManagePermission) process.doView(id);
			assert(p3.getName().equals("update"));
			
			process.doRemove(p3.getId());
			ManagePermission p4 = (ManagePermission) process.doView(id);
			assert(p4 == null);
			
		} catch (Exception e) {
			throw e;
		}
		
	}

}
