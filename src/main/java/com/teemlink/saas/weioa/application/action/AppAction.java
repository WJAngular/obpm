package com.teemlink.saas.weioa.application.action;

import java.util.Iterator;



import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.application.ejb.DomainApplicationSet;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.role.action.RoleHelper;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;

import com.teemlink.saas.weioa.base.action.BaseAction;

public class AppAction extends BaseAction {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7690498198992190934L;

	/**
	 * 获取我的应用列表
	 * @return
	 */
	public String doList(){
		try {
			WebUser user = getUser();
			String domainId = user.getDomainid();

			int page = 1;
			ApplicationProcess process = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
			DataPackage<ApplicationVO> packages = ((ApplicationProcess)process).doQueryByDomain(domainId, page, Integer.MAX_VALUE);
			setDatas(packages);
			return SUCCESS;
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 获取获选应用列表
	 * @return
	 */
	public String doCandidateList(){
		try {
			WebUser user = getUser();
			ApplicationProcess process = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);

			ParamsTable params = getParams();
			String domainId =  user.getDomainid();
			setDatas(process.doQueryUnBindApplicationsByDomain(params,domainId, 1, Integer.MAX_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 添加应用
	 * @return
	 */
	public String doAddApp(){
		try {
			WebUser user = getUser();
			String domainId = user.getDomainid();

			for (int i = 0; i < this._selects.length; i++) {
				ApplicationProcess process = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
				ApplicationVO vo = (ApplicationVO) process.doView(this._selects[i]);
				
				DomainApplicationSet domainApplicationSet = new DomainApplicationSet(vo.getId(),domainId , "");
				process.createDomainApplicationSet(domainApplicationSet);
			}
			
			
			addActionResult(true, "", "");
		}catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}

		return SUCCESS;
	}
	
	/**
	 * 移除应用
	 * @return
	 * @throws Exception
	 */
	public String doRemoveApp() throws Exception {
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String id = params.getParameterAsString("id");
			String domainId = user.getDomainid();
			DomainProcess dprocess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain = (DomainVO) dprocess.doView(domainId);
			ApplicationProcess process = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
			RoleHelper rh = new RoleHelper();
			for (Iterator<ApplicationVO> iterator = domain.getApplications().iterator(); iterator.hasNext();) {
				ApplicationVO vo = (ApplicationVO) iterator.next();
				if(id.equals(vo.getId())){
					rh.removeRole(domainId, id);
					process.removeDomainApplicationSet(domainId, id);
					break;
				}
			}
			PersistenceUtils.currentSession().clear();
			addActionResult(true, "", "");
		}catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	}
}
