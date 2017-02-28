package cn.myapps.core.xmpp;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.packet.IQ;

import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.user.ejb.BaseUser;

public abstract class XMPPNotification extends IQ {
	public final static String NODENAME = "notification";

	protected DomainVO domain;
	protected Collection<BaseUser> receivers;
	protected BaseUser sender;


	public BaseUser getSender() {
		return sender;
	}

	public void setSender(BaseUser sender) {
		this.sender = sender;
	}

	public XMPPNotification() {
		this.receivers = new ArrayList<BaseUser>();
	}
	
	public String getChildElementXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<notification xmlns=\"obpm:iq:notification\">");
		buffer.append(getInnerXML());
		buffer.append("</notification>");

		return buffer.toString();
	}
	
	/**
	 * 添加接收人
	 * @param userVO
	 */
	public void addReceiver(BaseUser userVO){
		this.receivers.add(userVO);
	}
	
	public Collection<BaseUser> getReceivers() {
		return receivers;
	}

	/**
	 * 获取提醒内容
	 * @return
	 */
	public abstract String getInnerXML();
	
	/**
	 * 发送packet
	 * @throws Exception
	 */
	public void send() throws Exception {
		XMPPSender.getInstance().processNotification(this);
	}

	/**
	 * 将接收者集合转换为字符串
	 * @return
	 */
	public abstract String toStrReceivers();
	
	/**
	 * xmpp所需的登录账号
	 * @return
	 * @throws Exception
	 */
	public abstract String getSenderName() throws Exception;
	
	/**
	 * xmpp所需的登录密码
	 * @return
	 * @throws Exception
	 */
	public abstract String getSenderPassword() throws Exception;
	
	/**
	 * 
	 * @param serviceName
	 * @return
	 */
	public abstract XMPPNotification getClone(String serviceName);
}
