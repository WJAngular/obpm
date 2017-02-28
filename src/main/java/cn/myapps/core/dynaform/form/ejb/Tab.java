package cn.myapps.core.dynaform.form.ejb;

import java.io.Serializable;
import java.util.Collection;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.resource.ejb.ResourceType;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.PreviewUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * @author nicholas
 */
public class Tab implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -223077910802191469L;
	
	static final String TYPE_VIEW = "view";
	static final String TYPE_FORM = "form";

	/**
	 * @uml.property name="name"
	 */
	private String name;
	
	private String type = TYPE_FORM;

	/**
	 * @uml.property name="hiddenScript"
	 */
	private String hiddenScript;

	/**
	 * @uml.property name="formId"
	 */
	private String formId;
	
	/**
	 * 是否关联, 是则为父子关系, 非则没有关系
	 */
	protected boolean relate = true;

	private String hiddenPrintScript;
	
	private String readOnlyScript;

	/**
	 * 
	 */
	private Form form;
	
	public View view;
	
	private boolean calculateOnRefresh;

	private boolean refreshOnChanged;

	/**
	 * 是否重计算
	 * 
	 * @return
	 */
	public boolean isCalculateOnRefresh() {
		return calculateOnRefresh;
	}

	/**
	 * 设置是否重计算
	 * 
	 * @param calculateOnRefresh
	 */
	public void setCalculateOnRefresh(boolean calculateOnRefresh) {
		this.calculateOnRefresh = calculateOnRefresh;
	}

	/**
	 * 是否刷新
	 * 
	 * @return
	 */
	public boolean isRefreshOnChanged() {
		return refreshOnChanged;
	}

	/**
	 * 是否刷新
	 * 
	 * @param refreshOnChanged
	 */
	public void setRefreshOnChanged(boolean refreshOnChanged) {
		this.refreshOnChanged = refreshOnChanged;
	}

	/**
	 * 获取关联的表单标识
	 * 
	 * @return 关联的表单标识
	 * @uml.property name="formId"
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * 设置 关联的表单标识
	 * 
	 * @param formId
	 *            关联的表单标识
	 * @uml.property name="formId"
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * 获取TAB的名字
	 * 
	 * @return the name
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置 TAB的名字
	 * 
	 * @param name
	 *            the name to set
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = StringUtil.dencodeHTML(name);
	}

	/**
	 * 获取TAB的隐藏脚本
	 * 
	 * @return TAB的隐藏脚本
	 * @uml.property name="hiddenScript"
	 */
	public String getHiddenScript() {
		return hiddenScript;
	}

	/**
	 * 设置TAB的隐藏脚本
	 * 
	 * @param hiddenScript
	 *            TAB的隐藏脚本
	 * @uml.property name="hiddenScript"
	 */
	public void setHiddenScript(String hiddenScript) {
		this.hiddenScript = StringUtil.dencodeHTML(hiddenScript);
	}

	public boolean isHidden(TabNormal tabMode, IRunner runner) throws Exception {
		StringBuffer suffix = new StringBuffer();
		suffix.append("Tab.").append(getName());
		suffix.append(".hiddenScript");

		return tabMode.field.runBooleanScript(runner, suffix.toString(), getHiddenScript());
	}
	
	public boolean isReadOnly(TabNormal tabMode, IRunner runner) throws Exception{
		StringBuffer suffix = new StringBuffer();
		suffix.append("Tab.").append(getName());
		suffix.append(".readOnlyScript");

		return tabMode.field.runBooleanScript(runner, suffix.toString(), getReadOnlyScript());
	}

	/**
	 * 获取TAB打印时隐藏脚本
	 * 
	 */
	public String getHiddenPrintScript() {
		return hiddenPrintScript;
	}

	/**
	 * 设置TAB打印时隐藏脚本
	 */
	public void setHiddenPrintScript(String hiddenPrintScript) {
		this.hiddenPrintScript = StringUtil.dencodeHTML(hiddenPrintScript);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRelate() {
		return relate;
	}

	public void setRelate(boolean relate) {
		this.relate = relate;
	}
	
	public String getReadOnlyScript() {
		return readOnlyScript;
	}

	public void setReadOnlyScript(String readOnlyScript) {
		this.readOnlyScript = StringUtil.dencodeHTML(readOnlyScript);
	}

	/**
	 * 输出选项卡中视图重计算脚本
	 * @param doc
	 * @param runner
	 * @param webUser
	 * @param hiddenAll
	 * @return
	 * @throws Exception
	 */
	public String toHtml4Refresh(Document doc, IRunner runner, WebUser webUser, boolean hiddenAll,int displayType) throws Exception {
		StringBuffer html = new StringBuffer();
		ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		String viewId =getFormId();
		view = (View) viewProcess.doView(viewId);
		if (view != null) {
			html.append("refreshField(\"").append(this.getFormId()).append("_divid").append("\",\"");
			html.append(this.getName()).append("\",\"").append(toFrameHtml(doc, runner, webUser,displayType)).append("\");");
		}
		
		return html.toString();
	}
	
	public String toHtml(Document doc, IRunner runner, WebUser webUser, boolean hiddenAll,int displayType) throws Exception {
		StringBuffer html = new StringBuffer();
		if(TYPE_FORM.equals(getType())){
			Form form = getForm();
			if(doc.getForm() != null){
				form.setShowLog(doc.getForm().isShowLog());
			}
			html.append(form.toCalctext(doc, runner, webUser, displayType));// 不打印版权信息
		}else if(TYPE_VIEW.equals(getType())){
			ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			String viewId =getFormId();
			view = (View) viewProcess.doView(viewId);
			if (view != null) {
				html.append("<table cellspacing='5' width='100%'><tr><td><p>");
				html.append("<span id='").append(this.getFormId()).append("_divid'").append(">");
				html.append(toFrameHtml(doc, runner, webUser,displayType));
				html.append("</span>");
				html.append("</p></td></tr>");
				html.append("</table>");
			}
			
		}
		
		
		
		return html.toString();
	}
	
	private String toFrameHtml(Document doc, IRunner runner, WebUser webUser,int displayType) throws Exception {
		return toFrameXml(doc, runner, webUser,displayType, "HTML");
	}

	private String toFrameXml(Document doc, IRunner runner, WebUser webUser,int displayType,String type) throws Exception {
		StringBuffer html = new StringBuffer();
		boolean disable = PermissionType.READONLY==displayType || PermissionType.DISABLED==displayType;
		if (type.equals("HTML")) {
			
			html.append("<input type='hidden' moduleType='IncludedView' id='" + view.getId() +"'");
			html.append(" userType = '" + (webUser instanceof PreviewUser)+"'");
			if(webUser instanceof PreviewUser)
				html.append(" skinType='"+((PreviewUser)webUser).getSkinType()+"'");
			html.append(" action='displayView.action'");
			html.append(" application='" + view.getApplicationid() + "'");
			html.append(" _viewid='" + view.getId() + "'");
			html.append(" _fieldid='" + view.getId() + "'");
			html.append(" _opentype='" + view.getOpenType() + "'");
			html.append(" parentid='");
			if(StringUtil.isBlank(doc.getId())){
				html.append("@");
			}else if("HOME_PAGE_ID".equals(doc.getId())){
				html.append("null");
				this.setRelate(false);
			}else{
				html.append(doc.getId());
			}
			html.append("' isRelate='" + this.isRelate() + "'");
			html.append(" divid='" + this.getName() + "_divid" + "'");
			html.append(" getEditMode='"+disable+"'");
			html.append(" isRefreshOnChanged='" + this.isRefreshOnChanged() + "'");
			html.append(" getName='" + this.getName()+ "'");
			html.append(" />");
		} else if (type.equals("XML")) {
			html.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE).append(
					"='" + ResourceType.RESOURCE_TYPE_MOBILE + "'").append(" ").append(MobileConstant.ATT_NAME).append("='"+this.name+"'");

			if (displayType == PermissionType.READONLY  || displayType == PermissionType.DISABLED) {
				//html.append(" ").append(MobileConstant.ATT_READONLY + "='true' ");
			}
			if (displayType == PermissionType.HIDDEN) {
				html.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
			}
			html.append(">");
			html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
					"='_viewid'>" + HtmlEncoder.encode(view.getId()) + "</").append(MobileConstant.TAG_PARAMETER)
					.append(">");

			if (doc.getId() != null && doc.getId().trim().length() > 0) {
				html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME)
						.append("='");
				if (this.isRelate()) { // 是否有父子关系
					html.append("isRelate'>" + this.isRelate() + "</").append(MobileConstant.TAG_PARAMETER).append(
					">");
					html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME)
					.append("='");
					html.append("parentid");
				} else {
					html.append("parentid");
				}
				html.append("'>" + HtmlEncoder.encode(doc.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(
						">");
			}
			if (this.isRefreshOnChanged()) {
				html.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME)
						.append("='refresh'>true</").append(MobileConstant.TAG_PARAMETER).append(">");
			}
			html.append("</").append(MobileConstant.TAG_ACTION).append(">");
		}
		return html.toString();
	}
	

	public String toMbXML(Document doc, IRunner runner, WebUser webUser, boolean hiddenAll) throws Exception {
		if (hiddenAll)
			return "";
		return getForm().toXMLCalctext(doc, runner, webUser, hiddenAll);
	}

	public String toPrintHtml(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return getForm().toPrintCalctext(doc, runner, webUser);
	}
	
	public String toPdfHtml(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return getForm().toPrintCalctext(doc, runner, webUser);
	}

	public Collection<FormField> getFields() {
		return getForm().getAllFields();
	}

	public Form getForm() {
		if (this.form == null) {
			try {
				//FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				FormProcess fp = new FormProcessBean(); //hotfix
				Form form = (Form) fp.doView(formId);

				this.form = form != null ? form : new Form();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.form;
	}
}
