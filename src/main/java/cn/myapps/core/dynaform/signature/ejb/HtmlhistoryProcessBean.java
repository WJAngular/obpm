package cn.myapps.core.dynaform.signature.ejb;

import java.util.List;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.dynaform.signature.dao.HtmlhistoryDAO;

/**
 * 
 * @author Alex
 * 
 */
public class HtmlhistoryProcessBean extends AbstractDesignTimeProcessBean<Htmlhistory>
		implements HtmlhistoryProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -479727172174843733L;

	protected IDesignTimeDAO<Htmlhistory> getDAO() throws Exception {

		return (HtmlhistoryDAO)DAOFactory.getDefaultDAO(Htmlhistory.class.getName());
	}

	public List<Htmlhistory> queryAll() throws Exception {

		return ((HtmlhistoryDAO) getDAO()).queryAll();
	}

	public List<Htmlhistory> queryById(String id) throws Exception {

		return ((HtmlhistoryDAO) getDAO()).queryById(id);
	}

	public void createHtmlhistory(Htmlhistory htmlhistory) throws Exception {
		((HtmlhistoryDAO) getDAO()).createHtmlhistory(htmlhistory);

	}

}
