package cn.myapps.core.networkdisk.action;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Environment;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.networkdisk.ejb.NetDisk;
import cn.myapps.core.networkdisk.ejb.NetDiskFile;
import cn.myapps.core.networkdisk.ejb.NetDiskFileProcess;
import cn.myapps.core.networkdisk.ejb.NetDiskFileProcessBean;
import cn.myapps.core.networkdisk.ejb.NetDiskFolder;
import cn.myapps.core.networkdisk.ejb.NetDiskFolderProcess;
import cn.myapps.core.networkdisk.ejb.NetDiskGroup;
import cn.myapps.core.networkdisk.ejb.NetDiskGroupProcess;
import cn.myapps.core.networkdisk.ejb.NetDiskPemission;
import cn.myapps.core.networkdisk.ejb.NetDiskPemissionProcess;
import cn.myapps.core.networkdisk.ejb.NetDiskProcess;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class NetworkDisk {

	private static DecimalFormat df = new DecimalFormat("0.00");
	
	public static String synchronousData(String path, String savePath,
			String userid){
		try{
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			File f = new File(savePath);
			if (!f.exists()) {
				if (!f.mkdirs()) {
					throw new IOException("create folder '" + f + "' failed!");
				}
			}
			DataPackage<NetDiskFolder> datapackage = netDiskFolderProcess
					.doQuery(new ParamsTable());
			NetDiskFolder netDiskFolder = null;
			if (datapackage.rowCount > 0) {
				for (Iterator<NetDiskFolder> it = datapackage.getDatas()
						.iterator(); it.hasNext();) {
					netDiskFolder = (NetDiskFolder) it.next();
					if(StringUtil.isBlank(netDiskFolder.getParentId())){
						if(netDiskFolder.getFolderPath().indexOf("networkdisk")>0){
							String folderPath = netDiskFolder.getFolderPath().substring(netDiskFolder.getFolderPath().indexOf("networkdisk") + 12);
							netDiskFolder.setFolderPath(folderPath);
							netDiskFolderProcess.doUpdate(netDiskFolder);
						}
					}
					File nFolder = new File(savePath + File.separator
							+ netDiskFolder.getFolderPath());
					if(!nFolder.exists()){
						if(!nFolder.mkdirs()){
							throw new IOException("create folder '" + nFolder + "' failed!");
						}
					}
				}
			}
			File[] list = f.listFiles();
			for(int i=0;list.length>i;i++){
				String parentid = list[i].getPath().substring(list[i].getPath().indexOf("networkdisk") + 12);
				synchronousFolder(list[i].listFiles(),parentid,userid);
			}
			synchronousFile();
		}catch(Exception e){
			
		}
		return getFolderTree(path,savePath,userid);
	}
	
	private static void synchronousFolder(File[] list,String parentId,String userid){
		try{
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			NetDiskFolder netDiskFolder = null;
			for (int i = 0; i < list.length; i++) {
				if (list[i].isDirectory()) {
					if(list[i].getPath().indexOf("networkdisk")>0){
						String path = list[i].getPath().substring(list[i].getPath().indexOf("networkdisk")+12);
						path = path.replace("\\", "\\\\");
						ParamsTable params = new ParamsTable();
						params.setParameter("t_folderpath", path);
						DataPackage<NetDiskFolder> datapackage = netDiskFolderProcess
								.doQuery(params);
						if(datapackage.rowCount >0){
							for (Iterator<NetDiskFolder> it = datapackage.getDatas()
									.iterator(); it.hasNext();) {
								netDiskFolder = (NetDiskFolder) it.next();
								netDiskFolder.setParentId(parentId);
								netDiskFolderProcess.doUpdate(netDiskFolder);
								break;
							}
						}else{
							netDiskFolder = new NetDiskFolder();
							netDiskFolder.setUserid(userid);
							netDiskFolder.setFolderPath(path);
							netDiskFolder.setParentId(parentId);
							netDiskFolder.setOrderno(0);
							netDiskFolderProcess.doCreate(netDiskFolder);
						}
						synchronousFolder(list[i].listFiles(),netDiskFolder.getId(),userid);
					}
				}
			}
		}catch(Exception e){
			
		}
	}
	
	private static void synchronousFile(){
		try{
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			NetDiskFileProcess netDiskFileProcess =(NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);
			DataPackage<NetDiskFile> data = netDiskFileProcess.doQuery(new ParamsTable());
			if(data.rowCount>0){
				for(Iterator<NetDiskFile> its = data.datas.iterator();its.hasNext();){
					NetDiskFile nFile = its.next();
					String webPath = nFile.getFolderWebPath();
					if(webPath.indexOf("networkdisk")>0){
						webPath = webPath.substring(webPath.indexOf("networkdisk")+12);
						nFile.setFolderWebPath(webPath);
					}
					String folderPath = nFile.getFolderWebPath().replace("/", File.separator);
					ParamsTable params = new ParamsTable();
					folderPath = folderPath.replace("\\", "\\\\");
					params.setParameter("t_folderpath", folderPath);
					DataPackage<NetDiskFolder> folderdata = netDiskFolderProcess.doQuery(params);
					if(folderdata.rowCount>0){
						NetDiskFolder folder = null;
						for(Iterator<NetDiskFolder> ites = folderdata.datas.iterator();ites.hasNext();){
							folder = ites.next();
							nFile.setFolderId(folder.getId());
							nFile.setFolderPath(folder.getFolderPath());
							break;
						}
					}
					netDiskFileProcess.doUpdate(nFile);
				}
			}
		}catch(Exception e){
			
		}
	}
	/**
	 * 获得相应文件夹及其文件
	 * 
	 * @param path
	 * @param savePath
	 * @param userid
	 * @return
	 * @author kharry
	 */
	public static String getFolderTree(String path, String savePath,
			String userid) {
		StringBuffer sb = new StringBuffer();
		try {
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
//			NetDiskFileProcess process = (NetDiskFileProcess) ProcessFactory.createProcess(NetDiskFileProcess.class);
//			process.createFile("/uploads/item/botbgv2.jpg", "11e2-a80c-7e8cb2d4-bfc3-c57f00da49b4", "11e2-9691-43143c3c-9cca-a7ceb00e89e8");
//			process.searchFile("Mac_Os_Xcode开发人员入门导引.pdf", "11e2-a56c-7b4d23b9-b678-2b06dfb57725");
//			process.removeFile("Mac_Os_Xcode开发人员入门导引.pdf", "11e2-a56c-7b4d23b9-b678-2b06dfb57725");
//			netDiskFolderProcess.createFolder("thisName","public","11e2-a56c-7b4d23b9-b678-2b06dfb57725", 4);
			// 获得文件保存的真实路径
			File f = new File(savePath + File.separator + path);
			if (!f.exists()) {
				if (!f.mkdirs()) {
					throw new IOException("create folder '" + f + "' failed!");
				}
			}
			String permission = "";
			ParamsTable params = new ParamsTable();
			params.setParameter("t_id", path);
			DataPackage<NetDiskFolder> datapackage = netDiskFolderProcess
					.doQuery(params);
			if(datapackage.rowCount >0){
				for(Iterator<NetDiskFolder> its = datapackage.datas.iterator();its.hasNext();){
					NetDiskFolder netDiskFolder = (NetDiskFolder)its.next();
					permission = appendFolderPermission(netDiskFolder,
							userid);
					break;
				}
			}
			sb.append("<root>");
			if (path.indexOf("public") != -1) {
				sb.append("<menuitem label='公共共享[根目录]");
			} else {
				sb.append("<menuitem label='我的网盘[根目录]");
			}
			sb.append("' path='").append(path);
			sb.append("' id='").append(path);
			sb.append(permission);
			sb.append("'>");
			sb = getChildFolderTree(sb, userid, "", path, savePath);
			sb.append("</root>");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 迭代获得相应的文件夹
	 * 
	 * @param sb
	 * @param userid
	 * @param permission
	 * @param parentId
	 * @param savePath
	 * @return
	 * @author kharry
	 */
	public static StringBuffer getChildFolderTree(StringBuffer sb,
			String userid, String permission, String parentId, String savePath) {
		try {
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("_orderby", "orderno");
			params.setParameter("t_parentid", parentId);
			DataPackage<NetDiskFolder> datapackage = netDiskFolderProcess
					.doQuery(params);
			NetDiskFolder netDiskFolder = null;
			if (datapackage.rowCount > 0) {
				for (Iterator<NetDiskFolder> it = datapackage.getDatas()
						.iterator(); it.hasNext();) {
					netDiskFolder = (NetDiskFolder) it.next();
					File nFolder = new File(savePath + File.separator
							+ netDiskFolder.getFolderPath());
					if(!nFolder.exists()){
						if(!nFolder.mkdirs()){
							throw new IOException("create folder '" + nFolder + "' failed!");
						}
					}
					if (nFolder.isDirectory()) {
						sb.append(
								"<menuitem label='" + nFolder.getName()
										+ "' path='").append(
								netDiskFolder.getFolderPath());
						sb.append("' id='").append(netDiskFolder.getId());
						sb.append("' orderno='").append(
								netDiskFolder.getOrderno());
						permission = appendFolderPermission(netDiskFolder,
								userid);
						sb.append(permission);
						sb.append("'>");
						if (nFolder.listFiles().length > 0) {
							getChildFolderTree(sb, userid, permission,
									netDiskFolder.getId(), savePath);
						} else {
							sb.append("</menuitem>");
						}
						permission = "";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return sb;
		}
		sb.append("</menuitem>");
		return sb;
	}

	/**
	 * 文件夹权限
	 * 
	 * @param Netdiskfolder
	 * @param userid
	 * @return
	 * @author kharry
	 */
	public static String appendFolderPermission(NetDiskFolder netDiskFolder,
			String userid) {
		StringBuffer sb = new StringBuffer();
		try {
			if (netDiskFolder != null) {
				if (netDiskFolder.getPemission() != null) {
					NetDiskPemission netDiskPemission = netDiskFolder
							.getPemission();
					SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
							.createProcess(SuperUserProcess.class);
					if (netDiskPemission.getSelectObject().equals("用户")) {
						if (netDiskPemission.getUsers().indexOf(userid) != -1
								|| superUserProcess.doView(userid) != null) {
							sb.append("' userid='").append(
									netDiskFolder.getUserid());
							sb.append("' pemissionid='").append(
									netDiskFolder.getPemission().getId());
							sb.append("' operate='").append(
									netDiskPemission.getOperate());
						}
					} else if (netDiskPemission.getSelectObject().equals("部门")) {
						DepartmentVO department = (DepartmentVO) netDiskPemission
								.getDepartment();
						for (Iterator<UserVO> iterator = department.getUsers()
								.iterator(); iterator.hasNext();) {
							UserVO user = (UserVO) iterator.next();
							if (user.getId().equals(userid)
									|| superUserProcess.doView(userid) != null) {
								sb.append("' userid='").append(
										netDiskFolder.getUserid());
								sb.append("' pemissionid='").append(
										netDiskFolder.getPemission().getId());
								sb.append("' operate='").append(
										netDiskPemission.getOperate());
								break;
							}
						}
					} else if (netDiskPemission.getSelectObject().equals("角色")) {
						RoleVO role = (RoleVO) netDiskPemission.getRole();
						for (Iterator<UserVO> iterator = role.getUsers()
								.iterator(); iterator.hasNext();) {
							UserVO user = (UserVO) iterator.next();
							if (user.getId().equals(userid)
									|| superUserProcess.doView(userid) != null) {
								sb.append("' userid='").append(
										netDiskFolder.getUserid());
								sb.append("' pemissionid='").append(
										netDiskFolder.getPemission().getId());
								sb.append("' operate='").append(
										netDiskPemission.getOperate());
								break;
							}
						}
					} else if (netDiskPemission.getSelectObject().equals("组")) {
						NetDiskGroup group = (NetDiskGroup) netDiskPemission
								.getGroup();
						if (group.getUseridGroup().indexOf(userid) != -1
								|| superUserProcess.doView(userid) != null) {
							sb.append("' userid='").append(
									netDiskFolder.getUserid());
							sb.append("' pemissionid='").append(
									netDiskFolder.getPemission().getId());
							sb.append("' operate='").append(
									netDiskPemission.getOperate());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return sb.toString();
	}

	/**
	 * 创建文件夹
	 * 
	 * @param folderPath
	 * @param savePath
	 * @param userid
	 * @param orderno
	 * @param parentId
	 * @return
	 * @author kharry
	 */
	public static String create(String folderPath, String savePath,
			String userid, int orderno, String parentId) {
		try {
			NetDiskFolder netDiskFolder = null;
			String temp = folderPath.substring(folderPath
					.lastIndexOf(File.separator));
			if (temp.toLowerCase().indexOf("public") != -1) {
				return "'public' 为关键字";
			}
			File file = new File(savePath + File.separator + folderPath);
			if (!file.exists()) {
				if (file.mkdirs()) {
					NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
							.createProcess(NetDiskFolderProcess.class);
					NetDiskFolder parentFolder = (NetDiskFolder) netDiskFolderProcess.doView(parentId);
					netDiskFolder = new NetDiskFolder();
					netDiskFolder.setUserid(userid);
					netDiskFolder.setFolderPath(folderPath);
					netDiskFolder.setParentId(parentId);
					netDiskFolder.setOrderno(orderno);
					if(parentFolder!= null && parentFolder.getPemission() != null){
						netDiskFolder.setPemission(parentFolder.getPemission());
					}
					netDiskFolderProcess.doCreate(netDiskFolder);
					return "";
				} else {
					return "create folder '" + folderPath + "' failed!";
				}
			} else {
				return "该文件夹已存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * 重命名
	 * 
	 * @param folderId
	 * @param newPath
	 * @param savePath
	 * @param orderno
	 * @return
	 * @author kharry
	 */
	public static String reName(String folderId, String newPath,
			String savePath, int orderno) {
		try {
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			NetDiskFolder rootFolder = (NetDiskFolder) netDiskFolderProcess
					.doView(folderId);
			if (rootFolder != null) {
				String oldPath = rootFolder.getFolderPath();
				if (oldPath.equals(newPath)) {
					rootFolder.setOrderno(orderno);
					netDiskFolderProcess.doUpdate(rootFolder);
				} else {
					File file = new File(savePath + File.separator + oldPath);
					if (file.exists()) {
						File newFile = new File(savePath + File.separator
								+ newPath);
						if (file.renameTo(newFile)) {
							rootFolder.setFolderPath(newPath);
							rootFolder.setOrderno(orderno);
							netDiskFolderProcess.doUpdate(rootFolder);
							reNameAllFile(rootFolder.getId(), oldPath, newPath,
									savePath);
							reNameAllFolder(rootFolder.getId(), oldPath,
									newPath, savePath);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "";
	}

	/**
	 * 重命名子目录
	 * 
	 * @param folderId
	 * @param oldPath
	 * @param newPath
	 * @param savePath
	 * @author kharry
	 */
	private static void reNameAllFolder(String folderId, String oldPath,
			String newPath, String savePath) {
		try {
			ParamsTable params = new ParamsTable();
			params.setParameter("t_parentid", folderId);
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			DataPackage<NetDiskFolder> dataPackageFolder = netDiskFolderProcess
					.doQuery(params);
			if (dataPackageFolder.rowCount > 0) {
				for (Iterator<NetDiskFolder> itFolder = dataPackageFolder
						.getDatas().iterator(); itFolder.hasNext();) {
					NetDiskFolder netDiskFolder = (NetDiskFolder) itFolder
							.next();
					String folderPath = netDiskFolder.getFolderPath();
					netDiskFolder.setFolderPath(folderPath.replace(oldPath,
							newPath));
					netDiskFolderProcess.doUpdate(netDiskFolder);
					reNameAllFolder(netDiskFolder.getId(), oldPath,
							netDiskFolder.getFolderPath(), savePath);
					reNameAllFile(netDiskFolder.getId(), folderPath,
							netDiskFolder.getFolderPath(), savePath);
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 重命名文件夹下的文件
	 * 
	 * @param folderId
	 * @param oldPath
	 * @param newPath
	 * @param savePath
	 * @author kharry
	 */
	private static void reNameAllFile(String folderId, String oldPath,
			String newPath, String savePath) {
		try {
			ParamsTable params = new ParamsTable();
			params.setParameter("t_folderid", folderId);
			NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);
			DataPackage<NetDiskFile> dataPackageFolder = netDiskFileProcess
					.doQuery(params);
			if (dataPackageFolder.rowCount > 0) {
				for (Iterator<NetDiskFile> itFolder = dataPackageFolder
						.getDatas().iterator(); itFolder.hasNext();) {
					NetDiskFile netDiskFile = (NetDiskFile) itFolder.next();
					String folderPath = netDiskFile.getFolderWebPath();
					netDiskFile.setFolderWebPath(folderPath.replace(oldPath,
							newPath));
					netDiskFileProcess.doUpdate(netDiskFile);
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 只删除文件夹及其文件
	 * 
	 * @param folderId
	 * @param savePath
	 * @param userid
	 * @return
	 * @author kharry
	 */
	public static String remove(String folderId, String savePath, String userid) {
		try {
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			NetDiskFolder rootFolder = (NetDiskFolder) netDiskFolderProcess
					.doView(folderId);
			if (rootFolder != null) {
				deleteAllFile(folderId, savePath);
				NetDiskFolder netDiskFolder = (NetDiskFolder) netDiskFolderProcess
						.doView(folderId);
				if (netDiskFolder != null) {
					deleteAllFolder(netDiskFolder.getId(), savePath);
					File file = new File(savePath + File.separator
							+ netDiskFolder.getFolderPath());
					if (file.exists()) {
						if (file.delete()) {
							netDiskFolderProcess.doRemove(netDiskFolder);
						} else {
							throw new OBPMValidateException("文件"
									+ netDiskFolder.getFolderPath() + "删除失败");
						}
					} else {
						netDiskFolderProcess.doRemove(netDiskFolder);
					}
				}
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * 删除文件夹及子文件夹
	 * 
	 * @param parentid
	 * @param savePath
	 * @throws Exception
	 * @author kharry
	 */
	private static void deleteAllFolder(String parentid, String savePath)
			throws Exception {
		try {
			ParamsTable params = new ParamsTable();
			params.setParameter("t_parentid", parentid);
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			DataPackage<NetDiskFolder> dataPackageFolder = netDiskFolderProcess
					.doQuery(params);
			if (dataPackageFolder.rowCount > 0) {
				for (Iterator<NetDiskFolder> itFolder = dataPackageFolder
						.getDatas().iterator(); itFolder.hasNext();) {
					NetDiskFolder netDiskFolder = (NetDiskFolder) itFolder
							.next();
					deleteAllFile(netDiskFolder.getId(), savePath);
					deleteAllFolder(netDiskFolder.getId(), savePath);
					File file = new File(savePath + File.separator
							+ netDiskFolder.getFolderPath());
					if (file.exists()) {
						if (file.delete()) {
							netDiskFolderProcess.doRemove(netDiskFolder);
						} else {
							throw new OBPMValidateException("文件"
									+ netDiskFolder.getFolderPath() + "删除失败");
						}
					} else {
						netDiskFolderProcess.doRemove(netDiskFolder);
					}
				}
			}

		} catch (Exception e) {

		}
	}

	/**
	 * 删除文件夹中所有文件
	 * 
	 * @param folderId
	 * @param savePath
	 * @author kharry
	 */
	private static void deleteAllFile(String folderId, String savePath) {
		try {
			ParamsTable params = new ParamsTable();
			params.setParameter("t_folderid", folderId);
			NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);
			DataPackage<NetDiskFile> datapackageFile = netDiskFileProcess
					.doQuery(params);
			if (datapackageFile.rowCount > 0) {
				for (Iterator<NetDiskFile> itFile = datapackageFile.getDatas()
						.iterator(); itFile.hasNext();) {
					NetDiskFile netDiskFile = (NetDiskFile) itFile.next();
					File file = new File(savePath + File.separator
							+ netDiskFile.getFolderWebPath() + File.separator
							+ netDiskFile.getName());
					if (file.exists()) {
						if (file.delete()) {
							netDiskFileProcess.doRemove(netDiskFile);
						} else {
							throw new OBPMValidateException("文件" + netDiskFile.getName()
									+ "删除失败");
						}
					} else {
						netDiskFileProcess.doRemove(netDiskFile);
					}
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 文件夹权限
	 * 
	 * @param datapackage
	 * @param path
	 * @param userid
	 * @return
	 */
	public static String appendFolderPermission(
			DataPackage<NetDiskFolder> datapackage, String path, String userid) {
		StringBuffer sb = new StringBuffer();
		try {
			NetDiskFolder netDiskFolder = null;
			if (datapackage.rowCount > 0) {
				for (Iterator<NetDiskFolder> it = datapackage.getDatas()
						.iterator(); it.hasNext();) {
					netDiskFolder = (NetDiskFolder) it.next();
					if (path.trim().indexOf(
							netDiskFolder.getFolderPath().trim()) != -1) {
						if (netDiskFolder.getPemission() != null) {
							NetDiskPemission netDiskPemission = netDiskFolder
									.getPemission();
							SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
									.createProcess(SuperUserProcess.class);
							if (netDiskPemission.getSelectObject().equals("用户")) {
								if (netDiskPemission.getUsers().indexOf(userid) != -1
										|| superUserProcess.doView(userid) != null) {
									sb.append("' id='").append(
											netDiskFolder.getId());
									sb.append("' userid='").append(
											netDiskFolder.getUserid());
									sb.append("' pemissionid='").append(
											netDiskFolder.getPemission()
													.getId());
									sb.append("' operate='").append(
											netDiskPemission.getOperate());
									break;
								}
							} else if (netDiskPemission.getSelectObject()
									.equals("部门")) {
								DepartmentVO department = (DepartmentVO) netDiskPemission
										.getDepartment();
								for (Iterator<UserVO> iterator = department
										.getUsers().iterator(); iterator
										.hasNext();) {
									UserVO user = (UserVO) iterator.next();
									if (user.getId().equals(userid)
											|| superUserProcess.doView(userid) != null) {
										sb.append("' id='").append(
												netDiskFolder.getId());
										sb.append("' userid='").append(
												netDiskFolder.getUserid());
										sb.append("' pemissionid='").append(
												netDiskFolder.getPemission()
														.getId());
										sb.append("' operate='").append(
												netDiskPemission.getOperate());
										break;
									}
								}
								break;
							} else if (netDiskPemission.getSelectObject()
									.equals("角色")) {
								RoleVO role = (RoleVO) netDiskPemission
										.getRole();
								for (Iterator<UserVO> iterator = role
										.getUsers().iterator(); iterator
										.hasNext();) {
									UserVO user = (UserVO) iterator.next();
									if (user.getId().equals(userid)
											|| superUserProcess.doView(userid) != null) {
										sb.append("' id='").append(
												netDiskFolder.getId());
										sb.append("' userid='").append(
												netDiskFolder.getUserid());
										sb.append("' pemissionid='").append(
												netDiskFolder.getPemission()
														.getId());
										sb.append("' operate='").append(
												netDiskPemission.getOperate());
										break;
									}
								}
								break;
							} else if (netDiskPemission.getSelectObject()
									.equals("组")) {
								NetDiskGroup group = (NetDiskGroup) netDiskPemission
										.getGroup();
								if (group.getUseridGroup().indexOf(userid) != -1
										|| superUserProcess.doView(userid) != null) {
									sb.append("' id='").append(
											netDiskFolder.getId());
									sb.append("' userid='").append(
											netDiskFolder.getUserid());
									sb.append("' pemissionid='").append(
											netDiskFolder.getPemission()
													.getId());
									sb.append("' operate='").append(
											netDiskPemission.getOperate());
									break;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return sb.toString();
	}

	// 重命名
	public static String reName(String oldNamePath, String newNamePath) {
		try {
			File file = new File(oldNamePath);
			if (file.renameTo(new File(newNamePath))) {
				if (oldNamePath.indexOf("public") != -1) {
					ParamsTable params = new ParamsTable();
					params.setParameter("sm_folderPath", "public");
					NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
							.createProcess(NetDiskFolderProcess.class);
					DataPackage<NetDiskFolder> datapackageFolder = netDiskFolderProcess
							.doQuery(params);
					if (datapackageFolder.rowCount > 0) {
						for (Iterator<NetDiskFolder> itFolder = datapackageFolder
								.getDatas().iterator(); itFolder.hasNext();) {
							NetDiskFolder netDiskFolder = (NetDiskFolder) itFolder
									.next();
							if (netDiskFolder.getFolderPath().trim()
									.equals(oldNamePath.trim())) {
								netDiskFolder.setFolderPath(newNamePath);
								netDiskFolderProcess.doUpdate(netDiskFolder);
							}
						}
					}
					String folderWebPath = newNamePath.substring(newNamePath
							.indexOf("networkdisk") - 1);
					NetDiskFileProcess netDisFileProcess = (NetDiskFileProcess) ProcessFactory
							.createProcess(NetDiskFileProcess.class);
					DataPackage<NetDiskFile> datapackageFile = netDisFileProcess
							.doQuery(params);
					if (datapackageFile.rowCount > 0) {
						for (Iterator<NetDiskFile> itFile = datapackageFile
								.getDatas().iterator(); itFile.hasNext();) {
							NetDiskFile netDiskFile = (NetDiskFile) itFile
									.next();
							if (netDiskFile.getFolderPath().trim()
									.equals(oldNamePath.trim())) {
								File oldfiles = new File(oldNamePath
										+ File.separator
										+ netDiskFile.getName());
								File folder = new File(newNamePath);
								if (folder.exists())
									folder.mkdir();
								File newfiles = new File(newNamePath
										+ File.separator
										+ netDiskFile.getName());
								oldfiles.renameTo(newfiles);
								netDiskFile.setFolderPath(newNamePath);
								netDiskFile.setFolderWebPath(folderWebPath);
								netDisFileProcess.doUpdate(netDiskFile);
							}
						}
					}
				}
				return "";
			} else {
				return "'" + oldNamePath + "'-->'" + newNamePath + "' 重命名失败!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	// 删除所有文件
	public static void delAllFile(String path) throws Exception {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.exists()) {
				if (temp.isFile()) {
					if (!temp.delete()) {
						throw new IOException("delete file '"
								+ temp.getAbsolutePath() + "' failed!");
					}
				}
				if (temp.isDirectory()) {
					delAllFile(temp.getPath());// 先删除文件夹里面的文件
				}
			}
		}
		file.delete();
	}

	// 删除所有文件
	public static void delAllFile(String path,
			DataPackage<NetDiskFile> datapackage, String userid)
			throws Exception {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			// if (path.endsWith(File.separator)) {
			if (path.endsWith("/")) {
				temp = new File(path + tempList[i]);
			} else {
				// temp = new File(path + File.separator + tempList[i]);
				temp = new File(path + "/" + tempList[i]);
			}
			if (temp.exists()) {
				if (temp.isFile()) {
					if (!temp.delete()) {
						throw new IOException("delete file '"
								+ temp.getAbsolutePath() + "' failed!");
					} else {
						for (Iterator<NetDiskFile> it = datapackage.getDatas()
								.iterator(); it.hasNext();) {
							NetDiskFile netDiskFile = (NetDiskFile) it.next();
							if (temp.getPath().indexOf(
									netDiskFile.getFolderPath()) != -1) {
								if (temp.getName()
										.equals(netDiskFile.getName())) {
									NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
											.createProcess(NetDiskFileProcess.class);
									netDiskFileProcess.doRemove(netDiskFile);
									break;
								}
							}
						}
					}
				}
				if (temp.isDirectory()) {
					delAllFile(path + "/" + tempList[i], datapackage, userid);// 先删除文件夹里面的文件
				}
			} else {
				throw new IOException("'" + temp.getAbsolutePath()
						+ "' does not exist!");
			}
		}
	}

	// 保存文件夹权限信息
	public static String saveFolderPemission(String id, String pemissionid) {
		try {
			NetDiskFolder netDiskFolder = null;
			String parentPemissionId = "";
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			NetDiskPemissionProcess netDiskPemissionProcess = (NetDiskPemissionProcess) ProcessFactory
					.createProcess(NetDiskPemissionProcess.class);
			netDiskFolder = (NetDiskFolder) netDiskFolderProcess.doView(id);
			if(netDiskFolder == null){
				netDiskFolder = new NetDiskFolder();
				netDiskFolder.setFolderPath(id);
				netDiskFolder.setOrderno(0);
				netDiskFolder.setId(id);
				netDiskFolderProcess.doCreate(netDiskFolder);
			}else{
				if (netDiskFolder.getPemission() != null) {
					parentPemissionId = netDiskFolder.getPemission().getId();
				}
			}
			if (pemissionid.equals("")) {
				netDiskFolder.setPemission(null);
				netDiskFolderProcess.doUpdate(netDiskFolder);
			} else {
				NetDiskPemission netDiskPemission = (NetDiskPemission) netDiskPemissionProcess
						.doView(pemissionid);
				netDiskFolder.setPemission(netDiskPemission);
				netDiskFolderProcess.doUpdate(netDiskFolder);
			}
			saveChildFolderPemission(netDiskFolder.getId(), pemissionid,
					parentPemissionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// 保存子文件夹权限信息
	public static String saveChildFolderPemission(String id,
			String pemissionId, String parentPemissionId) {
		try {
			NetDiskFolder netDiskFolder = null;
			ParamsTable params = new ParamsTable();
			params.setParameter("t_parentId", id);
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			DataPackage<NetDiskFolder> datapackage = netDiskFolderProcess
					.doQuery(params);
			for (Iterator<NetDiskFolder> it = datapackage.getDatas().iterator(); it
					.hasNext();) {
				boolean flag = false;
				netDiskFolder = (NetDiskFolder) it.next();
				String folderPemission = "";
				if (netDiskFolder.getPemission() != null) {
					folderPemission = netDiskFolder.getPemission().getId();
					if (!StringUtil.isBlank(folderPemission)) {
						if (folderPemission.equals(parentPemissionId)) {
							flag = true;
						} else {
							flag = false;
						}
					}
				} else {
					flag = true;
				}
				if (flag) {
					if (!StringUtil.isBlank(pemissionId)) {
						NetDiskPemissionProcess netDiskPemissionProcess = (NetDiskPemissionProcess) ProcessFactory
								.createProcess(NetDiskPemissionProcess.class);
						NetDiskPemission netDiskPemission = (NetDiskPemission) netDiskPemissionProcess
								.doView(pemissionId);
						netDiskFolder.setPemission(netDiskPemission);
						netDiskFolderProcess.doUpdate(netDiskFolder);
					} else {
						netDiskFolder.setPemission(null);
						netDiskFolderProcess.doUpdate(netDiskFolder);
					}
				}
				saveChildFolderPemission(netDiskFolder.getId(), pemissionId,
						folderPemission);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// 总的页数
	protected static int getTotalPages(int totalRows, int pageSize) {
		int totalPagesTemp = totalRows / pageSize;
		int mod = totalRows % pageSize;
		if (mod > 0) {
			totalPagesTemp += 1;
		}
		return totalPagesTemp;
	}

	// 迭代获得相应文件
	public static String getFiles(String path, String savePath,
			String folderId, String userid, int _currpage, int _pagelines,
			String userName) {
		StringBuffer sb = new StringBuffer();
		try {
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);
			if(!StringUtil.isBlank(folderId)){
				ParamsTable params = new ParamsTable();
				params.setParameter("t_folderid", folderId);
				params.setParameter("_currpage", _currpage);
				params.setParameter("_pagelines", _pagelines);
				params.setParameter("sm_name", userName);
				params.setParameter("t_userid", userid);
				DataPackage<NetDiskFile> datapackage = netDiskFileProcess
						.doQuery(params);
				if (datapackage.rowCount > 0) {
					sb.append("{\"files\":[");
					for (Iterator<NetDiskFile> it = datapackage.getDatas()
							.iterator(); it.hasNext();) {
						NetDiskFile netDiskFile = (NetDiskFile) it.next();
						sb.append("{\"name\":\"");
						sb.append(netDiskFile.getName());
						sb.append("\",\"id\":\"");
						sb.append(netDiskFile.getId());
						sb.append("\",\"type\":\"");
						sb.append(netDiskFile.getType());
						sb.append("\",\"size\":\"");
						if (netDiskFile.getSize() < 1024) {
							sb.append((double) netDiskFile.getSize() + " B");
						} else if (netDiskFile.getSize() >= 1024
								&& netDiskFile.getSize() < (1024 * 1024)) {
							sb.append((double) (netDiskFile.getSize() / 1024)
									+ " KB");
						} else if (netDiskFile.getSize() >= (1024 * 1024)) {
							sb.append((double) (netDiskFile.getSize() / (1024 * 1024))
									+ " M");
						}
	
						sb.append("\",\"modifyTime\":\"");
						sb.append(netDiskFile.getModifyTime());
						if (userid == null || userid.equals("")) {
							sb.append("\",\"userName\":\"");
							if (superUserProcess.doView(netDiskFile.getUserid()) != null) {
								sb.append(((SuperUserVO) superUserProcess
										.doView(netDiskFile.getUserid())).getName());
							} else if (userProcess.doView(netDiskFile.getUserid()) != null) {
								sb.append(((UserVO) userProcess.doView(netDiskFile
										.getUserid())).getName());
							}
						}
						sb.append("\",\"folderPath\":\"");
						sb.append(netDiskFile.getFolderPath());
						sb.append("\",\"fileWebPath\":\"");
						sb.append(netDiskFile.getFolderWebPath() + "/"
								+ netDiskFile.getName());
						sb.append("\",\"share\":\"");
						sb.append(netDiskFile.getPemission() != null);
						sb.append("\",\"pemissionid\":\"");
						sb.append(netDiskFile.getPemission() != null ? netDiskFile
								.getPemission().getId() : "");
	
						sb.append("\"},");
					}
					if (sb.lastIndexOf(",") != -1) {
						sb.deleteCharAt(sb.lastIndexOf(","));
					}
					sb.append("],\"totalPages\":\""
							+ getTotalPages(datapackage.rowCount, _pagelines)
							+ "\"}");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		}
		// System.out.println("getFiles:"+sb.toString());
		return sb.toString();
	}

	// 迭代获得相应公共文件
	public static String getFilesPublic(String path, String savePath,
			String folderId, String userid, String operate, int _currpage,
			int _pagelines, String userName) {
		StringBuffer sb = new StringBuffer();
		try {
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);
			if(!StringUtil.isBlank(folderId)){
				ParamsTable params = new ParamsTable();
				params.setParameter("t_folderId", folderId);
				params.setParameter("_currpage", _currpage);
				params.setParameter("_pagelines", _pagelines);
				params.setParameter("sm_name", userName);
				DataPackage<NetDiskFile> datapackage = netDiskFileProcess
						.doQuery(params);
				NetDiskPemission netDiskPemission = null;
	
				if (datapackage.rowCount > 0) {
					sb.append("{\"files\":[");
					for (Iterator<NetDiskFile> it = datapackage.getDatas()
							.iterator(); it.hasNext();) {
						NetDiskFile netDiskFile = (NetDiskFile) it.next();
						sb.append("{\"name\":\"");
						sb.append(netDiskFile.getName());
						sb.append("\",\"id\":\"");
						sb.append(netDiskFile.getId());
						sb.append("\",\"type\":\"");
						sb.append(netDiskFile.getType());
						sb.append("\",\"size\":\"");
						if (netDiskFile.getSize() < 1024) {
							sb.append((double) netDiskFile.getSize() + " B");
						} else if (netDiskFile.getSize() >= 1024
								&& netDiskFile.getSize() < (1024 * 1024)) {
							sb.append((double) (netDiskFile.getSize() / 1024)
									+ " KB");
						} else if (netDiskFile.getSize() >= (1024 * 1024)) {
							sb.append((double) (netDiskFile.getSize() / (1024 * 1024))
									+ " M");
						}
	
						sb.append("\",\"modifyTime\":\"");
						sb.append(netDiskFile.getModifyTime());
						sb.append("\",\"userName\":\"");
						if (superUserProcess.doView(netDiskFile.getUserid()) != null) {
							sb.append(((SuperUserVO) superUserProcess
									.doView(netDiskFile.getUserid())).getName());
						} else if (userProcess.doView(netDiskFile.getUserid()) != null) {
							sb.append(((UserVO) userProcess.doView(netDiskFile
									.getUserid())).getName());
						}
						sb.append("\",\"folderPath\":\"");
						sb.append(netDiskFile.getFolderPath());
						sb.append("\",\"fileWebPath\":\"");
						sb.append(netDiskFile.getFolderWebPath() + "/"
								+ netDiskFile.getName());
						sb.append("\",\"share\":\"");
						boolean flag = false;
						if (userid != null && !userid.equals("")
								&& netDiskFile.getPemission() != null) {
							netDiskPemission = netDiskFile.getPemission();
							if (netDiskPemission.getSelectObject().equals("用户")) {
								if (netDiskPemission.getUsers().indexOf(userid) != -1
										|| superUserProcess.doView(userid) != null) {
									flag = true;
								}
							} else if (netDiskPemission.getSelectObject().equals(
									"部门")) {
								DepartmentVO department = (DepartmentVO) netDiskPemission
										.getDepartment();
								for (Iterator<UserVO> iterator = department
										.getUsers().iterator(); iterator.hasNext();) {
									UserVO user = (UserVO) iterator.next();
									if (user.getId().equals(userid)
											|| superUserProcess.doView(userid) != null) {
										flag = true;
										break;
									}
								}
							} else if (netDiskPemission.getSelectObject().equals(
									"角色")) {
								RoleVO role = (RoleVO) netDiskPemission.getRole();
								for (Iterator<UserVO> iterator = role.getUsers()
										.iterator(); iterator.hasNext();) {
									UserVO user = (UserVO) iterator.next();
									if (user.getId().equals(userid)
											|| superUserProcess.doView(userid) != null) {
										flag = true;
										break;
									}
								}
							} else if (netDiskPemission.getSelectObject().equals(
									"组")) {
								NetDiskGroup group = (NetDiskGroup) netDiskPemission
										.getGroup();
								if (group.getUseridGroup().indexOf(userid) != -1
										|| superUserProcess.doView(userid) != null) {
									flag = true;
								}
							}
							if (flag) {
								sb.append(true);
								sb.append("\",\"operate\":\"");
								sb.append(operate + ","
										+ netDiskPemission.getOperate());
							} else {
								sb.append(true);
								sb.append("\",\"operate\":\"");
								sb.append(operate);
							}
						} else {
							sb.append(netDiskFile.getPemission() != null);
							sb.append("\",\"operate\":\"");
							sb.append(operate);
						}
						sb.append("\",\"pemissionid\":\"");
						sb.append(netDiskFile.getPemission() != null ? netDiskFile
								.getPemission().getId() : "");
						sb.append("\"},");
					}
					if (sb.lastIndexOf(",") != -1) {
						sb.deleteCharAt(sb.lastIndexOf(","));
					}
					sb.append("],\"totalPages\":\""
							+ getTotalPages(datapackage.rowCount, _pagelines)
							+ "\"}");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// System.out.println("getFiles:"+sb.toString());
		return sb.toString();
	}

	// 迭代获得相应共享文件
	public static String getShareFiles(String userid, int _currpage,
			int _pagelines, String userName) {
		StringBuffer sb = new StringBuffer();
		try {
			NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);

			ParamsTable params = new ParamsTable();
			params.setParameter("_currpage", _currpage);
			params.setParameter("_pagelines", _pagelines);
			params.setParameter("sm_name", userName);
			params.setParameter("inn_pemission", "11");
			params.setParameter("t_userid", userid);
			DataPackage<NetDiskFile> datapackage = netDiskFileProcess
					.doQuery(params);
			if (datapackage.rowCount > 0) {
				sb.append("{\"files\":[");
				for (Iterator<NetDiskFile> it = datapackage.getDatas()
						.iterator(); it.hasNext();) {
					NetDiskFile netDiskFile = (NetDiskFile) it.next();
					sb.append("{\"name\":\"");
					sb.append(netDiskFile.getName());
					sb.append("\",\"id\":\"");
					sb.append(netDiskFile.getId());
					sb.append("\",\"type\":\"");
					sb.append(netDiskFile.getType());
					sb.append("\",\"size\":\"");
					if (netDiskFile.getSize() < 1024) {
						sb.append((double) netDiskFile.getSize() + " B");
					} else if (netDiskFile.getSize() >= 1024
							&& netDiskFile.getSize() < (1024 * 1024)) {
						sb.append((double) (netDiskFile.getSize() / 1024)
								+ " KB");
					} else if (netDiskFile.getSize() >= (1024 * 1024)) {
						sb.append((double) (netDiskFile.getSize() / (1024 * 1024))
								+ " M");
					}
					sb.append("\",\"modifyTime\":\"");
					sb.append(netDiskFile.getModifyTime());
					sb.append("\",\"shareTime\":\"");
					sb.append(netDiskFile.getShareTime());
					sb.append("\",\"fileWebPath\":\"");
					String temp = netDiskFile.getFolderWebPath().substring(
							netDiskFile.getFolderWebPath().indexOf("/") + 1);
					temp = temp.substring(temp.indexOf("/") + 1);
					if (temp.indexOf("/") == -1) {
						sb.append("我的网盘[根目录]" + "/" + netDiskFile.getName());
					} else {
						temp = temp.substring(temp.indexOf("/"));
						sb.append("我的网盘[根目录]" + temp + "/"
								+ netDiskFile.getName());
					}
					sb.append("\",\"share\":\"");
					sb.append(netDiskFile.getPemission() != null);
					sb.append("\",\"pemissionid\":\"");
					sb.append(netDiskFile.getPemission() != null ? netDiskFile
							.getPemission().getId() : "");
					sb.append("\"},");
				}
				if (sb.lastIndexOf(",") != -1) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("],\"totalPages\":\""
						+ getTotalPages(datapackage.rowCount, _pagelines)
						+ "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		NetworkDisk net = new NetworkDisk();
		System.out.println(net.getuserShareFiles(
				"11de-c13a-26b53fc4-a3db-1bc87eaaad4c", 1, 15, ""));
	}

	// 迭代获得相应好友共享文件
	public static String getuserShareFiles(String userid, int _currpage,
			int _pagelines, String userName) {
		StringBuffer sb = new StringBuffer();
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);

			UserVO user = (UserVO) userProcess.doView(userid);

			ParamsTable params = new ParamsTable();
			params.setParameter("sm_name", userName);
			params.setParameter("inn_pemission", "11");
			DataPackage<NetDiskFile> datapackage = netDiskFileProcess
					.doQuery(params);
			int number = 0;
			if (datapackage.rowCount > 0) {
				sb.append("{\"files\":[");
				for (Iterator<NetDiskFile> it = datapackage.getDatas()
						.iterator(); it.hasNext();) {
					NetDiskFile netDiskFile = (NetDiskFile) it.next();
					boolean flag = false;
					if (!netDiskFile.getPemission().getUserid().equals(userid)
							&& netDiskFile.getPemission().getUsers() != null) {
						if (netDiskFile.getPemission().getUsers()
								.indexOf(userid) != -1) {
							flag = true;
						}
					} else if (!netDiskFile.getPemission().getUserid()
							.equals(userid)
							&& netDiskFile.getPemission().getDepartment() != null) {
						for (Iterator<DepartmentVO> iterator = user
								.getDepartments().iterator(); iterator
								.hasNext();) {
							DepartmentVO department = (DepartmentVO) iterator
									.next();
							if (netDiskFile.getPemission().getDepartment()
									.getId().equals(department.getId())) {
								flag = true;
								break;
							}
						}
					} else if (!netDiskFile.getPemission().getUserid()
							.equals(userid)
							&& netDiskFile.getPemission().getRole() != null) {
						for (Iterator<RoleVO> iterator = user.getRoles()
								.iterator(); iterator.hasNext();) {
							RoleVO role = (RoleVO) iterator.next();
							if (role.getId().equals(
									netDiskFile.getPemission().getRole()
											.getId())) {
								flag = true;
								break;
							}
						}
					} else if (!netDiskFile.getPemission().getUserid()
							.equals(userid)
							&& netDiskFile.getPemission().getGroup() != null) {
						if (netDiskFile.getPemission().getGroup()
								.getUseridGroup().indexOf(userid) != -1) {
							flag = true;
						}
					}
					if (flag) {
						number++;
						if (number > ((_currpage - 1) * _pagelines)
								&& number < (((_currpage) * _pagelines) + 1)) {
							sb.append("{\"name\":\"");
							sb.append(netDiskFile.getName());
							sb.append("\",\"id\":\"");
							sb.append(netDiskFile.getId());
							sb.append("\",\"type\":\"");
							sb.append(netDiskFile.getType());
							sb.append("\",\"size\":\"");
							if (netDiskFile.getSize() < 1024) {
								sb.append((double) netDiskFile.getSize() + " B");
							} else if (netDiskFile.getSize() >= 1024
									&& netDiskFile.getSize() < (1024 * 1024)) {
								sb.append((double) (netDiskFile.getSize() / 1024)
										+ " KB");
							} else if (netDiskFile.getSize() >= (1024 * 1024)) {
								sb.append((double) (netDiskFile.getSize() / (1024 * 1024))
										+ " M");
							}
							sb.append("\",\"modifyTime\":\"");
							sb.append(netDiskFile.getModifyTime());
							sb.append("\",\"shareTime\":\"");
							sb.append(netDiskFile.getShareTime());
							sb.append("\",\"userName\":\"");
							if (netDiskFile.getUserid() != null) {
								sb.append(((UserVO) userProcess
										.doView(netDiskFile.getUserid())) != null ? ((UserVO) userProcess
										.doView(netDiskFile.getUserid()))
										.getName() : "匿名");
							} else {
								sb.append("匿名");
							}
							sb.append("\",\"folderPath\":\"");
							sb.append(netDiskFile.getFolderPath());
							sb.append("\",\"fileWebPath\":\"");
							sb.append(netDiskFile.getFolderWebPath() + "/"
									+ netDiskFile.getName());
							sb.append("\",\"share\":\"");
							sb.append(netDiskFile.getPemission() != null);
							if (netDiskFile.getPemission() != null) {
								sb.append("\",\"operate\":\"");
								sb.append(netDiskFile.getPemission()
										.getOperate());
							}
							sb.append("\"},");
						}
					}
				}
				if (sb.lastIndexOf(",") != -1) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("],\"totalPages\":\""
						+ getTotalPages(number, _pagelines) + "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// 获得网盘基本信息
	public static String getInfo(String userid) {
		StringBuffer sb = new StringBuffer();
		try {
			NetDiskProcess netDiskProcess = (NetDiskProcess) ProcessFactory
					.createProcess(NetDiskProcess.class);
			NetDisk netDisk = (NetDisk) netDiskProcess.doView(userid);
			if (netDisk != null) {
				sb.append("{\"id\":\"");
				sb.append(netDisk.getId());
				sb.append("\",\"totalSizeLabel\":\"");
				if (netDisk.getTotalSize() < 1024) {
					sb.append(netDisk.getTotalSize() + " B");
				} else if (netDisk.getTotalSize() >= 1024
						&& netDisk.getTotalSize() < (1024 * 1024)) {
					sb.append(df.format(netDisk.getTotalSize() / 1024) + " KB");
				} else if (netDisk.getTotalSize() >= (1024 * 1024)) {
					sb.append(df.format(netDisk.getTotalSize()
							/ ((1024 * 1024)))
							+ " M");
				}
				sb.append("\",\"haveUseSizeLabel\":\"");
				if (netDisk.getHaveUseSize() < 1024) {
					sb.append(netDisk.getHaveUseSize() + " B");
				} else if (netDisk.getHaveUseSize() >= 1024
						&& netDisk.getHaveUseSize() < (1024 * 1024)) {
					sb.append(df.format(netDisk.getHaveUseSize() / 1024)
							+ " KB");
				} else if (netDisk.getHaveUseSize() >= (1024 * 1024)) {
					sb.append(df.format(netDisk.getHaveUseSize()
							/ ((1024 * 1024)))
							+ " M");
				}
				sb.append("\",\"canUseSizeLabel\":\"");
				if ((netDisk.getTotalSize() - netDisk.getHaveUseSize()) < 1024) {
					sb.append(netDisk.getTotalSize() - netDisk.getHaveUseSize()
							+ " B");
				} else if ((netDisk.getTotalSize() - netDisk.getHaveUseSize()) >= 1024
						&& (netDisk.getTotalSize() - netDisk.getHaveUseSize()) < (1024 * 1024)) {
					sb.append(df.format((netDisk.getTotalSize() - netDisk
							.getHaveUseSize()) / 1024) + " KB");
				} else if ((netDisk.getTotalSize() - netDisk.getHaveUseSize()) >= (1024 * 1024)) {
					long temp = netDisk.getTotalSize()
							- netDisk.getHaveUseSize();
					float result = temp / 1024;
					result = result / 1024;
					sb.append(df.format(result) + " M");
				}
				sb.append("\",\"totalSize\":\"");
				sb.append(netDisk.getTotalSize());
				sb.append("\",\"haveUseSize\":\"");
				sb.append(netDisk.getHaveUseSize());
				sb.append("\",\"uploadSize\":\"");
				sb.append(netDisk.getUploadSize());
				sb.append("\",\"pemission\":\"");
				sb.append(netDisk.getPemission() == null ? "false" : netDisk
						.getPemission());
				sb.append("\"}");
			} else {
				sb.append("{\"pemission\":\"false\"}");
			}
			// System.out.println("getInfo:"+sb.toString());
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		}
	}

	// 获得用户列表
	public static String getAllUser(String userid, String domainid,
			String userids, int _currpage, int _pagelines, String userName) {
		StringBuffer sb = new StringBuffer();
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_domainid", domainid);
			params.setParameter("_currpage", _currpage);
			params.setParameter("_pagelines", _pagelines);
			if (userName != null && !userName.equals("")) {
				params.setParameter("sm_name", userName);
			}
			DataPackage<UserVO> dataPackage = userProcess.doQuery(params);
			if (dataPackage.rowCount > 0) {
				sb.append("{\"users\":[");
				for (Iterator<UserVO> it = dataPackage.getDatas().iterator(); it
						.hasNext();) {
					UserVO userVO = (UserVO) it.next();
					if (!userVO.getId().equals(userid)) {
						sb.append("{\"id\":\"");
						sb.append(userVO.getId());
						sb.append("\",\"name\":\"");
						sb.append(userVO.getName());
						sb.append("\",\"selected\":");
						if (userids.equals(userVO.getId())
								|| userids.indexOf(userVO.getId()) != -1) {
							sb.append("true},");
						} else {
							sb.append("false},");
						}
					}
				}
				if (sb.lastIndexOf(",") != -1) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("],\"totalPages\":\""
						+ getTotalPages(dataPackage.rowCount, _pagelines)
						+ "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// 获得部门
	public static String getAllDepartment(String domainid, String departments,
			int _currpage, int _pagelines, String userName) {
		StringBuffer sb = new StringBuffer();
		try {
			DepartmentProcess departmentProcess = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_domainid", domainid);
			params.setParameter("_currpage", _currpage);
			params.setParameter("_pagelines", _pagelines);
			params.setParameter("sm_name", userName);
			DataPackage<DepartmentVO> dataPackage = departmentProcess
					.doQuery(params);
			if (dataPackage.rowCount > 0) {
				sb.append("{\"departments\":[");
				for (Iterator<DepartmentVO> it = dataPackage.getDatas()
						.iterator(); it.hasNext();) {
					DepartmentVO departmentVO = (DepartmentVO) it.next();
					sb.append("{\"id\":\"");
					sb.append(departmentVO.getId());
					sb.append("\",\"name\":\"");
					sb.append(departmentVO.getName());
					sb.append("\",\"selected\":");
					if (departments.equals(departmentVO.getId())) {
						sb.append("true},");
					} else {
						sb.append("false},");
					}
				}
				if (sb.lastIndexOf(",") != -1) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("],\"totalPages\":\""
						+ getTotalPages(dataPackage.rowCount, _pagelines)
						+ "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// System.out.println("getAllDepartment:"+sb.toString());
		return sb.toString();
	}

	// 获得角色
	public static String getAllRole(String applicationid, String roles,
			int _currpage, int _pagelines, String userName) {
		StringBuffer sb = new StringBuffer();
		try {
			RoleProcess roleProcess = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_applicationid", applicationid);
			params.setParameter("_currpage", _currpage);
			params.setParameter("_pagelines", _pagelines);
			params.setParameter("sm_name", userName);
			DataPackage<RoleVO> dataPackage = roleProcess.doQuery(params);
			if (dataPackage.rowCount > 0) {
				sb.append("{\"roles\":[");
				for (Iterator<RoleVO> it = dataPackage.getDatas().iterator(); it
						.hasNext();) {
					RoleVO roleVO = (RoleVO) it.next();
					sb.append("{\"id\":\"");
					sb.append(roleVO.getId());
					sb.append("\",\"name\":\"");
					sb.append(roleVO.getName());
					sb.append("\",\"selected\":");
					if (roles.equals(roleVO.getId())) {
						sb.append("true},");
					} else {
						sb.append("false},");
					}
				}
				if (sb.lastIndexOf(",") != -1) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("],\"totalPages\":\""
						+ getTotalPages(dataPackage.rowCount, _pagelines)
						+ "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// 获得角色
	@SuppressWarnings("null")
	public static String getAllRoleAndApplication(String applicationid,
			String roles, int _currpage, int _pagelines, String userName) {
		StringBuffer sb = new StringBuffer();
		try {
			RoleProcess roleProcess = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_applicationid", applicationid);
			params.setParameter("_currpage", _currpage);
			params.setParameter("_pagelines", _pagelines);
			params.setParameter("sm_name", userName);
			DataPackage<RoleVO> dataPackage = roleProcess.doQuery(params);
			if (dataPackage.rowCount > 0) {
				sb.append("{\"roles\":[");
				for (Iterator<RoleVO> it = dataPackage.getDatas().iterator(); it
						.hasNext();) {
					RoleVO roleVO = (RoleVO) it.next();
					ApplicationProcess process = (ApplicationProcess) ProcessFactory
							.createProcess(ApplicationProcess.class);
					ApplicationVO vo = (ApplicationVO) process.doView(roleVO
							.getApplicationid());
					if (vo == null) {
						continue;
					}
					sb.append("{\"id\":\"");
					sb.append(roleVO.getId());
					sb.append("\",\"name\":\"");
					sb.append(roleVO.getName());
					sb.append("\",\"application\":\"");
					sb.append(vo.getName());
					sb.append("\",\"selected\":");
					if (roles.equals(roleVO.getId())) {
						sb.append("true},");
					} else {
						sb.append("false},");
					}
				}
				if (sb.lastIndexOf(",") != -1) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("],\"totalPages\":\""
						+ getTotalPages(dataPackage.rowCount, _pagelines)
						+ "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// 获得组
	public static String getAllGroup(String userid, String groups,
			int _currpage, int _pagelines, String userName) {
		StringBuffer sb = new StringBuffer();
		try {
			NetDiskGroupProcess netDiskGroupProcess = (NetDiskGroupProcess) ProcessFactory
					.createProcess(NetDiskGroupProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_userid", userid);
			params.setParameter("_currpage", _currpage);
			params.setParameter("_pagelines", _pagelines);
			params.setParameter("sm_name", userName);
			DataPackage<NetDiskGroup> dataPackage = netDiskGroupProcess
					.doQuery(params);
			if (dataPackage.rowCount > 0) {
				sb.append("{\"groups\":[");
				for (Iterator<NetDiskGroup> it = dataPackage.getDatas()
						.iterator(); it.hasNext();) {
					NetDiskGroup netDiskGroup = (NetDiskGroup) it.next();
					sb.append("{\"id\":\"");
					sb.append(netDiskGroup.getId());
					sb.append("\",\"name\":\"");
					sb.append(netDiskGroup.getName());
					sb.append("\",\"description\":\"");
					sb.append(netDiskGroup.getDescription());
					sb.append("\",\"useridGroup\":\"");
					sb.append(netDiskGroup.getUseridGroup());
					sb.append("\",\"currpage\":\"");
					sb.append(_currpage);
					sb.append("\",\"selected\":");
					if (groups.equals(netDiskGroup.getId())) {
						sb.append("true},");
					} else {
						sb.append("false},");
					}
				}
				if (sb.lastIndexOf(",") != -1) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("],\"totalPages\":\""
						+ getTotalPages(dataPackage.rowCount, _pagelines)
						+ "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// 获得单个组
	public static String getGroup(String id) {
		StringBuffer sb = new StringBuffer();
		try {
			NetDiskGroupProcess netDiskGroupProcess = (NetDiskGroupProcess) ProcessFactory
					.createProcess(NetDiskGroupProcess.class);
			NetDiskGroup netDiskGroup = (NetDiskGroup) netDiskGroupProcess
					.doView(id);
			sb.append("{\"id\":\"");
			sb.append(netDiskGroup.getId());
			sb.append("\",\"name\":\"");
			sb.append(netDiskGroup.getName());
			sb.append("\",\"description\":\"");
			sb.append(netDiskGroup.getDescription());
			sb.append("\",\"useridGroup\":\"");
			sb.append(netDiskGroup.getUseridGroup());
			sb.append("\"}");
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// 保存组
	public static String saveGroup(String id, String name, String desc,
			String userid, String useridGroup) {
		try {
			NetDiskGroupProcess netDiskGroupProcess = (NetDiskGroupProcess) ProcessFactory
					.createProcess(NetDiskGroupProcess.class);
			NetDiskGroup netDiskGroup = null;
			if (id == null || id.equals("")) {
				netDiskGroup = new NetDiskGroup();
				netDiskGroup.setName(name);
				netDiskGroup.setDescription(desc);
				netDiskGroup.setUserid(userid);
				netDiskGroup.setUseridGroup(useridGroup);
				netDiskGroupProcess.doCreate(netDiskGroup);
				return "{\"icon\":\"assets/ok.png\",\"message\":\"新建组成功\"}";
			} else {
				netDiskGroup = (NetDiskGroup) netDiskGroupProcess.doView(id);
				netDiskGroup.setName(name);
				netDiskGroup.setDescription(desc);
				netDiskGroup.setUseridGroup(useridGroup);
				netDiskGroupProcess.doUpdate(netDiskGroup);
				return "{\"icon\":\"assets/ok.png\",\"message\":\"更新组成功\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 删除组
	public static String deleteGroup(String id, String userid) {
		try {
			NetDiskPemissionProcess netDiskPemissionProcess = (NetDiskPemissionProcess) ProcessFactory
					.createProcess(NetDiskPemissionProcess.class);
			NetDiskGroupProcess netDiskGroupProcess = (NetDiskGroupProcess) ProcessFactory
					.createProcess(NetDiskGroupProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_userid", userid);
			params.setParameter("t_usergroup", id);
			DataPackage<NetDiskPemission> dataPackage = netDiskPemissionProcess
					.doQuery(params);
			if (dataPackage.rowCount > 0) {
				for (Iterator<NetDiskPemission> it = dataPackage.getDatas()
						.iterator(); it.hasNext();) {
					NetDiskPemission netDiskPemission = (NetDiskPemission) it
							.next();
					if (netDiskPemission.getGroup() != null) {
						netDiskPemission.setGroup(null);
						netDiskPemissionProcess.doUpdate(netDiskPemission);
					}
				}
			}
			netDiskGroupProcess.doRemove(id);
			return "{\"icon\":\"assets/ok.png\",\"message\":\"删除组成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 设置共享
	public static String setShare(String fileid, String pemessionid) {
		try {
			NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);
			NetDiskFile netDiskFile = (NetDiskFile) netDiskFileProcess
					.doView(fileid);
			NetDiskPemissionProcess netDiskPemissionProcess = (NetDiskPemissionProcess) ProcessFactory
					.createProcess(NetDiskPemissionProcess.class);
			NetDiskPemission netDiskPemission = (NetDiskPemission) netDiskPemissionProcess
					.doView(pemessionid);
			netDiskFile.setPemission(netDiskPemission);
			netDiskFile.setShareTime(new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date()));
			netDiskFileProcess.doUpdate(netDiskFile);
			return "{\"icon\":\"assets/ok.png\",\"message\":\"设置共享成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 撤销共享
	public static String cancelShare(String fileid) {
		try {
			NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);
			NetDiskFile netDiskFile = (NetDiskFile) netDiskFileProcess
					.doView(fileid);
			netDiskFile.setPemission(null);
			netDiskFile.setShareTime(null);
			netDiskFileProcess.doUpdate(netDiskFile);
			return "{\"icon\":\"assets/ok.png\",\"message\":\"撤销共享成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 删除文件
	public static String deleteNetDiskFile(String userid, String fileid) {
		try {
			NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);
			NetDiskFile netDiskFile = (NetDiskFile) netDiskFileProcess
					.doView(fileid);
			File file = new File(Environment.getInstance().getApplicationRealPath() + File.separator + "networkdisk"+ File.separator + netDiskFile.getFolderPath() + File.separator
					+ netDiskFile.getName());
			if (file.exists()) {
				if (!file.delete())
					throw new IOException("delete file '"
							+ file.getAbsolutePath() + "' failed!");
			}

			if (userid != null && !userid.equals("")
					&& netDiskFile.getFolderPath().indexOf("public") == -1) {
				NetDiskProcess netDiskProcess = (NetDiskProcess) ProcessFactory
						.createProcess(NetDiskProcess.class);
				NetDisk netDisk = (NetDisk) netDiskProcess.doView(userid);
				netDisk.setHaveUseSize(netDisk.getHaveUseSize()
						- netDiskFile.getSize());
				netDiskProcess.doUpdate(netDisk);
			}
			netDiskFileProcess.doRemove(netDiskFile);
			return "{\"icon\":\"assets/ok.png\",\"message\":\"删除文件成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 获得权限列表
	public static String getPemission(String userid, String pemission,
			int _currpage, int _pagelines, String userName) {
		StringBuffer sb = new StringBuffer();
		try {
			NetDiskPemissionProcess netDiskPemissionProcess = (NetDiskPemissionProcess) ProcessFactory
					.createProcess(NetDiskPemissionProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_userid", userid);
			params.setParameter("_currpage", _currpage);
			params.setParameter("_pagelines", _pagelines);
			DataPackage<NetDiskPemission> dataPackage = netDiskPemissionProcess
					.doQuery(params);
			if (dataPackage.rowCount > 0) {
				sb.append("{\"pemission\":[");
				for (Iterator<NetDiskPemission> it = dataPackage.getDatas()
						.iterator(); it.hasNext();) {
					NetDiskPemission netDiskPemission = (NetDiskPemission) it
							.next();
					sb.append("{\"id\":\"");
					sb.append(netDiskPemission.getId());
					sb.append("\",\"name\":\"");
					sb.append(netDiskPemission.getName());
					sb.append("\",\"type\":\"");
					if (netDiskPemission.getType().equals("folder")) {
						sb.append("文件夹");
					} else {
						sb.append("文件");
					}
					sb.append("\",\"selectObject\":\"");
					if (netDiskPemission.getSelectObject().equals("用户")) {
						sb.append("用户");
						sb.append("\",\"pemission\":\"");
						sb.append(netDiskPemission.getUsers() != null ? netDiskPemission
								.getUsers() : "");
					} else if (netDiskPemission.getSelectObject().equals("部门")) {
						sb.append("部门");
						sb.append("\",\"pemission\":\"");
						sb.append(netDiskPemission.getDepartment() != null ? netDiskPemission
								.getDepartment().getId() : "");
					} else if (netDiskPemission.getSelectObject().equals("角色")) {
						sb.append("角色");
						sb.append("\",\"pemission\":\"");
						sb.append(netDiskPemission.getRole() != null ? netDiskPemission
								.getRole().getId() : "");
					} else if (netDiskPemission.getSelectObject().equals("组")) {
						sb.append("组");
						sb.append("\",\"pemission\":\"");
						sb.append(netDiskPemission.getGroup() != null ? netDiskPemission
								.getGroup().getId() : "");
					}
					sb.append("\",\"currpage\":\"");
					sb.append(_currpage);
					sb.append("\",\"operate\":\"");
					sb.append(netDiskPemission.getOperate());
					// 获得已设置权限文件夹
					sb.append("\",\"selected\":");
					if (pemission.indexOf(netDiskPemission.getId()) != -1) {
						sb.append("true");
					} else {
						sb.append("false");
					}
					sb.append("},");
				}
				if (sb.lastIndexOf(",") != -1) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("],\"totalPages\":\""
						+ getTotalPages(dataPackage.rowCount, _pagelines)
						+ "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// 创建权限
	public static String createPemission(String id, String userid, String name,
			String type, String selectObject, String pemission, String operate) {
		try {
			NetDiskPemission netDiskPemission = null;
			NetDiskPemissionProcess netDiskPemissionProcess = (NetDiskPemissionProcess) ProcessFactory
					.createProcess(NetDiskPemissionProcess.class);
			if (id == "") {
				netDiskPemission = new NetDiskPemission();
				netDiskPemission.setUserid(userid);
				netDiskPemission.setName(name);
				netDiskPemission.setType(type);
				netDiskPemission.setSelectObject(selectObject);
				if (selectObject.equals("用户")) {
					netDiskPemission.setUsers(pemission);
				} else if (selectObject.equals("部门")) {
					DepartmentProcess departmentProcess = (DepartmentProcess) ProcessFactory
							.createProcess(DepartmentProcess.class);
					DepartmentVO department = (DepartmentVO) departmentProcess
							.doView(pemission);
					netDiskPemission.setDepartment(department);
				} else if (selectObject.equals("角色")) {
					RoleProcess roleProcess = (RoleProcess) ProcessFactory
							.createProcess(RoleProcess.class);
					RoleVO role = (RoleVO) roleProcess.doView(pemission);
					netDiskPemission.setRole(role);
				} else if (selectObject.equals("组")) {
					NetDiskGroupProcess netDiskGroupProcess = (NetDiskGroupProcess) ProcessFactory
							.createProcess(NetDiskGroupProcess.class);
					NetDiskGroup group = (NetDiskGroup) netDiskGroupProcess
							.doView(pemission);
					netDiskPemission.setGroup(group);
				}
				netDiskPemission.setOperate(operate);
				netDiskPemissionProcess.doCreate(netDiskPemission);
				return "{\"icon\":\"assets/ok.png\",\"message\":\"创建权限成功\"}";
			} else {
				netDiskPemission = (NetDiskPemission) netDiskPemissionProcess
						.doView(id);
				if (!name.equals("")) {
					netDiskPemission.setName(name);
				}
				if (!type.equals("")) {
					netDiskPemission.setType(type);
				}
				if (!selectObject.equals("")) {
					netDiskPemission.setSelectObject(selectObject);
				}
				if (!pemission.equals("")) {
					if (netDiskPemission.getSelectObject().equals("用户")) {
						netDiskPemission.setUsers(pemission);
					} else if (netDiskPemission.getSelectObject().equals("部门")) {
						DepartmentProcess departmentProcess = (DepartmentProcess) ProcessFactory
								.createProcess(DepartmentProcess.class);
						DepartmentVO department = (DepartmentVO) departmentProcess
								.doView(pemission);
						netDiskPemission.setDepartment(department);
					} else if (netDiskPemission.getSelectObject().equals("角色")) {
						RoleProcess roleProcess = (RoleProcess) ProcessFactory
								.createProcess(RoleProcess.class);
						RoleVO role = (RoleVO) roleProcess.doView(pemission);
						netDiskPemission.setRole(role);
					} else if (netDiskPemission.getSelectObject().equals("组")) {
						NetDiskGroupProcess netDiskGroupProcess = (NetDiskGroupProcess) ProcessFactory
								.createProcess(NetDiskGroupProcess.class);
						NetDiskGroup group = (NetDiskGroup) netDiskGroupProcess
								.doView(pemission);
						netDiskPemission.setGroup(group);
					}
				}
				if (!operate.equals("")) {
					netDiskPemission.setOperate(operate);
				}
				netDiskPemissionProcess.doUpdate(netDiskPemission);
				return "{\"icon\":\"assets/ok.png\",\"message\":\"更新权限成功\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 删除权限
	public static String delPemission(String userid, String id) {
		try {
			ParamsTable params = new ParamsTable();
			params.setParameter("t_userid", userid);
			params.setParameter("t_pemission", id);
			NetDiskFileProcess netDiskFileProcess = (NetDiskFileProcess) ProcessFactory
					.createProcess(NetDiskFileProcess.class);
			DataPackage<NetDiskFile> datapackage = netDiskFileProcess
					.doQuery(params);
			for (Iterator<NetDiskFile> it = datapackage.getDatas().iterator(); it
					.hasNext();) {
				NetDiskFile netDiskFile = (NetDiskFile) it.next();
				netDiskFile.setPemission(null);
				netDiskFileProcess.doUpdate(netDiskFile);
			}
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory
					.createProcess(NetDiskFolderProcess.class);
			DataPackage<NetDiskFolder> datapackage1 = netDiskFolderProcess
					.doQuery(params);
			for (Iterator<NetDiskFolder> it = datapackage1.getDatas()
					.iterator(); it.hasNext();) {
				NetDiskFolder netDiskFolder = (NetDiskFolder) it.next();
				netDiskFolder.setPemission(null);
				netDiskFileProcess.doUpdate(netDiskFolder);
			}
			NetDiskPemissionProcess netDiskPemissionProcess = (NetDiskPemissionProcess) ProcessFactory
					.createProcess(NetDiskPemissionProcess.class);
			netDiskPemissionProcess.doRemove(id);
			return "{\"icon\":\"assets/ok.png\",\"message\":\"删除权限成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 后台获得权限列表 kharry
	public static String getPemissionAdmin(String userid, String pemission,
			int _currpage, int _pagelines, String userName, String type) {
		StringBuffer sb = new StringBuffer();
		try {
			NetDiskPemissionProcess netDiskPemissionProcess = (NetDiskPemissionProcess) ProcessFactory
					.createProcess(NetDiskPemissionProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("t_userid", userid);
			params.setParameter("_currpage", _currpage);
			params.setParameter("_pagelines", _pagelines);
			params.setParameter("t_type", type);
			params.setParameter("sm_name", userName);
			DataPackage<NetDiskPemission> dataPackage = netDiskPemissionProcess
					.doQuery(params);
			if (dataPackage.rowCount > 0) {
				sb.append("{\"pemission\":[");
				for (Iterator<NetDiskPemission> it = dataPackage.getDatas()
						.iterator(); it.hasNext();) {
					NetDiskPemission netDiskPemission = (NetDiskPemission) it
							.next();
					sb.append("{\"id\":\"");
					sb.append(netDiskPemission.getId());
					sb.append("\",\"name\":\"");
					sb.append(netDiskPemission.getName());
					sb.append("\",\"type\":\"");
					if (netDiskPemission.getType().equals("文件夹")
							|| netDiskPemission.getType().equals("文件")) {
						sb.append(netDiskPemission.getType());
					} else {
						sb.append("文件");
					}
					sb.append("\",\"selectObject\":\"");
					if (netDiskPemission.getSelectObject().equals("用户")) {
						sb.append("用户");
						sb.append("\",\"pemission\":\"");
						sb.append(netDiskPemission.getUsers() != null ? netDiskPemission
								.getUsers() : "");
					} else if (netDiskPemission.getSelectObject().equals("部门")) {
						sb.append("部门");
						sb.append("\",\"pemission\":\"");
						sb.append(netDiskPemission.getDepartment() != null ? netDiskPemission
								.getDepartment().getId() : "");
					} else if (netDiskPemission.getSelectObject().equals("角色")) {
						sb.append("角色");
						sb.append("\",\"pemission\":\"");
						sb.append(netDiskPemission.getRole() != null ? netDiskPemission
								.getRole().getId() : "");
					} else if (netDiskPemission.getSelectObject().equals("组")) {
						sb.append("组");
						sb.append("\",\"pemission\":\"");
						sb.append(netDiskPemission.getGroup() != null ? netDiskPemission
								.getGroup().getId() : "");
					}
					sb.append("\",\"currpage\":\"");
					sb.append(_currpage);
					sb.append("\",\"operate\":\"");
					sb.append(netDiskPemission.getOperate());
					// 获得已设置权限文件夹
					sb.append("\",\"selected\":");
					if (pemission.indexOf(netDiskPemission.getId()) != -1) {
						sb.append("true");
					} else {
						sb.append("false");
					}
					sb.append("},");
				}
				if (sb.lastIndexOf(",") != -1) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("],\"totalPages\":\""
						+ getTotalPages(dataPackage.rowCount, _pagelines)
						+ "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""
					+ e.getMessage() + "\"}";
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}
