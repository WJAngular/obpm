package cn.myapps.core.workflow.notification.ejb;

import junit.framework.TestCase;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.util.ProcessFactory;

public class NotificationProcessTest extends TestCase {
	private final static String APPLICATION_ID = "01b5d247-f18b-35e0-801a-23cb89ac6e37";

	NotificationProcess process;

	DocumentProcess docProcess;

	BillDefiProcess billDefiProcess;

	protected void setUp() throws Exception {
		super.setUp();
		process = new NotificationProcessBean(APPLICATION_ID);
		docProcess = new DocumentProcessBean(APPLICATION_ID);
		billDefiProcess = (BillDefiProcess) ProcessFactory
				.createProcess(BillDefiProcess.class);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testNotifyResponsiblesAfterApproved() throws Exception {
		Document doc = (Document) docProcess
				.doView("01b79fbc-9795-7340-ad25-d86b92896303");
		BillDefiVO flow = doc.getFlowVO();
		assertNotNull(flow);
		//process.notifyCurrentAuditors(doc, flow);
	}
	
	public void testNotifyOverDueAuditors() throws Exception {
		process.notifyOverDueAuditors();
	}
}
