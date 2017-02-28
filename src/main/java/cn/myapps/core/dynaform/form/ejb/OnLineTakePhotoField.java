package cn.myapps.core.dynaform.form.ejb;

import java.io.File;
import java.util.Map;

import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.StringUtil;

public class OnLineTakePhotoField extends FormField implements ValueStoreField {

	private static final long serialVersionUID = -8363899875473204943L;

	private Environment env = Environment.getInstance();

	/**
	 * 图片的高
	 */
	protected String imgh;
	/**
	 * 图片的宽
	 */
	protected String imgw;
	/**
	 * 相机像素
	 * 0:160*120 1:400*300 2:800*600
	 */
	protected String cameraPixelType ="0";
	
	protected String album;

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
		template.append(" cameraPixelType='"+getCameraPixelType() + "'");
		template.append(" album='"+getAlbum() + "'");
		template.append(">");
		return template.toString();
	}

	/**
	 * 以网格结合Document,返回的字符串为重定义后的html
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
			throws Exception {
		return toHtmlTxt(doc, runner, webUser);
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return toHtmlTxt(doc,runner,webUser,PermissionType.MODIFY);
	}
	
	/**
	 * 根据onlinetakephotoField的显示类型不同,返回的结果字符串不同.
	 * 新建的Document,onlinetakephotoField的显示类型为默认的MODIFY
	 * 。此时根据Form模版的onlinetakephotoField内容结合Document,返回的字符串为重定义后的html.
	 * 若根据流程节点设置对应onlinetakephotoField的显示类型不同,(默认为MODIFY),返回的结果字符串不同.
	 * 1)若节点设置对应onlinetakephotoField的显示类型为隐藏HIDDEN（值为3），返回 “******”字符串。
	 * 2)若节点设置对应onlinetakephotoField显示类型为MODIFY
	 * 内容结合Document中的ITEM存放的值,返回字符串为重定义后的html。
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser,permissionType);

		if(displayType == PermissionType.HIDDEN){
			return getHiddenValue();
		}
		
		Item item = null;
			if (doc != null) {
				item = doc.findItem(this.getName());
				boolean isnew = true;
				if (item != null && item.getValue() != null) {
					isnew = false;
				}
				html.append("<input moduleType='takePhoto' type='hidden'");
				html.append(" displayType= '" + displayType + "'");
				html.append(" contextPath= '" + env.getContextPath() + "'");
				html.append(" value= '" + getFieldValue(doc) + "'");
				html.append(" id= '" + getFieldId(doc) + "'");
				html.append(" imgw= '" + imgw + "'");
				html.append(" imgh= '" + imgh + "'");
				html.append(" name= '" + getName() + "'");
				html.append(" tagName= '" + getTagName() + "'");
				html.append(" cameraPixelType='"+getCameraPixelType() + "'");
				html.append(" discript= '" + getDiscript() + "'");
				html.append(" album='"+getAlbum() + "'");
				if (!isnew) {
					html.append(" value='" + getFieldValue(doc) + "'");
				}
				html.append("/>");
			}
		return html.toString();
	}
	
	
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {

		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			
			String src = getFieldValue(doc);//doc.getItemValueAsString(getName());
			src = src == null ? "" : src.trim();
			xmlText.append("<").append(MobileConstant.TAG_ONLINETAKEPHOTEFIELD);
			xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(src).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_WIDTH + "='" + getImgw() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_HIDDEN+ "='" + getImgh() + "'");
			
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
			xmlText.append("</").append(MobileConstant.TAG_ONLINETAKEPHOTEFIELD + ">");
		}
		return xmlText.toString();
	}

	/**
	 * 
	 * Form模版的(onlinetakephotoField)内容结合Document中的ITEM存放的值,返回字符串为重定义后的打印html文本
	 * 
	 * @param doc
	 *            Document
	 * @param runner
	 *            动态脚本执行器
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * @throws Exception
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}

			Item item = doc.findItem(this.getName());
			String value = "";

			if (item != null && item.getValue() != null) {
				value = (String) item.getValue();
				if(!StringUtil.isBlank(value)){
					Environment env = Environment.getInstance();
					String filePath = env.getRealPath(value);
					File file = new File(filePath);
					if (!file.exists()) {
						value = "/resource/image/photo.bmp";
					}
				}
			}

			if(!StringUtil.isBlank(value)){
				html.append("<img border='0' width='" + imgw + "' height='" + imgh
					+ "' src='" + env.getContextPath() + value + "' />");
			}
		}

		return html.toString();
	}
	
	

	/**
	 * Form模版的在线拍照组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的以PDF的形式输出
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 * @return PDF的格式的HTML的文本
	 */
	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			Item item = doc.findItem(this.getName());
			String value = "";

			if (item != null && item.getValue() != null) {
				value = (String) item.getValue();
				if(!StringUtil.isBlank(value)){
					Environment env = Environment.getInstance();
					String filePath = env.getRealPath(value);
					File file = new File(filePath);
					if (!file.exists()) {
						value = "/resource/image/photo.bmp";
					}
				}
			}

			html.append("<SPAN >&nbsp;");
			if(!StringUtil.isBlank(value)){
				html.append("<img border='0' width='" + imgw + "' height='" + imgh
					+ "' src='" +webUser.getServerAddr()+ env.getContextPath() + value + "' />");
			}
			html.append("</SPAN>");
		}

		return html.toString();
	}

	/**
	 * 获取字段显示值
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
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
			throws Exception {
		String fileFullName = getFieldValue(doc);
		StringBuffer image = new StringBuffer();
		if (fileFullName != null) {
			String webPath = fileFullName;
			String fileName = fileFullName.substring(fileFullName
					.lastIndexOf("/") + 1, fileFullName.length());
			String url = doc.get_params().getContextPath() + webPath;
			
			String viewReadOnly = "";
			if(doc.get_params().getParameterAsString("viewReadType") != null && doc.get_params().getParameterAsBoolean("viewReadType")){
				viewReadOnly = "true";
			}
			image.append("<div moduleType='viewTakePhoto' docId='" + doc.getId() + "' docFormid='" + doc.getFormid() + "' "+ 
					"imgw='" + imgw + "' imgh='" + imgh + "' fileName='" + fileName + "' url='" + url + "'  viewReadOnly ='" + viewReadOnly + "'>");
			image.append("</div>");
			return image.toString();
		}

		return super.getText(doc, runner, webUser);
	}

	/**
	 * 获取表单域真实值
	 * 
	 * @param doc
	 * @return
	 */
	protected String getFieldValue(Document doc) {
		String rtn = "";
		if (doc != null) {
			Item item = doc.findItem(getName());
			// 文本类型取值
			if (item != null && item.getValue() != null) {
				Object value = item.getValue();
				if (StringUtil.isBlank((String) value)) {
					rtn = "/resource/image/photo.png";
				} else {
					String valueStr = HtmlEncoder.encode((String) value + "");
					valueStr = valueStr != null && !valueStr.equals("null") ? valueStr
							: "";
					rtn = valueStr;
				}
			} else {
				rtn = "/resource/image/photo.png";
			}
		}
		return rtn;
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

	public String getCameraPixelType() {
		return cameraPixelType;
	}

	public void setCameraPixelType(String cameraPixelType) {
		this.cameraPixelType = cameraPixelType;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			
			String src = getFieldValue(doc);//doc.getItemValueAsString(getName());
			src = src == null ? "" : src.trim();
			xmlText.append("<").append(MobileConstant2.TAG_ONLINETAKEPHOTEFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_SRC).append("='").append(src).append("'");
			
			xmlText.append(" ").append(MobileConstant2.ATT_ID).append("='").append(getId()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");
			
			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='").append(true).append("'");
			}
			if (displayType == PermissionType.HIDDEN || 
					(getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("' ");
				}
			}
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
			}
			xmlText.append(">");
			xmlText.append(getDiscript());
			xmlText.append("</").append(MobileConstant2.TAG_ONLINETAKEPHOTEFIELD).append(">");
		}
		return xmlText.toString();
	}

}
