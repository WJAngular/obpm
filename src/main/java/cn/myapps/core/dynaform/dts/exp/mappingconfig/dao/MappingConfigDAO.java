package cn.myapps.core.dynaform.dts.exp.mappingconfig.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;

public interface  MappingConfigDAO extends IDesignTimeDAO<MappingConfig>{

	public abstract Collection<MappingConfig> getMCByTableName(String tableName, String application) throws Exception;
}
