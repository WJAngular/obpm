package cn.myapps.core.domain.ejb;

import java.util.ArrayList;
import java.util.Collection;

import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.fieldextends.ejb.FieldExtendsVO;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;

/**
 * 企业域信息导入导出元素
 *
 */
public class ExpImpElements {
	
	/**
	 * 企业域
	 */
	private DomainVO domain;
	
	/**
	 * 企业域下的用户
	 */
	private Collection<UserVO> users;
	
	/**
	 * 企业域下的日历
	 */
	private Collection<CalendarVO> calendars;
	
	/**
	 * 企业域下的扩展字段
	 */
	private Collection<FieldExtendsVO> fieldExtends;
	
	/**
	 * 企业域下的部门
	 */
	private Collection<DepartmentVO> departments;
	
	public DomainVO getDomain() {
		return domain;
	}
	
	public void setDomain(DomainVO domain) {
		this.domain = domain;
	}
	
	public Collection<UserVO> getUsers() {
		if(users == null){
			users = new ArrayList<UserVO>();
		}
		return users;
	}
	
	public void setUsers(Collection<UserVO> users) {
		this.users = users;
	}
	
	public Collection<CalendarVO> getCalendars() {
		if(calendars == null){
			calendars = new ArrayList<CalendarVO>();
		}
		return calendars;
	}
	
	public void setCalendars(Collection<CalendarVO> calendars) {
		this.calendars = calendars;
	}
	
	public Collection<FieldExtendsVO> getFieldExtends() {
		if(fieldExtends == null){
			fieldExtends = new ArrayList<FieldExtendsVO>();
		}
		return fieldExtends;
	}
	
	public void setFieldExtends(Collection<FieldExtendsVO> fieldExtends) {
		this.fieldExtends = fieldExtends;
	}

	public Collection<DepartmentVO> getDepartments() {
		return departments;
	}

	public void setDepartments(Collection<DepartmentVO> departments) {
		this.departments = departments;
	}
	
	
}
