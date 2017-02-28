package cn.myapps.core.workflow.storage.runtime.proxy.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;

/**
 * @author Happy
 *
 */
public interface WorkflowProxyProcess extends IRunTimeProcess<WorkflowProxyVO>{

	/**
	 * 根据代理人获取流程代理信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Collection<WorkflowProxyVO> getDatasByAgent(WebUser user, String applicationid) throws Exception;
	
	/**
	 * 根据所有者获取流程代理信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Collection<WorkflowProxyVO> getDatasByOwners(Collection<BaseUser> owners) throws Exception;

	/**
	 * 流程的唯一校验 
	 * @param flowId
	 * @return
	 * @throws Exception
	 */
	public boolean onlyCheckOnFlow(WorkflowProxyVO vo) throws Exception;
	
	/**
	 * 获取代理人
	 * @param owners
	 * @return
	 */
	public Collection<BaseUser> getAgentsByOwners(Collection<BaseUser> owners) throws Exception;
	
	public void doRemove(String[] pks) throws Exception;
	

	/**
	 * 判断用户是否代理流程
	 * @param userid
	 * 		当前用户id
	 * @return
	 * @throws Exception
	 */
	public boolean isFlowAgent(String userId) throws Exception;
}
