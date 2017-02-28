package cn.myapps.core.expimp.exp.ejb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.constans.Environment;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigProcess;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigVO;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.FormProcessBean;
import cn.myapps.core.dynaform.printer.ejb.Printer;
import cn.myapps.core.dynaform.printer.ejb.PrinterProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.expimp.ExpImpElements;
import cn.myapps.core.macro.repository.ejb.RepositoryProcess;
import cn.myapps.core.macro.repository.ejb.RepositoryVO;
import cn.myapps.core.multilanguage.ejb.MultiLanguage;
import cn.myapps.core.multilanguage.ejb.MultiLanguageProcess;
import cn.myapps.core.page.ejb.Page;
import cn.myapps.core.page.ejb.PageProcess;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.privilege.res.ejb.ResProcess;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportProcess;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.style.repository.ejb.StyleRepositoryProcess;
import cn.myapps.core.style.repository.ejb.StyleRepositoryVO;
import cn.myapps.core.task.ejb.Task;
import cn.myapps.core.task.ejb.TaskProcess;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.user.ejb.UserDefinedProcess;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryProcess;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryVO;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.core.widget.ejb.PageWidgetProcess;
import cn.myapps.core.workflow.statelabel.ejb.StateLabel;
import cn.myapps.core.workflow.statelabel.ejb.StateLabelProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.file.ZipUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.xml.XmlUtil;

public class ExpProcessBean extends AbstractDesignTimeProcessBean<ExpSelect>
		implements ExpProcess {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4118898639932846216L;

	final static Logger LOG = Logger.getLogger(ExpProcessBean.class);

	ApplicationProcess applicationProcess;
	ModuleProcess moduleProcess;
	// -软件类
	private RoleProcess roleProcess;
	private PageProcess pageProcess;
	private UserDefinedProcess homePageProcess;
	private ResourceProcess resourceProcess;
	private TaskProcess taskProcess;
	private RepositoryProcess repositoryProcess;
	private StyleRepositoryProcess styleRepositoryProcess;
	private ValidateRepositoryProcess validateRepositoryProcess;
	private IMPMappingConfigProcess excelMappingConfigProcess;
	private SummaryCfgProcess reminderProcess;
	private DataSourceProcess dataSourceProcess;
	private MultiLanguageProcess multiLanguageProcess;
	private StateLabelProcess stateLabelProcess;
	private PageWidgetProcess pageWidgetProcess;
	// --权限类
	private ResProcess resProcess;
	private PermissionProcess permissionProcess;

	// -模块类
	private FormProcess formProcess;
	private ViewProcess viewProcess;
	private BillDefiProcess billDefiProcess;
	private CrossReportProcess crossReportProcess;
	private PrinterProcess printerProcess;

	public ExpProcessBean() {
		try {
			formProcess = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);
			pageProcess = (PageProcess) ProcessFactory
					.createProcess(PageProcess.class);
			homePageProcess = (UserDefinedProcess) ProcessFactory
					.createProcess(UserDefinedProcess.class);
			viewProcess = (ViewProcess) ProcessFactory
					.createProcess(ViewProcess.class);
			billDefiProcess = (BillDefiProcess) ProcessFactory
					.createProcess(BillDefiProcess.class);
			roleProcess = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			applicationProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			moduleProcess = (ModuleProcess) ProcessFactory
					.createProcess(ModuleProcess.class);
			resourceProcess = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);
			repositoryProcess = (RepositoryProcess) ProcessFactory
					.createProcess(RepositoryProcess.class);
			styleRepositoryProcess = (StyleRepositoryProcess) ProcessFactory
					.createProcess(StyleRepositoryProcess.class);
			validateRepositoryProcess = (ValidateRepositoryProcess) ProcessFactory
					.createProcess(ValidateRepositoryProcess.class);
			excelMappingConfigProcess = (IMPMappingConfigProcess) ProcessFactory
					.createProcess(IMPMappingConfigProcess.class);
			reminderProcess = (SummaryCfgProcess) ProcessFactory
					.createProcess(SummaryCfgProcess.class);
			taskProcess = (TaskProcess) ProcessFactory
					.createProcess(TaskProcess.class);
			crossReportProcess = (CrossReportProcess) ProcessFactory
					.createProcess(CrossReportProcess.class);
			dataSourceProcess = (DataSourceProcess) ProcessFactory
					.createProcess(DataSourceProcess.class);
			printerProcess = (PrinterProcess) ProcessFactory
					.createProcess(PrinterProcess.class);
			multiLanguageProcess = (MultiLanguageProcess) ProcessFactory
					.createProcess(MultiLanguageProcess.class);

			resProcess = (ResProcess) ProcessFactory
					.createProcess(ResProcess.class);
			permissionProcess = (PermissionProcess) ProcessFactory
					.createProcess(PermissionProcess.class);
			stateLabelProcess = (StateLabelProcess) ProcessFactory
					.createProcess(StateLabelProcess.class);
			pageWidgetProcess = (PageWidgetProcess) ProcessFactory
					.createProcess(PageWidgetProcess.class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	void addApplicationElements(ExpImpElements elements,
			ApplicationVO application) throws Exception {
		Collection<ResVO> allRes = resProcess.doSimpleQuery(null, application
				.getId()); // 资源
		// 处理应用子元素
		Collection<RoleVO> allRoles = roleProcess.doSimpleQuery(null,
				application.getId()); // 角色
		Collection<ResourceVO> resourceList = resourceProcess.doSimpleQuery(
				null, application.getId()); // 菜单资源
		Collection<ResourceVO> allResources = resourceProcess
				.deepSearchResouece(resourceList, null, "", Integer.MAX_VALUE);
		Collection<PermissionVO> allPermissions = permissionProcess // 角色资源权限
		        .doSimpleQuery(null, application.getId());
		Collection<RepositoryVO> allRepositories = repositoryProcess
				.doSimpleQuery(null, application.getId()); // 函数库
		Collection<StyleRepositoryVO> allStyleRepositories = styleRepositoryProcess
				.doSimpleQuery(null, application.getId()); // 函数库
		Collection<ValidateRepositoryVO> allValidateRepositories = validateRepositoryProcess
				.doSimpleQuery(null, application.getId()); // 校验库
		Collection<IMPMappingConfigVO> allExcelMappingConfigs = excelMappingConfigProcess
				.doSimpleQuery(null, application.getId()); // Excel导入配置
		Collection<SummaryCfgVO> allReminders = reminderProcess.doSimpleQuery(null,
				application.getId()); // 提醒
		Collection<Page> allPages = pageProcess.doSimpleQuery(null, application
				.getId());
		Collection<UserDefined> allUserDefineds = homePageProcess.doSimpleQuery(null,
				application.getId());
		Collection<DataSource> allDataSource = dataSourceProcess.doSimpleQuery(
				null, application.getId());// 数据源
		Collection<Task> allTasks = taskProcess.
				doSimpleQuery(null, application.getId());  // 定时任务
		Collection<MultiLanguage> allMultiLanguages = multiLanguageProcess.doSimpleQuery(
				null, application.getId());// 多语言
		Collection<StateLabel> allStateLabel = stateLabelProcess.doSimpleQuery(
				null, application.getId());// 状态标签
		Collection<PageWidget> allPageWidget = pageWidgetProcess.doSimpleQuery(
				null, application.getId());//小工具

		elements.setRes(allRes);
		elements.setRoles(allRoles);
		elements.setResources(allResources);
		elements.setPermissions(allPermissions);
		elements.setRepositories(allRepositories);
		elements.setStyleRepositories(allStyleRepositories);
		elements.setValidateRepositories(allValidateRepositories);
		elements.setExcelMappingConfigs(allExcelMappingConfigs);
		elements.setReminders(allReminders);
		elements.setPages(allPages);
		elements.setUserDefineds(allUserDefineds);
		elements.setDataSource(allDataSource);
		elements.setTasks(allTasks);
		elements.setMultiLanguage(allMultiLanguages);
		elements.setStateLabels(allStateLabel);
		elements.setPageWidgets(allPageWidget);
	}

	void addModulesElements(ExpImpElements elements,
			Collection<ModuleVO> allModules) throws Exception {
		Collection<Form> allForms = new ArrayList<Form>(); // 表单
		Collection<View> allViews = new ArrayList<View>(); // 视图
		Collection<BillDefiVO> allWorkflows = new ArrayList<BillDefiVO>(); // 流程
		Collection<CrossReportVO> allCrossReports = new ArrayList<CrossReportVO>(); // 交叉报表定制
		Collection<Printer> allPrinters = new ArrayList<Printer>();// 打印配置

		for (Iterator<ModuleVO> iterator = allModules.iterator(); iterator
				.hasNext();) {
			ModuleVO module = iterator.next();

			ParamsTable params = new ParamsTable();
			params.setParameter("t_module", module.getId());
			Collection<Form> forms = formProcess.doSimpleQuery(params);
			allForms.addAll(forms);

			Collection<View> views = viewProcess.doSimpleQuery(params);
			allViews.addAll(views);

			Collection<BillDefiVO> workflows = billDefiProcess
					.doSimpleQuery(params);
			allWorkflows.addAll(workflows);

			Collection<CrossReportVO> crossReports = crossReportProcess
					.doSimpleQuery(params);
			allCrossReports.addAll(crossReports);

			Collection<Printer> printers = printerProcess.doSimpleQuery(params);
			allPrinters.addAll(printers);
			

		}

		elements.setModules(allModules);
		elements.setForms(allForms);
		elements.setViews(allViews);
		elements.setWorkflows(allWorkflows);
		elements.setCrossReports(allCrossReports);
		elements.setPrinter(allPrinters);

	}

	void addSelectElements(ExpImpElements elements, ExpSelect select)
			throws Exception {
		Collection<Form> allForms = new ArrayList<Form>(); // 表单
		Collection<View> allViews = new ArrayList<View>(); // 视图
		Collection<BillDefiVO> allWorkflows = new ArrayList<BillDefiVO>(); // 流程
		Collection<CrossReportVO> allCrossReports = new ArrayList<CrossReportVO>(); // 交叉报表定制
		Collection<Printer> allPrinters = new ArrayList<Printer>(); // 打印配置

		String[] forms = select.getForms();
		for (int i = 0; i < forms.length; i++) {
//			Form form = (Form) formProcess.doView(forms[i]);
			Form form = (Form) new FormProcessBean().doView(forms[i]); //hotfix
			allForms.add(form);
		}

		String[] views = select.getViews();
		for (int i = 0; i < views.length; i++) {
			View view = (View) viewProcess.doView(views[i]);
			allViews.add(view);
		}

		String[] workflows = select.getWorkflows();
		for (int i = 0; i < workflows.length; i++) {
			BillDefiVO workflow = (BillDefiVO) billDefiProcess
					.doView(workflows[i]);
			allWorkflows.add(workflow);
		}

		String[] crossReports = select.getCrossReports();
		for (int i = 0; i < crossReports.length; i++) {
			CrossReportVO crossReportVO = (CrossReportVO) crossReportProcess
					.doView(crossReports[i]);
			allCrossReports.add(crossReportVO);
		}

		String[] printers = select.getPrinters();
		for (int i = 0; i < printers.length; i++) {
			Printer printer = (Printer) printerProcess.doView(printers[i]);
			allPrinters.add(printer);
		}

		elements.setForms(allForms);
		elements.setViews(allViews);
		elements.setWorkflows(allWorkflows);
		elements.setCrossReports(allCrossReports);
		elements.setPrinter(allPrinters);
	}

	/**
	 * 导出应用所有子元素的XML文件,包含（角色,菜单资源,角色菜单权限,函数库,校验库,Excel导入配置,提醒,模块,表单,视图）
	 * 
	 * @param select
	 *            选择的应用、模块或元素等
	 * @param fileName
	 *            文件保存路径
	 * @return
	 * @throws Exception
	 */
	public File createZipFile(ExpSelect select, String fileName)
			throws Exception {
		File[] tobeZippedFiles = new File[1];
		ExpImpElements elements = new ExpImpElements();
		elements.setExportType(select.getExportType());

		Collection<ModuleVO> allModules = null; // 模块

		ApplicationVO application = (ApplicationVO) applicationProcess
				.doView(select.getApplicationid());

		Collection<ModuleVO> moduleList = null;
		ModuleVO moduleVO = null;
		String xmlFileName = "";

		switch (select.getExportType()) {
		case ExpSelect.EXPROT_TYPE_APPLICATION:
			addApplicationElements(elements, application);
			// 处理模块及其子元素
			moduleList = moduleProcess.doSimpleQuery(null, application.getId());
			allModules = moduleProcess.deepSearchModule(moduleList, null, "",
					Integer.MAX_VALUE);
			addModulesElements(elements, allModules);
			xmlFileName = application.getName();
			break;
		case ExpSelect.EXPROT_TYPE_MODULE:
			moduleVO = (ModuleVO) moduleProcess.doView(select.getModuleid());
			moduleList = moduleProcess.doSimpleQuery(null, application.getId());
			allModules = moduleProcess.deepSearchModule(moduleList, moduleVO,
					null, Integer.MAX_VALUE);
			addModulesElements(elements, allModules);
			xmlFileName = moduleVO.getName();
			break;
		case ExpSelect.EXPROT_TYPE_MODULE_ELEMENTS:
			moduleVO = (ModuleVO) moduleProcess.doView(select.getModuleid());
			addSelectElements(elements, select);
			xmlFileName = moduleVO.getName();
			break;
		default:
			break;
		}

		tobeZippedFiles[0] = getXmlFile(elements, xmlFileName, 0);

		File archiveFile = new File(fileName);
		LOG.info("ZipFileName------------>" + archiveFile.getAbsolutePath());
		ZipUtil.createZipArchive(archiveFile, tobeZippedFiles);

		return archiveFile;
	}

	/**
	 * 导出应用所有子元素的XML文件,包含（角色,菜单资源,角色菜单权限,函数库,校验库,Excel导入配置,提醒,模块,表单,视图）
	 * 
	 * @param applicationId
	 * @return
	 * @throws Exception
	 */
	public File createZipFile(ExpSelect select) throws Exception {
		String fileName = DateUtil.getCurDateStr("yyyyMMddhhmmss") + ".zip";
		return createZipFile(select, getRealFileName(fileName));
	}

	File getXmlFile(Object obj, String name, int version) throws Exception {
		String fileName = name + "-" + version + ".xml";
		return XmlUtil.toXmlFile(obj, getRealFileName(fileName));
	}

	String getRealFileName(String fileName) throws Exception {
		String exportDir = DefaultProperty.getProperty("EXPORT_PATH");
		String fullFileName = Environment.getInstance().getRealPath(
				exportDir + fileName);
		return fullFileName;
	}

	public File getExportFile(String fileName) throws Exception {
		String filepath = DefaultProperty.getProperty("EXPORT_PATH");
		String realpath = Environment.getInstance().getRealPath(
				filepath + "/" + fileName);
		File exportFile = new File(realpath);
		if (exportFile.exists()) {
			return exportFile;
		} else {
			return null;
		}

	}

	protected IDesignTimeDAO<ExpSelect> getDAO() throws Exception {
		return null;
	}
}
