package cn.myapps.km.category.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CategoryHelper {
	
	
	public Collection<Category> getRootCategory(String domainId) throws Exception{
		return getSubCategory("", domainId);
	}
	
	public Map<String, String> getRootCategoryMap(String domainId) throws Exception{
		Collection<Category> list = getSubCategory("", domainId);
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		Iterator<Category> it = list.iterator();
		while (it.hasNext()) {
			Category category = (Category) it.next();
			map.put(category.getId(), category.getName());
		}
		return map;
	}
	
	public Collection<Category> getSubCategory(String parentId,String domainId) throws Exception{
		Collection<Category> list = new ArrayList<Category>();
		CategoryProcess process = new CategoryProcessBean();
		try {
			list.addAll(process.doQuerySubCategory(parentId, domainId));
		} catch (Exception e) {
			throw e;
		}
		
		
		return list;
		
	}
	
	/**
	 * 根据模块Id查出该模块下的所有表单
	 * 
	 * @param moduleId
	 * @return map
	 * @throws Exception
	 */
	public Map<String, String> getSubCategoryMap(String parentId,String domainId) throws Exception {
		CategoryProcess process = new CategoryProcessBean();
		Collection<Category> list = process.doQuerySubCategory(parentId, domainId);
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		Iterator<Category> it = list.iterator();
		while (it.hasNext()) {
			Category category = (Category) it.next();
			map.put(category.getId(), category.getName());
		}
		return map;
	}
	
	public String createSubCategoryOption(String parentId,String domainId,String selectField,String def) throws Exception {
		LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) getSubCategoryMap(parentId, domainId);
		return createOptions(map, selectField, def);
	}
	
	private String createOptions(Map<String, String> map, String selectFieldName, String def) {
		StringBuffer fun = new StringBuffer();
		fun.append("new Function(\"");
		fun.append(createOptionScript(map, selectFieldName, def));
		fun.append("\")");

		return fun.toString();
	}
	
	private String createOptionScript(Map<String, String> map, String selectFieldName, String def) {
		StringBuffer fun = new StringBuffer();
		fun.append("var menuTemp=document.getElementById('" + selectFieldName + "');");
		fun.append("for (var m = menuTemp.options.length - 1; m >= 0; m--) {menuTemp.options[m] = null;}");

		int i = 0;

		for (Iterator<Entry<String, String>> iter = map.entrySet().iterator(); iter.hasNext(); i++) {
			//Object key = iter.next();
			Map.Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String value = entry.getValue();

			fun.append("menuTemp.options[" + i + "] = new Option('" + value + "', '" + key + "');");

			if (key.equals(def)) {
				fun.append("menuTemp.options[" + i + "].selected = true;");
			}
		}

		return fun.toString();
	}

}
