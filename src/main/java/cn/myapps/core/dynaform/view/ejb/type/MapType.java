package cn.myapps.core.dynaform.view.ejb.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.EditMode;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewType;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.json.JsonUtil;

public class MapType extends AbstractType implements ViewType {
	private final static String MAP_VIEW = "mapview";

	public final static String[] DEFAULT_KEY_FIELDS = { "mapcolumn" };
	public final static String[] MAP_KEY_FIELDS = { "titlecolumn","addresscolumn","detailcolumn" };

	public final static List<String> ALL_KEY_FIELDS = new ArrayList<String>();

	public final static Map<String, String> DEFAULT_FIELDS = new LinkedHashMap<String, String>();
	public final static Map<String, String> MAP_FIELDS = new LinkedHashMap<String, String>();
	
	public final static Map<String, String> ALL_FIELDS = new LinkedHashMap<String, String>();
	public final static Map<String, String> MAP_ALL_FIELDS = new LinkedHashMap<String, String>();
	static {
		//DEFAULT_FIELDS.put(DEFAULT_KEY_FIELDS[0], "{*[map]*}{*[Column]*}");
		DEFAULT_FIELDS.put(DEFAULT_KEY_FIELDS[0], "地图控件列");
		ALL_FIELDS.putAll(DEFAULT_FIELDS);
		
		MAP_FIELDS.put(MAP_KEY_FIELDS[0], "地图标题列");
		MAP_FIELDS.put(MAP_KEY_FIELDS[1], "地图详细地址列");
		MAP_FIELDS.put(MAP_KEY_FIELDS[2], "地图内容列");
		MAP_ALL_FIELDS.putAll(MAP_FIELDS);
	}

	public MapType(View view) {
		super(view);
	}

	public int intValue() {
		return View.VIEW_TYPE_MAP;
	}

	public static boolean addField(String key_Field, String name_Field) {
		if (ALL_FIELDS.containsKey(key_Field))
			return false;
		ALL_FIELDS.putAll(DEFAULT_FIELDS);
		ALL_KEY_FIELDS.add(key_Field);
		ALL_FIELDS.put(key_Field, name_Field);
		return true;
	}

	/**
	 * 获取地图视图关联的column集并映射为一个map
	 */
	public Map<String, Column> getColumnMapping() {
		Map<String, Column> columnMapping = new HashMap<String, Column>();
		if (view != null) {
			Iterator<Column> columns = view.getColumns().iterator();
			while (columns.hasNext()) {
				Column column = (Column) columns.next();
				columnMapping.put(column.getMappingField(), column);
			}
		}
		return columnMapping;
	}

	/**
	 * @SuppressWarnings relatedMap.get得到的对象为Object转型为Map<String, Column>存在一定的风险
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Column> getColumnMap() {
		Map<String, Object> relatedMap = JsonUtil.toMap(view.getRelatedMap());
		if (relatedMap.get(MAP_VIEW) != null && this.columnMapping == null) {
			this.columnMapping = (Map<String, Column>) relatedMap.get(MAP_VIEW);
		}

		return this.columnMapping;
	}
	
	//重载父类该方法，实现地图视图屏蔽分页功能
	public DataPackage<Document> getViewDatas(ParamsTable params, int page,
			int lines, WebUser user, Document sdoc) throws Exception {
		EditMode editMode = view.getEditModeType();
		addConditionToMode(editMode, user, params);
			return editMode.getDataPackage(params, user, sdoc);
	}
}
