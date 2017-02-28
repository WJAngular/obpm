package cn.myapps.core.validate.repository.action;

import java.util.Map;

import junit.framework.TestCase;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryProcess;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryVO;
import cn.myapps.util.ProcessFactory;

public class ValidateRepositoryHelperTest extends TestCase {

	ValidateRepositoryHelper helper = null;

	protected void setUp() throws Exception {
		super.setUp();
		helper = new ValidateRepositoryHelper();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for
	 * 'cn.myapps.core.validate.repository.action.ValidateRepositoryHelper.get_validate()'
	 */
	public void testGet_validate() throws Exception {
		//String moduleid = null;
		ModuleProcess dp = (ModuleProcess) ProcessFactory
				.createProcess(ModuleProcess.class);
		ValidateRepositoryProcess vp = (ValidateRepositoryProcess) ProcessFactory
				.createProcess(ValidateRepositoryProcess.class);
		//Collection coll = dp.doSimpleQuery(null, null);
		//ModuleVO mod = (ModuleVO) coll.iterator().next();
		// helper.set_moduleid(mod.getId());
		// Map map = helper.get_validate();
		Map<String, String> map = new ValidateRepositoryHelper()
				.get_validate("11de-96ab-1cd52bcd-979f-7d180a5b557b");
		if (map != null) {
			assertEquals(1, map.size());
		}
		ValidateRepositoryVO vo = new ValidateRepositoryVO();
		vo.setName("name");
		// vo.setModule(mod);
		// vo.setApplication(mod.getApplication());
		vp.doCreate(vo);

		// Map map1 = helper.get_validate();
		// if (map == null)
		// assertEquals(1, map1.size());
		// else
		// assertEquals(map.size() + 1, map1.size());
		dp.doRemove(vo.getId());
		helper.get_validate(vo.getApplicationid());

	}

}
