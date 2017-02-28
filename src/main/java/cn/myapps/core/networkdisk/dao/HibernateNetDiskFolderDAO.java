package cn.myapps.core.networkdisk.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.networkdisk.ejb.NetDiskFolder;

public class HibernateNetDiskFolderDAO extends HibernateBaseDAO<NetDiskFolder> implements NetDiskFolderDAO {

	public HibernateNetDiskFolderDAO(String voClassName) {
		super(voClassName);
	}
}
