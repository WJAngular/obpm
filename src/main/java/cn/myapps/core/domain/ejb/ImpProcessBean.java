package cn.myapps.core.domain.ejb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentProcessBean;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.fieldextends.ejb.FieldExtendsProcess;
import cn.myapps.core.fieldextends.ejb.FieldExtendsVO;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserProcessBean;
import cn.myapps.core.user.ejb.UserRoleSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcessBean;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.util.ObjectUtil;
import cn.myapps.util.file.ZipUtil;
import cn.myapps.util.xml.XmlUtil;

public class ImpProcessBean extends AbstractDesignTimeProcessBean<ExpImpElements> implements ImpProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2053786066483123595L;
	
	DomainProcess domainProcess;
	DepartmentProcess departmentProcess;
	UserProcess userProcess;
	CalendarProcess calendarProcess;
	FieldExtendsProcess fieldExtendsProcess;
	DomainVO domain;
	Collection<DepartmentVO> departments;
	Collection<UserVO> users;
	Collection<CalendarVO> calendars;
	Collection<FieldExtendsVO> fieldExtends;
	ExpImpElements elements;

	@Override
	protected IDesignTimeDAO<ExpImpElements> getDAO() throws Exception {
		return null;
	}
	
	public ImpProcessBean(){
		try {
			domainProcess = new DomainProcessBean();
			userProcess = new UserProcessBean();
			calendarProcess = new CalendarProcessBean();
			departmentProcess = new DepartmentProcessBean();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doImport(File impFile) throws Exception {
		try {
//			PersistenceUtils.beginTransaction();
			elements = parseFile(impFile);
			domain = elements.getDomain();
			departments = elements.getDepartments();
			users = elements.getUsers();
			calendars = elements.getCalendars();
			fieldExtends = elements.getFieldExtends();
			
			this.importDomain();
			this.importDepartment();
			this.importUser();
			this.importCalendar();
			this.importFieldExtend();
//			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
//			PersistenceUtils.rollbackTransaction();
			throw e;
		}finally{
//			PersistenceUtils.closeSessionAndConnection();
		}
	}

	/**
	 * 导入企业域的基本信息
	 * @throws Exception
	 */
	public void importDomain() throws Exception {
		DomainVO domainByName = domainProcess.getDomainByName(domain.getName());
		DomainVO domainById = (DomainVO) domainProcess.doView(domain.getId());
		if(domainByName != null || domainById != null){
			throw new OBPMValidateException("{*[core.domain.name.exist]*}");
		}
		domainProcess.doCreate(domain,false);
	}
	
	/**
	 * 导入企业域下的部门
	 * @throws Exception 
	 */
	public void importDepartment() throws Exception {
		importDepartmentWithoutFK();
		importDepartmentWithFK();
	}
	
	private void importDepartmentWithoutFK() throws Exception{
		Iterator<DepartmentVO> it = departments.iterator();
		while(it.hasNext()){
			DepartmentVO department = it.next();
			DepartmentVO _department = (DepartmentVO) ObjectUtil.clone(department);
			_department.setSuperior(null);
			departmentProcess.doCreate(_department);
		}
	}

	private void importDepartmentWithFK() throws Exception{
		Collection<DepartmentVO> _departments = elements.getDepartments();
		Iterator<DepartmentVO> it = _departments.iterator();
		while(it.hasNext()){
			DepartmentVO department = it.next();
			departmentProcess.doUpdate(department);
		}
	}

	/**
	 * 导入企业域的用户
	 * @throws Exception
	 */
	private void importUser() throws Exception{
		importUserWithoutFK();
		importUserWithFK();
	}
	
	/**
	 * 导入用户非级联的信息
	 * @throws Exception
	 */
	private void importUserWithoutFK() throws Exception{
		Collection<UserVO> _users = elements.getUsers();
		Iterator<UserVO> it = _users.iterator();
		while(it.hasNext()){
			UserVO user = it.next();
			UserVO _user = (UserVO) ObjectUtil.clone(user);
			_user.setUserDepartmentSets(new HashSet<UserDepartmentSet>());
			_user.setUserRoleSets(new HashSet<UserRoleSet>());
			_user.setSuperior(null);
			_user.setProxyUser(null);
			userProcess.doCreateWithoutPW(_user, false);
		}
	}
	
	/**
	 * 更新企业域下用户的信息
	 * @throws Exception
	 */
	private void importUserWithFK() throws Exception{
		Collection<UserVO> _users = elements.getUsers();
		Iterator<UserVO> it = _users.iterator();
		while(it.hasNext()){
			UserVO user = it.next();
			userProcess.doUpdate(user);
		}
	}

	/**
	 * 导入企业域的扩展字段
	 * @throws Exception
	 */
	private void importFieldExtend() throws Exception{
		Iterator<FieldExtendsVO> it = fieldExtends.iterator();
		while(it.hasNext()){
			FieldExtendsVO fieldExtend = it.next();
			fieldExtendsProcess.doCreate(fieldExtend);
		}
	}

	/**
	 * 导入企业域的日历
	 * @throws Exception
	 */
	private void importCalendar() throws Exception{
		Iterator<CalendarVO> it = calendars.iterator();
		while(it.hasNext()){
			CalendarVO calendar = it.next();
			calendarProcess.doCreate(calendar);
		}
	}

	public ExpImpElements parseFile(File impFile) throws Exception {
		try {
			String[] xmlContents = ZipUtil.readZipFile(impFile);
			if (xmlContents.length > 0) {
				ExpImpElements elements = (ExpImpElements) XmlUtil
						.toOjbect(xmlContents[0]);
				return elements;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
