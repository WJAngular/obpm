package cn.myapps.core.expimp.imp.ejb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.ValidationAwareSupport;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.module.action.ModuleHelper;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigProcess;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigVO;
import cn.myapps.core.dynaform.form.ejb.Confirm;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.printer.ejb.Printer;
import cn.myapps.core.dynaform.printer.ejb.PrinterProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.expimp.ExpImpElements;
import cn.myapps.core.expimp.exp.ejb.ExpSelect;
import cn.myapps.core.links.ejb.LinkProcess;
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
import cn.myapps.core.table.constants.ConfirmConstant;
import cn.myapps.core.table.model.NeedConfirmException;
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
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.cache.EhcacheProvider;
import cn.myapps.util.cache.MyCacheManager;
import cn.myapps.util.file.ZipUtil;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;
import cn.myapps.util.xml.XmlUtil;


public class ImpProcessBean implements ImpProcess {
	final static Logger LOG = Logger.getLogger(ImpProcessBean.class);

	FormProcess formProcess;
	ViewProcess viewProcess;
	BillDefiProcess billDefiProcess;
	RoleProcess roleProcess;
	ModuleProcess moduleProcess;
	ResourceProcess resourceProcess;
	RepositoryProcess repositoryProcess;
	ValidateRepositoryProcess validateRepositoryProcess;
	IMPMappingConfigProcess excelMappingConfigProcess;
	SummaryCfgProcess reminderProcess;
	ApplicationProcess applicationProcess;
	PageProcess pageProcess;
	TaskProcess taskProcess;
	CrossReportProcess crossReportProcess;
	DataSourceProcess dataSourceProcess;
	PrinterProcess printerProcess;
	ResProcess resProcess;
	MultiLanguageProcess multiLanguageProcess;
	StateLabelProcess stateLabelProcess;
	PageWidgetProcess pageWidgetProcess;

	// Import时需要的Process
	PermissionProcess permissionProcess;
	StyleRepositoryProcess styleProcess;
	UserDefinedProcess homePageProcess;
	LinkProcess linkProcess;

	public ImpProcessBean() {
		try {
			formProcess = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);
			viewProcess = (ViewProcess) ProcessFactory
					.createProcess(ViewProcess.class);
			billDefiProcess = (BillDefiProcess) ProcessFactory
					.createProcess(BillDefiProcess.class);
			roleProcess = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			moduleProcess = (ModuleProcess) ProcessFactory
					.createProcess(ModuleProcess.class);
			resourceProcess = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);
			repositoryProcess = (RepositoryProcess) ProcessFactory
					.createProcess(RepositoryProcess.class);
			validateRepositoryProcess = (ValidateRepositoryProcess) ProcessFactory
					.createProcess(ValidateRepositoryProcess.class);
			excelMappingConfigProcess = (IMPMappingConfigProcess) ProcessFactory
					.createProcess(IMPMappingConfigProcess.class);
			reminderProcess = (SummaryCfgProcess) ProcessFactory
					.createProcess(SummaryCfgProcess.class);

			homePageProcess = (UserDefinedProcess) ProcessFactory
					.createProcess(UserDefinedProcess.class);
			styleProcess = (StyleRepositoryProcess) ProcessFactory
					.createProcess(StyleRepositoryProcess.class);
			permissionProcess = (PermissionProcess) ProcessFactory
					.createProcess(PermissionProcess.class);
			applicationProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			pageProcess = (PageProcess) ProcessFactory
					.createProcess(PageProcess.class);
			taskProcess = (TaskProcess) ProcessFactory
					.createProcess(TaskProcess.class);
			crossReportProcess = (CrossReportProcess) ProcessFactory
					.createProcess(CrossReportProcess.class);
			dataSourceProcess = (DataSourceProcess) ProcessFactory
					.createProcess(DataSourceProcess.class);
			printerProcess = (PrinterProcess) ProcessFactory
					.createProcess(PrinterProcess.class);
			resProcess = (ResProcess) ProcessFactory
					.createProcess(ResProcess.class);
			multiLanguageProcess = (MultiLanguageProcess) ProcessFactory
			.createProcess(MultiLanguageProcess.class);
			linkProcess = (LinkProcess) ProcessFactory
					.createProcess(LinkProcess.class);
			stateLabelProcess = (StateLabelProcess) ProcessFactory
					.createProcess(StateLabelProcess.class);
			pageWidgetProcess = (PageWidgetProcess) ProcessFactory
					.createProcess(PageWidgetProcess.class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对导入的文件进行校验和处理
	 * 
	 * @param importFile
	 *            导入文件
	 * @return 校验感知
	 * @throws Exception
	 */
	public ValidationAware doImportValidate(ImpSelect select, File importFile)
			throws Exception {
		ExpImpElements elements = parseFile(importFile,select.getHandleMode());

		ValidationAware vas = new ValidationAwareSupport();

		ApplicationVO application = (ApplicationVO) applicationProcess
				.doView(select.getApplicationid());

		String typeName = (String) new ExpSelect().getExportTypeNameMap().get(
				Integer.valueOf(elements.getExportType()));

		boolean flag = false;

		switch (elements.getExportType()) {
		case ExpSelect.EXPROT_TYPE_APPLICATION:
			flag = select.getImportType() != ImpSelect.IMPORT_TYPE_APPLICATION;
			break;
		case ExpSelect.EXPROT_TYPE_MODULE:
			flag = select.getImportType() != ImpSelect.IMPORT_TYPE_MODULE;
			break;
		case ExpSelect.EXPROT_TYPE_MODULE_ELEMENTS:
			flag = select.getImportType() != ImpSelect.IMPORT_TYPE_MODULE_ELEMENTS;
			break;
		default:
			break;
		}
		if (flag) {
			throw new OBPMValidateException("{*[import.type.not.match]*} (" + typeName
					+ ")");
		}

		for (Iterator<Form> iterator = elements.getForms().iterator(); iterator
				.hasNext();) {
			Form form = iterator.next();
			form.setApplicationid(application.getId());
			try {
				formProcess.doChangeValidate(form);
			} catch (OBPMValidateException e) {
				if (e.getCause() instanceof NeedConfirmException) {
					Collection<Confirm> confirms = ((NeedConfirmException) e.getCause())
						.getConfirms();
					for (Iterator<Confirm> iterator2 = confirms.iterator(); iterator2
						.hasNext();) {
						Confirm confirm = iterator2.next();
						if (confirm.getMsgKeyCode() == ConfirmConstant.FIELD_DATA_EXIST) {
							vas.addActionMessage(confirm.getMessage());
						} else {
							vas.addActionError(confirm.getMessage());
						}
					}
				}
			}
		}

		return vas;
	}

	/**
	 * 解析文件包，将xml数据转换成对象
	 * @param importFile
	 * 		导入的文件
	 * @param handleMode
	 * 		导入处理方式（覆盖更新|复制）
	 * @return
	 * 	导入导出的集合对象，包含所有可导入导出的元素
	 */
	public ExpImpElements parseFile(File importFile,String handleMode) {
		try {
			String[] xmlContents = ZipUtil.readZipFile(importFile);
			if (xmlContents.length > 0) {
				String xml = xmlContents[0];
				
				if(ImpSelect.HANDLE_MODE_COPY.equals(handleMode)){//复制方式导入
					long ct = System.currentTimeMillis();
					Map<String, String> replaceTo = new HashMap<String, String>();
					Pattern p = Pattern.compile("(<id>[^>]*</id>)");
					Matcher m = p.matcher(xml);
					while (m.find()) {
						String part = m.group();
						String id = part.substring(4,part.length()-5);
						try {
							replaceTo.put(id, Sequence.getSequence());
						} catch (SequenceException e) {
							replaceTo.put(id, id+"1");
							e.printStackTrace();
						}
					}
					long ct2 = System.currentTimeMillis();
					System.err.println("软件复制--收集ID阶段耗时(ms)："+(ct2-ct));
					
					int length =  xml.length();//xml 总长度
					int b = 30;//切分成多少份
					int index = 0;//最后一次切分的位置
					int s = length/b;//每份的大致长度
					Map<Integer, String> map = new HashMap<Integer, String>();
					for(int i=0;i<b;i++){
						String part;
						int position = index+s;
						if(i+1==b || position>=length){
							part = xml.substring(index);
							b=i+1;
						}else{
							while(xml.charAt(position)!='>'){
								position++;
							}
							position++;
							part = xml.substring(index,position);
						}
						map.put(i,part);
						index = position;
					}
					
					for (Iterator<Entry<Integer, String>>  iterator = map.entrySet().iterator(); iterator.hasNext();) {
						Entry<Integer, String> entry = iterator.next();
						String pXml = entry.getValue();
						for (Iterator<Entry<String, String>> iterator2 = replaceTo.entrySet().iterator(); iterator2
								.hasNext();) {
							Entry<String, String> idEntry = iterator2.next();
							pXml = pXml.replaceAll(idEntry.getKey(), idEntry.getValue());
						}
						map.put(entry.getKey(), pXml);
					}
					
					StringBuilder sb = new StringBuilder();
					for(int i=0;i<b;i++){
						sb.append(map.get(i));
					}
					xml = sb.toString();
					
					/**
					ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					
					char[][] replaceMatch = new char[replaceTo.size()][];

					Iterator<String> it = replaceTo.keySet().iterator();
					int j = 0;
					while (it.hasNext()) {
						String key = it.next();
						replaceMatch[j] = key.toCharArray();
						j++;
					}

					int[] offset = new int[replaceMatch.length];
					int b;
					while ((b = is.read()) != -1) {
						char c = (char) b;
						boolean flag = false;
						for (int i = 0; i < replaceMatch.length; i++) {
							// System.out.println("offset["+i+"]"+offset[i]);
							if (c == replaceMatch[i][offset[i]]) {
								flag = true;
								offset[i]++;
								if (offset[i] == replaceMatch[i].length) {
									offset = new int[replaceMatch.length];
									os.write(replaceTo.get(new String(replaceMatch[i])).getBytes());
						///			os.flush();
								}
							} else {
								if (offset[i] > 0) {
									boolean only = true;
									for (int k = 0; k < offset.length; k++) {
										if (k != i && offset[k] > 0){
											only = false;
											break;
										}
									}
									
									if (only) {
						//				os.write(("["+new String(replaceMatch[i], 0, offset[i])+"]").getBytes());
										os.write(new String(replaceMatch[i], 0, offset[i]).getBytes());
						//				os.flush();
									}
								}
								offset[i] = 0;
								// System.out.print(c);
							}
						}
						if (!flag)
							os.write(c);
						//os.flush();
					}
					os.flush();
					
					xml = new String(os.toByteArray());
					**/
					
					long ct3 = System.currentTimeMillis();
					System.err.println("软件复制--替换ID阶段耗时(s)："+(ct3-ct2)/1000);
				}
				
				ExpImpElements elements = (ExpImpElements) XmlUtil
						.toOjbect(xml);
				return elements;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void doImport(ImpSelect select, File importFile) throws Exception {
		if (importFile == null) {
			throw new OBPMValidateException("{*[core.util.cannotsave]*}");
		}

		ExpImpElements elements = parseFile(importFile,select.getHandleMode());
		ApplicationVO application = (ApplicationVO) applicationProcess
				.doView(select.getApplicationid());
		ModuleVO superior = (ModuleVO) moduleProcess.doView(select
				.getModuleid());
		Collection<ModuleVO> modules = ((ModuleProcess) moduleProcess)
				.getModuleByApplication(application.getId());
		
		// 导入ModuleVO
		for (Iterator<ModuleVO> iterator = elements.getModules().iterator(); iterator
				.hasNext();) {
			ModuleVO module = iterator.next();
			module.setApplication(application);
			module.setApplicationid(application.getId());
			if (select.getImportType() == ImpSelect.IMPORT_TYPE_MODULE) {
				if (superior != null
						&& !module.getId().equals(superior.getId())) {
					module.setSuperior(superior);
				} else {
					module.setSuperior(null);
				}
			}
			String newname = ModuleHelper.renameModule(modules, module, 0, true);
			module.setName(newname);
			doCreateOrUpdate(moduleProcess, module);
		}
		
		// 导入ResVO
		for (Iterator<ResVO> iterator = elements.getRes().iterator(); iterator
				.hasNext();) {
			ResVO resVO = iterator.next();
			resVO.setApplicationid(application.getId());
			doCreateOrUpdate(resProcess, resVO);
		}

		// 导入MultiLanguage
		if(elements.getMultiLanguage() != null){
			for (Iterator<MultiLanguage> iterator = elements.getMultiLanguage().iterator(); iterator
					.hasNext();) {
				MultiLanguage multiLanguage = iterator.next();
				multiLanguage.setApplicationid(application.getId());
				doCreateOrUpdate(multiLanguageProcess, multiLanguage);
			}
		}
		
		// 导入Task
		for (Iterator<Task> iterator = elements.getTasks().iterator(); iterator
				.hasNext();) {
			Task task = iterator.next();
			task.setApplicationid(application.getId());
			doCreateOrUpdate(taskProcess, task);
		}
		
		// 导入RoleVO
		for (Iterator<RoleVO> iterator = elements.getRoles().iterator(); iterator
				.hasNext();) {
			RoleVO role = iterator.next();
			role.setApplicationid(application.getId());
			doCreateOrUpdate(roleProcess, role);
		}
		
		ResourceVO mobileRes = null;
		ResourceVO mobileImp = null;
		ParamsTable params = new ParamsTable();
		params.setParameter("t_applicationid", application.getId());
		params.setParameter("t_type", "100");
		params.setParameter("t_description", "Mobile");
		DataPackage<ResourceVO> datas = resourceProcess.doQuery(params);
		if(datas.getRowCount() >0){
			for(Iterator<ResourceVO> ites = datas.datas.iterator();ites.hasNext();){
				ResourceVO resourceVO = ites.next();
				if(resourceVO.getSuperior() == null){
					mobileRes = resourceVO;
					break;
				}
			}
		}
		
		// 导入ResourceVO
		for (Iterator<ResourceVO> iterator = elements.getResources().iterator(); iterator
				.hasNext();) {
			ResourceVO resource = iterator.next();
			resource.setApplicationid(application.getId());
			resource.setApplication(application.getId());
			//处理手机菜单mobile重复问题
			if(mobileRes !=null && mobileImp == null && resource.getSuperior() == null && resource.getType().equals("100")){
				mobileImp = resource;
				continue;
			}
			
			if(mobileRes !=null && mobileImp!= null){
				if(resource.getType().equals("100") && resource.getSuperior()!= null && resource.getSuperior().getId().equals(mobileImp.getId())){
					resource.setSuperior(mobileRes);
				}
			}
			
			doCreateOrUpdate(resourceProcess, resource);
		}

		// 导入PermissionVO
		for (Iterator<PermissionVO> iterator = elements.getPermissions()
				.iterator(); iterator.hasNext();) {
			PermissionVO permission = iterator.next();
			permission.setApplicationid(application.getId());
			doCreateOrUpdate(permissionProcess, permission);
		}

		// 导入RepositoryVO
		for (Iterator<RepositoryVO> iterator = elements.getRepositories()
				.iterator(); iterator.hasNext();) {
			RepositoryVO repository = iterator.next();
			repository.setApplicationid(application.getId());
			doCreateOrUpdate(repositoryProcess, repository);
		}
		
		// 导入StyleRepositoryVO
		for (Iterator<StyleRepositoryVO> iterator = elements.getStyleRepositories()
				.iterator(); iterator.hasNext();) {
			StyleRepositoryVO repository = iterator.next();
			repository.setApplicationid(application.getId());
			doCreateOrUpdate(styleProcess, repository);
		}

		// 导入ValidateRepositoryVO
		for (Iterator<ValidateRepositoryVO> iterator = elements
				.getValidateRepositories().iterator(); iterator.hasNext();) {
			ValidateRepositoryVO repository = iterator.next();
			repository.setApplicationid(application.getId());
			doCreateOrUpdate(validateRepositoryProcess, repository);
		}

		// 导入IMPMappingConfigVO
		for (Iterator<IMPMappingConfigVO> iterator = elements
				.getExcelMappingConfigs().iterator(); iterator.hasNext();) {
			IMPMappingConfigVO excelMappConfig = iterator.next();
			excelMappConfig.setApplicationid(application.getId());
			doCreateOrUpdate(excelMappingConfigProcess, excelMappConfig);
		}
		// 导入Page
		if (elements.getPages() != null)
			for (Iterator<Page> iterator = elements.getPages().iterator(); iterator
					.hasNext();) {
				Page page = iterator.next();
				StyleRepositoryVO style = page.getStyle();
				if (style != null) {
					style.setApplicationid(application.getId());
					doCreateOrUpdate(styleProcess, style);
				}
				try {
					UserDefined homePage = new UserDefined();
					homePage.setId(Sequence.getSequence());
					homePage.setName(page.getName());
					homePage.setDescription(page.getDiscription());
					homePage.setDefineMode(UserDefined.CUSTOMIZE_MODE);
					homePage.setTemplateContext(page.getTemplatecontext());
					homePage.setApplicationid(application.getId());
					homePage.setPublished(page.isDefHomePage());
					homePage.setRoleNames(page.getRoleNames());
					homePage.setRoleIds(page.getRoles());
					homePage.setStyle(page.getStyle());
					homePage.setVersion(page.getVersion());
					doCreateOrUpdate(homePageProcess, homePage);
				} catch (Exception e) {
					LOG.warn(e.getMessage());
				}
			}

		// 导入HomePage
		if (elements.getUserDefineds() != null)
			for (Iterator<UserDefined> iterator = elements.getUserDefineds()
					.iterator(); iterator.hasNext();) {
				UserDefined homePage = iterator.next();
				homePage.setApplicationid(application.getId());
				StyleRepositoryVO style = homePage.getStyle();
				if (style != null) {
					style.setApplicationid(application.getId());
					doCreateOrUpdate(styleProcess, style);
				}

				doCreateOrUpdate(homePageProcess, homePage);
			}


		// 导入Form
		for (Iterator<Form> iterator = elements.getForms().iterator(); iterator
				.hasNext();) {
			Form form = iterator.next();
			form.setCheckout(false);
			form.setCheckoutHandler("");
			form.setApplicationid(application.getId());

			StyleRepositoryVO style = form.getStyle();
			if (style != null) {
				style.setApplicationid(application.getId());
				doCreateOrUpdate(styleProcess, style);
			}
			if (select.getImportType() == ImpSelect.IMPORT_TYPE_MODULE_ELEMENTS) {
				form.setModule(superior);
			}
			form.setImprot(true);
			doCreateOrUpdate(formProcess, form);
		}

		// 导入Reminder
		for (Iterator<SummaryCfgVO> iterator = elements.getReminders().iterator(); iterator
				.hasNext();) {
			SummaryCfgVO reminder = iterator.next();
			reminder.setApplicationid(application.getId());

		/*	UserDefined homePage = reminder.getUserDefined();
			if (homePage != null) {
				homePage.setApplicationid(application.getId());
				doCreateOrUpdate(homePageProcess, homePage);
			}*/
			doCreateOrUpdate(reminderProcess, reminder);
		}
		
		// 导入View
		for (Iterator<View> iterator = elements.getViews().iterator(); iterator
				.hasNext();) {
			View view = iterator.next();
			view.setCheckout(false);
			view.setCheckoutHandler("");
			view.setApplicationid(application.getId());

			StyleRepositoryVO style = view.getStyle();
			if (style != null) {
				style.setApplicationid(application.getId());
				doCreateOrUpdate(styleProcess, style);
			}

			Form form = view.getSearchForm();
			if (form != null) {
				form.setApplicationid(application.getId());
				if (select.getImportType() == ImpSelect.IMPORT_TYPE_MODULE_ELEMENTS) {
					form.setModule(superior);
				}
				doCreateOrUpdate(formProcess, form);
			}
			if (select.getImportType() == ImpSelect.IMPORT_TYPE_MODULE_ELEMENTS) {
				view.setModule(superior);
			}
			doCreateOrUpdate(viewProcess, view);
		}

		// 导入Workflow
		for (Iterator<BillDefiVO> iterator = elements.getWorkflows().iterator(); iterator
				.hasNext();) {
			BillDefiVO flow = iterator.next();
			flow.setCheckout(false);
			flow.setCheckoutHandler("");
			flow.setApplicationid(application.getId());
			if (select.getImportType() == ImpSelect.IMPORT_TYPE_MODULE_ELEMENTS) {
				flow.setModule(superior);
			}
			doCreateOrUpdate(billDefiProcess, flow);
		}

		// 导入CrossReport
		for (Iterator<CrossReportVO> iterator = elements.getCrossReports()
				.iterator(); iterator.hasNext();) {
			CrossReportVO crossReportVO = iterator.next();
			crossReportVO.setCheckout(false);
			crossReportVO.setCheckoutHandler("");
			crossReportVO.setApplicationid(application.getId());
			if (select.getImportType() == ImpSelect.IMPORT_TYPE_MODULE_ELEMENTS) {
				crossReportVO.setModule(superior.getId());
			}
			doCreateOrUpdate(crossReportProcess, crossReportVO);
		}

		// 导入DataSource
		for (Iterator<DataSource> iterator = elements.getDataSource()
				.iterator(); iterator.hasNext();) {
			DataSource datasource = iterator.next();
			datasource.setApplicationid(application.getId());
			doCreateOrUpdate(dataSourceProcess, datasource);
		}
		// 导入Printer
		for (Iterator<Printer> iterator = elements.getPrinter().iterator(); iterator
				.hasNext();) {
			Printer printer = iterator.next();
			printer.setCheckout(false);
			printer.setCheckoutHandler("");
			printer.setApplicationid(application.getId());
			if (select.getImportType() == ImpSelect.IMPORT_TYPE_MODULE_ELEMENTS) {
				printer.setModule(superior);
			}
			doCreateOrUpdate(printerProcess, printer);
		}
		
		//导入状态标签
		if (elements.getStateLabels() != null){
			for(Iterator<StateLabel> iterator = elements.getStateLabels().iterator(); iterator
					.hasNext();){
				StateLabel stateLabel = iterator.next();
				stateLabel.setApplicationid(application.getId());
				doCreateOrUpdate(stateLabelProcess, stateLabel);
			}
		}
		
		//导入小工具
		for(Iterator<PageWidget> iterator = elements.getPageWidgets().iterator(); iterator
				.hasNext();){
			PageWidget pageWidget = iterator.next();
			pageWidget.setApplicationid(application.getId());
			doCreateOrUpdate(pageWidgetProcess, pageWidget);
		}
		
		//清除缓存
		EhcacheProvider cacheProvider = (EhcacheProvider) MyCacheManager.getProviderInstance();
		cacheProvider.clearAll();

	}
	
	
	public void doCreateOrUpdate(IDesignTimeProcess<?> process, ValueObject vo)
			throws Exception {
		ValueObject po = (ValueObject) process.doView(vo.getId());
		try {
			if (po == null) {
				process.doCreate(vo);
			} else {
				vo.setVersion(po.getVersion());
				process.doUpdate(vo);
			}
		} catch (Exception e) {
			LOG.error("Create or update " + vo.getClass().getName() + "("
					+ vo.getId() + ") failed ", e);
			throw e;
		}

	}
}
