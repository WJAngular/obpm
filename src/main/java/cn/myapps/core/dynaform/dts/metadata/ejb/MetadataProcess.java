package cn.myapps.core.dynaform.dts.metadata.ejb;

import java.util.Collection;

import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;

public interface MetadataProcess {
	
	/**
	 * 索引优化
	 * @param dataSource
	 * @throws Exception
	 */
	public void doIndexOptimization(DataSource dataSource) throws Exception;
	
	/**
	 * 获取数据源下的所有表对象的集合
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	public Collection<ITable> getAllTables(DataSource dataSource) throws Exception;

}
