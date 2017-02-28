package cn.myapps.core.workcalendar.standard.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.workcalendar.standard.ejb.StandardDayVO;

public class HibernateStandardDayDAO extends HibernateBaseDAO<StandardDayVO>
		implements StandardDayDAO {

	public HibernateStandardDayDAO(String valueObjectName){
		super(valueObjectName);
	}

}
