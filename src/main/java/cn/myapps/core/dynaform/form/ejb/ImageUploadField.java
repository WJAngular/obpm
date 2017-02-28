// Source file:
// C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\InputField.java

package cn.myapps.core.dynaform.form.ejb;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

/**
 * 
 * 上传图片的组件,可支持所有格式的图片文件上传
 */
public class ImageUploadField extends AbstractUploadField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2295552984683189284L;

	/**
	 * @roseuid 41ECB66E012A
	 */
	//private static String cssClass = "imageupload-cmd";
    /**
	    * 图片的高
	    */
	protected String imgh;
		/**
		 * 图片的宽
		 */
	protected String imgw;
	
	public ImageUploadField() {

	}

	public ValidateMessage validate(IRunner runner, Document doc) throws Exception {
		return null;
	}

	/**
	 * @roseuid 41ECB66E0152
	 */
	public void store() {

	}

	/**
	 * 添加Script
	 * 
	 * @param attachmentName
	 *            附件名
	 * @param uploadList
	 *            上传列表
	 * 
	 * @return 字符串内容为script形式的字符串
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
	 * @return java.lang.String
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


	/**
	 * Form模版的图片上传组件(ImageUploadField)内容结合Document中的ITEM存放的值,返回字符串为重定义后的以PDF的形式输出
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
		imgh = !StringUtil.isBlank(this.getImgh()) ? this.getImgh().trim() : "100";
		imgw = !StringUtil.isBlank(this.getImgw()) ? this.getImgw() : "100";
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			String value = "";
			String url = "";
			if (item != null && item.getValue() != null) {
				value = (String) item.getValue();
				if(value.startsWith("[")){
					Collection<Object> images = JsonUtil.toCollection(value);
					for(Iterator<?> it = images.iterator();it.hasNext();){
						@SuppressWarnings("unchecked")
						Map<String, String> image = (Map<String, String>)it.next();
						url = image.get("path");
						break;
					}
				}
			}
			
			if(StringUtil.isBlank(value)) return "";

			Environment env = Environment.getInstance();
			String filePath = env.getRealPath(url);
			File file = new File(filePath);
			if(file.exists()){
				int index = url.indexOf("/");
				if (index != -1) {
					url = doc.get_params().getContextPath() + url;
				}
				html.append("<SPAN >");
				html.append("<img border='0' width='"+imgw+"' height='"+imgh+"' src='" +webUser.getServerAddr() +url + "' />");
				html.append("</SPAN>");
			}else{
				html.append("&nbsp;");
			}
			//html.append(printHiddenElement(doc));
		}

		return html.toString();
	}
	
	/**
	 * 打印
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception{
		StringBuffer html = new StringBuffer();
		imgh = !StringUtil.isBlank(this.getImgh()) ? this.getImgh().trim() : "100";
		imgw = !StringUtil.isBlank(this.getImgw()) ? this.getImgw() : "100";
		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}

			Item item = doc.findItem(this.getName());
			String value = "";
			String url = "";
			if (item != null && item.getValue() != null) {
				value = (String) item.getValue();
				if(value.startsWith("[")){
					Collection<Object> images = JsonUtil.toCollection(value);
					for(Iterator<?> it = images.iterator();it.hasNext();){
						@SuppressWarnings("unchecked")
						Map<String, String> image = (Map<String, String>)it.next();
						url = image.get("path");
						break;
					}
				}
			}
			
			if(StringUtil.isBlank(url)) return "";

			Environment env = Environment.getInstance();
			String filePath = env.getRealPath(url);
			File file = new File(filePath);
			if(file.exists()){
				int index = url.indexOf("/");
				if (index != -1) {
					url = doc.get_params().getContextPath() + url;
				}
				html.append("<SPAN>");
				html.append("<img border='0' width='"+imgw+"' height='"+imgh+"' src='" +url + "' />");
				html.append("</SPAN>");
			}else{
				html.append("&nbsp;");
			}
		}

		return html.toString();
	};
	
	
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
			xmlText.append("<").append(MobileConstant.TAG_IMAGEFIELD);
			xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(src).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_WIDTH + "='" + getImgw() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_HIDDEN+ "='" + getImgh() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_FC+ "='" + path + "'");
			xmlText.append(" ").append(MobileConstant.ATT_FP+ "='" + fileSaveMode + "'");
			xmlText.append(" ").append(MobileConstant.ATT_SIZE).append("='").append(maxsize).append("'");
			
			xmlText.append(" ").append(MobileConstant.ATT_ID + "='" + getId() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_NAME + "='" + getName() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");
			
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
			xmlText.append(getDiscript());
			xmlText.append("</").append(MobileConstant.TAG_IMAGEFIELD + ">");
		}
		return xmlText.toString();
	}

	protected String getRefreshUploadListFunction(String fieldId, String uploadList,boolean readonly,boolean refresh,String applicationid,String opentype) {
		String height = !StringUtil.isBlank(this.getImgh()) ? this.getImgh().trim() : "100";
		String width = !StringUtil.isBlank(this.getImgw()) ? this.getImgw() : "100";
		
		return "refreshImgList(document.getElementById('" + fieldId + "').value, '" + uploadList + "', '"+Integer.parseInt(height)+"','"+Integer.parseInt(width)+"',"+readonly+","+refresh+",'"+applicationid+"','"+opentype+"');";
		
	}

	public String getLimitType() {
		return "image";
	}
/**
	 * 获取图片大小的高
	 * 
	 * @return 高的单位（px）
	 */
	
	public String getImgh() {
		return imgh;
	}

	/**
	 * 设置图片大小的高
	 * 
	 * @param imgh
	 */
	public void setImgh(String imgh) {
		this.imgh = imgh;
	}
	/**
	 * 获取图片大小的宽
	 * 
	 * @return 宽的单位（px）
	 */
	public String getImgw() {
		return imgw;
	}

	/**
	 * 设置图片大小的宽
	 * 
	 * @param imgw
	 */
	

	public void setImgw(String imgw) {
		this.imgw = imgw;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			
			String src = doc.getItemValueAsString(getName());
			String fileName = "";
			//新的图片上传控件的值为json格式,需要解析 2013-12-19
			if(src.startsWith("[")){
				Collection<Object> images = JsonUtil.toCollection(src);
				for(Iterator<?> it = images.iterator();it.hasNext();){
					@SuppressWarnings("unchecked")
					Map<String, String> image = (Map<String, String>)it.next();
					src = image.get("path");
					break;
				}
			}else{
				if(src.lastIndexOf("/") >0){
					fileName = src.substring(src.lastIndexOf("/")+1,src.length());
				}
			}
			
			
			String path;
			String fileSaveMode = getFilePattern() != null ? getFilePattern() : "00";
			int maxsize = !StringUtil.isBlank(getLimitsize()) ? Integer.valueOf(getLimitsize()).intValue() * 1024 : 10485760;

			if (!StringUtil.isBlank(fileSaveMode) && fileSaveMode.equals("01")) {
				path = getFileCatalog();
			} else {
				path = "ITEM_PATH";
			}
			xmlText.append("<").append(MobileConstant2.TAG_IMAGEFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_SRC).append("='").append(src).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_FILECATALOG).append("='").append(path).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_FILEPATTERN).append("='").append(fileSaveMode).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_SIZE).append("='").append(maxsize).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_IMAGENAME).append("='").append(fileName).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_ID).append("='").append(getId()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");
			
			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
			}
			if (displayType == PermissionType.HIDDEN || 
					(getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
				}
			}
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
			}
			xmlText.append(">");
			xmlText.append("</").append(MobileConstant2.TAG_IMAGEFIELD).append(">");
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
	
}