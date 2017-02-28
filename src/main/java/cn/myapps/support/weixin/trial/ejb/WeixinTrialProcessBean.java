package cn.myapps.support.weixin.trial.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserRoleSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.support.weixin.WeixinServiceProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

public class WeixinTrialProcessBean implements WeixinTrialProcess {

	public WeixinTrialProcessBean() {
		// TODO Auto-generated constructor stub
	}

	public UserVO doRegister(String domainid, String name, String telephone)
			throws Exception {
		
		UserVO vo = null;
		try {
			//PersistenceUtils.beginTransaction();
			// 1.检查手机号是否已注册，已注册直接返回成功页面，否则创建系统用户、并同步在企业号平台创建用户
			UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain = (DomainVO) domainProcess.doView(domainid);
			Collection<UserVO> uList = process.doQueryByHQL("from "+UserVO.class.getName()+" u where u.telephone='"+telephone+"' and u.domainid='"+domainid+"'", 1, 1);
			if(uList.isEmpty()){
				CalendarProcess calendarProcess = (CalendarProcess)ProcessFactory.createProcess(CalendarProcess.class);
				List<CalendarVO> calendars = (List<CalendarVO>) calendarProcess.doQueryByHQL("from "+CalendarVO.class.getName()+" vo where vo.name='Standard_Calendar' and vo.domainid='"+domainid+"'", 1, 1);
				CalendarVO calendar = calendars.isEmpty()? null : calendars.get(0);
				
				DepartmentProcess deptProcess = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
				List<DepartmentVO> dList = (List<DepartmentVO>) deptProcess.doQueryByHQL("from "+DepartmentVO.class.getName()+" vo where vo.domain.id='"+domainid+"' order by vo.level asc", 1, 1);
				DepartmentVO departmentVO = dList.get(0);
				
				//创建系统用户
				vo = new UserVO();
				vo.setId(Sequence.getSequence());
				
				Collection<UserDepartmentSet> userDepartmentSets = new HashSet<UserDepartmentSet>();
				Collection<DepartmentVO> departments = new ArrayList<DepartmentVO>();
				UserDepartmentSet userDepartmentSet = new UserDepartmentSet(vo.getId(), departmentVO.getId());
				userDepartmentSets.add(userDepartmentSet);
				departments.add(departmentVO);
				
				//设置角色
				RoleProcess roleProcess = (RoleProcess) ProcessFactory.createProcess(RoleProcess.class);
				for (Iterator<ApplicationVO> iterator = domain.getApplications().iterator(); iterator
						.hasNext();) {
					ApplicationVO app = iterator.next();
					Collection<RoleVO> roleList = roleProcess.getRolesByApplication(app.getId());
					for (Iterator<RoleVO> iterator2 = roleList.iterator(); iterator2
							.hasNext();) {
						RoleVO roleVO = iterator2.next();
						UserRoleSet set = new UserRoleSet(vo.getId(), roleVO
								.getId());
						vo.getUserRoleSets().add(set);
					}
				}
				
				vo.setName(name);
				vo.setLoginno(telephone);
				vo.setLoginpwd(telephone);
				vo.setTelephone(telephone);
				vo.setDomainid(domainid);
				vo.setDefaultDepartment(departmentVO.getId());
				vo.setDepartments(departments);
				vo.setUserDepartmentSets(userDepartmentSets);
				vo.setCalendarType(calendar !=null? calendar.getId() : null);
				vo.setDimission(UserVO.ONJOB);
				
				process.doCreate(vo);
				
				//创建微信企业号用户
				Integer[] dept = new Integer[]{1};//写死部门为顶级部门
				
				WeixinServiceProxy.createUser(domain.getId(), name, telephone, telephone, "", dept);
				
			}
			
			//PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			try {
				//PersistenceUtils.rollbackTransaction();
				if(vo !=null){
					UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
					process.doRemove(vo.getId());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				throw e1;
			}
			e.printStackTrace();
			throw e;
		}
		return vo;
	}

	public void clearDeptUsers(String domainid, String deptid) throws Exception {
		try {
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain = (DomainVO) domainProcess.doView(domainid);
			WeixinServiceProxy.clearDeptUsers(domain.getWeixinCorpID(), domain.getWeixinCorpSecret(), deptid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		try {
			new WeixinTrialProcessBean().clearDeptUsers("11e1-81e2-37f74759-9124-47aada6b7467", "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
