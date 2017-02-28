package cn.myapps.core.dynaform.dts.exp.columnmapping.action;

import java.util.Collection;
import java.util.HashMap;

import junit.framework.TestCase;
import cn.myapps.base.action.BaseAction;
import cn.myapps.core.dynaform.dts.exp.columnmapping.ejb.ColumnMapping;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfigProcess;
import cn.myapps.util.ProcessFactory;

/**
 * @author  nicholas
 */
public class ColumnMappingActionTest extends TestCase {

	ColumnMappingAction action;
	MappingConfig mc;
	ColumnMapping vo;
//	String name;
	protected void setUp() throws Exception {
		super.setUp();
		action=new ColumnMappingAction();
		mc=new MappingConfig();
		vo=new ColumnMapping();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'cn.myapps.core.dynaform.dts.exp.columnmapping.action.ColumnMappingAction.doSave()'
	 */
	public void testDoSave() throws Exception {
		MappingConfigProcess mp = (MappingConfigProcess) (ProcessFactory
					.createProcess(MappingConfigProcess.class));
		mp.doCreate(mc);
		action.setMappingid(mc.getId());
		
		vo.setType(ColumnMapping.COLUMN_TYPE_FIELD);
		vo.setFromName("test");
		vo.setToName("test");
		vo.setToType(ColumnMapping.DATA_TYPE_VARCHAR);
		vo.setLength("255");
		action.setContent(vo);
		action.doSave();
		
		doList();
		doView();
		 doEdit();
		 doDelete();

		mp.doRemove(mc.getId());
		

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
		mp.put("s_toname", "test");
		BaseAction.getContext().setParameters(mp);
		action.doList();
		Collection<?> data = action.getDatas().datas;
		assertNotNull(data);
		ColumnMapping ds = (ColumnMapping) data.iterator().next();
		assertEquals(ds.getToName(), "test");
	}

}
