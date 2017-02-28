package cn.myapps.km.disk.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.disk.ejb.IFile;
import cn.myapps.km.disk.ejb.NDisk;
import cn.myapps.km.disk.ejb.NDiskProcess;
import cn.myapps.km.disk.ejb.NDiskProcessBean;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.disk.ejb.NFileProcess;
import cn.myapps.km.disk.ejb.NFileProcessBean;
import cn.myapps.km.disk.ejb.SearchNFile;
import cn.myapps.km.log.ejb.Logs;
import cn.myapps.km.log.ejb.LogsProcess;
import cn.myapps.km.log.ejb.LogsProcessBean;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.permission.ejb.IFileAccess;
import cn.myapps.km.permission.ejb.PermissionHelper;
import cn.myapps.km.search.SearchEngine;
import cn.myapps.km.util.Config;
import cn.myapps.km.util.FileUtils;
import cn.myapps.km.util.Sequence;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.json.JsonUtil;


/**
 * @author Happy
 *
 */
public class NFileAction extends AbstractRunTimeAction<NFile> {
	
	Logger log = Logger.getLogger(getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7020543836464569639L;
	
	public static final String DOWNLOAD = "download";
	public static final String READER = "reader";
	
	/**
	 * swf文件地址
	 */
	private String swfUrl;
	
	/**
	 * pdf文件地址
	 */
	private String pdfUrl;
	
	/**
	 * 网盘类型
	 */
	private int _type;
	
	boolean read = true;
	
	boolean write = true;
	boolean download = true;
	
	/**
	 * 标识是否已经收藏过
	 */
	private boolean _favorited = false;
	
	/**
	 * 当前目录ID
	 */
	private String ndirid;
	
	private String downloadFileName;
	
	private InputStream downloadFile;
		
	public String getSwfUrl() {
		return swfUrl;
	}

	public void setSwfUrl(String swfUrl) {
		this.swfUrl = swfUrl;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}
	
	public int get_type() {
		return _type;
	}

	public void set_type(int type) {
		_type = type;
	}

	public boolean getRead() {
		return read;
	}

	public boolean getWrite() {
		return write;
	}

	public boolean getDownload() {
		return download;
	}

	public String getNdirid() {
		return ndirid;
	}

	public void setNdirid(String ndirid) {
		this.ndirid = ndirid;
	}

	/**
	 * 标识是否已经收藏过
	 * @param isFavorited
	 */
	public boolean is_favorited() {
		return _favorited;
	}

	/**
	 * 标识是否已经收藏过
	 * @param isFavorited
	 */
	public void set_favorited(boolean favorited) {
		_favorited = favorited;
	}
	
	

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public InputStream getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(InputStream downloadFile) {
		this.downloadFile = downloadFile;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public NFileAction() {
		super();
		setContent(new NFile());
	}

	@Override
	public NRunTimeProcess<NFile> getProcess() {
		return new NFileProcessBean();
	}
	
	public String doNew() {
		try {
			NFile file = new NFile();
			ParamsTable params = getParams();
			NUser user = getUser();
			String nDirId = params.getParameterAsString("_nDirId");
			if(StringUtil.isBlank(nDirId)){
				NDiskProcess nDiskprocess = new NDiskProcessBean();
				NDisk disk = null;
				if(NDisk.TYPE_PERSONAL == get_type()){
					disk = nDiskprocess.getNDiskByUser(user.getId());
					
				}else if(NDisk.TYPE_PUBLIC == get_type()){
					disk = nDiskprocess.getPublicDisk(getUser().getDomainid());
				}
				if(disk == null){
					addFieldError("1", "该用户还未配置网盘!");
					return INPUT;
				}
				nDirId = disk.getnDirId();
			}
			file.setNDirId(nDirId);
			if(2==get_type()){
				file.setState(IFile.STATE_PRIVATE);
			}else{
				file.setState(IFile.STATE_PUBLIC);
			}
			file.setId(Sequence.getSequence());
			file.setOrigin(NFile.ORIGN_UPLOAD);
			file.setOwnerId(user.getId());
			file.setCreatorId(user.getId());
			file.setCreator(user.getName());
			file.setDomainId(user.getDomainid());
			setContent(file);
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}
	
	public String doSave() {
		try {
			
			ParamsTable params = getParams();
			NUser user = getUser();
			
			String _files = params.getParameterAsString("_files");
			String rootCategoryId = params.getParameterAsString("rootCategoryId");
			String subCategoryId = params.getParameterAsString("subCategoryId");
			
			if(!StringUtil.isBlank(_files)){
				NDiskProcess nDiskProcess = new NDiskProcessBean();
				NDisk disk = null;
				if(NDisk.TYPE_PERSONAL == get_type()){
					disk = nDiskProcess.getNDiskByUser(user.getId());
				}else if(NDisk.TYPE_PUBLIC == get_type()){
					disk = nDiskProcess.getPublicDisk(user.getDomainid());
				}
				params.setParameter("_ndiskid", disk.getId());
				
				Collection<Object> files = JsonUtil.toCollection(_files);
				for (Iterator iterator = files.iterator(); iterator.hasNext();) {
					Map<String, String> fileInfo = (HashMap<String, String>) iterator.next();
					NFile file = new NFile();
					file.setId(fileInfo.get("id"));
					file.setName(fileInfo.get("name"));
					file.setTitle(fileInfo.get("title"));
					file.setSize(Long.parseLong(fileInfo.get("size")));
					file.setUrl(fileInfo.get("url"));
					file.setNDirId(fileInfo.get("nDirId"));
					file.setMemo(fileInfo.get("memo"));
					file.setCreateDate(new Date());
					file.setLastmodify(new Date());
					file.setOrigin(NFile.ORIGN_UPLOAD);
					file.setOwnerId(user.getId());
					file.setCreatorId(user.getId());
					file.setCreator(user.getName());
					file.setDomainId(user.getDomainid());
					file.setRootCategoryId(rootCategoryId);
					file.setSubCategoryId(subCategoryId);
					file.setDepartment(user.getDepartmentById(user.getDefaultDepartment()).getName());
					file.setDepartmentId(user.getDefaultDepartment());
					if(2==get_type()){
						file.setState(IFile.STATE_PRIVATE);
					}else{
						file.setState(IFile.STATE_PUBLIC);
					}
					
					((NFileProcess)getProcess()).createFile(file, params, getUser());
				}
			}
			
			addActionMessage("保存成功");
			
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}
	
	public String doView() {
		NFile file = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		LogsProcess logsProcess = new LogsProcessBean();
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			NUser user = getUser();
			
			if(!StringUtil.isBlank(id)){
				file = (NFile)getProcess().doView(id);
				if(file ==null) throw new Exception("找不到文件！");
				if(IFile.ORIGN_FAVORITE==file.getOrigin()){
					file = (NFile)getProcess().doView(file.getShareId());
					//file.setState(NFile.STATE_PRIVATE);
				}
				
				String sourcePath = getRealPath()+ file.getUrl();
				if(!new File(sourcePath).exists()){
					log.error(String.format("网盘文件[%s]的物理文档[%s]丢失！", file.getName(),sourcePath));
					 throw new FileNotFoundException(String.format("文件[%s]的物理文档不存在！", file.getName()));
				}
				
				//写入日志
				logsProcess.doLog(Logs.OPERATION_TYPE_VIEW, Logs.OPERATION_FILE, null, null, null, null, null, file, params, user);
				setContent(file);
				this.set_favorited(((NFileProcess)getProcess()).isFavorited(file.getId(), user));
			}
			
			if(!isPublicShareFile(file)){
				IFileAccess access = PermissionHelper.findIFileAccessWithInheritance(PermissionHelper.FILE_TYPE_FILE, file.getId(), user);
				if(access !=null){
					PermissionHelper.check(access, PermissionHelper.PERMISSION_TYPE_WRITE);
					read = PermissionHelper.check(access, PermissionHelper.PERMISSION_TYPE_READ);
					write = PermissionHelper.check(access, PermissionHelper.PERMISSION_TYPE_WRITE);
					download = PermissionHelper.check(access, PermissionHelper.PERMISSION_TYPE_DOWNLOAD);
				}else if((!file.getOwnerId().equals(user.getId()) && NDisk.TYPE_PERSONAL == get_type())
						|| (!user.isPublicDiskAdmin() && NDisk.TYPE_PUBLIC == get_type() 
								&& !PermissionHelper.verifyDirManagePermission(file.getNDirId(), user))){
					/*说明
					 *1.私有网盘下、文件所有者不是自己
					 *2.公有网盘下、用户不是公有网盘管理员并且目录没有被授权
					 */
					read = false;
					write = false;
					download = false;
				}
			}
			
			//有且只有read权限系统才去转换swf文件，防止先转换再去判断read权限并且无条件的转换影响系统性能，无须判断有无pdf文件
			if(read && Config.previewEnabled() && ((NFileProcessBean)getProcess()).isReadableFile(file.getType())){
				if(IFile.ORIGN_UPLOAD !=file.getOrigin() && IFile.ORIGN_ARCHIVE != file.getOrigin()){
					file = (NFile)getProcess().doView(file.getShareId());
				}
				
				String swfurl = request.getContextPath()+file.toSwfURL();
				swfurl = swfurl.replaceAll("\\\\", "/");
				String realSwfpath = request.getSession().getServletContext().getRealPath("")+file.toSwfURL();
				
				File f2 = new File(realSwfpath);
				if(!f2.exists()){
					String errorFilePath = realSwfpath.substring(0,realSwfpath.lastIndexOf(".")+1)+"err";
					if(new File(errorFilePath).exists()){
						addFieldError("0", "{*[cn.myapps.km.disk.view_file_damage]*}");
					}else{
						addFieldError("0", "{*[cn.myapps.km.disk.view_file_error]*}");
					}
					((NFileProcessBean)getProcess()).createSWF(file, file.getType(), params);
					
					return SUCCESS;
				}
				setSwfUrl(swfurl);
				
				
				String pdfurl = request.getContextPath()+file.toPdfURL();
				pdfurl = pdfurl.replaceAll("\\\\", "/");
				setPdfUrl(pdfurl);

				return READER;
			}else{
				return SUCCESS;
			}
			
		} catch (Exception e) {
			addFieldError("0", e.getMessage());
			return INPUT;
		}
	}
	
	private boolean isPublicShareFile(NFile file){
		if(file.getState()==IFile.STATE_PUBLIC && !StringUtil.isBlank(file.getShareId()))
			return true;
		
		return false;
	}
	
	public String doDownload() throws Exception{
		NFile nfile = null;
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String rootPath =  request.getSession().getServletContext().getRealPath("");
		String realPath = rootPath + File.separator + FileUtils.uploadFolder;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String now = dateFormat.format(new Date());
			String fileName = now + ".zip";
			
			
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			String[] _nFileids = id.split(";");
			//使用批量下载功能，最大能够下载2G的文件
			if(_nFileids.length > 1){
				byte[] buffer = new byte[1024];
				//创建一个zip文件
				String downloadDirPath = rootPath + File.separator + "downloads" +File.separator+ "zip";
				File downloadDir = new File(downloadDirPath);
				if (!downloadDir.exists() && !downloadDir.isDirectory()) {
					downloadDir.mkdirs();
				}
				String zipFilePath = downloadDir + File.separator + fileName;
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilePath));
				out.setEncoding("gbk");
				for(int i=0;i<_nFileids.length;i++){
					if(!StringUtil.isBlank(_nFileids[i])){
						nfile = (NFile)getProcess().doView(_nFileids[i]);
						if(nfile ==null) continue;
						String realFileName = "";
						
						realFileName = realPath+nfile.getUrl();
						File file = new File(realFileName);
						FileInputStream fis;
						try {
							fis = new FileInputStream(file);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;
						}
						this.setDownloadFileName(nfile.getName());
				        out.putNextEntry(new ZipEntry(downloadFileName));
				        int len;
				        //读入需要下载的文件的内容，打包到zip文件
				        while((len = fis.read(buffer))>0) {
				        	out.write(buffer,0,len);
				        }
				        fis.close();
					}
				}
				out.closeEntry();
				out.close();
				this.setDownloadFile(new FileInputStream(new File(zipFilePath)));
			//单个文件下载	
			}else{
					if(!StringUtil.isBlank(id)){
						nfile = (NFile)getProcess().doView(id);
						if(nfile ==null) throw new Exception("找不到文件！");
						String realFileName = "";
						realFileName = realPath+nfile.getUrl();
						File file = new File(realFileName);
						if (file.exists()) {
							fileName = nfile.getName();
							this.setDownloadFile(new FileInputStream(file));
						} else {
							throw new FileNotFoundException(String.format("文件[%s]的物理文档不存在！", nfile.getName()));
						}
						(new LogsProcessBean()).doLog(Logs.OPERATION_TYPE_DOWNLOAD, Logs.OPERATION_FILE, null, null, null, null,null, nfile, params, getUser());
					}	
					
			 }
			this.setDownloadFileName(java.net.URLEncoder.encode(fileName, "UTF-8"));
			String agent = request.getHeader("USER-AGENT");
			if(null != agent && -1 != agent.indexOf("Firefox")){
				this.setDownloadFileName(MimeUtility.encodeText(fileName, "UTF-8", "B"));
			}
			return SUCCESS;
		}catch(Exception e){
			addFieldError("1", e.getMessage());
			e.printStackTrace();
		}
		return  NONE;
	}
	
	public String doRename() throws Exception {
		NFileProcess nFileProcess = (NFileProcess) getProcess();
		String name = ((NFile) getContent()).getName();
		ParamsTable params = getParams();
		String renameNFileid = params.getParameterAsString("renameNFileid");
		
		try {
			NFile nFile = (NFile) nFileProcess.doView(renameNFileid);
			nFile.setName(name);
			nFile.setTitle(name);
			nFileProcess.doUpdate(nFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}

	/**
	 * 保存文件到我的网盘
	 * @return
	 * @throws Exception
	 */
	public String doSaveToMydisk() throws Exception{
		ParamsTable params = getParams();
		String _nFileids = params.getParameterAsString("_fileSelects");
		
		String[] nFileids;
		if(StringUtil.isBlank(_nFileids)){
			nFileids = new String[0];
		}
		else {
			nFileids = _nFileids.split(";");
		}
		
		NFileProcess nFileProcess = (NFileProcess) getProcess();
		NUser user = this.getUser();
		String userid = user.getId();
		
		try {
			for(int k=0; k<nFileids.length; k++){
				nFileProcess.doShareFile4Personal(nFileids[k], user,params, userid);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		addActionMessage("已成功保存文件到我的网盘!");
		return SUCCESS;
	}
	
	public void doSave4Ajax() throws Exception{
		try {
			NFile file = (NFile)getContent();
			((NFileProcess)getProcess()).doUpdate(file);
			ServletActionContext.getResponse().getWriter().print("success");
		} catch (Exception e) {
			ServletActionContext.getResponse().getWriter().print("更新失败！");
		}
		
	}
	
	public String doRefreshPreview(){
		try {
			
			NFile file = (NFile)getContent();
			ParamsTable params = getParams();
			NUser user = getUser();
			NDiskProcess nDiskProcess = new NDiskProcessBean();
			NDisk disk = null;
			if(NDisk.TYPE_PERSONAL == get_type()){
				disk = nDiskProcess.getNDiskByUser(user.getId());
			}else if(NDisk.TYPE_PUBLIC == get_type()){
				disk = nDiskProcess.getPublicDisk(user.getDomainid());
			}
			params.setParameter("_ndiskid", disk.getId());
			((NFileProcess)getProcess()).createSWF(file, file.getType(), getParams());
			ServletActionContext.getResponse().getWriter().print("success");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				ServletActionContext.getResponse().getWriter().print("fault");
			} catch (IOException e1) {
			}
		}
		return SUCCESS;
	}

	public String doShare() throws Exception{
		ParamsTable params = getParams();
		//String[] nFileids = params.getParameterAsArray("_fileSelects");
		String nFileid = (String) params.getParameter("_fileSelects");
		String[] nFileids = nFileid.split(";");
		String shareType = params.getParameterAsString("shareType");
		
		try {
			if("public".equals(shareType)){
				return shareToPublic(params, nFileids);
			}else if("personal".equals(shareType)){
				return shareToPersonal(params, nFileids);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}

	private String shareToPersonal(ParamsTable params, String[] nFileids) throws Exception{
		NFileProcess nFileProcess = (NFileProcess) getProcess();
		LogsProcess logsProcess = new LogsProcessBean();
		//String[] userids = params.getParameterAsArray("userids");
		String userid = (String) params.getParameter("userids");
		String[] userids=userid.split(";");  
		NUser user = this.getUser();
		try {
			for(int k=0; k<nFileids.length; k++){
				for(int i=0; i<userids.length; i++){
					nFileProcess.doShareFile4Personal(nFileids[k], user,params, userids[i]);
				}
				// 写入日志
				NFile file = (NFile) nFileProcess.doView(nFileids[k]);
				logsProcess.doLog(Logs.OPERATION_TYPE_SHARE, Logs.OPERATION_FILE, null, null, null, null, null, file, params, user);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		addActionMessage("{*[cn.myapps.km.disk.private_share_success]*}");
		return SUCCESS;
	}

	private String shareToPublic(ParamsTable params, String[] nFileids) throws Exception {
		NFileProcess nFileProcess = (NFileProcess) getProcess();
		LogsProcess logsProcess = new LogsProcessBean();
		NUser user = this.getUser();
		
		try {
			for(int i=0; i<nFileids.length; i++){
				nFileProcess.doShareFile4Public(nFileids[i], user,params);
				// 写入日志
				NFile file = (NFile) nFileProcess.doView(nFileids[i]);
				logsProcess.doLog(Logs.OPERATION_TYPE_SHARE, Logs.OPERATION_FILE, null, null, null, null, null, file, params, user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("1", e.getMessage());
			return SUCCESS;
		}
		
		ServletActionContext.getRequest().setAttribute("sharefileid", nFileids[0]);
		addActionMessage("{*[cn.myapps.km.disk.public_share_success]*}");
		
		return SUCCESS;
	}
	
	public String doFavorite() {
		try {
			NFileProcess nFileProcess = (NFileProcess) getProcess();
			ParamsTable params = getParams();
			String fileId = params.getParameterAsString("_fileId");
			if(!StringUtil.isBlank(fileId)){
				nFileProcess.doFavorite(fileId, getUser(), getParams());
				this.set_favorited(true);
			}else {
				throw new Exception("没有找到收藏文件！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("0", e.getMessage());
		}
		addActionMessage("已成功收藏文件!");
		return SUCCESS;
	}
	
	/**
	 * 推荐文件
	 * @return
	 */
	public String doRecommend() {
		try {
			NFileProcess nFileProcess = (NFileProcess) getProcess();
			LogsProcess logsProcess = new LogsProcessBean();
			ParamsTable params = getParams();
			NUser user = this.getUser();
			String fileId = params.getParameterAsString("_fileId");
			String userid = params.getParameterAsString("userids");
			if(StringUtil.isBlank(userid))
				throw new Exception("请选择用户！");
			if(!StringUtil.isBlank(fileId)){
				String[] userids = userid.split(";");
				for(int i=0; i<userids.length; i++){
					nFileProcess.doRecommend(fileId, getUser(), getParams(), userids[i]);
				}
				// 写入日志
				NFile file = (NFile) nFileProcess.doView(fileId);
				logsProcess.doLog(Logs.OPERATION_TYPE_RECOMMEND, Logs.OPERATION_FILE, null, null, null, null, null, file, params, user);
			}else {
				throw new Exception("没有找到推荐文件！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("0", e.getMessage());
		}
		addActionMessage("已成功推荐文件!");
		return SUCCESS;
	}
	
	public String doQuery() throws Exception {
		try {
			ParamsTable params = this.getParams();
			String queryString = params.getParameterAsString("queryString");
			
			NDiskProcess nDiskProcess = new NDiskProcessBean();
			NFileProcessBean nfileProcess = new NFileProcessBean();
			LogsProcess logsProcess = new LogsProcessBean();
			NDisk disk = null;
			if(NDisk.TYPE_PERSONAL == get_type()){
				disk = nDiskProcess.getNDiskByUser(getUser().getId());
			}else if(NDisk.TYPE_PUBLIC == get_type()){
				disk = nDiskProcess.getPublicDisk(getUser().getDomainid());
			}else if(NDisk.TYPE_ARCHIVE == get_type()){
				disk = nDiskProcess.getArchiveDisk(getUser().getDomainid());
			}
			
			String indexRealPath = ServletActionContext.getRequest().getRealPath("/ndisk");
			Collection<SearchNFile> nfiles = new ArrayList<SearchNFile>();
			
			int curragePage = params.getParameterAsInteger("_curragePage") == null || params.getParameterAsInteger("_curragePage")<= 0 ? 1:params.getParameterAsInteger("_curragePage");
			if(Config.previewEnabled()){
				Collection<SearchNFile> searchFiles = SearchEngine.search(queryString, indexRealPath, disk.getId(), 10, curragePage);
				
				for(Iterator<SearchNFile> it = searchFiles.iterator();it.hasNext();){
					SearchNFile searchFile = it.next();
					NFile file = (NFile) nfileProcess.doView(searchFile.getId());
					if(file != null){
						searchFile.setCreator(file.getCreator());
						searchFile.setDownloads(file.getDownloads());
						searchFile.setEvaluateSum(file.getGood() + file.getBad());
						searchFile.setSearchScore(file.getScore());
						searchFile.setNDirId(file.getNDirId());
					}
					nfiles.add(searchFile);
				}
			}else{	//当没构成全文检索的时候  则查找文件名
				DataPackage<NFile> datas = nfileProcess.doQueryByName(curragePage, 10,queryString,disk.getnDirId());
				if(datas.datas != null && datas.datas.size()>0){
					for (Iterator<NFile> iter = datas.datas.iterator();iter.hasNext();) {
						NFile file = iter.next();
						SearchNFile searchFile = new SearchNFile();
						searchFile.setId(file.getId());
						searchFile.setName(file.getName());
						searchFile.setMemo("......");
						nfiles.add(searchFile);
					}
				}
				ServletActionContext.getRequest().setAttribute("_curragePage",
						curragePage);
				ServletActionContext.getRequest().setAttribute("_rowCount",
						datas.rowCount);
			}
			NUser user = this.getUser();
			// 写入日志
			logsProcess.doLog(Logs.OPERATION_TYPE_SELECT, Logs.OPERATION_FILE, null, null, null, null, null, null, params, user);
			ServletActionContext.getRequest().setAttribute("nfiles", nfiles);
			ServletActionContext.getRequest().setAttribute("queryString", queryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	//服务器目录的绝对路径
	public static String getRealPath(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String realPath = request.getSession().getServletContext().getRealPath("")+File.separator+FileUtils.uploadFolder;
		return realPath;
	}
	

}
