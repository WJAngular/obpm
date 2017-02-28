package cn.myapps.core.personalmessage.dao;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.personalmessage.ejb.MessageBody;

public class HibernateMessageBodyDAO extends HibernateBaseDAO<MessageBody> implements
		MessageBodyDAO {

	public HibernateMessageBodyDAO(String voClassName) {
		super(voClassName);
	}

	public DataPackage<MessageBody> queryTrash(String userId, ParamsTable params) throws Exception {
//		String hql = "from " + _voClazzName + " vo where vo.senderId = '" + userId + "' and vo.trash = true and vo.delete = false order by vo.id";
//		String _currpage = params.getParameterAsString("_currpage");
//		String _pagelines = params.getParameterAsString("_pagelines");
//
//		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
//		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
//		return getDatapackage(hql, params, page, lines);
		return new DataPackage<MessageBody>();
	}
	
}