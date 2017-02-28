package cn.myapps.webservice;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.webservice.fault.SecurityServiceFault;
import cn.myapps.webservice.model.SimpleUser;

/**
 * 提供用户校验、修改密码、激活、禁用的功能接口
 * @author Ivan
 *
 */
public class SecurityService {
	
	/**
	 * 检查用户是否合法
	 * 
	 * @param domainName
	 *            域名称
	 * @param userAccount
	 *            用户账号
	 * @param userPassword
	 *            用户密码
	 * @param userType
	 *            用户类型
	 * @return 如果合法返回SimpleUser对象,否则抛异常
	 * @throws SecurityServiceFault
	 */
	public SimpleUser validateUser(String domainName, String userAccount,
			String userPassword, int userType) throws SecurityServiceFault {
		try {
			UserService userService = new UserService();
			return userService.validateUser(domainName, userAccount, userPassword, userType);
		} catch (Exception e) {
			throw new SecurityServiceFault(e.getMessage());
		}
	}
	
	/**
	 * 修改用户密码
	 * 
	 * @param domainName
	 *            域名称
	 * @param userAccount
	 *            用户账号
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 * @return  -1:失败 ,0:成功
	 * @throws SecurityServiceFault
	 */
	public int doChangePassword(String domainName, String userAccount,
			String oldPassword, String newPassword) throws SecurityServiceFault {
		int result = -1;
		try {
			SimpleUser sUser = validateUser(domainName, userAccount, oldPassword, 0);
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			userProcess.changePwd(sUser.getId(), oldPassword, newPassword);
			
			result = 0;
		} catch (Exception e) {
			throw new SecurityServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new SecurityServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 激活用户
	 * 
	 * @param adminAccount
	 *            管理员账号
	 * @param adminPw
	 *            管理员密码
	 * @param userId
	 *            用户id
	 * @return  -1:失败 ,0:成功
	 * @throws SecurityServiceFault
	 */
	public int activateUser(String adminAccount,String adminPw,String userId)
			throws SecurityServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			SuperUserProcess sUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			SuperUserVO vo = sUserProcess.login(adminAccount, adminPw);
			if(vo != null){
				//激活普通用户
				UserVO user = (UserVO) userProcess.doView(userId);
				if(user != null){
					user.setStatus(1);
					userProcess.doUpdate(user);
					result = 0;
				}
				//激活特权用户(超级管理员、企业与管理员、开发者)
				SuperUserVO superuser = (SuperUserVO) sUserProcess.doView(userId);
				if(superuser != null){
					superuser.setStatus(1);
					sUserProcess.doUpdate(superuser);
					result = 0;
				}
			}
		} catch (Exception e) {
			if(e.getMessage().indexOf("core.superuser.noeffectived") > 0)
				throw new SecurityServiceFault(adminAccount + ",您没有权限激活用户,原因：" + adminAccount + "未激活.");
			throw new SecurityServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new SecurityServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 禁用用户
	 * 
	 * @param adminAccount
	 *            管理员账号
	 * @param adminPw
	 *            管理员密码
	 * @param userId
	 *            用户id
	 * @return  -1:失败 ,0:成功
	 * @throws SecurityServiceFault
	 */
	public int inactiveUser(String adminAccount,String adminPw,String userId)
			throws SecurityServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			SuperUserProcess sUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			SuperUserVO vo = sUserProcess.login(adminAccount, adminPw);
			if(vo != null){
				//禁用普通用户
				UserVO user = (UserVO) userProcess.doView(userId);
				if(user != null){
					user.setStatus(0);
					userProcess.doUpdate(user);
					result = 0;
				}
				//禁用特权用户(超级管理员、企业与管理员、开发者)
				SuperUserVO superuser = (SuperUserVO) sUserProcess.doView(userId);
				if(superuser != null){
					superuser.setStatus(0);
					sUserProcess.doUpdate(superuser);
					result = 0;
				}
			}
		} catch (Exception e) {
			if(e.getMessage().indexOf("core.superuser.noeffectived") > 0)
				throw new SecurityServiceFault(adminAccount + ",您没有权限禁用用户,原因：" + adminAccount + "未激活.");
			throw new SecurityServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new SecurityServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
}
