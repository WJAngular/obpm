package cn.myapps.core.deploy.application.ejb;

import java.util.Collection;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;

public interface ApplicationProcess extends IDesignTimeProcess<ApplicationVO> {

	/**
	 * 获取域下的应用的集合保存(key: 企业域名称 value:应用)
	 * 
	 * @return 应用的集合
	 * @throws Exception
	 */
	public Map<String, ApplicationVO> getAppDomain_Cache() throws Exception;

	/**
	 * 判断是否为空
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean isEmpty() throws Exception;

	/**
	 * 根据管理员用户获取应用集合,并进行分页
	 * 
	 * @param userid
	 *            管理员用户
	 * @param page
	 *            页
	 * @param line
	 *            显示记录条数
	 * @return 应用集合<java.util.Collection>
	 * @throws Exception
	 */
	public Collection<ApplicationVO> queryApplications(String userid, int page, int line) throws Exception;

	/**
	 * 根据域获取域下的所有用户,并进行分页
	 * 
	 * @param domainId
	 *            域标识
	 * @param page
	 *            页码
	 * @param line
	 *            显示记录
	 * @return 应用集合<java.util.Collection>
	 * @throws Exception
	 */
	public Collection<ApplicationVO> queryAppsByDomain(String domainId, int page, int line) throws Exception;

	/**
	 * 根据管理员用户获取应用
	 * 
	 * @param userid
	 *            管理员用户标识
	 * @return 应用集合<java.util.Collection>
	 * @throws Exception
	 */
	public Collection<ApplicationVO> queryApplications(String userid) throws Exception;

	/**
	 * 根据域标识获取所属域下的所有应用
	 * 
	 * @param domainId
	 *            域标识
	 * @return 应用集合<java.util.Collection>
	 * @throws Exception
	 */
	public Collection<ApplicationVO> queryByDomain(String domainId) throws Exception;

	/**
	 * 根据阿里软件标识查询应用
	 * 
	 * @param appKey
	 *            阿里软件ID
	 * @return 应用对象
	 * @throws Exception
	 */
	public ApplicationVO findBySIPAppKey(String appKey) throws Exception;

	/**
	 * 根据应用名获取应用
	 * 
	 * @param name
	 *            应用名
	 * @return 应用对象
	 * @throws Exception
	 */
	public ApplicationVO doViewByName(String name) throws Exception;

	/**
	 * 添加开发者到应用
	 * 
	 * @param developerIds
	 *            所属开发者的标识
	 * @param id
	 *            应用标识
	 * @throws Exception
	 */
	public void addDevelopersToApplication(String[] developerIds, String id) throws Exception;

	/**
	 * 从应用中移除开发者
	 * 
	 * @param developerIds
	 *            开发用户
	 * @param id
	 *            应用标识
	 * @throws Exception
	 */
	public void removeDevelopersFromApplication(String[] developerIds, String id) throws Exception;

	/**
	 * 根据域开发者用户获取应用
	 * 
	 * @param developerId
	 *            开发者用户
	 * @return 应用集合
	 * @throws Exception
	 */
	public Collection<ApplicationVO> getApplicationsByDeveloper(String developerId) throws Exception;

	/**
	 * 根据域管理员用户获取应用
	 * 
	 * @param domainAdminId
	 *            域管理员用户
	 * @return 应用集合
	 * @throws Exception
	 */
	public Collection<ApplicationVO> getApplicationsByDoaminAdmin(String domainAdminId) throws Exception;


	/**
	 * 获取默认应用
	 * 
	 * @param defaultApplicationid
	 *            默认应用ID
	 * @param domainid
	 *            企业域ID
	 * @return
	 * @throws Exception
	 */
	public ApplicationVO getDefaultApplication(String defaultApplicationid, WebUser user) throws Exception;
	
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
	public DataPackage<ApplicationVO> doQueryByDomain(String domainId, int page, int line) throws Exception;
	
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
	public DataPackage<ApplicationVO> doQueryUnBindApplicationsByDomain(ParamsTable params,String domainId, int page, int line) throws Exception;
	
	/**删除软件时，直接根据applicationId删除摘要、视图、流程、报表、打印
	 * @param applicationId 软件id
	 * @throws Exception
	 */
	public void doDeletebyApplication(String applicationId) throws Exception;
	
	public Collection<DomainApplicationSet> getDomainApplicationSetByDomainId(String domainId) throws Exception;
	
	public void updateDomainApplicationSet(String domainId,String applicationId,String weixinAgentId) throws Exception;
	
	public void removeDomainApplicationSet(String domainId,String applicationId) throws Exception;
	
	public void createDomainApplicationSet(DomainApplicationSet domainApplicationSet) throws Exception;
	
	public DomainApplicationSet findDomainApplicationSet(String domainId,String applicationId) throws Exception;
	
	public Collection<DomainApplicationSet> queryDomainApplicationSet(String applicationId) throws Exception;

}
