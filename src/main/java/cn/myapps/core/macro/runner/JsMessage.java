package cn.myapps.core.macro.runner;

import java.util.HashMap;
import java.util.Map;

public class JsMessage {
	public final static int TYPE_ALERT = 16;
	
	public final static int TYPE_CONFIRM = 32;
	
	public final static int TYPE_SUCCESS = 1;
	public final static int TYPE_INFO = 2;
	public final static int TYPE_WARNING = 3;
	public final static int TYPE_DANGER = 4;
	

	private final static Map<Integer, String> TYPE_NAME_MAP = new HashMap<Integer, String>();

	static {
		TYPE_NAME_MAP.put(Integer.valueOf(TYPE_ALERT), "Alert");
		TYPE_NAME_MAP.put(Integer.valueOf(TYPE_CONFIRM), "Confirm");
		TYPE_NAME_MAP.put(Integer.valueOf(TYPE_SUCCESS), "Success");
		TYPE_NAME_MAP.put(Integer.valueOf(TYPE_INFO), "Info");
		TYPE_NAME_MAP.put(Integer.valueOf(TYPE_WARNING), "Warning");
		TYPE_NAME_MAP.put(Integer.valueOf(TYPE_DANGER), "Danger");
	}

	public JsMessage(int type, String content) {
		this.type = type;
		this.content = content;
	}

	private int type = Integer.MAX_VALUE;

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return (String) TYPE_NAME_MAP.get(Integer.valueOf(type));
	}
}
