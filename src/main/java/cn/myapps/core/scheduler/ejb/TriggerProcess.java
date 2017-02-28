package cn.myapps.core.scheduler.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface TriggerProcess extends IDesignTimeProcess<TriggerVO> {
	
	/**
	 * 获得就绪的任务触发器
	 * @param interval
	 * 		时间间隔(毫秒)
	 * @return
	 * @throws Exception
	 */
	public Collection<TriggerVO> getStandbyTrigger(long interval) throws Exception;
	
	/**
	 * 判断触发器中的任务是否已被取消
	 * @param id
	 * 		TriggerVO 的 id
	 * @return
	 */
	public boolean isCancel(String id) throws Exception;
	
	/**
	 * 把所有Trigger的Standby状态更新回Pending状态
	 * @throws Exception
	 */
	public void updateStandbyState2WaitingState() throws Exception;
	
	public void removeByToken(String token) throws Exception;
}
