package cn.myapps.core.email.email.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.email.email.dao.EmailUserDAO;
import cn.myapps.util.ProcessFactory;

public class EmailUserProcessImapBean extends AbstractDesignTimeProcessBean<EmailUser> implements
		EmailUserProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8921925024606594234L;

	protected IDesignTimeDAO<EmailUser> getDAO() throws Exception {
		return (EmailUserDAO) DAOFactory.getDefaultDAO(EmailUser.class.getName());
	}
	
	public EmailUser getEmailUser(String account, String domainid)
			throws Exception {
		return null;
	}

	public EmailUser getEmailUserByAccount(String account) throws Exception {
		return null;
	}

	public DataPackage<EmailUser> getEmailUsers(String domainid, ParamsTable params)
			throws Exception {
		return null;
	}
	
	public static void main(String[] args) {
		try {
			EmailUserProcess processBean = (EmailUserProcess) ProcessFactory.createProcess(EmailUserProcess.class);
			EmailUser user = new EmailUser();
			user.setAccount("tom");
			user.setName("tom");
			user.setPassword("123456");
			processBean.doCreateEmailUser(user);
			
			EmailUser user2 = new EmailUser();
			user2.setAccount("tao");
			user2.setName("tao");
			user2.setPassword("123456");
			processBean.doCreateEmailUser(user2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EmailUser getEmailUserByOwner(String ownerid, String domainid)
			throws Exception {
		//EmailUser user = new EmailUser();
		//user.setAccount("tom");
		//user.setName("tom");
		//user.setPassword("123456");
		return null;
	}

	public void doCreateEmailUser(EmailUser user) throws Exception {
		super.doUpdate(user);
	}

	public void doRemoveEmailUser(String id) throws Exception {
		
	}

	public void doUpdateEmailUser(EmailUser user) throws Exception {
		
	}

}
