/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package cn.myapps.core.filedownload.action;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;


/**
 * Action to demonstrate how to use file download. <p/> This action is used to
 * download a jpeg file from the image folder.
 * 
 * @author Claus Ibsen
 */
public class FileDownloadAction extends ActionSupport implements Action {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3689402898832298757L;

	private String filename;

	private String webPath;

	public String doDownload() throws Exception {
		String fileWebPath = getWebPath() + "/" + getFilename();
		FileDownloadUtil.doFileDownload(ServletActionContext.getResponse(), fileWebPath);

		return NONE;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getWebPath() {
		return webPath;
	}

	public void setWebPath(String webPath) {
		this.webPath = webPath;
	}
}
