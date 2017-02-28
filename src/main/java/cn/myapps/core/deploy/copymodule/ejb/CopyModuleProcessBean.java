package cn.myapps.core.deploy.copymodule.ejb;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.counter.ejb.CounterProcess;
import cn.myapps.core.counter.ejb.CounterProcessBean;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.util.ElementResplaceUtil;
import cn.myapps.util.ObjectUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class CopyModuleProcessBean extends AbstractDesignTimeProcessBean<CopyModuleVO> implements CopyModuleProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1350889920466703426L;

	public CopyModuleProcessBean() throws Exception {
		setProcess();
	}

	ModuleProcess moduleProcess;

	FormProcess formProcess;

	ViewProcess viewProcess;

	BillDefiProcess billDefiProcess;

	private ResourceProcess resourceProcess;

	private HashMap<String, String> formMap;

	private HashMap<String, String> viewMap;

	private HashMap<String, String> flowMap;

	HashMap<String, String> moduleMap;

	public void setProcess() throws Exception {
		moduleProcess = (ModuleProcess) ProcessFactory.createProcess(ModuleProcess.class);
		formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		billDefiProcess = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		resourceProcess = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);

	}

	public CounterProcess getCountProcess(String application) throws Exception {
		return new CounterProcessBean(application);
	}

	/**
	 * @SuppressWarnings IDesignTimeDAO<?>到IDesignTimeDAO<CopyModuleVO>的转型
	 */
	@SuppressWarnings("unchecked")
	protected IDesignTimeDAO<CopyModuleVO> getDAO() throws Exception {
		return (IDesignTimeDAO<CopyModuleVO>) DAOFactory.getDefaultDAO(CopyModuleVO.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.core.copymodule.ejb.CopyModuleProcess#CopyForm(cn.myapps.core
	 *      .copymodule.ejb.CopyModuleVO, java.lang.String,
	 *      cn.myapps.base.dao.ValueObject)
	 */
	public Form copyForm(CopyModuleVO vo, String moduleId, String oldFormId, String newFormid) throws Exception {
		Form form = new Form();
		ModuleVO module = getModuleById(moduleId);
		ModuleVO newmodule = getModuleById(vo.getModuleId());
		Collection<Form> formList = formProcess.getFormsByModule(moduleId, vo.getApplicationid());
		if (formList != null && formList.size() > 0) {
			for (Iterator<Form> iterator = formList.iterator(); iterator.hasNext();) {
				Form formVo = iterator.next();
				if (oldFormId != null) {
					Form newForm = (Form) formProcess.doView(oldFormId);
					if (formVo.equals(newForm)) {
						form.setApplicationid(vo.getApplicationid());
						form.setId(newFormid);
						if (newmodule == null) {
							form.setModule(module);
						} else {
							form.setModule(newmodule);
						}
						form.setActivitys(formVo.getActivitys());
						form.setActivityXML(formVo.getActivityXML());
						int count = getCount("FORM_" + formVo.getName(), form.getApplicationid(), form.getDomainid());
						String name = "copy" + count;
						form.setName(name + formVo.getName());
						form.setDomainid(formVo.getDomainid());
						form.setLastmodifier(formVo.getLastmodifier());
						form.setTemplatecontext(formVo.getTemplatecontext());
						form.setShowLog(formVo.isShowLog());
						form.setType(formVo.getType());
						form.setStyle(formVo.getStyle());
						form.setVersion(formVo.getVersion());
						formProcess.doCreate(form);
					}
				}
			}
		}
		return form;
	}

	public ModuleVO copyModule(CopyModuleVO vo, String oldModuleId, String newModuldeId, String superior)
			throws Exception {
		ModuleVO modulevo = new ModuleVO();
		ModuleVO moduleCo = getModuleById(oldModuleId);
		if (vo.getModuleId() != null) {
			modulevo.setId(newModuldeId);
			modulevo.setApplicationid(moduleCo.getApplicationid());
			modulevo.setApplication(moduleCo.getApplication());
			if (vo.getModulename() != null) {
				modulevo.setName(vo.getModulename());
			} else {
				int count = getCount("MODULE_" + modulevo.getName(), modulevo.getApplicationid(), modulevo.getDomainid());
				String name = "copy" + count;
				modulevo.setName(name + moduleCo.getName());
			}
			if (vo.getDescription() != null) {
				modulevo.setDescription(vo.getDescription());
			} else {
				modulevo.setDescription(moduleCo.getDescription());
			}
			ModuleVO superiorId = (ModuleVO) moduleProcess.doView(superior);
			modulevo.setSuperior(superiorId);
			modulevo.setSortId(moduleCo.getSortId());
			moduleProcess.doCreate(modulevo);
		}
		return modulevo;
	}

	/**
	 * select moduleId
	 * 
	 * @param pk
	 * @return
	 * @throws Exception
	 */
	public ModuleVO getModuleById(String pk) throws Exception {
		return (ModuleVO) moduleProcess.doView(pk);
	}

	/**
	 * 复制模块(包括模块所有子元素)
	 * 
	 * @param moduleId
	 *            旧模块ID
	 * @param newModuleId
	 *            新模块ID
	 * @param superiorId
	 *            上级模块ID
	 * @return 复制后的模块
	 * @throws Exception
	 */
	public ModuleVO copyModuleALL(String moduleId, String newModuleId, String superiorId) throws Exception {
		moduleMap = new HashMap<String, String>();
		if (!StringUtil.isBlank(moduleId)) {
			ModuleVO copiedModule = (ModuleVO) moduleProcess.doView(moduleId);
			if (copiedModule != null) {
				ModuleVO vo = new ModuleVO();
				if (StringUtil.isBlank(newModuleId)) {
					newModuleId = Sequence.getSequence();
				}
				vo.setId(newModuleId);
				moduleMap.put(copiedModule.getId(), vo.getId());
				vo.setApplication(copiedModule.getApplication());
				vo.setDescription(copiedModule.getDescription());
				vo.setLastmodifytime(copiedModule.getLastmodifytime());
				vo.setSortId(copiedModule.getSortId());
				vo.setApplicationid(copiedModule.getApplicationid());
				ModuleVO superior = (ModuleVO) moduleProcess.doView(superiorId);
				if (superior != null) {
					vo.setSuperior(superior);
				} else {
					vo.setSuperior(copiedModule.getSuperior());
				}
				int count = getCount("MODULE_" + copiedModule.getName(), copiedModule.getApplicationid(), copiedModule
						.getDomainid());
				String name = "copy" + count;
				vo.setName(copiedModule.getName() + name);
				moduleProcess.doCreate(vo);
				CopyAllThread copyThread = new CopyAllThread(copiedModule, vo, superiorId);
				new Thread(copyThread).start();

				return vo;
			}

		}
		return null;
	}

	public ModuleVO copyModuleALL(String moduleId, String superiorId) throws Exception {
		return copyModuleALL(moduleId, null, superiorId);
	}

	public ModuleVO copyModuleALL(String moduleid) throws Exception {
		return copyModuleALL(moduleid, null);
	}

	public void replaceAll(String moduleid, String application) throws Exception {
		ElementResplaceUtil resplace = new ElementResplaceUtil(formMap, viewMap, flowMap, moduleMap,
				new HashMap<String, String>(), null);
		resplace.resplace(application);
		// this.replaceForm(formMap, viewMap, flowMap, moduleMap, application);
		// this.replaceView(formMap, viewMap, application);
	}

	/**
	 * 复制本模块下的表单
	 * 
	 * @param formList
	 * @param vo
	 * @throws Exception
	 */
	public void copyAllForm(Collection<Form> formList, ModuleVO vo) throws Exception {
		formMap = new HashMap<String, String>();
		if (formList != null && formList.size() > 0) {
			for (Iterator<Form> iterator2 = formList.iterator(); iterator2.hasNext();) {
				Form form = iterator2.next();
				Form form_v = new Form();
				form_v.setModule(vo);
				String formid = Sequence.getSequence();
				form_v.setId(formid);
				formMap.put(form.getId(), formid);
				form_v.setActivityXML(form.getActivityXML());
				int count = getCount("FORM_" + form.getName(), form.getApplicationid(), form.getDomainid());
				String name = "copy" + count;
				form_v.setName(form.getName() + "_" + name);
				form_v.setDomainid(form.getDomainid());
				form_v.setLastmodifier(form.getLastmodifier());
				form_v.setTemplatecontext(form.getTemplatecontext());
				form_v.setShowLog(form.isShowLog());
				form_v.setType(form.getType());
				form_v.setStyle(form.getStyle());
				form_v.setVersion(form.getVersion());
				form_v.setApplicationid(form.getApplicationid());
				formProcess.doCreate(form_v);
			}
		}
	}

	/**
	 * 复制本模块下的视图
	 * 
	 * @param viewList
	 * @param vo
	 * @throws Exception
	 */
	public void copyAllView(Collection<View> viewList, ModuleVO vo) throws Exception {
		viewMap = new HashMap<String, String>();
		if (viewList != null && viewList.size() > 0) {
			// 顶级resource
			//复制模块不需要复制菜单，since 2.5sp7磐石版
			//ResourceVO reSuperior = createRsourceByModule(vo);
			for (Iterator<View> iterator2 = viewList.iterator(); iterator2.hasNext();) {
				View view = iterator2.next();
				View view_v = new View();
				ObjectUtil.copyProperties(view_v, view);
				int count = getCount("VIEW_" + view.getName(), view.getApplicationid(), view.getDomainid());
				String name = "copy" + count;
				view_v.setName(view.getName() + "_" + name);
				String viewid = Sequence.getSequence();
				view_v.setId(viewid);
				viewMap.put(view.getId(), view_v.getId());
				view_v.setModule(vo);
				// 替换view对应的resource
				//ResourceVO resourcevo = copyResource(view_v, reSuperior);
				//if (resourcevo != null) {
				//	view_v.setRelatedResourceid(resourcevo.getId());
				//}
				viewProcess.doCreate(view_v);
			}
		}
	}

	public void copyAllFlow(Collection<BillDefiVO> flowList, ModuleVO vo) throws Exception {
		flowMap = new HashMap<String, String>();
		if (flowList != null && flowList.size() > 0) {
			for (Iterator<BillDefiVO> iterator2 = flowList.iterator(); iterator2.hasNext();) {
				BillDefiVO billDefiVO = iterator2.next();
				BillDefiVO bill = new BillDefiVO();
				ObjectUtil.copyProperties(bill, billDefiVO);
				String flowid = Sequence.getSequence();
				bill.setId(flowid);
				bill.setModule(vo);
				int count = getCount("FLOW_" + bill.getSubject(), bill.getApplicationid(), bill.getDomainid());
				String name = "copy" + count;
				bill.setSubject(billDefiVO.getSubject() + "_" + name);
				billDefiProcess.doCreate(bill);
				flowMap.put(billDefiVO.getId(), flowid);
			}
		}
	}

	public View copyView(CopyModuleVO vo, String moduleId, String oldViewId, String newViewid) throws Exception {
		Collection<View> viewList = null;
		View view = new View();
		if (vo != null && moduleId != null) {
			ModuleVO module = getModuleById(moduleId);
			ModuleVO newmodule = getModuleById(vo.getModuleId());
			viewList = viewProcess.getViewsByModule(moduleId, vo.getApplicationid());
			for (Iterator<View> iterator = viewList.iterator(); iterator.hasNext();) {
				View viewVO = (View) iterator.next();
				View newView = (View) viewProcess.doView(oldViewId);
				if (newView.equals(viewVO)) {
					ObjectUtil.copyProperties(view, viewVO);
					view.setId(newViewid);
					view.setApplicationid(viewVO.getApplicationid());
					// 设置子元素(Activity)
					view.setActivityXML(viewVO.getActivityXML());
					// 设置子元素(Column)
					view.setColumnXML(viewVO.getColumnXML());

					int count = getCount("VIEW_" + view.getName(), view.getApplicationid(), view.getDomainid());
					String name = "copy" + count;
					view.setName(viewVO.getName() + name);

					//复制模块不需要复制菜单，since 2.5sp7磐石版
					//ResourceVO reSuperior = createRsourceByModule(view.getModule());
					//ResourceVO resvo = copyResource(view, reSuperior);
					view.setSearchForm(viewVO.getSearchForm());
					//if (resvo != null) {
					//	view.setRelatedResourceid(resvo.getId());
					//}
					view.setDescription(viewVO.getDescription());
					if (newmodule != null) {
						view.setModule(newmodule);
					} else {
						view.setModule(module);
					}
					viewProcess.doCreate(view);
				}
			}
		}
		return view;
	}

	public BillDefiVO copyFlow(CopyModuleVO vo, String moduleId, BillDefiVO newBillDefiVO, String _flowid) throws Exception {
		Collection<BillDefiVO> billDefiVOList = null;
		BillDefiVO bill = new BillDefiVO();
		if (vo != null && moduleId != null) {
			billDefiVOList = billDefiProcess.getBillDefiByModule(moduleId);
			ModuleVO newmodule = getModuleById(vo.getModuleId());
			ModuleVO module = getModuleById(moduleId);
			for (Iterator<BillDefiVO> iterator = billDefiVOList.iterator(); iterator.hasNext();) {
				BillDefiVO billDefiVO = iterator.next();
				if (billDefiVO.equals(newBillDefiVO)) {
					ObjectUtil.copyProperties(bill, newBillDefiVO);
					bill.setId(_flowid);
					bill.setApplicationid(newBillDefiVO.getApplicationid());
					bill.setFlow(billDefiVO.getFlow());
					if (newmodule == null) {
						bill.setModule(module);
					} else {
						bill.setModule(newmodule);
					}
					int count = getCount("FLOW_" + bill.getSubject(), bill.getApplicationid(), bill.getDomainid());
					String name = "copy" + count;
					bill.setSubject(name + newBillDefiVO.getSubject());
					viewProcess.doCreate(bill);
				}
			}
		}
		return bill;
	}

	/**
	 * 复制菜单
	 * 
	 * @param view
	 * @throws Exception
	 */
	public ResourceVO copyResource(View view, ResourceVO superior) throws Exception {
		try {
			if (!StringUtil.isBlank(view.getRelatedResourceid())) {
				ResourceVO resourcevo = (ResourceVO) resourceProcess.doView(view.getRelatedResourceid());
				if (resourcevo != null) {
					ResourceVO resvo = new ResourceVO();
					ObjectUtil.copyProperties(resvo, resourcevo);
					//resvo.setActionurl(resourcevo.getActionurl());
					//resvo.setApplication(resourcevo.getApplication());
					//resvo.setDisplayView(resourcevo.getDisplayView());
					resvo.setDomainid(resourcevo.getDomainid());
					resvo.setSuperior(superior);
					resvo.setModuleid(view.getModule().getId());
					resvo.setActionContent(view.getId());
					//resvo.setModule(view.getModule().getId());
					//resvo.setDisplayView(view.getId());
					String resourceid = Sequence.getSequence();
					resvo.setId(resourceid);
					//resvo.setRelatedPermissions(new HashSet<PermissionVO>());
					resvo.setDescription(view.getName());
					resourceProcess.doCreate(resvo);
					return resvo;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return null;

	}

	/**
	 * 获得count的下一个数字
	 * 
	 * @param name
	 *            名称
	 * @param applicationid
	 * 	软件id
	 * @param domainid
	 * 	企业域id
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getCount(String name, String applicationid, String domainid) throws Exception {
		return getCountProcess(applicationid).getNextValue(name, applicationid, domainid);
	}

	// create top resource
	public ResourceVO createRsourceByModule(ModuleVO module) throws Exception {
		ResourceVO rs = new ResourceVO();
		rs.setId(Sequence.getSequence());
		rs.setDescription(module.getName());
		rs.setApplication(module.getApplicationid());
		rs.setApplicationid(module.getApplicationid());
		//rs.setIsview(ResourceVO.ACCESS_CONTRAL_PRIVATE); // 默认为私有
		rs.setType("00");
		resourceProcess.doCreate(rs);
		return rs;
	}

	class CopyAllThread implements Runnable {
		ModuleVO oldModule;// old module

		ModuleVO newModule;// new module

		public CopyAllThread(ModuleVO oldModule, ModuleVO newModule, String superiorId) {
			this.oldModule = oldModule;
			this.newModule = newModule;
		}

		public void run() {
			try {
				if (oldModule != null && newModule != null) {
					// 复制Form
					Collection<Form> formList = formProcess.getFormsByModule(oldModule.getId(), oldModule
							.getApplicationid());
					copyAllForm(formList, newModule);
					// 复制view
					Collection<View> viewList = viewProcess.getViewsByModule(oldModule.getId(), oldModule
							.getApplicationid());
					copyAllView(viewList, newModule);
					// 复制流程
					Collection<BillDefiVO> flowList = billDefiProcess.getBillDefiByModule(oldModule.getId());
					copyAllFlow(flowList, newModule);
					replaceAll(newModule.getId(), oldModule.getApplicationid());
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					moduleProcess.deleteModule(newModule.getId(), newModule.getApplicationid());
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					Thread.interrupted(); // 中断线程
				}
			}
		}
	}
}
