package cn.myapps.core.workflow.storage.definition.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;

public interface BillDefiDAO extends IDesignTimeDAO<BillDefiVO> {
	public Collection<BillDefiVO> getBillDefiByModule(String moduleid)
			throws Exception;

	public BillDefiVO findBySubject(String subject, String applicationId)
			throws Exception;
}
