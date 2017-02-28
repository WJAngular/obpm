package cn.myapps.core.dynaform.dts.exp.mappingconfig.action;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;
import cn.myapps.base.action.BaseAction;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.core.dynaform.dts.exp.columnmapping.ejb.ColumnMapping;
import cn.myapps.core.dynaform.dts.exp.columnmapping.ejb.ColumnMappingProcess;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

/**
 * @author  nicholas
 */
public class MappingConfigActionTest extends TestCase {

	MappingConfigAction action;
	MappingConfig vo;
	String name;
    DataSource ds;
	DataSourceProcess dp; 
	String _ds;
	ColumnMapping cm;
	ColumnMappingProcess cp;
	Document doc;
	Document doc1;
	DocumentProcess proxy;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void initDate() throws Exception{
		
		proxy=(DocumentProcess)ProcessFactory.createProcess(DocumentProcess.class);
		
		dp= (DataSourceProcess) ProcessFactory
		.createProcess(DataSourceProcess.class);
		cp= (ColumnMappingProcess) ProcessFactory
		.createProcess(ColumnMappingProcess.class);
		action=new MappingConfigAction();
		vo=new MappingConfig();
	 
		 ds=new DataSource();
			ds.setName("MyDataSource");
			ds.setDriverClass("oracle.jdbc.driver.OracleDriver");
			ds.setUrl("jdbc:oracle:thin:@192.168.0.170:1521:XE");
			ds.setUsername("wpigb4");
			ds.setPassword("helloworld");
		dp= (DataSourceProcess) ProcessFactory
		.createProcess(DataSourceProcess.class);
		
		
		
		cm=new ColumnMapping();
		doc=new Document();
		doc.setFormname("thrive");
		doc.addStringItem("username","peng");
		doc.addStringItem("password","987654");
		doc.setIstmp(false);
		doc.setLastmodified(new Date());
		
		doc1=new Document();
		doc1.setFormname("thrive");
		doc1.addStringItem("username","viss");
		doc1.addStringItem("password","123456");
		doc1.setIstmp(false);
		doc1.setLastmodified(new Date());
		
//		proxy.doCreate(doc);
//		proxy.doCreate(doc1);
		
		dp.doCreate(ds);
		_ds=ds.getId();
	}
	
	
	/*
	 * Test method for 'cn.myapps.core.dynaform.dts.exp.mappingconfig.action.MappingConfigAction.doSave()'
	 */
	public void testDoSave() throws Exception {
		initDate();
		
//		//PersistenceUtils.getSessionSignal().sessionSignal++;
		action.set_ds(_ds);
		
	    name=Sequence.getSequence() + "mp";
		vo.setName(name);
	    vo.setDatasource(ds);
	    vo.setTablename("testExprot_ccda");
	    vo.setDescription("testExprot");
	    vo.setValuescript("$formname='thrive'");
	    action.setContent(vo);
	
	    action.doSave();
//		//PersistenceUtils.getSessionSignal().sessionSignal--;
		
		
		cm.setMappingConfig(vo);
	    cm.setFromName("username");
	    cm.setToName("username");
	    cm.setToType(ColumnMapping.DATA_TYPE_VARCHAR);
	    cm.setType(ColumnMapping.COLUMN_TYPE_FIELD);
	    cm.setLength("255");
	    cp.doCreate(cm);
		
		doView();
		doList();
		doEdit();
		
		exportAllDocument();
		incrementExportDocument();
		
		doDelete();
		
//		deleteRubbishData();
	}

	/*
	 * Test method for 'cn.myapps.core.dynaform.dts.exp.mappingconfig.action.MappingConfigAction.exportAllDocument()'
	 */
	public void exportAllDocument() throws Exception {
		String id = action.getContent().getId();
        
		action.set_selects(new String[] { id });
        action.exportAllDocument();
		//assertEquals(,"1");
	}

	/*
	 * Test method for 'cn.myapps.core.dynaform.dts.exp.mappingconfig.action.MappingConfigAction.IncrementExportDocument()'
	 */
	public void incrementExportDocument() throws Exception {

		doc.setLastmodified(new Date());
		proxy.doUpdate(doc);
		String id = action.getContent().getId();
		action.set_selects(new String[] { id });
        action.incrementExportDocument();
	//	assertEquals("1",action.getExportDocsNum());
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doEdit()'
	 */
	public void doEdit() throws Exception {
		String id = action.getContent().getId();

		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("id", new String[] { id });

		BaseAction.getContext().setParameters(mp);
		action.doEdit();
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doView()'
	 */
	public void doView() throws Exception {
		String id = action.getContent().getId();

		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("id", new String[] { id });

		BaseAction.getContext().setParameters(mp);
		action.doView();

		String afterViewId = action.getContent().getId();
		assertEquals(afterViewId, id);
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doDelete()'
	 */
	public void doDelete() throws Exception {
//		//PersistenceUtils.getSessionSignal().sessionSignal++;
		String id = action.getContent().getId();
		action.set_selects(new String[] { id });
		action.doDelete();
//		//PersistenceUtils.getSessionSignal().sessionSignal--;
	}

	/*
	 * Test method for 'cn.myapps.base.action.BaseAction.doList()'
	 */
	public void doList() throws Exception {
		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("s_name", name);
		BaseAction.getContext().setParameters(mp);
		action.doList();
		Collection<?> data = action.getDatas().datas;
		assertNotNull(data);
		MappingConfig ds = (MappingConfig) data.iterator().next();
		assertEquals(ds.getName(), name);
	}

/*	public void deleteRubbishData() throws Exception{
		
	
//		proxy.doRemove(doc.getId());
//		proxy.doRemove(doc1.getId());
		Statement stmt =null;
		Connection conn=rephelper.getConnection(ds);
		if(conn !=null)
			stmt = conn.createStatement();
		try {
			if(stmt !=null)
				stmt.execute("drop table testExprot_ccda");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
				conn = null;
			}
		}
		dp.doRemove(ds.getId());
	}*/
	                                
	
}
