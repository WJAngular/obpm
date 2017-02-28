package cn.myapps.util.cache;

import java.lang.reflect.Method;

import junit.framework.TestCase;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.department.ejb.DepartmentProcessBean;

public class CacheKeyTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(CacheKeyTest.class);
	}

	/*
	 * Test method for 'cn.myapps.util.cache.CacheKey.equals(Object)'
	 */
	public void testEqualsObject() throws SecurityException, NoSuchMethodException {
		Method method = DepartmentProcessBean.class
		.getMethod("doSimpleQuery",
				new Class[] { ParamsTable.class });
		
		ParamsTable params1 = new ParamsTable();
		ParamsTable params2 = new ParamsTable();
		
		CacheKey ck1 = new CacheKey("hello", method, new ParamsTable[]{params1});
		CacheKey ck2 = new CacheKey("hello", method, new ParamsTable[]{params2});
		
		assertTrue(ck1.equals(ck2));
		
		params1.setParameter("key1","value1");
		params2.setParameter("key1","value1");
		
		assertTrue(ck1.equals(ck2));
		
		params1.setParameter("key2","value2");
		params2.setParameter("key2","");
		
		assertFalse(ck1.equals(ck2));
	}

}
