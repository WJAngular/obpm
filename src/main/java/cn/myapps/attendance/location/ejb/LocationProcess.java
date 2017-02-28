package cn.myapps.attendance.location.ejb;

import java.util.List;

import cn.myapps.attendance.base.ejb.BaseProcess;

public interface LocationProcess extends BaseProcess<Location> {

	public List<Location> getLocationsByRule(String ruleId) throws Exception; 
}
