package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Date;

import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import junit.framework.TestCase;

public class FlowStateRTProcessTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDoCreate() throws Exception {
		String appid = "11de-ef9e-c010eee1-860c-e1cadb714510";
		// 1. 更新软件及软件相关数据库表结构
		ApplicationProcess appProcess = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
		ApplicationVO app = (ApplicationVO) appProcess.doView(appid);
		appProcess.doUpdate(app);
		
		// 2. 新建测试的FlowStateRT
		FlowStateRTProcess process = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, appid);
//		FlowStateRT stateRT = new FlowStateRT();
//		stateRT.setId("001");
//		stateRT.setApplicationid(appid);
//		stateRT.setFlowName("flowName");
//		stateRT.setFlowXML("flowXML");
//		stateRT.setLastModified(new Date());
//		stateRT.setLastModifierId("userid001");
//		
//		process.doCreate(stateRT);
		FlowStateRT stateRT = (FlowStateRT) process.doView("001");
		System.out.println(stateRT.getFlowName());
	}

	public void testDoUpdate() throws CreateProcessException {
		fail("Not yet implemented");
	}

	public void testFindFlowStateRTByDocidAndFlowid() {
		fail("Not yet implemented");
	}

	public void testDoRemove() {
		fail("Not yet implemented");
	}

	public void testDoView() {
		fail("Not yet implemented");
	}

}
