package cn.myapps.core.workflow.engine.instruction;


/**
 * 指令执行异常
 * @author Happy
 *
 */
public class InstructionException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4714454070426664344L;

	public InstructionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InstructionException(String message) {
		super(message);
	}

	public InstructionException(Throwable cause) {
		super(cause);
	}
	

}
