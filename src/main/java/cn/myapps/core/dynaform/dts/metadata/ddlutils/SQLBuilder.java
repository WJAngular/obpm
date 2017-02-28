package cn.myapps.core.dynaform.dts.metadata.ddlutils;


import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;
import cn.myapps.core.dynaform.dts.metadata.ejb.IForeignKey;
import cn.myapps.core.dynaform.dts.metadata.ejb.IIndex;
import cn.myapps.core.dynaform.dts.metadata.ejb.ITable;
/**
 * @author Happy
 *
 */
public abstract class SQLBuilder {
	
	public final static String SQL_DELIMITER = ";";

	protected Map<Object, Object> typeNames = new HashMap<Object, Object>();

	protected String schema;


	/**
	 * @param schema
	 *            the schema to set
	 * @uml.property name="schema"
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}


	protected String getTableFullName(ITable table) {
		if (schema != null && schema.trim().length() > 0) {
			String tableFullName = schema + "." + table.getName();
			return tableFullName;
		}
		return table.getName();
	}

	protected String getTableFullName(String tableName) {
		String rtn = tableName;
		if (schema != null && schema.trim().length() > 0) {
			String tableFullName = schema + "." + tableName;
			rtn = tableFullName;
		}
		return rtn.toUpperCase();
	}
	
	
	
	
	public String createForeignKey(ITable table,IForeignKey foreignKey) {
		StringBuffer _writer = new StringBuffer();
		_writer.append("ALTER TABLE ").append(this.getTableFullName(foreignKey.getFkTableName()));
		_writer.append(" ADD CONSTRAINT ").append(foreignKey.getFkName());
		_writer.append(" FOREIGN KEY (").append(foreignKey.getFkColumnName()).append(")");
		_writer.append(" REFERENCES ").append(this.getTableFullName(foreignKey.getPkTableName())).append(" (").append(foreignKey.getPkColumnName()).append(")");
		if(DatabaseMetaData.importedKeyCascade == foreignKey.getUpdateRule()){
			_writer.append(" ON UPDATE CASCADE ");
		}else if(DatabaseMetaData.importedKeyRestrict == foreignKey.getUpdateRule()){
			_writer.append(" ON UPDATE RESTRICT ");
		}else if(DatabaseMetaData.importedKeySetNull == foreignKey.getUpdateRule()){
			_writer.append(" ON UPDATE SET NULL ");
		}else if(DatabaseMetaData.importedKeyNoAction == foreignKey.getUpdateRule()){
			_writer.append(" ON UPDATE NO ACTION ");
		}
		
		if(DatabaseMetaData.importedKeyCascade == foreignKey.getDeleteRule()){
			_writer.append(" ON DELETE CASCADE ");
		}else if(DatabaseMetaData.importedKeyRestrict == foreignKey.getDeleteRule()){
			_writer.append(" ON DELETE RESTRICT ");
		}else if(DatabaseMetaData.importedKeySetNull == foreignKey.getDeleteRule()){
			_writer.append(" ON DELETE SET NULL ");
		}else if(DatabaseMetaData.importedKeyNoAction == foreignKey.getDeleteRule()){
			_writer.append(" ON DELETE NO ACTION ");
		}
		_writer.append(", ADD INDEX ").append(foreignKey.getFkName()).append(" (").append(foreignKey.getFkColumnName()).append(" ASC").append(")");
		_writer.append(SQL_DELIMITER);
		return _writer.toString();
	}
	
	/**
	 * 创建索引
	 * @param table
	 * @param index
	 * @return
	 */
	public String createIndex(IIndex index) {
		StringBuffer _writer = new StringBuffer();
		_writer.append("CREATE INDEX ");
		_writer.append(index.getName());
		_writer.append(" ON ");
		_writer.append(index.getTableName().toUpperCase());
		_writer.append(" (").append(index.getColumnName()).append(")");
		return _writer.toString();
	}
	
}
