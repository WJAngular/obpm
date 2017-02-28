package cn.myapps.base;

/**
 * 校验异常
 * @author linda
 *
 */
public class OBPMValidateException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3710244427683148491L;
	
	/**
	 * 本地异常信息（友好提示）
	 */
	private String validateMessage;

	public String getValidateMessage() {
		return validateMessage;
	}

	public void setValidateMessage(String validateMessage) {
		this.validateMessage = validateMessage;
	}

	public OBPMValidateException() {
		super();
	}

	/**
	 * 传入本地异常信息构造一个校验异常
	 * @param validateMessage
	 * 	本地异常信息
	 */
	public OBPMValidateException(String validateMessage) {
		super(validateMessage);
		this.validateMessage = validateMessage;
	}

	/**
	 * 传入异常实例构造一个校验异常
	 * @param cause
	 * 		异常实例
	 */
	public OBPMValidateException(Throwable cause) {
		super(cause);
	}

	/**
	 * 传入本地异常信息和异常实例构造一个校验异常
	 * @param validateMessage
	 * 		本地异常信息
	 * @param cause
	 * 		异常实例
	 */
	public OBPMValidateException(String validateMessage, Throwable cause) {
		super("", cause);
		this.validateMessage = validateMessage;
	}
	
	/**
	 * 传入本地异常信息、详细异常信息和异常实例构造一个校验异常
	 * @param validateMessage
	 * 		本地异常信息
	 * @param detailMessage
	 * 		详细异常信息
	 * @param cause
	 * 		异常实例
	 */
	public OBPMValidateException(String validateMessage,String detailMessage, Throwable cause) {
		super(detailMessage, cause);
		this.validateMessage = validateMessage;
	}
	
}
