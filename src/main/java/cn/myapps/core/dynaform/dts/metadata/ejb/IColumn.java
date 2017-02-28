package cn.myapps.core.dynaform.dts.metadata.ejb;

import java.sql.Types;
/**
 * 数据库表列对象
 * @author Happy
 *
 */
public class IColumn implements Cloneable {

	private String name;

	private int dataType;

	private boolean primaryKey;

	private boolean nullable ;

	private int columnSize;
	
	

	public IColumn(String name, int dataType, boolean primaryKey,
			boolean nullable, int columnSize) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.primaryKey = primaryKey;
		this.nullable = nullable;
		this.columnSize = columnSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public boolean getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	

	public boolean getNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object obj) {
		if(obj == null)return false;
		if(!(obj instanceof IColumn))return false;
		IColumn anColumn = (IColumn) obj;

		if (this.getName().equalsIgnoreCase(anColumn.getName()) && this.getDataType() == anColumn.getDataType()) {
			return true;
		} else {
			return super.equals(obj);
		}
	}
	
	public int hashCode(){
		return super.hashCode();
	}

	/**
	 * 比较新旧Column是否兼容
	 * 
	 * @param column
	 *            要比较的列
	 * @return true or false
	 */
	public boolean isCompatible(IColumn anColumn) {
		boolean rtn = false;

		int anotherDataType = anColumn.getDataType();

		switch (dataType) {
		case Types.VARCHAR:
			switch (anotherDataType) {
			case Types.CLOB:
				rtn = true;
				break;
			default:
				rtn = false;
			}
			break;

		case Types.NUMERIC:
			switch (anotherDataType) {
			case Types.CLOB:
				rtn = true;
				break;
			default:
				rtn = false;
			}
			break;

		case Types.DATE:
			switch (anotherDataType) {
			case Types.CLOB:
				rtn = true;
				break;
			default:
				rtn = false;
			}
			break;

		case Types.CLOB:
			switch (anotherDataType) {
			case Types.CLOB:
				rtn = true;
				break;
			default:
				rtn = false;
			}
			break;

		default:
			break;
		}
		return rtn;
	}
}
