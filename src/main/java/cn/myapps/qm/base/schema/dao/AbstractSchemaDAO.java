package cn.myapps.qm.base.schema.dao;

import java.sql.Connection;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cn.myapps.core.table.alteration.AddColumnChange;
import cn.myapps.core.table.alteration.AddTableChange;
import cn.myapps.core.table.ddlutil.AbstractTableDefinition;
import cn.myapps.core.table.ddlutil.ChangeLog;
import cn.myapps.core.table.model.Column;
import cn.myapps.core.table.model.Table;

public abstract class AbstractSchemaDAO {

	Logger log = Logger.getLogger(AbstractSchemaDAO.class);
	
	protected Connection connection;
	
	protected Map<String, Table> initTables = new HashMap<String, Table>();
	
	protected String dbType = "Mysql: ";

	protected String schema = "";
	
	protected AbstractTableDefinition definition;
	
	public AbstractSchemaDAO(Connection conn) throws Exception{
		this.connection = conn;
		
		Table questionnaire = new Table("QM_QUESTIONNAIRE");
		questionnaire.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		questionnaire.addColumn(new Column("", "CREATOR", Types.VARCHAR));
		questionnaire.addColumn(new Column("", "CREATORNAME",Types.VARCHAR));
		questionnaire.addColumn(new Column("", "CREATEDATE", Types.TIMESTAMP,"6"));
		questionnaire.addColumn(new Column("", "TITLE", Types.CLOB));
		questionnaire.addColumn(new Column("", "EXPLAINS", Types.CLOB));
		questionnaire.addColumn(new Column("", "Q_CONTENT", Types.CLOB));
		questionnaire.addColumn(new Column("", "ACTORIDS", Types.CLOB));
		questionnaire.addColumn(new Column("", "STATUS", Types.INTEGER, "3,0"));
		questionnaire.addColumn(new Column("", "ACTORNAMES", Types.CLOB));
		questionnaire.addColumn(new Column("", "SCORE",Types.INTEGER,"3,0"));
		questionnaire.addColumn(new Column("", "SCOPE",Types.VARCHAR));
		questionnaire.addColumn(new Column("", "OWNERIDS",Types.CLOB));
		questionnaire.addColumn(new Column("", "OWNERNAMES",Types.CLOB));
		questionnaire.addColumn(new Column("", "PUBLISHDATE", Types.TIMESTAMP,"6"));
		questionnaire.addColumn(new Column("", "CREATOR_DEPT_ID", Types.VARCHAR));
		questionnaire.addColumn(new Column("", "CREATOR_DEPT_NAME",Types.VARCHAR));
		questionnaire.addColumn(new Column("", "PATICIPATE_TOTAL",Types.INTEGER,"5,0"));
		questionnaire.addColumn(new Column("", "ANSWER_TOTAL",Types.INTEGER,"5,0"));
		
		Table answer = new Table("QM_ANSWER");
		answer.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		answer.addColumn(new Column("", "USER_ID", Types.VARCHAR));
		answer.addColumn(new Column("", "USER_NAME", Types.VARCHAR));
		answer.addColumn(new Column("", "USER_DEPARTMENT", Types.VARCHAR));
		answer.addColumn(new Column("", "ANSWER", Types.CLOB));
		answer.addColumn(new Column("", "QUESTIONNAIRE_ID", Types.VARCHAR));
		answer.addColumn(new Column("", "ANSWER_DATE", Types.TIMESTAMP,"6"));
		answer.addColumn(new Column("", "STATUS", Types.INTEGER, "3,0"));
		answer.addColumn(new Column("", "TOTAL",Types.INTEGER,"3,0"));
		
		initTables.put(questionnaire.getName(), questionnaire);
		initTables.put(answer.getName(), answer);
	}
	
	protected abstract Table getDBTable(String tableName);
	
	/**
	 * 对比新旧表格差异
	 * 
	 * @param newTable
	 * @param oldTable
	 * @return 变更日志
	 * @throws Exception
	 */
	protected ChangeLog compare(Table newTable, Table oldTable) throws Exception {
		ChangeLog log = new ChangeLog();
		if (oldTable == null) {
			AddTableChange change = new AddTableChange(newTable);
			log.getChanges().add(change);
		} else {
			if (!newTable.equals(oldTable)) {
				for (Iterator<Column> iterator = newTable.getColumns().iterator(); iterator.hasNext();) {
					Column newColumn = iterator.next();
					Column oldColumn = oldTable.findColumn(newColumn.getName());
					if (oldColumn == null) {
						AddColumnChange change = new AddColumnChange(newTable, newColumn);
						log.getChanges().add(change);
					}
				}
			}
		}
		return log;
	}
	
	/**
	 * 初始化数据库表
	 * 
	 * @throws Exception
	 */
	public void initTables() throws Exception {
		for (Iterator<Entry<String, Table>> iterator = initTables.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Table> entry = iterator.next();
			Table table = (Table) entry.getValue();
			Table dbTable = getDBTable(table.getName());
			ChangeLog log = compare(table, dbTable);
			definition.processChanges(log);
		}
	}
	
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
}
