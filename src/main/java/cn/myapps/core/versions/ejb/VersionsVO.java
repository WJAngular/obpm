package cn.myapps.core.versions.ejb;

import java.util.Date;

import cn.myapps.base.dao.ValueObject;

/**
 * obpm版本信息对象
 * @author Ivan
 *
 */
public class VersionsVO extends ValueObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 升级类型1：程序(源码)升级
	 */
	public static final int TYPE_SOURCECODE_UPGRADE = 1;
	
	/**
	 * 升级类型2：数据升级
	 */
	public static final int TYPE_DATA_UPGRADE = 2;
	
	private String id;
	
	/**
	 * 版本名称(如：R2.5-sp8)
	 */
	private String version_name;
	
	/**
	 * 版本号(如：16913)
	 */
	private String version_number;
	
	/**
	 * 升级时间
	 */
	private Date upgrade_date;
	
	/**
	 * 升级类型
	 * 1：程序(源码)升级
	 * 2：数据升级
	 */
	private int type;
	
	/**
	 * 升级备注信息
	 */
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion_name() {
		return version_name;
	}

	public void setVersion_name(String versionName) {
		version_name = versionName;
	}

	public String getVersion_number() {
		return version_number;
	}

	public void setVersion_number(String versionNumber) {
		version_number = versionNumber;
	}

	public Date getUpgrade_date() {
		return upgrade_date;
	}

	public void setUpgrade_date(Date upgradeDate) {
		upgrade_date = upgradeDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
