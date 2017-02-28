package cn.myapps.core.dynaform.dts.excelimport.config.action;

import java.util.Collection;

import cn.myapps.base.action.BaseHelper;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigProcess;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigVO;
import cn.myapps.util.ProcessFactory;

public class ImpHelper extends BaseHelper<IMPMappingConfigVO> {
	
	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public ImpHelper() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(IMPMappingConfigProcess.class));
	}

	public Collection<IMPMappingConfigVO> get_allMappingConfig(String application) throws Exception {

		IMPMappingConfigProcess proxy = (IMPMappingConfigProcess) ProcessFactory
				.createProcess((IMPMappingConfigProcess.class));
		return proxy.getAllMappingConfig(application);
	}
	
	public static Object get_MappingExcel(String id) throws Exception {
		IMPMappingConfigProcess proxy = (IMPMappingConfigProcess) ProcessFactory
				.createProcess(IMPMappingConfigProcess.class);
		return proxy.getMappingExcel(id);
	}


}
