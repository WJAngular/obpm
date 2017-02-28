package cn.myapps.km.report.ejb;

import java.util.Date;

/**
 * @author Happy
 *
 */
public class ReportItem {
	
	/**
	 * 操作类型
	 */
	private String operationType;
	 
	/**
	 * 文件的ID
	 */
	private String fileId;
	
	/**
	 * 文件的名字
	 */
	private String fileName;
	
	/**
	 * 文件状态（私有、公开）
	 */
	private int fileState;
	
	/**
	 * 用户的名字
	 */
	private String userName;
	
	/**
	 * 操作的日期
	 */
	private Date operationDate;
	
	/**
	 * 用户IP地址
	 */
	private String userIp;
	
	/**
	 * 操作的内容
	 */
	private String operationContent;
	
	/**
	 * 操作用户所在部门名称
	 */
	private String departmentName;
	
	/**
	 * 操作文件的上传者名字
	 */
	private String creator;
	
	/**
	 * 文件的主类别
	 */
	private String rootCategory;
	
	/**
	 * 文件的子类别
	 */
	private String subCategory;

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileState() {
		return fileState;
	}

	public void setFileState(int fileState) {
		this.fileState = fileState;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getOperationContent() {
		return operationContent;
	}

	public void setOperationContent(String operationContent) {
		this.operationContent = operationContent;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getRootCategory() {
		return rootCategory;
	}

	public void setRootCategory(String rootCategory) {
		this.rootCategory = rootCategory;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	

}
