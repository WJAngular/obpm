package cn.myapps.core.workflow.storage.runtime.action;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;


import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcessBean;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * @author happy
 *
 */
public class CirculatorAction extends AbstractRunTimeAction<Circulator> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5449790888013032605L;
	
	private SummaryCfgVO summaryCfg;

	public SummaryCfgVO getSummaryCfg() {
		return summaryCfg;
	}

	public void setSummaryCfg(SummaryCfgVO summaryCfg) {
		this.summaryCfg = summaryCfg;
	}

	@Override
	public IRunTimeProcess<Circulator> getProcess() {
		return new CirculatorProcessBean(getApplication());
	}
	
	
	public String doList() {
		try {
			SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);

			ParamsTable params = getParams();
			String lines = "10";
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					lines = cookie.getValue();
				}
			    cookie.getName();
			    cookie.getValue();
			}
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);

			String summaryCfgId = (String) params.getParameter("summaryCfgId");
			summaryCfg = (SummaryCfgVO) summaryCfgProcess.doView(summaryCfgId);
			setSummaryCfg(summaryCfg);
			if(summaryCfg != null){
				params.setParameter("formid", summaryCfg.getFormId());
				//params.setParameter("_orderby", summaryCfg.getOrderby());
				String currpage = params.getParameterAsString("_currpage");
				DataPackage<Circulator> datas = ((CirculatorProcess) getProcess()).getPendingByUser(params, getUser());
				//阅读待阅时返回，如果返回页无数据，跳到前一页
				if(datas.datas.isEmpty() && !StringUtil.isBlank(currpage)){
					int _currpage = Integer.parseInt(currpage);
					if(_currpage > 1) 
						_currpage = _currpage - 1;
					params.setParameter("_currpage", _currpage);
					datas = ((CirculatorProcess) getProcess()).getPendingByUser(params, getUser());
				}
				setDatas(datas);
			}
		}catch (OBPMValidateException e) {
			addFieldError("Pending list error", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String doWorkList() {
		try {
			ParamsTable params = getParams();
			setDatas(((CirculatorProcess) getProcess()).getWorksByUser(params, getUser()));
		} catch (OBPMValidateException e) {
			addFieldError("Pending list error", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	


}
