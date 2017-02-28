
package net.gdsc.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * 日期处理工具类
 * 
 * @author 吴景
 * @since 1.0
 * @version 2016-05-20 吴景
 */
public class DateUtil {
    
    /** 周中文字符串 */
    public static final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
    
    /** 国家法定节假日字符串 */
    public static final String holidays = "2012-10-01,2012-10-02,2012-10-03,2012-10-04,2012-10-05,2012-12-31,2013-01-01,2012-02-11,2012-02-12,2012-02-13,2012-02-14,2012-02-15,2013-04-04,2013-04-05,2013-04-29,2013-04-30,2013-05-01,2013-06-10,2013-06-11,2013-06-12,2013-09-19,2013-09-20,2013-10-01,2013-10-02,2013-10-03,2013-10-04,2013-10-07";
    
    /** 需要上班的周末字符串 */
    public static final String workHolidays = "2012-09-29,2012-12-29,2013-02-16,2013-02-17,2013-04-07,2013-04-27,2013-04-28,2013-06-08,2013-06-09,2013-09-22,2013-09-28,2013-09-29";
    
    /***************************** 获取日期部分内容 **********************************************************/
    /**
     * 获取指定日期对应的年份
     * 
     * @param date 日期
     * @return 年份
     */
    public static int getYearByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
    
    /**
     * 获取指定日期对应的月份
     * 
     * @param date 日期
     * @return 月份
     */
    public static int getMonthByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }
    
    /**
     * 获取指定日期对应的日
     * 
     * @param date 日期
     * @return 日
     */
    public static int getDayByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 获取指定日期对应的小时
     * 
     * @param date 日期
     * @return 小时
     */
    public static int getHourByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    
    /**
     * 获取指定日期对应的分钟
     * 
     * @param date 日期
     * @return 分钟
     */
    public static int getMinuteByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }
    
    /**
     * 获取指定日期对应的秒钟
     * 
     * @param date 日期
     * @return 秒
     */
    public static int getSecondByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }
    
    /**
     * 获取指定日期对应的毫秒
     * 
     * @param date 日期
     * @return 毫秒
     */
    public static long getMillisByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }
    
    /**
     * 获取两个日期之间的所有天的集合
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期集合
     */
    public static List<String> getBettwenDate(String startDate, String endDate) {
        List<String> result = new ArrayList<String>();
        Date start = DateUtil.parseDate(startDate);
        Date end = DateUtil.parseDate(endDate);
        for (int i = 0, days = diffDate(start, end); i < days + 1; i++) {
            result.add(formatDate(DateUtil.addDate(start, i)));
        }
        return result;
    }
    
    /**
     * 获取两个日期之间的所有月的集合
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期集合
     */
    public static List<String> getBettwenMonth(String startDate, String endDate) {
        List<String> result = new ArrayList<String>();
        Date start = DateUtil.parseDate(startDate + "-01");
        Date end = DateUtil.parseDate(endDate + "-01");
        for (int i = 0, months = diffMonth(start, end); i < months + 1; i++) {
            result.add(formatMonthDate(DateUtil.addMonth(start, i)));
        }
        return result;
    }
    
    /***************************** 格式化 **********************************************************/
    /**
     * 字符串转换成日期
     * 
     * @param param yyyy-MM-dd格式的字符串
     * @return yyyy-MM-dd格式的日期
     */
    public static Date parseDate(String param) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(param);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * 字符串转换成日期
     * 
     * @param param yyyy-MM-dd格式的字符串
     * @return yyyy-MM-dd HH:mm:ss格式的日期
     */
    public static Date parseDateTime(String param) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(param);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * 字符串转换成日期
     * 
     * @param param yyyy-MM格式的字符串
     * @return yyyy-MM-dd格式的日期
     */
    public static Date parseDateByMonth(String param) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            date = sdf.parse(param);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * 字符串转换成日期，如果传入的日期为空默认返回当前月的开始时间
     * 
     * @param param 日期参数
     * @return 日期
     */
    public static Date parseDateDefaultMonthBegin(String param) {
        return "".equals(param) ? DateUtil.getCurrMonthBegin() : DateUtil.parseDate(param);
    }
    
    /**
     * 字符串转换成日期，如果传入的日期为空默认返回当前月的结束时间
     * 
     * @param param 日期参数
     * @return 日期
     */
    public static Date parseDateDefaultMonthEnd(String param) {
        return "".equals(param) ? DateUtil.getCurrMonthEnd() : DateUtil.parseDate(param);
    }
    
    /**
     * 格式化指定格式的日期
     * 
     * @param date 日期参数
     * @param format 日期格式 如：yyyy-MM-dd yyyy-MM-dd HH:mm:ss
     * @return 指定格式的日期字符串
     */
    public static String formatDate(Date date, String format) {
        String result = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            result = dateFormat.format(date);
        } catch (Exception e) {
        }
        return result;
    }
    
    /**
     * 格式化指定格式的日期
     * 
     * @param param 字符串日期
     * @param format 日期格式 如：yyyy-MM-dd
     * @return 指定格式的日期
     */
    public static Date parseDate(String param, String format) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(param);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * 功能描述：常用的格式化日期
     * 
     * @param date Date 日期
     * @return String 日期字符串 yyyy-MM-dd格式
     */
    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }
    
    /**
     * 功能描述：常用的格式化日期
     * 
     * @param date Date 日期
     * @return String 日期字符串 yyyy-MM-dd HH:mm:ss格式
     */
    public static String formatTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 功能描述：常用的格式化日期
     * 
     * @param date Date 日期
     * @return String 日期字符串 yyyy-MM-dd格式
     */
    public static String formatMonthDate(Date date) {
        return formatDate(date, "yyyy-MM");
    }
    
    /**
     * 格式化日期字符串
     * 
     * @param param yyyyMMdd格式的字符串
     * @return yyyy-MM-dd格式的日期
     */
    public static Date parseDateNoFormat(String param) {
        return parseDate(param.substring(0, 4) + "-" + param.substring(4, 6) + "-" + param.substring(6,8));
    }
    /**
     * 格式化日期字符串
     * 
     * @param param yyyyMMddHHmmss格式的字符串
     * @return yyyy-MM-dd HH:mm:ss格式的日期
     */
    public static Date parseDateFormatByString(String param) {
    	return parseDateTime(param.substring(0, 4) + "-" + param.substring(4, 6) + "-" + param.substring(6,8)+" " + param.substring(8, 10)+":"+param.substring(10, 12)+":"+param.substring(12));
    }
    
    /***************************** 当前日期数据 **********************************************************/
    
    /**
     * 获取Timestamp格式的当前时间
     * 
     * @return Timestamp格式的当前时间
     */
    public static Timestamp getCurrTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * 获取yyyy/MM/dd格式的当前日期
     * 
     * @param date 日期参数
     * @return yyyy/MM/dd格式的当前日期
     */
    public static String getDate(Date date) {
        return formatDate(date, "yyyy/MM/dd");
    }
    
    /**
     * 获取yyyy-MM-dd格式的当前日期
     * 
     * @return yyyy-MM-dd格式的当前日期
     */
    public static String getCurrDate() {
        return formatDate(new Date(), "yyyy-MM-dd");
    }
    
    /**
     * 获取yyyyMM格式的当前日期
     * 
     * @return yyyyMM格式的当前日期
     */
    public static String getCurrMonth() {
        return formatDate(new Date(), "yyyyMM");
    }
    
    /**
     * 获取指定日期的HH:mm:ss格式字符串
     * 
     * @param date 日期参数
     * @return HH:mm:ss格式字符串
     */
    public static String getTime(Date date) {
        return formatDate(date, "HH:mm:ss");
    }
    
    /**
     * 获取指定日期的yyyy/MM/dd HH:mm:ss格式的时间字符串
     * 
     * @param date 日期参数
     * @return yyyy/MM/dd HH:mm:ss格式的时间字符串
     */
    public static String getDateTime(Date date) {
        return formatDate(date, "yyyy/MM/dd HH:mm:ss");
    }
    
    /**
     * 获取指定日期的yyyyMMddHHmmss格式的时间字符串
     * 
     * @return yyyyMMddHHmmss格式的时间字符串
     */
    public static String getCurrDateTime() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }
    
    /***************************** 日期运算 **********************************************************/
    
    /**
     * 月份加法
     * 
     * @param date 指定日期
     * @param month 相加的月数
     * @return 指定日期加上对应月数后的日期
     */
    public static Date addMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }
    
    /**
     * 天数加法
     * 
     * @param date 指定日期
     * @param day 相加的天数
     * @return 指定日期加上对应天数后的日期
     */
    public static Date addDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        long millis = getMillisByDate(date) + ((long) day) * 24 * 3600 * 1000;
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
    
    /**
     * 小时加法
     * 
     * @param date 指定日期
     * @param hour 相加的小时数
     * @return 指定日期加上对应小时数后的日期
     */
    public static Date addHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        long millis = getMillisByDate(date) + ((long) hour) * 3600 * 1000;
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
    
    /**
     * 计算两个Date型日期相差的天数
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 两日期相差的天数
     */
    public static int diffDate(Date startDate, Date endDate) {
        return (int) ((getMillisByDate(endDate) - getMillisByDate(startDate)) / (24 * 3600 * 1000));
    }
    
    /**
     * 计算两个Date型日期相差的月数
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 两日期相差的月数
     */
    @SuppressWarnings("deprecation")
    public static int diffMonth(Date startDate, Date endDate) {
        return (endDate.getYear() - startDate.getYear()) * 12 + endDate.getMonth() - startDate.getMonth();
    }
    
    /**
     * 判断两个日期是否为同一天
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期是否为同一天
     */
    public static boolean isSameDay(Date startDate, Date endDate) {
        return formatDate(startDate).equals(formatDate(endDate));
    }
    
    /**
     * 判断某个时间是否在其他两个时间区间中
     * 
     * @param currDate 当前待比较的时间
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 是否在两个日期中间
     */
    public static boolean isBetweenDay(Date currDate, Date startDate, Date endDate) {
        return getMillisByDate(currDate) - getMillisByDate(startDate) >= 0 && getMillisByDate(endDate) - getMillisByDate(currDate) >= 0;
    }
    
    /**
     * 计算两个Calendar型日期相差的天数（不区分起始日期）
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 两日期相差的天数
     */
    public static int getDaysBetween(Date startDate, Date endDate) {
        // 如果开始日期在结束日期之后，两者调换位置
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        
        if (startCalendar.after(endCalendar)) {
            Calendar temp = startCalendar;
            startCalendar = endCalendar;
            endCalendar = temp;
        }
        int days = endCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR);
        int year = endCalendar.get(Calendar.YEAR);
        if (startCalendar.get(Calendar.YEAR) != year) {
            startCalendar = (Calendar) startCalendar.clone();
            do {
                days += startCalendar.getActualMaximum(Calendar.DAY_OF_YEAR);
                startCalendar.add(Calendar.YEAR, 1);
            } while (startCalendar.get(Calendar.YEAR) != year);
        }
        return days;
    }
    
    /**
     * 计算两个Calendar型日期相差的工作日天数（不区分起始日期、假期除外）
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 两日期相差的天数
     */
    public static int getWorkingDay(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        
        int result = -1;
        // 如果开始日期在结束日期之后，两者调换位置
        if (startCalendar.after(endCalendar)) {
            Calendar temp = startCalendar;
            startCalendar = endCalendar;
            endCalendar = temp;
        }
        
        // 开始日期的日期偏移量
        int charge_start_date = 0;
        // 结束日期的日期偏移量
        int charge_end_date = 0;
        
        int stmp;
        int etmp;
        stmp = 7 - startCalendar.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - endCalendar.get(Calendar.DAY_OF_WEEK);
        
        // 日期不在同一个日期内
        if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
            charge_start_date = stmp - 1;
        }
        if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
            charge_end_date = etmp - 1;
        }
        
        result = (getDaysBetween(getNextMonday(startDate), getNextMonday(endDate)) / 7) * 5 + charge_start_date - charge_end_date;
        // System.out.println("charge_start_date>" + charge_start_date);
        // System.out.println("charge_end_date>" + charge_end_date);
        // System.out.println("between day is-->" + betweendays);
        return result;
    }
    
    /**
     * 计算两个日期之间的非工作日天数
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 两日期相差的非工作日天数
     */
    public static int getHolidays(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        
        return getDaysBetween(startDate, endDate) - getWorkingDay(startDate, endDate);
        
    }
    
    /***************************** 月份操作 **********************************************************/
    /**
     * 组装完整的日期字符串
     * 
     * @param time 时间参数
     * @return 组装成 yyyy-MM-dd hh24:mi:ss.0格式的日期字符串
     */
    public static String compareTimestamp(String time) {
        String result = "";
        if (time == null || "".equals(time)) {
            return null;
        } else if (time.length() == 7) {
            result = time + "-01 00:00:00.0";
        } else if (time.length() == 10) {
            result = time + " 00:00:00.0";
        } else if (time.length() == 13) {
            result = time + ":00:00.0";
        } else if (time.length() == 16) {
            result = time + ":00.0";
        } else if (time.length() > 11 && time.indexOf(':') > 0 && time.indexOf('.') < 0) {
            result = time + ":00.0";
        }
        return result;
    }
    
    /**
     * 获取yyyyMM格式的当前日期上个月份日期
     * 
     * @return yyyyMM格式的当前日期上个月份日期
     */
    public static String getPreMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        return formatDate(calendar.getTime(), "yyyyMM");
    }
    /**
     * 获取yyyyMMddHHmmss格式的当前日期前一天日期
     * 
     * @return yyyyMMddHHmmss格式的当前日期前一天日期
     */
    public static String getPreDateTime() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.DATE, -1);
    	return formatDate(calendar.getTime(), "yyyyMMddHHmmss");
    }
    
    /**
     * 获取yyyy-MM格式的当前日期前一天日期
     * 
     * @return yyyy-MM格式的当前日期前一天日期
     */
    public static String getFlagPreMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        return formatDate(calendar.getTime(), "yyyy-MM");
    }
    
    /**
     * 获取指定日期的月初日期字符串
     * 
     * @param param 日期字符串
     * @return yyyy-MM-dd格式的字符串
     */
    public static String getMonthBegin(String param) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(compareTimestamp(param)));
        calendar.set(Calendar.DATE, 1);
        return formatDate(calendar.getTime());
    }
    
    /**
     * 获取指定日期的月末日期字符串
     * 
     * @param param 日期字符串
     * @return yyyy-MM-dd格式的字符串
     */
    public static String getMonthEnd(String param) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(compareTimestamp(param)));
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatDate(calendar.getTime());
    }
    
    /**
     * 获取指定日期的月初日期字符串
     * 
     * @param param 日期字符串
     * @return yyyy-MM-dd格式的字符串
     */
    public static Date getMonthBeginDate(String param) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(compareTimestamp(param)));
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }
    
    /**
     * 获取指定日期的月末日期字符串
     * 
     * @param param 日期字符串
     * @return yyyy-MM-dd格式的字符串
     */
    public static Date getMonthEndDate(String param) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(compareTimestamp(param)));
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
    
    /**
     * 功能描述：取得指定月份的第一天
     * 
     * @param strdate String 字符型日期
     * @return String yyyy-MM-dd 格式
     */
    public static Date getCurrMonthBegin() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * 取得当前月份的最后一天
     * 
     * @return 当前月份的最后一天
     */
    public static Date getCurrMonthEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
    
    /**
     * 获取当前月的第一天
     * 
     * @return 当前月的第一天
     */
    public static String getCurrMonthBeginStr() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, 1);
        return formatDate(calendar.getTime());
    }
    
    /**
     * 取得当前月份的最后一天
     * 
     * @return 当前月份的最后一天
     */
    public static String getCurrMonthEndStr() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatDate(calendar.getTime());
    }
    
    /**
     * 根据指定日期获取Date型的上月的第一天
     * 
     * @param param 日期参数
     * @return 上月的第一天
     */
    public static Date getPreMonthBegin(Date param) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(param);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * 根据指定日期获取Date型的上月的最后一天
     * 
     * @param param 日期参数
     * @return 上月的最后一天
     */
    public static Date getPreMonthEnd(Date param) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(param);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
    
    /**
     * 根据指定日期获取yyyy-MM-dd格式的上月的第一天
     * 
     * @param param 日期参数
     * @return 上月的第一天
     */
    public static String getPreMonthBeginStr(Date param) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(param);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, 1);
        return formatDate(calendar.getTime());
    }
    
    /**
     * 根据指定日期获取yyyy-MM-dd格式的上月的最后一天
     * 
     * @param param 日期参数
     * @return 上月的最后一天
     */
    public static String getPreMonthEndStr(Date param) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(param);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatDate(calendar.getTime());
    }
    
    /***************************** 星期操作 **********************************************************/
    
    /**
     * 根据指定日期获取指定日期对应周的中文字符串(如：星期一)
     * 
     * @param date 指定日期
     * @return 周字符串
     */
    public static String getChineseWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayNames[dayOfWeek - 1];
    }
    
    /**
     * 根据字符串日期获得对应的中文星期
     * 
     * @param date 日期参数
     * @return 中文星期
     */
    public static String getChineseWeek(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(date));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayNames[dayOfWeek - 1];
    }
    
    /**
	* 根据日期计算属于第几周
	* @param date 格式 yyyy-MM-dd
	* @throws ParseException
	*/
	public static int getWeekOfYear(String date){
	   try {
	       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	       Calendar cal = Calendar.getInstance(Locale.CHINA);
	       cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
	       //cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
	       cal.setMinimalDaysInFirstWeek(1); // 设置每周最少为1天
	       cal.setTime(df.parse(date));
	       return cal.get(Calendar.WEEK_OF_YEAR);
	   } catch (Exception e) {
               e.printStackTrace();
	           }
	   return 0;
	}
    
    /**
     * 获取指定日期所在的下周一的Calendar格式的日期
     * 
     * @param date 指定日期
     * @return Calendar型日期
     */
    public static Date getNextMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        do {
            calendar = (Calendar) calendar.clone();
            calendar.add(Calendar.DATE, 1);
        } while (calendar.get(Calendar.DAY_OF_WEEK) != 2);
        return calendar.getTime();
    }
    
    /**
     * 获取当前日期对应的本周一的yyyy-MM-dd格式的日期
     * 
     * @return yyyy-MM-dd格式的日期
     */
    public static String getCurrMonday() {
        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(System.currentTimeMillis());
        curr.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return formatDate(curr.getTime());
        
    }
    
    /**
     * 获取当前日期对应的本周一的日期
     * 
     * @return Date型日期
     */
    public static Date getCurrMondayDate() {
        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(System.currentTimeMillis());
        curr.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return curr.getTime();
        
    }
    
    /**
     * 获取当前日期对应的上周一的yyyy-MM-dd格式的日期
     * 
     * @return yyyy-MM-dd格式的日期
     */
    public static String getPreWeekMonday() {
        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(System.currentTimeMillis());
        curr.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        curr.add(Calendar.DAY_OF_WEEK, -7);
        return formatDate(curr.getTime());
    }
    
    /**
     * 获取当前日期对应的周末的yyyy-MM-dd格式的日期
     * 
     * @return yyyy-MM-dd格式的日期
     */
    public static String getCurrSunday() {
        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(System.currentTimeMillis());
        curr.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        curr.add(Calendar.DAY_OF_WEEK, 7);
        return formatDate(curr.getTime());
    }
    
    /**
     * 判断指定日期是否为周末（周六或周日）
     * 
     * @param date 指定日期
     * @return 是否周末 是：true 否：false
     */
    public static boolean isWeekDays(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == Calendar.SATURDAY || dayofweek == Calendar.SUNDAY) {
            return workHolidays.indexOf(formatDate(date)) == -1;
        } else {
            return false;
        }
    }
    
    /**
     * 判断某个日期是否为法定节假日
     * 
     * @param date 指定日期
     * @return 是否周末 是：true 否：false
     */
    public static boolean isHolidays(Date date) {
        return holidays.indexOf(formatDate(date)) >= 0;
    }
    
    /**
     * 获得从当前日期开始的前N天的日期集合
     * 
     * @param days 需要获取的天数
     * @param format 日期格式化样式
     * @return 日期集合
     */
    public static List<String> getWorkDays(int days, String format) {
        List<String> result = new ArrayList<String>();
        Date currdate = new Date();
        Date date;
        int num = -1;
        do {
            date = addDate(currdate, num);
            if (!isWeekDays(date) && !isHolidays(date)) {
                result.add(formatDate(date, format));
            }
            num--;
        } while (result.size() < days);
        return result;
    }
    
    /**
     * 获取指定格式的从当前日期开始往前N天的工作日的集合
     * 
     * @param days 需要获取的天数
     * @param format 日期格式化样式
     * @return 工作日集合
     */
    public static List<String> getWorkDaysOrderDesc(int days, String format) {
        List<String> result = new ArrayList<String>();
        List<String> dates = getWorkDays(days, format);
        for (int j = dates.size() - 1; j >= 0; j--) {
            result.add(dates.get(j));
        }
        return result;
    }
    
    /**
     * 获取当前日期的上一个工作日日期
     * 
     * @return yyyy-MM-dd格式的日期
     */
    public static String getPreWorkDay() {
        Date currdate = new Date();
        Date date;
        int num = -1;
        do {
            date = addDate(currdate, num);
            num--;
        } while (isWeekDays(date) || isHolidays(date));
        
        return DateUtil.formatDate(date);
    }

	// 获取相隔分钟数
	static public long getDistinceMinute(String beforeDateTime,String afterDateTime) {
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long timeCount = 0;
		try {
			Date d1 = d.parse(beforeDateTime);
			Date d2 = d.parse(afterDateTime);
			timeCount = (d2.getTime() - d1.getTime()) / (60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeCount;
	}
	
	// 获取当前时间所在年的周数
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    // 获取当前时间所在年的最大周数
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

        return getWeekOfYear(c.getTime());
    }

    // 获取某年的第几周的开始日期
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);
        
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    // 获取某年的第几周的结束日期
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    // 获取当前时间所在周的开始日期
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    // 获取当前时间所在周的结束日期
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }
    
    /** 
     * 功能概述：计算指定年月的天数和周数<br> 
     * 创建时间：2010-5-17 下午05:25:58<br> 
     */
    public static int getWeeksByMonth(int year , int month){
    	int weeks = 0;
    	Calendar c = Calendar.getInstance();
    	try {
			c.set(Calendar.YEAR,year);
			c.set(Calendar.MONTH, month);
			weeks = c.getActualMaximum(Calendar.DAY_OF_WEEK);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return weeks;
    }
}
