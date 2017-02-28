package cn.myapps.core.dynaform.dts.metadata.ddlutils.db2;

import java.sql.Types;
import cn.myapps.core.dynaform.dts.metadata.ddlutils.SQLBuilder;
import cn.myapps.core.dynaform.dts.metadata.ejb.IForeignKey;
import cn.myapps.core.dynaform.dts.metadata.ejb.ITable;

public class Db2SQLBuilder extends SQLBuilder {
	
	

	public Db2SQLBuilder(String schema) {
		registerColumnType(Types.VARCHAR, "VARCHAR");
		registerColumnType(Types.LONGVARCHAR, "CLOB");
		registerColumnType(Types.NUMERIC, "DECIMAL(22,5)");
		registerColumnType(Types.INTEGER, "INTEGER");
		registerColumnType(Types.BIT, "SMALLINT");
		registerColumnType(Types.SMALLINT, "SMALLINT");
		registerColumnType(Types.DATE, "DATE");
		registerColumnType(Types.TIMESTAMP, "TIMESTAMP");
		registerColumnType(Types.CLOB, "CLOB");
		registerColumnType(Types.BLOB, "BLOB");
		registerColumnType(Types.DECIMAL, "DECIMAL(10,0)");
		registerColumnType(Types.DOUBLE, "DOUBLE");
		registerColumnType(Types.FLOAT, "FLOAT");
		registerColumnType(Types.BIGINT, "BIGINT");
		registerColumnType(Types.BINARY, "BLOB");
		registerColumnType(Types.VARBINARY, "BLOB");
		registerColumnType(Types.LONGVARBINARY, "BLOB");
		registerColumnType(Types.BOOLEAN, "INTEGER");
		registerColumnType(Types.SMALLINT, "SMALLINT");
		registerColumnType(Types.TINYINT, "SMALLINT");
		
		this.setSchema(schema);
	}
	
	private void registerColumnType(int code, String name) {
		typeNames.put(Integer.valueOf(code), name);
	}
	
	
	public String createForeignKey(ITable table,IForeignKey foreignKey) {
		StringBuffer _writer = new StringBuffer();
		return _writer.toString();
	}
}
