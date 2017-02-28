package cn.myapps.core.email.attachment.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.email.email.ejb.Email;
import cn.myapps.core.email.folder.ejb.EmailFolder;

public interface AttachmentProcess extends IDesignTimeProcess<Attachment> {

	public Attachment getAttachment(String emailid, EmailFolder folder, String fileName) throws Exception;
	
	public Collection<Attachment> getAttachmentsByEmail(Email email) throws Exception;
	
}
