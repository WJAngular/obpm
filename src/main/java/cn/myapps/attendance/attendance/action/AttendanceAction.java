package cn.myapps.attendance.attendance.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.myapps.attendance.attendance.ejb.Attendance;
import cn.myapps.attendance.attendance.ejb.AttendanceDetail;
import cn.myapps.attendance.attendance.ejb.AttendanceDetailProcess;
import cn.myapps.attendance.attendance.ejb.AttendanceDetailProcessBean;
import cn.myapps.attendance.attendance.ejb.AttendanceProcess;
import cn.myapps.attendance.attendance.ejb.AttendanceProcessBean;
import cn.myapps.attendance.base.action.BaseAction;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.http.HttpRequestUtil;

public class AttendanceAction extends BaseAction<Attendance> {
	
	private static final long serialVersionUID = -1021941166716325951L;

	public AttendanceAction() {
		this.content = new Attendance();
		this.process = new AttendanceProcessBean();
	}

	/**
	 * 签到功能
	 * @return
	 */
	public String doSignin() {

		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			Attendance po = (Attendance) getContent();
			Map<String, Object> result = ((AttendanceProcess)process).signin(po, params, user);
			Date signin = (Date) result.get("time");
			Boolean status = (Boolean) result.get("status");
			String message = (String) result.get("message");
			Attendance data = (Attendance) result.get("data");
			if(signin!=null){
				this.getDataMap().put("signin", signin);
			}
			addActionResult(status, message, data);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if (!(e instanceof OBPMValidateException)) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 签退功能
	 * @return
	 */
	public String doSignout() {
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			Map<String, Object> result = ((AttendanceProcess)process).signout(params, user);
			Date signout = (Date) result.get("time");
			Boolean status = (Boolean) result.get("status");
			String message = (String) result.get("message");
			Attendance data = (Attendance) result.get("data");
			if(signout!=null){
				this.getDataMap().put("signout", signout);
			}
			addActionResult(status, message, data);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if (!(e instanceof OBPMValidateException)) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	
	
	
	/**
	 * 考勤记录查询 
	 * @return
	 */
	public String doQuery() {
		try {
			ParamsTable params = getParams();
			
			DataPackage<Attendance> datas = ((AttendanceProcess)process).doQuery(params, getUser());
			dataMap.put("total", datas.getRowCount());
			dataMap.put("rows", datas.getDatas());
		} catch (Exception e) {
			//addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 获取当天考勤记录(手机端)
	 * @return
	 */
	public String recordQuery() {
		try {
			ParamsTable params = getParams();
			DataPackage<Attendance> datas = ((AttendanceProcess)process).recordQuery(params, getUser());
			addActionResult(true, "", datas.datas);
		} catch (Exception e) {
			//addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 根据条件查询相应的相关的考勤记录(手机端)
	 * @return
	 */
	public String queryBy() {
		try {
			ParamsTable params = getParams();
			DataPackage<Attendance> datas = ((AttendanceProcess)process).queryBy(params, getUser());
			addActionResult(true, "", datas.datas);
		} catch (Exception e) {
			//addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 统计考勤记录(手机端)
	 * @return
	 */
	public String querychart() {
		try {
			DataPackage<Attendance> datas = ((AttendanceProcess)process).queryChart(getUser());
			addActionResult(true, "", datas.datas);
		} catch (Exception e) {
			//addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}


	/**
	 * 获取相关选项(手机端)
	 * @return
	 */
	public String getSelect() {
		try {
			Collection<String> datas = new ArrayList<String>();
			WebUser user = getUser();
			String userId = user.getId();
			UserProcess us = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			Collection<UserVO> data = us.getUnderList(userId,1);
			Iterator<UserVO> it = data.iterator();
			while(it.hasNext()){
				   UserVO users = it.next();
				   String name = "<option value=" + users.getId() + ">" + users.getName() + "</option>";
				   datas.add(name);
				  }
			addActionResult(true, "", datas);
		} catch (Exception e) {
			//addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 根据考勤记录获取对应的考勤明细 
	 * @return
	 */
	public String doQueryAttendanceDetail() {
		try {
			ParamsTable params = getParams();
			String attendanceId = params.getParameterAsString("attendanceId");
			Attendance po = (Attendance) ((AttendanceProcess)process).doView(attendanceId);
			AttendanceDetailProcess adProcess = new AttendanceDetailProcessBean();
			DataPackage<AttendanceDetail> datas = adProcess.getAttendanceDetailByAttendance(po.getId(),po.getAttendanceDate(),null);
			dataMap.put("total", datas.getRowCount());
			dataMap.put("rows", datas.getDatas());
		} catch (Exception e) {
			//addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String ConvertGps2Baidu() {
		try {
			ParamsTable params = getParams();
			String form = params.getParameterAsString("from");
			String x = params.getParameterAsString("x");
			String y = params.getParameterAsString("y");
			
			String url = "http://api.map.baidu.com/ag/coord/convert?from="+ form + "&to=4&x=" + x + "&y=" + y;
			JSONObject result = HttpRequestUtil.get(url, null);
			
			Map<String,String> point = new HashMap<String, String>();
			point.put("x", Security.decodeBASE64(result.getString("x")));
			point.put("y", Security.decodeBASE64(result.getString("y")));
			
			addActionResult(true, "", point);
		} catch (Exception e) {
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
}
