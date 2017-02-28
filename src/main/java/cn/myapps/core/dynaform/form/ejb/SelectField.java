// Source file:
// C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\SelectField.java

package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 下拉框组件
 * 
 * @author Marky
 */
public class SelectField extends FormField implements ValueStoreField {
	
	private static final long serialVersionUID = -8469984129928896643L;

	protected static String cssClass = "select-cmd";

	/**
	 * 计算多值选项
	 * 
	 * @uml.property name="optionsScript"
	 */
	protected String optionsScript;

	/**
	 * 选项的编辑模式('00':设计 '01':脚本代码)
	 * 
	 * @uml.property name="optionsEditMode"
	 */
	protected String optionsEditMode;
	
	/**
	 * 选项设计模式关联的视图
	 * @uml.property name="dialogView"
	 */
	protected String dialogView = "";
	
	/**
	 * 选项真实值
	 * @uml.property name="optionsValue"
	 */
	protected String optionsValue = "";
	
	/**
	 * 选项显示值
	 * @uml.property name="optionsText"
	 */
	protected String optionsText = "";
	
	/**
	 * @roseuid 41ECB66D031D
	 */
	public SelectField() {

	}
/*
 * 

	protected boolean layer;

	public boolean isLayer() {
		return layer;
	}

	public void setLayer(boolean layer) {
		this.layer = layer;
	}
 * 
 */
	/**
	 * 获取多值选项脚本
	 * 
	 * @return 多值选项脚本
	 * @uml.property name="optionsScript"
	 */
	public String getOptionsScript() {
		return optionsScript;
	}

	/**
	 * 设置多值选项脚本
	 * 
	 * @param optionsScript
	 *            The optionsScript to set.
	 * @uml.property name="optionsScript"
	 */
	public void setOptionsScript(String optionsScript) {
		this.optionsScript = optionsScript;
	}

	/**
	 * 获取选项编辑模式
	 * @return
	 */
	public String getOptionsEditMode() {
		return optionsEditMode;
	}
	
	/**
	 * 设置选项编辑模式
	 * @param optionsEditMode
	 */
	public void setOptionsEditMode(String optionsEditMode) {
		this.optionsEditMode = optionsEditMode;
	}
	
	/**
	 * 获取所引用的视图
	 * 
	 * @return 引用的视图
	 * @uml.property name="dialogView"
	 */
	public String getDialogView() {
		return dialogView;
	}

	/**
	 * 设置引用的视图
	 * 
	 * @param dialogView
	 *            引用的视图
	 * @uml.property name="dialogView"
	 */
	public void setDialogView(String dialogView) {
		this.dialogView = dialogView;
	}

	/**
	 * 获取选项真实值
	 * @return
	 */
	public String getOptionsValue() {
		return optionsValue;
	}
	/**
	 * 设置选项真实值
	 * @param optionsValue
	 */
	public void setOptionsValue(String optionsValue) {
		this.optionsValue = optionsValue;
	}
	/**
	 * 获取选项显示值
	 * @return
	 */
	public String getOptionsText() {
		return optionsText;
	}
	/**
	 * 设置选项显示值
	 * @param optionsText
	 */
	public void setOptionsText(String optionsText) {
		this.optionsText = optionsText;
	}
	/**
	 * 重新计算SelectField.
	 * 
	 * @roseuid 41DB89D700F9
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
		getLog().debug("SelectField.recalculate");
		runValueScript(runner, doc);
		runOptionsScript(runner, doc, webUser);
	}

	/**
	 * 根据CheckboxField的显示类型不同,返回的结果字符串不同.
	 * 新建的Document,SelectField的显示类型为默认的MODIFY。
	 * 此时根据Form模版的SelectField内容结合Document的Item的值,返回的字符串为重定义后的html.
	 * 根据流程节点设置对应SelectField的显示类型不同,返回的结果字符串不同.
	 * 
	 * 1)节点设置对应SelectField的显示类型为MODIFY,
	 * 并根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,返回重定义后的html.
	 * 2)节点设置对应SelectField的显示类型为HIDDEN,返回"******".
	 * 3)节点设置对应SelectField的显示类型为READONLY,Document的ITEM存放的值为只读.
	 * 并根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,返回重定义后的html.
	 * 4)节点设置对应SelectField的显示类型为DISABLED,Document的ITEM存放的值为DISABLED.
	 * 并根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,返回重定义后的html.
	 * 
	 * 通过强化HTML的单值选项标签及语法，表达列表框的布局、属性、事件等。
	 * 
	 * @param doc
	 *            文档对象
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 字符串
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser, permissionType);

		if (doc != null) {
			try {
				html.append("<span moduleType='select'");
				html.append(toOtherpropsHtml());
				html.append(toAttr(doc, displayType));
				html.append(">");
				html.append(runOptionsScript(runner, doc, webUser));
				html.append("</span>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return html.toString();
	}

	/**
	 * 根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,输出重定义后的html文本以网格显示
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @param columnOptionsCache           
	 *             缓存控件选项(选项设计模式构建的Options)的Map
	 *             key: fieldId
	 *             value: Options
	 * @return 重定义后的html文本
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				try {
					html.append("<span moduleType='select'");
					html.append(" _subGridView='true'");
					html.append(toOtherpropsHtml());
					html.append(toAttr(doc, displayType));
					html.append(">");
					html.append(runOptionsScript(runner, doc, webUser, "HTML", columnOptionsCache));
					html.append("</span>");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return html.toString();
	}

	private String toAttr(Document doc, int displayType) {
		StringBuffer html = new StringBuffer();
		html.append(" _id='" + getFieldId(doc) + "'");
		html.append(" _name = '" + getName() + "'");
		html.append(" _fieldType='" + getTagName() + "'");
		html.append(" _displayType = '" + displayType + "'");
		html.append(" _isRefreshOnChanged='" + isRefreshOnChanged() + "'");
		html.append(" _textType='" + getTextType() + "'");
		html.append(" _hiddenValue='" + getHiddenValue() + "'");
		//表单描述字段
		html.append(" _discript ='" + getDiscript() + "'");
		
		return html.toString();
	}

	/**
	 * @param tmpltStr
	 * @return FormField
	 * @roseuid 41ECB66D0381
	 */
	public FormField init(String tmpltStr) {
		return null;
	}

	/**
	 * 获取模板描述下拉选项
	 * 
	 * @return 模板描述下拉选项
	 * @roseuid 41E7917A033F
	 */
	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<select'");
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
		template.append(" optionScript='" + getOptionsScript() + "'");
		template.append(" onRefresh='" + isCalculateOnRefresh() + "'");
		template.append("/>");
		return template.toString();

	}

	/**
	 * 返回执行多值选项脚本后重定义后的html，通过强化HTML标签及语法，表达下拉选项的布局、属性、事件、样式、等。
	 * 
	 * @param runner
	 *            AbstractRunner<code>(执行脚本接口类)</code>
	 * @see cn.myapps.core.macro.runner.JavaScriptRunner#run(String, String)
	 * 
	 * @param doc
	 *            文档对象
	 * @return 字符串内容为重定义后的html的下拉选框标签及值
	 * @throws Exception
	 */
	public String runOptionsScript(IRunner runner, Document doc, WebUser webUser) throws Exception {
		return runOptionsScript(runner, doc, webUser, "HTML", null);
	}

	private String runOptionsScript(IRunner runner, Document doc, WebUser webUser, String stringType, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();
		Options options = columnOptionsCache != null ? columnOptionsCache.get(this.getId()) : null;
		if(options == null){
			options = getOptions(runner, doc, webUser);
			if(columnOptionsCache != null && FormField.EDITMODE_VIEW.equals(optionsEditMode)){
				//选项是设计模式的时候才加入缓存
				columnOptionsCache.put(this.getId(), options);
			}
		}

		if (stringType.equals("HTML")) {
			if (options != null) {
				return toOptionForHtml(options, doc);
			}
		} else if (stringType.equals("XML")) {
			if (options != null) {
				return toOptionForXml(options, doc);
			} else {
				html.append("<").append(MobileConstant.TAG_OPTION).append(">");
				html.append("</").append(MobileConstant.TAG_OPTION).append(">");
			}
		}else if (stringType.equals("XML2")){
			if (options != null) {
				return toOptionForXml2(options, doc);
			} else {
				html.append("<").append(MobileConstant2.TAG_OPTION).append("></").append(MobileConstant2.TAG_OPTION).append(">");
			}
		}
		return html.toString();
	}

	private Options getOptions(IRunner runner, Document doc, WebUser webUser) throws Exception {
		Object result = null;
		Options options = null;
		if(FormField.EDITMODE_VIEW.equals(optionsEditMode)){
			//设计模式获取options
			options = getOptionsByDesign(webUser);
		}else{
			//脚本模式获取options
			if (getOptionsScript() != null && getOptionsScript().trim().length() > 0) {
	
				result = runner.run(getScriptLable("OptionsScript"), StringUtil.dencodeHTML(getOptionsScript()));
				if (result != null && result instanceof String) {
	
					String[] strlst = ((String) result).split(";");
					options = new Options();
					for (int i = 0; i < strlst.length; i++) {
						options.add(strlst[i], strlst[i]);
					}
				} else if (result instanceof Options) {
					options = (Options) result;
				}
			}
		}
		return options;
	}

	/**
	 * from 2.5sp9
	 * 返回执行多值选项脚本后重定义后的xml，通过强化HTML标签及语法，表达下拉选项的布局、属性、事件、样式、等。
	 * 
	 * @param runner
	 *            AbstractRunner<code>(执行脚本接口类)</code>
	 * @see cn.myapps.core.macro.runner.JavaScriptRunner#run(String, String)
	 * 
	 * @param doc
	 *            文档对象
	 * @return 字符串内容为重定义后的xml的下拉选框标签及值
	 * @throws Exception
	 */
	private String runOptionsScriptToXML2(IRunner runner, Document doc, WebUser webUser) throws Exception {
		return runOptionsScript(runner, doc, webUser, "XML2", null);
	}
	
	/**
	 * 返回执行多值选项脚本后重定义后的xml，通过强化HTML标签及语法，表达下拉选项的布局、属性、事件、样式、等。
	 * 
	 * @param runner
	 *            AbstractRunner<code>(执行脚本接口类)</code>
	 * @see cn.myapps.core.macro.runner.JavaScriptRunner#run(String, String)
	 * 
	 * @param doc
	 *            文档对象
	 * @return 字符串内容为重定义后的xml的下拉选框标签及值
	 * @throws Exception
	 */
	private String runOptionsScriptToXML(IRunner runner, Document doc, WebUser webUser) throws Exception {
		return runOptionsScript(runner, doc, webUser, "XML", null);
	}

	/**
	 * 根据打印时对应SelectField的显示类型不同,默认为MODIFY,返回的结果字符串不同.
	 * 若Document不为空且打印时对应SelectField的显示类型不为HIDDEN且字段类型不为HIDDEN,
	 * <P>
	 * 并根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,返回重定义后的打印html文本. 否则为空字符串.
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            AbstractRunner(执行脚本的接口类)
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * 
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的打印html为Form模版的SelectField组件内容结合Document中的ITEM存放的值
	 * @throws Exception
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		String html = "";
		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				html = this.getPrintHiddenValue();
			}else{
				html = "<span>" + getText(doc, runner, webUser) + "</span>";
			}
		}
		return html;
	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		String html = "";
		if (doc != null) {
				html = getText(doc, runner, webUser);
		}
		return html;
	}
	private String toOptionForHtml(Options options, Document doc) {
		StringBuffer html = new StringBuffer();
		Object value = null;
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			if (item != null)
				value = item.getValue();
		}
		Iterator<Option> iter = options.getOptions().iterator();
		while (iter.hasNext()) {
			Option element = (Option) iter.next();
			if (element.getValue() != null) {
				html.append("<span style='display:none;' ");
				if (value != null && element.getValue() != null) {
					if (value.equals(element.getValue())) {
						html.append(" isSelected=\"true\"");
					}
				} else {
					if (element.isDef()) {
						html.append(" isSelected=\"true\"");
					}
				}
				html.append(" cssClass='" + cssClass + "'");
				html.append(" value=\"");
			}
			html.append(HtmlEncoder.encode(element.getValue()));
			html.append("\"");
			html.append(">");
			html.append(element.getOption()).append("</span>");
		}
		return html.toString();
	}

	public boolean isRender(String destVal, String origVal) {
		if (optionsScript != null && optionsScript.trim().length() > 0) {
			return true;
		}
		return super.isRender(destVal, origVal);
	}

	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			xmlText.append("<").append(MobileConstant.TAG_SELECTFIELD);
			xmlText.append(" ").append(MobileConstant.ATT_ID + "='" + getId() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_NAME + "='" + getName() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant.ATT_READONLY + "='true' ");
			}
			if (displayType == PermissionType.HIDDEN || (getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}

			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant.ATT_REFRESH).append(" ='true' ");
			}
			xmlText.append(">");
			try {
				xmlText.append(runOptionsScriptToXML(runner, doc, webUser));
				xmlText.append("</").append(MobileConstant.TAG_SELECTFIELD + ">");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return xmlText.toString();
	}
	
	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			xmlText.append("<").append(MobileConstant2.TAG_SELECTFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_ID).append("='").append(getId()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
			}
			if (displayType == PermissionType.HIDDEN || (getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
				}
			}

			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
			}
			xmlText.append(">");
			try {
				xmlText.append(runOptionsScriptToXML2(runner, doc, webUser));
				xmlText.append("</").append(MobileConstant2.TAG_SELECTFIELD).append(">");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return xmlText.toString();
	}

	private String toOptionForXml(Options options, Document doc) {
		StringBuffer html = new StringBuffer();

		Object value = null;
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			if (item != null)
				value = item.getValue();
		}

		Iterator<Option> iter = options.getOptions().iterator();
		int count = 0;
		boolean flag = true;
		while (iter.hasNext()) {
			Option element = (Option) iter.next();
			if (element.getValue() != null) {
				html.append("<").append(MobileConstant.TAG_OPTION).append("");
				if (flag) {
					if (value != null && element.getValue() != null) {
						if (value.equals(element.getValue())) {
							html.append(" ").append(MobileConstant.ATT_SELECTED).append("='" + count + "'");
							flag = false;
						}
					} else {
						if (element.isDef()) {
							html.append(" ").append(MobileConstant.ATT_SELECTED).append("='" + count + "'");
							flag = false;
						}
					}
				}
				html.append(" ").append(MobileConstant.ATT_VALUE).append("='");

				html.append(HtmlEncoder.encode(element.getValue()));
				html.append("'");

				html.append(">");

				if (element.getOption() != null && !element.getOption().trim().equals(""))
					html.append(HtmlEncoder.encode(element.getOption()));
				else
					html.append("{*[Select]*}");
				html.append("</").append(MobileConstant.TAG_OPTION).append(">");
				count++;
			}
		}
		return html.toString();
	}
	
	private String toOptionForXml2(Options options, Document doc) {
		StringBuffer html = new StringBuffer();

		Object value = null;
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			if (item != null)
				value = item.getValue();
		}

		Iterator<Option> iter = options.getOptions().iterator();
		boolean flag = true;
		while (iter.hasNext()) {
			Option element = (Option) iter.next();
			if (element.getValue() != null) {
				html.append("<").append(MobileConstant2.TAG_OPTION);
				if (flag) {
					if (value != null && element.getValue() != null) {
						if (value.equals(element.getValue())) {
							html.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
							flag = false;
						}
					} else {
						if (element.isDef()) {
							html.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
							flag = false;
						}
					}
				}
				html.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(HtmlEncoder.encode(element.getValue())).append("'");
				html.append(">");

				if (element.getOption() != null && !element.getOption().trim().equals(""))
					html.append(HtmlEncoder.encode(element.getOption()));
				else
					html.append("");
				html.append("</").append(MobileConstant2.TAG_OPTION).append(">");
			}
		}
		return html.toString();
	}

	/**
	 * 获取字段显示值
	 * 
	 * @param doc 文档
 	 * @param runner 脚本运行器
 	 * @param webUser webuser
	 * @return
	 * @throws Exception
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return getText(doc, runner, webUser, null);
	}
	/**
	 * 获取Form模版的SelectField的选项脚本，通过动态语言执行器执行脚本后生成下拉选项,并验证下拉选项的可见性,如果可见就显示正常,否则显示
	 * "******"
	 * 
	 * @param runner
	 *            AbstractRunner(执行脚本的接口类)
	 * @param doc
	 *            (Document)文档对象
	 * @param webUser
	 *            webUser
	 * @param columnOptionsCache           
	 *             缓存控件选项(选项设计模式构建的Options)的Map
	 *             key: fieldId
	 *             value: Options
	 * @return 执行后生成的下拉选项
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		if (!StringUtil.isBlank(doc.getParentid()) && getDisplayType(doc, runner, webUser) == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		}
		Options options = columnOptionsCache != null ? columnOptionsCache.get(this.getId()) : null;
		if(options == null){
			options = getOptions(runner, doc, webUser);
			if(columnOptionsCache != null && FormField.EDITMODE_VIEW.equals(optionsEditMode)){
				//选项是设计模式的时候才加入缓存
				columnOptionsCache.put(this.getId(), options);
			}
		}
		if (options != null) {
			for (Iterator<Option> iterator = options.getOptions().iterator(); iterator.hasNext();) {
				Option option = (Option) iterator.next();
				if (option.getValue().equals(doc.getItemValueAsString(getName()))) {
					return option.getOption();
				}
			}
		}

		return "";
	}
	
	/**
	 * 选项设计模式下获取options
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Options getOptionsByDesign(WebUser user) throws Exception{
		ViewProcess vp = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		View viewVO =(View)vp.doView(dialogView);
		Options options = new Options();
		if(viewVO != null){
			DataPackage<Document> dataPackage = viewVO.getViewTypeImpl()
					.getViewDatas(new ParamsTable(), 1, Integer.MAX_VALUE, user, new Document());
			if(dataPackage !=null && dataPackage.datas.size()>0){
				Column optionsValueColumn = new Column();
				Column optionsTextColumn = new Column();
				Collection<Column> columns = viewVO.getColumns();
				for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext();) {
					Column col = (Column) iterator.next();
					if(col.getId().equals(optionsValue)){
						optionsValueColumn = col;
					}
					if(col.getId().equals(optionsText)){
						optionsTextColumn = col;
					}
				}
				IRunner runner = JavaScriptFactory.newInstance(viewVO.getApplicationid());
				for(Iterator<Document> iter = dataPackage.datas.iterator();iter.hasNext();){
					Document doc = iter.next();
					options.add(this.createOption(runner, doc, optionsValueColumn, optionsTextColumn, new ParamsTable(), user));
				}
			}
		}
		return options;
	}
	
	private Option createOption(IRunner runner, Document doc,Column optionsValueColumn,Column optionsTextColumn, ParamsTable params, WebUser user) throws Exception {
		runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
		Option option = new Option();
		Object resultValue = optionsValueColumn.getText(doc, runner, user);
		Object resultText = optionsTextColumn.getText(doc, runner, user);
		option.setValue(resultValue.toString());
		option.setOption(resultText.toString());
		return option;
	}
}
