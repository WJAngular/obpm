package cn.myapps.km.disk.ejb;

import java.util.Collection;


/**
 * 目录
 * @author xiuwei
 *
 */
public class NDir extends IFile{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8863682234298475660L;
	
	/**
	 * 文件夹类型(普通,如:用户新建)
	 */
	public static final int TYPE_NORMAL = 1;
	
	/**
	 * 文件夹类型(系统,如:系统创建)
	 */
	public static final int TYPE_SYSTEM = 2;
	
	/**
	 * 文件夹类型(归档)
	 */
	public static final int TYPE_ARCHIVE = 3;
	
	/**
	 * 我的文档
	 */
	public static final String MY_DOCUMENT = "{*[cn.myapps.km.dir.my_documents]*}";
	
	/**
	 * 我的收藏
	 */
	public static final String MY_COLLECTION = "{*[cn.myapps.km.dir.my_favorites]*}";
	
	/**
	 * 用户分享
	 */
	public static final String USER_SHARE = "{*[cn.myapps.km.dir.users_share]*}";
	
	/**
	 * 推荐文档
	 */
	public static final String RECOMMEND_DOCUMENT = "{*[cn.myapps.km.dir.recommended_documentation]*}";
	
	
	/**
	 * 名称
	 */
	private String name;
	 
	/**
	 * 所有者ID
	 */
	private String ownerId;
	
	/**
	 * 父目录ID
	 */
	private String parentId;
	
	/**
	 * 所属网盘的ID
	 */
	private String nDiskId;
	
	/**
	 * 路径
	 */
	private String path;
	 
	/**
	 * 包含的文件
	 */
	private Collection<NFile> nFile;
	 
	/**
	 * 包含的子目录
	 */
	private Collection<NDir> nDir;
	 
	/**
	 * 所属网盘
	 */
	private NDisk nDisk;
	
	/**
	 * 文件夹类型(普通,系统)
	 */
	private int type;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NDisk getnDisk() {
		return nDisk;
	}

	public void setnDisk(NDisk nDisk) {
		this.nDisk = nDisk;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getnDiskId() {
		return nDiskId;
	}

	public void setnDiskId(String nDiskId) {
		this.nDiskId = nDiskId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Collection<NFile> getnFile() {
		return nFile;
	}

	public void setnFile(Collection<NFile> nFile) {
		this.nFile = nFile;
	}

	public Collection<NDir> getnDir() {
		return nDir;
	}

	public void setnDir(Collection<NDir> nDir) {
		this.nDir = nDir;
	}
	
	
}
 
