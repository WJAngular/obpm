package cn.myapps.core.email.email.dao;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.email.email.ejb.EmailUser;

public interface EmailUserDAO extends IDesignTimeDAO<EmailUser> {

	public EmailUser queryEmailUserByAccount(String account) throws Exception;
	
	public EmailUser queryEmailUser(String account, String domainid) throws Exception;
	
	public DataPackage<EmailUser> queryEmailUsers(String domainid, ParamsTable params) throws Exception;
	
	public EmailUser queryEmailUserByOwner(String ownerid, String domainid) throws Exception;
	
}
