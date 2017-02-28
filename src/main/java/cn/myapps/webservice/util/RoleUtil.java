package cn.myapps.webservice.util;

import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.util.ObjectUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.webservice.WebServiceUtil;
import cn.myapps.webservice.fault.RoleServiceFault;
import cn.myapps.webservice.model.SimpleRole;

/**
 * RoleService工具类
 * @author ivan
 *
 */
public class RoleUtil {
	/**
	 * 把RoleVO转换为简单角色对象
	 * @param role 简单角色对象
	 * @param vo RoleVO
	 * @return 简单角色对象
	 * @throws Exception
	 */
	public static SimpleRole convertToSimple(SimpleRole role, RoleVO vo)throws Exception{
		try {
			ObjectUtil.copyProperties(role, vo);
			ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			ApplicationVO app = (ApplicationVO) applicationProcess.doView(vo.getApplicationid());
			if (app == null)
				throw new Exception("该应用(id=" + vo.getApplicationid() + ")不存在.");
			role.setApplicationName(app.getName());
			
			return role;
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}
	}
	
	/**
	 * 把简单角色对象转换为RoleVO对象
	 * @param vo RoleVO
	 * @param role 简单角色对象
	 * @return RoleVO
	 * @throws Exception
	 */
	public static RoleVO convertToVO(RoleVO vo, SimpleRole role)throws Exception{
		try {
			vo.setId(role.getId() != null ? role.getId() : vo.getId());
			vo.setName(role.getName() != null ? role.getName() : vo.getName());
			ApplicationVO application = WebServiceUtil.validateApplication(role
					.getApplicationName());
			vo.setApplicationid(application.getId());
			
			return vo;
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}
	}
}
