package cn.myapps.core.email.email.dao;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.email.email.ejb.Email;
import cn.myapps.core.email.email.ejb.EmailUser;

public interface EmailDAO extends IDesignTimeDAO<Email> {

	public DataPackage<Email> queryMessageByFolderId(String folderid, ParamsTable params) throws Exception;
	
	public void updateRead(String emailId) throws Exception;
	
	public void moveToOtherFolder(Email email, String otherFolderId) throws Exception;
	
	public void updateMarkRead(String[] ids) throws Exception;
	
	public int queryUnreadMessageCount(String folderid, EmailUser user) throws Exception;
	
	public int queryEmailCount(String folderid, EmailUser user) throws Exception;
	
	public void doRemoveEmailByEmailUser(String emailUserId) throws Exception;
}
