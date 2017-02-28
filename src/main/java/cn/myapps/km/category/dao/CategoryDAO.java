package cn.myapps.km.category.dao;

import java.util.Collection;

import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.category.ejb.Category;

public interface CategoryDAO extends NRuntimeDAO {
	
	public Collection<Category> querySubCategory(String parentId,String domainId) throws Exception;

}
