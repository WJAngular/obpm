package cn.myapps.km.disk.ejb;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;


import cn.myapps.core.logger.ejb.LogProcess;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.disk.dao.NDiskDAO;
import cn.myapps.km.log.ejb.Logs;
import cn.myapps.km.log.ejb.LogsProcess;
import cn.myapps.km.log.ejb.LogsProcessBean;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.FileUtils;
import cn.myapps.km.util.Sequence;
import cn.myapps.util.ProcessFactory;

public class NDiskProcessBean extends AbstractBaseProcessBean<NDisk> implements
		NDiskProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1400927371113610228L;

	public NRuntimeDAO getDAO() throws Exception {
		return DaoManager.getNDiskDAO(getConnection());
	}

	public void doCreate(NObject vo) throws Exception {
		NDisk disk = (NDisk) vo;

		// 级联创建网盘的根目录NDir
		try {
			beginTransaction();
			NDir dir = new NDir();
			String dirId = Sequence.getSequence();
			dir.setId(dirId);
			dir.setName(disk.getName());
			dir.setOwnerId(disk.getOwnerId());
			dir.setParentId(null);
			dir.setnDiskId(disk.getId());
			dir.setPath(File.separator+disk.getId());
			dir.setCreateDate(new Date());
			dir.setDomainId(disk.getDomainId());

			NDirProcess process = new NDirProcessBean();
			process.doCreate(dir);

			disk.setnDirId(dirId);
			getDAO().create(disk);
			FileUtils.createFolder(getRealPath() + dir.getPath()); // 创建一个文件夹
			// 在根目录下创建系统默认文件夹,归档网盘则不创建
			if(NDisk.TYPE_ARCHIVE != disk.getType()){
				createSystemNDir(process, disk, dir);
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
		}
	}
	

	private void createSystemNDir(NDirProcess process, NDisk disk, NDir dir)
			throws Exception {
		LogsProcess logProcess = new LogsProcessBean();
		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		UserVO baseuser = (UserVO) userProcess.doView(disk.getOwnerId());
		NUser user = null;
		if(baseuser !=null){
			user = new NUser(baseuser);
		}
		
		String[] dirnames = { NDir.MY_DOCUMENT, NDir.MY_COLLECTION,
				NDir.USER_SHARE, NDir.RECOMMEND_DOCUMENT };
		try {
			beginTransaction();
			for (int i = 0; i < dirnames.length; i++) {
				if (NDisk.TYPE_PUBLIC == disk.getType()
						&& dirnames[i] != NDir.USER_SHARE) {
					continue;
				}
				NDir ndir = new NDir();
				ndir.setId(Sequence.getSequence());
				ndir.setName(dirnames[i]);
				ndir.setOwnerId(dir.getOwnerId());
				ndir.setParentId(dir.getId());
				ndir.setnDiskId(dir.getnDiskId());
				ndir.setType(NDir.TYPE_SYSTEM);
				ndir.setPath(File.separator+disk.getId());
				ndir.setCreateDate(new Date());
				ndir.setDomainId(disk.getDomainId());
				process.doCreate(ndir);
				if(user !=null){
					logProcess.doLog(Logs.OPERATION_TYPE_CREATE, Logs.OPERATION_DIRECTORY, ndir, null, null, null, null, null, new ParamsTable(), user);
				}
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
		}
	}

	/**
	 * 根据用户查找网盘
	 */

	public NDisk getNDiskByUser(String userid) throws Exception {
		return ((NDiskDAO) getDAO()).getNDiskByUser(userid);
	}

	public void doUpdate(NObject vo) throws Exception {
		// TODO Auto-generated method stub

	}

	public void doRemove(String pk) throws Exception {
		NDisk disk = null;
		try {
			beginTransaction();
			disk = (NDisk) doView(pk);
			if (disk == null)
				return;
//			NDirProcess process = new NDirProcessBean();
//			process.doRemove(disk.getnDirId());

			getDAO().remove(pk);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	public NObject doView(String id) throws Exception {

		return getDAO().find(id);
	}

	public NDisk getPublicDisk(String domainid) throws Exception {
		return ((NDiskDAO) getDAO()).getPublicDisk(domainid);
	}

	public void createByUser(String userId, String domainId) throws Exception {
		NDisk disk = null;
		try {
			disk = this.getNDiskByUser(userId);
			if (disk != null)
				return;

			disk = new NDisk();
			disk.setId(Sequence.getSequence());
			disk.setName(NDisk.NAME_PERSONAL);
			disk.setType(NDisk.TYPE_PERSONAL);
			disk.setOwnerId(userId);
			disk.setDomainId(domainId);

			doCreate(disk);
		} catch (Exception e) {
			throw e;
		}

	}

	public void removeByUser(String userId) throws Exception {
		try {
			NDisk disk = getNDiskByUser(userId);
			if (disk != null) {
				beginTransaction();
				
				//删除用户文件
				NFileProcess nfileProcess = new NFileProcessBean();
				nfileProcess.doRemoveNFileByRemoveUser(userId);
				
				//删除用户文件夹
				NDirProcess ndirProcess = new NDirProcessBean();
				ndirProcess.doRemoveNdirByRemoveUser(userId);
				
				//删除物理文件
				FileUtils.delFolder(getRealPath() + "\\" + disk.getId());
				
				doRemove(disk.getId());
				commitTransaction();
			}
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}

	}

	/**
	 * 获取热门分享
	 */
	public DataPackage<IFile> doListHotest(int page, int lines, String domainid, String categoryID)
			throws Exception {
		return ((NDiskDAO) getDAO()).getHotestFiles(page, lines, domainid, categoryID);
	}

	/**
	 * 获取最新上传
	 */
	public DataPackage<IFile> doListNewestFile(int page, int lines, String domainid, String categoryID)
			throws Exception {
		return ((NDiskDAO) getDAO()).doListNewestFile(page, lines, domainid, categoryID);
	}

	public NDisk getArchiveDisk(String domainid) throws Exception {
		return ((NDiskDAO) getDAO()).getArchiveDisk(domainid);
	}
}
