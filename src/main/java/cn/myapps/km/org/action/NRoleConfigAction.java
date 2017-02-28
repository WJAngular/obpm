package cn.myapps.km.org.action;

import java.util.Collection;

import cn.myapps.core.department.ejb.DepartmentProcessBean;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.domain.ejb.DomainProcessBean;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.sysconfig.ejb.KmConfig;
import cn.myapps.core.user.ejb.UserProcessBean;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.disk.ejb.NDiskProcess;
import cn.myapps.km.disk.ejb.NDiskProcessBean;
import cn.myapps.km.org.ejb.NRole;
import cn.myapps.km.org.ejb.NRoleProcessBean;
import cn.myapps.km.org.ejb.NUserRoleSetProcess;
import cn.myapps.km.org.ejb.NUserRoleSetProcessBean;
import cn.myapps.util.property.PropertyUtil;

public class NRoleConfigAction extends  AbstractRunTimeAction<NRole> {

	private Collection<DomainVO> domains;
	private Collection<UserVO> users;
	private Collection<DepartmentVO> departments;
	
	public String doSaveConfig() {
		
		//级联创建KM个人网盘
		for (String userId : _selects) {
			try {
				UserVO user = (UserVO) new UserProcessBean().doView(userId);
				PropertyUtil.reload("km");
				if(PropertyUtil.get(KmConfig.ENABLE).equals("true")){

					NDiskProcess ndiskProcess = new NDiskProcessBean();
					ndiskProcess.createByUser(user.getId(), user.getDomainid());
					
					//保存KM角色
					NUserRoleSetProcess nUserRoleSetProcess = new NUserRoleSetProcessBean();
					nUserRoleSetProcess.doUpdateUserRoleSet(user.getId(), getParams().getParameterAsArray("_kmRoleSelectItem"));
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.addFieldError("1", e.getMessage());
				return ERROR;
			}
		}
		this.addActionMessage("{*[Save_Success]*}");
		return SUCCESS;
	}
	
	@SuppressWarnings("static-access")
	public String doConfigure() {
		try {
			DataPackage<NRole> datas = new DataPackage<NRole>();
			datas.setDatas(((NRoleProcessBean)getProcess()).doGetRoles());
			setDatas(datas);
			domains = new DomainProcessBean().getAllDomain();
			String domainId = getParams().getParameterAsString("domainId");
			cn.myapps.base.action.ParamsTable _params = new cn.myapps.base.action.ParamsTable().convertHTTP(this.getParams().getHttpRequest());
			_params.setParameter("sm_domainid", domainId);
			_params.setParameter("sm_dimission", 1);
			if (domainId != null) {
				users = new UserProcessBean().doSimpleQuery(_params);
				departments = new DepartmentProcessBean().queryByDomain(domainId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("1", "{*[core.sysconfig.km.role.config.error]*}");
			return INPUT;
			
		}
		return SUCCESS;
	}

	public NRunTimeProcess<NRole> getProcess() {
		return new NRoleProcessBean();
	}
	
	public Collection<DomainVO> getDomains() {
		return domains;
	}

	public Collection<UserVO> getUsers() {
		return users;
	}

	public void setUsers(Collection<UserVO> users) {
		this.users = users;
	}

	public Collection<DepartmentVO> getDepartments() {
		return departments;
	}

	public void setDepartments(Collection<DepartmentVO> departments) {
		this.departments = departments;
	}
	
	

}
