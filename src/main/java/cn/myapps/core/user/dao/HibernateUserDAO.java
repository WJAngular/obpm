package cn.myapps.core.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.base.dao.HibernateSQLUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.domain.dao.DomainDAO;
import cn.myapps.core.domain.dao.HibernateDomainDAO;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserRoleSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;
import flex.messaging.util.URLDecoder;

public class HibernateUserDAO extends HibernateBaseDAO<UserVO> implements UserDAO {
	public HibernateUserDAO(String voClassName) {
		super(voClassName);
	}
     
	/**
	 * @SuppressWarnings hibernate API不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public UserVO login(String loginno, String domain) throws Exception {
		// String hql = "FROM " + _voClazzName + " as vo WHERE vo.loginno=:login
		// and vo.domainid=:domain";
		// Query query = currentSession().createQuery(hql);
		// query.setString("login", loginno);
		// query.setString("domainid", domain);
		Session session = currentSession();
		Criteria criteria = session.createCriteria(this._voClazzName);
		criteria = criteria.add(Expression.like("loginno", loginno).ignoreCase());
		criteria = criteria.add(Expression.like("domainid", domain));
		List<UserVO> list = criteria.list();
		if (list.isEmpty()) {
			return null;
		} else {
			return (UserVO) list.get(0);
		}
	}

	/**
	 * @SuppressWarnings hibernate API不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public UserVO login(String loginno) throws Exception {
		String hql = "FROM " + _voClazzName + " as vo WHERE vo.loginno=:login";
		Query query = currentSession().createQuery(hql);
		query.setString("login", loginno);

		List<UserVO> list = query.list();
		if (list.isEmpty()) {
			return null;
		} else {
			return (UserVO) list.get(0);
		}
	}

	/**
	 * find the value object by primary key
	 * 
	 * @SuppressWarnings hibernate API不支持泛型
	 * @param id
	 *            primary key
	 * @see cn.myapps.base.dao.IDesignTimeDAO#find(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public ValueObject find(String id) throws Exception {
		Session session = currentSession();
		ValueObject rtn = null;
		if (id != null && id.length() > 0) {
			String hql = "FROM " + _voClazzName + " WHERE id='" + id + "'";
			Query query = session.createQuery(hql);
			query.setFirstResult(0);
			query.setMaxResults(1);
			List result = query.list();

			if (!result.isEmpty()) {
				rtn = (ValueObject) result.get(0);
			}
		}
		return rtn;

	}

	public Collection<UserVO> getDatasByRoleid(String parent, String domain) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.userRoleSets.roleId like '%" + parent + "%'";

		ParamsTable params = new ParamsTable();
		if (domain != null)
			params.setParameter("domainId", domain);
		return getDatas(hql, params);
	}

	public Collection<UserVO> getDatasByDept(String parent, String domain) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.userDepartmentSets.departmentId = '" + parent
				+ "' and vo.domainid ='" + domain + "'";
		return getDatas(hql);
	}

	public Collection<UserVO> getDatasByDept(String parent) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.userDepartmentSets.departmentId = '" + parent + "'";
		return getDatas(hql);
	}

	public Collection<UserVO> queryHasMail(String domain) throws Exception {
		return queryHasMail(null, domain);
	}

	public Collection<UserVO> queryHasMail(String application, String domain) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.email IS NOT NULL and vo.email !=''";
		/*if (application != null && application.trim().length() > 0) {
			hql += " AND vo.applicationid='" + application + "'";
		}*/
		if (domain != null && domain.trim().length() > 0) {
			hql += " and vo.domainid = '" + domain + "'";
		}
		return getDatas(hql);
	}

	/**
	 * @SuppressWarnings hibernate API不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public boolean isEmpty() throws Exception {
		String hql = "FROM " + _voClazzName;
		Session session = currentSession();
		Query query = session.createQuery(hql);
		List rst = query.list();
		if (!rst.isEmpty()) {
			return false;
		} else
			return true;
	}

	public DataPackage<UserVO> queryByRoleId(String roleid) throws Exception {
		String hql = "FROM " + _voClazzName + " vo where vo.userRoleSets.roleId = '" + roleid + "'";
		return getDatapackage(hql, 1, Integer.MAX_VALUE);
	}

	/**
	 * @SuppressWarnings hibernate API不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public Collection<UserVO> queryByDomain(String domainid, int page, int line) throws Exception {
		String hql = "FROM " + _voClazzName + " vo";
		if (domainid != null && domainid.trim().length() > 0) {
			hql += " WHERE vo.domainid='" + domainid + "'";
		}
		Query query = currentSession().createQuery(hql);
		query.setFirstResult((page - 1) * line);
		query.setMaxResults(line);
		return query.list();
	}

	public UserVO findByLoginno(String account, String domainid) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.loginno ='" + account + "'" + " AND vo.domainid='"
				+ domainid + "'";
		return (UserVO) getData(hql);

	}

	public UserVO findByLoginnoAndDoaminName(String loginno, String domainName) throws Exception {
		DomainDAO domainDAO = new HibernateDomainDAO(DomainVO.class.getName());
		DomainVO domain = domainDAO.getDomainByName(domainName);
		return findByLoginno(loginno, domain.getId());
	}

	public DataPackage<UserVO> listLinkmen(ParamsTable params) throws Exception {
		String hql = "FROM " + _voClazzName + " vo where vo.telephone is not null";
		String _currpage = params.getParameterAsString("_currpage");
		String _pagelines = params.getParameterAsString("_pagelines");

		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : Integer.MAX_VALUE;

		return getDatapackage(hql, params, page, lines);
	}

	// @SuppressWarnings("unchecked")
	public Collection<UserVO> queryUsersByName(String username, String domainid) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.name = '" + username + "' AND vo.domainid='" + domainid
				+ "'";
		return getDatas(hql);
	}

	// @SuppressWarnings("unchecked")
	public Collection<UserVO> queryByProxyUserId(String proxyid) throws Exception {
		String hql = "FROM " + _voClazzName + " vo";
		if (proxyid != null && proxyid.trim().length() > 0) {
			hql += " WHERE vo.proxyUser='" + proxyid + "'";
		}

		return getDatas(hql);
	}

	/**
	 * 更新用户默认应用
	 * 
	 * @param userid
	 *            用户ID
	 * @param defaultApplicationid
	 *            默认选择应用ID
	 * @throws Exception
	 */
	public void updateDefaultApplication(String userid, String defaultApplicationid) throws Exception {
		Session session = currentSession();

		String hqlUpdate = "update " + _voClazzName
				+ " vo set vo.defaultApplication = :defaultApplication where vo.id = :id";
		session.createQuery(hqlUpdate).setString("defaultApplication", defaultApplicationid).setString("id", userid)
				.executeUpdate();
	}

	public Collection<UserVO> queryByHQL(String hql) throws Exception {
		
		return getDatas(hql);
	}
	
	public DataPackage<UserVO> querySmByHQL(String hql, ParamsTable params, int page,
			int lines) throws Exception {
		return getDatapackage(hql, params, page, lines);
	}

	public void updateUserLockFlag(String loginno, int lockFlag)
			throws Exception {
		Session session = currentSession();

		String hqlUpdate = "update " + _voClazzName
				+ " vo set vo.lockFlag = :lockFlag where vo.loginno = :loginno";
		session.createQuery(hqlUpdate).setInteger("lockFlag", lockFlag).setString("loginno", loginno)
				.executeUpdate();
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
		String hql = "";
		String sm_name = params.getParameterAsString("sm_name");

		Long page = params.getParameterAsLong("_currpage");
		int currentPage = page != null ? page.intValue() : 1;
		
		Long lines = params.getParameterAsLong("_pagelines");
		int pagelines = lines != null ? lines.intValue() : 10;
		
		if (domain.trim().length() > 0) {
			hql = "SELECT DISTINCT u " + "FROM " + UserVO.class.getName() + " u," + RoleVO.class.getName() + " r," + UserRoleSet.class.getName() + " rs "
					+ " WHERE u.domainid='" + domain + "' " + "AND r.id=rs.roleId AND u.id=rs.userId AND r.applicationid='" + applicationid + "' ";;
			if (!StringUtil.isBlank(sm_name)) {
				hql += " AND u.name like '%" + sm_name + "%' ";
			}
		}
		
		DataPackage<UserVO> datas = new DataPackage<UserVO>();
		datas.datas = queryByHQL(hql, currentPage, pagelines);
		datas.rowCount = getTotalLines(hql);
		
		return datas;
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
		String departid = params.getParameterAsString("departid");

		Long page = params.getParameterAsLong("_currpage");
		int currentPage = page != null ? page.intValue() : 1;
		
		Long lines = params.getParameterAsLong("_pagelines");
		int pagelines = lines != null ? lines.intValue() : 10;
		
		String hql = "SELECT DISTINCT u " + "FROM " + UserVO.class.getName() + " u," + RoleVO.class.getName() + " r," + UserRoleSet.class.getName() + " rs "
				+ " WHERE u.domainid ='" + domain + "'";
		if (domain != null && domain.trim().length() > 0) {
			if (departid != null && !"".equals(departid)) {
				hql += " AND u.userDepartmentSets.departmentId='" + departid
						+ "' ";
			}
		}
		
		hql += "AND r.id=rs.roleId AND u.id=rs.userId AND r.applicationid='" + applicationid + "' ";
		return queryByHQL(hql, currentPage, pagelines);
	}
	
	public void doBatchCreate(Collection<UserVO> users)throws Exception{
		if(users != null){
			PreparedStatement statement = null;
			Connection connection = currentSession().connection();
			try {
				connection.setAutoCommit(false);
				for (Iterator<UserVO> iterator = users.iterator(); iterator.hasNext();) {
					UserVO userVO = (UserVO) iterator.next();
					StringBuffer sql = new StringBuffer();
					sql.append("INSERT INTO T_USER (ID,CALENDAR,LEVELS,REMARKS,SUPERIOR,PROXYUSER,DEFAULTDEPARTMENT," +
							"DEFAULTAPPLICATION,EMAIL,LOGINNO,LOGINPWD,NAME,NAME_LETTER,TELEPHONE,STATUS,DOMAINID,ISDOMAINUSER," +
							"STARTPROXYTIME,ENDPROXYTIME,USEIM,ORDERBYNO,LASTMODIFYPASSWORDTIME,PASSWORDARRAY,LOCKFLAG," +
							"PUBLICKEY,DIMISSION,FIELD1,FIELD2,FIELD3,FIELD4,FIELD5,FIELD6,FIELD7,FIELD8,FIELD9,FIELD10)");
					sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					// Transfer Data
					System.out.println("sql="+sql);
					statement = connection.prepareStatement(sql.toString());
					setBaseParameters(0, statement, userVO);
					// Exec SQL
					statement.executeUpdate();
					
					Collection<UserDepartmentSet> userDepartmentSet = userVO.getUserDepartmentSets();
					if(userDepartmentSet != null && !userDepartmentSet.isEmpty()){
						for (Iterator<UserDepartmentSet> iter = userDepartmentSet.iterator(); iter.hasNext();) {
							UserDepartmentSet uds = (UserDepartmentSet) iter.next();
							StringBuffer sql1 = new StringBuffer();
							sql1.append("INSERT INTO T_USER_DEPARTMENT_SET (ID,DEPARTMENTID,USERID) VALUES(?,?,?)");
							// Transfer Data
							statement = connection.prepareStatement(sql1.toString());
							//set parameter
							statement.setString(1, uds.getId() == null ? Sequence.getSequence() : uds.getId());
							statement.setString(2, uds.getDepartmentId());
							statement.setString(3, uds.getUserId());
							// Exec SQL
							statement.executeUpdate();
						}
					}
				}
				connection.commit();
			} catch (Exception e) {
				connection.rollback();
				throw e;
			} finally {
				connection.close();
			}
		}
	}
	
	protected int setBaseParameters(int currentIndex,PreparedStatement statement,UserVO user)throws Exception {
		statement.setString(++currentIndex, user.getId() == null ? Sequence.getSequence() : user.getId());
		statement.setString(++currentIndex, user.getCalendarType());
		statement.setInt(++currentIndex, user.getLevel());
		statement.setString(++currentIndex, user.getRemarks());
		statement.setString(++currentIndex, user.getSuperior() != null ? user.getSuperior().getId() : null);
		statement.setString(++currentIndex, user.getProxyUser()!= null ? user.getProxyUser().getId() : null);
		statement.setString(++currentIndex, user.getDefaultDepartment());
		statement.setString(++currentIndex, user.getDefaultApplication());
		statement.setString(++currentIndex, user.getEmail());
		statement.setString(++currentIndex, user.getLoginno());
		statement.setString(++currentIndex, user.getLoginpwd());
		statement.setString(++currentIndex, user.getName());
		statement.setString(++currentIndex, user.getNameLetter());
		statement.setString(++currentIndex, user.getTelephone());
		statement.setInt(++currentIndex, user.getStatus());
		statement.setString(++currentIndex, user.getDomainid());
		statement.setString(++currentIndex, user.getDomainUser());
		statement.setTimestamp(++currentIndex, user.getStartProxyTime()!= null ? new Timestamp(user.getStartProxyTime().getTime()) : null);
		statement.setTimestamp(++currentIndex, user.getEndProxyTime() != null ? new Timestamp(user.getEndProxyTime().getTime()) : null);
		statement.setInt(++currentIndex, user.getUseIM()?1:0);
		statement.setInt(++currentIndex, user.getOrderByNo());
		statement.setTimestamp(++currentIndex, user.getLastModifyPasswordTime() != null ? new Timestamp(user.getLastModifyPasswordTime().getTime()) : null);
		statement.setString(++currentIndex, user.getPasswordArray());
		statement.setInt(++currentIndex, user.getLockFlag());
		statement.setString(++currentIndex, user.getPublicKey());
		statement.setInt(++currentIndex, user.getDimission());
		statement.setString(++currentIndex, user.getField1());
		statement.setString(++currentIndex, user.getField2());
		statement.setString(++currentIndex, user.getField3());
		statement.setString(++currentIndex, user.getField4());
		statement.setString(++currentIndex, user.getField5());
		statement.setString(++currentIndex, user.getField6());
		statement.setString(++currentIndex, user.getField7());
		statement.setString(++currentIndex, user.getField8());
		statement.setString(++currentIndex, user.getField9());
		statement.setString(++currentIndex, user.getField10());
		return currentIndex;
	}

	/**
	 * 用户选择框查询
	 * @param params 参数
	 * @param user webuser
	 * @param request HttpServletRequest
	 * @param excludeId 排除的用户ID
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> queryForUserDialog(ParamsTable params,
			WebUser user, HttpServletRequest request, String excludeId)
			throws Exception {
		String domain = params.getParameterAsString("domain");
		String sm_name = params.getParameterAsString("sm_name");

		Long page = params.getParameterAsLong("_currpage");
		int currentPage = page != null ? page.intValue() : 1;
		
		Long lines = params.getParameterAsLong("_pagelines");
		int pagelines = lines != null ? lines.intValue() : 10;
		String hql = "FROM " + UserVO.class.getName()
				+ " WHERE domainid='" + domain + "' AND dimission=1";
		if (!StringUtil.isBlank(sm_name)) {
			sm_name = URLDecoder.decode(sm_name);
			hql += " AND (name like '%" + sm_name + "%' OR loginno like '%" + sm_name + "%' OR telephone like '%" + sm_name + "%')";
		}
		if(!StringUtil.isBlank(excludeId)){
			hql += " AND id NOT IN ('" + excludeId + "')";
		}
		hql += biuldUserIdStr(params, user, request);
		hql += " ORDER BY orderByNo,id";
		
		return getDatapackage(hql, null, currentPage, pagelines);
	}
	
	/**
	 * 用户选择框，根据部门获取用户
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
		String departid = params.getParameterAsString("departid");
		String domain = params.getParameterAsString("domain");
		
		Long page = params.getParameterAsLong("_currpage");
		int currentPage = page != null ? page.intValue() : 1;
		
		Long lines = params.getParameterAsLong("_pagelines");
		int pagelines = lines != null ? lines.intValue() : 10;
		
        if((this.dialect.indexOf("SQLServer")==-1 && this.dialect.indexOf("Oracle")==-1 )){
        	String hql = "SELECT distinct u FROM " + UserVO.class.getName()
    				+ " u WHERE u.domainid ='" + domain + "' AND u.dimission=1";
    		if (domain != null && domain.trim().length() > 0) {
    			if (departid != null && !"".equals(departid)) {
    				hql += " AND u.userDepartmentSets.departmentId IN (FROM "+DepartmentVO.class.getName()+" WHERE indexCode like'%"+departid+"%')";
    			}
    		}
    		if(!StringUtil.isBlank(excludeId)){
    			hql += " AND u.id NOT IN ('" + excludeId + "')";
    		}
    		hql += biuldUserIdStr(params, user, request);
    		hql += " ORDER BY u.orderByNo,u.id";
    		return getDatapackage(hql, null, currentPage, pagelines);
        
        }else{
        	String sql = " SELECT DISTINCT (u.id ),u.ORDERBYNO, u.CALENDAR , u.DEFAULTAPPLICATION ,u.DEFAULTDEPARTMENT,u.DIMISSION,u.DOMAINID,u.EMAIL,u.EMAILPUBLIC,u.ENDPROXYTIME,u.FIELD1,u.FIELD10,u.FIELD2, "  +
		             " u.FIELD3,u.FIELD4,u.FIELD5,u.FIELD6,u.FIELD7,u.FIELD8,u.FIELD9,u.ISDOMAINUSER,u.LASTMODIFYPASSWORDTIME,u.LEVELS,u.LIAISON_OFFICER,u.LOCKFLAG,u.LOGINNO,u.LOGINPWD,u.NAME,u.PASSWORDARRAY, "  + 
		             " u.PERMISSION_TYPE,u.PROXYUSER,u.PUBLICKEY,u.REMARKS,u.STARTPROXYTIME,u.STATUS,u.NAME_LETTER,u.SUPERIOR,u.TELEPHONE,u.TELEPHONE2,u.TELEPHONEPUBLIC,u.TELEPHONEPUBLIC2 , u.USEIM,u.USERINFOPUBLIC, "  + 
		             //text类型转换为char类型
		             " CAST(u.AVATAR as CHAR(1000) ) as AVATAR, CAST(u.FAVORITE_CONTACTS as CHAR(1000) ) as FAVORITE_CONTACTS "  + 
		             " FROM t_user u "  +
		             " WHERE u.DOMAINID ='" + domain + "' AND u.DIMISSION=1";
				if (domain != null && domain.trim().length() > 0) {
					if (departid != null && !"".equals(departid)) {
						sql += " AND u.DEFAULTDEPARTMENT IN (SELECT td.ID as DEFAULTDEPARTMENT FROM  t_department td WHERE td.INDEXCODE like '%"+departid+"%')";
					}
				}
				if(!StringUtil.isBlank(excludeId)){
					sql += " AND u.id NOT IN ('" + excludeId + "')";
				}
	   		   sql += biuldUserIdStr(params, user, request);
			   sql += " ORDER BY u.orderByNo,u.id";
			   return getDatapackageBySQL(sql, null, currentPage, pagelines);
        }
		
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
		String rolesid = params.getParameterAsString("rolesid");
		Long page = params.getParameterAsLong("_currpage");
		int currentPage = page != null ? page.intValue() : 1;
		
		Long lines = params.getParameterAsLong("_pagelines");
		int pagelines = lines != null ? lines.intValue() : 10;
		
		String hql = "FROM " + UserVO.class.getName()
				+ " WHERE userRoleSets.roleId='" + rolesid
				+ "' AND domainid ='" + user.getDomainid()
				+ "' AND dimission=1";
		hql += biuldUserIdStr(params, user, request);
		hql += " ORDER BY orderByNo,id";
		
		return getDatapackage(hql, null, currentPage, pagelines);
	}
	
	private String biuldUserIdStr(ParamsTable params,
			WebUser webuser, HttpServletRequest request) throws Exception {
		StringBuffer w = new StringBuffer();
		String getToPerson = params.getParameterAsString("_isGetToPerson");//指定审批人
		String getApprover2SubFlow = params.getParameterAsString("_isGetApprover2SubFlow");//子流程节点选择审批人	
		String getCirculator = params.getParameterAsString("_isGetCirculator");//抄送人
		if ((!StringUtil.isBlank(getToPerson) && "true".equals(getToPerson))
				||(!StringUtil.isBlank(getApprover2SubFlow) && "true".equals(getApprover2SubFlow))) {
			String _flowId = params.getParameterAsString("_flowId");
			String _docId = params.getParameterAsString("_docId");
			String _nodeId = params.getParameterAsString("_nodeId");
			Collection<BaseUser> users = StateMachineHelper.getPrincipalList(
					_docId, webuser, _nodeId, request, _flowId);
			if (users != null && !users.isEmpty()) {
				w.append(" AND id in(");
				for (Iterator<BaseUser> iterator = users.iterator(); iterator
						.hasNext();) {
					BaseUser user = (BaseUser) iterator.next();
					w.append("'").append(user.getId()).append("',");
				}
				w.setLength(w.length() - 1);
				w.append(")");
			}
		}else if(!StringUtil.isBlank(getCirculator)
				&& "true".equals(getCirculator)){
			String _flowId = params.getParameterAsString("_flowId");
			String _docId = params.getParameterAsString("_docId");
			String _nodeId = params.getParameterAsString("_nodeId");
			Collection<BaseUser> users = StateMachineHelper.getCirculatorList(
					_docId, webuser, _nodeId, request, _flowId);
			if (users != null && !users.isEmpty()) {
				w.append(" AND id in(");
				for (Iterator<BaseUser> iterator = users.iterator(); iterator
						.hasNext();) {
					BaseUser user = (BaseUser) iterator.next();
					w.append("'").append(user.getId()).append("',");
				}
				w.setLength(w.length() - 1);
				w.append(")");
			}
		}
		return w.toString();
	}
	
	public DataPackage<UserVO> queryOutOfRole(ParamsTable params,
			String roleid, int page, int pagelines) throws Exception{
		String sql = "SELECT distinct vo.id FROM " + getSchema() + "T_USER" + " vo";
		sql += " , T_USER_DEPARTMENT_SET ds , T_DEPARTMENT de ";
		sql += " WHERE ds.USERID = vo.id and ds.DEPARTMENTID = de.id and vo.ID not in (select s.USERID from " + getSchema() + "T_USER_ROLE_SET s";
		sql += " WHERE s.USERID IS NOT NULL and s.ROLEID='" + roleid + "')";

		HibernateSQLUtils sqlUtil = new HibernateSQLUtils();

		String whereClause = sqlUtil.createWhere(params);
		if (whereClause != null && whereClause.trim().length() > 0) {
			int p = sql.toLowerCase().indexOf(" where ");

			sql = (p >= 0) ? sql = sql.substring(0, p) + " where " + whereClause + " and " + sql.substring(p + 7) : sql
					+ " where " + whereClause;
		}

		if (params != null) {
			String application = (String) ((ParamsTable) params) // 根据instace
					// 查询
					.getParameter("application");
			if (application != null && application.trim().length() > 0) {
				if (sql.toLowerCase().indexOf(" where ") != -1) {
					sql += " and applicationid='" + application + "'";
				} else {
					sql += " where applicationid='" + application + "'";
				}
			}
		}

		sql = "SELECT u.* FROM T_USER u WHERE u.id IN ( " + sql + " )";
		
		String orderBy = sqlUtil.createOrderBy(params);
		if (orderBy != null && orderBy.trim().length() > 0) {
			int p = sql.toLowerCase().indexOf(" order by ");

			sql = (p >= 0) ? sql.substring(0, p + 10) + orderBy + ", " + sql.substring(p + 10) : sql + " order by "
					+ orderBy;
		}
		
		DataPackage<UserVO> result = new DataPackage<UserVO>();
		result.rowCount = getTotalLinesBySQL(sql);
		result.pageNo = page;
		result.linesPerPage = pagelines;

		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		result.datas = getDatasBySQL(sql, page, pagelines);
		return result;
		
	}

	@Override
	public Long getCountsByDepartment(String deptId) throws Exception {
		String hql = " SELECT count(*) FROM " + _voClazzName + " vo where vo.userDepartmentSets.departmentId = :deptId and dimission = 1 and status = 1 and userInfoPublic = true";
		Query query = currentSession().createQuery(hql).setString("deptId", deptId);
		return (Long) query.uniqueResult();
	}

	@Override
	public Long getCountsByRole(String roleId) throws Exception {
		String hql = " SELECT count(*) FROM " + _voClazzName + " vo where vo.userRoleSets.roleId = :roleId and dimission = 1 and status = 1 and userInfoPublic = true";
		Query query = currentSession().createQuery(hql).setString("roleId", roleId);
		return (Long) query.uniqueResult();
	}
}
