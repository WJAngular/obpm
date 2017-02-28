package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.table.constants.ConfirmConstant;
import cn.myapps.core.table.model.NeedConfirmException;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;

public class FormProcessBeanTest extends TestCase {

	/**
	 * varchar field
	 */
	private static final String FIELD_00001 = "<P>" + "<INPUT id=00001 name=text"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.InputField\"" + " numberPattern" + " datePattern"
			+ " discript" + " textType=\"text\"" + " refreshOnChanged=\"false\" " + " valueScript" + " validateRule"
			+ " hiddenScript" + " hiddenPrintScript" + " fieldtype=\"VALUE_TYPE_VARCHAR\"" + " dialogView"
			+ " selectDate=\"false\"" + " fieldkeyevent=\"Enterkey\" " + " readonlyScript" + " popToChoice=\"false\""
			+ " validateLibs" + " calculateOnRefresh=\"false\">" + "</P>";

	/**
	 * number field
	 */
	private static final String FIELD_00002 = "<P>" + "<INPUT id=00002 name=number"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.InputField\"" + " numberPattern" + " datePattern"
			+ " discript" + " textType=\"text\"" + " refreshOnChanged=\"false\"" + " valueScript" + " validateRule"
			+ " hiddenScript" + " hiddenPrintScript " + " fieldtype=\"VALUE_TYPE_NUMBER\""
			+ " dialogView selectDate=\"false\" " + " fieldkeyevent=\"Enterkey\"" + " readonlyScript"
			+ " popToChoice=\"false\" " + " validateLibs" + " calculateOnRefresh=\"false\">" + "</P>";

	/**
	 * date field
	 */
	private static final String FIELD_00003 = "<P>" + "<INPUT id=00003 name=date"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.InputField\"" + " numberPattern=\"\""
			+ " datePattern=\"\"" + " discript=\"\"" + " textType=\"text\"" + " refreshOnChanged=\"false\" "
			+ " valueScript=\"\"" + " validateRule=\"\"" + " hiddenScript=\"\"" + " hiddenPrintScript"
			+ " fieldtype=\"VALUE_TYPE_DATE\"" + " dialogView=\"\"" + " selectDate=\"false\" "
			+ " fieldkeyevent=\"Enterkey\"" + " readonlyScript=\"\"" + " popToChoice=\"false\" " + " validateLibs=\"\""
			+ " calculateOnRefresh=\"false\">" + "</P>";

	/**
	 * clob field
	 */
	private static final String FIELD_00004 = "<P>" + "<TEXTAREA id=00004 name=clob"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.TextareaField\"" + " discript"
			+ " refreshOnChanged=\"false\"" + " valueScript" + " validateRule" + " hiddenScript" + " hiddenPrintScript"
			+ " fieldtype=\"VALUE_TYPE_TEXT\"" + " validateLibs" + " calculateOnRefresh=\"false\">" + "</TEXTAREA>"
			+ "</P>";

	private static final String OLD_FORM_TEMPLATE_CONTEXT = FIELD_00001 + FIELD_00002 + FIELD_00003 + FIELD_00004;

	protected FormProcess formPross;

	protected DocumentProcess docPross;

	protected void setUp() throws Exception {
		super.setUp();
		formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);

		String applicationid = "";
		docPross = new DocumentProcessBean(applicationid);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSyncMappingDatas() {
		try {
			FormProcessBean process = new FormProcessBean();
			Form form = (Form) process.doView("11de-ede6-adf6d7cf-ad3d-d7f7c756c2f8");
			process.syncMappingDatas(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testCreateForm() {
		Form form = getOldForm();
		// 1. At first, validate change
		try {
			formPross.doChangeValidate(form);
		} catch (Exception unexpected) {
			assertFalse(unexpected instanceof NeedConfirmException);
		}
		// 2. Do create
		try {
			formPross.doCreate(form);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 3. After do create,validate change
		Confirm confirm = null;
		Form otherForm = getOldForm();
		try {
			// get new ValueObject with same name
			otherForm = getOldForm();
			otherForm.setId("000002");
			formPross.doChangeValidate(otherForm);
		} catch (NeedConfirmException expected) {
			Collection<Confirm> confirms = expected.getConfirms();
			confirm = (Confirm) ((ArrayList<Confirm>) confirms).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(confirm);
		// dynamic table exists
		assertEquals(confirm.getMsgKeyCode(), ConfirmConstant.FORM_EXIST);

		// 4. Sets new form name,and then validate change
		try {
			otherForm.setName("NEW_TEST");
			formPross.doChangeValidate(otherForm);
		} catch (Exception unexpected) {
			assertFalse(unexpected instanceof NeedConfirmException);
		}

		// 5. Do remove
		try {
			formPross.doRemove(form.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testUpdateForm() {

	}

	public void testDropField() {
		Form form = getOldForm();
		// 1. Do create
		try {
			formPross.doChangeValidate(form);
			formPross.doCreate(form);
			ParamsTable params = new ParamsTable();
			params.setParameter("text", "nicholas");
			params.setParameter("number", Integer.valueOf(0));
			params.setParameter("date", "2008-1-3");
			params.setParameter("blob", "remark");
			WebUser webUser = new WebUser(new UserVO());

			Document doc = form.createDocument(params, webUser);
			docPross.doCreate(doc, webUser);
		} catch (Exception unexpected) {
			assertFalse(unexpected instanceof NeedConfirmException);
		}

		// 5. Do remove
		try {
			formPross.doRemove(form.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Form getOldForm() {
		Form oldForm = new Form();
		oldForm.setId("000001");
		oldForm.setName("TEST");
		oldForm.setTemplatecontext(OLD_FORM_TEMPLATE_CONTEXT);
		oldForm.setType(Form.FORM_TYPE_NORMAL);

		return oldForm;
	}
}
