package cn.myapps.webservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.email.email.action.EmailUserHelper;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.sysconfig.ejb.LoginConfig;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserRoleSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.property.PropertyUtil;
import cn.myapps.util.xml.XmlUtil;
import cn.myapps.webservice.fault.UserServiceFault;
import cn.myapps.webservice.model.SimpleAdmin;
import cn.myapps.webservice.model.SimpleUser;
import cn.myapps.webservice.util.UserUtil;

/**
 * 提供用户增删改查和常用操作功能接口
 * @author Administrator
 *
 */
public class UserService {
	private final static Logger LOG = Logger.getLogger(UserService.class);

	public final static String STANDARD_CALENDAR_NAME = "Standard_Calendar";

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
	 * @return 如果合法返回true,否则返回false
	 * @throws UserServiceFault
	 *             用户服务异常
	 */
	public SimpleUser validateUser(String domainName, String userAccount,
			String userPassword, int userType) throws UserServiceFault {

		SimpleUser simpleUser =  null;
		try {
			BaseUser user = null;
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			SuperUserProcess sUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);

			switch (userType) {
			case SimpleUser.USER_TYPE_DOMAINUSER:
				user = userProcess.login(userAccount, userPassword, domainName,0);
				break;
			case SimpleAdmin.USER_TYPE_DEVELOPER:
			case SimpleAdmin.USER_TYPE_DOMAINADMIN:
			case SimpleAdmin.USER_TYPE_SUPERADMIN:
				user = sUserProcess.login(userAccount, userPassword);
				break;
			default:
				throw new UserServiceFault("Invaild.user.type");
			}

			if(user !=null){
				simpleUser = UserUtil.convertToSimple(user);
				simpleUser.setLoginpwd(userPassword); // 设置明文密码
				simpleUser.setDomainName(domainName); // 设置登录的域名称
			}

			return simpleUser;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return simpleUser;
	}

	/**
	 * 改变用户密码
	 * 
	 * @param id
	 *            用户ID
	 * @param password
	 *            用户密码
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int changePassword(String id, String password)
			throws UserServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(id);
			if(user == null){
				throw new Exception("该用户(ID=" + id + ")不存在.");
			}
			user.setLoginpwd(password);

			userProcess.doUpdate(user);
			
			result = 0;
		} catch (Exception e) {
			LOG.error("Change Password", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 改变管理员密码
	 * 
	 * @param id
	 *            管理员ID
	 * @param password
	 *            新密码
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int changeAdminPassword(String id, String password)
			throws UserServiceFault {
		int result = -1;
		try {
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			SuperUserVO admin = (SuperUserVO) superUserProcess.doView(id);
			admin.setLoginpwd(password);

			superUserProcess.doUpdate(admin);
			
			result = 0;
		} catch (Exception e) {
			LOG.error("Change Admin Password", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;

	}

	/**
	 * 创建超级管理员
	 * 
	 * @param admin
	 *            管理员值对象
	 * @return -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int createAdmin(SimpleAdmin admin) throws UserServiceFault {
		int result = -1;
		try {
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);

			if (validateAdminParameter(admin))
				throw new NullPointerException("对象或对象属性存在空值!");

			SuperUserVO vo = new SuperUserVO();
			UserUtil.convertToVO(vo, admin);

			try {
				superUserProcess.doCreate(vo);
			} catch (Exception e) {
				throw new Exception("该账号" + admin.getLoginno() + "已存在.");
			}
			result = 0;
		} catch (Exception e) {
			LOG.error("Create Admin Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 更新超级管理员信息
	 * 
	 * @param admin SimpleAdmin对象
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int updateAdmin(SimpleAdmin admin) throws UserServiceFault {
		int result = -1;
		try {
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			if (admin == null || StringUtil.isBlank(admin.getId()))
				throw new NullPointerException("对象为空或对象的ID为空.");

//			if (validateAdminParameter(admin))
//				throw new NullPointerException("对象或对象属性存在空值!");

			SuperUserVO vo = (SuperUserVO) superUserProcess.doView(admin
					.getId());
			if (vo == null)
				throw new Exception("数据库不存在该ID" + admin.getId() + "对象.");

			UserUtil.convertToVO(vo, admin);
			if(!vo.getLoginno().equals(admin.getLoginno())){
				SuperUserVO temp = (SuperUserVO) superUserProcess.doViewByLoginno(admin.getLoginno());
				if(temp != null){
					throw new Exception("该管理员帐号" + admin.getLoginno() + "已存在！");
				}
			}
			
			superUserProcess.doUpdate(vo);
			
			result = 0;
		} catch (Exception e) {
			LOG.error("Update Admin Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 获取管理员
	 * 
	 * @param id 用户(管理员)id
	 * @return SimpleAdmin对象
	 * @throws UserServiceFault
	 */
	public SimpleAdmin getAdmin(String id) throws UserServiceFault {
		try {
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			if (id == null)
				throw new NullPointerException("主键为空!");

			SuperUserVO vo = (SuperUserVO) superUserProcess.doView(id);
			if (vo != null) {
				return (SimpleAdmin) UserUtil.convertToSimple(vo, SimpleAdmin.class);
			}
		} catch (Exception e) {
			LOG.error("Get Admin Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}

		return null;
	}

	/**
	 * 删除超级管理员
	 * 
	 * @param id
	 *            管理员ID
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int deleteAdmin(String id) throws UserServiceFault {
		int result = -1;
		try {
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			if (id == null)
				throw new NullPointerException("主键为空!");

			superUserProcess.doRemove(id);
			
			result = 0;
		} catch (Exception e) {
			LOG.error("Delete Admin Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 创建用户
	 * 
	 * @param user
	 *            -简单用户
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 *             -WebService异常
	 */
	public int createUser(SimpleUser user) throws UserServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);

			if (validateUserParameter(user))
				throw new NullPointerException("对象或对象属性存在空值!");
			WebServiceUtil.validateDomain(user.getDomainName());

			UserVO vo = new UserVO();
			UserUtil.convertToVO(vo, user);
			
			try {
				// 关联或创建邮件用户
				HttpServletRequest request = ServletActionContext.getRequest();
				EmailUserHelper.checkAndCreateEmailUser(vo, request);
				
				userProcess.doCreate(vo);
			} catch (Exception e) {
				throw new Exception("该账号" + user.getLoginno() + "已存在.");
			}
			result = 0;
		} catch (Exception e) {
			LOG.error("Create User Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 更新用户信息
	 * 
	 * @param user SimpleUser对象
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int updateUser(SimpleUser user) throws UserServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			if (user == null || StringUtil.isBlank(user.getId()))
				throw new NullPointerException("对象为空或对象的ID为空.");
//			if (validateUserParameter(user))
//				throw new NullPointerException("对象或对象属性存在空值!");
			DomainVO domain = WebServiceUtil.validateDomain(user.getDomainName());

			UserVO vo = (UserVO) userProcess.doView(user.getId());

			if (vo == null)
				throw new Exception("数据库不存在该ID" + user.getId() + "对象.");

			UserUtil.convertToVO(vo, user);
			if (StringUtil.isBlank(user.getLoginpwd())) {
				vo.setLoginpwd(Web.DEFAULT_SHOWPASSWORD);
			}
			
			if(!vo.getLoginno().equals(user.getLoginno())){
				UserVO temp = (UserVO) userProcess.getUserByLoginno(user.getLoginno(), domain.getId());
				if(temp != null){
					throw new Exception("该用户帐号" + user.getLoginno() + "已存在！");
				}
			}
			
//			userProcess.doUpdate(vo);
			
			result = 0;
		} catch (Exception e) {
			LOG.error("Update User Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 根据主键查找用户
	 * 
	 * @param pk
	 *            -主键
	 * @return 用户
	 * @throws UserServiceFault
	 */
	public SimpleUser getUser(String pk) throws UserServiceFault {
		SimpleUser user = null;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			if (pk == null)
				throw new NullPointerException("主键为空!");

			UserVO vo = (UserVO) userProcess.doView(pk);
			if (vo != null) {
				user = UserUtil.convertToSimple(vo);
			}
		} catch (Exception e) {
			LOG.error("Get User Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return user;
	}

	/**
	 * 根据主键删除用户
	 * 
	 * @param pk
	 *            -主键
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int deleteUser(String pk) throws UserServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			if (pk == null)
				throw new NullPointerException("主键为空!");

			userProcess.doRemove(pk);
			
			result = 0;
		} catch (Exception e) {
			LOG.error("Delete User Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 根据主键删除用户
	 * @param pks 
	 * 			主键数组
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int deleteUser(String[] pks) throws UserServiceFault {
		int result = -1;
		try {
			if(pks != null){
				for (int i = 0; i < pks.length; i++) {
					this.deleteUser(pks[i]);
				}
			}
			result = 0;
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}
		return result;
	}

	/**
	 * 根据用户对象删除用户
	 * 
	 * @param user
	 *            -用户
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int deleteUser(SimpleUser user) throws UserServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			if (user == null || StringUtil.isBlank(user.getId()))
				throw new NullPointerException("对象为空或对象的ID为空.");
			WebServiceUtil.validateDomain(user.getDomainName());
			UserVO vo = (UserVO) userProcess.doView(user.getId());
			if(vo != null)
				userProcess.doRemove(vo);
			
			result = 0;
		} catch (Exception e) {
			LOG.error("Delete User Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 验证用户属性值
	 * 
	 * @param user
	 * @return
	 */
	private boolean validateUserParameter(SimpleUser user) {
		if (user == null || StringUtil.isBlank(user.getName())
				|| StringUtil.isBlank(user.getLoginno())
				|| StringUtil.isBlank(user.getLoginpwd())) {
			return true;
		}
		return false;
	}

	/**
	 * 验证管理员属性值
	 * 
	 * @param user
	 * @return
	 */
	private boolean validateAdminParameter(SimpleAdmin admin) {
		if (admin == null || StringUtil.isBlank(admin.getName())
				|| StringUtil.isBlank(admin.getLoginno())
				|| StringUtil.isBlank(admin.getLoginpwd())) {
			return true;
		}
		return false;
	}

	/**
	 * 设置用户所属的角色集合
	 * 
	 * @param userId
	 *            -用户id
	 * @param roles
	 *            -角色ID组
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int setRoleSet(String userId, String[] roles)
			throws UserServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			UserVO vo = (UserVO) userProcess.doView(userId);
			if (vo == null)
				throw new NullPointerException("对象为空或对象的ID为空!");
			DomainVO domain = vo.getDomain();

			Collection<UserRoleSet> coll = new HashSet<UserRoleSet>();
			if (roles != null) {
				for (int i = 0; i < roles.length; i++) {
					RoleProcess rp = (RoleProcess) ProcessFactory
							.createProcess(RoleProcess.class);
					RoleVO role = (RoleVO) rp.doView(roles[i]);
					boolean flag = true;
					if (role != null) {
						Iterator<?> it = domain.getApplications().iterator();
						while (it.hasNext()) {
							ApplicationVO temp = (ApplicationVO) it.next();
							if (temp.getId().equals(role.getApplicationid())) {
								UserRoleSet set = new UserRoleSet(vo.getId(),
										role.getId());
								coll.add(set);
								flag = false;
								break;
							}
						}
					}
					if (flag) {
						throw new Exception("该角色" + roles[i] + "对应的应用"
								+ role.getApplicationid() + "还没应用到该域"
								+ domain.getName());
					}
				}
			}
			vo.setUserRoleSets(coll);
			userProcess.doUpdate(vo);
			
			result = 0;
		} catch (Exception e) {
			LOG.error("Set RoleSet Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 设置用户所属的部门集合
	 * 
	 * @param userId
	 *            -用户id
	 * @param deps
	 *            -部门ID组
	 * @param defaultDepartmentId
	 *            -默认部门ID
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int setDepartmentSet(String userId, String[] deps, String defaultDepartmentId)
			throws UserServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			DomainProcess domainProcess = (DomainProcess) ProcessFactory
					.createProcess(DomainProcess.class);

			UserVO vo = (UserVO) userProcess.doView(userId);
			if (vo == null)
				throw new NullPointerException("用户对象为空或用户对象的ID为空!");
			DomainVO userDomain = vo.getDomain();
			
			Collection<UserDepartmentSet> coll = new HashSet<UserDepartmentSet>();
			if (deps != null) {
				DepartmentProcess da = (DepartmentProcess) ProcessFactory
						.createProcess(DepartmentProcess.class);

				for (int i = 0; i < deps.length; i++) {
					DepartmentVO dpt = (DepartmentVO) da.doView(deps[i]);
					if (dpt == null) {
						continue;
					}

					DomainVO deptDomain = (DomainVO) domainProcess.doView(dpt
							.getDomain().getId());
					if (deptDomain != null
							&& deptDomain.getName()
									.equals(userDomain.getName())) {
						UserDepartmentSet set = new UserDepartmentSet(vo
								.getId(), dpt.getId());
						coll.add(set);
					} else {
						throw new Exception("部门" + deps[i] + "不存在或部门与用户不在同一个域.");
					}
				}
			}
			vo.setUserDepartmentSets(coll);
			userProcess.doUpdate(vo);
			
			setDefaultDepartment(userId, defaultDepartmentId);
			
			result = 0;
		} catch (Exception e) {
			LOG.error("Set DepartmentSet Error", e);
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 设置用户的默认部门
	 * @param userId 用户id
	 * @param depId 部门id
	 * @return -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int setDefaultDepartment(String userId, String depId) throws UserServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
			UserVO user = WebServiceUtil.findUserWithValidate(userId);
			DepartmentVO department = WebServiceUtil.findDepartmentWithValidate(depId);
			
			if(!user.getDomainid().equals(department.getDomainid())){
				throw new UserServiceFault("用户["+user.getName()+"]与部门["+department.getName()+"]不在同一个企业域.");
			}
			
			Collection<DepartmentVO> depts = user.getDepartments();
			if(!depts.contains(department)){
				throw new UserServiceFault("用户["+user.getName()+"]不属于部门["+department.getName()+"],无法设置其为默认部门.");
			}
			
			user.setDefaultDepartment(department.getId());
			userProcess.doUpdate(user);
			result = 0;
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}
		return result;
	}

	/**
	 * 传入Json格式字符串创建一个用户
	 * @param jsonStr
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int createUserFromJson (String jsonStr)throws UserServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(jsonStr))
				throw new UserServiceFault("Parameter 'jsonStr' can not be null.");
			SimpleUser simpleUser = (SimpleUser) JsonUtil.toBean(jsonStr, SimpleUser.class);
			result = createUser(simpleUser);
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入XML格式字符串创建一个用户
	 * @param xmlStr
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int createUserFromXML (String xmlStr)throws UserServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(xmlStr))
				throw new UserServiceFault("Parameter 'xmlStr' can not be null.");
			SimpleUser simpleUser = (SimpleUser) XmlUtil.toOjbect(xmlStr);
			result = createUser(simpleUser);
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入Json格式字符串更新一个用户
	 * @param jsonStr
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int updateUserFromJson (String jsonStr)throws UserServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(jsonStr))
				throw new UserServiceFault("Parameter 'jsonStr' can not be null.");
			SimpleUser simpleUser = (SimpleUser) JsonUtil.toBean(jsonStr, SimpleUser.class);
			result = updateUser(simpleUser);
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入XML格式字符串更新一个用户
	 * @param xmlStr
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int updateUserFromXML (String xmlStr)throws UserServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(xmlStr))
				throw new UserServiceFault("Parameter 'xmlStr' can not be null.");
			SimpleUser simpleUser = (SimpleUser) XmlUtil.toOjbect(xmlStr);
			result = updateUser(simpleUser);
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入主键返回用户对象格式化成JSON的字符串
	 * @param pk 用户主键
	 * @return JSON字符串
	 * @throws UserServiceFault
	 */
	public String findUserFormat2Json(String pk) throws UserServiceFault {
		SimpleUser simpleUser = getUser(pk);
		return JsonUtil.toJson(simpleUser);
	}
	
	/**
	 * 传入主键返回用户对象格式化成XML的字符串
	 * @param pk 用户主键
	 * @return XML字符串
	 * @throws UserServiceFault
	 */
	public String findUserFormat2XML(String pk) throws UserServiceFault {
		SimpleUser simpleUser = getUser(pk);
		return XmlUtil.toXml(simpleUser);
	}
	
	/**
	 * 传入部门Id查询用户的集合
	 * @param depId 部门id
	 * @return SimpleUser对象集合
	 * @throws UserServiceFault
	 */
	public Collection<SimpleUser> getUsersByDepId(String depId) throws UserServiceFault {
		Collection<SimpleUser> simpleUser = new ArrayList<SimpleUser>();
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
			Collection<UserVO> vos = userProcess.queryByDepartment(depId);
			if(vos != null)
				for (Iterator<UserVO> it = vos.iterator(); it.hasNext();) {
					SimpleUser sUser = UserUtil.convertToSimple(it.next());
					simpleUser.add(sUser);
				}
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return simpleUser;
	}
	
	/**
	 * 传入角色Id查询用户的集合
	 * @param roleId 角色id
	 * @return SimpleUser对象集合
	 * @throws UserServiceFault
	 */
	public Collection<SimpleUser> getUsersByRoleId(String roleId) throws UserServiceFault {
		Collection<SimpleUser> simpleUser = new ArrayList<SimpleUser>();
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
			Collection<UserVO> vos = userProcess.queryByRole(roleId);
			if(vos != null)
				for (Iterator<UserVO> it = vos.iterator(); it.hasNext();) {
					SimpleUser sUser = UserUtil.convertToSimple(it.next());
					simpleUser.add(sUser);
				}
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return simpleUser;
	}
	
	/**
	 * 为指定的用户添加一个角色
	 * @param userId 用户id
	 * @param roleId 角色id
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int addRole(String userId,String roleId) throws UserServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
			WebServiceUtil.findUserWithValidate(userId);
			WebServiceUtil.findRoleWithValidate(roleId);
			String [] array = new String[1];
			array[0] = userId;
			userProcess.addUserToRole(array, roleId);
			
			result = 0;
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 为指定的用户移除一个角色
	 * @param userId 用户id
	 * @param roleId 角色id
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int removeRole(String userId,String roleId) throws UserServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(roleId)){
				throw new UserServiceFault("角色id不能为空");
			}
			
			UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
			
			UserVO user = (UserVO) userProcess.doView(userId);
			if(user == null){
				throw new UserServiceFault("该用户(ID=" + userId + ")不存在.");
			}
			Collection<UserRoleSet> oldroles = user.getUserRoleSets();
			Collection<UserRoleSet> roleSets = new HashSet<UserRoleSet>();

			for (Iterator<UserRoleSet> it = oldroles.iterator(); oldroles != null
					&& it.hasNext();) {
				UserRoleSet set = (UserRoleSet) it.next();
				if (!roleId.equals(set.getRoleId()))
					roleSets.add(set);
			}
			user.setUserRoleSets(roleSets);
			userProcess.doUpdate(user);
			
			result = 0;
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 为指定的用户添加一个部门
	 * @param userId 用户id
	 * @param depId 部门id
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int addDepartment(String userId,String depId) throws UserServiceFault {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
			WebServiceUtil.findUserWithValidate(userId);
			WebServiceUtil.findDepartmentWithValidate(depId);
			String [] array = new String[1];
			array[0] = userId;
			userProcess.addUserToDept(array, depId);
			
			result = 0;
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 为指定的用户移除一个部门
	 * @param userId 用户id
	 * @param depId 部门id
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int removeDepartment(String userId,String depId) throws UserServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(depId)){
				throw new UserServiceFault("部门id不能为空");
			}
			
			UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
			
			UserVO user = (UserVO) userProcess.doView(userId);
			if(user == null){
				throw new UserServiceFault("该用户(ID=" + userId + ")不存在.");
			}
			Collection<UserDepartmentSet> userDepartmentSets = user
					.getUserDepartmentSets();
			Collection<UserDepartmentSet> newSets = new HashSet<UserDepartmentSet>();

			// 删除UserDepartmentSet
			for (Iterator<UserDepartmentSet> iterator = userDepartmentSets
					.iterator(); iterator.hasNext();) {
				UserDepartmentSet set = (UserDepartmentSet) iterator.next();
				if (!depId.equals(set.getDepartmentId())) {
					newSets.add(set);
				}
			}

			user.setUserDepartmentSets(newSets);
			userProcess.doUpdate(user);
			
			result = 0;
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new UserServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 传入所有属性值创建一个用户
	 * @param id 用户id
	 * @param name 用户名称
	 * @param loginno 帐号
	 * @param loginpwd 密码
	 * @param email 邮箱
	 * @param telephone 电话
	 * @param domainName 企业域名称
	 * @param defaultDepartmentName 默认部门名称
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int createUser (String id, String name, String loginno, String loginpwd, String email, String telephone, String domainName, String defaultDepartmentName) 
			throws UserServiceFault {
		int result = -1;
		try {
			SimpleUser simpleUser = new SimpleUser();
			simpleUser.setId(id);
			simpleUser.setName(name);
			simpleUser.setLoginno(loginno);
			simpleUser.setLoginpwd(loginpwd);
			simpleUser.setEmail(email);
			simpleUser.setTelephone(telephone);
			simpleUser.setDomainName(domainName);
			simpleUser.setDefaultDepartmentName(defaultDepartmentName);
			result = createUser(simpleUser);
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入键值对更新一个用户
	 * 
	 * @param attributes
	 *            key为属性名,value为属性值(如：id=11de-96ac-d3a71f76-979f-7d180a5b557b)
	 * @return  -1:失败 ,0:成功
	 * @throws UserServiceFault
	 */
	public int updateUser (Map<String,String> attributes) throws UserServiceFault {
		int result = -1;
		try {
			if(attributes == null)
				throw new UserServiceFault("用户Map不能为空.");
			SimpleUser simpleUser = new SimpleUser();
			simpleUser.setId(attributes.get("id"));
			simpleUser.setName(attributes.get("name"));
			simpleUser.setLoginno(attributes.get("loginno"));
			simpleUser.setLoginpwd(attributes.get("loginpwd"));
			simpleUser.setEmail(attributes.get("email"));
			simpleUser.setTelephone(attributes.get("telephone"));
			simpleUser.setDomainName(attributes.get("domainName"));
			simpleUser.setDefaultDepartmentName(attributes.get("defaultDepartmentName"));
			result = updateUser(simpleUser);
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}
		return result;
	}
	
	public static void main(String[] args){
		try {
			/*
			UserService us = new UserService();
			SimpleAdmin admin = new SimpleAdmin();
			admin.setId(null);
			admin.setName("domainAdmin");
			admin.setLoginno("domainAdmin");
			admin.setLoginpwd("teemlink");
			admin.setUserType(new int[]{1,2,3});
			us.createAdmin(admin);
			admin = us.getAdmin("11e2-1993-2bcfc6d1-a7bd-adf744a0db03");
			System.out.println(admin.getName());
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
