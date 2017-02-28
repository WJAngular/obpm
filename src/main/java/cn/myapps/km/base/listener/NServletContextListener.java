package cn.myapps.km.base.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

/**
 * @author xiuwei
 *
 */
public class NServletContextListener extends HttpServlet implements
		ServletContextListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -678516392745089774L;

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO 初始化context时 创建必要的数据和一些变量
		

	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

}
