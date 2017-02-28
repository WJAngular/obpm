package cn.myapps.core.dynaform.dts.excelimport.config.ejb;

import java.util.Collection;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.dynaform.dts.excelimport.config.dao.IMPMappingConfigDAO;

public class IMPMappingConfigProcessBean extends AbstractDesignTimeProcessBean<IMPMappingConfigVO> implements
		IMPMappingConfigProcess {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -792302886598953202L;

	protected IDesignTimeDAO<IMPMappingConfigVO> getDAO() throws Exception {
		return (IMPMappingConfigDAO)DAOFactory.getDefaultDAO(IMPMappingConfigVO.class.getName());
	}

	public Collection<IMPMappingConfigVO> getAllMappingConfig(String application) throws Exception {
		return ((IMPMappingConfigDAO) getDAO()).getAllMappingConfig(application);
	}

	public Object getMappingExcel(String id) throws Exception {
		return ((IMPMappingConfigDAO) getDAO()).getMappingExcel(id);
	}

}
