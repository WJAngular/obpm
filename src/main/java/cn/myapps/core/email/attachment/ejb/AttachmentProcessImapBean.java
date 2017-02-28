package cn.myapps.core.email.attachment.ejb;

import java.util.Collection;

import javax.mail.Folder;
import javax.mail.Message;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.email.email.ejb.Email;
import cn.myapps.core.email.folder.ejb.EmailFolder;
import cn.myapps.core.email.runtime.mail.ImapProtocolImpl;
import cn.myapps.core.email.runtime.mail.ProtocolFactory;
import cn.myapps.core.email.runtime.model.EmailPart;
import cn.myapps.core.email.runtime.parser.MessageParser;
import cn.myapps.core.email.util.Constants;
import cn.myapps.util.StringUtil;

public class AttachmentProcessImapBean extends AbstractDesignTimeProcessBean<Attachment>
		implements AttachmentProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3358596562359189962L;
	private transient ProtocolFactory protocolFactory;
	
	public AttachmentProcessImapBean(ProtocolFactory protocolFactory) throws Exception {
		this.protocolFactory = protocolFactory;
	}
	
	protected IDesignTimeDAO<Attachment> getDAO() throws Exception {
		return null;
	}

	public Attachment getAttachment(String emailid, EmailFolder folder, 
			String fileName) throws Exception {
		String folderName = getFolderNameByEmailFolder(folder);
		ImapProtocolImpl protocol = (ImapProtocolImpl) protocolFactory.getImapProtocol(folderName);
		if (protocol != null) {
			protocol.connect(Constants.CONNECT_TYPE_READ_ONLY);
			Message message = protocol.getMessageByUID(Long.parseLong(emailid));
			if (message != null) {
				EmailPart part = MessageParser.parseMessagePart(message, fileName);
				if (part != null) {
					return Attachment.valueOf(part);
				}
			}
		}
		return null;
	}
	
	public String getFolderNameByEmailFolder(EmailFolder folder) {
		String folderName = folder.getName();
		if (StringUtil.isBlank(folderName)) {
			folderName = getFolderNameById(folder.getId());
		}
		return folderName;
	}
	
	public String getFolderNameById(String id) {
		Folder folder = protocolFactory.getConnectionMetaHandler().getFolderByUID(Long.parseLong(id));
		return folder.getFullName();
	}

	public Collection<Attachment> getAttachmentsByEmail(Email email)
			throws Exception {
		return null;
	}

}
