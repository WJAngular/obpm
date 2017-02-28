package cn.myapps.core.filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.privilege.operation.ejb.OperationVO;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

public class FileOperate {
	
	private static final Logger Log = Logger.getLogger(FileOperate.class);

	private String applicationId;
	
	public FileOperate() {
		
	}

	/**
	 * 通过获得根目录来获得根目录下的子目录组成一个数字符串
	 * 
	 * @param folderPath
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String loadFolderTree(String folderPath, String rolelist,String showPath) {
		StringBuffer sb = new StringBuffer();
		rolelist = rolelist.replaceAll("'", "");
		String[] roles = rolelist.split(",");
		
		
		if(roles.length>0 && applicationId ==null){
			try {
			
				RoleProcess process = (RoleProcess) ProcessFactory.createProcess(RoleProcess.class);
				applicationId = process.doView(roles[0]).getApplicationid();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory
					.createProcess(PermissionProcess.class);
			File f = new File(folderPath);
			if(f!=null){
				sb.append("<tree>");
				sb.append("<node label='" + f.getName() + "' ");
				sb.append("Create='"+isCreate(roles,f,permissionProcess,false)+"' ");
				sb.append("Delete='"+isDelete(roles, f, permissionProcess, false)+"' ");
				sb.append("Rename='"+isRename(roles, f, permissionProcess, false)+"' ");
				sb.append("Upload='"+isUpload(roles, f, permissionProcess, false)+"' ");
				sb.append("path='"+f.toString().substring(f.toString().lastIndexOf(f.getName()))+"' ");
				sb.append("realPath='"+f.toString()+"'");
				sb.append(">");
				sb = createNode(f.listFiles(), sb, permissionProcess, roles, f.getName(),isCreate(roles,f,permissionProcess,false),isDelete(roles,f,permissionProcess,false),isRename(roles,f,permissionProcess,false),isUpload(roles,f,permissionProcess,false));
				sb.append("</tree>");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sb.toString();
		}
	}

	// 递归方法
	public StringBuffer createNode(File[] list, StringBuffer sb, PermissionProcess permissionProcess, String[] roles,
			String rootName,boolean isCreate,boolean isDelete,boolean isRename,boolean isUpload) {
		try {
			if(list!=null){
				for (int i = 0; i < list.length; i++) {
					if (list[i].isDirectory()) {
						sb.append("<node label='" + list[i].getName() + "' ");
						isCreate = permissionProcess.check(roles, Sequence.getFileUUID(list[i], "uploads"),null,OperationVO.FOLDER_CREATE, ResVO.FOLDER_TYPE,applicationId,isCreate);
						sb.append("Create='"+isCreate+"' ");
						isDelete = permissionProcess.check(roles, Sequence.getFileUUID(list[i], "uploads"),null,OperationVO.FOLDER_DELETE, ResVO.FOLDER_TYPE,applicationId,isDelete);
						sb.append("Delete='"+isDelete+"' ");
						isRename = permissionProcess.check(roles, Sequence.getFileUUID(list[i], "uploads"),null,OperationVO.FOLDER_RENAME, ResVO.FOLDER_TYPE,applicationId,isRename);
						sb.append("Rename='"+isRename+"' ");
						isUpload = permissionProcess.check(roles, Sequence.getFileUUID(list[i], "uploads"),null,OperationVO.FILE_UPLOAD, ResVO.FOLDER_TYPE,applicationId,isUpload);
						sb.append("Upload='"+isUpload+"' ");
						sb.append("path='"+list[i].toString().substring(list[i].toString().lastIndexOf(rootName))+"' ");
						sb.append("realPath='"+list[i].toString()+"'");
						sb.append(">");
						if (list[i].listFiles() !=null && list[i].listFiles().length > 0) {
							this.createNode(list[i].listFiles(), sb, permissionProcess, roles, rootName,isCreate,isDelete,isRename,isUpload);
						} else {
							sb.append("</node>");
						}
					}
				}
			}
			sb.append("</node>");
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			return sb;
		}
	}
	
	
	/**
	 * 获得根目录下及根目录的所有目录的路径
	 */
	public String getAllFolderPaths(String folderPath) {
		StringBuffer sb = new StringBuffer();
		try {
			File f = new File(folderPath);
			if(f!=null){
				sb.append("<tree>");
				sb.append("<path realPath='"+f.toString()+"'>" + f.getName() + "</path>");
				File[] list = f.listFiles();
				if(list!=null){
					sb = createNodePath(list, f, f.getName(), sb);// 调用递归方法
				}
				sb.append("</tree>");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sb.toString();
		}
	}

	// 递归方法
	public StringBuffer createNodePath(File[] list, File f, String fileName, StringBuffer sb) {
		// Element scend = null;
		for (int i = 0; i < list.length; i++) {
			if (list[i].isDirectory()) {
				String tempPath = fileName;
				sb.append("<path  realPath='"+list[i].toString()+"'>");
				tempPath += "\\" + list[i].getName();
				sb.append(tempPath);// 增加内容
				sb.append("</path>");
				if (list[i].listFiles().length > 0) {
					this.createNodePath(list[i].listFiles(), f, tempPath, sb);
				}

			}
		}
		return sb;
	}
	
	/**
	 * 通过自定的文件目录路径来获得其下所有文件以组成一组字符串
	 * 
	 * @throws Exception
	 */
	public String getFolderFile(String folderPath, String rolelist ,String type) throws Exception {
		StringBuffer sb = new StringBuffer();
		rolelist = rolelist.replaceAll("'", "");
		String[] roles = rolelist.split(",");
		if(roles.length>0 && applicationId ==null){
			try {
			
				RoleProcess process = (RoleProcess) ProcessFactory.createProcess(RoleProcess.class);
				applicationId = process.doView(roles[0]).getApplicationid();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (folderPath != null && !folderPath.equals("")) {
			File f = new File(folderPath);
			PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory
					.createProcess(PermissionProcess.class);
			if (f != null) {
				sb.append("<files>");
				File[] list = f.listFiles();
				if (list !=null && f.listFiles().length > 0) {
					for (int i = 0; i < list.length; i++) {
						if (list[i].isFile()) {
							if (list[i].getName().lastIndexOf(".") != -1) {
								if(type.equals("image")){
									String fileName = list[i].getName().toLowerCase();
									if(fileName.endsWith(".jpg") || fileName.endsWith(".gif") || fileName.endsWith(".png") || fileName.endsWith(".jpeg")){
										sb.append("<file>");
										sb.append("<review>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_REVIEW, ResVO.FOLDER_TYPE,applicationId)+"</review>");
										sb.append("<edit>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_EDIT, ResVO.FOLDER_TYPE,applicationId)+"</edit>");
										sb.append("<deletefile>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_DELETE, ResVO.FOLDER_TYPE,applicationId)+"</deletefile>");
										sb.append("<down>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_DOWN, ResVO.FOLDER_TYPE,applicationId)+"</down>");
										sb.append("<remove>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_REMOVE, ResVO.FOLDER_TYPE,applicationId)+"</remove>");
										sb.append("<copy>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_COPY, ResVO.FOLDER_TYPE,applicationId)+"</copy>");
										sb.append("<addSelectFile>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_ADD_SELETE_FILE, ResVO.FOLDER_TYPE,applicationId)+"</addSelectFile>");
										sb.append("<name>" + list[i].getName() + "</name>");
										sb.append("<size>");
										if (list[i].length() < 1024) {
											sb.append(list[i].length() + " B");
										} else if (list[i].length() < (1024 * 1024) && list[i].length() >= 1024) {
											sb.append((double) (list[i].length() / 1024) + " KB");
										} else {
											sb.append((double) (list[i].length() / (1024 * 1024)) + " M");
										}
										sb.append("</size>");
										sb.append("<type>"
												+ list[i].getName().substring(list[i].getName().lastIndexOf("."))
												+ "</type>");
										sb.append("<realPath>" + list[i].toString() + "</realPath>");
										sb.append("<path>"
												+ list[i].toString().substring(list[i].toString().lastIndexOf("uploads")).replace("\\", "/")+ "</path>");
										sb.append("</file>");
									}
								}else{
									sb.append("<file>");
									sb.append("<review>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_REVIEW, ResVO.FOLDER_TYPE,applicationId)+"</review>");
									sb.append("<edit>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_EDIT, ResVO.FOLDER_TYPE,applicationId)+"</edit>");
									sb.append("<deletefile>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_DELETE, ResVO.FOLDER_TYPE,applicationId)+"</deletefile>");
									sb.append("<down>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_DOWN, ResVO.FOLDER_TYPE,applicationId)+"</down>");
									sb.append("<remove>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_REMOVE, ResVO.FOLDER_TYPE,applicationId)+"</remove>");
									sb.append("<copy>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_COPY, ResVO.FOLDER_TYPE,applicationId)+"</copy>");
									sb.append("<addSelectFile>"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_ADD_SELETE_FILE, ResVO.FOLDER_TYPE,applicationId)+"</addSelectFile>");
									sb.append("<name>" + list[i].getName() + "</name>");
									sb.append("<size>");
									if (list[i].length() < 1024) {
										sb.append(list[i].length() + " B");
									} else if (list[i].length() < (1024 * 1024) && list[i].length() >= 1024) {
										sb.append((double) (list[i].length() / 1024) + " KB");
									} else {
										sb.append((double) (list[i].length() / (1024 * 1024)) + " M");
									}
									sb.append("</size>");
									sb.append("<type>"
											+ list[i].getName().substring(list[i].getName().lastIndexOf("."))
											+ "</type>");
									sb.append("<realPath>" + list[i].toString() + "</realPath>");
									sb.append("<path>"
											+ list[i].toString().substring(list[i].toString().lastIndexOf("uploads")).replace("\\", "/")+ "</path>");
									sb.append("</file>");
								}
							}
						}
					}
					sb.append("</files>");
					return sb.toString();
				} else {
					return "";
				}
			} else {
				return "";
			}
		} else {
			return "";
		}

	}


	/**
	 * 新建目录
	 * 
	 * @param folderPath
	 *            String 如 c:/fqf
	 * @return boolean
	 */
	public String newFolder(String folderPath) {
			try {
				File myFilePath = new File(folderPath);
				if (!myFilePath.exists()) {

					if (!myFilePath.mkdir()) {
						Log.warn("Failed to create folder at " + folderPath + "");
						throw new IOException("Failed to create folder at " + folderPath + "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return "";
	}
	
	/**
	 * 新建目录
	 * 
	 * @param folderPath
	 *            String 如 c:/fqf
	 * @param parentPath
	 * 			  String 如 C:/fqf
	 * @return String
	 */
	public String newFolder(String folderPath,String parentPath) {
		try {
//			folderPath = folderPath.replaceAll("\\\\", "/");
			File myFilePath = new File(folderPath);
			if (!myFilePath.exists()) {
				if (!myFilePath.mkdir()) { 
					Log.warn("Failed to create folder at " + folderPath + "");
					throw new IOException("Failed to create folder at " + folderPath + "");
				}else{
					File parentFile = new File(parentPath);
					if(parentFile.exists()){
						try {
							PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory
									.createProcess(PermissionProcess.class);
						
							String filename = myFilePath.getName();
							String resId = Sequence.getFileUUID(myFilePath, "uploads");
							ParamsTable params = new ParamsTable();
							params.setParameter("s_res_id", Sequence.getFileUUID(parentFile, "uploads"));
							params.setParameter("s_resName", parentFile.getName());
							DataPackage<PermissionVO> data = permissionProcess.doQuery(params);
							if(data != null && data.rowCount > 0){
								for(Iterator<PermissionVO> it = data.datas.iterator();it.hasNext();){
									PermissionVO pvo = it.next();
									PermissionVO newPvo = new PermissionVO();																				newPvo.setRoleId(pvo.getRoleId());
									newPvo.setApplicationid(pvo.getApplicationid());
									newPvo.setResId(resId);
									newPvo.setOperationId(pvo.getOperationId());
									newPvo.setType(pvo.getType());
									newPvo.setResName(filename);
									newPvo.setResType(pvo.getResType());
									newPvo.setOperationCode(pvo.getOperationCode());
									
									permissionProcess.doCreateOrUpdate(newPvo);
								}
							}
						}catch(Exception e2){
							e2.printStackTrace();
							throw e2;
						}finally{
							try {
								PersistenceUtils.closeSessionAndConnection();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 删除多个文件
	 * 
	 * @param selectFilePath
	 * @param envRealPath
	 * @return
	 */
	public String delMoreFile(String[] realPahts) {
		try {
			for (int i = 0; i < realPahts.length; i++) {
				File myDelFile = new File(realPahts[i]);
				if (myDelFile.exists()) {
					if (!myDelFile.delete()) {
						Log.warn("Failed to delete file at " + realPahts[i] + "");
						throw new IOException("Failed to create folder at "
								+ realPahts[i] + "");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public String delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myDelFile = new File(filePath);

			if (!myDelFile.delete()) {
				Log.warn("Failed to delete file at " + filePath + "");
				throw new IOException("Failed to create folder at " + filePath + "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public String delFolder(String folderPath) {
		try {
//			folderPath = folderPath.replaceAll("\\\\", "/");
			delAllFile(folderPath); // 删除完里面所有内容
			File myFilePath = new File(folderPath);
			// 删除空文件夹
			if (!myFilePath.delete()) {
				Log.warn("Failed to delete file at " + folderPath + "");
				throw new IOException("Failed to create folder at " + folderPath + "");
			} else {
				try {
					PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory
							.createProcess(PermissionProcess.class);
					
					String filename = myFilePath.getName();
					String resId = Sequence.getFileUUID(myFilePath, "uploads");
					ParamsTable params = new ParamsTable();
					params.setParameter("s_res_id", resId);
					params.setParameter("s_resName", filename);
					DataPackage<PermissionVO> data = permissionProcess.doQuery(params);
					if(data != null && data.rowCount > 0){
						for(Iterator<PermissionVO> it = data.datas.iterator();it.hasNext();){
							PermissionVO pvo = it.next();
							permissionProcess.doRemove(pvo);
						}
					}
				}catch(Exception e2){
					e2.printStackTrace();
					throw e2;
				}finally{
					try {
						PersistenceUtils.closeSessionAndConnection();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 * @throws IOException
	 */
	public void delAllFile(String path) throws IOException {
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
//			if (path.endsWith(File.separator)) {
			if (path.endsWith(File.separator)) {	
				temp = new File(path + tempList[i]);
			} else {
//				temp = new File(path + File.separator + tempList[i]);
				temp = new File(path + "/" + tempList[i]);
			}
			if (temp.isFile()) {
				if (!temp.delete()) {
					Log.warn("Failed to delete file at " + path + "/" + tempList[i] + "");
					throw new IOException("Failed to create folder at " + path + "/" + tempList[i] + "");
				}
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 从旧的文件夹中选择的文件复制新指定的文件夹中
	 * 
	 * @param selectFilePath
	 * @param envRealPath
	 * @return
	 */
	public String copyMoreFile(String[] selectPaths, String newPath) {
		
		try {
			for (int i = 0; i < selectPaths.length; i++) {
				copyFile(selectPaths[i], newPath + selectPaths[i].substring(selectPaths[i].lastIndexOf("\\")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 * @throws Exception
	 */
	public void copyFolder(String oldPath, String newPath) throws Exception {

		try {
			// 如果文件夹不存在 则建立新文件夹
			if (!(new File(newPath).mkdirs())) {
				Log.warn("Failed to create folder at " + newPath + "");
				throw new IOException("Failed to create folder at " + newPath + "");
			}
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
//				if (oldPath.endsWith(File.separator)) {
				if (oldPath.endsWith("/")) {
					temp = new File(oldPath + file[i]);
				} else {
//					temp = new File(oldPath + File.separator + file[i]);
					temp = new File(oldPath + "/" + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}

	}

	/**
	 * 从旧的文件夹中选择的文件移动新指定的文件夹中
	 * 
	 * @param oldPath
	 * @param newPathList
	 * @param envRealPath
	 * @return
	 */
	public String moveMoreFile(String[] selectPaths, String newPath) {
		try {
			for (int i = 0; i < selectPaths.length; i++) {
					moveFile(selectPaths[i], newPath+ selectPaths[i].substring(selectPaths[i].lastIndexOf("\\")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 */
	public void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		delFile(oldPath);

	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 * @throws Exception
	 */
	public void moveFolder(String oldPath, String newPath) throws Exception {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);

	}

	/**
	 * 重命名文件夹或文件名
	 * 
	 * @param str
	 * @throws Exception
	 */
	public String reNameFolderOrFile(String oldNamePath, String newNamePath) {
		if (oldNamePath.indexOf("uploads") != oldNamePath.lastIndexOf("uploads")
				|| newNamePath.indexOf("uploads") != newNamePath.lastIndexOf("uploads")) {
			return "重命名文件失败";
		} else {
			try {
//				oldNamePath = oldNamePath.replaceAll("\\\\", "/");
//				newNamePath = newNamePath.replaceAll("\\\\", "/");
				File file = new File(oldNamePath);
				File newFile = new File(newNamePath);
				file.renameTo(new File(newNamePath));
				PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory
						.createProcess(PermissionProcess.class);
				
				String resId = Sequence.getFileUUID(newFile, "uploads");
				String resName = newFile.getName();
				ParamsTable params = new ParamsTable();
				params.setParameter("s_res_id", Sequence.getFileUUID(file, "uploads"));
				params.setParameter("s_resName", file.getName());
				DataPackage<PermissionVO> data = permissionProcess.doQuery(params);
				if(data != null && data.rowCount > 0){
					for(Iterator<PermissionVO> it = data.datas.iterator();it.hasNext();){
						PermissionVO pvo = it.next();
						pvo.setResId(resId);
						pvo.setResName(resName);
						permissionProcess.doCreateOrUpdate(pvo);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return "重命名文件失败";
			}finally{
				try {
					PersistenceUtils.closeSessionAndConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "重命名文件夹或文件成功";
	}
	
	/**
	 * 创建权限
	 * @param roles
	 * @param f
	 * @param permissionProcess
	 * @return
	 * @throws Exception
	 */
	private boolean isCreate(String[] roles,File f,PermissionProcess permissionProcess,boolean flag) throws Exception{
		return permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FOLDER_CREATE, ResVO.FOLDER_TYPE,applicationId,flag);
	}
	
	/**
	 * 删除权限
	 * @param roles
	 * @param f
	 * @param permissionProcess
	 * @return
	 * @throws Exception
	 */
	private boolean isDelete(String[] roles,File f,PermissionProcess permissionProcess,boolean flag) throws Exception{
		return permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FOLDER_DELETE, ResVO.FOLDER_TYPE,applicationId,flag);
	}
	
	/**
	 * 重命名权限
	 * @param roles
	 * @param f
	 * @param permissionProcess
	 * @return
	 * @throws Exception
	 */
	private boolean isRename(String[] roles,File f,PermissionProcess permissionProcess,boolean flag) throws Exception{
		return permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FOLDER_RENAME, ResVO.FOLDER_TYPE,applicationId,flag);
	}
	
	/**
	 * 上传权限
	 * @param roles
	 * @param f
	 * @param permissionProcess
	 * @return
	 * @throws Exception
	 */
	private boolean isUpload(String[] roles,File f,PermissionProcess permissionProcess,boolean flag) throws Exception{
		return permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_UPLOAD, ResVO.FOLDER_TYPE,applicationId,flag);
	}
	
	//////////----------------------以下方法为手机端调用-------------------//////////////////////
	/**
	 * 手机文件管理控件调用
	 * @return
	 */
	public String toMbXMLText(String rootPath,String filePath, String rolelist,String applicationId) throws Exception{
		StringBuffer xmlText = new StringBuffer();
		xmlText.append("<"+MobileConstant.TAG_FILES);
		rolelist = rolelist.replaceAll("'", "");
		String[] roles = rolelist.split(",");
		
		PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
		File f = new File(filePath);
		String src = "";
		if(f!=null){
			xmlText.append(" ").append(MobileConstant.ATT_PATH).append("='").append(f.getPath()).append("'");
			src = f.getPath().substring(f.getPath().lastIndexOf("uploads")-1);
			src = src.replace("\\", "/");
			xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(src).append("'");
			
			File[] files = f.listFiles();
			
			if (!filePath.equals(rootPath)) {
				xmlText.append(" ").append(MobileConstant.ATT_CREATE).append("='").append(isCreate(roles,f,permissionProcess,true)).append("'");
				xmlText.append(" ").append(MobileConstant.ATT_DELETE).append("='").append(isDelete(roles,f,permissionProcess,true)).append("'");
				xmlText.append(" ").append(MobileConstant.ATT_RENAME).append("='").append(isRename(roles,f,permissionProcess,true)).append("'");
				xmlText.append(" ").append(MobileConstant.ATT_UPLOAD).append("='").append(isUpload(roles,f,permissionProcess,true)).append("'");
				xmlText.append(">");
				/* 第一笔设定为[回到根目录] */
				xmlText.append("<").append(MobileConstant.TAG_FOLDER);
				xmlText.append(" ").append(MobileConstant.ATT_NAME+ "='根目录'");
				xmlText.append(" ").append(MobileConstant.ATT_PATH).append("='").append(rootPath).append("'");
				xmlText.append(" ").append(MobileConstant.ATT_CREATE).append("='").append(isCreate(roles,f,permissionProcess,true)).append("'");
				xmlText.append(" ").append(MobileConstant.ATT_DELETE).append("='").append("false").append("'");
				xmlText.append(" ").append(MobileConstant.ATT_RENAME).append("='").append("false").append("'");
				xmlText.append(" ").append(MobileConstant.ATT_UPLOAD).append("='").append(isUpload(roles,f,permissionProcess,true)).append("'");
				//xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(f.toString().substring(f.toString().lastIndexOf(f.getName()))).append("'");
				xmlText.append("></").append(MobileConstant.TAG_FOLDER + ">");
				/* 第二笔设定为[回上层] */
				xmlText.append("<").append(MobileConstant.TAG_FOLDER);
				xmlText.append(" ").append(MobileConstant.ATT_NAME+ "='返回上级目录'");
				xmlText.append(" ").append(MobileConstant.ATT_PATH).append("='").append(f.getParent()).append("'");
				xmlText.append(" ").append(MobileConstant.ATT_CREATE).append("='").append(isCreate(roles,f.getParentFile(),permissionProcess,isCreate(roles,f,permissionProcess,true))).append("'");
				xmlText.append(" ").append(MobileConstant.ATT_DELETE).append("='").append(isDelete(roles,f.getParentFile(),permissionProcess,isDelete(roles,f,permissionProcess,true))).append("'");
				xmlText.append(" ").append(MobileConstant.ATT_RENAME).append("='").append(isRename(roles,f.getParentFile(),permissionProcess,isRename(roles,f,permissionProcess,true))).append("'");
				xmlText.append(" ").append(MobileConstant.ATT_UPLOAD).append("='").append(isUpload(roles,f.getParentFile(),permissionProcess,isUpload(roles,f,permissionProcess,true))).append("'");
				//xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(f.getParent().toString().substring(f.getParent().toString().lastIndexOf(f.getParentFile().getName()))).append("'");
				xmlText.append("></").append(MobileConstant.TAG_FOLDER + ">");
			}else{
				xmlText.append(" ").append(MobileConstant.ATT_CREATE).append("='").append(isCreate(roles,f,permissionProcess,true)).append("'");
				xmlText.append(" ").append(MobileConstant.ATT_DELETE).append("='").append("false").append("'");
				xmlText.append(" ").append(MobileConstant.ATT_RENAME).append("='").append("false").append("'");
				xmlText.append(" ").append(MobileConstant.ATT_UPLOAD).append("='").append(isUpload(roles,f,permissionProcess,true)).append("'");
				xmlText.append(">");
			}
			if(files!=null){
				/* 将所有文件加入ArrayList中 */
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if(file.isDirectory()){
						xmlText.append("<").append(MobileConstant.TAG_FOLDER);
						xmlText.append(" ").append(MobileConstant.ATT_NAME+ "='"+file.getName()+"'");
						xmlText.append(" ").append(MobileConstant.ATT_PATH).append("='").append(file.toString()).append("'");
						xmlText.append(" ").append(MobileConstant.ATT_CREATE).append("='").append(isCreate(roles,file,permissionProcess,isCreate(roles,f,permissionProcess,true))).append("'");
						xmlText.append(" ").append(MobileConstant.ATT_DELETE).append("='").append(isDelete(roles,file,permissionProcess,isDelete(roles,f,permissionProcess,true))).append("'");
						xmlText.append(" ").append(MobileConstant.ATT_RENAME).append("='").append(isRename(roles,file,permissionProcess,isRename(roles,f,permissionProcess,true))).append("'");
						xmlText.append(" ").append(MobileConstant.ATT_UPLOAD).append("='").append(isUpload(roles,file,permissionProcess,isUpload(roles,f,permissionProcess,true))).append("'");
						//xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(f.toString().substring(file.toString().lastIndexOf(f.getName()))).append("'");
						xmlText.append("></").append(MobileConstant.TAG_FOLDER + ">");
					}else{
						xmlText.append("<").append(MobileConstant.TAG_FILE);
						xmlText.append(" ").append(MobileConstant.ATT_NAME+ "='"+file.getName()+"'");
						xmlText.append(" ").append(MobileConstant.ATT_PATH).append("='").append(file.toString()).append("'");
						src = file.getPath().substring(file.getPath().lastIndexOf("uploads")-1);
						src = src.replace("\\", "/");
						xmlText.append(" ").append(MobileConstant.ATT_SRC).append("='").append(src).append("'");
						xmlText.append(" ").append(MobileConstant.ATT_SIZE+ "='"+file.length()+"'");
						xmlText.append(" ").append(MobileConstant.ATT_REVIEW+ "='"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_REVIEW, ResVO.FOLDER_TYPE,applicationId,true)+"'");
						xmlText.append(" ").append(MobileConstant.ATT_EDIT+ "='"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_EDIT, ResVO.FOLDER_TYPE,applicationId,true)+"'");
						xmlText.append(" ").append(MobileConstant.ATT_DELETE+ "='"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_DELETE, ResVO.FOLDER_TYPE,applicationId,true)+"'");
						xmlText.append(" ").append(MobileConstant.ATT_DOWN+ "='"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_DOWN, ResVO.FOLDER_TYPE,applicationId,true)+"'");
						xmlText.append(" ").append(MobileConstant.ATT_REMOVE+ "='"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_REMOVE, ResVO.FOLDER_TYPE,applicationId,true)+"'");
						xmlText.append(" ").append(MobileConstant.ATT_COPY+ "='"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_COPY, ResVO.FOLDER_TYPE,applicationId,true)+"'");
						xmlText.append(" ").append(MobileConstant.ATT_ADDSELECTFILE+ "='"+permissionProcess.check(roles, Sequence.getFileUUID(f, "uploads"),null,OperationVO.FILE_ADD_SELETE_FILE, ResVO.FOLDER_TYPE,applicationId,true)+"'");
						xmlText.append("></").append(MobileConstant.TAG_FILE + ">");
					}
				}
			}
		}else{
			xmlText.append(">");
		}
		xmlText.append("</"+MobileConstant.TAG_FILES+">");
		return xmlText.toString();
	}
	
	
	/**
	 * 获得根目录下及根目录的所有目录的路径
	 */
	public String toMbFolderListXMLText(String folderPath) {
		StringBuffer xmlText = new StringBuffer();
		try {
			File f = new File(folderPath);
			if(f!=null){
				xmlText.append("<").append(MobileConstant.TAG_FOLDER);
				xmlText.append(" ").append(MobileConstant.ATT_NAME+ "='"+f.getPath().substring(f.getPath().lastIndexOf("uploads")-1)+"'");
				xmlText.append(" ").append(MobileConstant.ATT_PATH).append("='").append(f.getPath()).append("'");
				xmlText.append("></").append(MobileConstant.TAG_FOLDER + ">");
				File[] list = f.listFiles();
				if(list!=null){
					xmlText = toMbFolderXMLText(list, f, f.getName(), xmlText);// 调用递归方法
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlText.toString();
	}

	// 递归方法
	public StringBuffer toMbFolderXMLText(File[] list, File f, String fileName, StringBuffer sb) {
		// Element scend = null;
		for (int i = 0; i < list.length; i++) {
			if (list[i].isDirectory()) {
				String tempPath = fileName;
				tempPath += "\\" + list[i].getName();
				sb.append("<").append(MobileConstant.TAG_FOLDER);
				sb.append(" ").append(MobileConstant.ATT_NAME+ "='"+tempPath+"'");
				sb.append(" ").append(MobileConstant.ATT_PATH).append("='").append(list[i].getPath()).append("'");
				sb.append("></").append(MobileConstant.TAG_FOLDER + ">");
				if (list[i].listFiles().length > 0) {
					toMbFolderXMLText(list[i].listFiles(), f, tempPath, sb);
				}

			}
		}
		return sb;
	}
	
}
