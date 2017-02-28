package cn.myapps.core.dynaform.activity.ejb.type;

import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;

public class BatchSignature extends ActivityType {

	public BatchSignature(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public String getDefaultClass() {
		return VIEW_BUTTON_CLASS;
	}

	public String getButtonId() {
		return VIEW_BUTTON_ID;
	}

	public String getAfterAction() {
		return VIEW_NAMESPACE + "/displayView.action";
	}

	public String getBackAction() {
		return VIEW_NAMESPACE + "/displayView.action";
	}

	public String getBeforeAction() {
		return DOCUMENT_NAMESPACE ;
		
	}

	public String getDefaultOnClass() {
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String getOnClickFunction() {
		//return "DoBatchSignature()";
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+")";
	}
//	protected void addButton(String innerText, String function, String className) {
//		htmlBuilder.append("<span class='" + className + "'><a href=\"###\"");
//		htmlBuilder.append(" name='" + getButtonId() + "'");
//		htmlBuilder.append(" title='" + act.getName() + "'");
//		htmlBuilder.append(" onclick=\"" + function + "\"");
//		htmlBuilder.append(" onmouseover='this.className=\"" + getButtonOnClass() + "\"'");
//		htmlBuilder.append(" onmouseout='this.className=\"" + getButtonClass() + "\"'");
//		htmlBuilder.append(" >");
//		htmlBuilder.append("<span>");
//		htmlBuilder.append("<img style='border:0px solid blue;vertical-align:middle;' src='../../../resource/imgv2/front/act/act_"
//						+ act.getType() + ".gif' />&nbsp;");
//		htmlBuilder.append(innerText);
//		Environment env = Environment.getInstance();
//		HttpServletRequest request=ServletActionContext.getRequest();
//		htmlBuilder.append("<OBJECT id=\"SignatureControl\"  classid=\"clsid:D85C89BE-263C-472D-9B6B-5264CD85B36E\" codebase=\"iSignatureHTML.cab#version=7,1,0,180\" width=0 height=0 >");
//		htmlBuilder.append("<param name=\"ServiceUrl\" value=\"http://"+request.getServerName()+":"+request.getServerPort()+env.getContextPath()+"/core/dynaform/mysignature/doCommand.action?" +
//				           "FormID="+act.getOnActionForm()+
//				           "\"/>");
//		htmlBuilder.append("</OBJECT>");
//		htmlBuilder.append("</span></a></span>");
//	
//		
//	}
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		return null;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}

}
