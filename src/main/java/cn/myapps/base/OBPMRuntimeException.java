package cn.myapps.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 运行时异常（前台）
 * @author Happy
 *
 */
public class OBPMRuntimeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1466262341748019094L;
	
	/**
	 * 本地异常信息（友好提示）
	 */
	private String nativeMessage;
	
	private String completeMessage;
	
	public String getNativeMessage() {
		return nativeMessage;
	}

	public void setNativeMessage(String nativeMessage) {
		this.nativeMessage = nativeMessage;
	}

	public OBPMRuntimeException() {
		super();
	}

	/**
	 * 传入本地异常信息构造一个OBPM运行时异常
	 * @param nativeMessage
	 * 	本地异常信息
	 */
	public OBPMRuntimeException(String nativeMessage) {
		super("");
		this.nativeMessage = nativeMessage;
	}

	/**
	 * 传入异常实例构造一个OBPM运行时异常
	 * @param cause
	 * 		异常实例
	 */
	public OBPMRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * 传入本地异常信息和异常实例构造一个OBPM运行时异常
	 * @param nativeMessage
	 * 		本地异常信息
	 * @param cause
	 * 		异常实例
	 */
	public OBPMRuntimeException(String nativeMessage, Throwable cause) {
		super("", cause);
		this.nativeMessage = nativeMessage;
	}
	
	/**
	 * 传入本地异常信息、详细异常信息和异常实例构造一个OBPM运行时异常
	 * @param nativeMessage
	 * 		本地异常信息
	 * @param detailMessage
	 * 		详细异常信息
	 * @param cause
	 * 		异常实例
	 */
	public OBPMRuntimeException(String nativeMessage,String detailMessage, Throwable cause) {
		super(detailMessage, cause);
		this.nativeMessage = nativeMessage;
	}
	
	public String getCompleteMessage() {
		if(completeMessage ==null){
			
		StringBuffer msg = new StringBuffer(getCause().toString());
		Pattern pat = Pattern.compile("\n");
		Matcher mat = pat.matcher(msg.toString());
		StringBuffer result = new StringBuffer(mat.replaceAll(" "));

		StackTraceElement[] trace = getStackTrace();
        for (int i=0; i < trace.length; i++){
        	result.append("\tat " + trace[i]).append("\\n");
        }

        completeMessage = result.toString();
		}
		return completeMessage;
	}

}
