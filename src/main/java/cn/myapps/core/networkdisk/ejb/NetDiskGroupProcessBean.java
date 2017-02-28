package cn.myapps.core.networkdisk.ejb;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.networkdisk.dao.NetDiskGroupDAO;

public class NetDiskGroupProcessBean extends AbstractDesignTimeProcessBean<NetDiskGroup>
		implements NetDiskGroupProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5732761766615743594L;

	@Override
	protected IDesignTimeDAO<NetDiskGroup> getDAO() throws Exception {
		return (NetDiskGroupDAO)DAOFactory.getDefaultDAO(NetDiskGroup.class.getName());
	}

}
