package cn.myapps.core.dynaform.form.dao;

import java.sql.Connection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.table.ddlutil.AbstractTableDefinition;
import cn.myapps.core.table.ddlutil.AbstractValidator;
import cn.myapps.core.table.ddlutil.db2.DB2TableDefinition;
import cn.myapps.core.table.ddlutil.db2.DB2Validator;
import cn.myapps.util.DbTypeUtil;

public class DB2FormTableDAO extends AbstractFormTableDAO implements IRuntimeDAO, FormTableDAO {

	public DB2FormTableDAO(Connection conn) {
		super(conn);
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_DB2);
	}

	public AbstractValidator getValidator() {
		return new DB2Validator(conn);
	}

	public AbstractTableDefinition getTableDefinition() {
		return new DB2TableDefinition(conn);
	}
}
