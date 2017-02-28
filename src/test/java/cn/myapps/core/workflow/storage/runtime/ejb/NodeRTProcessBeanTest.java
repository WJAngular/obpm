package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.util.ProcessFactory;

public class NodeRTProcessBeanTest extends TestCase {
	BillDefiProcess bp;

	NodeRTProcess np;

	protected void setUp() throws Exception {
		bp = (BillDefiProcess) ProcessFactory
				.createProcess(BillDefiProcess.class);
		np = (NodeRTProcess) ProcessFactory.createProcess(NodeRTProcess.class);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

//	public void testDoCreateStringBillDefiVONodeString() throws Exception {
//		BillDefiVO flowVO = (BillDefiVO) bp
//				.doView("567e3e38-0b05-4f89-a306-0b213910805a");
//		FlowDiagram fd = flowVO.toFlowDiagram();
//		Node node = (Node) fd.getElementByID("1184661649031");
//		node.name = "testNode";
//		
//		np.doCreate("", flowVO, node, "80");
//	}

	 public NodeRTProcess getNp() {
		return np;
	}

	public void setNp(NodeRTProcess np) {
		this.np = np;
	}

	public BillDefiProcess getBp() {
		return bp;
	}

	public void setBp(BillDefiProcess bp) {
		this.bp = bp;
	}

	public void testQueryNodeRTByDocidAndFlowid() throws Exception {
		 Collection<NodeRT> noderts = np.doQuery("", "567e3e38-0b05-4f89-a306-0b213910805a");
		 for (Iterator<NodeRT> iter = noderts.iterator(); iter.hasNext();) {
			NodeRT nodert = (NodeRT) iter.next();
			Collection<ActorRT> actorrts = nodert.getActorrts();
			for (Iterator<ActorRT> iterator = actorrts.iterator(); iterator.hasNext();) {
				ActorRT actorrt = (ActorRT) iterator.next();
				assertNotNull(actorrt);
			}
		 }
	 }
	
	// public void testDoViewStringStringWebUser() {
	// fail("Not yet implemented");
	// }
	//
	// public void testDoQueryStringString() {
	// fail("Not yet implemented");
	// }
}
