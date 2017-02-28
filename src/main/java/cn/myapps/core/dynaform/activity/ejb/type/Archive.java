package cn.myapps.core.dynaform.activity.ejb.type;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.cache.MemoryCacheUtil;

public class Archive extends ActivityType {

	public Archive(Activity act) {
		super(act);
	}

	@Override
	public String getAfterAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	@Override
	public String getBackAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	@Override
	public String getBeforeAction() {
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action";
	}

	@Override
	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	@Override
	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	@Override
	public String getDefaultOnClass() {
		return DOCUMENT_BUTTON_ON_CLASS;
	}

	@Override
	public String getOnClickFunction() {
		//return "ev_action(" + ActivityType.ARCHIVE + ",'" + act.getId() + "')";
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+")";
		
	}
	
	@Override
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());
		try {
			proxy.doArchive(doc, user, params);
			action.addActionMessage("{*[cn.myapps.core.dynaform.activity.archive.message.success]*}");
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			action.setRuntimeException(new OBPMRuntimeException(e.getMessage(), e));
			action.addFieldError("1", e.getMessage());
			if((e instanceof ImpropriateException)){
				//加载数据库中最新的文档到上下文环境
				action.setContent(proxy.doView(action.getContent().getId()));
				MemoryCacheUtil.putToPrivateSpace(action.getContent().getId(), action.getContent(), getUser());
			}else{
				e.printStackTrace();
			}
			
		}

		return AbstractRunTimeAction.FORM;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
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
		
		try {
			proxy.doArchive(doc, user, params);
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			if((e instanceof ImpropriateException)){
				//加载数据库中最新的文档到上下文环境
				MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
			}else{
				e.printStackTrace();
			}
		}
		return doc;
	}

}
