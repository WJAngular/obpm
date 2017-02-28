package cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface MappingConfigProcess extends IDesignTimeProcess<MappingConfig>{
	
public abstract Collection<MappingConfig> getMCByTableName(String tableName, String application) throws Exception;

}
