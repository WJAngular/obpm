package cn.myapps.support.weixin.trial.ejb;

import cn.myapps.core.user.ejb.UserVO;

public interface WeixinTrialProcess {
	
	/**
	 * 注册用户
	 * @param domainid
	 * 		企业域id
	 * @param name
	 * 		用户名
	 * @param telephone
	 * 		电话
	 * @return
	 * 		用户对象
	 * @throws Exception
	 */
	public UserVO doRegister(String domainid,String name,String telephone) throws Exception;
	
	/**
	 * 清空部门下的所有用户
	 * @param domainid
	 * 		企业域id
	 * @param deptid
	 * 		微信部门id
	 * @throws Exception
	 */
	public void clearDeptUsers(String domainid,String deptid) throws Exception;

}
