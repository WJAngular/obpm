package cn.myapps.core.dynaform.dts.datasource.action;

import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;
import java.util.Map;
import junit.framework.TestCase;

/**
 * @author  nicholas
 */
public class DatasourceHelperTest extends TestCase {

	DatasourceHelper helper = null;

	public static void main(String[] args) {
	}

	protected void setUp() throws Exception {
		super.setUp();
		helper = new DatasourceHelper();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for
	 * 'cn.myapps.core.dynaform.dts.datasource.action.DatasourceHelper.getAllDatasource()'
	 */
	public void testGetAllDatasource() throws Exception {
		Map<String, String> map = helper.getAllDatasource(null);
		DataSourceProcess dp = (DataSourceProcess) ProcessFactory
				.createProcess(DataSourceProcess.class);
		DataSource ds = new DataSource();
		ds.setId(Sequence.getSequence());
		ds.setName("test");
		dp.doUpdate(ds);
		Map<String, String> map1 = helper.getAllDatasource(null);
		if (map == null)
			assertEquals(1, map1.size());
		else
			assertEquals(map.size() + 1, map1.size());
		dp.doRemove(ds.getId());
	}

}
