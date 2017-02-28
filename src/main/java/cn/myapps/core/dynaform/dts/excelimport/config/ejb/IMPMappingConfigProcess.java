package cn.myapps.core.dynaform.dts.excelimport.config.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface IMPMappingConfigProcess extends IDesignTimeProcess<IMPMappingConfigVO> {
	public Collection<IMPMappingConfigVO> getAllMappingConfig(String application) throws Exception;

	public Object getMappingExcel(String id) throws Exception;

}
