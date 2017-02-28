//Source file:
//C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\InputField.java

package cn.myapps.core.dynaform.form.ejb;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

/**
 * @author Marky
 */
public class InputField extends FormField implements ValueStoreField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6019940535384883667L;

	/**
	 * @roseuid 41ECB66E012A
	 */
	protected static String cssClass = "input-cmd";

	/**
	 * @uml.property name="dialogView"
	 */
	protected String dialogView = "";

	/**
	 * @uml.property name="suggest"
	 */
	protected String suggest = ""; //

	/**
	 * @uml.property name="popToChoice"
	 */
	protected boolean popToChoice;
	/**
	 * @uml.property name="fiedlketevent"
	 */
	protected String fieldkeyevent = "";

	/**
	 * @uml.property name="numberPattern"
	 */
	protected String numberPattern = ""; // 数字显示模式

	/**
	 * InputField构造函数
	 * 
	 */
	public InputField() {

	}

	/**
	 * 获取键盘事件类型字符串 ，为了实现只用键盘快速输入.两种事件类型:1.回车键(EnterKey),2.Tab(TabKey)
	 * 
	 * @return 键盘事件类型字符串
	 * @uml.property name="fieldkeyevent"
	 */
	public String getFieldkeyevent() {
		return fieldkeyevent;
	}

	/**
	 * 设置键盘事件类型字符串 ，为了实现只用键盘快速输入.两种事件类型:1.回车键(EnterKey),2.Tab(TabKey)
	 * 
	 * @param fieldkeyevent
	 *            表单字列键盘事件类型字符串
	 * @uml.property name="fieldkeyevent"
	 */
	public void setFieldkeyevent(String fieldkeyevent) {
		this.fieldkeyevent = fieldkeyevent;
	}

	/**
	 * @roseuid 41ECB66E0152
	 */
	public void store() {

	}

	/**
	 * 根据includeField的显示类型不同,返回的结果字符串不同.
	 * 新建的Document,includeField的显示类型为默认的MODIFY。
	 * 此时根据Form模版的includeField内容结合Document的Item的值,返回的字符串为重定义后的html.
	 * 根据Document流程节点设置对应includeField的显示类型不同,返回的结果字符串不同.
	 * 1)若节点设置对应includeField的显示类型为MODIFY与新建的Document时，返回的字符串一样。
	 * 2)若节点设置对应includeField的显示类型为READONLY,Document的ITEM存放的值为只读.
	 * 3)若节点设置对应includeField的显示类型为DISABLED,Document的ITEM存放的值为DISABLED.
	 * 根据Form模版的文档框内容结合Document中的ITEM存放的值,返回重定义后的html，
	 * 通过强化HTML标签及语法，表达文档框的布局、属性、事件、样式、值等。 若节点设置对应includeField的显示类型为HIDDEN,返回空.
	 * 
	 * @param doc
	 *            文档对象
	 * @return 重定义后的html为Form模版的文档框内容结合Document中的ITEM存放的值,
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		StringBuffer html = new StringBuffer();

		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		// 添加Field
		html.append(toStart());
		html.append(toAttr(doc, displayType));
		// 添加其他属性
		html.append(toOtherpropsHtml());

		html.append(" />");
		return html.toString();
	}

	public String toStart() {
		StringBuffer html = new StringBuffer();
		html.append("<input type='hidden' moduleType='input'");
		html.append(" _isBlank='" + StringUtil.isBlank(getTextType()) + "'");
		html.append(" _textType='" + getTextType() + "'");
		html.append(" _isBorderType='" + isBorderType() + "'");
		//表单描述字段
		html.append(" _discript ='" + getDiscript() + "'");

		return html.toString();
	}

	/**
	 * 生成属性
	 * 
	 * @param doc
	 * @param runner
	 * @param webUser
	 * @return
	 * @throws Exception
	 */
	private String toAttr(Document doc, int displayType) throws Exception {
		StringBuffer html = new StringBuffer();

		html.append(" _id=\"" + getFieldId(doc) + "\"");
		html.append(" _name=\"" + getName() + "\"");
		html.append(" _isRefreshOnChanged=\"" + isRefreshOnChanged() + "\"");
		html.append(" _displayType = '" + displayType + "'");
		html.append(" _hiddenValue='" + HtmlEncoder.encode(this.getHiddenValue()) + "'");
		html.append(" _fieldType='" + this.getFieldtype() + "'");
		html.append(" _fieldKeyEvent='" + getFieldkeyevent() + "'");
		html.append(" _cssClass=\"" + cssClass + "\"");
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			// 数字类型与文本类型取值
			if (item != null && item.getValue() != null) {
				Object value = item.getValue();
				if (value instanceof Number) {
					String _value = value.toString();
					DecimalFormat format = new DecimalFormat(getNumberPattern());
					if(_value.equals("0.0")){
						_value = "0";
					}else {
						_value = format.format(value);
					}
					if(_value.indexOf("E") != -1){//不使用科学计数法
						DecimalFormat dFormat = new DecimalFormat("##.######");
						_value = dFormat.format(value);
					}
					html.append(" value=\"" + _value + "\"");
					if (!getTextType().equalsIgnoreCase("password")) {
						html.append(" _title=\"" + _value + "\"");
					}
				} else {
					if (value instanceof String) {
						String valueStr = HtmlEncoder.encode((String) value + "");
//						valueStr = valueStr.replaceAll("\"", "");
						valueStr = valueStr != null && !valueStr.equals("null") ? valueStr : "";
						if (!getTextType().equalsIgnoreCase("password")) {
							html.append(" _title=\"").append(valueStr).append("\"");
						}
						html.append(" value=\"").append(valueStr).append("\"");
					} else {
						if (!getTextType().equalsIgnoreCase("password")) {
							html.append(" _title=\"").append(value).append("\"");
						}
						html.append(" value=\"").append(value).append("\"");
					}
				}
			}else{
				html.append(" value=\"").append("").append("\"");
			}
		}
		return html.toString();
	}

	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();

		int displayType = getDisplayType(doc, runner, webUser);
		
		if(displayType == PermissionType.HIDDEN){
			return getHiddenValue();
		}
		
		// 添加Field
			html.append(toStart());
			html.append(toAttr(doc, displayType));
			html.append(toOtherpropsHtml());
			html.append(" style='width:100%'");
			html.append(" _subGridView='true'");
			html.append(">");

		return html.toString();
	}
	
	/**
	 * 根据打印时对应InputField的显示类型不同,默认为MODIFY,返回的结果字符串不同.
	 * <P>
	 * 若Document不为空且打印时对应InputField的显示类型不为HIDDEN且字段类型不为HIDDEN,
	 * 并根据Form模版的文本框组件内容结合Document中的ITEM存放的值,返回重定义后的打印html文本. 否则为空字符串.
	 * 
	 * @param doc
	 *            Document
	 * @param runner
	 *            AbstractRunner(执行脚本的接口类)
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * 
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 字符串
	 * @throws Exception
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}

			if (!getTextType().equalsIgnoreCase("hidden")) {
				Item item = doc.findItem(this.getName());
				if (item != null) {
					Object value = item.getValue();
					html.append("<SPAN ");
					html.append(toOtherpropsHtml());
					html.append(">");
					if (value instanceof Number) {
						DecimalFormat format = new DecimalFormat(getNumberPattern());
						html.append(format.format((Number) item.getValue()));
					} else {
						 if(item.getValue()==null){
							html.append("&nbsp;");
						 }else{
						     if(StringUtil.isBlank(item.getValue().toString())){
							    html.append("&nbsp;");
						    }else{
							    html.append(HtmlEncoder.encode((String) item.getValue()));
						    }
						}
					}
					html.append("</SPAN>");
				}
			} else {
				html.append("&nbsp;");
			}
		}
		return html.toString();
	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}

			if (!getTextType().equalsIgnoreCase("hidden")) {
				Item item = doc.findItem(this.getName());
				if (item != null) {
					Object value = item.getValue();
					html.append("<SPAN ");
					html.append(toOtherpropsHtml());
					html.append(">");
					if (value instanceof Number) {
						DecimalFormat format = new DecimalFormat(getNumberPattern());
						html.append(format.format((Number) item.getValue()));
					} else {
						 if(item.getValue()==null){
							html.append("&nbsp;");
						 }else{
						     if(StringUtil.isBlank(item.getValue().toString())){
							    html.append("&nbsp;");
						    }else{
							    html.append(HtmlEncoder.encode((String) item.getValue()));
						    }
						}
					}
					html.append("</SPAN>");
				}
			} 
		}
		return html.toString();
	}

	/**
	 * 返回模板描述文本
	 * 
	 * @return java.lang.String
	 * @roseuid 41E7917A033F
	 */
	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<input type='" + this.getTextType() + "'");
		template.append(" class='" + cssClass + "'");
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
		template.append(" broderType='" + isBorderType() + "'");
		template.append(">");
		return template.toString();
	}

	/**
	 * 执行inputField值勤脚本,重新计算inputField.
	 * 
	 * @roseuid 41DB89D700F9
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
		getLog().debug("InputField.recalculate");
		runValueScript(runner, doc);
	}

	/**
	 * 根据InputField名称，值类型与值创建相应的Item.
	 * 
	 * @param doc
	 *            Document 对象
	 * @return item
	 * @throws ParseException
	 * @roseuid 41EBD62F00BE
	 */
	public Item createItem(Document doc, Object value) {
		Item item = doc.findItem(getName());

		if (item == null) {
			item = new Item();
			try {
				item.setId(Sequence.getSequence());
			} catch (SequenceException e) {
				e.printStackTrace();
			}
			item.setName(getName());
		}

		item.setName(getName());
		item.setType(this.getFieldtype());
		Object objValue = value;
		if (getFieldtype().equals(Item.VALUE_TYPE_NUMBER)) {
			if (value != null && ((String) value).trim().length() > 0) {
				DecimalFormat format = new DecimalFormat(getNumberPattern());
				try {
					objValue = format.parseObject((String) value);
				} catch (ParseException e) {
					getLog().warn(e.getMessage());
				}
			}
		}

		if (objValue != null) {
			item.setValue(objValue);
		}

		return item;
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
	 * 获取显示值
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		if (!StringUtil.isBlank(doc.getParentid())) {
			int displayType = getDisplayType(doc, runner, webUser);
			if (displayType == PermissionType.HIDDEN) {
				return this.getHiddenValue();
			}
		}

		DecimalFormat format = new DecimalFormat(getNumberPattern());
		if (getFieldtype().equals(Item.VALUE_TYPE_NUMBER)) {
			return format.format(doc.getItemValueAsDouble(getName()));
		} else {
			return doc.getItemValueAsString(getName());
		}

	}

	/**
	 * 返回可以弹出一个对话视图文本框.
	 * 
	 * @hibernate.property column="dialogView"
	 * @uml.property name="dialogView"
	 */
	public String getDialogView() {
		return dialogView;
	}

	/**
	 * 设置可以弹出一个对话视图文本框
	 * 
	 * @param dialogView
	 * @uml.property name="dialogView"
	 */

	public void setDialogView(String dialogView) {
		this.dialogView = dialogView;
	}

	/**
	 * @hibernate.property column="popToChoice"
	 * @uml.property name="popToChoice"
	 */
	public boolean isPopToChoice() {
		return popToChoice;
	}

	/**
	 * @param popToChoice
	 *            the popToChoice to set
	 * @uml.property name="popToChoice"
	 */
	public void setPopToChoice(boolean popToChoice) {
		this.popToChoice = popToChoice;
	}

	/**
	 * 返回数字显示模式
	 * 
	 * @return 数字显示模式
	 * @uml.property name="numberPattern"
	 */
	public String getNumberPattern() {
		return numberPattern.trim().length() > 0 ? numberPattern : "##.##";
	}

	/**
	 * 设置数字显示模式
	 * 
	 * @param numberPattern
	 * @uml.property name="numberPattern"
	 */
	public void setNumberPattern(String numberPattern) {
		this.numberPattern = numberPattern;
	}

	/**
	 * @hibernate.property column="suggest"
	 * @uml.property name="suggest"
	 */
	public String getSuggest() {
		return suggest;
	}

	/**
	 * @param suggest
	 * @uml.property name="suggest"
	 */

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		// 添加Field
		if (getTextType() != null) {
			xmlText.append("<").append(MobileConstant.TAG_TEXTFIELD).append(" ");
			
			xmlText.append(" ").append(MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (getTextType().equalsIgnoreCase("readonly") ||
					displayType == PermissionType.READONLY || 
					displayType == PermissionType.DISABLED) {
				xmlText.append(MobileConstant.ATT_TEXTTYPE).append("='readonly' ");
			}else if(getTextType().equalsIgnoreCase("password")){
				xmlText.append(" ").append(MobileConstant.ATT_TEXTTYPE).append("='password'");
			}
			if (isBorderType()){
				xmlText.append(MobileConstant.ATT_BORDERTYPE).append("='true' ");
			}
			if (displayType == PermissionType.HIDDEN || getTextType().equalsIgnoreCase("hidden")) {
				xmlText.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}
			
			if (getFieldtype()!=null && getFieldtype().equalsIgnoreCase("value_type_number")) {
				if(getNumberPattern().indexOf(".")==-1){
					xmlText.append(" ").append(MobileConstant.ATT_TYPE).append("='number'");
				}else{
					xmlText.append(" ").append(MobileConstant.ATT_TYPE).append("='numberDecimal'");
				}
			}

			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant.ATT_REFRESH).append("='true' ");
			}

			xmlText.append(" ").append(MobileConstant.ATT_NAME).append("='" + getName() + "'>");

			if (doc != null) {
				Item item = doc.findItem(this.getName());

				if (item != null && item.getValue() != null) {
					Object value = item.getValue();
					if (value instanceof Number) {
						DecimalFormat format = new DecimalFormat(getNumberPattern());
						xmlText.append(format.format((Number) item.getValue()));
					} else {
						if ((value instanceof String) && value.toString().trim().equals("")) {
						} else {
							String valueStr = HtmlEncoder.encode((String) value + "");
							xmlText.append(valueStr);
						}
					}
				}
			}

			xmlText.append("</").append(MobileConstant.TAG_TEXTFIELD).append(">");
		}
		return xmlText.toString();
	}
	
	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		// 添加Field
		if (getTextType() != null) {
			xmlText.append("<").append(MobileConstant2.TAG_TEXTFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
			
			if (getTextType().equalsIgnoreCase("readonly") ||
					displayType == PermissionType.READONLY || 
					displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant2.ATT_TEXTTYPE).append("='readonly'");
			}else if(getTextType().equalsIgnoreCase("password")){
				xmlText.append(" ").append(MobileConstant2.ATT_TEXTTYPE).append("='password'");
			}
			if (isBorderType()){
				xmlText.append(" ").append(MobileConstant2.ATT_BORDERTYPE).append("='true'");
			}
			if (displayType == PermissionType.HIDDEN || getTextType().equalsIgnoreCase("hidden")) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
				}
			}
			
			if (getFieldtype()!=null && getFieldtype().equalsIgnoreCase("value_type_number")) {
				if(getNumberPattern().indexOf(".")==-1){
					xmlText.append(" ").append(MobileConstant2.ATT_TYPE).append("='number'");
				}else{
					xmlText.append(" ").append(MobileConstant2.ATT_TYPE).append("='numberDecimal'");
				}
			}

			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
			}
			
			xmlText.append(">");
			
			if (doc != null) {
				Item item = doc.findItem(this.getName());

				if (item != null && item.getValue() != null) {
					Object value = item.getValue();
					if (value instanceof Number) {
						DecimalFormat format = new DecimalFormat(getNumberPattern());
						xmlText.append(format.format((Number) item.getValue()));
					} else {
						if ((value instanceof String) && value.toString().trim().equals("")) {
						} else {
							String valueStr = HtmlEncoder.encode((String) value + "");
							xmlText.append(valueStr);
						}
					}
				}
			}

			xmlText.append("</").append(MobileConstant2.TAG_TEXTFIELD).append(">");
		}
		return xmlText.toString();
	}

	public String getTagName() {
		if (getFieldtype().equals(Item.VALUE_TYPE_NUMBER)) {
			return "NumberField";
		}
		return super.getTagName();
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
