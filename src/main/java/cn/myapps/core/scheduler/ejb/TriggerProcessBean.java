package cn.myapps.core.scheduler.ejb;



import java.util.Collection;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.scheduler.dao.TriggerDAO;

public class TriggerProcessBean extends AbstractDesignTimeProcessBean<TriggerVO> implements TriggerProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5134365906764414191L;

	protected IDesignTimeDAO<TriggerVO> getDAO() throws Exception {
		return (TriggerDAO) DAOFactory.getDefaultDAO(TriggerVO.class.getName());
	}

	public Collection<TriggerVO> getStandbyTrigger(long interval)
			throws Exception {
		return ((TriggerDAO)getDAO()).getStandbyTrigger(interval);
	}

	public boolean isCancel(String id) throws Exception {
		return ((TriggerDAO)getDAO()).isCancel(id);
	}

	public void updateStandbyState2WaitingState() throws Exception {
		 ((TriggerDAO)getDAO()).updateStandbyState2WaitingState();
	}

	public void removeByToken(String token) throws Exception {
		 ((TriggerDAO)getDAO()).removeByToken(token);
	}
}
