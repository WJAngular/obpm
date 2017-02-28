package cn.myapps.core.email.email.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.email.folder.ejb.EmailFolder;

public interface EmailProcess extends IDesignTimeProcess<Email> {

	public DataPackage<Email> getEmailsByFolderId(String folderid, ParamsTable params) throws Exception;
	
	public DataPackage<Email> getEmailsByFolderUser(String folderid, ParamsTable params, EmailUser user) throws Exception;
	
	public void doUpdateRead(String emailid) throws Exception;
	
	public void doUpdateRead(String emailid, EmailFolder folder) throws Exception;
	
	public void doMoveToOtherFolder(Email email, String otherFolderid) throws Exception;
	
	public void doMoveTo(String[] ids, EmailFolder folder) throws Exception;
	
	public void doMoveTo(String[] ids, EmailFolder folder, EmailFolder other) throws Exception;
	
	public void doUpdateMarkRead(String[] ids, boolean read) throws Exception;
	
	public void doUpdateMarkRead(String[] ids, boolean read, EmailFolder folder) throws Exception;
	
	public void doToRecy(String[] ids) throws Exception;
	
	public void doToRecy(String[] ids, String folderid) throws Exception;
	
	public int getUnreadMessageCount(String folderid, EmailUser user) throws Exception;
	
	/**
	 * 只发送一封邮件
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public boolean sendEmail(Email email, EmailUser user) throws Exception;
	
	public boolean sendEmail(Email email, EmailUser user, boolean self) throws Exception;
	
	public int getEmailCount(EmailFolder folder, EmailUser user) throws Exception;
	
	public Email getEmailByID(String id, EmailFolder folder) throws Exception;
	
	public void doRemoveByFolder(String[] ids, EmailFolder folder) throws Exception;
	
	public void doSaveEmail(Email email, EmailFolder folder) throws Exception;
	
	/**
	 * 根据邮件用户ID删除邮件
	 * @param emailUserIds
	 * @throws Exception
	 */
	public void doRemoveEmailByEmailUser(String emailUserId) throws Exception;
	
}
