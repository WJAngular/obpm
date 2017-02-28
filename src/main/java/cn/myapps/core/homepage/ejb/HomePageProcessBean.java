package cn.myapps.core.homepage.ejb;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.homepage.dao.HomePageDAO;

public class HomePageProcessBean extends AbstractDesignTimeProcessBean<HomePage> implements HomePageProcess {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8441522680088647665L;
//
	protected IDesignTimeDAO<HomePage> getDAO() throws Exception {
		return (HomePageDAO) DAOFactory.getDefaultDAO(HomePage.class.getName());
	}

}
