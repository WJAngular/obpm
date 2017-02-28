package cn.myapps.core.email.attachment.ejb;

import java.util.Collection;
import java.util.Date;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.email.attachment.dao.AttachmentDAO;
import cn.myapps.core.email.email.ejb.Email;
import cn.myapps.core.email.email.ejb.EmailBody;
import cn.myapps.core.email.email.ejb.EmailBodyProcess;
import cn.myapps.core.email.email.ejb.EmailBodyProcessBean;
import cn.myapps.core.email.email.ejb.EmailProcess;
import cn.myapps.core.email.folder.ejb.EmailFolder;
import cn.myapps.core.email.folder.ejb.EmailFolderProcess;
import cn.myapps.core.email.folder.ejb.EmailFolderProcessBean;
import cn.myapps.core.email.util.AttachmentUtil;
import cn.myapps.util.ProcessFactory;

public class AttachmentProcessBean extends AbstractDesignTimeProcessBean<Attachment> implements
		AttachmentProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1933447807515757290L;

	protected IDesignTimeDAO<Attachment> getDAO() throws Exception {
		return (AttachmentDAO) DAOFactory.getDefaultDAO(Attachment.class.getName());
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(new AttachmentProcessBean().doView(""));
			EmailBodyProcess rp = new EmailBodyProcessBean();
			EmailBody body = new EmailBody();
			body.setContent("----");
			rp.doCreate(body);
			
			System.out.println(rp.doView(body.getId()));
			
			EmailFolderProcess ep = new EmailFolderProcessBean();
			ep.initCreatDefaultMailFolder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Attachment getAttachment(String emailid,
			EmailFolder folder, String fileName) throws Exception {
		EmailProcess process = (EmailProcess) ProcessFactory.createProcess(EmailProcess.class);
		Email email = (Email) process.doView(emailid);
		if (email != null
				&& email.getEmailBody().isMultipart()) {
			for (Attachment attachment : email.getEmailBody().getAttachments()) {
				if (attachment.getRealFileName().equals(fileName)) {
					return attachment;
				}
			}
		}
		return null;
	}
	
	@Override
	public void doCreate(ValueObject vo) throws Exception {
		((Attachment)vo).setCreateDate(new Date());
		super.doCreate(vo);
	}
	
	@Override
	public void doRemove(String pk) throws Exception {
		Attachment attachment = (Attachment) doView(pk);
		if (attachment != null) {
			doRemove(attachment);
		}
	}
	
	@Override
	public void doRemove(ValueObject obj) throws Exception {
		PersistenceUtils.currentSession().clear();//清除session
		super.doRemove(obj);
		AttachmentUtil.removeAttachmentFile(((Attachment)obj).getFileName());
	}

	public Collection<Attachment> getAttachmentsByEmail(Email email)
			throws Exception {
		return ((AttachmentDAO)getDAO()).queryAttachmentByEmails(email);
	}

}
