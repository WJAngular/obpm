package cn.myapps.core.workflow.storage.runtime.proxy.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.storage.runtime.proxy.ejb.WorkflowProxyVO;

/**
 * @author Happy
 *
 */
public interface WorkflowProxyDAO extends IRuntimeDAO {
	
	public DataPackage<WorkflowProxyVO> queryByFilter(ParamsTable params,WebUser user) throws Exception;
	
	/**
	 * 根据用户获取用户的流程代理信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public DataPackage<WorkflowProxyVO> queryByAgent(WebUser user, String applicationid) throws Exception;
	
	public DataPackage<WorkflowProxyVO> queryByOwners(Collection<BaseUser> owners) throws Exception;
	
	public long countByFlowAndOwner(WorkflowProxyVO vo) throws Exception;
	

	/**
	 * 判断用户是否代理流程
	 * @param userId
	 * 		当前用户id
	 * @return
	 * @throws Exception
	 */
	public boolean isFlowAgent(String userId) throws Exception;

}
