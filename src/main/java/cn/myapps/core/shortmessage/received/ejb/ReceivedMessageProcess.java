package cn.myapps.core.shortmessage.received.ejb;

import java.util.Collection;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;

public interface ReceivedMessageProcess extends IDesignTimeProcess<ReceivedMessageVO> {
	/**
	 * 根据Document ID 查询回复记录
	 * @param docid 文档ID
	 * @see cn.myapps.base.dao.DataPackage
	 * @return　返回回复记录结果 
	 * @throws Exception
	 */
	public DataPackage<ReceivedMessageVO> queryByDocId(String docid) throws Exception;
	
	/**
	 * 根据Document ID 查询回复记录
	 * @param parentid 关联的发送记录ID
	 * @see cn.myapps.base.dao.DataPackage
	 * @return　返回回复记录结果 
	 * @throws Exception
	 */
	public DataPackage<ReceivedMessageVO> queryByParent(String parentid) throws Exception;
	
	/**
	 * 查询出未读回复记录
	 * @return 返回未读回复记录集
	 * @throws Exception
	 */
	public Collection<ReceivedMessageVO> doQueryUnReadMessage() throws Exception;
}