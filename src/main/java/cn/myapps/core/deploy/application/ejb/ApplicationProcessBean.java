package cn.myapps.core.deploy.application.ejb;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.deploy.application.dao.ApplicationDAO;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.RuntimeDaoManager;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class ApplicationProcessBean extends AbstractDesignTimeProcessBean<ApplicationVO> implements ApplicationProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3793738490908856529L;

	public void doRemove(String pk) throws Exception {
		// 检查是否有下级模块
		super.doRemove(pk);
	}

	protected IDesignTimeDAO<ApplicationVO> getDAO() throws Exception {
		return (ApplicationDAO) DAOFactory.getDefaultDAO(ApplicationVO.class.getName());
	}

	public Collection<ApplicationVO> queryByDomain(String domainId) throws Exception {
		return ((ApplicationDAO) getDAO()).queryAppsByDomain(domainId, 1, Integer.MAX_VALUE);
	}

	public Map<String, ApplicationVO> getAppDomain_Cache() throws Exception {
		return ((ApplicationDAO) getDAO()).getAppDomain_Cache();

	}

	public boolean isEmpty() throws Exception {
		return ((ApplicationDAO) getDAO()).isEmpty();
	}

	public void doCreate(ValueObject vo) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			ApplicationVO app = (ApplicationVO) vo;
			testDB(app);
			if (vo.getId() == null || vo.getId().trim().length() == 0) {
				vo.setId(Sequence.getSequence());
			}

			if (vo.getSortId() == null || vo.getSortId().trim().length() == 0) {
				vo.setSortId(Sequence.getTimeSequence());
			}

			getDAO().create(vo);

			DataSource ds = app.getDataSourceDefine();
			// 初始化RT表
			if (ds != null && ds.getDbTypeName() != null) {
				Connection conn = null;

				try {
					conn = ds.getConnection();
					new RuntimeDaoManager().getApplicationInitDAO(conn, app.getApplicationid(), ds.getDbTypeName())
							.initTables();
				} catch (Exception e) {
					throw e;
				} finally {
					PersistenceUtils.closeConnection(conn);
				}
			}

			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			throw e;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.BaseProcess#doUpdate(cn.myapps.base.dao.ValueObject)
	 */
	public void doUpdate(ValueObject vo) throws Exception {
		ApplicationVO app = (ApplicationVO) vo;
		DataSource ds = app.getDataSourceDefine();
		if (ds != null && ds.getDbTypeName() != null) {
			testDB(app);

			Connection conn = null;
			try {
				conn = ds.getConnection();

				new RuntimeDaoManager().getApplicationInitDAO(conn, app.getApplicationid(), ds.getDbTypeName())
						.initTables();
			} catch (Exception e) {
				throw e;
			} finally {
				conn.close();
			}
		}
		super.doUpdate(vo);
	}

	public Collection<ApplicationVO> queryApplications(String suserid) throws Exception {
		return queryApplications(suserid, 1, Integer.MAX_VALUE);
	}

	public Collection<ApplicationVO> queryApplications(String suserid, int page, int line) throws Exception {
		return ((ApplicationDAO) getDAO()).queryApplications(suserid, page, line);
	}

	public Collection<ApplicationVO> queryAppsByDomain(String domainId, int page, int line) throws Exception {
		return ((ApplicationDAO) getDAO()).queryAppsByDomain(domainId, page, line);
	}

	public ApplicationVO getDefaultApplication(String defaultApplicationid, WebUser user) throws Exception {
		if (StringUtil.isBlank(defaultApplicationid)) {
			Collection<ApplicationVO> appList = queryByDomain(user.getDomainid());
			if (!appList.isEmpty()) {
				// 如果没有默认则选择第一个
				for (Iterator<ApplicationVO> iterator = appList.iterator(); iterator.hasNext();) {
					ApplicationVO application = (ApplicationVO) iterator.next();
					Collection<RoleVO> roles = user.getRolesByApplication(application.getId());
					if (roles != null && !roles.isEmpty()) { // 进行角色校验
						return application;
					}
				}
			}
		} else {
			ApplicationVO application = (ApplicationVO) doView(defaultApplicationid);
			if(application !=null){
				Collection<RoleVO> roles = user.getRolesByApplication(application.getId());
				if (roles != null && !roles.isEmpty()) { // 进行角色校验
					return application;
				} else {
					return getDefaultApplication("", user);
				}
			}else{
				return getDefaultApplication("", user);
			}
		}

		return null;
		//throw new Exception("{*[core.domain.user.noapp]*}");
	}

	public ApplicationVO findBySIPAppKey(String appKey) throws Exception {
		return ((ApplicationDAO) getDAO()).findBySIPAppKey(appKey);
	}

	public ApplicationVO doViewByName(String name) throws Exception {
		return ((ApplicationDAO) getDAO()).findByName(name);
	}

	public Collection<ApplicationVO> getApplicationsByDeveloper(String developerId) throws Exception {
		return ((ApplicationDAO) getDAO()).getApplicationsByDeveloper(developerId);
	}

	public Collection<ApplicationVO> getApplicationsByDoaminAdmin(String domainAdminId) throws Exception {
		return ((ApplicationDAO) getDAO()).getApplicationsByDoaminAdmin(domainAdminId);
	}

	/**
	 * 添加开发者到应用
	 */
	public void addDevelopersToApplication(String[] developerIds, String id) throws Exception {
		SuperUserProcess suerProcess = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);

		ApplicationVO application = (ApplicationVO) getDAO().find(id);
		if (application != null) {
			Set<SuperUserVO> tempSet = new HashSet<SuperUserVO>();
			Collection<SuperUserVO> developerSet = application.getOwners();
			tempSet.addAll(developerSet);
			for (int i = 0; i < developerIds.length; i++) {
				String developerId = developerIds[i];
				SuperUserVO developer = (SuperUserVO) suerProcess.doView(developerId);
				tempSet.add(developer);
			}
			application.setOwners(tempSet);
			doUpdate(application);
		}
	}

	/**
	 * 从应用中移除开发者
	 */
	public void removeDevelopersFromApplication(String[] developerIds, String id) throws Exception {
		ApplicationVO application = (ApplicationVO) getDAO().find(id);
		if (application != null) {
			Set<SuperUserVO> tempSet = new HashSet<SuperUserVO>();
			Collection<SuperUserVO> developerSet = application.getOwners();
			outer: for (Iterator<SuperUserVO> iterator = developerSet.iterator(); iterator.hasNext();) {
				SuperUserVO owener = iterator.next();
				for (int i = 0; i < developerIds.length; i++) {
					String developerId = developerIds[i];
					if (owener.getId().equals(developerId)) {
						continue outer;
					}
				}
				tempSet.add(owener);
			}
			application.setOwners(tempSet);
			doUpdate(application);
		}

	}

	public void testDB(ApplicationVO application) throws Exception {
		try {
			DataSource ds = application.getDataSourceDefine();
			Connection conn = ds.getConnection();
			conn.close();
		} catch (Exception e) {
			throw new OBPMValidateException("{*[connect.database.failed]*}");
		}
	}
	
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
	public DataPackage<ApplicationVO> doQueryByDomain(String domainId, int page, int line) throws Exception{
		return ((ApplicationDAO) getDAO()).queryByDomain(domainId, page, line);
	}
	
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
	public DataPackage<ApplicationVO> doQueryUnBindApplicationsByDomain(ParamsTable params,String domainId, int page, int line) throws Exception{
		return ((ApplicationDAO) getDAO()).queryUnBindApplicationsByDomain(params,domainId, page, line);
	}

	public void doDeletebyApplication(String applicationId) throws Exception {
		((ApplicationDAO) getDAO()).deletebyApplication(applicationId);
	}
	
	public Collection<DomainApplicationSet> getDomainApplicationSetByDomainId(String domainId) throws Exception {
		return ((ApplicationDAO) getDAO()).getDomainApplicationSetByDomainId(domainId);
	}
	
	public void updateDomainApplicationSet(String domainId,String applicationId,String weixinAgentId) throws Exception{
		((ApplicationDAO) getDAO()).updateDomainApplicationSet(domainId, applicationId, weixinAgentId);
	}
	
	public void removeDomainApplicationSet(String domainId,String applicationId) throws Exception{
		((ApplicationDAO) getDAO()).removeDomainApplicationSet(domainId, applicationId);
	}
	
	public void createDomainApplicationSet(DomainApplicationSet domainApplicationSet) throws Exception{
		((ApplicationDAO) getDAO()).createDomainApplicationSet(domainApplicationSet);
	}
	
	public DomainApplicationSet findDomainApplicationSet(String domainId,String applicationId) throws Exception{
		return ((ApplicationDAO) getDAO()).findDomainApplicationSet(domainId, applicationId);
	}
	
	public Collection<DomainApplicationSet> queryDomainApplicationSet(String applicationId) throws Exception {
		return ((ApplicationDAO) getDAO()).queryDomainApplicationSet(applicationId);
	}
}
