package cn.myapps.core.personalmessage.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.permission.ejb.PermissionPackage;
import cn.myapps.core.personalmessage.dao.HibernatePersonalMessageDAO;
import cn.myapps.core.personalmessage.dao.PersonalMessageDAO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.notifications.action.NotificationHelper;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class PersonalMessageProcessBean extends
		AbstractDesignTimeProcessBean<PersonalMessageVO> implements
		PersonalMessageProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6227715332742260082L;

	/**
	 * 发送单条站内短信
	 * 
	 * @param vo
	 *            短信内容
	 */
	public void doCreate(ValueObject vo) throws Exception {

		PersonalMessageVO senderVO = (PersonalMessageVO) vo;

		senderVO.setSendDate(new Date());

		if (senderVO.getBody() == null) {
			throw new OBPMValidateException("No send content!");
		}
		doCreateMessageBody(senderVO.getBody());

		try {
			PersonalMessageVO rever = new PersonalMessageVO();
			PropertyUtils.copyProperties(rever, senderVO);
			rever.setOwnerId(rever.getReceiverId());
			rever.setRead(false);
			rever.setInbox(true);
			super.doCreate(rever);

			senderVO.setOwnerId(senderVO.getSenderId());
			senderVO.setRead(true);
			senderVO.setOutbox(true);
			super.doCreate(senderVO);
			
			NotificationHelper.addNewMessageCount4User(rever.getReceiverId());
		} catch (Exception e) {
			doRemoveMessageBody(senderVO.getBody());
			throw e;
		}
		PermissionPackage.clearCache();
	}

	/**
	 * 发送单条站内短信,并返回该短信
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public PersonalMessageVO doSavePersonalMessageVO(PersonalMessageVO vo)
			throws Exception {

		PersonalMessageVO senderVO = (PersonalMessageVO) vo;

		senderVO.setSendDate(new Date());

		if (senderVO.getBody() == null) {
			throw new OBPMValidateException("No send content!");
		}
		doCreateMessageBody(senderVO.getBody());
		PersonalMessageVO rtn = null;

		try {
			PersonalMessageVO rever = new PersonalMessageVO();
			PropertyUtils.copyProperties(rever, senderVO);
			rever.setOwnerId(rever.getReceiverId());
			rever.setRead(false);
			rever.setInbox(true);
			super.doCreate(rever);
			
			NotificationHelper.addNewMessageCount4User(rever.getReceiverId());

			senderVO.setOwnerId(senderVO.getSenderId());
			senderVO.setRead(true);
			senderVO.setOutbox(true);
			super.doCreate(senderVO);
			rtn = rever;
		} catch (Exception e) {
			doRemoveMessageBody(senderVO.getBody());
			throw e;
		}
		PermissionPackage.clearCache();
		return rtn;
	}
	
	/**
	 * 发送多条站内短信,并返回该短信
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Collection<PersonalMessageVO> doSaveMorePersonalMessageVO(String[] ids,
			PersonalMessageVO vo) throws Exception {
		Collection<PersonalMessageVO> receiverList = new ArrayList<PersonalMessageVO>();
		
		PersonalMessageVO senderVO = (PersonalMessageVO) vo;

		senderVO.setSendDate(new Date());

		if (senderVO.getBody() == null) {
			throw new OBPMValidateException("No send content!");
		}
		super.doCreate(senderVO);
		try {
			for(int i = 0;i<ids.length;i++){
				PersonalMessageVO rever = new PersonalMessageVO();
				PropertyUtils.copyProperties(rever, senderVO);
				rever.setId(null);
				rever.setReceiverId(ids[i]);
				rever.setOwnerId(ids[i]);
				rever.setOutbox(false);
				rever.setRead(false);
				rever.setInbox(true);
				super.doCreate(rever);
				NotificationHelper.addNewMessageCount4User(rever.getReceiverId());
				receiverList.add(rever);
			}
		} catch (Exception e) {
			doRemoveMessageBody(senderVO.getBody());
			throw e;
		}
		PermissionPackage.clearCache();
		return receiverList;
	}
	
	private void doCreateMessageBody(MessageBody body) throws Exception {
		if (body != null && StringUtil.isBlank(body.getId())) {
			MessageBodyProcess bodyProcess = (MessageBodyProcess) ProcessFactory
					.createProcess(MessageBodyProcess.class);
			bodyProcess.doCreate(body);
		}
	}

	private void doRemoveMessageBody(MessageBody body) throws Exception {
		if (body != null && !StringUtil.isBlank(body.getId())) {
			MessageBodyProcess bodyProcess = (MessageBodyProcess) ProcessFactory
					.createProcess(MessageBodyProcess.class);
			bodyProcess.doRemove(body);
		}
	}

	/**
	 * 根据内容群发短信
	 * 
	 * @param messages
	 *            短信内容的集合
	 */
	public void doCreate(Collection<ValueObject> messages) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			for (Iterator<ValueObject> iterator = messages.iterator(); iterator
					.hasNext();) {
				PersonalMessageVO obj = (PersonalMessageVO) iterator.next();
				doCreate(obj);
			}
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}
		PermissionPackage.clearCache();
	}

	public void doCreate(String senderid, String receiverid, String title,
			String content) throws Exception {
		doCreate(senderid, receiverid, title, content, PersonalMessageVO.MESSAGE_TYPE_PERSONALMESSAGE);
	}

	public void doCreate(String senderid, String receiverid, String title,
			String content, String type) throws Exception {
		try {
			PersonalMessageVO sendVO = createVO(senderid, receiverid, title,
					content, type);
			doCreate(sendVO);
		} catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}

		PermissionPackage.clearCache();
	}

	public void doGroupSend(Collection<UserVO> users, PersonalMessageVO pmVO)
			throws Exception {
		sendMsgByUser(users, pmVO);
	}

	private PersonalMessageVO createVO(String senderid, String receiverid,
			String title, String content, String type) {
		PersonalMessageVO vo = new PersonalMessageVO();
		vo.setReceiverId(receiverid);
		vo.setSendDate(new Date());
		vo.setSenderId(senderid);

		MessageBody body = new MessageBody();
		body.setContent(content);
		body.setTitle(title);

		vo.setType(type);
		vo.setBody(body);
		return vo;
	}

	public void doUpdate(ValueObject vo) throws Exception {
		PersonalMessageVO pmVO = (PersonalMessageVO) vo;

		if(!pmVO.isRead()){
			NotificationHelper.subtractNewMessageCount4User(pmVO.getReceiverId());
		}
		pmVO.setRead(true);

		super.doUpdate(pmVO);
	}

	/**
	 * 将单条站内短信移至回收箱
	 */
	public void doSendToTrash(ValueObject vo) throws Exception {
		PersonalMessageVO pmVO = (PersonalMessageVO) vo;
		pmVO.setInbox(false);
		pmVO.setTrash(true);
		pmVO.setOutbox(false);
		this.doUpdate(pmVO);
	}

	/**
	 * 将一到多条站内短信移至回收箱
	 */
	public void doSendToTrash(String[] msgIds) throws Exception {
		for (int i = 0; i < msgIds.length; i++) {
			PersonalMessageVO pmVO = (PersonalMessageVO) doView(msgIds[i]);
			if (pmVO != null) {
				pmVO.setTrash(true);
				pmVO.setInbox(false);
				pmVO.setOutbox(false);
				this.doUpdate(pmVO);			
			}
		}
	}

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
	public DataPackage<PersonalMessageVO> doInbox(String userid,
			ParamsTable params) throws Exception {
		return ((PersonalMessageDAO) getDAO()).queryInBox(userid, params);
	}

	/**
	 * 将指定的站内短信删除
	 * 
	 * @param pk
	 *            站内短信标识
	 */
	public void doRemove(String pk) throws Exception {
		super.doRemove(pk);
	}

	protected IDesignTimeDAO<PersonalMessageVO> getDAO() throws Exception {
		return (PersonalMessageDAO) DAOFactory
				.getDefaultDAO(PersonalMessageVO.class.getName());
	}

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
	public DataPackage<PersonalMessageVO> doOutbox(String userid,
			ParamsTable params) throws Exception {
		return ((PersonalMessageDAO) getDAO()).queryOutbox(userid, params);
	}

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
	public DataPackage<PersonalMessageVO> doTrash(String userid,
			ParamsTable params) throws Exception {
		return ((HibernatePersonalMessageDAO) getDAO()).queryTrash(userid,
				params);
	}

	/**
	 * 根据部门组别群发短信
	 * 
	 * @param departmentid
	 *            部门唯一标识
	 * @param pmVO
	 *            需要发送的短信内容
	 * @throws Exception
	 */
	public void doCreateByDepartment(String departmentid, PersonalMessageVO pmVO)
			throws Exception {
		UserProcess uprocess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		ParamsTable params = new ParamsTable();
		params.setParameter("sm_userDepartmentSets.departmentId", departmentid);
		DataPackage<UserVO> package1 = uprocess.doQuery(params);
		Collection<UserVO> users = package1.datas;
		sendMsgByUser(users, pmVO);
	}

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
	public void doCreateByDepartment(String departmentid, String senderid,
			String title, String content) throws Exception {
		doCreateByDepartment(departmentid, senderid, title, content, PersonalMessageVO.MESSAGE_TYPE_PERSONALMESSAGE);

	}

	public void doCreateByDepartment(String departmentid, String senderid,
			String title, String content, String type) throws Exception {
		PersonalMessageVO vo = createVO(senderid, null, title, content, type);
		doCreateByDepartment(departmentid, vo);
	}

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
	public void doCreateByRole(String roleid, String domainid,
			PersonalMessageVO pmVO) throws Exception {
		UserProcess uprocess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		// DataPackage<UserVO> package1 = uprocess.queryOutOfRole(new
		// ParamsTable(), roleid);
		// Collection<UserVO> users = package1.datas;
		Collection<UserVO> users = uprocess.getDatasByGroup(roleid, domainid);
		sendMsgByUser(users, pmVO);
	}

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
	public void doCreateByRole(String roleid, String domainid, String senderid,
			String title, String content) throws Exception {
		doCreateByRole(roleid, domainid, senderid, title, content, PersonalMessageVO.MESSAGE_TYPE_PERSONALMESSAGE);
	}

	public void doCreateByRole(String roleid, String domainid, String senderid,
			String title, String content, String type) throws Exception {
		PersonalMessageVO vo = createVO(senderid, null, title, content, type);
		doCreateByRole(roleid, domainid, vo);
	}

	/**
	 * 根据用户集合发送站内短信
	 * 
	 * @param users
	 *            需要发送的用户集合
	 * @param pmVO
	 *            需要发送的短信内容
	 * @throws Exception
	 */
	private void sendMsgByUser(Collection<UserVO> users, PersonalMessageVO pmVO)
			throws Exception {
		sendMsgByUser(users, null, pmVO);
	}

	/**
	 * 根据用户集合发送站内短信
	 * 
	 * @param users
	 *            需要发送的用户集合
	 * @param pmVO
	 *            需要发送的短信内容
	 * @throws Exception
	 */
	protected void sendMsgByUser(Collection<UserVO> users, String senderid,
			PersonalMessageVO pmVO) throws Exception {
		Collection<ValueObject> msgs = new ArrayList<ValueObject>();
		if (users != null) {
			if (!StringUtil.isBlank(senderid)) {
				pmVO.setSenderId(senderid);
			}
			pmVO.setSendDate(new Date());
			doCreateMessageBody(pmVO.getBody());
			StringBuffer reUserId = new StringBuffer();
			for (Iterator<UserVO> iter = users.iterator(); iter.hasNext();) {
				UserVO user = iter.next();
				PersonalMessageVO msg = new PersonalMessageVO();
				PropertyUtils.copyProperties(msg, pmVO);
				msg.setReceiverId(user.getId());
				msg.setOwnerId(user.getId());
				msg.setInbox(true);
				NotificationHelper.addNewMessageCount4User(msg.getReceiverId());
				if (reUserId.length() > 2) {
					reUserId.append(",").append(user.getId());
				} else {
					reUserId.append(user.getId());
				}
				msgs.add(msg);
			}
			pmVO.setOwnerId(pmVO.getSenderId());
			pmVO.setReceiverId(reUserId.toString());
			pmVO.setRead(true);
			pmVO.setOutbox(true);
			try {
				super.doCreate(pmVO);
				super.doCreate(msgs);
			} catch (Exception e) {
				doRemoveMessageBody(pmVO.getBody());
				throw e;
			}
		}
	}

	public int countNewMessages(String userId) throws Exception {

		return ((HibernatePersonalMessageDAO) getDAO())
				.countNewMessages(userId);
	}

	public int countIsReadMessages(String userId) throws Exception {

		return ((HibernatePersonalMessageDAO) getDAO())
				.countIsReadMessages(userId);
	}
	
	public static void main(String[] args) throws Exception {
		MessageBody body = new MessageBody();
		body.setContent("asdfasf");

		PersonalMessageVO vo = new PersonalMessageVO();
		vo.setBody(body);
		vo.setDomainid("asdfasf");
		PersonalMessageProcessBean bean = new PersonalMessageProcessBean();
		try {
			bean.doCreate(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String departmentid = "11de-c138-7831ea17-9a62-8bacb70a86e1";
		String senderid = "11de-c13a-0cf76f8b-a3db-1bc87eaaad4c";
		String roleid = "01aec32a-6826-d440-86ee-7783c0983ba5";
		bean.doCreateByDepartment(departmentid, senderid, "dinga",
				"teafasdfasdfasdf");
		bean.doCreateByRole(roleid, "11de-c138-782d2f26-9a62-8bacb70a86e1",
				senderid, "TTTTTT", "content");
	}

	public String[] getReceiverUserIdsByMessageBodyId(String bodyId)
			throws Exception {
		return ((PersonalMessageDAO) getDAO())
				.getReceiverUserIdsByMessageBodyId(bodyId);
	}

	public DataPackage<PersonalMessageVO> doNoRead(String id, ParamsTable params)
			throws Exception {
		return ((PersonalMessageDAO) getDAO()).queryNewMessage(id, params);
	}

	public void doCreateByUserIds(String[] ids, PersonalMessageVO vo)
			throws Exception {
		Collection<ValueObject> receiverList = new ArrayList<ValueObject>();
		vo.setSendDate(new Date());
		//把信息ID置null从而doCreate
		vo.setId(null);
		if (ids != null && ids.length > 0) {
			StringBuffer reUserId = new StringBuffer();
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				if (!StringUtil.isBlank(id)) {
					PersonalMessageVO receiverVO = new PersonalMessageVO();
					PropertyUtils.copyProperties(receiverVO, vo);
					receiverVO.setReceiverId(id);
					receiverVO.setOwnerId(id);
					receiverVO.setInbox(true);
					receiverList.add(receiverVO);
					if (reUserId.length() > 2) {
						reUserId.append(",").append(id);
					} else {
						reUserId.append(id);
					}
				} else {
					throw new Exception(
							"{*[Receiver]*}{*[core.superuser.notexist]*}");
				}
			}
			doCreateMessageBody(vo.getBody());
			vo.setOwnerId(vo.getSenderId());
			vo.setRead(true);
			vo.setOutbox(true);
			vo.setReceiverId(reUserId.toString());
			try {
				super.doCreate(receiverList);
				super.doCreate(vo);
				for (Iterator<ValueObject> iterator = receiverList.iterator(); iterator
						.hasNext();) {
					PersonalMessageVO m = (PersonalMessageVO)iterator.next();
					NotificationHelper.addNewMessageCount4User(m.getOwnerId());
				}
			} catch (Exception e) {
				doRemoveMessageBody(vo.getBody());
				throw e;
			}
		}
	}
	
	public String doFindAllReader(String senderid,String receiverid,String bodyId)
		throws Exception {
		if(receiverid != null){
			String isReadReceiverids = "";
			String noReadReceiverids = "";
			String[] receiverids = receiverid.split(",");
			for(int i = 0;i < receiverids.length; i++){
				PersonalMessageVO pmvo = (PersonalMessageVO)((PersonalMessageDAO) getDAO())
						.queryPersonalMessageVO(senderid, receiverids[i] ,bodyId);
				if(pmvo != null){
					if((pmvo.isInbox() || pmvo.isTrash()) && !pmvo.isRead()){
						noReadReceiverids = noReadReceiverids + pmvo.getReceiverId() +",";
					}else{
						isReadReceiverids = isReadReceiverids + pmvo.getReceiverId() +",";
					}
				}
			}
			return isReadReceiverids +";"+ noReadReceiverids;
		}
		return null;
	}

	public void doVote(String msgId,String checkedOptionsId,String userId) throws Exception {
		if(!StringUtil.isBlank(checkedOptionsId)){
			String[] ids = checkedOptionsId.split(";");
			for(int i = 0;i < ids.length; i++){
				VoteOptionsVO vo = (VoteOptionsVO) new VoteOptionsProcessBean().findVoteOptionsVOById(ids[i]);
				if(!StringUtil.isBlank(vo.getVoters())){
					String newVoters = vo.getVoters()+";"+userId;
					vo.setVoters(newVoters);
				}else{
					vo.setVoters(userId);
				}
				vo.setVotes(vo.getVotes()+1);
				super.doUpdate(vo);
			}
			PersonalMessageVO pmVO = (PersonalMessageVO)getDAO().find(msgId);
			//修改分隔符以便回显
			String replaceSeparator = checkedOptionsId.replace(';', ',');
			pmVO.setCheckedOptionsId(replaceSeparator);
			super.doUpdate(pmVO);
		}
	}

	public void doRetracement(String[] msgs) throws Exception {
		List<ValueObject> cols = new ArrayList<ValueObject>();
		for (int i = 0; i < msgs.length; i++) {
			PersonalMessageVO pmVO = (PersonalMessageVO) doView(msgs[i]);
			if (pmVO != null) {
				pmVO.setTrash(false);
				cols.add(pmVO);
			}
		}
		super.doUpdate(cols);
	}

	public void doSingleRetracement(String msgId) throws Exception {
		PersonalMessageVO pmVO = (PersonalMessageVO) doView(msgId);
		if (pmVO != null) {
			pmVO.setTrash(false);
		}
		super.doUpdate(pmVO);
	}

	/**
	 * 删除发件箱中的附件后更新数据库信息
	 */
	public void doUpdateMsgAfterDeleteAttachment(String msgid,String attid) throws Exception {
		PersonalMessageVO pmvo = (PersonalMessageVO) doView(msgid);
		if(pmvo != null){
			String attachmentid = pmvo.getAttachmentId();
			if(! StringUtil.isBlank(attachmentid)){
				String newAttidString = this.changeAttachmentId(attachmentid, attid);
				PersonalMessageVO vo = new PersonalMessageVO();
				PropertyUtils.copyProperties(vo, pmvo);
				vo.setAttachmentId(newAttidString);
				super.doUpdate(vo);
			}
			Collection<ValueObject> personalmessageList = new ArrayList<ValueObject>();
			if(! StringUtil.isBlank(pmvo.getReceiverId())){
				String[] receiverids = pmvo.getReceiverId().split(",");
				for(int i = 0;i < receiverids.length; i++){
					PersonalMessageVO pm = findPersonalMessageVO(pmvo.getSenderId(),receiverids[i],pmvo.getBody().getId());
					String newAttid = this.changeAttachmentId(pm.getAttachmentId(), attid);
					PersonalMessageVO vo = new PersonalMessageVO();
					PropertyUtils.copyProperties(vo, pm);
					vo.setAttachmentId(newAttid);
					personalmessageList.add(vo);
				}
			}
			super.doUpdate(personalmessageList);
		}else{
			throw new Exception("{*[更新数据失败!]*}");
		}
	}
	
	/**
	 * 根据条件查找PersonalMessageVO
	 * @param senderid
	 * @param receiverid
	 * @param senddate
	 * @return
	 * @throws Exception
	 */
	public PersonalMessageVO findPersonalMessageVO(String senderid,String receiverid,String bodyId) throws Exception{
		PersonalMessageVO pmvo = (PersonalMessageVO)((PersonalMessageDAO) getDAO())
			.queryPersonalMessageVO(senderid, receiverid ,bodyId);
		return pmvo;
	}
	
	/**
	 * 去除已删除附件的ID，重新拼接AttachmentId
	 * @param attachmentid
	 * @param attid
	 * @return
	 */
	public String changeAttachmentId(String attachmentid ,String attid){
		String[] attids = attachmentid.split(";");
		StringBuffer newAttid = new StringBuffer(); 
		for(int i = 0;i < attids.length; i++){
			if(! attids[i].equalsIgnoreCase(attid)){
				newAttid.append(attids[i]).append(";");
			}
		}
		String newAttids = newAttid.toString();
		String newAttidString = newAttids.endsWith(";")? newAttid.substring(0, newAttid.length()-1):newAttids;
		return newAttidString;
	}
	
	/**
	 * 根据用户查询通知公告的集合
	 * @param user
	 * 		用户
	 * @return
	 * 		公告的集合
	 * @throws Exception
	 */
	public Collection<PersonalMessageVO> doQueryAnnouncementsByUser(WebUser user)throws Exception {
		return ((PersonalMessageDAO)getDAO()).queryAnnouncementsByUser(user);
	}
	
	/**
	 * 根据时间查找当前用户最新的信息
	 * @param userid
	 * @param date
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int doQueryNewMessagWithTime(String userid,String date, ParamsTable params)
			throws Exception {
		return ((PersonalMessageDAO)getDAO()).queryNewMessagWithTime(userid, date, params);
	}
}
