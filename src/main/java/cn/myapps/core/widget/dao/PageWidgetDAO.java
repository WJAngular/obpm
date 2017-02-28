package cn.myapps.core.widget.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.widget.ejb.PageWidget;

public interface PageWidgetDAO extends IDesignTimeDAO<PageWidget> {
	
	/**
	 * 根据传入的企业域id获取企业域下所有激活软件的已发布Widget集合
	 * @param domainId
	 * @return
	 * @throws Exception
	 */
	public Collection<PageWidget> queryPublishedWidgetsByDomainId(String domainId) throws Exception;

}
