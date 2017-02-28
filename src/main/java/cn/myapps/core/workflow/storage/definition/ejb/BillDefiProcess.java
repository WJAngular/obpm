package cn.myapps.core.workflow.storage.definition.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface BillDefiProcess extends IDesignTimeProcess<BillDefiVO> {
	/**
	 * 
	 * @param moduleid
	 *            模块标识
	 * @return 流程对象的集合
	 * @throws Exception
	 */
	public Collection<BillDefiVO> getBillDefiByModule(String moduleid)
			throws Exception;

	/**
	 * 根据参数，查询出流程对象
	 * 
	 * @param subject
	 *            流程主题
	 * @param applicationId
	 *            应用标识
	 * @return 流程对象
	 * @throws Exception
	 */
	public BillDefiVO doViewBySubject(String subject, String applicationId)
			throws Exception;

	/**
	 * 校验是否有重复的名称
	 * 
	 * @param vo
	 *            流程对象
	 * @return
	 * @throws Exception
	 */
	public boolean isSubjectExisted(BillDefiVO vo) throws Exception;

	/**
	 * 删除多个流程对象
	 * 
	 * @param flowList
	 *            流程对象集合
	 * @throws Exception
	 */
	public abstract void doRemove(Collection<BillDefiVO> flowList)
			throws Exception;

	/**
	 * 流程复制
	 * @param viewIds
	 * @throws Exception
	 */
	public void doCopyFlow(String[] viewIds) throws Exception;
	
	public void updateOpinion(String pk,String opinion) throws Exception;
	public void removeOpinion(String pk,String opinionid) throws Exception;
}
