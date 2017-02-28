package cn.myapps.core.dynaform.activity.ejb.type;



import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
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

public class SaveStartWorkFlow extends ActivityType {

	public SaveStartWorkFlow(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -9022536030969777318L;

	

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
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBackAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBeforeAction() {
		return "/portal/dynaform/activity/handle.action";
	}

	public String getDefaultOnClass() {
		
		return DOCUMENT_BUTTON_ON_CLASS;
	}

	public String doExecute(AbstractRunTimeAction action, Document doc, WebUser user,
			ParamsTable params) throws Exception {
		
		DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, doc.getApplicationid());
		try {
			doc = DocumentHelper.rebuildDocument(doc, params,user);
			doc.setDomainid(action.getDomain());
			doc.setVersions(Integer.valueOf(params.getParameterAsString("content.versions")));
			params.removeParameter("_attitude");
			doc = proxy.doStartFlowOrUpdate(doc, params, user);
			
			/**
			if(!StringUtil.isBlank(doc.getStateid())){
				//把当前审批节点设置到上下文
				NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user,null);
				if(nodert != null){
					ServletActionContext.getRequest().setAttribute("_targetNodeRT", nodert);
					((ActivityRunTimeAction)action).set_targetNode(nodert.getNodeid());
				}
			}
			**/
			action.setContent(doc);
			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
			action.addActionMessage("{*[Save_Success]*}");
		} catch (OBPMValidateException e) {
			action.addFieldError("1", e.getValidateMessage());
			if((e.getCause() instanceof ImpropriateException)){
				//加载数据库中最新的文档到上下文环境
				action.setContent(proxy.doView(action.getContent().getId()));
				MemoryCacheUtil.putToPrivateSpace(action.getContent().getId(), action.getContent(), getUser());
			}else{
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			action.setRuntimeException(new OBPMRuntimeException(e.getMessage(), e));
			e.printStackTrace();
		}
		return AbstractRunTimeAction.FORM;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		Document doc = null;
		try {
			String formid = params.getParameterAsString("_formid");
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, act.getApplicationid());
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = (Form) formProcess.doView(formid);
			String docid = params.getParameterAsString("_docid");
			Document olddoc = (Document) proxy.doView(docid);
            //重新构建document的时候，需要从缓存中获取docid来构建
            if (olddoc == null) {
                olddoc = (Document) proxy.doView(docid);
            }
            if(olddoc == null){
                olddoc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, user);
            }
            if(form != null){
                if (olddoc != null) {
                    doc = form.createDocument(olddoc, params, user);
                } else {
                    doc = form.createDocument(params, user);
                }
            }else{
                doc = new Document();
            }
			if (docid != null && docid.length() > 0) {
				doc.setId(docid);
			}
			doc.setDomainid(user.getDomainid());
			proxy.doStartFlowOrUpdate(doc, params, user);
			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
			
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	

}
