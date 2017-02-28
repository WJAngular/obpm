package cn.myapps.core.deploy.application.action;

import java.util.Collection;
import java.util.HashMap;

import junit.framework.TestCase;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.util.sequence.Sequence;

/**
 * @author nicholas
 */
public class ApplicationActionTest extends TestCase {
	ApplicationAction action;

	String appname = null;

	ApplicationVO appvo;

	protected void setUp() throws Exception {
		super.setUp();
		action = new ApplicationAction();
		appvo = new ApplicationVO();

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doSave()'
	 */
	public void testDoSave() throws Exception {
		appname = Sequence.getSequence() + "ApplicationName";

		appvo.setName(appname);

		action.setContent(appvo);

		action.doSave();

		doView();

		doList();

		doEdit();

		doDelete();

	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doView()'
	 */
	public void doView() throws Exception {
		String id = action.getContent().getId();

		HashMap<String, String[]> mp = new HashMap<String, String[]>();
		mp.put("id", new String[] { id });

		// action.getContext().setParameters(mp);
		action.doView();

	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doList()'
	 */
	public void doList() throws Exception {

		HashMap<String, String> mp = new HashMap<String, String>();
		mp.put("s_name", appname);
		// action.getContext().setParameters(mp);
		action.doList();
		Collection<ApplicationVO> data = action.getDatas().datas;
		assertNotNull(data);
		ApplicationVO appvos = (ApplicationVO) data.iterator().next();

		assertEquals(appvos.getName(), appname);
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doEdit()'
	 */
	public void doEdit() throws Exception {

		String id = action.getContent().getId();

		HashMap<String, String[]> mp = new HashMap<String, String[]>();
		mp.put("id", new String[] { id });

		// action.getContext().setParameters(mp);
		action.doEdit();

	}

	/*
	 * Test method for
	 * 'cn.myapps.core.deploy.application.action.ApplicationAction.doDelete()'
	 */
	public void doDelete() throws Exception {
		String id = action.getContent().getId();

		action.set_selects(new String[] { id });

		action.doDelete();
		PersistenceUtils.closeSession();

	}

}
