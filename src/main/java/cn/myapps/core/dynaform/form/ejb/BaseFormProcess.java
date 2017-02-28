package cn.myapps.core.dynaform.form.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;

/**
 * 
 * 
 * @author Marky
 * 
 */
public interface BaseFormProcess<E> extends IDesignTimeProcess<E> {

	/**
	 * 创建所属表单
	 * 
	 * @param vo
	 *            值对象
	 * @param user
	 *            用户
	 */
	public abstract void doCreate(ValueObject vo, WebUser user) throws Exception;

	/**
	 * 更新表单
	 * 
	 * @param vo
	 *            表单值对象
	 * @param user
	 *            用户
	 */
	public abstract void doUpdate(ValueObject vo, WebUser user) throws Exception;

	/**
	 * 根据表单名以及应用标识查询,返回表单对象.
	 * 
	 * @param formName
	 *            表单名
	 * @param application
	 *            应用标识
	 * @return 表单对象
	 */

	public abstract Form doViewByFormName(String formName, String application) throws Exception;

	/**
	 * 根据参数条件以及应用标识,返回相应字段集合.
	 * 
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @param params
	 *            参数表
	 * @param application
	 *            应用标识
	 * @return 相应字段集合
	 */
	public abstract Collection<FormField> doGetFields(ParamsTable params, String application) throws Exception;

	/**
	 * 判断是否存在重复字段名称
	 * 
	 * @param form
	 *            表单对象
	 */
	public abstract boolean haveDuplicateFieldNames(Form form) throws Exception;

	/**
	 * 改变表单名
	 * 
	 * @param oldform
	 *            旧表单对象
	 * @param newform
	 *            新表单对象
	 * @param application
	 *            应用标识
	 * @throws Exception
	 */

	public abstract void changeFormName(Form oldform, Form newform, String application) throws Exception;

	/**
	 * 根据应用标识,返回所有表单
	 * 
	 * @param application
	 *            应用标识
	 * @return 所有表单
	 */
	public abstract Collection<E> get_formList(String application) throws Exception;

	/**
	 * 根据所属模块应用标识,返回相应表单
	 * 
	 * @param application
	 *            应用标识
	 * @param moduleid
	 *            所属模块主键
	 * @return 表单集合
	 * @throws Exception
	 */
	public abstract Collection<E> getFormsByModule(String moduleid, String application) throws Exception;

	/**
	 * 根据应用标识,返回相应查询表单
	 * 
	 * @param appid
	 *            应用标识
	 * @param application
	 *            应用标识
	 * @return 查询表单
	 * @throws Exception
	 */

	public abstract Collection<E> getSearchFormsByApplication(String appid, String application) throws Exception;

	/**
	 * 根据所属Module 返回查询表单集合
	 * 
	 * @param moduleid
	 *            模块主键
	 * @param application
	 *            应用标识
	 * @return Search Form 集合
	 * @throws Exception
	 */
	public abstract Collection<E> getSearchFormsByModule(String moduleid, String application) throws Exception;
	
	/**
	 * 根据所属Module 返回普通表单集合
	 * 
	 * @param moduleid
	 *            模块主键
	 * @param application
	 *            应用标识
	 * @return 普通表单集合
	 * @throws Exception
	 */
	public abstract Collection<E> getRelatedFormsByModule(String moduleid, String application) throws Exception;

	/**
	 * 更新表单值对象
	 * 
	 * @param vo
	 *            表单值对象
	 */
	public abstract void doUpdate(ValueObject vo) throws Exception;

	/**
	 * 根据参数条件以及应用标识,返回表单的DataPackage .
	 * <p>
	 * DataPackage为一个封装类，此类封装了所得到的Form数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @param params
	 *            参数表
	 * @param application
	 *            应用标识
	 * @return 表单的DataPackage.
	 */
	public abstract DataPackage<E> doFormList(ParamsTable params, String application) throws Exception;

	/**
	 * 在保存或更新时对Form的改变进行校验
	 * 
	 * @param newForm
	 *            发生改变后的表单
	 * @throws Exception
	 *             部分改变需要用户确认的异常
	 */
	public void doChangeValidate(Form newForm) throws Exception;

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
	 * 验证关联名是否可用.
	 * 
	 * @param relationName
	 *            关联名
	 * 
	 * @return true|false; true:可用; false: 不可用
	 */
	public boolean checkRelationName(String formid, String relationName) throws Exception;

	/**
	 * 删除多个表单对象
	 * @param formList 表单对象集合
	 * @throws Exception
	 */
	public abstract void doRemove(Collection<E> formList)throws Exception;

	
	/**
	 * 根据表单编号来获得该表单信息并生成该表单视图
	 * @param formid 表单编号
	 * @param content 
	 * @return
	 * @throws Exception
	 */
	public abstract Form oneKeyCreateView(String formid) throws Exception;
	
	/**
	 * 同步数据到t_document表中的mapping字段
	 * @param params
	 * @throws Exception
	 */
	public String doSynchronouslyData(ParamsTable params,WebUser user,Form form) throws Exception;

	/**
	 * 修改对应数据的Formname字段
	 * @param obj
	 * @throws Exception
	 */
	public void doDocumentFormName(ValueObject obj) throws Exception;
	
	/**
	 * 清除表单数据
	 * @param obj
	 * @throws Exception
	 */
	public void doClearFormData(ValueObject obj) throws Exception;
	
	/**
	 * 清楚
	 * @param from
	 * @param fields
	 * @throws Exception
	 */
	public void doClearColumnData(Form from, String[] fields) throws Exception;
	
	/**
	 * 根据模块获取模板表单的集合
	 * @param moduleid
	 * @param application
	 * @return
	 * @throws Exception
	 */
	public abstract Collection<E> getTemplateFormsByModule(String moduleid,String application) throws Exception;
	
	/**
	 * 根据模板获取普通(含映射)表单的集合
	 * @param moduleid
	 * @param application
	 * @return
	 * @throws Exception
	 */
	public abstract Collection<Form> getNormalFormsByModule(String moduleid,String application) throws Exception;
	
	/**
	 * 获取软件下的所有表单的集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * 		表单集合
	 * @throws Exception
	 */
	public Collection<Form> getFormsByApplication(String applicationId) throws Exception;
	
}
