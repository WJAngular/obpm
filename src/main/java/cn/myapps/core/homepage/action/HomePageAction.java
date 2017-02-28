package cn.myapps.core.homepage.action;


import cn.myapps.base.action.BaseAction;
import cn.myapps.core.homepage.ejb.HomePage;
import cn.myapps.core.homepage.ejb.HomePageProcess;
import cn.myapps.util.ProcessFactory;

public class HomePageAction extends BaseAction<HomePage> {
//
//	private String tempRoles;
//	/**
//	 * 
//	 */
	private static final long serialVersionUID = -4664734701291763551L;
//
//	/**
//	 * @SuppressWarnings 工厂方法不支持泛型
//	 * @throws ClassNotFoundException
//	 */
	@SuppressWarnings("unchecked")
	public HomePageAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(HomePageProcess.class), new HomePage());
	}

}
