package cn.myapps.webservice;

import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class WebServiceUtil {

	public static DomainVO validateDomain(String domainName) throws Exception {

		if (StringUtil.isBlank(domainName)) {
			throw new Exception("域名称不能为空.");
		} else {
			DomainProcess dp = (DomainProcess) ProcessFactory
					.createProcess(DomainProcess.class);
			DomainVO vo = (DomainVO) dp.getDomainByName(domainName);
			if (vo == null) {
				throw new Exception("该域(" + domainName + ")不存在.");
			}
			return vo;
		}

	}

	public static ApplicationVO validateApplication(String name)
			throws Exception {
		ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
				.createProcess(ApplicationProcess.class);
		ApplicationVO app = (ApplicationVO) applicationProcess
				.doViewByName(name);
		if (app == null)
			throw new Exception("该应用(" + name + ")不存在.");
		return app;
	}
	
	/**
	 * 校验应用，返回该应用对象
	 * @param applicationId
	 * @return
	 * @throws Exception
	 */
	public static ApplicationVO validateApplicationById(String applicationId)
			throws Exception {
		ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
				.createProcess(ApplicationProcess.class);
		ApplicationVO app = (ApplicationVO) applicationProcess
				.doView(applicationId);
		if (app == null)
			throw new Exception("该应用(ID=" + applicationId + ")不存在.");
		return app;
	}
	
	/**
	 * 校验用户，返回该用户对象
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static UserVO findUserWithValidate (String userId) 
			throws Exception {
		UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		UserVO user = (UserVO) userProcess.doView(userId);
		if(user == null){
			throw new Exception("该用户(ID=" + userId + ")不存在.");
		}
		return user;
	}
	
	/**
	 * 校验部门，返回该部门对象
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public static DepartmentVO findDepartmentWithValidate (String depId)
			throws Exception {
		DepartmentProcess process = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);
		DepartmentVO vo = (DepartmentVO) process.doView(depId);
		if (vo == null){
			throw new Exception("该部门(ID=" + depId + ")不存在.");
		}
		return vo;
	}
	
	/**
	 * 校验角色，返回该角色对象
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public static RoleVO findRoleWithValidate (String roleId)
			throws Exception {
		RoleProcess process = (RoleProcess) ProcessFactory
				.createProcess(RoleProcess.class);
		RoleVO vo = (RoleVO) process.doView(roleId);
		if (vo == null){
			throw new Exception("该角色(ID=" + roleId + ")不存在.");
		}
		return vo;
	}
}
