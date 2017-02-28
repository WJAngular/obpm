package cn.myapps.core.workflow.storage.runtime.proxy.ejb;

import java.util.ArrayList;
import java.util.Collection;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserProcessBean;
import cn.myapps.core.workflow.storage.runtime.intervention.dao.FlowInterventionDAO;
import cn.myapps.core.workflow.storage.runtime.proxy.dao.WorkflowProxyDAO;
import cn.myapps.util.RuntimeDaoManager;

/**
 * @author Happy
 * 
 */
public class WorkflowProxyProcessBean extends AbstractRunTimeProcessBean<WorkflowProxyVO> implements
		WorkflowProxyProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3862446754753478202L;

	public WorkflowProxyProcessBean(String applicationId) {
		super(applicationId);
	}

	@Override
	protected IRuntimeDAO getDAO() throws Exception {
		RuntimeDaoManager runtimeDao = new RuntimeDaoManager();
		WorkflowProxyDAO workflowProxyDAO = (WorkflowProxyDAO) runtimeDao.getWorkflowProxyDAO(getConnection(),
				getApplicationId());
		return workflowProxyDAO;
	}

	public void doRemove(String[] pks) throws Exception {
		if (pks != null) {
			for (int i = 0; i < pks.length; i++) {
				String id = pks[i];
				if (id.endsWith(";"))
					id = id.substring(0, id.length() - 1);
				doRemove(id);
			}
		}

	}

	@Override
	public DataPackage<WorkflowProxyVO> doQuery(ParamsTable params, WebUser user) throws Exception {
		if (getApplicationId() != null) {
			return ((WorkflowProxyDAO) this.getDAO()).queryByFilter(params, user);
		}
		return new DataPackage<WorkflowProxyVO>();
	}

	public Collection<WorkflowProxyVO> getDatasByAgent(WebUser user, String applicationid) throws Exception {
		return ((WorkflowProxyDAO) this.getDAO()).queryByAgent(user, applicationid).datas;
	}

	public boolean onlyCheckOnFlow(WorkflowProxyVO vo) throws Exception {
		long size = ((WorkflowProxyDAO) this.getDAO()).countByFlowAndOwner(vo);
		return size == 0;
	}

	public Collection<BaseUser> getAgentsByOwners(Collection<BaseUser> owners) throws Exception {
		if (owners == null || owners.isEmpty())
			return new ArrayList<BaseUser>();
		Collection<WorkflowProxyVO> list = getDatasByOwners(owners);
		Collection<BaseUser> rtn = new ArrayList<BaseUser>();
		UserProcess userProcess = new UserProcessBean();
		for (WorkflowProxyVO vo : list) {
			String[] agents = vo.getAgents().split(",");
			for (String agentid : agents) {
				rtn.add((BaseUser) userProcess.doView(agentid));
			}
		}
		return rtn;
	}

	public Collection<WorkflowProxyVO> getDatasByOwners(Collection<BaseUser> owners) throws Exception {

		return ((WorkflowProxyDAO) this.getDAO()).queryByOwners(owners).datas;
	}
	
	/**
	 * 判断用户是否代理流程
	 * @param userid
	 * 		当前用户id
	 * @return
	 * @throws Exception
	 */
	public boolean isFlowAgent(String userId) throws Exception{
		return ((WorkflowProxyDAO)getDAO()).isFlowAgent(userId);
	}

}
