package cn.myapps.core.homepage.ejb;

import cn.myapps.util.ProcessFactory;
import junit.framework.TestCase;

public class ReminderProcessTest extends TestCase {
	private ReminderProcess process;

	private String FORM_ID = "01b573bf-7b7d-1760-b0c6-04b592698388";

	private String APPLICATIOND_ID = "01b56341-3591-5e20-b09f-3f30bfefdcda";

	protected void setUp() throws Exception {
		super.setUp();
		process = (ReminderProcess) ProcessFactory
				.createProcess(ReminderProcess.class);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDoViewByForm() throws Exception {
		Reminder reminder = process.doViewByForm(FORM_ID, APPLICATIOND_ID);
		assertNotNull(reminder);
	}
}
