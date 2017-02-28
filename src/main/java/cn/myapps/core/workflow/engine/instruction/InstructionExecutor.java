package cn.myapps.core.workflow.engine.instruction;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

/**
 * 持久化指令执行器（单例模式）
 * @author Happy
 *
 */
public class InstructionExecutor {
	
	Logger log = Logger.getLogger(InstructionExecutor.class);
	
	/**
	 * 指令执行器实例
	 */
	private static InstructionExecutor INSTANCE = null;
	
	private ThreadLocal<Queue<Instruction>> queueHolder = new ThreadLocal<Queue<Instruction>>();
	
	/**
	 * 获取指令执行器实例
	 * @return
	 */
	public static InstructionExecutor getInstance(){
		
		if(INSTANCE==null){
			synchronized (InstructionExecutor.class) {
				INSTANCE = new InstructionExecutor();
			}
		}
		return INSTANCE;
		
	}
	
	/**
	 * 添加一个指令到指令队列
	 * @param instruction
	 * 		指令对象
	 */
	public void put(Instruction instruction){
		Queue<Instruction> queue = getQueue();
		queue.add(instruction);
	}
	
	/**
	 * 执行队列中的指令
	 * @throws InstructionException
	 */
	public void execute() throws InstructionException{
		
			Queue<Instruction> queue = getQueue();
			Instruction instruction = null;
			log.debug("###########开始执行持久化指令##########");
			long ct = System.currentTimeMillis();
			while((instruction = queue.poll())!=null){
				instruction.run();
			}
			log.debug("###########结束执行持久化指令 耗时：[ "+(System.currentTimeMillis()-ct)+"ms ]##########");
	}
	
	/**
	 * 获取指令队列
	 * @return
	 */
	private Queue<Instruction> getQueue(){
		Queue<Instruction> queue = queueHolder.get();
		if(queue==null){
			queue = new ConcurrentLinkedQueue<Instruction>();
			queueHolder.set(queue);
		}
		
		return queue;
	}

	/**
	 * 清空指令队列
	 */
	public void clear() {
		Queue<Instruction> queue = queueHolder.get();
		if(queue != null){
			queue.clear();
			log.debug("清空持久化指令队列！");
		}
	}

}
