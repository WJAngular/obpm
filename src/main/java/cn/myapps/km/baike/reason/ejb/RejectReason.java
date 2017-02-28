package cn.myapps.km.baike.reason.ejb;

import java.util.Date;

import cn.myapps.km.base.ejb.NObject;

	/**
	 * 驳回原因
	 * @author Able
	 * 
	 */
public class RejectReason extends NObject {

	private static final long serialVersionUID = -8506336113995975325L;
	
	/**
	 * 内容ID
	 */
	private String contentId;
	
	/**
	 * 原因
	 */
	private String reason;
	
	/**
	 * 驳回时间
	 */
	private Date rejectTime;

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Date getRejectTime() {
		return rejectTime;
	}

	public void setRejectTime(Date rejectTime) {
		this.rejectTime = rejectTime;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println(obj);
		if(obj==null){
			return false;
		}else if(obj instanceof RejectReason){
			RejectReason reason=(RejectReason)obj;
			return (this.getId().equals(reason.getId()))
				 &&(this.getContentId().equals(reason.getContentId()))
				   &&(this.getReason().equals(reason.getReason()));
		}
		return false;
	}

	@Override
	public String toString() {
		return "Reason [getContentId()=" + getContentId() +"getReason()="+getReason()+"]";
	}

	
}
