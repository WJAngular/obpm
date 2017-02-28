package cn.myapps.core.dynaform.form.ejb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.upload.ejb.UploadProcess;
import cn.myapps.core.upload.ejb.UploadVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public abstract class AbstractUploadField extends FormField implements
		ValueStoreField {

	private static final long serialVersionUID = -9107366526757917068L;

	protected String limitsize;

	protected String openType;

	protected String limitType = "";

	protected String fileCatalog;

	protected String filePattern;

	protected String limitNumber;// 一次上传限制上传数量

	protected String fileType;// 上传文件类型('00'表示所有,'01'表示自定义)

	protected String customizeType;// 自定义的上传文件类型

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getCustomizeType() {
		return customizeType;
	}

	public void setCustomizeType(String customizeType) {
		this.customizeType = customizeType;
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

	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		return null;
	}

	public String toTemplate() {
		return null;
	}

	/**
	 * 根据Form模版的组件内容结合Document中的ITEM存放的值,输出重定义后的html文本以网格显示
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @param columnOptionsCache           
	 *             缓存控件选项(选项设计模式构建的Options)的Map
	 *             key: fieldId
	 *             value: Options
	 * @return 重定义后的html文本
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
			throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				html.append("<span moduleType='uploadFile'");
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
		int displayType = getDisplayType(doc, runner, webUser, permissionType);

		if (displayType == PermissionType.HIDDEN) {
			html.append("<input type='hidden' name='")
				.append(name).append("' value='")
				.append(doc.getItemValueAsString(name))
				.append("' fieldType='")
				.append(this.fieldtype)
				.append("' id='")
				.append(this.id)
				.append("'/>")
				.append(this.getHiddenValue());
			return html.toString();
		} else {
			if (doc != null) {
				html.append("<span moduleType='uploadFile'");
				html.append(toAttr(doc, runner, webUser, displayType));
			}
		}
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
	private String toAttr(Document doc, IRunner runner, WebUser webUser, int displayType) throws Exception {
		StringBuffer html = new StringBuffer();
		Item item = doc.findItem(this.getName());
		boolean isnew = true;
		if (item != null && item.getValue() != null) {
			isnew = false;
		}
		String path;
		String fileSaveMode = getFilePattern() != null ? getFilePattern()
				: "00";
		if (!StringUtil.isBlank(fileSaveMode)
				&& fileSaveMode.equals("01")) {
			path = getFileCatalog();
		} else {
			path = "ITEM_PATH";
		}

		String uploadList = "uploadList";

		uploadList = uploadList + "_" + getFieldId(doc);
		html.append(" id='" + getFieldId(doc) + "'");
		html.append(" name='" + getName() + "'");
		html.append(" tagName='" + getTagName() + "'");
		if (!isnew) {
			String val = (String) item.getValue();
			//文件上传旧数据兼容,如果不是json格式,转换为json格式
			if(!StringUtil.isBlank(val) && !val.startsWith("[")){
				StringBuffer jsonVal = new StringBuffer();
				jsonVal.append("[");
				String[] fileNames = val.split(";");
				for(int i=0; i<fileNames.length; i++){
					jsonVal.append("{\"name\":\"").append(fileNames[i].substring(fileNames[i].lastIndexOf("/") + 1)).append("\",");
					jsonVal.append("\"path\":\"").append(fileNames[i]).append("\"},");
				}
				if(jsonVal.length() > 1){
					jsonVal.setLength(jsonVal.length() - 1);
				}
				jsonVal.append("]");
				val = jsonVal.toString();
			}
			html.append(" value='" + val + "'");
		}
		if (displayType == PermissionType.READONLY
				|| displayType == PermissionType.DISABLED) {
			html.append(" disabled='disabled'");
		}
		int maxsize = !StringUtil.isBlank(getLimitsize()) ? Integer
				.valueOf(getLimitsize()).intValue() : 10240;
		html.append(" maxsize='" + maxsize + "'");
		html.append(" filelabel = '{*[File]*}'");
		html.append(" uploadlabel ='{*[Upload]*}'");
		html.append(" deleteLabel ='{*[Delete]*}'");
//		表单描述字段
		html.append(" discript ='" + getDiscript() + "'");
		html.append(" path='" + path + "'");
		html.append(" limitType ='" + getLimitType() + "'");
		html.append(" uploadList ='" + uploadList + "'");

		html.append(" refreshOnChanged='" + super.refreshOnChanged
						+ "'");
		html.append(" fileSaveMode='" + fileSaveMode + "'");
		html.append(" application ='" + doc.getApplicationid() + "'");
		html.append(" limitNumber ='" + limitNumber + "'");
		html.append(" fileType='" + fileType + "'");
		html.append(" fieldtype='" + getTagName() + "'");
		html.append(" customizeType='" + customizeType + "'");
		html.append(" opentype='" + getOpenType() + "'");
		html.append(" imgHeight='" + getImgh() + "'");
		html.append(" imgWidth='" + getImgw() + "'");

		html.append(">");
		html.append(getText(doc, runner, webUser));
		html.append("</span>");
		
		return html.toString();
	}

	public abstract String getImgh();
	
	public abstract String getImgw();

	public abstract String toPrintHtmlTxt(Document doc, IRunner runner,
			WebUser webUser) throws Exception;

	protected abstract String getRefreshUploadListFunction(String fieldId,
			String uploadList, boolean readonly, boolean refresh,
			String applicationid, String opentype);

	protected abstract String addScript(String id, String uploadList,
			boolean readonly, boolean refresh, String applicationid,
			String opentype);

	/**
	 * 获取上传文件的大小
	 * 
	 * @return 上传文件的大小
	 */
	public String getLimitsize() {
		return limitsize;
	}

	/**
	 * 设置上传文件的大小
	 * 
	 * @param limitsize
	 *            上传文件的大小
	 */
	public void setLimitsize(String limitsize) {
		this.limitsize = limitsize;
	}

	/**
	 * 获取上传文件大小的单位(b,kb,m)
	 * 
	 * @return 上传文件大小的单位
	 */
	public String getLimitType() {
		return limitType;
	}

	/**
	 * 设置上传文件大小的单位
	 * 
	 * @param limitType
	 */

	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public String getLimitNumber() {
		return limitNumber;
	}

	public void setLimitNumber(String limitNumber) {
		this.limitNumber = limitNumber;
	}
}
