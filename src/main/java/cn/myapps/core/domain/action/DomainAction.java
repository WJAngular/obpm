package cn.myapps.core.domain.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.axis.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.application.ejb.DomainApplicationSet;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.domain.ejb.ExpImpElements;
import cn.myapps.core.domain.ejb.ExpProcess;
import cn.myapps.core.domain.ejb.ImpProcess;
import cn.myapps.core.fieldextends.ejb.FieldExtendsProcess;
import cn.myapps.core.fieldextends.ejb.FieldExtendsVO;
import cn.myapps.core.role.action.RoleHelper;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.sysconfig.ejb.LdapConfig;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workcalendar.calendar.action.CalendarHelper;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSModeProxy;
import cn.myapps.support.weixin.WeixinServiceProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;

import com.opensymphony.xwork2.ActionContext;

/**
 * @see cn.myapps.base.action.BaseAction DomainAction class.
 * @author Chris
 * @since JDK1.4
 */
public class DomainAction extends BaseAction<DomainVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4824002796094284157L;

	public String _strstatus = "true";
	
	private String domainId;
	private File impFile;
	private String synchronize;
	
	
	public String getSynchronize() {
		PropertyUtil.reload("sso");
		if(PropertyUtil.get("authentication.type").equals("sso") 
				&& PropertyUtil.get("sso.implementation").equals("cn.myapps.core.sso.ADUserSSO")){
			synchronize = "true";
		}else {
			synchronize = "false";
		}
		return synchronize;
	}

	public void setSynchronize(String synchronize) {
		this.synchronize = synchronize;
	}
	public File getImpFile() {
		return impFile;
	}

	public void setImpFile(File impFile) {
		this.impFile = impFile;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	/**
	 * 返回 域状态
	 * 
	 * @return "true"为可用，"false"为不可用
	 * @throws Exception
	 */
	public String get_strstatus() throws Exception {
		DomainVO domain = (DomainVO) getContent();
		if (domain.getStatus() == 1) {
			return "true";
		} else {
			return "false";
		}
	}
	
	/**
	 * 设置 域状态
	 * 
	 * @param strname
	 *            域状态字符串true or false
	 * @throws Exception
	 */
	public void set_strstatus(String strname) throws Exception {
		DomainVO domain = (DomainVO) getContent();
		if (strname != null) {
			if (strname.equalsIgnoreCase("true")) {
				domain.setStatus(1);
			} else {
				domain.setStatus(0);
			}
		}
	}


	public Map<String, String> getWeixinProxyType() {
		Map<String,String> weixinProxyType = new LinkedHashMap<String, String>();
		weixinProxyType.put("none", "{*[cn.myapps.core.domain.weixinProxyType.none]*}");
		weixinProxyType.put("cloud","{*[cn.myapps.core.domain.weixinProxyType.cloud]*}");
		if(!Environment.licenseType.equals("S.标准版")){
			weixinProxyType.put("local","{*[cn.myapps.core.domain.weixinProxyType.local]*}");
		}
		return weixinProxyType;
	}

	/**
	 * 
	 * DepartmentAction structure function.
	 * 
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public DomainAction() throws Exception {
		super((IDesignTimeProcess) ProcessFactory.createProcess(DomainProcess.class), new DomainVO());
	}

	public String doNew() {
		setContent(new DomainVO());
		return SUCCESS;
	}

	/**
	 * Delete a DomainVO.
	 * 
	 * @return If the action execution was successful.return "SUCCESS".Show an
	 *         success view .
	 *         <p>
	 *         If the action execution was a failure. return "ERROR".Show an
	 *         error view, possibly asking the user to retry entering data.
	 *         <p>
	 *         The "INPUT" is also used if the given input params are invalid,
	 *         meaning the user should try providing input again.
	 * 
	 * @throws Exception
	 */
	public String doDelete() {
		try {
			FieldExtendsProcess fieldExtendsProcess = (FieldExtendsProcess) ProcessFactory.createProcess(FieldExtendsProcess.class);
			String errorField = "";
			if (_selects != null) {
				for (int i = 0; i < _selects.length; i++) {
					String id = _selects[i];
					try {
						for (Iterator<FieldExtendsVO> iterator = fieldExtendsProcess.queryUserFieldExtends(id).iterator(); iterator.hasNext();){
							FieldExtendsVO fieldExtend = iterator.next();
							fieldExtendsProcess.doRemove(fieldExtend);
						}
						process.doRemove(id);
						CalendarHelper cldHelper = new CalendarHelper();
						cldHelper.removeCalendarByDomain(id);
					} catch (OBPMValidateException e) {
						errorField = e.getValidateMessage() + "," + errorField;
					}catch (Exception e) {
						this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
						e.printStackTrace();
					}
				}
				if (!errorField.equals("")) {
					if (errorField.endsWith(",")) {
						errorField = errorField.substring(0, errorField.length() - 1);
					}
					this.addFieldError("1", errorField);
					return INPUT;
				}
				addActionMessage("{*[delete.successful]*}");
			}

			return SUCCESS;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * Save tempDomain.
	 * 
	 * @return If the action execution was successful.return "SUCCESS".Show an
	 *         success view .
	 *         <p>
	 *         If the action execution was a failure. return "ERROR".Show an
	 *         error view, possibly asking the user to retry entering data.
	 *         <p>
	 *         The "INPUT" is also used if the given input params are invalid,
	 *         meaning the user should try providing input again.
	 * 
	 * @throws Exception
	 */
	public String doSave() {

		try {
			DomainVO tempDomain = (DomainVO) (this.getContent());
			String userid = getUser().getId();
			boolean flag = false;
			String tempname = tempDomain.getName();
			if(StringUtil.isBlank(tempname)){
				this.addFieldError("1", "{*[cn.myapps.core.domain.label.name.illegal]*}");
				flag = true;
			}else{
				DomainVO domain = ((DomainProcess) process).getDomainByName(tempname);
				if (domain != null) {			
					tempDomain.setLog(domain.getLog());
					if (tempDomain.getId() == null || tempDomain.getId().trim().length() <= 0) {
						this.addFieldError("1", "{*[core.domain.exist]*}");
						flag = true;
					} else if (!tempDomain.getId().trim().equalsIgnoreCase(domain.getId())) {
						this.addFieldError("1", "{*[core.domain.exist]*}");
						flag = true;
					}
				}
			}
			if (!flag) {
				if (tempDomain.getId() == null || tempDomain.getId().equals("")) {
					SuperUserProcess up = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);
					SuperUserVO user = (SuperUserVO) up.doView(userid);
					tempDomain.getUsers().add(user);
					process.doCreate(tempDomain);
					CalendarHelper cldHelper = new CalendarHelper();
					cldHelper.createCalendarByDomain(tempDomain.getId());
				} else {
					DomainVO po = (DomainVO) process.doView(tempDomain.getId());
					if (po != null) {
						tempDomain.setApplications(po.getApplications());
						tempDomain.setUsers(po.getUsers());
					}
					process.doUpdate(tempDomain);
				}
				ServletActionContext.getRequest().getSession().removeAttribute(Web.SKIN_TYPE);
				ServletActionContext.getRequest().getSession().setAttribute(Web.SKIN_TYPE, tempDomain.getSkinType());
				WeixinServiceProxy.cleanWeixinSecretCache();
				this.addActionMessage("{*[Save_Success]*}");
				return SUCCESS;
			} else {
				return INPUT;

			}
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	@SuppressWarnings("unchecked")
	public String doList() {
		try {
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
			if (params.getParameterAsString("t_users.id") != null) {
				if (!"".equals(params.getParameterAsString("t_users.id"))) {
					Map<String, String> request = (Map) ActionContext.getContext().get("request");
					request.put("userId", params.getParameterAsString("t_users.id"));
				}
			}
			WebUser user = getUser();
			if (user.isSuperAdmin()) {
				params.removeParameter("t_users.id");
			}
			return super.doList();
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 
	 * @return 返回Domain控制管理页
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String doDisplayView() throws Exception {
		Map params = getContext().getParameters();
		String[] ids = (String[]) (params.get("id"));
		String id = null;
		if (ids != null && ids.length > 0) {
			id = ids[0];
		}
		ValueObject contentVO = process.doView(id);
		setContent(contentVO);
		return SUCCESS;
	}

	public String doRemoveApp() throws Exception {
		ParamsTable params = getParams();
		String[] ids = params.getParameterAsArray("_selects");
		try {
			if (ids == null || ids.length <= 0)
				throw new OBPMValidateException("{*[core.domain.notChoose]*}");
			String domainId = params.getParameterAsString("domain");
			DomainProcess dprocess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain = (DomainVO) dprocess.doView(domainId);
			ApplicationProcess process = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
			//Set<ApplicationVO> set = new HashSet<ApplicationVO>();
			RoleHelper rh = new RoleHelper();
			//set.addAll(domain.getApplications());
			for (Iterator<ApplicationVO> iterator = domain.getApplications().iterator(); iterator.hasNext();) {
				ApplicationVO vo = (ApplicationVO) iterator.next();
				for (int i = 0; i < ids.length; i++) {
					if(ids[i].equals(vo.getId())){
						rh.removeRole(domainId, ids[i]);
						process.removeDomainApplicationSet(domainId, ids[i]);
					}
				}
			}
			//domain.setApplications(set);
			//dprocess.doUpdate(domain);
			PersistenceUtils.currentSession().clear();
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}
	
	public String doEditByUser() throws Exception{
		ParamsTable params = getParams();
		String domainid=params.getParameterAsString("domain");
		DomainVO vo=(DomainVO) this.process.doView(domainid);
		setContent(vo);
		return SUCCESS;
	}

	public String doRemoveAdmin() throws Exception {
		ParamsTable params = getParams();
		String error = "";
		if (_selects != null) {
			try {

				String domainId = params.getParameterAsString("domain");

				WebUser user = getUser();
				DomainProcess dprocess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
				SuperUserProcess suprocess = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);
				DomainVO domain = (DomainVO) dprocess.doView(domainId);

				Collection<SuperUserVO> admins = new HashSet<SuperUserVO>();
				admins.addAll(domain.getUsers());
				for (int i = 0; i < _selects.length; i++) {
					if (!_selects[i].equals(user.getId())) {
						SuperUserVO vo = (SuperUserVO) suprocess.doView(_selects[i]);

						if (admins.contains(vo)) {
							admins.remove(vo);
						}
					} else {
						error = "{*[core.domain.cannotremove]*}";
					}
				}

				domain.setUsers(admins);
				dprocess.doUpdate(domain);
				PersistenceUtils.currentSession().clear();
				if (!StringUtil.isBlank(error)) {
					this.addFieldError("1", error);
				} else {
					this.addActionMessage("{*[delete.successful]*}");
				}
			}catch (OBPMValidateException e) {
				this.addFieldError("1", e.getValidateMessage());
				return INPUT;
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
				return INPUT;
			}
		}
		return SUCCESS;
	}

	public String confirm() throws Exception {
		ParamsTable params = getParams();

		String[] ids = params.getParameterAsArray("_selects");
		try {
			if (ids == null || ids.length <= 0)
				throw new OBPMValidateException("{*[core.domain.notChoose]*}");
			String domainId = params.getParameterAsString("domain");

			//DomainProcess dprocess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);

			//DomainVO domain = (DomainVO) dprocess.doView(domainId);
			ApplicationProcess process = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
			//Set<ApplicationVO> old = new HashSet<ApplicationVO>();
			//old.addAll(domain.getApplications());

			for (int i = 0; i < ids.length; i++) {
				ApplicationVO vo = (ApplicationVO) process.doView(ids[i]);
				if (vo != null) {
					//old.add(vo);
					DomainApplicationSet domainApplicationSet = new DomainApplicationSet(vo.getId(),domainId , "");
					process.createDomainApplicationSet(domainApplicationSet);
				}
			}

			//domain.setApplications(old);
			//dprocess.doUpdate(domain);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}

	public String confirmAdmin() throws Exception {

		ParamsTable params = getParams();

		String[] ids = params.getParameterAsArray("_selects");
		try {
			if (ids == null || ids.length <= 0)
				throw new OBPMValidateException("{*[core.domain.notChoose]*}");
			String domainId = (String) params.getParameterAsString("domain");

			DomainProcess dprocess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);

			SuperUserProcess suprocess = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);

			DomainVO domain = (DomainVO) dprocess.doView(domainId);

			Collection<SuperUserVO> admins = new HashSet<SuperUserVO>();
			admins.addAll(domain.getUsers());

			for (int i = 0; i < ids.length; i++) {
				SuperUserVO user = (SuperUserVO) suprocess.doView(ids[i]);
				if (user != null) {
					user.setDomainAdmin(true);
					admins.add(user);
				}
			}

			domain.setUsers(admins);
			dprocess.doUpdate(domain);
		}catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}

	/**
	 * 获取企业域绑定的软件列表
	 * @return
	 * @throws Exception
	 */
	public String doListApps() throws Exception {
		try {
			this.validateQueryParams();
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
			String domainId = params.getParameterAsString("domain");
			String _currpage = params.getParameterAsString("_currpage");

			int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
			Collection<ApplicationVO> apps = new ArrayList<ApplicationVO>();
			ApplicationProcess process = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
			DataPackage packages = ((ApplicationProcess)process).doQueryByDomain(domainId, page, lines);
			Collection<DomainApplicationSet> set = process.getDomainApplicationSetByDomainId(domainId);
			
			for (Iterator<ApplicationVO> iterator = packages.datas.iterator(); iterator.hasNext();) {
				ApplicationVO app = iterator.next();
				for (Iterator<DomainApplicationSet> iterator2 = set.iterator(); iterator2.hasNext();) {
					DomainApplicationSet domainApplicationSet = (DomainApplicationSet) iterator2
							.next();
					if(domainApplicationSet.getApplicationId().equals(app.getId())){
						app.setWeixinAgentId(domainApplicationSet.getWeixinAgentId());
						apps.add(app);
						set.remove(domainApplicationSet);
						break;
					}
					
				}
			}
			packages.setDatas(apps);
			setDatas(packages);
			return SUCCESS;
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * @see com.opensymphony.xwork.ActionContext#getParameters()
	 * @SuppressWarnings getParameters()不支持泛型
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String holdAdmin() throws Exception {
		try {
			Map params = getContext().getParameters();
			String[] ids = (String[]) (params.get("domain"));
			String id = null;
			if (ids != null && ids.length > 0) {
				id = ids[0];
			}
			ValueObject contentVO = process.doView(id);
			setContent(contentVO);
			Collection col = ((DomainVO) contentVO).getUsers();
			if (col != null && col.size() > 0) {
				DataPackage datas = new DataPackage();
				ParamsTable paramsTable = getParams();
				String _currpage = paramsTable.getParameterAsString("_currpage");
				String _pagelines = paramsTable.getParameterAsString("_pagelines");
				int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
				int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
				Iterator it = col.iterator();
				datas.datas = new HashSet();
				// JDBC1.0
				for (int i = 0; it.hasNext(); i++) {
					if (i < (page - 1) * lines) {
						it.next();
						continue;
					}
					if (i >= page * lines)
						break;
					datas.datas.add(it.next());
				}

				datas.pageNo = page;
				datas.linesPerPage = lines;
				datas.rowCount = col.size();
				setDatas(datas);
			}
			return SUCCESS;
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String findManager() throws Exception {
		try {
			ParamsTable params = getParams();
			super.validateQueryParams();
			WebUser user = getUser();
			String name = params.getParameterAsString("sm_name");
			String managerLogin = params.getParameterAsString("sm_users.loginno");
			String _currpage = params.getParameterAsString("_currpage");
			String _pagelines = params.getParameterAsString("_pagelines");
			int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
			int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : 10;
			DomainProcess dprocess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);

			if (!user.isSuperAdmin()) {
				managerLogin = user.getName();
			}
			if (StringUtils.isBlank(managerLogin) && StringUtils.isBlank(name)) {
				return doList();
			} else {
				if (managerLogin == null) {
					managerLogin = "";
				}
				if (name == null) {
					name = "";
				}
				setDatas(dprocess.queryDomainsbyManagerLoginnoAndName(managerLogin.trim(), name.trim(), page, lines));
			}
			return SUCCESS;
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String get_password() {
		DomainVO vo = (DomainVO) getContent();
		if (vo != null && !StringUtil.isBlank(vo.getSmsMemberPwd()))
			return vo.getSmsMemberPwd();
		return "";
	}

	public void set_password(String _password) {
		DomainVO vo = (DomainVO) getContent();
		vo.setSmsMemberPwd(_password);
	}
	
	public String startexport(){
		return SUCCESS;
	}

	public String startimport(){
		return SUCCESS;
	}
	
	/**
	 * 企业域信息的导出
	 * @return
	 */
	public String doExport(){
		ExpImpElements elements = new ExpImpElements();
		try {
			ExpProcess process = (ExpProcess) ProcessFactory.createProcess(ExpProcess.class);
			File file = process.createZipFile(elements, domainId);
			ActionContext.getContext().put("filename", file.getName());
		}catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[core.domain.export.fail]*}!",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * 企业域信息的导入
	 * @return
	 */
	public String doImport(){
		try{
			ImpProcess process = (ImpProcess) ProcessFactory.createProcess(ImpProcess.class);
			process.doImport(getImpFile());
		}catch(OBPMValidateException e){
			if(e.getMessage().equals("{*[core.domain.name.exist]*}")){
				this.addFieldError("1", e.getValidateMessage());
			}else{
				this.addFieldError("1", "{*[core.domain.file.mistake]*}");
			}
			return INPUT;
		}catch(Exception e){
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		this.addActionMessage("{*[expimp.import.successful]*}");
		return SUCCESS;
	}
	
	public String doDownload() {
		try {
			ExpProcess expProcess = (ExpProcess) ProcessFactory.createProcess(ExpProcess.class);
			String[] filename = (String[]) ActionContext.getContext()
					.getParameters().get("filename");
			File exportFile = expProcess.getExportFile(filename[0]);
			if (exportFile != null) {
				setResponse(exportFile);
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return NONE;
	}
	
	public String doSynchLDAP() throws Exception{
		PropertyUtil.reload("sso");
		String manager = PropertyUtil.get(LdapConfig.MANAGER);
		String managerPassword = PropertyUtil.get(LdapConfig.MANAGERPASSWORD);
		DomainVO domain = (DomainVO) this.getContent();
		
		LdapContext ldapContext = null;
		try{
			DomainProcess process = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			if(StringUtil.isBlank(manager) || StringUtil.isBlank(managerPassword)){
				throw new OBPMValidateException("{*[core.domain.LDAPConfig]*}");
			}else{
				ldapContext = this.getLdapContext(manager, managerPassword);
				process.synchLDAP(ldapContext, domain);
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage() != null ? e.getValidateMessage() : "{*[core.domain.configError]*}");
			return INPUT;
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}finally {
			try {
				if(ldapContext != null) 
					ldapContext.close();
			} catch (NamingException e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}
		
		this.addActionMessage("同步成功");
		return SUCCESS;
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
	
	/**
	 * 获取LdapContext
	 * 
	 * @param userDn
	 * @param password
	 * @return
	 * @throws NamingException
	 */
	public LdapContext getLdapContext(String userDn, String password)
			throws NamingException {
		Control[] ctl = null;
		Hashtable<String, String> env = new Hashtable<String, String>();

		String url = PropertyUtil.get(LdapConfig.LDAP_URL);
		String pooled = PropertyUtil.get(LdapConfig.LDAP_POOLED);
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		// url,ldap的地址
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, userDn);
		env.put(Context.SECURITY_CREDENTIALS, password);
		// ldap连接池
		if ("true".equals(pooled) || "false".equals(pooled))
			env.put("com.sun.jndi.ldap.connect.pool", pooled);
		return new InitialLdapContext(env, ctl);
	}
	
	/**
	 * 测试天翎短信平台
	 */
	public void doTestSMS(){
		ParamsTable params = getParams();
		DomainVO vo = (DomainVO)getContent();
		String telephone = params.getParameterAsString("_telephone");
		JSONObject msg = new JSONObject();
		try {
			SMSModeProxy sender = new SMSModeProxy();
			int result = sender.sendWithoutLog(vo.getSmsMemberCode(), vo.getSmsMemberPwd(), telephone, "感谢您使用天翎通知服务，祝您工作愉快！");
			
			if(result>0){
				msg.put("type", "success");
				msg.put("message", "success");
			}else{
				msg.put("type", "failed");
				msg.put("message", "failed");
			}
		}catch (java.lang.reflect.InvocationTargetException e) {
			if(e.getCause() instanceof AxisFault){
				AxisFault f = (AxisFault)e.getCause();
				String m = f.getMessage();
				if(m.indexOf("无效的会员账号或密码")>=0){
					msg.put("type", "invalid");
					msg.put("message", m);
				}else if(m.indexOf("短信剩余数量已不足")>=0){
					msg.put("type", "insufficient");
					msg.put("message", m);
				}else {
					msg.put("type", "connectionFailed");
					msg.put("message", "connectionFailed");
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");  
            response.setCharacterEncoding("utf-8");  
            PrintWriter pw = response.getWriter(); 
            pw.write(msg.toString());
            pw.flush();  
            pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 从企业微信上同步组织结构到企业域
	 * @return
	 */
	public String doSynchFromWeixin(){
		DomainVO vo = (DomainVO)getContent();
		try {
			((DomainProcess)process).synchFromWeixin(vo);
			this.addActionMessage("同步成功");
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage() != null ? e.getValidateMessage() : "同步失败");
			return INPUT;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 从企业微信上同步组织结构到企业域
	 * @return
	 */
	public String doSynch2Weixin(){
		DomainVO vo = (DomainVO)getContent();
		try {
			((DomainProcess)process).synch2Weixin(vo);
			this.addActionMessage("同步成功");
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage() != null ? e.getValidateMessage() : "同步失败");
			return INPUT;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	
	public void testWeChat(){
		ParamsTable params = getParams();
		String CorpID = params.getParameterAsString("CorpID");
		String CorpSecret = params.getParameterAsString("CorpSecret");
		String msg =null;
		try {
			String string = WeixinServiceProxy.getAccessToken(CorpID, CorpSecret);
			if( string == null)
				msg = "error";
			else
				msg = "success";
		} catch (Exception e2) {
			e2.printStackTrace();
			msg = e2.getMessage();
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("ContentType", "text/json");  
        response.setCharacterEncoding("utf-8");  
        
		try {
			PrintWriter pw = response.getWriter();
			 pw.write(msg.toString());
		        pw.flush();  
		        pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
       
	}
	/**
	 * 更新软件的微信应用id
	 */
	public void doUpdateWeixinAgentId(){
		ParamsTable params = getParams();
		String domainId = params.getParameterAsString("domainId");
		String applicationId = params.getParameterAsString("applicationId");
		String weixinAgentId = params.getParameterAsString("weixinAgentId");
		String msg ="success";
		try {
			ApplicationProcess process = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
			process.updateDomainApplicationSet(domainId, applicationId, weixinAgentId);
		} catch (Exception e2) {
			e2.printStackTrace();
			msg = e2.getMessage();
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("ContentType", "text/json");  
        response.setCharacterEncoding("utf-8");  
        
		try {
			PrintWriter pw = response.getWriter();
			 pw.write(msg.toString());
		        pw.flush();  
		        pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	public String excelImportUserAndDept(){
		ParamsTable params = getParams();
		String domianId = params.getParameterAsString("domainid");
		String _path = params.getParameterAsString("_path");
		Environment evt = Environment.getInstance();
		String excelPath = evt.getRealPath(_path);
		
		if (!excelPath.toLowerCase().endsWith(".xls") && !excelPath.toLowerCase().endsWith(".xlsx")) {
			this.addFieldError("fileTypeError", "{*[core.dts.excelimport.config.cannotimport]*}");   //错误提示信息
			return SUCCESS;
		}
		
		try {
			File excelfile = new File(excelPath); //获取excel文件
			
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain =  (DomainVO) process.doView(domianId);
			
			JSONObject result = domainProcess.excelImportToDomain(domain, excelfile);
			
			Map<String, String> request = (Map) ActionContext.getContext().get("request");
			
			request.put("result", result.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("fileTypeError", "{*[core.domain.excel.import.fail]*}");   //错误提示信息
			return SUCCESS;
		}
		
		return SUCCESS;
	}
	
	
	public void excelExportUserAndDept() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("appliction/excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("用户列表.xls".getBytes(), "ISO-8859-1"));
			ServletOutputStream outputStream = response.getOutputStream();
			
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			domainProcess.excelExportFromDomain(outputStream, null);
			
			if(outputStream != null){
				outputStream.close();
			}
		} catch (Exception e) {
			this.addFieldError("fileTypeError", "{*[core.domain.excel.export.fail]*}");   //错误提示信息
			e.printStackTrace();
		}
	}
	
}
