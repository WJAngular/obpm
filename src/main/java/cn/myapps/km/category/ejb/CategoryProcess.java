package cn.myapps.km.category.ejb;

import java.util.Collection;

import cn.myapps.km.base.ejb.NRunTimeProcess;

public interface CategoryProcess extends NRunTimeProcess<Category> {
	
	public Collection<Category> doQuerySubCategory(String parentId,String domainId) throws Exception;

}
