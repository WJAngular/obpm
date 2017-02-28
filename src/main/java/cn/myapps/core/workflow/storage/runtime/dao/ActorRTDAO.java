package cn.myapps.core.workflow.storage.runtime.dao;

import java.util.Collection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;

public interface ActorRTDAO extends IRuntimeDAO {
	
	public void create(Collection<ActorRT> actorrts) throws Exception;

}
