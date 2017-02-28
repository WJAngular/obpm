package cn.myapps.core.workcalendar.special.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.workcalendar.special.ejb.SpecialDayVO;

public class HibernateSpecialDayDAO extends HibernateBaseDAO<SpecialDayVO> implements SpecialDayDAO {

	public HibernateSpecialDayDAO(String valueObjectName){
		super(valueObjectName);
	}
	
}
