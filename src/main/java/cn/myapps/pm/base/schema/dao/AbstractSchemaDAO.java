package cn.myapps.pm.base.schema.dao;

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

	protected String dbType = "Oracle: ";

	protected String schema = "";

	protected Connection connection;

	protected AbstractTableDefinition definition;

	protected Map<String, Table> initTables = new HashMap<String, Table>();
	
	public AbstractSchemaDAO(Connection conn) throws Exception {
		this.connection = conn;
		
		Table project = new Table("PM_PROJECT");
		project.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		project.addColumn(new Column("", "NAME", Types.VARCHAR));
		project.addColumn(new Column("", "CREATOR",  Types.VARCHAR));
		project.addColumn(new Column("", "CREATOR_ID",  Types.VARCHAR));
		project.addColumn(new Column("", "MANAGER",  Types.VARCHAR));
		project.addColumn(new Column("", "MANAGER_ID",  Types.VARCHAR));
		project.addColumn(new Column("", "CREATE_DATE", Types.TIMESTAMP, "6"));
		project.addColumn(new Column("", "TASKS_TOTAL", Types.INTEGER, "10,0"));
		project.addColumn(new Column("", "FINISHED_TASKS_NUM", Types.INTEGER, "10,0"));
		project.addColumn(new Column("", "DOMAIN_ID", Types.VARCHAR));
		project.addColumn(new Column("", "NOTIFICATION", Types.BIT, "1,0"));
		
		Table task = new Table("PM_TASK");
		task.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		task.addColumn(new Column("", "NAME", Types.VARCHAR));
		task.addColumn(new Column("", "DESCRIPTION", Types.CLOB));
		task.addColumn(new Column("", "LEVELS", Types.INTEGER, "3,0"));
		task.addColumn(new Column("", "STATUS", Types.BIT, "1,0"));
		task.addColumn(new Column("", "CREATOR", Types.VARCHAR));
		task.addColumn(new Column("", "CREATOR_ID", Types.VARCHAR));
		task.addColumn(new Column("", "CREATE_DATE", Types.TIMESTAMP, "6"));
		task.addColumn(new Column("", "START_DATE", Types.TIMESTAMP, "6"));
		task.addColumn(new Column("", "END_DATE", Types.TIMESTAMP, "6"));
		task.addColumn(new Column("", "REMIND_MODE", Types.INTEGER, "3,0"));
		task.addColumn(new Column("", "TAGS", Types.VARCHAR,"550"));
		task.addColumn(new Column("", "EXECUTER", Types.VARCHAR));
		task.addColumn(new Column("", "EXECUTER_ID", Types.VARCHAR));
		task.addColumn(new Column("", "SUB_TASKS", Types.CLOB));
		task.addColumn(new Column("", "REMARK", Types.CLOB));
		task.addColumn(new Column("", "LOGS", Types.CLOB));
		task.addColumn(new Column("", "PROJECT_ID",  Types.VARCHAR));
		task.addColumn(new Column("", "PROJECT_NAME",  Types.VARCHAR));
		task.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		task.addColumn(new Column("", "ATTACHMENT",  Types.CLOB));
		
		
		Table tag = new Table("PM_TAG");
		tag.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		tag.addColumn(new Column("", "NAME", Types.VARCHAR));
		tag.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		
		Table activity = new Table("PM_ACTIVITY");
		activity.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		activity.addColumn(new Column("", "TASK_ID", Types.VARCHAR));
		activity.addColumn(new Column("", "USER_ID", Types.VARCHAR));
		activity.addColumn(new Column("", "USER_NAME", Types.VARCHAR));
		activity.addColumn(new Column("", "OPERATION_TYPE", Types.INTEGER, "3,0"));
		activity.addColumn(new Column("", "OPERATION_DATE", Types.TIMESTAMP, "6"));
		activity.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		
		Table projectMenberSet = new Table("PM_PROJECT_MEMBER_SET");
		//projectMenberSet.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		projectMenberSet.addColumn(new Column("", "PROJECT_ID", Types.VARCHAR));
		projectMenberSet.addColumn(new Column("", "USER_ID", Types.VARCHAR));
		projectMenberSet.addColumn(new Column("", "USER_NAME", Types.VARCHAR));
		projectMenberSet.addColumn(new Column("", "MEMBER_TYPE", Types.INTEGER));
		projectMenberSet.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		
		Table taskFollowerSet = new Table("PM_TASK_FOLLOWER_SET");
		//taskFollowerSet.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		taskFollowerSet.addColumn(new Column("", "TASK_ID", Types.VARCHAR));
		taskFollowerSet.addColumn(new Column("", "USER_ID", Types.VARCHAR));
		taskFollowerSet.addColumn(new Column("", "USER_NAME", Types.VARCHAR));
		taskFollowerSet.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		
		
		initTables.put(project.getName(), project);
		initTables.put(task.getName(), task);
		initTables.put(tag.getName(), tag);
		initTables.put(activity.getName(), activity);
		initTables.put(projectMenberSet.getName(), projectMenberSet);
		initTables.put(taskFollowerSet.getName(), taskFollowerSet);
		
	}
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

	protected abstract Table getDBTable(String tableName);
	
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}

}
