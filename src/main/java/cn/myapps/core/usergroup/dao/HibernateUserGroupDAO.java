package cn.myapps.core.usergroup.dao;

import org.hibernate.Query;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.usergroup.ejb.UserGroupVO;

public class HibernateUserGroupDAO extends HibernateBaseDAO<UserGroupVO> implements UserGroupDAO{
	public HibernateUserGroupDAO(String voClassName) {
		super(voClassName);
	}

	public DataPackage<UserGroupVO> getUserGroupsByUser(String userid)
			throws Exception {
		String hql = "FROM " + _voClazzName + " vo" + " WHERE vo.ownerId=?";
		
		Query query = currentSession().createQuery(hql);
		query.setParameter(0, userid);
		
		DataPackage<UserGroupVO> rtn = new DataPackage<UserGroupVO>();
		rtn.datas = query.list();
		return rtn;
	}
}
