package cn.myapps.core.dynaform.dts.exp.columnmapping.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.dts.exp.columnmapping.ejb.ColumnMapping;

public interface ColumnMappingDAO extends IDesignTimeDAO<ColumnMapping>{

	public abstract Collection<ColumnMapping> getColMapBytoName(String toName, String application) throws Exception;
}
