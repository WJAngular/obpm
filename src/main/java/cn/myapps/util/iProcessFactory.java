package cn.myapps.util;

import net.sf.cglib.proxy.MethodInterceptor;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.base.ejb.IRunTimeProcess;

public interface iProcessFactory extends MethodInterceptor{
	public IRunTimeProcess createRuntimeProcess(Class<?> iProcessClazz,
			String applicationid) throws CreateProcessException ;

	public IDesignTimeProcess createProcess(Class<?> iProcessClazz)
			throws ClassNotFoundException;

	public IDesignTimeProcess<?> createProcess(String clazzName)
			throws ClassNotFoundException;
}
