package cn.myapps.core.dynaform.summary.action;

import java.util.Collection;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * @author Happy
 *
 */
public class SummaryCfgAction extends BaseAction<SummaryCfgVO> {
	private static final long serialVersionUID = 5617087045674948355L;

	@SuppressWarnings("unchecked")
	public SummaryCfgAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(SummaryCfgProcess.class), new SummaryCfgVO());
	}
	
	public String doNew() {
		SummaryCfgVO vo = (SummaryCfgVO)this.getContent();
		if(StringUtil.isBlank(vo.getType())){
			vo.setType("00");
		}
		return super.doNew();
	}

	public String doHomepageList() {
		ParamsTable params = this.getParams();
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
		try {
			String application = getParams()
					.getParameterAsString("application");
			String s_application = getParams().getParameterAsString(
					"s_application");
			if ((application == null || application.trim().equals(""))
					&& (s_application == null || s_application.trim()
							.equals(""))) {
				this.setDatas(new DataPackage());
			} else {
				return super.doList();
			}
			return SUCCESS;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	public String doList() {
		try {
			this.validateQueryParams();
			ParamsTable params = getParams();
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
			datas = this.process.doQuery(params, getUser());
		}catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}

}
