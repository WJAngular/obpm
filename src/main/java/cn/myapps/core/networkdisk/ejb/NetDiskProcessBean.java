package cn.myapps.core.networkdisk.ejb;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.networkdisk.dao.NetDiskDAO;

public class NetDiskProcessBean extends AbstractDesignTimeProcessBean<NetDisk> implements
		NetDiskProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9065002664723531746L;

	@Override
	protected IDesignTimeDAO<NetDisk> getDAO() throws Exception {
		return (NetDiskDAO)DAOFactory.getDefaultDAO(NetDisk.class.getName());
	}

}
