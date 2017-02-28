package cn.myapps.core.personalmessage.attachment.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.personalmessage.attachment.ejb.PM_AttachmentVO;

public class HibernatePM_AttachmentDAO extends HibernateBaseDAO<PM_AttachmentVO> implements
		AttachmentDAO {

	public HibernatePM_AttachmentDAO(String voClassName) {
		super(voClassName);
	}

}
