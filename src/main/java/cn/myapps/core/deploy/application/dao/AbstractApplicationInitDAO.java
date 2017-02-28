package cn.myapps.core.deploy.application.dao;

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

public abstract class AbstractApplicationInitDAO {
	Logger log = Logger.getLogger(AbstractApplicationInitDAO.class);

	protected String dbType = "Oracle: ";

	protected String schema = "";

	protected Connection connection;

	protected AbstractTableDefinition definition;

	protected final static Map<String, Table> INIT_TABLEMAP = new HashMap<String, Table>();

	public AbstractApplicationInitDAO(Connection conn) throws Exception {
		this.connection = conn;

		// T_DOCUMENT表模型
		Table table0 = new Table("T_DOCUMENT");
		table0.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		table0.addColumn(new Column("", "LASTMODIFIED", Types.TIMESTAMP, "6"));
		table0.addColumn(new Column("", "FORMNAME", Types.VARCHAR));
		// table0.addColumn(new Column("", "TASKID", Types.DECIMAL, "19,0"));
		table0.addColumn(new Column("", "AUDITDATE", Types.TIMESTAMP, "6"));
		table0.addColumn(new Column("", "AUTHOR", Types.VARCHAR));
		table0.addColumn(new Column("", "AUTHOR_DEPT_INDEX", Types.VARCHAR,"2000"));
		table0.addColumn(new Column("", "CREATED", Types.TIMESTAMP, "6"));
//		table0.addColumn(new Column("", "ISSUBDOC", Types.BIT, "1,0"));
		// table0.addColumn(new Column("", "CHANNELSNAME", Types.VARCHAR));
		table0.addColumn(new Column("", "FORMID", Types.VARCHAR));
//		table0.addColumn(new Column("", "CHILDS", Types.VARCHAR));  remove since 2.6 
		table0.addColumn(new Column("", "ISTMP", Types.BIT, "1,0"));
//		table0.addColumn(new Column("", "FLOWID", Types.VARCHAR)); remove since 2.6 
		table0.addColumn(new Column("", "VERSIONS", Types.INTEGER, "10,0"));
		table0.addColumn(new Column("", "SORTID", Types.VARCHAR));
		table0.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		table0.addColumn(new Column("", "STATELABEL", Types.VARCHAR));
		table0.addColumn(new Column("", "INITIATOR", Types.VARCHAR));
		table0.addColumn(new Column("", "AUDITUSER", Types.VARCHAR));
		table0.addColumn(new Column("", "AUDITORNAMES", Types.CLOB));
		table0.addColumn(new Column("", "LASTFLOWOPERATION", Types.VARCHAR));
		table0.addColumn(new Column("", "PARENT", Types.VARCHAR));
		table0.addColumn(new Column("", "STATE", Types.VARCHAR));
		table0.addColumn(new Column("", "STATEINT", Types.INTEGER, "10,0"));
		table0.addColumn(new Column("", "LASTMODIFIER", Types.VARCHAR));
		table0.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		table0.addColumn(new Column("", "AUDITORLIST", Types.CLOB));
		table0.addColumn(new Column("", "STATELABELINFO", Types.CLOB));
		table0.addColumn(new Column("", "PREVAUDITNODE", Types.CLOB));
		table0.addColumn(new Column("", "PREVAUDITUSER", Types.CLOB));
		table0.addColumn(new Column("", "OPTIONITEM", Types.CLOB));
		// 映射ID
		table0.addColumn(new Column("", "MAPPINGID", Types.VARCHAR));

		// T_FLOWSTATERT表模型
		Table table1 = new Table("T_FLOWSTATERT");
		table1.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		table1.addColumn(new Column("", "DOCID", Types.VARCHAR));
		table1.addColumn(new Column("", "FLOWID", Types.VARCHAR));
		table1.addColumn(new Column("", "STATE", Types.INTEGER, "10,0"));
		table1.addColumn(new Column("", "PARENT", Types.VARCHAR));
		table1.addColumn(new Column("", "FLOWNAME", Types.VARCHAR));
		table1.addColumn(new Column("", "FLOWXML", Types.CLOB));
		table1.addColumn(new Column("", "LASTMODIFIERID", Types.VARCHAR));
		table1.addColumn(new Column("", "LASTMODIFIED", Types.TIMESTAMP, "6"));
		table1.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		table1.addColumn(new Column("", "SUBFLOWNODEID", Types.VARCHAR));
		table1.addColumn(new Column("", "COMPLETE", Types.INTEGER, "10,0"));
		table1.addColumn(new Column("", "CALLBACK", Types.INTEGER, "10,0"));
		table1.addColumn(new Column("", "TOKEN", Types.VARCHAR));
		table1.addColumn(new Column("", "STATELABEL", Types.VARCHAR));
		table1.addColumn(new Column("", "INITIATOR", Types.VARCHAR));
		table1.addColumn(new Column("", "AUDITUSER", Types.VARCHAR));
		table1.addColumn(new Column("", "AUDITORNAMES", Types.CLOB));
		table1.addColumn(new Column("", "AUDITORLIST", Types.CLOB));
		table1.addColumn(new Column("", "LASTFLOWOPERATION", Types.VARCHAR));
		table1.addColumn(new Column("", "AUDITDATE", Types.TIMESTAMP, "6"));
		table1.addColumn(new Column("", "SUB_POSITION", Types.INTEGER, "10,0"));
		table1.addColumn(new Column("", "ISARCHIVED", Types.INTEGER, "10,0"));
		table1.addColumn(new Column("", "ISTERMINATED", Types.BIT, "1,0"));
		table1.addColumn(new Column("", "PREV_AUDIT_NODE", Types.VARCHAR));
		table1.addColumn(new Column("", "PREV_AUDIT_USER", Types.VARCHAR));

		// T_COUNTER表模型
		Table table2 = new Table("T_COUNTER");
		table2.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		table2.addColumn(new Column("", "COUNTER", Types.INTEGER, "10,0"));
		table2.addColumn(new Column("", "NAME", Types.VARCHAR));
		table2.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		table2.addColumn(new Column("", "DOMAINID", Types.VARCHAR));

		// T_ACTORRT表模型
		Table table3 = new Table("T_ACTORRT");
		table3.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		table3.addColumn(new Column("", "ACTORID", Types.VARCHAR));
		table3.addColumn(new Column("", "NAME", Types.VARCHAR));
		table3.addColumn(new Column("", "ISPROCESSED", Types.BIT, "1,0"));
		table3.addColumn(new Column("", "TYPE", Types.INTEGER, "10,0"));
		table3.addColumn(new Column("", "NODERT_ID", Types.VARCHAR));
		table3.addColumn(new Column("", "FLOWSTATERT_ID", Types.VARCHAR));
		table3.addColumn(new Column("", "DOC_ID", Types.VARCHAR));
		table3.addColumn(new Column("", "DEADLINE", Types.TIMESTAMP));
		table3.addColumn(new Column("", "PENDING", Types.BIT, "1,0"));
		table3.addColumn(new Column("", "ISREAD", Types.BIT, "1,0"));
		table3.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		table3.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		table3.addColumn(new Column("", "APPROVAL_POSITION", Types.INTEGER, "10,0"));
		table3.addColumn(new Column("", "LASTOVERDUEREMINDER", Types.TIMESTAMP));
		table3.addColumn(new Column("", "REMINDER_TIMES", Types.INTEGER, "10,0"));

		// T_NODERT表模型
		Table table4 = new Table("T_NODERT");
		table4.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		table4.addColumn(new Column("", "NAME", Types.VARCHAR));
		table4.addColumn(new Column("", "NODEID", Types.VARCHAR));
		table4.addColumn(new Column("", "FLOWID", Types.VARCHAR));
		table4.addColumn(new Column("", "DOCID", Types.VARCHAR));
		table4.addColumn(new Column("", "FLOWSTATERT_ID", Types.VARCHAR));
		table4.addColumn(new Column("", "NOTIFIABLE", Types.BIT, "1,0"));
		table4.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		table4.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		table4.addColumn(new Column("", "STATELABEL", Types.VARCHAR));
		table4.addColumn(new Column("", "FLOWOPTION", Types.VARCHAR));
		table4.addColumn(new Column("", "SPLITTOKEN", Types.VARCHAR));
		table4.addColumn(new Column("", "PASSCONDITION", Types.INTEGER, "10,0"));
		table4.addColumn(new Column("", "PARENTNODERTID", Types.VARCHAR));
		table4.addColumn(new Column("", "DEADLINE", Types.TIMESTAMP, "6"));
		table4.addColumn(new Column("", "ORDERLY", Types.BIT, "1,0"));
		table4.addColumn(new Column("", "APPROVAL_POSITION", Types.INTEGER, "10,0"));
		table4.addColumn(new Column("", "STATE", Types.INTEGER, "10,0"));
		table4.addColumn(new Column("", "LASTPROCESSTIME",Types.TIMESTAMP));
		table4.addColumn(new Column("", "REMINDER_TIMES", Types.INTEGER, "10,0"));

		// T_RELATIONHIS表模型
		Table table5 = new Table("T_RELATIONHIS");
		table5.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		table5.addColumn(new Column("", "ACTIONTIME", Types.TIMESTAMP));
		table5.addColumn(new Column("", "PROCESSTIME", Types.TIMESTAMP));
		table5.addColumn(new Column("", "STARTNODENAME", Types.VARCHAR));
		table5.addColumn(new Column("", "FLOWID", Types.VARCHAR));
		table5.addColumn(new Column("", "FLOWNAME", Types.VARCHAR));
		table5.addColumn(new Column("", "DOCID", Types.VARCHAR));
		table5.addColumn(new Column("", "ENDNODEID", Types.VARCHAR));
		table5.addColumn(new Column("", "ENDNODENAME", Types.VARCHAR));
		table5.addColumn(new Column("", "STARTNODEID", Types.VARCHAR));
		table5.addColumn(new Column("", "ISPASSED", Types.BIT, "1,0"));
		table5.addColumn(new Column("", "ATTITUDE", Types.VARCHAR,"2000"));
		table5.addColumn(new Column("", "AUDITOR", Types.VARCHAR));
		table5.addColumn(new Column("", "FLOWOPERATION", Types.VARCHAR));
		table5.addColumn(new Column("", "REMINDERCOUNT", Types.INTEGER, "10,0"));
		table5.addColumn(new Column("", "FLOWSTATERT_ID", Types.VARCHAR));
		table5.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));

		// T_ACTORHIS表模型
		Table table6 = new Table("T_ACTORHIS");
		table6.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		table6.addColumn(new Column("", "ACTORID", Types.VARCHAR));
		table6.addColumn(new Column("", "NAME", Types.VARCHAR));
		table6.addColumn(new Column("", "AGENTID", Types.VARCHAR));
		table6.addColumn(new Column("", "AGENTNAME", Types.VARCHAR));
		table6.addColumn(new Column("", "TYPE", Types.INTEGER, "10,0"));
		table6.addColumn(new Column("", "PROCESSTIME", Types.TIMESTAMP));
		table6.addColumn(new Column("", "ATTITUDE", Types.VARCHAR,"2000"));
		table6.addColumn(new Column("", "NODEHIS_ID", Types.VARCHAR));
		table6.addColumn(new Column("", "FLOWSTATERT_ID", Types.VARCHAR));
		table6.addColumn(new Column("", "DOC_ID", Types.VARCHAR));
		table6.addColumn(new Column("", "SIGNATURE", Types.CLOB));

		// T_PENDING表模型
		Table table7 = new Table("T_PENDING");
		table7.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		table7.addColumn(new Column("", "DOCID", Types.VARCHAR));
		table7.addColumn(new Column("", "LASTMODIFIED", Types.TIMESTAMP, "6"));
		table7.addColumn(new Column("", "FORMNAME", Types.VARCHAR));
		table7.addColumn(new Column("", "AUDITDATE", Types.TIMESTAMP, "6"));
		table7.addColumn(new Column("", "AUTHOR", Types.VARCHAR));
		table7.addColumn(new Column("", "CREATED", Types.TIMESTAMP, "6"));
		table7.addColumn(new Column("", "ISSUBDOC", Types.BIT, "1,0"));
		table7.addColumn(new Column("", "FORMID", Types.VARCHAR));
		table7.addColumn(new Column("", "CHILDS", Types.VARCHAR));
		table7.addColumn(new Column("", "ISTMP", Types.BIT, "1,0"));
		table7.addColumn(new Column("", "FLOWID", Types.VARCHAR));
		table7.addColumn(new Column("", "VERSIONS", Types.INTEGER, "10,0"));
		table7.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		table7.addColumn(new Column("", "STATELABEL", Types.VARCHAR));
		table7.addColumn(new Column("", "AUDITUSER", Types.VARCHAR));
		table7.addColumn(new Column("", "AUDITORNAMES", Types.CLOB));
		table7.addColumn(new Column("", "LASTFLOWOPERATION", Types.VARCHAR));
		table7.addColumn(new Column("", "PARENT", Types.VARCHAR));
		table7.addColumn(new Column("", "STATE", Types.VARCHAR));
		table7.addColumn(new Column("", "LASTMODIFIER", Types.VARCHAR));
		table7.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		table7.addColumn(new Column("", "SUMMARY", Types.CLOB));

		// T_PENDING_ACTOR_SET表模型
		Table table8 = new Table("T_PENDING_ACTOR_SET");
		table8.addColumn(new Column("", "DOCID", Types.VARCHAR));
		table8.addColumn(new Column("", "ACTORID", Types.VARCHAR));
		table8.addColumn(new Column("", "DOMAINID", Types.VARCHAR));

		Table table9 = new Table("T_UPLOAD");
		table9.addColumn(new Column("", "ID", Types.VARCHAR));
		table9.addColumn(new Column("", "NAME", Types.VARCHAR));
		table9.addColumn(new Column("", "IMGBINARY", Types.BLOB));
		table9.addColumn(new Column("", "FIELDID", Types.VARCHAR));
		table9.addColumn(new Column("", "TYPE", Types.VARCHAR));
		table9.addColumn(new Column("", "FILESIZE", Types.INTEGER, "10,0"));
		table9.addColumn(new Column("", "USERID", Types.VARCHAR));
		table9.addColumn(new Column("", "MODIFYDATE", Types.VARCHAR));
		table9.addColumn(new Column("", "PATH", Types.LONGVARCHAR));
		table9.addColumn(new Column("", "FOLDERPATH", Types.LONGVARCHAR));
		
		//流程干预信息表
		Table table10 = new Table("T_FLOW_INTERVENTION");
		table10.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		table10.addColumn(new Column("", "SUMMARY", Types.CLOB));
		table10.addColumn(new Column("", "FLOWNAME", Types.VARCHAR));
		table10.addColumn(new Column("", "STATELABEL", Types.VARCHAR));
		table10.addColumn(new Column("", "INITIATOR", Types.VARCHAR));
		table10.addColumn(new Column("", "INITIATORID", Types.VARCHAR));
		table10.addColumn(new Column("", "LASTAUDITOR", Types.VARCHAR));
		table10.addColumn(new Column("", "FIRSTPROCESSTIME", Types.TIMESTAMP, "6"));
		table10.addColumn(new Column("", "LASTPROCESSTIME", Types.TIMESTAMP, "6"));
		table10.addColumn(new Column("", "FLOWID", Types.VARCHAR));
		table10.addColumn(new Column("", "FORMID", Types.VARCHAR));
		table10.addColumn(new Column("", "DOCID", Types.VARCHAR));
		table10.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		table10.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		table10.addColumn(new Column("", "VERSION", Types.INTEGER, "10,0"));
		table10.addColumn(new Column("", "STATUS", Types.VARCHAR));
		table10.addColumn(new Column("", "AUDITORNAMES", Types.CLOB));
		table10.addColumn(new Column("", "AUDITORLIST", Types.CLOB));
		table10.addColumn(new Column("", "LASTFLOWOPERATION", Types.VARCHAR));
		table10.addColumn(new Column("", "INITIATOR_DEPT", Types.VARCHAR));
		table10.addColumn(new Column("", "INITIATOR_DEPT_ID", Types.VARCHAR));
		
		//流程代理信息表
		Table table11 = new Table("T_FLOW_PROXY");
		table11.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		table11.addColumn(new Column("", "FLOWNAME", Types.VARCHAR));
		table11.addColumn(new Column("", "FLOWID", Types.VARCHAR));
		table11.addColumn(new Column("", "DESCRIPTION", Types.VARCHAR));
		table11.addColumn(new Column("", "STATE",  Types.VARCHAR));
		table11.addColumn(new Column("", "AGENTS", Types.CLOB));
		table11.addColumn(new Column("", "AGENTSNAME", Types.CLOB));
		table11.addColumn(new Column("", "OWNER", Types.VARCHAR));
		table11.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		table11.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		table11.addColumn(new Column("", "VERSION", Types.INTEGER, "10,0"));
		
		//抄送人信息表
		Table circulator = new Table("T_CIRCULATOR");
		circulator.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		circulator.addColumn(new Column("", "NAME", Types.VARCHAR));
		circulator.addColumn(new Column("", "USERID", Types.VARCHAR));
		circulator.addColumn(new Column("", "DOC_ID", Types.VARCHAR));
		circulator.addColumn(new Column("", "NODERT_ID",  Types.VARCHAR));
		circulator.addColumn(new Column("", "FLOWSTATERT_ID",  Types.VARCHAR));
		circulator.addColumn(new Column("", "CCTIME",  Types.TIMESTAMP, "6"));
		circulator.addColumn(new Column("", "READTIME", Types.TIMESTAMP, "6"));
		circulator.addColumn(new Column("", "DEADLINE", Types.TIMESTAMP, "6"));
		circulator.addColumn(new Column("", "ISREAD", Types.INTEGER, "10,0"));
		circulator.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		circulator.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		circulator.addColumn(new Column("", "VERSION", Types.INTEGER, "10,0"));
		
		//数据地图模板表
		Table datamapTemplate = new Table("T_DATAMAPTEMPLATE");
		datamapTemplate.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		datamapTemplate.addColumn(new Column("", "DATAMAPCFG_ID", Types.VARCHAR));
		datamapTemplate.addColumn(new Column("", "CULEFIELD", Types.VARCHAR));
		datamapTemplate.addColumn(new Column("", "CULEFIELD2", Types.VARCHAR));
		datamapTemplate.addColumn(new Column("", "TEMPLATE", Types.CLOB));
		datamapTemplate.addColumn(new Column("", "CONTENT", Types.CLOB));
		datamapTemplate.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		datamapTemplate.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		
		//
		Table flowReminderHistory = new Table("T_WORKFLOW_REMINDER_HISTORY");
		flowReminderHistory.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		flowReminderHistory.addColumn(new Column("", "REMINDER_CONTENT", Types.VARCHAR));
		flowReminderHistory.addColumn(new Column("", "USER_ID", Types.VARCHAR));
		flowReminderHistory.addColumn(new Column("", "USER_NAME", Types.VARCHAR));
		flowReminderHistory.addColumn(new Column("", "NODE_NAME", Types.VARCHAR));
		flowReminderHistory.addColumn(new Column("", "DOC_ID", Types.VARCHAR));
		flowReminderHistory.addColumn(new Column("", "FLOW_INSTANCE_ID", Types.VARCHAR));
		flowReminderHistory.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		flowReminderHistory.addColumn(new Column("", "APPLICATIONID", Types.VARCHAR));
		flowReminderHistory.addColumn(new Column("", "PROCESS_TIME", Types.TIMESTAMP));
		
		

		INIT_TABLEMAP.put(table0.getName(), table0);
		INIT_TABLEMAP.put(table1.getName(), table1);
		INIT_TABLEMAP.put(table2.getName(), table2);
		INIT_TABLEMAP.put(table3.getName(), table3);
		INIT_TABLEMAP.put(table4.getName(), table4);
		INIT_TABLEMAP.put(table5.getName(), table5);//T_RELATIONHIS
		INIT_TABLEMAP.put(table6.getName(), table6);
		INIT_TABLEMAP.put(table7.getName(), table7);
		INIT_TABLEMAP.put(table8.getName(), table8);//T_PENDING_ACTOR_SET
		INIT_TABLEMAP.put(table9.getName(), table9);//T_UPLOAD
		INIT_TABLEMAP.put(table10.getName(), table10);
		INIT_TABLEMAP.put(table11.getName(), table11);
		INIT_TABLEMAP.put(circulator.getName(), circulator);
		INIT_TABLEMAP.put(datamapTemplate.getName(), datamapTemplate);
		INIT_TABLEMAP.put(flowReminderHistory.getName(), flowReminderHistory);
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
}
