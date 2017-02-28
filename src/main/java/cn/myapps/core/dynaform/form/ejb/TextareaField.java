//Source file:
//C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\InputField.java

package cn.myapps.core.dynaform.form.ejb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.AbstractRunner;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.StringUtil;

/**
 * @author Marky
 */
public class TextareaField extends FormField implements ValueStoreField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2705826322040773848L;

	protected static String cssClass = "textarea-cmd";

	/**
	 * @roseuid 41ECB66E012A
	 */
	public TextareaField() {

	}

	/**
	 * @roseuid 41ECB66E0152
	 */
	public void store() {

	}

	/**
	 * 根据TextareaField的显示类型不同,返回的结果字符串不同.
	 * 新建的Document,TextareaField的显示类型为默认的MODIFY。此时根据Form模版的TextareaField内容结合Document的Item的值,返回的字符串为重定义后的html.
	 * 还可以根据节点设置对应TextareaField的显示类型不同,返回的结果字符串不同.
	 * 1)节点设置对应TextareaField为隐藏时,返回"******"字符串.
	 * 2)若节点设置对应TextareaField为只读根据Form模版的多文档框(TextareaField)组件内容结合Document中的ITEM存放的值,
	 * 返回重定义后的html的多文本框Textarea，并且多文本框Textarea值为只读.
	 * 3)若节点设置对应TextareaField为DISABLED,返回重定义后的html的多文本框Textarea为DISABLED.
	 * 否则根据Form模版的多文档框(TextareaField)组件内容结合Document中的ITEM存放的值,返回重定义后的html的多文本框Textarea.
	 * 
	 * @param doc
	 *            文档对象
	 * @see cn.myapps.core.dynaform.form.ejb.FormField#getDisplayType(AbstractRunner,
	 *      Document, WebUser, int)
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的html为Form模版的多文档框(TextareaField)组件内容结合Document中的ITEM存放的值,
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {

		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser,permissionType);
		html.append("<textarea moduleType=\"textarea\"");
		html.append(toOtherpropsHtml());
		html.append(toAttr(doc, displayType));
		html.append(" >");
		html.append(toValue(doc));
		html.append("</textarea>");
		return html.toString();
	}

	private String toAttr(Document doc, int displayType) throws Exception {
		StringBuffer html = new StringBuffer();
		html.append(" _displayType = '" + displayType + "'");
		html.append(" _textType='" + getTextType() + "'");
		html.append(" _discript ='" + getDiscript() + "'");
		html.append(" _hiddenValue='" + getHiddenValue() + "'");
		html.append(" _id=\"" + getFieldId(doc) + "\"");
		html.append(" _name=\"" + getName() + "\"");
		html.append(" _fieldType=\"" + getTagName() + "\"");
		html.append(" _isRefreshOnChanged=\"" + isRefreshOnChanged() + "\"");
		html.append(" _cssClass=\"" + cssClass + "\"");
		html.append(" _isBorderType=\"" + isBorderType() + "\"");
		//html.append(" value=\"" + toValue(doc) + "\"");
		return html.toString();
	}

	/**
	 * 获取TextareaField的值
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @return TextareaField的值
	 */
	public String toValue(Document doc) {
		StringBuffer html = new StringBuffer();
		// 生成值
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			if (item != null && item.getValue() != null) {
				html.append(HtmlEncoder.encode((String)item.getValue()+""));
			}
		}
		return html.toString();
	}

	/**
	 * 获取TextareaField的width
	 * 
	 * @return TextareaField的width
	 */
	protected String toWidthHtml() {
		StringBuffer buffer = new StringBuffer();
		Map<String, String> coll = getOtherPropsAsMap();
		Collection<Entry<String, String>> entrys = coll.entrySet();
		Iterator<Entry<String, String>> it = entrys.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) it.next();
			if(entry.getKey().equals("style")){
				if(!StringUtil.isBlank(entry.getValue())){
					String _temp = entry.getValue().toLowerCase();
					int _index = _temp.indexOf("width");
					if(_index >-1){
						_temp = _temp.substring(_index);
						int _index2 = _temp.indexOf(";");
						if(_index2 >-1){
							_temp = _temp.substring(0, _index2);
							
						}
						_temp = _temp.replace("width", "");
						_temp = _temp.replace(":", "");
					}else{
						_temp = "200px";
					}
					buffer.append(_temp.trim());
				}
				break;
			}
		}
		return buffer.toString();
	}

	/**
	 * 
	 * Form模版的多文档框(TextareaField)组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的打印html文本
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
	 * @return 重定义后的打印html为Form模版的多文档框(TextareaField)组件内容结合Document中的ITEM存放的值
	 * @throws Exception
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}

			Item item = doc.findItem(this.getName());

			if (item != null && item.getValue() != null && !StringUtil.isBlank(item.getValue().toString())) {
				html.append("<SPAN ");
				html.append(toOtherpropsHtml());
				html.append("><span style=\"width: 100%;");
				html.append("word-wrap: break-word; word-break: break-all;\">");
				html.append(item.getValue().toString().replaceAll("\n", "<br/>"));
				html.append("</span></SPAN>");
			}
		}
		return html.toString();
	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			Item item = doc.findItem(this.getName());

			if (item != null && item.getValue() != null && !StringUtil.isBlank(item.getValue().toString())) {
				html.append("<SPAN><p style=\"width: 100%;");
				html.append("word-wrap: break-word; word-break: break-all;\">");
				html.append(item.getValue().toString().replace("\n", "<br/>") + "");
				html.append("</p></SPAN>");
				//html.append(printHiddenElement(doc));
			}
		}
		return html.toString();
	}

	/**
	 * 返回模板描述多文本.
	 * 
	 * @return 字符串内容为描述多文本
	 * @roseuid 41E7917A033F
	 */
	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<textarea'");
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
	 * 多文本框值脚本重新计算.
	 * 
	 * @roseuid 41DB89D700F9
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
		getLog().debug("TextareaField.recalculate");
		runValueScript(runner, doc);
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
		html.append("<").append(MobileConstant.TAG_TEXTAREAFIELD).append(" ").append(
				MobileConstant.ATT_LABEL).append(" ='").append(getMbLabel() + "'");
		html.append(" ").append(MobileConstant.ATT_NAME).append("='" + getName() + "'");
		
		if (displayType == PermissionType.READONLY || displayType == PermissionType.DISABLED) {
			html.append(" ").append(MobileConstant.ATT_DISABLED).append(" ='true' ");
		}
		
		if (displayType == PermissionType.HIDDEN) {
			html.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
			if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
				html.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
			}
		}

		if (isRefreshOnChanged()) {
			html.append(" ").append(MobileConstant.ATT_REFRESH).append("='true'");
		}
		html.append(">");
		if(doc!=null){
			Item item = doc.findItem(this.getName());
			if (item != null && item.getValue() != null) {
				html.append(HtmlEncoder.encode((String)item.getValue()+""));
			}
		}
		html.append("</").append(MobileConstant.TAG_TEXTAREAFIELD).append(">");

		return html.toString();
	}
	
	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
		html.append("<").append(MobileConstant2.TAG_TEXTAREAFIELD);
		html.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");
		html.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'").append(" ");
		
		if (displayType == PermissionType.READONLY || displayType == PermissionType.DISABLED) {
			html.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
		}
		
		if (displayType == PermissionType.HIDDEN) {
			html.append(" ").append(MobileConstant2.ATT_HIDDEN).append(" ='true'");
			if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
				html.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
			}
		}
		
		if (isRefreshOnChanged()) {
			html.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
		}
		html.append(">");
		
		if(doc!=null){
			Item item = doc.findItem(this.getName());
			if (item != null && item.getValue() != null) {
				html.append(HtmlEncoder.encode((String)item.getValue())).append("");
			}
		}
		html.append("</").append(MobileConstant2.TAG_TEXTAREAFIELD).append(">");

		return html.toString();
	}
	

	/**
	 * 根据Form模版的TextareaField组件内容结合Document中的ITEM存放的值,输出重定义后的html文本以网格显示
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @return 重定义后的html文本
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (displayType == PermissionType.HIDDEN) {// 节点设置对应field隐藏
			return this.getHiddenValue();
		} else {
			html.append("<textarea moduleType=\"textarea\"");
			html.append(" _subGridView='true'");
			html.append(toOtherpropsHtml());
			html.append(toAttr(doc, displayType));
			html.append(">");
			html.append(toValue(doc));
			html.append("</textarea>");
		}
		return html.toString();
	}

	/**
	 * 获取组件名
	 * 
	 * @return 组件名
	 */
	public String getTagName() {
		return "TextAreaField";
	}

}
