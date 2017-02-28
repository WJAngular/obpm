package cn.myapps.core.xmpp.service;

import cn.myapps.constans.Environment;
import junit.framework.TestCase;

public class MenuServiceTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetAllMenusByUser() {
		MenuService service = new MenuService();
		Environment.getInstance().setContextPath("/");
		service.getAllMenusByUser("testuser", "demo");
		System.out.println("From: " + service.getFrom());
		System.out.println(service.toXML());
	}

}
