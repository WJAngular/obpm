package cn.myapps.core.networkdisk.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.networkdisk.ejb.NetDiskFile;

public class HibernateNetDiskFileDAO extends HibernateBaseDAO<NetDiskFile> implements
		NetDiskFileDAO {

	public HibernateNetDiskFileDAO(String voClassName) {
		super(voClassName);
	}
}
