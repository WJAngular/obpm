package cn.myapps.core.validate.repository.action;

import java.util.Collection;
import java.util.HashMap;

import junit.framework.TestCase;
import cn.myapps.base.action.BaseAction;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryVO;

public class ValidateRepositoryActionTest extends TestCase {

	ValidateRepositoryAction action=null;
	ValidateRepositoryVO vo=null;
	String name=null;
	protected void setUp() throws Exception {
		super.setUp();
		action=new ValidateRepositoryAction();
		vo=new ValidateRepositoryVO();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'cn.myapps.core.validate.repository.action.ValidateRepositoryAction.doSave()'
	 */
	public void testDoSave() throws Exception {
		name="test";
		vo.setName(name);
		action.setContent(vo);
		action.doSave();
		doView();
		doList();
		doEdit();
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doEdit()'
	 */
	public void doEdit() {

	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doView()'
	 */
	public void doView() throws Exception {
		String id = action.getContent().getId();

		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("id", new String[] { id });

		BaseAction.getContext().setParameters(mp);
		action.doView();

		String afterViewId = action.getContent().getId();
		assertEquals(afterViewId, id);
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doDelete()'
	 */
	public void doDelete() throws Exception {
		String id = action.getContent().getId();
		action.set_selects(new String[] { id });
		action.doDelete();

	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doList()'
	 */
	public void doList() throws Exception {
		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("s_name", name);
		BaseAction.getContext().setParameters(mp);
		action.doList();
		Collection<ValidateRepositoryVO> data = action.getDatas().datas;
		assertNotNull(data);
		//DataSource ds = (DataSource) data.iterator().next();
		//assertEquals(ds.getName(), name);

	}

}
