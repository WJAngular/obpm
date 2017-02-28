package cn.myapps.attendance.location.dao;

import java.util.List;

import cn.myapps.attendance.base.dao.BaseDAO;
import cn.myapps.attendance.location.ejb.Location;

public interface LocationDAO extends BaseDAO {

	public List<Location> getLocationsByRule(String ruleId) throws Exception;
}
