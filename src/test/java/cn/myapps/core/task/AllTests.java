package cn.myapps.core.task;

import junit.framework.Test;
import junit.framework.TestSuite;
import cn.myapps.core.task.ejb.TaskTest;

public final class AllTests {

	/**
	 * Hide constructor to prevent generation of class instances.
	 * <p>
	 */
	private AllTests() {

		// empty
	}

	/**
	 * Returns the JUnit test suite for this package.
	 * <p>
	 * 
	 * @return the JUnit test suite for this package
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for package "
				+ AllTests.class.getPackage().getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(TaskTest.class);
		// $JUnit-END$
		return suite;
	}
}