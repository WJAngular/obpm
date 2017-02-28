package cn.myapps.core.superuser.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;

/**
 * 
 * 管理员的接口类,在属管理员的用户的接口类
 */
public interface SuperUserProcess extends IDesignTimeProcess<SuperUserVO> {
	/**
	 * 创建管理员
	 */
	public abstract void doCreate(ValueObject vo) throws Exception;

	/**
	 * 移除管理员
	 */
	public abstract void doRemove(String pk) throws Exception;

	/**
	 * 更新管理员
	 */
	public abstract void doUpdate(ValueObject vo) throws Exception;

	/**
	 * 修改管理员用户密码
	 * 
	 * @param id
	 * @param oldPwd
	 * @param newPwd
	 * @throws Exception
	 */
	public abstract void changePwd(String id, String oldPwd, String newPwd) throws Exception;

	/**
	 * SuperUser 登录操作
	 * 
	 * @param loginno
	 *            管理员登录帐号名
	 * @param password
	 *            管理员登录密码
	 * @return SuperUserVO
	 */
	public abstract SuperUserVO login(String no, String password) throws Exception;

	/**
	 * 根据loginno获取管理员
	 * 
	 * @param no
	 *            管理员登录帐号名
	 * @return SuperUserVO
	 * @throws Exception
	 */

	public abstract SuperUserVO login(String no) throws Exception;

	/**
	 * 根据Domain返回数据
	 * 
	 * @param domain
	 *            Domain的唯一标识
	 */
	public abstract Collection<SuperUserVO> getDatasByDomain(String domain) throws Exception;

	/**
	 * 获取管理员的类型
	 * 
	 * @param userType
	 *            用户类型
	 * @return 所属类型的用户列表<java.util.Collection>
	 * @throws Exception
	 */
	public abstract Collection<SuperUserVO> getDatasByType(int userType) throws Exception;

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
	 * 返回所有的用户填写的Email地址
	 * 
	 * @return 用户的Email集合<java.util.Collection>
	 * @throws Exception
	 */
	public Collection<SuperUserVO> doQueryHasMail() throws Exception;

	/**
	 * 管理员判断是否为空
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isEmpty() throws Exception;

	/**
	 * 获得默认的管理员用户(admin为默认的不管理员)
	 * 
	 * @return SuperUserVO
	 * @throws Exception
	 */
	public SuperUserVO getDefaultAdmin() throws Exception;

	/**
	 * 查询管理员
	 * 
	 * @param loginno
	 *            管理员名
	 * @return SuperUserVO
	 * @throws Exception
	 */
	public SuperUserVO doViewByLoginno(String loginno) throws Exception;

	/**
	 * 根据应用查找未加入的开发者
	 * 
	 * @param params
	 *            查询参数
	 * @return 开发者数据包
	 * @throws Exception
	 */
	public DataPackage<SuperUserVO> getUnjoinedDeveloperList(ParamsTable params) throws Exception;

	/**
	 * 根据参数条件以及应用标识,返回已加入的开发者的DataPackage
	 * 
	 * @param params
	 *            查询参数
	 * @return 已加入的开发者数据包<cn.myapps.base.dao.DataPackage>
	 * @throws Exception
	 */
	public DataPackage<SuperUserVO> getJoinedDeveloperList(ParamsTable params) throws Exception;

	/**
	 * 根据参数条件以及应用标识,返回管理员的用户的DataPackage
	 * 
	 * @param params
	 * @return 管理员的用户<cn.myapps.base.dao.DataPackage>
	 * @throws Exception
	 */
	public DataPackage<SuperUserVO> getUnJoinedAdminList(ParamsTable params) throws Exception;
}
