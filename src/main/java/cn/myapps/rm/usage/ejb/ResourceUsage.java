package cn.myapps.rm.usage.ejb;

import java.util.Date;

import cn.myapps.rm.base.ejb.BaseObject;

/**
 * 资源占用
 * @author Happy
 *
 */
public class ResourceUsage extends BaseObject {

	private static final long serialVersionUID = -7840355528447431452L;
	
	/**
	 * 资源id
	 */
	private String resourceId;
	
	/**
	 * 资源名称
	 */
	private String resourceName;
	
	/**
	 * 资源编号
	 */
	private String resourceSerial;
	
	/**
	 * 占用方式（时段占用|周期性占用）
	 */
	private int usageMode;
	
	/**
	 * 使用人
	 */
	private String user;
	
	/**
	 * 使用人id
	 */
	private String userId;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 *开始时间
	 */
	private Date startDate;
	/**
	 *结束时间
	 */
	private Date endDate;
	
	/**
	 * 操作时间
	 */
	private Date createDate;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 是否生效
	 */
	private boolean effective;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceSerial() {
		return resourceSerial;
	}

	public void setResourceSerial(String resourceSerial) {
		this.resourceSerial = resourceSerial;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getUsageMode() {
		return usageMode;
	}

	public void setUsageMode(int usageMode) {
		this.usageMode = usageMode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isEffective() {
		return effective;
	}

	public void setEffective(boolean effective) {
		this.effective = effective;
	} 
	

}
