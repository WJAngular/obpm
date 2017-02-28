package com.teemlink.saas.weioa.org.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;

import cn.myapps.attendance.util.dataprovider.easyui.model.Department;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.util.StringUtil;
import cn.myapps.support.weixin.WeixinServiceProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

import com.teemlink.saas.weioa.base.action.BaseAction;

public class OrganizationAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4692448477758276122L;

	public String doList(){
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			
			String deptId = params.getParameterAsString("deptId");
			int lines = 10;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					lines = Integer.parseInt(cookie.getValue());
				}
			    cookie.getName();
			    cookie.getValue();
			}
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);
			
			DepartmentProcess deptProcess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			UserProcess userProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			
			params.setParameter("t_domainid", user.getDomainid());
			if(!StringUtil.isBlank(deptId)){
				params.setParameter("sm_userDepartmentSets.departmentId",deptId);
			}if(!StringUtil.isBlank(params.getParameterAsString("keyword"))){
				this.setDatas(userProcess.doQuerySmByHQL("from "+UserVO.class.getName()+"  where  (name LIKE'%"
						+params.getParameterAsString("keyword")+"%' or loginno LIKE'%"
						+params.getParameterAsString("keyword")+"%' or telephone LIKE'%"
						+params.getParameterAsString("keyword")+"%' or email LIKE'%"
						+params.getParameterAsString("keyword")+"%')", params, params.getParameterAsInteger("_currpage"), lines));
			}else{
			this.setDatas(userProcess.doQuery(params, user));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	public String getDepartmentTree(){
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
	
	public String doCreateDepartment() {
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			
			DepartmentVO dept = new DepartmentVO();
			dept.setId(Sequence.getSequence());
			dept.setDomainid(user.getDomainid());
			dept.setDomain((DomainVO) domainProcess.doView(user.getDomainid()));
			dept.setName(params.getParameterAsString("name"));
			DepartmentVO superior = (DepartmentVO) process.doView(params.getParameterAsString("parent"));
			dept.setSuperior(superior);
			dept.setLevel(superior.getLevel()+1);
			dept.setOrderByNo(params.getParameterAsInteger("position"));
			int weixinDeptId = WeixinServiceProxy.createDepartment(user.getDomainid(), dept.getName(), superior.getWeixinDeptId()!=null? Integer.valueOf(superior.getWeixinDeptId()) : 1);
			dept.setWeixinDeptId(String.valueOf(weixinDeptId));
			
			process.doCreate(dept);
			
			addActionResult(true, "", dept.getId());
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	
	public String doRenameDepartment() {
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
			
			DepartmentVO dept = (DepartmentVO) process.doView(params.getParameterAsString("id"));
			if(dept !=null){
				dept.setName(params.getParameterAsString("name"));
				if(!StringUtil.isBlank(dept.getWeixinDeptId())){
					DepartmentVO superior = dept.getSuperior();
					int pId = 0;
					if(superior !=null){
						pId = superior.getWeixinDeptId()!=null? Integer.valueOf(superior.getWeixinDeptId()) : 1;
					}
					WeixinServiceProxy.updateDepartment(user.getDomainid(),Integer.valueOf(dept.getWeixinDeptId()),dept.getName(), pId);
				}
				process.doUpdate(dept);
			}
			
			
			addActionResult(true, "", "");
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	
	public String doDeleteDepartment() {
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
			
			String id = params.getParameterAsString("id");
			
			if(!StringUtil.isBlank(id)){
				DepartmentVO dept = (DepartmentVO) process.doView(id);
				WeixinServiceProxy.deleteDepartment(user.getDomainid(), dept.getWeixinDeptId());
				process.doRemove(id);
			}
			addActionResult(true, "", "");
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 同步通讯录
	 * @return
	 */
	public String doSynch(){
		try {
			WebUser user = getUser();
			String domainId = user.getDomainid();
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain = (DomainVO) domainProcess.doView(domainId);
			domainProcess.synchFromWeixin(domain);
			domainProcess.synch2Weixin(domain);
			
		} catch (Exception e) {
			e.printStackTrace();
			this.addActionError(e.getMessage());
		}
		
		return SUCCESS;
	}

}
