package cn.myapps.core.macro.runner;

import javax.servlet.http.HttpServletRequest;

public class CompileHelper {
	public static String compile(String script, HttpServletRequest request)
			throws Exception {

		try{
			//String sessionid = request.getSession().getId();
			
			String applicationid = "APPLICATION_ID";
			
			String label = "Line ";
			
			JavaScriptDebuger debuger = JavaScriptDebuger.getInstance(applicationid);
			String msg = debuger.compile(label, script);
			return msg;
			
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
