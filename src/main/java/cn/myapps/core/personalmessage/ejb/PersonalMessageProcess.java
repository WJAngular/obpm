package cn.myapps.core.personalmessage.ejb;

import java.util.Collection;
import java.util.Date;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserVO;

public interface PersonalMessageProcess extends
		IDesignTimeProcess<PersonalMessageVO> {

	/**
	 * 将指定的站内短信删除
	 * 
	 * @param pk
	 *            站内短信标识
	 */
	public abstract void doRemove(String pk) throws Exception;

	/**
	 * 根据条件查询用户所有在收件箱中的站内短信
	 * 
	 * @param userid
	 *            用户的标识
	 * @param params
	 *            查询的条件
	 * @return 站内短信的集合
	 * @throws Exception
	 */
	public abstract DataPackage<PersonalMessageVO> doInbox(String userid,
			ParamsTable params) throws Exception;

	/**
	 * 根据条件查询用户所有在发件箱中的站内短信
	 * 
	 * @param userid
	 *            用户的标识
	 * @param params
	 *            查询的条件
	 * @return 站内短信的集合
	 * @throws Exception
	 */
	public abstract DataPackage<PersonalMessageVO> doOutbox(String id,
			ParamsTable params) throws Exception;

	/**
	 * 根据条件查询用户所有在回收箱中的站内短信
	 * 
	 * @param userid
	 *            用户的标识
	 * @param params
	 *            查询的条件
	 * @return 站内短信的集合
	 * @throws Exception
	 */
	public abstract DataPackage<PersonalMessageVO> doTrash(String userid,
			ParamsTable params) throws Exception;

	/**
	 * 将单条站内短信移至回收箱
	 */
	public abstract void doSendToTrash(ValueObject vo) throws Exception;

	/**
	 * 将一到多条站内短信移至回收箱
	 */
	public abstract void doSendToTrash(String[] msgs) throws Exception;

	/**
	 * 根据角色分组进行群发短信
	 * 
	 * @param roleid
	 *            角色组唯一标识
	 * @param domainid
	 *            需要发送的用户组所在的域标识
	 * @param pmVO
	 *            需要发送的短信内容
	 * @throws Exception
	 */
	public abstract void doCreateByRole(String roleid, String domainid,
			PersonalMessageVO pmVO) throws Exception;

	/**
	 * 根据角色分组进行群发短信
	 * 
	 * @param roleid
	 *            角色组唯一标识
	 * @param domainid
	 *            需要发送的用户组所在的域标识
	 * @param senderid
	 *            发送者标识
	 * @param title
	 *            短信的标题
	 * @param content
	 *            短信的内容
	 * @throws Exception
	 */
	public abstract void doCreateByRole(String roleid, String domainid,
			String senderid, String title, String content) throws Exception;

	/**
	 * 根据部门组别群发短信
	 * 
	 * @param departmentid
	 *            部门唯一标识
	 * @param pmVO
	 *            需要发送的短信内容
	 * @throws Exception
	 */
	public abstract void doCreateByDepartment(String departmentid,
			PersonalMessageVO pmVO) throws Exception;

	/**
	 * 根据部门群发短信
	 * 
	 * @param departmentid
	 *            部门标识
	 * @param senderid
	 *            发送者标识
	 * @param title
	 *            短信标题
	 * @param content
	 *            短信内容
	 * @throws Exception
	 */
	public abstract void doCreateByDepartment(String departmentid,
			String senderid, String title, String content) throws Exception;

	/**
	 * 查找用户是否有新的站内短信
	 * 
	 * @param user
	 *            当前用户
	 * @return 短信数量
	 * @throws Exception
	 */
	public abstract int countNewMessages(String userId) throws Exception;
	
	/**
	 * 查找用户已读的站内短信
	 * 
	 * @param user
	 *            当前用户
	 * @return 短信数量
	 * @throws Exception
	 */
	public abstract int countIsReadMessages(String userId) throws Exception;

	public String[] getReceiverUserIdsByMessageBodyId(String bodyId)
			throws Exception;

	public abstract DataPackage<PersonalMessageVO> doNoRead(String id,
			ParamsTable params) throws Exception;

	public void doCreate(String senderid, String receiverid, String title,
			String content, String type) throws Exception;

	public void doCreate(String senderid, String receiverid, String title,
			String content) throws Exception;

	public void doGroupSend(Collection<UserVO> users, PersonalMessageVO pmVO)
			throws Exception;

	public void doCreateByDepartment(String departmentid, String senderid,
			String title, String content, String type) throws Exception;

	public void doCreateByRole(String roleid, String domainid, String senderid,
			String title, String content, String type) throws Exception;

	public void doCreateByUserIds(String[] ids, PersonalMessageVO vo)
			throws Exception;

	public PersonalMessageVO doSavePersonalMessageVO(PersonalMessageVO vo)
			throws Exception;
	
	public Collection<PersonalMessageVO> doSaveMorePersonalMessageVO(String[] ids,PersonalMessageVO vo)
			throws Exception;
	
	
	/**
	 * 根据条件查询已读信息的用户和未读信息的用户
	 * @param senderid		发送者ID
	 * @param receiverid	接收者ID(一个或多个拼接组成的String)
	 * @param bodyId		信息内容ID
	 * @return				由已读信息的用户和未读信息的用户拼接组成的String
	 * @throws Exception
	 */
	public String doFindAllReader(String senderid,String receiverid,String bodyId)
			throws Exception;

	/**
	 * 执行投票动作
	 * @param msgId 信息ID
	 * @param checkedOptionsId 用户选中选项的ID(一个或多个的拼接字符串)
	 * @param userId 投票userId
	 * @throws Exception
	 */
	public void doVote(String msgId,String checkedOptionsId,String userId)throws Exception;
	
	/**
	 * 根据信息ID把信息从‘回收箱’中‘回撤’
	 * @param msgIds		信息IDS
	 * @throws Exception
	 */
	public void doRetracement(String[] msgIds) throws Exception;
	
	/**
	 * 根据信息ID把信息从‘回收箱’中‘回撤’
	 * @param msgId		信息ID
	 * @throws Exception
	 */
	public void doSingleRetracement(String msgId) throws Exception;

	/**
	 * 删除发件箱中的附件后更新数据库信息
	 * @param msgid  personalmessageId
	 * @param attid  附件ID
	 * @throws Exception
	 */
	public abstract void doUpdateMsgAfterDeleteAttachment(String msgid,String attid)throws Exception;
	
	/**
	 * 根据用户查询通知公告的集合
	 * @param user
	 * 		用户
	 * @return
	 * 		公告的集合
	 * @throws Exception
	 */
	public Collection<PersonalMessageVO> doQueryAnnouncementsByUser(WebUser user)throws Exception;
			
	/**
	 * 根据时间查找当前用户最新的信息
	 * @param userid
	 * @param date
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int doQueryNewMessagWithTime(String userid,String date, ParamsTable params) throws Exception;
}