package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.core.user.action.WebUser;

/**
 * @author happy
 *
 */
public interface CirculatorProcess extends IRunTimeProcess<Circulator> {

	/**
	 * 根据NodeRtId查询
	 * @return
	 * @throws Exception
	 */
	public Collection<Circulator> doQueryByNodeRtId(String id) throws Exception;
	
	/**
	 * 根据用户查找代办
	 * @param user
	 * 		登陆用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Circulator> getPendingByUser(ParamsTable params,WebUser user) throws Exception;
	
	/**
	 * 获取用户的已阅、未阅信息
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Circulator> getWorksByUser(ParamsTable params,WebUser user) throws Exception;
	
	/**
	 * 根据当前文档的信息查找
	 * @return
	 */
	public Circulator findByCurrDoc(String docId ,String flowStateId,boolean isRead,WebUser user) throws Exception;
	
	/**
	 * 根据外键(DOCID、NODERT_ID、FLOWSTATERT_ID)删除Circulator
	 * @param key
	 * 		外键字段名
	 * @param val
	 * 		外键值
	 * @throws Exception
	 */
	public void doRemoveByForeignKey(String key, Object val) throws Exception;
	
	/**
	 * 获取待办的记录数量
	 * 
	 * @param params
	 *            参数
	 * @param user
	 *            webUser
	 * @return 记录数量
	 * @throws Exception
	 */
	public long conutByFilter(ParamsTable params, WebUser user) throws Exception;
}
