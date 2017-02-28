package cn.myapps.core.deploy.application.dao;

import java.util.Collection;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.application.ejb.DomainApplicationSet;

public interface ApplicationDAO extends IDesignTimeDAO<ApplicationVO> {

	public Map<String, ApplicationVO> getAppDomain_Cache() throws Exception;

	public Collection<ApplicationVO> getAllApplication() throws Exception;

	public boolean isEmpty() throws Exception;

	public Collection<ApplicationVO> queryAppsByDomain(String domainId, int page, int line) throws Exception;

	public Collection<ApplicationVO> queryApplications(String suserid, int page, int line) throws Exception;

	public ApplicationVO findBySIPAppKey(String appKey) throws Exception;

	public ApplicationVO findByName(String name) throws Exception;
	
	/**
	 * 查询企业域下的软件列表
	 * @param domainId
	 * 		企业域id
	 * @param page
	 * 		当前页数
	 * @param line
	 * 		每页记录条数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<ApplicationVO> queryByDomain(String domainId, int page, int line) throws Exception;
	
	/**
	 * 查询没有绑定到指定企业域下的软件列表
	 * @param params
	 * 		参数表
	 * @param domainId
	 * 		企业域id
	 * @param page
	 * 		当前页数
	 * @param line
	 * 		每页记录条数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<ApplicationVO> queryUnBindApplicationsByDomain(ParamsTable params,String domainId, int page, int line) throws Exception;
	
	/**
	 * 根据域开发者用户获取应用
	 * 
	 * @param developerId
	 *            开发者用户
	 * @return 应用集合
	 * @throws Exception
	 */
	public Collection<ApplicationVO> getApplicationsByDeveloper(String developerId) throws Exception;
	
	/**删除软件时，直接根据applicationId删除摘要、视图、流程、报表、打印
	 * @param applicationId
	 * @throws Exception
	 */
	public void deletebyApplication(String applicationId) throws Exception;

	/**
	 * 根据域管理员用户获取应用
	 * 
	 * @param domainAdminId
	 *            域管理员用户
	 * @return 应用集合
	 * @throws Exception
	 */
	public Collection<ApplicationVO> getApplicationsByDoaminAdmin(String domainAdminId) throws Exception;
	
	public Collection<DomainApplicationSet> getDomainApplicationSetByDomainId(String domainId) throws Exception;
	
	public void updateDomainApplicationSet(String domainId,String applicationId,String weixinAgentId) throws Exception;
	
	public void removeDomainApplicationSet(String domainId,String applicationId) throws Exception;
	
	public void createDomainApplicationSet(DomainApplicationSet domainApplicationSet) throws Exception;
	
	public DomainApplicationSet findDomainApplicationSet(String domainId,String applicationId) throws Exception;
	
	public Collection<DomainApplicationSet> queryDomainApplicationSet(String applicationId) throws Exception;

}
