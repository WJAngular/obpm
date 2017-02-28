package cn.myapps.core.dynaform.dts.metadata.ddlutils.mssql;

import java.sql.Types;

import cn.myapps.core.dynaform.dts.metadata.ddlutils.SQLBuilder;


public class MsSQLBuilder extends SQLBuilder {
	
	public MsSQLBuilder(String schema) {
		registerColumnType(Types.VARCHAR, "NVARCHAR");
		registerColumnType(Types.LONGVARCHAR, "NTEXT");
		registerColumnType(Types.NUMERIC, "NUMERIC(22,5)");
		registerColumnType(Types.INTEGER, "NUMERIC(10,0)");
		registerColumnType(Types.BIT, "BIT");
		registerColumnType(Types.DATE, "SMALLDATETIME");
		registerColumnType(Types.TIMESTAMP, "DATETIME");
		registerColumnType(Types.CLOB, "NTEXT");
		registerColumnType(Types.BLOB, "VARBINARY");
		registerColumnType(Types.DECIMAL, "NUMERIC(10,0)");
		registerColumnType(Types.DOUBLE, "FLOAT");
		registerColumnType(Types.FLOAT, "FLOAT");
		registerColumnType(Types.BIGINT, "BIGINT");
		registerColumnType(Types.BINARY, "BINARY");
		registerColumnType(Types.VARBINARY, "VARBINARY");
		registerColumnType(Types.LONGVARBINARY, "VARBINARY");
		registerColumnType(Types.BOOLEAN, "NUMERIC(1,0)");
		registerColumnType(Types.TINYINT, "TINYINT");
		registerColumnType(Types.SMALLINT, "BIT");
		
		this.setSchema(schema);
		
	}
	
	private void registerColumnType(int code, String name) {
		typeNames.put(Integer.valueOf(code), name);
	}

	
	

}
