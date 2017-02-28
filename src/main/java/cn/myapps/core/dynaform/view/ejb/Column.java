package cn.myapps.core.dynaform.view.ejb;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.AttachmentUploadField;
import cn.myapps.core.dynaform.form.ejb.FileManagerField;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ImageUploadField;
import cn.myapps.core.dynaform.form.ejb.ImageUploadToDataBaseField;
import cn.myapps.core.dynaform.form.ejb.InputField;
import cn.myapps.core.dynaform.form.ejb.MapField;
import cn.myapps.core.dynaform.form.ejb.NullField;
import cn.myapps.core.dynaform.form.ejb.OnLineTakePhotoField;
import cn.myapps.core.dynaform.form.ejb.Options;
import cn.myapps.core.dynaform.form.ejb.SelectField;
import cn.myapps.core.dynaform.form.ejb.UserField;
import cn.myapps.core.dynaform.form.ejb.ValueStoreField;
import cn.myapps.core.dynaform.form.ejb.WordField;
import cn.myapps.core.dynaform.view.ejb.editmode.DesignEditMode;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.util.CurrDocJsUtil;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

/**
 * @hibernate.class table="T_COLUMN"
 * 
 * @author marky
 */
public class Column extends ValueObject implements Comparable<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5375309298623433939L;

	private static final Logger log = Logger.getLogger(Column.class);
	/**
	 * 脚本编辑模式
	 */
	public static final String COLUMN_TYPE_SCRIPT = "COLUMN_TYPE_SCRIPT";
	/**
	 * 视图编辑模式
	 */
	public static final String COLUMN_TYPE_FIELD = "COLUMN_TYPE_FIELD";
	
	/**
	 * 操作列
	 */
	public static final String COLUMN_TYPE_OPERATE = "COLUMN_TYPE_OPERATE";
	
	/**
	 * 图标列
	 */
	public static final String COLUMN_TYPE_LOGO = "COLUMN_TYPE_LOGO";

	/**
	 * 序号列
	 */
	public static final String COLUMN_TYPE_ROWNUM = "COLUMN_TYPE_ROWNUM";
	
	/**
	 * 真实值
	 */
	public static final String SHOW_TYPE_VALUE = "00";

	/**
	 * 显示值
	 */
	public static final String SHOW_TYPE_TEXT = "01";
	
	/**
	 * 操作列按钮类型
	 */
	public static final String BUTTON_TYPE_DELETE = "00";
	
	public static final String BUTTON_TYPE_DOFLOW = "01";
	
	public static final String BUTTON_TYPE_TEMPFORM = "03";

	public static final String BUTTON_TYPE_SCRIPT = "04";
	
	public static final String BUTTON_TYPE_JUMP ="05"; //操作列增加跳转类型按钮
	
	/**
	 * 列的格式的类型(常规,数值,货币)
	 */
	public static final String FORMATTYPE_SIMPLE = "simple";
	
	public static final String FORMATTYPE_NUM = "number";
	
	public static final String FORMATTYPE_CURR = "currency";
	
	private String id;

	private String name;

	private String width;

	private String valueScript;

	private String hiddenScript;

	private String parentView;

	private String type = COLUMN_TYPE_FIELD;

	private String formid;

	private String fieldName;

	private boolean flowReturnCss; // 需要回退时增加样式

	private String imageName; // 回退的时候在前面的images

	private String fontColor; // 显示的字体

	private int orderno; // 排序

	private boolean sum; // 当页小计
	
	private boolean total; // 跨页总计

	private FormField field;
	
	private String isOrderByField;//默认排序
	
	private String orderType;
	
	private String multiLanguageLabel;//多语言标签
	
	private String actionScript;
	
	/**
	 * 单击列头排序
	 */
	private boolean clickSorting = true;
	
	/**
	 * 视图列排序标准 ("00":数据库默认;"01":中文拼音)
	 */
	private String sortStandard;
	
	private String jumpMapping; //操作列跳转类型的映射配置
	
	private String mappingform;//跳转类型的目标表单ID
	
	private String formatType;//列的格式的类型
	
	private String decimalsNum;//数值的小数位数
	
	private boolean thouSign;//千位分隔符
	
	private String decimalsCurr;//货币类型的小数位数
	
	private String currType;//货币类型
	
	private String color = "000000";//字体颜色
	
	private String fontSize = "12";//字体大小
	
	private String groundColor = "FFFFFF";//底色
	
	private boolean showIcon = false;//以图标显示
	
	private String iconMapping = "";//以图标显示的映射关系

	public boolean isShowIcon() {
		return showIcon;
	}

	public void setShowIcon(boolean showIcon) {
		this.showIcon = showIcon;
	}

	public String getIconMapping() {
		return iconMapping;
	}

	public void setIconMapping(String iconMapping) {
		this.iconMapping = iconMapping;
	}

	public String getGroundColor() {
		return groundColor;
	}

	public void setGroundColor(String groundColor) {
		this.groundColor = groundColor;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCurrType() {
		return currType;
	}

	public void setCurrType(String currType) {
		this.currType = currType;
	}

	public String getDecimalsCurr() {
		return decimalsCurr;
	}

	public void setDecimalsCurr(String decimalsCurr) {
		this.decimalsCurr = decimalsCurr;
	}

	public boolean isThouSign() {
		return thouSign;
	}

	public void setThouSign(boolean thouSign) {
		this.thouSign = thouSign;
	}

	public String getDecimalsNum() {
		return decimalsNum;
	}

	public void setDecimalsNum(String decimalsNum) {
		this.decimalsNum = decimalsNum;
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	public String getMappingform() {
		return mappingform;
	}

	public void setMappingform(String mappingform) {
		this.mappingform = mappingform;
	}

	public String getJumpMapping() {
		return jumpMapping;
	}

	public void setJumpMapping(String jumpMapping) {
		this.jumpMapping = jumpMapping;
	}

	public boolean isTotal() {
		return total;
	}

	public void setTotal(boolean total) {
		this.total = total;
	}

	public String getSortStandard() {
		return sortStandard;
	}

	public void setSortStandard(String sortStandard) {
		this.sortStandard = sortStandard;
	}

	public String getActionScript() {
		return actionScript;
	}

	public void setActionScript(String actionScript) {
		this.actionScript = actionScript;
	}

	public String getMultiLanguageLabel() {
		return multiLanguageLabel;
	}

	public void setMultiLanguageLabel(String multiLanguageLabel) {
		this.multiLanguageLabel = multiLanguageLabel;
	}

	/**
	 * 操作列按钮类型
	 */
	private String buttonType;
	
	/**
	 * 操作列按钮名称
	 */
	private String buttonName;
	
	private String approveLimit;

	/**
	 * 视图列表时可见
	 */
	private boolean visible = true;
	
	/**
	 * 导出Excel时可见
	 */
	private boolean visible4ExpExcel = true;
	
	/**
	 * 动态打印时可见
	 */
	private boolean visible4Print = true;
	
	/**
	 * 模板表单
	 */
	private String templateForm;
	
	/**
	 * 图标
	 */
	private String icon;

	/*
	 * 字段值显示类型
	 */
	private String showType;

	/**
	 * 映射字段
	 */
	private String mappingField;

	/**
	 * 显示方式的常量
	 */
	public static final String DISPLAY_ALL = "00";
	public static final String DISPLAY_PART = "01";

	/**
	 * 显示方式(默认是显示全部内容)
	 */
	private String displayType = DISPLAY_ALL;

	/**
	 * 显示内容的长度(默认长度为-1,表示显示所有)
	 */
	private String displayLength = "-1";

	/**
	 * 是否显示title(默认显示)
	 */
	private boolean showTitle = true;

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getDisplayLength() {
		return displayLength;
	}

	public void setDisplayLength(String displayLength) {
		this.displayLength = displayLength;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	/**
	 * 获取排序
	 * 
	 * @hibernate.property column="ORDERNO"
	 * @return 排序号
	 */
	public int getOrderno() {
		return orderno;
	}

	/**
	 * 设置排序
	 * 
	 * @param orderno
	 *            排序
	 */
	public void setOrderno(int orderno) {
		this.orderno = orderno;
	}

	/**
	 * 主键
	 * 
	 * @hibernate.id column="ID" generator-class="assigned"
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id
	 *            主键
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取值脚本
	 * 
	 * @hibernate.property column="VALUESCRIPT" type="text"
	 * @return 值脚本
	 */
	public String getValueScript() {
		return valueScript;
	}

	/**
	 * 设置 值脚本
	 * 
	 * @param valueScript
	 *            值脚本
	 */
	public void setValueScript(String valueScript) {
		this.valueScript = valueScript;
	}

	/**
	 * 获取列宽度
	 * 
	 * @hibernate.property column="WIDTH"
	 * @return string
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * 设置 列宽度
	 * 
	 * @param width
	 *            列宽度
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * 获取名字
	 * 
	 * @hibernate.property column="NAME"
	 * @return 名字
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名字
	 * 
	 * @param name
	 *            名字
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取类型,分别:1.字段(COLUMN_TYPE_FIELD),2.脚本(COLUMN_TYPE_SCRIPT)
	 * 如果选择字段,在视图显示列(column)的值为表单字段值,否则视图column为执行脚本(SCRIPT)后返回的值
	 * 
	 * @hibernate.property column="CTYPE"
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型 1.字段(COLUMN_TYPE_FIELD),2.脚本(COLUMN_TYPE_SCRIPT)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取字列名
	 * 
	 * @hibernate.property column="FIELDNAME"
	 * @return string 字列名
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 设置字列名
	 * 
	 * @param fieldName
	 *            字列名
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 获取相关表单Form主键
	 * 
	 * @hibernate.property column="FORMID"
	 * @return string
	 */
	public String getFormid() {
		return formid;
	}

	/**
	 * 设置相关表单Form主键
	 * 
	 * @param formid
	 *            表单Form主键
	 */
	public void setFormid(String formid) {
		this.formid = formid;
	}

	/**
	 * 获取操作列类型按钮的名称
	 * @return
	 */
	public String getButtonName() {
		return buttonName;
	}

	/**
	 * 设置操作列按钮的名称
	 * @param buttonName
	 */
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	
	/**
	 * 视图列表时是否可见
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * 设置视图列表时是否可见
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * 获取列导出Excel时是否可见
	 * @return
	 */
	public boolean isVisible4ExpExcel() {
		return visible4ExpExcel;
	}

	/**
	 * 设置列导出Excel时是否可见
	 * @param visible4ExpExcel
	 */
	public void setVisible4ExpExcel(boolean visible4ExpExcel) {
		this.visible4ExpExcel = visible4ExpExcel;
	}

	/**
	 * 获取列动态打印时是否可见
	 * @return
	 */
	public boolean isVisible4Print() {
		return visible4Print;
	}

	/**
	 * 设置列动态打印时是否可见
	 * @param visible4Print
	 */
	public void setVisible4Print(boolean visible4Print) {
		this.visible4Print = visible4Print;
	}

	/**
	 * 获得模板表单的From主键
	 * @return
	 */
	public String getTemplateForm() {
		return templateForm;
	}
	
	/**
	 *设置模板表单的From主键 
	 * @param templateForm
	 */
	public void setTemplateForm(String templateForm) {
		this.templateForm = templateForm;
	}

	/**
	 * 获取Column的显示值
	 * 
	 * @param doc
	 *            文档
	 * @param runner
	 *            动态语言执行器
	 * @return text 显示值
	 * @throws Exception
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();

		String value = getTextString(doc, runner, webUser);
		
//		if(DISPLAY_PART.equals(this.getDisplayType())){
//			if(value !=null && value.length()>Integer.parseInt(this.getDisplayLength())){
//				value = value.substring(0, Integer.parseInt(this.getDisplayLength()))+"......";
//			}
//		}
		html.append(value);
		return (!StringUtil.isBlank(html.toString()) ? html.toString() : " ");
	}
	
	/**
	 * 获取Column的显示值
	 * 
	 * @param doc
	 *            文档
	 * @param runner
	 *            动态语言执行器
	 * @param webUser
	 *            webUser
	 * @param  columnOptionsCache         
	 *        缓存控件选项(选项设计模式构建的Options)的Map
	 * 			key--fieldId
	 * 			value--Options
	 * @return text 显示值
	 * @throws Exception
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();

		String value = getTextString(doc, runner, webUser, columnOptionsCache);
		
		html.append(value);
		return (!StringUtil.isBlank(html.toString()) ? html.toString() : " ");
	}
	
	
	/**
	 * 小计的getText方法,避免格式化时,小计出错
	 * @param doc
	 * @param runner
	 * @param webUser
	 * @return
	 * @throws Exception
	 */
	public String getText4Sum(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();

		String value = getTextString(doc, runner, webUser);
		
		html.append(value);
		return (!StringUtil.isBlank(html.toString()) ? html.toString() : "&nbsp");
	}


	//按后台列格式的类型配置格式化该列的值
	private String formatColumn(String value) {
		if(this.getFormatType().equals(Column.FORMATTYPE_NUM)){
			try {
				double num = Double.parseDouble(value);
				String formatStr = "";
				int decimal = 0;
				if(!StringUtil.isBlank(this.getDecimalsNum())){
					decimal = Integer.parseInt(this.getDecimalsNum());
					formatStr += ".";
				}
				
				for(int i=0; i<decimal; i++){
					formatStr += "0";
				}
				
				if(this.isThouSign()){
					DecimalFormat df = new DecimalFormat(",###" + formatStr);
					return df.format(num);
				}else{
					DecimalFormat df = new DecimalFormat("###" + formatStr);
					return df.format(num);
				}
			} catch (NumberFormatException e) {
				return value;
			}
		}else if(this.getFormatType().equals(Column.FORMATTYPE_CURR)){
			try {
				double num = Double.parseDouble(value);
				
				int _num = 0;
				if(!StringUtil.isBlank(this.getDecimalsCurr())){
					_num = Integer.parseInt(this.getDecimalsCurr());
				}
				
				if(StringUtil.isBlank(this.getCurrType())){
					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.CHINA);
					nf.setMinimumFractionDigits(_num);
					return nf.format(num);
				}else if("zh_CN".equals(this.getCurrType())){
					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.CHINA);
					nf.setMinimumFractionDigits(_num);
					return nf.format(num);
				}else if("ja_JP".equals(this.getCurrType())){
					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.JAPAN);
					nf.setMinimumFractionDigits(_num);
					return nf.format(num);
				}else if("en_US".equals(this.getCurrType())){
					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
					nf.setMinimumFractionDigits(_num);
					return nf.format(num);
				}else if("en_GB".equals(this.getCurrType())){
					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.UK);
					nf.setMinimumFractionDigits(_num);
					return nf.format(num);
				}
			} catch (NumberFormatException e) {
				return value;
			}
		}
		
		return value;
	}
	
	/**获取Column值
	 * @param doc 文档
	 * @param runner 脚本运行器
	 * @param webUser 当前登录用户
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String getTextString(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return getTextString(doc, runner, webUser, null);
	}

	/**获取Column值
	 * @param doc 文档
	 * @param runner 脚本运行器
	 * @param webUser 当前登录用户
	 * @param columnOptionsCache           
	 *             缓存控件选项(选项设计模式构建的Options)的Map
	 *             key: fieldId
	 *             value: Options
	 * @return
	 * @throws Exception
	 */
	
	public String getTextString(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		Object result = null;
		StringBuffer html = new StringBuffer();
		StringBuffer labelBuilder = new StringBuffer();
		String text = "";
		if (getType() != null && getType().equals(COLUMN_TYPE_SCRIPT)) {
			labelBuilder.append("View.").append(getParentView());
			labelBuilder.append(".Column(").append(getId()).append(")." + getName());

			//runner.initBSFManager(doc, new ParamsTable(), webUser, new ArrayList<ValidateMessage>());
			result = runner.run(labelBuilder.toString(), getValueScript());
			text = result != null ? result.toString() : "";
		} else if (getType() != null && getType().equals(COLUMN_TYPE_FIELD)) {
			text = getValue(doc, runner, webUser, columnOptionsCache);
		}
		//列的格式类型未非常规类型时,需进行格式化
		if(this.getFormatType() != null && !this.getFormatType().equals(Column.FORMATTYPE_SIMPLE)){
			text = formatColumn(text);
		}
		return text;
	}
	


	/**
	 * 获取Column的真实值
	 * 
	 * @param doc
	 *            文档
	 * @return value 真实值
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getValue(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		String result = null;
		if (!StringUtil.isBlank(fieldName)) {
			if (fieldName.startsWith("$")) {
				String propertyName = fieldName.substring(1, fieldName.length());
				result = StringUtil.dencodeHTML(doc.getValueByPropertyName(propertyName));
				
				if((fieldName.equalsIgnoreCase("$auditorNames") || fieldName.equalsIgnoreCase("$stateLabel")) &&  !result.startsWith("[") &&  result.indexOf(":")>=0){
					String[] resultlist = result.split(":");
					//StringBuffer html = new StringBuffer("<table>");
					StringBuffer html = new StringBuffer();
					for(int i=0;i<resultlist.length;i++){
						String name = resultlist[i];
						//html.append("<tr><td title=\"").append(name).append("\">").append(name).append("&nbsp").append("</td></tr>");
						html.append(name);
					}
					//html.append("</table>");
					result =html.toString();
				}
				if(fieldName.equalsIgnoreCase("$stateLabel") && !StringUtil.isBlank(doc.getStateLabelInfo()) && doc.getStateLabelInfo().endsWith("]")){
					result = StringUtil.dencodeHTML(doc.getStateLabelInfo());
				}
			} else {
				FormField field = getFormField();
				
				
				if (SHOW_TYPE_TEXT.equals(getShowType()) 
						&& !(field instanceof AttachmentUploadField) 
						&& !(field instanceof ImageUploadField)) {	//显示值
					// 此方法慎用，有运算脚本的字段会影响性能
					result = field.getText(doc, runner, webUser, columnOptionsCache);
				} else {	//真实值
					result = field.getValue(doc, runner, webUser);
				}

			}
		}
		return (result != null ? result.toString() : "");
	}
	

	/**
	 * 获取真实值(仅文件(图片)上传调用)
	 * 
	 * @param doc
	 *            文档
	 * @return value 真实值
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getValue4FileUplad(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		String result = null;
		if (!StringUtil.isBlank(fieldName)) {
			FormField field = getFormField();
			result = field.getValue(doc, runner, webUser);
		}
		return (result != null ? result.toString() : "");
	}
	
	/**
	 * 返回真实值和显示值(用户选择框 \部门选择框\下拉选择框\智能提示框)
	 * 
	 * @param doc
	 *            文档
	 * @return value 真实值
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getValueAndText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		String result = null;
		if (!StringUtil.isBlank(fieldName)) {
			FormField field = getFormField();
			result = field.getValueAndText(doc, runner, webUser,columnOptionsCache);
		}
		return (result != null ? result.toString() : "");
	}
	
	public int compareTo(Object o) {
		if (o != null && o instanceof Column) {
			int thisOrderno = this.orderno;
			int otherOrderno = ((Column) o).orderno;
			return (thisOrderno - otherOrderno);
			// return (this.orderno - ((Column) o).orderno);
		}
		return -1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return super.equals(obj);
	}

	/**
	 * 获取关联的视图
	 * 
	 * @return
	 */
	public String getParentView() {
		return parentView;
	}

	/**
	 * 设置关联的视图
	 * 
	 * @param parentView
	 *            视图标识
	 */
	public void setParentView(String parentView) {
		this.parentView = parentView;
	}

	/**
	 * 列之前图片,用于流程回退时可在列前增加图片标识
	 * 
	 * @hibernate.property column="IMAGENAME"
	 * @return 图片路径F
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * 设置图片的标识
	 * 
	 * @param imageName
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * 第一列显示字体带有颜色,用于流程回退时可在第一列字体带有颜色
	 * 
	 * @hibernate.property column="FONTCOLOR"
	 * @return 颜色色值
	 */
	public String getFontColor() {
		return fontColor;
	}

	/**
	 * 设置第一列显示字体的颜色
	 * 
	 * @param fontColor
	 */
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	/**
	 * 获取流程是否增加回退标识(是:在第一表字体带有颜色,并在死前加上一个图片. 否:原型)
	 * 
	 * @hibernate.property column = "FLOWRETURNCSS"
	 * @return 布尔值
	 */
	public boolean getFlowReturnCss() {
		return flowReturnCss;
	}

	/**
	 * 设置流程是否增加回退标识
	 * 
	 * @param flowReturnCss
	 */
	public void setFlowReturnCss(boolean flowReturnCss) {
		this.flowReturnCss = flowReturnCss;
	}

	/**
	 * 查询流程是否是回退的状态,如果是回退的状态,在列前加一个小灯泡的标识,并第一列字体带有颜色
	 * 
	 * @param doc
	 *            Document(文档)
	 * @param text
	 *            列的显示值
	 * @return 以html的形式,来设置列的回退标识
	 * @throws Exception
	 */
	public String getFlowReturnOperation(Document doc, String text) throws Exception {
		StringBuffer html = new StringBuffer();

		if ("81".equals(doc.getLastFlowOperation())) {
			html.append("<img src=\"../../../resource/imgnew/backstatelabel" + getImageName() + ".gif\" border=\"0\"/>");
			//html.append("</img>");
			if (getFieldName() != null) {
				html.append("<font color=\"" + this.fontColor + "\">");
				html.append(text);
				html.append("</font>");				
			}
			html.append("</img>");
		} else {
			html.append(text);
		}
		return html.toString();
	}

	/**
	 * 获取FormField
	 * 
	 * @return FormField
	 */
	public FormField getFormField() {
		try {
			if (field == null) {
				FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				Form form = (Form) formProcess.doView(this.getFormid());
				if (form != null) {
					field = form.findFieldByName(this.getFieldName());
				}
			}

			if (field == null) {
				field = new NullField();
				field.setName(getFieldName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}

	/**
	 * 将列以网格的形式输出,提供视图打开方式为网格时调用此方法
	 * 
	 * @param doc
	 *            Document对
	 * @param runner
	 *            动态语言执行器
	 * @param webUser
	 *            webUser
	 * @return 标准的html中的表格形式输出
	 */
	public String toGridColumnHtml(Document doc, IRunner runner, WebUser webUser, boolean isEdit, Map<String,Options> columnOptionsCache) {
		StringBuffer htmlBuilder = new StringBuffer();

		try {
			htmlBuilder.append("<td class='table_th_td' ");
			String style = "";
			if (!StringUtil.isBlank(getWidth())) {
				if (getWidth().equals("0")) {
					style = " style='display: none;'";
				} else {
					style = " width= '" + getWidth() + "' style='max-width:"+ getWidth() +";word-wrap:break-word;'";
				}
			}

			htmlBuilder.append(style);
			htmlBuilder.append(" colWidth = '" + this.getWidth() + "'");
			htmlBuilder.append(" colGroundColor='" + this.getGroundColor() + "'");
			htmlBuilder.append(" colColor='" + this.getColor() + "'");
			htmlBuilder.append(" colFontSize='" + this.getFontSize() + "'");
			htmlBuilder.append(" colIsEdit='" + isEdit +"'");
			htmlBuilder.append(" colDocId='" + doc.getId() + "'");
			htmlBuilder.append(" displayType='" + this.getDisplayType() + "'");
			htmlBuilder.append(" displayLength='" + this.getDisplayLength() + "'");
			htmlBuilder.append(">");
			htmlBuilder.append(getContent(doc, runner, webUser, isEdit, columnOptionsCache));
			htmlBuilder.append("</td>\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return htmlBuilder.toString();
	}

	private String replaceContent(String content) {
		content = content.replaceAll("\\\"", "\\\\\"");
		content = content.replaceAll("\\\'", "\\\\\'");
		return content;
	}

	/**
	 * 获取单元格创建脚本
	 * 
	 * @param doc
	 *            Document对象
	 * @param runner
	 *            动态语言执行器
	 * @param webUser
	 *            webuser
	 * @return 脚本
	 * @throws Exception
	 */
	public String getCellCreateScript(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer scriptBuilder = new StringBuffer();

		scriptBuilder.append("createColumn({");
		if(!StringUtil.isBlank(this.getHiddenScript())){
			if(isHiddenColumn(runner)){
				scriptBuilder.append("style:'display:none;'");
			}else if(getWidth() != null){
				scriptBuilder.append("style:'width: ").append(getWidth()).append(";'");
			}
		}else if(!isVisible()){
			scriptBuilder.append("style:'display:none;'");
		}else if(getWidth() != null){
			if (getWidth().equals("0")) {
				scriptBuilder.append("style:'display:none;'");
			} else {
				scriptBuilder.append("style:'width: ").append(getWidth()).append(";'");
			}
		}
		
		scriptBuilder.append("}, \"");
		// 表格列内容
		String content = getContent(doc, runner, webUser, true, null);
		scriptBuilder.append(replaceContent(content));

		scriptBuilder.append("\")");
		return scriptBuilder.toString();
	}

	/**
	 * 获取单元格刷新脚本
	 * 
	 * @param doc
	 *            Document
	 * @param runner
	 *            动态语言执行器
	 * @param webUser
	 *            webuser
	 * @return 刷新脚本
	 * @throws Exception
	 */
	public String getCellRefreshScript(Document doc, IRunner runner, WebUser webUser) throws Exception {
		FormField formField = getFormField();

		StringBuffer refreshScript = new StringBuffer();
		if (formField != null) {
			String id = doc.getId() + "_" + this.getFieldName();
			String showId = id + "_show";
			String editId = id + "_edit";

			if (getType() != null && getType().equals(COLUMN_TYPE_SCRIPT)) {
				// 刷新显示值
				String textResult = this.getText(doc, runner, webUser);
				textResult = textResult.replaceAll("'", "\\\\'");
				refreshScript.append("refreshField('").append(showId).append("', '");
				refreshScript.append(showId).append("', '").append(textResult).append("');");
			} else if (getType() != null && getType().equals(COLUMN_TYPE_FIELD)) {
				if (formField.isCalculateOnRefresh() && formField instanceof ValueStoreField) {
					String destVal = doc.getItemValueAsString(formField.getName());
					String origVal = doc.get_params().getParameterAsString(formField.getName());
					if (formField.isRender(destVal, origVal)) {
						// 刷新显示值
						refreshScript.append("refreshField('").append(showId).append("', '");
						refreshScript.append(showId).append("', '").append(this.getText(doc, runner, webUser)).append(
								"');");

						// 刷新编辑器
						String fieldHTML = ((ValueStoreField) formField).toGridHtmlText(doc, runner, webUser, null);
						refreshScript.append("refreshField('").append(editId).append("', '");
						refreshScript.append(editId).append("', \"").append(replaceContent(fieldHTML)).append("\");");
					}
				}
			}
		}

		return refreshScript.toString();
	}

	/**
	 * 获取列数据组成html,并在列有数据显示栏中增加相应的效果
	 * 
	 * @param doc
	 *            Document
	 * @param runner
	 *            动态语言执行器
	 * @param webUser
	 *            webuser
	 * @return 以html的形式
	 * @throws Exception
	 */
	public String getContent(Document doc, IRunner runner, WebUser webUser, boolean isEdit, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer builder = new StringBuffer();

		String id = doc.getId();
		if(this.type.equals(COLUMN_TYPE_FIELD)){
			id += "_" + this.getFieldName();
		}else {
			id += "_";
		}
//		String id = doc.getId() + "_" + this.getFieldName();
		String showId = id + "_show";
		String editId = id + "_edit";

		FormField formField = getFormField();

		// String style = "";
		// if (!StringUtil.isBlank(getWidth())) {
		// if (getWidth().equals("0")) {
		// style = " style='display: none;'";
		// } else {
		// style = " style='width: " + getWidth() + ";'";
		// }
		// }

		String result = doc.getItemValueAsString(fieldName);
		String tempStr = this.getText(doc, runner, webUser, columnOptionsCache);
		
		HttpServletRequest request = ServletActionContext.getRequest();
		if(request != null){
			DataPackage<?> datas = null;
			if(request.getAttribute("datas") != null){
				//request设置datas属性见ViewRunTimeAction.java和ViewAction.java
				datas = (DataPackage<?>) request.getAttribute("datas");
			}
		}
		
		// 生成显示值的Div
		String titleStr = tempStr;
		if((formField instanceof ImageUploadField) || (formField instanceof ImageUploadToDataBaseField) || (formField instanceof FileManagerField) || (formField instanceof OnLineTakePhotoField))
			titleStr = formField.getValue(doc, runner, webUser);
		
		if(this.getFlowReturnCss())
			tempStr = getFlowReturnOperation(doc,tempStr);
		
		if(this.showTitle){
			builder.append("<div class='grid-column-show' title='").append(HtmlEncoder.encode(titleStr)).append("'");
		}else{
			builder.append("<div class='grid-column-show' title='").append("").append("'");
		}
		builder.append(" id=").append("'").append(showId).append("'");
		builder.append(" style='color:").append("#").append(color).append(";'");
//		if(isEdit){
//			builder.append(" onclick=").append("'").append("doRowEdit(\"" + doc.getId() + "\", this)").append("'");
//		}
		// builder.append(style);
		builder.append(">");
		if(!tempStr.equals("&nbsp")){
			if(!(formField instanceof ImageUploadField) && !(formField instanceof ImageUploadToDataBaseField) 
					&& !(formField instanceof FileManagerField) && !(formField instanceof OnLineTakePhotoField)
					&& !this.getType().equals(Column.COLUMN_TYPE_SCRIPT) && !this.getFlowReturnCss() && !(formField instanceof NullField))
				tempStr = HtmlEncoder.encode(tempStr);
		}
		if(formField instanceof WordField ){
			tempStr ="<span style='color:#f00'>{*[cn.myapps.core.dynaform.view.GridNotSupportWordField]*}<span>";
		}
		if(formField instanceof MapField){
			tempStr = "<img src = '" + request.getContextPath() + "/portal/share/images/view/map.png' style='margin: 0 5px;'>";
		}
		if(!isEdit && formField instanceof AttachmentUploadField && !StringUtil.isBlank(tempStr)){
			tempStr ="";
			JSONArray files = JSONArray.fromObject(result);
			for (Iterator<JSONObject> iterator = files.iterator(); iterator.hasNext();) {
				JSONObject file = iterator.next();
				tempStr+="<a href='" + request.getContextPath() + file.getString("path")+"' target='_blank'>"+file.getString("name")+"</a>&nbsp;" ;
				
			}
		}
		builder.append(tempStr);
		
//		String displayType = this.getDisplayType();
//		if (Column.DISPLAY_ALL.equals(displayType) || tempStr.equals("&nbsp")) { //默认显示全部文字
//			builder.append(tempStr);
//		} else { //显示部分文字
//			int displayLength = -1;
//			String length = this.getDisplayLength();
//			//不为空/空字符串/非数字类型
//			if(!StringUtil.isBlank(length) && length.matches("\\d+"))
//				displayLength = Integer.valueOf(length);
//			if (displayLength > -1) {
//				if (tempStr.length() > displayLength) {
//					String r = tempStr.substring(0,
//							displayLength);
//					builder.append(r + "...");
//				} else {
//					builder.append(tempStr);
//				}
//			} else {
//				builder.append(tempStr);
//			}
//		}
		builder.append("</div>");

		// 生成编辑器的Div
		if (formField instanceof ValueStoreField) {
			builder.append("<div class='grid-column-edit' style='display:none'");
			builder.append(" id=").append("'").append(editId).append("'");
			// builder.append(style);
			builder.append(" showType=\"" + getShowType() + "\" displayType=\"" + displayType + "\"");
			builder.append(">");
			builder.append(((ValueStoreField) formField).toGridHtmlText(doc, runner, webUser, columnOptionsCache));

			builder.append("</div>");
		}
		// 为‘序号列’生成编辑器的Div，还原表格行时需用到
		if(formField instanceof NullField && (this.type.equals(COLUMN_TYPE_ROWNUM) || this.type.equals(COLUMN_TYPE_SCRIPT))){
			builder.append("<div class='grid-column-edit' style='display:none'");
			builder.append(" id=").append("'").append(editId).append("'");
			builder.append(">");
			builder.append("<input type='hidden' isBorderType='false' id='").append(id).append("'");
			builder.append(" name='").append(this.getFieldName()).append("'");
			builder.append(" value='").append(tempStr).append("'>");
			if(this.type.equals(COLUMN_TYPE_SCRIPT)){
				builder.append(tempStr);
			}
			builder.append("</div>");
		}
		return builder.toString();
	}

	/**
	 * 是否汇总
	 * 
	 * @return
	 */
	public boolean isSum() {
		return sum;
	}

	/**
	 * 设置是否汇总
	 * 
	 * @param sum
	 */
	public void setSum(boolean sum) {
		this.sum = sum;
	}

	/**
	 * 获取汇总值
	 * 
	 * @return 汇总值
	 * @throws Exception
	 */
	public String getSum(WebUser user, ParamsTable params) throws Exception {
		if (isSum()) {
			ViewProcess vp = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			View parent = (View) vp.doView(parentView);
			DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,parent.getApplicationid());
			if (parent != null) {
				Document doc = parent.getSearchForm().createDocument(params, user);

				String query = vp.getQueryString(parent, params, user, doc);
				String label = getName() + "{*[cn.myapps.core.dynaform.view.Grant_Total]*}";
				if (parent.getEditMode().equals(View.EDIT_MODE_CODE_DQL)) {
					return label + ": " + Double.toString(dp.sumByDQL(query, fieldName, user.getDomainid()));
				} else {
					return label + ": " + Double.toString(dp.sumBySQL(query, user.getDomainid()));
				}
			}
		}

		return "";
	}

	/**
	 * 设置当前页面数据小计,在页面的右下角显示
	 * 
	 * @param datas
	 *            数据集合
	 * @return 数据汇总数
	 * @throws Exception
	 */
	public String getSumByDatas(DataPackage<Document> datas, IRunner runner, WebUser webUser) {
		double total = 0;
		String numberPattern = "";
		String num = "0";
		if (isSum()) {
			try {
				FormField formField = getFormField();
				if (formField instanceof InputField) {
					InputField inputField = (InputField) formField;
					numberPattern = inputField.getNumberPattern();
				}
			} catch (Exception e) {
				log.warn("get number pattern error!");
			}

			for (Iterator<Document> iterator = datas.datas.iterator(); iterator.hasNext();) {
				Document doc = iterator.next();
				try {
					runner.declareBean("$CURRDOC", new CurrDocJsUtil(doc), CurrDocJsUtil.class);

					String tempVal = getText(doc, runner, webUser);
					if(tempVal.contains(",")){
						tempVal = tempVal.replace(",", "");
					}
					if(tempVal.contains("￥")){
						tempVal = tempVal.replace("￥", "");
					}
					if(tempVal.contains("$")){
						tempVal = tempVal.replace("$", "");
					}
					if(tempVal.contains("£")){
						tempVal = tempVal.replace("£", "");
					}
					
					double val = Double.parseDouble(tempVal);
					total += val;
				} catch (Exception e) {
					log.warn("sum data error!");
				}
			}

			
			if(this.getFormatType() != null && !this.getFormatType().equals(Column.FORMATTYPE_SIMPLE)){
				num = formatColumn(total+"");
			} else if (!StringUtil.isBlank(numberPattern)){
				DecimalFormat format = new DecimalFormat(numberPattern);
				num = format.format(total);
			}else {
				num = "" + total;
			}

			return "{*[cn.myapps.core.dynaform.view.current_page_total]*}:" + num;
		}
		return "";
	}
	
	/**
	 * 设置所查所有数据总计,在页面的右下角显示
	 * @param webUser
	 * @return
	 */
	public String getSumTotal(WebUser webUser, String domainid) {
		ParamsTable params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
		return getSumTotal(webUser, domainid, params);
	}
	
	/**
	 * 表单打印时,表单有包含元素包含视图时汇总
	 * @param webUser
	 * @param domainid
	 * @param viewid
	 * @return
	 */
	public String getSumTotal(WebUser webUser, String domainid, ParamsTable params){
		if(this.getType().equals(Column.COLUMN_TYPE_SCRIPT)){
			return "";
		}
		double total = 0;
		String numberPattern = "";
		String num = "0";
		String viewid = this.parentView;
		if(viewid == "" || viewid == null){
			return "";
		}
		if (isTotal()) {
			try {
				FormField formField = getFormField();
				if (formField instanceof InputField) {
					InputField inputField = (InputField) formField;
					numberPattern = inputField.getNumberPattern();
				}
			} catch (Exception e) {
				log.warn("get number pattern error!");
			}
			
			try {
				ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
				View view = (View) viewProcess.doView(viewid);
				if(view.getEditModeType() instanceof DesignEditMode){
					total = view.getViewTypeImpl().getSumTotal(params, webUser, this.getFieldName(), this.getFormid(), domainid);
				}else {
					return "";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if(this.getFormatType() != null && !this.getFormatType().equals(Column.FORMATTYPE_SIMPLE)){
				num = formatColumn(total+"");
			} else if (!StringUtil.isBlank(numberPattern)){
				DecimalFormat format = new DecimalFormat(numberPattern);
				num = format.format(total);
			}else {
				num = "" + total;
			}

			return "  {*[cn.myapps.core.dynaform.view.Grant_Total]*}:"+num;
		}
		return "";
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}
	
	public String getIsOrderByField() {
		return isOrderByField;
	}

	public void setIsOrderByField(String isOrderByField) {
		this.isOrderByField = isOrderByField;
	}
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * 获取隐藏脚本
	 * 
	 * @return 隐藏脚本
	 * @hibernate.property column="HIDDENSCRIPT" type = "text"
	 * @uml.property name="hiddenScript"
	 */
	public String getHiddenScript() {
		return hiddenScript;
	}

	/**
	 * 设置隐藏脚本
	 * 
	 * @param hiddenScript
	 *            隐藏脚本
	 * @uml.property name="hiddenScript"
	 */
	public void setHiddenScript(String hiddenScript) {
		this.hiddenScript = hiddenScript;
	}

	public String getMappingField() {
		return mappingField;
	}

	public void setMappingField(String mappingField) {
		this.mappingField = mappingField;
	}
	
	/**
	 * 获取操作按钮类型
	 * @return
	 */
	public String getButtonType() {
		return buttonType;
	}

	/**
	 * 设置操作按钮类型
	 * @param buttonType
	 */
	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	public String getApproveLimit() {
		return approveLimit;
	}

	public void setApproveLimit(String approveLimit) {
		this.approveLimit = approveLimit;
	}
	
	/**
	 * 获取图标路径
	 * @return
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 设置图标路径
	 * @param icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 是否单击列头排序
	 * @param clickSorting
	 */
	public void setClickSorting(boolean clickSorting) {
		this.clickSorting = clickSorting;
	}

	/**
	 * 是否单击列头排序
	 * @return
	 */
	public boolean isClickSorting() {
		return clickSorting;
	}

	public boolean isHiddenColumn(IRunner runner) {
		try {
			if (this.getHiddenScript() != null && this.getHiddenScript().trim().length() > 0) {
				StringBuffer label = new StringBuffer();
				label.append("View").append(".Activity(").append(this.getId()).append(")." + this.getName()).append(
						".runHiddenScript");

				Object result = runner.run(label.toString(), this.getHiddenScript());// 运行脚本
				if (result != null && result instanceof Boolean) {
					return ((Boolean) result).booleanValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
