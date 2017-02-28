package cn.myapps.core.dynaform.form.ejb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.upload.ejb.UploadProcess;
import cn.myapps.core.upload.ejb.UploadVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class AttachmentUploadToDataBaseField extends FormField implements ValueStoreField{

	private static final long serialVersionUID = -482924526686699415L;
	protected String limitsize;//限制大小
	protected String limitNumber;//一次上传限制上传数量
	protected String fileType;//上传文件类型('00'表示所有,'01'表示自定义)
	protected String customizeType;//自定义的上传文件类型
	protected Environment env = Environment.getInstance();
	
	@Override
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {

		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			
			String src = doc.getItemValueAsString(getName());
			src = src == null ? "" : src.trim();
			int maxsize = !StringUtil.isBlank(getLimitsize()) ? Integer.valueOf(getLimitsize()).intValue() * 1024 : 10485760;

			xmlText.append("<").append(MobileConstant.TAG_ATTACHMENTUPLOADTODATABASEFIELD);
			//xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(src).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_SIZE).append("='").append(maxsize).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_DISCRIPT).append("='").append(getDiscript()).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_ID + "='" + getFieldId(doc) + "'");
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
				String[] srcs = src.split(";");
				for(int i=0;i<srcs.length;i++){
					String str = srcs[i];
					xmlText.append("<").append(MobileConstant.TAG_OPTION).append("");
					xmlText.append(" ").append(MobileConstant.ATT_VALUE).append("='");
					xmlText.append(HtmlEncoder.encode(str.split("_")[0]));
					xmlText.append("'");
					xmlText.append(">");
					if(!StringUtil.isBlank(str)){
						xmlText.append(HtmlEncoder.encode(str.split("_")[1]));
					}
					xmlText.append("</").append(MobileConstant.TAG_OPTION).append(">");
				}
			}
			xmlText.append("</").append(MobileConstant.TAG_ATTACHMENTUPLOADTODATABASEFIELD + ">");
		}
		return xmlText.toString();
	}

	/**
	 * 返回模板描述
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
		template.append(">");
		return template.toString();
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
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser,Map<String,Options> columnOptionsCache)
			throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				html.append(" <input type='hidden' moduleType='uploadToDatabase' ");
				html.append(" subGridView='true'");
				html.append(toAttr(doc, runner, webUser, displayType));
			}
		}
		return html.toString();
	}

	/**
	 * 输出HTML
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				html.append(" <input type='hidden' moduleType='uploadToDatabase'");
				html.append(toAttr(doc, runner, webUser, displayType));
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
	private String toAttr(Document doc, IRunner runner, WebUser webUser, int displayType) throws Exception {
		StringBuffer html = new StringBuffer();
		Item item = null;
		item = doc.findItem(this.getName());
		boolean isnew = true;
		if (item != null && item.getValue() != null) {
			isnew = false;
		}
		//从t_upload表中查找出field的文件
		StringBuffer sb = new StringBuffer();
		UploadProcess uploadProcess = (UploadProcess)ProcessFactory.createRuntimeProcess(UploadProcess.class, doc.getApplicationid());
		Collection<UploadVO> datas = uploadProcess.findByColumnName("FIELDID",getFieldId(doc));
		if(datas.size()>0){
			for (Iterator<UploadVO> ite = datas.iterator(); ite.hasNext();) {
				UploadVO uploadVO = (UploadVO)ite.next();
				sb.append(uploadVO.getId()+"_"+uploadVO.getName());
				sb.append(";");
			}
			if(sb.lastIndexOf(";")!=-1){
				sb.deleteCharAt(sb.lastIndexOf(";"));
			}
		}
		String uploadList = "uploadFileToDataBaseList";
		uploadList = uploadList + "_" + getFieldId(doc);
		int maxsize = !StringUtil.isBlank(getLimitsize()) ? Integer.valueOf(getLimitsize()).intValue() : 10240;
		html.append(" id='" + getFieldId(doc) + "'");
		html.append(" applicationid='"+doc.getApplicationid()+"'");
		html.append(" uploadList='" + uploadList + "'");
		html.append(" limitNumber='" + limitNumber + "'");
		html.append(" fileType='" + fileType + "'");
		html.append(" text='" + getText(doc, runner, webUser) + "'");
		html.append(" customizeType='" + customizeType + "'");
		html.append(" maxsize='" + maxsize + "'");
		html.append(" refreshOnChanged='"+ super.refreshOnChanged + "'");
		html.append(" name='" + getName() + "'");
		html.append(" displayType='"+ displayType +"'");
		html.append(" fieldType='" + getTagName() + "'");
		//表单描述字段
//		html.append(" discript ='" + getDiscript() + "'");
		html.append(" uploadLabel='{*[Upload]*}'");
		html.append(" deleteLabel='{*[Delete]*}'");
		if (displayType == PermissionType.MODIFY || displayType == PermissionType.READONLY || displayType == PermissionType.DISABLED) {
			html.append(" modifyReadonly='true'");
			if(displayType == PermissionType.READONLY || displayType == PermissionType.DISABLED){
				html.append(" disabled='true'");
			}
		}
		if (!isnew) {
			html.append(" value='" + sb.toString() + "'");
		}

		html.append("/>");
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
	protected String addScript(String fieldId, String uploadList,boolean readonly,boolean refresh,String applicationid) {
		StringBuffer script = new StringBuffer();
		script.append("<script language='JavaScript'>");
		script.append(getRefreshUploadListFunction(fieldId, uploadList,readonly,refresh,applicationid));
		script.append("</script>");

		return script.toString();
	}
	
	protected String getRefreshUploadListFunction(String fieldId, String uploadList,boolean readonly,boolean refresh,String applicationid) {
		return "refreshAttachmentUploadToDataBaseList(document.getElementById('" + fieldId + "').value, '" + uploadList + "',"+readonly+","+refresh+",'"+applicationid+"')";
	}

	/**
	 * 打印
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}else{
				//从t_upload表中查找出field的文件
				sb.append(this.getFileFullName(doc));
			}
		}
		return sb.toString();
	}
	
	/**
	 * pdf打印
	 */
	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (doc != null) {
				//从t_upload表中查找出field的文件
				sb.append(this.getFileFullName(doc));
		}
		return sb.toString();
	}
	
	private String getFileFullName(Document doc)throws Exception {
		//从t_upload表中查找出field的文件
		StringBuffer sb = new StringBuffer();
		UploadProcess uploadProcess = (UploadProcess)ProcessFactory.createRuntimeProcess(UploadProcess.class,doc.getApplicationid());
		Collection<UploadVO> datas = uploadProcess.findByColumnName("FIELDID",getFieldId(doc));
		if(datas.size()>0){
			for (Iterator<UploadVO> ite = datas.iterator(); ite.hasNext();) {
				UploadVO uploadVO = (UploadVO)ite.next();
				sb.append(uploadVO.getName());
				sb.append(";");
			}
			if(sb.lastIndexOf(";")!=-1){
				sb.deleteCharAt(sb.lastIndexOf(";"));
			}
		}
		return sb.toString();
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
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		//从t_upload表中查找出field的文件
		StringBuffer sb = new StringBuffer();
		UploadProcess uploadProcess = (UploadProcess)ProcessFactory.createRuntimeProcess(UploadProcess.class,doc.getApplicationid());
		Collection<UploadVO> datas = uploadProcess.findByColumnName("FIELDID",getFieldId(doc));
		if(datas.size()>0){
			for (Iterator<UploadVO> ite = datas.iterator(); ite.hasNext();) {
				UploadVO uploadVO = (UploadVO)ite.next();
				sb.append(uploadVO.getId()+"_"+uploadVO.getName());
				sb.append(";");
			}
			if(sb.lastIndexOf(";")!=-1){
				sb.deleteCharAt(sb.lastIndexOf(";"));
			}
		}
		String fileFullName = sb.toString();
		int index = fileFullName.split(";")[0].indexOf("_");
		if (index != -1) {
			String fileName = fileFullName.split(";")[0].substring(index + 1, fileFullName.split(";")[0].length());
			if(datas.size()>1){
				return fileName+"...";
			}else{
				return fileName;
			}
		}
		if (!StringUtil.isBlank(doc.getParentid())) {
			int displayType = getDisplayType(doc, runner, webUser);
			if (displayType == PermissionType.HIDDEN) {
				return this.getHiddenValue();
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取上传的所有附件名(分号分隔)
	 */
	public String getText2(Document doc, IRunner runner, WebUser webUser) throws Exception {
		String fileFullName = this.getFileFullName(doc);
		if (!StringUtil.isBlank(doc.getParentid())) {
			int displayType = getDisplayType(doc, runner, webUser);
			if (displayType == PermissionType.HIDDEN) {
				return this.getHiddenValue();
			}
		}
		return fileFullName;
	}

	public String getLimitsize() {
		return limitsize;
	}

	public void setLimitsize(String limitsize) {
		this.limitsize = limitsize;
	}

	public String getLimitNumber() {
		return limitNumber;
	}

	public void setLimitNumber(String limitNumber) {
		this.limitNumber = limitNumber;
	}
	
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

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
