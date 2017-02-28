package cn.myapps.core.remoteserver.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.remoteserver.ejb.RemoteServerVO;

public class HibernateRemoteServerDAO extends HibernateBaseDAO<RemoteServerVO> implements RemoteServerDAO{

	public HibernateRemoteServerDAO(String voClassName) {
		super(voClassName);
	}
}
