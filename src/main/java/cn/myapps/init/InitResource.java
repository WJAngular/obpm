package cn.myapps.init;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

public class InitResource implements IInitialization {

	public void run() throws InitializationException {
		run("");
	}

	public void run(String applicationid) throws InitializationException {
		try {
			ResourceProcess process = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("s_applicationid", applicationid);
			Collection<ResourceVO> colls = process.doSimpleQuery(params, null);

			if (colls.isEmpty()) {
				/**
				 * Application Menu
				 */
				ResourceVO app = new ResourceVO();
				app.setId(Sequence.getSequence());
				app.setDescription("App Definition");
				app.setOrderno(1);
				app.setSuperior(null);
				app.setType("00");
				app.setApplication("");
				app.setReport("");
				app.setReportAppliction("");
				app.setReportModule("");
				app.setSortId(Sequence.getTimeSequence());
				app.setApplicationid(applicationid);
				process.doUpdate(app);

				/**
				 * Dev Stiduo Menu
				 */
				ResourceVO dev = new ResourceVO();
				dev.setId(Sequence.getSequence());
				dev.setDescription("Dev Studio");
				dev.setOrderno(2);
				dev.setSuperior(null);
				dev.setType("00");
				dev.setApplication("");
				dev.setReport("");
				dev.setReportAppliction("");
				dev.setReportModule("");
				dev.setSortId(Sequence.getTimeSequence());
				dev.setApplicationid(applicationid);
				process.doUpdate(dev);

				addDevSubResource(dev, applicationid);

/*				*//**
				 * Mobile Menu
				 *//*
				ResourceVO mobile = new ResourceVO();
				mobile.setId("mobile");
				mobile.setActionclass("none");
				mobile.setActionmethod("none");
				mobile.setActionurl("none");
				mobile.setDescription("Mobile");
				mobile.setOrderno("3");
				mobile.setSuperior(null);
				mobile.setType("00");
				mobile.setApplication(applicationid);
				mobile.setDisplayView("");
				mobile.setModule("");
				mobile.setResourceAction("00");
				mobile.setIsprotected(true);
				mobile.setReport("");
				mobile.setReportAppliction("");
				mobile.setReportModule("");
				mobile.setSortId(Sequence.getTimeSequence());
				mobile.setApplicationid(applicationid);
				process.doUpdate(mobile);*/

				/**
				 * System Menu
				 */
				ResourceVO system = new ResourceVO();
				system.setId(Sequence.getSequence());
				system.setDescription("System");
				system.setOrderno(4);
				system.setSuperior(null);
				system.setType("00");
				system.setApplication("");
				system.setReport("");
				system.setReportAppliction("");
				system.setReportModule("");
				system.setSortId(Sequence.getTimeSequence());
				system.setApplicationid(applicationid);
				process.doUpdate(system);
				
				addSystemSubResource(system, applicationid);
			}
		} catch (Exception e) {
			throw new InitializationException(e.getMessage());
		}
	}

	/**
	 * 增加System菜单项
	 * 
	 * @param system
	 * @param applicationid
	 * @throws Exception
	 */
	public void addSystemSubResource(ResourceVO system, String applicationid)
			throws Exception {
		ResourceProcess process = (ResourceProcess) ProcessFactory
				.createProcess(ResourceProcess.class);

		/**
		 * Menu/Page List Item
		 */
		{
			ResourceVO resource = new ResourceVO();
			resource.setId(Sequence.getSequence());
			resource.setDescription("Menu/Page");
			resource.setOrderno(1);
			resource.setSuperior(system);
			resource.setType("00");
			resource.setApplication("");
			resource.setReport("");
			resource.setReportAppliction("");
			resource.setReportModule("");
			resource.setSortId(Sequence.getTimeSequence());
			resource.setApplicationid(applicationid);
			process.doUpdate(resource);
		}

		/**
		 * Role List Item
		 */
		{
			ResourceVO resource = new ResourceVO();
			resource.setId(Sequence.getSequence());
			resource.setDescription("Role");
			resource.setOrderno(2);
			resource.setSuperior(system);
			resource.setType("00");
			resource.setApplication("");
			resource.setReport("");
			resource.setReportAppliction("");
			resource.setReportModule("");
			resource.setSortId(Sequence.getTimeSequence());
			resource.setApplicationid(applicationid);
			process.doUpdate(resource);
		}

		/**
		 * Excel Import Mapping Config List Item
		 */
		{
			ResourceVO resource = new ResourceVO();

			resource.setId(Sequence.getSequence());
			resource.setDescription("ExcelImpConfig");
			resource.setOrderno(3);
			resource.setSuperior(system);
			resource.setType("00");
			resource.setApplication("");
			resource.setReport("");
			resource.setReportAppliction("");
			resource.setReportModule("");
			resource.setSortId(Sequence.getTimeSequence());
			resource.setApplicationid(applicationid);
			process.doUpdate(resource);
		}

		/**
		 * Multi Language List Item
		 */
		{
			ResourceVO resource = new ResourceVO();
			resource.setId(Sequence.getSequence());
			resource.setDescription("Language");
			resource.setOrderno(4);
			resource.setSuperior(system);
			resource.setType("00");
			resource.setApplication("");
			resource.setReport("");
			resource.setReportAppliction("");
			resource.setReportModule("");
			resource.setSortId(Sequence.getTimeSequence());
			resource.setApplicationid(applicationid);
			process.doUpdate(resource);
		}

		/**
		 * Task List Item
		 */
		{
			ResourceVO resource = new ResourceVO();
			resource.setId(Sequence.getSequence());
			resource.setDescription("Task");
			resource.setOrderno(5);
			resource.setSuperior(system);
			resource.setType("00");
			resource.setApplication("");
			resource.setReport("");
			resource.setReportAppliction("");
			resource.setReportModule("");
			resource.setSortId(Sequence.getTimeSequence());
			resource.setApplicationid(applicationid);
			process.doUpdate(resource);
		}

		/**
		 * State Label List Item
		 */
		{
			ResourceVO resource = new ResourceVO();

			resource.setId(Sequence.getSequence());
			resource.setDescription("StateLabel");
			resource.setOrderno(6);
			resource.setSuperior(system);
			resource.setType("00");
			resource.setApplication("");
			resource.setReport("");
			resource.setReportAppliction("");
			resource.setReportModule("");
			resource.setSortId(Sequence.getTimeSequence());
			resource.setApplicationid(applicationid);
			process.doUpdate(resource);
		}

		/**
		 * Calendar Item List
		 */
		{
			ResourceVO resource = new ResourceVO();
			resource.setId(Sequence.getSequence());
			resource.setDescription("Calendar");
			resource.setOrderno(7);
			resource.setSuperior(system);
			resource.setType("00");
			resource.setApplication("none");
			resource.setReport("none");
			resource.setReportAppliction("none");
			resource.setReportModule("none");
			resource.setSortId(Sequence.getTimeSequence());
			resource.setApplicationid(applicationid);
			process.doUpdate(resource);
		}
	}

	/**
	 * 增加Dev Stiduo的菜单项
	 * 
	 * @param dev
	 * @param applicationid
	 * @throws Exception
	 */
	public void addDevSubResource(ResourceVO dev, String applicationid)
			throws Exception {
		ResourceProcess process = (ResourceProcess) ProcessFactory
				.createProcess(ResourceProcess.class);

		{
			ResourceVO resource = new ResourceVO();
			resource.setId(Sequence.getSequence());
			resource.setDescription("Functions");
			resource.setOrderno(0);
			resource.setSuperior(dev);
			resource.setType("00");
			resource.setApplication("");
			resource.setReport("");
			resource.setReportAppliction("");
			resource.setReportModule("");
			resource.setSortId(Sequence.getTimeSequence());
			resource.setApplicationid(applicationid);
			process.doUpdate(resource);
		}

		{
			ResourceVO resource = new ResourceVO();
			resource.setId(Sequence.getSequence());
			resource.setDescription("Debuger");
			resource.setOrderno(1);
			resource.setSuperior(dev);
			resource.setType("00");
			resource.setApplication("");
			resource.setReport("");
			resource.setReportAppliction("");
			resource.setReportModule("");
			resource.setSortId(Sequence.getTimeSequence());
			resource.setApplicationid(applicationid);
			process.doUpdate(resource);
		}

		{
			ResourceVO resource = new ResourceVO();
			resource.setId(Sequence.getSequence());
			resource.setDescription("StateMoniter");
			resource.setOrderno(2);
			resource.setSuperior(dev);
			resource.setType("00");
			resource.setApplication("");
			resource.setReport("");
			resource.setReportAppliction("");
			resource.setReportModule("");
			resource.setSortId(Sequence.getTimeSequence());
			resource.setApplicationid(applicationid);
			process.doUpdate(resource);
		}
	}
}
