package cn.myapps.core.expimp.exp.action;

import junit.framework.TestCase;
import cn.myapps.constans.Environment;

public class ExpActionTest extends TestCase {
	private Environment evt;
 	// private Logger _log = Logger.getLogger(ExpActionTest.class);

	protected void setUp() throws Exception {
		super.setUp();
		evt = Environment.getInstance(); // ����·��c:\downloads\export
		evt.setApplicationRealPath("c:/");
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
}
