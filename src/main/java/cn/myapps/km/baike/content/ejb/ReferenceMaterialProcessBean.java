package cn.myapps.km.baike.content.ejb;



import java.util.Collection;

import cn.myapps.km.baike.content.dao.ReferenceMaterialDAO;
import cn.myapps.km.base.dao.BDaoManager;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractRunTimeProcessBean;



/**
 * @author abel
 * 参考资料ejb层的实现类
 */
public class ReferenceMaterialProcessBean extends AbstractRunTimeProcessBean<ReferenceMaterial> implements ReferenceMaterialProcess{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1427609446379858841L;

	/**
	 * 获取某版本内容所引用的参考资料
	 */
	public Collection<ReferenceMaterial> getReferenceMaterials(String entryContentId) throws Exception{
		return (Collection<ReferenceMaterial>) ((ReferenceMaterialDAO)getDAO()).getReferenceMaterials(entryContentId);
	}

	@Override
	protected NRuntimeDAO getDAO() throws Exception {
		return BDaoManager.getReferenceMaterialDAO(getConnection());
	}
}
