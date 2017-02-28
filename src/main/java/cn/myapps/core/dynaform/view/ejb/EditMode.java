package cn.myapps.core.dynaform.view.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;

public interface EditMode {
	/**
	 * 获取查询语句
	 * 
	 * @param params
	 *            参数
	 * @param user
	 *            当前用户
	 * @param sDoc
	 *            查询文档
	 * @return 查询语句
	 */
	public String getQueryString(ParamsTable params, WebUser user, Document sDoc) throws Exception;

	/**
	 * 获取文档数据包
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
	public DataPackage<Document> getDataPackage(ParamsTable params, WebUser user, Document sDoc) throws Exception;

	/**
	 * 获取文档数据包
	 * 
	 * @param params
	 *            参数
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示的行数
	 * @param user
	 *            当前用户
	 * @param sDoc
	 *            查询文档
	 * @return 文档数据包
	 * @throws Exception
	 */
	public DataPackage<Document> getDataPackage(ParamsTable params, int page, int lines, WebUser user, Document sDoc) throws Exception;

	/**
	 * 获取文档总行数
	 * 
	 * @param params
	 *            参数
	 * @param user
	 *            当前用户
	 * @param sDoc
	 *            查询文档
	 * @return 文档总行数
	 * @throws Exception
	 */
	public long count(ParamsTable params, WebUser user, Document sDoc) throws Exception;

	/**
	 * 添加查询条件(暂时只能处理字符串条件)
	 * 
	 * @param name
	 *            名称
	 * @param val
	 *            值
	 */
	public void addCondition(String name, String val);
	
	public void addCondition(String name, String val, String operator);

	/**
	 * 设置所查所有数据总计
	 * @param params
	 * @param user
	 * @param fieldName
	 * @param _formid
	 * @param domainid
	 * @return
	 */
	public double getSumTotal(ParamsTable params, WebUser user, String fieldName, String _formid, String domainid);
}
