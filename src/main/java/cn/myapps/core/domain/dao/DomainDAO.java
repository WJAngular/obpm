package cn.myapps.core.domain.dao;

import java.util.Collection;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.domain.ejb.DomainVO;

public interface DomainDAO extends IDesignTimeDAO<DomainVO> {

	public DomainVO getDomainByName(String tempname) throws Exception;
	
	public Collection<DomainVO> queryDomains(String userid, int page, int line) throws Exception;

	public DataPackage<DomainVO> queryDomainsByManager(String manager, int page, int line) throws Exception;
	
	public DataPackage<DomainVO> queryDomainsByName(String name,int page,int line) throws Exception;
	
	public DataPackage<DomainVO> queryDomainsbyManagerAndName(String manager,String name,int page,int line) throws Exception;
	
	public Collection<DomainVO> getAllDomain() throws Exception;
	
	public Collection<DomainVO> getDomainByStatus(int status) throws Exception;
	
	public Collection<DomainVO> getDomainByStatusAndUser(int status,String userid) throws Exception;
	
	public DataPackage<DomainVO> queryDomainsbyManagerLoginnoAndName(String manager,
			String name, int page, int line) throws Exception;
	public Collection<DomainVO> queryDomainsByStatus(int status)
	throws Exception;
}
