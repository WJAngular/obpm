package cn.myapps.km.disk.ejb;


import cn.myapps.km.base.ejb.NObject;


/**
 * 网盘
 * @author xiuwei
 *
 */
public class NDisk extends NObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5470838629132408347L;
	/**
	 * 公有
	 */
	public static final int TYPE_PUBLIC = 1;
	/**
	 * 个人私有
	 */
	public static final int TYPE_PERSONAL = 2;
	/**
	 * 部门私有
	 */
	public static final int TYPE_DEPARTMENT = 3;
	/**
	 * 角色私有
	 */
	public static final int TYPE_ROLE = 4;
	
	/**
	 * 归档私有
	 */
	public static final int TYPE_ARCHIVE = 5;
	/**
	 * 公有文库名称
	 */
	public static final String NAME_PUBLIC = "{*[cn.myapps.km.dir.public_library]*}";
	/**
	 * 个人文库名称
	 */
	public static final String NAME_PERSONAL = "{*[cn.myapps.km.dir.my_library]*}";
	/**
	 * 部门文库名称
	 */
	public static final String NAME_DEPARTMENT = "{*[cn.myapps.km.dir.department_library]*}";
	
	/**
	 * 归档文库名称
	 */
	public static final String NAME_ARCHIVE = "{*[cn.myapps.km.dir.archive_library]*}";
	/**
	 * 类型(公有、个人私有、部门私有、角色私有)
	 */
	private int type;
	
	/**
	 * 所有者Id
	 */
	private String ownerId;
	
	 
	/**
	 * 名称
	 */
	private String name;
	 
	/**
	 * 根目录
	 */
	private String nDirId;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getnDirId() {
		return nDirId;
	}

	public void setnDirId(String nDirId) {
		this.nDirId = nDirId;
	}

}
 
