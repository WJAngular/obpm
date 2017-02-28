package cn.myapps.core.dynaform.view.ejb;

import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;

public interface ViewType {
	public long countViewDatas(ParamsTable params, WebUser user, Document sDoc) throws Exception;

	/**
	 * 获取视图数据
	 * 
	 * @param params
	 *            参数
	 * @param user
	 *            当前用户
	 * @param sDoc
	 *            查询文档
	 * @return 文档数据包
	 * @throws Exception
	 */
	public DataPackage<Document> getViewDatas(ParamsTable params, WebUser user, Document sDoc) throws Exception;

	public DataPackage<Document> getViewDatasPage(ParamsTable params, int page, int lines, WebUser user, Document sDoc)
			throws Exception;
	public DataPackage<Document> getViewDatas(ParamsTable params, int page, int lines, WebUser user, Document sdoc)
	throws Exception;

	/**
	 * 视图类型int值
	 * 
	 * @return 类型int值
	 */
	public int intValue();

	public Map<String, Column> getColumnMapping();

	public double getSumTotal(ParamsTable params, WebUser user, String fieldName, String _formid, String domainid);
}
