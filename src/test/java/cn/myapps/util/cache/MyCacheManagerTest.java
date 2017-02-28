package cn.myapps.util.cache;

import java.io.IOException;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentProcessBean;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.util.ProcessFactory;

import junit.framework.TestCase;

public class MyCacheManagerTest extends TestCase {
	private static final String DEPARTMENT_ID = "D-07-03-02";

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MyCacheManagerTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'cn.myapps.util.cache.MyCacheManager.getInstance()'
	 */
	public void testGetInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			IOException {
		ICacheProvider provider = MyCacheManager.getProviderInstance();
		CacheConfig cconf = CacheConfig.getInstance();

		for (Iterator<MethodCacheConfig> iter = cconf.getMethodCacheConfigs().values().iterator(); iter.hasNext();) {
			MethodCacheConfig mcc = iter.next();
			assertNotNull(provider.getCache(mcc.getSignature()));
		}

	}

	public void testCache() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException,
			InterruptedException {
		ICacheProvider provider = MyCacheManager.getProviderInstance();
		CacheConfig cconf = CacheConfig.getInstance();

		for (Iterator<MethodCacheConfig> iter = cconf.getMethodCacheConfigs().values().iterator(); iter.hasNext();) {
			MethodCacheConfig mcc = iter.next();
			IMyCache mycache = provider.getCache(mcc.getSignature());
			assertNotNull(mycache);

			mycache.put("hello", "hellovalue");
			// wait(5000);
			Object obj = ((IMyElement) mycache.get("hello")).getValue();
			assertNotNull(obj);
			assertEquals("hellovalue", obj);
		}

	}

	public void testBuildCacheKeyString() throws IOException, SecurityException, NoSuchMethodException {
		// (""+MyCacheManager.buildCacheKeyString(DepartmentProcessBean.class.getMethod("doSimpleQuery",new
		// Class[]{ParamsTable.class})));
		assertEquals(
				"cn.myapps.core.department.ejb.DepartmentProcessBean.doSimpleQuery(cn.myapps.base.action.ParamsTable)",
				MyCacheManager.buildCacheKeyString(DepartmentProcessBean.class.getMethod("doSimpleQuery",
						new Class[] { ParamsTable.class })));
	}

	public void testCleanCacheByCacheName() throws Exception {
		ICacheProvider provider = MyCacheManager.getProviderInstance();

		String cacheName = MyCacheManager.buildCacheKeyString(DepartmentProcessBean.class, DepartmentProcessBean.class
				.getMethod("doUpdate", new Class[] { ValueObject.class }));

		assertTrue(provider.clearByCacheName(cacheName));

		DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		// ��ݿ����������һ�����
		process.doView(DEPARTMENT_ID);
		PersistenceUtils.closeSession();
		DepartmentVO dept = (DepartmentVO) process.doView(DEPARTMENT_ID);
		process.doUpdate(dept);
		PersistenceUtils.closeSession();
		process.doView(DEPARTMENT_ID);
	}
}
