package cn.myapps.core.dynaform.form.dao;

import java.sql.Connection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.table.ddlutil.AbstractTableDefinition;
import cn.myapps.core.table.ddlutil.AbstractValidator;
import cn.myapps.core.table.ddlutil.mysql.MysqlTableDefinition;
import cn.myapps.core.table.ddlutil.mysql.MysqlValidator;
import cn.myapps.util.DbTypeUtil;

/**
 * 
 * @author Chris
 * 
 */
public class MysqlFormTableDAO extends AbstractFormTableDAO implements IRuntimeDAO, FormTableDAO {

	public MysqlFormTableDAO(Connection conn) {
		super(conn);
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MYSQL);
	}

	public AbstractValidator getValidator() {
		return new MysqlValidator(conn);
	}

	public AbstractTableDefinition getTableDefinition() {
		return new MysqlTableDefinition(conn);
	}
}
