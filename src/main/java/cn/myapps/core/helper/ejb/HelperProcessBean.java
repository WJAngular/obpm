package cn.myapps.core.helper.ejb;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.helper.dao.HelperDAO;

public class HelperProcessBean extends AbstractDesignTimeProcessBean<HelperVO> implements HelperProcess{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5697810186004525595L;

	protected IDesignTimeDAO<HelperVO> getDAO() throws Exception {
		return (HelperDAO) DAOFactory.getDefaultDAO(HelperVO.class.getName());
	}

	public HelperVO getHelperByName(String urlname, String application) throws Exception
	{
		return (((HelperDAO)getDAO())).getHelperByName(urlname, application);
	
	}
	
}
