package cn.myapps.core.table.ddlutil.hsql;

import java.sql.Connection;

import cn.myapps.core.table.ddlutil.AbstractValidator;
import cn.myapps.core.table.ddlutil.mysql.MysqlBuilder;
import cn.myapps.util.DbTypeUtil;
/**
 * 
 * @author Chris
 *
 */
public class HsqldbValidator extends AbstractValidator{
	
	public HsqldbValidator(Connection conn) {
		super(conn, new MysqlBuilder());
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_HSQLDB);
		_builder.setSchema(schema);
	}

	protected String getCatalog() {
		return null;
	}

	protected String getSchemaPattern() {
		return schema;
	}

}
