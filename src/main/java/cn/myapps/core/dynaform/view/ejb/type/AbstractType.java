package cn.myapps.core.dynaform.view.ejb.type;

import java.util.HashMap;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.EditMode;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public abstract class AbstractType {
	protected View view;

	protected Map<String, Column> columnMapping;

	public AbstractType(View view) {
		this.view = view;
	}

	public long countViewDatas(ParamsTable params, WebUser user, Document sdoc) throws Exception {
		EditMode editMode = view.getEditModeType();
		addConditionToMode(editMode, user, params);
		long count = editMode.count(params, user, sdoc);

		return count;
	}

	public DataPackage<Document> getViewDatas(ParamsTable params, WebUser user, Document sdoc) throws Exception {
		String _currpage = params.getParameterAsString("_currpage");
		String _pagelines = view.getPagelines();

		// 分页参数
		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : Integer
				.parseInt(Web.DEFAULT_LINES_PER_PAGE);

		return getViewDatas(params, page, lines, user, sdoc);
	}

	public DataPackage<Document> getViewDatas(ParamsTable params, int page, int lines, WebUser user, Document sdoc)
			throws Exception {
		EditMode editMode = view.getEditModeType();
		addConditionToMode(editMode, user, params);
		if (view.isPagination()) {
			return editMode.getDataPackage(params, page, lines, user, sdoc);
		} else {
			return editMode.getDataPackage(params, user, sdoc);
		}
	}

	public DataPackage<Document> getViewDatasPage(ParamsTable params, int page, int lines, WebUser user, Document sdoc)
			throws Exception {
		EditMode editMode = view.getEditModeType();
		addConditionToMode(editMode, user, params);
		return editMode.getDataPackage(params, page, lines, user, sdoc);
	}

	protected void addConditionToMode(EditMode editMode, WebUser user, ParamsTable params) throws Exception {
		// 获取参数
		String parentid = params.getParameterAsString("parentid");
		boolean isRelate = params.getParameterAsBoolean("isRelate");
		if(params.getParameterAsBoolean("isExcelExpOperation") && !StringUtil.isBlank(parentid) && isRelate){//导出Excel时用到,多个parentid
			editMode.addCondition("PARENT", parentid, "in");
		}else if (!StringUtil.isBlank(parentid) && isRelate) {
			editMode.addCondition("PARENT", parentid); // 添加父文档查询条件
		}
	}

	public abstract int intValue();

	public String toHtml(ParamsTable params, WebUser user) throws Exception {
		return "";
	}

	public Map<String, Column> getColumnMapping() {
		return new HashMap<String, Column>();
	}
	
	/**
	 * 设置所查所有数据总计
	 * @param params
	 * @param user
	 * @param fieldName
	 * @param _formid
	 * @param domainid
	 * @return
	 */
	public double getSumTotal(ParamsTable params, WebUser user, String fieldName, String _formid, String domainid){
		EditMode editMode = view.getEditModeType();
		return editMode.getSumTotal(params, user, fieldName, _formid, domainid);
	}
}
