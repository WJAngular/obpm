package cn.myapps.core.links.ejb;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.links.dao.LinkDAO;

/**
 * @author Happy
 *
 */
public class LinkProcessBean extends AbstractDesignTimeProcessBean<LinkVO> implements
		LinkProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 658247972879997267L;

	public LinkProcessBean() {
	}

	protected IDesignTimeDAO<LinkVO> getDAO() throws Exception {
		return (LinkDAO) DAOFactory.getDefaultDAO(LinkVO.class.getName());
	}

}
