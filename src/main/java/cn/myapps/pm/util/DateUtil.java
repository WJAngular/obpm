package cn.myapps.pm.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 封装日期相关操作的工具类
 * @author Happy
 *
 */
public class DateUtil {
	
	
	/**
	 * 获取几个小时后的日期
	 * @param currDate
	 * 		当前日期
	 * @param afterHour
	 * 		几个小时后
	 * @return
	 */
	public static Date getDateAfterHour(Date currDate,int afterHour){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currDate);
		calendar.add(Calendar.HOUR, afterHour);
		return calendar.getTime();
		
		
	}

}
