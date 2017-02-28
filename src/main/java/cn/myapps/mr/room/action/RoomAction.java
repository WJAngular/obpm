package cn.myapps.mr.room.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class RoomAction extends BaseAction<Room> {

	public RoomAction() {
		super();
		content = new Room();
		process =  new RoomProcessBean();
	}
	
	public String queryAllRoom(){
		try {
			ParamsTable params = getParams();

			Collection<Room> rooms = null; 
			rooms = ((RoomProcess)process).getAllRooms();

			addActionResult(true, "", rooms);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String queryRoomByArea(){
		try {
			ParamsTable params = getParams();

			String areaid = params.getParameterAsString("areaid");
			
			Collection<Room> rooms = null; 
			rooms = ((RoomProcess)process).getRoomByArea(areaid);

			addActionResult(true, "", rooms);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String queryRoomById(){
		try {
			ParamsTable params = getParams();

			String id = params.getParameterAsString("id");
			
			Collection<Room> rooms = null; 
			rooms = ((RoomProcess)process).getRoomById(id);

			addActionResult(true, "", rooms);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public void doinitmanagement() throws Exception{
		ParamsTable params = getParams();
		String id = params.getParameterAsString("id");	//会议室id
		
		String msg="";
		PrintWriter pw = null;
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			
			Room room = ((RoomProcess)process).getRoomById(id).iterator().next();
			//根据id获取会议室记录，以JSON格式存入
			msg="{\"area\":\""+room.getAreaId()+"\",\"room\":\""+room.getName()+"\","
			+"\"number\":\""+room.getNumber()+"\",\"equipment\":\""+room.getEquipment()+"\","
			+"\"note\":\""+room.getNote()+"\",\"" +"areaname\":\""+room.getArea()+"\",\"id\":\""+room.getId()+"\"}";
		} catch (IOException e) {
			msg = e.getMessage();
			e.printStackTrace();
		}
		pw.write(msg.toString()); // 把内容放入response中
		pw.flush();
		pw.close();
	}
	
	public void doSaveRoom()
	{
		ParamsTable params = getParams();
		String name = params.getParameterAsString("name");//会议名称
		String area = params.getParameterAsString("area");//会议区域
		String areaid = params.getParameterAsString("areaid");
		String number = params.getParameterAsString("number");//容纳人数
		String equipment = params.getParameterAsString("equipment");//设备
		String note = params.getParameterAsString("note");//备注
		String id = params.getParameterAsString("id");//id
		
		String msg="";
		PrintWriter pw = null;
		//根据会议室x和时间段y获取已存在的（会议室预定）记录，保存到msg中
		try {
			
			WebUser user = getUser();
			Room room = new Room();
			room.setName(name);
			room.setArea(area);
			room.setCreator(user.getName());
			room.setEquipment(equipment);
			room.setNote(note);
			room.setNumber(number);
			room.setCreatorId(user.getId());
			room.setDomainid(user.getDomainid());
			room.setAreaId(areaid);
			if(id!=null&&!"undefined".equals(id))
				room.setId(id);
			boolean b = ((RoomProcess)process).save(room);
			
			if(!b)
				msg="success";
			else {
				msg = "null";
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();

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
	
	public void doDeleteRoom(){
		ParamsTable params = getParams();
		String str = params.getParameterAsString("str");//id
		
		String id[] = str.split(",");
		
		String msg="";
		PrintWriter pw = null;
		try {
			
			for(int i=0; i<id.length; i++)
			{
				((RoomProcess)process).delete(id[i]);
			}
			msg = "success";
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
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
