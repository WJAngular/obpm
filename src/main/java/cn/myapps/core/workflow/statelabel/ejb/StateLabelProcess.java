package cn.myapps.core.workflow.statelabel.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface StateLabelProcess extends IDesignTimeProcess<StateLabel> {
	/**
	 * 根据应用获取流程的状态名称
	 * 
	 * @param application
	 *            应用标识
	 * @return 流程的状态(Statelable)对象
	 * @throws Exception
	 */
	public Collection<StateLabel> doQueryName(String application)
			throws Exception;

	/**
	 * 根据流程的状态的名称查询当下应用下的流程的状态的集合
	 * 
	 * @param name
	 *            流程的状态的名称
	 * @param application
	 *            应用 标识
	 * @return 流程的状态(Statelable)的集合
	 * @throws Exception
	 */
	public Collection<StateLabel> doQueryByName(String name, String application)
			throws Exception;

	/**
	 * 根据应用获取流程的状态的值
	 * 
	 * @param application
	 *            应用 标识
	 * @return 流程的状态(Statelable)的集合
	 * @throws Exception
	 */
	public Collection<StateLabel> doQueryState(String application)
			throws Exception;

}
