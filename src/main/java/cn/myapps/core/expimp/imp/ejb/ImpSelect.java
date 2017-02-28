package cn.myapps.core.expimp.imp.ejb;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImpSelect implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2022983949004770335L;
	public final static int IMPORT_TYPE_APPLICATION = 0x00001;
	public final static int IMPORT_TYPE_MODULE = 0x00010;
	public final static int IMPORT_TYPE_MODULE_ELEMENTS = 0x00100;
	
	/**
	 * 以覆盖更新的方式导入
	 */
	public static final String HANDLE_MODE_UPDATE = "update";
	/**
	 * 以复制的方式导入
	 */
	public static final String HANDLE_MODE_COPY = "copy";

	private String moduleid;
	private String applicationid;
	private int importType;
	/**
	 * 导入处理方式（覆盖更新|复制）
	 */
	private String handleMode;

	public String getModuleid() {
		return moduleid;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	public String getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	public int getImportType() {
		return importType;
	}

	public void setImportType(int importType) {
		this.importType = importType;
	}

	public String getHandleMode() {
		return handleMode;
	}

	public void setHandleMode(String handleMode) {
		this.handleMode = handleMode;
	}

	/**
	 * 获取导入(类型-名称)映射
	 * 
	 * @return
	 */
	public Map<Integer, String> getImportTypeNameMap() {
		Map<Integer, String> typeNameMap = new LinkedHashMap<Integer, String>();
		// typeNameMap.put("", "{*[Select]*}");
		typeNameMap.put(0, "{*[Select]*}");
		typeNameMap.put(Integer.valueOf(IMPORT_TYPE_APPLICATION),
				"{*[Application]*}");
		typeNameMap.put(Integer.valueOf(IMPORT_TYPE_MODULE), "{*[Module]*}");
		typeNameMap.put(Integer.valueOf(IMPORT_TYPE_MODULE_ELEMENTS),
				"{*[cn.myapps.core.deploy.module.elements]*}");

		return typeNameMap;
	}
}
