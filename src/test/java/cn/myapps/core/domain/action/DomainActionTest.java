package cn.myapps.core.domain.action;

import java.util.Collection;
import java.util.HashMap;

import junit.framework.TestCase;
import cn.myapps.base.action.BaseAction;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.util.sequence.Sequence;

/**
 * @author chris
 */
public class DomainActionTest extends TestCase {

	private DomainAction action;
	String domainName = null;
	DomainVO domain = new DomainVO();

	protected void setUp() throws Exception {
		super.setUp();
		action = new DomainAction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'cn.myapps.core.action.DepartmentAction.doSave()'
	 */

	public void testDoSave() throws Exception {
		domainName = "teemlink";
		// domain.setId(Sequence.getSequence());
		domain.setName(domainName);
		domain.setApplicationid(Sequence.getSequence());
		domain.setDescription("域");
		// DepartmentVO dpt = new DepartmentVO();
		// dpt.setName("dptfortest1");
		// domain.getDepartments().add(dpt);
		action.setContent(domain);
		action.doSave(); // 保存前检索是否有相同名字。
		// doView();
		doList();
		doEdit();
		doDelete();

	}

	/*
	 * Test method for 'cn.myapps.core.action.DepartmentAction.doView()'
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
	 * Test method for 'cn.myapps.core.action.DepartmentAction.doList()'
	 */
	public void doList() throws Exception {

		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("s_name", domainName);
		BaseAction.getContext().setParameters(mp);
		action.doList();
		Collection<DomainVO> data = action.getDatas().datas;
		assertNotNull(data);
		DomainVO dep = data.iterator().next();
		assertEquals(dep.getName(), domainName);

	}

	/*
	 * Test method for 'cn.myapps.core.action.DepartmentAction.doEdit()'
	 */
	public void doEdit() throws Exception {

		String id = action.getContent().getId();
		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("id", new String[] { id });

		BaseAction.getContext().setParameters(mp);
		action.doEdit();

	}

	/*
	 * Test method for 'cn.myapps.core.action.DepartmentAction.doDelete()'
	 */

	public void doDelete() throws Exception {

		String id = action.getContent().getId();
		action.set_selects(new String[] { id });
		action.doDelete();
		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("s_name", domainName);
		BaseAction.getContext().setParameters(mp);
		action.doList();
		Collection<DomainVO> data = action.getDatas().datas;
		assertFalse(data.size() > 0);

	}

}
