package cn.myapps.core.networkdisk.ejb;

import java.io.File;
import java.io.IOException;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.constans.Environment;
import cn.myapps.core.networkdisk.dao.NetDiskFolderDAO;
import cn.myapps.util.ProcessFactory;

public class NetDiskFolderProcessBean extends AbstractDesignTimeProcessBean<NetDiskFolder>
		implements NetDiskFolderProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6946299735767950445L;

	@Override
	protected IDesignTimeDAO<NetDiskFolder> getDAO() throws Exception {
		return (NetDiskFolderDAO)DAOFactory.getDefaultDAO(NetDiskFolder.class.getName());
	}
	
	/**
	 * 创建文件夹
	 * @param folderName 文件夹名称
	 * @param parentId 父文件夹id
	 * @param userid 用户id
	 * @param orderno 序列号
	 */
	public void createFolder(String folderName,String parentId,String userid,int orderno) throws Exception {
		try {
			NetDiskFolder netDiskFolder = null;
			String folderPath = "";
			NetDiskFolderProcess netDiskFolderProcess = (NetDiskFolderProcess) ProcessFactory.createProcess(NetDiskFolderProcess.class);
			NetDiskFolder netdiskParentFolder = (NetDiskFolder) netDiskFolderProcess.doView(parentId);
			if(netdiskParentFolder != null){
				folderPath = netdiskParentFolder.getFolderPath() + File.separator + folderName;
			}else{
				folderPath = parentId + File.separator + folderName;
			}
			ParamsTable params = new ParamsTable();
			params.setParameter("t_parentId", parentId);
			params.setParameter("t_folderPath", folderPath);
			DataPackage<NetDiskFolder> datas = this.doQuery(params);
			if(datas.rowCount >0){
				throw new OBPMValidateException("文件夹 folderPath 已存在");
			}else{
				File folder = new File(Environment.getInstance().getApplicationRealPath() + File.separator + "networkdisk" + File.separator + folderPath);
				if (!folder.exists()) {
					if (!folder.mkdirs()) {
						throw new IOException("create folder '" + folder + "' failed!");
					}
				}
				netDiskFolder = new NetDiskFolder();
				netDiskFolder.setUserid(userid);
				netDiskFolder.setFolderPath(folderPath);
				netDiskFolder.setOrderno(orderno);
				netDiskFolder.setParentId(parentId);
				netDiskFolderProcess.doCreate(netDiskFolder);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}

}
