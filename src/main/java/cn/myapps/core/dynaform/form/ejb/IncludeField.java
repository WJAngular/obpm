//Source file:
//C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\InputField.java

package cn.myapps.core.dynaform.form.ejb;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;

/**
 * @author nicholas
 * @uml.dependency supplier="cn.myapps.core.dynaform.form.ejb.IncludedElement"
 *                 stereotypes="Omondo::Import"
 */
public class IncludeField extends FormField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7324947435225302640L;

	public static final String INCLUDE_TYPE_VIEW = "0";

	public static final String INCLUDE_TYPE_PAGE = "1";

	/**
	 * @uml.property name="includeType"
	 */
	protected String includeType;

	/**
	 * @uml.property name="integratePage"
	 */
	protected boolean integratePage;

	/**
	 * @uml.property name="enabled"
	 */
	protected boolean enabled;

	/**
	 * 是否关联, 是则为父子关系, 非则没有关系
	 */
	protected boolean relate = true;
	
	/**
	 * @uml.property name="fixation"
	 */
	protected boolean fixation;
	
	public boolean isFixation() {
		return fixation;
	}

	public void setFixation(boolean fixation) {
		this.fixation = fixation;
	}

	/**
	 * @uml.property name="fixationHeight"
	 */
	protected String fixationHeight = "";

	public String getFixationHeight() {
		return fixationHeight;
	}

	public void setFixationHeight(String fixationHeight) {
		this.fixationHeight = fixationHeight;
	}

	private IncludedElement element;

	/**
	 * @roseuid 41ECB66E012A
	 */
	public IncludeField() {

	}

	public ValidateMessage validate(IRunner bsf, Document doc) throws Exception {
		return null;
	}

	/**
	 * 根据includeField的显示类型不同,返回的结果字符串不同.
	 * 新建的Document,includeField的显示类型为默认的MODIFY。此时根据Form模版的includeField内容结合Document的Item的值,返回的字符串为重定义后的html.
	 * 根据流程节点设置对应includeField的显示类型不同,返回的结果字符串各不同.
	 * 1)若节点设置对应includeField为DISABLED,返回重定义后的html的includeField为DISABLED.
	 * 否则重定义后的html为根据设置includeField组件的includeType属性的值类型与enabled属性的值来决定定义的内容。
	 * <p>
	 * include值类型两种常量为：INCLUDE_TYPE_VIEW(值为"0")，INCLUDE_TYPE_PAGE(值为"1").
	 * enabled属性的值为true|false.默认为false.
	 * <p>
	 * 有三种不同的返回值：
	 * <p>
	 * 1）includeField组件的includeType属性设置为view而enabled属性设置为true时，
	 * 若相应的Document没有数据记录时，返回重定义后的html为空字符.
	 * 否则Form模版的includeField组件内容结合Document中的ITEM存放的值,返回重定义后的html.
	 * <p>
	 * 2) includeField组件的includeType属性设置为view而enabled属性设置为false时,
	 * Form模版的includeField组件内容结合Document中的ITEM存放的值,返回重定义后的html.
	 * <p>
	 * <p>
	 * 3）includeField组件的includeType属性设置为pag时，若没有找到相应的page,返回空字符.
	 * 否则page模版的includeField组件内容结合Document中的ITEM存放的值,返回重定义后的html.
	 * <p>
	 * 通过强化HTML标签及语法，表达includeType的布局、属性、事件、样式、等。
	 * 
	 * @param doc
	 *            文档对象
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的html
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else
			return element.toHtmlTxt(doc, runner, webUser);
	}

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return element.toPrintHtmlTxt(doc, runner, webUser);
	}
	
	

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		return element.toPdfHtmlTxt(doc, runner, webUser);
	}

	public String getEditMode(IRunner runner, Document doc, WebUser webUser) throws Exception {
		int displayType = getDisplayType(doc, runner, webUser);

		String editmode = "true";
		if (displayType == PermissionType.DISABLED || displayType == PermissionType.READONLY) {
			editmode = "false";
		}
		return editmode;
	}

	/**
	 * 返回模板描述IncludeField
	 * 
	 * @return java.lang.String
	 * @roseuid 41E7917A033F
	 */
	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<span'");
		template.append(" className='" + this.getClass().getName() + "'");
		template.append(" id='" + getId() + "'");
		template.append(" name='" + getName() + "'");
		template.append(" formid='" + getFormid() + "'");
		template.append(" discript='" + getDiscript() + "'");
		template.append(" refreshOnChanged='" + isRefreshOnChanged() + "'");
		template.append(" validateRule='" + getValidateRule() + "'");
		template.append(" hiddenScript='" + getHiddenScript() + "'");
		template.append("/>");
		return template.toString();
	}

	/**
	 * 获取是否整合到页面. true为整合到页面，false为不整合到页面,默认为不整合到页面
	 * 
	 * @return true or false.
	 * @uml.property name="integratePage"
	 */
	public boolean isIntegratePage() {
		return integratePage;
	}

	/**
	 * 设置是否整合到页面. true为整合到页面，false为不整合到页面,默认为不整合到页面.
	 * 
	 * @param integratePage
	 *            boolean
	 * @uml.property name="integratePage"
	 */
	public void setIntegratePage(boolean integratePage) {
		this.integratePage = integratePage;
	}

	/**
	 * 返回include Type 类型:（0:'View',1:'Page'）.
	 * 
	 * @return include Type 类型:（0:'View',1:'Page'）
	 * @uml.property name="includeType"
	 */

	public String getIncludeType() {
		return includeType;
	}

	/**
	 * 返回没有Document时是否显示VIEW, true为不显示,默认显示
	 * 
	 * @return
	 * @uml.property name="enabled"
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 设置没有Document时是否显示VIEW, true为不显示,默认显示
	 * 
	 * @param enabled
	 * @uml.property name="enabled"
	 */

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 设置 include Type 类型:（0:'View',1:'Page'）
	 * 
	 * @param includeType
	 * @uml.property name="includeType"
	 */
	public void setIncludeType(String includeType) {
		if (includeType.equals(INCLUDE_TYPE_PAGE)) {
			element = new IncludedPage(this);
		} else if (includeType.equals(INCLUDE_TYPE_VIEW)) {
			element = new IncludedView(this);
		}
		this.includeType = includeType;
	}

	/**
	 * 获取包含的视图
	 * 
	 * @return
	 * @throws Exception
	 */
	public View getIncludeView() throws Exception {
		if (includeType.equals(INCLUDE_TYPE_VIEW)) {
			IRunner runner = JavaScriptFactory.getInstance("", get_form().getApplicationid());
			IncludedElement element = new IncludedView(this);
			return (View) element.getValueObject(runner);
		}

		return null;
	}

	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return element.toXMLTxt(doc, runner, webUser);
	}

	public boolean isRelate() {
		return relate;
	}

	public void setRelate(boolean relate) {
		this.relate = relate;
	}

	/**
	 * 重新计算
	 * 
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
		getLog().debug("IncludeField.recalculate");
		runValueScript(runner, doc);
	}
	
	public String getRefreshScript(IRunner runner, Document doc, WebUser webUser) throws Exception {
		return super.getRefreshScript(runner, doc, webUser);
	}
	
	/**
	 * 获取Field显示类型. 分别：1.只读(READONLY)2.修改(MODIFY),3.隐藏(HIDDEN),4.屏蔽(DISABLED)
	 * 
	 * @param doc
	 *            (Document)文档对象
	 * @param runner
	 *            动态语言执行器
	 * @param webUser
	 *            web用户
	 * @return 字段显示类型
	 * @throws Exception
	 */
	public int getDisplayType(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {

		// 根据流程中定义的权限检索字段
		int fieldPermission = PermissionType.MODIFY;
		if (doc.getFieldPermList(webUser) != null) {
			fieldPermission = doc.getFieldPermList(webUser).checkPermission(this);
		}

		// 第一优先级为hidden
		if (fieldPermission == PermissionType.HIDDEN || runBooleanScript(runner, "hiddenScript", getHiddenScript())){
//				|| checkRolePermission(webUser, OperationVO.FORMFIELD_HIDDEN)) {//字段暂无权限配置 因此注释
			return PermissionType.HIDDEN;
		}
		// 第二优先级为disable
		if (!doc.isEditAble(webUser)){
//				|| checkRolePermission(webUser, OperationVO.FORMFIELD_DISABLED)) {//字段暂无权限配置 因此注释
			return PermissionType.DISABLED;
		}
		// 第三优先级为readonly
		if (fieldPermission == PermissionType.READONLY
				|| runBooleanScript(runner, "readOnlyScript", getReadonlyScript()) ){
//				|| checkRolePermission(webUser, OperationVO.FORMFIELD_READONLY)) {//字段暂无权限配置 因此注释
			return PermissionType.READONLY;
		}

		return fieldPermission;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		return element.toXMLTxt2(doc, runner, webUser);
	}
}
