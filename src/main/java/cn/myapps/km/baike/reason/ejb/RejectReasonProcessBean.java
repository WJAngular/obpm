package cn.myapps.km.baike.reason.ejb;

import java.util.Collection;
import java.util.Date;

import cn.myapps.km.baike.reason.dao.RejectReasonDao;
import cn.myapps.km.base.dao.BDaoManager;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.util.sequence.Sequence;

/**
 * @author Able
 * process层，实现业务
 */
public class RejectReasonProcessBean extends AbstractRunTimeProcessBean<RejectReason> implements RejectReasonProcess{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -160006938248239439L;
	
	@Override
	protected NRuntimeDAO getDAO() throws Exception {
		NRuntimeDAO dao =  BDaoManager.getReasonDAO(getConnection());
		return dao;
	}
	
	/**
	 * 当前用户创建词条
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo) throws Exception {
		RejectReason reason = (RejectReason)vo;
		if (reason.getId() == null || reason.getId().trim().length() == 0) {
			reason.setId(Sequence.getSequence());
		}
		reason.setContentId(reason.getContentId());
		reason.setReason(reason.getReason());
		reason.setRejectTime(new Date());
		super.doCreate(reason);
	}
	
	
	public Collection<RejectReason> queryAllReason(String contentId) throws Exception{
		return ((RejectReasonDao)getDAO()).queryAllReason(contentId);
	}
	
}