package cn.myapps.core.personalmessage.ejb;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.personalmessage.dao.MessageBodyDAO;

public class MessageBodyProcessBean extends AbstractDesignTimeProcessBean<MessageBody> implements MessageBodyProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7045793505461260376L;

	protected IDesignTimeDAO<MessageBody> getDAO() throws Exception {
		return (MessageBodyDAO)DAOFactory.getDefaultDAO(MessageBody.class.getName());
	}

}
