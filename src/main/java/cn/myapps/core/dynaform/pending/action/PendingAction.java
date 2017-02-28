package cn.myapps.core.dynaform.pending.action;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;


import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.pending.ejb.PendingProcess;
import cn.myapps.core.dynaform.pending.ejb.PendingProcessBean;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.homepage.ejb.Reminder;
import cn.myapps.util.ProcessFactory;

public class PendingAction extends AbstractRunTimeAction<PendingVO> {

	private static final long serialVersionUID = -8862606885675120112L;

	private Reminder reminder;
	
	private SummaryCfgVO summaryCfg;

	public Reminder getReminder() {
		return reminder;
	}

	public void setReminder(Reminder reminder) {
		this.reminder = reminder;
	}
	
	

	public SummaryCfgVO getSummaryCfg() {
		return summaryCfg;
	}

	public void setSummaryCfg(SummaryCfgVO summaryCfg) {
		this.summaryCfg = summaryCfg;
	}

	public String doList() {
		try {
			SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);

			ParamsTable params = getParams();
			String lines = "6";
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);
			String summaryCfgId = (String) params.getParameter("summaryCfgId");
			summaryCfg = (SummaryCfgVO) summaryCfgProcess.doView(summaryCfgId);
			setSummaryCfg(summaryCfg);
			if(summaryCfg != null){
				params.setParameter("formid", summaryCfg.getFormId());
				params.setParameter("_orderby", summaryCfg.getOrderby());
				setDatas(((PendingProcess) getProcess()).doQueryByFilter(params, getUser()));
			}
		} catch (OBPMValidateException e) {
			addFieldError("Pending list error", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public IRunTimeProcess<PendingVO> getProcess() {
		return new PendingProcessBean(getApplication());
	}

}
