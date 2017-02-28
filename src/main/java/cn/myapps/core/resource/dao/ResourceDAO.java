package cn.myapps.core.resource.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.resource.ejb.ResourceVO;

public interface ResourceDAO extends IDesignTimeDAO<ResourceVO> {

	/**
	 * Get the family tree.
	 * 
	 * @param parent
	 *            The parent resouce.
	 * @return The family tree.
	 * @throws Exception
	 */
	public abstract Collection<ResourceVO> getFamilyTree(String parent, String application)
			throws Exception;

	public Collection<ResourceVO> getProtectResources(String application) throws Exception;

	public Collection<ResourceVO> getTopProtectResources(String application)
			throws Exception;

	public Collection<ResourceVO> queryTopResources(String application) throws Exception;

	public ResourceVO getResourceByViewId(String viewId, String application)
			throws Exception;

	public Collection<ResourceVO> getDatasByParent(String parent) throws Exception;
	/**
	 * 删除所有引用视图对象集合vos中的视图对象的菜单
	 * 
	 * @param vos 视图对象集合
	 * @throws Exception
	 */
	public void removeByViewList(Collection<View> vos, String application) throws Exception;
}
