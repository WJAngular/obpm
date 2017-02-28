//Source file:
//C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\SelectField.java

package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.AbstractRunner;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringList;
import cn.myapps.util.StringUtil;

/**
 * Checkbox组件
 * 
 * @author nicholas
 */
public class CheckboxField extends FormField implements ValueStoreField {
	protected final static Logger log = Logger.getLogger(CheckboxField.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -3929760258362916386L;

	protected static String cssClass = "checkbox-cmd";

	/**
	 * 计算多值选项
	 */
	protected String optionsScript;

	/**
	 * 换行个数
	 */
	protected String newlineCount = "3";

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
	 * 构造函数
	 * 
	 * @roseuid 41ECB66D031D
	 */
	public CheckboxField() {

	}

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
	 * 执行CheckboxField值勤脚本,重新计算CheckboxField.
	 * 
	 * @roseuid 41DB89D700F9
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
		log.debug("CheckboxField.recalculate");
		runValueScript(runner, doc);
		runOptionsScript(runner, doc, webUser);
	}

	/**
	 * 
	 * Form模版的复选框组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的html，
	 * 通过强化HTML标签及语法，表达复选框(checkbox)的属性、事件、样式、值等。
	 * 
	 * @param doc
	 *            文档对象
	 * @see cn.myapps.core.dynaform.form.ejb.CheckboxField#runOptionsScript(AbstractRunner,
	 *      Document, WebUser)
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的html的复选框
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) {
		StringBuffer html = new StringBuffer();
		try {
			html.append(runOptionsScript(runner, doc, webUser));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return html.toString();
	}

	/**
	 * 生成模块描述
	 */
	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<input type='radio'");
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
		template.append("/>");
		return template.toString();

	}

	/**
	 * 根据CheckboxField的显示类型不同,返回的结果字符串不同.
	 * 新建的Document,CheckboxField的显示类型为默认的MODIFY。此时根据Form模版的CheckboxField内容结合Document的Item的值,返回的字符串为重定义后的html.
	 * 同时流程节点设置对应CheckboxField的显示类型不同,返回的结果字符串不同.
	 * 1)若节点设置对应CheckboxField的显示类型为MODIFY.执行多值选项脚本后并根据Form模版的CheckboxField内容结合Document的item值,返回重定义后的html
	 * 2)若节点设置对应CheckboxField的显示类型为DISABLED或READONLY,
	 * 并根据Form模版的CheckboxField内容结合Document的item值且ITEM存放的值被DISABLED,返回重定义后的html.
	 * 通过强化HTML标签及语法，表达CheckboxField的布局、属性、事件、样式、等。 否则返回"******"字符串.
	 * 
	 * @param runner
	 *            AbstractRunner(执行脚本接口类)
	 * @param doc
	 *            文档
	 * @param webUser
	 * @return 字符串内容为html多值选项标签和语法
	 * @throws Exception
	 */
	public String runOptionsScript(IRunner runner, Document doc, WebUser webUser) throws Exception {
		//Object result = null;
		int displayType = getDisplayType(doc, runner, webUser);

		Options options = getOptions(runner, doc, webUser);

		if (options != null) {
			StringBuffer html = new StringBuffer();

			Collection<String> checkedList = getCheckedList(runner, doc);

			if (doc != null) {
				Iterator<Option> iter = options.getOptions().iterator();

				html.append("<span moduleType='checkbox'");
				html.append(" _id='" + getFieldId(doc) + "'");
				html.append(" _name='" + this.getName() + "'");
				html.append(" _displayType='"+ displayType +"'");
				html.append(" _textType='" + getTextType() + "'");
				html.append(" _hiddenValue='" + getHiddenValue() + "'");
				html.append(" _isRefreshOnChanged='" + isRefreshOnChanged() + "'");
				html.append(" _class='" + cssClass + "'");
				html.append(" _getLayout='" + this.getLayout() + "'");
				//表单描述字段
				html.append(" _discript ='" + getDiscript() + "'");
				html.append(" classname ='" + this.getClass().getName() + "'");
				html.append(toOtherpropsHtml());
				html.append(" >");
				while (iter.hasNext()) {
					Option element = (Option) iter.next();
					html.append("<span style='display:none;'");
					html.append(" value=\"" + HtmlEncoder.encode(element.getValue()) + "\"");
					html.append(" text='" + element.getOption() + "'");
					html.append(" isDef ='" + element.isDef() + "'");
					html.append(" checkedListSize='" + checkedList.size()+"'");
					html.append(" checkedListContains='" + checkedList.contains(element.getValue()) + "'");
					html.append(" >");
					html.append("</span>");
				}
				html.append("</span>");
				return html.toString();
			}
			}
		return "";
	}

	/**
	 * 根据文档的字段获取选中的列表
	 * 
	 * @param runner
	 *            动态语言执行器
	 * @param doc
	 *            文档
	 * @return 选中列表的集合
	 */
	public Collection<String> getCheckedList(IRunner runner, Document doc) {
		try {
			String checkedListStr = doc.getItemValueAsString(this.getName());

			if (!StringUtil.isBlank(checkedListStr)) {
				return Arrays.asList(checkedListStr.split(";"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<String>();
	}
	
	

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		if (doc != null) {
			Options options = getOptions(runner, doc, webUser);

			if (options != null) {
				Object value = null;
				StringList valueList = null;
				StringBuffer html = new StringBuffer();
				Item item = doc.findItem(this.getName());

				if (item != null)
					value = item.getValue();
				if (value != null)
					valueList = new StringList((String) value, ';');

				Iterator<Option> iter = options.getOptions().iterator();
				html.append("<SPAN >");
				StringBuffer val = new StringBuffer();
				while (iter.hasNext()) {
					Option element = (Option) iter.next();

					if (valueList != null && element.getValue() != null) {
						if (valueList.indexOf(element.getValue()) >= 0) {
							if (this.getLayout() != null && this.getLayout().equalsIgnoreCase("vertical")) {
								val.append(element.getOption()).append("<br>");
							} else {
								val.append(element.getOption()).append(";");
							}
						}
					}
				}
				if(val.length()>0){
					val.setLength(val.length()-1);
				}else{
					val.append("&nbsp;");
				}
				html.append(val);
				html.append("</SPAN>");
				html.append( printHiddenElement(doc));
				return html.toString();
			}
		}
		return "";
	}

	/**
	 * 根据打印时对应CheckboxField的显示类型不同,默认为MODIFY,返回的结果字符串不同.
	 * 打印时对应CheckboxField的显示类型默认为MODIFY.
	 * 若Document不为空且打印时对应CheckboxField的显示类型不为HIDDEN,
	 * <p>
	 * 并根据Form模版的CheckboxField(多值选项组件)内容结合Document中的ITEM存放的值,返回重定义后的打印html文本.
	 * 否则为空字符串.
	 * 
	 * @param doc
	 *            Document
	 * @param runner
	 *            动态语言执行器
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * 
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return Form模版的多值选项组件内容结合Document中的ITEM存放的值为重定义后的打印html
	 * @throws Exception
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		//Object result = null;

		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);
			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}

			Options options = getOptions(runner, doc, webUser);

			if (options != null) {
				Object value = null;
				StringList valueList = null;
				StringBuffer html = new StringBuffer();
				Item item = doc.findItem(this.getName());

				if (item != null)
					value = item.getValue();
				if (value != null)
					valueList = new StringList((String) value, ';');

				Iterator<Option> iter = options.getOptions().iterator();
				html.append("<SPAN>");
				StringBuffer val = new StringBuffer();
				while (iter.hasNext()) {
					Option element = (Option) iter.next();

					if (valueList != null && element.getValue() != null) {
						if (valueList.indexOf(element.getValue()) >= 0) {
							if (this.getLayout() != null && this.getLayout().equalsIgnoreCase("vertical")) {
								val.append(element.getOption()).append("<br>");
							} else {
								val.append(element.getOption()).append(";");
							}
						}
					}
				}
				if(val.length()>0){
					val.setLength(val.length()-1);
				}else{
					val.append("&nbsp;");
				}
				html.append(val);
				html.append("</SPAN>");
				return html.toString();
			}
		}
		return "";
	}

	/**
	 * 
	 */
	public boolean isRender(String destVal, String origVal) {
		if (optionsScript != null && optionsScript.trim().length() > 0) {
			return true;
		}
		return super.isRender(destVal, origVal);
	}

	/**
	 * 生成手机所支持的标记,只提供给手机客端使用
	 */
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {

		try {
			/*
			 * <CheckBoxField label=’Field Label’> <Option>Option Text1</Option>
			 * <Option>Option Text2</Option> </CheckBoxField>
			 */

			//Object result = null;
			int displayType = getDisplayType(doc, runner, webUser);

			Options options = getOptions(runner, doc, webUser);

			if (options != null) {
				StringBuffer xmlText = new StringBuffer();

				Collection<String> checkedList = getCheckedList(runner, doc);

				if (doc != null) {
					xmlText.append("<").append(MobileConstant.TAG_CHECKBOXFIELD).append(" ").append("").append(
							MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("' ").append(MobileConstant.ATT_NAME)
							.append(" ='").append(getName()).append("'");

					if (isRefreshOnChanged()) {
						xmlText.append(" ").append(MobileConstant.ATT_REFRESH).append("='true' ");
					}

					if ((this.getTextType() != null && this.getTextType().equalsIgnoreCase("READONLY"))||
							displayType == PermissionType.DISABLED || displayType == PermissionType.READONLY) {
						xmlText.append(" ").append(MobileConstant.ATT_READONLY).append("='true'");
					}
					if (displayType == PermissionType.HIDDEN) {
						xmlText.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
						if(!getHiddenValue().equals("") && !getHiddenValue().equals(null)  && !getHiddenValue().equals("&nbsp;")){
							xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
						}
					}

					xmlText.append(">");

					Iterator<Option> iter = options.getOptions().iterator();
					int count = 0;
					while (iter.hasNext()) {

						Option element = (Option) iter.next();

						xmlText.append("<").append(MobileConstant.TAG_OPTION).append(" ").append(MobileConstant.ATT_VALUE)
								.append(" = ");

						xmlText.append("'");
						xmlText.append(HtmlEncoder.encode(element.getValue()));
						xmlText.append("'");
						if (checkedList.size() > 0) {
							if (checkedList.contains(element.getValue())) {
								xmlText.append(" ").append(MobileConstant.ATT_SELECTED).append("='" + count + "'");
							}
						} else {
							if (element.isDef()) {
								xmlText.append(" ").append(MobileConstant.ATT_SELECTED).append("='" + count + "'");
							}
						}
						xmlText.append(">");

						xmlText.append(HtmlEncoder.encode(element.getOption()));
						xmlText.append("</").append(MobileConstant.TAG_OPTION).append(">");
					}

					xmlText.append("</").append(MobileConstant.TAG_CHECKBOXFIELD).append(">");

					return xmlText.toString();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		try {
			/*
			 * <CheckBoxField label=’Field Label’> <Option>Option Text1</Option>
			 * <Option>Option Text2</Option> </CheckBoxField>
			 */
			//Object result = null;
			int displayType = getDisplayType(doc, runner, webUser);

			Options options = getOptions(runner, doc, webUser);

			if (options != null) {
				StringBuffer xmlText = new StringBuffer();

				Collection<String> checkedList = getCheckedList(runner, doc);

				if (doc != null) {
					xmlText.append("<").append(MobileConstant2.TAG_CHECKBOXFIELD);
					xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");
					xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
					xmlText.append(" ").append(MobileConstant2.ATT_LAYOUT).append("='").append(getLayout()).append("'");

					if (isRefreshOnChanged()) {
						xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
					}

					if ((this.getTextType() != null && this.getTextType().equalsIgnoreCase("READONLY"))||
							displayType == PermissionType.DISABLED || displayType == PermissionType.READONLY) {
						xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
					}
					if (displayType == PermissionType.HIDDEN) {
						xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
						if(!getHiddenValue().equals("") && !getHiddenValue().equals(null)  && !getHiddenValue().equals("&nbsp;")){
							xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
						}
					}
					xmlText.append(">");

					Iterator<Option> iter = options.getOptions().iterator();
					int count = 0;
					while (iter.hasNext()) {

						Option element = (Option) iter.next();

						xmlText.append("<").append(MobileConstant2.TAG_OPTION);
						xmlText.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(HtmlEncoder.encode(element.getValue())).append("'");
						if (checkedList.size() > 0) {
							if (checkedList.contains(element.getValue())) {
								xmlText.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
							}
						} else {
							if (element.isDef()) {
								xmlText.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
							}
						}
						xmlText.append(">");

						xmlText.append(HtmlEncoder.encode(element.getOption()));
						xmlText.append("</").append(MobileConstant2.TAG_OPTION).append(">");
					}

					xmlText.append("</").append(MobileConstant2.TAG_CHECKBOXFIELD).append(">");

					return xmlText.toString();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	protected String toHiddenHtml(Document doc) {
		StringBuffer builder = new StringBuffer();
		builder.append(super.toHiddenHtml(doc));
		builder.append("<input");
		builder.append(" name='").append(getName()).append("'");
		builder.append(" type='hidden'");
		builder.append(" value='");
		try {
			builder.append(doc.getItemValueAsString(getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		builder.append("' >");
		return builder.toString();
	}

	/**
	 * 获取选项
	 * 
	 * @param runner
	 *            动态语言执行器
	 * @param doc
	 *            文档标识
	 * @return 选项对象
	 * @throws Exception
	 */
	protected Options getOptions(IRunner runner, Document doc, WebUser webUser) throws Exception {
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
						String[] optstr = strlst[i].split(":");
						if (optstr.length >= 2) {
							options.add(optstr[0], optstr[1]);
						} else {
							options.add(strlst[i], strlst[i]);
						}
					}
				} else if (result instanceof Options) {
					options = (Options) result;
				}
			}
		}
		return options;

	}

	/**
	 * 根据Form模版的组件内容结合Document中的ITEM存放的值,输出重定义后的html文本以网格显示
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
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser,Map<String,Options> columnOptionsCache) throws Exception {

		int displayType = getDisplayType(doc, runner, webUser);
		
		if(displayType == PermissionType.HIDDEN){
			return getHiddenValue();
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
			StringBuffer html = new StringBuffer();

			Collection<String> checkedList = getCheckedList(runner, doc);

			if (doc != null) {
				Iterator<Option> iter = options.getOptions().iterator();
				
				html.append("<span moduleType='gridcheckbox'");
				html.append(" _id='" + getFieldId(doc) + "'");
				html.append(" _fieldType='" + getTagName() + "'");
				html.append(" _name='" + this.getName() + "'");
				html.append(" _displayType='"+ displayType +"'");
				html.append(" _textType='" + getTextType() + "'");
				html.append(" _isRefreshOnChanged='" + isRefreshOnChanged() + "'");
				html.append(" _class='" + cssClass + "'");
				html.append(" _getLayout='" + this.getLayout() + "'");
				//表单描述字段
//				html.append(" _discript ='" + getDiscript() + "'");
				html.append(" classname ='" + this.getClass().getName() + "'");
				html.append(toOtherpropsHtml());
				html.append(" >");
				while (iter.hasNext()) {
					Option element = (Option) iter.next();
					html.append("<span style='display:none;'");
					html.append(" value='" + HtmlEncoder.encode(element.getValue()) + "'");
					html.append(" text='" + element.getOption() + "'");
					html.append(" isDef ='" + element.isDef() + "'");
					html.append(" checkedListSize='" + checkedList.size()+"'");
					html.append(" checkedListContains='" + checkedList.contains(element.getValue()) + "'");
					html.append(" >");
					html.append("</span>");
				}
				html.append("</span>");
				return html.toString();
			}
		}
		return "";
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
	 * 获取字段显示值
	 * 
	 * @param doc 文档
 	 * @param runner 脚本运行器
 	 * @param webUser webuser
 	 * @param columnOptionsCache
 	 * 			缓存控件选项(选项设计模式构建的Options)的Map
 	 * 			key--fieldId
	 *		    value--Options
	 * @return
	 * @throws Exception
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		if (!StringUtil.isBlank(doc.getParentid()) && getDisplayType(doc, runner, webUser) == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		}

		StringBuffer text = new StringBuffer();
		Options options = columnOptionsCache != null ? columnOptionsCache.get(this.getId()) : null;
		if(options == null){
			options = getOptions(runner, doc, webUser);
			if(columnOptionsCache != null && FormField.EDITMODE_VIEW.equals(optionsEditMode)){
				//选项是设计模式的时候才加入缓存
				columnOptionsCache.put(this.getId(), options);
			}
		}
		if (options != null) {
			String value = doc.getItemValueAsString(getName());
			String[] values = value.split(";");
			if (values != null && values.length > 0) {
				for (Iterator<Option> iterator = options.getOptions().iterator(); iterator.hasNext();) {
					Option option = (Option) iterator.next();
					for (int i = 0; i < values.length; i++) {
						if (option.getValue().equals(values[i])) {
							text.append(option.getOption()).append(";");
						}
					}
				}
				if (text.lastIndexOf(";") != -1) {
					text.deleteCharAt(text.lastIndexOf(";"));
				}
			}
		}

		return text.toString();
	}

	public String getValueMapScript() {
		StringBuffer scriptBuffer = new StringBuffer();
		scriptBuffer.append("putToValuesMap(valuesMap");
		scriptBuffer.append(",'").append(this.getName());
		scriptBuffer.append("',getCheckedListStr('");
		scriptBuffer.append(this.getName()).append("')");
		scriptBuffer.append(");");

		return scriptBuffer.toString();
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

	protected String toOtherpropsHtml() {
		StringBuffer buffer = new StringBuffer();
		Map<?, ?> coll = getOtherPropsAsMap();
		Collection<?> entrys = coll.entrySet();
		Iterator<?> it = entrys.iterator();
		while (it.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) it.next();
			if (entry.getKey().equals("value")) continue;
			buffer.append(" ");
			buffer.append(entry.getKey());
			buffer.append("=");
			String value = (String) entry.getValue();
			int pos1 = value.indexOf("'");
			int pos2 = value.indexOf("\"");
			if (pos1 > pos2) {
				buffer.append("'");
				buffer.append(value);
				buffer.append("'");
			} else {
				buffer.append("'");
				buffer.append(value);
				buffer.append("' ");
			}

		}
		return buffer.toString();
	}
}
