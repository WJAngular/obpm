//Source file:
//C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\SelectField.java

package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
import cn.myapps.util.StringList;
import cn.myapps.util.StringUtil;

/**
 * @author nicholas
 */
public class RadioField extends FormField implements ValueStoreField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7218067956139893236L;

	protected static String cssClass = "radio-cmd";

	/**
	 * 计算多值选项
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
	public RadioField() {

	}

	/**
	 * 返回多值选项脚本
	 * 
	 * @return Returns the optionsScript.
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
	 * 执行RadioField值勤脚本，重新计算.
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @see cn.myapps.core.macro.runner.JavaScriptRunner#run(String, String)
	 * @param doc
	 *            文档对象
	 * @param displayType
	 *            Document显示类型
	 * @throws Exception
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
		getLog().debug("RadioField.recalculate");
		runOptionsScript(runner, doc, webUser);
		runValueScript(runner, doc);
	}

	/**
	 * 
	 * Form模版的RadioField组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的html，
	 * 
	 * @param doc
	 *            文档对象
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webuser
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的html为Form模版的Radio组件内容结合Document中的ITEM存放的值
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
	 * 根据打印时对应RadioField的显示类型不同,默认为MODIFY,返回的结果字符串不同.
	 * 若Document不为空且打印时对应RadioField的显示类型不为HIDDEN,
	 * <p>
	 * 并根据Form模版的RadioField组件内容结合Document中的ITEM存放的值,返回重定义后的打印html文本
	 * 通过强化HTML标签及语法，表达RadioField的布局、属性、事件、样式、等。
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * 
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 打印重定义后的打印html为Form模版的文本框组件内容结合Document中的ITEM存放的值
	 * @throws Exception
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		//Object result = null;
		StringBuffer html = new StringBuffer();

		int displayType = getPrintDisplayType(doc, runner, webUser);

		if (displayType == PermissionType.HIDDEN) {
			return this.getPrintHiddenValue();
		}

		if (doc != null) {
			
			Options options = getOptions(runner, doc, webUser);

			if (options != null) {
				Object value = null;
				StringList valueList = null;
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
//		return printHiddenElement(doc);
		return "";
	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
		if (doc != null) {
			
			Options options = getOptions(runner, doc, webUser);

			if (options != null) {
				Object value = null;
				StringList valueList = null;
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
				//html.append(printHiddenElement(doc));
				return html.toString();
			}
		}
		return "";
	}

	/**
	 * 获取模板描述单选项
	 * 
	 * @return 模板描述单选项
	 * @roseuid 41E7917A033F
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
	 * 根据RadioField的显示类型不同,返回的结果字符串不同.
	 * 新建的Document,RadioField的显示类型为默认的MODIFY。此时根据Form模版的RadioField内容结合Document的Item的值,返回的字符串为重定义后的html.
	 * 根据流程节点设置对应RadioField的显示类型不同,默认为MODIFY,返回的结果字符串不同.
	 * 1）若节点设置对应RadioField的显示类型为HIDDEN,返回"******".
	 * 2）若节点设置对应RadioField的显示类型为READONLY,Document的ITEM存放的值为只读.
	 * 3）若节点设置对应RadioField的显示类型为DISABLED,Document的ITEM存放的值为DISABLED.
	 * 
	 * 并根据Form模版的RadioField执行多值选项脚本内容结合Document中的ITEM存放的值,返回字符串为重定义后的html.
	 * 通过强化HTML的单值选项标签及语法，表达单值选项的布局、属性、事件、样式、等。
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @see cn.myapps.core.macro.runner.JavaScriptRunner#run(String, String)
	 * @return 字符串内容定义后的html的单值选项标签及语法
	 * @throws Exception
	 */
	public String runOptionsScript(IRunner runner, Document doc, WebUser webUser) throws Exception {
		//Object result = null;
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		Options options = getOptions(runner, doc, webUser);
		
		if (options != null && doc !=null) {
			Object value = null;
			StringList valueList = null;
			Item item = doc.findItem(this.getName());

			if (item != null)
				value = item.getValue();
			if (value != null)
				valueList = new StringList((String) value, ';');

			if (doc != null) {
				Iterator<Option> iter = options.getOptions().iterator();
				html.append("<span moduleType='radio'");
				html.append(" _id='" + getFieldId(doc) + "'");
				html.append(" _name='" + this.getName() + "'");
				html.append(" _displayType='"+ displayType +"'");
				html.append(" _textType='" + getTextType() + "'");
				html.append(" _hiddenValue='" + getHiddenValue() + "'");
				html.append(" _isRefreshOnChanged='" + isRefreshOnChanged() + "'");
				html.append(" _cssClass='" + cssClass + "'");
				html.append(" _getLayout='" + this.getLayout() + "'");
				html.append(" _valueList =\"" + valueList + "\"");
//				表单描述字段
				html.append(" _discript ='" + getDiscript() + "'");
				html.append(" classname ='" + this.getClass().getName() + "'");
				html.append(toOtherpropsHtml());
				html.append(" >");
				while (iter.hasNext()) {
					Option element = (Option) iter.next();
					html.append("<span style='display:none;'");
					html.append(" value=\"" + HtmlEncoder.encode(element.getValue()) + "\"");
					html.append(" getValue=\"" + element.getValue() + "\"");
					html.append(" get0ption='" + element.getOption() + "'");
					html.append(" isDef ='" + element.isDef() + "'");
					html.append(" >");
					html.append("</span>");
				}
				html.append("</span>");
			}
		}
		
		return html.toString();
	}

	public boolean isRender(String destVal, String origVal) {
		if (optionsScript != null && optionsScript.trim().length() > 0) {
			return true;
		}
		return super.isRender(destVal, origVal);
	}

	/**
	 * 用于为手机平台XML串生成
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @return 手机平台XML串生成
	 */
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		Options options = getOptions(runner, doc, webUser);

		if (options != null && doc != null) {
			Object value = null;
			StringList valueList = null;
			Item item = doc.findItem(this.getName());

			if (item != null)
				value = item.getValue();
			if (value != null)
				valueList = new StringList((String) value, ';');

			if (doc != null) {
				html.append("<").append(MobileConstant.TAG_RADIOFIELD).append(" ").append("").append(
						MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");

				html.append(" ").append(MobileConstant.ATT_NAME).append("='" + getName() + "'");

				if (isRefreshOnChanged()) {
					html.append(" ").append(MobileConstant.ATT_REFRESH).append("='true' ");
				}
				if ((this.getTextType() != null && this.getTextType().equalsIgnoreCase("READONLY"))
						|| displayType == PermissionType.DISABLED || displayType == PermissionType.READONLY) {
					html.append(" ").append(MobileConstant.ATT_READONLY).append("='true' ");
				}
				if (displayType == PermissionType.HIDDEN) {
					html.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
					if(!getHiddenValue().equals("") && !getHiddenValue().equals(null)){
						html.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
					}
				}

				html.append(">");
				Iterator<Option> iter = options.getOptions().iterator();
				int count = 0;
				boolean flag = true;
				while (iter.hasNext()) {
					Option element = (Option) iter.next();

					html.append("<").append(MobileConstant.TAG_OPTION).append(
							"  " + MobileConstant.ATT_VALUE + " = ");
					html.append("'");
					html.append(HtmlEncoder.encode(element.getValue()));
					html.append("'");
					if (flag) {
						if (valueList != null && element.getValue() != null) {
							if (valueList.indexOf(element.getValue()) >= 0) {
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
					html.append(">");
					html.append(HtmlEncoder.encode(element.getOption()));
					html.append("</").append(MobileConstant.TAG_OPTION).append(">");
					count++;
				}
				html.append("</").append(MobileConstant.TAG_RADIOFIELD).append(">");
			}
		}
		return html.toString();
	}
	
	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		Options options = getOptions(runner, doc, webUser);

		if (options != null && doc != null) {
			Object value = null;
			StringList valueList = null;
			Item item = doc.findItem(this.getName());

			if (item != null)
				value = item.getValue();
			if (value != null)
				valueList = new StringList((String) value, ';');

			if (doc != null) {
				xmlText.append("<").append(MobileConstant2.TAG_RADIOFIELD);
				xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");
				xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
				xmlText.append(" ").append(MobileConstant2.ATT_LAYOUT).append("='").append(getLayout()).append("'");
				
				if (isRefreshOnChanged()) {
					xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
				}
				if ((this.getTextType() != null && this.getTextType().equalsIgnoreCase("READONLY"))
						|| displayType == PermissionType.DISABLED || displayType == PermissionType.READONLY) {
					xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
				}
				if (displayType == PermissionType.HIDDEN) {
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
					if(!getHiddenValue().equals("") && !getHiddenValue().equals(null)){
						xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
					}
				}

				xmlText.append(">");
				
				Iterator<Option> iter = options.getOptions().iterator();
				boolean flag = true;
				while (iter.hasNext()) {
					Option element = (Option) iter.next();

					xmlText.append("<").append(MobileConstant2.TAG_OPTION);
					xmlText.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(HtmlEncoder.encode(element.getValue())).append("'");
					if (flag) {
						if (valueList != null && element.getValue() != null) {
							if (valueList.indexOf(element.getValue()) >= 0) {
								xmlText.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
								flag = false;
							}
						} else {
							if (element.isDef()) {
								xmlText.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
								flag = false;
							}
						}
					}
					xmlText.append(">");
					xmlText.append(HtmlEncoder.encode(element.getOption()));
					xmlText.append("</").append(MobileConstant2.TAG_OPTION).append(">");
				}
				xmlText.append("</").append(MobileConstant2.TAG_RADIOFIELD).append(">");
			}
		}
		return xmlText.toString();
	}

	/**
	 * 根据Form模版的RadioField组件内容结合Document中的ITEM存放的值,输出重定义后的html文本以网格显示
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

		if (options != null && doc !=null) {
			Object value = null;
			StringList valueList = null;
			Item item = doc.findItem(this.getName());

			if (item != null)
				value = item.getValue();
			if (value != null)
				valueList = new StringList((String) value, ';');

			if (doc != null) {
				Iterator<Option> iter = options.getOptions().iterator();
				html.append("<span moduleType='radiogrid'");
				html.append(" _id='" + getFieldId(doc) + "'");
				html.append(" _name='" + this.getName() + "'");
				html.append(" _displayType='"+ displayType +"'");
				html.append(" _textType='" + getTextType() + "'");
				html.append(" _isRefreshOnChanged='" + isRefreshOnChanged() + "'");
				html.append(" _cssClass='" + cssClass + "'");
				html.append(" _getLayout='" + this.getLayout() + "'");
				html.append(" _valueList ='" + valueList + "'");
				//表单描述字段
//				html.append(" _discript ='" + getDiscript() + "'");
				html.append(" classname ='" + this.getClass().getName() + "'");
				html.append(toOtherpropsHtml());
				html.append(" >");
				while (iter.hasNext()) {
					Option element = (Option) iter.next();
					html.append("<span style='display:none;'");
					html.append(" value='" + HtmlEncoder.encode(element.getValue()) + "'");
					html.append(" getValue='" + element.getValue() + "'");
					html.append(" get0ption='" + element.getOption() + "'");
					html.append(" isDef ='" + element.isDef() + "'");
					html.append(" >");
					html.append("</span>");
				}
				html.append("</span>");
			}
		}
		return html.toString();
	}

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
	 * 获取Form模版的RadioField的选项脚本，通过动态语言执行器执行脚本后生成Radio,并验证Radio的可见性,如果可见就显示正常,否则显示"******"
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
 	 * @param columnOptionsCache
 	 * 			缓存控件选项(选项设计模式构建的Options)的Map
 	 * 			key--fieldId
	 *		    value--Options
	 * @return 执行后生成的Radio
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
