package cn.myapps.core.deploy.module.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.action.ApplicationAction;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.util.sequence.Sequence;

/**
 * @author nicholas
 */
public class ModuleActionTest extends TestCase {

	ModuleAction action;
	ModuleAction action2;
	ApplicationAction appaction;
	String name1 = null;
	String name2 = null;
	String appname = null;
	ModuleVO movo1 = new ModuleVO();
	ModuleVO movo2 = new ModuleVO();

	ApplicationVO appvo = new ApplicationVO();

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ModuleActionTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		action = new ModuleAction();
		action2 = new ModuleAction();
		appaction = new ApplicationAction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doSave()'
	 */
	public void testDoSave() throws Exception {

		// //PersistenceUtils.getSessionSignal().sessionSignal++;
		name1 = Sequence.getSequence() + "ModuleName111";
		name2 = Sequence.getSequence() + "ModuleName222";
		appname = "ApplicationName";
		movo1.setName(name1);
		movo2.setName(name2);

		appvo.setName(appname);
		movo1.setApplication(appvo);
		movo2.setApplication(appvo);
		action.setContent(movo1);
		action2.setContent(movo2);
		appaction.setContent(appvo);
		appaction.doSave();
		action.doSave();
		action2.doSave();
		// //PersistenceUtils.getSessionSignal().sessionSignal--;
		PersistenceUtils.closeSession();

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

		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("id", new String[] { id });

		BaseAction.getContext().setParameters(mp);
		action.doView();

	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doList()'
	 */
	public void doList() throws Exception {

		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("s_name", name1);
		BaseAction.getContext().setParameters(mp);
		action.doList();
		Collection<ModuleVO> data = action.getDatas().datas;
		assertNotNull(data);
		ModuleVO movos = (ModuleVO) data.iterator().next();
		assertEquals(movos.getName(), name1);
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doEdit()'
	 */
	public void doEdit() throws Exception {

		String id = action.getContent().getId();

		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("id", new String[] { id });

		BaseAction.getContext().setParameters(mp);
		action.doEdit();
	}

	/*
	 * Test method for
	 * 'cn.myapps.core.deploy.application.action.ApplicationAction.doDelete()'
	 */
	public void doDelete() throws Exception {
		// //PersistenceUtils.getSessionSignal().sessionSignal++;
		String id = action.getContent().getId();
		String id2 = action2.getContent().getId();
		action.set_selects(new String[] { id, id2 });
		action.doDelete();
		String appid = appaction.getContent().getId();

		appaction.set_selects(new String[] { appid });

		appaction.doDelete();
		// //PersistenceUtils.getSessionSignal().sessionSignal--;
		PersistenceUtils.closeSession();

	}

}
