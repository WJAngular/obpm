package cn.myapps.core.department.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.department.ejb.DepartmentVO;

public interface DepartmentDAO extends IDesignTimeDAO<DepartmentVO> {

	public abstract Collection<DepartmentVO> getDatasByParent(String parent) throws Exception;

//	public abstract String getIdByName(String deptname, String application,
//			String domain) throws Exception;

	public abstract Collection<DepartmentVO> getAllDepartment(String application,
			String domain) throws Exception;

	public abstract Collection<DepartmentVO> getDepartmentByLevel(int level,
			String application, String domain) throws Exception;

	public abstract Collection<DepartmentVO> getDepartmentByName(String byName, String domain)
			throws Exception;

	public abstract Collection<DepartmentVO> getDepartmentByCode(String byCode, String domain)
			throws Exception;

	public abstract DepartmentVO getRootDepartmentByApplication(
			String application, String domain) throws Exception;

	public long getChildrenCount(String parent) throws Exception;

	public abstract Collection<DepartmentVO> queryByDomain(String domain, int page,
			int pagelines) throws Exception;

	public abstract DepartmentVO findByName(String name, String domain)
			throws Exception;
	
	/**
	 * 根据索引编号查找部门
	 * @param indexCode
	 * @return
	 * @throws Exception
	 */
	public DepartmentVO findByIndexCode(String indexCode) throws Exception;
	
	/**
	 * 根据索引编号查找直属上级部门
	 * @param indexCode
	 * @return
	 * @throws Exception
	 */
	public DepartmentVO findLineSuperiorByIndexCode(String indexCode) throws Exception;
	
	/**
	 * 根据索引编号查找直属下级部门的集合
	 * @param indexCode
	 * @return
	 * @throws Exception
	 */
	public Collection<DepartmentVO> queryLineSubordinatesByIndexCode(String indexCode) throws Exception;
	
	/**
	 * 根据索引编号查找所有上级部门的集合
	 * @param indexCode
	 * @return
	 * @throws Exception
	 */
	public Collection<DepartmentVO> queryAllSuperiorsByIndexCode(String indexCode) throws Exception;
	
	/**
	 * 根据索引编号查找所有下级部门的集合
	 * @param indexCode
	 * @return
	 * @throws Exception
	 */
	public Collection<DepartmentVO> queryAllSubordinatesByIndexCode(String indexCode) throws Exception;
	
	/**
	 * 根据部门名字和层级获得部门对象
	 * 
	 * @param name
	 *            部门名
	 @param level
	 *            部门层级
	 * @param domainId
	 *            域标识
	 * @return 部门对象
	 * @throws Exception
	 */
	public DepartmentVO getDepartmentByNameAndLevel(String name,int level, String domainId) throws Exception;
}
