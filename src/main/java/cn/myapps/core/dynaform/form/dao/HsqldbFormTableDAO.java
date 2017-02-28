package cn.myapps.core.dynaform.form.dao;

import java.sql.Connection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.table.ddlutil.AbstractTableDefinition;
import cn.myapps.core.table.ddlutil.AbstractValidator;
import cn.myapps.core.table.ddlutil.hsql.HsqldbTableDefinition;
import cn.myapps.core.table.ddlutil.hsql.HsqldbValidator;
import cn.myapps.util.DbTypeUtil;

/**
 * 
 * @author Chris
 * 
 */
public class HsqldbFormTableDAO extends AbstractFormTableDAO implements IRuntimeDAO, FormTableDAO {

	public HsqldbFormTableDAO(Connection conn) {
		super(conn);
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_HSQLDB);
	}

	public AbstractValidator getValidator() {
		return new HsqldbValidator(conn);
	}

	public AbstractTableDefinition getTableDefinition() {
		return new HsqldbTableDefinition(conn);
	}
}
