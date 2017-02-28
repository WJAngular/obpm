package cn.myapps.core.dynaform.view.ejb.type;

import java.util.HashMap;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewType;
import cn.myapps.core.user.action.WebUser;

public class NullType implements ViewType {
	protected View view;

	public NullType(View view) {
		this.view = view;
	}

	public DataPackage<Document> getViewDatas(ParamsTable params, WebUser user, Document doc) throws Exception {
		return new DataPackage<Document>();
	}

	public int intValue() {
		return Integer.MAX_VALUE;
	}

	public Map<String, Column> getColumnMapping() {
		return new HashMap<String, Column>();
	}

	public DataPackage<Document> getViewDatasPage(ParamsTable params, int page, int lines, WebUser user, Document doc)
			throws Exception {
		return new DataPackage<Document>();
	}

	public long countViewDatas(ParamsTable params, WebUser user, Document doc) throws Exception {
		return 0;
	}

	public DataPackage<Document> getViewDatas(ParamsTable params, int page,
			int lines, WebUser user, Document sdoc) throws Exception {
		return new DataPackage<Document>();
	}

	public double getSumTotal(ParamsTable params, WebUser user, String name, String _formid, String domainid) {
		return 0;
	}
}
