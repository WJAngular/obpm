package cn.myapps.core.validate.repository.dao;

import java.util.Collection;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryVO;

public class HibernateValidateRepositoryDAO extends HibernateBaseDAO<ValidateRepositoryVO> implements
		ValidateRepositoryDAO {
	public HibernateValidateRepositoryDAO(String voClassName) {
		super(voClassName);
	}


	public Collection<ValidateRepositoryVO> getValidateRepositoryByApplication(String application)
			throws Exception {

		String hql = "from " + _voClazzName + " sp where (sp.applicationid='"
				+ application + "'" + " ) ";
		return getDatas(hql);
	}


	public boolean isValidateNameExist(String id, String name, String application)
			throws Exception {
		String hql = "from ValidateRepositoryVO rp where rp.name=" + "'"+name+"'";
		if(application != null && application.length()>0) {
			hql += (" and rp.applicationid = '" + application + "' ");
		}
		
		if(id != null && !id.equals("")){
			hql += (" and rp.id !='"+id+"' ");
		}
		
		if(this.getData(hql) != null){
			throw new OBPMValidateException("{*[duplicate_name]*}");	
		}
		else
			return false;
	}
}
