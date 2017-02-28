package cn.myapps.core.resource.ejb;

import java.util.Collection;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.dynaform.view.ejb.View;

public interface ResourceProcess extends IDesignTimeProcess<ResourceVO> {

	/**
	 * 获取当前菜单的下级菜单,并以树的形式显示
	 * 
	 * @return 菜单集合
	 * @throws Exception
	 */
	public abstract Collection<ResourceVO> getFamilyTree(String application)
			throws Exception;

	/**
	 * 根据菜单的集合生成菜单并以树(Tree)形式的集合.
	 * 
	 * @param colls
	 *            菜单的集合
	 * @param startNode
	 *            菜单的开始节点
	 * @param excludeNodeId
	 *            不包括的节点
	 * @param deep
	 *            深入级别
	 * @return 树(Tree)形式的菜单集合
	 * @throws Exception
	 */
	public Map<String, String> deepSearchMenuTree(Collection<ResourceVO> cols,
			ResourceVO startNode, String excludeNodeId, int deep)
			throws Exception;

	/**
	 * 获取为受保护的菜单集合
	 * 
	 * @param application
	 *            应用标识
	 * @return 菜单集合
	 * @throws Exception
	 */
	public Collection<ResourceVO> getProtectResources(String application)
			throws Exception;

	/**
	 * 获取应用的顶级菜单,并菜单为受保护的菜单集合
	 * 
	 * @param application
	 *            应用标识
	 * @return 菜单集合
	 * @throws Exception
	 */
	public Collection<ResourceVO> getTopProtectResources(String application)
			throws Exception;

	/**
	 * 根据菜单的集合生成菜单并以树(Tree)形式的集合.
	 * 
	 * @param colls
	 *            菜单的集合
	 * @param startNode
	 *            菜单的开始节点
	 * @param excludeNodeId
	 *            不包括的节点
	 * @param deep
	 *            深入级别
	 * @return 树(Tree)形式的菜单集合
	 * @throws Exception
	 */
	public Collection<ResourceVO> deepSearchResouece(
			Collection<ResourceVO> colls, ResourceVO startNode,
			String excludeNodeId, int deep) throws Exception;

	/**
	 * 获取应用的顶级菜单
	 * 
	 * @param application
	 *            应用标识
	 * @return 菜单集合
	 * @throws Exception
	 */
	public Collection<ResourceVO> getTopResources(String application)
			throws Exception;

	/**
	 * 根据顶级菜单的description得到顶级菜单
	 * 
	 * @param description
	 * @return 菜单对象
	 * @throws Exception
	 */
	public ResourceVO getTopResourceByName(String description,
			String application) throws Exception;

	/**
	 * 根据视图获取对应的菜单
	 * 
	 * @param viewId
	 *            标识
	 * @param application
	 *            应用标识
	 * @return 菜单对象
	 * @throws Exception
	 */
	public ResourceVO getResourceByViewId(String viewId, String application)
			throws Exception;

	/**
	 * 根据菜单描述查询菜单对象
	 * 
	 * @param description
	 *            菜单描述
	 * @param application
	 *            应用标识
	 * @return 菜单对象
	 * @throws Exception
	 */
	public ResourceVO getResourceByName(String description, String application)
			throws Exception;

	/**
	 * 复制菜单
	 * 
	 * @param applicationid
	 *            应用标识
	 * @param originid
	 *            原菜单标识
	 * @param destid
	 *            新菜单标识
	 * @throws Exception
	 */
	public void copyResource(String applicationid, String originid,
			String destid) throws Exception;

	/**
	 * 复制菜单
	 * 
	 * @param applicationid
	 *            应用标识
	 * @param originids
	 *            原菜单标识集
	 * @param destid
	 *            新菜单标识
	 * @throws Exception
	 */
	public void copyResources(String applicationid, String[] originids,
			String destid) throws Exception;

	/**
	 * 删除所有引用视图对象集合viewlist中的视图对象的菜单
	 * 
	 * @param viewlist
	 *            视图对象集合
	 * @throws Exception
	 */
	public abstract void doRemoveByViewList(Collection<View> viewlist,
			String application) throws Exception;

	/**
	 * 获得上级下孩子集合
	 * 
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	public Collection<ResourceVO> doGetDatasByParent(String parent)
			throws Exception;

	/**
	 * 创建菜单
	 * 
	 * @throws Exception
	 */
	public void doCreateMenu(ResourceVO resourceVO) throws Exception;

	public Collection<ResourceVO> doSimpleQuery(ParamsTable params,
			String application) throws Exception;

	public Collection<ResourceVO> doQueryBySQL(String sql) throws Exception;
}
