//Source file:
//C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\InputField.java

package cn.myapps.core.dynaform.form.ejb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
 * 日期组件
 * 
 * @author Marky
 */
public class DateField extends FormField implements ValueStoreField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7492816203889946802L;

	/**
	 * @roseuid 41ECB66E012A
	 */
	protected static String cssClass = "Wdate";

	protected String datePattern = ""; // 日期显示模式

	protected String prev_Name = "";
	
	private static final String PATTERN_Y = "Y";
	
	private static final String PATTERN_YM = "YM";

	private static final String PATTERN_YMD = "YMD";

	private static final String PATTERN_YMD_HMS = "YMD_HMS";

	private static final String PATTERN_YMD_HM = "YMD_HM";

	private static final String PATTERN_HMS = "HMS";

	private static Map<String, String> datePatternMap = new HashMap<String, String>();

	static { // 日期格式
		datePatternMap.put(PATTERN_Y, "yyyy");
		datePatternMap.put(PATTERN_YM, "yyyy-MM");
		datePatternMap.put(PATTERN_YMD, "yyyy-MM-dd");
		datePatternMap.put(PATTERN_YMD_HMS, "yyyy-MM-dd HH:mm:ss");
		datePatternMap.put(PATTERN_YMD_HM, "yyyy-MM-dd HH:mm");
		datePatternMap.put(PATTERN_HMS, "HH:mm:ss");
	}

	/**
	 * InputField构造函数
	 * 
	 */
	public DateField() {

	}

	/**
	 * @roseuid 41ECB66E0152
	 */
	public void store() {

	}

	/**
	 * 获取日期型的多种格式 (yyyy;yyyy-MM;yyyy-MM-dd;yyyy-MM-dd HH:mm:ss;yyyy-MM-dd HH:mm;HH:mm:ss)
	 * 
	 * @return 日期
	 */
	public String getDatePatternValue() {
		String key = getDatePattern();
		if (datePatternMap.containsKey(key)) {
			return (String) datePatternMap.get(key);
		} else {
			return (String) datePatternMap.get(PATTERN_YMD);
		}
	}
	
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return toHtmlTxt(doc,runner,webUser,PermissionType.MODIFY);
	}
	
	/**
	 * 根据DateField的显示类型不同,返回的结果字符串不同. 新建的Document,DateField的显示类型为默认的MODIFY。
	 * 此时根据Form模版的DateField内容结合Document的Item的值,返回的字符串为重定义后的html.
	 * 根据Document流程节点设置对应DateField的显示类型不同,返回的结果字符串不同.
	 * 1)若节点设置对应DateField的显示类型为MODIFY与新建的Document时，返回的字符串一样。
	 * 2)若节点设置对应DateField的显示类型为READONLY,Document的ITEM存放的值为只读.
	 * 3)若节点设置对应DateField的显示类型为DISABLED,Document的ITEM存放的值为DISABLED.
	 * 根据Form模版的文档框内容结合Document中的ITEM存放的值,返回重定义后的html，
	 * 通过强化HTML标签及语法，表达文档框的布局、属性、事件、样式、值等。 若节点设置对应DateField的显示类型为HIDDEN,返回空.
	 * 
	 * @param doc
	 *            文档对象
	 * @param runner
	 *            动态语言执行器
	 * @param webUser
	 *            webUser
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的html为Form模版的文档框内容结合Document中的ITEM存放的值,
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		// 添加Field
			html.append(toStart());
			html.append(toAttr(doc, displayType));
			html.append(toOtherpropsHtml());
			html.append("/>");
		return html.toString();
	}
	public String getName() {
		return HtmlEncoder.encode((String) name);
	}
	
	/**
	 * 生成日期存显示值的头标记
	 * 
	 * @return html标记
	 */
	public String toStart() {
		StringBuffer html = new StringBuffer();
		html.append("<input type='hidden' moduleType='dateinput' data-enhance='false' ");
		return html.toString();
	}

	public String getRefreshScript(IRunner runner, Document doc, WebUser webUser, boolean isHidden) throws Exception {
		StringBuffer buffer = new StringBuffer();
		// buffer.append("alert('"+this.getName()+"');");
		String divid = this.getId() + "_divid";

		String fieldHTML = "";
		if (!isHidden) {
			fieldHTML = this.toHtmlTxt(doc, runner, webUser);
			fieldHTML = fieldHTML.replaceAll("\\\'", "\\\\'");
			fieldHTML = fieldHTML.replaceAll("\"", "\\\\\"");
			fieldHTML = fieldHTML.replaceAll("\r\n", "");
		}

		buffer.append("refreshField(\"").append(divid).append("\",\"");
		buffer.append(this.getName()).append("\",\"").append(fieldHTML).append("\");");

		return buffer.toString();
	}

	/**
	 * 生成控件属性
	 * 
	 * @param doc
	 *            (Document)文档对象
	 * @param runner
	 *            动态语言执行器
	 * @param webUser
	 *            webUser
	 * @return html的标记(<input id='' name='' class='' >)
	 * @throws Exception
	 */
	public String toAttr(Document doc, int displayType) throws Exception {
		StringBuffer html = new StringBuffer();
		html.append(" _displayType='" + displayType + "'");
		html.append(" _hiddenValue='" + StringUtil.dencodeHTML((String) hiddenValue) +"'");
		html.append(" _id='" + getFieldId(doc) + "'");
		html.append(" _name='" + getName() + "'");
		html.append(" _class='" + cssClass + "'");
		html.append(" _fieldType='" + getTagName() + "'");
		html.append(" _textType='" + getTextType() + "'");
		//表单描述字段
		html.append(" _discript ='" + getDiscript() + "'");
		
		String skin = "whyGreen";
		String minDate = "";
		String maxDate = "";

		if (!StringUtil.isBlank(getPrev_Name())) {
			html.append("_getPrevName='true'");
			//" +doc.getId() + "_" + getPrev_Name() + " 此组合既是被参考的日期控件的ID
			html.append("_getPrevNameMinDate='" +doc.getId() + "_" + getPrev_Name() + "'");
		}

		String dateFmt = getDatePatternValue();
		html.append(" _dateFmt='"+dateFmt+"'");
		html.append(" _skin='"+skin+"'");
		html.append(" _minDate='"+minDate+"'");
		html.append(" _maxDate='"+maxDate+"'");
		html.append(" _isRefreshOnChanged='" + isRefreshOnChanged() + "'");
		if (!datePattern.equals(PATTERN_HMS)) {
			html.append(" _noPatternHms='true'");
		}
		
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			if (item != null && item.getValue() != null) {
				Object value = item.getValue();
				if (value instanceof Date) {
					Date d = (Date) value;

					String datePattern = getDatePatternValue();
					SimpleDateFormat format = new SimpleDateFormat(datePattern);

					html.append(" value=\"" + format.format(d) + "\"");
				} else {
					html.append(" value=\"" + HtmlEncoder.encode((String)item.getValue()+"") + "\"");
				}
			}else{
				html.append(" value=\"").append("").append("\"");
			}
		}
		return html.toString();
	}

	/**
	 * 
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		// 添加Field
		if (displayType == PermissionType.HIDDEN) {// 节点设置对应field隐藏
			return this.getHiddenValue();
		} else {
			html.append(toStart());
			html.append(toAttr(doc, displayType));
			html.append(toOtherpropsHtml());
			html.append(" _subGridView='true'");
			html.append("/>");

		}
		return html.toString();
	}

	/**
	 * 根据打印时对应DateField的显示类型不同,默认为MODIFY,返回的结果字符串不同.
	 * <P>
	 * 若Document不为空且打印时对应DateField的显示类型不为HIDDEN且字段类型不为HIDDEN,
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
				if (item != null && item.getValue() != null) {
					Object value = item.getValue();
					html.append("<SPAN");
					html.append(toOtherpropsHtml()+">");
					if (value instanceof Date) {
						String datePattern = getDatePatternValue();
	
						SimpleDateFormat format = new SimpleDateFormat(datePattern);
	
						html.append(format.format(value));
						html.append("</SPAN>");
					} else {
						if(StringUtil.isBlank(item.getValue().toString())){
							html.append("&nbsp;");
						}else{
							html.append(HtmlEncoder.encode((String)item.getValue()+""));
							html.append("</SPAN>");
						}
					}
					
					
				}else{
					html.append("<SPAN");
					html.append(toOtherpropsHtml()+">");
					html.append("&nbsp;");
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
		template.append(">");
		return template.toString();
	}

	/**
	 * 执行inputField值勤脚本,重新计算inputField.
	 * 
	 * @roseuid 41DB89D700F9
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
		getLog().debug("DateField.recalculate");
		runValueScript(runner, doc);
	}

	/**
	 * 根据DateField名称，日期值模式与值创建相应的Item. 日期模式. 1）yyyy-MM-dd ； 2）yyyy-MM-dd
	 * HH:mm:ss ;
	 * 
	 * @param doc
	 *            Document 对象
	 * @return item
	 * @throws ParseException 
	 * @roseuid 41EBD62F00BE
	 */
	public Item createItem(Document doc, Object value)  {
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
		Object date = getDateValue(value);

		if (date != null ) {
			item.setValue(date);
		}else{
			if(value !=null && item.getDatevalue() != null){
				item.setValue("");
			}
		}
		return item;
	}

	public Date getDateValue(Object value) {
		Date date = null;
		if (getFieldtype().equals(Item.VALUE_TYPE_DATE)) {
			try {
				if (value != null && ((String) value).trim().length() > 0) {
					SimpleDateFormat format = new SimpleDateFormat();

					String datePattern = getDatePatternValue();

					try {
						format = new SimpleDateFormat(datePattern);
						date = format.parse((String) value);
					} catch (ParseException e1) {
						try {
							format = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
							date = format.parse((String) value);
						} catch (ParseException e2) {
							try {
								format = new SimpleDateFormat("yyyy-M月-dd", Locale.CHINA);
								date = format.parse((String) value);
							} catch (ParseException e3) {
								try {
									format = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
									date = format.parse((String) value);
								} catch (ParseException e5) {
									try {
										format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
										date = format.parse((String) value);
									}catch (ParseException e4) {
									getLog().warn("FieldName: " + getName());
								}
							}
						}
					}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return date;
	}

	public String getText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return getText(doc, runner, webUser, null);
	}
	
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		Date dateValue = doc.getItemValueAsDate(getName());

		try {
			if (dateValue != null) {
				SimpleDateFormat format = new SimpleDateFormat(getDatePatternValue(), Locale.CHINA);
				String dateStr = format.format(dateValue);
				return dateStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.getText(doc, runner, webUser);
	}

	/**
	 * 前面的日期名称. 作用是用来实现前面的日期不能大于后面的日期.
	 * 
	 * @return 前面的日期字符串
	 * @uml.property name="prev_Name"
	 */
	public String getPrev_Name() {
		return prev_Name;
	}

	/**
	 * 设置前面的日期名称
	 * 
	 * @param prev_Name
	 * @uml.property name="prev_Name"
	 */
	public void setPrev_Name(String prev_Name) {
		this.prev_Name = prev_Name;
	}

	/**
	 * 获取日期显示模式. 1）yyyy; 2）yyyy-MM; 3）yyyy-MM-dd ；4）yyyy-MM-dd HH:mm 5）yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 日期显示模式
	 * @uml.property name="datePattern"
	 */
	public String getDatePattern() {
		return datePattern;
	}

	/**
	 * 设置日期显示模式。 1）yyyy; 2）yyyy-MM; 3）yyyy-MM-dd ；4）yyyy-MM-dd HH:mm 5）yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datePattern
	 * @uml.property name="datePattern"
	 */
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}
	
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		// 添加Field
		if (getTextType() != null) {
			html.append("<").append(MobileConstant.TAG_DATEFIELD);
			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				html.append(" ").append(MobileConstant.ATT_READONLY).append("='true' ");
			}
			if (displayType == PermissionType.HIDDEN || (getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				html.append(" ").append(MobileConstant.ATT_TYPE).append(
						"='hidden'");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					html.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}
			
			html.append(" ").append(MobileConstant.ATT_NAME).append("='" + getName() + "'");

			if (isRefreshOnChanged()) {
				html.append(" ").append(MobileConstant.ATT_REFRESH).append("='true' ");
			}

			html.append(" ").append(MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY || displayType == PermissionType.DISABLED
					|| getTextType().equalsIgnoreCase("readonly")) {
				html.append(" ").append(MobileConstant.ATT_DISABLED).append("='true' ");
			}
			if (displayType == PermissionType.HIDDEN) {
				html.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
			}
			if (!getDatePattern().equals("")) {
				html.append(" ").append(MobileConstant.ATT_PATTEN).append(" ='" + getDatePattern() + "' ");
			} else {
				html.append(" ").append(MobileConstant.ATT_PATTEN).append("='YYYY-MM-DD'");
			}
			html.append(" >");

			if (doc != null) {
				Item item = doc.findItem(this.getName());
				if (item != null && item.getValue() != null) {
					Object value = item.getValue();
					if (value instanceof Date) {
						Date d = (Date) value;
						String datePattern = getDatePatternValue();
						SimpleDateFormat format = new SimpleDateFormat(datePattern);

						html.append(format.format(d));
					} else {
						html.append(HtmlEncoder.encode((String)item.getValue()+""));
					}
				}
			}
			html.append("</").append(MobileConstant.TAG_DATEFIELD).append(">");

		}
		return html.toString();
	}
	
	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		// 添加Field
		if (getTextType() != null) {
			xmlText.append("<").append(MobileConstant2.TAG_DATEFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
			
//			if (displayType == PermissionType.READONLY
//					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
//					|| displayType == PermissionType.DISABLED) {
//				xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
//			}
			if (displayType == PermissionType.HIDDEN || (getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
				}
			}

			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
			}


			if (displayType == PermissionType.READONLY || displayType == PermissionType.DISABLED
					|| getTextType().equalsIgnoreCase("readonly")) {
				xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
			}
			if (!getDatePattern().equals("")) {
				xmlText.append(" ").append(MobileConstant2.ATT_PATTEN).append("='").append(getDatePattern()).append("'");
			} else {
				xmlText.append(" ").append(MobileConstant2.ATT_PATTEN).append("='YYYY-MM-DD'");
			}
			
			xmlText.append(">");
			
			if (doc != null) {
				Item item = doc.findItem(this.getName());
				if (item != null && item.getValue() != null) {
					Object value = item.getValue();
					if (value instanceof Date) {
						Date d = (Date) value;
						String datePattern = getDatePatternValue();
						SimpleDateFormat format = new SimpleDateFormat(datePattern);
						xmlText.append(format.format(d));
					} else {
						xmlText.append(HtmlEncoder.encode((String)item.getValue()+""));
					}
				}
			}
			xmlText.append("</").append(MobileConstant2.TAG_DATEFIELD).append(">");

		}
		return xmlText.toString();
	}
	
	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			if (!getTextType().equalsIgnoreCase("hidden")) {
				Item item = doc.findItem(this.getName());
				if (item != null && item.getValue() != null) {
					Object value = item.getValue();
					html.append("<SPAN ");
					html.append(toOtherpropsHtml()+">");
					if (value instanceof Date) {
						String datePattern = getDatePatternValue();

						SimpleDateFormat format = new SimpleDateFormat(datePattern);

						html.append(format.format(value));
						html.append("</SPAN>");
					} else {
						if(StringUtil.isBlank(item.getValue().toString())){
							html.append("&nbsp;");
						}else{
							html.append(HtmlEncoder.encode((String)item.getValue()+""));
							html.append("</SPAN>");
						}
					}
					
					
				}else{
					html.append("<SPAN ");
					html.append(toOtherpropsHtml()+">");
					html.append("&nbsp;");
					html.append("</SPAN>");
				}
			}else{
			    html.append(printHiddenElement(doc));
			}
		}
		return html.toString();
	}
}
