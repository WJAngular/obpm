package cn.myapps.core.validate.repository.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryVO;

public interface ValidateRepositoryDAO extends IDesignTimeDAO<ValidateRepositoryVO> {

	/**
	 * 根据 application应用标识 ,获取validate集合.
	 * @param application
	 *            应用标识
	 * @return validate集合.
	 * @throws Exception
	 */
	public Collection<ValidateRepositoryVO> getValidateRepositoryByApplication(String application)
			throws Exception;
	
	/**
	 * 根据检验库名，判断名称是否唯一
	 * @param name
	 * @param application
	 * @return 存在返回true,否则返回false
	 * @throws Exception
	 */
	public boolean isValidateNameExist(String id, String name,String application)throws Exception;
}
