package cn.myapps.core.dynaform.activity.ejb.type;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.filedownload.action.FileDownloadUtil;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public class FileDownload extends ActivityType {

	public FileDownload(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4618802972127624357L;

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
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action";
	}

	public String getDefaultOnClass() {

		return DOCUMENT_BUTTON_ON_CLASS;
	}

	public String getOnClickFunction() {
		//return "doFileDonwload('" + act.getId() + "')";
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+",{target:this})";
	}
	
//	protected void addButton(String innerText, String function, String className) {
//		htmlBuilder.append("<span class='" + className + "'><a href=\"###\"");
//		htmlBuilder.append(" id='" + getButtonId() + "'");
//		htmlBuilder.append(" title='" + act.getName() + "'");
//		htmlBuilder.append(" onclick=\"" + function + "\"");
//		htmlBuilder.append(" onmouseover='this.className=\"" + getButtonOnClass() + "\"'");
//		htmlBuilder.append(" onmouseout='this.className=\"" + getButtonClass() + "\"'");
//		htmlBuilder.append(" >");
//		htmlBuilder.append("<span>");
//		htmlBuilder
//				.append("<img style='border:0px solid blue;vertical-align:middle;' src='../../../resource/imgv2/front/act/act_"
//						+ act.getType() + ".gif' />&nbsp;");
//		htmlBuilder.append(innerText);
//		htmlBuilder.append("</span></a></span>");
//	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		return null;
	}
	
	public String doProcess(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),
				doc.getApplicationid());
		runner.initBSFManager(doc, params, getUser(),
				new java.util.ArrayList<ValidateMessage>());

		// 处理文件下载按钮
		if (act.getType() == ActivityType.FILE_DOWNLOAD
				&& !StringUtil.isBlank(act.getFileNameScript())) {
			StringBuffer label = new StringBuffer();
			label.append("Activity(").append(act.getId()).append(
					")." + act.getName()).append("fileNameScript");
			String result = (String) runner.run(label.toString(), act
					.getFileNameScript());

			FileDownloadUtil.doFileDownload(ServletActionContext.getResponse(),
					result);
		}

		// 运行后脚本
		if (!StringUtil.isBlank(act.getAfterActionScript())) {
			StringBuffer label = new StringBuffer();
			label.append("Activity Action(").append(act.getId()).append(
					")." + act.getName()).append("afterActionScript");
			runner.run(label.toString(), act.getAfterActionScript());
		}

		return action.NONE;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}

}
