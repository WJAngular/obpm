package cn.myapps.util;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class DateUtilTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetDateStr() {
		fail("Not yet implemented");
	}

	public void testGetDateStrC() {
		fail("Not yet implemented");
	}

	public void testGetDateStrCompact() {
		fail("Not yet implemented");
	}

	public void testGetDateTimeStrDate() {
		fail("Not yet implemented");
	}

	public void testGetDateTimeStrC() {
		fail("Not yet implemented");
	}

	public void testGetCurDateStr() {
		fail("Not yet implemented");
	}

	public void testParseDateString() {
		fail("Not yet implemented");
	}

	public void testParseDateStringString() {
		fail("Not yet implemented");
	}

	public void testParseDateC() {
		fail("Not yet implemented");
	}

	public void testParseDateTime() {
		fail("Not yet implemented");
	}

	public void testParseDateTimeC() {
		fail("Not yet implemented");
	}

	public void testParseTime() {
		fail("Not yet implemented");
	}

	public void testParseTimeC() {
		fail("Not yet implemented");
	}

	public void testYearOfDate() {
		fail("Not yet implemented");
	}

	public void testMonthOfDate() {
		fail("Not yet implemented");
	}

	public void testDayOfDate() {
		fail("Not yet implemented");
	}

	public void testGetDateTimeStrDateDouble() {
		fail("Not yet implemented");
	}

	public void testDiffDateMDateDate() {
		fail("Not yet implemented");
	}

	public void testDiffDateD() {
		fail("Not yet implemented");
	}

	public void testDiffDateMIntInt() {
		fail("Not yet implemented");
	}

	public void testGetNextMonthFirstDate() {
		fail("Not yet implemented");
	}

	public void testGetNextMonthDate() {
		fail("Not yet implemented");
	}

	public void testGetFrontDateByDayCount() {
		fail("Not yet implemented");
	}

	public void testGetFirstDayStringString() {
		fail("Not yet implemented");
	}

	public void testGetFirstDayIntInt() {
		fail("Not yet implemented");
	}

	public void testGetFirstDayDate() {
		fail("Not yet implemented");
	}

	public void testGetLastDayStringString() {
		fail("Not yet implemented");
	}

	public void testGetLastDayIntInt() {
		fail("Not yet implemented");
	}

	public void testGetLastDayDate() {
		fail("Not yet implemented");
	}

	public void testGetTodayStr() {
		fail("Not yet implemented");
	}

	public void testGetToday() {
		fail("Not yet implemented");
	}

	public void testGetTodayAndTime() {
		fail("Not yet implemented");
	}

	public void testGetTodayC() {
		fail("Not yet implemented");
	}

	public void testGetThisYearMonth() {
		fail("Not yet implemented");
	}

	public void testGetYearMonth() {
		fail("Not yet implemented");
	}

	public void testGetDistinceYear() {
		fail("Not yet implemented");
	}

	public void testGetDistinceMonth() {
		fail("Not yet implemented");
	}

	public void testGetDistinceMonth1() {
		fail("Not yet implemented");
	}

	public void testGetDistinceDayStringString() {
		fail("Not yet implemented");
	}

	public void testGetDistinceDayDateDate() {
		fail("Not yet implemented");
	}

	public void testGetDistinceDayDateDate1() {
		fail("Not yet implemented");
	}

	public void testGetDistinceDayString() {
		fail("Not yet implemented");
	}

	public void testGetDistinceTimeStringString() {
		fail("Not yet implemented");
	}

	public void testGetDistinceTimeString() {
		fail("Not yet implemented");
	}

	public void testGetDistinceMinuteStringString() {
		fail("Not yet implemented");
	}

	public void testGetDistinceMinuteString() {
		fail("Not yet implemented");
	}

	public void testIsOvertime() {
		fail("Not yet implemented");
	}

	public void testGetTimestamStr() {
		fail("Not yet implemented");
	}

	public void testGetTimeStr() {
		fail("Not yet implemented");
	}

	public void testIsBeforeCheckDate() {
		fail("Not yet implemented");
	}

	public void testFormat() {
		fail("Not yet implemented");
	}

	public void testGetDaysOfMonth() {
		fail("Not yet implemented");
	}

	public void testDiffDateH() {
		fail("Not yet implemented");
	}

	public void testGetNextDateByYearCount() {
		fail("Not yet implemented");
	}

	public void testGetNextDateByMonthCount() {
		fail("Not yet implemented");
	}

	public void testGetNextDateByDayCount() {
		fail("Not yet implemented");
	}

	public void testGetNextDateByMinuteCount() {
		fail("Not yet implemented");
	}

	public void testGetTimeDiff() {
		Calendar calendar = Calendar.getInstance();
		Date beforeTime = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		calendar.add(Calendar.SECOND, 60);
		Date afterTime = calendar.getTime();

		assertEquals("Time Difference is 60(s)", 60, DateUtil.getDiffTime(
				beforeTime, afterTime) / 1000);
	}

	public void testGetDateTimeDiff() {
		Calendar calendar = Calendar.getInstance();
		Date beforeTime = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		calendar.add(Calendar.SECOND, 60);
		Date afterTime = calendar.getTime();

		// 24 * 3600 + 60 = 84460
		assertEquals("Time Difference is 86460(s)", 86460, DateUtil
				.getDiffDateTime(beforeTime, afterTime) / 1000);
	}
}
