package cn.myapps.core.sysconfig.action;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.core.sysconfig.util.ExportUtil;


public class ExportAction extends ActionSupport {
	
	private static final long serialVersionUID = 4087246225170028124L;
	/**
	 * 运行时异常
	 */
	private OBPMRuntimeException runtimeException;
	/**
	 * 获取运行时异常
	 * @return
	 */
	public OBPMRuntimeException getRuntimeException() {
		return runtimeException;
	}

	/**
	 * 设置运行时异常
	 * @param runtimeException
	 */
	public void setRuntimeException(OBPMRuntimeException runtimeException) {
		this.runtimeException = runtimeException;
	}

	
	public InputStream getExportFile() {
		URL url = ExportAction.class.getClassLoader().getResource("sysConfig.xml");
		if(url != null)
			try {
				return url.openStream();
			} catch (IOException e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		/*
		String file = ExportUtil.getFile();
		if(file != null){
			if(file.startsWith("/") && file.length() > 1)
				file = file.substring(1);
			InputStream in = ServletActionContext.getServletContext().getResourceAsStream(file);
			System.out.println("================" + in);
			return in;
		}
		*/
		return null;
	}
	
	public String doExport() {
		try {
			ExportUtil.export();
		}catch (OBPMValidateException e) {
			addFieldError("export.error", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
}
