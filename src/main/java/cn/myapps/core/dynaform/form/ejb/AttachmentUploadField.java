// Source file:
// C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\InputField.java

package cn.myapps.core.dynaform.form.ejb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

public class AttachmentUploadField extends AbstractUploadField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3744258226928041132L;

	/**
	 * @roseuid 41ECB66E012A
	 */

	public AttachmentUploadField() {

	}


	public void store() {

	}
	
	

	/**
	 * 
	 * Form模版的附件上传组件内容结合Document,返回重定义后的打印html.
	 * 
	 * @param doc
	 *            Document对象
	 * @param runner
	 *            动态执行脚本器
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * 
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return Form模版的附件上传组件内容结合Document为重定义后的打印html
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
			String value = "";
			html.append("<SPAN>");
			if (item != null && item.getValue() != null && !item.getValue().equals("")) {
				value = (String) item.getValue();
				if (value.startsWith("[")) {
					JSONArray jsonArray = new JSONArray(value);
					for(int i=0;i<jsonArray.length();i++){
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						html.append(jsonObj.get("name")).append("<br />");
					}
					
					html.delete(html.lastIndexOf("<br />"), html.lastIndexOf(">")+1);
				} else {
					String[] valueArry = value.split(";");
					for(int i=0;i<valueArry.length;i++){
						int index = valueArry[i].lastIndexOf("/");
						html.append(valueArry[i].substring(index + 1, valueArry[i].length())).append("<br />");
					}
					html.delete(html.lastIndexOf("<br />"), html.lastIndexOf(">")+1);
				}
			}else{
				html.append("&nbsp;");
			}
			html.append("</SPAN>");

		}

		return html.toString();
	}
	
	/**
	 * Form模版的文件上传组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的以PDF的形式输出
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 * @return PDF的格式的HTML的文本
	 */
	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			Item item = doc.findItem(this.getName());
			String value = "";
			html.append("<SPAN >");
			if (item != null && item.getValue() != null && !item.getValue().equals("")) {
				value = (String) item.getValue();
				JSONArray jsonArray = new JSONArray(value);
				for(int i=0;i<jsonArray.length();i++){
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					html.append(jsonObj.get("name")).append(";&nbsp;");
				}

				html.deleteCharAt(html.lastIndexOf(";&nbsp;"));
			}else{
				html.append("&nbsp;");
			}
			html.append("</SPAN>");

		}

		return html.toString();
	}

	/**
	 * 添加页面上传按钮的Script
	 * 
	 * @param attachmentName
	 *            附件名
	 * @param uploadList
	 *            上传列表
	 * @return 字符串内容为script
	 */
	protected String addScript(String fieldId, String uploadList,boolean readonly,boolean refresh,String applicationid,String opentype) {
		StringBuffer script = new StringBuffer();
		script.append("<script language='JavaScript'>");
		script.append(getRefreshUploadListFunction(fieldId, uploadList,readonly,refresh,applicationid,opentype));
		script.append("</script>");

		return script.toString();
	}

	/**
	 * 返回模板描述
	 * 
	 * @return 描述
	 * @roseuid 41E7917A033F
	 */
	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<span'");
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
		template.append("/>");
		return template.toString();
	}


	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {

		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			
			String src = doc.getItemValueAsString(getName());
			src = src == null ? "" : src.trim();
			String path;
			String fileSaveMode = getFilePattern() != null ? getFilePattern() : "00";
			int maxsize = !StringUtil.isBlank(getLimitsize()) ? Integer.valueOf(getLimitsize()).intValue() * 1024 : 10485760;

			if (!StringUtil.isBlank(fileSaveMode) && fileSaveMode.equals("01")) {
				path = getFileCatalog();
			} else {
				path = "ITEM_PATH";
			}
			xmlText.append("<").append(MobileConstant.TAG_ATTACHMENTUPLOADFIELD);
			//xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(src).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_FC+ "='" + path + "'");
			xmlText.append(" ").append(MobileConstant.ATT_FP+ "='" + fileSaveMode + "'");
			xmlText.append(" ").append(MobileConstant.ATT_SIZE).append("='").append(maxsize).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_DISCRIPT).append("='").append(getDiscript()).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_ID + "='" + getId() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_NAME + "='" + getName() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");
			if("01".equals(getFileType() )){
				xmlText.append(" ").append(MobileConstant.ATT_CUSTOMIZETYPE).append("='").append(getCustomizeType()).append("'");
			}
			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant.ATT_READONLY + "='true' ");
			}
			if (displayType == PermissionType.HIDDEN || 
					(getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant.ATT_REFRESH).append(" ='true' ");
			}
			xmlText.append(">");
			if(!StringUtil.isBlank(src)){
					String key = "";
					String value = "";
					
					if(src.startsWith("[")){
						Collection<Object> files = JsonUtil.toCollection(src);
						for(Iterator<Object> it = files.iterator(); it.hasNext();){
							Map<String, String> file = (Map<String, String>)it.next();
							key = file.get("name");
							value = file.get("path");
							if(value.length() >0){
								int webIndex = value.lastIndexOf("/");
								int urlIndex = value.lastIndexOf("_/uploads");
	
								if(urlIndex>=0){
									value = value.substring(urlIndex+1);
								}
								xmlText.append("<").append(MobileConstant.TAG_OPTION).append("");
								xmlText.append(" ").append(MobileConstant.ATT_VALUE).append("='");
								xmlText.append(HtmlEncoder.encode(value));
								xmlText.append("'");
								xmlText.append(">");
								if(!StringUtil.isBlank(key)){
									xmlText.append(key);
								}
								xmlText.append("</").append(MobileConstant.TAG_OPTION).append(">");
						}
//							value = value.substring(webIndex + 1,value.length());
						}
					}else {
						String[] srcs = src.split(";");
						for(int i=0;i<srcs.length;i++){
							value= srcs[i];
							key = value.substring(value.lastIndexOf("/")+1);
							xmlText.append("<").append(MobileConstant.TAG_OPTION).append("");
							xmlText.append(" ").append(MobileConstant.ATT_VALUE).append("='");
							xmlText.append(HtmlEncoder.encode(value));
							xmlText.append("'");
							xmlText.append(">");
							if(!StringUtil.isBlank(key)){
								xmlText.append(key);
							}
							xmlText.append("</").append(MobileConstant.TAG_OPTION).append(">");
						}
					
				}
			}
			xmlText.append("</").append(MobileConstant.TAG_ATTACHMENTUPLOADFIELD + ">");
		}
		return xmlText.toString();
	}

	/**
	 * 获取上传的附件名,在页面的显示附件名
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return getText(doc, runner, webUser, null);
	}
	/**
	 * 获取上传的附件名,在页面的显示附件名
	 */
	@SuppressWarnings("unchecked")
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		String fileFullName = doc.getItemValueAsString(getName());
		//新的文件上传控件的值为json格式,需要解析 2013-12-19
		if(fileFullName.startsWith("[")){
			Collection<Object> files = JsonUtil.toCollection(StringUtil.dencodeHTML(fileFullName));
			String filename = "";
			for(Iterator<Object> it = files.iterator(); it.hasNext();){
				Map<String, String> file = (Map<String, String>)it.next();
				if("".equals(filename)){
					filename = file.get("name");
				}else{
					filename += ";" + file.get("name");
				}
			}
			return filename;
		}else {
			int index = fileFullName.split(";")[0].lastIndexOf("/");
			if (index != -1) {
				String fileName = fileFullName.split(";")[0].substring(index + 1, fileFullName.split(";")[0].length());
				if(fileFullName.split(";").length>1){
					return fileName+"...";
				}else{
					return fileName;
				}
			}
			return super.getText(doc, runner, webUser);
		}
	}
	
	/**
	 * 获取上传的所有附件名(分号分隔)
	 */
	public String getText2(Document doc, IRunner runner, WebUser webUser) throws Exception {
		String fileFullName = doc.getItemValueAsString(getName());
		int index = fileFullName.split(";")[0].lastIndexOf("/");
		if (index != -1) {
			String[] fileFullNames = fileFullName.split(";");
			String fileName = "";
			for (int i = 0; i < fileFullNames.length; i++) {
				String fullName = fileFullNames[i];
				fileName += fullName.substring(fullName.lastIndexOf("/") + 1);
				if(fileFullNames.length - 1 > i)
					fileName += ";";
			}
			return fileName;
		}
		return super.getText(doc, runner, webUser);
	}

	protected String getRefreshUploadListFunction(String fieldId, String uploadList,boolean readonly,boolean refresh,String applicationid,String opentype) {
		return "refreshUploadList(document.getElementById('" + fieldId + "').value, '" + uploadList + "',"+readonly+","+refresh+",'"+applicationid+"','"+opentype+"')";
	}

	@Override
	public String getImgh() {
		return null;
	}

	@Override
	public String getImgw() {
		return null;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			
			String src = doc.getItemValueAsString(getName());
			src = src == null ? "" : src.trim();
			String path;
			String fileSaveMode = getFilePattern() != null ? getFilePattern() : "00";
			int maxsize = !StringUtil.isBlank(getLimitsize()) ? Integer.valueOf(getLimitsize()).intValue() * 1024 : 10485760;

			if (!StringUtil.isBlank(fileSaveMode) && fileSaveMode.equals("01")) {
				path = getFileCatalog();
			} else {
				path = "ITEM_PATH";
			}
			xmlText.append("<").append(MobileConstant2.TAG_UPLOADFIELD).append(" ");
			//xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(src).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_FILECATALOG).append("='").append(path).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_FILEPATTERN).append("='").append(fileSaveMode).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_SIZE).append("='").append(maxsize).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_DISCRIPT).append("='").append(getDiscript()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_ID).append("='").append(getId()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_LIMITNUMBER).append("='").append(getLimitNumber()).append("'");
			if("01".equals(getFileType() )){
				xmlText.append(" ").append(MobileConstant2.ATT_CUSTOMIZETYPE).append("='").append(getCustomizeType()).append("'");
			}
			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
			}
			if (displayType == PermissionType.HIDDEN || 
					(getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'").append(" ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
				}
			}
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
			}
			xmlText.append(">");
			if(!StringUtil.isBlank(src)){
				String[] srcs = src.split(";");
				for(int i=0;i<srcs.length;i++){
					String key = "";
					String value = "";
					
					if(src.startsWith("[")){
						Collection<Object> files = JsonUtil.toCollection(src);
						for(Iterator<Object> it = files.iterator(); it.hasNext();){
							xmlText.append("<").append(MobileConstant2.TAG_OPTION);
							Map<String, String> file = (Map<String, String>)it.next();
							key = file.get("name");
							value = file.get("path");
							xmlText.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(HtmlEncoder.encode(value)).append("'");
							xmlText.append(">");
							if(!StringUtil.isBlank(key)){
								xmlText.append(key);
							}
							xmlText.append("</").append(MobileConstant2.TAG_OPTION).append(">");
						}
//						if(files.size() > 1){
//							key += "...";
//						}
						if(value.length() >0){
							int webIndex = value.lastIndexOf("/");
							int urlIndex = value.lastIndexOf("_/uploads");

							if(urlIndex>=0){
								value = value.substring(urlIndex+1);
							}
//							value = value.substring(webIndex + 1,value.length());
						}
					}else {
						value= srcs[i];
						key = value.substring(value.lastIndexOf("/")+1);
						xmlText.append("<").append(MobileConstant2.TAG_OPTION);
						xmlText.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(HtmlEncoder.encode(value)).append("'");
						xmlText.append(">");
						if(!StringUtil.isBlank(key)){
							xmlText.append(key);
						}
						xmlText.append("</").append(MobileConstant2.TAG_OPTION).append(">");
					}
				}
			}
			xmlText.append("</").append("UPLOADFIELD").append(">");
		}
		return xmlText.toString();
	}
}
