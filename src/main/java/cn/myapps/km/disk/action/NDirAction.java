package cn.myapps.km.disk.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;


import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.contans.Web;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.disk.ejb.NDir;
import cn.myapps.km.disk.ejb.NDirProcess;
import cn.myapps.km.disk.ejb.NDirProcessBean;
import cn.myapps.km.disk.ejb.NDisk;
import cn.myapps.km.disk.ejb.NDiskProcess;
import cn.myapps.km.disk.ejb.NDiskProcessBean;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.disk.ejb.NFileProcess;
import cn.myapps.km.disk.ejb.NFileProcessBean;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.search.IndexBuilder;
import cn.myapps.km.util.FileUtils;
import cn.myapps.km.util.Sequence;
import cn.myapps.km.util.SequenceException;
import cn.myapps.km.util.StringUtil;
import cn.myapps.km.log.ejb.Logs;
import cn.myapps.km.log.ejb.LogsProcess;
import cn.myapps.km.log.ejb.LogsProcessBean;

public class NDirAction extends AbstractRunTimeAction<NDir> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6483037135551057768L;
	
	/**
	 * 视图类型:普通
	 */
	public static final String VIEWTYPE_NORMAL = "1";
	
	/**
	 * 视图类型:树形
	 */
	public static final String VIEWTYPE_TREE = "2";
	
	public NDirAction() {
		super();
		setContent(new NDir());
	}

	@Override
	public NRunTimeProcess<NDir> getProcess() {
		// TODO Auto-generated method stub
		return new NDirProcessBean();
	}
	
	/**
	 * 目录或文件
	 */
	private Collection<NObject> nObjects;
	
	/**
	 * 网盘ID
	 */
	private String ndiskid;
	
	/**
	 * 当前目录ID
	 */
	private String ndirid;
	
	/**
	 * 选中的目录id集合
	 */
	private String[] _dirSelects;
	
	/**
	 * 选中的文件id集合
	 */
	private String[] _fileSelects;
	
	/**
	 * 导航的Json数据
	 */
	private String naviJson;
	
	/**
	 * 网盘类型
	 */
	private int _type;
	
	/**
	 * 视图类型
	 */
	private String viewType;
	
	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getNaviJson() {
		return naviJson;
	}

	public void setNaviJson(String naviJson) {
		this.naviJson = naviJson;
	}

	public String[] get_dirSelects() {
		return _dirSelects;
	}

	public void set_dirSelects(String[] dirSelects) {
		_dirSelects = dirSelects;
	}

	public String[] get_fileSelects() {
		return _fileSelects;
	}

	public void set_fileSelects(String[] fileSelects) {
		_fileSelects = fileSelects;
	}
	
	public String getNdiskid() {
		return ndiskid;
	}

	public void setNdiskid(String ndiskid) {
		this.ndiskid = ndiskid;
	}

	public String getNdirid() {
		return ndirid;
	}

	public void setNdirid(String ndirid) {
		this.ndirid = ndirid;
	}

	public Collection<NObject> getnObjects() {
		return nObjects;
	}

	public void setnObjects(Collection<NObject> nObjects) {
		this.nObjects = nObjects;
	}

	public int get_type() {
		return _type;
	}

	public void set_type(int _type) {
		this._type = _type;
	}

	/**
	 * 新建目录
	 * @return
	 */
	public String doNew(){
		return SUCCESS;
	}
	
	/**
	 * 保存目录
	 * @return
	 * @throws Exception 
	 */
	public String doSave() throws Exception{
		NDir dir = (NDir) (this.getContent());
		NDirProcess nDirProcess = (NDirProcess) getProcess();
		LogsProcess logsProcess = new LogsProcessBean();
		try {
			//NDir parentDir = (NDir) nDirProcess.doView(ndirid);
			NUser user = getUser();
			dir.setId(Sequence.getSequence());
			dir.setOwnerId(getUser().getId());
			dir.setParentId(ndirid);
			dir.setnDiskId(ndiskid);
			dir.setPath(File.separator+ndiskid);
			dir.setCreateDate(new Date());
			dir.setType(NDir.TYPE_NORMAL);
			dir.setDomainId(user.getDomainid());
			nDirProcess.doCreate(dir);
			logsProcess.doLog(Logs.OPERATION_TYPE_CREATE, Logs.OPERATION_DIRECTORY, dir, null, null, null, null, null, getParams(), user);
			
		} catch (SequenceException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 删除目录或文件
	 * @return
	 */
	public String doDelete() throws Exception{
		NDirProcess nDirProcess = (NDirProcess) getProcess();
		NDiskProcess nDiskProcess = new NDiskProcessBean();
		LogsProcess logsProcess = new LogsProcessBean();
		NDisk disk = null;
		if(NDisk.TYPE_PERSONAL == get_type()){
			disk = nDiskProcess.getNDiskByUser(getUser().getId());
		}else if(NDisk.TYPE_PUBLIC == get_type()){
			disk = nDiskProcess.getPublicDisk(getUser().getDomainid());
		}else if(NDisk.TYPE_ARCHIVE == get_type()){
			disk = nDiskProcess.getArchiveDisk(getUser().getDomainid());
		}
		
		try {
			String indexRealPath = ServletActionContext.getRequest().getRealPath("/ndisk");
			if(_dirSelects!=null){
				logsProcess.doLog(Logs.OPERATION_TYPE_DELETE, Logs.OPERATION_DIRECTORY, null, null, null, _dirSelects, null, null, getParams(), getUser());
			}
			if(_fileSelects!=null){
				logsProcess.doLog(Logs.OPERATION_TYPE_DELETE, Logs.OPERATION_FILE, null, null, null, null, _fileSelects, null, getParams(), getUser());
			}
			nDirProcess.doRemoveNdirAndNFile(_dirSelects, _fileSelects, disk.getId(), indexRealPath, get_type());
		} catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("1", e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 浏览目录
	 */
	@SuppressWarnings("unchecked")
	public String doView() throws Exception{
		NDirProcess nDirProcess = (NDirProcess) getProcess();
		ParamsTable params = getParams();
		int lines = 20;
		Cookie[] cookies = ServletActionContext.getRequest().getCookies();
		for(Cookie cookie : cookies){
			if(Web.KM_FILELIST_PAGELINE.equals(cookie.getName())){
				lines = Integer.parseInt(cookie.getValue());
			}
		    cookie.getName();
		    cookie.getValue();
		}
		int currPage = params.getParameterAsInteger("_currpage") == null ? 1:params.getParameterAsInteger("_currpage");
		String orderbyfield = StringUtil.isBlank(params.getParameterAsString("orderbyfield"))? "name" : params.getParameterAsString("orderbyfield");
		String _sortStatus = StringUtil.isBlank(params.getParameterAsString("_sortStatus")) ? "ASC" : params.getParameterAsString("_sortStatus");
		try {
			DataPackage datas = nDirProcess.getUnderNDirAndNFile(currPage,lines,ndirid,orderbyfield,_sortStatus);
			setDatas(datas);
			ServletActionContext.getRequest().setAttribute("orderbyfield", orderbyfield);
			ServletActionContext.getRequest().setAttribute("_sortStatus", _sortStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return getRetureResult();
	}
	
	private String getRetureResult() {
		if(NDisk.TYPE_PERSONAL == get_type()){
			if(VIEWTYPE_NORMAL.equals(viewType)){
				return "personalList";
			}else if(VIEWTYPE_TREE.equals(viewType)){
				return "personalSubTree";
			}
		}else if(NDisk.TYPE_PUBLIC == get_type()){
			if(VIEWTYPE_NORMAL.equals(viewType)){
				return "publicList";
			}else if(VIEWTYPE_TREE.equals(viewType)){
				return "publicSubTree";
			}
		}else if(NDisk.TYPE_ARCHIVE == get_type()){
			if(VIEWTYPE_NORMAL.equals(viewType)){
				return "personalList";
			}else if(VIEWTYPE_TREE.equals(viewType)){
				return "personalSubTree";
			}
		}
		return INPUT;
	}

	/**
	 * 文件夹或文件重命名
	 * @return
	 * @throws Exception
	 */
	public String doRename() throws Exception{
		NDirProcess nDirProcess = (NDirProcess) getProcess();
		NFileProcess nFileProcess = new NFileProcessBean();
		LogsProcess logsProcess = new LogsProcessBean();
		String name = getContent().getName();
		ParamsTable params = getParams();
		String renameNDirid = params.getParameterAsString("renameNDirid");
		String renameNFileid = params.getParameterAsString("renameNFileid");
		
		try {
			if(!StringUtil.isBlank(renameNDirid)){
				NDir dir = (NDir) nDirProcess.doView(renameNDirid);
				logsProcess.doLog(Logs.OPERATION_TYPE_RENAME, Logs.OPERATION_DIRECTORY, dir, null, name, null, null, null, params, getUser());
				dir.setName(name);
				nDirProcess.doUpdate(dir);
			}else if(!StringUtil.isBlank(renameNFileid)){
				NFile nFile = (NFile) nFileProcess.doView(renameNFileid);
				logsProcess.doLog(Logs.OPERATION_TYPE_RENAME, Logs.OPERATION_FILE, null, null, name, null, null, nFile, params, getUser());
				nFile.setName(name);
				nFileProcess.doUpdate(nFile);
				
				//更新索引
				IndexBuilder builder = IndexBuilder.getInstance();
				builder.setRealPath(getRealPath());
				builder.setNdiskid(ndiskid);
				builder.addNFile(nFile);
				builder.setUpdate(true);//为更新索引
				builder.buildIndex();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 文件,文件夹的移动
	 * @return
	 * @throws Exception
	 */
	public String doMove() throws Exception {
		NDirProcess nDirprocess = (NDirProcess) getProcess();
		NFileProcess nFileProcess = new NFileProcessBean();
		LogsProcess logsProcess = new LogsProcessBean();
		ParamsTable params = getParams();
		String parentid = params.getParameterAsString("moveto");
		
		try {
			if(StringUtil.isBlank(parentid)){
				throw new Exception("此次操作不合法！");
			}
			
			if(_dirSelects != null && !StringUtil.isBlank(_dirSelects[0])){
				nDirprocess.moveNDir(parentid, _dirSelects[0].split(";"));
				logsProcess.doLog(Logs.OPERATION_TYPE_MOVE, Logs.OPERATION_DIRECTORY, null, parentid, null, _dirSelects[0].split(";"), null, null, params, getUser());
			}
			
			if(_fileSelects != null && !StringUtil.isBlank(_fileSelects[0])){
				nFileProcess.moveNFile(parentid, _fileSelects[0].split(";"));
				logsProcess.doLog(Logs.OPERATION_TYPE_MOVE, Logs.OPERATION_FILE, null, parentid, null, null, _fileSelects[0].split(";"), null, params, getUser());
			}
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("1", e.getMessage());
			return INPUT;
			
		}
		
		addActionMessage("成功移动文件!");
		return SUCCESS;
	}
	
	/**
	 * 获取该网盘下,此文件夹的下级文件夹
	 * @param ndiskid
	 * @param parentid
	 * @return 
	 * @return
	 */
	public String getChildren() throws Exception{
		StringBuffer json = new StringBuffer();
		
		try {
			NDirProcess process = new NDirProcessBean();
			ParamsTable params = this.getParams();
			String ndiskid = params.getParameterAsString("ndiskid");
			String parentid = params.getParameterAsString("parentid");
			Collection<NDir> ndirs = process.getUnderNDirByDisk(ndiskid, parentid);
			
			json.append("[");
			
			if(ndirs.size() > 0){
				for(Iterator<NDir> it = ndirs.iterator(); it.hasNext();){
					NDir dir = it.next();
					json.append("{");
					json.append("\"id\":\"" + dir.getId() + "\",");
					json.append("\"name\":\"" + dir.getName() + "\",");
					json.append("\"parentid\":\"" + dir.getParentId() + "\",");
					if(process.countUnderNDir(ndiskid, dir.getId()) > 0){
						json.append("\"isparent\":true");
					}else {
						json.append("\"isparent\":false");
					}
					json.append("},");
				}
				json.setLength(json.length() - 1);
			}
			json.append("]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		ActionContext.getContext().put("DATA", json.toString());
		
		return SUCCESS;
	}
	
	//服务器目录的绝对路径
	public static String getRealPath(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String realPath = request.getSession().getServletContext().getRealPath("")+File.separator+FileUtils.uploadFolder;
		return realPath;
	}
}
