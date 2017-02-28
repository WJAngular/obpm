package cn.myapps.webservice.client;

import java.rmi.RemoteException;

import cn.myapps.webservice.fault.UserServiceFault;
import cn.myapps.webservice.model.SimpleUser;
import junit.framework.TestCase;

public class UserServiceTest extends TestCase {
	UserService service;

	protected void setUp() throws Exception {
		super.setUp();
		UserServiceServiceLocator locator = new UserServiceServiceLocator();
		service = locator.getUserService();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testValidateUser() {
		// step1. 已域用户身份登陆
		String domainName = "d1";
		String userAccount = "d1sec";
		String userPassword = "123";
		int userType = 0; // 域用户

		try {
			SimpleUser user = service.validateUser(domainName, userAccount, userPassword, userType);
			assertNotNull(user);
		} catch (UserServiceFault e) {
			// 不经过此
			assertFalse(true);
		} catch (RemoteException e) {
			// 不经过此
			assertFalse(true);
		}

		// step2. 身份改变后登陆无效
		userType = 1; // 管理员
		try {
			service.validateUser(domainName, userAccount, userPassword, userType);
			// 不经过此
			assertTrue(false);
		} catch (UserServiceFault e) {
			// 经过此处
			assertTrue(e.getMessage(), true);
		} catch (RemoteException e) {
			assertFalse(true);
		}

		// step3. 管理员登陆
		userAccount = "admin";
		userPassword = "123";
		try {
			SimpleUser validateUser = service.validateUser(domainName, userAccount, userPassword, userType);
			assertNotNull(validateUser);
			assertFalse(true);
		} catch (UserServiceFault e) {
			// 不经过此
			assertFalse(true);
		} catch (RemoteException e) {
			// 不经过此
			assertFalse(true);
		}
	}

}
