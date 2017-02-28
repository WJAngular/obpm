package cn.myapps.core.dynaform.dts.metadata.ejb;

/**
 * 外键
 * @author Happy
 *
 */
public class IForeignKey {
	
	/**
	 * 主键表名称
	 */
	private String pkTableName;
	/**
	 * 主键列名称
	 */
	private String pkColumnName;
	/**
	 * 被导入的外键表名称
	 */
	private String fkTableName;
	/**
	 * 被导入的外键列名称
	 */
	private String fkColumnName;
	/**
	 * 外键的名称（可为 null）
	 */
	private String fkName;
	/**
	 * 主键的名称（可为 null）
	 */
	private String pkName;
	
	private int updateRule;
	
	private int deleteRule;
	
	
	
	public IForeignKey(String pkTableName, String pkColumnName,
			String fkTableName, String fkColumnName, String fkName,
			String pkName ,int updateRule ,int deleteRule) {
		super();
		this.pkTableName = pkTableName;
		this.pkColumnName = pkColumnName;
		this.fkTableName = fkTableName;
		this.fkColumnName = fkColumnName;
		this.fkName = fkName;
		this.pkName = pkName;
		this.updateRule = updateRule;
		this.deleteRule = deleteRule;
	}
	public String getPkTableName() {
		return pkTableName;
	}
	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}
	public String getPkColumnName() {
		return pkColumnName;
	}
	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}
	public String getFkTableName() {
		return fkTableName;
	}
	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}
	public String getFkColumnName() {
		return fkColumnName;
	}
	public void setFkColumnName(String fkColumnName) {
		this.fkColumnName = fkColumnName;
	}
	public String getFkName() {
		return fkName;
	}
	public void setFkName(String fkName) {
		this.fkName = fkName;
	}
	public String getPkName() {
		return pkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	public int getUpdateRule() {
		return updateRule;
	}
	public void setUpdateRule(int updateRule) {
		this.updateRule = updateRule;
	}
	public int getDeleteRule() {
		return deleteRule;
	}
	public void setDeleteRule(int deleteRule) {
		this.deleteRule = deleteRule;
	}
	
	
	

}
