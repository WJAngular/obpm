package cn.myapps.util.notification;

import cn.myapps.support.weixin.WeixinServiceProxy;

/**
 * 微信图文消息
 * @author Happy
 *
 */
public class RichWeixinMessage implements Message {
	
	/**
	 * 用户账号列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 */
	private String touser;
	/**
	 * 消息标题
	 */
	private String title;
	/**
	 * 消息描述
	 */
	private String description;
	/**
	 * 消息url
	 */
	private String url;
	/**
	 * 图片url
	 */
	private String picUrl;
	/**
	 * 企业域id
	 */
	private String domainId;
	/**
	 * 发送目标软件id（null 表示发送到企业小助手
	 */
	private String appid;
	

	public RichWeixinMessage(String touser, String title, String description,
			String url, String picUrl, String domainId, String appid) {
		super();
		this.touser = touser;
		this.title = title;
		this.description = description;
		this.url = url;
		this.picUrl = picUrl;
		this.domainId = domainId;
		this.appid = appid;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
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

	@Override
	public void send() {
		try {
			WeixinServiceProxy.sendRichTextMessage(touser, title, description, url, picUrl, domainId, appid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
