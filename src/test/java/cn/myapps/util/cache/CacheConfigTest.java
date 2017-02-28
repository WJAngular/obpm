package cn.myapps.util.cache;

import junit.framework.TestCase;

public class CacheConfigTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(CacheConfigTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'cn.myapps.util.cache.CacheConfig.createCacheConfig()'
	 */
	public void testGetInstance() throws Exception {
		CacheConfig cc = CacheConfig.getInstance();
		assertEquals(cc.getProviderClassName(), EhcacheProvider.class.getName());
		assertTrue(cc.getMethodCacheConfigs().size()>0);
		
		ICacheProvider provider = MyCacheManager.getProviderInstance();
		String[] cacheNames = provider.getCacheNames();
		assertNotNull(cacheNames);
	}

	public void testInitCacheClear() throws Exception {
		CacheConfig cc = CacheConfig.getInstance();
		assertTrue(cc.getMethodCleaners().size() > 0);
	}
}
