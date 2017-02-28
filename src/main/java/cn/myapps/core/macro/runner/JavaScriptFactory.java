package cn.myapps.core.macro.runner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import cn.myapps.util.StringUtil;

import com.jamonapi.proxy.MonProxyFactory;

public class JavaScriptFactory {

	private static final ThreadLocal<Map<String,IRunner>> _threadRunner = new ThreadLocal<Map<String,IRunner>>();

	private static final HashMap<String, Object> _debugRunners = new HashMap<String, Object>();

	private static final HashSet<String> _debugSessionIds = new HashSet<String>();

	public static void clear() {
		// IRunner runner = (IRunner) _threadRunner.get();
		// if (runner != null) {
		// runner.clear();
		// }
		JavaScriptRunner.clearScripts();
	}

	public static boolean isDebug(String sessionid) {
		if (sessionid != null) {
			return _debugSessionIds.contains(sessionid);
		} else {
			return false;
		}
	}

	public static IRunner getDebugerInstance(String sessionid) {
		Object d = _debugRunners.get(sessionid);
		if (d != null) {
			return (IRunner) d;
		} else {
			return null;
		}
	}

	public static IRunner getInstance(String sessionid, String applicationid) {
		if (isDebug(sessionid)) {
			Map<String,IRunner> runnerMap = _threadRunner.get();
			if (runnerMap !=null && runnerMap.get(applicationid) != null) {
				IRunner runner = (IRunner)runnerMap.get(applicationid);
				runner.clear();
				_threadRunner.remove();
			}

			return JavaScriptDebuger.getInstance(applicationid);
		} else {
			Map<String,IRunner> runnerMap = _threadRunner.get();
			if(runnerMap == null) runnerMap = new HashMap<String, IRunner>();
			IRunner runner =  runnerMap.get(applicationid);
			if (runner == null) {
				runner = (IRunner) MonProxyFactory.monitor(new JavaScriptRunner(applicationid));
				runnerMap.put(applicationid,runner);
				_threadRunner.set(runnerMap);
			}
			
			if (runner.get_htmlJsUtil() != null) {
				runner.get_htmlJsUtil().clear();
			}
			return runner;
		}

	}
	public static IRunner newInstance(String applicationid) {
		IRunner runner = (IRunner) MonProxyFactory.monitor(new JavaScriptRunner(applicationid));
		runner.setApplicationId(applicationid);
		if (runner.get_htmlJsUtil() != null) {
			runner.get_htmlJsUtil().clear();
		}
		return runner;
	}
	

	public static void set_debug(String sessionid, boolean debug) {
		if (debug) {
			_debugSessionIds.add(sessionid);
		} else {
			_debugSessionIds.remove(sessionid);
		}
	}

}
