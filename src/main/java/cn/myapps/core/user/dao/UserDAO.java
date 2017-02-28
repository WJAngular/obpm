package cn.myapps.core.user.dao;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserVO;

public interface UserDAO extends IDesignTimeDAO<UserVO> {

	public abstract UserVO login(String loginno, String domain)
			throws Exception;

	public abstract Collection<UserVO> getDatasByRoleid(String parent,
			String domain) throws Exception;

	public abstract Collection<UserVO> getDatasByDept(String parent,
			String domain) throws Exception;

	public Collection<UserVO> queryHasMail(String domain) throws Exception;

	public boolean isEmpty() throws Exception;

	public Collection<UserVO> queryHasMail(String application, String domain)
			throws Exception;

	public abstract DataPackage<UserVO> queryByRoleId(String roleid)
			throws Exception;

	public Collection<UserVO> queryByDomain(String domainid, int page, int line)
			throws Exception;

	public abstract UserVO login(String no) throws Exception;

	public Collection<UserVO> getDatasByDept(String parent) throws Exception;

	public abstract UserVO findByLoginno(String loginno, String domainid)
			throws Exception;

	public abstract DataPackage<UserVO> listLinkmen(ParamsTable params)
			throws Exception;

	public abstract Collection<UserVO> queryUsersByName(String username,
			String domainid) throws Exception;

	public abstract Collection<UserVO> queryByProxyUserId(String proxyid)
			throws Exception;

	/**
	 * 更新用户默认应用
	 * 
	 * @param userid
	 *            用户ID
	 * @param defaultApplicationid
	 *            默认选择应用ID
	 * @throws Exception
	 */
	public void updateDefaultApplication(String userid,
			String defaultApplicationid) throws Exception;
	/**
	 * 更新用户锁定标志
	 * 登录名
	 * @param loginno
	 * 要锁定的状态
	 * @param lockFlag
	 * @throws Exception
	 */
	public void updateUserLockFlag(String loginno, int lockFlag) throws Exception;
	public Collection<UserVO> queryByHQL(String hql) throws Exception;
	
	public DataPackage<UserVO> querySmByHQL(String hql, ParamsTable params, int page,
			int lines) throws Exception;
	
	/**
	 * 根据企业域以及应用查找用户
	 * @param domian
	 * 			企业域id
	 * @param applicationid
	 * 			应用id
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryByDomainAndApplicationWithHQL(String domain, String applicationid, ParamsTable params) throws Exception;
	
	/**
	 * 根据企业域、应用以及部门查找用户
	 * @param domain
	 * 			企业域id
	 * @param applicationid
	 * 			应用id
	 * @param departmentid
	 * 			部门id
	 * @return
	 * @throws Exception
	 */
	public Collection<UserVO> queryByDomainAndApplicationAndDeptWithHQL(String domain, String applicationid, String departmentid, ParamsTable params) throws Exception;
	
	/**
	 * JDBC批量创建用户
	 * @param users
	 * @throws Exception
	 */
	public void doBatchCreate(Collection<UserVO> users)throws Exception;
	
	/**
	 * 用户选择框查询
	 * @param params 参数
	 * @param user webuser
	 * @param request HttpServletRequest
	 * @param excludeId 排除的用户ID
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryForUserDialog(ParamsTable params,WebUser user,HttpServletRequest request,String excludeId)throws Exception;
	
	/**
	 * 用户选择框，根据部门获取用户
	 * @param params 参数
	 * @param user webuser
	 * @param request HttpServletRequest
	 * @param excludeId 排除的用户ID
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryByDeptForUserDialog(ParamsTable params,WebUser user,HttpServletRequest request,String excludeId)throws Exception;
	
	/**
	 * 用户选择框，根据角色获取用户
	 * @param params 参数
	 * @param user webuser
	 * @param request HttpServletRequest
	 * @param excludeId 排除的用户ID
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryByRoleForUserDialog(ParamsTable params,WebUser user,HttpServletRequest request,String excludeId)throws Exception;

	/**
	 * 查询不包含在指定角色下的用户
	 * @param params
	 * @param roleid
	 * @param page
	 * @param pagelines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryOutOfRole(ParamsTable params,
			String roleid, int page, int pagelines) throws Exception;
	
	/**
	 * 获取指定部门所有用户数
	 * 
	 * @param dptid
	 *            部门ID
	 * @return 返回指定部门下的所有用户对象的集合
	 */
	public Long getCountsByDepartment(String deptId) throws Exception;
	
	/**
	 * 获取指定角色下所有用户数
	 * 
	 * @param dptid
	 *            部门ID
	 * @return 返回指定部门下的所有用户对象的集合
	 */
	public Long getCountsByRole(String roleId) throws Exception;
}
