package cn.myapps.core.upload.dao;

import java.sql.Connection;

import cn.myapps.core.dynaform.document.dql.HsqldbSQLFunction;
import cn.myapps.util.DbTypeUtil;

public class HsqldbUploadDAO extends AbstractUploadDAO implements UploadDAO {

	public HsqldbUploadDAO(Connection conn, String applicationId)
			throws Exception {
		super(conn, applicationId);
		dbType = "HypersonicSQL: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_HSQLDB);
		sqlFuction = new HsqldbSQLFunction();
	}

}
