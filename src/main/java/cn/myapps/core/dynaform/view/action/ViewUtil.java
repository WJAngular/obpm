package cn.myapps.core.dynaform.view.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.form.ejb.AttachmentUploadField;
import cn.myapps.core.dynaform.form.ejb.CheckboxField;
import cn.myapps.core.dynaform.form.ejb.DateField;
import cn.myapps.core.dynaform.form.ejb.FileManagerField;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.ImageUploadField;
import cn.myapps.core.dynaform.form.ejb.InputField;
import cn.myapps.core.dynaform.form.ejb.RadioField;
import cn.myapps.core.dynaform.form.ejb.SelectField;
import cn.myapps.core.dynaform.form.ejb.SuggestField;
import cn.myapps.core.dynaform.form.ejb.TextareaField;
import cn.myapps.core.dynaform.form.ejb.TreeDepartmentField;
import cn.myapps.core.dynaform.form.ejb.UserField;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.web.DWRHtmlUtils;

public class ViewUtil {
	public Map<String, String> getViewsByModule(String moduleid, String application) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("none", "Select");
		// //PersistenceUtils.getSessionSignal().sessionSignal++;
		ViewProcess process = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		Collection<View> datas = process.getViewsByModule(moduleid, application);
		Iterator<View> it = datas.iterator();
		while (it.hasNext()) {
			View vo = (View) it.next();
			map.put(vo.getId(), vo.getName());
		}
		// //PersistenceUtils.getSessionSignal().sessionSignal--;
		PersistenceUtils.closeSession();
		return map;
	}

	public Map<String, String> getColsByView(String viewid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		// //PersistenceUtils.getSessionSignal().sessionSignal++;
		ViewProcess process = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		View view = (View) process.doView(viewid);
		if (view != null) {
			Iterator<Column> iter = view.getColumns().iterator();
			while (iter.hasNext()) {
				Column col = iter.next();
				map.put(col.getId(), col.getName());
			}
			// //PersistenceUtils.getSessionSignal().sessionSignal--;
			PersistenceUtils.closeSession();
		}
		return map;

	}

	/**
	 * 根据视图id获取列(视图列的字段id以及类型)
	 * @param viewid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getFilterColsTypeByView(String viewid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		ViewProcess process = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		View view = (View) process.doView(viewid);
		if (view != null) {
			Iterator<Column> iter = view.getColumns().iterator();
			while (iter.hasNext()) {
				Column col = iter.next();
				FormField field = col.getFormField();
				if(Column.COLUMN_TYPE_SCRIPT.equals(col.getType())
						|| (Column.COLUMN_TYPE_FIELD.equals(col.getType()) && (field instanceof InputField 
								|| field instanceof DateField || field instanceof RadioField
								|| field instanceof CheckboxField || field instanceof SelectField
								|| field instanceof SuggestField
								|| field instanceof ImageUploadField
								|| field instanceof AttachmentUploadField
								|| field instanceof UserField
								|| field instanceof FileManagerField
								|| field instanceof TextareaField
								|| field instanceof TreeDepartmentField
								    ))){
					String tpyeName = field.getClass().getName();
					map.put(col.getId(), tpyeName);
				}
			}
			PersistenceUtils.closeSession();
		}
		return map;

	}
	
	/**
	 * 根据视图id获取列(字段类型和脚本类型的列)
	 * @param viewid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getFilterColsByView(String viewid) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		ViewProcess process = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		View view = (View) process.doView(viewid);
		if (view != null) {
			Iterator<Column> iter = view.getColumns().iterator();
			while (iter.hasNext()) {
				Column col = iter.next();
				FormField field = col.getFormField();
				if(Column.COLUMN_TYPE_SCRIPT.equals(col.getType())
						|| (Column.COLUMN_TYPE_FIELD.equals(col.getType()) && (field instanceof InputField 
								|| field instanceof DateField || field instanceof RadioField
								|| field instanceof CheckboxField || field instanceof SelectField
								|| field instanceof SuggestField))){
					map.put(col.getId(), col.getName());
				}
			}
			PersistenceUtils.closeSession();
		}
		return map;
		
	}
	
	public String creatView(String selectFieldName, String moduleid, String def, String application) throws Exception {
		Map<String, String> map = getViewsByModule(moduleid, application);
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}

	public String creatCol(String selectFieldName, String viewid, String def, String application) throws Exception {
		Map<String, String> map = getViewsByModule(viewid, application);
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);
	}
	
	public String getViewType(String viewid) throws Exception{
		String viewType = "";
		ViewProcess process = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		View view = (View) process.doView(viewid);
		if(view != null){
			viewType = view.getViewType() + "";
		}
		return viewType;
	}
}
