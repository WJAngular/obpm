package cn.myapps.core.helper.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.helper.ejb.HelperVO;

public class HibernateHelperDAO extends HibernateBaseDAO<HelperVO> implements HelperDAO {
	public HibernateHelperDAO(String voClassName) {
		super(voClassName);
	}

	public HelperVO getHelperByName(String urlname, String application)
			throws Exception {

		String hql = "from HelperVO sp where url =" + "'" + urlname + "'";
		if (application != null && application.length() > 0) {
			hql += (" and applicationid = '" + application + "' ");
		}
		return (HelperVO) this.getData(hql);

	}

}
