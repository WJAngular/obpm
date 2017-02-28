package cn.myapps.km.disk.ejb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.disk.dao.NDirDAO;
import cn.myapps.km.search.SearchEngine;
import cn.myapps.km.util.FileUtils;
import cn.myapps.km.util.Sequence;


public class NDirProcessBean extends AbstractBaseProcessBean<NDir> implements NDirProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8922299902395074235L;

	public NRuntimeDAO getDAO() throws Exception {
		return DaoManager.getNDirDAO(getConnection());
	}

	public void doCreate(NObject vo) throws Exception {
		NDir dir = (NDir) vo;
		if(dir.getId() == null){
			dir.setId(Sequence.getSequence());
		}
		try {
			beginTransaction();
			getDAO().create(dir);
			commitTransaction();
			//FileUtils.createFolder(filePath); //创建文件夹
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
		}
	}

	public void doUpdate(NObject vo) throws Exception {
		((NDirDAO)getDAO()).update(vo);
	}

	public NDir doView(String id) throws Exception {
		return (NDir) getDAO().find(id);
	}

	public void doRemove(String pk) throws Exception {
		NDirProcess process = new NDirProcessBean();
		NFileProcess nFileProcess = new NFileProcessBean();
		try {
			NDir dir = (NDir) process.doView(pk);
			if(dir.getType() == NDir.TYPE_SYSTEM){
				throw new Exception("系统文件夹,不能被删除!");
			}
			
			//删除子文件夹
			Collection<NDir> dirs = process.getUnderNDir(pk);
			for(Iterator<NDir> it = dirs.iterator(); it.hasNext();){
				NDir subDir = it.next();
				doRemove(subDir.getId()); 
			}
			beginTransaction();
			getDAO().remove(pk);
			nFileProcess.deleteFileByNDirid(pk); //删除文件记录
			commitTransaction();
			//FileUtils.delFolder(getRealPath() + dir.getPath()); //删除物理文件夹
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public void moveNDir(String parentid, String[] dirids) throws Exception{
		((NDirDAO)getDAO()).moveNDir(parentid, dirids);
	}

	
	public Collection<IFile> getUnderNDirAndNFile(String parentid) throws Exception {
		return ((NDirDAO)getDAO()).getUnderNDirAndNFile(parentid);
	}

	
	public Collection<NDir> getUnderNDir(String parentid) throws Exception {
		return ((NDirDAO)getDAO()).getUnderNDir(parentid);
	}
	
	public Collection<NDir> getUnderNDirByDisk(String ndiskid, String parentid) throws Exception{
		return ((NDirDAO)getDAO()).getUnderNDirByDisk(ndiskid, parentid);
	}
	
	public long countUnderNDir(String ndiskid, String parentid) throws Exception{
		return ((NDirDAO)getDAO()).countUnderNDir(ndiskid, parentid);
	}
	
	public DataPackage<IFile> getUnderNDirAndNFile(int page, int lines, String parentid,String orderbyFile,String orderbyMode) throws Exception{
		return ((NDirDAO)getDAO()).getUnderNDirAndNFile(page, lines, parentid,orderbyFile,orderbyMode);
	}
	
	
	public Collection<NDir> getNDirByNDisk(String ndiskid) throws Exception{
		return ((NDirDAO)getDAO()).getNDirByNDisk(ndiskid);
	}
	
	
	public NDir getPublicShareNDir(String ndiskid) throws Exception{
		return ((NDirDAO)getDAO()).getPublicShareNDir(ndiskid);
	}
	
	public NDir getPersonalShareNDir(String ownerid) throws Exception{
		return ((NDirDAO)getDAO()).getPersonalShareNDir(ownerid);
	}
	
	public NDir getPersonalFavoritesNDir(String ownerid) throws Exception {
		return ((NDirDAO)getDAO()).findPersonalFavoritesNDir(ownerid);
	}
	
	public NDir getPersonalRecommendNDir(String ownerid) throws Exception {
		return ((NDirDAO)getDAO()).findPersonalRecommendNDir(ownerid);
	}

	public void doRemoveNdirByRemoveUser(String userid) throws Exception{
		((NDirDAO)getDAO()).doRemoveNdirByRemoveUser(userid);
	}
	
	public void doRemoveNdirAndNFile(String[] dirSelects, String[] fileSelects,
			String diskId, String indexRealPath, int type) throws Exception {
		NFileProcess nFileProcess = new NFileProcessBean();

		try {
			if (dirSelects != null) {
				for (int i = 0; i < dirSelects.length; i++) {
					doRemove(dirSelects[i]);
				}
			}
			if (fileSelects != null) {
				for (int j = 0; j < fileSelects.length; j++) {
					try {
						beginTransaction();
						nFileProcess.doRemove(fileSelects[j]);
						// 删除索引
						if (NDisk.TYPE_PERSONAL == type) {
							SearchEngine.deletePrivateDiskIndex(fileSelects[j],
									indexRealPath, diskId);
						} else if (NDisk.TYPE_PUBLIC == type) {
							SearchEngine.deletePublicDiskIndex(fileSelects[j],
									indexRealPath, diskId);
						}
						commitTransaction();
					} catch (Exception e) {
						rollbackTransaction();
						e.printStackTrace();
						throw e;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public Collection<NDir> getSuperiorNDirList(String nDirid)throws Exception {
		Stack<NDir> result = new Stack<NDir>();
		NDir dir = doView(nDirid);
		result.push(dir);
		while(dir.getParentId() != null){
			dir = doView(dir.getParentId());
			result.push(dir);
		}
		return result;
	}
}
