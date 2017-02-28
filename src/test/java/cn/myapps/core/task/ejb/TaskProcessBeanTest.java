package cn.myapps.core.task.ejb;

import junit.framework.TestCase;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

public class TaskProcessBeanTest extends TestCase {
	private TaskProcess process;

	private Task vo;

	protected void setUp() throws Exception {
		super.setUp();
		process = (TaskProcess) ProcessFactory.createProcess(TaskProcess.class);
		vo = new Task();
		vo.setId(Sequence.getSequence());
		vo.setName("TestTask");
		vo.getDaysOfWeek().add(Integer.valueOf(1));
		vo.getDaysOfWeek().add(Integer.valueOf(3));
		vo.getDaysOfWeek().add(Integer.valueOf(5));

		process.doCreate(vo);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		Task po = (Task) process.doView(vo.getId());

		process.doRemove(po);
	}

	public void testDoQueryString() {
		fail("Not yet implemented");
	}

	public void testDoUpdate() {
		fail("Not yet implemented");
	}
}
