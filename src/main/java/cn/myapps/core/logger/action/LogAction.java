package cn.myapps.core.logger.action;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;


import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.constans.Web;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.logger.ejb.LogProcess;
import cn.myapps.core.logger.ejb.LogVO;
import cn.myapps.util.ProcessFactory;

public class LogAction extends BaseAction<LogVO> {

	private static final long serialVersionUID = 8435578592541938310L;
	
	private static final Logger LOG = Logger.getLogger(LogAction.class);
	
	/**
	 * 默认构造方法
	 * @SuppressWarnings ProcessFactory.createProcess不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public LogAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(LogProcess.class), new LogVO());
	}
	
	@Override
	public String doList() {
		try {
			this.validateQueryParams();
			int lines = 10;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					lines = Integer.parseInt(cookie.getValue());
				}
			    cookie.getName();
			    cookie.getValue();
			}
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);
			setDatas(((LogProcess)process).getLogsByDomain(params, getUser()));
			//setDatas(((LogProcess)process).doQuery(getParams(), getUser()));
		} catch (OBPMValidateException e) {
			LOG.error("doList", e);
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			LOG.error("doList", e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}
	
	@Override
	public String doDelete() {
		return super.doDelete();
	}
	
	public String get_log() {
		try {
			DomainProcess process = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain = (DomainVO) process.doView(getParams().getParameterAsString("domain"));
			if (domain != null && domain.getLog() != null) {
				return domain.getLog().toString();
			}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return "false";
	}
	
	private String _log = "false";
	
	public void set_log(String _log) {
		this._log = _log;
	}
	
	@Override
	public String doSave() {
		try {
			DomainProcess process = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain = (DomainVO) process.doView(getParams().getParameterAsString("domain"));
			if (domain != null) {
				domain.setLog(Boolean.parseBoolean(_log));
				process.doUpdate(domain);
				this.addActionMessage("{*[Save_Success]*}");
			}
		}catch (OBPMValidateException e) {
			LOG.warn(e);
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			LOG.warn(e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return SUCCESS;
	}
	
	private static final String QUERY_REGEX = "[!$^&*()+=|{}';'\",<>/?~！#￥%……&*（）——|{}【】‘；：”“'。，、？]";
	
	@Override
	protected String getQueryRegex() {
		// 涉及到IP
		return QUERY_REGEX;
	}
	
}
