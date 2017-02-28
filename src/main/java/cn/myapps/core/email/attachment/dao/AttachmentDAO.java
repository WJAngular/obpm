package cn.myapps.core.email.attachment.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.email.attachment.ejb.Attachment;
import cn.myapps.core.email.email.ejb.Email;

public interface AttachmentDAO extends IDesignTimeDAO<Attachment> {

	public Collection<Attachment> queryAttachmentByEmails(Email email) throws Exception;
	
	public int queryAttachmentCountByEmail(Email email) throws Exception;
	
}
