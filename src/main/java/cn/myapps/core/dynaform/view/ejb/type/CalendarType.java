package cn.myapps.core.dynaform.view.ejb.type;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.dynaform.view.ejb.ViewType;
import cn.myapps.core.user.action.WebUser;
//import cn.myapps.util.DateUtil;
import cn.myapps.util.ProcessFactory;

public class CalendarType extends AbstractType implements ViewType {
	
	public final static String[] DEFAULT_KEY_FIELDS = {"CldViewDateColum"};
	
	public final static List<String> ALL_KEY_FIELDS = new ArrayList<String>();
	
	public final static Map<String, String> DEFAULT_FIELDS = new LinkedHashMap<String, String>();
	public final static Map<String, String> ALL_FIELDS = new LinkedHashMap<String, String>();
	static {
		DEFAULT_FIELDS.put(DEFAULT_KEY_FIELDS[0], "{*[cn.myapps.core.dynaform.view.CldViewDateColum]*}");
		ALL_FIELDS.putAll(DEFAULT_FIELDS);
	}
	
	public CalendarType(View view) {
		super(view);
	}

	public DataPackage<Document> getViewDatas(ParamsTable params, WebUser user, Document sdoc) throws Exception {
		ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);

		String _pagelines = view.getPagelines();
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
		Date currentDate = params.getParameterAsDate("currentDate");
		
		Calendar currentDate1 = Calendar.getInstance();
		if(currentDate != null)
			currentDate1.setTime(currentDate);
		currentDate1.set(Calendar.HOUR, 0);
		currentDate1.set(Calendar.MINUTE, 0);
		currentDate1.set(Calendar.SECOND, 0);
		
		Calendar currentDate2 = Calendar.getInstance();
		if(currentDate != null)
			currentDate2.setTime(currentDate);
		//get click day data, so add one day.
		currentDate2.add(Calendar.DATE, 1);
		currentDate2.set(Calendar.HOUR, 0);
		currentDate2.set(Calendar.MINUTE, 0);
		currentDate2.set(Calendar.SECOND, 0);
//		DataPackage<Document> datas = viewProcess.getDataPackage(view, params, user, view.getApplicationid(), currentDate2.getTime(),
//				DateUtil.getNextDateByDayCount(currentDate2.getTime(), 1), lines);
		//Just to get click day data . modify by Dolly 2011-3-24
		DataPackage<Document> datas = viewProcess.getDataPackage(view, params, user, view.getApplicationid(), currentDate1.getTime(),
				currentDate2.getTime(), lines);

		return datas;
	}

	public int intValue() {
		return View.VIEW_TYPE_CALENDAR;
	}
	
	public static boolean addField(String key_Field, String name_Field){
		if(ALL_FIELDS.containsKey(key_Field))return false;
		ALL_FIELDS.putAll(DEFAULT_FIELDS);
		ALL_KEY_FIELDS.add(key_Field);
		ALL_FIELDS.put(key_Field, name_Field);
		return true;
	}
	
	/**
	 * 获取日历视图关联的column集并映射为一个map
	 */
	public Map<String, Column> getColumnMapping() {
		Map<String, Column> columnMapping = new HashMap<String, Column>();
		if(view != null){
			Iterator<Column> columns = view.getColumns().iterator();
			while(columns.hasNext()){
				Column column = (Column)columns.next();
				columnMapping.put(column.getMappingField(), column);
			}
		}
		return columnMapping;
	}
}
