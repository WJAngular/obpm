package cn.myapps.core.dynaform.form.dao;

import java.sql.Connection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.table.ddlutil.AbstractTableDefinition;
import cn.myapps.core.table.ddlutil.AbstractValidator;
import cn.myapps.core.table.ddlutil.oracle.OracleTableDefinition;
import cn.myapps.core.table.ddlutil.oracle.OracleValidator;
import cn.myapps.util.DbTypeUtil;

public class OracleFormTableDAO extends AbstractFormTableDAO implements IRuntimeDAO, FormTableDAO {

	public OracleFormTableDAO(Connection conn) {
		super(conn);
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_ORACLE);
	}

	public AbstractValidator getValidator() {
		return new OracleValidator(conn);
	}

	public AbstractTableDefinition getTableDefinition() {
		return new OracleTableDefinition(conn);
	}
}
