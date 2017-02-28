package cn.myapps.core.dynaform.form.dao;

import java.sql.Connection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.table.ddlutil.AbstractTableDefinition;
import cn.myapps.core.table.ddlutil.AbstractValidator;
import cn.myapps.core.table.ddlutil.mssql.MssqlTableDefinition;
import cn.myapps.core.table.ddlutil.mssql.MssqlValidator;
import cn.myapps.util.DbTypeUtil;

/**
 * 
 * @author Chris
 * 
 */
public class MssqlFormTableDAO extends AbstractFormTableDAO implements IRuntimeDAO, FormTableDAO {

	public MssqlFormTableDAO(Connection conn) {
		super(conn);
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MSSQL);
	}

	public AbstractValidator getValidator() {
		return new MssqlValidator(conn);
	}

	public AbstractTableDefinition getTableDefinition() {
		return new MssqlTableDefinition(conn);
	}
}
