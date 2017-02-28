package cn.myapps.core.workflow.element;

import junit.framework.TestCase;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.util.ProcessFactory;

public class FlowDiagramTest extends TestCase {
	BillDefiProcess bp;

	protected void setUp() throws Exception {
		bp = (BillDefiProcess) ProcessFactory
				.createProcess(BillDefiProcess.class);
		bp.doQuery(new ParamsTable());
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for
	 * 'cn.myapps.core.workflow.element.FlowDiagram.toJpegImage(File)'
	 */
	public void testToJpegImageFile() throws Exception {
		// SessionSignal signal = PersistenceUtils.getSessionSignal();
		// signal.sessionSignal++;
		/*
		 * Just for test FileInputStream fis = new
		 * FileInputStream("C:\\WORKFLOW.TXT");
		 * 
		 * InputStreamReader isr = new InputStreamReader(fis);
		 * 
		 * BufferedReader br = new BufferedReader(isr);
		 * 
		 * StringBuffer sb = new StringBuffer();
		 * 
		 * while (true) { String s = br.readLine(); if (s == null) { break; }
		 * else { sb.append(s + "\n"); } }
		 * 
		 * // BillDefiVO flowVO = (BillDefiVO)bp.doView("1164598740139000");
		 * WFRunner wfr = new WFRunner(sb.toString(), "aannvv"); FlowDiagram fd
		 * = wfr.getFlowDiagram();
		 * fd.setFlowstatus(FlowType.FLOWSTATUS_OPEN_RUN_RUNNING);
		 * 
		 * 
		 * ImageUtil imageUtil = new ImageUtil(fd); imageUtil.toImage(new
		 * File("c://flowImage.jpg")); imageUtil.toMobileImage(new
		 * File("c://flowMobile.png"));
		 * 
		 * // PersistenceUtils.closeSession();
		 */
	}
}
