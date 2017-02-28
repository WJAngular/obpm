package cn.myapps.core.style.repository.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

/**
 * 
 * @author Marky
 * 
 */
public interface StyleRepositoryProcess extends IDesignTimeProcess<StyleRepositoryVO> {
	/**
	 * 根据样式库名以及应用标识查询,返回样式库对象StyleRepositoryVO
	 * 
	 * @param name
	 *            样式库名
	 * @param application
	 *            应用标识
	 * @return 样式库对象StyleRepositoryVO
	 * @throws Exception
	 */

	public StyleRepositoryVO getRepositoryByName(String name, String application)
			throws Exception;

	/**
	 * 根据模块(module)主键(id)以及应用标识来查询,获取相应样式库(StyleRepository)集合.
	 * 
	 * @param moduleid
	 *            模块主键
	 * @param application
	 *            应用标识
	 * @return 样式库(StyleRepository)集合
	 * @throws Exception
	 */
	public Collection<StyleRepositoryVO> getStyleRepositoryByModule(String moduleid,
			String application) throws Exception;

	/**
	 * 根据应用标识查询,返回相应样式库(StyleRepository)集合.
	 * 
	 * @param application
	 *            应用标识
	 * @return 样式库(StyleRepository)集合
	 * @throws Exception
	 */
	public Collection<StyleRepositoryVO> getStyleRepositoryByApplication(String applicationid)
			throws Exception;
	
	/**
	 * 根据样式名，判断名称是否唯一
	 * @param id
	 * @param name
	 * @param application
	 * @return 存在返回true,否则返回false
	 * @throws Exception
	 */
	public boolean isStyleNameExist(String id, String name,String application)throws Exception;
}
