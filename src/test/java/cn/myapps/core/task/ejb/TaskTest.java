package cn.myapps.core.task.ejb;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;
import cn.myapps.util.sequence.Sequence;

public class TaskTest extends TestCase {
	private Task task;

	private final static int TODAY_OF_WEEK = 1;

	private final static int TODAY_OF_MOTH = 24;

	protected void setUp() throws Exception {
		super.setUp();
		task = new Task();
		String taskId = Sequence.getSequence();
		task.setId(taskId);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test task execution within one minute after runnining time
	 */
	public void testTaskExecution() {
		task.setName("TaskExecution");
		task.setPeriod(TaskConstants.REPEAT_TYPE_NONE);
		task.setRepeatTimes(1);

		// From now on, after 5 minute
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 5);
		task.setRunningTime(calendar.getTime());
		assertFalse("Execution time has not arrived", task.isExecuteAble());

		// From now on, between 0 and 1 minute
		calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -30);
		task.setRunningTime(calendar.getTime());
		assertTrue("Execution time has arrived", task.isExecuteAble());
	}

	/**
	 * Test daily task repeated times
	 */
	public void testDailyTaskRepeatedTimes() throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date sysDate = new Date();

		Date executeAbleDate = getExecuteAbleDate();
		task.setName("DailyTask");
		task.setPeriod(TaskConstants.REPEAT_TYPE_DAILY);
		task.setRunningTime(executeAbleDate);

		// Repeat the task
		task.setRepeatTimes(1);
		Task cloneTask = (Task) task.clone();
		assertTrue("At first, execute able", task.isExecuteAble(sysDate));
		assertFalse("After the first, execute unable, repeat times is 1", task
				.isExecuteAble(sysDate));

		// Clone task
		assertTrue(task.hashCode() != cloneTask.hashCode());
		assertTrue(
				"Difference object with same ID, execute unable, repeat times is 1",
				cloneTask.isExecuteAble(sysDate));

		// In next day
		calendar.setTime(sysDate);
		calendar.add(Calendar.DATE, 1);
		assertTrue("Change system date to next day, execute able", task
				.isExecuteAble(calendar.getTime()));

		// Set two of repeated times
		task.setRepeatTimes(2);
		assertTrue("Repeated times is two, execute able", task.isExecuteAble());
	}

	/**
	 * Test daily task frequency
	 */
	public void testDailyTaskFrequency() {
		task.setName("DailyTask");
		task.setPeriod(TaskConstants.REPEAT_TYPE_DAILY);
		task.setRunningTime(getExecuteAbleDate());
		task.setFrequency(5);
		task.setRepeatTimes(2);

		assertTrue("At first, execute able", task.isExecuteAble());
		assertFalse("After the first, execute unable", task.isExecuteAble());
	}

	/**
	 * Test weekly task
	 */
	public void testWeeklyTask() {
		task.setName("WeeklyTask");
		task.setPeriod(TaskConstants.REPEAT_TYPE_WEEKLY);
		task.setRunningTime(getExecuteAbleDate());
		task.getDaysOfWeek().add(Integer.valueOf(TODAY_OF_WEEK));
		task.setRepeatTimes(1);

		assertTrue("Execute every " + TODAY_OF_WEEK + "th day of week", task
				.isExecuteAble());
	}

	/**
	 * Test monthly task
	 */
	public void testMonthlyTask() {
		task.setName("MonthlyTask");
		task.setPeriod(TaskConstants.REPEAT_TYPE_MONTHLY);
		task.setRunningTime(getExecuteAbleDate());
		task.setDayOfMonth(TODAY_OF_MOTH);
		task.setRepeatTimes(1);

		assertTrue("Execute every " + TODAY_OF_MOTH + "th day of month", task
				.isExecuteAble());
	}

	/**
	 * Get execute able date
	 * 
	 * @return Date
	 */
	private Date getExecuteAbleDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -30);
		return calendar.getTime();
	}
}