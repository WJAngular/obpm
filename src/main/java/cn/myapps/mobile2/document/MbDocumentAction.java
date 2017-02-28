package cn.myapps.mobile2.document;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentBuilder;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mobile2.service.MbServiceHelper;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;


public class MbDocumentAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8599356573641534856L;
	
	/**
	 * 输出值
	 */
	private String result;
	
	public String doViewDocument() throws Exception {
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			String applicationid = params.getParameterAsString("application");
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationid);
			String _docid = getParams().getParameterAsString("_docid");
			String result = "";
			if(!StringUtil.isBlank(_docid)){
				Document doc = (Document) process.doView(_docid);
				if(doc != null){
					//sp 11
					result = MbDocumentXMLBuilder.toMobileXML(doc, user, params);
				}
			}else{
				FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				String formid = params.getParameterAsString("_formid");
				Form form =  (Form) formProcess.doView(formid);
				Document doc = form.createDocument(params, user);
				DocumentBuilder builder = new DocumentBuilder(doc, params);
				builder.setForm(form);
				Document newDoc = builder.getNewDocument(user);
				MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), doc, getUser());
				if(newDoc != null){
					//sp11
					result = MbDocumentXMLBuilder.toMobileXML(newDoc, user, params);
				}
			}
			if (result != null) {
				this.result = result;
			}
			
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return SUCCESS; 
	}
	
	/**
	 * 刷新重计算
	 * @return
	 * @throws Exception
	 */
	public String doRefresh() throws Exception {
		try {
			WebUser user = this.getUser();

			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);

			ParamsTable params = getParams();
			String formid = params.getParameterAsString("_formid");

			Form form = (Form) formProcess.doView(formid);

			String docid = params.getParameterAsString("_docid");
			String application = params.getParameterAsString("_application");
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,application);
			

			Document doc = (Document) process.doView(docid);
			if(doc == null){
				doc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, user);
			}
			
			if(form != null){
				if(doc != null){
					doc = form.createDocument(doc, params, user);
				} else {
					doc = form.createDocument(params, user);
				}
			}

			if (docid != null && docid.length() > 0) {
				doc.setId(docid);
			}
			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
			String result = MbDocumentXMLBuilder.toMobileXML(doc, user, params);
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
	
	public String doFlowDocument() throws Exception {
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			
			Document doc = MbServiceHelper.getDocument(null, params, user);
			
			String result = MbDocumentXMLBuilder.toFlowXML(doc, user, params);
			if (result != null) {
				this.result = result;
			}
			
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return SUCCESS; 
	}
	
	
	public WebUser getUser() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user == null) {
			throw new Exception("[*TIMEOUT*]");
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
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
			params.setSessionid(ServletActionContext.getRequest().getSession().getId());
			if (params.getParameter("_pagelines") == null)
				params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
		}
		return params;
	}

	public String getResult() {
		return result;
	}
}