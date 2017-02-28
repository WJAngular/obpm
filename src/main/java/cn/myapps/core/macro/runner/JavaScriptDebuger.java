package cn.myapps.core.macro.runner;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.repository.ejb.RepositoryProcess;
import cn.myapps.core.macro.repository.ejb.RepositoryVO;
import cn.myapps.core.macro.runner.Dim.SourceInfo;
import cn.myapps.core.macro.runner.Dim.StackFrame;
import cn.myapps.core.macro.util.CurrDocJsUtil;
import cn.myapps.core.macro.util.FactoryJsUtil;
import cn.myapps.core.macro.util.HTMLJsUtil;
import cn.myapps.core.macro.util.PrintJsUtil;
import cn.myapps.core.macro.util.Tools;
import cn.myapps.core.macro.util.WebJsUtil;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.ftp.FTPUpload;
import cn.myapps.util.mail.EmailUtil;

public class JavaScriptDebuger extends AbstractRunner implements GuiCallback {

	public static final ThreadLocal<Map<String,JavaScriptDebuger>> _threadDebuger = new ThreadLocal<Map<String,JavaScriptDebuger>>();

	private static final HashMap<String, JavaScriptDebuger> _debugRunners = new HashMap<String, JavaScriptDebuger>();

//	HTMLJsUtil _htmlJsUtil;

	private Dim engine;

	private String sessionid;

	private boolean finnished;

	private DebugFrameInfo debugInfo;

	// private ScriptContext context;

	// private String label;

	private HashSet<String> libs = new HashSet<String>();

	private String applicationid;

	ContextFactory contextFactory;

	private Context context;

	private Scriptable scope;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	public void clear() {
		this.libs.clear();
		contextFactory.exit();
		engine.detach();
		_threadDebuger.set(null);

		// _debugRunners.remove(key)
	}

	private JavaScriptDebuger(String applicationId) {
		contextFactory = new ContextFactory();
		engine = new Dim();
		engine.attachTo(contextFactory);

		context = new Context();

		try {
			contextFactory.exit();
		} catch (Exception e) {
			// e.printStackTrace();
		}

		contextFactory.enter(context);

		scope = new ImporterTopLevel(context);

		engine.setGuiCallback(this);

		engine.setScopeProvider(new ScopeProvider() {
			public Scriptable getScope() {
				return scope;
			}
		});

		// Load BaseLib
		try {
			String baselib = readBaseLib();
			// context.compileString(baselib, "baselib", 1, null);
				if (baselib != null && baselib.length() > 0) {
	
				Script script = engine.compileScript("baselib", baselib);
	
				script.exec(context, scope);
			}
			
			if(!StringUtil.isBlank(applicationId)){
				//编译软件下的函数库
				RepositoryProcess rp = (RepositoryProcess) ProcessFactory.createProcess(RepositoryProcess.class);
				Collection<RepositoryVO> libs = rp.doQueryByApplication(applicationId);
				for (Iterator<RepositoryVO> iterator = libs.iterator(); iterator.hasNext();) {
					RepositoryVO lib = iterator.next();
					if (lib != null && !StringUtil.isBlank(lib.getContent())) {
						Script script = context.compileString(lib.getContent(), lib.getName(), 1, null);
						script.exec(context, scope);
					}
				}
				this.setApplicationId(applicationId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// -- Internals only below this point
	// private void createScriptEngine() {
	// ScriptEngineManager manager = new ScriptEngineManager();
	//
	// ScriptEngine engine = manager.getEngineByName(LANG);
	// if (engine == null) {
	// throw new RuntimeException("cannot load " + LANG + " engine");
	// }
	// engine.setBindings(createBindings(), ScriptContext.ENGINE_SCOPE);
	// }

	public static JavaScriptDebuger getInstance(String applicationid) {
		Map<String,JavaScriptDebuger> debugerMap = _threadDebuger.get();
		if(debugerMap == null) debugerMap = new HashMap<String, JavaScriptDebuger>();
		JavaScriptDebuger runner = (JavaScriptDebuger) debugerMap.get(applicationid);
		if (runner == null) {
			runner = new JavaScriptDebuger(applicationid);
			debugerMap.put(applicationid ,runner);
			_threadDebuger.set(debugerMap);
		}
		if (runner._htmlJsUtil != null) {
			runner._htmlJsUtil.clear();
		}

		return runner;
	}

	public static JavaScriptDebuger getDebugInstance(String sessionid) {
		if (sessionid != null) {
			JavaScriptDebuger runner = (JavaScriptDebuger) _debugRunners.get(sessionid);
			if (runner != null) {
				return runner;
			}
		}
		return null;
	}

	/**
	 * 编译
	 * @param label
	 * @param js
	 * @return
	 * @throws Exception
	 */
	public String compile(String label, String js) throws Exception {
		try {
			if (js != null) {
				if (js.indexOf("#include") != -1) {
					String Content = js;

					while (Content.indexOf("#include") != -1) {
						int k = 0, t = 0, e = 0;

						k = Content.indexOf("#include");
						t = Content.indexOf("\"", k + 8);
						e = Content.indexOf("\"", t + 1);

						String st = Content.substring(0, k);
						String ed = Content.substring(e + 1, Content.length());
						Content = st + ed;
					}
					js = Content;
				}
				engine.compileScript(label, js);
			}
		} catch (Exception e) {
			return "ERROR: " + e.getMessage();
		}
		return "SUCCESS!";
	}
	
	public Object run(String label, String js) throws Exception {
//		System.out.println("run label: " + label);
		try {
			setFinnished(false);
			Moniter.registRunningInfo(label, js);

			Object result = null;

			if (js != null) {
				if (js.indexOf("#include") != -1) {
					String Content = js;

					while (Content.indexOf("#include") != -1) {
						String libname = null;
						int k = 0, t = 0, e = 0;

						k = Content.indexOf("#include");
						t = Content.indexOf("\"", k + 8);
						e = Content.indexOf("\"", t + 1);
						libname = Content.substring(t + 1, e);

						if (!libs.contains(libname + "@" + this.applicationid)) {
							RepositoryProcess rp = (RepositoryProcess) ProcessFactory
									.createProcess(RepositoryProcess.class);
							RepositoryVO rpvo = rp.getRepositoryByName(libname, this.applicationid);
							if (rpvo != null && rpvo.getContent() != null && rpvo.getContent().trim().length() > 0) {

								Script script = engine.compileScript(rpvo.getName(), rpvo.getContent());

								script.exec(context, scope);
							}
							libs.add(libname + "@" + this.applicationid);
						}
						String st = Content.substring(0, k);
						String ed = Content.substring(e + 1, Content.length());
						Content = st + ed;
					}
					js = Content;
				}

				try {
					_debugRunners.put(sessionid, this);
					if (JavaScriptFactory.isDebug(sessionid)) {
						engine.setBreak();
					}
					result = engine.evalScript(label, js);
				}catch (Exception e) {
					e.printStackTrace();
					throw e;
				}finally {
					_debugRunners.remove(sessionid);
				}
				if (result instanceof NativeJavaObject) {
					NativeJavaObject nobj = (NativeJavaObject) result;
					result = nobj.unwrap();
				}

			}

			return result;

		} catch (Exception e) {
			throw new Exception("JavaScriptRunning:[" + label + "]: " + e.getMessage(), e);
		} finally {
			setFinnished(true);
			Moniter.unRegistRunningInfo(label);
		}
	}

	public String getHtmlText() {
		if (_htmlJsUtil != null) {
			String text = _htmlJsUtil.toString();
			if (text != null) {
				return text;
			}
		}
		return "";
	}

	public void declareBean(String registName, Object bean, Class<?> clazz) throws Exception {
		// ScriptableObject.defineClass(scope, clazz);

		Object jsBean = Context.javaToJS(bean, scope);
		scope.put(registName, scope, jsBean);
		// scope.put(registName, scope, bean);
		// engine.put(registName, bean);
	}

	public void undeclareBean(String registName) throws Exception {
		scope.delete(registName);
	}

	public void initBSFManager(Document currdoc, ParamsTable params, WebUser user, Collection<ValidateMessage> errors) throws Exception {

		if (params != null && params.getSessionid() != null) {
			this.sessionid = params.getSessionid();
		}

		// 定以自动发送服务
		if (this.applicationid != null && this.applicationid.trim().length() > 0) {
			declareBean("$EMAIL", new EmailUtil(this.applicationid), EmailUtil.class);

		} else {
			declareBean("$EMAIL", new EmailUtil(), EmailUtil.class);
		}
		declareBean("$FTP", new FTPUpload(), FTPUpload.class);

		// 设置页面参数
		if (currdoc != null) {
			currdoc.set_params(params);
		}

		if (currdoc != null)
			declareBean("$CURRDOC", new CurrDocJsUtil(currdoc), CurrDocJsUtil.class);

		// 申明打印工具类
		declareBean("$PRINTER", new PrintJsUtil(this.sessionid), PrintJsUtil.class);

		// 申明WEB工具类
		if (currdoc != null && params != null && user != null)
			declareBean("$WEB", new WebJsUtil(currdoc, params, user, errors), WebJsUtil.class);

		// 申明工具类
		declareBean("$TOOLS", new Tools(), Tools.class);

		// JSFactory类，可调用任一proxy
		declareBean("$BEANFACTORY", new FactoryJsUtil(), FactoryJsUtil.class);

		// PROCESSFactory类，可调用任一proxy
		declareBean("$PROCESSFACTORY", ProcessFactory.getInstance(), ProcessFactory.class);

		/*
		 * // 函数库 BSF_MANAGER.declareBean("$REPOSITORY", new RepositoryVO(),
		 * RepositoryVO.class);
		 */
		// HTML类
		if (_htmlJsUtil == null)
			_htmlJsUtil = new HTMLJsUtil();
		declareBean("$HTML", _htmlJsUtil, HTMLJsUtil.class);

	}

	/**
	 * @hibernate.property column="engine"
	 */
	public Dim getEngine() {
		return engine;
	}

	public void setEngine(Dim engine) {
		this.engine = engine;
	}

	// ////////////
	// volatile DebugFrameInfo _debugInfo;

	public void dispatchNextGuiEvent() throws InterruptedException {

	}

	public void enterInterrupt(StackFrame lastFrame, String threadTitle, String alertMessage) {
	}

	// boolean guiEventThread = false;
	public boolean isGuiEventThread() {
		return false;
	}

	public void updateSourceText(SourceInfo sourceInfo) {

	}

	public boolean isFinnished() {
		return finnished;
	}

	public void setFinnished(boolean finnished) {
		this.finnished = finnished;
	}

	public DebugFrameInfo getDebugInfo() {
		return debugInfo;
	}

	public void clearDebugInfo() {
		debugInfo = null;
	}

	public void returnDebugInfo(String url, String source, int lineno) {
		this.debugInfo = new DebugFrameInfo(url, source, lineno);
	}
}
