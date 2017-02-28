package cn.myapps.core.deploy.module.action;

import cn.myapps.base.action.BaseAction;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.action.ApplicationAction;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.web.DWRHtmlUtils;
import java.util.*;
import junit.framework.TestCase;

/**
 * @author nicholas
 */
public class ModuleHelperTest extends TestCase {

	ModuleHelper helper;
	ModuleAction action;
	ModuleAction action2;
	ApplicationAction appaction;
	String name1 = null;
	String name2 = null;
	String appname = null;
	ModuleVO movo1 = new ModuleVO();
	ModuleVO movo2 = new ModuleVO();
	ApplicationVO appvo = new ApplicationVO();

	protected void setUp() throws Exception {
		super.setUp();
		helper = new ModuleHelper();
		action = new ModuleAction();
		action2 = new ModuleAction();
		appaction = new ApplicationAction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for
	 * 'cn.myapps.core.deploy.module.action.ModuleHelper.createSuperiorOptionFunc(String,
	 * String, String, String)'
	 */
	public void testCreateSuperiorOptionFunc() throws Exception {

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
		String excludeNodeId = "";
		String applicationId = appvo.getId();
		String selectFieldName = "id";
		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("s_name", name1);
		BaseAction.getContext().setParameters(mp);
		action.doList();
		Collection<ModuleVO> data = action.getDatas().datas;
		assertNotNull(data);
		ModuleVO movos = (ModuleVO) data.iterator().next();
		assertEquals(movos.getName(), name1);

		String s = helper.createSuperiorOptionFunc(selectFieldName, applicationId, excludeNodeId, "", null);

		assertTrue(s.indexOf(action.getContent().getId()) != -1);// 断言是否包含有id
		doGet_moduleList();
		doCreateQueryOption();
		doDelete();

	}

	/*
	 * Test method for
	 * 'cn.myapps.core.deploy.module.action.ModuleHelper.get_moduleList()'
	 */
	public void doGet_moduleList() throws Exception {

		ModuleProcess mp = (ModuleProcess) ProcessFactory.createProcess(ModuleProcess.class);
		Collection<ModuleVO> col = helper.get_moduleList(null);
		String moduleid = "id";

		Map<String, String> dm = mp.deepSearchModuleTree(col, appvo.getId(), null, "", 0);
		assertTrue(DWRHtmlUtils.createOptions(dm, moduleid, "").indexOf(action.getContent().getId()) != -1);// //断言是否包含有id

	}

	/*
	 * Test method for
	 * 'cn.myapps.core.deploy.module.action.ModuleHelper.createQueryOption(String,
	 * String, String, String)'
	 */
	public void doCreateQueryOption() throws Exception {

//		String selectFieldName = "application";
//		String applicationId = appvo.getId();
//		String moduleid = action.getContent().getId();
//		String def = "";
		// assertFalse(
		// helper.createQueryOption(selectFieldName,applicationId,moduleid,def,
		// null).contains(appvo.getId()));//断言是否包含有applicationid

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
