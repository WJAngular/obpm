package cn.myapps.core.email.folder.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.email.email.ejb.EmailUser;

public interface EmailFolderProcess extends IDesignTimeProcess<EmailFolder> {

	public void initCreatDefaultMailFolder() throws Exception;

	public EmailFolder getEmailFolderById(String folderid) throws Exception;

	public EmailFolder getEmailFolderByOwnerId(String folderName, String ownerId) throws Exception;

	/**
	 * 根据文件名称判断该文件是否已创建
	 * 
	 * @param folderName
	 * @param ownerid
	 * @return
	 */
	public boolean emailFolderIsCreate(String folderName, String ownerid) throws Exception;

	public void doCreateEmailFolderByName(String folderName, String ownerId)
			throws Exception;
	
	public Collection<EmailFolder> getSystemEmailFolders() throws Exception;
	
	public EmailFolder getSystemEmailFolderByName(String folderName) throws Exception;
	
	public DataPackage<EmailFolder> getPersonalEmailFolders(EmailUser user, ParamsTable params) throws Exception;

	public void doRemoveEmailFolder(EmailFolder folder, EmailUser user) throws Exception;
	
	public int getPersonalEmailFolderCount(String ownerid) throws Exception;
	
}
