package cn.myapps.core.links.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.links.ejb.LinkVO;

/**
 * @author Happy
 *
 */
public class HibernateLinkDAO extends HibernateBaseDAO<LinkVO> implements LinkDAO {

	public HibernateLinkDAO(String valueObjectName) {
		super(valueObjectName);
	}

	public HibernateLinkDAO() {
	}

}
