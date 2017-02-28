package cn.myapps.core.dynaform.dts.excelimport.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;

/**
 * Excel导入服务提供者 实现Excel文档数据导入动态表单的功能
 * @author Happy.Lau
 * 
 */
public interface ImportProvider {

	public int getMasterSheetRowCount() throws Exception;

	public Map<String, String> getMasterSheetRow(int row);

	public Map<String, String> getDetailSheetValueList(String sheetName,
			String columnName, String matchValue);

	public Collection<LinkedHashMap<String, String>> getDetailSheetRowCollection(
			String sheetName, String columnName, String matchValue)
			throws Exception;

	public String creatDocument(WebUser user, ParamsTable params,
			String applicationid) throws Exception;

	public String getCellStringValue(Cell cell);

}
