package cn.myapps.core.macro.repository.action;

import java.util.Collection;
import java.util.HashMap;

import cn.myapps.base.action.BaseAction;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.action.ApplicationAction;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.module.action.ModuleAction;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.macro.repository.ejb.RepositoryVO;
import cn.myapps.util.sequence.Sequence;
import junit.framework.TestCase;

public class RepositoryActionTest extends TestCase {

	   RepositoryAction action;
	   ModuleAction moduleaction;
	   ApplicationAction appaction;
	   String name=null;
	   RepositoryVO rep=new RepositoryVO();
	   ApplicationVO appvo=new ApplicationVO();
	   ModuleVO movo=new ModuleVO();
	   String applicationid=null;
	   String  moduleid=null;
	protected void setUp() throws Exception {
		super.setUp();
		action=new RepositoryAction();
		moduleaction=new ModuleAction();
		appaction=new ApplicationAction();
		
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'cn.myapps.core.macro.repository.action.RepositoryAction.doSave()'
	 */
	public void testDoSave() throws Exception{
		applicationid=Sequence.getSequence();
		appvo.setId(applicationid);
		name=Sequence.getSequence()+"name";
		rep.setName(name);
		
		moduleid=Sequence.getSequence();
		movo.setId(moduleid);
		
		action.setContent(rep);
		
	//	action.set_applicationid(appvo.getId());
//		action.set_moduleid(movo.getId());
		action.doSave();
		doView();
		doList();
		doEdit();
		doDelete();
	
		
		

	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doNew()'
	 */
	public void testDoNew() {

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
	 * Test method for 'cn.myapps.base.action.BaseAction.doView()'
	 */
	public void doView() throws Exception{

		String id = action.getContent().getId();

		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("id", new String[] { id });

		BaseAction.getContext().setParameters(mp);
		action.doView();
	}

	
	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doList()'
	 */
	public void doList() throws Exception{
		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("s_name", name);
		BaseAction.getContext().setParameters(mp);
		action.doList();
		moduleaction.doList();
		appaction.doList();
		Collection<RepositoryVO> data = action.getDatas().datas;
		assertNotNull(data);
		RepositoryVO repvo=(RepositoryVO)data.iterator().next();
		assertEquals(repvo.getName(), name);

	}
	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doDelete()'
	 */
	public void doDelete()throws Exception {
		//PersistenceUtils.getSessionSignal().sessionSignal++;
		String id = action.getContent().getId();

		action.set_selects(new String[] { id });
		action.doDelete();
       
		//PersistenceUtils.getSessionSignal().sessionSignal--;
		PersistenceUtils.closeSession();


	}

}
