package cn.myapps.core.dynaform.activity.ejb.type;

import java.sql.DataTruncation;


import com.ibm.db2.jcc.a.in;
import com.mysql.jdbc.MysqlDataTruncation;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.action.DocumentHelper;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.cache.MemoryCacheUtil;

public class SaveCloseWindow extends ActivityType {

	public SaveCloseWindow(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 90175457607017688L;

	public String getOnClickFunction() {
		return "Activity.doExecute('" + act.getId() + "', " + act.getType() + ")";
	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getAfterAction() {
		return DOCUMENT_SHARE_JSP_NAMESPACE + "/close.jsp";
	}

	public String getBackAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
		
	}

	public String getBeforeAction() {
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action";
	}

	public String getDefaultOnClass() {
		
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());
		try {
			doc.setDomainid(action.getDomain());
			if(doc.getIstmp()){
				doc.refreshFlowInstancesCache();
				doc.setState("");
			}
			doc = DocumentHelper.rebuildDocument(doc, params,user);
			proxy.doCreateOrUpdate(doc, user);

			action.setContent(doc);
			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
			action.addActionMessage("{*[Save_Success]*}");
		} catch (Exception e) {
			String message = e.getMessage();
			action.setRuntimeException(new OBPMRuntimeException(message, e));
			if(e instanceof MysqlDataTruncation || e instanceof DataTruncation || e instanceof in){
				message = message.substring(message.indexOf("_") + 1, message.indexOf("'", message.indexOf("_")));
				action.addFieldError("1", "{*[Out.of.range.value1]*}" + ": \"" + message + "\" {*[Out.of.range.value2]*}");
			}else{
				action.addFieldError("1", message);
			}
			if((e instanceof ImpropriateException)){
				//加载数据库中最新的文档到上下文环境
				action.setContent(proxy.doView(action.getContent().getId()));
				MemoryCacheUtil.putToPrivateSpace(action.getContent().getId(), action.getContent(), getUser());
			}else{
				e.printStackTrace();
			}
		}
		return "close";
	}
	
	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		try {
			Document doc =null;
			String formid = params.getParameterAsString("_formid");
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, act.getApplicationid());
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = (Form) formProcess.doView(formid);
			String docid = params.getParameterAsString("_docid");
			Document olddoc = (Document) proxy.doView(docid);
			
			if(olddoc == null){
				olddoc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, user);
			}
			if (olddoc != null) {
				doc = form.createDocument(olddoc, params, user);
			} else {
				doc = form.createDocument(params, user);
			}
			if (docid != null && docid.length() > 0) {
				doc.setId(docid);
			}
			doc.setDomainid(user.getDomainid());
			proxy.doCreateOrUpdate(doc, user);
			
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
