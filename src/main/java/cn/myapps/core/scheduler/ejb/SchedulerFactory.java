package cn.myapps.core.scheduler.ejb;


/**
 * 任务调度器工厂
 * @author Happy
 *
 */
public class SchedulerFactory {
	
	private static final Object LOCK = new Object();
	
	private static Scheduler scheduler;
	
	
	/**
	 * 获取任务调度器(单例模式)
	 * @return
	 */
	public static Scheduler getScheduler(){
		if(scheduler ==null){
			synchronized(LOCK){
				scheduler = new RegularScheduler();
			}
		}
		return scheduler;
	}
	

}
