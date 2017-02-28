package cn.myapps.core.dynaform.form.ddlutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import junit.framework.TestCase;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.table.ddlutil.AbstractTableDefinition;
import cn.myapps.core.table.ddlutil.ChangeLog;
import cn.myapps.core.table.ddlutil.oracle.OracleTableDefinition;

public class TableDefinitionTest extends TestCase {

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

	private static final String OLD_FORM_TEMPLATE_CONTEXT = FIELD_00001
			+ FIELD_00002

			+ FIELD_00003

			+ FIELD_00004;

	private static final String MODIFY_FIELD_00002 = "<P>"
			+ "<INPUT id=00002 name=text2"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.InputField\""
			+ " numberPattern" + " datePattern" + " discript"
			+ " textType=\"text\"" + " refreshOnChanged=\"false\" "
			+ " valueScript" + " validateRule" + " hiddenScript"
			+ " hiddenPrintScript" + " fieldtype=\"VALUE_TYPE_VARCHAR\""
			+ " dialogView" + " selectDate=\"false\""
			+ " fieldkeyevent=\"Enterkey\" " + " readonlyScript"
			+ " popToChoice=\"false\"" + " validateLibs"
			+ " calculateOnRefresh=\"false\">" + "</P>";

	private static final String FIELD_00005 = "<P>"
			+ "<INPUT id=00005 name=number2"
			+ " className=\"cn.myapps.core.dynaform.form.ejb.InputField\""
			+ " numberPattern" + " datePattern" + " discript"
			+ " textType=\"text\"" + " refreshOnChanged=\"false\" "
			+ " valueScript" + " validateRule" + " hiddenScript"
			+ " hiddenPrintScript" + " fieldtype=\"VALUE_TYPE_NUMBER\""
			+ " dialogView" + " selectDate=\"false\""
			+ " fieldkeyevent=\"Enterkey\" " + " readonlyScript"
			+ " popToChoice=\"false\"" + " validateLibs"
			+ " calculateOnRefresh=\"false\">" + "</P>";

	/**
	 * this form changes field 00002 name to text2 and type to varchar,and then
	 * add one field 00005
	 */
	private static final String NEW_FORM_FOR_FIELD_ADDANDMODIFY = FIELD_00001
			+ MODIFY_FIELD_00002

			+ FIELD_00003

			+ FIELD_00004

			+ FIELD_00005;

	private static final String NEW_FORM_FOR_FIELD_DROP = FIELD_00001
			+ FIELD_00002

			+ FIELD_00003;

	private Connection conn;

	private Form oldForm;
	protected void setUp() throws Exception {
		conn = PersistenceUtils.getDBConnection();
		// oldForm = getOldForm();
		// showFields(oldForm);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		conn.close();
		super.tearDown();
	}


	 public void testCreateTable() throws Exception {
		AbstractTableDefinition builder = new OracleTableDefinition(conn);
		// 1.create table
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, null);

		// 2.drop table
		newForm = null;
		oldForm = this.oldForm;
		log.compare(newForm, oldForm);

		builder.processChanges(log);
	}

	public void testTableRename() throws Exception {
		Form tempNewForm = getNewFormOfRename();
		AbstractTableDefinition builder = new OracleTableDefinition(conn);
		// 1.create table
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, null);

		// 2.table rename
		newForm = tempNewForm;
		oldForm = this.oldForm;
		log.compare(newForm, oldForm);

		// 3.drop table
		newForm = null;
		oldForm = tempNewForm;
		log.compare(newForm, oldForm);

		builder.processChanges(log);
	}

	public void testTableRenameWithData() throws Exception {
		Form tempNewForm = getNewFormOfRename();
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

		// 2.table rename
		newForm = tempNewForm;
		oldForm = this.oldForm;
		log = new ChangeLog();
		log.compare(newForm, oldForm);

		// 3.drop table
		newForm = null;
		oldForm = tempNewForm;
		log.compare(newForm, oldForm);

		builder.processChanges(log);
		
		statement.close();
	}

	public void testColumnAddAndModify() throws Exception {
		Form tempNewForm = getNewFormOfFieldAddAndModify();
		showFields(tempNewForm);
		AbstractTableDefinition builder = new OracleTableDefinition(conn);
		// 1.create table
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, oldForm);

		// 2.column add and change type and rename
		newForm = tempNewForm;
		oldForm = this.oldForm;
		log.compare(newForm, oldForm);

		// 3.drop table
		newForm = null;
		oldForm = tempNewForm;
		log.compare(newForm, oldForm);

		builder.processChanges(log);
	}

	public void testColumnAddAndModifyWithData() throws Exception {
		Form tempNewForm = getNewFormOfFieldAddAndModify();
		showFields(tempNewForm);

		AbstractTableDefinition builder = new OracleTableDefinition(conn);
		// 1.create table
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, oldForm);
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

		// 3.column add and change type and rename
		newForm = tempNewForm;
		oldForm = this.oldForm;
		log = new ChangeLog();
		log.compare(newForm, oldForm);

		// 4.drop table
		newForm = null;
		oldForm = tempNewForm;
		log.compare(newForm, oldForm);

		builder.processChanges(log);
		
		statement.close();
	}

	public void testColumnDrop() throws Exception {
		Form tempNewForm = getNewFormOfFieldDrop();
		showFields(tempNewForm);

		AbstractTableDefinition builder = new OracleTableDefinition(conn);
		// 1.create table
		Form newForm = this.oldForm;
		Form oldForm = null;
		ChangeLog log = new ChangeLog();
		log.compare(newForm, oldForm);

		// 2.column drop
		newForm = tempNewForm;
		oldForm = this.oldForm;
		log.compare(newForm, oldForm);

		// 3.drop table
		newForm = null;
		oldForm = tempNewForm;
		log.compare(newForm, oldForm);

		builder.processChanges(log);
	}

	private void showFields(Form form) {
		Collection<FormField> colls = form.getFields();
		for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
			FormField field = (FormField) iter.next();
			assertNotNull(field);
		}
	}

//	private Form getOldForm() {
//		Form oldForm = new Form();
//		oldForm.setId("000001");
//		oldForm.setName("TEST");
//		oldForm.setTemplatecontext(OLD_FORM_TEMPLATE_CONTEXT);
//		oldForm.setType(Form.FORM_TYPE_NORMAL);
//
//		return oldForm;
//	}

	private Form getNewFormOfFieldAddAndModify() {
		Form newForm = new Form();
		newForm.setId("000001");
		newForm.setName("TEST");
		newForm.setTemplatecontext(NEW_FORM_FOR_FIELD_ADDANDMODIFY);
		newForm.setType(Form.FORM_TYPE_NORMAL);

		return newForm;
	}

	private Form getNewFormOfFieldDrop() {
		Form newForm = new Form();
		newForm.setId("000001");
		newForm.setName("TEST");
		newForm.setTemplatecontext(NEW_FORM_FOR_FIELD_DROP);
		newForm.setType(Form.FORM_TYPE_NORMAL);

		return newForm;
	}

	private Form getNewFormOfRename() {
		Form newForm = new Form();
		newForm.setId("000001");
		newForm.setName("NEW_TEST");
		newForm.setTemplatecontext(OLD_FORM_TEMPLATE_CONTEXT);
		newForm.setType(Form.FORM_TYPE_NORMAL);

		return newForm;
	}

	public Form getOldForm() {
		return oldForm;
	}

	public void setOldForm(Form oldForm) {
		this.oldForm = oldForm;
	}

//	private void showSchemas() throws SQLException {
//		DatabaseMetaData dbmeda = conn.getMetaData();
//		ResultSet schemas = dbmeda.getSchemas();
//		while (schemas.next()) {
//			ResultSetMetaData rsmeta = schemas.getMetaData();
//			int count = rsmeta.getColumnCount();
//			for (int i = 1; i <= count; i++) {
//				//(schemas.getObject(i));
//			}
//		}
//	}
}
