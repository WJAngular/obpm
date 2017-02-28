package cn.myapps.webservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserRoleSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.xml.XmlUtil;
import cn.myapps.webservice.fault.RoleServiceFault;
import cn.myapps.webservice.fault.UserServiceFault;
import cn.myapps.webservice.model.SimpleRole;
import cn.myapps.webservice.util.RoleUtil;

/**
 * 提供角色增删改和常用操作查功能接口
 * @author Administrator
 *
 */
public class RoleService {
	
	/**
	 * 传入一个SimpleRole对象创建一个角色
	 * @param role SimpleRole对象
	 * @return -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int createRole(SimpleRole role) throws RoleServiceFault {
		int result = -1;
		try {
			RoleProcess process = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			if (role == null || StringUtil.isBlank(role.getName())) {
				throw new NullPointerException("对象为空或对象的名称为空.");
			}

			ApplicationVO application = WebServiceUtil.validateApplication(role
					.getApplicationName());
			RoleVO vo = new RoleVO();
			RoleUtil.convertToVO(vo, role);

			RoleVO temp = ((RoleProcess) process).doViewByName(role.getName(),
					application.getId());
			if (temp == null) {
				process.doCreate(vo);
			} else {
				if (role.getName().equalsIgnoreCase(temp.getName())) {
					throw new Exception("该角色名称" + role.getName() + "已存在.");
				}
			}
			result = 0;
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new RoleServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 传入一个SimpleRole对象更新一个角色
	 * @param role SimpleRole对象
	 * @return  -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int updateRole(SimpleRole role) throws RoleServiceFault {
		int result = -1;
		try {
			RoleProcess process = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			if (role == null || StringUtil.isBlank(role.getName())) {
				throw new NullPointerException("对象为空或对象名称为空!");
			}
			ApplicationVO application = WebServiceUtil.validateApplication(role
					.getApplicationName());
			RoleVO vo = (RoleVO) process.doView(role.getId());
			if (vo == null)
				throw new Exception("数据库不存在该" + role.getId() + "对象.");

			if (!vo.getName().equals(role.getName())) {
				RoleVO temp = ((RoleProcess) process).doViewByName(role
						.getName(), application.getId());
				if (temp != null) {
					throw new Exception("该角色名称" + role.getName() + "已存在.");
				}
			}

			RoleUtil.convertToVO(vo, role);
			process.doUpdate(vo);
			
			result = 0;
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new RoleServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 传入主键查找一个角色
	 * @param pk 角色主键
	 * @return SimpleRole对象
	 * @throws RoleServiceFault
	 */
	public SimpleRole getRole(String pk) throws RoleServiceFault {
		SimpleRole role = null;
		try {
			RoleProcess process = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			if (pk == null) {
				throw new NullPointerException("主键为空.");
			}
			RoleVO vo = (RoleVO) process.doView(pk);
			if (vo != null) {
				role = new SimpleRole();
				RoleUtil.convertToSimple(role, vo);
			}
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new RoleServiceFault(e.getMessage());
			}
		}
		return role;
	}

	/**
	 * 传入主键删除一个角色
	 * @param pk 主键
	 * @return  -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int deleteRole(String pk) throws RoleServiceFault {
		int result = -1;
		try {
			RoleProcess process = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			if (pk == null) {
				throw new NullPointerException("主键为空.");
			}
			process.doRemove(pk);
			
			result = 0;
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new RoleServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 传入主键数组删除角色
	 * @param pks 主键数组
	 * @return  -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int deleteRole(String[] pks) throws RoleServiceFault {
		int result = -1;
		try {
			if(pks != null){
				for (int i = 0; i < pks.length; i++) {
					String pk = pks[i];
					deleteRole(pk);
				}
			}
			result = 0;
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}
		return result;
	}

	/**
	 * 设置角色权限
	 * 
	 * @param role
	 *            -角色
	 * @param permission
	 *            -权限ID字符串组
	 * @return  -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int setPermissionSet(SimpleRole role, String[] resources)
			throws RoleServiceFault {
		int result = -1;
		try {
			RoleProcess process = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			if (role == null || StringUtil.isBlank(role.getId()))
				throw new NullPointerException("对象为空或对象的ID为空!");
			ApplicationVO application = WebServiceUtil.validateApplication(role
					.getApplicationName());
			RoleVO vo = (RoleVO) process.doView(role.getId());
			if (vo == null)
				throw new Exception("数据库不存在该ID对象.");

			Collection<PermissionVO> coll = new HashSet<PermissionVO>();
			if (resources != null) {
				for (String id : resources) {
					ResourceProcess rp = (ResourceProcess) ProcessFactory
							.createProcess(ResourceProcess.class);
					ResourceVO res = (ResourceVO) rp.doView(id);
					if (res != null
							&& res.getApplicationid().equals(
									application.getId())) {
						PermissionVO per = new PermissionVO();
						per.setApplicationid(vo.getApplicationid());
						per.setId(Sequence.getSequence());
						per.setResourceId(res.getId());
						coll.add(per);
					} else {
						throw new Exception(id + "不存在或权限与角色不在同一应用！");
					}
				}
			}
			vo.setPermission(coll);
			process.doUpdate(vo);
			
			result = 0;
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new RoleServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 传入Json格式字符串创建一个角色
	 * @param jsonStr
	 * @return  -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int createRoleFromJson (String jsonStr) throws RoleServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(jsonStr))
				throw new RoleServiceFault("Parameter 'jsonStr' can not be null.");
			SimpleRole simpleRole = (SimpleRole) JsonUtil.toBean(jsonStr, SimpleRole.class);
			result = createRole(simpleRole);
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入XML格式字符串创建一个角色
	 * @param xmlStr
	 * @return  -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int createRoleFromXML (String xmlStr) throws RoleServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(xmlStr))
				throw new RoleServiceFault("Parameter 'xmlStr' can not be null.");
			SimpleRole simpleRole = (SimpleRole) XmlUtil.toOjbect(xmlStr);
			result = createRole(simpleRole);
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入主键返回角色对象格式化成JSON的字符串
	 * @param pk 主键
	 * @return JSON字符串
	 * @throws RoleServiceFault
	 */
	public String findRoleFormat2Json(String pk) throws RoleServiceFault {
		SimpleRole simpleRole = getRole(pk);
		return JsonUtil.toJson(simpleRole);
	}
	
	/**
	 * 传入主键返回角色对象格式化成XML的字符串
	 * @param pk 主键
	 * @return XML字符串
	 * @throws RoleServiceFault
	 */
	public String findRoleFormat2XML(String pk) throws RoleServiceFault {
		SimpleRole simpleRole = getRole(pk);
		return XmlUtil.toXml(simpleRole);
	}
	
	/**
	 * 传入用户Id查询角色的集合
	 * @param userId 用户id
	 * @return SimpleRole对象集合
	 * @throws RoleServiceFault
	 */
	public Collection<SimpleRole> getRolesByUserId(String userId) throws RoleServiceFault {
		Collection<SimpleRole> simpleRole = new ArrayList<SimpleRole>();
		try {
			RoleProcess process = (RoleProcess) ProcessFactory
				.createProcess(RoleProcess.class);
			Collection<RoleVO> roles = process.queryByUser(userId);
			for (Iterator<RoleVO> iterator = roles.iterator(); iterator.hasNext();) {
				RoleVO roleVO = (RoleVO) iterator.next();
				SimpleRole role = new SimpleRole();
				RoleUtil.convertToSimple(role, roleVO);
				simpleRole.add(role);
			}
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new RoleServiceFault(e.getMessage());
			}
		}
		return simpleRole;
	}
	
	/**
	 * 为指定的角色添加一个用户
	 * @param roleId 角色id
	 * @param userId 用户id
	 * @return  -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int addUser(String roleId,String userId) throws RoleServiceFault {
		int result = -1;
		try {
			RoleVO role = WebServiceUtil.findRoleWithValidate(roleId);
			UserVO vo = WebServiceUtil.findUserWithValidate(userId);
			
			UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
			
			DomainVO domain = vo.getDomain();
			boolean flag = true;
			if (role != null) {
				Iterator<?> it = domain.getApplications().iterator();
				while (it.hasNext()) {
					ApplicationVO temp = (ApplicationVO) it.next();
					if (temp.getId().equals(role.getApplicationid())) {
						flag = false;
						break;
					}
				}
			}
			if (flag) {
				throw new Exception("该角色" + roleId + "对应的应用"
						+ role.getApplicationid() + "还没应用到该域"
						+ domain.getName());
			}
			
			Collection<UserRoleSet> coll = vo.getUserRoleSets();
			boolean exist = false;
			for (Iterator<UserRoleSet> iterator = coll.iterator(); iterator.hasNext();) {
				UserRoleSet userRoleSet = (UserRoleSet) iterator.next();
				if(userRoleSet.getRoleId().equals(roleId)){
					exist = true;//角色下已存在该用户
					break;
				}
			}
			if(!exist){
				String [] array = new String[1];
				array[0] = userId;
				userProcess.addUserToRole(array, roleId);
			}
			result = 0;
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new RoleServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 为角色添加用户集合
	 * @param roleId 角色id
	 * @param userIds 用户id数组
	 * @return  -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int addUsers(String roleId, String[] userIds) throws RoleServiceFault {
		int result = -1;
		try {
			if(userIds == null)
				throw new RoleServiceFault("用户数组不能为null");
			for (int i = 0; i < userIds.length; i++) {
				addUser(roleId,userIds[i]);
			}
			
			result = 0;
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new RoleServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 为指定的角色移除一个用户
	 * @param id 角色id
	 * @param userId 用户id
	 * @return  -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int removeUser(String id,String userId) throws RoleServiceFault {
		int result = -1;
		try {
			new UserService().removeRole(userId, id);
			
			result = 0;
		} catch (UserServiceFault e) {
			throw new RoleServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new RoleServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 传入所有属性值创建一个角色
	 * @param id 角色id
	 * @param name 角色名
	 * @param engname 角色engname
	 * @param applicationName 软件名称
	 * @return  -1:失败 ,0:成功
	 * @throws RoleServiceFault
	 */
	public int createRole (String id, String name, String engname, String applicationName)
			throws RoleServiceFault {
		int result = -1;
		try {
			SimpleRole role = new SimpleRole();
			role.setId(id);
			role.setName(name);
			role.setEngname(engname);
			role.setApplicationName(applicationName);
			result = createRole(role);
		} catch (Exception e) {
			throw new RoleServiceFault(e.getMessage());
		}
		return result;
	}

}
