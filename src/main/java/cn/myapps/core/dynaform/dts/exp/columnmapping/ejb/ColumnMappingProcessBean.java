package cn.myapps.core.dynaform.dts.exp.columnmapping.ejb;

import java.util.Collection;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.dynaform.dts.exp.columnmapping.dao.ColumnMappingDAO;

public class ColumnMappingProcessBean extends AbstractDesignTimeProcessBean<ColumnMapping> implements ColumnMappingProcess{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -293753905158314015L;

	protected IDesignTimeDAO<ColumnMapping> getDAO() throws Exception {
		return (ColumnMappingDAO)DAOFactory.getDefaultDAO(ColumnMapping.class.getName());
	}

	public Collection<ColumnMapping> getColMapBytoName(String toName, String application) throws Exception {
	      
		return ((ColumnMappingDAO)getDAO()).getColMapBytoName(toName, application);
	}

}
