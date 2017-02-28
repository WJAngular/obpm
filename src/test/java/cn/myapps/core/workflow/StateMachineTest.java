package cn.myapps.core.workflow;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import org.junit.Test;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainProcessBean;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserRoleSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.action.CalendarHelper;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

/**
 * 模拟多用户并发走流程的一个单元测试
 * @author Happy
 *
 */
public class StateMachineTest {
	
	private static final Logger log = Logger.getLogger("压力测试");
	
	//基础数据Id
	private static final String applicationid ="12e1-7a32-6e81e104-8112-3bbdf6cee8y6";
	private static final String domainid ="41e1-733b-6e81e804-8112-3bbdf6cee8y6";
	private static final String moduleid ="11w1-7332-6ee1e104-8112-vbbdf6cee8y6";
	private static final String formid ="1ce1-7332-6e83e104-8112-3bbdf6ceelyb";
	private static final String flowid ="11e1-1008-6e8abc04-8112-3bbdf6cee8y6";
	private static final String roleid ="11e1-7332-6e81e104-8112-3bbdf6cee8y6";
	private static final String depid ="11e1-7332-6edce104-8112-123456cee8y6";
	private static final String userid ="11e1-fe5b-68882f48-83b5-7902bd10980d";
	
	private static final int runtimeDataSource_dbtype =DataSource.DB_MYSQL;
	private static final String runtimeDataSource_driver ="com.mysql.jdbc.Driver";
	private static final String runtimeDataSource_url = "jdbc:mysql://localhost:3306/t_runtime?useUnicode=true&characterEncoding=utf8";
	private static final String runtimeDataSource_poolsize = "100";
	private static final String runtimeDataSource_timeout = "3600";
	private static final String runtimeDataSource_username = "root";
	private static final String runtimeDataSource_password = "";
	
//	//数据源
//	private static final int runtimeDataSource_dbtype =DataSource.DB_MYSQL;
//	private static final String runtimeDataSource_driver ="com.mysql.jdbc.Driver";
//	private static final String runtimeDataSource_url = "jdbc:mysql://localhost:3306/dev_rt2?useUnicode=true&characterEncoding=utf8";
//	private static final String runtimeDataSource_poolsize = "20";
//	private static final String runtimeDataSource_timeout = "3600";
//	private static final String runtimeDataSource_username = "root";
//	private static final String runtimeDataSource_password = "";
	
	//流程节点Id常量
	private static final String FLOW_StartNode = "1347606760683";
	private static final String FLOW_ManualNode_A = "1347606806306";
	private static final String FLOW_ManualNode_B = "1347606838267";
	private static final String FLOW_CompleteNode = "1347606865968";
	
	private static final Map<String, String> docParams = new HashMap<String, String>();
	private boolean isStop = false;
	private WebUser user;
	
	//测试调整常量
	private static final int maxThread = 50;//单元测试线程数
	private static final int testTime = 1;//测试时长（单位：分钟）
	private static final int delay = 1;//线程延时（单位：毫秒）
	
	private static volatile int count = 0;
	
	static{
		docParams.put("nickname", "happy");
		docParams.put("addr", "GuangZhou");
		docParams.put("_formid", formid);
		docParams.put("application", applicationid);
		docParams.put("applicationid", applicationid);
		docParams.put("domainid", domainid);
	}
	
	
	@Test
	public void test() throws Exception {
		
		createMyappsTemplate();//初始化测试模板
		
		getWebUser();//初始化用户（模拟登陆）

		for (int i = 0; i < maxThread; i++) {
			new UThread().start();
		}

		Thread.sleep(testTime * 60 * 1000);
		isStop = true;
		System.err.println("####################count###########----->"+count);
	}
	
	public class UThread extends Thread {

		public void run() {
			this.setName("User");
			while (!isStop) {
				try {
					//保存文档 &开启流程
					Document doc = doSaveDocumentAndStartWorkFlow(getParams(), getWebUser());
					String docId = doc.getId();
					//提交流程到下一节点 
					doFlow2NextNode(docId, getParams(), getWebUser(), FLOW_ManualNode_A, new String[]{FLOW_ManualNode_B}, FlowType.RUNNING2RUNNING_NEXT);
					//提交到结束节点
					doFlow2NextNode(docId, getParams(), getWebUser(), FLOW_ManualNode_B, new String[]{FLOW_CompleteNode}, FlowType.RUNNING2COMPLETE);
					
					Thread.sleep(delay);
					
				} catch (Exception e) {
					e.printStackTrace();
					
					this.destroy();
				} 
			}
			
		}
		
		
	}

	
	
	public Document doSaveDocumentAndStartWorkFlow(ParamsTable params,WebUser user) throws Exception{
		Document doc = null;
		try{
		FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationid);
		Form form = (Form) formProcess.doView(formid);
		doc = form.createDocument(params, user);
		params.setParameter("_flowid", flowid);
		process.doStartFlowOrUpdate(doc, params, user);
		count++;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
		return doc;
	}
	
	public void doFlow2NextNode(String docId,ParamsTable params,WebUser user,String currNodeid,String[] nextNodeIds ,String flowType) throws Exception {
		try {
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationid);
			Document doc = (Document) proxy.doView(docId);
			doc = proxy.doFlow(doc, params, currNodeid, nextNodeIds,flowType,"", user);
			count++;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
	}
	
	
	
	private ParamsTable getParams(){
		ParamsTable params = new ParamsTable();
		params.putAll(docParams);
		return params;
	}
	
	private WebUser getWebUser() throws Exception{
		if(user != null) return user;
		user =  new WebUser((UserVO)ProcessFactory.createProcess(UserProcess.class).doView(userid));
		return user;
		
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 创建测试软件模板
	 */
	public void createMyappsTemplate(){
		//1.创建数据源，创建软件
		//2.创建角色
		//4.创建模块
		//5.创建表单
		//6.创建流程
		//7.创建企业域
		//8.创建部门
		//9.创建用户
		try {
			if(ProcessFactory.createProcess(ApplicationProcess.class).doView(applicationid) ==null){
				this.createApplication();
				this.createRole();
				this.createMoudle();
				this.createForm();
				this.createBillDefiVO();
				this.createDomain();
				this.createDepartment();
				this.createUser();
				
				log.info("测试软件模板初始化完毕！");
			}else{
				log.warning("测试软件模板已经初始化！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 创建数据源，创建软件
	 * @throws Exception
	 */
	private void createApplication() throws Exception{
		try {
			
			DataSourceProcess dsProcess = (DataSourceProcess)ProcessFactory.createProcess(DataSourceProcess.class);
			ApplicationProcess appProcess = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
			
			DataSource ds = new DataSource();
			String dsId = Sequence.getSequence();
			ds.setApplicationid(applicationid);
			ds.setName("forStateMachineTest");
			ds.setId(dsId);
			ds.setDbType(runtimeDataSource_dbtype);
			ds.setDriverClass(runtimeDataSource_driver);
			ds.setUrl(runtimeDataSource_url);
			ds.setPoolsize(runtimeDataSource_poolsize);
			ds.setTimeout(runtimeDataSource_timeout);
			ds.setUsername(runtimeDataSource_username);
			ds.setPassword(runtimeDataSource_password);
			dsProcess.doCreate(ds);
			
			ApplicationVO app = new ApplicationVO();
			app.setId(applicationid);
			app.setName("性能测试"+new Random(1000).nextLong());
			app.setActivated(true);
			app.setDatasourceid(dsId);
			app.setDomainid(domainid);
			app.setDescription("单元测试-性能测试");
			appProcess.doCreate(app);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
		
	}
	
	/**
	 * 创建角色
	 * @throws Exception
	 */
	private void createRole() throws Exception{
		try {
			
			RoleProcess process = (RoleProcess)ProcessFactory.createProcess(RoleProcess.class);
			RoleVO vo = new RoleVO();
			vo.setId(roleid);
			vo.setApplicationid(applicationid);
			vo.setName("测试角色A");
			process.doCreate(vo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
	}
	
	private void createMoudle() throws Exception {
		try {
			
			ModuleProcess process = (ModuleProcess)ProcessFactory.createProcess(ModuleProcess.class);
			ModuleVO vo = new ModuleVO();
			vo.setId(moduleid);
			vo.setApplicationid(applicationid);
			ApplicationVO app = new ApplicationVO();
			app.setId(applicationid);
			vo.setApplication(app);
			vo.setName("性能测试模块");
			vo.setDescription("性能测试模块");
			process.doCreate(vo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
		
		
	}
	
	private void createForm() throws Exception {
		try {
		
			FormProcess process = (FormProcess)ProcessFactory.createProcess(FormProcess.class);
			Form form = new Form();
			form.setId(formid);
			form.setName("unit_test");
			form.setType(Form.FORM_TYPE_NORMAL);
			form.setApplicationid(applicationid);
			ModuleVO module = new ModuleVO();
			module.setId(moduleid);
			form.setModule(module);
			form.setActivityXML("<tree-set><no-comparator/></tree-set>");
			String template ="<table borderColor=\"#b4ccee\" cellSpacing=\"1\" cellPadding=\"1\" width=\"400\" border=\"0\">"
						    +"<tbody>"
						    +"<tr>"
						    +"<td>昵称：</td>"
						    +" <td><input id=\"11e1-fe36-52d0f3bd-9cc9-e18a1fa786e7\" class=\"cn.myapps.core.dynaform.form.ejb.InputField\" value=\"\" name=\"nickname\" classname=\"cn.myapps.core.dynaform.form.ejb.InputField\" text=\"text\" fieldType=\"VALUE_TYPE_VARCHAR\" textType=\"text\" fieldkeyevent=\"Tabkey\" numberPattern=\"\" borderType=\"false\" refreshOnChanged=\"false\" calculateOnRefresh=\"false\" mobile=\"true\" discript=\"\" editMode=\"01\" processDescription=\"[];[]\" valueScript=\"\" filtercondition=\"\" validateRule=\"\" hiddenScript=\"\" hiddenValue=\"\" hiddenPrintScript=\"\" printHiddenValue=\"\" readonlyScript=\"\" validateLibs=\"\" /></td>"
						    +"</tr>"
						    +"<tr>"
						    +"<td>地址：</td>"
						    +"<td><input id=\"11e1-fe36-5799ed7e-9cc9-e18a1fa786e7\" class=\"cn.myapps.core.dynaform.form.ejb.InputField\" value=\"\" name=\"addr\" classname=\"cn.myapps.core.dynaform.form.ejb.InputField\" text=\"text\" fieldType=\"VALUE_TYPE_VARCHAR\" textType=\"text\" fieldkeyevent=\"Tabkey\" numberPattern=\"\" borderType=\"false\" refreshOnChanged=\"false\" calculateOnRefresh=\"false\" mobile=\"true\" discript=\"\" editMode=\"01\" processDescription=\"[];[]\" valueScript=\"\" filtercondition=\"\" validateRule=\"\" hiddenScript=\"\" hiddenValue=\"\" hiddenPrintScript=\"\" printHiddenValue=\"\" readonlyScript=\"\" validateLibs=\"\" /></td>"
						    +"</tr>"
						    +"<tr>"
						    +" <td>&nbsp;</td>"
						    +"<td>&nbsp;</td>"
						    +"</tr>"
						    +"</tbody>"
						    +"</table>"
						    +"<p>&nbsp;</p>";
			form.setTemplatecontext(template);
			process.doCreate(form);
		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
		
	}
	
	private void createBillDefiVO() throws Exception{
		try{
			
			BillDefiProcess bp = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
			BillDefiVO flow = new BillDefiVO();
			flow.setId(flowid);
			flow.setApplicationid(applicationid);
			ModuleVO module = new ModuleVO();
			module.setId(moduleid);
			flow.setModule(module);
			flow.setSubject("性能测试流程");
			
			String template ="<cn.myapps.core.workflow.element.FlowDiagram>"
						+"<ACTION_NORMAL>0</ACTION_NORMAL>"
						+"<ACTION_REMOVE>1</ACTION_REMOVE>"
						+"<ACTION_ADD_ABORTNODE>16</ACTION_ADD_ABORTNODE>"
						+"<ACTION_ADD_AUTONODE>17</ACTION_ADD_AUTONODE>"
						+"<ACTION_ADD_COMPLETENODE>18</ACTION_ADD_COMPLETENODE>"
						+"<ACTION_ADD_MANUALNODE>19</ACTION_ADD_MANUALNODE>"
						+"<ACTION_ADD_STARTNODE>20</ACTION_ADD_STARTNODE>"
						+"<ACTION_ADD_SUSPENDNODE>21</ACTION_ADD_SUSPENDNODE>"
						+"<ACTION_ADD_TERMINATENODE>22</ACTION_ADD_TERMINATENODE>"
						+"<ACTION_ADD_SUBFLOW>23</ACTION_ADD_SUBFLOW>"
						+"<ACTION_ADD_RELATION>4096</ACTION_ADD_RELATION>"
						+"<ACTION_EDIT_NODE>268435472</ACTION_EDIT_NODE>"
						+"<ACTION_EDIT_RELATION>268439552</ACTION_EDIT_RELATION>"
						+"<ACTION_BREAK_LINE>1048576</ACTION_BREAK_LINE>"
						+"<flowstatus>16</flowstatus>"
						+"<flowpath></flowpath>"
						+"<deleteMSG>null</deleteMSG>"
						+"<width>2000</width>"
						+"<height>1536</height>"
						+"<_applicationid>null</_applicationid>"
						+"<_sessionid>null</_sessionid>"
						+"<TOP_ALIGNMENT>0.0</TOP_ALIGNMENT>"
						+"<CENTER_ALIGNMENT>0.5</CENTER_ALIGNMENT>"
						+"<BOTTOM_ALIGNMENT>1.0</BOTTOM_ALIGNMENT>"
						+"<LEFT_ALIGNMENT>0.0</LEFT_ALIGNMENT>"
						+"<RIGHT_ALIGNMENT>1.0</RIGHT_ALIGNMENT>"
						+"<WIDTH>1</WIDTH>"
						+"<HEIGHT>2</HEIGHT>"
						+"<PROPERTIES>4</PROPERTIES>"
						+"<SOMEBITS>8</SOMEBITS>"
						+"<FRAMEBITS>16</FRAMEBITS>"
						+"<ALLBITS>32</ALLBITS>"
						+"<ERROR>64</ERROR>"
						+"<ABORT>128</ABORT>"
						+"<cn.myapps.core.workflow.element.StartNode>"
						+"<id>1347606760683</id>"
						+"<name>开始</name>"
						+"<scale>0</scale>"
						+"<note></note>"
						+"<x>66</x>"
						+"<y>90</y>"
						+"<width>75</width>"
						+"<height>80</height>"
						+"<m_width>20</m_width>"
						+"<m_height>20</m_height>"
						+"<prenodeid></prenodeid>"
						+"<statelabel>开始</statelabel>"
						+"<backnodeid></backnodeid>"
						+"<formname></formname>"
						+"<fieldpermlist></fieldpermlist>"
						+"<isstartandnext>false</isstartandnext>"
						+"<_iscurrent>false</_iscurrent>"
						+"</cn.myapps.core.workflow.element.StartNode>"
						+"<cn.myapps.core.workflow.element.ManualNode>"
						+"<ACTOR_EDIT_MODE_DESIGN>0</ACTOR_EDIT_MODE_DESIGN>"
						+"<ACTOR_EDIT_MODE_CODE>1</ACTOR_EDIT_MODE_CODE>"
						+"<PASS_CONDITION_OR>0</PASS_CONDITION_OR>"
						+"<PASS_CONDITION_AND>1</PASS_CONDITION_AND>"
						+"<PASS_CONDITION_ORDERLY_AND>2</PASS_CONDITION_ORDERLY_AND>"
						+"<actorListScript></actorListScript>"
						+"<actorEditMode>0</actorEditMode>"
						+"<namelist>(R11e1-7332-6e81e104-8112-3bbdf6cee8y6|A;)</namelist>"
						+"<realnamelist></realnamelist>"
						+"<passcondition>0</passcondition>"
						+"<exceedaction></exceedaction>"
						+"<issetcurruser>false</issetcurruser>"
						+"<inputform></inputform>"
						+"<isgather>false</isgather>"
						+"<issplit>false</issplit>"
						+"<cBack>true</cBack>"
						+"<splitStartNode></splitStartNode>"
						+"<isFrontEdit>false</isFrontEdit>"
						+"<backType>0</backType>"
						+"<isToPerson>false</isToPerson>"
						+"<bnodelist></bnodelist>"
						+"<retracementEditMode>0</retracementEditMode>"
						+"<cRetracement>false</cRetracement>"
						+"<retracementScript></retracementScript>"
						+"<notificationStrategyJSON></notificationStrategyJSON>"
						+"<isCarbonCopy>false</isCarbonCopy>"
						+"<isSelectCirculator>false</isSelectCirculator>"
						+"<circulatorEditMode>0</circulatorEditMode>"
						+"<circulatorListScript></circulatorListScript>"
						+"<circulatorNamelist></circulatorNamelist>"
						+"<userList></userList>"
						+"<circulatorNamelistByUser></circulatorNamelistByUser>"
						+"<orgField>auditor</orgField>"
						+"<orgScope>superior</orgScope>"
						+"<isLimited>false</isLimited>"
						+"<timeLimitEditMode>0</timeLimitEditMode>"
						+"<timeLimitDay></timeLimitDay>"
						+"<timeLimitHour></timeLimitHour>"
						+"<timeLimitMinute></timeLimitMinute>"
						+"<timeLimitScript></timeLimitScript>"
						+"<id>1347606806306</id>"
						+"<name>节点A</name>"
						+"<scale>0</scale>"
						+"<note></note>"
						+"<x>223</x>"
						+"<y>90</y>"
						+"<width>75</width>"
						+"<height>80</height>"
						+"<m_width>20</m_width>"
						+"<m_height>20</m_height>"
						+"<prenodeid></prenodeid>"
						+"<statelabel>节点A</statelabel>"
						+"<backnodeid></backnodeid>"
						+"<formname></formname>"
						+"<fieldpermlist></fieldpermlist>"
						+"<isstartandnext>false</isstartandnext>"
						+"<_iscurrent>false</_iscurrent>"
						+"</cn.myapps.core.workflow.element.ManualNode>"
						+"<cn.myapps.core.workflow.element.ManualNode>"
						+"<ACTOR_EDIT_MODE_DESIGN>0</ACTOR_EDIT_MODE_DESIGN>"
						+"<ACTOR_EDIT_MODE_CODE>1</ACTOR_EDIT_MODE_CODE>"
						+"<PASS_CONDITION_OR>0</PASS_CONDITION_OR>"
						+"<PASS_CONDITION_AND>1</PASS_CONDITION_AND>"
						+"<PASS_CONDITION_ORDERLY_AND>2</PASS_CONDITION_ORDERLY_AND>"
						+"<actorListScript></actorListScript>"
						+"<actorEditMode>0</actorEditMode>"
						+"<namelist>(R11e1-7332-6e81e104-8112-3bbdf6cee8y6|A;)</namelist>"
						+"<realnamelist></realnamelist>"
						+"<passcondition>0</passcondition>"
						+"<exceedaction></exceedaction>"
						+"<issetcurruser>false</issetcurruser>"
						+"<inputform></inputform>"
						+"<isgather>false</isgather>"
						+"<issplit>false</issplit>"
						+"<cBack>true</cBack>"
						+"<splitStartNode></splitStartNode>"
						+"<isFrontEdit>false</isFrontEdit>"
						+"<backType>0</backType>"
						+"<isToPerson>false</isToPerson>"
						+"<bnodelist></bnodelist>"
						+"<retracementEditMode>0</retracementEditMode>"
						+"<cRetracement>false</cRetracement>"
						+"<retracementScript></retracementScript>"
						+"<notificationStrategyJSON></notificationStrategyJSON>"
						+"<isCarbonCopy>false</isCarbonCopy>"
						+"<isSelectCirculator>false</isSelectCirculator>"
						+"<circulatorEditMode>0</circulatorEditMode>"
						+"<circulatorListScript></circulatorListScript>"
						+"<circulatorNamelist></circulatorNamelist>"
						+"<userList></userList>"
						+"<circulatorNamelistByUser></circulatorNamelistByUser>"
						+"<orgField>auditor</orgField>"
						+"<orgScope>superior</orgScope>"
						+"<isLimited>false</isLimited>"
						+"<timeLimitEditMode>0</timeLimitEditMode>"
						+"<timeLimitDay></timeLimitDay>"
						+"<timeLimitHour></timeLimitHour>"
						+"<timeLimitMinute></timeLimitMinute>"
						+"<timeLimitScript></timeLimitScript>"
						+"<id>1347606838267</id>"
						+"<name>节点B</name>"
						+"<scale>0</scale>"
						+"<note></note>"
						+"<x>339</x>"
						+"<y>88</y>"
						+"<width>75</width>"
						+"<height>80</height>"
						+"<m_width>20</m_width>"
						+"<m_height>20</m_height>"
						+"<prenodeid></prenodeid>"
						+"<statelabel>节点B</statelabel>"
						+"<backnodeid></backnodeid>"
						+"<formname></formname>"
						+"<fieldpermlist></fieldpermlist>"
						+"<isstartandnext>false</isstartandnext>"
						+"<_iscurrent>false</_iscurrent>"
						+"</cn.myapps.core.workflow.element.ManualNode>"
						+"<cn.myapps.core.workflow.element.CompleteNode>"
						+"<id>1347606865968</id>"
						+"<name>完成</name>"
						+"<scale>0</scale>"
						+"<note></note>"
						+"<x>465</x>"
						+"<y>88</y>"
						+"<width>75</width>"
						+"<height>80</height>"
						+"<m_width>20</m_width>"
						+"<m_height>20</m_height>"
						+"<prenodeid></prenodeid>"
						+"<statelabel>完成</statelabel>"
						+"<backnodeid></backnodeid>"
						+"<formname></formname>"
						+"<fieldpermlist></fieldpermlist>"
						+"<isstartandnext>false</isstartandnext>"
						+"<_iscurrent>false</_iscurrent>"
						+"</cn.myapps.core.workflow.element.CompleteNode>"
						+"<cn.myapps.core.workflow.element.Relation>"
						+"<state></state>"
						+"<startnodeid>1347606760683</startnodeid>"
						+"<endnodeid>1347606806306</endnodeid>"
						+"<ispassed>false</ispassed>"
						+"<isreturn>false</isreturn>"
						+"<condition></condition>"
						+"<filtercondition></filtercondition>"
						+"<editMode></editMode>"
						+"<processDescription></processDescription>"
						+"<action></action>"
						+"<pointstack>66;90;223;90</pointstack>"
						+"<EDITMODE_VIEW>00</EDITMODE_VIEW>"
						+"<EDITMODE_CODE>01</EDITMODE_CODE>"
						+"<validateScript></validateScript>"
						+"<id>1347606878497</id>"
						+"<name></name>"
						+"<scale>0</scale>"
						+"<note></note>"
						+"</cn.myapps.core.workflow.element.Relation>"
						+"<cn.myapps.core.workflow.element.Relation>"
						+"<state></state>"
						+"<startnodeid>1347606806306</startnodeid>"
						+"<endnodeid>1347606838267</endnodeid>"
						+"<ispassed>false</ispassed>"
						+"<isreturn>false</isreturn>"
						+"<condition></condition>"
						+"<filtercondition></filtercondition>"
						+"<editMode></editMode>"
						+"<processDescription></processDescription>"
						+"<action></action>"
						+"<pointstack>223;90;339;88</pointstack>"
						+"<EDITMODE_VIEW>00</EDITMODE_VIEW>"
						+"<EDITMODE_CODE>01</EDITMODE_CODE>"
						+"<validateScript></validateScript>"
						+"<id>1347606881231</id>"
						+"<name></name>"
						+"<scale>0</scale>"
						+"<note></note>"
						+"</cn.myapps.core.workflow.element.Relation>"
						+"<cn.myapps.core.workflow.element.Relation>"
						+"<state></state>"
						+"<startnodeid>1347606838267</startnodeid>"
						+"<endnodeid>1347606865968</endnodeid>"
						+"<ispassed>false</ispassed>"
						+"<isreturn>false</isreturn>"
						+"<condition></condition>"
						+"<filtercondition></filtercondition>"
						+"<editMode></editMode>"
						+"<processDescription></processDescription>"
						+"<action></action>"
						+"<pointstack>339;88;461;90</pointstack>"
						+"<EDITMODE_VIEW>00</EDITMODE_VIEW>"
						+"<EDITMODE_CODE>01</EDITMODE_CODE>"
						+"<validateScript></validateScript>"
						+"<id>1347606883547</id>"
						+"<name></name>"
						+"<scale>0</scale>"
						+"<note></note>"
						+"</cn.myapps.core.workflow.element.Relation>"
						+"</cn.myapps.core.workflow.element.FlowDiagram>";
			flow.setFlow(template);
			bp.doCreate(flow);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
	}
	
	/**
	 * 创建企业域
	 * @throws Exception
	 */
	private void createDomain() throws Exception {
		try{
			DomainProcess process = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			ApplicationProcess appProcess = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
			DomainVO vo = new DomainVO();
			vo.setId(domainid);
			vo.setName("性能测试"+new Random(1000).nextLong());
			Collection<ApplicationVO> apps = new HashSet<ApplicationVO>();
			apps.add((ApplicationVO) appProcess.doView(applicationid));
			vo.setApplications(apps);
			CalendarHelper cldHelper = new CalendarHelper();
			cldHelper.createCalendarByDomain(vo.getId());
			process.doCreate(vo);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
	}
	
	private void createDepartment() throws Exception {
		try{
		DepartmentProcess depProcess = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		DepartmentVO dep = new DepartmentVO();
		dep.setId(depid);
		dep.setName("性能测试部门");
		dep.setDomainid(domainid);
		dep.setDomain((DomainVO) new DomainProcessBean().doView(domainid));
		depProcess.doCreate(dep);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
	}
	
	private void createUser() throws Exception {
		try{
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			
			UserVO user = new UserVO();
			user.setId(userid);
			user.setName("testusera");
			user.setApplicationid(applicationid);
			user.setDomainid(domainid);
			user.setLoginno("testusera");
			user.setLoginpwd("teemlink");
			user.setDefaultApplication(applicationid);
			user.setDefaultDepartment(depid);
			user.setStatus(1);
			user.setLockFlag(1);
			
			UserDepartmentSet userDepartmentSet = new UserDepartmentSet(user.getId(), depid);
			user.getUserDepartmentSets().add(userDepartmentSet);
			
			UserRoleSet userRoleSetSet = new UserRoleSet(user.getId(), roleid);
			user.getUserRoleSets().add(userRoleSetSet);
			 
			 
			process.doCreate(user);
			
		
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
	}
	
	private void createUser(String id) throws Exception {
		try{
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			
			UserVO user = new UserVO();
			user.setId(id);
			user.setName("testusera"+System.currentTimeMillis());
			user.setApplicationid(applicationid);
			user.setDomainid(domainid);
			user.setLoginno("testusera"+System.currentTimeMillis());
			user.setLoginpwd("teemlink");
			user.setDefaultApplication(applicationid);
			user.setDefaultDepartment(depid);
			user.setStatus(1);
			user.setLockFlag(1);
			
			UserDepartmentSet userDepartmentSet = new UserDepartmentSet(user.getId(), depid);
			user.getUserDepartmentSets().add(userDepartmentSet);
			
			UserRoleSet userRoleSetSet = new UserRoleSet(user.getId(), roleid);
			user.getUserRoleSets().add(userRoleSetSet);
			 
			 
			process.doCreate(user);
			Thread.sleep(2);
			
		
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			PersistenceUtils.closeSessionAndConnection();
		}
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			StateMachineTest test = new StateMachineTest();
			for (int i = 0; i < 20; i++) {
				String id = new Random(100).nextLong()+UUID.randomUUID().toString();
				test.createUser(id);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
