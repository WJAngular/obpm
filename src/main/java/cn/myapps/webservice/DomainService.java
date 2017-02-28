package cn.myapps.webservice;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.webservice.fault.DomainServiceFault;
import cn.myapps.webservice.model.SimpleDomain;
import cn.myapps.webservice.util.DomainUtil;

public class DomainService {
	/**
	 * 根据域管理员ID查找所管理的域列表
	 * 
	 * @param domainAdminId
	 *            管理员ID
	 * @return domain list 域列表
	 * @throws DomainServiceFault
	 */
	public Collection<SimpleDomain> searchDomainsByDomainAdmin(
			String domainAdminId) throws DomainServiceFault {
		try {
			SuperUserProcess userProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			SuperUserVO superUserVO = (SuperUserVO) userProcess
					.doView(domainAdminId);
			Collection<?> domains = superUserVO.getDomains();
			return DomainUtil.convertToSimple(domains);
		} catch (Exception e) {
			throw new DomainServiceFault(e.getMessage());
		}
	}

	/**
	 * 根据名称查找域
	 * 
	 * @param name
	 *            　名称
	 * @return　应用
	 * @throws DomainServiceFault
	 */
	public SimpleDomain searchDomainByName(String name)
			throws DomainServiceFault {
		try {
			DomainProcess domainProcess = (DomainProcess) ProcessFactory
					.createProcess(DomainProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_name", name);
			Collection<?> domainList = domainProcess.doSimpleQuery(params);
			if (domainList != null && !domainList.isEmpty()) {
				DomainVO domain = (DomainVO) domainList.iterator().next();
				return DomainUtil.convertToSimple(domain);
			}
			return null;
		} catch (Exception e) {
			throw new DomainServiceFault(e.getMessage());
		}
	}

}
