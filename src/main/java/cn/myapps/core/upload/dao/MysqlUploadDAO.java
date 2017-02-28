package cn.myapps.core.upload.dao;

import java.sql.Connection;

import cn.myapps.core.dynaform.document.dql.MysqlSQLFunction;
import cn.myapps.util.DbTypeUtil;

public class MysqlUploadDAO extends AbstractUploadDAO implements UploadDAO {

	public MysqlUploadDAO(Connection conn, String applicationId)
			throws Exception {
		super(conn, applicationId);
		dbType = "MY SQL: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MYSQL);
		sqlFuction = new MysqlSQLFunction();
	}

}
