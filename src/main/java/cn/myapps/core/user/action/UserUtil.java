package cn.myapps.core.user.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.action.DomainHelper;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserProcessBean;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.storage.runtime.proxy.ejb.WorkflowProxyProcess;
import cn.myapps.km.org.ejb.NUserRoleSet;
import cn.myapps.km.org.ejb.NUserRoleSetProcessBean;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import cn.myapps.util.web.DWRHtmlUtils;

public class UserUtil {

	private static final Logger LOG = Logger.getLogger(UserUtil.class);

	public Map<String, String> getUserOptionsByDomain(String domainId)
			throws Exception {
		Map<String, String> options = new LinkedHashMap<String, String>();

		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		Collection<UserVO> userList = up.queryByDomain(domainId, 1,
				Integer.MAX_VALUE);
		for (Iterator<UserVO> iterator = userList.iterator(); iterator
				.hasNext();) {
			UserVO user = (UserVO) iterator.next();
			options.put(user.getId(), user.getName());
		}

		return options;
	}

	public Collection<String> getDepartmentids(String userid) throws Exception {
		Collection<String> rtn = new ArrayList<String>();

		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		UserVO user = (UserVO) up.doView(userid);
		if (user != null) {
			Collection<DepartmentVO> depts = user.getDepartments();
			if (depts != null && depts.size() > 0) {
				for (Iterator<DepartmentVO> iter = depts.iterator(); iter
						.hasNext();) {
					DepartmentVO dept = (DepartmentVO) iter.next();
					rtn.add(dept.getId());
				}
			}
		}
		return rtn;
	}

	public Collection<String> getDepartmentOpenList(String userid)
			throws Exception {
		List<String> rtn = new ArrayList<String>();

		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		UserVO user = (UserVO) up.doView(userid);
		if (user != null) {
			Collection<DepartmentVO> depts = user.getDepartments();
			if (depts != null && depts.size() > 0) {
				for (Iterator<DepartmentVO> iter = depts.iterator(); iter
						.hasNext();) {
					DepartmentVO dept = (DepartmentVO) iter.next();
					DepartmentVO superior = dept.getSuperior();

					while (superior != null) {
						rtn.add(superior.getId());
						superior = superior.getSuperior();
					}
					rtn.add(dept.getId());
				}
			}
		}

		Collections.reverse(rtn);

		return rtn;
	}

	/**
	 * 根据应用获取角色组
	 * 
	 * @param applicationVO
	 *            应用标识
	 * @return 角色组的Map集合
	 * @throws Exception
	 */
	public Map<String, String> getRolesByApplication(ApplicationVO application)
			throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (application == null)
			return map;
		RoleProcess rp = (RoleProcess) ProcessFactory
				.createProcess(RoleProcess.class);

		Collection<RoleVO> roles = rp
				.getRolesByApplication(application.getId());

		for (Iterator<RoleVO> iter = roles.iterator(); iter.hasNext();) {
			RoleVO vo = iter.next();
			map.put(vo.getId(), vo.getName());
		}
		return map;
	}

	/**
	 * 根据应用获取角色组
	 * 
	 * @param applicationid
	 *            应用标识
	 * @return 角色组的Map集合
	 * @throws Exception
	 */
	public Map<String, String> getRolesByApplicationid(String applicationid)
			throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (applicationid == null || applicationid.trim().length() <= 0)
			return map;

		ApplicationProcess ap = (ApplicationProcess) ProcessFactory
				.createProcess(ApplicationProcess.class);

		return getRolesByApplication((ApplicationVO) ap.doView(applicationid));
	}

	public String[] getRolesIDByUser(String userid) throws Exception {
		String[] roleids = null;
		if (userid == null || userid.equals(""))
			return roleids;
		try {
			UserProcess up = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			UserVO user = (UserVO) up.doView(userid);
			if (user == null)
				return roleids;
			Collection<RoleVO> col = user.getRoles();
			if (!col.isEmpty()) {
				int i = 0;
				roleids = new String[col.size()];
				Iterator<RoleVO> it = col.iterator();
				while (it.hasNext()) {
					RoleVO role = it.next();
					roleids[i] = role.getId();
					i++;
				}
			}
		} finally {
			PersistenceUtils.closeSession();
		}

		return roleids;
	}

	public Collection<ApplicationVO> getApplications(String userid)
			throws Exception {
		Collection<ApplicationVO> rtn = new ArrayList<ApplicationVO>();
		SuperUserProcess sprocess = (SuperUserProcess) ProcessFactory
				.createProcess(SuperUserProcess.class);
		BaseUser user = (SuperUserVO) sprocess.doView(userid);
		if (user == null) {
			UserProcess up = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			user = (UserVO) up.doView(userid);
		}
		if (user != null) {
			DomainVO vo = DomainHelper.getDomainVO(user);
			rtn = vo.getApplications();
		}
		return rtn;
	}

	public Map<String, String> getRolesByApplications(
			Collection<ApplicationVO> applications) throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (applications == null) {
			return map;
		}
		for (Iterator<ApplicationVO> iterator = applications.iterator(); iterator
				.hasNext();) {
			ApplicationVO app = iterator.next();
			Map<String, String> tmp = getRolesByApplication(app);
			if (tmp != null)
				map.putAll(tmp);
		}
		return map;
	}

	/**
	 * 创建适合在页面上使用的选择框
	 * 
	 * @param applications
	 *            角色组所在的应用标识
	 * @param userid
	 * 
	 * @param divid
	 * @return
	 * @throws Exception
	 */
	public String creatRoleList(Collection<ApplicationVO> applications,
			String userid, String divid) throws Exception {
		Map<String, String> map = getRolesByApplications(applications);
		String[] roleid = getRolesIDByUser(userid);
		return DWRHtmlUtils.createCheckbox(map, divid, roleid);
	}

	/**
	 * 创建适合在页面上使用的选择框
	 * 
	 * @param domainid
	 *            角色组所在的域标识
	 * @param userid
	 * 
	 * @param divid
	 * @return
	 * @throws Exception
	 */
	public String createRoleList(String domainid, String userid, String divid)
			throws Exception {
		Map<String, String> map = getRolesByDomain(domainid);
		String[] roleid = getRolesIDByUser(userid);
		return DWRHtmlUtils.createCheckbox(map, divid, roleid);
	}

	private Map<String, String> getRolesByDomain(String domainid)
			throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (domainid == null || domainid.trim().length() <= 0) {
			return map;
		}
		DomainVO vo = DomainHelper.getDomainVO(domainid);
		return getRolesByApplications(vo.getApplications());
	}

	public void setSessionTimeOut() {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().setMaxInactiveInterval(1);
	}

	public Map<Integer, String> getDomainPermission() {
		LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(Integer.valueOf(BaseUser.NORMAL_DOMAIN), "{*[Normal_Domain]*}");
		map.put(Integer.valueOf(BaseUser.UPGRADE_DOMAIN),
				"{*[Upgrade_Domain]*}");
		map.put(Integer.valueOf(BaseUser.ADVANCED_DOMAIN),
				"{*[Advanced_Domain]*}");
		map.put(Integer.valueOf(BaseUser.VIP_DOMAIN), "{*[VIP_Domain]*}");
		map.put(Integer.valueOf(BaseUser.SUPER_DOMAIN), "{*[Super_Domain]*}");
		return map;
	}

	public Map<String, String> getDomainLevels() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("" + BaseUser.NORMAL_DOMAIN, "{*[Normal_Domain]*}");
		map.put("" + BaseUser.UPGRADE_DOMAIN, "{*[Upgrade_Domain]*}");
		map.put("" + BaseUser.ADVANCED_DOMAIN, "{*[Advanced_Domain]*}");
		map.put("" + BaseUser.VIP_DOMAIN, "{*[VIP_Domain]*}");
		map.put("" + BaseUser.SUPER_DOMAIN, "{*[Super_Domain]*}");
		return map;
	}

	public String findUserName(String userid) throws Exception {
		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		UserVO user = (UserVO) up.doView(userid);
		if (user != null)
			return user.getName();
		else
			return "system";
	}

	public String findUserLoginNo(String userid) throws Exception {
		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		UserVO user = (UserVO) up.doView(userid);
		if (user != null)
			return user.getLoginno();
		else
			return "";
	}

	public String queryUserIdsByName(String name, String domainid)
			throws Exception {
		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		return up.queryUserIdsByName(name, domainid);
	}

	public String queryUserIdByAccount(String account, String domainid)
			throws Exception {
		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		return up.findUserIdByAccount(account, domainid);
	}

	/**
	 * 根据用户角色，取出要展开的应用
	 * 
	 * @author bluce
	 * @param userid
	 * @return 应用集合
	 * @throws Exception
	 * @date 2010-05-10
	 */
	public Collection<String> getApplicationOpenList(String userid)
			throws Exception {
		List<String> rtn = new ArrayList<String>();
		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		UserVO user = (UserVO) up.doView(userid);
		Collection<RoleVO> roles = user.getRoles();
		if (roles != null && roles.size() > 0) {
			for (Iterator<RoleVO> iter = roles.iterator(); iter.hasNext();) {
				RoleVO role = (RoleVO) iter.next();
				String applicationId = role.getApplicationid();
				if (!rtn.contains(applicationId)) {
					// 通过角色获取应用id
					rtn.add(applicationId);
				}
			}
		}
		return rtn;
	}

	/**
	 * 通过用户获取角色ids，并且以集合形式返回(前台页面显示用户角色勾选时用)
	 * 
	 * @author Bluce
	 * @param userid
	 * @return 角色ids
	 * @throws Exception
	 * @date 2010-05-10
	 */
	public Collection<String> getRolesids(String userid) throws Exception {
		Collection<String> rtn = new ArrayList<String>();
		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		UserVO user = (UserVO) up.doView(userid);
		if (user != null) {
			Collection<RoleVO> roles = user.getRoles();
			if (roles != null && roles.size() > 0) {
				for (Iterator<RoleVO> iter = roles.iterator(); iter.hasNext();) {
					RoleVO role = (RoleVO) iter.next();
					rtn.add(role.getId());
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 通过用户获取KM角色ids，并且以集合形式返回(前台页面显示用户角色勾选时用)
	 * 
	 * @param userid
	 * @return 角色ids
	 * @throws Exception
	 * @date 2010-05-10
	 */
	public Collection<String> getKmRolesids(String userid) throws Exception {
		Collection<String> rtn = new ArrayList<String>();
		NUserRoleSetProcessBean process = new NUserRoleSetProcessBean();
		 Collection<NUserRoleSet> sets = process.doQuertByUser(userid);
		if (!sets.isEmpty()) {
				for (Iterator<NUserRoleSet> iter = sets.iterator(); iter.hasNext();) {
					NUserRoleSet set = (NUserRoleSet) iter.next();
					rtn.add(set.getRoleId());
				}
		}
		return rtn;
	}

	/**
	 * 根据用户id查找用户手机号码,ids以;隔开
	 * 
	 * @param ids
	 * @return 返回以(;)号隔开的手机号码字符串
	 */
	public String getUserTelByIds(String ids) {
		StringBuffer result = new StringBuffer();
		try {
			UserProcess up = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			if (!StringUtil.isBlank(ids)) {
				String[] arrayIds = ids.split(";");
				for (int i = 0; i < arrayIds.length; i++) {
					UserVO user = (UserVO) up.doView(arrayIds[i]);
					if (result.length() < 1) {
						result.append(user.getTelephone());
					} else {
						result.append(";" + user.getTelephone());
					}
				}
			}
		} catch (Exception e) {
			LOG.warn(e.toString());
		}
		return result.toString();
	}
	
	public int findUserLockFlag(String userid) throws Exception {
		UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		UserVO user = (UserVO) up.doView(userid);
		if (user != null)
			return user.getLockFlag();
		else
			return 0;
	}
	
	public boolean isFlowAgent(WebUser user) throws Exception{
		try{
			ApplicationHelper appHelper = new ApplicationHelper();
			Collection<ApplicationVO> appList = appHelper.getListByWebUser(user);
			if(appList != null){
				for(Iterator<ApplicationVO> iter = appList.iterator();iter.hasNext();){
					ApplicationVO appVo = iter.next();
					WorkflowProxyProcess process = (WorkflowProxyProcess) ProcessFactory.createRuntimeProcess(WorkflowProxyProcess.class, appVo.getId());
					boolean isFlowAgent = process.isFlowAgent(user.getId());
					if(isFlowAgent){
						return true;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
}
