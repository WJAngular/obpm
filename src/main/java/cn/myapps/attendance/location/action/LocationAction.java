package cn.myapps.attendance.location.action;

import cn.myapps.attendance.base.action.BaseAction;
import cn.myapps.attendance.location.ejb.Location;
import cn.myapps.attendance.location.ejb.LocationProcessBean;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public class LocationAction extends BaseAction<Location> {

	private static final long serialVersionUID = -6071636025847192578L;
	
	
	public LocationAction() {
		super();
		content = new Location();
		process = new LocationProcessBean();
	}
	
	public String doNew(){
		return SUCCESS;
	}
	
	
	public String doSave(){
		try {
			WebUser user = getUser();
			Location location = (Location) getContent();
			location.setDomainid(user.getDomainid());
			if(StringUtil.isBlank(location.getId())){
				location = (Location) process.doCreate(location);
			}else{
				location = (Location) process.doUpdate(location);
			}
			
			addActionResult(true, "添加成功", location);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
		
	}
	
	public String doQuery() {
		try {
			DataPackage<Location> datas = process.doQuery(getParams(), getUser());
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
	
	public String doQuerySimple() {
		try {
			list = process.doSimpleQuery(getParams(), getUser());
		} catch (Exception e) {
			//addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	
	
	public String doDelete(){
		try {
			ParamsTable params = getParams();
			String ids = params.getParameterAsString("ids");
			if(!StringUtil.isBlank(ids)){
				process.doRemove(ids.split(";"));
				addActionResult(true, "删除成功", null);
			}else{
				addActionResult(false,"object not found!", null);
			}
			
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String doEdit(){
		try{
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			Location location = (Location) process.doView(id);
			addActionResult(true, "", location);
		}catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}

}
