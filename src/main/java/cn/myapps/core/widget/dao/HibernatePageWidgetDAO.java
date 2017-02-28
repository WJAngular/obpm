package cn.myapps.core.widget.dao;

import java.util.Collection;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.widget.ejb.PageWidget;

/**
 * @author Happy
 *
 */
public class HibernatePageWidgetDAO extends HibernateBaseDAO<PageWidget> implements PageWidgetDAO {

	public HibernatePageWidgetDAO(String valueObjectName) {
		super(valueObjectName);
	}

	public HibernatePageWidgetDAO() {
	}
	
	/**
	 * 根据传入的企业域id获取企业域下所有激活软件的已发布Widget集合
	 * @param domainId
	 * @return
	 * @throws Exception
	 */
	public Collection<PageWidget> queryPublishedWidgetsByDomainId(String domainId) throws Exception {
		String sql  ="select widget.* from t_page_widget widget ," +
				"(" +
				"select da.applicationid from t_application_domain_set da,t_application app " +
					"where da.applicationid = app.id and app.activated =1 and da.domainid='"+domainId+"'" +
				") temp " +
				"where widget.applicationid = temp.applicationid and widget.published = 1 ORDER BY widget.ORDERNO";
		
		return getDatasBySQL(sql);
	}

}
