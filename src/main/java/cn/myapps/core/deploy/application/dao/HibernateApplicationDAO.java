package cn.myapps.core.deploy.application.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.application.ejb.DomainApplicationSet;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

/**
 * @author nicholas
 */
public class HibernateApplicationDAO extends HibernateBaseDAO<ApplicationVO>
		implements ApplicationDAO {

	/**
	 * @uml.property name="appDomain_Cache"
	 */
	private static Map<String, ApplicationVO> appDomain_Cache = null;

	public HibernateApplicationDAO(String voClassName) {
		super(voClassName);
	}

	public Collection<ApplicationVO> getAllApplication() throws Exception {
		String hql = "FROM " + _voClazzName;
		return getDatas(hql);
	}

	public void create(ValueObject vo) throws Exception {
		try {
			Session session = currentSession();
			session.save(vo);
			refreshCache();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void remove(String id) throws Exception {
		Session session = currentSession();
		ValueObject vo = find(id);
		if (vo != null)
			session.delete(vo);
		refreshCache();
	}

	public void update(ValueObject vo) throws Exception {
		Session session = currentSession();
		session.merge(vo);
		refreshCache();
	}

	public void refreshCache() throws Exception {
		if (appDomain_Cache != null)
			appDomain_Cache.clear();
		else
			appDomain_Cache = new HashMap<String, ApplicationVO>();
		Collection<ApplicationVO> rs = getAllApplication();
		if (rs != null) {
			for (Iterator<ApplicationVO> iter = rs.iterator(); iter.hasNext();) {
				ApplicationVO vo = iter.next();
				if (vo.getDomainName() != null
						&& vo.getDomainName().trim().length() > 0)
					appDomain_Cache.put(vo.getDomainName(), vo);
			}
		}

	}

	/**
	 * @return the appDomain_Cache
	 * @uml.property name="appDomain_Cache"
	 */
	public Map<String, ApplicationVO> getAppDomain_Cache() throws Exception {
		if (appDomain_Cache == null)
			refreshCache();
		return appDomain_Cache;
	}

	/**
	 * @SuppressWarnings Hibernate3.2版本不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public boolean isEmpty() throws Exception {
		String hql = "select count(*) from " + _voClazzName;
		Session session = currentSession();
		Query query = session.createQuery(hql);

		List rst = query.list();

		Long amount;
		if (!rst.isEmpty()) {
			amount = (Long) rst.get(0);
			return amount.intValue() <= 0;
		} else
			return true;

	}

	/**
	 * @SuppressWarnings Hibernate3.2版本不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public Collection<ApplicationVO> queryApplications(String userid, int page,
			int line) throws Exception {
		String hql = "FROM " + _voClazzName + " vo";
		if (userid != null && userid.trim().length() > 0) {
			hql += " WHERE vo.owners.id='" + userid + "'";
		}
		Query query = currentSession().createQuery(hql);
		query.setFirstResult((page - 1) * line);
		query.setMaxResults(line);
		return query.list();
	}

	/**
	 * @SuppressWarnings Hibernate3.2版本不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public Collection<ApplicationVO> queryAppsByDomain(String domainId,
			int page, int line) throws Exception {
		/**
		String hql = "FROM " + _voClazzName + " vo";
		if (domainId != null && domainId.trim().length() > 0) {
			hql += " WHERE vo.domainApplicationSet.domainId='" + domainId
					+ "' AND vo.activated =1";
		}
		Query query = currentSession().createQuery(hql);
		query.setFirstResult((page - 1) * line);
		query.setMaxResults(line);
		return query.list();
		**/
		
		
		String sql = "select vo.* from " + getSchema()
				+ "T_APPLICATION vo where vo.activated =1 and ";
		sql += " vo.ID in (select s.APPLICATIONID from " + getSchema()
				+ "t_application_domain_set s where s.DOMAINID ='" + domainId
				+ "') order by vo.id desc";

		return getDatasBySQL(sql);
	}

	public ApplicationVO findBySIPAppKey(String appKey) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.sipAppkey = '"
				+ appKey + "'";
		return (ApplicationVO) getData(hql);
	}

	public ApplicationVO findByName(String name) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.name = '" + name
				+ "'";
		return (ApplicationVO) getData(hql);
	}
	
	public void deletebyApplication(String applicationId) throws Exception {
		Session session = currentSession();
		String hql="delete from t_summary where  applicationid='" + applicationId + "' ";
		Query query = session.createSQLQuery(hql);
		query.executeUpdate();
		hql="delete from t_view where  applicationid='" + applicationId + "' ";
		query = session.createSQLQuery(hql);
		query.executeUpdate();
		hql="delete from t_link where  applicationid='" + applicationId + "' ";
		query = session.createSQLQuery(hql);
		query.executeUpdate();
		hql="delete from t_billdefi where  applicationid='" + applicationId + "' ";
		query = session.createSQLQuery(hql);
		query.executeUpdate();
		hql="delete from t_crossreport where  applicationid='" + applicationId + "' ";
		query = session.createSQLQuery(hql);
		query.executeUpdate();
		hql="delete from t_printer where  applicationid='" + applicationId + "' ";
		query = session.createSQLQuery(hql);
		query.executeUpdate();
		hql="delete from t_application_superuser_set where  applicationid='" + applicationId + "' ";
		query = session.createSQLQuery(hql);
		query.executeUpdate();
		
	}

	/**
	 * 查询企业域下的软件列表
	 * 
	 * @param domainId
	 *            企业域id
	 * @param page
	 *            当前页数
	 * @param line
	 *            每页记录条数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<ApplicationVO> queryByDomain(String domainId, int page,
			int line) throws Exception {
		String sql = "select vo.* from " + getSchema()
				+ "T_APPLICATION vo where";
		sql += " vo.ID in (select s.APPLICATIONID from " + getSchema()
				+ "t_application_domain_set s where s.DOMAINID ='" + domainId
				+ "')";

		return getDatapackageBySQL(sql, null, page, line);
	}

	/**
	 * 查询没有绑定到指定企业域下的软件列表
	 * @param params
	 * 		参数表
	 * @param domainId
	 * 		企业域id
	 * @param page
	 * 		当前页数
	 * @param line
	 * 		每页记录条数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<ApplicationVO> queryUnBindApplicationsByDomain(ParamsTable params,String domainId, int page, int line) throws Exception{
		String sql = "select vo.* from " + getSchema()
				+ "T_APPLICATION vo where";
		sql += " vo.ID not in (select s.APPLICATIONID from " + getSchema()
				+ "t_application_domain_set s where s.DOMAINID ='" + domainId
				+ "')";

		return getDatapackageBySQL(sql, params, page, line);
	}
	
	/**
	 * 根据域开发者用户获取应用
	 * 
	 * @param developerId
	 *            开发者用户
	 * @return 应用集合
	 * @throws Exception
	 */
	public Collection<ApplicationVO> getApplicationsByDeveloper(String developerId) throws Exception {
		String hql = "FROM " + _voClazzName + " vo";
		hql += " INNER JOIN FETCH vo.owners owners WHERE owners.id = '" + developerId + "' ";
		hql += " AND owners.developer = true";
		hql += " AND owners.superAdmin = false";

		return getDatas(hql);
	}

	/**
	 * 根据域管理员用户获取应用
	 * 
	 * @param domainAdminId
	 *            域管理员用户
	 * @return 应用集合
	 * @throws Exception
	 */
	public Collection<ApplicationVO> getApplicationsByDoaminAdmin(String domainAdminId) throws Exception{
		String hql = "FROM " + _voClazzName + " vo ";
		hql += " INNER JOIN FETCH vo.domains d";
		hql += " INNER JOIN FETCH d.users u";
		hql += " WHERE u.id='" + domainAdminId + "'";

		return getDatas(hql);
	}
	
	public Collection<DomainApplicationSet> getDomainApplicationSetByDomainId(String domainId) throws Exception{
		Collection<DomainApplicationSet> rtn = new ArrayList<DomainApplicationSet>();
		//String hql ="from "+DomainApplicationSet.class.getName()+" vo where vo.domainId='" + domainId + "'";
		String sql ="select * from t_application_domain_set where domainid='" + domainId + "'";
		
		Session session = currentSession();
		//Query query = session.createQuery(hql);
		SQLQuery sqlQuery = session.createSQLQuery(sql).addScalar("APPLICATIONID").addScalar("DOMAINID").addScalar("WEIXINAGENTID");
		
		List list = sqlQuery.list();
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			String applicationId = (String) object[0];
			String domainid = (String) object[1];
			String weixinAgentId = (String) object[2];
			DomainApplicationSet set = new DomainApplicationSet(applicationId,domainid,weixinAgentId);
			rtn.add(set);
		}

		//Collection<DomainApplicationSet> rtn = query.list();

		return rtn;
	}
	
	public void updateDomainApplicationSet(String domainId,String applicationId,String weixinAgentId) throws Exception{
		String sql = "update t_application_domain_set set weixinAgentId=? where domainId=? and applicationId=?";
		
		Session session = currentSession();
		Transaction trans = session.beginTransaction();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setString(0, weixinAgentId);
			sqlQuery.setString(1, domainId);
			sqlQuery.setString(2, applicationId);
			sqlQuery.executeUpdate();
			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			throw e;
		}
		
	}
	
	public void removeDomainApplicationSet(String domainId,String applicationId) throws Exception{
		String sql = "delete from t_application_domain_set where domainId=? and applicationId=?";
		
		Session session = currentSession();
		Transaction trans = session.beginTransaction();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setString(0, domainId);
			sqlQuery.setString(1, applicationId);
			sqlQuery.executeUpdate();
			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			throw e;
		}
		
	}
	
	public void createDomainApplicationSet(DomainApplicationSet domainApplicationSet) throws Exception{
		if(StringUtil.isBlank(domainApplicationSet.getId())){
			domainApplicationSet.setId(Sequence.getSequence());
		}
		Session session = currentSession();
		Transaction trans = session.beginTransaction();
		try {
			session.save(domainApplicationSet);
			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			throw e;
		}
		
	}
	
	public DomainApplicationSet findDomainApplicationSet(String domainId,String applicationId) throws Exception{
		String sql ="select * from t_application_domain_set where domainid=? and applicationid=?";
		
		Session session = currentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql).addScalar("APPLICATIONID").addScalar("DOMAINID").addScalar("WEIXINAGENTID");
		sqlQuery.setString(0, domainId);
		sqlQuery.setString(1, applicationId);
		
		sqlQuery.setMaxResults(1);
		List list = sqlQuery.list();
		
		if(list.isEmpty()) return null;
		
		Object[] object = (Object[]) list.get(0);
		String applicationid = (String) object[0];
		String domainid = (String) object[1];
		String weixinAgentId = (String) object[2];
		DomainApplicationSet set = new DomainApplicationSet(applicationid,domainid,weixinAgentId);

		return set;
	}
	
	public Collection<DomainApplicationSet> queryDomainApplicationSet(String applicationId) throws Exception{
		Collection<DomainApplicationSet> rtn = new HashSet<DomainApplicationSet>();
		String sql ="select * from t_application_domain_set where applicationid=?";
		
		Session session = currentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql).addScalar("APPLICATIONID").addScalar("DOMAINID").addScalar("WEIXINAGENTID");
		sqlQuery.setString(0, applicationId);
		
		//sqlQuery.setMaxResults(1);
		List list = sqlQuery.list();
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			String applicationid = (String) object[0];
			String domainid = (String) object[1];
			String weixinAgentId = (String) object[2];
			DomainApplicationSet set = new DomainApplicationSet(applicationid,domainid,weixinAgentId);
			rtn.add(set);
		}

		return rtn;
	}

}
