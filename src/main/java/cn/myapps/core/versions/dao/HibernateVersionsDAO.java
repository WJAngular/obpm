package cn.myapps.core.versions.dao;

import java.util.Collection;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.versions.ejb.VersionsVO;

public class HibernateVersionsDAO extends HibernateBaseDAO<VersionsVO> implements VersionsDAO{
	
	public HibernateVersionsDAO(String valueObjectName) {
		super(valueObjectName);
	}

	public Collection<VersionsVO> queryByVersionAndType(String name, String number,
			int type) throws Exception {
		String hql = "FROM " + _voClazzName + " WHERE version_name='" + name
				+ "' AND version_number='" + number + "' AND type=" + type;
		return getDatas(hql);
	}

	public Collection<VersionsVO> queryByType(int type) throws Exception {
		String hql = "FROM " + _voClazzName + " WHERE type=" + type + " order by upgrade_date desc";
		return getDatas(hql);
	}

}
