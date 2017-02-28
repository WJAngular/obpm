package cn.myapps.core.scheduler.ejb;

/**
 * 任务调度器
 * @author Happy
 *
 */
public interface Scheduler {
	
	/**
	 * 启动任务调度器
	 * @throws Exception
	 */
	public void start() throws Exception;
	
	/**
	 * 关闭任务调度器
	 * @throws Exception
	 */
	public void shutdown() throws Exception;
	
	/**
	 * 任务调度器是否已启动
	 * @return
	 * @throws Exception
	 */
	public boolean isStart() throws Exception;
	
	/**
	 * 任务调度器是否已关闭
	 * @return
	 * @throws Exception
	 */
	public boolean isShutdown() throws Exception;
	
	/**
	 * 添加任务
	 * @param vo
	 * @throws Exception
	 */
	public void addTrigger(TriggerVO vo) throws Exception;
	
	/**
	 * 取消任务
	 * @param token
	 * @throws Exception
	 */
	public void cancelTrigger(String token) throws Exception;
	
	

}
