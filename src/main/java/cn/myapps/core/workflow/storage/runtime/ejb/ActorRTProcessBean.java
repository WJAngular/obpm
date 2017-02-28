package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Collection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.workflow.storage.runtime.dao.AbstractActorRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.ActorRTDAO;
import cn.myapps.util.RuntimeDaoManager;

public class ActorRTProcessBean extends AbstractRunTimeProcessBean<ActorRT>
		implements ActorRTProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2839268794842601223L;

	public ActorRTProcessBean(String applicationId) {
		super(applicationId);
	}

	protected IRuntimeDAO getDAO() throws Exception {
		return new RuntimeDaoManager().getActorRtDAO(getConnection(),
				getApplicationId());
	}

	public Collection<ActorRT> queryByFlowStateRT(String stateId)
			throws Exception {
		return ((AbstractActorRTDAO) getDAO()).queryByForeignKey(
				"FLOWSTATERT_ID", stateId);
	}

	public Collection<ActorRT> queryByNodeRT(String nodeRTId,int position) throws Exception {
		return ((AbstractActorRTDAO) getDAO()).queryByNoderRT(nodeRTId,position);
	}
	
	public Collection<ActorRT> queryByNodeRT(String nodeRTId) throws Exception {
		return ((AbstractActorRTDAO) getDAO()).queryByForeignKey("NODERT_ID", nodeRTId);
	}

	public void create(Collection<ActorRT> actorrts) throws Exception {
		((ActorRTDAO) getDAO()).create(actorrts);
	}
}
