package cn.myapps.core.dynaform.form.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import com.opensymphony.xwork2.ActionContext;


import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.sequence.Sequence;

public class WordField extends FormField implements ValueStoreField {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9221526684588846664L;

	private String openType;

	/**
	 * 签章脚本
	 */
	private String signatureScript;
	
	/**
	 * 模板和套红脚本
	 */
	private String templateScript;
	
	/**
	 * 留痕脚本
	 */
	private String revisionScript;

	public String getSignatureScript() {
		return signatureScript;
	}

	public void setSignatureScript(String signatureScript) {
		this.signatureScript = signatureScript;
	}

	public String getTemplateScript() {
		return templateScript;
	}

	public void setTemplateScript(String templateScript) {
		this.templateScript = templateScript;
	}

	public String getRevisionScript() {
		return revisionScript;
	}

	public void setRevisionScript(String revisionScript) {
		this.revisionScript = revisionScript;
	}

	/**
	 * 
	 * Form模版的WordField组件内容结合Document中的ITEM存放的值,返回重定义后的html
	 * 
	 * @param doc
	 *            文档对象
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的html为Form模版的WordField组件内容结合Document中的ITEM存放的值
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		StringBuffer html = new StringBuffer();
		Item item = null;
		boolean saveable = true;
		int displayType = getDisplayType(doc, runner, webUser, permissionType);

		String isSignature = "";
		if(!runBooleanScript(runner, "signatureScript", StringUtil.dencodeHTML(getSignatureScript()))){
			isSignature += "1";
		} 
		if(!runBooleanScript(runner, "templateScript", StringUtil.dencodeHTML(getTemplateScript()))){
			isSignature += "2";
		} 
		if(!runBooleanScript(runner, "revisionScript", StringUtil.dencodeHTML(getRevisionScript()))){
			isSignature += "3";
		}
		
		
		ParamsTable params = new ParamsTable();
		params.setParameter("saveable", saveable);
		params.setParameter("displayType", displayType);
		params.setParameter("signature", isSignature);
		if (webUser.isDeveloper() || webUser.isDomainAdmin()
				|| webUser.isSuperAdmin()) {
			saveable = false;
		}
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				item = doc.findItem(this.getName());
				if (item == null) {
					item = new Item();
					try {
						item.setId(Sequence.getSequence());
					} catch (Exception e) {
					}
				}
					html = toHtml(doc, params, getOpenType());
			}
		}
		return html.toString();
	}

	public StringBuffer toPreviewHtml(Document doc, int displayType)
			throws Exception {
		StringBuffer html = new StringBuffer();

		if (getOpenType() != null
				&& ("2".equals(getOpenType()) || "3".equals(getOpenType()))) {
			html.append("<button class='button-class'");
			html.append(" onclick=\"preWordDialog('" + getId() + "', '"
					+ this.getName() + "', 'WordControl', '"
					+ Sequence.getSequenceTimes() + "', '" + this.getName()
					+ "'");
			html.append(", " + getOpenType() + ", " + displayType + ")\"");
			html.append(">");

			html.append(" <img src='" + this.getContextPath(doc)
					+ "/core/dynaform/form/images/word.gif'></img>");
			html.append("</button>");
			html
					.append("<font size=2 color='red'>" + getDiscript()
							+ "</font>");
		} else {// 生成一个iframe
			html.append("<iframe id='" + getId() + "' src='");
			html.append(this.getContextPath(doc)
					+ "/core/dynaform/document/newword.action?id=" + getId());
			html.append("&_docid=" + Sequence.getSequenceTimes());
			html.append("&filename=" + this.getName() + "&_type=word");
			html.append("&_isEdit=1");
			html.append("&saveable=false" + "'");
			html.append(" name='word'");
			html
					.append(" frameborder='0' width='100%' height='645px' scrolling='no' style='overflow:visible;z-index:-1px;' type='word'");
			html.append("></iframe>");

		}
		return html;
	}

	/**
	 * 
	 * Form模版的WordField内容结合Document中的ITEM存放的word文档的名,返回重定义后的html，
	 * 
	 * @param doc
	 * @param type
	 *            显示方式(1:默认(在页面以iframe的方式显示) 2:弹出窗口 3: 弹出层)
	 * @param doc
	 *            (Document)文档对象
	 * @param isPre
	 *            是否为预览模式
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的html
	 * 
	 */

	public StringBuffer toHtml(Document doc, ParamsTable params, String type) throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<input type='hidden' moduleType='wordField' ");
		html.append(" name='" + this.getName() + "'");
		html.append(" id='" + getName() + "'");
		html.append(" getId='" + getId() + "'");
		//表单描述字段
//		html.append(" discript ='" + getDiscript() + "'");
		String wordid = String.valueOf(Sequence.getSequenceTimes());
		html.append(" showWord='{*[Show]*} {*[Word]*}'");
		html.append(" wordid='" + wordid + "'");
		html.append(" secondvalue='" + wordid + "'");
		html.append(" getItemValue='" + doc.getItemValueAsString(this.getName()) + "'");
		html.append(" docgetId='" + doc.getId() + "'");
		html.append(" docgetFormname='" + doc.getFormname() + "'");
		html.append(" openType='" + getOpenType() + "'");
		html.append(" displayType='" + params.getParameter("displayType") + "'");
		html.append(" contentversions='content.versions'");
		html.append(" saveable='" + params.getParameterAsBoolean("saveable")+"'");
		html.append(" isSignature='" + params.getParameterAsBoolean("isSignature")+ "'");
		html.append(" filename='" + Sequence.getSequenceTimes() + "'");
		html.append(" _docid='" + doc.getId() + "'");
		html.append(" _docid='" + doc.getId() + "'");
		html.append(" _type= 'word' ");
		html.append(" fieldname='" + this.getName() + "'");
		html.append(" formname='" + doc.getFormname() + "'");
		html.append(" content.versions='" + doc.getVersions() + "'");
		html.append(" application='" + doc.getApplicationid() + "'");
		html.append(" _isEdit='" + params.getParameter("displayType") + "'");
		html.append(" signature='" + params.getParameter("signature") + "'");
		html.append("/>");
		return html;
	}

	/**
	 * 生成SCRIPT的方法
	 * 
	 * @param doc
	 *            文档对象
	 * @param displayType
	 *            类型
	 * @return 生成的function
	 * @throws Exception
	 */
	public String addScript(Document doc, boolean saveable, int displayType)
			throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<script>");
		html.append("function showWordControl(){");
		html
				.append("var id =document.getElementById('" + getId()
						+ "').value;");
		html.append(" var fileName = document.getElementById(\"" + getName()
				+ "\").value;");
		html
				.append("showWordDialog('{*[Show]*} {*[Word]*}', 'WordControl', id, ");
		if (doc.getItemValueAsString(this.getName()) != null
				&& !doc.getItemValueAsString(this.getName()).equals("")) {
			html.append("'" + doc.getItemValueAsString(this.getName()) + "'");
		} else {
			html.append("'" + doc.getId() + "'");
		}
		html.append(", '" + getName() + "', " + getOpenType() + ", "
				+ displayType + ", ");
		if (saveable) {
			html.append("true)");
		} else {
			html.append("false)");
		}
		html.append("}");
		html.append("</script>");

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
		template.append(" hiddenScript='" + getHiddenScript() + "'");
		template.append(" hiddenPrintScript='" + getHiddenPrintScript() + "'");
		template.append(" opentype='" + getOpenType() + "'");
		template.append(">");
		return template.toString();
	}

	/**
	 * 空实现
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser)
			throws Exception {
	}

	/**
	 * 空实现
	 */
	public Object runValueScript(IRunner runner, Document doc) throws Exception {
		return null;
	}

	/**
	 * modify：修复打印word时没有数据问题。
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}
			//word控件不支持表单中打印
//			Item item = doc.findItem(this.getName());
//
//			if (item != null && item.getValue() != null) {
//				html.append("<pre>");
//				if (item.getTextvalue().trim().length() > 0) {
//					html.append(getWordValue(item.getTextvalue()) + "");
//				}
//				html.append("</pre>");
//			}
		}
		return html.toString();
	}
	
	

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<SPAN >");
		html.append("<img border='0' width='"+16+"' height='"+16+"' src='" +webUser.getServerAddr() + doc.get_params().getContextPath() + "/core/dynaform/form/images/word.gif" + "' />");
		html.append("</SPAN>");
		return html.toString();
	}

	/**
	 * 读取word文件内容。
	 */
	public String getWordValue(String fileName) throws Exception {
		StringBuffer buffer = new StringBuffer();
		String dir = DefaultProperty.getProperty("WEB_DOCPATH");
		StringBuffer savePath = new StringBuffer(getEnvironment().getRealPath(
				dir));
		File file = null;
		WordExtractor extractor = null;
		file = new File(savePath + fileName);
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			HWPFDocument document = new HWPFDocument(fis);
			extractor = new WordExtractor(document);
			String[] fileData = extractor.getParagraphText();
			for (int i = 0; i < fileData.length; i++) {
				if (fileData[i] != null)
					buffer.append(fileData[i]);
			}
			return buffer.toString();
		}
		return "";
	}

	public Environment getEnvironment() {
		Environment evt = (Environment) ActionContext.getContext()
				.getApplication().get(Environment.class.getName());
		return evt;
	}

	/**
	 * 2.6新增
	 * 
	 * @param runner
	 * @return
	 * @throws Exception
	 */
	public boolean runSignatureScript(IRunner runner) throws Exception {
		return runBooleanScript(runner, "signatureScript", StringUtil.dencodeHTML(getSignatureScript()));
	}

	/**
	 * 空实现
	 */
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		return null;
	}

	/**
	 * 获取打开视图框的类型(1:弹出窗口,3:弹出层)
	 * 
	 * @return 打开视图框的类型 (1:弹出窗口,3:弹出层)
	 */
	public String getOpenType() {
		return openType;
	}

	/**
	 * 设置打开视图框的类型
	 * 
	 * @param openType
	 *            打开视图框的类型
	 */
	public void setOpenType(String openType) {
		this.openType = openType;
	}

	/**
	 * 空实现
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
			throws Exception {
		return "<span style='color:#f00'>{*[cn.myapps.core.dynaform.view.GridNotSupportWordField]*}</span>";
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
		
		if (doc != null) {
			xmlText.append("<").append(MobileConstant2.TAG_WORDFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.HIDDEN || (getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
				}
			}
			String webDocPath = DefaultProperty.getProperty("WEB_DOCPATH");
			if(!StringUtil.isBlank(webDocPath)){
				xmlText.append(" ").append(MobileConstant2.ATT_WEBDOCPATH).append("='").append(webDocPath).append("'");
			}
			xmlText.append(">");
			
			
			String fileName = getValue(doc, runner, webUser);
			if(!StringUtil.isBlank(fileName)){
				xmlText.append(fileName);
			}else{
				xmlText.append(String.valueOf(Sequence.getSequenceTimes()));
			}
			xmlText.append("</").append(MobileConstant2.TAG_WORDFIELD).append(">");
		}
		return xmlText.toString();
	}

}
