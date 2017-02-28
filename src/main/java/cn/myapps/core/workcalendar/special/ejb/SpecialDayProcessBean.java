package cn.myapps.core.workcalendar.special.ejb;

import java.util.Date;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.permission.ejb.PermissionPackage;
import cn.myapps.core.workcalendar.special.dao.SpecialDayDAO;
import cn.myapps.util.sequence.Sequence;

public class SpecialDayProcessBean extends AbstractDesignTimeProcessBean<SpecialDayVO> implements SpecialDayProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2151238020191365574L;

	protected IDesignTimeDAO<SpecialDayVO> getDAO() throws Exception {
		IDesignTimeDAO<SpecialDayVO> dao = (SpecialDayDAO) DAOFactory.getDefaultDAO(SpecialDayVO.class.getName());
		return dao;
	}

	public void doUpdate(ValueObject vo) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			SpecialDayVO po = (SpecialDayVO) getDAO().find(vo.getId());
			if (po != null) {
				SpecialDayVO vos = (SpecialDayVO) vo;
				po.setWorkingDayStatus(vos.getWorkingDayStatus());
				po.setStartDate(vos.getStartDate());
				po.setStartTime1(vos.getStartTime1());
				po.setStartTime2(vos.getStartTime2());
				po.setStartTime3(vos.getStartTime3());
				po.setStartTime4(vos.getStartTime4());
				po.setStartTime5(vos.getStartTime5());
				po.setEndDate(vos.getEndDate());
				po.setEndTime1(vos.getEndTime1());
				po.setEndTime2(vos.getEndTime2());
				po.setEndTime3(vos.getEndTime3());
				po.setEndTime4(vos.getEndTime4());
				po.setEndTime5(vos.getEndTime5());
				po.setRemark(vos.getRemark());
				po.setLastModifyDate(new Date());
				getDAO().update(po);
			} else {
				po = (SpecialDayVO) vo;
				po.setLastModifyDate(new Date());
				po.setSortId(Sequence.getTimeSequence());
				po.getCalendar().getSpecialDays().add(po);
				getDAO().create(po);
			}
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}
		//PermissionPackage.clearCache();
	}
}
