package cn.myapps.km.permission.ejb;

import java.util.Date;

import cn.myapps.km.base.ejb.NObject;

public class IFileAccess extends NObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3338742560202411663L;
	/**
	 * 作用域-用户
	 */
	public static final String SCOPE_USER = "user";
	/**
	 * 作用域-部门
	 */
	public static final String SCOPE_DEPT = "dept";
	/**
	 * 作用域-角色
	 */
	public static final String SCOPE_ROLE = "role";
	/**
	 * 作用域-部门和角色
	 */
	public static final String SCOPE_DEPT_ROLE = "deptAndrole";
	
	/**
	 * 不可读
	 */
	public static final int READ_MODE_FALSE = -1;
	/**
	 * 可读
	 */
	public static final int READ_MODE_TRUE = 1;
	/**
	 * 不可写
	 */
	public static final int WRITE_MODE_FALSE = -1;
	/**
	 * 可写
	 */
	public static final int WRITE_MODE_TRUE = 1;
	/**
	 * 不可下载
	 */
	public static final int DOWNLOAD_MODE_FALSE = -1;
	/**
	 * 可下载
	 */
	public static final int DOWNLOAD_MODE_TRUE = 1;
	/**
	 * 不可打印
	 */
	public static final int PRINT_MODE_FALSE = -1;
	/**
	 * 可打印
	 */
	public static final int PRINT_MODE_TRUE = 1;

	/**
	 * 权限作用域
	 */
	private String scope;
	 
	/**
	 * 作用目标ID
	 */
	private String ownerId;
	 
	/**
	 * 文件ID
	 */
	private String fileId;
	
	private String permissionId;
	
	/**
	 * 开始时间
	 */
	private Date startDate;
	
	/**
	 * 结束时间
	 */
	private Date endDate;
	 
	/**
	 * 阅读权限
	 */
	private int readMode;
	 
	/**
	 * 修改权限
	 */
	private int writeMode;
	 
	/**
	 * 下载权限
	 */
	private int downloadMode;
	 
	/**
	 * 打印权限
	 */
	private int printMode;
	 

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
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

	public int getReadMode() {
		return readMode;
	}

	public void setReadMode(int readMode) {
		this.readMode = readMode;
	}

	public int getWriteMode() {
		return writeMode;
	}

	public void setWriteMode(int writeMode) {
		this.writeMode = writeMode;
	}

	public int getDownloadMode() {
		return downloadMode;
	}

	public void setDownloadMode(int downloadMode) {
		this.downloadMode = downloadMode;
	}

	public int getPrintMode() {
		return printMode;
	}

	public void setPrintMode(int printMode) {
		this.printMode = printMode;
	}

}
