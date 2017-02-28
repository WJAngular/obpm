package cn.myapps.core.usersetup.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.usersetup.ejb.UserSetupVO;

public class HibernateUserSetupDAO extends HibernateBaseDAO<UserSetupVO> implements UserSetupDAO {
	public HibernateUserSetupDAO(String voClassName) {
		super(voClassName);
	}
	
	@SuppressWarnings("unchecked")
	public ValueObject find(String id) throws Exception {
		Session session = currentSession();
		ValueObject rtn = null;
		if (id != null && id.length() > 0) {
			String hql = "FROM " + _voClazzName + " WHERE id='" + id + "'";
			Query query = session.createQuery(hql);
			query.setFirstResult(0);
			query.setMaxResults(1);
			List result = query.list();

			if (!result.isEmpty()) {
				rtn = (ValueObject) result.get(0);
			}
		}
		return rtn;
	}
}
