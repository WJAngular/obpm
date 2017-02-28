package cn.myapps.util;

import java.sql.Connection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.counter.dao.DB2CounterDAO;
import cn.myapps.core.counter.dao.HsqldbCounterDAO;
import cn.myapps.core.counter.dao.MssqlCounterDAO;
import cn.myapps.core.counter.dao.MysqlCounterDAO;
import cn.myapps.core.counter.dao.OracleCounterDAO;
import cn.myapps.core.datamap.runtime.dao.Db2DataMapTemplateDAO;
import cn.myapps.core.datamap.runtime.dao.HsqlDataMapTemplateDAO;
import cn.myapps.core.datamap.runtime.dao.MssqlDataMapTemplateDAO;
import cn.myapps.core.datamap.runtime.dao.MysqlDataMapTemplateDAO;
import cn.myapps.core.datamap.runtime.dao.OracleDataMapTemplateDAO;
import cn.myapps.core.deploy.application.dao.AbstractApplicationInitDAO;
import cn.myapps.core.deploy.application.dao.DB2ApplicationInitDAO;
import cn.myapps.core.deploy.application.dao.HsqldbApplicationInitDAO;
import cn.myapps.core.deploy.application.dao.MssqlApplicationInitDAO;
import cn.myapps.core.deploy.application.dao.MysqlApplicationInitDAO;
import cn.myapps.core.deploy.application.dao.OracleApplicationInitDAO;
import cn.myapps.core.dynaform.document.dao.DB2DocStaticTblDAO;
import cn.myapps.core.dynaform.document.dao.HsqldbDocStaticTblDAO;
import cn.myapps.core.dynaform.document.dao.MssqlDocStaticTblDAO;
import cn.myapps.core.dynaform.document.dao.MysqlDocStaticTblDAO;
import cn.myapps.core.dynaform.document.dao.OracleDocStaticTblDAO;
import cn.myapps.core.dynaform.form.dao.DB2FormTableDAO;
import cn.myapps.core.dynaform.form.dao.HsqldbFormTableDAO;
import cn.myapps.core.dynaform.form.dao.MssqlFormTableDAO;
import cn.myapps.core.dynaform.form.dao.MysqlFormTableDAO;
import cn.myapps.core.dynaform.form.dao.OracleFormTableDAO;
import cn.myapps.core.dynaform.pending.dao.DB2PendingDAO;
import cn.myapps.core.dynaform.pending.dao.HsqldbPendingDAO;
import cn.myapps.core.dynaform.pending.dao.MssqlPendingDAO;
import cn.myapps.core.dynaform.pending.dao.MysqlPendingDAO;
import cn.myapps.core.dynaform.pending.dao.OraclePendingDAO;
import cn.myapps.core.report.crossreport.runtime.dao.OracleRuntimeDAO;
import cn.myapps.core.report.oreport.dao.DB2OReportDAO;
import cn.myapps.core.report.oreport.dao.HsqldbOReportDAO;
import cn.myapps.core.report.oreport.dao.MssqlOReportDAO;
import cn.myapps.core.report.oreport.dao.MysqlOReportDAO;
import cn.myapps.core.report.oreport.dao.OracleOReportDAO;
import cn.myapps.core.report.standardreport.dao.DB2StandarReportDAO;
import cn.myapps.core.report.standardreport.dao.HsqldbStandarReportDAO;
import cn.myapps.core.report.standardreport.dao.MssqlStandarReportDAO;
import cn.myapps.core.report.standardreport.dao.MysqlStandarReportDAO;
import cn.myapps.core.report.standardreport.dao.OracleStandarReportDAO;
import cn.myapps.core.upload.dao.DB2UploadDAO;
import cn.myapps.core.upload.dao.HsqldbUploadDAO;
import cn.myapps.core.upload.dao.MssqlUploadDAO;
import cn.myapps.core.upload.dao.MysqlUploadDAO;
import cn.myapps.core.upload.dao.OracleUploadDAO;
import cn.myapps.core.workflow.analyzer.DB2AnalyzerDAO;
import cn.myapps.core.workflow.analyzer.HsqldbAnalyzerDAO;
import cn.myapps.core.workflow.analyzer.MssqlAnalyzerDAO;
import cn.myapps.core.workflow.analyzer.MysqlAnalyzerDAO;
import cn.myapps.core.workflow.analyzer.OracleAnalyzerDAO;
import cn.myapps.core.workflow.notification.dao.DB2NotificationDAO;
import cn.myapps.core.workflow.notification.dao.HsqldbNotificationDAO;
import cn.myapps.core.workflow.notification.dao.MssqlNotificationDAO;
import cn.myapps.core.workflow.notification.dao.MysqlNotificationDAO;
import cn.myapps.core.workflow.notification.dao.OracleNotificationDAO;
import cn.myapps.core.workflow.storage.runtime.dao.DB2ActorRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.DB2CirculatorDAO;
import cn.myapps.core.workflow.storage.runtime.dao.DB2FlowReminderHistoryDAO;
import cn.myapps.core.workflow.storage.runtime.dao.DB2FlowStateRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.DB2NodeRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.DB2RelationHISDAO;
import cn.myapps.core.workflow.storage.runtime.dao.HsqldbActorRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.HsqldbCirculatorDAO;
import cn.myapps.core.workflow.storage.runtime.dao.HsqldbFlowReminderHistoryDAO;
import cn.myapps.core.workflow.storage.runtime.dao.HsqldbFlowStateRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.HsqldbNodeRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.HsqldbRelationHISDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MssqlActorRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MssqlCirculatorDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MssqlFlowReminderHistoryDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MssqlFlowStateRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MssqlNodeRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MssqlRelationHISDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MysqlActorRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MysqlCirculatorDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MysqlFlowReminderHistoryDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MysqlFlowStateRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MysqlNodeRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.MysqlRelationHISDAO;
import cn.myapps.core.workflow.storage.runtime.dao.OracleActorRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.OracleCirculatorDAO;
import cn.myapps.core.workflow.storage.runtime.dao.OracleFlowReminderHistoryDAO;
import cn.myapps.core.workflow.storage.runtime.dao.OracleFlowStateRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.OracleNodeRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.OracleRelationHISDAO;
import cn.myapps.core.workflow.storage.runtime.intervention.dao.DB2FlowInterventionDAO;
import cn.myapps.core.workflow.storage.runtime.intervention.dao.HsqldbFlowInterventionDAO;
import cn.myapps.core.workflow.storage.runtime.intervention.dao.MssqlFlowInterventionDAO;
import cn.myapps.core.workflow.storage.runtime.intervention.dao.MysqlFlowInterventionDAO;
import cn.myapps.core.workflow.storage.runtime.intervention.dao.OracleFlowInterventionDAO;
import cn.myapps.core.workflow.storage.runtime.proxy.dao.DB2WorkflowProxyDAO;
import cn.myapps.core.workflow.storage.runtime.proxy.dao.HsqldbWorkflowProxyDAO;
import cn.myapps.core.workflow.storage.runtime.proxy.dao.MssqlWorkflowProxyDAO;
import cn.myapps.core.workflow.storage.runtime.proxy.dao.MysqlWorkflowProxyDAO;
import cn.myapps.core.workflow.storage.runtime.proxy.dao.OracleWorkflowProxyDAO;

public class RuntimeDaoManager {

	public IRuntimeDAO getActorRtDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleActorRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlActorRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlActorRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbActorRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2ActorRTDAO(conn);
			}
		}
		return null;
	}

	public IRuntimeDAO getCounterDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleCounterDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlCounterDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlCounterDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbCounterDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2CounterDAO(conn);
			}
		}
		return null;
	}

	public IRuntimeDAO getDocStaticTblDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleDocStaticTblDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlDocStaticTblDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlDocStaticTblDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbDocStaticTblDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2DocStaticTblDAO(conn, applicationId);
			}
		}
		return null;
	}

	public IRuntimeDAO abstractUploadDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleUploadDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlUploadDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlUploadDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbUploadDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2UploadDAO(conn, applicationId);
			}
		}
		return null;
	}

	public IRuntimeDAO getFlowStateRTDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleFlowStateRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlFlowStateRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlFlowStateRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbFlowStateRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2FlowStateRTDAO(conn);
			}
		}
		return null;
	}

	public IRuntimeDAO getFormTableDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			return getFormTableDAOByDbType(conn, dbType);
		}
		return null;
	}

	public IRuntimeDAO getFormTableDAODtId(Connection conn, String datasourceId)
			throws Exception {
		if (datasourceId != null) {
			String dbType = DbTypeUtil.getDBTypeByDtId(datasourceId);
			return getFormTableDAOByDbType(conn, dbType);
		}
		return null;
	}

	public IRuntimeDAO getFormTableDAOByDbType(Connection conn, String dbType)
			throws Exception {
		if (DbTypeUtil.DBTYPE_ORACLE.equals(dbType)) {
			return new OracleFormTableDAO(conn);
		} else if (DbTypeUtil.DBTYPE_MSSQL.equals(dbType)) {
			return new MssqlFormTableDAO(conn);
		} else if (DbTypeUtil.DBTYPE_MYSQL.equals(dbType)) {
			return new MysqlFormTableDAO(conn);
		} else if (DbTypeUtil.DBTYPE_HSQLDB.equals(dbType)) {
			return new HsqldbFormTableDAO(conn);
		} else if (DbTypeUtil.DBTYPE_DB2.equals(dbType)) {
			return new DB2FormTableDAO(conn);
		}
		return null;
	}

	public IRuntimeDAO getNodeRTDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleNodeRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlNodeRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlNodeRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbNodeRTDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2NodeRTDAO(conn);
			}
		}
		return null;
	}

	public IRuntimeDAO getRelationHisDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleRelationHISDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlRelationHISDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlRelationHISDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbRelationHISDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2RelationHISDAO(conn);
			}
		}
		return null;
	}

	public AbstractApplicationInitDAO getApplicationInitDAO(Connection conn,
			String applicationId, String dbType) throws Exception {
		if (dbType.trim().equals("")) {
			if (applicationId != null) {
				dbType = DbTypeUtil.getDBType(applicationId);
			}
		}
		if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
			return new OracleApplicationInitDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
			return new MssqlApplicationInitDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
			return new MysqlApplicationInitDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
			return new HsqldbApplicationInitDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
			return new DB2ApplicationInitDAO(conn);
		}
		return null;
	}

	public IRuntimeDAO getPendingDAO(Connection conn, String applicationId)
			throws Exception {
		String dbType = DbTypeUtil.getDBType(applicationId);
		if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
			return new OraclePendingDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
			return new MssqlPendingDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
			return new MysqlPendingDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
			return new HsqldbPendingDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
			return new DB2PendingDAO(conn);
		}
		return null;
	}

	public IRuntimeDAO getFlowInterventionDAO(Connection conn,
			String applicationId) throws Exception {
		String dbType = DbTypeUtil.getDBType(applicationId);
		if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
			return new OracleFlowInterventionDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
			return new MssqlFlowInterventionDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
			return new MysqlFlowInterventionDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
			return new HsqldbFlowInterventionDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
			return new DB2FlowInterventionDAO(conn);
		}
		return null;
	}

	public IRuntimeDAO getWorkflowProxyDAO(Connection conn, String applicationId)
			throws Exception {
		String dbType = DbTypeUtil.getDBType(applicationId);
		if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
			return new OracleWorkflowProxyDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
			return new MssqlWorkflowProxyDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
			return new MysqlWorkflowProxyDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
			return new HsqldbWorkflowProxyDAO(conn);
		} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
			return new DB2WorkflowProxyDAO(conn);
		}
		return null;
	}

	public IRuntimeDAO getOReportDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleOReportDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlOReportDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlOReportDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbOReportDAO(conn, applicationId);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2OReportDAO(conn, applicationId);
			}
		}
		return null;
	}

	public IRuntimeDAO getReportDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleStandarReportDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlStandarReportDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlStandarReportDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbStandarReportDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2StandarReportDAO(conn);
			}
		}
		return null;
	}

	public IRuntimeDAO getCrossReportDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleRuntimeDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlStandarReportDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlStandarReportDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbStandarReportDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2StandarReportDAO(conn);
			}
		}
		return null;
	}

	public IRuntimeDAO getNotificationDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbtype = DbTypeUtil.getDBType(applicationId);
			if (dbtype.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleNotificationDAO(conn);
			} else if (dbtype.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlNotificationDAO(conn);
			} else if (dbtype.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlNotificationDAO(conn);
			} else if (dbtype.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbNotificationDAO(conn);
			} else if (dbtype.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2NotificationDAO(conn);
			}
		}
		return null;
	}

	public IRuntimeDAO getCirculatorDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleCirculatorDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlCirculatorDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlCirculatorDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbCirculatorDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2CirculatorDAO(conn);
			}
		}
		return null;
	}
	
	public IRuntimeDAO getFlowReminderHistoryDAO(Connection conn, String applicationId)
			throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleFlowReminderHistoryDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlFlowReminderHistoryDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlFlowReminderHistoryDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbFlowReminderHistoryDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2FlowReminderHistoryDAO(conn);
			}
		}
		return null;
	}

	public IRuntimeDAO getDataMapTemplateDAO(Connection conn,
			String applicationId) throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleDataMapTemplateDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlDataMapTemplateDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlDataMapTemplateDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqlDataMapTemplateDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new Db2DataMapTemplateDAO(conn);
			}
		}
		return null;
	}

	public IRuntimeDAO getFlowMonitorDAO(Connection conn,
			String applicationId) throws Exception {
		if (applicationId != null) {
			String dbType = DbTypeUtil.getDBType(applicationId);
			if (dbType.equals(DbTypeUtil.DBTYPE_ORACLE)) {
				return new OracleAnalyzerDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
				return new MssqlAnalyzerDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_MYSQL)) {
				return new MysqlAnalyzerDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_HSQLDB)) {
				return new HsqldbAnalyzerDAO(conn);
			} else if (dbType.equals(DbTypeUtil.DBTYPE_DB2)) {
				return new DB2AnalyzerDAO(conn);
			}
		}
		return null;
	}

}
