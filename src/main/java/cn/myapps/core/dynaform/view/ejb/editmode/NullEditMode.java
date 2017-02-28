package cn.myapps.core.dynaform.view.ejb.editmode;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.EditMode;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.user.action.WebUser;

/**
 * 
 * @author nicholas zhen
 * 
 */
public class NullEditMode extends AbstractEditMode implements EditMode {

	public NullEditMode(View view) {
		super(view);
	}

	public String getQueryString(ParamsTable params, WebUser user, Document sDoc) {
		return "";
	}

	public DataPackage<Document> getDataPackage(ParamsTable params, WebUser user, Document doc) throws Exception {
		return new DataPackage<Document>();
	}

	public DataPackage<Document> getDataPackage(ParamsTable params, int page, int lines, WebUser user, Document doc) throws Exception {
		return new DataPackage<Document>();
	}

	public long count(ParamsTable params, WebUser user, Document doc) throws Exception {
		return 0;
	}

	public double getSumTotal(ParamsTable params, WebUser user,
			String fieldName, String formid, String domainid) {
		return 0;
	}
}
