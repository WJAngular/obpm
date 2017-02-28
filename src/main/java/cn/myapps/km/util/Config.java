package cn.myapps.km.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Variant;

/**
 * @author xiuwei
 *
 */
public class Config {

	private static Boolean enabled = false;//SWF文件转换器是否可正常使用
	public static boolean hasCheck = false;
	protected static boolean isWpsEnabled = false;
	protected static boolean isPdf2swfEnabled = false;
	protected static String OS = System.getProperty("os.name");
	protected static String DIRECTORY_SEPARATOR = null;
	
	private static Integer WPS_VERSION = null;
	
	/**
	 * 是否支持在线预览功能
	 * <p>
	 *  检测系统是否正常安装WPS和SwfTools软件，这两款组件是实现在线预览功能的必要外部组件。
	 * </p>
	 * @return
	 */
	public static boolean previewEnabled(){
		if(!hasCheck){
			try {
				isWpsEnabled = wpsEnabled();
				hasCheck = true;
				enabled = isWpsEnabled;
			} catch (Exception e) {
				enabled = false;
			}
		}
		return enabled;

	}
	
	public static synchronized void refresh(){
		hasCheck = false;
	}
	
	/**
	 * 检测系统是否正确安装金山WPS软件
	 * @return
	 */
	public static synchronized boolean wpsEnabled() throws Exception{
		if(!OS.toLowerCase().contains("windows")) return false;
		ActiveXComponent wps = null;
		try {
			ComThread.InitMTA(true);
			try {
				wps = new ActiveXComponent("wps.application");
				WPS_VERSION = 8;
			} catch (Exception e) {
				wps = new ActiveXComponent("Kwps.application");
				if(wps !=null) WPS_VERSION = 9;
			}
			
			if(wps !=null) return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			try {
				if (wps != null) {
					wps.invoke("Quit", new Variant(false));
				}
			} catch (Exception e) {
				wps.invoke("Terminate");
				throw e;
			}
			if (wps != null)
				wps.safeRelease();
			
			ComThread.Release();
		}
		return false;
	}
	/**
	 * 检测系统是否正确安装SwfTools软件
	 * @return
	 */
	public static synchronized boolean pdf2swfEnabled(){
		try {
			Process p = Runtime.getRuntime().exec("pdf2swf.exe --version 2>&1");
			BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream( )));
			String line;
			while ((line = is.readLine( )) != null){
				if(line.toLowerCase().startsWith("error"))
					return false;
			}
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取系统安装的金山WPS软件的版本号
	 * @return
	 */
	public static int getWpsVersion(){
		if(WPS_VERSION ==null){
			try {
				wpsEnabled();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return WPS_VERSION.intValue();
	}
	
}
