package cn.myapps.core.email.email.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;

public interface EmailUserProcess extends IDesignTimeProcess<EmailUser> {

	public EmailUser getEmailUserByAccount(String account) throws Exception;
	
	public EmailUser getEmailUser(String account, String domainid) throws Exception;
	
	public DataPackage<EmailUser> getEmailUsers(String domainid, ParamsTable params) throws Exception;
	
	public EmailUser getEmailUserByOwner(String ownerid, String domainid) throws Exception;
	
	public void doCreateEmailUser(EmailUser user) throws Exception;
	
	public void doUpdateEmailUser(EmailUser user) throws Exception;
	
	public void doRemoveEmailUser(String id) throws Exception;
	
}
