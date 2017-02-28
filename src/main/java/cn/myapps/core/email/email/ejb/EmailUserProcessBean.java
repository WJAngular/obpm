package cn.myapps.core.email.email.ejb;

import java.util.Date;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.email.email.dao.EmailUserDAO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;

public class EmailUserProcessBean extends AbstractDesignTimeProcessBean<EmailUser> implements
		EmailUserProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8921925024606594233L;

	protected IDesignTimeDAO<EmailUser> getDAO() throws Exception {
		return (EmailUserDAO) DAOFactory.getDefaultDAO(EmailUser.class.getName());
	}
	
	public EmailUser getEmailUser(String account, String domainid)
			throws Exception {
		EmailUser user = ((EmailUserDAO)getDAO()).queryEmailUser(account, domainid);
		if (user != null && user.getPassword() != null && user.getPassword().trim().length() > 0) {
			user.setPassword(Security.decodeBASE64(user.getPassword()));
		}
		return user;
	}

	public EmailUser getEmailUserByAccount(String account) throws Exception {
		return ((EmailUserDAO)getDAO()).queryEmailUserByAccount(account);
	}

	public DataPackage<EmailUser> getEmailUsers(String domainid, ParamsTable params)
			throws Exception {
		return ((EmailUserDAO)getDAO()).queryEmailUsers(domainid, params);
	}
	
	public static void main(String[] args) {
		try {
			EmailUserProcess processBean = (EmailUserProcess) ProcessFactory.createProcess(EmailUserProcess.class);
			EmailUser user = new EmailUser();
			user.setAccount("tom");
			user.setName("tom");
			user.setPassword("123456");
			processBean.doCreate(user);
			
			EmailUser user2 = new EmailUser();
			user2.setAccount("tao");
			user2.setName("tao");
			user2.setPassword("123456");
			processBean.doCreate(user2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EmailUser getEmailUserByOwner(String ownerid, String domainid)
			throws Exception {
		EmailUser user = ((EmailUserDAO)getDAO()).queryEmailUserByOwner(ownerid, domainid);
		if (user != null && user.getPassword() != null && user.getPassword().trim().length() > 0) {
			user.setPassword(Security.decodeBASE64(user.getPassword()));
		}
		return user;
	}

	public void doCreateEmailUser(EmailUser user) throws Exception {
		
		this.validateAccount(user);
		
		if (user != null && user.getPassword() != null && user.getPassword().trim().length() > 0) {
			user.setPassword(Security.encodeToBASE64(user.getPassword()));
		}
		super.doCreate(user);
	}
	
	@Override
	public void doCreate(ValueObject vo) throws Exception {
		EmailUser user = (EmailUser) vo;

		this.validateAccount(user);
		
		if (user != null && user.getPassword() != null && user.getPassword().trim().length() > 0) {
			user.setPassword(Security.encodeToBASE64(user.getPassword()));
		}
		user.setCreateDate(new Date());
		super.doCreate(vo);
	}

	public void doRemoveEmailUser(String id) throws Exception {
		super.doRemove(id);
	}

	public void doUpdateEmailUser(EmailUser user) throws Exception {

		this.validateAccount(user);
		
		if (user != null && user.getPassword() != null && user.getPassword().trim().length() > 0) {
			user.setPassword(Security.encodeToBASE64(user.getPassword()));
		}
		super.doUpdate(user);
	}
	
	@Override
	public void doUpdate(ValueObject vo) throws Exception {
		EmailUser user = (EmailUser) vo;
		try {
			PersistenceUtils.beginTransaction();
			
			this.validateAccount(user);
			
			EmailUser po = (EmailUser) getDAO().find(vo.getId());
			if (po != null) {
				if (user.getPassword() != null && user.getPassword().trim().length() > 0) {
					//String base64 = Security.encodeToBASE64(user.getPassword());
					//user.setPassword(base64);
					//po.setPassword(base64);
				} else {
					//user.setPassword(((EmailUser)po).getPassword());
				}
				po.setAccount(user.getAccount());
				//if (Web.DEFAULT_SHOWPASSWORD.equals(user.getPassword())) {
				//	user.setPassword(((EmailUser)po).getPassword());
				//} else {
				//	user.setPassword(Security.encodeToBASE64(user.getPassword()));
				//}
				//PropertyUtils.copyProperties(po, user);
				po.setDefaultDepartment(user.getDefaultDepartment());
				getDAO().update(po);
			} else {
				if (user != null && user.getPassword() != null && user.getPassword().trim().length() > 0) {
					user.setPassword(Security.encodeToBASE64(user.getPassword()));
				}
				getDAO().update(user);
			}

			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 校验邮件用户是否存在
	 * @param emailUser
	 * @return
	 * @throws Exception
	 */
	public void validateAccount(EmailUser emailUser)throws Exception {
		if(StringUtil.isBlank(emailUser.getAccount())){
			// 邮件账号为空不校验
			return;
		}
		EmailUser user = getEmailUser(emailUser.getAccount(), emailUser.getDomainid());
		if(user != null && !user.getOwnerid().equals(emailUser.getOwnerid())){
			// 该邮件账号已经被其他用户创建
			throw new OBPMValidateException(user.getAccount()
					+ "{*[core.email.account.create.error]*}" + " ["
					+ user.getName() + "] "
					+ "{*[core.email.account.change]*}!");
		}
	}

}
