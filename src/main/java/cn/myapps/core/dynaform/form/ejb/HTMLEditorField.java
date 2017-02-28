// Source file:
// C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\InputField.java

package cn.myapps.core.dynaform.form.ejb;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;

/**
 * HTMLEditor(html的编辑器的组件)
 * 
 * @author Marky
 * 
 */
public class HTMLEditorField extends FormField implements ValueStoreField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5462825321461415157L;

	/**
	 * 获取ITEM保存类型
	 * (VALUE_TYPE_VARCHAR,VALUE_TYPE_NUMBER,VALUE_TYPE_DATE,VALUE_TYPE_BLOB
	 * ,VALUE_TYPE_TEXT,VALUE_TYPE_STRINGLIST,VALUE_TYPE_INCLUDE) 详细你参考ITEM的常量
	 */
	
	protected String areaWidth;
	
	protected String areaHeight;
	
	public String getAreaWidth() {
		if(this.areaWidth==null || "".equals(this.areaWidth)){
			this.areaWidth = "600px";
		}
		if(!this.areaWidth.contains("%") && !this.areaWidth.contains("px")){
			return this.areaWidth + "px";
		}
		return this.areaWidth;
	}

	public void setAreaWidth(String areaWidth) {
		this.areaWidth = areaWidth;
	}

	public String getAreaHeight() {
		if(this.areaHeight==null || "".equals(this.areaHeight)){
			this.areaHeight = "200px";
		}
		if(!this.areaHeight.contains("px")){
			return this.areaHeight + "px";
		}
		return this.areaHeight;
	}

	public void setAreaHeight(String areaHeight) {
		this.areaHeight = areaHeight;
	}

	public String getFieldtype() {
		return Item.VALUE_TYPE_TEXT;
	}

	/**
	 * 构造函数
	 * 
	 * @roseuid 41ECB66E012A
	 */
	public HTMLEditorField() {

	}

	/**
	 * 实现
	 */
	public ValidateMessage validate(IRunner runner, Document doc)
			throws Exception {
		return null;
	}

	/**
	 * 根据HTEMLEDITORFIELD的显示类型不同,返回的结果字符串不同.
	 * 新建的Document,HTEMLEDITORFIELD的显示类型为默认的MODIFY
	 * 。此时根据Form模版的HTEMLEDITORFIELD内容结合Document的Item的值,返回的字符串为重定义后的html.
	 * 若根据流程节点设置对应HTEMLEDITORFIELD的显示类型不同,返回的结果字符串不同.
	 * 
	 * 1)若节点设置对应HTEMLEDITORFIELD的显示类型为隐藏HIDDEN（值为3），返回 “******”字符串。
	 * 2)若为MODIFY(值为2)时,Form模版的html编辑器组件内容结合Document中的ITEM存放的值,返回重定义后的html。
	 * 否则返回字符串内容为Document中的ITEM存放的值重定义后的html，
	 * 
	 * @param doc
	 *            文档对象
	 * @param runner
	 *            动态语言脚本执行器
	 * @param webUser
	 *            webuser
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 字符串内容为重定义后的html的html编辑器组件标签及语法
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		
		
		StringBuffer html = new StringBuffer();
		
		Item item = null;
		int displayType = getDisplayType(doc, runner, webUser, permissionType);

		if(displayType == PermissionType.HIDDEN){
			return getHiddenValue();
		}
		try{
			if (doc != null) {
				item = doc.findItem(this.getName());
				String text =  item.getTextvalue();
				String tmp = null;
					html.append("<textarea moduleType='htmlEditor' data-echance='false' style='display:none;WIDTH: " + getAreaWidth() + "; HEIGHT: " + getAreaHeight() + "' ");
					html.append(" displayType='" + displayType + "'");
					html.append(" id='" + getId() + "'");
					html.append(" name='" + getName() + "' ");
//					html.append(" text='" + text + "' ");
					//表单描述字段
					html.append(" _discript ='" + getDiscript() + "'");
					html.append(">");
					if (text != null) {
						tmp = HtmlEncoder.encode(text);
						html.append(tmp);
					}
					html.append("</textarea>");
				}
		   return html.toString();
		}
		catch(Exception e){
			throw new OBPMRuntimeException();
		}
			
	}

	/**
	 * 
	 * Form模版的html编辑器组件内容结合Document中的ITEM存放的值,返回重定义后的打印html文本.
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态语言脚本执行器
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * 
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return Form模版的html编辑器组件内容结合Document中的ITEM存放的值为重定义后的打印html
	 * @throws Exception
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
		Item item = null;
		int displayType = getPrintDisplayType(doc, runner, webUser);
		if (displayType == PermissionType.HIDDEN) {
			return this.getPrintHiddenValue();
		} else {
			if (doc != null) {
				item = doc.findItem(this.getName());
				String text = item.getTextvalue();
				html
						.append("<div style='width:100%; height:80%;border: 0px solid black;'>");
				if (text != null) {
					html.append(text);
				}
				html.append("</div>");
				return html.toString();
			}
		}
		return html.toString();
	}
	
	

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
		Item item = null;
		if (doc != null) {
			item = doc.findItem(this.getName());
			String text = item.getTextvalue()+item.getTextvalue();
			//正则表达式判断标签为img时，删除_xhe_src属性和补全src路径。 2015-1-26
			Matcher matcher = Pattern.compile("<img .+?>").matcher(text);
			StringBuffer sbr = new StringBuffer();
			while (matcher.find()) {
				String str = matcher.group();
				str = Pattern.compile("_xhe_src=\".+?\"").matcher(str).replaceAll("").replace("src=\"/", "src=\""+webUser.getServerAddr());
			    matcher.appendReplacement(sbr, str);
			}
			matcher.appendTail(sbr);
			text = sbr.toString();
			html.append("<div style='width:100%; height:80%;border: 0px solid black;'>");
			if (text != null) {
				html.append(text);
			}
			html.append("</div>");
			return html.toString();
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
		template.append("<input type='text'");
		template.append(" className='" + this.getClass().getName() + "'");
		template.append(" id='" + getId() + "'");
		template.append(" name='" + getName() + "'");
		template.append(" formid='" + getFormid() + "'");
		template.append(" discript='" + getDiscript() + "'");
		template.append(" refreshOnChanged='" + isRefreshOnChanged() + "'");
		template.append(" valueScript='" + getValueScript() + "'");
		template.append(" editMode='" + getEditMode() + "'");
		template.append(" hiddenScript='" + getHiddenScript() + "'");
		template.append(" hiddenPrintScript='" + getHiddenPrintScript() + "'");
		template.append(">");
		return template.toString();
	}

	public void recalculate(IRunner runner, Document doc, WebUser webUser)
			throws Exception {
		getLog().debug("HTMLEditorField.recalculate");
		runValueScript(runner, doc);
	}

	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
		
		xmlText.append("<").append(MobileConstant.TAG_HTMLFIELD).append(" ");
		xmlText.append(" ").append(MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");
		if (displayType == PermissionType.READONLY || displayType == PermissionType.DISABLED) {
			xmlText.append(MobileConstant.ATT_TEXTTYPE).append("='readonly' ");
		}
			
		if (displayType == PermissionType.HIDDEN || getTextType().equalsIgnoreCase("hidden")) {
			xmlText.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
			if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
				xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
			}
		}

		xmlText.append(" ").append(MobileConstant.ATT_NAME).append("='" + getName() + "'>");

		if (doc != null) {
			Item item = doc.findItem(this.getName());
	
			if (item != null && item.getValue() != null) {
				Object value = item.getValue();
				String valueStr = (new sun.misc.BASE64Encoder()).encode(((String)value).getBytes("utf-8"));
				xmlText.append(valueStr);
			}
		}

		xmlText.append("</").append(MobileConstant.TAG_HTMLFIELD).append(">");
		return xmlText.toString();
	}

	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
			throws Exception {
		return null;
	}

	public String getValueMapScript(Document doc, ParamsTable params,
			WebUser user) {
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),
				params.getParameterAsString("applicationid"));
		StringBuffer scriptBuffer = new StringBuffer();
		try {
			int displayType = this.getDisplayType(doc, runner, user);
			if (displayType == PermissionType.MODIFY){
				scriptBuffer.append("setHTMLValue();");
				scriptBuffer.append("valuesMap['" + this.getName() + "'] = ev_getValue('" + this.getName() + "');");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scriptBuffer.toString();
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
		
		xmlText.append("<").append(MobileConstant2.TAG_HTMLFIELD).append(" ");
		xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");
		if (displayType == PermissionType.READONLY || displayType == PermissionType.DISABLED) {
			xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='").append("true").append("'");
		}
			
		if (displayType == PermissionType.HIDDEN || getTextType().equalsIgnoreCase("hidden")) {
			xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='").append("true").append("'");
			if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"'");
			}
		}

		xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='" + getName() + "'>");

		if (doc != null) {
			Item item = doc.findItem(this.getName());
	
			if (item != null && item.getValue() != null) {
				Object value = item.getValue();
				String valueStr = (new sun.misc.BASE64Encoder()).encode(((String)value).getBytes("utf-8"));
				xmlText.append(valueStr);
			}
		}

		xmlText.append("</").append(MobileConstant2.TAG_HTMLFIELD).append(">");
		return xmlText.toString();
	}

}
/*
 * FormField InputField.init(java.lang.String){ return null; }
 */
