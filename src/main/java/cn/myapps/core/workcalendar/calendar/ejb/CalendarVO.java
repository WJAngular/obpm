package cn.myapps.core.workcalendar.calendar.ejb;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.workcalendar.special.ejb.SpecialDayVO;
import cn.myapps.core.workcalendar.standard.ejb.BaseDay;
import cn.myapps.core.workcalendar.standard.ejb.DayPart;
import cn.myapps.core.workcalendar.standard.ejb.StandardDayVO;
import cn.myapps.core.workcalendar.util.Month;
import cn.myapps.util.DateUtil;

/**
 * 日历
 * 
 * @author Chris
 * 
 */
public class CalendarVO extends ValueObject {

	/**
	 * 
	 */
	private static final Logger log = Logger.getLogger(CalendarVO.class);
	private static final long serialVersionUID = 1L;

	private String id;

	private String type;

	private String name;

	private String remark;

	private Date lastModifyDate;

	private Collection<StandardDayVO> standardDays;

	private Collection<SpecialDayVO> specialDays;

	private int workingTime;

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * 获取特例天
	 * 
	 * @return 特例天
	 */
	public Collection<SpecialDayVO> getSpecialDays() {
		if (this.specialDays == null)
			this.specialDays = new HashSet<SpecialDayVO>();
		return this.specialDays;
	}

	/**
	 * 获取常规天
	 * 
	 * @return 常规天
	 */
	public Collection<StandardDayVO> getStandardDays() {
		if (this.standardDays == null)
			this.standardDays = new HashSet<StandardDayVO>();
		return this.standardDays;
	}

	/**
	 * 根据当前日期获取常规天
	 * 
	 * @param calendar
	 * @return
	 */
	public BaseDay getStandardDayByDate(Calendar calendar) {
		if (this.standardDays == null)
			this.standardDays = new HashSet<StandardDayVO>();
		Iterator<StandardDayVO> its = this.standardDays.iterator();
		BaseDay standard = null;
		BaseDay[] stdrd = new StandardDayVO[7];
		int weekDay = 0;
		if (its != null)
			while (its.hasNext()) {
				standard = (BaseDay) its.next();
				stdrd[standard.getWeekDays()] = standard;
			}
		weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		standard = new StandardDayVO();
		standard.setApplicationid(stdrd[weekDay].getApplicationid());
		standard.setCalendar(stdrd[weekDay].getCalendar());
		standard.setDayIndex(stdrd[weekDay].getDayIndex());
		standard.setStartTime1(stdrd[weekDay].getStartTime1());
		standard.setStartTime2(stdrd[weekDay].getStartTime2());
		standard.setStartTime3(stdrd[weekDay].getStartTime3());
		standard.setStartTime4(stdrd[weekDay].getStartTime4());
		standard.setStartTime5(stdrd[weekDay].getStartTime5());
		standard.setEndTime1(stdrd[weekDay].getEndTime1());
		standard.setEndTime2(stdrd[weekDay].getEndTime2());
		standard.setEndTime3(stdrd[weekDay].getEndTime3());
		standard.setEndTime4(stdrd[weekDay].getEndTime4());
		standard.setEndTime5(stdrd[weekDay].getEndTime5());
		standard.setId(stdrd[weekDay].getId());
		standard.setWeekDays(stdrd[weekDay].getWeekDays());
		standard.setWorkingDayStatus(stdrd[weekDay].getWorkingDayStatus());
		standard.setRemark(stdrd[weekDay].getRemark());

		return standard;
	}

	private boolean isSpecialDay(Calendar monthCalendar, SpecialDayVO special) {
		// 同一天，且开始日期 < monthCalendar日期，结束日期 > 当前日期
//		return special.getStartDate().getDate() == monthCalendar.getTime().getDate()
//				&& special.getStartDate().compareTo(monthCalendar.getTime()) <= 0
//				&& special.getEndDate().compareTo(new Date()) >= 0;
		return special.getStartDate().compareTo(monthCalendar.getTime()) <= 0
		&& special.getEndDate().compareTo(monthCalendar.getTime()) >= 0;
	}

	/**
	 * 根据当前日期获取特例天
	 * 
	 * @param calendar
	 * @return 特例天
	 */
	public SpecialDayVO getSpecialDayByDate(Calendar calendar) {
		if (this.specialDays == null)
			this.specialDays = new HashSet<SpecialDayVO>();
		Iterator<SpecialDayVO> its = this.specialDays.iterator();
		SpecialDayVO special = null;
		if (its != null)

			while (its.hasNext()) {
				special = (SpecialDayVO) its.next();
				if (special != null && isSpecialDay(calendar, special)) {
					SpecialDayVO standard = new SpecialDayVO();
					standard.setApplicationid(special.getApplicationid());
					standard.setCalendar(special.getCalendar());
					standard.setDayIndex(special.getDayIndex());
					standard.setStartTime1(special.getStartTime1());
					standard.setStartTime2(special.getStartTime2());
					standard.setStartTime3(special.getStartTime3());
					standard.setStartTime4(special.getStartTime4());
					standard.setStartTime5(special.getStartTime5());
					standard.setEndTime1(special.getEndTime1());
					standard.setEndTime2(special.getEndTime2());
					standard.setEndTime3(special.getEndTime3());
					standard.setEndTime4(special.getEndTime4());
					standard.setEndTime5(special.getEndTime5());
					standard.setId(special.getId());
					standard.setWeekDays(special.getWeekDays());
					standard.setWorkingDayStatus(special.getWorkingDayStatus());
					standard.setRemark(special.getRemark());
					standard.setStartDate(special.getStartDate());
					standard.setEndDate(special.getEndDate());
					return standard;
				}
			}
		return null;
	}

	/**
	 * 获取日历的类型
	 * 
	 * @return 日历的类型
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 设置标识
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 设置名称
	 * 
	 * @param name名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置特殊日历
	 * 
	 * @param specialDays
	 *            特殊日历
	 */
	public void setSpecialDays(Collection<SpecialDayVO> specialDays) {
		this.specialDays = specialDays;
	}

	/**
	 * 设置正常日历
	 * 
	 * @param standardDays
	 *            正常日历
	 */
	public void setStandardDays(Collection<StandardDayVO> standardDays) {
		this.standardDays = standardDays;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置描述
	 * 
	 * @param remark
	 *            描述
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取最后一次修改时间
	 * 
	 * @return 修改时间
	 */
	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	/**
	 * 设置最后一次修改时间
	 * 
	 * @param lastModifyDate
	 *            最后一次修改时间
	 */
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public Month getMonth(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);

		return getMonth(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH));
	}

	/**
	 * 获得某年某月的所有天
	 * 
	 * @param year
	 *            年
	 * @param monthIndex
	 *            月份
	 * @param calendar
	 *            日历类别
	 * @return year年monthIndex月的所有天
	 */
	public Month getMonth(int year, int monthIndex) {
		Month mm = new Month();
		BaseDay[][] days = new BaseDay[6][7];
//		for (int i = 0; i < 6; i++)
//			for (int j = 0; j < 7; j++)
//				days[i][j] = null;
		Calendar thisMonth = getThisMonth(year, monthIndex);
		if (thisMonth == null)
			return null;

		int firstIndex = thisMonth.get(Calendar.DAY_OF_WEEK) - 1;
		int maxIndex = thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

		int day;// 日
		BaseDay[][] std = new BaseDay[6][7];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				day = (i * 7 + j) - firstIndex + 1;

				if (day > 0 && day <= maxIndex) {
					Calendar month = thisMonth;
					month.set(Calendar.DAY_OF_MONTH, day);
					std[i][j] = getSpecialDayByDate(month);
					if (std[i][j] != null) {
						std[i][j].setDayIndex(day);
					} else {
						std[i][j] = getStandardDayByDate(thisMonth);
						std[i][j].setDayIndex(day);
					}
				} else {
					Calendar month = getThisMonth(year, monthIndex);
					month.set(Calendar.DAY_OF_MONTH, day);
					std[i][j] = getStandardDayByDate(month);
					if (std[i][j] != null) {
						std[i][j].setDayIndex(0);
					}
				}
				days[i][j] = std[i][j];
			}
		}
		mm.setYear(year);
		mm.setMonthIndex(monthIndex);
		mm.setDays(days);

		return mm;
	}

	public static Calendar getThisMonth(int year, int monthIndex) {
		Calendar thisMonth = Calendar.getInstance();
		if (year > 1900)
			thisMonth.set(Calendar.YEAR, year);
		else
			thisMonth.set(Calendar.YEAR, 1901);

		if (monthIndex >= 0 && monthIndex < 12)
			thisMonth.set(Calendar.MONTH, monthIndex);
		else
			thisMonth.set(Calendar.MONTH, 0);

		thisMonth.setFirstDayOfWeek(Calendar.SUNDAY);

		thisMonth.set(Calendar.DAY_OF_MONTH, 1);
		return thisMonth;
	}

	/**
	 * 获取工作时间
	 * 
	 * @return 工作时间
	 */
	public int getWorkingTime() {
		return workingTime;
	}

	/**
	 * 设置工作时间
	 * 
	 * @param workingTime
	 *            工作时间
	 */
	public void setWorkingTime(int workingTime) {
		this.workingTime = workingTime;
	}

	public String toString() {
		StringBuffer rtn = new StringBuffer();

		Collection<StandardDayVO> standardDays = getStandardDays();
		rtn.append("StandardDay: \n");
		for (Iterator<StandardDayVO> iterator = standardDays.iterator(); iterator.hasNext();) {
			StandardDayVO day = (StandardDayVO) iterator.next();
			rtn.append(day.getStartTime1() + "~" + day.getEndTime1() + "\n");
			rtn.append(day.getStartTime2() + "~" + day.getEndTime2() + "\n");
			rtn.append(day.getStartTime3() + "~" + day.getEndTime3() + "\n");
			rtn.append(day.getStartTime4() + "~" + day.getEndTime4() + "\n");
			rtn.append(day.getStartTime5() + "~" + day.getEndTime5() + "\n");
		}

		Collection<SpecialDayVO> specialDays = getSpecialDays();
		rtn.append("SpecialDay: \n");
		for (Iterator<SpecialDayVO> iterator = specialDays.iterator(); iterator.hasNext();) {
			SpecialDayVO day = (SpecialDayVO) iterator.next();
			rtn.append(day.getStartTime1() + "~" + day.getEndTime1() + "\n");
			rtn.append(day.getStartTime2() + "~" + day.getEndTime2() + "\n");
			rtn.append(day.getStartTime3() + "~" + day.getEndTime3() + "\n");
			rtn.append(day.getStartTime4() + "~" + day.getEndTime4() + "\n");
			rtn.append(day.getStartTime5() + "~" + day.getEndTime5() + "\n");
		}

		return rtn.toString();
	}

	/**
	 * 根据当前时间，获取当日时段
	 * 
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public DayPart findDayPart(Date currentDate) throws Exception {
		Month month = getMonth(currentDate);

		DayPart[] dayParts = month.getDayParts(currentDate);
		for (int i = 0; i < dayParts.length; i++) {
			if (dayParts[i].includes(currentDate)) {
				return dayParts[i];
			}
		}

		return null;
	}

	/**
	 * 寻找下一个开始时段日期
	 * 
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public Date findNextStartDate(Date currentDate) throws Exception {
		Calendar cld = Calendar.getInstance();
		cld.setTime(currentDate);

		Month month = getMonth(currentDate);

		// 不是工作日，则跳到下一天
		if (!month.isWorkingDay(cld.getTime())) {
			cld.add(Calendar.DAY_OF_MONTH, 1);
			cld.set(Calendar.HOUR_OF_DAY, 0);
			cld.set(Calendar.MINUTE, 0);
			month = getMonth(cld.getTime());
			//findNextStartDate(cld.getTime());
		}

		DayPart[] dayParts = month.getDayParts(cld.getTime());
		for (int i = 0; i < dayParts.length; i++) {
			if (dayParts[i].isStartAfter(cld.getTime())) {
				cld.set(Calendar.HOUR_OF_DAY, dayParts[i].getFromHour());
				cld.set(Calendar.MINUTE, dayParts[i].getFromMinute());
				return cld.getTime();
			}
		}

		// 当天时段都不存在，则顺延到下一天的第一个时段
		cld.add(Calendar.DAY_OF_MONTH, 1);
		month = getMonth(cld.getTime());
		if (month.isWorkingDay(cld.getTime())) {
			dayParts = month.getDayParts(cld.getTime());
			if (dayParts.length > 0) {
				cld.set(Calendar.HOUR_OF_DAY, dayParts[0].getFromHour());
				cld.set(Calendar.MINUTE, dayParts[0].getFromMinute());
			} else {
				log.info(DateUtil.format(currentDate, "yyyy-MM-dd HH:mm:ss") + " isWorkingDay: "
						+ month.isWorkingDay(currentDate));
			}
			return cld.getTime();
		} else {
			return findNextStartDate(cld.getTime());
		}
	}
	
	/**
	 * 判断当前时间是否是工作时间
	 * 
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public boolean isWorkingTime(Date currentDate) throws Exception {
		Calendar cld = Calendar.getInstance();
		cld.setTime(currentDate);

		Month month = getMonth(currentDate);

		// currentDate不是工作日，即不是工作时间
		if (!month.isWorkingDay(cld.getTime())) {
			return false;
		}

		//currentDate是工作日，并且currentDate在工作时间范围内
		DayPart[] dayParts = month.getDayParts(cld.getTime());
		for (int i = 0; i < dayParts.length; i++) {
			if (dayParts[i].includes(cld.getTime())) {
				return true;
			}
		}
		
		//currentDate是工作日，并且currentDate不在工作时间范围内
		return false;
	}
	
	/**
	 * 寻找当前日期的下班时刻
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public Date findEndDate(Date currentDate) throws Exception {
		Calendar cld = Calendar.getInstance();
		cld.setTime(currentDate);

		Month month = getMonth(currentDate);

		if (month.isWorkingDay(cld.getTime())) {
			DayPart[] dayParts = month.getDayParts(cld.getTime());
			if (dayParts[dayParts.length - 1] != null) {
				return dayParts[dayParts.length - 1].getEndTime(cld.getTime());
			}
		}
		return null;
	}
	
	/**
	 * 寻找当前日期的上班时刻
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public Date findStartDate(Date currentDate) throws Exception {
		Calendar cld = Calendar.getInstance();
		cld.setTime(currentDate);

		Month month = getMonth(currentDate);

		if (month.isWorkingDay(cld.getTime())) {
			DayPart[] dayParts = month.getDayParts(cld.getTime());
			if (dayParts[0] != null) {
				if(dayParts[0].includes(cld.getTime())){
					return dayParts[0].getStartTime(cld.getTime());
				}
			}
		}
		return null;
	}
	
	/**
	 * 判断两个日期是否同一天
	 * @return
	 * @throws Exception
	 */
	public boolean isSameDay(Date date1, Date date2)throws Exception {
		Calendar cld1 = Calendar.getInstance();
		cld1.setTime(date1);
		Calendar cld2 = Calendar.getInstance();
		cld2.setTime(date2);
		return (cld1.get(Calendar.YEAR) == cld2.get(Calendar.YEAR)) && 
			   (cld1.get(Calendar.MONTH) == cld2.get(Calendar.MONTH)) &&
			   (cld1.get(Calendar.DAY_OF_MONTH) == cld2.get(Calendar.DAY_OF_MONTH));
	}
	
	
	/**
	 * 寻找当前日期区间内上班时刻
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public Date findTimeRegionStartDate(Date currentDate) throws Exception {
		Calendar cld = Calendar.getInstance();
		cld.setTime(currentDate);

		Month month = getMonth(currentDate);

		if (month.isWorkingDay(cld.getTime())) {
			DayPart[] dayParts = month.getDayParts(cld.getTime());
			if (dayParts != null) {
				for(DayPart dp :dayParts){ 
					if(dp.includes(currentDate) || (!dp.includes(currentDate) && dp.isStartAfter(currentDate))){   //获取正确的上班时间段，可提前
						return   dp.getStartTime(cld.getTime());
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据工作时段查找下班时刻
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public Date findTimeRegionEndDate(String timeRegion) throws Exception{
		Date date = new Date() ;
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		Month month = getMonth(date);
		if (month.isWorkingDay(cld.getTime())) {
			DayPart[] dayParts = month.getDayParts(cld.getTime());
			if (dayParts != null) {
				for(DayPart dp :dayParts){
					if(dp.toString().equals(timeRegion)){
						return dp.getEndTime(cld.getTime());
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据工作时段结束时间 和 当前时间，判断是否符合记录条件
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public Boolean isTimeRegionEndDate(Date currentDate, Date timeRegion) throws Exception{
		
		Calendar cld = Calendar.getInstance();
		cld.setTime(timeRegion);
		Month month = getMonth(timeRegion);
		if (month.isWorkingDay(cld.getTime())) {
			DayPart[] dayParts = month.getDayParts(cld.getTime());
			if (dayParts != null) {
			    int size = dayParts.length;
			    if(size > 1 ){
			    	int pos = 0 ;
			    	for(int index = 0 ; index < size ; index ++){
						 if(index == size - 1){
							 pos = 0 ;
						 }else{
							 pos ++ ;
						 }
						 if(dayParts[index].includes(timeRegion)){
							 if(dayParts[index].includes(currentDate) 
									 || dayParts[index].isEnd(currentDate) && dayParts[pos].isStartAfter(currentDate)
									 || (index == size - 1 && dayParts[index].isEnd(currentDate)) ){
								   return true;
							 }
						 }
					}
			    }else{
			    	return true ;
			    }
			}
		}
		return false;
	}
	
	
	/**
	 * 根据当前时间，获取当日时段(包括提前时间)
	 * 
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public String getDayPartIncludeStartAfterTime(Date currentDate,Boolean isMultiPeriod) throws Exception {
		Month month = getMonth(currentDate);
		DayPart[] dayParts = month.getDayParts(currentDate);
		
		if(isMultiPeriod){
			for (int i = 0; i < dayParts.length; i++) {
				if (dayParts[i].includes(currentDate)||(!dayParts[i].includes(currentDate) && dayParts[i].isStartAfter(currentDate))) {
					return dayParts[i].toString();
				}
			}
		}else{
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < dayParts.length; i++) {
					sb.append(dayParts[i].toString()).append(",");
			}
			if(sb.length() != 0 ){
				return sb.substring(0, sb.length()-1);
			}
		}
		return null;
	}
	
	
	/**
	 * @throws Exception 
	 * 
	 */
	
	public Boolean isLastDayPart(Date currentDate,String dayPart,Boolean isMultiPeriod) throws Exception{
		Month month = getMonth(currentDate);
		DayPart[] dayParts = month.getDayParts(currentDate);
		
		if(isMultiPeriod){
			 return dayParts[dayParts.length-1].toString().equals(dayPart);
		}else{
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < dayParts.length; i++) {
					sb.append(dayParts[i].toString()).append(",");
			}
		}
		
		return false ;
	}
}
