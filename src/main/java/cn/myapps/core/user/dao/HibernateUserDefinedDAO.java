package cn.myapps.core.user.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;

public class HibernateUserDefinedDAO extends HibernateBaseDAO<UserDefined> implements UserDefinedDAO{
	public HibernateUserDefinedDAO(String voClassName) {
		super(voClassName);
	}

	public Collection<UserDefined> findByApplication(String applicationId) throws Exception {
		String hql = "from " + this._voClazzName + " vo where vo.applicationid = '" + applicationId + "'";
		return getDatas(hql);
	}

	public int queryCountByName(String name, String applicationid)
			throws Exception {
		String hql = "FROM " + _voClazzName + " WHERE name='" + name + "' and applicationid='" + applicationid + "'";
		return getTotalLines(hql);
	}

	
	@SuppressWarnings("unchecked")
	public UserDefined login(String name) throws Exception {
	
		Session session = currentSession();
		Criteria criteria = session.createCriteria(this._voClazzName);
		criteria = criteria.add(Expression.like("name", name).ignoreCase());
		List<UserDefined> list = criteria.list();
		if (list.isEmpty()) {
			return null;
		} else {
			return (UserDefined) list.get(0);
		}
	}
	
	/**
	 * 获取用户自定义的首页
	 * @param user
	 * 		当前登录用户
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws Exception
	 */
	public UserDefined findMyCustomUserDefined(WebUser user) throws Exception{
		String hql = "from " + this._voClazzName + " vo where vo.userId='"+user.getId()+"'";
		return (UserDefined) getData(hql);
	}
	
	/**
	 * 根据传入的软件id获取软件下的所有已发布的首页集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws Exception
	 */
	public Collection<UserDefined> queryPublishedUserDefinedsByApplication(String applicationId) throws Exception{
		String hql = "from " + this._voClazzName + " vo where vo.applicationid = '" + applicationId + "' and vo.published =true";
		return getDatas(hql);
	}
	
}
