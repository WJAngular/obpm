package cn.myapps.km.disk.ejb;

import java.util.Date;

import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.FileUtils;

/**
 * 抽象的文件(包含目录以及文档文件)
 * @author Richard
 *
 */
public class IFile extends NObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8264243977259737407L;
	
	/**
	 * 来自上传
	 */
	public static final int ORIGN_UPLOAD = 0;
	/**
	 * 来自分享
	 */
	public static final int ORIGN_SHARE = 1;
	/**
	 * 来自订阅
	 */
	public static final int ORIGN_SUBSCRIBE = 2;
	/**
	 * 来自转存
	 */
	public static final int ORIGN_REUPLOAD = 3;
	/**
	 * 来自收藏
	 */
	public static final int ORIGN_FAVORITE = 4;
	/**
	 * 来自推荐
	 */
	public static final int ORIGN_RECOMMEND = 5;
	/**
	 * 来自归档
	 */
	public static final int ORIGN_ARCHIVE = 6;
	
	public static final int STATE_PUBLIC = 1;
	public static final int STATE_PRIVATE = 2;
	public static final int STATE_ARCHIVE = 3;
	/**
	 * 类型
	 */
	private String fileType;
	
	/**
	 * 状态（私有、公开）
	 */
	private int state;
	
	private String ownerId;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	/**
	 * 大小(目录为null,文档文件有值)
	 */
	private long size;
	
	/**
	 * 来源（上传、分享、订阅）
	 */
	private int origin;
	
	/**
	 * 创建者名称
	 */
	private String creator;
	
	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public String getFileSize(){
		return FileUtils.FormetFileSize(size);
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	
}
