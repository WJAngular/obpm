package cn.myapps.core.personalmessage.dao;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.personalmessage.ejb.MessageBody;

public interface MessageBodyDAO extends IDesignTimeDAO<MessageBody> {

	public DataPackage<MessageBody> queryTrash(String userId, ParamsTable params) throws Exception;
	
}