package cn.myapps.km.baike.content.dao;


import java.util.Collection;

import cn.myapps.km.baike.content.ejb.ReferenceMaterial;
import cn.myapps.km.base.dao.NRuntimeDAO;


/**
 * 
 * @author Abel
 *词条内容DAO层接口
 */
public interface ReferenceMaterialDAO extends NRuntimeDAO{	
	
	/**
	 * 通过EntryId获取参考资料
	 * @param contentId
	 * @return
	 * @throws Exception
	 */
	public Collection<ReferenceMaterial> getReferenceMaterials(String entryContentId) throws Exception;
}
