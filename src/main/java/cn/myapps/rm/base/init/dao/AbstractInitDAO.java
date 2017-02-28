package cn.myapps.rm.base.init.dao;

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

/**
 * 初始化系统表结构
 * @author Happy
 *
 */
public abstract class AbstractInitDAO {
	
	Logger log = Logger.getLogger(AbstractInitDAO.class);

	protected String dbType = "Oracle: ";

	protected String schema = "";

	protected Connection connection;

	protected AbstractTableDefinition definition;

	protected final static Map<String, Table> INIT_TABLEMAP = new HashMap<String, Table>();
	
	public AbstractInitDAO(Connection conn) throws Exception {
		this.connection = conn;
		
		Table resource = new Table("RM_RESOURCE");
		resource.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		resource.addColumn(new Column("", "NAME", Types.VARCHAR));
		resource.addColumn(new Column("", "DESCRIPTION", Types.CLOB));
		resource.addColumn(new Column("", "SERIAL", Types.VARCHAR));
		resource.addColumn(new Column("", "R_TYPE", Types.VARCHAR));
		resource.addColumn(new Column("", "CREATOR",  Types.VARCHAR));
		resource.addColumn(new Column("", "CREATOR_ID",  Types.VARCHAR));
		resource.addColumn(new Column("", "CREATE_DATE", Types.TIMESTAMP, "6"));
		resource.addColumn(new Column("", "DOMAIN_ID", Types.VARCHAR));
		
		Table resourceUsage = new Table("RM_RESOURCE_USAGE");
		resourceUsage.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		resourceUsage.addColumn(new Column("", "RESOURCE_ID", Types.VARCHAR));
		resourceUsage.addColumn(new Column("", "RESOURCE_NAME", Types.VARCHAR));
		resourceUsage.addColumn(new Column("", "RESOURCE_SERIAL", Types.VARCHAR));
		resourceUsage.addColumn(new Column("", "USER",  Types.VARCHAR));
		resourceUsage.addColumn(new Column("", "USER_ID", Types.VARCHAR));
		resourceUsage.addColumn(new Column("", "REMARK", Types.CLOB));
		resourceUsage.addColumn(new Column("", "START_DATE", Types.TIMESTAMP, "6"));
		resourceUsage.addColumn(new Column("", "END_DATE", Types.TIMESTAMP, "6"));
		resourceUsage.addColumn(new Column("", "CREATE_DATE", Types.TIMESTAMP, "6"));
		resourceUsage.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		resource.addColumn(new Column("", "USAGE_MODE", Types.INTEGER, "10,0"));
		resource.addColumn(new Column("", "STATUS", Types.VARCHAR));
		resource.addColumn(new Column("", "EFFECTIVE", Types.INTEGER, "10,0"));
		
		Table role = new Table("RM_ROLE");
		role.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		role.addColumn(new Column("", "NAME", Types.VARCHAR));
		role.addColumn(new Column("", "R_LEVEL", Types.INTEGER, "10,0"));
		
		Table user_role_set = new Table("RM_USER_ROLE_SET");
		user_role_set.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		user_role_set.addColumn(new Column("", "USERID", Types.VARCHAR));
		user_role_set.addColumn(new Column("", "ROLEID", Types.VARCHAR));
		
		
		INIT_TABLEMAP.put(resource.getName(), resource);
		INIT_TABLEMAP.put(resourceUsage.getName(), resourceUsage);
		INIT_TABLEMAP.put(role.getName(), role);
		INIT_TABLEMAP.put(user_role_set.getName(), user_role_set);
		
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
		for (Iterator<Entry<String, Table>> iterator = INIT_TABLEMAP.entrySet().iterator(); iterator.hasNext();) {
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
