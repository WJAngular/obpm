package cn.myapps.core.domain.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentProcessBean;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationProcessBean;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.dao.DomainDAO;
import cn.myapps.core.permission.ejb.PermissionPackage;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleProcessBean;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.sysconfig.ejb.LdapConfig;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserProcessBean;
import cn.myapps.core.user.ejb.UserRoleSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.support.weixin.WeixinServiceProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.EhcacheProvider;
import cn.myapps.util.cache.MyCacheManager;
import cn.myapps.util.property.PropertyUtil;
import cn.myapps.util.sequence.Sequence;

public class DomainProcessBean extends AbstractDesignTimeProcessBean<DomainVO> implements DomainProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4350448235990331151L;

	public void doCreate(ValueObject vo) throws Exception {
		doCreate(vo,true);
	}
	
	public void doCreate(ValueObject vo,boolean isCreateDeps) throws Exception {
		super.doCreate(vo);
		
		if(isCreateDeps){
		// 创建一个部门
			DomainVO domain = (DomainVO) vo;
			DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
			DepartmentVO depVO = new DepartmentVO();
			depVO.setId(Sequence.getSequence());
			depVO.setName(domain.getName());
			depVO.setApplicationid(domain.getApplicationid());
			depVO.setCode("00");// 为默认的code的00
			depVO.setDomain(domain);
			depVO.setLevel(0);
			depVO.setDomainid(domain.getId());
			depVO.setIndexCode(depVO.getId());
			process.doCreate(depVO);
		}

		PermissionPackage.clearCache();
	}

	public void doRemove(String pk) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			// 检查是否有下属部门
			DomainVO tempDomain = (DomainVO) ((DomainDAO) getDAO()).find(pk);
			if (tempDomain != null && tempDomain.getDepartments().size() > 0) {
				throw new OBPMValidateException("{*[core.domain.department.hassub]*}",new DomainException("{*[core.domain.department.hassub]*}"));
			}
			if (tempDomain != null && tempDomain.getApplications().size() > 0) {
				throw new OBPMValidateException("{*[core.domain.hasapp]*}",new DomainException("{*[core.domain.hasapp]*}"));
			}

			if (tempDomain != null) {
				UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				Collection<UserVO> cols = process.queryByDomain(tempDomain.getId(), 1, 10);
				if (cols != null && cols.size() > 0) {
					throw new OBPMValidateException("{*[core.domain.user.hassub]*}",new DomainException("{*[core.domain.user.hassub]*}"));
				}
				tempDomain.setUsers(null);
				tempDomain.setApplications(null);
				// Update(tempDomain);
				super.doRemove(pk);
				PersistenceUtils.commitTransaction();
				PermissionPackage.clearCache();
			}
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
		try {
			PersistenceUtils.beginTransaction();
			DomainVO domain = (DomainVO) vo;
			DomainVO doo = (DomainVO) getDAO().find(vo.getId());
			String smswd = domain.getSmsMemberPwd();
			if (smswd != null && !smswd.trim().equals(doo.getSmsMemberPwd())
					&& !smswd.trim().equals(Web.DEFAULT_SHOWPASSWORD)) {
				domain.setSmsMemberPwd(smswd);
			} else {
				domain.setSmsMemberPwd(doo.getSmsMemberPwd());
			}

			getDAO().update(vo);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	protected IDesignTimeDAO<DomainVO> getDAO() throws Exception {
		return (DomainDAO)DAOFactory.getDefaultDAO(DomainVO.class.getName());
	}

	public DomainVO getDomainByName(String tempname) throws Exception {
		return ((DomainDAO) getDAO()).getDomainByName(tempname);
	}

	public Collection<DomainVO> queryDomains(String userid, int page, int line) throws Exception {
		return ((DomainDAO) getDAO()).queryDomains(userid, page, line);
	}

	public DataPackage<DomainVO> queryDomainsByManager(String manager, int page, int line) throws Exception {
		return ((DomainDAO) getDAO()).queryDomainsByManager(manager, page, line);
	}

	public Collection<DomainVO> getAllDomain() throws Exception {
		return ((DomainDAO) getDAO()).getAllDomain();
	}
	
	public Collection<DomainVO> getDomainByStatus(int status) throws Exception {
		return ((DomainDAO) getDAO()).getDomainByStatus(status);
	}

	public DataPackage<DomainVO> queryDomainsByManagerAndName(String managerName, String name, int page, int line)
			throws Exception {

		return ((DomainDAO) getDAO()).queryDomainsbyManagerAndName(managerName, name, page, line);
	}

	public DataPackage<DomainVO> queryDomainsByName(String name, int page, int line) throws Exception {

		return ((DomainDAO) getDAO()).queryDomainsByName(name, page, line);
	}

	public DataPackage<DomainVO> queryDomainsbyManagerLoginnoAndName(String manager, String name, int page, int line)
			throws Exception {
		return ((DomainDAO) getDAO()).queryDomainsbyManagerLoginnoAndName(manager, name, page, line);
	}

	public DomainVO getDomainByDomainName(String name) throws Exception {
		return ((DomainDAO) getDAO()).getDomainByName(name);
	}
	
	public Collection<DomainVO> queryDomainsByStatus(int status) throws Exception {
		return ((DomainDAO) getDAO()).queryDomainsByStatus(status);
	}

	/**
	 * 企业域同步LDAP部门及用户信息
	 */
	public void synchLDAP(LdapContext ldapContext, DomainVO domain) throws Exception{
		try {
			PersistenceUtils.beginTransaction();
			doSynchDeptByLDAP(ldapContext, domain);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			throw e;
		}
		doSynchUserByLDAP(ldapContext, domain);
		EhcacheProvider cacheProvider = (EhcacheProvider) MyCacheManager.getProviderInstance();
		cacheProvider.clearAll();
	}
	
	/**
	 * 同步部门
	 * @param ldapContext
	 * @param domain
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void doSynchDeptByLDAP(LdapContext ldapContext, DomainVO domain) throws Exception{
		PropertyUtil.reload("sso");
		String enterDept = PropertyUtil.get(LdapConfig.ENTERDEPT);
		String deptClass = PropertyUtil.get(LdapConfig.DEPTCALSS);
		String dnKey = "distinguishedName";
		
		DepartmentProcess process = new DepartmentProcessBean();
		
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		NamingEnumeration results = ldapContext.search(enterDept, "objectClass=" + deptClass, constraints);
		
		while(results !=null && results.hasMore()){
			DepartmentVO department = new DepartmentVO();
			DepartmentVO superior = new DepartmentVO();
			int level = -1;
			SearchResult result = (SearchResult) results.next();
			
			
			if(StringUtil.isBlank(result.getName())){
				Collection<DepartmentVO> superiors = process.getDepartmentByLevel(0, "", domain.getId());
				Iterator<DepartmentVO> it = superiors.iterator();
				while(it.hasNext()){
					superior = it.next();
				}
				level = 1;
			}else{
				String dn = result.getAttributes().get(dnKey) != null ? (String)result.getAttributes().get(dnKey).get() : null;
				String[] dns = null;
				if(dn != null){
					dns = dn.split(",");
				}
				String superiorName = dns[1].substring(dns[1].indexOf("=") + 1);
				Collection<DepartmentVO> superiors = process.getDepartmentByName(superiorName, domain.getId());
				Iterator<DepartmentVO> it = superiors.iterator();
				while(it.hasNext()){
					DepartmentVO _superior = it.next();
					if(_superior.getLevel() != 0){
						superior = _superior;
					}
				}
				level = superior.getLevel() + 1;
			}
			
			String name = result.getAttributes().get("name") != null ? (String)result.getAttributes().get("name").get() : null;
			
			Collection<DepartmentVO> departments = process.getDepartmentByName(name, domain.getId());
			String _id = "";
			if(departments.size() > 0){
				Iterator<DepartmentVO> it = departments.iterator();
				while(it.hasNext()){
					DepartmentVO _department = it.next();
					_id = _department.getId();
				}
				
				department.setId(_id);
				department.setName(name);
				department.setDomain(domain);
				department.setDomainid(domain.getId());
				department.setSuperior(superior);
				department.setLevel(level);
				
				process.doUpdate(department);
			}else{
				department.setName(name);
				department.setDomain(domain);
				department.setDomainid(domain.getId());
				department.setSuperior(superior);
				department.setLevel(level);
				
				process.doCreate(department);
			}
		}
	}
	
	/**
	 * 同步用户
	 * @param ldapContext
	 * @param domain
	 * @throws Exception
	 */
	private void doSynchUserByLDAP(LdapContext ldapContext,
			DomainVO domain) throws Exception{
		Collection<UserVO> createlist = new ArrayList<UserVO>();//需要创建的用户集合
		Collection<UserVO> updatelist = new ArrayList<UserVO>();//需要更新的用户集合
		DepartmentVO department = new DepartmentVO();
		String enterDept = PropertyUtil.get(LdapConfig.ENTERDEPT);
		String userClass = PropertyUtil.get(LdapConfig.USERCLASS);
		String mailKey = PropertyUtil.get(LdapConfig.EMAIL);
		String userNameKey = PropertyUtil.get(LdapConfig.NAME);
		String telKey = PropertyUtil.get(LdapConfig.TELEPHONE);
		String loginnoKey = PropertyUtil.get(LdapConfig.LOGINNO);
		String password = "123456";
		String dnKey = "distinguishedName";
		
		DepartmentProcess departmentProcess = new DepartmentProcessBean();
		ApplicationProcess applicationProcess = new ApplicationProcessBean();
		RoleProcess roleProcess = new RoleProcessBean();
		UserProcess userProcess = new UserProcessBean();
		
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		NamingEnumeration<SearchResult> results = ldapContext.search(enterDept, "objectClass=" + userClass, constraints);
		
		while(results.hasMore()){
			SearchResult result = results.next();
			Attributes attributes = result.getAttributes();
			
			String loginno = attributes.get(loginnoKey) != null ? (String)attributes.get(loginnoKey).get() : null;
			if(!StringUtil.isBlank(loginno)){
				loginno = loginno.substring(0, loginno.indexOf("@"));
			}
			String userName = attributes.get(userNameKey) != null ? (String)attributes.get(userNameKey).get() : null;
			String mail = attributes.get(mailKey) != null ? (String)attributes.get(mailKey).get() : null;
			String telephoneNO = attributes.get(telKey) != null ? (String)attributes.get(telKey).get() : null;
			String dn = attributes.get(dnKey) != null ? (String)attributes.get(dnKey).get() : null;
			String[] dns = null;
			if(dn != null){
				dns = dn.split(",");
			}
			String deptName = dns[1].substring(dns[1].indexOf("=") + 1);
			
			Collection<DepartmentVO> departments = departmentProcess.getDepartmentByName(deptName, domain.getId());
			Iterator<DepartmentVO> it = departments.iterator();
			while(it.hasNext()){
				department = it.next();
			}
			UserVO _user = userProcess.getUserByLoginno(loginno, domain.getId());
			if(_user != null){
				UserDepartmentSet userDepartmentSet = new UserDepartmentSet(_user.getId(), department.getId());
				_user.getUserDepartmentSets().clear();
				_user.getUserDepartmentSets().add(userDepartmentSet);
				
				_user.setLoginno(loginno);
				_user.setLoginpwd(password);
				_user.setDomainid(domain.getId());
				_user.setEmail(mail);
				_user.setName(userName);
				_user.setTelephone(telephoneNO);
				_user.setDepartments(departments);
				_user.setDefaultDepartment(department.getId());
				//userProcess.doUpdate(_user);
				updatelist.add(_user);
			}else{
				UserVO user = new UserVO();
				user.setId(Sequence.getSequence());
				UserDepartmentSet userDepartmentSet = new UserDepartmentSet(user.getId(), department.getId());
				Collection<UserDepartmentSet> userDepartmentSets = new HashSet<UserDepartmentSet>();
				userDepartmentSets.add(userDepartmentSet);
				
				user.setLoginno(loginno);
				user.setLoginpwd(password);
				user.setDomainid(domain.getId());
				user.setEmail(mail);
				user.setName(userName);
				user.setTelephone(telephoneNO);
				user.setDepartments(departments);
				user.setDefaultDepartment(department.getId());
				user.setUserDepartmentSets(userDepartmentSets);
				
				//设置系统默认角色
				if(user.getUserRoleSets().size() == 0){
					Collection<ApplicationVO> apps = applicationProcess.queryAppsByDomain(domain.getId(), 0, 100);
					for(Iterator<ApplicationVO> app_its = apps.iterator(); app_its.hasNext();){
						ApplicationVO app = app_its.next();
						if(app.isActivated()){
							Collection<RoleVO> roles = roleProcess.getDefaultRolesByApplication(app.getApplicationid());
							if(roles != null && roles.size() > 0 ){
								for(Iterator<RoleVO> role_it = roles.iterator(); role_it.hasNext();){
									RoleVO role = role_it.next();
									UserRoleSet set = new UserRoleSet(user.getId(), role.getId());
									user.getUserRoleSets().add(set);
								}
							}
						}
					}
				}
				
				//userProcess.doCreate(user);
				createlist.add(user);
			}
		}
		try {
			//create users
			userProcess.doBatchCreate(createlist);
			
			//update users
			PersistenceUtils.beginTransaction();
			for (Iterator<UserVO> iterator = updatelist.iterator(); iterator.hasNext();) {
				UserVO userVO = (UserVO) iterator.next();
				userProcess.doUpdate(userVO);
			}
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			try {
				PersistenceUtils.rollbackTransaction();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			throw e;
		}
	}
	
	public Collection<DomainVO> queryDomainsByStatusAndUser(int status,
			String userid) throws Exception {
		return ((DomainDAO) getDAO()).getDomainByStatusAndUser(status,userid);
	}

	/* (non-Javadoc)
	 * @see cn.myapps.core.domain.ejb.DomainProcess#synchFromWeixin(cn.myapps.core.domain.ejb.DomainVO)
	 */
	public void synchFromWeixin(DomainVO domain) throws Exception {
		WeixinServiceProxy.synchFromWeixin(domain);
	}

	public void synch2Weixin(DomainVO domain) throws Exception {
		WeixinServiceProxy.synch2Weixin(domain);
	}

	@Override
	public JSONObject excelImportToDomain(DomainVO domain, File excelFile) throws Exception {
		
		DepartmentProcess deptProcess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
		DepartmentVO root = deptProcess.getRootDepartmentByApplication("", domain.getId());
		
		CalendarProcess calendarProcess = (CalendarProcess)ProcessFactory.createProcess(CalendarProcess.class);
		List<CalendarVO> calendars = (List<CalendarVO>) calendarProcess.doQueryByHQL("from "+CalendarVO.class.getName()+" vo where vo.name='Standard_Calendar' and vo.domainid='"+domain.getId()+"'", 1, 1);
		CalendarVO calendar = calendars.isEmpty()? null : calendars.get(0);
		
		
		JSONArray userList = new JSONArray();   //用户容器
		JSONArray deptList = new JSONArray();   //部门容器
		JSONArray errorsList = new JSONArray(); //提示信息容器
		JSONObject result = new JSONObject() ;  //结果集
		Map<String,Integer> general = new HashMap<String,Integer>() ;  //综合信息
		general.put("USER_ADD_SUCCESS", 0);
		general.put("DEPT_ADD_SUCCESS", 0);
		general.put("USER_ADD_FAIL", 0);
		general.put("DEPT_ADD_FAIL", 0);
		
		Map<String,DepartmentVO> deptsMap = new HashMap<String,DepartmentVO>();  //部门集合

		
		//第一步：获取数据
		getDatasFromExcel(userList,deptList,errorsList,general,root,excelFile); //excel导入
		
		//第二步：添加部门
		addDeptByDeptJsonArray(general,deptsMap,errorsList,deptList,domain);
		
		//第三步：添加用户
		JSONArray detail = addUserByUserJsonArray(general,deptsMap,userList,errorsList,domain,calendar);
				
		//第四步：整合结果集
		result.put("general", JSONObject.fromObject(general));
		result.put("detail", detail);
		
		return result;
	}
	
	 /**
     * 
     * @param userList
     *                用户数据集合
     * @param deptList
     *                部门数据集合
     * @param errorsList
     *                错误数据集合
     * @param general
     *                结果集数据
       "general": {"DEPT_ADD_FAIL": 0,"DEPT_ADD_SUCCESS": 0,"USER_ADD_SUCCESS": 0,"USER_ADD_FAIL": 0}
     *
     * @param root
     *                根节点
     * @param userExcel
     *                EXCEL文件
     * @throws Exception
     */
	private void getDatasFromExcel(JSONArray userList, JSONArray deptList,JSONArray errorsList, Map<String, Integer> general,DepartmentVO root, File userExcel) throws Exception {
		
		//数据容器
		JSONObject user ;
		JSONObject dept;
		JSONObject error ;
		JSONArray  errorArray;
		
		Map<String,JSONObject> userMap ;
		
		Workbook workbook = WorkbookFactory.create(new FileInputStream(userExcel));
		
		Sheet sheet = workbook.getSheetAt(0);
		
		if(sheet.getPhysicalNumberOfRows() > 2){
			userMap = new HashMap<String,JSONObject>();
			for(int k = 2; k < sheet.getPhysicalNumberOfRows(); k++){
				
				 user  = new JSONObject();
				 dept = new JSONObject();
				 error = new JSONObject();
				 errorArray = new JSONArray();
				
				 Row row = sheet.getRow(k);
				 
				user.put("ROWNUM", k+1);
				 
				String name = "" ;   //名称校验
				try {
					name= row.getCell(0).getStringCellValue() ;
				} catch (Exception e1) {
					errorArray.add("用户名称不能为空");
				}
				user.put("NAME", name);
				
				String loginno = "" ;  //账号校验 
				try {
					loginno= row.getCell(1).getStringCellValue() ;
				} catch (Exception e1) {
					errorArray.add("账号不能为空");
				}
				user.put("LOGINNO", loginno);
				
		
				String telephone = "";   // 电话校验
				try {
					telephone = row.getCell(2).getStringCellValue();
				} catch (Exception e) {
					try {
						double temMobile = row.getCell(2).getNumericCellValue();//读取大数据值的方式
						telephone = BigDecimal.valueOf(temMobile).toString();
					} catch (Exception e1) {
						
					}
				}
				user.put("TELEPHONE", telephone);
			
				String email = "" ;  //邮箱校验 
				try {
					email= row.getCell(3).getStringCellValue();
				} catch (Exception e1) {
					
				}
				user.put("EMAIL", email);
				
				String depts = null ;  //部门 
				try {
					depts= row.getCell(4).getStringCellValue();
				} catch (Exception e1) {
					
				}
				
				//获取部门数据
				dept = getDeptJSONObject(general,errorArray,root,depts);
				
				user.put("depts",depts);
				
				if (userMap.containsKey(loginno)) {
					errorArray.add("账户名重复");
				} else {
					if (errorArray.size() == 0) {
						userMap.put(loginno, user);
					}
				}

				//获取所有错误列表
				error.put("error", errorArray);

				userList.add(user);
				deptList.add(dept);
				errorsList.add(error);
			}
		}
		
	}
	
	/**
	 * 
	 * @param errorArray
	 *                 错误列表
	 * @param root
	 *                 根目录
	 * @param deptStr
	 *                 部门
	 * @return
	 */

	private JSONObject getDeptJSONObject(Map<String,Integer> general,JSONArray errorArray, DepartmentVO root, String deptStr) {

		JSONObject deptJSONObject = new JSONObject();
		JSONArray deptJSONArray = new JSONArray();
		Boolean flag = true ;
		if (deptStr != null && deptStr != "") {
			
			String[] depts = deptStr.split(";");
			for (String dept : depts) {
				/**
				 * 部门字段进行校验（1、根目录一致 ； 2、部门结构一致）
				 */
				//根目录校验
				int index = dept.indexOf("/");
			    String Root = index > -1 ? dept.substring(0, index) : dept;
			    
			    if(root != null && !Root.equals(root.getName())){
			    	flag = false ; 
			    	break ;
			    }
			   
			    //部门结构校验
			    String regEx = "^[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+(/[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+)*$";
			    Pattern pattern = Pattern.compile(regEx);
			    Matcher matcher = pattern.matcher(dept);
			   
			    if(!matcher.matches()){ 
			    	flag = false ;
			    	break ;
			    }
				deptJSONArray.add(dept);
			}
		}else{
			flag = false ;
		}
		
		if(flag){
			deptJSONObject.put("depts", deptJSONArray);
		}else{
			deptJSONObject.put("depts", "[]");
			errorArray.add("部门格式不正确");
			
			int num = general.get("DEPT_ADD_FAIL");
			num ++ ;
			general.put("DEPT_ADD_FAIL", num);
		}

		return deptJSONObject;
	}
	
	 /**
     *  @param general
     *              结果集数据
                    "general": {"DEPT_ADD_FAIL": 0,"DEPT_ADD_SUCCESS": 0,"USER_ADD_SUCCESS": 0,"USER_ADD_FAIL": 0}   
     * @param deptsMap
     *              部门Map
     * @param deptList
     *               部门数据集合
     * @param errorsList
     *               错误数据集合
     * @param domain
     */
	private void addDeptByDeptJsonArray(Map<String, Integer> general,Map<String, DepartmentVO> deptsMap,JSONArray errorList, JSONArray deptList, DomainVO domain) {

		int size = deptList.size();

		//第一步：创建部门
		for (int i = 0; i < size; i++) {
			JSONObject error = errorList.getJSONObject(i);
			JSONArray errors = error.getJSONArray("error");
			if(error.getJSONArray("error").isEmpty()){  // 在用户没有任何问题下添加部门
				
				JSONObject depts = deptList.getJSONObject(i);
				JSONArray deptJSONArray = depts.getJSONArray("depts");

				int deptSize = deptJSONArray.size();
				for (int j = 0; j < deptSize; j++) {
					String deptStr = deptJSONArray.get(j).toString();
					String orginStr = deptStr;
					if(!deptsMap.containsKey(deptStr)){
						try {
							createDept(general,deptsMap,deptStr, orginStr ,null, 0, domain);
						} catch (Exception e) {
							errors.add("创建部门失败");
							//1、部门集合中去除对应的键值对
							deptsMap.remove(deptStr);
							//2、结果集更新数据
							int num = general.get("DEPT_ADD_FAIL");
							num ++ ;
							general.put("DEPT_ADD_FAIL", num);
						}
					}
				}
			}
		}
	}
	/**
	 * 
	 * @param general
	 *          结果集数据
                "general": {"DEPT_ADD_FAIL": 0,"DEPT_ADD_SUCCESS": 0,"USER_ADD_SUCCESS": 0,"USER_ADD_FAIL": 0}   
	 * @param deptsMap
	 *           部门Map
	 * @param deptStr
	 *           切割后的部门字段，以/划分
	 * @param orginStr
	 *           部门字段
	 * @param Superior
	 *           上级部门
	 * @param level
	 *           级别
	 * @param domain
	 *           企业域
	 * @throws Exception
	 */

	private void createDept(Map<String, Integer> general,Map<String, DepartmentVO> deptsMap,String deptStr, String orginStr, DepartmentVO Superior, int level,DomainVO domain) throws Exception {

		DepartmentProcess dp = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);

		int pos = deptStr.indexOf("/");
		if (pos != -1) { // 存在层级结构
			String deptName = deptStr.substring(0, pos);
			String subDept = deptStr.substring(pos + 1, deptStr.length());
			DepartmentVO deptVO = null;
			Collection<DepartmentVO> _depts = dp.getDepartmentByName(deptName,domain.getId());

			Boolean createDeptflag = true;

		   //判断是否存在该部门
		   for (DepartmentVO _dept : _depts) {
				if (_dept.getLevel() == level && _dept.getSuperior() == Superior) {
					createDeptflag = false;
					deptVO = _dept;
					break;
				}
			}	
			if (createDeptflag) {
				// 添加相应的属性
				deptVO = new DepartmentVO();
				deptVO.setName(deptName);
				deptVO.setLevel(level);
				deptVO.setDomain(domain);
				deptVO.setSuperior(Superior);
				dp.doCreateOrUpdate(deptVO);
				
				//结果集添加相关信息
				int num = general.get("DEPT_ADD_SUCCESS");
				num ++ ;
				general.put("DEPT_ADD_SUCCESS", num);
			}
			
			level++;
			createDept(general ,deptsMap,subDept, orginStr, deptVO, level, domain);
		} else { // 不存在层级结构
			
			String deptName = deptStr;
			DepartmentVO deptVO = null;

			Collection<DepartmentVO> _depts = dp.getDepartmentByName(deptName,domain.getId());

			Boolean createDeptflag = true;

			//判断是否存在该部门
			for (DepartmentVO _dept : _depts) {
				if (_dept.getLevel() == level && _dept.getSuperior() == Superior) {
					createDeptflag = false;
					deptVO = _dept;
					break;
				}
			}
			if (createDeptflag) {
				// 添加相应的属性
				deptVO = new DepartmentVO();
				deptVO.setName(deptName);
				deptVO.setLevel(level);
				deptVO.setDomain(domain);
				deptVO.setSuperior(Superior);
				dp.doCreateOrUpdate(deptVO);
			}
			
			//结果集添加相关信息
			int num = general.get("DEPT_ADD_SUCCESS");
			num ++ ;
			general.put("DEPT_ADD_SUCCESS", num);

			deptsMap.put(orginStr, deptVO);
		}
	}
	
	/**
     * 
     * @param general
     *              结果集数据
                    "general": {"DEPT_ADD_FAIL": 0,"DEPT_ADD_SUCCESS": 0,"USER_ADD_SUCCESS": 0,"USER_ADD_FAIL": 0}   
     * @param deptsMap
     *              部门Map
     * @param userList
     *               用户数据集合
     * @param errorsList
     *               错误数据集合
     * @param domain
     *               企业域
     * @param calendar
     *               日历
     * @return
     * @throws Exception
     */
	private JSONArray addUserByUserJsonArray(Map<String,Integer> general,Map<String, DepartmentVO> deptsMap, JSONArray userList, JSONArray errorsList, DomainVO domain,CalendarVO calendar ) throws Exception {
		
		UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);

		JSONArray detail = new JSONArray();
		int size = userList.size();
		UserVO  _user ;
		for (int i = 0; i < size; i++) {
			JSONObject error = errorsList.getJSONObject(i);
			JSONArray errors = error.getJSONArray("error");
			if(errors.isEmpty()){  //  无任何问题下，新增用户
				
				JSONObject user = userList.getJSONObject(i);
				
				//判断用户是否存在
				String LoginNo = user.getString("LOGINNO");
				_user = process.getUserByLoginno(LoginNo, domain.getId());
				
				if(_user == null){   //用户不存在
					_user = new UserVO();
					_user.setId(Sequence.getSequence());       //id
					_user.setName(user.getString("NAME"));     //名称
					_user.setCalendarType(calendar !=null? calendar.getId() : null); //日历
					_user.setEmail(user.getString("EMAIL"));   //邮箱
					_user.setLoginno(user.getString("LOGINNO"));//账号
					_user.setLoginpwd("123456");               //密码 
					_user.setTelephone(user.getString("TELEPHONE")); //手机
					_user.setDomainid(domain.getId());         //企业域
					_user.setDimission(UserVO.ONJOB);          //启用
					
					//设置部门
					String depts = user.getString("depts");
					String[] deptsArray = depts.split(";");
					DepartmentVO deptVO = null ;
					for(String _dept : deptsArray){
						deptVO = deptsMap.get(_dept);
						UserDepartmentSet set = new UserDepartmentSet(_user.getId(), deptVO.getId());
						_user.getUserDepartmentSets().add(set);
						_user.getDepartments().add(deptVO);
					}
					
				
					_user.setDefaultDepartment(deptVO.getId());  	//默认部门
					
					//设置默认角色
					RoleProcess rp = (RoleProcess) ProcessFactory.createProcess(RoleProcess.class);
					Collection<ApplicationVO> apps = domain.getApplications();
					
					for(Iterator<ApplicationVO> app_its = apps.iterator(); app_its.hasNext();){
						ApplicationVO app = app_its.next();
						if(app.isActivated()){
							//获取软件下系统默认角色
							Collection<RoleVO> roles = rp.getDefaultRolesByApplication(app.getApplicationid());
							if(roles != null && roles.size() > 0 ){
								for(Iterator<RoleVO> it = roles.iterator(); it.hasNext();){
									RoleVO role = it.next();
									UserRoleSet set = new UserRoleSet(_user.getId(), role.getId());
									_user.getUserRoleSets().add(set);
								}
							}
						}
					}
					
					try {
						process.doCreateOrUpdate(_user);
						Integer num = general.get("USER_ADD_SUCCESS");
						num++ ;
						general.put("USER_ADD_SUCCESS", num);
					} catch (Exception e) {
						Integer num = general.get("USER_ADD_FAIL");
						num++ ;
						general.put("USER_ADD_FAIL", num);
					}
					
				}else{   //用户存在
					_user.setName(user.getString("NAME"));
					_user.setEmail(user.getString("EMAIL")); //邮箱
					_user.setTelephone(user.getString("TELEPHONE")); //手机
					
					// 清空原有的部门集合
					_user.getUserDepartmentSets().clear();
					_user.getDepartments().clear();
					
					//设置部门
					String depts = user.getString("depts");
					String[] deptsArray = depts.split(";");
					DepartmentVO deptVO = null ;
					for(String _dept : deptsArray){
						deptVO = deptsMap.get(_dept);
						UserDepartmentSet set = new UserDepartmentSet(_user.getId(), deptVO.getId());
						_user.getUserDepartmentSets().add(set);
						_user.getDepartments().add(deptVO);
					}
					_user.setDefaultDepartment(deptVO.getId());  	//默认部门
					
					try {
						process.doCreateOrUpdate(_user);
						Integer num = general.get("USER_ADD_SUCCESS");
						num++ ;
						general.put("USER_ADD_SUCCESS", num);
					} catch (Exception e) {
						Integer num = general.get("USER_ADD_FAIL");
						num++ ;
						general.put("USER_ADD_FAIL", num);
					}
				}
			}else{
				 //更新结果集数据
				Integer num = general.get("USER_ADD_FAIL");
				num++ ;
				general.put("USER_ADD_FAIL", num);
				
				JSONObject user = userList.getJSONObject(i);
				user.put("error", errors);
				detail.add(user);
			}
		}
		
		return detail;
	}
	

	@Override
	public void excelExportFromDomain(OutputStream outputStream,DomainVO domain) throws Exception {
		
		UserProcess up = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
		
		ParamsTable params = new ParamsTable();
		DataPackage<UserVO> listUsers = up.listUsers(params,null);
		Collection<UserVO> datas = listUsers.getDatas();
		
		exportExcel(datas,outputStream);
	}
	
	private void exportExcel(Collection<UserVO> userList,OutputStream outputStream) {
		try {
			String[] titles = {"用户名(必填)","帐号(必填)","手机","电子邮箱","部门(必填)"};
			//1、创建工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			//1.1、创建头标题样式
			HSSFCellStyle style1 = createCellStyle(workbook, (short)13);
			
			//1.2、创建列标题样式
			HSSFCellStyle style2 = createCellStyle(workbook, (short)12);
			
			//1.3、创建合并单元格对象（合并第1行第1列到第5列）//起始行号，结束行号，起始列号，结束列号
			CellRangeAddress cellRangeAddress_1 = new CellRangeAddress(0, 0, 0, titles.length);
			
			
			//2、创建工作表
			HSSFSheet sheet = workbook.createSheet("用户列表");
			//2.1、设置合并单元格对象
			sheet.addMergedRegion(cellRangeAddress_1);
			//2.2、设置默认列宽
			sheet.setDefaultColumnWidth(25);
			
			//3、创建行
			//3.1、创建头标题行并且设置头标题和样式
			HSSFRow row1 = sheet.createRow(0);
			HSSFCell _cell = row1.createCell(0);
			_cell.setCellStyle(style1);
			_cell.setCellValue("用户列表");
			
			//3.2、创建列标题行并且设置列标题和样式
			HSSFRow row2 = sheet.createRow(1);
			
			for(int i = 0; i < titles.length; i++){
				HSSFCell cell = row2.createCell(i);
				cell.setCellStyle(style2);
				cell.setCellValue(titles[i]);
			}
			
			//4、创建单元格（写入数据库中的用户列表）
			if(userList != null){
				int rowIndex = 2 ;
				for(UserVO user : userList){
					HSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(user.getName());
					row.createCell(1).setCellValue(user.getLoginno());
					row.createCell(2).setCellValue(user.getTelephone());
					row.createCell(3).setCellValue(user.getEmail());
					row.createCell(4).setCellValue(getUserDepartment(user));
				}
			}
			
			//5、输出
			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getUserDepartment(UserVO user) {
		
		Collection<DepartmentVO> departments = user.getDepartments();
		StringBuffer sb = new StringBuffer();
		String name ;
		for(DepartmentVO dept : departments){
			 name = getSuperiorName(dept);
			 sb.append(name).append(";");
		}
		String userDept = sb.substring(0, sb.length()-1);
		
		return userDept;
	}

	private String getSuperiorName(DepartmentVO dept) {
		String supDeptName ;
		if(dept.getSuperior() != null){
			supDeptName = getSuperiorName(dept.getSuperior());
		}else{
			return dept.getName();
		}
		return supDeptName + "/"  + dept.getName();
	}

	/**
	 * 创建单元格样式
	 * @param workbook 工作簿
	 * @param fontSize 字体大小
	 * @return 单元格样式
	 */
	public HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontSize){
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		//1.1.1、创建头标题字体
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//字体加粗
		font.setFontHeightInPoints(fontSize);
		//在样式中加载字体
		style.setFont(font);
		return style;
	}
}
