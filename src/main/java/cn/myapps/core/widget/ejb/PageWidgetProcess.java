package cn.myapps.core.widget.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface PageWidgetProcess extends IDesignTimeProcess<PageWidget> {
	
	/**
	 * 根据传入的企业域id获取企业域下所有激活软件的已发布Widget集合
	 * @param domainId
	 * @return
	 * @throws Exception
	 */
	public Collection<PageWidget> doQueryPublishedWidgetsByDomainId(String domainId) throws Exception;
	
	

}
