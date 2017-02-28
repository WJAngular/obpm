//Source file: C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\InputField.java

package cn.myapps.core.dynaform.form.ejb;

import java.io.Serializable;
import java.util.regex.Pattern;

import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

/**
 * @author nicholas
 */
public class Textpart implements FormElement, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1227998221872724851L;

	// private static String cssClass = "textpart-cmd";

	/**
	 * @uml.property name="text"
	 */
	private String text;
	
	/**
	 * 标签名
	 */
	private String tagName;

	/**
	 * @roseuid 41ECB66E012A
	 */
	public Textpart() {

	}
	
	/**
	 * 包含“标签名”和“标签”的构造函数
	 */
	public Textpart(String tagName,String text ) {
		super();
		this.text = text;
		this.tagName = tagName.toLowerCase();
	}



	/**
	 * @return boolean
	 * @roseuid 41ECB66E013E
	 */
	public boolean validate() {
		return true;
	}

	/**
	 * @roseuid 41ECB66E0152
	 */
	public void store() {

	}

	/**
	 * @param doc
	 * @return java.lang.String
	 * @roseuid 41ECB66E015C
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) {
		return text;
	}

	public String runOptionsScript() {
		return null;
	}

	public String toTemplate() {
		return text;
	}

	public Item createItem(Object value) {
		// Nothing todo.
		return null;
	}

	/**
	 * 返回文本内容
	 * 
	 * @return Returns the text.
	 * @uml.property name="text"
	 */
	public String getText() {
		if (text == null) {
			text = "";
		}
		return text;
	}

	/**
	 * 设置文本内容
	 * 
	 * @param text
	 *            The text to set.
	 * @uml.property name="text"
	 */
	public void setText(String text) {
		this.text = text;
	}

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		return text;
	}

	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		return text;
	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		if(tagName !=null && tagName.equals("img")){
			//标签为img时，删除“_fcksavedurl”属性，补充src路径。
			text = Pattern.compile("_fcksavedurl='.+?' ").matcher(text).replaceAll("");
			text = text.replace("src='/", "src='"+webUser.getServerAddr());
		}
		return text;
	}

	/**
	 * @return 标签名
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * 设置标签名
	 * @param tagName
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
}
/*
 * FormField TextpartField.init(java.lang.String){ return null; }
 */
