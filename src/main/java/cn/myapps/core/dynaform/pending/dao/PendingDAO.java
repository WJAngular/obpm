package cn.myapps.core.dynaform.pending.dao;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.dao.FlowStateRTDAO;

public interface PendingDAO extends IRuntimeDAO {

	public DataPackage<PendingVO> queryByFilter(ParamsTable params, WebUser user)
			throws Exception;

	public long countByFilter(ParamsTable params, WebUser user)
			throws Exception;

	public FlowStateRTDAO getStateRTDAO();

	public void setStateRTDAO(FlowStateRTDAO stateRTDAO);

	public void remove(ValueObject vo) throws Exception;

    public void removeByDocId(String pk)throws Exception;
    
    public void updateSummaryByDocument(String docId,String summary) throws Exception;
    
	public DataPackage<PendingVO> queryByUser(WebUser user) throws Exception;

    
}
