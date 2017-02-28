package cn.myapps.km.disk.ejb;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import cn.myapps.constans.Environment;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.disk.dao.NFileDAO;
import cn.myapps.km.log.ejb.Logs;
import cn.myapps.km.log.ejb.LogsProcess;
import cn.myapps.km.log.ejb.LogsProcessBean;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.search.IndexBuilder;
import cn.myapps.km.search.IndexBundle;
import cn.myapps.km.util.FileUtils;
import cn.myapps.km.util.Sequence;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.converter.ConverterBundle;
import cn.myapps.util.converter.ConverterRunner;

public class NFileProcessBean extends AbstractBaseProcessBean<NFile> implements
		NFileProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1400927371113610228L;

	public NRuntimeDAO getDAO() throws Exception {
		return DaoManager.getNFileDAO(getConnection());
	}

	public void doCreate(NObject vo) throws Exception {
		NFile nfile = (NFile) vo;

		try {
			// 1.设置id
			if (nfile.getId() == null) {
				nfile.setId(Sequence.getSequence());
			}

			// 2.有文件名获取文件类型
			String name = nfile.getName();
			if (StringUtil.isBlank(nfile.getType())
					&& (!StringUtil.isBlank(name))
					&& name.lastIndexOf(".") > -1) {
				nfile.setType(name.substring(name.lastIndexOf(".") + 1, name
						.length()));
			}
			beginTransaction();
			getDAO().create(nfile);
			commitTransaction();

		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}


	public void doCreate(NObject vo, String userid, String ndirid,
			String savePath) throws Exception {
		NFile nfile = (NFile) vo;

		// 1.拼装目录路径,检查目录路径对应文件夹是否存在
		String dirUrl = savePath + "/" + userid;

		// 2.拼装文件存放路径
		String fileUrl = dirUrl + "/" + nfile.getName();
		nfile.setUrl(fileUrl);

		if (nfile.getOrigin() == NFile.ORIGN_UPLOAD) {
			if (fileUrl != "" && fileUrl != null) {
				File file = new File(nfile.getUrl());
				// 3.判断文件是否存在 存在则计算大小
				if (!file.exists()) {
					throw new IOException("The file does not exist ("
							+ nfile.getUrl() + ")");
				} else {
					long size = file.length();
					nfile.setSize(size);
				}
			}
		}

		// 4.设置目录id 创建者id 拥有者id
		nfile.setNDirId(ndirid);
		nfile.setCreatorId(userid);
		nfile.setOwnerId(userid);
		this.doCreate(nfile);
	}

	/**
	 * 转存
	 * 
	 * @param oldfileid
	 *            源文件id
	 * @param userid
	 *            用户id
	 * @param ndirid
	 *            目录id
	 * @param savePath
	 *            根目录路径
	 * @throws Exception
	 */
	public void doCopyFile(String oldfileid, String userid, String ndirid,
			String savePath) throws Exception {
		NFile newfile = new NFile();

		// 1.根据oldfileid查询数据
		NFile oldfile = ((NFileDAO) getDAO()).find(oldfileid);

		// 2.将oldfile中数据复制到newfile中
		newfile.setName(oldfile.getName());
		newfile.setSize(oldfile.getSize());

		// 3.拼装目录路径,检查目录路径对应文件夹是否存在
		String dirUrl = savePath + "/" + userid;

		// 4.拼装文件存放路径,并将oldfile中文件复制到newfile中
		String fileUrl = dirUrl + "/" + newfile.getName();
		newfile.setUrl(fileUrl);
		if (newfile.getUrl() != null && newfile.getUrl() != "") {
			FileUtils.copyFile(newfile.getUrl(), oldfile.getUrl());
		}

		// 5.设置目录id 创建者id 拥有者id
		newfile.setNDirId(ndirid);
		newfile.setCreatorId(userid);
		newfile.setOwnerId(userid);
		doCreate(newfile);
	}

	public void doShareFile4Personal(String pk, NUser user,ParamsTable params, String sharedUserId)
			throws Exception {
		NDirProcess nDirProcess = new NDirProcessBean();
		NDiskProcess nDiskProcess = new NDiskProcessBean();
		try {
			NDir ndir = nDirProcess.getPersonalShareNDir(sharedUserId);
			NDisk disk = nDiskProcess.getNDiskByUser(sharedUserId);
			NFile newfile = new NFile();
			newfile.setState(IFile.STATE_PRIVATE);
			doShareFile(newfile, pk, user,params, ndir.getId(), disk.getId(),
					sharedUserId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doShareFile4Public(String pk, NUser user,ParamsTable params) throws Exception {
		NDirProcess nDirProcess = new NDirProcessBean();
		NDiskProcess nDiskProcess = new NDiskProcessBean();

		try {
			NDisk disk = nDiskProcess.getPublicDisk(user.getDomainid());
			NDir ndir = nDirProcess.getPublicShareNDir(disk.getId());
			NFile newfile = new NFile();
			newfile.setState(IFile.STATE_PUBLIC);
			doShareFile(newfile, pk, user,params, ndir.getId(), disk.getId(), user
					.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 分享
	 * 
	 * @param oldfileid
	 *            原文件id
	 * @throws Exception
	 */
	public void doShareFile(NFile newfile, String oldfileid, NUser user,ParamsTable params,
			String ndirid, String ndiskid, String sharedUserId)
			throws Exception {
		// 1.根据oldfileid查询数据
		NFile oldfile = ((NFileDAO) getDAO()).find(oldfileid);

		// 2.将oldfile中数据复制到newfile中
		newfile.setId(Sequence.getSequence());
		newfile.setName(oldfile.getName());
		newfile.setSize(oldfile.getSize());
		newfile.setUrl(oldfile.getUrl());
		newfile.setCreatorId(oldfile.getCreatorId());
		newfile.setSize(oldfile.getSize());
		newfile.setShareId(StringUtil.isBlank(oldfile.getShareId()) ? oldfileid
				: oldfile.getShareId());
		newfile.setNDirId(ndirid);
		newfile.setTitle(oldfile.getTitle());
		newfile.setMemo(oldfile.getMemo());
		newfile.setRootCategoryId(oldfile.getRootCategoryId());
		newfile.setSubCategoryId(oldfile.getSubCategoryId());
		newfile.setDepartment(oldfile.getDepartment());
		newfile.setDepartmentId(oldfile.getDepartmentId());
		newfile.setCreateDate(oldfile.getCreateDate());


		// 3.domainid,creator
		newfile.setDomainId(oldfile.getDomainId());
		newfile.setCreator(oldfile.getCreator());
		newfile.setClassification(oldfile.getClassification());

		// 5.所有者userid
		newfile.setOwnerId(sharedUserId);
		newfile.setOrigin(NFile.ORIGN_SHARE);

		String shareid = StringUtil.isBlank(oldfile.getShareId()) ? oldfileid
				: oldfile.getShareId();
		if (isSharedInThisNdir(ndirid, shareid)) {
			throw new Exception("该文件已被分享过!");
		}

		this.doCreate(newfile);


		// 建立索引
		IndexBuilder builder = IndexBuilder.getInstance();
		builder.setRealPath(getRealPath());
		builder.setNdiskid(ndiskid);
		builder.addNFile(newfile);
		builder.buildIndex();
	}

	public void doFavorite(String fileId, NUser user, ParamsTable params)
			throws Exception {
		NDirProcess nDirProcess = new NDirProcessBean();
		NDiskProcess nDiskProcess = new NDiskProcessBean();
		LogsProcess logsProcess = new LogsProcessBean();
		try {
			NDir ndir = nDirProcess.getPersonalFavoritesNDir(user.getId());
			NDisk disk = nDiskProcess.getNDiskByUser(user.getId());

			NFile oldfile = ((NFileDAO) getDAO()).find(fileId);
			NFile newfile = new NFile();

			// 2.将oldfile中数据复制到newfile中
			newfile.setId(Sequence.getSequence());
			newfile.setName(oldfile.getName());
			newfile.setSize(oldfile.getSize());
			newfile.setUrl(oldfile.getUrl());
			newfile.setCreatorId(oldfile.getCreatorId());
			newfile.setSize(oldfile.getSize());
			newfile.setState(IFile.STATE_PRIVATE);
			newfile.setNDirId(ndir.getId());
			newfile.setTitle(oldfile.getTitle());
			newfile.setRootCategoryId(oldfile.getRootCategoryId());
			newfile.setSubCategoryId(oldfile.getSubCategoryId());
			newfile.setDepartment(oldfile.getDepartment());
			newfile.setDepartmentId(oldfile.getDepartmentId());

			// 5.创建者userid
			newfile.setOwnerId(user.getId());
			newfile.setOrigin(NFile.ORIGN_FAVORITE);
			newfile.setShareId(oldfile.getId());
			this.doCreate(newfile);


			// 写入日志
			logsProcess.doLog(Logs.OPERATION_TYPE_FAVORITE, Logs.OPERATION_FILE, null, null, null, null, null, oldfile, params, user);

			// 建立索引
			IndexBuilder builder = IndexBuilder.getInstance();
			builder.setRealPath(getRealPath(params));
			builder.setNdiskid(disk.getId());
			builder.addNFile(newfile);
			builder.buildIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 推荐文件
	 * @param fileId 文件id
	 * @param user 推荐者
	 * @param params
	 * @param reconmendUserId 被推荐用户id
	 * @throws Exception
	 */
	public void doRecommend(String fileId, NUser user, ParamsTable params, String reconmendUserId)
			throws Exception {
		NDirProcess nDirProcess = new NDirProcessBean();
		NDiskProcess nDiskProcess = new NDiskProcessBean();
		try {
			NDir ndir = nDirProcess.getPersonalRecommendNDir(reconmendUserId);
			NDisk disk = nDiskProcess.getNDiskByUser(reconmendUserId);
			if(disk == null || ndir == null){
				throw new Exception("用户ID=" +reconmendUserId+ "个人网盘未设置!");
			}
		
			NFile oldfile = ((NFileDAO) getDAO()).find(fileId);
			NFile newfile = new NFile();
		
			// 2.将oldfile中数据复制到newfile中
			newfile.setId(Sequence.getSequence());
			newfile.setName(oldfile.getName());
			newfile.setSize(oldfile.getSize());
			newfile.setUrl(oldfile.getUrl());
			newfile.setCreatorId(oldfile.getCreatorId());
			newfile.setSize(oldfile.getSize());
			newfile.setState(IFile.STATE_PRIVATE);
			newfile.setNDirId(ndir.getId());
			newfile.setTitle(oldfile.getTitle());
			newfile.setRootCategoryId(oldfile.getRootCategoryId());
			newfile.setSubCategoryId(oldfile.getSubCategoryId());
			newfile.setDepartment(oldfile.getDepartment());
			newfile.setDepartmentId(oldfile.getDepartmentId());
		
			// 5.创建者userid
			newfile.setOwnerId(reconmendUserId);
			newfile.setOrigin(NFile.ORIGN_RECOMMEND);
			String shareid = StringUtil.isBlank(oldfile.getShareId()) ? oldfile.getId()
					: oldfile.getShareId();
			if (isSharedInThisNdir(ndir.getId(), shareid)) {
				throw new Exception("该文件已被推荐过!");
			}
			newfile.setShareId(shareid);
			this.doCreate(newfile);
		
			// 建立索引
			IndexBuilder builder = IndexBuilder.getInstance();
			builder.setRealPath(getRealPath(params));
			builder.setNdiskid(disk.getId());
			builder.addNFile(newfile);
			builder.buildIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doUpdate(NObject vo) throws Exception {
		// TODO Auto-generated method stub
		NFile nfile = (NFile) vo;

		// 更新最后修改时间
		nfile.setLastmodify(new Date());
		getDAO().update(nfile);
	}

	public void doRemove(String pk) throws Exception {
		NFile nfile = (NFile) getDAO().find(pk);

		// 1.类型为上传或转存时先判断是否存在收藏该文件，有的话删除记录
		if (nfile.getOrigin() == NFile.ORIGN_UPLOAD
				|| nfile.getOrigin() == NFile.ORIGN_REUPLOAD) {
			((NFileDAO) getDAO()).removeShare(pk);
		}

		// 2.删除这条数据记录
		getDAO().remove(pk);

		// 3.删掉文件
		if (nfile.getUrl() != null && nfile.getUrl() != "") {
			deletePhysicalFile(nfile);
		}
	}

	private void deletePhysicalFile(NFile file) throws Exception {
		String realPath = Environment.getInstance().getApplicationRealPath()
				+ FileUtils.uploadFolder;
		String source = realPath + file.getUrl();

		if(source.lastIndexOf(file.getId()+ ".") != -1){//判断Url是否含有以fileid命名的文件夹
			
			String destPath = source.substring(0, source.lastIndexOf(file.getId()
					+ ".") - 1)
					+ File.separator + FileUtils.SWF_PATH + File.separator;
			File s = new File(source);
			if (s.exists()) {
				s.delete();//删除上传文件
			}
			File pdf = new File(destPath + file.getId() + "." + NFile.TYPE_PDF);
			if (pdf.exists()) {
				pdf.delete();//删除pdf文件
			}
			File swf = new File(destPath + file.getId() + "." + NFile.TYPE_SWF);
			if (swf.exists()) {
				swf.delete();//删除swf文件
			}
		}
	}

	public NObject doView(String id) throws Exception {
		// TODO Auto-generated method stub
		return (NFile) getDAO().find(id);
	}

	public DataPackage<NFile> doQuery(String userid, String ndirid)
			throws Exception {
		DataPackage<NFile> result = ((NFileDAO) getDAO()).query(userid, ndirid);
		return result;

	}

	public void deleteFileByNDirid(String ndirid) throws Exception {
		((NFileDAO) getDAO()).deleteFileByNDirid(ndirid);
	}

	public void createFile(NFile file, ParamsTable params, NUser user)
			throws Exception {

		// 有文件名获取文件类型
		String name = file.getName();
		LogsProcess logsProcess = new LogsProcessBean();
		if (StringUtil.isBlank(file.getType()) && (!StringUtil.isBlank(name))
				&& name.lastIndexOf(".") > -1) {
			file.setType(name.substring(name.lastIndexOf(".") + 1, name
					.length()));
		}
		try {
			beginTransaction();
			doCreate(file);
			logsProcess.doLog(Logs.OPERATION_TYPE_UPLOAD, Logs.OPERATION_FILE, null, null, null, null, null, file, params, user);
			commitTransaction();
			
			String type = file.getType();
			if (isReadableFile(type)) {
				createSWF(file, type, params);
			}
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	public void createSWF(NFile file, String type, ParamsTable params)
			throws Exception {
		try {
			String realPath = getRealPath(params);
			String ndiskid = params.getParameterAsString("_ndiskid");
			String source = realPath + file.getUrl();
			if(!new File(source).exists()) return;
			ConverterBundle bundle = new ConverterBundle(type, file, source);
			IndexBundle indexBundle = new IndexBundle(file, realPath, ndiskid);
			ConverterRunner runner = ConverterRunner.getInstance();
			runner.setIndexBundle(indexBundle);
			runner.addBundle(bundle);
			runner.convert();
		} catch (Exception e) {
			throw e;
		}
	}

	public void moveNFile(String parentid, String[] fileids) throws Exception {
		((NFileDAO) getDAO()).moveNFile(parentid, fileids);
	}

	/**
	 * 是否为可阅读文档类型
	 * 
	 * @param type
	 * @return
	 */
	public boolean isReadableFile(String type) {
		boolean readable = false;
		if (NFile.TYPE_DOC.equalsIgnoreCase(type)
				|| NFile.TYPE_DOCX.equalsIgnoreCase(type)
				|| NFile.TYPE_PDF.equalsIgnoreCase(type)
				|| NFile.TYPE_XLS.equalsIgnoreCase(type)
				|| NFile.TYPE_XLSX.equalsIgnoreCase(type)
				|| NFile.TYPE_PPT.equalsIgnoreCase(type)
				|| NFile.TYPE_PPTX.equalsIgnoreCase(type)
				|| NFile.TYPE_WPS.equalsIgnoreCase(type)
				|| NFile.TYPE_TXT.equalsIgnoreCase(type)
				|| NFile.TYPE_HTML.equalsIgnoreCase(type)
				|| NFile.TYPE_HTM.equalsIgnoreCase(type)
				|| NFile.TYPE_DPS.equalsIgnoreCase(type)
				|| NFile.TYPE_ET.equalsIgnoreCase(type)
				|| NFile.TYPE_POT.equalsIgnoreCase(type)
				|| NFile.TYPE_PPS.equalsIgnoreCase(type)
				|| NFile.TYPE_RTF.equalsIgnoreCase(type)
				|| NFile.TYPE_JPEG.equalsIgnoreCase(type)
				|| NFile.TYPE_JPG.equalsIgnoreCase(type)
				|| NFile.TYPE_PNG.equalsIgnoreCase(type)
				|| NFile.TYPE_GIF.equalsIgnoreCase(type)) {
			readable = true;
		}
		return readable;

	}

	/**
	 * 用户是否已经收藏文件
	 * 
	 * @param fileId
	 *            文件id
	 * @param user
	 *            用户
	 * @throws Exception
	 */
	public boolean isFavorited(String fileId, NUser user) throws Exception {
		long count = ((NFileDAO) getDAO()).countFavorited(fileId, user);
		if (count > 0) {
			return true;
		}
		return false;
	}

	public boolean isSharedInThisNdir(String ndirid, String nfileid)
			throws Exception {
		long count = ((NFileDAO) getDAO()).isSharedInThisNdir(ndirid, nfileid);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void doRemoveNFileByRemoveUser(String userid) throws Exception {
		((NFileDAO) getDAO()).doRemoveNFileByRemoveUser(userid);
	}
	
	public long getUploadFilesTotal() throws Exception {
		
		return ((NFileDAO) getDAO()).countBySql("SELECT COUNT(*) FROM KM_NFILE WHERE ORIGIN ="+IFile.ORIGN_UPLOAD);
	}
	
	/**
	 * 根据文件名查找文件
	 * @param name
	 * @throws Exception
	 */
	public DataPackage<NFile> doQueryByName(int page,int lines,String name,String dirid) throws Exception{
		return ((NFileDAO)getDAO()).queryByName(page, lines, name, dirid);
	}

}
