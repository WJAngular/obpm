package cn.myapps.core.table.ddlutil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.core.dynaform.form.ejb.Confirm;
import cn.myapps.core.table.alteration.AddColumnChange;
import cn.myapps.core.table.alteration.AddTableChange;
import cn.myapps.core.table.alteration.ColumnDataTypeChange;
import cn.myapps.core.table.alteration.ColumnRenameChange;
import cn.myapps.core.table.alteration.DropColumnChange;
import cn.myapps.core.table.alteration.DropTableChange;
import cn.myapps.core.table.alteration.ModelChange;
import cn.myapps.core.table.alteration.TableRenameChange;
import cn.myapps.core.table.constants.ConfirmConstant;
import cn.myapps.core.table.model.Column;
import cn.myapps.core.table.model.DuplicateException;
import cn.myapps.core.table.model.NeedConfirmException;
import cn.myapps.core.table.model.Table;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

/**
 * @author nicholas
 */
public abstract class AbstractValidator {

	protected static Logger log = Logger.getLogger(AbstractValidator.class);

	protected Connection conn;

	protected String schema;

	protected SQLBuilder _builder;

	protected final static String[] TABLE_TYPES = new String[] { "TABLE" };

	protected Collection<Confirm> confirms = new HashSet<Confirm>();

	protected AbstractValidator(Connection conn, SQLBuilder _builder) {
		this._builder = _builder;
		this.conn = conn;
	}

	public void checkChanges(ChangeLog changeLog) throws Exception {
		Collection<ModelChange> changes = changeLog.getChanges();

		for (Iterator<ModelChange> iterator = changes.iterator(); iterator.hasNext();) {
			ModelChange change = (ModelChange) iterator.next();
			invokeCheckChangeHandler(change);
		}

		if (confirms.size() > 0) {
			throw new OBPMValidateException(new NeedConfirmException(confirms));
		}
	}

	protected void invokeCheckChangeHandler(ModelChange change) throws Exception {
		Class<?> curClass = getClass();

		log.info("class " + change.getClass());
		// find the handler for the change
		while ((curClass != null) && !Object.class.equals(curClass)) {

			Method method = null;
			try {
				try {
					method = curClass.getDeclaredMethod("checkChange", new Class[] { change.getClass() });
				} catch (NoSuchMethodException ex) {
					// we actually expect this one
				}

				if (method != null) {
					method.invoke(this, new Object[] { change });
					return;
				} else {
					curClass = curClass.getSuperclass();
				}
			} catch (InvocationTargetException ex) {
				if (ex.getTargetException() instanceof SQLException) {
					throw (SQLException) ex.getTargetException();
				} else {
					throw new Exception(ex.getTargetException());
				}
			}
		}
	}

	// Add Table Change
	public void checkChange(AddTableChange change) throws SQLException, SequenceException, OBPMValidateException {
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet rs = metaData.getTables(getCatalog(), getSchemaPattern(), change.getTable().getName(), TABLE_TYPES);

		try {
			if (rs.next()) {
				Confirm confirm = new Confirm(change.getTable().getFormName(), ConfirmConstant.FORM_EXIST);
				confirm.setId(Sequence.getSequence());
				confirms.add(confirm);
			}
		} finally {
			rs.close();
		}

		checkFieldDuplicate(change);
	}

	// drop tableDropTableChange
	public void checkChange(DropTableChange change) throws SQLException, SequenceException {
		StringBuffer buffer = new StringBuffer();
		getSQLBuilder().setWriter(buffer);
		getSQLBuilder().getTableDatas(change.getTable());

		String sql = getSQLBuilder().getSQL();
		if (evaluateBatch(sql, false)) {
			Confirm confirm = new Confirm(change.getTable().getFormName(), ConfirmConstant.FORM_DATA_EXIST);
			confirm.setId(Sequence.getSequence());
			confirms.add(confirm);
		}
	}

	// rename table
	public void checkChange(TableRenameChange change) throws SQLException, SequenceException {
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet rs = metaData.getTables(getCatalog(), getSchemaPattern(), change.getTargetTable().getName(),
				TABLE_TYPES);

		try {
			if (rs.next()) {
				Confirm confirm = new Confirm(change.getTargetTable().getFormName(), ConfirmConstant.FORM_EXIST);
				confirm.setId(Sequence.getSequence());
				confirms.add(confirm);
			}
		} finally {
			rs.close();
		}
	}

	// add column
	public void checkChange(AddColumnChange change) throws SQLException, SequenceException, OBPMValidateException {
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet rs = metaData.getColumns(getCatalog(), getSchemaPattern(), change.getTable().getName(), change
				.getTargetColumn().getName());

		try {
			if (rs.next()) {
				Confirm confirm = new Confirm(change.getTable().getFormName(), ConfirmConstant.FIELD_EXIST);
				confirm.setId(Sequence.getSequence());
				confirm.setFieldName(change.getTargetColumn().getFieldName());
				confirm.setOldFieldId(change.getTargetColumn().getId());
				confirms.add(confirm);
			}
		} finally {
			rs.close();
		}

		checkFieldDuplicate(change);
	}

	// drop column
	public void checkChange(DropColumnChange change) throws SQLException, SequenceException {
		StringBuffer buffer = new StringBuffer();
		getSQLBuilder().setWriter(buffer);
		getSQLBuilder().getColumnDatas(change.getTable(), change.getSourceColumn());

		String checkSql = getSQLBuilder().getSQL();
		if (evaluateBatch(checkSql, true)) {

			Confirm confirm = new Confirm(change.getTable().getFormName(), ConfirmConstant.FIELD_DATA_EXIST);
			confirm.setId(Sequence.getSequence());
			confirm.setOldFieldId(change.getSourceColumn().getId());
			confirm.setFieldName(change.getSourceColumn().getFieldName());
			confirms.add(confirm);

		}
	}

	// rename column
	public void checkChange(ColumnRenameChange change) throws SQLException, SequenceException {
		StringBuffer buffer = new StringBuffer();
		getSQLBuilder().setWriter(buffer);
		getSQLBuilder().findColumn(change.getTable(), change.getTargetColumn());

		String sql = getSQLBuilder().getSQL();
		if (evaluateBatch(sql, false)) {

			Confirm confirm = new Confirm(change.getTable().getFormName(), ConfirmConstant.FIELD_EXIST);
			confirm.setId(Sequence.getSequence());
			confirm.setNewFieldId(change.getTargetColumn().getId());
			confirm.setOldFieldId(change.getSourceColumn().getId());
			confirm.setFieldName(change.getSourceColumn().getFieldName());
			confirms.add(confirm);
		}
	}

	// modify column type
	public void checkChange(ColumnDataTypeChange change) throws SQLException, SequenceException {
		StringBuffer buffer = new StringBuffer();
		getSQLBuilder().setWriter(buffer);

		Table table = change.getTable();
		Column targetColumn = change.getTargetColumn();
		Column sourceColumn = change.getSourceColumn();

		getSQLBuilder().getColumnDatas(table, sourceColumn);

		String checkSql = getSQLBuilder().getSQL();
		if (evaluateBatch(checkSql, true)) {
			if (!sourceColumn.isCompatible(targetColumn)) {

				Confirm confirm = new Confirm(change.getTable().getFormName(), ConfirmConstant.FIELD_TYPE_INCOMPATIBLE);
				confirm.setId(Sequence.getSequence());
				confirm.setNewFieldId(change.getTargetColumn().getId());
				confirm.setOldFieldId(change.getSourceColumn().getId());
				confirm.setFieldName(change.getSourceColumn().getFieldName());
				confirms.add(confirm);

			}
		}
	}

	protected void checkFieldDuplicate(ModelChange change) throws SequenceException, OBPMValidateException {
		Collection<Column> allCol = change.getTable().getColumns();
		for (Iterator<Column> iter = allCol.iterator(); iter.hasNext();) {
			Column column = (Column) iter.next();
			try {
				change.getTable().findColumn(column.getName());
			} catch (DuplicateException dup) {
				Confirm confirm = new Confirm(change.getTable().getFormName(), ConfirmConstant.FIELD_DUPLICATE);
				confirm.setOldFieldId(column.getId());
				confirm.setFieldName(column.getFieldName());
				confirm.setId(Sequence.getSequence());
				confirms.add(confirm);
			}
		}
	}

	public boolean evaluateBatch(String checkSql, boolean continueOnError) throws SQLException {
		Statement statement = null;
		boolean hasError = false;
		ResultSet rs = null;
		int errors = 0;
		int commandCount = 0;

		// we tokenize the SQL along the delimiters, and we also make sure that
		// only delimiters
		// at the end of a line or the end of the string are used (row mode)
		try {
			statement = conn.createStatement();

			String[] commands = checkSql.split(SQLBuilder.SQL_DELIMITER);

			for (int i = 0; i < commands.length; i++) {
				String command = commands[i];

				if (command.trim().length() == 0) {
					continue;
				}
				commandCount++;

				try {
					log.info("executing SQL: " + command);

					rs = statement.executeQuery(command);

					if (rs.next()) {
						hasError = true;
					}
				} catch (SQLException ex) {
					if (continueOnError) {
						log.warn("SQL Command " + command + " failed with: " + ex.getMessage());
						errors++;
					} else {
						throw new SQLException("Error while executing SQL " + command);
					}
				}
			}
			log.info("Executed " + commandCount + " SQL command(s) with " + errors + " error(s)");
		} catch (SQLException ex) {
			ex.printStackTrace();///////////
			throw new SQLException("Error while executing SQL");
		} finally {
			closeStatement(statement);
		}
		return hasError;
	}

	private void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception e) {
				log.info("Ignoring exception that occurred while closing statement", e);
			}
		}
	}

	public SQLBuilder getSQLBuilder() {
		return _builder;
	}

	public Collection<Confirm> getConfirms() {
		return confirms;
	}

	protected abstract String getCatalog();

	protected abstract String getSchemaPattern();
}
