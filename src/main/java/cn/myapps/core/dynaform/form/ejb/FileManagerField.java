// Source file:
// C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\InputField.java

package cn.myapps.core.dynaform.form.ejb;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.AbstractRunner;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.StringUtil;

/**
 * 
 * 文件管理组件，可以管理指定目录下的所有文件
 */
public class FileManagerField extends FormField implements ValueStoreField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2295552984683189284L;

	/**
	 * @roseuid 41ECB66E012A
	 */
	// private static String cssClass = "imageupload-cmd";
	/**
	 * 图片的高
	 */
	protected String imgh;
	/**
	 * 图片的宽
	 */
	protected String imgw;
	/**
	 * 限制大小
	 */
	protected String limitsize;
	/**
	 * 文件类型
	 */
	protected String limitType;
	/**
	 * 根据模式获得保存路径
	 */
	protected String fileCatalog;
	/**
	 * 根据模式获得保存路径
	 */
	protected String filePattern;

	public static final String DEFAULT = "00";// 默认

	public static final String DEFINITION = "01";// 自定义

	public ValidateMessage validate(IRunner runner, Document doc)
			throws Exception {
		return null;
	}

	/**
	 * @roseuid 41ECB66E0152
	 */
	public void store() {

	}

	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
			throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				html.append("<input moduleType='fileManager' type='hidden'");
				html.append(" subGridView='true'");
				html.append(toAttr(doc, runner, webUser,displayType));
			}
		}
		return html.toString();
	}

	/**
	 * 根据ImageUploadField的显示类型不同,返回的结果字符串不同.
	 * 新建的Document,ImageUploadField的显示类型为默认的MODIFY
	 * 。此时根据Form模版的ImageUploadField内容结合Document,返回的字符串为重定义后的html.
	 * 若根据流程节点设置对应ImageUploadField的显示类型不同,(默认为MODIFY),返回的结果字符串不同.
	 * 1)若节点设置对应ImageUploadField的显示类型为隐藏HIDDEN（值为3），返回 “******”字符串。
	 * 2)若节点设置对应ImageUploadField显示类型为MODIFY
	 * (值为2)时,Form模版的图片上传组件(ImageUploadField)
	 * 内容结合Document中的ITEM存放的值,返回字符串为重定义后的html。
	 * 通过强化HTML标签及语法，表达附件上传组件(ImapgUploadField)的布局、属性、事件、样式、等。 否则返回空字符串。
	 * 
	 * @param doc
	 *            文档对象
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 字符串内容为重定义后的html的图片上传组件标签及语法
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				html.append("<input moduleType='fileManager' type='hidden'");
				html.append(toAttr(doc, runner, webUser,displayType));
			}
		}
		return html.toString();
	}
	/**
	 * 生成属性
	 * 
	 * @param doc
	 * @param item
	 * @param displayType
	 * @return
	 * @throws Exception
	 */
	private String toAttr(Document doc, IRunner runner, WebUser webUser,int displayType) throws Exception {
		StringBuffer html = new StringBuffer();
		Item item = null;
		item = doc.findItem(this.getName());
		boolean isnew = true;
		if (item != null && item.getValue() != null) {
			isnew = false;
		}
		String path;
		String fileSaveMode = getFilePattern() != null ? getFilePattern()
				: DEFAULT;
		if (!StringUtil.isBlank(fileSaveMode)
				&& fileSaveMode.equals(DEFINITION)) {
			path = getFileCatalog();
		} else {
			path = "ITEM_PATH";
		}
		String uploadList = "uploadList";
		uploadList = uploadList + "_" + getFieldId(doc);
		html.append(" id='" + getFieldId(doc) + "'");
		html.append(" name='" + getName() + "'");
		html.append(" fieldType='" + getTagName() + "'");
				html.append(" displayType = '" + displayType + "'");
		html.append(" limitType='" + getLimitType() + "'");
		if (!isnew) {
			html.append(" value='" + item.getValue() + "'");
		}

		if (displayType == PermissionType.READONLY
				|| displayType == PermissionType.DISABLED) {
			html.append(" disabled='disabled'");
		}

		int maxsize = !StringUtil.isBlank(getLimitsize()) ? Integer
				.valueOf(getLimitsize()).intValue() * 1024 : 3145728;

		html.append(" maxsize='" + maxsize + "' ");
		html.append(" path='" + URLEncoder.encode(path, "UTF-8") + "'");
		html.append(" fileSaveMode='" + fileSaveMode + "'");
		html.append(" application='" + doc.getApplicationid() + "'");
		//html.append(" discript='" + getDiscript() + "'");
		html.append(" uploadList='" + uploadList + "'");
		html.append(" refreshOnChanged='" + super.refreshOnChanged + "'");
		html.append(" imgWidth='"+getImgw()+"'");
		html.append(" imgHeight='"+getImgh()+"'");
		html.append(" fileManagerLabel='{*[FileManager]*}'");
		html.append(" fileRemoveLabel='{*[Delete]*}'");
		html.append("/>");
		return html.toString();
	}
	
	/**
	 * 获取图片或文件的URL地址，并输出HTML的文本
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
	 * 获取图片或文件的URL地址，并输出HTML的文本
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
 	 * @param columnOptionsCache
 	 * 			缓存控件选项(选项设计模式构建的Options)的Map
 	 * 			key--fieldId
	 *		    value--Options
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
			throws Exception {
		if (limitType.equals("image")) {
			return imageGetText(doc, runner, webUser);
		} else if (limitType.equals("file")) {
			return fileGetText(doc, runner, webUser);
		} else {
			return null;
		}
	}

	/**
	 * 空实现
	 */
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {

			String src = doc.getItemValueAsString(getName());
			src = src == null ? "" : src.trim();
			String path;
			String fileSaveMode = getFilePattern() != null ? getFilePattern()
					: "00";
			int maxsize = !StringUtil.isBlank(getLimitsize()) ? Integer
					.valueOf(getLimitsize()).intValue() * 1024 : 10485760;

			if (!StringUtil.isBlank(fileSaveMode) && fileSaveMode.equals("01")) {
				path = getFileCatalog();
			} else {
				path = "ITEM_PATH";
			}
			xmlText.append("<").append(MobileConstant.TAG_FILEMANAGERFIELD);
			// xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(src).append("'");
			xmlText.append(" ").append(
					MobileConstant.ATT_FC + "='" + path + "'");
			xmlText.append(" ").append(
					MobileConstant.ATT_FP + "='" + fileSaveMode + "'");
			xmlText.append(" ").append(
					MobileConstant.ATT_LIMITTYPE + "='" + limitType + "'");
			xmlText.append(" ").append(MobileConstant.ATT_SIZE).append("='")
					.append(maxsize).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_DISCRIPT).append("='").append(getDiscript()).append("'");
			xmlText.append(" ").append(
					MobileConstant.ATT_ID + "='" + getId() + "'");
			xmlText.append(" ").append(
					MobileConstant.ATT_NAME + "='" + getName() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_LABEL).append("='")
					.append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType()
							.equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(
						MobileConstant.ATT_READONLY + "='true' ");
			}
			if (displayType == PermissionType.HIDDEN
					|| (getTextType() != null && getTextType()
							.equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant.ATT_HIDDEN).append(
						" ='true' ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant.ATT_REFRESH).append(
						" ='true' ");
			}
			xmlText.append(">");
			if (!StringUtil.isBlank(src)) {
				String[] srcs = src.split(";");
				for (int i = 0; i < srcs.length; i++) {
					String str = srcs[i];
					xmlText.append("<").append(MobileConstant.TAG_OPTION)
							.append("");
					xmlText.append(" ").append(MobileConstant.ATT_VALUE)
							.append("='");
					xmlText.append(HtmlEncoder.encode(str));
					xmlText.append("'");
					xmlText.append(">");
					if (!StringUtil.isBlank(str)) {
						if (str.lastIndexOf("/") != -1)
							xmlText.append(str
									.substring(str.lastIndexOf("/") + 1));
						if (str.lastIndexOf("\\") != -1)
							xmlText.append(str
									.substring(str.lastIndexOf("\\") + 1));
					}
					xmlText.append("</").append(MobileConstant.TAG_OPTION)
							.append(">");
				}
			}
			xmlText.append("</").append(
					MobileConstant.TAG_FILEMANAGERFIELD + ">");
		}
		return xmlText.toString();
	}

	/**
	 * 根据类型输出相应的模板
	 */
	public String toTemplate() {
		if (limitType.equals("image")) {
			return imageToTemplate();
		}
		return null;
	}

	/**
	 * 根据类型输出相应的打印html
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		if (limitType.equals("file")) {
			return fileToPrintHtmlTxt(doc, runner, webUser);
		} else if (limitType.equals("image")) {
			return imageToPrintHtmlTxt(doc, runner, webUser);
		}
		return null;
	}

	/**
	 * 根据类型输出相应的pdf导出html
	 */
	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		if (limitType.equals("file")) {
			return fileToPdfHtmlTxt(doc, runner, webUser);
		} else if (limitType.equals("image")) {
			return imageToPdfHtmlTxt(doc, runner, webUser);
		}
		return null;
	}



	/**
	 * 返回模板描述
	 * 
	 * @return java.lang.String
	 * @roseuid 41E7917A033F
	 */
	public String imageToTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<input type='text'");
		template.append(" className='" + this.getClass().getName() + "'");
		template.append(" id='" + getId() + "'");
		template.append(" name='" + getName() + "'");
		template.append(" formid='" + getFormid() + "'");
		template.append(" discript='" + getDiscript() + "'");
		template.append(" hiddenScript='" + getHiddenScript() + "'");
		template.append(" hiddenPrintScript='" + getHiddenPrintScript() + "'");
		template.append(">");
		return template.toString();
	}

	public void imageRecalculate(IRunner runner, Document doc, WebUser webUser)
			throws Exception {
	}

	public Object imageRunValueScript(IRunner runner, Document doc)
			throws Exception {
		return null;
	}

	/**
	 * 
	 * Form模版的图片上传组件(ImageUploadField)内容结合Document中的ITEM存放的值,返回字符串为重定义后的打印html文本
	 * 
	 * @param doc
	 *            Document
	 * @param runner
	 *            动态脚本执行器
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * 
	 * @see cn.myapps.core.dynaform.form.ejb.FileManagerField#toHtmlTxt(Document,
	 *      AbstractRunner, WebUser, int)
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 字符串内容为重定义后的打印html的图片上传组件标签及语法
	 * @throws Exception
	 */
	public String imageToPrintHtmlTxt(Document doc, IRunner runner,
			WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		imgh = !StringUtil.isBlank(this.getImgh()) ? this.getImgh().trim()
				: "100";
		imgw = !StringUtil.isBlank(this.getImgw()) ? this.getImgw() : "100";
		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}

			Item item = doc.findItem(this.getName());
			String fileFullName = doc.getItemValueAsString(getName());
			String value = "";
			String url = "";
			String webPath = "";

			html.append("<SPAN >");

			if (item != null && item.getValue() != null
					&& !fileFullName.equals("") && !item.getValue().equals("")) {
				value = (String) item.getValue();
				String[] strArry = value.split(";");
				HttpServletRequest request = doc.get_params().getHttpRequest();
				String requestUrl = "http://" + request.getServerName() + ":" +  request.getServerPort() + request.getContextPath();
				for (int i = 0; i < strArry.length; i++) {
					webPath = strArry[i];
					url = requestUrl + webPath;
					Environment env = Environment.getInstance();
					String filePath = env.getRealPath(webPath);
					File file = new File(filePath);
					if (file.exists()) {
						html.append("<img border='0' width='" + imgw
								+ "' height='" + imgh + "' src='" + url
								+ "' />");
					}
				}
			} else {
				html.append("&nbsp;");
			}
			html.append("</SPAN>");
		}

		return html.toString();
	}

	/**
	 * Form模版的图片组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的以PDF的形式输出
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 * @return PDF的格式的HTML的文本
	 */
	public String imageToPdfHtmlTxt(Document doc, IRunner runner,
			WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		imgh = !StringUtil.isBlank(this.getImgh()) ? this.getImgh().trim()
				: "100";
		imgw = !StringUtil.isBlank(this.getImgw()) ? this.getImgw() : "100";
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			String fileFullName = doc.getItemValueAsString(getName());
			String value = "";
			String url = "";
			String webPath = "";

			html.append("<SPAN >");

			if (item != null && item.getValue() != null
					&& !fileFullName.equals("") && !item.getValue().equals("")) {
				value = (String) item.getValue();
				String[] strArry = value.split(";");
				HttpServletRequest request = doc.get_params().getHttpRequest();
				String requestUrl = "http://" + request.getServerName() + ":" +  request.getServerPort() + request.getContextPath();
				for (int i = 0; i < strArry.length; i++) {
					webPath = strArry[i];
					url = requestUrl + webPath;
					Environment env = Environment.getInstance();
					String filePath = env.getRealPath(webPath);
					File file = new File(filePath);
					if (file.exists()) {
						html.append("<img border='0' width='" + imgw
								+ "' height='" + imgh + "' src='" + url
								+ "' />");
					}
				}
			} else {
				html.append("&nbsp;");
			}
			html.append("</SPAN>");
		}

		return html.toString();
	}

	/**
	 * 获取图片上传的URL的地址，并输出HTML的文本
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 */
	public String imageGetText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		String fileFullName = doc.getItemValueAsString(getName());
		imgh = !StringUtil.isBlank(this.getImgh()) ? this.getImgh().trim()
				: "100";
		imgw = !StringUtil.isBlank(this.getImgw()) ? this.getImgw() : "100";
		StringBuffer image = new StringBuffer();
		if (!StringUtil.isBlank(fileFullName)) {
			String uploadList = "uploadList";
			uploadList = uploadList + "_" + getFieldId(doc);
			String viewReadOnly = "";
			if(doc.get_params().getParameterAsString("viewReadType") != null && doc.get_params().getParameterAsBoolean("viewReadType")){
				viewReadOnly = "true";
			}
			image.append("<div moduleType='viewFileManager' " +
					"imgw='" + imgw + "' imgh='" + imgh + "' " +
					"str='" + fileFullName.split(";").length + "' uploadListId='" + uploadList + "' viewReadOnly ='" + viewReadOnly + "'>");
			for (int i = 0; i < fileFullName.split(";").length; i++) {
				String webPath = fileFullName.split(";")[i];
				String fileName = fileFullName.split(";")[i].substring(fileFullName
						.split(";")[i].lastIndexOf("/") + 1, fileFullName
						.split(";")[i].length());
				String url = doc.get_params().getContextPath() + webPath;
				image.append("<a subType='aField' docId='" + doc.getId() + "' docFormid='" + doc.getFormid() + "' " +
							"fileName='" + fileName + "' url='" + url + "' imgw='" + imgw + "' imgh='" + imgh + "'></a>");
			}
			image.append("</div>");
			return image.toString();

			// return fileName;
		}

		return super.getText(doc, runner, webUser);
	}



	// 图片上传区结束

	// 文件上传区开始

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
	public String fileToPrintHtmlTxt(Document doc, IRunner runner,
			WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}

			Item item = doc.findItem(this.getName());
			String value = "";
			html.append("<SPAN >");
			if (item != null && item.getValue() != null
					&& !item.getValue().equals("")) {
				value = (String) item.getValue();
				String[] valueArry = value.split(";");
				for (int i = 0; i < valueArry.length; i++) {
					int index = valueArry[i].lastIndexOf("/");
					html.append(
							valueArry[i].substring(index + 1, valueArry[i]
									.length())).append(";");
				}
				html.deleteCharAt(html.lastIndexOf(";"));
			} else {
				html.append("&nbsp;");
			}
			html.append("</SPAN>");

		}

		return html.toString();
	}

	/**
	 * Form模版的文件组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的以PDF的形式输出
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 * @return PDF的格式的HTML的文本
	 */
	public String fileToPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			Item item = doc.findItem(this.getName());
			String value = "";
			html.append("<SPAN >");
			if (item != null && item.getValue() != null
					&& !item.getValue().equals("")) {
				value = (String) item.getValue();
				String[] valueArry = value.split(";");
				for (int i = 0; i < valueArry.length; i++) {
					int index = valueArry[i].lastIndexOf("/");
					html.append(
							valueArry[i].substring(index + 1, valueArry[i]
									.length())).append(";");
				}
				html.deleteCharAt(html.lastIndexOf(";"));
			} else {
				html.append("&nbsp;");
			}
			html.append("</SPAN>");

		}

		return html.toString();
	}


	/**
	 * 返回模板描述
	 * 
	 * @return 描述
	 * @roseuid 41E7917A033F
	 */
	public String file() {
		StringBuffer template = new StringBuffer();
		template.append("<input type='text'");
		template.append(" className='" + this.getClass().getName() + "'");
		template.append(" id='" + getId() + "'");
		template.append(" name='" + getName() + "'");
		template.append(" formid='" + getFormid() + "'");
		template.append(" discript='" + getDiscript() + "'");
		template.append(" hiddenScript='" + getHiddenScript() + "'");
		template.append(" hiddenPrintScript='" + getHiddenPrintScript() + "'");
		template.append(">");
		return template.toString();
	}

	/**
	 * 获取上传的附件名,在页面的显示附件名
	 */
	public String fileGetText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		String fileFullName = doc.getItemValueAsString(getName());
		if (fileFullName != null) {
			String fileName = fileFullName.split(";")[0].substring(fileFullName
					.split(";")[0].lastIndexOf("/") + 1, fileFullName
					.split(";")[0].length());
			if (fileFullName.split(";").length > 1) {
				return fileName + "...";
			} else {
				return fileName;
			}
		}
		return super.getText(doc, runner, webUser);
	}

	// 文件上传区结束

	public String getImgh() {
		return imgh;
	}

	public void setImgh(String imgh) {
		this.imgh = imgh;
	}

	public String getImgw() {
		return imgw;
	}

	public void setImgw(String imgw) {
		this.imgw = imgw;
	}

	public String getLimitsize() {
		return limitsize;
	}

	public void setLimitsize(String limitsize) {
		this.limitsize = limitsize;
	}

	public String getFileCatalog() {
		return fileCatalog;
	}

	public void setFileCatalog(String fileCatalog) {
		this.fileCatalog = fileCatalog;
	}

	public String getFilePattern() {
		return filePattern;
	}

	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}

	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	public String getLimitType() {
		return limitType;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}