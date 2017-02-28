package cn.myapps.core.email.email.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.email.email.ejb.EmailBody;

public class HibernateEmailBodyDAO extends HibernateBaseDAO<EmailBody> implements
		EmailBodyDAO {

	public HibernateEmailBodyDAO(String voClassName) {
		super(voClassName);
	}
}
