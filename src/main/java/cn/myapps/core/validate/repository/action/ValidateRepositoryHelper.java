package cn.myapps.core.validate.repository.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.myapps.core.dynaform.form.ejb.FormfieldValidateScript;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryProcess;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryVO;
import cn.myapps.util.ProcessFactory;

public class ValidateRepositoryHelper {

	/**
	 * 取得该模块及其下属模块,还有所属应用,还有不属于任何应用及模块的所有validate
	 * 
	 * @return validate集合
	 * @throws Exception
	 */
	public Map<String, String> get_validate(String applicationid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> validateMap = FormfieldValidateScript.getValidateScripts();
		ValidateRepositoryProcess sp = (ValidateRepositoryProcess) ProcessFactory
				.createProcess(ValidateRepositoryProcess.class);
		
		//加入系统自带的校验脚本
		for(Iterator it = validateMap.keySet().iterator(); it.hasNext();){
			Object id = (Object)it.next();
			map.put((String)id, "{*[" + (String)id + "]*}");
		}
		
		Collection<ValidateRepositoryVO> coll = sp.get_validate(applicationid);

		for (Iterator<ValidateRepositoryVO> iter = coll.iterator(); iter.hasNext();) {
			ValidateRepositoryVO em = (ValidateRepositoryVO) iter.next();
			map.put(em.getId(), em.getName());
		}
		return map;
	}
	
	/**
	 * 获取系统自带的校验脚本
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> get_SysValidateScripts() throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Map<String, String> validateMap = FormfieldValidateScript.getValidateScripts();
		//加入系统自带的校验脚本
		for(Iterator it = validateMap.keySet().iterator(); it.hasNext();){
			Object id = (Object)it.next();
			map.put((String)id, "{*[" + (String)id + "]*}");
		}
		return map;
	}
	
	/**
	 * 获取校验库的校验脚本
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> get_ValidateScriptsLib(String applicationid) throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		ValidateRepositoryProcess sp = (ValidateRepositoryProcess) ProcessFactory
				.createProcess(ValidateRepositoryProcess.class);
		Collection<ValidateRepositoryVO> coll = sp.get_validate(applicationid);

		for (Iterator<ValidateRepositoryVO> iter = coll.iterator(); iter.hasNext();) {
			ValidateRepositoryVO em = (ValidateRepositoryVO) iter.next();
			map.put(em.getId(), em.getName());
		}
		return map;
	}

}
