package cn.myapps.attendance.base.schema.dao;

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
		
		Table location = new Table("AM_LOCATION");
		location.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		location.addColumn(new Column("", "NAME", Types.VARCHAR));
		location.addColumn(new Column("", "LONGITUDE",  Types.NUMERIC,"10,10"));
		location.addColumn(new Column("", "LATITUDE",  Types.NUMERIC,"10,10"));
		location.addColumn(new Column("", "DOMAIN_ID", Types.VARCHAR));
		
		Table rule = new Table("AM_RULE");
		rule.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		rule.addColumn(new Column("", "NAME", Types.VARCHAR));
		rule.addColumn(new Column("", "ORGANIZATION_TYPE", Types.INTEGER, "3,0"));
		rule.addColumn(new Column("", "ORGANIZATIONS", Types.CLOB));
		rule.addColumn(new Column("", "ORGANIZATIONS_TEXT", Types.CLOB));
		rule.addColumn(new Column("", "LOCATIONS_TEXT", Types.VARCHAR, "1000"));
		rule.addColumn(new Column("", "RANGES", Types.INTEGER, "6,0"));
		rule.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		rule.addColumn(new Column("", "MULTI_PERIOD", Types.BIT, "1,0"));
		
		Table attendance = new Table("AM_ATTENDANCE");
		attendance.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		attendance.addColumn(new Column("", "USER_ID", Types.VARCHAR));
		attendance.addColumn(new Column("", "USER_NAME", Types.VARCHAR));
		attendance.addColumn(new Column("", "DEPT_ID", Types.VARCHAR));
		attendance.addColumn(new Column("", "DEPT_NAME", Types.VARCHAR));
		attendance.addColumn(new Column("", "WORKING_HOURS", Types.NUMERIC,"10,10"));
		attendance.addColumn(new Column("", "STATUS", Types.INTEGER, "3,0"));
		attendance.addColumn(new Column("", "ATTENDANCE_DATE", Types.TIMESTAMP, "6"));
		attendance.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		attendance.addColumn(new Column("", "MULTI_PERIOD", Types.BIT, "1,0"));
		
		Table attendanceDetail = new Table("AM_ATTENDANCE_DETAIL");
		attendanceDetail.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		attendanceDetail.addColumn(new Column("", "USER_ID", Types.VARCHAR));
		attendanceDetail.addColumn(new Column("", "USER_NAME", Types.VARCHAR));
		attendanceDetail.addColumn(new Column("", "DEPT_ID", Types.VARCHAR));
		attendanceDetail.addColumn(new Column("", "DEPT_NAME", Types.VARCHAR));
		attendanceDetail.addColumn(new Column("", "SIGNIN_TIME", Types.TIMESTAMP, "6"));
		attendanceDetail.addColumn(new Column("", "SIGNOUT_TIME", Types.TIMESTAMP, "6"));
		attendanceDetail.addColumn(new Column("", "SIGNIN_LOCATION", Types.VARCHAR));
		attendanceDetail.addColumn(new Column("", "SIGNOUT_LOCATION", Types.VARCHAR));
		attendanceDetail.addColumn(new Column("", "WORKING_HOURS", Types.NUMERIC,"10,10"));
		attendanceDetail.addColumn(new Column("", "STATUS", Types.INTEGER, "3,0"));
		attendanceDetail.addColumn(new Column("", "ATTENDANCE_DATE", Types.TIMESTAMP, "6"));
		attendanceDetail.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		attendanceDetail.addColumn(new Column("", "TIMEREGION", Types.VARCHAR));
		attendanceDetail.addColumn(new Column("", "ATTENDANCE_ID", Types.VARCHAR));
		
		Table ruleLocationSet = new Table("AM_RULE_LOCATION_SET");
		ruleLocationSet.addColumn(new Column("", "RULE_ID", Types.VARCHAR));
		ruleLocationSet.addColumn(new Column("", "LOCATION_ID", Types.VARCHAR));
		ruleLocationSet.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		
		
		
		initTables.put(location.getName(), location);
		initTables.put(rule.getName(), rule);
		initTables.put(attendance.getName(), attendance);
		initTables.put(attendanceDetail.getName(), attendanceDetail);
		initTables.put(ruleLocationSet.getName(), ruleLocationSet);
		
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
