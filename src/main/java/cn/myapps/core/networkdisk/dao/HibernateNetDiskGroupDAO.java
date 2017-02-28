package cn.myapps.core.networkdisk.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.networkdisk.ejb.NetDiskGroup;

public class HibernateNetDiskGroupDAO extends HibernateBaseDAO<NetDiskGroup> implements
		NetDiskGroupDAO {

	public HibernateNetDiskGroupDAO(String voClassName) {
		super(voClassName);
	}

}
