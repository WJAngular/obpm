package cn.myapps.webservice;

import cn.myapps.webservice.fault.UserServiceFault;
import cn.myapps.webservice.model.SimpleUser;
import junit.framework.TestCase;

public class UserServiceTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateUser() throws UserServiceFault {
		UserService userService = new UserService();
		SimpleUser user = new SimpleUser();
		user.setId("0001");
		user.setName("用户1");
		user.setDomainName("demo2");
		user.setLoginno("u1");
		user.setLoginpwd("u1");

		userService.createUser(user);
	}

	public void testUpdateUser() {
		fail("Not yet implemented");
	}

	public void testDeleteUserString() {
		fail("Not yet implemented");
	}

	public void testDeleteUserSimpleUser() {
		fail("Not yet implemented");
	}

	public void testSetRoleSet() {
		fail("Not yet implemented");
	}

	public void testSetDepartmentSet() {
		fail("Not yet implemented");
	}

}
