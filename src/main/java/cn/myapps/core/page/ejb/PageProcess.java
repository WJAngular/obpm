package cn.myapps.core.page.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.form.ejb.BaseFormProcess;

public interface PageProcess extends BaseFormProcess<Page> {
	/**
	 * 获取应用的默认页
	 * 
	 * @param application
	 *            应用标识
	 * @return 页
	 * @throws Exception
	 */
	public Page getDefaultPage(String application) throws Exception;

	/**
	 * 根据页名称的和应用标识查询页
	 * 
	 * @param name
	 *            页名称
	 * @param application
	 *            应用标识
	 * @return 页
	 * @throws Exception
	 */
	public Page doViewByName(String name, String application) throws Exception;

	/**
	 * 根据参数查询页,并对页的记录给予分页, 分布类(cn.myapps.base.dao,DataPackage)
	 * 
	 * @param params
	 *            参数
	 * @param application
	 *            应用标识
	 * @return 数据集合()
	 * @throws Exception
	 */
	public DataPackage<Page> doListExcludeMod(ParamsTable params, String application) throws Exception;

	/**
	 * 获取应用所属应用的下的所有页
	 * 
	 * @param application
	 *            应用标识
	 * @return 页集合
	 * @throws Exception
	 */
	public Collection<Page> getPagesByApplication(String application) throws Exception;
}
