package cn.myapps.km.baike.content.ejb;



import java.util.Collection;

import cn.myapps.km.base.ejb.NRunTimeProcess;


/**
 * @author Abel
  */
public interface ReferenceMaterialProcess  extends NRunTimeProcess<ReferenceMaterial>{

	/**
	 * 获取某版本内容所引用的参考资料
	 */
	public Collection<ReferenceMaterial> getReferenceMaterials(String contentId) throws Exception;
}