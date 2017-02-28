package cn.myapps.core.personalmessage.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.user.action.WebUser;

public class HibernatePersonalMessageDAO extends HibernateBaseDAO<PersonalMessageVO> implements
		PersonalMessageDAO {

	public HibernatePersonalMessageDAO(String voClassName) {
		super(voClassName);
	}

	public DataPackage<PersonalMessageVO> queryTrash(String userid, ParamsTable params)
			throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("from ").append(_voClazzName).append(" vo ");
		hql.append("where vo.ownerId= '").append(userid).append("' and vo.trash = true");
		
		String _currpage = params.getParameterAsString("_currpage");
		String _pagelines = params.getParameterAsString("_pagelines");

		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
		return getDatapackage(hql.toString(), params, page, lines);
	}
	
	public int countNewMessages(String userid) throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from ").append(_voClazzName).append(" vo ");
		hql.append("where vo.ownerId ='").append(userid);
	//	hql.append("' and vo.trash = false and vo.read = false and vo.inbox = true");
		hql.append("'  and vo.read = false and vo.inbox = true");

		Session session = currentSession();
		Query query = session.createQuery(hql.toString());
		List<?> list = query.list();
		if (list != null && !list.isEmpty()) {
			return Integer.parseInt(list.get(0).toString());
		}
		return 0;
	}
	
	public int countIsReadMessages(String userid) throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from ").append(_voClazzName).append(" vo ");
		hql.append("where vo.ownerId ='").append(userid);
		//hql.append("' and vo.trash = false and vo.read = true and vo.inbox = true");
		hql.append("'  and vo.read = true and vo.inbox = true");

		Session session = currentSession();
		Query query = session.createQuery(hql.toString());
		List<?> list = query.list();
		if (list != null && !list.isEmpty()) {
			return Integer.parseInt(list.get(0).toString());
		}
		return 0;
	}
	
	public String[] getReceiverUserIdsByMessageBodyId(String bodyId)
			throws Exception {
		String hql = "select vo.receiverId from " + _voClazzName + " vo where BODYID = '" + bodyId + "'";
		Collection<PersonalMessageVO> collection = getDatas(hql);
		if (collection != null && !collection.isEmpty()) {
			String[] result = new String[collection.size()];
			int count = 0;
			for (Iterator<PersonalMessageVO> it = collection.iterator(); it.hasNext(); ) {
				Object vo = it.next();
				result[count ++] = vo.toString();
			}
			return result;
		}
		return new String[0];
	}
	
	/**
	 * 根据时间查找当前用户最新的信息
	 * @param userid
	 * @param date
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryNewMessagWithTime(String userid,String date, ParamsTable params)
			throws Exception {
		
		StringBuffer hql = new StringBuffer();
		hql.append("from ").append(_voClazzName).append(" vo ");
		hql.append("where vo.receiverId= '").append(userid).append("' and vo.sendDate >'").append(date).append("' and vo.trash = false and vo.read = false order by vo.sendDate desc");
		
		Session session = currentSession();
		Query query = session.createQuery(hql.toString());
		List<?> list = query.list();
		if (list != null && !list.isEmpty()) {
			return list.size();
		}
		return 0;
	}

	public DataPackage<PersonalMessageVO> queryNewMessage(String userid, ParamsTable params)
			throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("from ").append(_voClazzName).append(" vo ");
		hql.append("where vo.receiverId= '").append(userid).append("' and vo.trash = false and vo.read = false order by vo.sendDate desc");
		
		String _currpage = params.getParameterAsString("_currpage");
		String _pagelines = params.getParameterAsString("_pagelines");

		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
		return getDatapackage(hql.toString(), params, page, lines);
	}

	public DataPackage<PersonalMessageVO> queryInBox(String userid, ParamsTable params)
			throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("from ").append(_voClazzName).append(" vo ");
		hql.append("where vo.receiverId = '").append(userid).append("'");
		String messageType = params.getParameterAsString("messageType");
		String searchKeyword = StringUtils.defaultString(params.getParameterAsString("keyword"), "");
		if(StringUtils.equals(messageType, "0")) {
			hql.append(" and vo.inbox = true and vo.senderId is null and vo.type='0'");
		} else if(StringUtils.equals(messageType, "1")) {
			hql.append(" and vo.inbox = true and vo.senderId is not null and vo.type='0'");
		} else if(StringUtils.equals(messageType, "2")) {
			hql.append(" and vo.inbox = true  and vo.type='1'");
		} else if(StringUtils.equals(messageType, "3")) {
			hql.append(" and vo.inbox = true  and vo.type='2'");
		} else {
			hql.append(" and vo.inbox = true");
		}
		
		hql.append(" and vo.body.content like '%" + searchKeyword + "%'");
		
		if(params.getParameterAsString("_isRead") != null){
			boolean _isRead = params.getParameterAsBoolean("_isRead");
			if(_isRead){
				hql.append(" and vo.read = true");
			}else{
				hql.append(" and vo.read = false");
			}
		}
		String _currpage = params.getParameterAsString("_currpage");
		String _pagelines = params.getParameterAsString("_pagelines");

		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
		return getDatapackage(hql.toString(), params, page, lines);
	}

	public DataPackage<PersonalMessageVO> queryOutbox(String userid, ParamsTable params)
			throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("from ").append(_voClazzName).append(" vo ");
		hql.append("where vo.ownerId = '").append(userid).append("' and vo.outbox = true and vo.trash = false");
		
		String _currpage = params.getParameterAsString("_currpage");
		String _pagelines = params.getParameterAsString("_pagelines");

		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
		return getDatapackage(hql.toString(), params, page, lines);
	}
	
	
	public ValueObject queryPersonalMessageVO(String senderid,String receiverid ,String bodyId)throws Exception {
		Session session = currentSession();
		ValueObject rtn = null;
		if (senderid != null && senderid.length() > 0) {
			String hql = "FROM " + _voClazzName + " WHERE senderid=? and receiverid=? and bodyid=? and isoutbox=?";
			Query query = session.createQuery(hql);
			query.setParameter(0, senderid);
			query.setParameter(1, receiverid);
			query.setParameter(2, bodyId);
			query.setParameter(3, false);
			query.setFirstResult(0);
			query.setMaxResults(1);

			List result = query.list();

			if (!result.isEmpty()) {
				rtn = (ValueObject) result.get(0);
			}
		}
		return rtn;
	}
	
	/**
	 * 根据用户查询通知公告的集合
	 * @param user
	 * 		用户
	 * @return
	 * 		公告的集合
	 * @throws Exception
	 */
	public Collection<PersonalMessageVO> queryAnnouncementsByUser(WebUser user)throws Exception {
		String hql = "from "+_voClazzName+" vo where vo.type='"+PersonalMessageVO.MESSAGE_TYPE_ANNOUNCEMENT+"' and vo.receiverId = '"+user.getId()+"' and vo.inbox = true order by vo.sendDate desc";
		return getDatas(hql, 1, 10);
		
	}

}