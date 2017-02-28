package cn.myapps.core.dynaform.form.dao;

import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.table.ddlutil.ChangeLog;

public interface FormTableDAO {

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
	
	public abstract void updateDynaTable(Form newForm, Form oldForm, DataSource dt) throws Exception;

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
	
	public abstract void dropDynaTable(Form oldForm, DataSource dt) throws Exception;

	public abstract boolean isDynaTableExists(Form form) throws Exception;

	public abstract void synchronizeDynaTable(Form formVO) throws Exception;

	public void createOrUpdateDynaTable(Form newForm, Form oldForm) throws Exception;

	public void changeValidate(ChangeLog log) throws Exception;
}
