package cn.myapps.core.table.ddlutil.mssql;

import java.sql.Connection;

import cn.myapps.core.table.ddlutil.AbstractValidator;
import cn.myapps.util.DbTypeUtil;

/**
 * 
 * @author Chris
 * 
 */
public class MssqlValidator extends AbstractValidator {

	public MssqlValidator(Connection conn) {
		super(conn, new MssqlBuilder());
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MSSQL);
		_builder.setSchema(schema);

	}

	protected String getCatalog() {
		return null;
	}

	protected String getSchemaPattern() {
		return "DBO";
	}

}
