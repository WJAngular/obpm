package cn.myapps.mr.base.schema.dao;

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
import cn.myapps.mr.base.schema.dao.AbstractSchemaDAO;

public abstract class AbstractSchemaDAO {

	Logger log = Logger.getLogger(AbstractSchemaDAO.class);

	protected String dbType = "Oracle: ";

	protected String schema = "";

	protected Connection connection;

	protected AbstractTableDefinition definition;

	protected Map<String, Table> initTables = new HashMap<String, Table>();
	
	public AbstractSchemaDAO(Connection conn) throws Exception {
		this.connection = conn;
		
		Table reservation = new Table("MR_RESERVATION");
		reservation.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		reservation.addColumn(new Column("", "NAME", Types.VARCHAR));
		reservation.addColumn(new Column("", "CREATOR",  Types.VARCHAR));
		reservation.addColumn(new Column("", "CREATOR_ID",  Types.VARCHAR));
		reservation.addColumn(new Column("", "CREATOR_TEL",  Types.VARCHAR));
		reservation.addColumn(new Column("", "CONTENT",  Types.VARCHAR, "1000"));
		reservation.addColumn(new Column("", "AREA_ID",  Types.VARCHAR));
		reservation.addColumn(new Column("", "AREA", Types.VARCHAR));
		reservation.addColumn(new Column("", "ROOM", Types.VARCHAR));
		reservation.addColumn(new Column("", "ROOM_ID", Types.VARCHAR));
		reservation.addColumn(new Column("", "START_TIME", Types.TIMESTAMP));
		reservation.addColumn(new Column("", "END_TIME", Types.TIMESTAMP));
		reservation.addColumn(new Column("", "DOMAIN_ID", Types.VARCHAR));
		
		Table area = new Table("MR_AREA");
		area.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		area.addColumn(new Column("", "NAME", Types.VARCHAR));
		area.addColumn(new Column("", "CREATOR", Types.VARCHAR));
		area.addColumn(new Column("", "CREATOR_ID", Types.VARCHAR));
		area.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		
		Table room = new Table("MR_ROOM");
		room.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		room.addColumn(new Column("", "AREA", Types.VARCHAR));
		room.addColumn(new Column("", "AREA_ID", Types.VARCHAR));
		room.addColumn(new Column("", "CREATOR", Types.VARCHAR));
		room.addColumn(new Column("", "CREATOR_ID", Types.VARCHAR));
		room.addColumn(new Column("", "NUMBER", Types.VARCHAR));
		room.addColumn(new Column("", "NOTE", Types.VARCHAR,"1000"));
		room.addColumn(new Column("", "NAME", Types.VARCHAR));
		room.addColumn(new Column("", "EQUIPMENT", Types.VARCHAR,"1000"));
		room.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		
		initTables.put(reservation.getName(), reservation);
		initTables.put(area.getName(), area);
		initTables.put(room.getName(), room);
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
