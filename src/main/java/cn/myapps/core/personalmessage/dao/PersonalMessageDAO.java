package cn.myapps.core.personalmessage.dao;

import java.util.Collection;
import java.util.Date;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.user.action.WebUser;

public interface PersonalMessageDAO extends IDesignTimeDAO<PersonalMessageVO> {

	/**
	 * 查找回收箱中的站内短信
	 * 
	 * @param userid
	 *            用户标识
	 * @param page
	 *            分页信息
	 * @param line
	 *            每页行数
	 * @return 站内短信
	 * @throws Exception
	 */
	public DataPackage<PersonalMessageVO> queryTrash(String userid, ParamsTable params)
			throws Exception;

	/**
	 * 查询新的站内短信条数
	 * 
	 * @param userid
	 *            用户标识
	 * @return 新站内短信的数量
	 * @throws Exception
	 */
	public int countNewMessages(String userid) throws Exception;
	
	public String[] getReceiverUserIdsByMessageBodyId(String bodyId) throws Exception;

	public DataPackage<PersonalMessageVO> queryNewMessage(String userid, ParamsTable params)
		throws Exception;
	
	/**
	 * 根据时间查找当前用户最新的信息
	 * @param userid
	 * @param date
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryNewMessagWithTime(String userid,String date, ParamsTable params)
			throws Exception;
	
	public DataPackage<PersonalMessageVO> queryInBox(String userid, ParamsTable params)
		throws Exception;
	
	public DataPackage<PersonalMessageVO> queryOutbox(String userid, ParamsTable params) throws Exception;
	
	/**
	 * 根据条件查询某一条信息
	 * @param senderid    发送者ID
	 * @param receiverid  接收者ID
	 * @param bodyId    信息内容ID
	 * @return			     某条信息对象
	 * @throws Exception
	 */
	public ValueObject queryPersonalMessageVO(String senderid, String receiverid ,String bodyId)
		throws Exception;
	
	/**
	 * 根据用户查询通知公告的集合
	 * @param user
	 * 		用户
	 * @return
	 * 		公告的集合
	 * @throws Exception
	 */
	public Collection<PersonalMessageVO> queryAnnouncementsByUser(WebUser user)throws Exception;
	
}