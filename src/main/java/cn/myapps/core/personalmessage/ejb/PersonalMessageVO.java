package cn.myapps.core.personalmessage.ejb;

import java.io.Serializable;
import java.util.Date;

import cn.myapps.base.dao.ValueObject;

/**
 * @hibernate.class table="T_MESSAGE" batch-size="10" lazy="true"
 */
public class PersonalMessageVO extends ValueObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8549419681060378811L;

	/**
	 * 站内短信
	 */
	public static final String MESSAGE_TYPE_PERSONALMESSAGE = "0";
	
	/**
	 * 公告
	 */
	public static final String MESSAGE_TYPE_ANNOUNCEMENT = "1";

	/**
	 * 投票
	 */
	public static final String MESSAGE_TYPE_VOTE = "2";

	/**
	 * 问卷
	 */
	public static final String MESSAGE_TYPE_QUESTION = "3";
	
	private MessageBody body;
	
	/**
	 * 一个或多个附件上传路径组成的字符串
	 */
	private String attachments;
	
	/**
	 * 一个或多个附件ID组成的字符串
	 */
	private String attachmentId;

	
	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	/**
	 * 接收者标识
	 */
	private String receiverId;

	private String senderId;
	
	/**
	 * 类型：0表示站内短信，1表示公告，2表示投票，3表示问卷
	 */
	private String type;
	
	/**
	 * 从页面textarea获取的投票选项文本
	 */
	private String voteOptions;
	
	/**
	 * 由投票选项ID组成的字符串
	 */
	private String voteOptionsId;
	
	/**
	 * 选项的类型：0表示radio，1表示checkbox
	 */
	private String radioOrCheckbox;

	/**
	 * 用户投票时选中的选项ID
	 */
	private String checkedOptionsId;
	
	public String getRadioOrCheckbox() {
		return radioOrCheckbox;
	}

	public void setRadioOrCheckbox(String radioOrCheckbox) {
		this.radioOrCheckbox = radioOrCheckbox;
	}

	public String getCheckedOptionsId() {
		return checkedOptionsId;
	}

	public void setCheckedOptionsId(String checkedOptionsId) {
		this.checkedOptionsId = checkedOptionsId;
	}

	public String getType() {
		if(type == null)
			type = "0";
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVoteOptions() {
		return voteOptions;
	}

	public void setVoteOptions(String voteOptions) {
		this.voteOptions = voteOptions;
	}

	public String getVoteOptionsId() {
		return voteOptionsId;
	}

	public void setVoteOptionsId(String voteOptionsId) {
		this.voteOptionsId = voteOptionsId;
	}

	/**
	 * 已读信息用户的ID组成的字符串
	 */
	private String isReadReceiverId;
	
	/**
	 * 未读信息用户的ID组成的字符串
	 */
	private String noReadReceiverId;

	/**
	 * 是否已读
	 */
	private boolean read;

	/**
	 * 回收箱
	 */
	private boolean trash;

	private boolean inbox;
	private boolean outbox;

	/** 信息所有者 */
	private String ownerId;

	private Date sendDate;
	
	/**
	 * 发布名义
	 */
	private String sendTitular;

	/**
	 * 获取接收者标识
	 * 
	 * @return 接收者标识
	 */
	public String getReceiverId() {
		return receiverId;
	}

	/**
	 * 设置接收者标识
	 * 
	 * @param receiverId
	 *            接收者标识
	 */
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * 获取是否已读
	 * 
	 * @return 是否已读
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * 设置是否已读
	 * 
	 * @param read
	 *            是否已读
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * 获取是否是回收箱
	 * 
	 * @return 回收箱
	 */
	public boolean isTrash() {
		return trash;
	}

	/**
	 * 设置是否是回收箱
	 * 
	 * @param trash
	 *            回收箱
	 */
	public void setTrash(boolean trash) {
		this.trash = trash;
	}

	public MessageBody getBody() {
		return body;
	}

	public void setBody(MessageBody body) {
		this.body = body;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public boolean isInbox() {
		return inbox;
	}

	public void setInbox(boolean inbox) {
		this.inbox = inbox;
	}

	public boolean isOutbox() {
		return outbox;
	}

	public void setOutbox(boolean outbox) {
		this.outbox = outbox;
	}

	public String getIsReadReceiverId() {
		return isReadReceiverId;
	}

	public void setIsReadReceiverId(String isReadReceiverId) {
		this.isReadReceiverId = isReadReceiverId;
	}

	public String getNoReadReceiverId() {
		return noReadReceiverId;
	}

	public void setNoReadReceiverId(String noReadReceiverId) {
		this.noReadReceiverId = noReadReceiverId;
	}

	public String getSendTitular() {
		return sendTitular;
	}

	public void setSendTitular(String sendTitular) {
		this.sendTitular = sendTitular;
	}
}