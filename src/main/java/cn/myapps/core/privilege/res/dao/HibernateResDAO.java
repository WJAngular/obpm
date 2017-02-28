package cn.myapps.core.privilege.res.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.privilege.res.ejb.ResVO;

public class HibernateResDAO extends HibernateBaseDAO<ResVO> implements
		ResDAO {
	public HibernateResDAO(String voClassName) {
		super(voClassName);
	}

}
