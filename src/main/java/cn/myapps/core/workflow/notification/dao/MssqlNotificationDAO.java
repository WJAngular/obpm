package cn.myapps.core.workflow.notification.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;

public class MssqlNotificationDAO extends AbstractNotificationDAO implements
		NotificationDAO {

	public MssqlNotificationDAO(Connection conn) throws Exception {
		super(conn);
		this.DBType = DbTypeUtil.DBTYPE_MSSQL;
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MSSQL);
	}
}
