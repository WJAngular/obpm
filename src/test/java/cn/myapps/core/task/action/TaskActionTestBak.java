package cn.myapps.core.task.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import cn.myapps.base.action.BaseAction;
import cn.myapps.core.task.ejb.Task;
import cn.myapps.core.task.ejb.TaskConstants;
import cn.myapps.util.timer.TimerRunner;

public class TaskActionTestBak extends TestCase {
	TaskAction action;

	Calendar calendar = Calendar.getInstance();

	Map<String, Object> params = new HashMap<String, Object>();

	protected void setUp() throws Exception {
		super.setUp();
		action = new TaskAction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAction() throws Exception {
		String id = doSave();
		doStart(id);
		doStop(id);
		doEdit(id);
		doDelete(id);
	}

	/*
	 * Test method for 'cn.myapps.core.task.action.TaskAction.doStart()'
	 */
	@SuppressWarnings("deprecation")
	public void doStart(String id) throws Exception {
		// PersistenceUtils.getSessionSignal().sessionSignal++;
		params.put("id", new String[] { id });
		BaseAction.getContext().setParameters(params);
		action.doView();
		Date da = new Date();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, da.getHours() + 1);
		// action.set_time(dateStr);
		action.doStart();
		// PersistenceUtils.getSessionSignal().sessionSignal--;

		assertTrue(TimerRunner.runningList.size() > 0);
	}

	/*
	 * Test method for 'cn.myapps.core.task.action.TaskAction.doStop()'
	 */
	public void doStop(String id) throws Exception {
		// PersistenceUtils.getSessionSignal().sessionSignal++;
		params.put("id", new String[] { id });
		BaseAction.getContext().setParameters(params);
		action.doView();
		action.doStop();
		// PersistenceUtils.getSessionSignal().sessionSignal--;

		assertTrue(TimerRunner.runningList.size() < 1);
	}

	/*
	 * Test method for 'cn.myapps.core.dynaform.form.action.FormAction.doSave()'
	 */
	public String doSave() throws Exception {
		Task vo = new Task();
		// PersistenceUtils.getSessionSignal().sessionSignal++;

		vo.setName("testTask");
		vo.setPeriod(TaskConstants.REAPET_TYPE_NOTREAPET);
		calendar.add(Calendar.MINUTE, 1);
		vo.setRunningTime(calendar.getTime());
		vo.setRuntimes(99999999);
		vo.setRepeatTimes(1);
		vo.setTaskScript("java.lang.System.out.println(\"*****Task script invoke*****\");");
		vo.setType(TaskConstants.TASK_TYPE_SCRIPT);
		vo.setFrequency(5);
		action.setContent(vo);
		// PersistenceUtils.getSessionSignal().sessionSignal--;
		action.doSave();

		// PersistenceUtils.getSessionSignal().sessionSignal++;
		params.put("id", new String[] { vo.getId() });
		BaseAction.getContext().setParameters(params);
		// PersistenceUtils.getSessionSignal().sessionSignal--;
		action.doView();
		Task findVO = (Task) action.getContent();

		assertEquals(findVO.getName(), vo.getName());

		return findVO.getId();
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doEdit()'
	 */
	public void doEdit(String id) throws Exception {
		// PersistenceUtils.getSessionSignal().sessionSignal++;
		params.put("id", new String[] { id });
		BaseAction.getContext().setParameters(params);
		action.doView();
		Task oldy = (Task) action.getContent();

		oldy.setName("NewTestTask");
		action.setContent(oldy);
		action.doSave();

		BaseAction.getContext().setParameters(params);
		action.doView();
		Task newly = (Task) action.getContent();
		// PersistenceUtils.getSessionSignal().sessionSignal--;

		assertEquals(oldy.getName(), newly.getName());
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doDelete()'
	 */
	public void doDelete(String id) throws Exception {
		// PersistenceUtils.getSessionSignal().sessionSignal++;
		action.set_selects(new String[] { id });
		action.doDelete();

		params.put("id", new String[] { id });
		BaseAction.getContext().setParameters(params);
		action.doView();
		Task find = (Task) action.getContent();
		// PersistenceUtils.getSessionSignal().sessionSignal--;

		assertNull(find);
	}

	public static void main(String[] args) throws Exception {
		junit.textui.TestRunner.run(TaskActionTestBak.class);
	}
}
