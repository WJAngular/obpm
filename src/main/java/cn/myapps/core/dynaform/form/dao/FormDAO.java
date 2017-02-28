// Source file:
// C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\dao\\FormDAO.java

package cn.myapps.core.dynaform.form.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.form.ejb.Form;

/**
 * 
 * @author Marky
 * 
 */
public interface FormDAO<E> extends IDesignTimeDAO<E> {
	/**
	 * 根据表单名以及应用标识查询,返回表单对象.
	 * 
	 * @param formName
	 *            表单名
	 * @param application
	 *            应用标识
	 * @return 表单对象
	 */
	public Form findByFormName(String formName, String application) throws Exception;

	/**
	 * 根据关联名以及应用标识查询,返回表单对象.
	 * 
	 * @param relationName
	 *            关联名
	 * @param application
	 *            应用标识
	 * @return 表单对象
	 */
	public Form findFormByRelationName(String relationName, String application) throws Exception;

	/**
	 * 根据所属模块以及应用标识查询,返回相应表单集合.
	 * 
	 * @param application
	 *            应用标识
	 * @param 所属模块主键
	 * @return 表单集合
	 * @throws Exception
	 */
	public Collection<E> getFormsByModule(String moduleid, String application) throws Exception;

	/**
	 * 根据所属Module以及应用标识, 返回查询表单集合.
	 * 
	 * @param moduleid
	 *            模块主键
	 * @param application
	 *            应用标识
	 * @return Search Form 集合
	 * @throws Exception
	 */
	public Collection<E> getSearchFormsByModule(String moduleid, String application) throws Exception;

	/**
	 * 根据所属Module以及应用标识, 返回表单集合.
	 * 
	 * @param moduleid
	 *            模块主键
	 * @param application
	 *            应用标识
	 * @return Form 集合
	 * @throws Exception
	 */
	public Collection<E> getRelatedFormsByModule(String moduleid, String application) throws Exception;

	/**
	 * 根据应用标识查询,返回相应查询表单集合.
	 * 
	 * @param application
	 *            应用标识
	 * @param appid
	 *            应用标识
	 * @return 查询表单集合
	 * @throws Exception
	 */

	public Collection<E> getSearchFormsByApplication(String appid, String application) throws Exception;

	/**
	 * 根据参数条件以及应用标识查询,返回表单的DataPackage. DataPackage为一个封装类，此类封装了所得到的Form数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @param params
	 *            参数表
	 * @application 应用标识
	 * @return 表单的DataPackage
	 */
	public DataPackage<E> queryForm(ParamsTable params, String application) throws Exception;
	
	/**
	 * 根据模块查找模板表单的集合
	 * @param application
	 * @return
	 * @throws Exception
	 */
	public Collection<E> queryTemplateFormsByModule(String moduleid,String application) throws Exception;
	
	/**
	 * 根据模板获取普通(含映射)表单的集合
	 * @param moduleid
	 * @param application
	 * @return
	 * @throws Exception
	 */
	public abstract Collection<E> queryNormalFormsByModule(String moduleid,String application) throws Exception;
	
	/**
	 * 获取软件下的所有表单的集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * 		表单集合
	 * @throws Exception
	 */
	public Collection<Form> getFormsByApplication(String applicationId) throws Exception;
	
	/**
	 * 获取模块下的所有片段表单
	 * @param moduleid
	 * 	模块id
	 * @param application
	 * 	软件id
	 * @return
	 * 	片段表单集合
	 * @throws Exception
	 */
	public Collection<E> queryFragmentFormsByModule(String moduleid,String application) throws Exception;

}
