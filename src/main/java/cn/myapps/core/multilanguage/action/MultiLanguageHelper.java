package cn.myapps.core.multilanguage.action;

import java.util.Map;
import java.util.TreeMap;

import cn.myapps.core.multilanguage.ejb.LanguageType;
import cn.myapps.util.property.MultiLanguageProperty;

public class MultiLanguageHelper {
	public Map<Integer, String> getTypeList() {
		Map<Integer, String> rtn = new TreeMap<Integer, String>();
		try {
			int[] types = LanguageType.TYPES;
			String[] names = LanguageType.NAMES;
	 		
			for (int i = 0; i < types.length; i++) {
				rtn.put(Integer.valueOf(types[i]) , names[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}

	public String getTypeName(int type) {
		return LanguageType.getName(type);
	}
	
	public int getType(String name){
		return MultiLanguageProperty.getType(name);
	}
}
