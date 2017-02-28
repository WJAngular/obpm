package cn.myapps.core.macro.runner;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import cn.myapps.constans.Environment;
import cn.myapps.core.macro.repository.ejb.RepositoryProcess;
import cn.myapps.core.macro.repository.ejb.RepositoryVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class JavaScriptRunner extends AbstractRunner {

	private static HashSet<String> libs = new HashSet<String>();

	private static HashMap<String, Script> scripts = new HashMap<String, Script>();

	ContextFactory contextFactory;

	private Context context;

	private Scriptable scope;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	public static void clearScripts() {
		libs.clear();
		scripts.clear();
	}

	
	public JavaScriptRunner(String applicationId) {
		Monitor monitor = MonitorFactory.start("New JavaScriptRunner");
		contextFactory = new ContextFactory();
		context = contextFactory.enter();
		scope = new ImporterTopLevel(context);

		// Load BaseLib
		try {
			String baselib = readBaseLib();
			if (baselib != null && baselib.length() > 0) {
				Script script = context.compileString(baselib, "baselib", 1, null);

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
		monitor.stop();
	}

	public Object run(String label, String js) throws Exception {
		try {
			Object result = null;

			if (js != null) {
				if (js.indexOf("#include") != -1) {
					String Content = js;

					Monitor monitor = MonitorFactory.start("include lib");
					while (Content.indexOf("#include") != -1) {
						String libname = null;
						int k = 0, t = 0, e = 0;

						k = Content.indexOf("#include");
						t = Content.indexOf("\"", k + 8);
						e = Content.indexOf("\"", t + 1);
						libname = Content.substring(t + 1, e);

						String threadId = Thread.currentThread().getName() + "";
						if (!libs.contains(libname + "@" + this.applicationId + "@" + threadId)) {
							RepositoryProcess rp = (RepositoryProcess) ProcessFactory.createProcess(RepositoryProcess.class);
							RepositoryVO rpvo = rp.getRepositoryByName(libname, this.applicationId);
							if (rpvo != null && rpvo.getContent() != null && rpvo.getContent().trim().length() > 0) {

								Script script = context.compileString(rpvo.getContent(), rpvo.getName(), 1, null);

								script.exec(context, scope);
								libs.add(libname + "@" + this.applicationId + "@" + threadId);
							}
						}
						String st = Content.substring(0, k);
						String ed = Content.substring(e + 1, Content.length());
						Content = st + ed;
					}
					monitor.stop();
					js = Content;
				}
				Monitor monitor = MonitorFactory.start("Compile JavaScriptRunner Label: " + label);

				Script script = (Script) scripts.get(label);
				if (script == null) {
					script = context.compileString(js, label, 1, null);
					scripts.put(label, script);
				}

				monitor.stop();

				monitor = MonitorFactory.start("Exec JavaScriptRunner Label: " + label);
				result = script.exec(context, scope);
				monitor.stop();

				if (result instanceof NativeJavaObject) {
					NativeJavaObject nobj = (NativeJavaObject) result;
					result = nobj.unwrap();
				}

			}
			if(result instanceof Undefined){
				return null;
			}
			return result;

		} catch (Exception e) {
			Environment env = Environment.getInstance();
			String path = env.getRealPath("/logs/iscript/");
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(path + "/" + sessionid + ".log", true));
			PrintWriter printWriter = new PrintWriter(outStream);
			e.printStackTrace(printWriter);

			printWriter.close();

			throw new Exception("JavaScriptRunning:[" + label + "]: " + e.getMessage(), e);
		} finally {
			// Moniter.unRegistRunningInfo(label);
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
		if ((bean instanceof Number) || (bean instanceof String) || (bean instanceof Boolean)) {
			scope.put(registName, scope, bean);
		} else {
			// Must wrap non-scriptable objects before presenting to Rhino
			Scriptable wrapped = Context.toObject(bean, scope);
			scope.put(registName, scope, wrapped);
		}

		// Object jsBean = Context.javaToJS(bean, scope);
		// scope.put(registName, scope, jsBean);
	}

	public void clear() {
		if (context != null) {
			Context.exit();
			libs.clear();
			scripts.clear();
		}
	}

	public void undeclareBean(String registName) throws Exception {
		scope.delete(registName);
	}

}
