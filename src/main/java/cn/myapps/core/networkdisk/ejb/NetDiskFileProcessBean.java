package cn.myapps.core.networkdisk.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;



import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.constans.Environment;
import cn.myapps.core.networkdisk.dao.NetDiskFileDAO;
import cn.myapps.util.ProcessFactory;

public class NetDiskFileProcessBean extends AbstractDesignTimeProcessBean<NetDiskFile>
		implements NetDiskFileProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8277239506770546970L;

	@Override
	protected IDesignTimeDAO<NetDiskFile> getDAO() throws Exception {
		return (NetDiskFileDAO)DAOFactory.getDefaultDAO(NetDiskFile.class.getName());
	}

	//获得总页数
	public int getTotalLines(String hql) throws Exception {
		return ((NetDiskFileDAO)getDAO()).getTotalLines(hql);
	}
	
	/**
	 * 增加文档
	 * @param fileName 文件名
	 * @param filePath 文件路径
	 * @param folderId 文件夹id
	 * @param userId 用户id
	 */
	public void createFile(String fileWebPath, String folderId ,String userId)
			throws Exception {
		try{
			String filePath = fileWebPath.replace("/", File.separator);
			File file = new File(Environment.getInstance().getApplicationRealPath() + File.separator + filePath);
			if(file.exists()){
				NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess)ProcessFactory.createProcess(NetDiskFolderProcess.class);
				NetDiskFolder netDiskFolder = (NetDiskFolder)netDiskFolderProcess.doView(folderId);
				if(netDiskFolder !=null){
					String folderPath = netDiskFolder.getFolderPath();
					String folderWebPath = null;
					if(folderPath.indexOf("\\") > -1){
						folderWebPath = folderPath.replace("\\", "/");
					}
							
					String fileName = null;
					String extName = null;
					if(filePath.lastIndexOf(File.separator)>-1){
						fileName = filePath.substring(filePath.lastIndexOf(File.separator) +1);
					}
					if (fileName != null || fileName.lastIndexOf(".") >= -1) {
						extName = fileName.substring(fileName.lastIndexOf("."));
					}
					
					String realFolderPath = Environment.getInstance().getApplicationRealPath() + File.separator + "networkdisk" +File.separator + folderPath ;
					NetDiskFile netDiskFile = new NetDiskFile();
					netDiskFile.setName(fileName);
					netDiskFile.setFolderPath(folderPath);
					netDiskFile.setFolderWebPath(folderWebPath);
					reName(netDiskFile,0,fileName,extName);		
					
					int bytesum = 0; 
					int byteread = 0; 
					InputStream inStream = new FileInputStream(file.getPath()); //读入原文件 
				    FileOutputStream fs = new FileOutputStream(realFolderPath + File.separator + netDiskFile.getName() ); 
				    byte[] buffer = new byte[1444]; 
				    while ( (byteread = inStream.read(buffer)) != -1) { 
				       bytesum += byteread; //字节数 文件大小 
				       System.out.println(bytesum); 
				       fs.write(buffer, 0, byteread); 
				    }
				    inStream.close(); 
				            
					long size = file.length();
					
					netDiskFile.setType(extName);
					netDiskFile.setSize(size);
					netDiskFile.setModifyTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					netDiskFile.setUserid(userId);
					netDiskFile.setFolderId(folderId);
					this.doCreate(netDiskFile);
				}else{
					throw new OBPMValidateException("找不到文件夹");
				}
			}else{
				throw new OBPMValidateException("找不到对应的文件");
			}
		}catch (Exception e) {
			throw e;
		}finally {
			try{
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	/**
	 * 查找文档
	 * @param fileName 文件名
	 * @param folderId 所属文件夹id
	 * @return netdiskFile 
	 */
	public NetDiskFile searchFile(String fileName,String folderId) throws Exception{
		try{
			ParamsTable params = new ParamsTable();
			params.setParameter("t_folderid", folderId);
			params.setParameter("t_name", fileName);
			DataPackage<NetDiskFile> datapackage = this.doQuery(params);
			if(datapackage.rowCount > 0){
				for(Iterator<NetDiskFile> it = datapackage.getDatas().iterator();it.hasNext();){
					NetDiskFile netdiskFile = (NetDiskFile)it.next();
					return netdiskFile;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			try{
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw e;
			}
		}
		return null;
	}
	
	/**
	 * 删除文档
	 * @param fileName 文件名
	 * @param folderId 所属文件夹id
	 */
	public void removeFile(String fileName,String folderId) throws Exception{
		try{
			ParamsTable params = new ParamsTable();
			params.setParameter("t_folderid", folderId);
			params.setParameter("t_name", fileName);
			DataPackage<NetDiskFile> datapackage = this.doQuery(params);
			if(datapackage.rowCount>0){
				for(Iterator<NetDiskFile> it = datapackage.getDatas().iterator();it.hasNext();){
					NetDiskFile netdiskFile = (NetDiskFile)it.next();
					File file = new File(Environment.getInstance().getApplicationRealPath() + File.separator + "networkdisk" + File.separator + netdiskFile.getFolderWebPath() + File.separator + netdiskFile.getName());
					if(file.exists()){
						if(file.delete()){
							this.doRemove(netdiskFile);
						}else{
							throw new IOException("delete file '" + file.getAbsolutePath() + "' failed!");
						}
					}else{
						this.doRemove(netdiskFile);
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			try{
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	//递归文件名称
		protected void reName(NetDiskFile netDiskFile,int i,String fileName,String extName){
			File file = new File(Environment.getInstance().getApplicationRealPath() + File.separator + "networkdisk" + File.separator + netDiskFile.getFolderPath()+File.separator+netDiskFile.getName());
			if(file.exists()){
				i++;
				netDiskFile.setName(fileName+i+extName);
				reName(netDiskFile,i,fileName,extName);
			}
		}
}
