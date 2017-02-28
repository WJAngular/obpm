package cn.myapps.base;

import cn.myapps.core.deploy.application.ejb.ApplicationProcessBean;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcessBean;
import cn.myapps.util.sequence.Sequence;

public abstract class AbstractRuntimeProcessBeanTest {

	private ApplicationProcessBean applicationProcess;
	private ApplicationVO applicationVO;

	protected AbstractRuntimeProcessBeanTest() throws Exception {

		DataSourceProcessBean dataSourceProcess = new DataSourceProcessBean();
		DataSource dataSourceVO = new DataSource();
		dataSourceVO.setId(Sequence.getSequence());
		// dataSourceVO.setApplicationid(applicationid)
		dataSourceVO.setDbType(DataSource.DB_HSQL);
		dataSourceVO.setDriverClass("org.hsqldb.jdbcDriver");
		dataSourceVO.setUrl("jdbc:hsqldb:obpm");
		dataSourceVO.setUsername("sa");
		dataSourceVO.setPassword("");
		dataSourceProcess.doCreate(dataSourceVO);

		applicationProcess = new ApplicationProcessBean();
		applicationVO = new ApplicationVO();
		applicationVO.setId(Sequence.getSequence());
		applicationVO.setName("TEST");
		applicationVO.setDescription("TEST description");
		applicationVO.setDatasourceid(dataSourceVO.getId());

		applicationProcess.doCreate(applicationVO);
	}

	protected String getApplicationId() {
		return applicationVO.getId();
	}
}
