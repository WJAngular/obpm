package com.teemlink.saas.weioa.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.security.action.LoginHelper;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserRoleSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.support.weixin.WeixinServiceProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

public class WarpSecurityValid {

	private static final String DOMAIN_ID = "11e1-81e2-37f74759-9124-47aada6b7467";
	private static final String DEFAULT_APPLICATION_ID="11de-f053-df18d577-aeb6-19a7865cfdb6";
	private static final String COOKIE_NAME="weixin_user_cookie";

	public static void auth(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain) throws Exception {

		boolean hasCoolie = false;
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if(COOKIE_NAME.equals(cookie.getName())){
				String userId = cookie.getValue();
				UserProcess process = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				UserVO vo = (UserVO) process.doView(userId);
				if(vo!=null){
					DomainProcess domainProcess = (DomainProcess) ProcessFactory
							.createProcess(DomainProcess.class);
					DomainVO domain = (DomainVO) domainProcess.doView(DOMAIN_ID);
					LoginHelper.initWebUser(request, vo, DEFAULT_APPLICATION_ID, domain.getName());
					hasCoolie = true;
				}
			}
		}
		if(!hasCoolie){
			createUser(request, response);
		}

		chain.doFilter(request, response);
		return;
	}
	
	
	private static void createUser(HttpServletRequest request,
			HttpServletResponse response){
		UserVO vo = null;
		try {
			// PersistenceUtils.beginTransaction();
			UserProcess process = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			DomainProcess domainProcess = (DomainProcess) ProcessFactory
					.createProcess(DomainProcess.class);
			DomainVO domain = (DomainVO) domainProcess.doView(DOMAIN_ID);
			CalendarProcess calendarProcess = (CalendarProcess) ProcessFactory
					.createProcess(CalendarProcess.class);
			List<CalendarVO> calendars = (List<CalendarVO>) calendarProcess
					.doQueryByHQL(
							"from "
									+ CalendarVO.class.getName()
									+ " vo where vo.name='Standard_Calendar' and vo.domainid='"
									+ DOMAIN_ID + "'", 1, 1);
			CalendarVO calendar = calendars.isEmpty() ? null : calendars.get(0);

			DepartmentProcess deptProcess = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);
			List<DepartmentVO> dList = (List<DepartmentVO>) deptProcess
					.doQueryByHQL("from " + DepartmentVO.class.getName()
							+ " vo where vo.domain.id='" + DOMAIN_ID
							+ "' order by vo.level asc", 1, 1);
			DepartmentVO departmentVO = dList.get(0);

			// 创建系统用户
			vo = new UserVO();
			vo.setId(Sequence.getSequence());

			Collection<UserDepartmentSet> userDepartmentSets = new HashSet<UserDepartmentSet>();
			Collection<DepartmentVO> departments = new ArrayList<DepartmentVO>();
			UserDepartmentSet userDepartmentSet = new UserDepartmentSet(
					vo.getId(), departmentVO.getId());
			userDepartmentSets.add(userDepartmentSet);
			departments.add(departmentVO);

			// 设置角色
			RoleProcess roleProcess = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			for (Iterator<ApplicationVO> iterator = domain.getApplications()
					.iterator(); iterator.hasNext();) {
				ApplicationVO app = iterator.next();
				Collection<RoleVO> roleList = roleProcess
						.getRolesByApplication(app.getId());
				for (Iterator<RoleVO> iterator2 = roleList.iterator(); iterator2
						.hasNext();) {
					RoleVO roleVO = iterator2.next();
					UserRoleSet set = new UserRoleSet(vo.getId(),
							roleVO.getId());
					vo.getUserRoleSets().add(set);
				}
			}

			vo.setName(" ");
			vo.setLoginno(UUID.randomUUID().toString());
			vo.setLoginpwd("123456");
			vo.setTelephone("");
			vo.setDomainid(DOMAIN_ID);
			vo.setDefaultDepartment(departmentVO.getId());
			vo.setDepartments(departments);
			vo.setUserDepartmentSets(userDepartmentSets);
			vo.setCalendarType(calendar != null ? calendar.getId() : null);
			vo.setDimission(UserVO.ONJOB);

			 process.doCreate(vo);

			// 创建微信企业号用户
			// Integer[] dept = new Integer[]{1};//写死部门为顶级部门
			//
			// WeixinServiceProxy.createUser(domain.getId(), name, telephone,
			// telephone, "", dept);
			
			//转换WebUser对象
			LoginHelper.initWebUser(request, vo, DEFAULT_APPLICATION_ID, domain.getName());
			
			
			//创建永久cookie
			Cookie cookie = new Cookie(COOKIE_NAME, vo.getId());
			cookie.setMaxAge(Integer.MAX_VALUE);
			response.addCookie(cookie);
		} catch (Exception e) {
			try {
				if (vo != null) {
					 UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
					 process.doRemove(vo.getId());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

}
