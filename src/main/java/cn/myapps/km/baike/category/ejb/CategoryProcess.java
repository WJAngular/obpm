package cn.myapps.km.baike.category.ejb;

import java.util.Collection;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/**
 * 
 * @author jodg
 *
 */
public interface CategoryProcess extends NRunTimeProcess<Category> {
	
	public Collection<Category> doQuery() throws Exception;
	
	public Category doFindByEntryId(String entryId) throws Exception;
	
	public DataPackage<Category> doQueryByEntryId(String entryId, int page, int lines) throws Exception;
	
	/**
	 * 通过entryId查询分类
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public Collection<Category> doQueryByEntryId(String entryId) throws Exception;
	
	/**
	 * 通过entryId查询分类
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public Collection<Category> doQuerySubCategory(String parentId,String domainId) throws Exception;

}
