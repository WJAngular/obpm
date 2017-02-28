package com.teemlink.saas;

import java.util.Collection;
import java.util.HashSet;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserProcessBean;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.workcalendar.calendar.action.CalendarHelper;
import cn.myapps.km.disk.ejb.NDisk;
import cn.myapps.km.disk.ejb.NDiskProcessBean;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;


public class RegisterAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 5234053871110545662L;
	/**
	 * 运行时异常
	 */
	private OBPMRuntimeException runtimeException;
	
	

	public OBPMRuntimeException getRuntimeException() {
		return runtimeException;
	}

	public void setRuntimeException(OBPMRuntimeException runtimeException) {
		this.runtimeException = runtimeException;
	}

	ParamsTable params = null;

	public String doRegister() throws Exception {
		SuperUserProcess userProcess = null;
		SuperUserVO user = null;
		try {
			params = getParams();

			String domainName = params.getParameterAsString("domainname");
			String loginemail = params.getParameterAsString("loginemail");
			String loginpwd = params.getParameterAsString("mainPassword");

			if (domainName == null || loginemail == null || loginpwd == null) {
				throw new Exception(
						"Please input Enterprise , Login Email and Password!");
			}

			int pos = loginemail.indexOf("@");
			String userName = pos > 0 ? loginemail.substring(0, pos)
					: loginemail;

			userProcess = new SuperUserProcessBean();

			DomainProcess domainProcess =(DomainProcess) ProcessFactory.createProcess(DomainProcess.class);

			

			user = userProcess.doViewByLoginno(loginemail);

			if (user == null) {
				user = new SuperUserVO();
			} else {
				throw new Exception("User existed!");
			}

			user.setLoginno(loginemail);
			user.setName(userName);
			user.setEmail(loginemail);
			user.setLoginpwd(loginpwd);

			user.setDomainAdmin(true);
			user.setStatus(1);

			// /// Create Domain
			DomainVO domain = new DomainVO();

			domain.setId(Sequence.getSequence());
			domain.setName(domainName);
			domain.setStatus(1);

			HashSet<DomainVO> domains = new HashSet<DomainVO>();

			domains.add(domain);

			user.setDomains(domains);

			Collection<SuperUserVO> admins = new HashSet<SuperUserVO>();
			admins.add(user);

			domain.setUsers(admins);
			userProcess.doCreate(user);
			try {
				domainProcess.doCreate(domain);
			} catch (Exception e) {
				userProcess.doRemove(user.getId());
				throw e;
			}
			CalendarHelper helper = new CalendarHelper();//创建工作日历
			helper.createCalendarByDomain(domain.getId());
			initPublicDisk(domain.getId());

			// setContent(user);
		}catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}

	public ParamsTable getParams() {
		if (params == null) {
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
			params.setSessionid(ServletActionContext.getRequest().getSession()
					.getId());
		}

		return params;
	}
	
	private void initPublicDisk(String domainid) throws Exception {
		try {
			NDiskProcessBean process = new NDiskProcessBean();
			
			NDisk pDisk = process.getPublicDisk(domainid);
			if(pDisk ==null){
				NDisk disk = new NDisk();
				disk.setId(Sequence.getSequence());
				disk.setName(NDisk.NAME_PUBLIC);
				disk.setType(NDisk.TYPE_PUBLIC);
				disk.setDomainId(domainid);
				process.doCreate(disk);
			}
			
			
		} catch (Exception e) {
			throw e;
		}
	}
}
