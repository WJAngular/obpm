// Source file:
// C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\InputField.java

package cn.myapps.core.dynaform.form.ejb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.AbstractRunner;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.upload.ejb.UploadProcess;
import cn.myapps.core.upload.ejb.UploadVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 
 * 上传图片的组件,可支持所有格式的图片文件上传
 */
public class ImageUploadToDataBaseField extends FormField implements ValueStoreField{
	private static final long serialVersionUID = 2295552984683189284L;
	//private static String cssClass = "imageupload-cmd";
	protected String imgh;//图片高
	protected String imgw;//图片宽
	protected String limitsize;//限制大小
	protected Environment env = Environment.getInstance();


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
	protected String addScript(String fieldId, String uploadList,boolean readonly,boolean refresh,String applicationid) {
		StringBuffer script = new StringBuffer();
		script.append("<script language='JavaScript'>");
		script.append(getRefreshUploadListFunction(fieldId, uploadList,readonly,refresh,applicationid));
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
	 * @see cn.myapps.core.dynaform.form.ejb.ImageUploadField#toHtmlTxt(Document,
	 *      AbstractRunner, WebUser, int)
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 字符串内容为重定义后的打印html的图片上传组件标签及语法
	 * @throws Exception
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
	throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				html.append(" <input type='hidden' moduleType='ImageUploadToDatabase' ");
				html.append(" subGridView='true'");
				html.append(toAttr(doc, runner, webUser, displayType));
			}
		}
		return html.toString();
	}
	
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
		throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				html.append(" <input type='hidden' moduleType='ImageUploadToDatabase' ");
				html.append(toAttr(doc, runner, webUser, displayType));
			}
		}
		return html.toString();
	}
	private String toAttr(Document doc, IRunner runner, WebUser webUser , int displayType) throws Exception {
		StringBuffer html = new StringBuffer();
		Item item = null;
		item = doc.findItem(this.getName());
		boolean isnew = true;
		if (item != null && item.getValue() != null) {
			isnew = false;
		}
		
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
		
		String uploadList = "uploadToDataBaseList";
		uploadList = uploadList + "_" + getFieldId(doc);
		int maxsize = !StringUtil.isBlank(getLimitsize()) ? Integer.valueOf(getLimitsize()).intValue() : 10240;
		html.append(" id='" + getFieldId(doc) + "'");
		html.append(" applicationid='"+doc.getApplicationid()+"'");
		html.append(" uploadList='" + uploadList + "'");
		html.append(" maxsize='" + maxsize + "'");
		html.append(" refreshOnChanged='"+ super.refreshOnChanged + "'");
		html.append(" name='" + getName() + "'");
		html.append(" fieldType='" + getTagName() + "'");
		//表单描述字段
//		html.append(" discript ='" + getDiscript() + "'");
		html.append(" uploadLabel='{*[Upload]*}'");
		html.append(" deleteLabel='{*[Delete]*}'");
		html.append(" imgHeight='"+ getImgh() +"'");
		html.append(" imgWidth='"+ getImgw() +"'");
		
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
			if(!StringUtil.isBlank(fileFullName)){
				html.append("<SPAN >");
				html.append("<img border='0' width='"+imgw+"' height='"+imgh+"' src='" +env.getContextPath() + "/ShowImageServlet?type=image&id=" 
						+ fileFullName.split("_")[0] + "&applicationid="+ doc.getApplicationid() +"' alt='"+fileFullName.split("_")[1]+"' />");
				html.append("</SPAN>");
			}else{
				html.append("&nbsp;");
			}
			html.append(printHiddenElement(doc));
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
			if(!StringUtil.isBlank(fileFullName)){
				html.append("<SPAN >");
				html.append("<img border='0' width='"+imgw+"' height='"+imgh+"' src='" +env.getContextPath() + "/ShowImageServlet?type=image&id=" 
						+ fileFullName.split("_")[0] + "&applicationid="+ doc.getApplicationid() +"' alt='"+fileFullName.split("_")[1]+"' />");
				html.append("</SPAN>");
			}else{
				html.append("&nbsp;");
			}
			html.append(printHiddenElement(doc));
		}

		return html.toString();
	};
	
	
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {

		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			
			String src = doc.getItemValueAsString(getName());
			src = src == null ? "" : src.trim();
			int maxsize = !StringUtil.isBlank(getLimitsize()) ? Integer.valueOf(getLimitsize()).intValue() * 1024 : 10485760;
			
			xmlText.append("<").append(MobileConstant.TAG_IMAGEUPLOADTODATABASEFIELD);
			xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(src).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_WIDTH + "='" + getImgw() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_HIDDEN+ "='" + getImgh() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_SIZE).append("='").append(maxsize).append("'");
			
			xmlText.append(" ").append(MobileConstant.ATT_ID + "='" + src.split("_")[0] + "'");
			xmlText.append(" ").append(MobileConstant.ATT_FIELDID+ "='"+getFieldId(doc) +"'");
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
			xmlText.append("</").append(MobileConstant.TAG_IMAGEUPLOADTODATABASEFIELD + ">");
		}
		return xmlText.toString();
	}

	/**
	 * 获取图片上传的URL的地址，并输出HTML的文本
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
	 * 获取图片上传的URL的地址，并输出HTML的文本
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
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
		imgh = !StringUtil.isBlank(this.getImgh()) ? this.getImgh().trim() : "100";
		imgw = !StringUtil.isBlank(this.getImgw()) ? this.getImgw() : "100";
		String fileFullName = sb.toString();
		StringBuffer image = new StringBuffer();
		if (!StringUtil.isBlank(fileFullName)) {
			String url =env.getContextPath() + "/ShowImageServlet?type=image&id="+fileFullName.split("_")[0]+"&applicationid="+ doc.getApplicationid();
			String viewReadOnly = "";
			if(doc.get_params().getParameterAsString("viewReadType") != null && doc.get_params().getParameterAsBoolean("viewReadType")){
				viewReadOnly = "true";
			}
			image.append("<div moduleType='viewImageUpload2DataBase' docId='" + doc.getId() + "' docFormid='" + doc.getFormid() + "' "+ 
					"imgw='" + imgw + "' imgh='" + imgh + "' fileName='" + fileFullName + "' url='" + url + "'  viewReadOnly ='" + viewReadOnly + "'>");
			image.append("</div>");
			return image.toString();
		}
		if (!StringUtil.isBlank(doc.getParentid())) {
			int displayType = getDisplayType(doc, runner, webUser);
			if (displayType == PermissionType.HIDDEN) {
				return this.getHiddenValue();
			}
		}
		return sb.toString();
	}

	protected String getRefreshUploadListFunction(String fieldId, String uploadList,boolean readonly,boolean refresh,String applicationid) {
		String height = !StringUtil.isBlank(this.getImgh()) ? this.getImgh().trim() : "100";
		String width = !StringUtil.isBlank(this.getImgw()) ? this.getImgw() : "100";
		
		return "refreshImgToDataBaseList(document.getElementById('" + fieldId + "').value, '" + uploadList + "',"+Integer.parseInt(height)+","+Integer.parseInt(width)+","+readonly+","+refresh+",'"+applicationid+"');";
		
	}
	
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

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}