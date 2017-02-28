package cn.myapps.core.deploy.application.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.application.ejb.CopyApplicationProcess;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.dts.datasource.action.DatasourceHelper;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.FormProcessBean;
import cn.myapps.core.dynaform.form.ejb.FormTableProcessBean;
import cn.myapps.core.resource.dao.ResourceDAO;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.tree.Node;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.ResponseUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;



/**
 * @see cn.myapps.base.action.BaseAction ApplicationAction class.
 * @author Darvense
 * @since JDK1.4
 */
public class ApplicationAction extends BaseAction<ApplicationVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(ApplicationAction.class);

	private String id;

	private String overviewFile;

	public String getId() {
		return id;
	}

	public void setId(String _applicationid) {
		this.id = _applicationid;
	}

	/**
	 * 
	 * ApplicationAction structure function.
	 * 
	 * @SuppressWarnings 工厂方法无法使用泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public ApplicationAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(ApplicationProcess.class),
				new ApplicationVO());
	}

	/**
	 * @SuppressWarnings WebWork不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public String doEdit() {
		Map params = getContext().getParameters();
		String id = ((String[]) params.get("id"))[0];
		HttpSession session = ServletActionContext.getRequest().getSession();
		// String
		// currentApplication=(String)session.getAttribute("currentApplication");
		session.setAttribute("currentApplication", id);
		return super.doEdit();
	}

	/**
	 * get_ispublished method
	 * 
	 * @return Return true or false String
	 */
//	public String get_ispublished() {
//		ApplicationVO content = (ApplicationVO) getContent();
//		if (content.isIspublished()) {
//			return "true";
//		} else {
//			return "false";
//		}
//	}

	/**
	 * Set the _ispublished
	 * 
	 * @param ispublished
	 */
//	public void set_ispublished(String ispublished) {
//		ApplicationVO content = (ApplicationVO) getContent();
//		if (ispublished != null) {
//			if (ispublished.trim().equalsIgnoreCase("true")) {
//				content.setIspublished(true);
//				return;
//			}
//		}
//		content.setIspublished(false);
//	}

	/**
	 * get_isdefaultsite method
	 * 
	 * @return Return true or false String
	 */
//	public String get_isdefaultsite() {
//		ApplicationVO content = (ApplicationVO) getContent();
//		if (content.isIsdefaultsite()) {
//			return "true";
//		} else {
//			return "false";
//		}
//	}

	/**
	 * set_isdefaultsite method
	 * 
	 * @param isdefaultsite
	 */
//	public void set_isdefaultsite(String isdefaultsite) {
//		ApplicationVO content = (ApplicationVO) getContent();
//		if (isdefaultsite != null) {
//			if (isdefaultsite.trim().equalsIgnoreCase("true")) {
//				content.setIsdefaultsite(true);
//				return;
//			}
//		}
//		content.setIspublished(false);
//	}

	/**
	 * Delete a tempApp
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
		String errorField = "";

		try {
			PersistenceUtils.beginTransaction();
			if (_selects != null) {
				for (int i = 0; i < _selects.length; i++) {
					String id = _selects[i];
					ApplicationVO tempApp = (ApplicationVO) (process.doView(id));
					if (tempApp.getDomains().size() > 0) {
						throw new OBPMValidateException("{*[core.application.referenced]*}");
					}
					
					ModuleProcess moduleProcess = (ModuleProcess) ProcessFactory
							.createProcess(ModuleProcess.class);
					FormProcess formProcess = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);
					//拿到模块
					String moduleHql="from cn.myapps.core.deploy.module.ejb.ModuleVO where applicationid = '"+id+"'";
					Collection<ModuleVO> modules = moduleProcess.doQueryByHQL(moduleHql, 1, Integer.MAX_VALUE);
					//删除表单
					String hql="from cn.myapps.core.dynaform.form.ejb.Form where applicationid = '"+id+"'";
					Collection<Form> formList = formProcess.doQueryByHQL(hql, 1, Integer.MAX_VALUE);
					FormProcessBean formProcessBean=new FormProcessBean();
					if (formList != null && !formList.isEmpty()) {
						for(Iterator<Form> form = formList.iterator();form != null && form.hasNext();){
							formProcessBean.doRemove(form.next());
						}
					}
					try {
						int removedModuleSize = 0;
						if (modules != null) {
							for (Iterator<ModuleVO> iterator = modules
									.iterator(); iterator.hasNext();) {
								ModuleVO module = (ModuleVO) iterator
									.next();
								module.setApplication(null);
								module.setSuperior(null);
								moduleProcess.doRemove(module);
								removedModuleSize++;
							}
							if (modules.size() == removedModuleSize) {
								tempApp.setOwners(null);
								tempApp.setModules(null);
								tempApp.setDomains(null);
								process.doRemove(id);
								deleteDataSource(id);
							} else {
								errorField = "("
										+ tempApp.getName()
										+ "){*[core.application.hasrelation]*}"
										+ (StringUtil.isBlank(errorField) ? ""
												: "," + errorField);
							}
						} else {
							tempApp.setOwners(null);
							process.doUpdate(tempApp);
							process.doRemove(id);
							deleteDataSource(id);
						}
						
						if(!StringUtil.isBlank(errorField)){
							throw new OBPMValidateException("errorField");
						}else{
							PersistenceUtils.commitTransaction();
						}

					} catch (OBPMValidateException e) {
						PersistenceUtils.rollbackTransaction();
						 errorField = "(" + tempApp.getName() +
						 "){*[core.application.delete.nosuccess]*}";
					}catch (Exception e) {
						PersistenceUtils.rollbackTransaction();
						this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
						e.printStackTrace();
						return INPUT;
					}
					
					if (!errorField.equals("")) {
						this.addFieldError("1", errorField);
						return INPUT;
					}
				}
				this.addActionMessage("{*[delete.successful]*}");

			}
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}

	/**
	 * 删除对应软件下所有数据源
	 * 
	 * @param id
	 */
	private void deleteDataSource(String id) {
		try {
			// 删除该软件下所有数据源
			DataSourceProcess dataSourceProcess = (DataSourceProcess) ProcessFactory
					.createProcess(DataSourceProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_applicationid", id);
			DataPackage<DataSource> dataPackage = dataSourceProcess
					.doQuery(params);
			if (dataPackage.rowCount > 0) {
				for (Iterator<DataSource> iteratorDataSource = dataPackage.datas
						.iterator(); iteratorDataSource.hasNext();) {
					DataSource dataSource = (DataSource) iteratorDataSource
							.next();
					dataSourceProcess.doRemove(dataSource);
				}
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
	}

	/**
	 * 同步当前应用关联的数据库的所有表单的表(同步生成的表没有数据)
	 * 
	 * @return
	 */
	public String doSynFormTable() {
		try {
			ApplicationVO vo = (ApplicationVO) getContent();

			String newDtsId = vo.getDatasourceid();
			String oldDtsId = ((ApplicationVO) ((ApplicationProcess) process)
					.doView(vo.getId())).getDatasourceid();
			if (newDtsId != null && !newDtsId.equals(oldDtsId)) {
				this.addFieldError("saveBeforeSyn",
						"{*[cn.myapps.core.deploy.application.save_current_application]*}");
				return INPUT;
			}
			FormProcess fp = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);
			FormTableProcessBean tableProcess = new FormTableProcessBean(vo
					.getApplicationid());
			Iterator<Form> forms = fp.get_formList(vo.getId()).iterator();
			while (forms.hasNext()) {
				tableProcess.createOrUpdateDynaTable(forms.next(), null);
			}
			this.addActionMessage("{*[cn.myapps.core.deploy.application.synchronization_success]*}");
			return SUCCESS;
		} catch (OBPMValidateException e) {
			this.addFieldError("synFail",
					"{*[cn.myapps.core.deploy.application.synchronization_fail_info]*}:"
							+ e.getValidateMessage() + "]");
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.deploy.application.synchronization_fail_info]*}:"
					+ e.getMessage() + "]",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String doSave() {
		try {
			ApplicationVO vo = (ApplicationVO) getContent();
			SuperUserProcess sup = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			SuperUserVO user = (SuperUserVO) sup.doView(getUser().getId());

			String applicationname = vo.getName();

			ApplicationVO application = ((ApplicationProcess) process)
					.doViewByName(applicationname);
			// vo.setCreater(user);

			vo.getOwners().add(user);

			if (application != null) {
				if (application.getId() == null
						|| application.getId().trim().length() <= 0) {
					this.addFieldError("1", "{*[core.application.exist]*}");
					return INPUT;
				}
				if (!vo.getId().trim().equalsIgnoreCase(application.getId())) {
					this.addFieldError("1", "{*[core.application.exist]*}");
					return INPUT;
				}
				/*
				 * DataSource datasource = application.getDataSourceDefine(); if
				 * (StringUtil.isBlank(application.getDatasourceid()) ||
				 * datasource == null || StringUtil.isBlank(datasource.getId()))
				 * { this.addFieldError("empty.datasource",
				 * "{*[core.appliction.datasource.empty]*}"); return INPUT; }
				 */
			}

			if (vo.getDescription() == null
					|| vo.getDescription().trim().length() == 0)
				vo.setDescription("");
			if (vo.getId() != null) {
				AbstractRunTimeProcessBean.removeDataSource(vo.getId());
				DbTypeUtil.remove(vo.getId());
			}
			super.doSave();
			// new ResourceHelper().addReportResource(vo.getId());
		}  catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public String doCopySave() {
		try {
			ApplicationVO vo = (ApplicationVO) getContent();
			/*
			 * if (vo.getName() == null || vo.getName().equals("")) { throw new
			 * Exception("{*[page.name.notexist]*}"); }
			 */
			SuperUserProcess sup = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			SuperUserVO user = (SuperUserVO) sup.doView(getUser().getId());
			vo.getOwners().add(user);
			if (vo.getDescription() == null
					|| vo.getDescription().trim().length() == 0)
				vo.setDescription("");
			if (vo.getId() != null) {
				AbstractRunTimeProcessBean.removeDataSource(vo.getId());
				DbTypeUtil.remove(vo.getId());
			}
			super.doSave();
		}catch (OBPMValidateException e) {
			this.addFieldError("Exception", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String listApps() throws Exception {
		try {
			super.validateQueryParams();
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
			WebUser user = getUser();
			DataPackage<ApplicationVO> packages = null;
			if (!user.isSuperAdmin()) {
				params.setParameter("s_owners.id", user.getId());
			} else {
				if (params.getParameterAsString("sm_name") == null
						|| params.getParameterAsString("sm_description") == null) {
					String _currpage = params.getParameterAsString("_currpage");
					String _pagelines = params
							.getParameterAsString("_pagelines");
					String realPath = params.getParameterAsString("realPath");
					params = new ParamsTable();
					params.setParameter("_currpage", _currpage);
					params.setParameter("_pagelines", _pagelines);
					params.setParameter("realPath", realPath);
				}
			}
			packages = process.doQuery(params, user);
			setDatas(packages);
			return SUCCESS;
		}catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}

	public String doNew() {
		String appid = null;
		String sortid = null;
		try {
			appid = Sequence.getSequence();
			sortid = Sequence.getTimeSequence();
		} catch (SequenceException e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		ApplicationVO appVO = new ApplicationVO();
		appVO.setId(appid);
		appVO.setSortId(sortid);
		appVO.setApplicationid(appid);
		setContent(appVO);
		return SUCCESS;
	}

	/**
	 * 创建一个应用将旧的的应用信息复制到此应用中
	 * 
	 * @return
	 */
	public String doCopyNew() {
		try {
			ParamsTable params = this.getParams();
			String appId = params.getParameterAsString("application");
			ApplicationVO oldapp = (ApplicationVO) ((ApplicationProcess) process)
					.doView(appId);
			ApplicationVO newapp = new ApplicationVO();
			PropertyUtils.copyProperties(newapp, oldapp);
			newapp.setId(Sequence.getSequence());
			newapp.setSortId(Sequence.getTimeSequence());
			String uniqueLog = "_copy";
			newapp.setName(oldapp.getName() + uniqueLog);
			newapp.setDescription(oldapp.getDescription() + uniqueLog);
			// 复制applicationid为oldapp.getId()的所有数据源
			copyDtsAndResetProperty(oldapp.getId(), newapp);
			this.setContent(newapp);
			return SUCCESS;
			// return doSave();
		} catch (OBPMValidateException e) {
			this.addFieldError("error", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 复制applicationid为oldappid的所有数据源
	 * 
	 * @param oldappid
	 * @param newapp
	 * @throws Exception
	 */
	protected void copyDtsAndResetProperty(String oldappid, ApplicationVO newapp)
			throws Exception {
		DataSourceProcess dp = (DataSourceProcess) ProcessFactory
				.createProcess(DataSourceProcess.class);
		DatasourceHelper dh = new DatasourceHelper();
		Iterator<DataSource> it_dts = dh.getDataSources(oldappid).iterator();
		Collection<ValueObject> col_newdts = new ArrayList<ValueObject>();
		String uniqueLog = "_copy";
		while (it_dts.hasNext()) {
			DataSource olddts = it_dts.next();
			DataSource newdts = new DataSource();
			PropertyUtils.copyProperties(newdts, olddts);
			newdts.setId(Sequence.getSequence());// 赋予一个id
			newdts.setSortId(Sequence.getTimeSequence());// 赋予一个sortid
			newdts.setApplicationid(newapp != null ? newapp.getId() : "");// 重新赋予一个applicationid
			newdts.setName(olddts.getName() + uniqueLog);// 重新命名
			// 同步新的应用的datasourceid
			if (!StringUtil.isBlank(olddts.getId())
					&& olddts.getId().equals(newapp.getDatasourceid()))
				newapp.setDatasourceid(newdts.getId());
			col_newdts.add(newdts);
		}
		dp.doUpdate(col_newdts);
	}


	/**
	 * 列出所有未加入的应用开发者
	 * 
	 * @SuppressWarnings setDatas方法接收了非ApplicationVO的集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String doListUnjoinedDeveloper() {
		try {
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			DataPackage datas = superUserProcess
					.getUnjoinedDeveloperList(getParams());

			setDatas(datas);
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 列出所有已加入的应用开发者
	 * 
	 * @SuppressWarnings setDatas方法接收了非ApplicationVO的集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String doListJoinedDeveloper() {
		try {
			this.validateQueryParams();
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			DataPackage datas = superUserProcess
					.getJoinedDeveloperList(getParams());
			setDatas(datas);
			return SUCCESS;
		}catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * getModuleTree
	 * 
	 * @throws ClassNotFoundException
	 */
	public String doModuleTree() throws ClassNotFoundException {
		ParamsTable params = getParams();
		ApplicationHelper helper = new ApplicationHelper();
		String applicationid = params.getParameterAsString("id");
		String parentid = params.getParameterAsString("parentid");
		if (applicationid != null && !"".equals(applicationid)) {
			Collection<Node> modTree = helper.getModuleTree(applicationid,
					parentid);
			if (modTree.size() > 0) {
				ResponseUtil.setJsonToResponse(ServletActionContext
						.getResponse(), JsonUtil.collection2Json(modTree));
			}
		}
		return SUCCESS;
	}

	/**
	 * 加入开发者
	 * 
	 * @return
	 */
	public String doAddDeveloper() {
		try {
			String[] selects = get_selects();
			if (selects != null && selects.length > 0) {
				((ApplicationProcess) process).addDevelopersToApplication(
						get_selects(), getId());
				this.addActionMessage("{*[cn.myapps.core.deploy.application.add_success]*}");
				return SUCCESS;
			} else {
				throw new OBPMValidateException("{*[core.domain.notChoose]*}");
			}

		}catch (OBPMValidateException e) {
			doListUnjoinedDeveloper();
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			doListUnjoinedDeveloper();
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * copy application
	 * 
	 * @return
	 */
	public String doCopy() {
		ParamsTable params = getParams();
		try {
			// new application id
			String application = params.getParameterAsString("content.id");
			// old application id
			String applicationid = params.getParameterAsString("application");
			CopyApplicationProcess process = (CopyApplicationProcess) ProcessFactory.createRuntimeProcess(CopyApplicationProcess.class, application);
			// process.copyComponent(applicationid);
			// process.copyDataSource(applicationid);
			// process.copyExcelConf(applicationid);
			// process.copyModule(applicationid);
			// process.copyMacrolibs(applicationid);
			// process.copyValidatelibs(applicationid);
			// process.copyPage(applicationid);
			// process.copyStatelabel(applicationid);
			// process.copyStylelibs(applicationid);
			// process.copyMenu(applicationid);
			// process.copyRole(applicationid);
			// process.copyHomepage(applicationid);
			// process.copyReminder(applicationid);
			process.copyAll(applicationid);
			this.addActionMessage("{*[Copy_Success]*}");
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}

	/**
	 * 移除开发者
	 * 
	 * @return
	 */
	public String doRemoveDeveloper() {
		try {
			String[] selects = get_selects();
			if (selects != null && selects.length > 0) {
				((ApplicationProcess) process).removeDevelopersFromApplication(
						get_selects(), getId());
				this.addActionMessage("{*[cn.myapps.core.deploy.application.remove_success]*}");
				return SUCCESS;
			} else {
				throw new OBPMValidateException("{*[core.domain.notChoose]*}");
			}
		}catch (OBPMValidateException e) {
			doListJoinedDeveloper();
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			doListJoinedDeveloper();
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public void set_type(String _type) throws Exception {
		if (_type != null) {
			ApplicationVO vo = (ApplicationVO) this.getContent();
			vo.setType(_type);
		}
	}

	public String get_type() throws Exception {
		ApplicationVO vo = (ApplicationVO) this.getContent();
		return vo != null ? vo.getType() : "";
	}

	public String get_dtsname() {
		String _dtsname = "";
		try {
			DataSourceProcess dp = (DataSourceProcess) ProcessFactory
					.createProcess(DataSourceProcess.class);
			ApplicationVO aVO = (ApplicationVO) this.getContent();
			if (aVO != null) {
				DataSource dts = (DataSource) dp.doView(aVO.getDatasourceid());
				if (dts != null)
					_dtsname = dts.getName();
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return _dtsname;
	}

	public String get_appName() {
		String appName = "";
		ApplicationVO cur_app = (ApplicationVO) getContent();
		ApplicationProcess ap;
		try {
			if (cur_app != null) {
				ap = (ApplicationProcess) ProcessFactory
						.createProcess(ApplicationProcess.class);
				ApplicationVO appvo = (ApplicationVO) ap
						.doView(cur_app.getId());
				if (appvo != null)
					appName = appvo.getName();
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return appName;
	}

	public String get_datasourceid() {
		String dtsid = "";
		ApplicationVO cur_app = (ApplicationVO) getContent();
		ApplicationProcess ap;
		try {
			if (cur_app != null) {
				ap = (ApplicationProcess) ProcessFactory
						.createProcess(ApplicationProcess.class);
				ApplicationVO appvo = (ApplicationVO) ap
						.doView(cur_app.getId());
				if (appvo != null)
					dtsid = appvo.getDatasourceid();
			}
		}  catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return dtsid;
	}

	public String doAddApp() throws Exception {
		ApplicationProcess process = (ApplicationProcess) ProcessFactory
				.createProcess(ApplicationProcess.class);

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
		
		String _currpage = params.getParameterAsString("_currpage");
		String domainId =  params.getParameterAsString("domain");
		
		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
		
		setDatas(process.doQueryUnBindApplicationsByDomain(params,domainId, page, lines));
		return SUCCESS;
	}

	public String getOverviewFile() {
		return overviewFile;
	}

	public void setOverviewFile(String overviewFile) {
		this.overviewFile = overviewFile;
	}

	public InputStream getOverview() {
		if (this.overviewFile != null) {
			final URL url = ApplicationAction.class.getClassLoader()
					.getResource(this.overviewFile);
			if (url != null) {
				try {
					return url.openStream();
				} catch (IOException e) {
					this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
					e.printStackTrace();
				}finally {
					new Thread(new Runnable() {
						public void run() {
							File file = new File(url.getFile());
							while (file.exists()) {
								if (file.delete()) {
									break;
								}
							}
						}
					}).start();
				}
			}
		}
		return null;
	}

	/**
	 * 创建概览
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doCreateOverview() {
		try {
			this.overviewFile = "appOverview" + System.currentTimeMillis()
					+ ".pdf";
			if (!StringUtil.isBlank(this.id)
					&& !StringUtil.isBlank(overviewFile)) {
				ApplicationUtil.createOverview(this.id, overviewFile);
			}
			return SUCCESS;
		}catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
}
