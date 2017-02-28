package cn.myapps.core.sysconfig.action;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.core.sysconfig.util.ImportUtil;


public class ImportAction extends ActionSupport {

	private static final long serialVersionUID = -8566223843186650982L;

	private File xmlConfig;
	private String xmlConfigContentType;
	private String xmlConfigFileName;
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


	public File getXmlConfig() {
		return xmlConfig;
	}

	public void setXmlConfig(File xmlConfig) {
		this.xmlConfig = xmlConfig;
	}

	public String getXmlConfigContentType() {
		return xmlConfigContentType;
	}

	public void setXmlConfigContentType(String xmlConfigContentType) {
		this.xmlConfigContentType = xmlConfigContentType;
	}

	public String getXmlConfigFileName() {
		return xmlConfigFileName;
	}

	public void setXmlConfigFileName(String xmlConfigFileName) {
		this.xmlConfigFileName = xmlConfigFileName;
	}

	public String doImport() {
		if (!validateImport())
			return INPUT;
		try {
			ImportUtil.load(this.xmlConfig);
			this.addActionMessage("{*[expimp.import.successful]*}");
			return SUCCESS;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("import.error"+"{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	private boolean validateImport() {
		if (this.xmlConfig == null) {
			this.addFieldError("empty.file", "{*{[empty.file.to.import]}*}");
			return false;
		}
		return true;
	}

}
