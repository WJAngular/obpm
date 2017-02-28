package cn.myapps.core.table.ddlutil.oracle;

import java.sql.Connection;

import cn.myapps.core.table.ddlutil.AbstractValidator;
import cn.myapps.util.DbTypeUtil;

/**
 * 
 * @author Chris
 * 
 */
public class OracleValidator extends AbstractValidator {

	public OracleValidator(Connection conn) {
		super(conn, new OracleBuilder());
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_ORACLE);
		_builder.setSchema(schema);
	}

	protected String getCatalog() {
		return null;
	}

	protected String getSchemaPattern() {
		return schema;
	}

}
