package cn.myapps.util;

import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.base.ejb.IRunTimeProcess;

public abstract class ProcessFactory {

	private static iProcessFactory _process;

	private ProcessFactory() {
	}

	public static iProcessFactory getInstance() {
		if (_process == null) {
			try {
				_process = (iProcessFactory) CLoader.init().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _process;
	}

	public static IRunTimeProcess createRuntimeProcess(Class<?> iProcessClazz,
			String applicationid) throws CreateProcessException {

		return getInstance().createRuntimeProcess(iProcessClazz, applicationid);
	}

	public static IDesignTimeProcess createProcess(Class<?> iProcessClazz)
			throws ClassNotFoundException {
		return getInstance().createProcess(iProcessClazz);
	}

	public static IDesignTimeProcess<?> createProcess(String clazzName)
			throws ClassNotFoundException {

		return getInstance().createProcess(clazzName);
	}
}
