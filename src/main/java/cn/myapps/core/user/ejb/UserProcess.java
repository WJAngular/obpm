package cn.myapps.core.user.ejb;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;

/**
 * 
 * 用户操作接口
 * 
 */
public interface UserProcess extends IDesignTimeProcess<UserVO> {
	/**
	 * 创建一个用户对象
	 * 
	 * @param ValueObject
	 */
	public abstract void doCreate(ValueObject vo) throws Exception;

	/**
	 * 移除一个用户对象
	 * 
	 * @param pk
	 */
	public abstract void doRemove(String pk) throws Exception;

	/**
	 * 更新一个用户对象
	 */
	public abstract void doUpdate(ValueObject vo) throws Exception;

	/**
	 * 修改用户密码
	 * 
	 * @param id
	 *            用户标识
	 * @param oldPwd
	 *            旧密码
	 * @param newPwd
	 *            新密码
	 * @throws Exception
	 */
	public abstract void changePwd(String id, String oldPwd, String newPwd)
			throws Exception;

	/**
	 * 用户登陆
	 * 
	 * @param no
	 *            用户名
	 * @param password
	 *            用户密码
	 * @param domain
	 *            域名
	 * @return 返回用户(UserVO)对象
	 * @throws Exception
	 */
	public abstract UserVO login(String no, String password, String domain,int pwdErrorTimes)
			throws Exception;

	/**
	 * 用户登陆
	 * 
	 * @param no
	 *            用户名
	 * @param domain
	 *            域名
	 * @return 返回用户(UserVO)对象
	 * @throws Exception
	 */
	public abstract UserVO login(String no, String domain) throws Exception;

	/**
	 * 获得上级部门下的用户
	 * 
	 * @param parent
	 *            部门标识
	 * 
	 * @param domainId
	 *            域标识
	 * @return 用户(UserVO)对象集合
	 * @throws Exception
	 */
	public abstract Collection<UserVO> getDatasByDept(String parent,
			String domainId) throws Exception;

	/**
	 * 获得上级部门下的用户
	 * 
	 * @param parent
	 *            部门标识
	 * @return 用户(UserVO)对象集合
	 * @throws Exception
	 */
	public abstract Collection<UserVO> getDatasByDept(String parent)
			throws Exception;

	/**
	 * 获得上级角色下的用户
	 * 
	 * @param parent
	 *            上级角色标识
	 * @param domainId
	 *            域标识
	 * @return 用户(UserVO)对象集合
	 * @throws Exception
	 */
	public abstract Collection<UserVO> getDatasByGroup(String parent,
			String domainId) throws Exception;

	/**
	 * 针对某个用户更新
	 * 
	 * @param vo
	 *            用户对象
	 * @throws Exception
	 */
	public abstract void doPersonalUpdate(ValueObject vo) throws Exception;

	/**
	 * 初始化用户对象
	 * 
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public WebUser getWebUserInstance(String userid) throws Exception;

	/**
	 * 创建一个用户对象
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public UserVO createUser(UserVO user) throws Exception;

	/**
	 * 获取应用下用户的Email地址
	 * 
	 * @param domain
	 *            企业域标识
	 * @return Email地址集合
	 * @throws Exception
	 */
	public Collection<UserVO> doQueryHasMail(String domain)
			throws Exception;

	/**
	 * 是否为空
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean isEmpty() throws Exception;

	/**
	 * 获得域下所有用户
	 * 
	 * @param domainid
	 *            域标识
	 * @param page
	 *            页码
	 * @param line
	 *            记录
	 * @return 用户(UserVO)集合
	 * @throws Exception
	 */
	public Collection<UserVO> queryByDomain(String domainid, int page, int line)
			throws Exception;

	/**
	 * 获得某个指定角色下的所有用户
	 * 
	 * @param roleid
	 *            角色标识
	 * @return 用户数据集合
	 * @throws Exception
	 */
	public abstract DataPackage<UserVO> doQueryByRoleId(String roleid)
			throws Exception;

	/**
	 * 根据参数条件以及应用标识,返回用户的DataPackage .
	 * <p>
	 * DataPackage为一个封装类，此类封装了所得到的User数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * 
	 * @param params
	 *            参数类
	 * @param user
	 *            用户
	 * @return
	 * @throws Exception
	 */
	public abstract DataPackage<UserVO> listUsers(ParamsTable params,
			WebUser user) throws Exception;

	/**
	 * 登陆
	 * 
	 * @param loginno
	 *            登陆名
	 * @return
	 * @throws Exception
	 */
	public abstract UserVO login(String loginno) throws Exception;

	/**
	 * 获得默认值应用标识
	 * 
	 * @param userid
	 *            用户标识
	 * @return 应用标识
	 * @throws Exception
	 */
	public String getDefaultApplicationId(String userid) throws Exception;

	/**
	 * 深度查询用户树
	 * 
	 * @param cols
	 *            所有用户列表
	 * @param startNode
	 *            开始节点
	 * @param excludeNodeId
	 *            排除节点
	 * @param deep
	 *            深度
	 * @return 用户树
	 * @throws Exception
	 */
	public Map<String, String> deepSearchTree(Collection<?> cols,
			UserVO startNode, String excludeNodeId, int deep) throws Exception;
	
	/**
	 * 深度查询用户树,用于上级选择框
	 * 
	 * @param cols
	 *            所有用户列表
	 * @param startNode
	 *            开始节点
	 * @param excludeNodeId
	 *            排除节点
	 * @param deep
	 *            深度
	 * @return 用户树
	 * @throws Exception
	 */
	public Collection<BaseUser> deepSearchTree2(Collection<?> cols,
			BaseUser startNode, String excludeNodeId, int deep) throws Exception;

	/**
	 * 根据参数条件以及应用标识,返回用户的DataPackage .
	 * <p>
	 * DataPackage为一个封装类，此类封装了所得到的User数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * 
	 * @param params
	 *            参数类
	 * @param user
	 *            用户
	 * @return 用户的DataPackage
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryUsersExcept(ParamsTable params, WebUser user)
			throws Exception;

	/**
	 * 获得用户(UserVO)对象
	 * 
	 * @param loginno
	 *            登陆名
	 * @param domainid
	 *            域标识
	 * @return 用户(UserVO)对象
	 * @throws Exception
	 */
	public UserVO getUserByLoginno(String loginno, String domainid)
			throws Exception;

	/**
	 * 获得联系电话不为空的数据集
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> listLinkmen(ParamsTable params) throws Exception;

	/**
	 * 获得用户(UserVO)的标识, 多条时,默认用(;)分开
	 * 
	 * @param username
	 *            用户名
	 * @param domainid
	 *            域标识
	 * @return 用户(UserVO)的标识,多条时,默认用(;)分开
	 * @throws Exception
	 */
	public String queryUserIdsByName(String username, String domainid)
			throws Exception;

	/**
	 * 根据帐号获取用户标识
	 * 
	 * @param account
	 *            用户帐号
	 * @param domainid
	 *            域标识
	 * @return 用户标识
	 * @throws Exception
	 */
	public String findUserIdByAccount(String account, String domainid)
			throws Exception;

	/**
	 * 查询代理用户集合
	 * 
	 * @param proxyid
	 *            用户标识
	 * @return
	 * @throws Exception
	 */
	public Collection<UserVO> queryByProxyUserId(String proxyid)
			throws Exception;

	/**
	 * 不清空缓存的更新方法
	 * 
	 * @param vo
	 * @throws Exception
	 */
	public void doUpdateWithCache(ValueObject vo) throws Exception;

	/**
	 * 更新用户默认选择的应用
	 * 
	 * @param userid
	 *            用户标识
	 * @param defaultApplicationid
	 *            用户标识
	 * @throws Exception
	 */
	public void doUpdateDefaultApplication(String userid,
			String defaultApplicationid) throws Exception;

	/**
	 * 获取用户对象的下级对象
	 * 
	 * @param userId
	 *            用户标识
	 * @return 下级用户对象
	 * @throws Exception
	 */
	public Collection<UserVO> getUnderList(String userId) throws Exception;

	/**
	 * 获取用户对象的下级对象
	 * 
	 * @param userId
	 *            用户标识
	 * @param maxDeep
	 *            最大深度值
	 * @return 下级用户对象
	 * @throws Exception
	 */
	public Collection<UserVO> getUnderList(String userId, int maxDeep)
			throws Exception;

	/**
	 * 获取用户上级列表
	 * 
	 * @param userId
	 *            用户id
	 * @param maxDeep
	 *            查询深度
	 * @return 上级用户对象集
	 * @throws Exception
	 */
	public Collection<UserVO> getSuperiorList(String userId)
			throws Exception;

	/**
	 * 获取指定部门所有用户
	 * 
	 * @param dptid
	 *            部门ID
	 * @return 返回指定部门下的所有用户对象的集合
	 */
	public Collection<UserVO> queryByDepartment(String deptId) throws Exception;
	
	/**
	 *查询给定部门下的用户，并通过用户的是否公开个人信息属性筛选
	 * @param deptId
	 * 		部门id
	 * @param isUserinfoPublic
	 * 		是否公开个人信息
	 * @return
	 * @throws Exception
	 */
	public Collection<UserVO> queryByDepartment(final String deptId ,boolean isUserinfoPublic) throws Exception;

	/**
	 * 获取指定部门之外的所有用户
	 * 
	 * @param params
	 *            参数表
	 * @param dptid
	 *            部门ID
	 * @return 返回指定部门下的所有用户对象的集合
	 */
	public DataPackage<UserVO> queryOutOfDepartment(ParamsTable params,
			String deptid) throws Exception;

	/**
	 * 为指定部门添加用户
	 * 
	 * @param userids
	 *            数组：用户要添加的用户集合
	 * @param deptid
	 *            部门id
	 */
	public void addUserToDept(String[] userids, String deptid) throws Exception;

	/**
	 * 获取指定角色之外的所有用户
	 * 
	 * @param params
	 *            参数表
	 * @param roleid
	 *            角色ID
	 * @return 返回指定部门下的所有用户对象的集合
	 */
	public DataPackage<UserVO> queryOutOfRole(ParamsTable params, String roleid)
			throws Exception;
	
	/**
	 * 获取指定角色之外的所有用户
	 * 
	 * @param params
	 *            参数表
	 * @param roleid
	 *            角色ID
	 * @return 返回指定部门下的所有用户对象的集合
	 */
	public DataPackage<UserVO> queryOutOfRoleByDomain(ParamsTable params, String roleid,String domainId)
			throws Exception;

	/**
	 * 为指定角色添加用户
	 * 
	 * @param userids
	 *            数组：用户要添加的用户集合
	 * @param roleid
	 *            角色id
	 */
	public void addUserToRole(String[] userids, String roleid) throws Exception;

	/**
	 * /** 获取指定角色下的所有用户
	 * 
	 * @param roleid
	 *            角色ID
	 * @return 返回指定角色下的所有用户对象的集合
	 */
	public Collection<UserVO> queryByRole(String roleId) throws Exception;

	/**
	 * 获取指定部门并角色的所有用户
	 * 
	 * @param dptid
	 *            部门ID
	 * @param roleid
	 *            角色ID
	 * @return 返回指定部门并角色的所有用户对象的集合
	 */
	public Collection<UserVO> queryByDptIdAndRoleId(String deptId, String roleId)
			throws Exception;

	/**
	 * 获取当前域下面的所有用户
	 * 
	 * @return 返回当前域下面的所有用户对象的集合
	 */
	public Collection<UserVO> queryByDomain(String domainid) throws Exception;

	public UserVO getUserByLoginnoAndDoaminName(final String loginno,
			final String domainName) throws Exception;
	
	
	public void updateUserLockFlag(String loginno, int lockFlag) throws Exception;
	
	public Collection<UserVO> doQueryByHQL(String hql) throws Exception;
	
	public DataPackage<UserVO> doQuerySmByHQL(String hql, ParamsTable params, int page,
			int lines) throws Exception;
	
	/**
	 * 
	 * @param vo
	 * 			用户对象
	 * @param flag
	 * 			密码加密标识
	 * @throws Exception
	 */
	public void doCreateWithoutPW(final ValueObject vo, boolean flag) throws Exception;
	
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
	 * 用户选择框,查询用户
	 * @param params 参数
	 * @param user webuser
	 * @param request HttpServletRequest
	 * @param excludeId 排除的用户ID
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryForUserDialog(ParamsTable params,WebUser user,HttpServletRequest request,String excludeId) throws Exception;
	
	/**
	 * 用户选择框,根据部门查询用户
	 * @param params 参数
	 * @param user webuser
	 * @param request HttpServletRequest
	 * @param excludeId 排除的用户ID
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryByDeptForUserDialog(ParamsTable params,WebUser user,HttpServletRequest request,String excludeId) throws Exception;
	
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
