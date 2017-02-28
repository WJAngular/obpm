package cn.myapps.km.permission.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.km.disk.ejb.IFile;
import cn.myapps.km.disk.ejb.NDir;
import cn.myapps.km.disk.ejb.NDirProcess;
import cn.myapps.km.disk.ejb.NDirProcessBean;
import cn.myapps.km.disk.ejb.NDisk;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.disk.ejb.NFileProcess;
import cn.myapps.km.disk.ejb.NFileProcessBean;
import cn.myapps.km.org.ejb.NRole;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

public class PermissionHelper {

	/**
	 * 权限类型-阅读
	 */
	public static final int PERMISSION_TYPE_READ = 1;
	/**
	 * 权限类型-写入
	 */
	public static final int PERMISSION_TYPE_WRITE = 2;
	/**
	 * 权限类型-下载
	 */
	public static final int PERMISSION_TYPE_DOWNLOAD = 3;
	/**
	 * 权限类型-打印
	 */
	public static final int PERMISSION_TYPE_PRINT = 4;
	
	public static final int FILE_TYPE_DIR = 1;
	public static final int FILE_TYPE_FILE =2;
	
	public static Map<String,String> MANAGE_PERMISSIONS = new HashMap<String,String>();
	
	public static synchronized void cleanMnangePermission(){
		MANAGE_PERMISSIONS.clear();
	}
	
	public static synchronized void initMnangePermission(){
		Map<String,String> temp = new HashMap<String,String>();
		ManagePermissionProcess process = new ManagePermissionProcessBean();
		try {
			Collection<ManagePermissionItem> list = process.doQueryAllPermissionItems();
			if(list.isEmpty()){
				//避免没有授权数据时 程序会多次进入此方法
				temp.put("empty", "false");
			}
			for (Iterator<ManagePermissionItem> iterator = list.iterator(); iterator.hasNext();) {
				ManagePermissionItem item = iterator.next();
				temp.put(item.getResourceType()+"_"+item.getResource()+"_"+item.getScope()+"_"+item.getOwner().trim()+"_"+item.getDomainId(), "true");
			}
			MANAGE_PERMISSIONS.putAll(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据用户和目录id获取操作权限信息，返回JSON格式字符串
	 * @param diskType
	 * 		网盘类型
	 * @param fileType
	 * 		文件类型
	 * @param fileId
	 * 		文件id
	 * @param dirId
	 * 		文件所在目录id
	 * @param user
	 * 		用户对象
	 * @return
	 * 		JSON格式字符串
	 */
	public static String getPermissonsWithJson(int diskType,int fileType,String fileId,String dirId,NUser user){
		
		if(NDisk.TYPE_PERSONAL == diskType) return "";
		Map<String, Object> permissions = new HashMap<String, Object>();
		
		try {
			if(user.isPublicDiskAdmin()) {
				//开启所有权限
				permissions.put("id", fileId);
				permissions.put("share", 2);
				if(fileType==FILE_TYPE_FILE){
					permissions.put("download", 1);
					permissions.put("authorize", 1);
				}else{
					permissions.put("download", 2);
					permissions.put("authorize", 1);
					permissions.put("adminAuthorize", 1);
				}
				permissions.put("del", 1);
				permissions.put("move", 1);
				permissions.put("rename", 1);
				
			}else {
				if(verifyDirManagePermission(dirId,user)){//具有此目录的管理权限
					permissions.put("id", fileId);
					permissions.put("share", 2);
					if(fileType==FILE_TYPE_FILE){
						permissions.put("download", 1);
						permissions.put("authorize", 1);
					}else{
						permissions.put("download", 2);
						permissions.put("authorize", 1);
						permissions.put("adminAuthorize", 2);
					}
					permissions.put("del", 1);
					permissions.put("move", 1);
					permissions.put("rename", 1);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JsonUtil.toJson(permissions);
	}
	
	/**
	 * 验证目录管理权限
	 * @param dirId
	 * @param user
	 * @return
	 */
	public static boolean verifyDirManagePermission(String dirId,NUser user) throws Exception{
		try {
			if(user.isPublicDiskAdmin()) return true;
			//1.验证用户本身是否具有权限
			String key = ManagePermission.RESOURCE_TYPE_DIR+"_"+dirId+"_"+ManagePermission.SCOPE_USER+"_"+user.getId()+"_"+user.getDomainid();
			if(MANAGE_PERMISSIONS.isEmpty()) initMnangePermission();
			if(MANAGE_PERMISSIONS.get(key) !=null) return true;
			
			//2.验证用户的角色是否具有权限
			for (Iterator<NRole> iterator = user.getKmRoles().iterator(); iterator.hasNext();) {
				NRole role = iterator.next();
				key = ManagePermission.RESOURCE_TYPE_DIR+"_"+dirId+"_"+ManagePermission.SCOPE_ROLE+"_"+role.getId()+"_"+user.getDomainid();
				if(MANAGE_PERMISSIONS.get(key) !=null) return true;
			}
		} catch (Exception e) {
			throw e;
		}
		NDirProcess process = new NDirProcessBean();
		NDir dir = (NDir)process.doView(dirId);
		if(dir !=null && !StringUtil.isBlank(dir.getParentId())){//递归校验父目录管理权限
			return verifyDirManagePermission(dir.getParentId(),user);
		}
		return false;
	}
	
	
	public static boolean verify(IFile file,NUser user,int permissionType) throws Exception {
		
		try {
			if(file.getOwnerId().equals(user.getId())) return true;
			int fileType = FILE_TYPE_FILE;
			if(file instanceof NDir)
				fileType = FILE_TYPE_DIR;
			return checkPermissionWithInheritance(fileType, file.getId(), IFileAccess.SCOPE_USER, user.getId(), PermissionHelper.PERMISSION_TYPE_READ);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	
	/**
	 * 检查指定组织对文件或目录的操作权限
	 * @param fileType
	 * 		文件类型（目录|文件）
	 * @param fileId
	 * 		文件ID
	 * @param scope
	 * 		作用域（用户、角色、部门）
	 * @param ownerId
	 * 		作用ID（用户id，角色id，部门id）
	 * @param permissionType
	 * 		权限类型（阅读、下载、写入、打印）
	 * @return
	 * @throws Exception
	 */
	public static boolean checkPermission(int fileType,String fileId, String scope,
			String ownerId, int permissionType) throws Exception {
		try {
			IFileAccess acc = null;
			
			acc = findIFileAccess(fileType, fileId, scope, ownerId);
			
			if (acc == null) return false;
			
			return check(acc, permissionType);
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 用继承的方式检查指定组织对文件或目录的操作权限
	 * @param fileType
	 * 		文件类型（目录|文件）
	 * @param fileId
	 * 		文件ID
	 * @param scope
	 * 		作用域（用户、角色、部门）
	 * @param ownerId
	 * 		作用ID（用户id，角色id，部门id）
	 * @param permissionType
	 * 		权限类型（阅读、下载、写入、打印）
	 * @return
	 * @throws Exception
	 */
	public static boolean checkPermissionWithInheritance(int fileType,String fileId, String scope,
			String ownerId, int permissionType) throws Exception {
		try {
			IFileAccess acc = null;
			
			acc = findIFileAccessWithInheritance(fileType, fileId, scope, ownerId);
				
			if(acc==null) return false;
			
			return check(acc, permissionType);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 根据传入的授权信息和权限类型校验权限
	 * @param access
	 * @param permissionType
	 * @return
	 */
	public static boolean check(IFileAccess access,int permissionType){
		try {
			if(access.getStartDate() !=null && access.getEndDate() !=null){//授权时效判断
				if(access.getStartDate().before(new Date()) && access.getEndDate().after(new Date())){
					
				}else{
					return false;
				}
			}
			
			switch (permissionType) {
			case PERMISSION_TYPE_READ:
				if(IFileAccess.READ_MODE_TRUE == access.getReadMode()) return true;
				break;
			case PERMISSION_TYPE_WRITE:
				if(IFileAccess.WRITE_MODE_TRUE == access.getWriteMode()) return true;
				break;
			case PERMISSION_TYPE_DOWNLOAD:
				if(IFileAccess.DOWNLOAD_MODE_TRUE == access.getDownloadMode()) return true;
				break;
			case PERMISSION_TYPE_PRINT:
				if(IFileAccess.PRINT_MODE_TRUE == access.getPrintMode()) return true;
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 找到一个授权信息
	 * @param fileType
	 * 		文件类型（目录|文件）
	 * @param fileId
	 * 		文件ID
	 * @param scope
	 * 		作用域（用户、角色、部门）
	 * @param ownerId
	 * 		作用ID（用户id，角色id，部门id）
	 * @return
	 * @throws Exception
	 */
	public static IFileAccess findIFileAccess(int fileType,String fileId, String scope,
			String ownerId) throws Exception {
		FileAccessProcess fprocess = new FileAccessProcessBean();
		DirAccessProcess dprocess = new DirAccessProcessBean();
		IFileAccess acc = null;
		try {
			if(FILE_TYPE_FILE == fileType){//文件
				acc = (IFileAccess) fprocess.findByOwner(fileId, ownerId, scope);
			}else if(FILE_TYPE_DIR == fileType){//目录
				acc = (IFileAccess) dprocess.findByOwner(fileId, ownerId, scope);
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return acc;
	}
	
	/**
	 * 找到一个授权信息
	 * @param fileType
	 * 		文件类型（目录|文件）
	 * @param fileId
	 * 		文件ID
	 * @param user
	 * 		用户对象
	 * @return
	 * @throws Exception
	 */
	public static IFileAccess findIFileAccessWithInheritance(int fileType,String fileId,
			NUser user) throws Exception {
		IFileAccess acc = null;
		ArrayList<IFileAccess> accessList = new ArrayList<IFileAccess>();
		IFileAccess acc_user = null;
		IFileAccess acc_dept = null;
		//查找用户授权信息
		acc_user = findIFileAccessWithInheritance(fileType, fileId, IFileAccess.SCOPE_USER, user.getId());
		if (acc_user!=null) {
			accessList.add(acc_user);
		}
		acc_dept = findIFileAccessWithInheritance(fileType, fileId, IFileAccess.SCOPE_DEPT, user.getDefaultDepartment());
		if (acc_dept!=null) {
			accessList.add(acc_dept);
		}
		//查找角色授权信息-KM角色
		for (Iterator<NRole> iterator = user.getKmRoles().iterator(); iterator.hasNext();) {
			NRole role = iterator.next();
			IFileAccess acc_role = findIFileAccessWithInheritance(fileType, fileId, IFileAccess.SCOPE_ROLE, role.getId());
			if (acc_role!=null) {
				accessList.add(acc_role);
			}
			
			IFileAccess	acc_dept_role = findIFileAccessWithInheritance(fileType, fileId, IFileAccess.SCOPE_DEPT_ROLE, user.getDefaultDepartment()+";"+role.getId());
			if (acc_dept_role!=null) {
				accessList.add(acc_dept_role);
			}
		}
		//查找角色授权信息-软件角色
		for (Iterator<RoleVO> iterator = user.getRoles().iterator(); iterator.hasNext();) {
			RoleVO role = iterator.next();
			IFileAccess acc_role = findIFileAccessWithInheritance(fileType, fileId, IFileAccess.SCOPE_ROLE, role.getId());
			if (acc_role!=null) {
				accessList.add(acc_role);
			}
			
			IFileAccess	acc_dept_role = findIFileAccessWithInheritance(fileType, fileId, IFileAccess.SCOPE_DEPT_ROLE, user.getDefaultDepartment()+";"+role.getId());
			if (acc_dept_role!=null) {
				accessList.add(acc_dept_role);
			}
		}
		
		
		//合并权限
		acc = mergeFileAccess(accessList);
		
		return acc;
	}
	
	/**
	 * 合并权限
	 * @return
	 */
	public static IFileAccess mergeFileAccess(List<IFileAccess> accessList){
		if (accessList==null || accessList.size()==0) {
			return null;
		}
		IFileAccess access = null;
		for (int i=0; i<accessList.size(); i++) {
			IFileAccess acc = accessList.get(i);
			if (i==0) {
				access = acc;
				continue;
			}
			if (acc.getReadMode() > access.getReadMode()) {
				access.setReadMode(acc.getReadMode());
			}
			if(acc.getWriteMode() > access.getWriteMode()){
				access.setWriteMode(acc.getWriteMode());
			}
			if (acc.getDownloadMode() > access.getDownloadMode()) {
				access.setDownloadMode(acc.getDownloadMode());
			}
			if(acc.getPrintMode() > access.getPrintMode()){
				access.setPrintMode(acc.getPrintMode());
			}
		}
		
		return access;
		
	}
	
	/**
	 * 检查用户是否拥有给定文件的任一操作权限
	 * @param fileId
	 * @param user
	 * @return
	 */
	public static boolean checkNFilePermission(String fileId,NUser user){
		boolean result = false;
		try {
			IFileAccess acc = findIFileAccessWithInheritance(PermissionHelper.FILE_TYPE_FILE, fileId, IFileAccess.SCOPE_USER, user.getId());
			if(acc ==null){
				//查找角色授权信息
				for (Iterator<NRole> iterator = user.getKmRoles().iterator(); iterator.hasNext();) {
					NRole role = iterator.next();
					if(role.getLevel() == NRole.LEVEL_DOMAIN_ADMIN){
						return true;
					}
					acc = findIFileAccessWithInheritance(PermissionHelper.FILE_TYPE_FILE, fileId, IFileAccess.SCOPE_ROLE, role.getId());
				}
			}
			if(acc !=null){
				boolean read = PermissionHelper.check(acc, PermissionHelper.PERMISSION_TYPE_READ);
				if(read) return true;
				boolean write = PermissionHelper.check(acc, PermissionHelper.PERMISSION_TYPE_WRITE);
				if(write) return true;
				boolean download = PermissionHelper.check(acc, PermissionHelper.PERMISSION_TYPE_DOWNLOAD);
				if(download) return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 用权限继承的方式找到对应的一个授权信息
	 * @param fileType
	 * 		文件类型（目录|文件）
	 * @param fileId
	 * 		文件ID
	 * @param scope
	 * 		作用域（用户、角色、部门）
	 * @param ownerId
	 * 		作用ID（用户id，角色id，部门id）
	 * @return
	 * @throws Exception
	 */
	public static IFileAccess findIFileAccessWithInheritance(int fileType,String fileId, String scope,
			String ownerId) throws Exception {
		
		FileAccessProcess process = new FileAccessProcessBean();
		DirAccessProcess dprocess = new DirAccessProcessBean();
		NDirProcess dirProcess = new NDirProcessBean();
		NFileProcess fileProcess = new NFileProcessBean();
		IFileAccess acc = null;
		try {
			
			
			//1.第一步检查文件本身是否有相应的授权信息
			if(FILE_TYPE_FILE == fileType){//文件
				acc = (IFileAccess) process.findByOwner(fileId, ownerId, scope);
			}else if(FILE_TYPE_DIR == fileType){//目录
				acc = (IFileAccess) dprocess.findByOwner(fileId, ownerId, scope);
			}
			
			if (acc == null){//2.第二步 找不到文件本身的授权信息，那么离文件最近的上级目录授权信息对此文件生效。
				if(FILE_TYPE_FILE == fileType){//文件
					NFile file = (NFile)fileProcess.doView(fileId);
					if(file !=null){
						acc = getIFileAccess(file.getNDirId(),scope,ownerId);
					}
				}else if(FILE_TYPE_DIR == fileType){//目录
					
					NDir dir = (NDir) dirProcess.doView(fileId);
					if(dir !=null){
						acc = getIFileAccess(dir.getParentId(), scope, ownerId);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return acc;
	}
	
	/**
	 * 获取离文件最近的上级目录授权信息
	 * @param fileId
	 * 		目录ID
	 * @param scope
	 * 		作用域（用户|角色）
	 * @param ownerId
	 * 		作用域下面的个体ID （用户ID|角色ID）
	 * @return
	 * 		
	 * @throws Exception
	 */
	private static IFileAccess getIFileAccess(String fileId, String scope,
			String ownerId) throws Exception {
		if(StringUtil.isBlank(fileId)) return null;
		
		DirAccessProcess process = new DirAccessProcessBean();
		NDirProcess dirProcess = new NDirProcessBean();
		IFileAccess acc = null;
		
		try {
			acc =  process.findByOwner(fileId, ownerId, scope);
			if(acc ==null){
				NDir dir = (NDir) dirProcess.doView(fileId);
				if(dir !=null){
					acc = getIFileAccess(dir.getParentId(), scope, ownerId);
				}
				
			}
			if(acc !=null){
				return acc;
			}
		} catch (Exception e) {
			throw e;
		}
		
		return acc;
		
	}
	
	public boolean isFavoritesDir(String ndirId) throws Exception {
		NDirProcess dirProcess = new NDirProcessBean();
		NDir dir = (NDir) dirProcess.doView(ndirId);
		
		if(dir !=null){
			return dir.getType()==NDir.TYPE_SYSTEM && NDir.MY_COLLECTION.equals(dir.getName());
		}
		return false;
		
	}
	
	

}
