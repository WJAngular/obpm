package cn.myapps.core.dynaform.dts.excelimport.config.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigVO;

public interface IMPMappingConfigDAO extends IDesignTimeDAO<IMPMappingConfigVO> {
	public Collection<IMPMappingConfigVO> getAllMappingConfig(String application) throws Exception;

	public Object getMappingExcel(String id) throws Exception;
}
