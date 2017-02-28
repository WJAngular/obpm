package cn.myapps.core.usergroup.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.usergroup.ejb.UserGroupSet;
import cn.myapps.util.StringUtil;

public class HibernateUserGroupSetDAO extends HibernateBaseDAO<UserGroupSet> implements UserGroupSetDAO{
	public HibernateUserGroupSetDAO(String voClassName) {
		super(voClassName);
	}

	public boolean isUserInThisGroup(String userid, String UserGroupId)
			throws Exception {
		String hql = "SELECT COUNT(*) FROM " + _voClazzName + " vo" + " WHERE vo.userId=? AND vo.userGroupId=?";
		
		Query query = currentSession().createQuery(hql);
		query.setParameter(0, userid);
		query.setParameter(1, UserGroupId);
		
		Number num = (Number) query.uniqueResult();
		int count = num.intValue();
		if(count > 0){
			return true;
		}
		return false;
	}

	public DataPackage<UserVO> getUserByGroup(String userGroupId, ParamsTable params)
			throws Exception {
		String hql = "SELECT U FROM cn.myapps.core.user.ejb.UserVO U WHERE U.dimission=1 AND U.id in (SELECT G.userId FROM " + _voClazzName + " G WHERE G.userGroupId=?)";
		
		String sm_name = params.getParameterAsString("sm_name");
		if(!StringUtil.isBlank(sm_name)){
			hql += " AND U.name like '%" + sm_name + "%'";
		}
		String order = params.getParameterAsString("_orderby");
		if(!StringUtil.isBlank(order)){
			hql += " order by "+order;
		}
		
		String _currpage = params.getParameterAsString("_currpage");
		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
		
		Query query = currentSession().createQuery(hql);
		query.setParameter(0, userGroupId);
		query.setFirstResult((page - 1) * 15);
		query.setMaxResults(15);
		
		DataPackage<UserVO> result = new DataPackage<UserVO>();
		result.rowCount = getTotalLines4Contacts(userGroupId);
		result.pageNo = page;
		result.linesPerPage = 15;

		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}

		result.datas = query.list();
		return result;
	}
	
	private int getTotalLines4Contacts(String userGroupId) throws Exception{
		Session session = currentSession();
		String newhql = "SELECT COUNT(*) FROM cn.myapps.core.user.ejb.UserVO U WHERE U.id in (SELECT G.userId FROM " + _voClazzName + " G WHERE G.userGroupId=?)";

		Query query = session.createQuery(newhql);
		query.setParameter(0, userGroupId);

		Number num = (Number) query.uniqueResult();
		int count = num.intValue();
		return count;
	}

	public void deleteByUserGroupId(String userGroupId) throws Exception {
		String hql= "DELETE FROM " + _voClazzName + " vo WHERE vo.userGroupId=?";
		
		Query query = currentSession().createQuery(hql);
		query.setParameter(0, userGroupId);
		
		query.executeUpdate();
	}

	public void deleteByUser(String[] userid, String userGroupId)
			throws Exception {
		List<String> userids = Arrays.asList(userid);
		String hql = "DELETE FROM " + _voClazzName + " vo WHERE vo.userGroupId=? AND vo.userId in (:userids)";
		
		Query query = currentSession().createQuery(hql);
		query.setParameter(0, userGroupId);
		query.setParameterList("userids", userids);
		
		query.executeUpdate();
	}
}
