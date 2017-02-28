package cn.myapps.core.expimp.imp.action;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.constans.Environment;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityProcess;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfigProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.repository.ejb.RepositoryProcess;
import cn.myapps.core.macro.repository.ejb.RepositoryVO;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.style.repository.ejb.StyleRepositoryProcess;
import cn.myapps.core.style.repository.ejb.StyleRepositoryVO;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.util.ProcessFactory;

public class ImpActionTest extends TestCase {
	ImpAction action;
	Environment evt;

	protected void setUp() throws Exception {
		super.setUp();
		action = new ImpAction();
		evt = Environment.getInstance(); // ����·��c:\downloads\export
		evt.setApplicationRealPath("c:/");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'cn.myapps.core.expimp.imp.action.ImpAction.doUpload()'
	 */
	public void testDoUpload() throws Exception {
//		String path = System.getProperty("user.dir") + File.separator + "downloads" + File.separator + "export" + File.separator;
		String path = System.getProperty("user.dir") + "/" + "downloads" + "/" + "export" + "/";
		//File dir = new File("c:/downloads/export/");
		File dir = new File(path);
		File impFile = dir.listFiles()[0];

		action.setImpFile(impFile);
		action.setImpFileFileName(impFile.getName());

		deleteAllDir(impFile);

		doImp();
	}

	/*
	 * Test method for 'cn.myapps.core.expimp.imp.action.ImpAction.doImp()'
	 */
	public void doImp() throws Exception {
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private Map<String[], IDesignTimeProcess> getValueObjects() throws Exception {
		Map<String[], IDesignTimeProcess> rtn = new LinkedHashMap<String[], IDesignTimeProcess>();

		// PersistenceUtils.getSessionSignal().sessionSignal++;
		ApplicationProcess appProcess = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
		Collection<ApplicationVO> apps = appProcess.doSimpleQuery(null, null);
		assertTrue(apps.size() > 0);

		ModuleProcess mdProcess = (ModuleProcess) ProcessFactory.createProcess(ModuleProcess.class);
		Collection<ModuleVO> module = mdProcess.doSimpleQuery(null, null);
		assertTrue(module.size() > 0);

		StyleRepositoryProcess styleProcess = (StyleRepositoryProcess) ProcessFactory
				.createProcess(StyleRepositoryProcess.class);
		Collection<StyleRepositoryVO> style = styleProcess.doSimpleQuery(null, null);
		assertTrue(style.size() > 0);

		BillDefiProcess bdProcess = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		Collection<BillDefiVO> bill = bdProcess.doSimpleQuery(null, null);
		assertTrue(bill.size() > 0);

		FormProcess fmProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> form = fmProcess.doSimpleQuery(null, null);
		assertTrue(form.size() > 0);

		ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		Collection<View> view = viewProcess.doSimpleQuery(null, null);
		assertTrue(view.size() > 0);

		ActivityProcess actProcess = (ActivityProcess) ProcessFactory.createProcess(ActivityProcess.class);
		Collection<Activity> activity = actProcess.doSimpleQuery(null, null);
		assertTrue(activity.size() > 0);

/*		ReportConfigProcess repcgProcess = (ReportConfigProcess) ProcessFactory
				.createProcess(ReportConfigProcess.class);
		Collection<ReportConfig> reportconfig = repcgProcess.doSimpleQuery(null, null);
		assertTrue(reportconfig.size() > 0);*/

		MappingConfigProcess mpcgProcess = (MappingConfigProcess) ProcessFactory
				.createProcess(MappingConfigProcess.class);
		Collection<MappingConfig> mappingconfig = mpcgProcess.doSimpleQuery(null, null);
		assertTrue(mappingconfig.size() > 0);

//		ComponentProcess cpProcess = (ComponentProcess) ProcessFactory.createProcess(ComponentProcess.class);
//		Collection<Component> comps = cpProcess.doSimpleQuery(null, null);
//		assertTrue(comps.size() > 0);

		ResourceProcess resProcess = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
		Collection<ResourceVO> res = resProcess.doSimpleQuery(null, null);
//		assertTrue(comps.size() > 0);
//
//		assertTrue(comps.size() > 0);

		RepositoryProcess repProcess = (RepositoryProcess) ProcessFactory.createProcess(RepositoryProcess.class);
		Collection<RepositoryVO> reps = repProcess.doSimpleQuery(null, null);
		assertTrue(reps.size() > 0);

//		rtn.put(getIds(reportconfig), repcgProcess);
		rtn.put(getIds(mappingconfig), mpcgProcess);
		rtn.put(getIds(activity), actProcess);
		rtn.put(getIds(view), viewProcess);
		rtn.put(getIds(bill), bdProcess);
		rtn.put(getIds(form), fmProcess);
		rtn.put(getIds(style), styleProcess);
		rtn.put(getIds(module), mdProcess);
		rtn.put(getIds(apps), appProcess);
//		rtn.put(getIds(comps), cpProcess);
		rtn.put(getIds(res), resProcess);
		rtn.put(getIds(reps), repProcess);

		// PersistenceUtils.getSessionSignal().sessionSignal--;
		return rtn;
	}

	private String[] getIds(Collection<?> colls) {
		String[] rtn = new String[colls.size()];
		int count = 0;
		for (Iterator<?> iter = colls.iterator(); iter.hasNext();) {
			rtn[count] = ((ValueObject) iter.next()).getId();
			count++;
		}
		return rtn;
	}

	private void deleteAllDir(File file) {
		if (file.isDirectory() && file.getParentFile() != null) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if(!files[i].delete())
					System.err.println("delete file '" + files[i].getAbsolutePath() + "' failed!");
			}
		}

		if (file.getParentFile() != null) {
			deleteAllDir(file.getParentFile());
		}
	}
}
