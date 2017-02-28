package cn.myapps.core.dynaform.form.ejb;

import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.core.table.ddlutil.ChangeLog;

public interface FormTableProcess extends IRunTimeProcess<Form> {
	/**
	 * 创建动态表
	 * 
	 * @param vo
	 *            表单值对象
	 * @param conn
	 *            JDBC Connection
	 * @throws Exception
	 */
	public abstract void createDynaTable(Form newForm) throws Exception;

	/**
	 * 更新动态表
	 * 
	 * @param vo
	 *            表单值对象
	 * @param conn
	 *            JDBC Connection
	 * @throws Exception
	 */
	public abstract void updateDynaTable(Form newForm, Form oldForm) throws Exception;

	/**
	 * 删除动态表
	 * 
	 * @param id
	 *            表单ID
	 * @param conn
	 *            JDBC Connection
	 * @throws Exception
	 */
	public abstract void dropDynaTable(Form oldForm) throws Exception;

	/**
	 * 检查动态表是否已经存在
	 */
	public abstract boolean isDynaTableExists(Form form) throws Exception;

	/**
	 * 同步动态表
	 */
	public abstract void synchronizeDynaTable(Form formVO) throws Exception;

	/**
	 * 在保存或更新时对Form的改变进行校验
	 * 
	 * @throws Exception
	 */
	public void doChangeValidate(ChangeLog log) throws Exception;

	/**
	 * 创建或者更新动态表,如果传入的表单存在就更新表单,否则创建新表单
	 * 
	 * @param newForm
	 *            新表单对象
	 * @param oldForm
	 *            旧表单对象
	 */

	public void createOrUpdateDynaTable(Form newForm, Form oldForm) throws Exception;
}
