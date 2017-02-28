package cn.myapps.core.upload.dao;

import java.sql.Connection;

import cn.myapps.core.dynaform.document.dql.OracleSQLFunction;
import cn.myapps.util.DbTypeUtil;

public class OracleUploadDAO  extends AbstractUploadDAO implements UploadDAO{

	public OracleUploadDAO(Connection conn, String applicationId)
			throws Exception {
		super(conn, applicationId);
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_ORACLE);
		sqlFuction = new OracleSQLFunction();
	}

}
