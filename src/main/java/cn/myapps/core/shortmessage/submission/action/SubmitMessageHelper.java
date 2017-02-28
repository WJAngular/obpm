package cn.myapps.core.shortmessage.submission.action;

import java.util.HashMap;
import java.util.Map;

import cn.myapps.core.shortmessage.submission.ejb.MessageType;

public class SubmitMessageHelper {

	public Map<String, String> getContentTypes() {
		String[] names = MessageType.NAMES;
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < names.length; i++) {
			map.put(""+MessageType.getType(names[i]), names[i]);
		}
		return map;
	}
	
	/**
	 * 获取带国际化的ContentTypes
	 * @return map
	 */
	public Map<String, String> get_i18nContentTypes() {
		String[] names = MessageType.i18n_NAMES;
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < names.length; i++) {
			map.put(""+i, names[i]);
		}
		return map;
	}
}
