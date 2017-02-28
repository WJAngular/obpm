package cn.myapps.core.scheduler.ejb;

import java.io.Serializable;

/**
 * 任务
 * @author Happy
 *
 */
public interface Job extends Serializable{
	
	/**
	 * 执行任务
	 * @throws Exception
	 */
	public void execute() throws Exception;

}
