package cn.myapps.mobile2.view;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mobile2.document.MbDocumentXMLBuilder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;


public class MbViewAction extends ActionSupport {

	private static final long serialVersionUID = 4008746927530763515L;
	
	/**
	 *输出值
	 */
	private String result;
	
	public MbViewAction() throws ClassNotFoundException {
		super();
	}

	public String doDisplayView() throws Exception {
		try {
			ParamsTable params = getParams();
			String viewid = params.getParameterAsString("_viewid");
			ViewProcess viewprocess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			View view = (View) viewprocess.doView(viewid);
			WebUser user = getUser();
			if (view == null) throw new Exception("View id is null or view is not exist!");
			
			//判断是否修改视图 是的话只更新单条记录 否的话更新全部记录
			String change = params.getParameterAsString("_change");
			String result = "";
			if(!StringUtil.isBlank(change) && change.equals("true")){
				result = MbViewHelper.doViewChangeData(view, user, params);
			}else{
				result = MbViewXMLBuilder.toMobileXML(view, user, params);
			}
			if(!StringUtil.isBlank(result)){
				this.result = result;
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}
		return SUCCESS;
	}
	
	/**
	 * 获取查询表单
	 * @return
	 * @throws Exception
	 */
	public String doSearchDocument() throws Exception {
		try{
			WebUser user = getUser();
			ParamsTable params = getParams();
			String viewid = getParams().getParameterAsString("_viewid");
			ViewProcess viewprocess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			View view = (View) viewprocess.doView(viewid);
			if(view != null){
				Form searchForm = view.getSearchForm();
				if(searchForm != null){
					Document searchDoc = searchForm.createDocument(params, user);
					String result = MbDocumentXMLBuilder.toMobileXML(searchDoc, user, params);
					if(!StringUtil.isBlank(result)){
						this.result = result;
					}
				}
			}
		}catch(Exception e){
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}
		return SUCCESS;
	}
	
	
	
	public WebUser getUser() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user == null) {
			throw new Exception("[*TIMEOUT*]{*[page.timeout]*}");
		}
		return user;
	}
	
	/**
	 * Get the Parameters table
	 * 
	 * @return ParamsTable
	 */
	public ParamsTable getParams() {
		ParamsTable params = null;
		if (params == null) {
			// If the parameters table is empty, then initiate it.
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
			params.setSessionid(ServletActionContext.getRequest().getSession().getId());

			if (params.getParameter("_pagelines") == null)
				params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
		}

		return params;
	}
	
	protected Document getSearchDocument(View view) {
		if (view.getSearchForm() != null) {
			try {
				return view.getSearchForm().createDocument(getParams(), getUser());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new Document();
	}
	
	protected void changeOrderBy(ParamsTable params,View view) {
		if (params.getParameter("_sortCol") == null || params.getParameter("_sortCol").equals("")) {
			String[] orderFields = view.getDefaultOrderFieldArr();
			params.setParameter("_sortCol", orderFields);
			if(orderFields.length>0 && orderFields[0] != null && orderFields[0].startsWith("ITEM_")){
				params.setParameter("_sortCol", orderFields[0].substring(5,orderFields[0].indexOf(" ")));
				params.setParameter("_sortStatus", (orderFields[0].indexOf("ASC") > 0)? "ASC" : "DESC");
			}
		} else {
			String[] colFields = view.getClickSortingFieldArr();
			String fieldName = view.getFormFieldNameByColsName(params.getParameterAsString("_sortCol"));
			boolean contains = false;
			for(int i=0; i<colFields.length; i++){
				if(colFields[i].contains(fieldName)){
					colFields[i] = colFields[0];
					contains = true;
					break;
				}
			}
			String sortStatus = params.getParameterAsString("_sortStatus");
			String sortStandard = this.getSortStandardValue(params.getParameterAsString("_sortCol"),view);
			if(colFields.length > 0 && contains){//存在排序字段时才去修改排序的状态
				if(! StringUtil.isBlank(sortStandard)){
					colFields[0] = fieldName + " " + sortStatus + sortStandard;
				}else{
					colFields[0] = fieldName + " " + sortStatus;
				}
			}
			String[] colField = new String[0];
			if(colFields.length > 0)
				colField = new String[]{colFields[0]};
			params.setParameter("_sortCol", colField);
			if(colField.length>0 && colField[0] != null && colField[0].startsWith("ITEM_")){
				params.setParameter("_sortCol", colField[0].substring(5,colField[0].indexOf(" ")));
				params.setParameter("_sortStatus", (colField[0].indexOf("ASC") > 0)? "ASC" : "DESC");
			}
		}
	}
	
	/**
	 * 获取视图排序列的排序标准(00或01)
	 * @param colsName 列名
	 * @return 00|01|null
	 */
	private String getSortStandardValue(String colsName,View view){
		Collection<Column> cols = view.getColumns();
		for (Iterator<Column> iter = cols.iterator(); iter.hasNext();) {
			Column vo = iter.next();
			if(vo.getFieldName().equalsIgnoreCase(colsName)){
				if(! StringUtil.isBlank(vo.getSortStandard())){
					return vo.getSortStandard();
				}
			}
		}
		return null;
	}

	public String getResult() {
		return result;
	}
}
