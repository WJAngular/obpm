package cn.myapps.core.networkdisk.ejb;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.networkdisk.dao.NetDiskPemissionDAO;

public class NetDiskPemissionProcessBean extends AbstractDesignTimeProcessBean<NetDiskPemission>
		implements NetDiskPemissionProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4311331486716417514L;

	@Override
	protected IDesignTimeDAO<NetDiskPemission> getDAO() throws Exception {
		return (NetDiskPemissionDAO)DAOFactory.getDefaultDAO(NetDiskPemission.class.getName());
	}

}
