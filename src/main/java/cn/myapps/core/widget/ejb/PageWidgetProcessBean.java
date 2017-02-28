package cn.myapps.core.widget.ejb;

import java.util.Collection;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.widget.dao.PageWidgetDAO;

/**
 * @author Happy
 *
 */
public class PageWidgetProcessBean extends AbstractDesignTimeProcessBean<PageWidget> implements PageWidgetProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 658247972879997267L;

	public PageWidgetProcessBean() {
	}

	protected IDesignTimeDAO<PageWidget> getDAO() throws Exception {
		return (PageWidgetDAO) DAOFactory.getDefaultDAO(PageWidget.class.getName());
	}

	/**
	 * 根据传入的企业域id获取企业域下所有激活软件的已发布Widget集合
	 * @param domainId
	 * @return
	 * @throws Exception
	 */
	public Collection<PageWidget> doQueryPublishedWidgetsByDomainId(
			String domainId) throws Exception {
		return ((PageWidgetDAO) getDAO()).queryPublishedWidgetsByDomainId(domainId);
	}

}
