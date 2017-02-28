package cn.myapps.attendance.rule.dao;

import cn.myapps.attendance.base.dao.BaseDAO;
import cn.myapps.base.dao.ValueObject;

public interface RuleDAO extends BaseDAO {
	
	public ValueObject findBySQL(String sql) throws Exception;

}
