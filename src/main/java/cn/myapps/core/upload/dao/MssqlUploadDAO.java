package cn.myapps.core.upload.dao;

import java.sql.Connection;

import cn.myapps.core.dynaform.document.dql.MssqlSQLFunction;
import cn.myapps.util.DbTypeUtil;

public class MssqlUploadDAO  extends AbstractUploadDAO implements UploadDAO{

	public MssqlUploadDAO(Connection conn, String applicationId)
			throws Exception {
		super(conn, applicationId);
		dbType = "MS SQL Server: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MSSQL);
		sqlFuction = new MssqlSQLFunction();
	}

}
