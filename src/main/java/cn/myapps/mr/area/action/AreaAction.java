package cn.myapps.mr.area.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.area.ejb.Area;
import cn.myapps.mr.area.ejb.AreaProcess;
import cn.myapps.mr.area.ejb.AreaProcessBean;
import cn.myapps.mr.base.action.BaseAction;
import cn.myapps.mr.room.ejb.Room;
import cn.myapps.mr.room.ejb.RoomProcess;
import cn.myapps.mr.room.ejb.RoomProcessBean;

public class AreaAction extends BaseAction<Area> {

	public AreaAction() {
		super();
		content = new Area();
		process = new AreaProcessBean();
	}
	
	public String queryAllArea(){
		try {
			ParamsTable params = getParams();
			
			Collection<Area> areas = null; 
			areas = ((AreaProcess)process).getAllAreas();

			addActionResult(true, "", areas);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public void newArea(){
		ParamsTable params = getParams();
		String name = params.getParameterAsString("name");
		String msg="";
		PrintWriter pw = null;
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			
			WebUser user = getUser();
			Area area = new Area();
			area.setName(name);
			area.setCreatorId(user.getId());
			area.setCreator(user.getName());
			area.setDomainid(user.getDomainid());

			((AreaProcess)process).create(area);
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
	
	public void updateArea(){
		ParamsTable params = getParams();
		String id = params.getParameterAsString("id");//
		String name = params.getParameterAsString("name");
		
		String msg="";
		PrintWriter pw = null;
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			
			WebUser user = getUser();
			Area area =new Area();
			area.setId(id);
			area.setName(name);
			((AreaProcess)process).update(area);
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
	
	public void deleteArea(){
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
			((AreaProcess)process).delete(id);
			
			//删除该区域下所有会议室
			RoomProcess roomProcess = new RoomProcessBean();
			Collection<Room> rooms= roomProcess.getRoomByArea(id);
			
			for(Iterator it=rooms.iterator();it.hasNext();){
				Room r= (Room)it.next();
				roomProcess.delete(r.getId());
			}
			
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
