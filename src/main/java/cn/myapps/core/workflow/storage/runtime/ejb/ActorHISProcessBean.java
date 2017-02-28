package cn.myapps.core.workflow.storage.runtime.ejb;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.util.RuntimeDaoManager;

public class ActorHISProcessBean extends AbstractRunTimeProcessBean<ActorHIS>
		implements ActorHISProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6320676638289535048L;

	public ActorHISProcessBean(String applicationId) {
		super(applicationId);
	}

	protected IRuntimeDAO getDAO() throws Exception {
		// return new OracleActorRTDAO(getConnection());
		// ApplicationVO app=getApplicationVO(getApplicationId());

		return new RuntimeDaoManager().getActorRtDAO(getConnection(),
				getApplicationId());
	}
}
