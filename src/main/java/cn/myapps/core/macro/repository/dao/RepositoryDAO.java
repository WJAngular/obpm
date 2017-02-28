package cn.myapps.core.macro.repository.dao;

import java.util.Collection;

import cn.myapps.core.macro.repository.ejb.RepositoryVO;

public interface RepositoryDAO {

	public RepositoryVO getRepositoryByName(String name, String application) throws Exception;
	
	/**
	 * 根据名称，判断在该应用中是否已经存在相同名称的库
	 * @param id 
	 * @param name
	 * @param application
	 * @return 存在返回true,否则返回false
	 * @throws Exception
	 */
	public boolean isMacroNameExist(String id,String name,String application)throws Exception;
	
	/**
	 * 根据传入的软件id获取函数库的集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * 		函数库的集合
	 * @throws Exception
	 */
	public Collection<RepositoryVO> queryByApplication(String applicationId) throws Exception;
	
}
