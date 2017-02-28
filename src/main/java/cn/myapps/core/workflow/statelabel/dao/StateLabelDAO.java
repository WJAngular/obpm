package cn.myapps.core.workflow.statelabel.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.workflow.statelabel.ejb.StateLabel;

public interface StateLabelDAO extends IDesignTimeDAO<StateLabel> {
	public Collection<StateLabel> queryByName(String name, String application)
			throws Exception;

	public Collection<StateLabel> queryName(String application)
			throws Exception;

	public Collection<StateLabel> queryStates(String application)
			throws Exception;
}
