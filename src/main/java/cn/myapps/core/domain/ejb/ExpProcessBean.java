package cn.myapps.core.domain.ejb;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.constans.Environment;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentProcessBean;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.fieldextends.ejb.FieldExtendsProcess;
import cn.myapps.core.fieldextends.ejb.FieldExtendsProcessBean;
import cn.myapps.core.fieldextends.ejb.FieldExtendsVO;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserProcessBean;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcessBean;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.util.DateUtil;
import cn.myapps.util.file.ZipUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.xml.XmlUtil;

public class ExpProcessBean extends AbstractDesignTimeProcessBean<ExpImpElements> implements ExpProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4836077810431530069L;

	DomainProcess domainProcess;
	DepartmentProcess departmentProcess;
	UserProcess userProcess;
	CalendarProcess calendarProcess;
	FieldExtendsProcess fieldextendsProcess;
	
	DomainVO domain;
	Collection<DepartmentVO> departments;
	Collection<UserVO> users;
	Collection<CalendarVO> calendars;
	Collection<FieldExtendsVO> fieldExtends;
	
	@Override
	protected IDesignTimeDAO<ExpImpElements> getDAO() throws Exception {
		return null;
	}
	
	public ExpProcessBean(){
		try {
			domainProcess = new DomainProcessBean();
			departmentProcess = new DepartmentProcessBean();
			userProcess = new UserProcessBean();
			calendarProcess = new CalendarProcessBean();
			fieldextendsProcess = new FieldExtendsProcessBean();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File createZipFile(ExpImpElements elements, String domainId)
			throws Exception {
		String zipFileName = DateUtil.getCurDateStr("yyyyMMddhhmmss") + ".zip";
		File[] file = new File[1];
		File zipFile = new File(getRealFileName(zipFileName));
		elements = getExpElements(elements, domainId);
		file[0] = getXmlFile(elements, elements.getDomain().getName());
		ZipUtil.createZipArchive(zipFile, file);
		return zipFile;
	}

	public ExpImpElements getExpElements(ExpImpElements elements, String domainId) throws Exception{
		domain = (DomainVO) domainProcess.doView(domainId);
		departments = departmentProcess.queryByDomain(domainId);
		users = userProcess.queryByDomain(domainId);
		calendars = calendarProcess.doQueryListByDomain(domainId);
		fieldExtends = fieldextendsProcess.queryUserFieldExtends(domainId);
		setLazyLoadfalse();
		elements.setDomain(domain);
		elements.setDepartments(departments);
		elements.setUsers(users);
		elements.setCalendars(calendars);
		elements.setFieldExtends(fieldExtends);
		return elements;
	}
	
	String getRealFileName(String fileName) throws Exception {
		String exportDir = DefaultProperty.getProperty("EXPORT_PATH");
		String fullFileName = Environment.getInstance().getRealPath(
				exportDir + fileName);
		return fullFileName;
	}
	
	File getXmlFile(Object obj, String name) throws Exception {
		String fileName = name + ".xml";
		return XmlUtil.toXmlFile(obj,getRealFileName(fileName));
	}
	
	public File getExportFile(String fileName) throws Exception {
		String filepath = DefaultProperty.getProperty("EXPORT_PATH");
		String realpath = Environment.getInstance().getRealPath(
				filepath + "/" + fileName);
		File exportFile = new File(realpath);
		if (exportFile.exists()) {
			return exportFile;
		} else {
			return null;
		}

	}
	
	public void setLazyLoadfalse(){
		domain.setLazyLoad(false);
		
		Collection<SuperUserVO> superUsers = domain.getUsers();
		
		Iterator<DepartmentVO> departmentIt = departments.iterator();
		Iterator<UserVO> userIt = users.iterator();
		Iterator<CalendarVO> calendarIt = calendars.iterator();
		Iterator<FieldExtendsVO> fieldExtendIt = fieldExtends.iterator();
		Iterator<SuperUserVO> superUserIt = superUsers.iterator();
		
		while(departmentIt.hasNext()){
			DepartmentVO department = departmentIt.next();
			department.setLazyLoad(false);
		}
		
		while(userIt.hasNext()){
			UserVO user = userIt.next();
			user.setLazyLoad(false);
		}
		
		while(calendarIt.hasNext()){
			CalendarVO calendar = calendarIt.next();
			calendar.setLazyLoad(false);
		}
		
		while(fieldExtendIt.hasNext()){
			FieldExtendsVO fieldExtend = fieldExtendIt.next();
			fieldExtend.setLazyLoad(false);
		}
		
		while(superUserIt.hasNext()){
			SuperUserVO superUser = superUserIt.next();
			superUser.getDomains().clear();
			
			Collection<DomainVO> domains = superUser.getDomains();
			Iterator<DomainVO> it = domains.iterator();
			while(it.hasNext()){
				DomainVO domain = it.next();
				domain.setLazyLoad(false);
			}
		}
	}
}
