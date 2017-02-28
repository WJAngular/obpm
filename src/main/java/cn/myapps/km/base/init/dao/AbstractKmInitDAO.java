package cn.myapps.km.base.init.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

public abstract class AbstractKmInitDAO {
	
	Logger log = Logger.getLogger(AbstractKmInitDAO.class);

	protected String dbType = "Oracle: ";

	protected String schema = "";

	protected Connection connection;

	protected AbstractTableDefinition definition;

	protected final static Map<String, Table> INIT_TABLEMAP = new HashMap<String, Table>();
	
	public AbstractKmInitDAO(Connection conn) throws Exception {
		this.connection = conn;
		
		Table logs = new Table("KM_LOGS");
		logs.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		logs.addColumn(new Column("", "OPERATION_TYPE", Types.VARCHAR));
		logs.addColumn(new Column("", "FILE_ID", Types.VARCHAR));
		logs.addColumn(new Column("", "FILE_NAME", Types.VARCHAR));
		logs.addColumn(new Column("", "USER_ID", Types.VARCHAR));
		logs.addColumn(new Column("", "USER_NAME",  Types.VARCHAR));
		logs.addColumn(new Column("", "OPERATIONDATE",  Types.TIMESTAMP, "6"));
		logs.addColumn(new Column("", "USER_IP", Types.VARCHAR));
		logs.addColumn(new Column("", "OPERATION_CONTENT", Types.VARCHAR));
		logs.addColumn(new Column("", "DEPARTMENT_ID", Types.VARCHAR));
		logs.addColumn(new Column("", "DEPARTMENT_NAME", Types.VARCHAR));
		logs.addColumn(new Column("", "OPERATION_FILE_OR_DIRECTORY", Types.INTEGER, "10,0"));
		
		Table comments = new Table("KM_COMMENTS");
		comments.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		comments.addColumn(new Column("", "FILE_ID", Types.VARCHAR));
		comments.addColumn(new Column("", "USER_ID", Types.VARCHAR));
		comments.addColumn(new Column("", "USER_NAME",  Types.VARCHAR));
		comments.addColumn(new Column("", "ASSESSMENTDATE",  Types.TIMESTAMP, "6"));
		comments.addColumn(new Column("", "GOOD",  Types.BIT, "1,0"));
		comments.addColumn(new Column("", "BAD",  Types.BIT, "1,0"));
		comments.addColumn(new Column("", "CONTENT", Types.VARCHAR));
		
		
		
		Table disk = new Table("KM_NDISK");
		disk.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		disk.addColumn(new Column("", "NAME", Types.VARCHAR));
		disk.addColumn(new Column("", "TYPE", Types.INTEGER, "10,0"));
		disk.addColumn(new Column("", "OWNER_ID", Types.VARCHAR));
		disk.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		disk.addColumn(new Column("", "DIR_ID",  Types.VARCHAR));
		
		Table dir = new Table("KM_NDIR");
		dir.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		dir.addColumn(new Column("", "NAME", Types.VARCHAR));
		dir.addColumn(new Column("", "TYPE", Types.INTEGER, "10,0"));
		dir.addColumn(new Column("", "OWNER_ID", Types.VARCHAR));
		dir.addColumn(new Column("", "PARENT_ID",  Types.VARCHAR));
		dir.addColumn(new Column("", "NDISK_ID",  Types.VARCHAR));
		dir.addColumn(new Column("", "PATH", Types.LONGVARCHAR));
		dir.addColumn(new Column("", "CREATEDATE",  Types.TIMESTAMP, "6"));
		dir.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		
		Table file = new Table("KM_NFILE");
		file.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		file.addColumn(new Column("", "NAME", Types.VARCHAR));
		file.addColumn(new Column("", "TYPE", Types.VARCHAR));
		file.addColumn(new Column("", "OWNERID", Types.VARCHAR));
		file.addColumn(new Column("", "URL", Types.LONGVARCHAR));
		file.addColumn(new Column("", "VERSION", Types.INTEGER, "10,0"));
		file.addColumn(new Column("", "MEMO", Types.CLOB));
		file.addColumn(new Column("", "FILESIZE", Types.INTEGER, "10,0"));
		file.addColumn(new Column("", "LASTMODIFY",  Types.TIMESTAMP, "6"));
		file.addColumn(new Column("", "CREATEDATE",  Types.TIMESTAMP, "6"));
		file.addColumn(new Column("", "CREATORID",  Types.VARCHAR));
		file.addColumn(new Column("", "STATE", Types.INTEGER, "10,0"));
		file.addColumn(new Column("", "NDIRID",  Types.VARCHAR));
		file.addColumn(new Column("", "SHAREID",  Types.VARCHAR));
		file.addColumn(new Column("", "TITLE",  Types.VARCHAR));
		file.addColumn(new Column("", "ORIGIN", Types.INTEGER, "10,0"));
		file.addColumn(new Column("", "CREATOR",  Types.VARCHAR));
		file.addColumn(new Column("", "CLASSIFICATION",  Types.VARCHAR));
		file.addColumn(new Column("", "ROOT_CATEGORY_ID",  Types.VARCHAR));
		file.addColumn(new Column("", "SUB_CATEGORY_ID",  Types.VARCHAR));
		file.addColumn(new Column("", "VIEWS",  Types.NUMERIC));
		file.addColumn(new Column("", "DOWNLOADS",  Types.NUMERIC));
		file.addColumn(new Column("", "FAVORITES",  Types.NUMERIC));
		file.addColumn(new Column("", "DOMAIN_ID",  Types.VARCHAR));
		//file.addColumn(new Column("", "DOMAIN_ID",  Types.INTEGER, "10,0"));
		file.addColumn(new Column("", "GOOD",  Types.INTEGER, "10,0"));
		file.addColumn(new Column("", "BAD",  Types.INTEGER, "10,0"));
		file.addColumn(new Column("", "DEPARTMENT", Types.VARCHAR));
		file.addColumn(new Column("", "DEPARTMENT_ID", Types.VARCHAR));
		
		Table permission = new Table("KM_PERMISSION");
		permission.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		permission.addColumn(new Column("", "NAME", Types.VARCHAR));
		permission.addColumn(new Column("", "FILETYPE", Types.INTEGER, "10,0"));
		permission.addColumn(new Column("", "FILEID", Types.VARCHAR));
		permission.addColumn(new Column("", "SCOPE", Types.VARCHAR));
		permission.addColumn(new Column("", "OWNERIDS", Types.LONGVARCHAR));
		permission.addColumn(new Column("", "OWNERNAMES", Types.LONGVARCHAR));
		permission.addColumn(new Column("", "STARTDATE",  Types.TIMESTAMP, "6"));
		permission.addColumn(new Column("", "ENDDATE",  Types.TIMESTAMP, "6"));
		permission.addColumn(new Column("", "READMODE", Types.INTEGER, "10,0"));
		permission.addColumn(new Column("", "WRITEMODE", Types.INTEGER, "10,0"));
		permission.addColumn(new Column("", "DOWNLOADMODE", Types.INTEGER, "10,0"));
		permission.addColumn(new Column("", "PRINTMODE", Types.INTEGER, "10,0"));
		permission.addColumn(new Column("", "USERID", Types.VARCHAR));
		permission.addColumn(new Column("", "ROLEID", Types.VARCHAR));
		permission.addColumn(new Column("", "DEPTID", Types.VARCHAR));
		Table fileaccess = new Table("KM_FILEACCESS");
		fileaccess.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		fileaccess.addColumn(new Column("", "FILEID", Types.VARCHAR));
		fileaccess.addColumn(new Column("", "SCOPE", Types.VARCHAR));
		fileaccess.addColumn(new Column("", "OWNERID", Types.VARCHAR));
		fileaccess.addColumn(new Column("", "STARTDATE",  Types.TIMESTAMP, "6"));
		fileaccess.addColumn(new Column("", "ENDDATE",  Types.TIMESTAMP, "6"));
		fileaccess.addColumn(new Column("", "READMODE", Types.INTEGER, "10,0"));
		fileaccess.addColumn(new Column("", "WRITEMODE", Types.INTEGER, "10,0"));
		fileaccess.addColumn(new Column("", "DOWNLOADMODE", Types.INTEGER, "10,0"));
		fileaccess.addColumn(new Column("", "PRINTMODE", Types.INTEGER, "10,0"));
		fileaccess.addColumn(new Column("", "PERMISSIONID", Types.VARCHAR));
		
		Table diraccess = new Table("KM_DIRACCESS");
		diraccess.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		diraccess.addColumn(new Column("", "FILEID", Types.VARCHAR));
		diraccess.addColumn(new Column("", "SCOPE", Types.VARCHAR));
		diraccess.addColumn(new Column("", "OWNERID", Types.VARCHAR));
		diraccess.addColumn(new Column("", "STARTDATE",  Types.TIMESTAMP, "6"));
		diraccess.addColumn(new Column("", "ENDDATE",  Types.TIMESTAMP, "6"));
		diraccess.addColumn(new Column("", "READMODE", Types.INTEGER, "10,0"));
		diraccess.addColumn(new Column("", "WRITEMODE", Types.INTEGER, "10,0"));
		diraccess.addColumn(new Column("", "DOWNLOADMODE", Types.INTEGER, "10,0"));
		diraccess.addColumn(new Column("", "PRINTMODE", Types.INTEGER, "10,0"));
		diraccess.addColumn(new Column("", "PERMISSIONID", Types.VARCHAR));
		
		Table role = new Table("KM_ROLE");
		role.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		role.addColumn(new Column("", "NAME", Types.VARCHAR));
		role.addColumn(new Column("", "R_LEVEL", Types.INTEGER, "10,0"));
		
		Table user_role_set = new Table("KM_USER_ROLE_SET");
		user_role_set.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		user_role_set.addColumn(new Column("", "USERID", Types.VARCHAR));
		user_role_set.addColumn(new Column("", "ROLEID", Types.VARCHAR));
		
		Table category = new Table("KM_CATEGORY");
		category.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		category.addColumn(new Column("", "NAME", Types.VARCHAR));
		category.addColumn(new Column("", "DESCRIPTION", Types.LONGVARCHAR));
		category.addColumn(new Column("", "PARENT_ID", Types.VARCHAR));
		category.addColumn(new Column("", "SORT", Types.INTEGER, "10,0"));
		category.addColumn(new Column("", "DOMAIN_ID", Types.VARCHAR));
		
		Table managePermission = new Table("KM_MANAGE_PERMISSION");
		managePermission.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		managePermission.addColumn(new Column("", "NAME", Types.VARCHAR));
		managePermission.addColumn(new Column("", "DOMAIN_ID", Types.VARCHAR));
		managePermission.addColumn(new Column("", "RESOURCE_TYPE", Types.VARCHAR));
		managePermission.addColumn(new Column("", "RESOURCE_ID", Types.VARCHAR));
		managePermission.addColumn(new Column("", "SCOPE", Types.VARCHAR));
		managePermission.addColumn(new Column("", "OWNERIDS", Types.LONGVARCHAR));
		managePermission.addColumn(new Column("", "OWNERNAMES", Types.LONGVARCHAR));
		
		Table managePermissionItem = new Table("KM_MANAGE_PERMISSION_ITEM");
		managePermissionItem.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		managePermissionItem.addColumn(new Column("", "DOMAIN_ID", Types.VARCHAR));
		managePermissionItem.addColumn(new Column("", "RESOURCE_TYPE", Types.VARCHAR));
		managePermissionItem.addColumn(new Column("", "RESOURCE_ID", Types.VARCHAR));
		managePermissionItem.addColumn(new Column("", "SCOPE", Types.VARCHAR));
		managePermissionItem.addColumn(new Column("", "OWNER", Types.VARCHAR));
		managePermissionItem.addColumn(new Column("", "PERMISSION", Types.VARCHAR));
		
		/*--------------------------------------加入baike表结构----------------------------------------------*/
		//词条
		Table entry = new Table("BAIKE_ENTRY");
		entry.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		entry.addColumn(new Column("", "NAME", Types.VARCHAR));
		entry.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		entry.addColumn(new Column("", "CREATED", Types.DATE));
		entry.addColumn(new Column("", "AUTHOR", Types.VARCHAR));
		entry.addColumn(new Column("", "CONTENT",  Types.VARCHAR));
		entry.addColumn(new Column("", "EDIT_COUNT", Types.INTEGER));
		entry.addColumn(new Column("", "LATESTCONTENTID", Types.VARCHAR));
		entry.addColumn(new Column("", "CATEGORYID", Types.VARCHAR));
		entry.addColumn(new Column("", "KEYWORD", Types.VARCHAR));
		entry.addColumn(new Column("", "POINTS", Types.INTEGER));
		entry.addColumn(new Column("", "BROWSE_COUNT", Types.INTEGER));
		
		//词条分类
		Table baikeCategory = new Table("BAIKE_CATEGORY");
		baikeCategory.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		baikeCategory.addColumn(new Column("", "NAME", Types.VARCHAR));
		baikeCategory.addColumn(new Column("", "DESCRIPTION", Types.CLOB));
		baikeCategory.addColumn(new Column("", "PARENTID", Types.VARCHAR));
		baikeCategory.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		baikeCategory.addColumn(new Column("", "ORDERBY", Types.INTEGER));
		
		//词条内容
		Table entryContent = new Table("BAIKE_ENTRY_CONTENT");
		entryContent.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		//entryContent.addColumn(new Column("", "NAME", Types.VARCHAR));
		//entryContent.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		entryContent.addColumn(new Column("", "ENTRYID", Types.VARCHAR));
		entryContent.addColumn(new Column("", "AUTHOR", Types.VARCHAR));
		entryContent.addColumn(new Column("", "SAVETIME", Types.DATE));
		entryContent.addColumn(new Column("", "SUBMMITTIME", Types.DATE));
		entryContent.addColumn(new Column("", "HANDLETIME", Types.DATE));
		entryContent.addColumn(new Column("", "VERSIONNUM", Types.INTEGER));
		entryContent.addColumn(new Column("", "REASON", Types.CLOB));
		entryContent.addColumn(new Column("", "STATE", Types.VARCHAR));
		entryContent.addColumn(new Column("", "SUMMARY", Types.CLOB));
		entryContent.addColumn(new Column("", "CONTENT",  Types.CLOB));
		
		//词条内容参考资料
		Table referenceMaterial = new Table("BAIKE_ENTRY_REFERENCEMATERIAL");
		referenceMaterial.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		//referenceMaterial.addColumn(new Column("", "NAME", Types.VARCHAR));
		//referenceMaterial.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		referenceMaterial.addColumn(new Column("", "ENTRYCONTENTID", Types.VARCHAR));
		referenceMaterial.addColumn(new Column("", "ARTICLENAME", Types.VARCHAR));
		referenceMaterial.addColumn(new Column("", "URL", Types.VARCHAR));
		referenceMaterial.addColumn(new Column("", "WEBNAME", Types.VARCHAR));
		referenceMaterial.addColumn(new Column("", "PUBLISHDATE", Types.DATE));
		referenceMaterial.addColumn(new Column("", "REFERENCEDATE", Types.DATE));
		
		//百科用户属性
		Table userAttribute = new Table("BAIKE_USER_ATTRIBUTE");
		userAttribute.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		userAttribute.addColumn(new Column("", "USERID", Types.VARCHAR));
		//userAttribute.addColumn(new Column("", "DOMAINID", Types.VARCHAR));
		userAttribute.addColumn(new Column("", "INTEGRAL", Types.INTEGER));
		userAttribute.addColumn(new Column("", "THROUGHPUTRATE", Types.INTEGER));
		userAttribute.addColumn(new Column("", "FIELD1", Types.VARCHAR));
		userAttribute.addColumn(new Column("", "FIELD2", Types.VARCHAR));
		userAttribute.addColumn(new Column("", "FIELD3", Types.VARCHAR));
		userAttribute.addColumn(new Column("", "FIELD4", Types.VARCHAR));
		userAttribute.addColumn(new Column("", "FIELD5", Types.VARCHAR));
		
		//百科用户与词条关联关系
		Table userEntrySet = new Table("BAIKE_USER_ENTRY_SET");
		userEntrySet.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		userEntrySet.addColumn(new Column("", "USERID", Types.VARCHAR));
		userEntrySet.addColumn(new Column("", "ENTRYID", Types.VARCHAR));
		userEntrySet.addColumn(new Column("", "TYPE", Types.VARCHAR));
		
		//知识悬赏答案
		Table knowledgeAnswer = new Table("BAIKE_ENTRY_KNOWLEDGEANSWER");
		knowledgeAnswer.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		knowledgeAnswer.addColumn(new Column("", "QUESTIONID", Types.VARCHAR));
		knowledgeAnswer.addColumn(new Column("", "AUTHOR", Types.VARCHAR));
		knowledgeAnswer.addColumn(new Column("", "CONTENT", Types.CLOB));
		knowledgeAnswer.addColumn(new Column("", "SUBMITTIME", Types.DATE));
		knowledgeAnswer.addColumn(new Column("", "STATE", Types.VARCHAR));
		
		//知识悬赏问题
		Table knowledgeQuestion = new Table("BAIKE_ENTRY_KNOWLEDGEQUESTION");
		knowledgeQuestion.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		knowledgeQuestion.addColumn(new Column("", "CATEGORYID", Types.VARCHAR));
		knowledgeQuestion.addColumn(new Column("", "AUTHOR", Types.VARCHAR));
		knowledgeQuestion.addColumn(new Column("", "TITLE", Types.VARCHAR));
		knowledgeQuestion.addColumn(new Column("", "CONTENT", Types.CLOB));
		knowledgeQuestion.addColumn(new Column("", "CREATED", Types.DATE));
		knowledgeQuestion.addColumn(new Column("", "POINT", Types.NUMERIC));
		
		//驳回理由
		Table history = new Table("BAIKE_READ_HISTORY");
		history.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		history.addColumn(new Column("", "AUTHOR", Types.VARCHAR));
		history.addColumn(new Column("", "ENTRYID", Types.VARCHAR));
		history.addColumn(new Column("", "READTIME", Types.DATE));
		history.addColumn(new Column("", "ENTRYNAME", Types.VARCHAR));
		
		//浏览记录
		Table reason = new Table("BAIKE_REJECT_REASON");
		reason.addColumn(new Column("", "ID", Types.VARCHAR, true, true));
		reason.addColumn(new Column("", "CONTENTID", Types.VARCHAR));
		reason.addColumn(new Column("", "REASON", Types.CLOB));
		reason.addColumn(new Column("", "REJECTTIME", Types.DATE));
				
		INIT_TABLEMAP.put(entryContent.getName(), entryContent);
		INIT_TABLEMAP.put(referenceMaterial.getName(), referenceMaterial);
		INIT_TABLEMAP.put(entry.getName(), entry);
		INIT_TABLEMAP.put(baikeCategory.getName(), baikeCategory);
		INIT_TABLEMAP.put(userAttribute.getName(), userAttribute);
		INIT_TABLEMAP.put(userEntrySet.getName(), userEntrySet);
		INIT_TABLEMAP.put(knowledgeAnswer.getName(), knowledgeAnswer);
		INIT_TABLEMAP.put(knowledgeQuestion.getName(), knowledgeQuestion);
		INIT_TABLEMAP.put(history.getName(), history);
		INIT_TABLEMAP.put(reason.getName(), reason);
		/*--------------------------------------------------------------------------*/
		
		INIT_TABLEMAP.put(logs.getName(), logs);
		INIT_TABLEMAP.put(comments.getName(), comments);
		INIT_TABLEMAP.put(disk.getName(), disk);
		INIT_TABLEMAP.put(dir.getName(), dir);
		INIT_TABLEMAP.put(file.getName(), file);
		INIT_TABLEMAP.put(permission.getName(), permission);
		INIT_TABLEMAP.put(fileaccess.getName(), fileaccess);
		INIT_TABLEMAP.put(diraccess.getName(), diraccess);
		INIT_TABLEMAP.put(role.getName(), role);
		INIT_TABLEMAP.put(user_role_set.getName(), user_role_set);
		INIT_TABLEMAP.put(category.getName(), category);
		INIT_TABLEMAP.put(managePermission.getName(), managePermission);
		INIT_TABLEMAP.put(managePermissionItem.getName(), managePermissionItem);
		
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
		updateLogsSchema();
	}
	
	/**
	 * 更新KM_LOGS表的OPERATION_FILE_OR_DIRECTORY字段，设置默认值
	 * @throws SQLException
	 */
	private void updateLogsSchema() throws SQLException {
		String sql = "UPDATE "
			+ getFullTableName("KM_LOGS")
			+ " SET  OPERATION_FILE_OR_DIRECTORY = 1 WHERE OPERATION_FILE_OR_DIRECTORY is null";
			log.info(sql);
			PreparedStatement stmt =null;
			try {
				stmt = connection.prepareStatement(sql);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}finally{
				if(stmt !=null) stmt.close();
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
