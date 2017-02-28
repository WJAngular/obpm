package cn.myapps.core.domain.ejb;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;

import javax.naming.ldap.LdapContext;

import net.sf.json.JSONObject;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;

/**
 * 
 * 域管理, 通过extends(IDesignTimeProcess) 实现,域对应用同时进行了管理.
 */
public interface DomainProcess extends IDesignTimeProcess<DomainVO> {

	/**
	 * 获取域对象
	 * 
	 * @param 域名
	 * @return 域对象(#DomainVO)
	 * @throws Exception
	 */
	public DomainVO getDomainByName(String tempname) throws Exception;
	
	/**
	 * 获取域对象
	 * 
	 * @param 域名
	 * @return 域对象(#DomainVO)
	 * @throws Exception
	 */
	public DomainVO getDomainByDomainName(String name) throws Exception;

	/**
	 * 获取域对象的集合
	 * 
	 * @param userid
	 *            用户标识
	 * @param page
	 *            页码
	 * @param line
	 *            记录数
	 * @return 域对象集合(java.utll.Collection)
	 * @throws Exception
	 */
	public Collection<DomainVO> queryDomains(String userId, int page, int line) throws Exception;

	/**
	 * 根据参数条件以及应用标识,返回表单的DataPackage .
	 * <p>
	 * DataPackage为一个封装类，此类封装了所得到的Domain数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @param manager
	 *            管理员名
	 * @param page
	 *            页码
	 * @param line
	 *            记录数
	 * @return 域对象的数据集合
	 * @throws Exception
	 */
	public DataPackage<DomainVO> queryDomainsByManager(String managerName, int page, int line) throws Exception;

	/**
	 * 根据参数条件以及应用标识,返回表单的DataPackage .
	 * <p>
	 * DataPackage为一个封装类，此类封装了所得到的Domain数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @param name
	 *            域名
	 * @param page
	 *            页码
	 * @param line
	 *            记录数
	 * @return 域对象的数据集合
	 * @throws Exception
	 */
	public DataPackage<DomainVO> queryDomainsByName(String name, int page, int line) throws Exception;

	/**
	 * 根据参数条件以及应用标识,返回表单的DataPackage .
	 * <p>
	 * DataPackage为一个封装类，此类封装了所得到的Domain数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @param manager
	 *            管理员名
	 * @param name
	 *            域名
	 * @param page
	 *            页码
	 * @param line
	 *            记录数
	 * @return 域对象的数据集合
	 * @throws Exception
	 */
	public DataPackage<DomainVO> queryDomainsByManagerAndName(String managerName, String name, int page, int line)
			throws Exception;

	/**
	 * 获取所有域对象的集合
	 * 
	 * @return 域对象的集合(java.utll.Collection)
	 * @throws Exception
	 */
	public Collection<DomainVO> getAllDomain() throws Exception;
	
	/**
	 * 根据激活状态获取所有域对象的集合
	 * 
	 * @return 域对象的集合(java.utll.Collection)
	 * @throws Exception
	 */
	public Collection<DomainVO> getDomainByStatus(int status) throws Exception;

	/**
	 * 根据管理员登录名和域名称查询域
	 * 
	 * @param managerLoginno
	 *            -管理员登录名
	 * @param name
	 *            -域名称
	 * @param page
	 *            -页
	 * @param line
	 *            -行
	 * @return
	 * @throws Exception
	 */
	public DataPackage<DomainVO> queryDomainsbyManagerLoginnoAndName(String manager, String name, int page, int line)
			throws Exception;

	/**
	 * 创建企业域
	 * @param vo 
	 * 		企业域
	 * @param isCreateDeps
	 * 		是否创建默认顶级部门
	 * @throws Exception
	 */
	public void doCreate(ValueObject vo,boolean isCreateDeps) throws Exception;
	
	public Collection<DomainVO> queryDomainsByStatus(int status)throws Exception;
	
	public Collection<DomainVO> queryDomainsByStatusAndUser(int status,String userid)throws Exception;
	/**
	 * 企业域同步LDAP部门及用户信息
	 * @param ldapContext
	 * @throws Exception
	 */
	public void synchLDAP(LdapContext ldapContext, DomainVO domain) throws Exception;
	
	/**
	 * 从企业微信上同步组织结构到企业域
	 * @param domain
	 * @throws Exception
	 */
	public void synchFromWeixin(DomainVO domain) throws Exception;
	
	/**
	 * 同步企业域组织结构到企业微信
	 * @param domain
	 * @throws Exception
	 */
	public void synch2Weixin(DomainVO domain) throws Exception;
	
	
	/**
	 * Excel导入部门、用户
	 * @param domain
	 *             企业域
	 * @param file
	 *            Excel文件
	 * @return
	 * @throws Exception 
	 */
	public JSONObject excelImportToDomain(DomainVO domain,File file) throws Exception;
	
	/**
	 * Excel导出部门、用户
	 * @param domain
	 *             企业域
	 * @return
	 * @throws Exception 
	 */
	public void excelExportFromDomain(OutputStream outputStream,DomainVO domain) throws Exception;
}
