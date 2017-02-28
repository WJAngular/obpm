package cn.myapps.webservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateSQLUtils;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.webservice.fault.ApplicationServiceFault;
import cn.myapps.webservice.model.SimpleApplication;
import cn.myapps.webservice.util.ApplicationUtil;

public class ApplicationService {

	/**
	 * 查询软件列表
	 * 
	 * @param name
	 *            软件名称
	 * @return 软件列表
	 * @throws ApplicationServiceFault
	 */
	public Collection<SimpleApplication> searchApplicationsByName(String name)
			throws ApplicationServiceFault {
		try {
			ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_name", name);
			Collection<ApplicationVO> appList = (Collection<ApplicationVO>) applicationProcess
					.doSimpleQuery(params);
			if (appList != null && !appList.isEmpty()) {
				return ApplicationUtil.convertToSimple(appList);
			}

		} catch (Exception e) {
			throw new ApplicationServiceFault(e.getMessage());
		}

		return new ArrayList<SimpleApplication>();
	}

	/**
	 * 根据名称查询软件
	 * 
	 * @param name
	 *            软件名称
	 * @return 软件
	 * @throws ApplicationServiceFault
	 *             软件服务异常
	 */
	public SimpleApplication searchApplicationByName(String name)
			throws ApplicationServiceFault {
		try {
			ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			ApplicationVO app = applicationProcess.doViewByName(name);
			if (app != null) {
				return ApplicationUtil.convertToSimple(app);
			}
		} catch (Exception e) {
			throw new ApplicationServiceFault(e.getMessage());
		}

		return null;
	}

	/**
	 * 根据参数进行查询,匹配条件前缀 + 字段名称=Key,如"="为t_xxx,详细请查看HibernateSQLUtils
	 * 
	 * @see HibernateSQLUtils
	 * @param parameters
	 * @return 应用集合
	 * @throws ApplicationServiceFault
	 */
	public Collection<SimpleApplication> searchApplicationsByFilter(
			Map<String, Object> parameters) throws ApplicationServiceFault {
		try {
			ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			ParamsTable params = new ParamsTable();
			params.putAll(parameters);

			DataPackage<ApplicationVO> appPackage = (DataPackage<ApplicationVO>) applicationProcess
					.doQuery(params);
			if (appPackage != null && appPackage.datas != null
					&& !appPackage.datas.isEmpty()) {
				return ApplicationUtil.convertToSimple(appPackage.datas);
			}

		} catch (Exception e) {
			throw new ApplicationServiceFault(e.getMessage());
		}

		return new ArrayList<SimpleApplication>();
	}
	
	/**
	 * 根据json参数进行查询,匹配条件前缀 + 字段名称=Key,如"="为t_xxx,即"t_ID=1"为"ID=1"详细请查看HibernateSQLUtils
	 * @param parameters 参数(Json格式)
	 * @return 应用集合
	 * @throws ApplicationServiceFault
	 */
	public Collection<SimpleApplication> searchApplicationsByFilter(
			String parameters) throws ApplicationServiceFault {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtil.isBlank(parameters))
				params = JsonUtil.toMap(parameters);
			return searchApplicationsByFilter(params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationServiceFault(e.getMessage());
		}
	}

	/**
	 * 根据域标识查询应用
	 * 
	 * @param domainId
	 *            域ID
	 * @return 应用的集合
	 * @throws ApplicationServiceFault
	 */
	public Collection<SimpleApplication> searchApplicationsByDomain(
			String domainId) throws ApplicationServiceFault {
		try {
			DomainProcess domainProcess = (DomainProcess) ProcessFactory
					.createProcess(DomainProcess.class);
			DomainVO domain = (DomainVO) domainProcess.doView(domainId);
			Collection<ApplicationVO> appList = domain.getApplications();

			return ApplicationUtil.convertToSimple(appList);
		} catch (Exception e) {
			throw new ApplicationServiceFault(e.getMessage());
		}
	}

	/**
	 * 根据域管理员用户获取应用
	 * 
	 * @param domainAdminId
	 *            域管理员用户
	 * @return 应用集合
	 * @throws ApplicationServiceFault
	 */
	public Collection<SimpleApplication> searchApplicationsByDomainAdmin(
			String domainAdminId) throws ApplicationServiceFault {
		try {
			ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			Collection<ApplicationVO> appList = applicationProcess
					.getApplicationsByDoaminAdmin(domainAdminId);
			if (appList != null && !appList.isEmpty()) {
				return ApplicationUtil.convertToSimple(appList);
			}

		} catch (Exception e) {
			throw new ApplicationServiceFault(e.getMessage());
		}

		return new ArrayList<SimpleApplication>();
	}

	/**
	 * 查询软件列表
	 * 
	 * @param developerId
	 *            开发者ID
	 * @return 软件列表
	 * @throws ApplicationServiceFault
	 *             软件服务异常
	 */
	public Collection<SimpleApplication> searchApplicationsByDeveloper(
			String developerId) throws ApplicationServiceFault {
		try {
			ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			Collection<ApplicationVO> appList = applicationProcess
					.getApplicationsByDeveloper(developerId);
			if (appList != null && !appList.isEmpty()) {
				return ApplicationUtil.convertToSimple(appList);
			}

		} catch (Exception e) {
			throw new ApplicationServiceFault(e.getMessage());
		}

		return new ArrayList<SimpleApplication>();
	}

	/**
	 * 企业用户订购应用
	 * 
	 * @param userAccount
	 *            域管理员账号
	 * @param domainName
	 *            域名称
	 * @param applicationId
	 *            应用ID
	 * @throws ApplicationServiceFault
	 */
	public boolean addApplication(String userAccount, String domainName,
			String applicationId) throws ApplicationServiceFault {
		boolean isVaild = false;

		try {
			SuperUserProcess sUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			DomainProcess domainProcess = (DomainProcess) ProcessFactory
					.createProcess(DomainProcess.class);
			ParamsTable params = new ParamsTable();

			params.setParameter("t_loginno", userAccount);
			Collection<?> userList = sUserProcess.doSimpleQuery(params);
			if (userList != null && !userList.isEmpty()) {
				SuperUserVO user = (SuperUserVO) userList.toArray()[0];
				DomainVO domain = user.getDomainByName(domainName);
				if (domain != null) {
					ApplicationVO application = (ApplicationVO) applicationProcess
							.doView(applicationId);
					Collection<ApplicationVO> appSet = new HashSet<ApplicationVO>();
					appSet.addAll(domain.getApplications());
					appSet.add(application);

					domain.setApplications(appSet);
					domainProcess.doUpdate(domain);
					isVaild = true;
				}
			}
		} catch (Exception e) {
			throw new ApplicationServiceFault(e.getMessage());
		}

		return isVaild;
	}

	public static void main(String[] args) throws ApplicationServiceFault {
		ApplicationService service = new ApplicationService();
		Map<String, Object> params = new HashMap<String, Object>();
		Collection<?> appList = service.searchApplicationsByFilter(params);
		for (Iterator<?> iterator = appList.iterator(); iterator.hasNext();) {
			// SimpleApplication sp = (SimpleApplication) iterator.next();
		}
	}
}
