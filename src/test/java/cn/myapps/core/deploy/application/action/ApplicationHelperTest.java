package cn.myapps.core.deploy.application.action;

import java.util.Collection;

import junit.framework.TestCase;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;

/**
 * @author nicholas
 */
public class ApplicationHelperTest extends TestCase {

	ApplicationHelper helper;
	ApplicationVO appvo = new ApplicationVO();

	protected void setUp() throws Exception {
		super.setUp();
		helper = new ApplicationHelper();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public ApplicationVO getAppvo() {
		return this.appvo;
	}

	public void setAppvo(ApplicationVO appvo) {
		this.appvo = appvo;
	}

	/*
	 * Test method for
	 * 'cn.myapps.core.deploy.application.action.ApplicationHelper.get_applicationList()'
	 */
	public void testGet_applicationList() throws Exception {

		Collection<ApplicationVO> col = helper.getAppList();

		assertNull(col);

	}

}
