package cn.myapps.km.baike.category.dao;

import java.util.Collection;

import cn.myapps.km.baike.category.ejb.Category;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;

public interface CategoryDAO extends NRuntimeDAO {
	
	public Collection<Category> query() throws Exception;
	
	public Collection<Category> querySubCategory(String parentId,String domainId) throws Exception;
	
	public Category findByEntryId(String entryId) throws Exception;
	
	/**
	 * 通过entryId查询分类
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Category> queryCategoryByEntryId(String entryId, int page, int lines) throws Exception;
	
	/**
	 * 通过entryId查询分类
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public Collection<Category> queryCategoryByEntryId(String entryId) throws Exception;
	
	

}
