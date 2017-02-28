package cn.myapps.attendance.location.ejb;

import java.util.List;

import cn.myapps.attendance.base.dao.BaseDAO;
import cn.myapps.attendance.base.dao.DaoManager;
import cn.myapps.attendance.base.ejb.AbstractBaseProcessBean;
import cn.myapps.attendance.location.dao.LocationDAO;


public class LocationProcessBean extends AbstractBaseProcessBean<Location>
		implements LocationProcess {


	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getLocationDAO(getConnection());
	}

	public List<Location> getLocationsByRule(String ruleId) throws Exception {
		return ((LocationDAO)getDAO()).getLocationsByRule(ruleId);
	}


}
