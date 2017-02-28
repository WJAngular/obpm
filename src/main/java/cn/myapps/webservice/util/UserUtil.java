package cn.myapps.webservice.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.util.ObjectUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.webservice.UserService;
import cn.myapps.webservice.WebServiceUtil;
import cn.myapps.webservice.fault.UserServiceFault;
import cn.myapps.webservice.model.SimpleAdmin;
import cn.myapps.webservice.model.SimpleUser;

/**
 * UserService工具类
 * @author ivan
 *
 */
public class UserUtil {
	
	/**
	 * 转换为简单用户对象
	 * 
	 * @param user
	 *            用户
	 * @return SimpleUser 用户
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static SimpleUser convertToSimple(BaseUser user)
			throws Exception {
		if (user != null) {
			SimpleUser dest = new SimpleUser();
			ObjectUtil.copyProperties(dest, user);
			if(user instanceof UserVO){
				setOtherProperties(dest, (UserVO) user);
			}
			return dest;
		}
		return null;
	}

	/**
	 * 转换为简单用户对象
	 * @SuppressWarnings convertToSimple方法不支持泛型
	 * @param user
	 *            用户
	 * @return SimpleUser 用户
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	public static Object convertToSimple(BaseUser user, Class beanClass)
			throws IllegalAccessException, InvocationTargetException,
			InstantiationException {
		if (user != null) {
			Object dest = beanClass.newInstance();
			ObjectUtil.copyProperties(dest, user);
			if(dest instanceof SimpleAdmin && user instanceof SuperUserVO){
				setOtherProperties((SimpleAdmin) dest, (SuperUserVO) user);
			}
			return dest;
		}
		return null;
	}

	/**
	 * 把简单用户对象SimpleUser转换为UserVO用户对象
	 * @param vo UserVO
	 * @param user 简单用户对象
	 * @return
	 * @throws Exception
	 */
	public static UserVO convertToVO(UserVO vo, SimpleUser user)throws Exception{
		try {
			vo.setId(user.getId() != null ? user.getId() : vo.getId());
			vo.setName(user.getName() != null ? user.getName() : vo.getName());
			vo.setLoginno(user.getLoginno() != null ? user.getLoginno() : vo.getLoginno());
			vo.setLoginpwd(user.getLoginpwd() != null ? user.getLoginpwd() : vo.getLoginpwd());
			vo.setEmail(user.getEmail() != null ? user.getEmail() : vo.getEmail());
			vo.setTelephone(user.getTelephone() != null ? user.getTelephone() : vo.getTelephone());
			vo.setDimission(1);
			vo.setLockFlag(1);
			vo.setStatus(1);
			setDomainInfo(vo,user);
			
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}
		return vo;
	}
	
	/**
	 * 把简单管理员对象SimpleAdmin转换为SuperUserVO对象
	 * @param vo SuperUserVO
	 * @param admin 简单管理员对象
	 * @return
	 * @throws Exception
	 */
	public static SuperUserVO convertToVO(SuperUserVO vo, SimpleAdmin admin)throws Exception{
		try {
			vo.setId(admin.getId() != null ? admin.getId() : vo.getId());
			vo.setName(admin.getName() != null ? admin.getName() : vo.getName());
			vo.setLoginno(admin.getLoginno() != null ? admin.getLoginno() : vo.getLoginno());
			vo.setLoginpwd(admin.getLoginpwd() != null ? admin.getLoginpwd() : vo.getLoginpwd());
			vo.setEmail(admin.getEmail() != null ? admin.getEmail() : vo.getEmail());
			
			setOtherInfo(vo, admin);
			
		} catch (Exception e) {
			throw new UserServiceFault(e.getMessage());
		}
		return vo;
	}
	
	/**
	 * 为SimpleUser设置其它属性
	 * @param dest
	 * @param user
	 * @throws Exception
	 */
	private static void setOtherProperties(SimpleUser dest, UserVO user)
		throws Exception{
		DomainProcess dp = (DomainProcess) ProcessFactory
			.createProcess(DomainProcess.class);
		DepartmentProcess process = (DepartmentProcess) ProcessFactory
			.createProcess(DepartmentProcess.class);
		//为SimpleUser设置defaultDepartmentName属性
		DepartmentVO vo = (DepartmentVO) process.doView(user.getDefaultDepartment());
		dest.setDefaultDepartmentName(vo != null ? vo.getName() : null);
		
		//为SimpleUser设置domainName属性
		DomainVO domain = (DomainVO) dp.doView(user.getDomainid());
		dest.setDomainName(domain != null ? domain.getName() : null);
	}
	
	/**
	 * 为SimpleAdmin设置其它属性
	 * @param dest
	 * @param user
	 */
	private static void setOtherProperties(SimpleAdmin dest, SuperUserVO user){
		//为SimpleAdmin设置applicationNames属性
		Collection<ApplicationVO> apps = user.getApplications();
		String [] appNames = null;
		if(apps != null){
			appNames = new String [apps.size()];
			int i = 0;
			for (Iterator<ApplicationVO> iterator = apps.iterator(); iterator.hasNext();) {
				ApplicationVO applicationVO = (ApplicationVO) iterator
						.next();
				appNames[i++] = applicationVO.getName();
			}
		}
		dest.setApplicationNames(appNames);
		
		//为SimpleAdmin设置domainNames属性
		Collection<DomainVO> domains = user.getDomains();
		String [] domainNames = null;
		if(domains != null){
			domainNames = new String [domains.size()];
			int i = 0;
			for (Iterator<DomainVO> iterator = domains.iterator(); iterator.hasNext();) {
				DomainVO domainVO = (DomainVO) iterator
						.next();
				domainNames[i++] = domainVO.getName();
			}
		}
		dest.setDomainNames(domainNames);
		
		//为SimpleAdmin设置userType属性
		int[] type = new int[0];
		if(user.isSuperAdmin()){
			type = new int[1];
			type[0] = SimpleAdmin.USER_TYPE_SUPERADMIN;
			dest.setUserType(type);
		}else {
			int number = 0;
			if(user.isDeveloper()){
				number ++;
			}
			if(user.isDomainAdmin()){
				number ++;
			}
	
			type = new int[number];
			int index = 0;
			if(user.isDeveloper()){
				type[index++] = SimpleAdmin.USER_TYPE_DEVELOPER;
			}
			if(user.isDomainAdmin()){
				type[index++] = SimpleAdmin.USER_TYPE_DOMAINADMIN;
			}
			
			dest.setUserType(type);
		}
	}
	
	/**
	 * 为UserVO设置企业域信息
	 * 
	 * @param vo
	 *            企业域用户
	 * @param user
	 *            简单用户对象
	 * @throws Exception
	 */
	private static void setDomainInfo(UserVO vo, SimpleUser user) throws Exception {
		DepartmentProcess process = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);
		CalendarProcess calendarProcess = (CalendarProcess) ProcessFactory
				.createProcess(CalendarProcess.class);

		// 设置企业域
		DomainVO domain = WebServiceUtil.validateDomain(user.getDomainName());
		vo.setDomainid(domain.getId());

		// 设置工作日历
		CalendarVO calendar = (CalendarVO) calendarProcess.doViewByName(
				UserService.STANDARD_CALENDAR_NAME, domain.getId());
		vo.setCalendarType(calendar.getId());
		
		//设置默认部门
		String deptName = user.getDefaultDepartmentName();
		Collection<UserDepartmentSet> coll = vo.getUserDepartmentSets();
		if (deptName == null) {
			if (vo.getDefaultDepartment() == null) {
				if(coll.isEmpty() && coll.size() <= 0){
					// 默认增加根部门并设置为默认部门
					DepartmentVO defaultDep = (DepartmentVO) process
							.getRootDepartmentByApplication(null, domain.getId());
					if(defaultDep == null)
						throw new UserServiceFault("企业域["+domain.getName()+"]还没有部门,请先添加部门.");
					UserDepartmentSet set = new UserDepartmentSet(vo
							.getId(), defaultDep.getId());
					coll.add(set);
					vo.setUserDepartmentSets(coll);
					vo.setDefaultDepartment(defaultDep != null ? defaultDep.getId() : null);
				}else{
					//设置其中一个用户所在部门为默认部门
					UserDepartmentSet set = coll.iterator().next();
					DepartmentVO defaultDep = (DepartmentVO) process.doView(set.getDepartmentId());
					vo.setDefaultDepartment(defaultDep.getId());
				}
			}
		} else {
			DepartmentVO defaultDep = (DepartmentVO) process.doViewByName(deptName, domain.getId());
			if (defaultDep == null) {
				// 默认部门不存在数据库
				throw new UserServiceFault("部门["
						+ defaultDep + "]不存在企业域["
						+ domain.getName() + "]下.");
			}
			boolean exist = false;
			for (Iterator<UserDepartmentSet> iterator = coll.iterator(); iterator.hasNext();) {
				UserDepartmentSet set = (UserDepartmentSet) iterator.next();
				if(defaultDep.getId().equals(set.getDepartmentId())){
					exist = true;
					break;
				}
			}
			//设置的默认部门不存在用户所属部门,则增加这个部门再去设置默认
			if(!exist){
				UserDepartmentSet set = new UserDepartmentSet(vo
						.getId(), defaultDep.getId());
				coll.add(set);
				vo.setUserDepartmentSets(coll);
			}
			//设置默认
			vo.setDefaultDepartment(defaultDep.getId());
		}
	}
	
	/**
	 * 为SuperUserVO设置其它属性
	 * @param vo
	 * @param admin
	 * @throws Exception
	 */
	private static void setOtherInfo(SuperUserVO vo, SimpleAdmin admin) throws Exception{
		//设置可管理应用
		String [] apps = admin.getApplicationNames();
		Collection<ApplicationVO> applications = new HashSet<ApplicationVO>();
		if(apps != null){
			for (int i = 0; i < apps.length; i++) {
				ApplicationVO appVO = WebServiceUtil.validateApplication(apps[i]);
				applications.add(appVO);
			}
			vo.setApplications(applications);
		}
		
		//设置可管理企业域
		String [] domainNames = admin.getDomainNames();
		Collection<DomainVO> domains = new HashSet<DomainVO>();
		if(domainNames != null){
			for (int i = 0; i < domainNames.length; i++) {
				DomainVO domain = WebServiceUtil.validateDomain(domainNames[i]);
				domains.add(domain);
			}
			vo.setDomains(domains);
		}
		
		//设置特权用户类型
		int[] type = admin.getUserType();
		if(type != null && type.length > 0){
			for (int i = 0; i < type.length; i++) {
				switch(type[i]){
				case SimpleAdmin.USER_TYPE_DEVELOPER:
					vo.setDeveloper(true);
					break;
				case SimpleAdmin.USER_TYPE_DOMAINADMIN:
					vo.setDomainAdmin(true);
					break;
				case SimpleAdmin.USER_TYPE_SUPERADMIN:
					vo.setSuperAdmin(true);
					break;
				default:
					throw new UserServiceFault("特权用户类型不正确:" + type[i] + "，可用的特权用户类型(1:开发者，2:企业域管理员，3:超级管理员)");
				}
			}
		}
		if(vo.isSuperAdmin()){//是超级管理员时不可以是开发者和企业域管理员
			vo.setDeveloper(false);
			vo.setDomainAdmin(false);
		}
		
	}

}
