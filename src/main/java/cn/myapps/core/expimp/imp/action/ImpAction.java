package cn.myapps.core.expimp.imp.action;

import java.io.File;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ValidationAware;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.constans.Environment;
import cn.myapps.core.expimp.imp.ejb.ImpProcess;
import cn.myapps.core.expimp.imp.ejb.ImpProcessBean;
import cn.myapps.core.expimp.imp.ejb.ImpSelect;
import cn.myapps.util.StringUtil;
import cn.myapps.util.file.FileOperate;
import cn.myapps.util.property.DefaultProperty;

public class ImpAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8303636509753933546L;

	private File impFile;

	private String impFilePath;

	private String impFileFileName;

	private String impFileContentType;

	private String moduleid;

	private String applicationid;

	private int importType;

	private ImpSelect impSelect;
	
	/**
	 * 导入处理方式（覆盖更新|复制）
	 */
	private String handleMode;
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

	public ImpAction() throws ClassNotFoundException {
		impSelect = new ImpSelect();
	}

	public String doStart() throws Exception {
		return SUCCESS;
	}

	public String doNext() throws Exception {
		return SUCCESS;
	}

	/**
	 * 获取上传文件并做相应的处理,如果有错误信息则转到相应的页面做处理
	 * 
	 * @return INPUT|SUCCESS
	 */
	public String doImp() {
		try {
			ImpProcess impProcess = new ImpProcessBean();
			
			if(importType!=1){
				handleMode = ImpSelect.HANDLE_MODE_UPDATE;
			}

			impSelect.setModuleid(moduleid);
			impSelect.setApplicationid(applicationid);
			impSelect.setImportType(importType);
			impSelect.setHandleMode(handleMode);
			impSelect.setHandleMode(handleMode);

			String impFilePath = getFileWebPath(impFileFileName);
			// 1. 校验xml文件是否可导入
			ValidationAware improtValidationAware = impProcess.doImportValidate(impSelect, getImpFile());
			if (improtValidationAware.hasErrors() || improtValidationAware.hasActionMessages()) {
				setActionErrors(improtValidationAware.getActionErrors());
				setActionMessages(improtValidationAware.getActionMessages());
				setImpFilePath(impFilePath);
				
				// 2. 把文件写入到本地目录
				FileOperate.writeFile(getFileRealPath(impFilePath), getImpFile());
				return "confirm";
			}
			
			impProcess.doImport(impSelect, getImpFile());
			ActionContext.getContext().put("refresh", "leftFrame");
			addActionMessage("{*[expimp.import.successful]*}");
		} catch (OBPMValidateException e) {
			addFieldError("","{*[expimp.import.failure]*}! "+e.getValidateMessage());
			e.printStackTrace();
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}

	private String getFileRealPath(String fileWebPath) throws Exception {
		String fileRealPath = Environment.getInstance().getRealPath(fileWebPath);
		return fileRealPath;
	}

	private String getFileWebPath(String impFileName) throws Exception {
		String exportPath = DefaultProperty.getProperty("EXPORT_PATH");
		String fileWebPath = exportPath + impFileName;
		return fileWebPath;
	}

	public String doBack() {
		return SUCCESS;
	}

	/**
	 * 出现提示信息后的后续处理
	 * 
	 * @return INPUT|SUCCESS
	 */
	public String doImpNext() {
		try {
			ImpProcess impProcess = new ImpProcessBean();
			
			if(importType!=1){
				handleMode = ImpSelect.HANDLE_MODE_UPDATE;
			}

			String fileRealPath = getFileRealPath(getImpFilePath());
			File importFile = new File(fileRealPath);

			impSelect.setModuleid(moduleid);
			impSelect.setApplicationid(applicationid);
			impSelect.setImportType(importType);
			impSelect.setHandleMode(handleMode);

			impProcess.doImport(impSelect, importFile);
			ActionContext.getContext().put("refresh", "leftFrame");
			addActionMessage("{*[expimp.import.successful]*}");
		} catch (OBPMValidateException e) {
			addFieldError("", "{*[expimp.import.failure]*}! "+e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}

	public void validate() {
		switch (importType) {
		case ImpSelect.IMPORT_TYPE_MODULE_ELEMENTS:
			if (StringUtil.isBlank(moduleid)) {
				addFieldError("", "{*[please.choose.module]*}");
			}
		default:
			break;
		}
	}

	public File getImpFile() {
		return impFile;
	}

	public void setImpFile(File impFile) {
		this.impFile = impFile;
	}

	public String getImpFilePath() {
		return impFilePath;
	}

	public void setImpFilePath(String impFilePath) {
		this.impFilePath = impFilePath;
	}

	public String getImpFileFileName() {
		return impFileFileName;
	}

	public void setImpFileFileName(String impFileFileName) {
		this.impFileFileName = impFileFileName;
	}

	public String getImpFileContentType() {
		return impFileContentType;
	}

	public void setImpFileContentType(String impFileContentType) {
		this.impFileContentType = impFileContentType;
	}

	public String getModuleid() {
		return moduleid;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	public String getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	public int getImportType() {
		return importType;
	}

	public void setImportType(int importType) {
		this.importType = importType;
	}

	public ImpSelect getImpSelect() {
		return impSelect;
	}

	public void setImpSelect(ImpSelect impSelect) {
		this.impSelect = impSelect;
	}

	public String getHandleMode() {
		return handleMode;
	}

	public void setHandleMode(String handleMode) {
		this.handleMode = handleMode;
	}
}
