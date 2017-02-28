package cn.myapps.core.dynaform.view.action;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import cn.myapps.core.dynaform.view.ejb.Column;

public class ColumnHelper {
	public static final HashMap<String, String> typeList = new LinkedHashMap<String, String>();
	static {
		typeList.put(Column.COLUMN_TYPE_FIELD, "{*[Field]*}");
		typeList.put(Column.COLUMN_TYPE_SCRIPT, "{*[Script]*}");
		typeList.put(Column.COLUMN_TYPE_OPERATE, "{*[Operate]*}");
		typeList.put(Column.COLUMN_TYPE_LOGO, "{*[LogoUrl]*}");
		typeList.put(Column.COLUMN_TYPE_ROWNUM, "{*[RowNum]*}");
	}
	
	public static final HashMap<String, String> buttonTypeList = new HashMap<String, String>();
	static{
		buttonTypeList.put(Column.BUTTON_TYPE_DELETE, "{*[Delete]*}");
		buttonTypeList.put(Column.BUTTON_TYPE_DOFLOW, "{*[core.dynaform.form.Submit_WorkFlow]*}");
		buttonTypeList.put(Column.BUTTON_TYPE_TEMPFORM, "{*[core.dynaform.form.type.open_by_templateform]*}");
		buttonTypeList.put(Column.BUTTON_TYPE_SCRIPT, "{*[Script]*}");
		buttonTypeList.put(Column.BUTTON_TYPE_JUMP, "{*[core.dynaform.form.type.Jump]*}");
	}
	
	public static final HashMap<String, String> formatType = new HashMap<String, String>();
	static{
		formatType.put(Column.FORMATTYPE_SIMPLE, "{*[core.dynaform.view.column.format.normal]*}");
		formatType.put(Column.FORMATTYPE_NUM, "{*[core.dynaform.view.column.format.number]*}");
		formatType.put(Column.FORMATTYPE_CURR, "{*[core.dynaform.view.column.format.currency]*}");
	}
	
	public static final HashMap<String, String> currType = new LinkedHashMap<String, String>();
	static{
		currType.put(Locale.CHINA.toString(), "{*[core.dynaform.view.column.format.currency.Renminbi]*}");
		currType.put(Locale.US.toString(), "{*[core.dynaform.view.column.format.currency.dollar]*}");
		currType.put(Locale.JAPAN.toString(), "{*[core.dynaform.view.column.format.currency.yen]*}");
		currType.put(Locale.UK.toString(), "{*[core.dynaform.view.column.format.currency.pound]*}");
	}

	/**
	 * 返回Column type
	 * 
	 * @return Column type
	 */
	public Map<String, String> getTypeList() {
		return typeList;
	}
	
	/**
	 * 返回buttonType
	 * 
	 * @return buttonType
	 */
	public HashMap<String, String> getButtontypelist() {
		return buttonTypeList;
	}
	
	/**
	 * 返回格式的类型
	 * @return
	 */
	public Map<String, String> getFormatType(){
		return formatType;
	}
	
	/**
	 * 返回小数位位数
	 * @return
	 */
	public Map<String, String> getDecimalsNum(){
		HashMap<String, String> decimalsNum = new LinkedHashMap<String, String>();
		for(int i=1; i<=10; i++){
			decimalsNum.put(i+"", i+"");
		}
		return decimalsNum;
	}

	/**
	 * 返回货币类型
	 * @return
	 */
	public Map<String, String> getCurrType(){
		return currType;
	}
	
	/**
	 * 返回字体大小
	 * @return
	 */
	public Map<String, String> getFontSize(){
		HashMap<String, String> fontSize = new LinkedHashMap<String, String>();
		for(int i=12; i<=48; i+=2){
			fontSize.put(i+"", i+"");
		}
		return fontSize;
	}
}
