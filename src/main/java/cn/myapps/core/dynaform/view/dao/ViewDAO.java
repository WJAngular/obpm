package cn.myapps.core.dynaform.view.dao;

import java.util.Collection;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.view.ejb.View;

public interface ViewDAO extends IDesignTimeDAO<View> {
	/**
	 * 根据视图名与应用标识查询,返回视图对象
	 * 
	 * @param name
	 *            视图名
	 * @param application
	 *            应用标识
	 * @return 视图对象
	 * @throws Exception
	 */
	public View findViewByName(String name, String application)
			throws Exception;
	
	/**
	 * 根据视图名与模块标识查询,返回是否视图对象
	 * 
	 * @param name
	 *            视图名
	 * @param module
	 *            模块标识
	 * @return 是否视图对象
	 * @throws Exception
	 */
	public boolean existViewByNameModule(String name, String module)
			throws Exception;

	/**
	 * 根据模块主键与应用标识查询，返回视图的DataPackage
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @param moduleid
	 *            模块主键
	 * @param application
	 *            应用标识
	 * @return 视图的DataPackage
	 * @throws Exception
	 */

	public DataPackage<View> getViewsByModuleId(String moduleid, String application)
			throws Exception;

	/**
	 * 根据模块主键与应用标识查询，返回视图(view)的集合
	 * 
	 * @param moduleid
	 *            模块主键
	 * @param application
	 *            应用标识
	 * @return 视图(view)的集合
	 * @throws Exception
	 */

	public Collection<View> getViewByModule(String moduleid, String application)
			throws Exception;
	
	public String findViewNameById(String viewid) throws Exception;
	
	/**
	 * 获取软件下的所有视图(view)集合
	 * @param application
	 *		 软件ID
	 * @return
	 * 		 视图(view)集合
	 * @throws Exception
	 */
	public Collection<View> getViewsByApplication(String applicationId)throws Exception;
	
}
