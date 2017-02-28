package cn.myapps.km.log.ejb;

import java.util.Date;

import cn.myapps.km.base.ejb.NObject;

/**
 *文件浏览、下载、收藏、分享的日记
 * @author linda
 *
 */
public class Logs extends NObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3797218546613480853L;
	
	/**操作类型-检索**/
	public static final String OPERATION_TYPE_SELECT = "SELECT";
	
	/**操作类型-上传**/
	public static final String OPERATION_TYPE_UPLOAD = "UPLOAD";

	/**操作类型-预览**/
	public static final String OPERATION_TYPE_VIEW = "VIEW";
	
	/**操作类型-下载**/
	public static final String OPERATION_TYPE_DOWNLOAD = "DOWNLOAD";
	
	/**操作类型-收藏**/
	public static final String OPERATION_TYPE_FAVORITE = "FAVORITE";
	
	/**操作类型-分享**/
	public static final String OPERATION_TYPE_SHARE = "SHARE";
	
	/**操作类型-推荐**/
	public static final String OPERATION_TYPE_RECOMMEND = "RECOMMEND";
	
	/**操作类型-删除**/
	public static final String OPERATION_TYPE_DELETE = "DELETE";
	
	/**操作类型-重命名**/
	public static final String OPERATION_TYPE_RENAME = "RENAME";
	
	/**操作类型-移动**/
	public static final String OPERATION_TYPE_MOVE = "MOVE";
	
	/**操作类型-新建**/
	public static final String OPERATION_TYPE_CREATE = "CREATE";
	
	/**文件操作**/
	public static final int OPERATION_FILE = 1;
	
	/**目录操作**/
	public static final int OPERATION_DIRECTORY = 2;
	
	/**
	 * 操作类型
	 */
	private String operationType;
	
	/**
	 * 操作类型（文件或者目录操作）
	 */
	private int operationFileOrDirectory;
	 
	/**
	 * 文件的ID
	 */
	private String fileId;
	
	/**
	 * 文件的名字
	 */
	private String fileName;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
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
	 * 操作用户所在部门ID
	 */
	private String departmentId;
	
	/**
	 * 操作用户所在部门名称
	 */
	private String departmentName;
	
	/**
	 * 操作文件的上传者名字
	 */
	private String creator;
	
	/**
	 * 浏览次数
	 */
	private int views;
	
	/**
	 * 下载次数
	 */
	private int downloads;
	
	/**
	 * 收藏次数
	 */
	private int favorites;
	
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
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
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public int getDownloads() {
		return downloads;
	}
	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}
	public int getFavorites() {
		return favorites;
	}
	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}
	public int getOperationFileOrDirectory() {
		return operationFileOrDirectory;
	}
	public void setOperationFileOrDirectory(int operationFileOrDirectory) {
		this.operationFileOrDirectory = operationFileOrDirectory;
	}
	
	
	
	
}
 
