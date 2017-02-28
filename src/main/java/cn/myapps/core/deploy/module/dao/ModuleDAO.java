package cn.myapps.core.deploy.module.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.deploy.module.ejb.ModuleVO;

public interface ModuleDAO extends IDesignTimeDAO<ModuleVO> {
	public Collection<ModuleVO> getModuleByApplication(String application)
			throws Exception;

	public ModuleVO findByName(String name, String application) throws Exception;
}
