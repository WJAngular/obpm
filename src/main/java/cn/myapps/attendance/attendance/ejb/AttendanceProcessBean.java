package cn.myapps.attendance.attendance.ejb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.myapps.attendance.attendance.dao.AttendanceDAO;
import cn.myapps.attendance.base.dao.BaseDAO;
import cn.myapps.attendance.base.dao.DaoManager;
import cn.myapps.attendance.base.ejb.AbstractBaseProcessBean;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;


public class AttendanceProcessBean extends AbstractBaseProcessBean<Attendance>
		implements AttendanceProcess {


	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getAttendanceDAO(getConnection());
	}

	public Map<String,Object> signin(Attendance attendance,ParamsTable params,WebUser user) throws Exception {
		
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			Attendance po = findTodayAttendanceByUser(user);
			String multiPeriod = params.getParameterAsString("multiPeriod");  
			String signinLocation = params.getParameterAsString("signinLocation");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			
			if(po == null){
				//1.获取数据，创建考勤记录
				po = attendance; 
				po.setUserId(user.getId());
				po.setUserName(user.getName());
				po.setDeptId(user.getDefaultDepartment());
				po.setDeptName(user.getDepartmentById(user.getDefaultDepartment()).getName());
				po.setDomainid(user.getDomainid());
				po.setMultiPeriod("true".equals(multiPeriod));
				po.setAttendanceDate(calendar.getTime());
				po.setStatus(Attendance.STATUS_LEAVE_EARLY);
				
				//2.生成考勤明细
				AttendanceDetail ar = new AttendanceDetail();
				ar.setUserId(user.getId());
				ar.setUserName(user.getName());
				ar.setDeptId(user.getDefaultDepartment());
				ar.setDeptName(user.getDepartmentById(user.getDefaultDepartment()).getName());
				ar.setDomainid(user.getDomainid());
				ar.setSigninTime(new Date());
				ar.setSigninLocation(signinLocation);
				ar.setAttendanceDate(calendar.getTime());
				
				CalendarProcess calendarProcess = (CalendarProcess) ProcessFactory.createProcess(CalendarProcess.class);
				CalendarVO cal = (CalendarVO) calendarProcess.doView(user.getCalendarType());
				
				 Date st = null;  // 工作时间段
				  //2.判断是否为分时段
				  if(po.isMultiPeriod()){
					   String dayPart = cal.getDayPartIncludeStartAfterTime(new Date(),po.isMultiPeriod());
					   st  = cal.findTimeRegionStartDate(new Date());
					   //因思程科技需求 ，弃掉该时段为非打卡时间
					   if(st == null ){
							  return getResultMap(null,false,"该时段为非打卡时间！",po);
					   }
					   
					   if(st.getTime()<ar.getSigninTime().getTime()){ //迟到状态
							ar.setStatus(AttendanceDetail.STATUS_LATE);
							po.setStatus(Attendance.STATE_LATE_AND_LEAVE_EARLY);
						}else{
							ar.setStatus(AttendanceDetail.STATUS_LEAVE_EARLY);
						}
					   ar.setTimeRegion(dayPart);
				  }else{
					    String dayPart = cal.getDayPartIncludeStartAfterTime(new Date(),po.isMultiPeriod());
					    st = cal.findStartDate(new Date());
					    //因思程科技需求 ，弃掉该时段为非打卡时间
					    if(st == null ){
							  return getResultMap(null,false,"该时段为非打卡时间！",po);
					   }
					
					    if(st.getTime()<ar.getSigninTime().getTime()){//迟到状态
							ar.setStatus(AttendanceDetail.STATUS_LATE);
							po.setStatus(Attendance.STATE_LATE_AND_LEAVE_EARLY);
						}else{
							ar.setStatus(AttendanceDetail.STATUS_LEAVE_EARLY);
						}
						ar.setTimeRegion(dayPart);
				  }
				  po.getAttendanceDetailList().add(ar);
				  po = (Attendance) doCreate(po);
			
				  return getResultMap(ar.getSigninTime(),true,"签到成功",po);
				  
			}else{
				//获取考勤明细
				AttendanceDetailProcess adProcess = new AttendanceDetailProcessBean();
				DataPackage<AttendanceDetail> datas = adProcess.getTodayAttendanceDetailByAttendanceId(po.getId());
				po.setAttendanceDetailList((List<AttendanceDetail>) datas.getDatas());
				List<AttendanceDetail> list = po.getAttendanceDetailList();
				
				if(po.isMultiPeriod()){
					CalendarProcess calendarProcess = (CalendarProcess) ProcessFactory.createProcess(CalendarProcess.class);
					CalendarVO cal = (CalendarVO) calendarProcess.doView(user.getCalendarType());
					
					String dayPart = cal.getDayPartIncludeStartAfterTime(new Date(),po.isMultiPeriod());
					Date st = cal.findTimeRegionStartDate(new Date());
					 //因思程科技需求 ，弃掉该时段为非打卡时间
					 if(st == null || StringUtil.isBlank(dayPart) ){
						  return getResultMap(null,false,"该时段为非打卡时间！",po);
				     }
					
					Boolean isExist = false ; 
					Date date = new Date();
					for(AttendanceDetail ar : list){
						if(dayPart.equals(ar.getTimeRegion()) && ar.getSignoutTime() == null ){ // 存在时间区间范围内且未被签退
							isExist = true;
							date = ar.getSigninTime();
							break;
						}
					}
					if(isExist){
						  return getResultMap(null,false,"您已经在"+new SimpleDateFormat("HH:mm").format(date)+"签过到啦！",po);
					}else{
						AttendanceDetail ar = new AttendanceDetail();
						ar.setUserId(user.getId());
						ar.setUserName(user.getName());
						ar.setDeptId(user.getDefaultDepartment());
						ar.setDeptName(user.getDepartmentById(user.getDefaultDepartment()).getName());
						ar.setDomainid(user.getDomainid());
						ar.setSigninTime(new Date());
						ar.setSigninLocation(signinLocation);
					    if(st.getTime()<ar.getSigninTime().getTime()){ //迟到状态
							ar.setStatus(AttendanceDetail.STATUS_LATE);
							po.setStatus(Attendance.STATE_LATE_AND_LEAVE_EARLY);
						}else{
							ar.setStatus(AttendanceDetail.STATUS_LEAVE_EARLY);
						}
					    //设置时间段
					    ar.setTimeRegion(dayPart);
					    ar.setAttendanceDate(calendar.getTime());
					    ar.setAttendanceId(po.getId());
					    
						po.getAttendanceDetailList().add(ar);
						po = (Attendance)doUpdate(po);
						
						return getResultMap(ar.getSigninTime(),true,"签到成功",po);
					}
				}else{
					  return getResultMap(null,false,"您已经在"+new SimpleDateFormat("HH:mm").format(list.get(0).getSigninTime())+"签过到啦！",po);
				}
			}
		} catch (Exception e) {
			if (!(e instanceof OBPMValidateException)) {
				e.printStackTrace();
			}
			throw e ;
		}
	}

	public Map<String,Object> signout(ParamsTable params,WebUser user) throws Exception {
		
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			String signoutLocation = params.getParameterAsString("_signoutLocation");
			String multiPeriod = params.getParameterAsString("multiPeriod");  
			CalendarProcess calendarProcess = (CalendarProcess) ProcessFactory.createProcess(CalendarProcess.class);
			CalendarVO cal = (CalendarVO) calendarProcess.doView(user.getCalendarType());
			Attendance po = findTodayAttendanceByUser(user);
			if(po ==null){
				//因思程科技业务需求，如不签到，则可以签退
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);	
				//1.获取数据，创建考勤记录
				po = new Attendance(); 
				po.setUserId(user.getId());
				po.setUserName(user.getName());
				po.setDeptId(user.getDefaultDepartment());
				po.setDeptName(user.getDepartmentById(user.getDefaultDepartment()).getName());
				po.setDomainid(user.getDomainid());
				po.setMultiPeriod("true".equals(multiPeriod));
				po.setAttendanceDate(calendar.getTime());
				po.setStatus(Attendance.STATUS_NORMAL);
				//2.生成考勤明细
				String dayPart = cal.getDayPartIncludeStartAfterTime(new Date(),po.isMultiPeriod());
				AttendanceDetail ar = new AttendanceDetail();
				ar.setUserId(user.getId());
				ar.setUserName(user.getName());
				ar.setDeptId(user.getDefaultDepartment());
				ar.setDeptName(user.getDepartmentById(user.getDefaultDepartment()).getName());
				ar.setDomainid(user.getDomainid());
				ar.setAttendanceDate(calendar.getTime());
				ar.setTimeRegion(dayPart);
				ar.setStatus(AttendanceDetail.STATUS_NORMAL);
				Date st = null;  // 工作时间段
				  //2.判断是否为分时段
				if(po.isMultiPeriod()){
					   st  = cal.findTimeRegionStartDate(new Date());
					   //因思程科技需求 ，弃掉该时段为非打卡时间
					   if(st == null ){
							  return getResultMap(null,false,"该时段为非打卡时间！",po);
					   }
				  }else{
					    st = cal.findStartDate(new Date());
					    //因思程科技需求 ，弃掉该时段为非打卡时间
					    if(st == null ){
							  return getResultMap(null,false,"该时段为非打卡时间！",po);
					   }
				  }  
				
				po.getAttendanceDetailList().add(ar);
				po = (Attendance) doCreate(po);
				//因思程科技业务需求，如不签到，则可以签退
//			    return getResultMap(null,false,"您今天还未签到",po);
			}
			Date outTime = null;//获取工作日历的下班时间
			Date currentDate = new Date();
			
			//获取考勤明细
			AttendanceDetailProcess adProcess = new AttendanceDetailProcessBean();
			DataPackage<AttendanceDetail> datas = adProcess.getTodayAttendanceDetailByAttendanceId(po.getId());
			po.setAttendanceDetailList((List<AttendanceDetail>) datas.getDatas());
			List<AttendanceDetail> list = po.getAttendanceDetailList();
  
			if(po.isMultiPeriod()){
				AttendanceDetail ar = null ;
				Boolean isExist = false ;
				for(AttendanceDetail tmp: list){
					
					outTime = cal.findTimeRegionEndDate(tmp.getTimeRegion()); 
					 //因思程科技需求 ，弃掉该时段为非打卡时间
					 if(outTime == null){
						  return getResultMap(null,false,"该时段为非打卡时间！",po);
				     }
					
					if(cal.isTimeRegionEndDate(currentDate,outTime)){ //判断
						isExist =true ;
						ar = tmp;
						break ;
					}
				}
				if(isExist){
					ar.setSignoutLocation(signoutLocation);
					ar.setSignoutTime(currentDate);
					if(outTime.getTime()>ar.getSignoutTime().getTime()){//早退状态
						if(ar.getStatus()==AttendanceDetail.STATUS_LATE){
							ar.setStatus(AttendanceDetail.STATE_LATE_AND_LEAVE_EARLY);
						}else if(ar.getStatus()==AttendanceDetail.STATUS_LEAVE_EARLY) {
							ar.setStatus(AttendanceDetail.STATUS_LEAVE_EARLY);
						}else if(ar.getStatus()==AttendanceDetail.STATE_LATE_AND_LEAVE_EARLY){
							ar.setStatus(AttendanceDetail.STATE_LATE_AND_LEAVE_EARLY);
						}else if(ar.getStatus() == AttendanceDetail.STATUS_NORMAL){
							ar.setStatus(AttendanceDetail.STATUS_LEAVE_EARLY);
						}
					}else {
						if(ar.getStatus()==AttendanceDetail.STATUS_LEAVE_EARLY){
							ar.setStatus(Attendance.STATUS_NORMAL);//考勤正常
					    }else if(ar.getStatus()==AttendanceDetail.STATUS_LATE){
					    	ar.setStatus(Attendance.STATUS_LATE);
					    }else if(ar.getStatus()==AttendanceDetail.STATE_LATE_AND_LEAVE_EARLY){
					    	ar.setStatus(Attendance.STATUS_LATE);
					    }else if(ar.getStatus() == AttendanceDetail.STATUS_NORMAL){
					    	ar.setStatus(Attendance.STATUS_NORMAL);//考勤正常
						}
					}
					if(ar.getSigninTime() == null){
						ar.setWorkingHours(0.00);
					}else{
						ar.setWorkingHours(DateUtil.getWorkingTimesCount(ar.getSigninTime(), ar.getSignoutTime(), user.getCalendarType()));
					}
					
					UpdateStateAndWorkingHours(po,cal);
					
					doUpdate(po);
					
					 return getResultMap(ar.getSignoutTime(),true,"签退成功",po);
				}else{
				    return getResultMap(null,false,"您在该时段还未签到",po);
				}
			}else{ 
				AttendanceDetail ar = list.get(0);
				ar.setSignoutLocation(signoutLocation);
				ar.setSignoutTime(new Date());
				outTime = cal.findEndDate(new Date());
				 //因思程科技需求 ，弃掉该时段为非打卡时间
				 if(outTime == null){
					  return getResultMap(null,false,"该时段为非打卡时间！",po);
			     }
				 if(outTime.getTime()>ar.getSignoutTime().getTime()){//早退状态
						if(ar.getStatus()==AttendanceDetail.STATUS_LATE){
							ar.setStatus(AttendanceDetail.STATE_LATE_AND_LEAVE_EARLY);
						}else if(ar.getStatus()==AttendanceDetail.STATUS_LEAVE_EARLY) {
							ar.setStatus(AttendanceDetail.STATUS_LEAVE_EARLY);
						}else if(ar.getStatus()==AttendanceDetail.STATE_LATE_AND_LEAVE_EARLY){
							ar.setStatus(AttendanceDetail.STATE_LATE_AND_LEAVE_EARLY);
						}else if(ar.getStatus() == AttendanceDetail.STATUS_NORMAL){
							ar.setStatus(AttendanceDetail.STATUS_LEAVE_EARLY);
						}
					}else {
						if(ar.getStatus()==AttendanceDetail.STATUS_LEAVE_EARLY){
							ar.setStatus(Attendance.STATUS_NORMAL);//考勤正常
					    }else if(ar.getStatus()==AttendanceDetail.STATUS_LATE){
					    	ar.setStatus(Attendance.STATUS_LATE);
					    }else if(ar.getStatus()==AttendanceDetail.STATE_LATE_AND_LEAVE_EARLY){
					    	ar.setStatus(Attendance.STATUS_LATE);
					    }else if(ar.getStatus() == AttendanceDetail.STATUS_NORMAL){
					    	ar.setStatus(Attendance.STATUS_NORMAL);//考勤正常
						}
					}
				if(ar.getSigninTime() == null){
					ar.setWorkingHours(0.00);
				}else{
					ar.setWorkingHours(DateUtil.getWorkingTimesCount(ar.getSigninTime(), ar.getSignoutTime(), user.getCalendarType()));
				}
				UpdateStateAndWorkingHours(po,cal);
				doUpdate(po);
				return getResultMap(ar.getSignoutTime(),true,"签退成功",po);

			}
		} catch (Exception e) {
			if (!(e instanceof OBPMValidateException)) {
				e.printStackTrace();
			}
			throw e ;
		}
	}
	
	/**
	 * 获取结果集
	 * @param time
	 * @param status
	 * @param message
	 * @param attendance
	 * @return
	 */
	private Map<String,Object> getResultMap(Date time, Boolean status, String message, Attendance attendance){
		Map<String,Object> result = new HashMap<String,Object>();
		 result.put("time", time);
	     result.put("status", status);
		 result.put("message", message);
		 result.put("data", attendance);
		 return result; 
	}
	
	/**
	 * 更新工作时长和考勤状态
	 * @param po
	 * @param cal 
	 */
	private void UpdateStateAndWorkingHours(Attendance po, CalendarVO cal) {
		
		try {
			Double workingHours = 0.0 ;
	     	
	     	Map<String,Integer> stateMap = new HashMap<String,Integer>();
	     	
	     	for(AttendanceDetail ad : po.getAttendanceDetailList()){
				 //1.更新工作时间
				 workingHours += ad.getWorkingHours();
				 //2.获取考勤状态
				 stateMap.put(ad.getTimeRegion(), ad.getStatus());
			}
			po.setWorkingHours(workingHours);
			
			//根据状态Map更新记录状态
			if(stateMap.containsValue(Attendance.STATE_LATE_AND_LEAVE_EARLY)){
				po.setStatus(Attendance.STATE_LATE_AND_LEAVE_EARLY);
			}else if(stateMap.containsValue(Attendance.STATUS_LATE) && stateMap.containsValue(Attendance.STATUS_LEAVE_EARLY)){
				po.setStatus(Attendance.STATE_LATE_AND_LEAVE_EARLY);
			}else{
				if(stateMap.containsValue(Attendance.STATUS_LATE)){
					po.setStatus(Attendance.STATUS_LATE);
				}else if(stateMap.containsValue(Attendance.STATUS_LEAVE_EARLY)){
					po.setStatus(Attendance.STATUS_LEAVE_EARLY);
				}else if(stateMap.containsValue(Attendance.STATUS_NORMAL)){
					po.setStatus(Attendance.STATUS_NORMAL);
				}else{
					po.setStatus(Attendance.STATUS_UNNORMAL);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ValueObject doCreate(ValueObject vo) throws Exception {
		try {
			beginTransaction();
			if(StringUtil.isBlank(vo.getId())){
				vo.setId(Sequence.getSequence());
			}
			vo = getDAO().create(vo);
			
			//考勤明细
			Attendance attendance = (Attendance)vo ;
			AttendanceDetailProcess process = new AttendanceDetailProcessBean();
			List<AttendanceDetail> attendanceDetailList = attendance.getAttendanceDetailList();
			for(AttendanceDetail detail : attendanceDetailList){
				detail.setAttendanceId(vo.getId());
				process.doCreate(detail);
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return vo;
		
	}
	
	@Override
	public ValueObject doUpdate(ValueObject vo) throws Exception {
		try {
			beginTransaction();
			vo = getDAO().update(vo);
			
			//考勤明细
			Attendance attendance = (Attendance)vo ;
			AttendanceDetailProcess process = new AttendanceDetailProcessBean();
			List<AttendanceDetail> attendanceDetailList = attendance.getAttendanceDetailList();
			for(AttendanceDetail detail : attendanceDetailList){
				if(detail.getId() != null){
					process.doUpdate(detail);
				}else{
					process.doCreate(detail);
				}
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return vo;
	}

	public Attendance findTodayAttendanceByUser(WebUser user) throws Exception {
		return ((AttendanceDAO)getDAO()).findTodayAttendanceByUser(user);
	}

	public DataPackage<Attendance> recordQuery(ParamsTable params, WebUser user)
			throws Exception {
		return (DataPackage<Attendance>) ((AttendanceDAO) getDAO()).recordquery(params, user);
	}
	
	public DataPackage<Attendance> queryBy(ParamsTable params, WebUser user)
			throws Exception {
		return (DataPackage<Attendance>) ((AttendanceDAO) getDAO()).queryby(params, user);
	}

	public DataPackage<Attendance> queryChart(WebUser user) 
			throws Exception {
		return (DataPackage<Attendance>) ((AttendanceDAO) getDAO()).queryChart(user);
	}

	/** 
	* @Title: findAttendanceByDate 
	* @Description: TODO
	* @param: @param date
	* @param: @return
	* @param: @throws Exception 
	* @throws 
	*/
	@Override
	public List<Attendance> findAttendanceByDate(String date) throws Exception {
		return ((AttendanceDAO)getDAO()).findAttendanceByDate(date);
	}
}
