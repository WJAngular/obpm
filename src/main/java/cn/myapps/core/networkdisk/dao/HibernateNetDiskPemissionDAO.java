package cn.myapps.core.networkdisk.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.networkdisk.ejb.NetDiskPemission;

public class HibernateNetDiskPemissionDAO extends HibernateBaseDAO<NetDiskPemission> implements
		NetDiskPemissionDAO {

	public HibernateNetDiskPemissionDAO(String voClassName) {
		super(voClassName);
	}
}
