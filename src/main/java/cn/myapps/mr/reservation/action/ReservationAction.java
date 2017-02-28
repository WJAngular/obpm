package cn.myapps.mr.reservation.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.python.modules.newmodule;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.base.action.BaseAction;
import cn.myapps.mr.reservation.ejb.Reservation;
import cn.myapps.mr.reservation.ejb.ReservationProcess;
import cn.myapps.mr.reservation.ejb.ReservationProcessBean;
import cn.myapps.mr.room.ejb.Room;
import cn.myapps.mr.room.ejb.RoomProcess;
import cn.myapps.mr.room.ejb.RoomProcessBean;
import cn.myapps.pm.base.ejb.BaseProcess;
import cn.myapps.pm.task.ejb.Task;
import cn.myapps.pm.task.ejb.TaskProcess;
import cn.myapps.util.StringUtil;

public class ReservationAction extends BaseAction<Reservation> {
	
	public ReservationAction() {
		super();
		content = new Reservation();
		process = new ReservationProcessBean();
	}
	
	/**
	 * 前台点击已保存的会议预定记录时，返回已经保存的信息。
	 * @throws Exception 
	 */
	public void initReservation() throws Exception
	{
		ParamsTable params = getParams();
//		String x = params.getParameterAsString("x");//会议时间号
//		String y = params.getParameterAsString("y");	//id
		String resid = params.getParameterAsString("id");
		String msg="";
		//根据会议室x和时间段y获取已存在的（会议室预定）记录，保存到msg中
		try {
			Reservation reservation = (Reservation)((ReservationProcess)process).queryById(resid);
			msg="{\"roomid\":\""+reservation.getRoomId()
			+"\",\"starttime\":\""+reservation.getStartTime()
			+"\",\"endtime\":\""+reservation.getEndTime()
			+"\",\"content\":\""+reservation.getContent()
			+"\",\"number\":\""+reservation.getCreatorTel()
			+"\",\"name\":\""+reservation.getName()
			+"\",\"username\":\""+reservation.getCreator()
			+"\",\"areaid\":\""+reservation.getAreaId()
			+"\",\"id\":\""+reservation.getId()+"\"}";

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			PrintWriter pw = response.getWriter();
			pw.write(msg.toString()); // 把内容放入response中
			pw.flush();
			pw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveReservation()
	{
		ParamsTable params = getParams();
		String name = params.getParameterAsString("name");//会议名称
		String content = params.getParameterAsString("content1");//会议内容
		String starttime = params.getParameterAsString("starttime");//开始时间
		String endtime = params.getParameterAsString("endtime");//结束时间
		String number = params.getParameterAsString("number");//结束时间
		String roomid = params.getParameterAsString("roomid");//结束时间
		String id = params.getParameterAsString("id");//
		String areaid = params.getParameterAsString("areaid");//
		String area = params.getParameterAsString("area");
		String room = params.getParameterAsString("room");
		
		String msg="";
		PrintWriter pw = null;
		//根据会议室x和时间段y获取已存在的（会议室预定）记录，保存到msg中
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			
			Date d1=null,d2=null;
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  //2015-03-10 00:00:00
	        d1 = sdf.parse(starttime); 
	        d2 = sdf.parse(endtime); 
			
			WebUser user = getUser();
			Reservation reservation = new Reservation();
			reservation.setName(name);
			reservation.setContent(content);
			reservation.setCreator(user.getName());
			reservation.setCreatorId(user.getId());
			reservation.setCreatorTel(number);
			reservation.setArea(area);
			reservation.setAreaId(areaid);
			reservation.setStartTime(d1);
			reservation.setEndTime(d2);
			reservation.setDomainid(user.getDomainid());
			reservation.setRoom(room);
			reservation.setRoomId(roomid);
			if(id!=null&&!"undefined".equals(id))
				reservation.setId(id);
			boolean b = ((ReservationProcess)process).doCreate(reservation, null);
			if(!b)
				msg="success";
			else {
				msg = "保存失败！";
			}
		} catch (IOException e) {
			msg = e.getMessage();
			e.printStackTrace();
		} catch (Exception e1) {
			msg = e1.getMessage();
			e1.printStackTrace();
		}finally{
			pw.write(msg.toString()); // 把内容放入response中
			pw.flush();
			pw.close();
		}
	}
	
	//前台点击“会议室预定”时初始化页面
	public String queryReservation(){
		try {
			ParamsTable params = getParams();
			String name = params.getParameterAsString("name");
			int status = params.getParameterAsInteger("status");
			String dates = params.getParameterAsString("date");
			Date date = null;
			DateFormat df = DateFormat.getDateInstance();

			if(dates!=null)
				date = df.parse(dates);
			else
				date = new Date();
			WebUser user = getUser();
			
			Collection<Reservation> reservations = null;
			reservations = ((ReservationProcess) process).queryByTime(date,user);
			
			addActionResult(true, "", reservations);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	/**
	 * “我的预定”初始化
	 * 获取满足条件下的所有会议室
	 */
	public String queryMyReservation(){
		try {
			ParamsTable params = getParams();
			String name = params.getParameterAsString("name");
			int status = params.getParameterAsInteger("status");
			String dates1 = params.getParameterAsString("date1");
			String dates2 = params.getParameterAsString("date2");
			String area = params.getParameterAsString("area");
			String room = params.getParameterAsString("room");
			int pages = params.getParameterAsInteger("pages");	//显示页数
			int lines = params.getParameterAsInteger("lines");	//显示行数
			Date date1 = null;
			Date date2 = null;
			DateFormat df = DateFormat.getDateInstance();
			
			if(lines==0)
				lines=5;
			if(dates1!=null){
				date1 = df.parse(dates1);
			}
			if(dates2!=null){
				date2 = df.parse(dates2);
			}
			WebUser user = getUser();
			Collection<Reservation> reservations = null;
			reservations = ((ReservationProcess) process).queryMyReservation(user,area,room,date1,date2,pages,lines);
			int count = ((ReservationProcess) process).countMyReservation(user,area,room,date1,date2);
			int countpage = count/lines+(count%lines>0?1:0);
			
			addActionResult(true, "", reservations);
			addActionCount(countpage);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * “我的预定”中获取点击的预定记录
	 */
	public void domyreservation() throws Exception{
		ParamsTable params = getParams();
		String id = params.getParameterAsString("id");	//会议室id
		
		String msg="";
		try {
			Reservation reservation = (Reservation)((ReservationProcess)process).queryById(id);
			//根据id获取会议室记录，以JSON格式存入
			msg="{\"creator\":\""+reservation.getCreator()
			+"\",\"creatorTel\":\""+reservation.getCreatorTel()
			+"\",\"name\":\""+reservation.getName()
			+"\",\"content\":\""+reservation.getContent()
			+"\",\"starttime\":\""+reservation.getStartTime()
			+"\",\"endtime\":\""+reservation.getEndTime()
			+"\",\"area\":\""+reservation.getAreaId()
			+"\",\"room\":\""+reservation.getRoomId()
			+"\",\"id\":\""+reservation.getId()
			+"\"}";
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			PrintWriter pw = response.getWriter();
			pw.write(msg.toString()); // 把内容放入response中
			pw.flush();
			pw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * “我的预定”中保存修改后的预定记录
	 */
	public void updateMyreservation()
	{
		ParamsTable params = getParams();
		String creatorTel = params.getParameterAsString("creatorTel");//预定人电话
		String name = params.getParameterAsString("name");//会议名称
		String content = params.getParameterAsString("mycontent");//会议内容
		String starttime = params.getParameterAsString("starttime");//开始时间
		String endtime = params.getParameterAsString("endtime");//结束时间
		String area = params.getParameterAsString("area");//区域
		String room = params.getParameterAsString("room");//会议室
		String areaid = params.getParameterAsString("areaid");//区域
		String roomid = params.getParameterAsString("roomid");//会议室
		String id = params.getParameterAsString("id");//
		
		String msg="";
		PrintWriter pw = null;
		//根据会议室x和时间段y获取已存在的（会议室预定）记录，保存到msg中
		try {
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			
			Date d1=null,d2=null;
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  //2015-03-10 00:00:00
	        d1 = sdf.parse(starttime); 
	        d2 = sdf.parse(endtime); 
			
			WebUser user = getUser();
			Reservation reservation = (Reservation)((ReservationProcess)process).queryById(id);
			reservation.setId(id);
			reservation.setDomainid(user.getDomainid());
			reservation.setCreator(user.getName());
			reservation.setCreatorId(user.getId());
			reservation.setCreatorTel(creatorTel);
			reservation.setName(name);
			reservation.setContent(content);
			reservation.setStartTime(d1);
			reservation.setEndTime(d2);
			reservation.setArea(area);
			reservation.setRoom(room);
			reservation.setRoomId(roomid);
			reservation.setAreaId(areaid);
			if(id!=null&&!"undefined".equals(id))
				reservation.setId(id);
			boolean b = ((ReservationProcess)process).doCreate(reservation, null);
			if(!b)
				msg="success";
			else {
				msg = "保存失败！";
			}
			
		} catch (IOException e) {
			msg = e.getMessage();
			e.printStackTrace();
		} catch (Exception e1) {
			msg = e1.getMessage();
			e1.printStackTrace();
		}
		pw.write(msg.toString()); // 把内容放入response中
		pw.flush();
		pw.close();
		
	}
	
	public void deleteMyReservation()
	{
		ParamsTable params = getParams();
		String id = params.getParameterAsString("id");//
		
		String msg="";
		PrintWriter pw = null;
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			
			WebUser user = getUser();
			((ReservationProcess)process).delete(id);
			msg="success";
		} catch (IOException e) {
			msg = e.getMessage();
			e.printStackTrace();
		} catch (Exception e1) {
			msg = e1.getMessage();
			e1.printStackTrace();
		}finally{
			pw.write(msg.toString()); // 把内容放入response中
			pw.flush();
			pw.close();
		}
	}
}
