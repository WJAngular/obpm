package cn.myapps.mobile2.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.myapps.base.OBPMValidateException;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jfree.util.Log;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.FileManagerField;
import cn.myapps.core.filemanager.FileOperate;
import cn.myapps.core.upload.ejb.UploadProcess;
import cn.myapps.core.upload.ejb.UploadVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;


import flex.messaging.util.URLDecoder;

/**
 * @author nicholas
 */
public class MbFileAction extends ActionSupport {

	private static final long serialVersionUID = -3253948249606151825L;

	private static final Logger LOG = Logger.getLogger(MbFileAction.class);
	
	/**
	 * 输出值
	 */
	private String result;
	
	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}

	private Map<String, List<String>> fieldErrors;

	public void addFieldError(String fieldname, String message) {
		List<String> thisFieldErrors = getFieldErrors().get(fieldname);

		if (thisFieldErrors == null) {
			thisFieldErrors = new ArrayList<String>();
			this.fieldErrors.put(fieldname, thisFieldErrors);
		}
		thisFieldErrors.add(message);
	}

	public Map<String, List<String>> getFieldErrors() {
		if (fieldErrors == null)
			fieldErrors = new HashMap<String, List<String>>();
		return fieldErrors;
	}

	/**
	 * @SuppressWarnings API不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public void setFieldErrors(Map fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
	
	/**
	 * 删除列表中所有文件
	 * 
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 */
	public String doDeleteFile() throws Exception {
		try {
			ParamsTable params = getParams();
			String fileFullName = params.getParameterAsString("fileFullName");
			String application = params.getParameterAsString("application");
			if (!StringUtil.isBlank(fileFullName)) {
				fileFullName = URLDecoder.decode(fileFullName,"UTF-8");
				UploadProcess uploadProcess = (UploadProcess) ProcessFactory.createRuntimeProcess(UploadProcess.class, application);
				String[] fileFullNameArry = fileFullName.split(";");
				for (int i = 0; i < fileFullNameArry.length; i++) {
					UploadVO uploadVO = (UploadVO) uploadProcess.findByColumnName1("PATH", fileFullNameArry[i]);
						if (uploadVO != null) {
							String fileRealPath = getEnvironment().getRealPath(uploadVO.getPath());
							File file = new File(fileRealPath);
							if (file.exists()) {
								if (!file.delete()) {
									Log.warn("File(" + fileRealPath + ") delete failed");
									throw new Exception("File(" + fileRealPath + ") delete failed");
								}
							}
							uploadProcess.doRemove(uploadVO.getId());
						} else {//add by peter for onlinecarme
                            String fullPath = Environment.getInstance().getRealPath(fileFullNameArry[i]);
                            File f = new File(fullPath);
                            if(f.exists()==true && !f.isDirectory()){
                                f.delete();
                            }
                            return "";
                        }

                }
				}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			StringBuffer xml = new StringBuffer();
			xml.append("[ERROR]*");
			xml.append(e.getMessage());
			this.result = xml.toString();
			LOG.warn(e);
		}
		return "";
	}
	
	/**
	 * 获取文件管理uploads下文件夹及文件
	 * @return
	 */
	public String doFileList(){
		try{
			ParamsTable params = getParams();
			String filePattern = params.getParameterAsString("filePattern");
			String rootpath = params.getParameterAsString("fileCatalog");
			String path = params.getParameterAsString("path");
			String application = params.getParameterAsString("application");
			
			if (filePattern.equals(FileManagerField.DEFINITION)) {
				rootpath = "uploads" + rootpath;
			}else{
				rootpath = "uploads";
			}
			rootpath =  Environment.getInstance().getRealPath(rootpath);
			rootpath = rootpath.replace("/", "\\");
			if(StringUtil.isBlank(path)){
				path = rootpath;
			}
			//获得角色列表
			String rolelist = getUser().getRolelist(application);
			String xmlText = new FileOperate().toMbXMLText(rootpath,path,rolelist,application);
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("toXml", xmlText);
		}catch(Exception e){
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 获取所有文件夹列表
	 * @return
	 */
	public String doFolderList(){
		try{
			ParamsTable params = getParams();
			String filePattern = params.getParameterAsString("filePattern");
			String rootpath = params.getParameterAsString("fileCatalog");
			
			if (filePattern.equals(FileManagerField.DEFINITION)) {
				rootpath = "uploads" + rootpath;
			}else{
				rootpath = "uploads";
			}
			rootpath =  Environment.getInstance().getRealPath(rootpath);
			rootpath = rootpath.replace("/", "\\");
			String xmlText = new FileOperate().toMbFolderListXMLText(rootpath);
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("toXml", xmlText);
		}catch(Exception e){
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 文件操作
	 * @return
	 */
	public String doFileOperate(){
		try{
			ParamsTable params = getParams();
			String operateType = params.getParameterAsString("operateType");
			//新建文件夹
			if(operateType.equals("NewFolder")){
				String newPath = params.getParameterAsString("newPath");
				new FileOperate().newFolder(newPath);
			}else if(operateType.equals("ReNameFolder")){//重命名
				String newPath = params.getParameterAsString("newPath");
				String oldPath = params.getParameterAsString("oldPath");
				new FileOperate().reNameFolderOrFile(oldPath, newPath);
			}else if(operateType.equals("DeleteFolder")){//删除文件夹
				String newPath = params.getParameterAsString("newPath");
				new FileOperate().delFolder(newPath);
			}else if(operateType.equals("DeleteFile")){//删除文件
				String newPath = params.getParameterAsString("newPath");
				if(!StringUtil.isBlank(newPath)){
					String[] newPaths = newPath.split(";");
					new FileOperate().delMoreFile(newPaths);
				}
			}else if(operateType.equals("CopyFile")){//复制文件
				String selectPaths = params.getParameterAsString("selectPaths");
				String newPath = params.getParameterAsString("newPath");
				if(!StringUtil.isBlank(selectPaths) && !StringUtil.isBlank(newPath)){
					String[] selectPaths1 = selectPaths.split(";");
					new FileOperate().copyMoreFile(selectPaths1,newPath);
				}
			}else if(operateType.equals("RemoveFile")){//移动文件
				String selectPaths = params.getParameterAsString("selectPaths");
				String newPath = params.getParameterAsString("newPath");
				if(!StringUtil.isBlank(selectPaths) && !StringUtil.isBlank(newPath)){
					String[] selectPaths1 = selectPaths.split(";");
					new FileOperate().moveMoreFile(selectPaths1,newPath);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	/**
	 * 获取设置环境
	 * 
	 * @return Environment
	 */
	public Environment getEnvironment() {
		String ctxPath = ServletActionContext.getRequest().getContextPath();
		Environment evt = Environment.getInstance();
		evt.setContextPath(ctxPath);
		return evt;
	}
	
	/**
	 * Get the Parameters table
	 * 
	 * @return ParamsTable
	 */
	public ParamsTable getParams() {
		ParamsTable params = null;
		if (params == null) {
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
			params.setSessionid(ServletActionContext.getRequest().getSession().getId());

			if (params.getParameter("_pagelines") == null)
				params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
		}

		return params;
	}
	
	public WebUser getUser() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user == null) {
			throw new Exception("{*[page.timeout]*}");
		}
		return user;
	}

	public String getResult() {
		return result;
	}
}