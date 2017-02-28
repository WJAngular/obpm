package cn.myapps.core.dynaform.dts.exp.mappingconfig.action;

import java.util.Collection;

import cn.myapps.base.action.BaseHelper;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfigProcess;
import cn.myapps.util.ProcessFactory;

public class MappingConfigHelper extends BaseHelper<MappingConfig> {
	
	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public MappingConfigHelper() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(MappingConfigProcess.class));
	}

	public Collection<?> get_allMappingConifgs(String application)
			throws Exception {
		MappingConfigProcess dp = (MappingConfigProcess) ProcessFactory
				.createProcess((MappingConfigProcess.class));
		Collection<?> dc = dp.doSimpleQuery(null, application);
		return dc;
	}
}
