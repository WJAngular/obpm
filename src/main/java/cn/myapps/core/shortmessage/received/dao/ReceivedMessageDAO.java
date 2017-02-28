package cn.myapps.core.shortmessage.received.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.shortmessage.received.ejb.ReceivedMessageVO;

public interface ReceivedMessageDAO extends IDesignTimeDAO<ReceivedMessageVO> {
	
	/**
	 * 返回回复记录对象
	 * @param replyCode 回复代码
	 * @param recvtel 回复者电话号码
	 * @return 回复记录对象
	 * @throws Exception
	 */
	public ReceivedMessageVO getMessageByReplyCode(String replyCode,
			String recvtel) throws Exception;
	
	/**
	 * 返回未读回复记录集合
	 * @return 未读回复记录集合
	 * @throws Exception
	 */
	public Collection<ReceivedMessageVO> queryUnReadMessage() throws Exception;

}