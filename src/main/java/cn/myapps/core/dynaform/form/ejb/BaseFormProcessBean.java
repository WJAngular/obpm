package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.dql.DQLASTUtil;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.dynaform.form.dao.FormDAO;
import cn.myapps.core.dynaform.form.ejb.mapping.TableMapping;
import cn.myapps.core.dynaform.summary.dao.SummaryCfgDAO;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.links.dao.LinkDAO;
import cn.myapps.core.links.ejb.LinkVO;
import cn.myapps.core.resource.dao.ResourceDAO;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.table.ddlutil.ChangeLog;
import cn.myapps.core.table.model.NeedConfirmException;
import cn.myapps.core.upload.ejb.UploadProcess;
import cn.myapps.core.upload.ejb.UploadVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.Debug;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

/**
 * 
 * @author Marky
 * 
 */
public abstract class BaseFormProcessBean<E> extends AbstractDesignTimeProcessBean<E> implements BaseFormProcess<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5735795428110465695L;
	public final static Logger log = Logger.getLogger(BaseFormProcessBean.class);
	private String reViewName = "";// 重命名已视图存在的视图名
	private int k=0;
	/**
	 * 创建一个表单值对象. 创建表单时，不能有重名的表单名.若有重名则抛出异常信息。
	 * 
	 * @throws ImpropriateException
	 * @see cn.myapps.core.dynaform.form.action.ImpropriateException#ImpropriateException(String)
	 * 
	 * @param vo
	 *            表单值对象
	 */
	public void doCreate(ValueObject vo) throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("s_name", ((Form) vo).getName());
		if (((Form) vo).getModule() != null && ((Form) vo).getModule().getId() != null
				&& !((Form) vo).getModule().getId().equals("")) {
			params.setParameter("s_module", ((Form) vo).getModule().getId());
		}
		params.setParameter("s_applicationid", ((Form) vo).getApplicationid());
		Collection<?> colls = this.doSimpleQuery(params);

		if (colls != null && colls.size() > 0) {
			// throw new ImpropriateException("Exist same name (" + ((Form)
			// vo).getName() + "),please choose another!");
			/**
			 * modifid by alex
			 */
			throw new OBPMValidateException("{*[Exist.same.name]*} (" + ((Form) vo).getName()
					+ "),{*[please.choose.another]*}!", new ImpropriateException("{*[Exist.same.name]*} (" + ((Form) vo).getName()
							+ "),{*[please.choose.another]*}!")) ;
		} else {
			FormDAO<E> dao = ((FormDAO<E>) getDAO());
			FormTableProcess tableProcess = new FormTableProcessBean(vo.getApplicationid());
			try {
				PersistenceUtils.beginTransaction();
				// tableProcess.beginTransaction();

				if (StringUtil.isBlank(vo.getId())) {
					vo.setId(Sequence.getSequence());
				}

				if (StringUtil.isBlank(vo.getSortId())) {
					vo.setSortId(Sequence.getTimeSequence());
				}

				tableProcess.createDynaTable((Form) vo);
				dao.create(vo);

				// tableProcess.commitTransaction();
				PersistenceUtils.commitTransaction();
			} catch (Exception e) {
				// tableProcess.rollbackTransaction();
				PersistenceUtils.rollbackTransaction();
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	public void doDocumentFormName(ValueObject obj) throws Exception {
		FormTableProcess tableProcess = null;
		try {
			Form form = (Form) doView(obj.getId());
			if (form != null) {
				tableProcess = new FormTableProcessBean(form.getApplicationid());
				DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,form.getApplicationid());

				tableProcess.beginTransaction();

				if (tableProcess.isDynaTableExists(form)) {
					try {
						if(!form.getName().equals(((Form)obj).getName())){
							docProcess.doChangeFormName(form); // 当表单名称发现改变时修改对应的业务表单数据的FormName字段值
						}
					} catch (Exception e) {
						log.warn("Remove form(" + form.getName() + ") data failded!", e);
					}
				}
				tableProcess.commitTransaction();
			}

		} catch (Exception e) {
			if (tableProcess != null) {
				tableProcess.rollbackTransaction();
			}
			throw e;
		}
	}

	public void doClearFormData(ValueObject obj) throws Exception {
		FormTableProcess tableProcess = null;
		try {
			Form form = (Form) doView(obj.getId());
			if (form != null) {
				tableProcess = new FormTableProcessBean(form.getApplicationid());
				DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,form.getApplicationid());

				tableProcess.beginTransaction();

				if (tableProcess.isDynaTableExists(form)) {
					try {
						docProcess.doRemoveByFormName(form); // 删除表单相关的所有数据
					} catch (Exception e) {
						log.warn("Remove form(" + form.getName() + ") data failded!", e);
					}
				}
				tableProcess.commitTransaction();
			}

		} catch (Exception e) {
			if (tableProcess != null) {
				tableProcess.rollbackTransaction();
			}
			throw e;
		}
	}

	public void doClearColumnData(Form form, String[] fields) throws Exception {
		try {
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,form.getApplicationid());
			docProcess.doRemoveDocByFields(form, fields);
		} catch (Exception e) {
			log.warn("Clear data for fields  failed!");
			throw e;
		}

	}

	public void doRemove(ValueObject obj) throws Exception {
		FormTableProcess tableProcess = null;
		try {
			if (obj != null) {
				Form form = (Form) obj;
				tableProcess = new FormTableProcessBean(form.getApplicationid());

				DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,form.getApplicationid());

				PersistenceUtils.beginTransaction();
				// tableProcess.beginTransaction();
				if (form != null) {
					if (tableProcess.isDynaTableExists(form)) {
						try {
							docProcess.doRemoveByFormName(form); // 删除表单相关的所有数据
						} catch (Exception e) {
							log.warn("Remove form(" + form.getName() + ") data failded!", e);
						}
					}

					tableProcess.dropDynaTable(form);
					getDAO().remove(form);
				}

				// tableProcess.commitTransaction();
				PersistenceUtils.commitTransaction();
			}
		} catch (Exception e) {
			if (tableProcess != null) {
				// tableProcess.rollbackTransaction();
			}
			PersistenceUtils.rollbackTransaction();
			throw e;
		}
	}

	/**
	 * 重载父类doRemove方法 删除表单,并删除关联的Link(链接)/Ressource(菜单)
	 */
	//@SuppressWarnings("unchecked")
	public void doRemove(String formId) throws Exception {
		try {
			PersistenceUtils.beginTransaction();// 启动事务
			
			IDesignTimeDAO<ResourceVO> resourceDAO = (ResourceDAO) DAOFactory.getDefaultDAO(ResourceVO.class.getName());
			// 查询链接所有关联菜单(Resource)
			Iterator<ResourceVO> it_Res = resourceDAO.getDatas("from ResourceVO lv where lv.actionContent='" + formId + "'")
					.iterator();
			while (it_Res.hasNext()) {// 迭代删除关联的菜单(Resource)
				ResourceVO res = it_Res.next();
				resourceDAO.remove(res);// 删除关联的菜单(Resource)
			}
			
			IDesignTimeDAO<LinkVO> linkDAO = (LinkDAO) DAOFactory.getDefaultDAO(LinkVO.class.getName());
			// 查询表单id(formId) = "formId" 的所有关联链接(Link)
			Iterator<LinkVO> it_Link = linkDAO.getDatas("from LinkVO lv where lv.actionContent like '" + formId + "'")
					.iterator();
			while (it_Link.hasNext()) {// 迭代删除所有关联的链接(Link)
				LinkVO link = it_Link.next();
				linkDAO.remove(link);// 删除当前的链接(Link)
			}
			
			IDesignTimeDAO<SummaryCfgVO> summaryDAO = (SummaryCfgDAO) DAOFactory.getDefaultDAO(SummaryCfgVO.class.getName());
			// 查询所有关联摘要(SummaryCfgVO)
			Iterator<SummaryCfgVO> it_summary = summaryDAO.getDatas("from SummaryCfgVO s where s.formId='" + formId + "'")
					.iterator();
			while (it_summary.hasNext()) {// 迭代删除所有关联摘要(SummaryCfgVO)
				summaryDAO.remove(it_summary.next());
			}

			// 删除该表单
			Form form = (Form) getDAO().find(formId);
			this.doRemove(form);
			PersistenceUtils.currentSession().flush();
			PersistenceUtils.commitTransaction();// 提交事务
		} catch (Exception e) {
			try {// 事务回滚
				PersistenceUtils.rollbackTransaction();
				throw e;
			} catch (Exception e1) {
				throw e1;
			}
		}
	}

	public void doRemove(String[] pks) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			if (pks != null) {
				for (int i = 0; i < pks.length; i++) {
					doRemove(pks[i]);
				}
			}
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			throw e;
		}
	}

	/**
	 * 创建表单值对象
	 * 
	 * @param vo
	 *            表单值对象
	 * @param user
	 *            用户
	 * 
	 */
	public void doCreate(ValueObject vo, WebUser user) throws Exception {
		Form formVO = (Form) vo;
		formVO.setId(Sequence.getSequence());
		String template = formVO.getTemplatecontext();
		// template =
		// template.replaceAll("\\[计算插入模板\\]</MARQUEE>|</IMAGE>|</IMG>","");
		template = template.replaceAll("</IMAGE>|</IMG>", "");
		formVO.setTemplatecontext(template);

		((FormDAO<E>) getDAO()).create(formVO, user);
	}

	/**
	 * 更新表单对象
	 * 
	 * @param vo
	 *            表单值对象
	 * @param user
	 *            用户
	 * 
	 */

	public void doUpdate(ValueObject vo, WebUser user) throws Exception {
		Form formVO = (Form) vo;
		// 若模版的名字修改,相应修改该站点下面的所有模板内容
		if (formVO.getType() == 2 || formVO.getType() == 3) {

			Form oldform = (Form) doView(formVO.getId());
			if (oldform != null && formVO.getName() != null && oldform.getName() != null
					&& !oldform.getName().equals(formVO.getName())) {
				Debug.println("Update FormName Starting------");
				changeFormName(oldform, formVO, vo.getApplicationid());
				Debug.println("Update FormName End------");
			}
		}
		String template = formVO.getTemplatecontext();
		// template =
		// template.replaceAll("\\[计算插入模板\\]</MARQUEE>|</IMAGE>|</IMG>","");
		template = template.replaceAll("</IMAGE>|</IMG>", "");
		formVO.setTemplatecontext(template);
		((FormDAO<E>) getDAO()).update(formVO, user);
	}

	/**
	 * 根据表单名以及应用标识查询查询,返回表单对象.
	 * 
	 * @param formName
	 *            表单名
	 * @param application
	 *            应用标识
	 * @return 表单对象
	 */
	public Form doViewByFormName(String formName, String application) throws Exception {
		return (Form) ((FormDAO<E>) getDAO()).findByFormName(formName, application);
	}

	/**
	 * 根据表单名以及应用标识查询查询,返回表单对象.
	 * 
	 * @param formName
	 *            表单名
	 * @param application
	 *            应用标识
	 * @return 表单对象
	 */
	public Form findFormByRelationName(String formName, String application) throws Exception {
		return (Form) ((FormDAO<E>) getDAO()).findFormByRelationName(formName, application);
	}

	/**
	 * 根据参数条件以及应用标识查询,返回相应字段集合.
	 * @SuppressWarnings getDAO方法不支持泛型
	 * @param params
	 *            参数表
	 * @param application
	 *            应用标识
	 * @return 相应字段集合.
	 */
	@SuppressWarnings("unchecked")
	public Collection<FormField> doGetFields(ParamsTable params, String application) throws Exception {

		Collection<FormField> rtn = new ArrayList<FormField>();
		params.setParameter("application", application);
		DataPackage<Form> result = ((FormDAO) getDAO()).query(params);
		// get all fields
		Iterator<Form> iter = result.datas.iterator();
		while (iter.hasNext()) {
			Form form = (Form) iter.next();
			rtn.addAll(form.getAllFields());
		}
		return rtn;
	}

	/**
	 * 检查字段名称合法性.
	 * 
	 * @param fieldname
	 *            字段名
	 * @return true or false
	 */
	/*
	 * private boolean checkFieldNames(String fieldname) throws Exception {
	 * String[] keyword = { "id", "taskid", "siteid", "channelid", "caption",
	 * "author", "source", "ispicture", "isfirst", "weight", "relationalwords",
	 * "keywords", "summary", "content", "formname", "created", "lastmodified",
	 * "readers", "writers", "owners", "owner", "flow", "state" }; String[]
	 * htmltag = { " ", "<", ">", "[", "]", "{", "}", "'", "\"" }; if
	 * (fieldname == null) return true;
	 * 
	 * for (int i = 0; i < keyword.length; ++i) if
	 * (fieldname.toLowerCase().trim().equals(keyword[i])) return false; for
	 * (int i = 0; i < htmltag.length; ++i) if (fieldname.indexOf(htmltag[i]) >=
	 * 0) return false; return true; }
	 */

	// 判断是否存在重复字段名称
	/**
	 * 判断是否存在重复字段名称
	 * 
	 * @param form
	 *            表单对象
	 * @return true or false
	 */
	public boolean haveDuplicateFieldNames(Form form) throws Exception {
		Collection<FormField> fields = form.getFields();
		if (fields != null) {
			Iterator<FormField> iterator = fields.iterator();
			while (iterator.hasNext()) {
				FormField field = (FormField) iterator.next();
				if (field != null) {
					if (checkFieldCount(form, field.getName()) > 1)
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检查表单重名字段数
	 * 
	 * @param form
	 *            表单对象
	 * @param fieldName
	 *            字段
	 * @return 重名字段数
	 * @throws Exception
	 */
	private int checkFieldCount(Form form, String fieldName) throws Exception {
		int count = 0;
		Collection<FormField> fields = form.getFields();
		if (fields != null) {
			Iterator<FormField> iterator = fields.iterator();
			while (iterator.hasNext()) {
				FormField field = (FormField) iterator.next();
				if (field != null) {
					if (field.getName().trim().equals(fieldName))
						count++;
				}
			}
		}
		return count;
	}

	/**
	 * 检查是否有重名表单
	 * 
	 * @param form
	 *            表单对象
	 * @param application
	 *            应用标识
	 */
	/*
	 * private boolean checkDuplicateName(Form form, String application) throws
	 * Exception { ParamsTable params = new ParamsTable();
	 * params.setParameter("s_name", form.getName()); //
	 * params.setParameter("l_siteid", String.valueOf(form.getSiteid()));
	 * params.setParameter("xl_id", String.valueOf(form.getId()));
	 * 
	 * DataPackage<?> forms = ((FormDAO) getDAO()).query(params); if (forms !=
	 * null) return forms.datas.size() <= 0; else return true; }
	 */
	/**
	 * 更改表单名. 若模版的名字修改,相应修改该站点下面的所有模板内容.
	 * @SuppressWarnings getDAO方法不支持泛型
	 * @param oldform
	 *            旧表单对象
	 * @param newform
	 *            新表单对象
	 * @param application
	 *            应用标识
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void changeFormName(Form oldform, Form newform, String application) throws Exception {

		ParamsTable params = new ParamsTable();
		// params.setParameter("n_siteid", String.valueOf(newform.getSiteid()));
		DataPackage<Form> datas = ((FormDAO) getDAO()).query(params);

		if (datas != null) {
			Iterator<Form> forms = datas.getDatas().iterator();
			while (forms.hasNext()) {
				Form form = (Form) forms.next();
				String content = form.getTemplatecontext();
				if (content != null) {
					String regex = "/" + oldform.getName() + ".html2";
					String replacement = "/" + newform.getName() + ".html2";
					Debug.println("---->" + regex + " replace to " + replacement);
					content = content.replaceAll(regex, replacement);
					form.setTemplatecontext(content);
				}
				((FormDAO) getDAO()).update(form);
			}
		}
	}

	/**
	 * 根据应用标识查询,返回所有表单集合.
	 * 
	 * @application 应用标识
	 * @return 所有表单集合.
	 */
	public Collection<E> get_formList(String application) throws Exception {
		return this.doSimpleQuery(null, application);
		// return ((FormDAO) getDAO()).simpleQuery(null, application);
	}

	/**
	 * 根据所属模块以及应用标识查询,返回相应表单集合.
	 * 
	 * @param application
	 *            应用标识
	 * @param 所属模块主键
	 * @return 表单集合.
	 * @throws Exception
	 */
	public Collection<E> getFormsByModule(String moduleid, String application) throws Exception {
		return ((FormDAO<E>) getDAO()).getFormsByModule(moduleid, application);
	}
	
	/**
	 * 根据所属Module 返回普通表单集合
	 * 
	 * @param moduleid
	 *            模块主键
	 * @param application
	 *            应用标识
	 * @return 普通表单集合
	 * @throws Exception
	 */
	public Collection<E> getRelatedFormsByModule(String moduleid, String application) throws Exception {
		return ((FormDAO<E>) getDAO()).getRelatedFormsByModule(moduleid, application);
	}

	/**
	 * 根据应用标识查询,返回相应查询表单集合.
	 * 
	 * @param application
	 *            应用标识
	 * @return 查询集合.
	 * @throws Exception
	 */
	public Collection<E> getSearchFormsByApplication(String appid, String application) throws Exception {
		return ((FormDAO<E>) getDAO()).getSearchFormsByApplication(appid, application);
	}

	/**
	 * 此方法不支持泛型
	 */
	@SuppressWarnings("unchecked")
	protected IDesignTimeDAO<E> getDAO() throws Exception {
		return (IDesignTimeDAO<E>) DAOFactory.getDefaultDAO(Form.class.getName());
	}

	/**
	 * 根据所属Module以及应用标识查询,返回查询表单集合.
	 * 
	 * @param moduleid
	 *            模块主键
	 * @param application
	 *            应用标识
	 * @return Search Form 集合.
	 * @throws Exception
	 */
	public Collection<E> getSearchFormsByModule(String moduleid, String application) throws Exception {
		return ((FormDAO<E>) getDAO()).getSearchFormsByModule(moduleid, application);
	}

	/**
	 * 更新表单值对象. 更新表单值对象时，若存在表单版本不一致时，提示"Already having been impropriate by
	 * others , can not Save"。 否则将相应的版本号加上1。
	 * 
	 * 
	 * 
	 * @param vo
	 *            值对象
	 */
	public void doUpdate(ValueObject vo) throws Exception {
		FormTableProcessBean tableProcess = new FormTableProcessBean(vo.getApplicationid());
		PersistenceUtils.beginTransaction();
		tableProcess.beginTransaction();
		try {
			Form newForm = (Form) vo;
			Form oldForm = (Form) getDAO().find(vo.getId());
			// if (oldForm != null && newForm.getVersion() !=
			// oldForm.getVersion())
			// throw new ImpropriateException("{*[core.util.cannotsave]*}");
			Collection<Form> all = new ArrayList<Form>();
			all.add(newForm);
			all.addAll(getSuperiors(newForm));

			updateAllDynaTables(tableProcess, all);

			newForm.setVersion(((Form) vo).getVersion() + 1);
			if (oldForm != null) {
				PropertyUtils.copyProperties(oldForm, newForm);
				getDAO().update(oldForm);
			} else {
				getDAO().update(newForm);
			}
			tableProcess.commitTransaction();
			PersistenceUtils.commitTransaction();
		} catch (ImpropriateException e) {
			tableProcess.rollbackTransaction();
			PersistenceUtils.rollbackTransaction();
			throw e;
		} catch (Exception e) {
			tableProcess.rollbackTransaction();
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}

	public void updateAllDynaTables(FormTableProcessBean tableProcess, Collection<Form> newForms) throws Exception {
		try {
			for (Iterator<Form> iter = newForms.iterator(); iter.hasNext();) {
				Form newForm = (Form) iter.next();
				Form oldForm = (Form) getDAO().find(newForm.getId());
				tableProcess.createOrUpdateDynaTable(newForm, oldForm);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Collection<Form> getSuperiors(Form form) throws Exception {
		Collection<Form> superiors = new ArrayList<Form>();
		if (form.getModule() == null) {
			return superiors;
		}

		Collection<E> forms = getFormsByModule(form.getModule().getId(), form.getApplicationid());
		for (Iterator<E> iter = forms.iterator(); iter.hasNext();) {
			Form superior = (Form) iter.next();
			if (superior.isContain(form)) {
				Form cloneSuperior = (Form) superior.clone();
				cloneSuperior.addSubForm(form);
				superiors.add(cloneSuperior);
				superiors.addAll(getSuperiors(cloneSuperior));
			}
		}
		return superiors;
	}

	/**
	 * 根据参数条件以及应用标识查询,返回表单(Form)的DataPackage.
	 * <p>
	 * DataPackage为一个封装类，此类封装了所得到的Form数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * 
	 * @param params
	 *            参数表
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @application 应用标识
	 * @return 表单数据集
	 */
	public DataPackage<E> doFormList(ParamsTable params, String application) throws Exception {
		return ((FormDAO<E>) getDAO()).queryForm(params, application);
	}

	/**
	 * 在保存或更新时对Form的改变进行校验
	 * 
	 * @param newForm
	 *            发生改变后的表单
	 * @throws NeedConfirmException
	 *             部分改变需要用户确认的异常
	 */
	public void doChangeValidate(Form newForm) throws Exception {
		try {
			FormTableProcessBean process = new FormTableProcessBean(newForm.getApplicationid());
			if (process.isHasDynaTable(newForm)) {
				process.doChangeValidate(getChangeLog(newForm));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private ChangeLog getChangeLog(Form newForm) throws Exception {
		Form oldForm = (Form) doView(newForm.getId());
		ChangeLog log = new ChangeLog();
		log.compare(newForm, oldForm);
		return log;
	}

	public boolean checkRelationName(String formid, String relationName) throws Exception {
		try {
			Form form = ((FormDAO<E>) getDAO()).findFormByRelationName(relationName, null);
			if (form == null || (form != null && form.getId() != null && form.getId().equals(formid)))
				return true;
		} catch (Exception e) {
			throw e;
		}
		return false;

	}

	public void syncMappingDatas(Form form) throws Exception {

		ApplicationProcess appPorcess = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
		ApplicationVO application = (ApplicationVO) appPorcess.doView(form.getApplicationid());
		Collection<DomainVO> domains = application.getDomains();

		DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,form.getApplicationid());
		TableMapping tableMapping = form.getTableMapping();

		SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);

		String sql0 = "SELECT *,  '" + form.getId() + "' FORMID FROM " + tableMapping.getTableName();
		Collection<Document> datas = docProcess.queryBySQL(sql0);

		// 1. 获取所有引用的域
		for (Iterator<DomainVO> iterator = domains.iterator(); iterator.hasNext();) {
			DomainVO domain = (DomainVO) iterator.next();

			if (datas != null && !datas.isEmpty()) {
				for (Iterator<Document> iterator2 = datas.iterator(); iterator2.hasNext();) {
					Document doc = (Document) iterator2.next();
					String sql1 = "SELECT doc.* FROM " + getDAO().getSchema() + DQLASTUtil._TBNAME + " doc";
					sql1 += " WHERE doc.MAPPINGID='" + doc.getItemValueAsString(TableMapping.MAPPINGID) + "'";

					Document docHead = docProcess.findBySQL(sql1, domain.getId());

					if (docHead == null || StringUtil.isBlank(docHead.getId())) {
						WebUser admin = new WebUser(superUserProcess.getDefaultAdmin());
						//admin.setApplicationid(form.getApplicationid());
						admin.setDomainid(domain.getId());

						Document newDoc = form.createDocument(new ParamsTable(), admin);
						newDoc.setIstmp(false);
						newDoc.setItems(doc.getItems());

						docProcess.doCreate(newDoc);
					}
				}
			}
		}
	}

	/**
	 * 根据表单编号来生成视图
	 * 
	 * @param formid
	 *            表单编号
	 * @throws Exception
	 */


	public Form oneKeyCreateView(String formid) throws Exception {
		FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		ViewProcess viewPross = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);

		Form form = (Form) formPross.doView(formid);// 获得form
		//int k = 0;// 递增达到重命名的效果
		reViewName = form.getName();
		// 调用重命名方法
		
		
		String str = "";
		

		try {
			if (viewPross.get_existViewByNameModule(form.getName(), form.getModule().getId())) {
				k++;
				str = "_" + k;
				reViewName=reViewName + str;
			} else {
				if (k != 0) {
					k=0;
					str = "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Collection<FormField> formfield = form.getValueStoreFields();// 获得form存储值的field

		// 新建视图
		View view = new View();
		if (view.getId() == null || view.getId().trim().length() <= 0) {
			view.setId(Sequence.getSequence());
			view.setSortId(Sequence.getTimeSequence());
		}
		view.setName(reViewName);
		view.setOpenType(View.OPEN_TYPE_NORMAL);
		view.setLastmodifytime(new Date());
		view.setApplicationid(form.getApplicationid());
		view.setModule(form.getModule());
		view.setPagelines("10");
		view.setShowTotalRow(true);
		view.setPagination(true);
		view.setRelatedForm(form.getId());

		// 将表单中对应有值的列转换为视图的列
		int i = 0;
		for (Iterator<FormField> iterator = formfield.iterator(); iterator.hasNext();) {
			FormField field = (FormField) iterator.next();

			//过滤掉不需要生成控件
			if(field instanceof IncludeField
					|| (field instanceof HTMLEditorField) 
					|| (field instanceof TabField) 
					|| (field instanceof FlowHistoryField)
					|| (field instanceof FileManagerField)
					|| (field instanceof SurveyField)
					|| (field instanceof ImageUploadToDataBaseField)
					|| (field instanceof ViewDialogField)
					|| (field instanceof ReminderField)
					|| (field instanceof QRCodeField))
				continue;
			
			//将需要生成的控件添加到视图列
			Column column = new Column();
			if (column.getId() == null || column.getId().trim().length() <= 0) {
				column.setId(Sequence.getSequence());
				column.setOrderno(i);
			}
			if (field.getDiscript() != null && !field.getDiscript().equals("")) {
				column.setName(field.getDiscript());
			} else {
				column.setName(field.getName());
			}
			column.setFormid(form.getId());
			column.setApplicationid(form.getApplicationid());
			column.setFieldName(field.getName());
			column.setParentView(view.getId());
			column.setIsOrderByField("false");
			column.setShowType("01");	//默认'值类型'为'显示值'
			view.getColumns().add(column);
			i++;
		}

		// 分别创建两个按钮 新建，删除
		Activity activityCreate = new Activity();
		if (activityCreate.getId() == null || activityCreate.getId().trim().length() <= 0) {
			activityCreate.setId(Sequence.getSequence());
			activityCreate.setOrderno(0);
		}
		activityCreate.setApplicationid(form.getApplicationid());
		activityCreate.setName("新建");
		activityCreate.setParentView(view.getId());
		activityCreate.setType(ActivityType.DOCUMENT_CREATE);
		activityCreate.setOnActionForm(form.getId());

		view.getActivitys().add(activityCreate);

		Activity activityDelete = new Activity();
		if (activityDelete.getId() == null || activityDelete.getId().trim().length() <= 0) {
			activityDelete.setId(Sequence.getSequence());
			activityDelete.setOrderno(1);
		}
		activityDelete.setApplicationid(form.getApplicationid());
		activityDelete.setName("删除");
		activityDelete.setParentView(view.getId());
		activityDelete.setType(ActivityType.DOCUMENT_DELETE);

		view.getActivitys().add(activityDelete);

		viewPross.doCreate(view);
		return form;
	}

	/**
	 * 同步映射表单数据
	 * @param params
	 * @param user
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public String doSynchronouslyData(ParamsTable params, WebUser user, Form form) throws Exception {
		DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,form.getApplicationid());
		return process.doSynchronouslyMappingFormData(params, user, form);
	}
	
	public Collection<Form> getNormalFormsByModule(String moduleid,
			String application) throws Exception {
		return ((FormDAO<Form>) getDAO()).queryNormalFormsByModule(moduleid, application);
	}
}
