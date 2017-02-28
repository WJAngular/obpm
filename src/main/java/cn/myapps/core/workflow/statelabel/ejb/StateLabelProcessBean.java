package cn.myapps.core.workflow.statelabel.ejb;

import java.util.Collection;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.workflow.statelabel.dao.StateLabelDAO;

;

public class StateLabelProcessBean extends
		AbstractDesignTimeProcessBean<StateLabel> implements StateLabelProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4652165914002594393L;

	//@SuppressWarnings("unchecked")
	protected IDesignTimeDAO<StateLabel> getDAO() throws Exception {
		return (StateLabelDAO) DAOFactory.getDefaultDAO(StateLabel.class.getName());
	}

	public Collection<StateLabel> doQueryName(String application)
			throws Exception {
		return ((StateLabelDAO) getDAO()).queryName(application);
	}

	public Collection<StateLabel> doQueryByName(String name, String application)
			throws Exception {
		return ((StateLabelDAO) getDAO()).queryByName(name, application);
	}

	public Collection<StateLabel> doQueryState(String application)
			throws Exception {
		return ((StateLabelDAO) getDAO()).queryStates(application);
	}
}
