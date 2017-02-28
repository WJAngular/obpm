package cn.myapps.core.dynaform.dts.metadata.ejb;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.metadata.ddlutils.DBUtils;
import cn.myapps.core.dynaform.dts.metadata.ddlutils.SQLBuilder;
import cn.myapps.core.dynaform.dts.metadata.ddlutils.db2.Db2SQLBuilder;
import cn.myapps.core.dynaform.dts.metadata.ddlutils.mssql.MsSQLBuilder;
import cn.myapps.core.dynaform.dts.metadata.ddlutils.mysql.MysqlSQLBuilder;
import cn.myapps.core.dynaform.dts.metadata.ddlutils.oracle.OracleSQLBuilder;
public class MetadataProcessBean implements MetadataProcess {
	

	public void doIndexOptimization(DataSource dataSource) throws Exception {
		try {
			Map<String,ITable> tableMap = DBUtils.getTables(dataSource);
			ITable t_flowstatert = tableMap.get("T_FLOWSTATERT");
			if(t_flowstatert != null){
				IIndex index_docid = new IIndex("index_docid", t_flowstatert.getName(), "DOCID", true, 3, "A");
				String sql1 = this.getSQLBuilder(dataSource).createIndex(index_docid);
				DBUtils.executeUpdate(sql1, dataSource);

				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("PARENT");
				columnNameSet.add("COMPLETE");
				columnNameSet.add("FLOWID");
				columnNameSet.add("DOCID");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_flowstatert.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_nodert = tableMap.get("T_NODERT");
			if(t_nodert != null){
				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("DOCID");
				columnNameSet.add("FLOWSTATERT_ID");
				columnNameSet.add("NODEID");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_nodert.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_actorrt = tableMap.get("T_ACTORRT");
			if(t_actorrt != null){
				IIndex index_fsrt = new IIndex("index_fsrt_"+t_actorrt.getName(), t_actorrt.getName(), "FLOWSTATERT_ID", true, 3, "A");
				IIndex index_ndrt = new IIndex("index_ndrt", t_actorrt.getName(), "NODERT_ID", true, 3, "A");
				String sql1 = this.getSQLBuilder(dataSource).createIndex(index_fsrt);
				String sql2 = this.getSQLBuilder(dataSource).createIndex(index_ndrt);
				DBUtils.executeUpdate(sql1, dataSource);
				DBUtils.executeUpdate(sql2, dataSource);

				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("DEADLINE");
				columnNameSet.add("DOC_ID");
				columnNameSet.add("LASTOVERDUEREMINDER");
				columnNameSet.add("PENDING");
				columnNameSet.add("ACTORID");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_actorrt.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_actorhis = tableMap.get("T_ACTORHIS");
			if(t_actorhis != null){
				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("FLOWSTATERT_ID");
				columnNameSet.add("DOC_ID");
				columnNameSet.add("ACTORID");
				columnNameSet.add("NODEHIS_ID");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_actorhis.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_circulator = tableMap.get("T_CIRCULATOR");
			if(t_circulator != null){
				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("FLOWSTATERT_ID");
				columnNameSet.add("DOC_ID");
				columnNameSet.add("ISREAD");
				columnNameSet.add("USERID");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_circulator.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_counter = tableMap.get("T_COUNTER");
			if(t_counter != null){
				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("APPLICATIONID");
				columnNameSet.add("DOMAINID");
				columnNameSet.add("NAME");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_counter.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_document = tableMap.get("T_DOCUMENT");
			if(t_document != null){
				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("STATE");
				columnNameSet.add("PARENT");
				columnNameSet.add("MAPPINGID");
				columnNameSet.add("STATELABEL");
				columnNameSet.add("ISTMP");
				columnNameSet.add("FORMNAME");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_document.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_flow_intervention = tableMap.get("T_FLOW_INTERVENTION");
			if(t_flow_intervention != null){
				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("DOCID");
				columnNameSet.add("FLOWID");
				columnNameSet.add("APPLICATIONID");
				columnNameSet.add("STATUS");
				columnNameSet.add("DOMAINID");
				columnNameSet.add("LASTAUDITOR");
				columnNameSet.add("FLOWNAME");
				columnNameSet.add("INITIATOR");
				columnNameSet.add("STATELABEL");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_flow_intervention.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_flow_proxy = tableMap.get("T_FLOW_PROXY");
			if(t_flow_proxy != null){
				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("OWNER");
				columnNameSet.add("APPLICATIONID");
				columnNameSet.add("DOMAINID");
				columnNameSet.add("FLOWID");
				columnNameSet.add("STATE");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_flow_proxy.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_pending = tableMap.get("T_PENDING");
			if(t_pending != null){
				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("AUDITUSER");
				columnNameSet.add("APPLICATIONID");
				columnNameSet.add("STATE");
				columnNameSet.add("DOMAINID");
				columnNameSet.add("DOCID");
				columnNameSet.add("FORMNAME");
				columnNameSet.add("FORMID");
				columnNameSet.add("AUTHOR");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_pending.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_relationhis = tableMap.get("T_RELATIONHIS");
			if(t_relationhis != null){
				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("AUDITOR");
				columnNameSet.add("STARTNODEID");
				columnNameSet.add("ENDNODEID");
				columnNameSet.add("APPLICATIONID");
				columnNameSet.add("FLOWSTATERT_ID");
				columnNameSet.add("FLOWOPERATION");
				columnNameSet.add("STARTNODENAME");
				columnNameSet.add("ACTIONTIME");
				columnNameSet.add("DOCID");
				columnNameSet.add("FLOWNAME");
				columnNameSet.add("FLOWID");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_relationhis.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_upload = tableMap.get("T_UPLOAD");
			if(t_upload != null){
				Set<String> columnNameSet = new HashSet<String>();
				columnNameSet.add("PATH");
				columnNameSet.add("NAME");
				for(String columnName:columnNameSet){
					IIndex index = new IIndex("index_" + columnName, t_upload.getName(), columnName, true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index);
					DBUtils.executeUpdate(sql, dataSource);
				}
			}
			ITable t_pending_actor_set = tableMap.get("T_PENDING_ACTOR_SET");
			if(t_pending_actor_set != null){
				IIndex index = new IIndex("index_" + "DOCID",
						t_pending_actor_set.getName(), "DOCID", true, 3, "A");
				String sql = this.getSQLBuilder(dataSource).createIndex(index);
				DBUtils.executeUpdate(sql, dataSource);
			}
			Set<Entry<String, ITable>> entrySet = tableMap.entrySet();
			for (Iterator<Entry<String, ITable>> iterator = entrySet.iterator(); iterator.hasNext();) {
				Entry<String, ITable> entry = iterator.next();
				if(entry.getKey().toUpperCase().startsWith("AUTH_")){
					ITable auth_table = entry.getValue();
					IIndex index_docid = new IIndex("index_doc_id_"+auth_table.getName(), auth_table.getName(), "DOC_ID", true, 3, "A");
					String sql = this.getSQLBuilder(dataSource).createIndex(index_docid);
					DBUtils.executeUpdate(sql, dataSource);
				}
				
			}
			
		} catch (Exception e) {
			throw e;
		}finally{
			dataSource.getConnection().close();
		}
	}
	
	public Collection<ITable> getAllTables(DataSource dataSource) throws Exception {
		try {
			return DBUtils.getTables(dataSource).values();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			dataSource.getConnection().close();
		}
		
	}
	
	protected SQLBuilder getSQLBuilder(DataSource dataSource)throws Exception {
		String dbType = dataSource.getDbTypeName();
		String schema = DBUtils.getSchema(dataSource.getConnection(), dbType);
		if (dbType.equals(DBUtils.DBTYPE_ORACLE)) {
			return new OracleSQLBuilder(schema);
		} else if (dbType.equals(DBUtils.DBTYPE_MYSQL)) {
			return new MysqlSQLBuilder(schema);
		}else if (dbType.equals(DBUtils.DBTYPE_MSSQL)) {
			return new MsSQLBuilder(schema);
		}else if (dbType.equals(DBUtils.DBTYPE_DB2)) {
			return new Db2SQLBuilder(schema);
		}
		
		return null;
	}

}
