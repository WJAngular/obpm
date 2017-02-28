package cn.myapps.core.privilege.operation.dao;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.privilege.operation.ejb.OperationVO;

public interface OperationDAO extends IDesignTimeDAO<OperationVO> {

	public boolean isEmpty(String applicationId) throws Exception;

	/**
	 * 获取总数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getTotal() throws Exception;
}