package cn.myapps.mobile.upload;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;


/**
 * 手机上传图片
 * @author Tom
 *
 */
public class MbUploadAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	public static final String PATH = "path";

	public String doUpload() {

		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String path = MbUploadActionHelper.getFilePath();
			MbUploadActionHelper.saveFile(request, path);
			request.setAttribute(PATH, "/"+path+"_"+"Form Mobile");
		} catch (Exception e) {
			addFieldError("SystemError", e.toString());
			return ERROR;
		}
		return SUCCESS;
	}
	

}
