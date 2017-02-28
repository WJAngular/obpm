package cn.myapps.core.workflow.engine.instruction;

/**
 * 指令
 * @author Happy
 *
 */
public interface Instruction {
	
	/**
	 * 设置内容
	 * @param content
	 */
	public void setContent(Object content);
	
	/**
	 * 运行指令
	 * @throws InstructionException
	 */
	public void run() throws InstructionException;

}
