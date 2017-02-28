package cn.myapps.core.workcalendar.standard.ejb;

import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.permission.ejb.PermissionPackage;
import cn.myapps.core.workcalendar.standard.dao.StandardDayDAO;

public class StandardDayProcessBean extends AbstractDesignTimeProcessBean<StandardDayVO> implements StandardDayProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3983462796565055563L;

	protected IDesignTimeDAO<StandardDayVO> getDAO() throws Exception {
		IDesignTimeDAO<StandardDayVO> dao = (StandardDayDAO) DAOFactory.getDefaultDAO(StandardDayVO.class.getName());
		return dao;
	}

	public void doUpdate(ValueObject vo) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			StandardDayVO po = (StandardDayVO) getDAO().find(vo.getId());
			if (po != null) {
				PropertyUtils.copyProperties(po, vo);
				po.setLastModifyDate(new Date());
				getDAO().update(po);
			}
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}
		//PermissionPackage.clearCache();
	}
}
