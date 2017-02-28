package cn.myapps.core.personalmessage.attachment.ejb;

import java.util.Date;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.personalmessage.attachment.action.AttachmentUtil;
import cn.myapps.core.personalmessage.attachment.dao.AttachmentDAO;


public class AttachmentProcessBean extends AbstractDesignTimeProcessBean<PM_AttachmentVO> implements
		AttachmentProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1933447807515757290L;

	protected IDesignTimeDAO<PM_AttachmentVO> getDAO() throws Exception {
		return (AttachmentDAO) DAOFactory.getDefaultDAO(PM_AttachmentVO.class.getName());
	}
	
	@Override
	public void doCreate(ValueObject vo) throws Exception {
		((PM_AttachmentVO)vo).setCreateDate(new Date());
		super.doCreate(vo);
	}
	
	@Override
	public void doRemove(String pk) throws Exception {
		PM_AttachmentVO attachment = (PM_AttachmentVO) doView(pk);
		if (attachment != null) {
			doRemove(attachment);
		}
	}
	
	@Override
	public void doRemove(ValueObject obj) throws Exception {
		super.doRemove(obj);
		AttachmentUtil.removeAttachmentFile(((PM_AttachmentVO)obj).getFileName());
	}

	public PM_AttachmentVO getAttachment(String id) throws Exception {
		
		return (PM_AttachmentVO) super.doView(id);
	}


}
