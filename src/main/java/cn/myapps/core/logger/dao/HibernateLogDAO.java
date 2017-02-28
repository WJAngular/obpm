package cn.myapps.core.logger.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.logger.ejb.LogVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public class HibernateLogDAO extends HibernateBaseDAO<LogVO> implements LogDAO {

	public HibernateLogDAO(String valueObjectName) {
		super(valueObjectName);
	}

	public DataPackage<LogVO> queryLog(ParamsTable params, WebUser user)
			throws Exception {
		return query(params, user);
	}

	public DataPackage<LogVO> queryLog(ParamsTable params, WebUser user, String domain)
			throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM ").append(_voClazzName).append(" vo");
		//if (user.isDomainAdmin()) {
			hql.append(" WHERE (").append("vo.domainid = '").append(domain + "'");
			//hql.append(" OR ").append("vo.superUser = '").append(user.getId() + "')");
			hql.append(")");
		//}
		String operator = params.getParameterAsString("sm_operator");
		String date = params.getParameterAsString("sm_date");
		String ip = params.getParameterAsString("sm_ip");
		if (!StringUtil.isBlank(operator)) {
			if (hql.indexOf("WHERE") >= 0) {
				hql.append(" AND");
			} else {
				hql.append(" WHERE");
			}
			hql.append(" (vo.operator LIKE '%").append(operator.trim()).append("%')");
		}
		if (!StringUtil.isBlank(ip)) {
			if (hql.indexOf("WHERE") >= 0) {
				hql.append(" AND");
			} else {
				hql.append(" WHERE");
			}
			hql.append(" (vo.ip LIKE '%").append(ip.trim()).append("%')");
		}
            if (!StringUtil.isBlank(date)) {
			
			if (hql.indexOf("WHERE") >= 0) {
				hql.append(" AND");
			} else {
				hql.append(" WHERE");
			}
			Configuration cfg = new Configuration().configure();
			// 获取hibernate方言
			String dialect = cfg.getProperty("hibernate.dialect");
			if (dialect.equals("org.hibernate.dialect.OracleDialect")) {
			hql.append(" ( to_char(vo.date,'YYYY-MM-DD')").append("='"+date+"')");
			}else if(dialect.equals("cn.myapps.util.dialect.SQLServerUnicodeDialect")){
				hql.append(" ( convert(varchar(10),vo.date, 23) = '").append(date.trim()).append("')");
			}
			else{
				hql.append(" (vo.date LIKE '%").append(date.trim()).append("%')");
	
			}

		} 
		
		
		hql.append(" ORDER BY vo.date DESC");
		String _currpage = params.getParameterAsString("_currpage");
		String _pagelines = params.getParameterAsString("_pagelines");

		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
		return getDatapackage(hql.toString(), page, lines);
	}
	
	public void deleteLogsByUser(String userid) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append("DELETE FROM ").append(_voClazzName).append(" vo ");
		hql.append("WHERE vo.user.id=?");
		
		try {
			PersistenceUtils.beginTransaction();
			Session session = currentSession();
			Query query = session.createQuery(hql.toString());
			query.setParameter(0, userid);
			query.executeUpdate();
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
		}
	}
	
}
