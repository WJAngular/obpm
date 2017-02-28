package cn.myapps.core.dynaform.form.ddlutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import junit.framework.TestCase;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.form.ejb.Confirm;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.table.ddlutil.AbstractTableDefinition;
import cn.myapps.core.table.ddlutil.AbstractValidator;
import cn.myapps.core.table.ddlutil.ChangeLog;
import cn.myapps.core.table.ddlutil.oracle.OracleTableDefinition;
import cn.myapps.core.table.ddlutil.oracle.OracleValidator;
import cn.myapps.core.table.model.NeedConfirmException;

public class ValidationTest extends TestCase {

	/**
	 * varchar field
	 */
	private static final String FIELD_00001 = "<P>"
			+ "<INPUT id=00001 name=text"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.InputField\""
			+ " numberPattern" + " datePattern" + " discript"
			+ " textType=\"text\"" + " refreshOnChanged=\"false\" "
			+ " valueScript" + " validateRule" + " hiddenScript"
			+ " hiddenPrintScript" + " fieldtype=\"VALUE_TYPE_VARCHAR\""
			+ " dialogView" + " selectDate=\"false\""
			+ " fieldkeyevent=\"Enterkey\" " + " readonlyScript"
			+ " popToChoice=\"false\"" + " validateLibs"
			+ " calculateOnRefresh=\"false\">" + "</P>";

	/**
	 * number field
	 */
	private static final String FIELD_00002 = "<P>"
			+ "<INPUT id=00002 name=number"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.InputField\""
			+ " numberPattern" + " datePattern" + " discript"
			+ " textType=\"text\"" + " refreshOnChanged=\"false\""
			+ " valueScript" + " validateRule" + " hiddenScript"
			+ " hiddenPrintScript " + " fieldtype=\"VALUE_TYPE_NUMBER\""
			+ " dialogView selectDate=\"false\" "
			+ " fieldkeyevent=\"Enterkey\"" + " readonlyScript"
			+ " popToChoice=\"false\" " + " validateLibs"
			+ " calculateOnRefresh=\"false\">" + "</P>";

	/*
	 * modify field
	 */
	private static final String MODIFY_FIELD_00002 = "<P>"
			+ "<INPUT id=00002 name=text"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.InputField\""
			+ " numberPattern" + " datePattern" + " discript"
			+ " textType=\"text\"" + " refreshOnChanged=\"false\" "
			+ " valueScript" + " validateRule" + " hiddenScript"
			+ " hiddenPrintScript" + " fieldtype=\"VALUE_TYPE_DATE\""
			+ " dialogView" + " selectDate=\"false\""
			+ " fieldkeyevent=\"Enterkey\" " + " readonlyScript"
			+ " popToChoice=\"false\"" + " validateLibs"
			+ " calculateOnRefresh=\"false\">" + "</P>";

	/**
	 * date field
	 */
	private static final String FIELD_00003 = "<P>"
			+ "<INPUT id=00003 name=date"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.InputField\""
			+ " numberPattern=\"\"" + " datePattern=\"\"" + " discript=\"\""
			+ " textType=\"text\"" + " refreshOnChanged=\"false\" "
			+ " valueScript=\"\"" + " validateRule=\"\"" + " hiddenScript=\"\""
			+ " hiddenPrintScript" + " fieldtype=\"VALUE_TYPE_DATE\""
			+ " dialogView=\"\"" + " selectDate=\"false\" "
			+ " fieldkeyevent=\"Enterkey\"" + " readonlyScript=\"\""
			+ " popToChoice=\"false\" " + " validateLibs=\"\""
			+ " calculateOnRefresh=\"false\">" + "</P>";

	/**
	 * clob field
	 */
	private static final String FIELD_00004 = "<P>"
			+ "<TEXTAREA id=00004 name=clob"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.TextareaField\""
			+ " discript" + " refreshOnChanged=\"false\"" + " valueScript"
			+ " validateRule" + " hiddenScript" + " hiddenPrintScript"
			+ " fieldtype=\"VALUE_TYPE_TEXT\"" + " validateLibs"
			+ " calculateOnRefresh=\"false\">" + "</TEXTAREA>" + "</P>";

	private static final String DUP_FIELD_00001 = "<P>"
			+ "<INPUT id=00006 name=text"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.InputField\""
			+ " numberPattern" + " datePattern" + " discript"
			+ " textType=\"text\"" + " refreshOnChanged=\"false\" "
			+ " valueScript" + " validateRule" + " hiddenScript"
			+ " hiddenPrintScript" + " fieldtype=\"VALUE_TYPE_VARCHAR\""
			+ " dialogView" + " selectDate=\"false\""
			+ " fieldkeyevent=\"Enterkey\" " + " readonlyScript"
			+ " popToChoice=\"false\"" + " validateLibs"
			+ " calculateOnRefresh=\"false\">" + "</P>";

	private static final String OLD_FORM_TEMPLATE_CONTEXT = FIELD_00001
			+ FIELD_00002

			+ FIELD_00003

			+ FIELD_00004;

	private static final String OLD_FORM_DUP_TEMPLATE_CONTEXT = FIELD_00001
			+ FIELD_00002

			+ FIELD_00003

			+ FIELD_00004

			+ DUP_FIELD_00001;

	private static final String NEW_FORM_FOR_FIELD_MODIFY = FIELD_00001
			+ MODIFY_FIELD_00002

			+ FIELD_00003

			+ FIELD_00004;

	private Connection conn;

	private Form oldForm;

	public void testCreateTable() throws Exception {
		// step1 create a table
		AbstractTableDefinition builder = new OracleTableDefinition(conn);
		// 1.create table
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, null);
		builder.processChanges(log);

		// 2.create table too ,to show the error message
		AbstractValidator checkbuild = new OracleValidator(conn);

		try {
			checkbuild.checkChanges(log);
		} catch (NeedConfirmException ex) {
			Collection<Confirm> message = ex.getConfirms();
			for (Iterator<Confirm> iter = message.iterator(); iter.hasNext();) {
				Confirm str = (Confirm) iter.next();
				assertNotNull(str);
			}
		}

		log = new ChangeLog();
		// 3.drop table
		newForm = null;
		oldForm = this.oldForm;
		log.compare(newForm, oldForm);
		builder.processChanges(log);
	}

	public void testCreateDuplicateField() throws Exception {
		AbstractValidator checkbuild = new OracleValidator(conn);

		Form newForm = this.getOldFormDupField();
		ChangeLog log = new ChangeLog();
		log.compare(newForm, null);

		try {
			checkbuild.checkChanges(log);
		} catch (NeedConfirmException ex) {
			Collection<Confirm> message = ex.getConfirms();
			for (Iterator<Confirm> iter = message.iterator(); iter.hasNext();) {
				Confirm str = (Confirm) iter.next();
				assertNotNull(str);
			}
		}
	}

	public void testRenameTable() throws Exception {
		Form tempNewForm = getNewFormOfRename();
		AbstractTableDefinition builder = new OracleTableDefinition(conn);
		// 1.create table1
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, null);

		// 2.create table2
		log.compare(tempNewForm, null);

		builder.processChanges(log);

		AbstractValidator checkbuild = new OracleValidator(conn);
		// 3.table rename check
		log = new ChangeLog();
		newForm = tempNewForm;
		oldForm = this.oldForm;
		log.compare(newForm, oldForm);

		try {
			checkbuild.checkChanges(log);
		} catch (NeedConfirmException ex) {
			Collection<Confirm> message = ex.getConfirms();		
			for (Iterator<Confirm> iter = message.iterator(); iter.hasNext();) {
				Confirm str = (Confirm) iter.next();
				assertNotNull(str);
			}
		}

		log = new ChangeLog();
		// 4.drop table1
		newForm = null;
		oldForm = tempNewForm;
		log.compare(newForm, oldForm);

		// 5.drop table2
		newForm = null;
		oldForm = this.oldForm;
		log.compare(newForm, oldForm);
		builder.processChanges(log);

	}

	public void testDropTable() throws Exception {
		AbstractTableDefinition builder = new OracleTableDefinition(conn);
		// 1.create table
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, null);
		builder.processChanges(log);

		// 2.insert data
		String sql = "insert into TLK_TEST(ID,ITEM_TEXT,ITEM_NUMBER,ITEM_DATE,ITEM_CLOB)";
		sql += " values(?,?,?,?,?)";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, "document ID");
		statement.setString(2, "for varchar test");
		statement.setDouble(3, 100.32);
		statement.setDate(4, new java.sql.Date(new Date().getTime()));
		statement.setString(5, "for clob test");
		statement.execute();

		// chect drop table
		AbstractValidator checkbuild = new OracleValidator(conn);

		log = new ChangeLog();
		newForm = null;
		oldForm = this.oldForm;
		log.compare(newForm, oldForm);

		try {
			checkbuild.checkChanges(log);
		} catch (NeedConfirmException ex) {
			Collection<Confirm> message = ex.getConfirms();			
			for (Iterator<Confirm> iter = message.iterator(); iter.hasNext();) {
				Confirm str = (Confirm) iter.next();
				assertNotNull(str);
			}
		}

		builder.processChanges(log);
		
		statement.close();
	}

	public void testAddColumn() throws Exception {
		// step1 create a table
		AbstractTableDefinition builder = new OracleTableDefinition(conn);
		// 1.create table
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, null);
		builder.processChanges(log);

		// check Add Table
		newForm = this.getOldFormDupField();
		oldForm = this.oldForm;

		log = new ChangeLog();
		log.compare(newForm, oldForm);

		AbstractValidator checkbuild = new OracleValidator(conn);
		try {
			checkbuild.checkChanges(log);
		} catch (NeedConfirmException ex) {
			Collection<Confirm> message = ex.getConfirms();
			for (Iterator<Confirm> iter = message.iterator(); iter.hasNext();) {
				Confirm str = (Confirm) iter.next();
				assertNotNull(str);
			}
		}

		// drop table
		log = new ChangeLog();
		log.compare(null, oldForm);
		builder.processChanges(log);

	}

	public void testDropColumn() throws Exception {
		AbstractTableDefinition builder = new OracleTableDefinition(conn);
		// create table
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, null);
		builder.processChanges(log);

		// insert data
		String sql = "insert into TLK_TEST(ID,ITEM_TEXT,ITEM_NUMBER,ITEM_DATE,ITEM_CLOB)";
		sql += " values(?,?,?,?,?)";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, "document ID");
		statement.setString(2, "for varchar test");
		statement.setDouble(3, 100.32);
		statement.setDate(4, new java.sql.Date(new Date().getTime()));
		statement.setString(5, "for clob test");
		statement.execute();

		// drop column checkValidate
		AbstractValidator checkbuild = new OracleValidator(conn);
		newForm = null;
		oldForm = this.oldForm;
		log = new ChangeLog();
		log.compare(newForm, oldForm);

		try {
			checkbuild.checkChanges(log);
		} catch (NeedConfirmException ex) {
			Collection<Confirm> message = ex.getConfirms();
			for (Iterator<Confirm> iter = message.iterator(); iter.hasNext();) {
				Confirm str = (Confirm) iter.next();
				assertNotNull(str);
			}
		}

		// drop Table
		builder.processChanges(log);
		statement.close();

	}

	public void testRenameColumn() throws Exception {
		AbstractTableDefinition builder = new OracleTableDefinition(conn);

		// create table1
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, null);
		builder.processChanges(log);

		// table1 rename and check
		AbstractValidator checkbuild = new OracleValidator(conn);
		newForm = getRenameField();
		oldForm = this.oldForm;

		log = new ChangeLog();
		log.compare(newForm, oldForm);

		try {
			checkbuild.checkChanges(log);
		} catch (NeedConfirmException ex) {
			Collection<Confirm> message = ex.getConfirms();
			for (Iterator<Confirm> iter = message.iterator(); iter.hasNext();) {
				Confirm str = (Confirm) iter.next();
				assertNotNull(str);
			}
		}

		// drop table
		newForm = null;
		oldForm = this.oldForm;

		log = new ChangeLog();
		log.compare(newForm, oldForm);

		builder.processChanges(log);
	}

	public void testColumnTypeChange() throws Exception {
		AbstractTableDefinition builder = new OracleTableDefinition(conn);

		// create table1
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, null);
		builder.processChanges(log);

		// insert data
		String sql = "insert into TLK_TEST(ID,ITEM_TEXT,ITEM_NUMBER,ITEM_DATE,ITEM_CLOB)";
		sql += " values(?,?,?,?,?)";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, "document ID");
		statement.setString(2, "for varchar test");
		statement.setDouble(3, 100.32);
		statement.setDate(4, new java.sql.Date(new Date().getTime()));
		statement.setString(5, "for clob test");
		statement.execute();

		// type change and check
		AbstractValidator checkbuild = new OracleValidator(conn);
		newForm = this.getNewFormOfFieldModify();
		oldForm = this.oldForm;

		log = new ChangeLog();
		log.compare(newForm, oldForm);

		try {
			checkbuild.checkChanges(log);
		} catch (NeedConfirmException ex) {
			Collection<Confirm> message = ex.getConfirms();
			for (Iterator<Confirm> iter = message.iterator(); iter.hasNext();) {
				Confirm str = (Confirm) iter.next();
				assertNotNull(str);
			}
		}

		// drop table
		newForm = null;
		oldForm = this.oldForm;

		log = new ChangeLog();
		log.compare(newForm, oldForm);

		builder.processChanges(log);
		
		statement.close();
	}

	private Form getNewFormOfRename() {
		Form newForm = new Form();
		newForm.setId("000001");
		newForm.setName("NEW_TEST");
		newForm.setTemplatecontext(OLD_FORM_TEMPLATE_CONTEXT);
		newForm.setType(Form.FORM_TYPE_NORMAL);

		return newForm;
	}

	protected void setUp() throws Exception {
		conn = PersistenceUtils.getDBConnection();

		oldForm = getOldForm();
		showFields(oldForm);

		super.setUp();
	}

	protected void tearDown() throws Exception {
		conn.close();
		super.tearDown();
	}

	private Form getOldForm() {
		Form oldForm = new Form();
		oldForm.setId("000001");
		oldForm.setName("TEST");
		oldForm.setTemplatecontext(OLD_FORM_TEMPLATE_CONTEXT);
		oldForm.setType(Form.FORM_TYPE_NORMAL);

		return oldForm;
	}

	private Form getNewFormOfFieldModify() {
		Form newForm = new Form();
		newForm.setId("000001");
		newForm.setName("TEST");
		newForm.setTemplatecontext(NEW_FORM_FOR_FIELD_MODIFY);
		newForm.setType(Form.FORM_TYPE_NORMAL);

		return newForm;
	}

	private Form getOldFormDupField() {
		Form oldForm = new Form();
		oldForm.setId("000001");
		oldForm.setName("TEST");
		oldForm.setTemplatecontext(OLD_FORM_DUP_TEMPLATE_CONTEXT);
		oldForm.setType(Form.FORM_TYPE_NORMAL);

		return oldForm;
	}

	private Form getRenameField() {
		Form oldForm = new Form();
		oldForm.setId("000001");
		oldForm.setName("TEST");
		oldForm.setTemplatecontext(OLD_FORM_DUP_TEMPLATE_CONTEXT);
		oldForm.setType(Form.FORM_TYPE_NORMAL);

		return oldForm;
	}

//	private Form getAddColumn() {
//		Form oldForm = new Form();
//		oldForm.setId("000001");
//		oldForm.setName("TEST");
//		oldForm.setTemplatecontext(OLD_FORM_DUP_TEMPLATE_CONTEXT);
//		oldForm.setType(Form.FORM_TYPE_NORMAL);
//
//		return oldForm;
//
//	}

	private void showFields(Form form) {
		Collection<FormField> colls = form.getFields();
		for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
			FormField field = (FormField) iter.next();
			assertNotNull(field);
		}
	}
}
