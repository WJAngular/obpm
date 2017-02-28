package cn.myapps.core.dynaform.dts.metadata.ejb;

public class IIndex {
	
	/**
	 * 索引的名称（TYPE 为 tableIndexStatistic 时索引名称为 null ）
	 */
	private String name;
	/**
	 * 索引表名称
	 */
	private String tableName;
	/**
	 * 索引列名称
	 */
	private String columnName;
	/**
	 * 索引值是否可以不唯一。TYPE 为 tableIndexStatistic 时索引值为 false 
	 */
	private boolean unique;
	/**
	 * 索引类型： 
		tableIndexStatistic - 此标识与表的索引描述一起返回的表统计信息 
		tableIndexClustered - 此为集群索引 
		tableIndexHashed - 此为散列索引 
		tableIndexOther - 此为某种其他样式的索引
	 */
	private int type; 
	/**
	 * 列排序序列，"A" => 升序，"D" => 降序，如果排序序列不受支持，可能为 null；TYPE 为 tableIndexStatistic 时排序序列为 null 
	 */
	private String sort;
	
	

	public IIndex() {
	}
	
	
	public IIndex(String name, String tableName, String columnName,
			boolean unique, int type, String sort) {
		super();
		this.name = name;
		this.tableName = tableName;
		this.columnName = columnName;
		this.unique = unique;
		this.type = type;
		this.sort = sort;
	}





	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getTableName() {
		return tableName;
	}



	public void setTableName(String tableName) {
		this.tableName = tableName;
	}



	public String getColumnName() {
		return columnName;
	}



	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}



	public boolean isUnique() {
		return unique;
	}



	public void setUnique(boolean unique) {
		this.unique = unique;
	}



	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}



	public String getSort() {
		return sort;
	}



	public void setSort(String sort) {
		this.sort = sort;
	}
	
	

}
