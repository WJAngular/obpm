package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.dao.CirculatorDAO;
import cn.myapps.util.RuntimeDaoManager;

/**
 * @author happy
 *
 */
public class CirculatorProcessBean extends AbstractRunTimeProcessBean<Circulator>
		implements CirculatorProcess {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2438437972568735973L;

	public CirculatorProcessBean(String applicationId) {
		super(applicationId);
	}

	@Override
	protected IRuntimeDAO getDAO() throws Exception {
		return new RuntimeDaoManager().getCirculatorDAO(getConnection(),
				getApplicationId());
	}
	
	/**
	 * 根据NODERT_ID查询抄送人
	 * @return
	 * @throws Exception
	 */
	public Collection<Circulator> doQueryByNodeRtId(String id) throws Exception {
		return ((CirculatorDAO)this.getDAO()).queryByForeignKey("NODERT_ID", id);
	}

	public DataPackage<Circulator> getPendingByUser(ParamsTable params,WebUser user)
			throws Exception {
		return ((CirculatorDAO)this.getDAO()).queryPendingByUser(params, user);
	}

	public DataPackage<Circulator> getWorksByUser(ParamsTable params,
			WebUser user) throws Exception {
		return ((CirculatorDAO)this.getDAO()).queryWorksByUser(params, user);
	}

	public Circulator findByCurrDoc(String docId, String flowStateId,
			boolean isRead, WebUser user) throws Exception {
		return ((CirculatorDAO)this.getDAO()).findByCurrDoc(docId, flowStateId, isRead, user);
	}

	public void doRemoveByForeignKey(String key, Object val) throws Exception {
		((CirculatorDAO)this.getDAO()).removeByForeignKey(key, val);
	}

	public long conutByFilter(ParamsTable params, WebUser user) throws Exception {
		CirculatorDAO circulatorDAO = (CirculatorDAO) getDAO();
		return circulatorDAO.countByFilter(params, user);
	}

}
