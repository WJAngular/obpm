package cn.myapps.core.dynaform.form.ejb;

import java.util.Collection;


/**
 * 
 * 详细请查看BaseFormProcess
 * @author Nicholas
 * 
 */
public interface FormProcess extends BaseFormProcess<Form> {
	
	/**
	 * 获取模块下的所有片段表单集合
	 * @param moduleid
	 * 	模块id
	 * @param application
	 * 	软件id
	 * @return
	 * 片段表单集合
	 * @throws Exception
	 */
	public Collection<Form> getFragmentFormsByModule(String moduleid,String application) throws Exception;
	
}
