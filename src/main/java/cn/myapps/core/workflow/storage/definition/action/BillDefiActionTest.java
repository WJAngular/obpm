package cn.myapps.core.workflow.storage.definition.action;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import cn.myapps.base.action.BaseAction;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;

public class BillDefiActionTest extends TestCase {
	BillDefiAction action;

	Map<String, Object> params = new HashMap<String, Object>();

	protected void setUp() throws Exception {
		super.setUp();
		action = new BillDefiAction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAction() throws Exception {
		String id = doSave();
		doEdit(id);
		doDelete(id);
	}

	/*
	 * Test method for 'cn.myapps.core.dynaform.form.action.FormAction.doSave()'
	 */
	public String doSave() throws Exception {
		BillDefiVO vo = new BillDefiVO();
		// PersistenceUtils.getSessionSignal().sessionSignal++;
		vo.setSubject("testBillDefiVO");
		vo.setFlow("<cn.myapps.core.workflow.element.FlowDiagram>\n"
				+ "<ACTION_NORMAL>0</ACTION_NORMAL>\n"
				+ "<ACTION_REMOVE>1</ACTION_REMOVE>\n"
				+ "<ACTION_ADD_ABORTNODE>16</ACTION_ADD_ABORTNODE>\n"
				+ "<ACTION_ADD_AUTONODE>17</ACTION_ADD_AUTONODE>\n"
				+ "<ACTION_ADD_COMPLETENODE>18</ACTION_ADD_COMPLETENODE>\n"
				+ "<ACTION_ADD_MANUALNODE>19</ACTION_ADD_MANUALNODE>\n"
				+ "<ACTION_ADD_STARTNODE>20</ACTION_ADD_STARTNODE>\n"
				+ "<ACTION_ADD_SUSPENDNODE>21</ACTION_ADD_SUSPENDNODE>\n"
				+ "<ACTION_ADD_TERMINATENODE>22</ACTION_ADD_TERMINATENODE>\n"
				+ "<ACTION_ADD_RELATION>4096</ACTION_ADD_RELATION>\n"
				+ "<ACTION_EDIT_NODE>268435472</ACTION_EDIT_NODE>\n"
				+ "<ACTION_EDIT_RELATION>268439552</ACTION_EDIT_RELATION>\n"
				+ "<ACTION_BREAK_LINE>1048576</ACTION_BREAK_LINE>\n"
				+ "<flowstatus>16</flowstatus>\n" + "<flowpath></flowpath>\n"
				+ "<deleteMSG>null</deleteMSG>\n"
				+ "<TOP_ALIGNMENT>0.0</TOP_ALIGNMENT>\n"
				+ "<CENTER_ALIGNMENT>0.5</CENTER_ALIGNMENT>\n"
				+ "<BOTTOM_ALIGNMENT>1.0</BOTTOM_ALIGNMENT>\n"
				+ "<LEFT_ALIGNMENT>0.0</LEFT_ALIGNMENT>\n"
				+ "<RIGHT_ALIGNMENT>1.0</RIGHT_ALIGNMENT>\n"
				+ "<WIDTH>1</WIDTH>\n" + "<HEIGHT>2</HEIGHT>\n"
				+ "<PROPERTIES>4</PROPERTIES>\n" + "<SOMEBITS>8</SOMEBITS>\n"
				+ "<FRAMEBITS>16</FRAMEBITS>\n" + "<ALLBITS>32</ALLBITS>\n"
				+ "<ERROR>64</ERROR>\n" + "<ABORT>128</ABORT>\n"
				+ "<cn.myapps.core.workflow.element.StartNode>\n"
				+ "<prenodeid></prenodeid>\n" + "<backnodeid></backnodeid>\n"
				+ "<formname></formname>\n"
				+ "<fieldpermlist></fieldpermlist>\n"
				+ "<isstartandnext>false</isstartandnext>\n" + "<x>68</x>\n"
				+ "<y>46</y>\n" + "<width>46</width>\n"
				+ "<height>55</height>\n" + "<_iscurrent>false</_iscurrent>\n"
				+ "<scale>0</scale>\n" + "<name>start</name>\n"
				+ "<note></note>\n" + "<id>1177556151406</id>\n"
				+ "</cn.myapps.core.workflow.element.StartNode>\n"
				+ "<cn.myapps.core.workflow.element.ManualNode>\n"
				+ "<remaindertype>0</remaindertype>\n"
				+ "<beforetime></beforetime>\n"
				+ "<namelist>(U1172662443515001|nicholas;)</namelist>\n"
				+ "<realnamelist></realnamelist>\n"
				+ "<passcondition>0</passcondition>\n"
				+ "<exceedaction>0</exceedaction>\n"
				+ "<limittimecount></limittimecount>\n"
				+ "<issetcurruser>false</issetcurruser>\n"
				+ "<inputform></inputform>\n" + "<isgather>false</isgather>\n"
				+ "<issplit>false</issplit>\n" + "<prenodeid></prenodeid>\n"
				+ "<backnodeid></backnodeid>\n" + "<formname></formname>\n"
				+ "<fieldpermlist></fieldpermlist>\n"
				+ "<isstartandnext>false</isstartandnext>\n" + "<x>199</x>\n"
				+ "<y>125</y>\n" + "<width>46</width>\n"
				+ "<height>55</height>\n" + "<_iscurrent>false</_iscurrent>\n"
				+ "<scale>0</scale>\n" + "<name>node1</name>\n"
				+ "<note></note>\n" + "<id>1177556160531</id>\n"
				+ "</cn.myapps.core.workflow.element.ManualNode>\n"
				+ "<cn.myapps.core.workflow.element.Relation>\n"
				+ "<state></state>\n"
				+ "<startnodeid>1177556151406</startnodeid>\n"
				+ "<endnodeid>1177556160531</endnodeid>\n"
				+ "<ispassed>false</ispassed>\n"
				+ "<isreturn>false</isreturn>\n" + "<condition></condition>\n"
				+ "<action></action>\n"
				+ "<pointstack>64;69;198;117</pointstack>\n"
				+ "<validateScript></validateScript>\n" + "<scale>0</scale>\n"
				+ "<name></name>\n" + "<note></note>\n"
				+ "<id>1177556266593</id>\n"
				+ "</cn.myapps.core.workflow.element.Relation>\n"
				+ "<cn.myapps.core.workflow.element.CompleteNode>\n"
				+ "<prenodeid></prenodeid>\n" + "<backnodeid></backnodeid>\n"
				+ "<formname></formname>\n"
				+ "<fieldpermlist></fieldpermlist>\n"
				+ "<isstartandnext>false</isstartandnext>\n" + "<x>341</x>\n"
				+ "<y>69</y>\n" + "<width>46</width>\n"
				+ "<height>55</height>\n" + "<_iscurrent>false</_iscurrent>\n"
				+ "<scale>0</scale>\n" + "<name>end</name>\n"
				+ "<note></note>\n" + "<id>1177556293437</id>\n"
				+ "</cn.myapps.core.workflow.element.CompleteNode>\n"
				+ "<cn.myapps.core.workflow.element.Relation>\n"
				+ "<state></state>\n"
				+ "<startnodeid>1177556160531</startnodeid>\n"
				+ "<endnodeid>1177556293437</endnodeid>\n"
				+ "<ispassed>false</ispassed>\n"
				+ "<isreturn>false</isreturn>\n" + "<condition></condition>\n"
				+ "<action></action>\n"
				+ "<pointstack>196;106;335;78</pointstack>\n"
				+ "<validateScript></validateScript>\n" + "<scale>0</scale>\n"
				+ "<name></name>\n" + "<note></note>\n"
				+ "<id>1177556296828</id>\n"
				+ "</cn.myapps.core.workflow.element.Relation>\n"
				+ "</cn.myapps.core.workflow.element.FlowDiagram>\n");
		action.setContent(vo);
		// PersistenceUtils.getSessionSignal().sessionSignal--;
		action.doSave();

		// PersistenceUtils.getSessionSignal().sessionSignal++;
		params.put("id", vo.getId());
		BaseAction.getContext().setParameters(params);
		// PersistenceUtils.getSessionSignal().sessionSignal--;
		action.doView();
		BillDefiVO findVO = (BillDefiVO) action.getContent();

		assertEquals(findVO.getSubject(), vo.getSubject());

		return findVO.getId();
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doEdit()'
	 */
	public void doEdit(String id) throws Exception {
		// PersistenceUtils.getSessionSignal().sessionSignal++;
		params.put("id", new String[] { id });
		BaseAction.getContext().setParameters(params);
		action.doView();
		BillDefiVO oldy = (BillDefiVO) action.getContent();

		oldy.setSubject("NewTestBillDefiVO");
		action.setContent(oldy);
		action.doSave();

		BaseAction.getContext().setParameters(params);
		action.doView();
		BillDefiVO newly = (BillDefiVO) action.getContent();
		// PersistenceUtils.getSessionSignal().sessionSignal--;

		assertEquals(oldy.getSubject(), newly.getSubject());
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doDelete()'
	 */
	public void doDelete(String id) throws Exception {
		// PersistenceUtils.getSessionSignal().sessionSignal++;
		action.set_selects(new String[] { id });
		action.doDelete();

		params.put("id", new String[] { id });
		BaseAction.getContext().setParameters(params);
		action.doView();
		BillDefiVO find = (BillDefiVO) action.getContent();
		// PersistenceUtils.getSessionSignal().sessionSignal--;

		assertNull(find);
	}

}
