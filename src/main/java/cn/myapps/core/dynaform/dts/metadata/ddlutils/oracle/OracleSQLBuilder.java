package cn.myapps.core.dynaform.dts.metadata.ddlutils.oracle;

import java.sql.Types;

import cn.myapps.core.dynaform.dts.metadata.ddlutils.SQLBuilder;
import cn.myapps.core.dynaform.dts.metadata.ejb.ITable;

public class OracleSQLBuilder extends SQLBuilder {
	public OracleSQLBuilder(String schema) {
		registerColumnType(Types.VARCHAR, "VARCHAR2");
		registerColumnType(Types.LONGVARCHAR, "CLOB");
		registerColumnType(Types.NUMERIC, "NUMBER(22,5)");
		registerColumnType(Types.INTEGER, "NUMBER(10,0)");
		registerColumnType(Types.BIT, "NUMBER(1,0)");
		registerColumnType(Types.DATE, "DATE");
		registerColumnType(Types.TIMESTAMP, "TIMESTAMP(6)");
		registerColumnType(Types.CLOB, "CLOB");
		registerColumnType(Types.BLOB, "BLOB");
		registerColumnType(Types.DECIMAL, "NUMBER(10,0)");
		registerColumnType(Types.DOUBLE, "FLOAT");
		registerColumnType(Types.FLOAT, "FLOAT");
		registerColumnType(Types.BIGINT, "NUMBER(19,0)");
		registerColumnType(Types.BINARY, "RAW(2000)");
		registerColumnType(Types.VARBINARY, "RAW(2000)");
		registerColumnType(Types.LONGVARBINARY, "BLOB");
		registerColumnType(Types.BOOLEAN, "NUMBER(1,0)");
		registerColumnType(Types.SMALLINT, "NUMBER(1,0)");
		registerColumnType(Types.TINYINT, "NUMBER(1,0)");
		
		this.setSchema(schema);
		
//		if(DBUtils.DBTYPE_MSSQL.equals(resDbType)){
//			registerColumnType(Types.DOUBLE, "FLOAT");
//		}
	}
	
	private void registerColumnType(int code, String name) {
		typeNames.put(Integer.valueOf(code), name);
	}
	

	public String getTableFullName(ITable table) {
		if (schema != null && schema.trim().length() > 0) {
			String tableFullName = schema + "." + table.getName();
			return tableFullName;
		}
		return table.getName();
	}
	

}
