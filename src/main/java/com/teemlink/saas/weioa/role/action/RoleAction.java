package com.teemlink.saas.weioa.role.action;

import java.util.Iterator;









import java.util.List;





import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.role.action.RoleHelper;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.ProcessFactory;

import com.teemlink.saas.weioa.base.action.BaseAction;

public class RoleAction extends BaseAction {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7690498198992190934L;
	
	private String applicationId;
	
	private String roleId;
	
	private List<RoleVO> roleList = null;
	
	private List<UserVO> userList = null;
	

	public List<RoleVO> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleVO> roleList) {
		this.roleList = roleList;
	}

	public List<UserVO> getUserList() {
		return userList;
	}

	public void setUserList(List<UserVO> userList) {
		this.userList = userList;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return
	 */
	public String doList(){
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String domainId = user.getDomainid();
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
			
			RoleProcess process = (RoleProcess)ProcessFactory.createProcess(RoleProcess.class);
			UserProcess userProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			roleList = (List<RoleVO>) process.doQueryByHQL("from "+RoleVO.class.getName()+" r where r.applicationid='"+applicationId+"' and r.status=1 order by id desc", 1, Integer.MAX_VALUE);
			
			if(StringUtil.isBlank(roleId)){
				this.roleId =  roleList.get(0).getId();
			}
			params.setParameter("sm_domainid", domainId);
			params.setParameter("sm_userRoleSets.roleId", roleId);
			this.setDatas(userProcess.doQuery(params, user));
			
			return SUCCESS;
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}
	
	public String doUserListUnjoinedRole() {
		try {
			ParamsTable params = getParams();
			int lines = 20;
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);
			params.setParameter("sm_dimission", 1);
			if (!roleId.equals("")) {
				UserProcess userProcess = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				setDatas(userProcess.queryOutOfRoleByDomain(params, roleId, params.getParameterAsString("domain")));
			}
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}
	public String doAddUserToRole() {
		try {
			String[] selects = get_selects();
			if (selects != null && selects.length > 0) {
				UserProcess userProcess = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				userProcess.addUserToRole(selects, roleId);
			}
			addActionResult(true, "", "");
		}catch (Exception e) {
			addActionResult(false, "操作失败，服务器无法响应本次请求！", null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 移除应用
	 * @return
	 * @throws Exception
	 */
	public String doRemoveApp() throws Exception {
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String id = params.getParameterAsString("id");
			String domainId = user.getDomainid();
			DomainProcess dprocess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain = (DomainVO) dprocess.doView(domainId);
			ApplicationProcess process = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
			RoleHelper rh = new RoleHelper();
			for (Iterator<ApplicationVO> iterator = domain.getApplications().iterator(); iterator.hasNext();) {
				ApplicationVO vo = (ApplicationVO) iterator.next();
				if(id.equals(vo.getId())){
					rh.removeRole(domainId, id);
					process.removeDomainApplicationSet(domainId, id);
					break;
				}
			}
			PersistenceUtils.currentSession().clear();
			addActionResult(true, "", "");
		}catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	}
}
