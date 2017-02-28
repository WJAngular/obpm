package cn.myapps.km.disk.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;

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
import cn.myapps.km.util.StringUtil;




public class NDiskAction extends AbstractRunTimeAction<NDisk> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4691878458455714150L;


	@Override
	public NRunTimeProcess<NDisk> getProcess() {
		// TODO Auto-generated method stub
		return new NDiskProcessBean();
	}
	
	private String n;
	
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
	 * 网盘类型
	 */
	private int _type;
	
	/**
	 * 导航的Json数据
	 */
	private String naviJson;
	
	public String getNaviJson() {
		return naviJson;
	}

	public void setNaviJson(String naviJson) {
		this.naviJson = naviJson;
	}

	public int get_type() {
		return _type;
	}

	public void set_type(int _type) {
		this._type = _type;
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

	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	
	public String doView() throws Exception {
		return SUCCESS;
	}

	/**
	 * 查看列表信息
	 */
	@SuppressWarnings("unchecked")
	public String doListView() throws Exception {
		ParamsTable params = this.getParams();
		NDiskProcess nDiskprocess = new NDiskProcessBean();
		NDirProcess nDirProcess = new NDirProcessBean();
		NDisk disk = null;
		NDir dir = null;
		String _ndirid = "";
		try {
			if(ndirid == null || ndiskid == null){
				if(NDisk.TYPE_PERSONAL == get_type()){
					disk = nDiskprocess.getNDiskByUser(getUser().getId());
					
				}else if(NDisk.TYPE_PUBLIC == get_type()){
					disk = nDiskprocess.getPublicDisk(getUser().getDomainid());
				}else if(NDisk.TYPE_ARCHIVE == get_type()){
					disk = nDiskprocess.getArchiveDisk(getUser().getDomainid());
				}
				if(disk == null || getUser().getKmRoles().isEmpty()){
					addFieldError("1", "该用户还未配置网盘!");
					return "error";
				}
				dir = (NDir) nDirProcess.doView(disk.getnDirId());
				if(dir == null){
					addFieldError("1", "没有找到网盘目录!");
					return getDiskResult();
				}
			}
			
			if(ndirid == null && dir != null){
				_ndirid = dir.getId();
			}else {
				_ndirid = ndirid;
			}
			
			int lines = 20;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.KM_FILELIST_PAGELINE.equals(cookie.getName())){
					lines = Integer.parseInt(cookie.getValue());
				}
			    cookie.getName();
			    cookie.getValue();
			}
			DataPackage datas = nDirProcess.getUnderNDirAndNFile(1,lines,_ndirid,"name","ASC");
			setDatas(datas);
			
			if(StringUtil.isBlank(ndirid))
				setNdirid(dir.getId());
			if(StringUtil.isBlank(ndiskid)) 
				setNdiskid(disk.getId());
			ServletActionContext.getRequest().setAttribute("orderbyfield", "name");
			ServletActionContext.getRequest().setAttribute("_sortStatus", "ASC");
			

			if(params.getParameterAsBoolean("scanDir")){
				Stack<NDir> superiorNDirList = (Stack<NDir>) nDirProcess.getSuperiorNDirList(_ndirid);
				if(superiorNDirList.size() > 0){
					superiorNDirList.remove(superiorNDirList.size() - 1);
					Collections.reverse(superiorNDirList);
					StringBuffer _naviJson = new StringBuffer();
					_naviJson.append("[");
					for(Iterator<NDir> it = superiorNDirList.iterator(); it.hasNext();){
						NDir _dir = it.next();
						_naviJson.append("{\"id\":\"").append(_dir.getId()).append("\",");
						_naviJson.append("\"name\":\"").append(_dir.getName()).append("\",");
						_naviJson.append("\"pId\":\"").append(_dir.getParentId()).append("\"},");
					}
					if(_naviJson.length() > 1){
						_naviJson.setLength(_naviJson.length() - 1);
					}
					_naviJson.append("]");
					this.setNaviJson(_naviJson.toString());
				}
			}
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			e.printStackTrace();
			return INPUT;
		}
		return getDiskResult();
	}
	
	public String doListHotest() {
		NDiskProcess nDiskprocess = new NDiskProcessBean();
		try {
			ParamsTable params = getParams();
			Integer page = params.getParameterAsInteger("_currpage");
			Integer lines = params.getParameterAsInteger("_rowcount");
			String categoryID = params.getParameterAsString("categoryid");
			
			if (page == null || page <= 0)page = 1;
			if (lines == null || lines <= 0)lines = 10;
			
			String domainid = getUser().getDomainid();
			
			DataPackage datas = nDiskprocess.doListHotest(page, 10, domainid, categoryID);
			setDatas(datas);
			ServletActionContext.getRequest().setAttribute("categoryid", categoryID);
			ServletActionContext.getRequest().setAttribute("dataType", "topShare");
			return SUCCESS;
			
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			e.printStackTrace();
			return INPUT;
		}
	}
	
	public String doListNewest() {
		NDiskProcess nDiskprocess = new NDiskProcessBean();
		try {
			ParamsTable params = getParams();
			Integer page = params.getParameterAsInteger("_currpage");
			Integer lines = params.getParameterAsInteger("_rowcount");
			String categoryID = params.getParameterAsString("categoryid");
			
			if (page == null || page <= 0)page = 1;
			if (lines == null || lines <= 0)lines = 10;
			
			String domainid = getUser().getDomainid();
			
			DataPackage datas = nDiskprocess.doListNewestFile(page, 10, domainid, categoryID);
			setDatas(datas);
			ServletActionContext.getRequest().setAttribute("categoryid", categoryID);
			ServletActionContext.getRequest().setAttribute("dataType", "topUpload");
			return SUCCESS;
			
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			e.printStackTrace();
			return INPUT;
		}
	}
	
	public String doJumpToTree(){
		return SUCCESS;
	}

	private String getDiskResult() {
		if(NDisk.TYPE_PERSONAL == get_type()){
			return "personal";
		}else if(NDisk.TYPE_PUBLIC == get_type()){
			return "public";
		}else if(NDisk.TYPE_ARCHIVE == get_type()){
			return "personal";
		}
		
		return INPUT;
	}
	
	/**
	 * 打开个人设置
	 */
	public String doSetting() throws Exception {
		
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doTreeView() throws Exception {
		return getDiskResult();
	}
}
