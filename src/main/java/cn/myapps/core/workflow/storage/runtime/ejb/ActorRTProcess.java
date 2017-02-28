package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IRunTimeProcess;

public interface ActorRTProcess extends IRunTimeProcess<ActorRT> {
	
	public void create(Collection<ActorRT> actorrts) throws Exception;
	/**
	 * 
	 * @param stateId
	 * @return
	 * @throws Exception
	 */
	public Collection<ActorRT> queryByFlowStateRT(String stateId)
			throws Exception;

	/**
	 * 获取处理人的集合（不含顺序会签节点中还没有处理权限的处理人）
	 * @param nodeRTId
	 * @param position
	 * @return
	 * @throws Exception
	 */
	public Collection<ActorRT> queryByNodeRT(String nodeRTId,int position) throws Exception;
	
	/**
	 * 获取所有处理人的集合（包含顺序会签节点中还没有处理权限的处理人）
	 * @param nodeRTId
	 * @return
	 * @throws Exception
	 */
	public Collection<ActorRT> queryByNodeRT(String nodeRTId) throws Exception;
}
