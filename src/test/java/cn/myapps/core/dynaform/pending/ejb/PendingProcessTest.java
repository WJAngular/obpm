package cn.myapps.core.dynaform.pending.ejb;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;

public class PendingProcessTest extends TestCase {
	private final static String OBPM_APPLICATION_ID = "01b56341-3591-5e20-b09f-3f30bfefdcda";
	private final static String DOC_ID = "01b579c1-986e-bd80-8ffa-a401a0972036";
	PendingProcess process;
	DocumentProcess docProcess;
	UserProcess userProcess;

	protected void setUp() throws Exception {
		super.setUp();
		process = new PendingProcessBean(OBPM_APPLICATION_ID);
		docProcess = new DocumentProcessBean(OBPM_APPLICATION_ID);
		userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCRUD() throws Exception {
		try {
			Document doc = (Document) docProcess.doView(DOC_ID);
			PendingVO pending = new PendingVO();
			BeanUtils.copyProperties(pending, doc);
			process.doCreate(pending);

			pending = (PendingVO) process.doView(DOC_ID);
			pending.setSummary("My Sumarry");
			process.doUpdate(pending);

			assertNotNull(pending);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			process.doRemove(DOC_ID);
		}
	}

	public void testDoCreateByDocument() throws Exception {
		Document doc = (Document) docProcess.doView(DOC_ID);
		process.doCreateOrRemoveByDocument(doc,null);

		UserVO user = (UserVO) userProcess
				.doView("01b573fe-b1f5-7e60-af90-0aa773c14b3b");
		ParamsTable params = new ParamsTable();
		params.setParameter("formid", "0001");
		DataPackage<PendingVO> dataPackage = process.doQueryByFilter(params, new WebUser(
				user));
		assertEquals(1, dataPackage.datas.size());
		// UserVO user = (UserVO) userProcess
		// .doView("01b573fe-b1f5-7e60-af90-0aa773c14b3b");
		// ParamsTable params = new ParamsTable();
		// params.setParameter("formid", "0001");
		// DataPackage dataPackage = process.doQueryByForm(params, new WebUser(
		// user));
		// assertEquals(1, dataPackage.datas.size());
	}
}
