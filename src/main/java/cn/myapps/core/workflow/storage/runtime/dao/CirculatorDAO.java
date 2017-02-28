package cn.myapps.core.workflow.storage.runtime.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;

public interface CirculatorDAO extends IRuntimeDAO {
	/**
	 * 根据外键(DOCID、NODERT_ID、FLOWSTATERT_ID)级联查找Circulator
	 * 
	 * @param key
	 *            外键
	 * @param val
	 *            外键值
	 * @return 
	 * @throws Exception
	 */
	public Collection<Circulator> queryByForeignKey(String key, Object val) throws Exception;
	
	/**
	 * 根据外键(DOCID、NODERT_ID、FLOWSTATERT_ID)删除Circulator
	 * @param key
	 * 		外键字段名
	 * @param val
	 * 		外键值
	 * @throws Exception
	 */
	public void removeByForeignKey(String key, Object val) throws Exception;
	
	public DataPackage<Circulator> queryPendingByUser(ParamsTable params,WebUser user) throws Exception;
	
	public DataPackage<Circulator> queryWorksByUser(ParamsTable params,WebUser user) throws Exception;
	

	/**
	 * 根据当前文档的信息查找
	 * @return
	 */
	public Circulator findByCurrDoc(String docId ,String flowStateId,boolean isRead,WebUser user) throws Exception;
	
	/**
	 * 获取记录总数
	 * @return
	 */
	public long countByFilter(ParamsTable params, WebUser user) throws Exception;
}
