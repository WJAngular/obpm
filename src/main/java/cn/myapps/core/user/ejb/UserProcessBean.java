package cn.myapps.core.user.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;

import com.github.stuxuhai.jpinyin.PinyinHelper;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.constans.Framework;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.dao.DomainDAO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.sysconfig.ejb.LoginConfig;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.dao.UserDAO;
import cn.myapps.core.usersetup.ejb.UserSetupVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;
import cn.myapps.util.sequence.Sequence;

public class UserProcessBean extends AbstractDesignTimeProcessBean<UserVO> implements UserProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4965110888092349931L;
	public final static HashMap<String, WebUser> _cache = new HashMap<String, WebUser>();

	public void doCreate(final ValueObject vo) throws Exception {
		doCreateWithoutPW(vo, true);
	}
	
	public void doCreateWithoutPW(final ValueObject vo, boolean flag) throws Exception {
		UserVO tmp = null;
		tmp = ((UserDAO) getDAO()).login(((UserVO) vo).getLoginno(), ((UserVO) vo).getDomainid());
		if (tmp != null) {
			throw new OBPMValidateException("{*[core.user.account.exist]*}",new ExistNameException("{*[core.user.account.exist]*}"));
		}

		final UserVO user = (UserVO) vo;

		if (user.getLoginpwd() == null) {
			user.setLoginpwd("");
		} else if(flag){
			user.setLoginpwd(encrypt(user.getLoginpwd()));
		}

		final String name = user.getName();
		final String nameLetter = PinyinHelper.getShortPinyin(name);
		if (!StringUtil.isBlank(nameLetter)) {
			user.setNameLetter(nameLetter);
		}
		try {
			PersistenceUtils.beginTransaction();
			if (user.getId() == null || user.getId().trim().length() == 0) {
				user.setId(Sequence.getSequence());
			}

			if (user.getSortId() == null || user.getSortId().trim().length() == 0) {
				user.setSortId(Sequence.getTimeSequence());
			}

			((UserDAO) getDAO()).create(user);
			PersistenceUtils.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}
		//PermissionPackage.clearCache();
	}

	public void doRemove(final String pk) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			// 检查是否是根部门
			if (pk.equals(Framework.ADMINISTRATOR)) {
				throw new OBPMValidateException("{*[core.user.cannotdelete]*}");
			}
			getDAO().remove(pk);

			PersistenceUtils.commitTransaction();
		} catch (final HibernateException he) {
			throw new OBPMValidateException("{*[core.user.cannotdelete]*}");
		} catch (final Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}

		super.doRemove(pk);
		//PermissionPackage.clearCache();
	}

	public void doRemove(final String[] pks) throws Exception {
		try {
			super.doRemove(pks);
		} catch (final Exception e) {
			throw new Exception("{*[core.user.cannotremove]*}");
		}
	}

	/**
	 * 更新用户默认选择的应用
	 * 
	 * @param userid
	 * @param defaultApplicationid
	 * @throws Exception
	 */
	public void doUpdateDefaultApplication(final String userid, final String defaultApplicationid) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			((UserDAO) getDAO()).updateDefaultApplication(userid, defaultApplicationid);
			PersistenceUtils.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}

	}

	/**
	 * 不清空缓存的更新方法
	 * 
	 * @param vo
	 * @throws Exception
	 */
	public void doUpdateWithCache(final ValueObject vo) throws Exception {
		final UserVO user = (UserVO) vo;
		update(user);
	}

	/**
	 * 清空缓存的更新方法
	 */
	public void doUpdate(final ValueObject vo) throws Exception {
		
	final UserVO user = (UserVO) vo;
	update(user);
	//PermissionPackage.clearCache();
	}
	private void update(final UserVO vo) throws Exception {
		final UserVO user = (UserVO) vo;
		UserVO tmp = null;

		try {
			tmp = ((UserDAO) getDAO()).login((user.getLoginno()), user.getDomainid());
			if (tmp != null && !tmp.getId().equals(vo.getId())) {
				throw new OBPMValidateException("{*[core.user.account.exist]*}",new ExistNameException("{*[core.user.account.exist]*}"));
			}

			PersistenceUtils.beginTransaction();

			final UserVO po = (UserVO) getDAO().find(vo.getId());
			final String name = user.getName();
			final String nameLetter = PinyinHelper.getShortPinyin(name);
			if(po != null && nameLetter != null && !nameLetter.equals(po.getNameLetter())){
				user.setNameLetter(nameLetter);
			}
			final String loginwd = user.getLoginpwd();
			if (po != null && loginwd != null && !loginwd.trim().equals(po.getLoginpwd())
					&& !loginwd.trim().equals(Web.DEFAULT_SHOWPASSWORD)) {
				user.setLoginpwd(encrypt(loginwd));
			} else if (po != null) {
				if (!po.getUseIM() && vo.getUseIM() && decrypt(po.getLoginpwd()) == null){
					throw new OBPMValidateException("{*[reset.password.before.use.im]*}");
				}
				
				user.setLoginpwd(po.getLoginpwd());
			}
			if (po != null) {
				// 如果当前用户等级发生改变, 相应的下级也递归改变
				if (user.getLevel() != po.getLevel()) {
					changeLevel((UserVO) vo);
				}
				PropertyUtils.copyProperties(po, vo);

				getDAO().update(po);
			} else {
				getDAO().update(vo);
			}
			PersistenceUtils.commitTransaction();
		} catch (final Exception e) {
			PersistenceUtils.rollbackTransaction();
			throw e;
		}
		

	}

	/**
	 * 更改所有下级用户等级
	 * 
	 * @param vo
	 * @throws Exception
	 */
	private void changeLevel(final UserVO vo) throws Exception {
		final ParamsTable params = new ParamsTable();
		params.setParameter("t_superior", vo.getId());

		// 获取下级列表
		final Collection<?> underList = ((UserDAO) getDAO()).simpleQuery(params);

		final Iterator<?> itmp = underList.iterator();
		while (itmp.hasNext()) {
			final UserVO curUser = (UserVO) itmp.next();
			curUser.setLevel(vo.getLevel() + 1);
			getDAO().update(curUser);
			changeLevel(curUser);
		}
	}

	public Collection<UserVO> getUnderList(final String userId) throws Exception {
		return getUnderList(userId, 10);
	}

	public Collection<UserVO> getUnderList(final String userId, final int maxDeep) throws Exception {
		if (maxDeep <= 0) {
			return new ArrayList<UserVO>();
		}
		final ParamsTable params = new ParamsTable();
		params.setParameter("t_superior", userId);
		final Collection<UserVO> underList = ((UserDAO) getDAO()).simpleQuery(params);
		final Collection<UserVO> cols = new ArrayList<UserVO>();
		cols.addAll(underList);

		if (underList != null && !underList.isEmpty()) {
			for (final Iterator<UserVO> iterator = underList.iterator(); iterator.hasNext();) {
				final UserVO user = iterator.next();
				cols.addAll(getUnderList(user.getId(), maxDeep - 1));
			}
		}
		return cols;
	}

	public Collection<UserVO> getSuperiorList(String userId) throws Exception {
		Collection<UserVO> superiors = new ArrayList<UserVO>();
		final ParamsTable params = new ParamsTable();
		params.setParameter("id", userId);
		UserVO user = (UserVO) getDAO().find(userId);
		if (user != null) {
			while (true) {
				UserVO superior = user.getSuperior();
				if (superior != null) {
					superiors.add(superior);
					user = superior;
				} else {
					break;
				}
			}
		}
		return superiors;
	}

	public void changePwd(final String id, final String oldPwd, final String newPwd) throws Exception {
		final UserVO vo = (UserVO) getDAO().find(id);
		if (!oldPwd.equals(decrypt(vo.getLoginpwd()))) {
			throw new OBPMValidateException("{*[core.user.password.error]*}");
		}
		vo.setLoginpwd(encrypt(newPwd));
		super.doUpdate(vo);
	}

	public UserVO login(final String no) throws Exception {
		UserVO vo = null;
		try {
			vo = ((UserDAO) getDAO()).login(no);
		} catch (final Exception ex) {
			throw new Exception("{*[core.user.notexist]*}");
		}
		if (vo != null) {
			_cache.remove(vo.getId());
		}
		return vo;
	}

	public UserVO login(final String no, final String password, final String domainName,int pwdErrorTimes) throws Exception {
		UserVO vo = null;
		DomainProcess process = null;
		try{
			process = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
		}catch(Exception e){
			throw new Exception("myApps is not legally authorized");
		}
		final DomainVO domain = process.getDomainByDomainName(domainName);
		
		PropertyUtil.reload("passwordLegal");
		String loginFailTimesString = PropertyUtil.get(LoginConfig.LOGIN_FAIL_TIMES);
		
		int loginFailTimes = -1;

		if (loginFailTimesString != null && !loginFailTimesString.trim().equals("")) {
			loginFailTimes = Integer.parseInt(loginFailTimesString);
		}
		 
		if (domain == null || domain.getStatus() == 0) {
			
			throw new OBPMValidateException("{*[core.domain.notexist]*}");		
		}
		vo = ((UserDAO) getDAO()).login(no, domain.getId());

		if (vo == null) {
			
			throw new OBPMValidateException("{*[core.user.notexist]*}");
		}
		
		_cache.remove(vo.getId());
		
		if(vo.getLockFlag()==0){
			throw new OBPMValidateException("{*[LockedAccount]*}");
		}
		if(vo.getDimission()==0){
			throw new OBPMValidateException("{*[cn.myapps.core.user.isdimissioned]*}");
		}

		if (vo.getLoginpwd() != null
				&& (password.equals(decrypt(vo.getLoginpwd())) || vo.getLoginpwd().equals(encryptOld(password))) || password.equals(vo.getLoginpwd())) {
			if (vo.getStatus() == 1) {
				return vo;
			} else {
				// modified by alex-->
				throw new OBPMValidateException("{*[core.user.noeffectived]*}");
				// <--modified by alex
			}

		} else {
			if (loginFailTimes == -1) {
				throw new OBPMValidateException("{*[core.user.password.error]*}");
			}
			if(loginFailTimes<=pwdErrorTimes){
				this.updateUserLockFlag(no, 0);
				throw new OBPMValidateException("{*[LockedAccount]*}");
			}
			throw new OBPMValidateException("{*[core.user.password.error]*}"+",{*[if.zai.fail.login]*}"+(loginFailTimes-pwdErrorTimes)+" {*[ci.system.will.lock]*}");
		}
	}

	public WebUser getWebUserInstance(final String userid) throws Exception {
		WebUser tmp = (WebUser) _cache.get(userid);
		if (tmp != null) {
			return tmp;
		}
		final UserVO user = (UserVO) doView(userid);
		if (user != null) {
			tmp = new WebUser(user);
			_cache.put(userid, tmp);
			return tmp;
		}
		return null;
	}

	public UserVO login(final String no, final String domain) throws Exception {
		UserVO vo = null;
		try {
			vo = ((UserDAO) getDAO()).login(no, domain);
		} catch (final Exception ex) {
			if (ex.getMessage() != null && ex.getMessage().equals("Row does not exist")) {
				throw new OBPMValidateException("{*[core.user.notexist]*}");
			} else {
				throw ex;
			}
		}
		if (vo != null)
			_cache.remove(vo.getId());
		return vo;
	}

	public Collection<UserVO> getDatasByDept(String parent, final String domain) throws Exception {
		if (parent == null) {
			parent = "";
		}
		return ((UserDAO) getDAO()).getDatasByDept(parent, domain);
	}

	public Collection<UserVO> getDatasByDept(String parent) throws Exception {
		if (parent == null) {
			parent = "";
		}
		return ((UserDAO) getDAO()).getDatasByDept(parent);
	}

	public Collection<UserVO> getDatasByGroup(String parent, final String domain) throws Exception {
		if (parent == null) {
			parent = "";
		}
		return ((UserDAO) getDAO()).getDatasByRoleid(parent, domain);
	}

	/**
	 * 新的密码加密机制
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	private String encrypt(final String s) throws Exception {
		return Security.encryptPassword(s);
	}
	
	/**
	 * 新的密码解密机制
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	private String decrypt(final String s){
		return Security.decryptPassword(s);
	}

	/**
	 * 旧的密码加密机制(主要用于登录)
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	private String encryptOld(final String s) throws Exception {
		return StringUtil.left(Security.encodeToMD5(s), Framework.PASSWORD_LENGTH);
	}

	// @SuppressWarnings("unchecked")
	protected IDesignTimeDAO<UserVO> getDAO() throws Exception {
		return (UserDAO) DAOFactory.getDefaultDAO(UserVO.class.getName());
	}

	public void doPersonalUpdate(final ValueObject vo) throws Exception {
		final UserVO po = (UserVO) vo;
		final UserVO oldValue = (UserVO) getDAO().find(po.getId());
		po.setId(oldValue.getId());
		po.setDeveloper(oldValue.isDeveloper());
		po.setDomainAdmin(oldValue.isDomainAdmin());
		po.setDomainid(oldValue.getDomainid());
		po.setStatus(oldValue.getStatus());
		po.setSuperAdmin(oldValue.isSuperAdmin());
		po.setUserDepartmentSets(oldValue.getUserDepartmentSets());
		po.setUserRoleSets(oldValue.getUserRoleSets());
		po.setSuperior(oldValue.getSuperior());
		po.setField1(oldValue.getField1());
		po.setField2(oldValue.getField2());
		po.setField3(oldValue.getField3());
		po.setField4(oldValue.getField4());
		po.setField5(oldValue.getField5());
		po.setField6(oldValue.getField6());
		po.setField7(oldValue.getField7());
		po.setField8(oldValue.getField8());
		po.setField9(oldValue.getField9());
		po.setField10(oldValue.getField10());
		
		if(oldValue.getUserSetup() == null){
			UserSetupVO	usersetup = new UserSetupVO();
			//usersetup.setId(Sequence.getSequence());
			usersetup.setId(po.getId());
			oldValue.setUserSetup(usersetup);
		}
		if (po.getUserSetup() != null) {
			// 更新用户设置
			oldValue.getUserSetup().setPendingStyle(po.getUserSetup().getPendingStyle());
			oldValue.getUserSetup().setUserSkin(po.getUserSetup().getUserSkin());
			oldValue.getUserSetup().setUserStyle(po.getUserSetup().getUserStyle());
			oldValue.getUserSetup().setUseHomePage(po.getUserSetup().getUseHomePage());
			oldValue.getUserSetup().setGeneralPage(po.getUserSetup().getGeneralPage());
			oldValue.getUserSetup().setStatus(po.getUserSetup().getStatus());
		}
		po.setUserSetup(oldValue.getUserSetup());

		update(po);
		//PermissionPackage.clearCache();
	}

	public UserVO createUser(final UserVO user) throws Exception {
		doCreate(user);
		return user;
	}

	public ValueObject doView(final String pk) throws Exception {
		return getDAO().find(pk);
	}

	public Collection<UserVO> doQueryHasMail(final String domain) throws Exception {
		return ((UserDAO) getDAO()).queryHasMail(domain);
	}

	public boolean isEmpty() throws Exception {
		return ((UserDAO) getDAO()).isEmpty();
	}

	public DataPackage<UserVO> doQueryByRoleId(final String roleid) throws Exception {

		return ((UserDAO) getDAO()).queryByRoleId(roleid);
	}

	public Collection<UserVO> queryByDomain(final String domainid) throws Exception {
		return queryByDomain(domainid, 1, Integer.MAX_VALUE);
	}

	public Collection<UserVO> queryByDomain(final String domainid, final int page, final int line) throws Exception {
		return ((UserDAO) getDAO()).queryByDomain(domainid, page, line);
	}

	public Collection<UserVO> queryByProxyUserId(final String proxyid) throws Exception {
		return ((UserDAO) getDAO()).queryByProxyUserId(proxyid);
	}

	public DataPackage<UserVO> listUsers(final ParamsTable params, final WebUser user) throws Exception {
		return ((UserDAO) getDAO()).query(params, user);
	}

	public String getDefaultApplicationId(final String userid) throws Exception {
		final ApplicationProcess appProcess = (ApplicationProcess) ProcessFactory
				.createProcess(ApplicationProcess.class);
		final Collection<ApplicationVO> appList = appProcess.queryApplications(userid);
		if (!appList.isEmpty()) {
			final ApplicationVO application = appList.iterator().next();

			return application.getId();
		}
		return null;
	}

	public UserVO getUserByLoginno(final String loginno, final String domainid) throws Exception {
		return ((UserDAO) getDAO()).findByLoginno(loginno, domainid);
	}
	
	public UserVO getUserByLoginnoAndDoaminName(final String loginno, final String domainName) throws Exception {
		DomainDAO domainDAO = (DomainDAO) DAOFactory.getDefaultDAO(DomainVO.class.getName());
		final DomainVO domain = domainDAO.getDomainByName(domainName);
		if (domain == null || domain.getStatus() == 0) {
			throw new OBPMValidateException("{*[core.domain.notexist]*}, domain name is: " + domainName);
		}

		String hql = "FROM " + UserVO.class.getName() + " vo WHERE vo.loginno ='" + loginno + "'"
				+ " AND vo.domainid='" + domain.getId() + "'";

		return (UserVO) getDAO().getData(hql);
	}

	/**
	 * 深度查询获取用户树
	 * 
	 * @param cols
	 *            所有用户列表
	 * @param startNode
	 *            开始
	 * @param excludeNodeId
	 *            排除的节点ID
	 * @param deep
	 *            深度
	 * @return 树型用户列表
	 * @throws Exception
	 */
	public Map<String, String> deepSearchTree(final Collection<?> cols, final UserVO startNode,
			final String excludeNodeId, final int deep) throws Exception {
		final Map<String, String> list = new LinkedHashMap<String, String>();

		if (startNode != null) {
			list.put(startNode.getId(), startNode.getName());
		}

		final Iterator<?> iter = cols.iterator();
		while (iter.hasNext()) {
			final UserVO vo = (UserVO) iter.next();
			if (startNode == null) {
				if (vo.getSuperior() == null) {
					if (vo.getId() != null && !vo.getId().equals(excludeNodeId)) {
						final Map<String, String> tmp = deepSearchTree(cols, vo, excludeNodeId, deep + 1);
						list.putAll(tmp);
					}
				}
			} else {
				if (vo.getSuperior() != null && vo.getSuperior().getId().equals(startNode.getId())) {
					if (vo.getId() != null && !vo.getId().equals(excludeNodeId)) {
						final Map<String, String> tmp = deepSearchTree(cols, vo, excludeNodeId, deep + 1);
						list.putAll(tmp);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 深度查询获取用户树
	 * 
	 * @param cols
	 *            所有用户列表
	 * @param startNode
	 *            开始
	 * @param excludeNodeId
	 *            排除的节点ID
	 * @param deep
	 *            深度
	 * @return 树型用户列表
	 * @throws Exception
	*/
	public Collection<BaseUser> deepSearchTree2(Collection<?> cols,
			BaseUser startNode, String excludeNodeId, int deep) throws Exception{
		final Collection<BaseUser> list = new ArrayList<BaseUser>();
		
		if (startNode != null) {
			list.add(startNode);
		}

		final Iterator<?> iter = cols.iterator();
		while (iter.hasNext()) {
			final BaseUser vo = (BaseUser) iter.next();
			if (startNode == null) {
				if (vo.getSuperior() == null) {
					if (vo.getId() != null && !vo.getId().equals(excludeNodeId)) {
						final Collection<BaseUser> tmp = deepSearchTree2(cols, vo, excludeNodeId, deep + 1);
						list.addAll(tmp);
					}
				}
			} else {
				if (vo.getSuperior() != null && vo.getSuperior().getId().equals(startNode.getId())) {
					if (vo.getId() != null && !vo.getId().equals(excludeNodeId)) {
						final Collection<BaseUser> tmp = deepSearchTree2(cols, vo, excludeNodeId, deep + 1);
						list.addAll(tmp);
					}
				}
			}
		}
		return list;
	}
	/**
	 * 查询
	 */
	public DataPackage<UserVO> queryUsersExcept(final ParamsTable params, final WebUser user) throws Exception {
		return getDAO().query(params, user);
	}

	public DataPackage<UserVO> listLinkmen(final ParamsTable params) throws Exception {
		return ((UserDAO) getDAO()).listLinkmen(params);
	}

	public String queryUserIdsByName(final String username, final String domainid) throws Exception {
		final Collection<UserVO> list = ((UserDAO) getDAO()).queryUsersByName(username, domainid);
		StringBuffer namelist = new StringBuffer();
		for (final Iterator<UserVO> iter = list.iterator(); iter.hasNext();) {
			final UserVO vo = (UserVO) iter.next();
			namelist.append(vo.getId()).append(",");
		}
		if (namelist.toString().endsWith(",")) {
			String temp = namelist.substring(0, namelist.length() - 1);
			namelist.setLength(0);
			namelist.append(temp);
		}
		return namelist.toString();
	}

	public String findUserIdByAccount(final String account, final String domainid) throws Exception {
		final UserVO vo = getUserByLoginno(account, domainid);
		return vo==null?"":vo.getId();
	}

	public Collection<UserVO> queryByDepartment(final String deptId) throws Exception {
		String sql = "SELECT vo.* FROM " + getDAO().getSchema() + "T_USER" + " vo";
		sql += " WHERE vo.ID in (select s.USERID from " + getDAO().getSchema() + "T_USER_DEPARTMENT_SET s";
		sql += " WHERE s.DEPARTMENTID='" + deptId + "')";

		return (Collection<UserVO>) getDAO().getDatasBySQL(sql);
	}
	
	/**
	 *查询给定部门下的用户，并通过用户的是否公开个人信息属性筛选
	 * @param deptId
	 * 		部门id
	 * @param isUserinfoPublic
	 * 		是否公开个人信息
	 * @return
	 * @throws Exception
	 */
	public Collection<UserVO> queryByDepartment(final String deptId ,boolean isUserinfoPublic) throws Exception {
		String sql = "SELECT vo.* FROM " + getDAO().getSchema() + "T_USER" + " vo";
		sql += " WHERE vo.userInfopublic =1 and vo.ID in (select s.USERID from " + getDAO().getSchema() + "T_USER_DEPARTMENT_SET s";
		sql += " WHERE s.DEPARTMENTID='" + deptId + "') order by NAME_LETTER";

		return (Collection<UserVO>) getDAO().getDatasBySQL(sql);
	}

	// department info page click button to show user UnjoinedDeptlist and add
	// ---- dolly 2011-1-9
	public DataPackage<UserVO> queryOutOfDepartment(final ParamsTable params, final String deptid) throws Exception {
		final DepartmentVO dept = (DepartmentVO) DAOFactory.getDefaultDAO(DepartmentVO.class.getName()).find(deptid);
		String sql = "SELECT vo.* FROM " + getDAO().getSchema() + "T_USER" + " vo";
		sql += " WHERE vo.DOMAINID='" + dept.getDomain().getId() + "' AND vo.dimission=1 AND vo.ID not in (select s.USERID from "
				+ getDAO().getSchema() + "T_USER_DEPARTMENT_SET s";
		sql += " WHERE (s.USERID!='' or s.USERID!='null') and s.DEPARTMENTID='" + deptid + "')";
		return (DataPackage<UserVO>) getDAO().getDatapackageBySQL(sql, params, getPage(params), getPagelines(params));
	}

	public DataPackage<UserVO> queryOutOfRole(final ParamsTable params, final String roleid) throws Exception {
		return ((UserDAO) getDAO()).queryOutOfRole(params,roleid,getPage(params),getPagelines(params));
	}
	
	public DataPackage<UserVO> queryOutOfRoleByDomain(final ParamsTable params, final String roleid,String domainId) throws Exception {
		String sql = "SELECT vo.* FROM " + getDAO().getSchema() + "T_USER" + " vo";
		sql += " WHERE vo.domainid  = '"+domainId+"' and vo.ID not in (select s.USERID from " + getDAO().getSchema() + "T_USER_ROLE_SET s";
		sql += " WHERE s.USERID IS NOT NULL and s.ROLEID='" + roleid + "')";

		return (DataPackage<UserVO>) getDAO().getDatapackageBySQL(sql, params, getPage(params), getPagelines(params));
	}

	public int getPage(final ParamsTable params) {
		final String _currpage = params.getParameterAsString("_currpage");
		return (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
	}

	public int getPagelines(final ParamsTable params) {
		final String _pagelines = params.getParameterAsString("_pagelines");
		return (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : Integer.MAX_VALUE;

	}

	public void addUserToDept(final String[] userids, final String deptid) throws Exception {
		final UserProcess uerProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		final DepartmentVO dept = (DepartmentVO) DAOFactory.getDefaultDAO(DepartmentVO.class.getName()).find(deptid);
		if (dept != null) {
			// tempSet.addAll(developerSet);
			for (int i = 0; i < userids.length; i++) {
				final String userid = userids[i];
				final UserVO user = (UserVO) uerProcess.doView(userid);
				final UserDepartmentSet uds = new UserDepartmentSet(user.getId(), dept.getId());
				user.getUserDepartmentSets().add(uds);
				doUpdate(user);
			}
		}
	}

	public void addUserToRole(final String[] userids, final String roleid) throws Exception {
		final UserProcess uerProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		final RoleVO role = (RoleVO) DAOFactory.getDefaultDAO(RoleVO.class.getName()).find(roleid);
		if (role != null) {
			// tempSet.addAll(developerSet);
			for (int i = 0; i < userids.length; i++) {
				final String userid = userids[i];
				final UserVO user = (UserVO) uerProcess.doView(userid);
				final UserRoleSet uRs = new UserRoleSet(user.getId(), role.getId());
				user.getUserRoleSets().add(uRs);
				doUpdate(user);
			}
		}

	}

	public Collection<UserVO> queryByRole(final String roleId) throws Exception {
		String sql = "SELECT vo.* FROM " + getDAO().getSchema() + "T_USER" + " vo";
		sql += " WHERE vo.ID in (select s.USERID from " + getDAO().getSchema() + "T_USER_ROLE_SET s";
		sql += " WHERE s.ROLEID='" + roleId + "') ORDER BY NAME_LETTER,ORDERBYNO ASC";

		return getDAO().getDatasBySQL(sql);
	}

	public Collection<UserVO> queryByDptIdAndRoleId(final String deptId, final String roleId) throws Exception {
		String sql = "SELECT vo.* FROM " + getDAO().getSchema() + "T_USER" + " vo";
		sql += " WHERE vo.ID in (select s.USERID from " + getDAO().getSchema() + "T_USER_ROLE_SET s";
		sql += " WHERE s.ROLEID='" + roleId + "') and vo.ID in (select s.USERID from " + getDAO().getSchema()
				+ "T_USER_DEPARTMENT_SET s";
		sql += " WHERE s.DEPARTMENTID='" + deptId + "')";
		return getDAO().getDatasBySQL(sql);
	}

	public static void main(String[] args) throws Exception {
		Collection<UserVO> users = new UserProcessBean().getSuperiorList("11de-c13a-0cf76f8b-a3db-1bc87eaaad4c");
		for (Iterator<UserVO> it = users.iterator(); it != null && it.hasNext();) {
			System.out.println(">>>>>>>>>>>>>" + it.next().getName());
		}
	}

	public Collection<UserVO> doQueryByHQL(String hql) throws Exception {
		return ((UserDAO) getDAO()).queryByHQL(hql);
	}
	
	public DataPackage<UserVO> doQuerySmByHQL(String hql, ParamsTable params, int page,
			int lines) throws Exception{
		return ((UserDAO) getDAO()).querySmByHQL(hql, params, lines, lines);
	}

	public void updateUserLockFlag(String loginno, int lockFlag)
			throws Exception {
		PersistenceUtils.beginTransaction();
		((UserDAO) getDAO()).updateUserLockFlag(loginno, lockFlag);
		PersistenceUtils.commitTransaction();
	}
	
	/**
	 * 根据企业域以及应用查找用户
	 * @param domian
	 * 			企业域id
	 * @param applicationid
	 * 			应用id
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryByDomainAndApplicationWithHQL(String domain, String applicationid, ParamsTable params) throws Exception{
		return ((UserDAO) getDAO()).queryByDomainAndApplicationWithHQL(domain, applicationid, params);
	}
	
	/**
	 * 根据企业域、应用以及部门查找用户
	 * @param domain
	 * 			企业域id
	 * @param applicationid
	 * 			应用id
	 * @param departmentid
	 * 			部门id
	 * @return
	 * @throws Exception
	 */
	public Collection<UserVO> queryByDomainAndApplicationAndDeptWithHQL(String domain, String applicationid, String departmentid, ParamsTable params) throws Exception{
		return ((UserDAO) getDAO()).queryByDomainAndApplicationAndDeptWithHQL(domain, applicationid, departmentid, params);
	}

	public void doBatchCreate(Collection<UserVO> users) throws Exception {
		((UserDAO) getDAO()).doBatchCreate(users);
	}
	
	/**
	 * 用户选择框,查询用户
	 * @param params 参数
	 * @param user webuser
	 * @param request HttpServletRequest
	 * @param excludeId 排除的用户ID
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryForUserDialog(ParamsTable params,WebUser user,HttpServletRequest request,String excludeId) throws Exception{
		return ((UserDAO) getDAO()).queryForUserDialog(params,user,request,excludeId);
	}

	/**
	 * 用户选择框,根据部门查询用户
	 * @param params 参数
	 * @param user webuser
	 * @param request HttpServletRequest
	 * @param excludeId 排除的用户ID
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryByDeptForUserDialog(ParamsTable params,
			WebUser user, HttpServletRequest request, String excludeId)
			throws Exception {
		return ((UserDAO) getDAO()).queryByDeptForUserDialog(params,user,request,excludeId);
	}

	/**
	 * 用户选择框，根据角色获取用户
	 * @param params 参数
	 * @param user webuser
	 * @param request HttpServletRequest
	 * @param excludeId 排除的用户ID
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryByRoleForUserDialog(ParamsTable params,
			WebUser user, HttpServletRequest request, String excludeId)
			throws Exception {
		return ((UserDAO) getDAO()).queryByRoleForUserDialog(params,user,request,excludeId);
	}

	/**
	 * 获取指定部门所有用户数
	 * 
	 * @param dptid
	 *            部门ID
	 * @return 返回指定部门下的所有用户对象的集合
	 */
	public Long getCountsByDepartment(String deptId) throws Exception {
		return ((UserDAO) getDAO()).getCountsByDepartment(deptId);
	}
	
	
	/**
	 * 获取指定角色下所有用户数
	 * 
	 * @param dptid
	 *            部门ID
	 * @return 返回指定部门下的所有用户对象的集合
	 */
	public Long getCountsByRole(String roleId) throws Exception {
		return ((UserDAO) getDAO()).getCountsByRole(roleId);
	}
	
}
