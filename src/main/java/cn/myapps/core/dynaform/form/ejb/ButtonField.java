package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.MultiLanguageProperty;
import cn.myapps.util.sequence.Sequence;

public class ButtonField extends FormField {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2243912010678637603L;

	private int actType;
	private String actionView;
	private String actionForm;
	private String actionFlow;
	public String getActionView() {
		return actionView;
	}

	public void setActionView(String actionView) {
		this.actionView = actionView;
	}

	public String getActionForm() {
		return actionForm;
	}

	public void setActionForm(String actionForm) {
		this.actionForm = actionForm;
	}

	public String getActionFlow() {
		return actionFlow;
	}

	public void setActionFlow(String actionFlow) {
		this.actionFlow = actionFlow;
	}

	public String getActionPrint() {
		return actionPrint;
	}

	public void setActionPrint(String actionPrint) {
		this.actionPrint = actionPrint;
	}

	public String getDispatcherUrl() {
		return dispatcherUrl;
	}

	public void setDispatcherUrl(String dispatcherUrl) {
		this.dispatcherUrl = dispatcherUrl;
	}

	public int getDispatcherMode() {
		return dispatcherMode;
	}

	public void setDispatcherMode(int dispatcherMode) {
		this.dispatcherMode = dispatcherMode;
	}

	public String getDispatcherParams() {
		return dispatcherParams;
	}

	public void setDispatcherParams(String dispatcherParams) {
		this.dispatcherParams = dispatcherParams;
	}
	
	public void setJumpMode(int jumpMode) {
		this.jumpMode = jumpMode;
	}

	public int getJumpMode() {
		return jumpMode;
	}
	
	public int getJumpActOpenType() {
		return jumpActOpenType;
	}

	public void setJumpActOpenType(int jumpActOpenType) {
		this.jumpActOpenType = jumpActOpenType;
	}

	public String getTargetList() {
		return targetList;
	}

	public void setTargetList(String targetList) {
		this.targetList = targetList;
	}
	
	public int getJumpType() {
		return jumpType;
	}

	public void setJumpType(int jumpType) {
		this.jumpType = jumpType;
	}

	/**
	 * 跳转到新建表单
	 */
	private int jumpType;
	private String actionPrint;
	private String fileNameScript;
	private String impmappingconfigid;
	private String stateToShow;
	private String approveLimit;
	private String beforeActionScript;
	private String afterActionScript;
	private String dispatcherUrl;
	private int dispatcherMode;
	private String dispatcherParams;
	private String transpond;
	
	/**
	 * 跳转按钮模式（0:跳转到动态表单  1：跳转到指定URL）
	 */
	private int jumpMode;
	
	/**
	 * 跳转按钮打开类型（0:当前页  1：弹出层  2：页签  3：新窗口）
	 */
	private int jumpActOpenType = Activity.JUMPOPENTYPE_CURRENTPAGE;
	
	private String targetList;
	
	private int flowMode;
	private String flowShowType;
	private String startFlowScript;
	private boolean withOld;
	
	public int getFlowMode() {
		return flowMode;
	}

	public void setFlowMode(int flowMode) {
		this.flowMode = flowMode;
	}

	public void setWithOld(boolean withOld) {
		this.withOld = withOld;
	}

	public boolean getWithOld() {
		return withOld;
	}

	public String getFlowShowType() {
		return flowShowType;
	}

	public void setFlowShowType(String flowShowType) {
		this.flowShowType = flowShowType;
	}

	public String getStartFlowScript() {
		return startFlowScript;
	}

	public void setStartFlowScript(String startFlowScript) {
		this.startFlowScript = startFlowScript;
	}
	
	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		Activity act = getActivity();
		StringBuffer xmlText =  new StringBuffer();
		try{
			if (act.isHidden(runner, doc.getForm(), doc, webUser, ResVO.FORM_TYPE)) {
				return null;
			}
			
			if(act.getType() == ActivityType.WORKFLOW_PROCESS){
				if(StringUtil.isBlank(doc.getStateid()) || !StateMachineHelper.isDocEditUser(doc,webUser)){
					return null;
				}
			}
			
			xmlText.append("<").append(MobileConstant2.TAG_BUTTONFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_TYPE).append("='").append(act.getType()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(act.getName()).append("'");
			if(act.getReadonlyScript() != null && act.getReadonlyScript().trim().length()>0){
				xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='").append(act.isReadonly(runner, doc.getFormname())).append("'");
			}
			xmlText.append(" ").append(MobileConstant2.ATT_ACTIONID).append("='").append(act.getId()).append("'>");
			if(act.getType() == ActivityType.SAVE_SARTWORKFLOW){
				String flowid = "";
				if(doc.getState() !=null && StringUtil.isBlank(doc.getState().getFlowid())){
					flowid = doc.getState().getFlowid();
				}else if(!StringUtil.isBlank(doc.getForm().getOnActionFlow())){
					flowid = doc.getForm().getOnActionFlow();
				}
				if(!StringUtil.isBlank(flowid)){
					xmlText.append("<").append(MobileConstant2.TAG_PARAMS);
					xmlText.append(" ").append(MobileConstant2.ATT_KEY).append("='").append("_flowid").append("'");
					xmlText.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(flowid).append("'>");
					xmlText.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
				}
			}else if(act.getType() == ActivityType.FILE_DOWNLOAD){
				if(!StringUtil.isBlank(act.getFileNameScript())){
					// 处理文件下载按钮
					if (act.getType() == ActivityType.FILE_DOWNLOAD
							&& !StringUtil.isBlank(act.getFileNameScript())) {
						
						String script = StringUtil.dencodeHTML(act.getFileNameScript());
						
						StringBuffer label = new StringBuffer();
						label.append("Activity(").append(act.getId()).append(
								")." + act.getName()).append("fileNameScript");
						String fileUrl = (String) runner.run(label.toString(),script);
						xmlText.append("<").append(MobileConstant2.TAG_PARAMS);
						xmlText.append(" ").append(MobileConstant2.ATT_KEY).append("='").append("_fileUrl").append("'");
						xmlText.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(fileUrl).append("'>");
						xmlText.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
					}
				}
			}
			xmlText.append("</").append(MobileConstant2.TAG_BUTTONFIELD).append(">");
			return xmlText.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		Activity act = getActivity();
		StringBuffer template = new StringBuffer();
		if (act.getType() != ActivityType.PRINT && act.getType() != ActivityType.PRINT_WITHFLOWHIS) {
			boolean isHidden = false;
			if (act.getType() == ActivityType.WORKFLOW_PROCESS ) {
				// 存在流程才显示流程相关按钮
				isHidden = (doc.getState() == null);
			}
			if (!isHidden &&act.getType() == ActivityType.START_WORKFLOW) { // 启动流程
				if (!StringUtil.isBlank(doc.getParentid()) || !(doc.getFlowid() == null
						|| doc.getFlowid() == "" || doc.getStateLabel() == null || doc.getStateLabel().equals("")))
					isHidden = true;
			}
			
			if(act.isStateToHidden(doc) || isHidden){
				return "";
			}
			
			StateMachineHelper helper = new StateMachineHelper(doc);
			String ntemp = act.getName();
			String actname = MultiLanguageProperty.getProperty(MultiLanguageProperty.getName(2), ntemp, ntemp);
			actname = actname.replaceAll("&", "&amp;");
			template.append("<").append(MobileConstant.TAG_BUTTONFIELD);
			template.append(" ").append(MobileConstant.ATT_NAME).append("='");
			template.append(getName()+"'");
			int displayType = getDisplayType(doc, runner, webUser);
			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				
					template.append(" ").append(MobileConstant.ATT_READONLY + "='true' ");
			}
			if (displayType == PermissionType.HIDDEN || 
					(getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				template.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					template.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}
			template.append(" ").append(MobileConstant.ATT_VALUE).append("='").append(getMbLabel()).append("'>");
			template.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_ID).append(
					"='");
			template.append(act.getId());
			template.append("' ").append(MobileConstant.ATT_NAME).append("='");
			template.append(actname);
			template.append("' ").append(MobileConstant.ATT_TYPE).append("='");
			template.append(act.getType() + "'");

			StringBuffer label = new StringBuffer();
			label.append("Form(").append(getId()).append(")." + getName()).append(".Activity(")
			.append(act.getId()).append(")." + act.getName())
				.append(".Activity.HiddenScript");
			Object result = runner.run(label.toString(), act.getHiddenScript());
			if (result != null && result instanceof Boolean) {
				if (((Boolean) result).booleanValue()) {
					template.append(" ").append(MobileConstant.ATT_HIDDEN).append(" = 'true' ");
				}
			}
			template.append(">");
			
			if (act.getType() == ActivityType.WORKFLOW_PROCESS) {
				template.append(helper.toFlowXMLText(doc, webUser,new ParamsTable()));
			}
			
			//流程开启按钮脚本处理模式
			if(act.getType()==ActivityType.START_WORKFLOW && act.getEditMode()==1){
				StringBuffer start = new StringBuffer();
				label.append("Activity(").append(act.getId()).append(")." + act.getName()).append(".startFlowScript");
				String resultStart = (String)runner.run(start.toString(), act.getStartFlowScript());
				template.append("<"+MobileConstant.TAG_PARAMETER).append(" "+MobileConstant.ATT_NAME).append("='selectFlow'>");
				template.append(resultStart);
				template.append("</"+MobileConstant.TAG_PARAMETER+">");
			}
			
			template.append("</").append(MobileConstant.TAG_ACTION).append(">");
			if (act.getType() == ActivityType.WORKFLOW_PROCESS) {
				template.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME)
						.append("='_flowid'>");
				template.append(act.getOnActionFlow());
				template.append("</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
			}
			template.append("</").append(MobileConstant.TAG_BUTTONFIELD).append(">");
			
			if (act.getType() == ActivityType.WORKFLOW_PROCESS) {
				template.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_ID)
						.append(" = '");
				template.append(Sequence.getSequence());
				template.append("' ").append(MobileConstant.ATT_NAME).append("='{*[Flow]*}{*[Diagram]*}' ").append(
						MobileConstant.ATT_TYPE).append(" = '");
				template.append(ActivityType.DOCUEMNT_VIEWFLOWIMAGE);
				template.append("'>");
				template.append("</").append(MobileConstant.TAG_ACTION).append(">");
				if (!StringUtil.isBlank(doc.getStateid())) {
					template.append("<").append(MobileConstant.TAG_ACTION).append(" ")
							.append(MobileConstant.ATT_ID).append(" = '");
					template.append(Sequence.getSequence());
					template.append("' ").append(MobileConstant.ATT_NAME).append("='{*[Flow]*}{*[History]*}' ").append(
							MobileConstant.ATT_TYPE).append(" = '");
					template.append("824");
					template.append("'>");
					template.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(
							MobileConstant.ATT_NAME).append("='_docid'>" + doc.getId() + "</").append(
							MobileConstant.TAG_PARAMETER).append(">");
					template.append("</").append(MobileConstant.TAG_ACTION).append(">");
				}
			}
		}
		return template.toString();
	}

	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<input type='button'");
		template.append(" className='" + this.getClass().getName() + "'");
		template.append(" id='" + getId() + "'");
		template.append(" name='" + getName() + "'");
		template.append(" formid='" + getFormid() + "'");
		template.append(" discript='" + getDiscript() + "'");
		template.append(" hiddenScript='" + getHiddenScript() + "'");
		template.append(" hiddenPrintScript='" + getHiddenPrintScript() + "'");
		template.append(" refreshOnChanged='" + isRefreshOnChanged() + "'");
		template.append(" validateRule='" + getValidateRule() + "'");
		template.append(" valueScript='" + getValueScript() + "'");
		template.append("/>");
		return template.toString();
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		Activity act = getActivity();
		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		}
		
		boolean isHidden = false;
		if (act.getType() == ActivityType.WORKFLOW_PROCESS ) {
			// 存在流程才显示流程相关按钮
			isHidden = (doc.getState() == null);
		}
		if (!isHidden &&act.getType() == ActivityType.START_WORKFLOW) { // 启动流程
			if (!StringUtil.isBlank(doc.getParentid()) || !(doc.getFlowid() == null
					|| doc.getFlowid() == "" || doc.getStateLabel() == null || doc.getStateLabel().equals("")))
				isHidden = true;
		}
		
		if(act.isStateToHidden(doc) || isHidden){
			return "";
		}

		String rtn = act.toButtonHtml(doc, runner, webUser, displayType).replace("moduleType='activityButton'", "moduleType='formActivityButton'");
		//if(webUser.getEquipment()==webUser.EQUIPMENT_PC){
			rtn = rtn.replace("type='hidden'", "type='button'");
		//}
		if(this.getOtherPropsAsMap() != null && this.getOtherPropsAsMap().get("style") != null){
			rtn = rtn.replace("style='display:none;'", "style='" + this.getOtherPropsAsMap().get("style") + "'");
		}else{
			rtn = rtn.replace("style='display:none;'","");
		}
		return rtn;
	}

	public Activity getActivity() {
		Activity act = new Activity();
		act.setName(name);
		act.setId(id);
		act.setJumpMode(jumpMode);
		act.setJumpType(jumpType);
		act.setJumpActOpenType(jumpActOpenType);
		act.setTargetList(targetList);
		act.setDispatcherMode(dispatcherMode);
		act.setDispatcherUrl(StringUtil.dencodeHTML(dispatcherUrl));
		act.setDispatcherParams(StringUtil.dencodeHTML(dispatcherParams));
		act.setAfterActionScript(StringUtil.dencodeHTML(afterActionScript));
		act.setApplicationid(get_form().getApplicationid());
		act.setApproveLimit(approveLimit);
		act.setBeforeActionScript(StringUtil.dencodeHTML(beforeActionScript));
		// act.setDomainid(webUser.getDomainid());
		act.setFileNameScript(StringUtil.dencodeHTML(fileNameScript));
		act.setHiddenScript(StringUtil.dencodeHTML(hiddenScript));
		act.setImpmappingconfigid(impmappingconfigid);
		act.setOnActionFlow(actionFlow);
		act.setOnActionPrint(actionPrint);
		act.setOnActionForm(actionForm);
		act.setOnActionView(actionView);
		act.setParentForm(getFormid());
		act.setStateToShow(stateToShow);
		act.setType(actType);
		act.setEditMode(flowMode);
		act.setFlowShowType(flowShowType);
		act.setStartFlowScript(startFlowScript);
		act.setWithOld(withOld);

		return act;
	}

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		if (doc != null) {
			int printDisplayType = getPrintDisplayType(doc, runner, webUser);
			//如果按钮为"打印时隐藏",则隐藏
			if (printDisplayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}
			int displayType = getDisplayType(doc, runner, webUser);
			if (displayType == PermissionType.HIDDEN) {
				return this.getHiddenValue();
			}
			Activity act = getActivity();
			String rtn = "<input type='button' value='" + act.getName() + "'";
			if(webUser.getEquipment()!=webUser.EQUIPMENT_PC){
				rtn = rtn.replace("type='button'", "type='hidden'");
			}
			if(this.getOtherPropsAsMap() != null && this.getOtherPropsAsMap().get("style") != null){
				rtn += " style='" + this.getOtherPropsAsMap().get("style") + "'";
			}
			rtn += " />";
			return rtn;
		}
		return "";
	}
	
	

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		return "";
	}

	public int getActType() {
		return actType;
	}

	public void setActType(int actType) {
		this.actType = actType;
	}

	public String getFileNameScript() {
		return fileNameScript;
	}

	public void setFileNameScript(String fileNameScript) {
		this.fileNameScript = fileNameScript;
	}

	public String getImpmappingconfigid() {
		return impmappingconfigid;
	}

	public void setImpmappingconfigid(String impmappingconfigid) {
		this.impmappingconfigid = impmappingconfigid;
	}

	public String getStateToShow() {
		return stateToShow;
	}

	public void setStateToShow(String stateToShow) {
		this.stateToShow = stateToShow;
	}

	public String getApproveLimit() {
		return approveLimit;
	}

	public void setApproveLimit(String approveLimit) {
		this.approveLimit = approveLimit;
	}

	public String getBeforeActionScript() {
		return beforeActionScript;
	}

	public void setBeforeActionScript(String beforeActionScript) {
		this.beforeActionScript = beforeActionScript;
	}

	public String getAfterActionScript() {
		return afterActionScript;
	}

	public void setAfterActionScript(String afterActionScript) {
		this.afterActionScript = afterActionScript;
	}

	public String getTranspond() {
		return transpond;
	}

	public void setTranspond(String transpond) {
		this.transpond = transpond;
	}
}
