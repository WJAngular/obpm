package cn.myapps.core.email.folder.dao;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.email.folder.ejb.EmailFolder;

public interface EmailFolderDAO extends IDesignTimeDAO<EmailFolder> {

	public EmailFolder queryMailFolderById(String folderid) throws Exception;
	
	public EmailFolder queryMailFolderByOwnerId(String folderName, String ownerId) throws Exception;
	
	/**
	 * 根据文件名称判断该文件是否已创建
	 * @param folderName
	 * @return
	 */
	public boolean judgeMailFolderIsCreate(String folderName, String ownerid) throws Exception;
	
	public int queryPersonalEmailFolderCount(String ownerid) throws Exception;
	
	public DataPackage<EmailFolder> queryPersonalEmailFolders(String ownerid, ParamsTable params) throws Exception;
	
}
