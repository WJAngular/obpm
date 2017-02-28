package cn.myapps.core.workflow.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.action.DocumentAction;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.util.ProcessFactory;

public class StateMachineActionTest extends TestCase {

	UserVO user;

	Document doc;

	BillDefiVO flow;

	StateMachineAction action;

	DocumentAction docAction;

	protected void setUp() throws Exception {
		super.setUp();

		action = new StateMachineAction();
		docAction = new DocumentAction();
		Map<String, Object> session = new HashMap<String, Object>();

		UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		user = new UserVO();
		user.setName("nicholas");
		user.setEmail("zhen_001@163.com");
		user.setLoginno("nic");
		user.setLoginpwd("123456");
		user.setStatus(1);
		// user.setLanguageType(LanguageType.LANGUAGE_TYPE_ENGLISH);
		up.doCreate(user);

		session.put(Web.SESSION_ATTRIBUTE_USER, new WebUser(user));
		DocumentAction.getContext().setSession(session);
		DocumentAction.getContext().setSession(session);

		BillDefiProcess bp = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		flow = new BillDefiVO();
		flow.setSubject("testBillDefiVO");
		flow.setFlow("<cn.myapps.core.workflow.element.FlowDiagram>\n" + "<ACTION_NORMAL>0</ACTION_NORMAL>\n"
				+ "<ACTION_REMOVE>1</ACTION_REMOVE>\n" + "<ACTION_ADD_ABORTNODE>16</ACTION_ADD_ABORTNODE>\n"
				+ "<ACTION_ADD_AUTONODE>17</ACTION_ADD_AUTONODE>\n"
				+ "<ACTION_ADD_COMPLETENODE>18</ACTION_ADD_COMPLETENODE>\n"
				+ "<ACTION_ADD_MANUALNODE>19</ACTION_ADD_MANUALNODE>\n"
				+ "<ACTION_ADD_STARTNODE>20</ACTION_ADD_STARTNODE>\n"
				+ "<ACTION_ADD_SUSPENDNODE>21</ACTION_ADD_SUSPENDNODE>\n"
				+ "<ACTION_ADD_TERMINATENODE>22</ACTION_ADD_TERMINATENODE>\n"
				+ "<ACTION_ADD_RELATION>4096</ACTION_ADD_RELATION>\n"
				+ "<ACTION_EDIT_NODE>268435472</ACTION_EDIT_NODE>\n"
				+ "<ACTION_EDIT_RELATION>268439552</ACTION_EDIT_RELATION>\n"
				+ "<ACTION_BREAK_LINE>1048576</ACTION_BREAK_LINE>\n" + "<flowstatus>16</flowstatus>\n"
				+ "<flowpath></flowpath>\n" + "<deleteMSG>null</deleteMSG>\n" + "<TOP_ALIGNMENT>0.0</TOP_ALIGNMENT>\n"
				+ "<CENTER_ALIGNMENT>0.5</CENTER_ALIGNMENT>\n" + "<BOTTOM_ALIGNMENT>1.0</BOTTOM_ALIGNMENT>\n"
				+ "<LEFT_ALIGNMENT>0.0</LEFT_ALIGNMENT>\n" + "<RIGHT_ALIGNMENT>1.0</RIGHT_ALIGNMENT>\n"
				+ "<WIDTH>1</WIDTH>\n" + "<HEIGHT>2</HEIGHT>\n" + "<PROPERTIES>4</PROPERTIES>\n"
				+ "<SOMEBITS>8</SOMEBITS>\n" + "<FRAMEBITS>16</FRAMEBITS>\n" + "<ALLBITS>32</ALLBITS>\n"
				+ "<ERROR>64</ERROR>\n" + "<ABORT>128</ABORT>\n" + "<cn.myapps.core.workflow.element.StartNode>\n"
				+ "<prenodeid></prenodeid>\n" + "<backnodeid></backnodeid>\n" + "<formname></formname>\n"
				+ "<fieldpermlist></fieldpermlist>\n" + "<isstartandnext>false</isstartandnext>\n" + "<x>68</x>\n"
				+ "<y>46</y>\n" + "<width>46</width>\n" + "<height>55</height>\n" + "<_iscurrent>false</_iscurrent>\n"
				+ "<scale>0</scale>\n" + "<name>start</name>\n" + "<note></note>\n" + "<id>1177556151406</id>\n"
				+ "</cn.myapps.core.workflow.element.StartNode>\n" + "<cn.myapps.core.workflow.element.ManualNode>\n"
				+ "<remaindertype>0</remaindertype>\n" + "<beforetime></beforetime>\n" + "<namelist>(U"
				+ user.getId()
				+ "|nicholas;)</namelist>\n"
				+ "<realnamelist></realnamelist>\n"
				+ "<passcondition>0</passcondition>\n"
				+ "<exceedaction>0</exceedaction>\n"
				+ "<limittimecount></limittimecount>\n"
				+ "<issetcurruser>false</issetcurruser>\n"
				+ "<inputform></inputform>\n"
				+ "<isgather>false</isgather>\n"
				+ "<issplit>false</issplit>\n"
				+ "<prenodeid></prenodeid>\n"
				+ "<backnodeid></backnodeid>\n"
				+ "<formname></formname>\n"
				+ "<fieldpermlist></fieldpermlist>\n"
				+ "<isstartandnext>false</isstartandnext>\n"
				+ "<x>199</x>\n"
				+ "<y>125</y>\n"
				+ "<width>46</width>\n"
				+ "<height>55</height>\n"
				+ "<_iscurrent>false</_iscurrent>\n"
				+ "<scale>0</scale>\n"
				+ "<name>node1</name>\n"
				+ "<note></note>\n"
				+ "<id>1177556160531</id>\n"
				+ "</cn.myapps.core.workflow.element.ManualNode>\n"
				+ "<cn.myapps.core.workflow.element.Relation>\n"
				+ "<state></state>\n"
				+ "<startnodeid>1177556151406</startnodeid>\n"
				+ "<endnodeid>1177556160531</endnodeid>\n"
				+ "<ispassed>false</ispassed>\n"
				+ "<isreturn>false</isreturn>\n"
				+ "<condition></condition>\n"
				+ "<action></action>\n"
				+ "<pointstack>64;69;198;117</pointstack>\n"
				+ "<validateScript></validateScript>\n"
				+ "<scale>0</scale>\n"
				+ "<name></name>\n"
				+ "<note></note>\n"
				+ "<id>1177556266593</id>\n"
				+ "</cn.myapps.core.workflow.element.Relation>\n"
				+ "<cn.myapps.core.workflow.element.CompleteNode>\n"
				+ "<prenodeid></prenodeid>\n"
				+ "<backnodeid></backnodeid>\n"
				+ "<formname></formname>\n"
				+ "<fieldpermlist></fieldpermlist>\n"
				+ "<isstartandnext>false</isstartandnext>\n"
				+ "<x>341</x>\n"
				+ "<y>69</y>\n"
				+ "<width>46</width>\n"
				+ "<height>55</height>\n"
				+ "<_iscurrent>false</_iscurrent>\n"
				+ "<scale>0</scale>\n"
				+ "<name>end</name>\n"
				+ "<note></note>\n"
				+ "<id>1177556293437</id>\n"
				+ "</cn.myapps.core.workflow.element.CompleteNode>\n"
				+ "<cn.myapps.core.workflow.element.Relation>\n"
				+ "<state></state>\n"
				+ "<startnodeid>1177556160531</startnodeid>\n"
				+ "<endnodeid>1177556293437</endnodeid>\n"
				+ "<ispassed>false</ispassed>\n"
				+ "<isreturn>false</isreturn>\n"
				+ "<condition></condition>\n"
				+ "<action></action>\n"
				+ "<pointstack>196;106;335;78</pointstack>\n"
				+ "<validateScript></validateScript>\n"
				+ "<scale>0</scale>\n"
				+ "<name></name>\n"
				+ "<note></note>\n"
				+ "<id>1177556296828</id>\n"
				+ "</cn.myapps.core.workflow.element.Relation>\n" + "</cn.myapps.core.workflow.element.FlowDiagram>\n");
		bp.doCreate(flow);

		doc = new Document();
		doc.setFlowid(flow.getId());
		// dp.doCreate(doc);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		up.doRemove(user.getId());

		BillDefiProcess bp = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		bp.doRemove(flow.getId());

		DocumentProcess dp = (DocumentProcess) ProcessFactory.createProcess(DocumentProcess.class);
		dp.doRemove(doc.getId());
	}

	public StateMachineAction getAction() {
		return action;
	}

	public void setAction(StateMachineAction action) {
		this.action = action;
	}

	public void startFlow() throws Exception {
		// PersistenceUtils.getSessionSignal().sessionSignal++;
		docAction.set_docid(doc.getId());
		docAction.setContent(doc);
		docAction.set_flowid(flow.getId());
		// docAction.startFlow();
		// PersistenceUtils.getSessionSignal().sessionSignal--;
	}

	/*
	 * Test method for
	 * 'cn.myapps.core.workflow.engine.StateMachineHelper.toFlowHtmlText(String,
	 * String, WebUser)'
	 */
	public void testWorkFlow() throws Exception {
		startFlow();
		DocumentProcess dp = (DocumentProcess) ProcessFactory.createProcess(DocumentProcess.class);
		Document findDoc = (Document) dp.doView(doc.getId());
		assertEquals(findDoc.getState().getState(), FlowState.RUNNING);

		doNext();
		findDoc = (Document) dp.doView(doc.getId());
		assertEquals(findDoc.getState().getState(), FlowState.COMPLETE);
	}

	public void doNext() throws Exception {
		// PersistenceUtils.getSessionSignal().sessionSignal++;

		// Current Node
		NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, new WebUser(user),null);

		BillDefiProcess bp = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		BillDefiVO findFlow = (BillDefiVO) bp.doView(flow.getId());
		Collection<Node> nextNodeList = findFlow.getNextNodeList(nodert.getNodeid(),doc,null, new WebUser(user));
		assertTrue(nextNodeList.size() > 0);

		// Next Nodes
		String[] nextids = new String[nextNodeList.size()];
		int i = 0;
		for (Iterator<Node> iter = nextNodeList.iterator(); iter.hasNext();) {
			Node nextNode = (Node) iter.next();
			nextids[i] = nextNode.id;
			i++;
		}

		// action.set_nextids(nextids);
		// action.set_currid(nodert.getNodeid());
		// action.set_docid(doc.getId());
		// action.set_flowid(flow.getId());
		// action.set_flowType("");
		// action.set_attitude("");
		Environment evt = Environment.getInstance();
		evt.setApplicationRealPath("c:/");
		// StateMachine.doFlow(doc.getId(), findFlow, nodert.getNodeid(),
		// nextids,
		// new WebUser(user), "", "", evt);

		// PersistenceUtils.getSessionSignal().sessionSignal--;
	}
}
