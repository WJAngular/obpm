package cn.myapps.core.macro.repository.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface RepositoryProcess extends IDesignTimeProcess<RepositoryVO> {
	/**
	 * 根据函数库名,返回库RepositoryVO对象
	 * 
	 * @param name
	 *            libname 函数库名
	 * @param application
	 *            应用标识
	 * @return RepositoryVO对象
	 * @throws Exception
	 */

	public RepositoryVO getRepositoryByName(String name, String application) throws Exception;

	/**
	 * 根据库名，查询在同一应用下是否有相同名称的库
	 * @param id
	 * @param name
	 * @param application
	 * @return 存在返回true,否则返回false
	 * @throws Exception
	 */
	public boolean isMacroNameExist(String id,String name,String application) throws Exception;
	
	/**
	 * 根据传入的软件id获取函数库的集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * 		函数库的集合
	 * @throws Exception
	 */
	public Collection<RepositoryVO> doQueryByApplication(String applicationId) throws Exception;
}
