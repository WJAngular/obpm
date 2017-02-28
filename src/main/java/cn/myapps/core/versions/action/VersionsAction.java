package cn.myapps.core.versions.action;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;


import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.versions.ejb.VersionsProcess;
import cn.myapps.core.versions.ejb.VersionsVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class VersionsAction extends BaseAction<VersionsVO>{

	private static final long serialVersionUID = 1L;

	/**
	 * @SuppressWarnings 工厂方法创建的业务类，无法使用泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public VersionsAction() throws Exception {
		super(ProcessFactory.createProcess(VersionsProcess.class), new VersionsVO());
	}
	
	public String doList() {
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
		String[] orderbys = getParams().getParameterAsArray("_orderby");
		if(!isHasOrderBy(orderbys)){
			//默认按升级日期倒序排序
			getParams().setParameter("_orderby", new String []{"upgrade_date desc"});
		}
		return super.doList();
	}
	
	private boolean isHasOrderBy(String[] arrays){
		if(arrays != null){
			for (String string : arrays) {
				if(!StringUtil.isBlank(string)){
					return true;
				}
			}
		}
		return false;
	}

}
