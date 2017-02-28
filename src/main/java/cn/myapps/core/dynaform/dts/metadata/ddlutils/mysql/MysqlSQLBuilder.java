package cn.myapps.core.dynaform.dts.metadata.ddlutils.mysql;

import java.sql.Types;

import cn.myapps.core.dynaform.dts.metadata.ddlutils.SQLBuilder;

public class MysqlSQLBuilder extends SQLBuilder {
	public MysqlSQLBuilder(String schema) {
		registerColumnType(Types.VARCHAR, "VARCHAR");
		registerColumnType(Types.LONGVARCHAR, "TEXT");
		registerColumnType(Types.NUMERIC, "DECIMAL(22,5)");
		registerColumnType(Types.DECIMAL, "DECIMAL(22,5)");
		registerColumnType(Types.INTEGER, "INT");
		registerColumnType(Types.BIT, "BIT(1)");
		registerColumnType(Types.DATE, "DATE");
		registerColumnType(Types.TINYINT, "TINYINT");
		registerColumnType(Types.BLOB, "MEDIUMBLOB");
		registerColumnType(Types.TIMESTAMP, "DATETIME");
		registerColumnType(Types.CLOB, "MEDIUMTEXT");
		registerColumnType(Types.DOUBLE, "DOUBLE");
		registerColumnType(Types.FLOAT, "FLOAT");
		registerColumnType(Types.BIGINT, "BIGINT");
		registerColumnType(Types.BINARY, "BINARY");
		registerColumnType(Types.LONGVARBINARY, "MEDIUMBLOB");
		registerColumnType(Types.VARBINARY, "MEDIUMBLOB");
		registerColumnType(Types.SMALLINT, "BIT(1)");
		
		this.setSchema(schema);
//		if(DBUtils.DBTYPE_MYSQL.equals(resDbType)){
//			registerColumnType(Types.LONGVARCHAR, "MEDIUMTEXT");
//		}
		
	}

	private void registerColumnType(int code, String name) {
		typeNames.put(Integer.valueOf(code), name);
	}

}
