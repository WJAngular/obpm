package cn.myapps.core.dynaform.dts.exp.mappingconfig.action;

import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfigProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;
import java.util.Collection;
import junit.framework.TestCase;

/**
 * @author  nicholas
 */
public class MappingConfigHelperTest extends TestCase {

	MappingConfigHelper helper=null;


	protected void setUp() throws Exception {
		super.setUp();
		helper=new MappingConfigHelper();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'cn.myapps.core.dynaform.dts.exp.mappingconfig.action.MappingConfigHelper.get_allMappingConifgs()'
	 */
	public void testGet_allMappingConifgs(String application) throws Exception {
		Collection<?> coll=helper.get_allMappingConifgs(application);
		MappingConfigProcess mp=(MappingConfigProcess)ProcessFactory.createProcess(MappingConfigProcess.class);
		MappingConfig mc=new MappingConfig();
		mc.setId(Sequence.getSequence());
		mc.setName("test");
		mp.doUpdate(mc);
		Collection<?> coll2=helper.get_allMappingConifgs(application);
		if(coll==null)
			assertEquals(1,coll2.size());
		else
		assertEquals(coll.size()+1,coll2.size());
		mp.doRemove(mc.getId());
	}

}
