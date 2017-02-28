package cn.myapps.mobile.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.action.ViewAction;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewType;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;


public class MbViewAction extends ViewAction {

	private static final long serialVersionUID = 1876974711850496098L;
	private static final Logger LOG = Logger.getLogger(MbViewAction.class);

	private String _currpage;
	private String _mapStr;

	public MbViewAction() throws ClassNotFoundException {
		super();
	}

	public String doDisplaySearchForm() throws Exception {
		try {
			view = (View) process.doView(_viewid);
			if (view != null) {
				setContent(view);
				toSearchFormXml();
			} else {
				throw new Exception("View id is null!");
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			LOG.warn(e);
			return ERROR;
		}
		return SUCCESS;
	}

	private void toSearchFormXml() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		WebUser user = getUser();
		ParamsTable params = new ParamsTable();
		params.setParameter("_viewid", _viewid);
		params.setParameter("_mapStr", _mapStr);
		params.setParameter("parentid", parent);
		Document tdoc = parent != null ? parent : new Document();
		IRunner runner = JavaScriptFactory.getInstance(request.getSession().getId(), getApplication());
		runner.initBSFManager(tdoc, params, user, new ArrayList<ValidateMessage>());
		Form searchForm = view.getSearchForm();
		if (searchForm != null) {
			Document searchDoc = searchForm.createDocument(params, user);
			String xmlText = searchForm.toMbXML(searchDoc, params, user, new ArrayList<ValidateMessage>(), getEnvironment());
			session.setAttribute("toXml", xmlText);
		}
	}

	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}

	public String doDialogView() throws Exception {
		return doDisplayView(true);
	}

	public String doDisplayView() throws Exception {
		return doDisplayView(false);
	}
	
	private String doDisplayView(boolean isDialogView) throws Exception {
		try {
			ParamsTable params = getParams();
			view = (View) process.doView(_viewid);
			if (view == null) throw new Exception("View id is null or view is not exist!");
			
			Document searchDocument = getSearchDocument(view);
			// 设置Action属性
			setContent(view);
			setParent(null);
			setCurrentDocument(searchDocument);
			changeOrderBy(params);
			// 分页参数
			int page = 1;
			if (!StringUtil.isBlank(_currpage)) {
				try {
					page = Integer.parseInt(_currpage);
				} catch (Exception e) {
					_currpage = "1";
					LOG.warn(e);
				}
			} else {
				_currpage = "1";
			}
			ViewType viewType = view.getViewTypeImpl();
			DataPackage<Document> datas = viewType.getViewDatasPage(params, page, get_pagelines(), getUser(), searchDocument);
			if (datas == null) datas = new DataPackage<Document>();
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			MbViewHelper helper = new MbViewHelper(getApplication());
			String xml = helper.toViewListXml(isDialogView, datas,searchDocument,view,request,params,getUser(),parent,get_currpage(),get_pagelines());
			session.setAttribute("toXml", xml);
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String get_currpage() {
		return _currpage;
	}

	public void set_currpage(String _currpage) {
		this._currpage = _currpage;
	}
	
	public String get_mapStr() {
		return _mapStr;
	}

	public void set_mapStr(String str) {
		_mapStr = str;
	}

	public String getApplication() {
		//if (!StringUtil.isBlank(application)) {
		//	return application;
		//}
		return application;
	}

	private Map<String, List<String>> fieldErrors;

	public void addFieldError(String fieldname, String message) {
		List<String> thisFieldErrors = getFieldErrors().get(fieldname);

		if (thisFieldErrors == null) {
			thisFieldErrors = new ArrayList<String>();
			this.fieldErrors.put(fieldname, thisFieldErrors);
		}
		thisFieldErrors.add(message);
	}

	public Map<String, List<String>> getFieldErrors() {
		if (fieldErrors == null) fieldErrors = new HashMap<String, List<String>>();
		return fieldErrors;
	}

	/**
	 * @SuppressWarnings API支持泛型Map(String, List(String))
	 */
	@SuppressWarnings("unchecked")
	public void setFieldErrors(Map fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
	
	public int get_pagelines() {
		String _pagelines = view.getPagelines();
		int lines = 0;
		if(view.isPagination()){
			lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
		}else{
			lines = Integer.MAX_VALUE;
		}
		return lines;
	}
}
