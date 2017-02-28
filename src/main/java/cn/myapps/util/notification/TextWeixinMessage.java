package cn.myapps.util.notification;

import cn.myapps.support.weixin.WeixinServiceProxy;

/**
 * 微信图文消息
 * @author Happy
 *
 */
public class TextWeixinMessage implements Message {
	
	/**
	 * 用户账号列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 */
	private String touser;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 企业域id
	 */
	private String domainId;
	/**
	 * 发送目标软件id（null 表示发送到企业小助手）
	 */
	private String appid;
	

	public TextWeixinMessage(String touser, String content, String domainId, String appid) {
		super();
		this.touser = touser;
		this.content = content;
		this.domainId = domainId;
		this.appid = appid;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public void send() {
		try {
			WeixinServiceProxy.sendTextMessage(touser, content, domainId, appid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
