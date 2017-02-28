package cn.myapps.core.email.email.ejb;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.email.email.dao.EmailBodyDAO;
import cn.myapps.core.email.util.Constants;

public class EmailBodyProcessBean extends AbstractDesignTimeProcessBean<EmailBody> implements
		EmailBodyProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6863199658668569708L;

	protected IDesignTimeDAO<EmailBody> getDAO() throws Exception {
		return (EmailBodyDAO) DAOFactory.getDefaultDAO(EmailBody.class.getName());
	}
	
	@Override
	public void doCreate(ValueObject vo) throws Exception {
		EmailBody body = (EmailBody) vo;
		checkAddress(body);
		super.doCreate(body);
	}
	
	private void checkAddress(EmailBody emailBody) {
		//if (EmailConfig.isInternalEmail()) {
			
		//}
		if (emailBody == null) {
			return;
		}
		emailBody.setFrom(Constants.emailAddress2Account(emailBody.getFrom()));
		emailBody.setBcc(changeAddress(emailBody.getBcc()));
		emailBody.setCc(changeAddress(emailBody.getCc()));
		emailBody.setTo(changeAddress(emailBody.getTo()));
	}
	
	private String changeAddress(String address) {
		if (address == null) {
			return null;
		}
		String[] addresss = address.split(";");
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < addresss.length; i++) {
			if (buffer.length() > 1) {
				buffer.append(";").append(Constants.emailAddress2Account(addresss[i]));
			} else {
				buffer.append(Constants.emailAddress2Account(addresss[i]));
			}
		}
		return buffer.toString();
	}

	@Override
	public void doUpdate(ValueObject vo) throws Exception {
		EmailBody body = (EmailBody) vo;
		checkAddress(body);
		super.doUpdate(body);
	}

}
