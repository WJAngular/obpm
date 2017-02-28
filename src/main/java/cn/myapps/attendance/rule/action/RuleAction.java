package cn.myapps.attendance.rule.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.myapps.attendance.base.action.BaseAction;
import cn.myapps.attendance.location.ejb.Location;
import cn.myapps.attendance.rule.ejb.Rule;
import cn.myapps.attendance.rule.ejb.RuleProcess;
import cn.myapps.attendance.rule.ejb.RuleProcessBean;
import cn.myapps.attendance.util.dataprovider.easyui.model.Department;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class RuleAction extends BaseAction<Rule> {

	private static final long serialVersionUID = -6071636025847192578L;
	
	
	public RuleAction() {
		super();
		content = new Rule();
		process = new RuleProcessBean();
	}
	
	public String doNew(){
		return SUCCESS;
	}
	
	
	public String doSave(){
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			String[] _locations = params.getParameterAsArray("_locations");
			
			Rule rule = (Rule) getContent();
			rule.setDomainid(user.getDomainid());
			rule.setOrganizationType(Rule.TYPE_DEPT);
			
			if(_locations.length>0){
				List<Location> list = new ArrayList<Location>();
				for (int i = 0; i < _locations.length; i++) {
					String location = _locations[i];
					if(StringUtil.isBlank(location)) continue;
					Location l = new Location();
					l.setId(location);
					list.add(l);
				}
				rule.setLocations(list);
			}
			if(StringUtil.isBlank(rule.getId())){
				rule = (Rule) process.doCreate(rule);
			}else{
				rule = (Rule) process.doUpdate(rule);
			}
			
			addActionResult(true, "添加成功", rule);
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
			Rule rule = (Rule) process.doView(id);
			addActionResult(true, "", rule);
		}catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	
	public String doQuery() {
		try {
			
			DataPackage<Rule> datas = process.doQuery(getParams(), getUser());
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
	
	public String getDepartments(){
		List<Department> depts = new ArrayList<Department>();
		try {
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			Map<String,List<Department>> deptMap = new HashMap<String, List<Department>>();//key 部门id value 子部门集合
			List<DepartmentVO> departments = (List<DepartmentVO>) process.doQueryByHQL("from "+DepartmentVO.class.getName()+" vo where vo.domain.id='"+getUser().getDomainid()+"' order by vo.level desc", 1, Integer.MAX_VALUE);
			for (Iterator<DepartmentVO> iterator = departments.iterator(); iterator.hasNext();) {
				DepartmentVO departmentVO = iterator.next();
				Department d = new Department();
				d.setId(departmentVO.getId());
				d.setText(departmentVO.getName());
				DepartmentVO s = departmentVO.getSuperior();
				if(deptMap.get(d.getId()) !=null){
					d.setChildren(deptMap.get(d.getId()));
				}
				if(s !=null){
					List<Department> list = deptMap.get(s.getId());
					if(list == null) {
						list = new ArrayList<Department>();
						deptMap.put(s.getId(), list);
					}
					list.add(d);
				}else{
					depts.add(d);
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		list = depts;
		
		return SUCCESS;
		
	}
	
	public String getRule(){
		
		try {
			Rule rule = ((RuleProcess)process).getRuleByUser(getUser());
			if(rule == null) {
				throw new OBPMValidateException("管理员还没有设置您的考勤规则，请与系统管理员联系！");
			}
			addActionResult(true, "", rule);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
		
		
	}
		

}
