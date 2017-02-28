package cn.myapps.core.workcalendar.calendar.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;

public interface CalendarDAO extends IDesignTimeDAO<CalendarVO> {
	
	public CalendarVO doViewByName(String name,String domainid) throws Exception ;

	public Collection<CalendarVO> doQueryList(String domainid) throws Exception;
	
	public DataPackage<CalendarVO> doQueryListBySearch(ParamsTable params,int page,int lines) throws Exception;
	
	public int queryCountByName(String name, String domainid) throws Exception;
	
	public void saveCalendar(String id, String name, String remark) throws Exception;
	
}
