package cn.myapps.core.privilege.operation.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.privilege.operation.ejb.OperationVO;

public class HibernateOperationDAO extends HibernateBaseDAO<OperationVO> implements OperationDAO {
	public HibernateOperationDAO(String voClassName) {
		super(voClassName);
	}

	public boolean isEmpty(String applicationId) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.applicationid='" + applicationId + "'";
		Session session = currentSession();
		Query query = session.createQuery(hql);
		List<?> rst = query.list();
		if (!rst.isEmpty()) {
			return false;
		} else
			return true;
	}

	public int getTotal() throws Exception {
		String hql = "FROM " + _voClazzName;
		return getTotalLines(hql);
	}
}
