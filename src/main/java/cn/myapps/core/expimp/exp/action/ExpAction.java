package cn.myapps.core.expimp.exp.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.constans.Environment;
import cn.myapps.core.expimp.exp.ejb.ExpProcess;
import cn.myapps.core.expimp.exp.ejb.ExpProcessBean;
import cn.myapps.core.expimp.exp.ejb.ExpSelect;
import cn.myapps.util.StringUtil;


public class ExpAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6603303079282994162L;

	private ExpSelect expSelect = null; // 导出的对象

	private String applicationid;

	private String moduleid;

	private int exportType;

	private ExpProcess process;
	
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

	public ExpAction() {
		process = new ExpProcessBean();
		expSelect = new ExpSelect();
	}

	public String doStart() throws Exception {
		String[] applicationid = (String[])ActionContext.getContext().getParameters().get("applicationid");
		setApplicationid(applicationid[0]);
		return SUCCESS;

	}

	public String doNext() throws Exception {
		return SUCCESS;
	}

	public String doExp() {
		try {
			ExpProcess expProcess = (ExpProcess) process;
			getExpSelect().setApplicationid(applicationid);
			getExpSelect().setModuleid(moduleid);
			getExpSelect().setExportType(exportType);

			File xmlFile = expProcess.createZipFile(getExpSelect());
			ActionContext.getContext().put("filename", xmlFile.getName());
		} catch (OBPMValidateException e) {
			addFieldError("","{*[expimp.export.failure]*}! "+e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}

	public String doDownload() {
		try {
			ExpProcess expProcess = (ExpProcess) process;
			String[] filename = (String[]) ActionContext.getContext()
					.getParameters().get("filename");
			File exportFile = expProcess.getExportFile(filename[0]);
			if (exportFile != null) {
				setResponse(exportFile);
			}
		}catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return NONE;
	}

	public String doBack() {
		return SUCCESS;
	}

	/**
	 * 校验
	 */
	public void validate() {
		switch (exportType) {
		case ExpSelect.EXPROT_TYPE_MODULE:
			if (StringUtil.isBlank(moduleid)) {
				addFieldError("", "{*[please.choose.module]*}");
			}
			break;
		case ExpSelect.EXPROT_TYPE_MODULE_ELEMENTS:
			if (getExpSelect().isEmpty()) {
				addFieldError("", "{*[please.choose.one]*}");
			}
		default:
			break;
		}
	}

	public void setResponse(File file) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		String encoding = Environment.getInstance().getEncoding();
		response.setContentType("application/x-msdownload; charset=" + encoding
				+ "");
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ java.net.URLEncoder.encode(file.getName(), encoding) + "\"");
		OutputStream os = response.getOutputStream();

		BufferedInputStream reader = new BufferedInputStream(
				new FileInputStream(file));
		byte[] buffer = new byte[4096];
		int i = -1;
		while ((i = reader.read(buffer)) != -1) {
			os.write(buffer, 0, i);
		}
		os.flush();
		os.close();

		reader.close();
	}

	public ExpSelect getExpSelect() {
		return expSelect;
	}

	public void setExpSelect(ExpSelect expSelect) {
		this.expSelect = expSelect;
	}

	public int getExportType() {
		return exportType;
	}

	public void setExportType(int exportType) {
		this.exportType = exportType;
	}
}
