package cn.myapps.core.dynaform.dts.exp.columnmapping.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface ColumnMappingProcess extends IDesignTimeProcess<ColumnMapping>{
	
	public abstract Collection<ColumnMapping> getColMapBytoName(String toName, String application) throws Exception;

}
