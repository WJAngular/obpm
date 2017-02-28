package cn.myapps.km.permission.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import cn.myapps.km.base.ejb.NObject;

/**
 * 授权记录
 * @author xiuwei
 *
 */
public class Permission extends NObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2036407200954976627L;
	/**
	 * 目录类型
	 */
	public static final int FILE_TYPE_DIR = 1;
	/**
	 * 文件类型
	 */
	public static final int FILE_TYPE_FILE =2;
	
	/**
	 * 文件类型（文件夹|文件）
	 */
	private int fileType;
	/**
	 * 文件ID
	 */
	private String fileId;
	/**
	 * 权限作用域（用户|角色）
	 */
	private String scope;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 角色Id
	 */
	private String roleId;
	
	/**
	 * 部门Id
	 */
	private String deptId;
	
	/**
	 * 
	 */
	private String ownerIds;
	
	/**
	 * 名称
	 */
	private String ownerNames;
	
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

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getOwnerIds() {
		return ownerIds;
	}

	public void setOwnerIds(String ownerIds) {
		this.ownerIds = ownerIds;
	}

	public String getOwnerNames() {
		return ownerNames;
	}

	public void setOwnerNames(String ownerNames) {
		this.ownerNames = ownerNames;
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
	
	/**
	 * 判断权限是否时间限制
	 * @return
	 */
	public boolean isTimeLimit(){
		boolean flag = false;
		if(startDate != null && endDate != null){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 判断授权是否生效
	 * @return true 生效  false 不生效
	 */
	public boolean isTimeAct(){
		boolean flag = false;
		if(startDate!=null && endDate!=null){
			if(startDate.before(new Date()) && endDate.after(new Date())){
				flag = true;
			}else{
				flag = false;
			}
		}else{
			flag = true;
		}
		return flag;
	}
}
