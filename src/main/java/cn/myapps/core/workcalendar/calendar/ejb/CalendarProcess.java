package cn.myapps.core.workcalendar.calendar.ejb;

import java.util.Collection;
import java.util.Date;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;

public interface CalendarProcess extends IDesignTimeProcess<CalendarVO> {

	/**
	 * 根据日历类别calendar及起始日期与结束日期，统计出calendar日历中startDate与endDate之间多少天是工作日
	 * 
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param calendar
	 *            日历类别
	 * @param domain
	 *            域
	 * @return 工作日天数
	 * @throws Exception
	 */
	public double countWorkingDays(Date startDate, Date endDate, String calendar) throws Exception;

	/**
	 * 根据日历类别calendar及年份月份，统计出calendar日历中year年month月有多少天是工作日
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param calendar
	 *            日历类别
	 * @return 工作日天数
	 * @throws Exception
	 */
	public double countWorkingDays(String year, String month, String calendar) throws Exception;

	/**
	 * 根据日历类别calendar及年份，统计出calendar日历中year年中有多少天是工作日
	 * 
	 * @param year
	 *            年份
	 * @param calendar
	 *            日历类别
	 * @return 工作日天数
	 * @throws Exception
	 */
	public double countWorkingDays(String year, String calendar) throws Exception;

	/**
	 * 根据日历类别calendar及起始日期与结束日期，统计出calendar日历中startDate与endDate之间多少工作时间
	 * 
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param calendar
	 *            日历类别
	 * @return 工作日天数
	 * @throws Exception
	 */
	public double countTimesOfWorkingDays(Date startDate, Date endDate, String calendar) throws Exception;

	/**
	 * 获得某年的所有天
	 * 
	 * @param year
	 * @param calendar
	 *            日历类别
	 * @return year年的所有天
	 */
	// public Month[] creatMonthDays(String year,String calendar)throws
	// Exception;
	/**
	 * 根据年月日及日历类别判断year年month月day日是否为特例日期
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param calendar
	 *            日历类别
	 * @param status
	 *            工作日状态：01，工作日；02，非工作日
	 * @return true|false
	 */
	// public boolean isSpecailCaseDay(String year,String month,String
	// day,String
	// calendar,String status) throws Exception;
	/**
	 * 根据开始日期及结束日期，返回工作日日历中的特例日期
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param status
	 *            工作状态：工作日或非工作日
	 * @param calendar
	 *            日历类别
	 * @return dates is type of Collection
	 */
	// public Collection getSpecailCaseDatas(Date startDate,Date endDate,String
	// calendar,String status)throws Exception;
	/**
	 * 返回常规工作周设置
	 * 
	 * @param status
	 *            工作状态：工作日或非工作日
	 * @param calendar
	 *            日历类别
	 * @return dates is type of Collection
	 */
	// public Collection getWorkaDayDatas(String calendar,String status)throws
	// Exception;
	/**
	 * 根据年月日及日历类别判断year年month月day日是否为工作日
	 * 
	 * @param domainid
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param calendar
	 *            日历类别
	 * @param status
	 *            工作日状态：01，工作日；02，非工作日
	 * @return true|false
	 */
	// public boolean isWorkingDay(String year,String month,String day,String
	// calendar,String status)throws Exception;
	public Collection<CalendarVO> doQueryListByDomain(String domainid) throws Exception;
	
	public DataPackage<CalendarVO> doQueryList(ParamsTable params) throws Exception;

	/**
	 * 根据日历的名称获取日历对象
	 * 
	 * @param calendarName
	 *            日历名称
	 * @param domainid
	 *            域名
	 * @return 日历对象
	 * @throws Exception
	 */
	public ValueObject doViewByName(String calendarName, String domainid) throws Exception;

	/**
	 * 获取下一天的时间(根据日历工作时段计算)
	 * (工作时段如：9:00-12:00 13:00-18:00)
	 * @param currentDate
	 *            日期型
	 * @param minuteCount
	 *            分钟计数
	 * @param calendar
	 *            日历对象标识
	 * @return 日期型
	 * @throws Exception
	 */
	public Date getNextDateByMinuteCount(Date currentDate, int minuteCount, String calendar) throws Exception;

	/**
	 * 获取下一天的时间
	 * 
	 * @param currentDate
	 *            日期型
	 * @param minuteCount
	 *            分钟计数
	 * @param calendar
	 *            日历对象标识
	 * @return 日期型
	 * @throws Exception
	 */
	public Date getNextDate(Date currentDate, int minuteCount, String calendar) throws Exception;
	
	public int getCountByName(String name, String domainid) throws Exception;
	
	public void doUpdate(String id, String name, String remark) throws Exception;
	
	/**
	 * 根据工作日历，判断当前时间currentDate是否为工作时间
	 * @param currentDate
	 * @param calendar
	 * @return
	 * @throws Exception
	 */
	public boolean isWorkingTime(Date currentDate, String calendar)throws Exception;
	
}
