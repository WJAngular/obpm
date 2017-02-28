package cn.myapps.km.desktop.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.constans.Web;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.sysconfig.ejb.KmConfig;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.disk.ejb.IFile;
import cn.myapps.km.disk.ejb.NDir;
import cn.myapps.km.disk.ejb.NDirProcess;
import cn.myapps.km.disk.ejb.NDirProcessBean;
import cn.myapps.km.disk.ejb.NDisk;
import cn.myapps.km.disk.ejb.NDiskProcess;
import cn.myapps.km.disk.ejb.NDiskProcessBean;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.disk.ejb.NFileProcess;
import cn.myapps.km.disk.ejb.NFileProcessBean;
import cn.myapps.km.log.ejb.LogsProcess;
import cn.myapps.km.log.ejb.LogsProcessBean;
import cn.myapps.km.org.ejb.NRole;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.FileUtils;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.property.MultiLanguageProperty;
import cn.myapps.util.property.PropertyUtil;


public class KmServicesAction extends ActionSupport {

	private static final long serialVersionUID = 7924618426580902584L;

	public KmServicesAction() {
		super();
	}
	
	
	public void doGetDiskInfo() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		PrintWriter writer = getPrintWriter(request, response);
		try {
			NDiskProcess nDiskprocess = new NDiskProcessBean();
			NDisk disk = null;
			NUser user = getUser();
			ParamsTable params = getParams();
			int diskType = params.getParameterAsInteger("diskType");
			if(NDisk.TYPE_PERSONAL == diskType){
				disk = nDiskprocess.getNDiskByUser(user.getId());
				
			}else if(NDisk.TYPE_PUBLIC == diskType){
				disk = nDiskprocess.getPublicDisk(user.getDomainid());
			}
			if(disk == null || user.getKmRoles().isEmpty()){
				response.setStatus(500);
				writer.println("您没有绑定网盘，请联系管理员！");
				return;
			}else{
				Map<String,String> map = new HashMap<String, String>();
				map.put("nDiskId", disk.getId());
				map.put("nDirId", disk.getnDirId());
				writer.println(JsonUtil.toJson(map));
			}
			
		} catch (Exception e) {
			response.setStatus(500);
			writer.println(-200);
			e.printStackTrace();
		}finally{
			if(writer !=null ){
				writer.flush();
				writer.close();
			}
		}
		}
	
	public void doSaveFile() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		PrintWriter writer = getPrintWriter(request, response);
		try {
			NUser user = getUser();
			ParamsTable params = getParams();
			String nFileId = params.getParameterAsString("nFileId");
			String fileName = params.getParameterAsString("fileName");
			long size = params.getParameterAsLong("size");
			String nDirId = params.getParameterAsString("nDirId");
			int type = params.getParameterAsInteger("type");
			String url = "";
			
			if(!StringUtil.isBlank(nDirId)){
				NDirProcess process = new NDirProcessBean();
				NDir dir = (NDir) process.doView(nDirId);
				if(dir != null) url = dir.getPath()+File.separator+nFileId+fileName.substring(fileName.lastIndexOf("."), fileName.length());
			}
			
			NFile file = new NFile();
			file.setId(nFileId);
			file.setName(fileName);
			file.setTitle(fileName);
			file.setSize(size);
			file.setUrl(url);
			file.setNDirId(nDirId);
			file.setMemo("");
			file.setCreateDate(new Date());
			file.setLastmodify(new Date());
			file.setOrigin(NFile.ORIGN_UPLOAD);
			file.setOwnerId(user.getId());
			file.setCreatorId(user.getId());
			file.setCreator(user.getName());
			file.setDomainId(user.getDomainid());
			file.setRootCategoryId("");
			file.setSubCategoryId("");
			file.setDepartment(user.getDepartmentById(user.getDefaultDepartment()).getName());
			file.setDepartmentId(user.getDefaultDepartment());
			if(2==type){
				file.setState(IFile.STATE_PRIVATE);
			}else{
				file.setState(IFile.STATE_PUBLIC);
			}
			
			new NFileProcessBean().createFile(file, params, user);
			writer.println(200);
			
		} catch (Exception e) {
			writer.println(-200);
			e.printStackTrace();
		}finally{
			if(writer !=null ){
				writer.flush();
				writer.close();
			}
		}
		
	}
	
	public void doSaveDir(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		PrintWriter writer = getPrintWriter(request, response);
		NDirProcess nDirProcess = new NDirProcessBean();
		try {
			NUser user = getUser();
			ParamsTable params = getParams();
			String nDirId = params.getParameterAsString("nDirId");
			String nDiskId = params.getParameterAsString("nDiskId");
			String parentId = params.getParameterAsString("parentId");
			String name = params.getParameterAsString("name");
			
			NDir dir = new NDir();
			dir.setId(nDirId);
			dir.setName(name);
			dir.setOwnerId(user.getId());
			dir.setParentId(parentId);
			dir.setnDiskId(nDiskId);
			dir.setPath(File.separator+nDiskId);
			dir.setCreateDate(new Date());
			dir.setType(NDir.TYPE_NORMAL);
			dir.setDomainId(user.getDomainid());
			nDirProcess.doCreate(dir);
			writer.println(200);
		} catch (Exception e) {
			writer.println(-200);
			e.printStackTrace();
		}finally{
			if(writer !=null ){
				writer.flush();
				writer.close();
			}
		}
	}
	
	public void doDeleteFile() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		NFileProcess process = new NFileProcessBean();
		NDirProcess dirProcess = new NDirProcessBean();
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("nFileId");
			int type = params.getParameterAsInteger("type");
			
			if(!StringUtil.isBlank(id)){
				if(type ==1){
					 process.doRemove(id);
				}else if(type ==2){
					dirProcess.doRemove(id);
				}
				
			}
		} catch (Exception e) {
			response.setStatus(500);
		}
	}
	
	public void doRenameFile() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		NFileProcess process = new NFileProcessBean();
		NDirProcess dirProcess = new NDirProcessBean();
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("nFileId");
			String name = params.getParameterAsString("name");
			int type = params.getParameterAsInteger("type");
			
			if(!StringUtil.isBlank(id)){
				if(type ==1){
					NFile file = (NFile) process.doView(id);
					if(file !=null){
						file.setName(name);
						file.setTitle(name);
						process.doUpdate(file);
					}else{
						response.setStatus(404);
					}
				}else if(type ==2){
					NDir dir = (NDir) dirProcess.doView(id);
					if(dir != null){
						dir.setName(name);
						dirProcess.doUpdate(dir);
					}else{
						response.setStatus(404);
					}
				}
				
			}
		} catch (Exception e) {
			response.setStatus(500);
		}
	}
	
	public void doDownload() throws Exception {
		NFile file = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		NFileProcess process = new NFileProcessBean();
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("nFileId");
			
			if(!StringUtil.isBlank(id)){
				file = (NFile) process.doView(id);
				if(file ==null) {
					response.setStatus(404);
					throw new Exception("找不到文件！");
				}
				
				FileUtils.doFileDownload(request, response, file);
				
			}
			
		} catch (Exception e) {
			response.setStatus(500);
		}
		
	}
	
	
	public void doLogin(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		PrintWriter writer = getPrintWriter(request, response);
		try {
			HttpSession session = request.getSession();
			
			ParamsTable params = getParams();
			String username = params.getParameterAsString("username");
			String domainname = params.getParameterAsString("domainname");
			String domainId = null;
			
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain = domainProcess.getDomainByDomainName(domainname);
			if(domain!=null) domainId = domain.getId();
			
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = process.login(username,domainId);
			if (user != null && user.getStatus() == 1) {
				WebUser webUser = new WebUser(user);
				UserVO vo = (UserVO) user.clone();
				vo.setLoginpwd(null);
				process.doUpdateWithCache(vo);

				session.setAttribute(Web.SESSION_ATTRIBUTE_DOMAIN, webUser.getDomainid());
				
				String language = MultiLanguageProperty.getName(2);
				session.setAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE, language);
				session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);
				NUser.login(ServletActionContext.getRequest(), webUser);
				writer.println(200);
			}else{
				response.setStatus(404);
				writer.println(-1);
			}
			
		} catch (Exception e) {
			writer.println(-200);
			response.setStatus(500);
			e.printStackTrace();
		}finally{
			if(writer !=null ){
				writer.flush();
				writer.close();
			}
		}
		
	}
	
	public void isKmUser(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		PrintWriter writer = getPrintWriter(request, response);
		try {
			
			ParamsTable params = getParams();
			String username = params.getParameterAsString("username");
			String domainname = params.getParameterAsString("domainname");
			String domainId = null;
			
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain = domainProcess.getDomainByDomainName(domainname);
			if(domain!=null) domainId = domain.getId();
			
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = process.login(username,domainId);
			if (user != null && user.getStatus() == 1) {
				WebUser webUser = new WebUser(user);
				PropertyUtil.reload("km");
				if(PropertyUtil.get(KmConfig.ENABLE).equals("true")){
					NUser nUser = new NUser(webUser);
					Collection<NRole> roles = nUser.getKmRoles();
					if(roles!=null && !roles.isEmpty()){
						return;
					}
				}
			}
			response.setStatus(404);
			
		} catch (Exception e) {
			response.setStatus(500);
			e.printStackTrace();
		}finally{
			if(writer !=null ){
				writer.flush();
				writer.close();
			}
		}
		
	}
	
	public void doQuerySynchronizeTasks() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter writer = getPrintWriter(request, response);
		LogsProcess process = new LogsProcessBean();
		try {
			NUser user = getUser();
			ParamsTable params = getParams();
			long lastSynchTime = params.getParameterAsLong("lastSynchTime");//上次同步时间
			int diskType = params.getParameterAsInteger("diskType");
			
			if(lastSynchTime >0){
				String tasks = process.doQuerySynchronizeTasks(lastSynchTime, diskType, user);
				if(tasks != null){
					writer.println(tasks);
				}
			}
			
		} catch (Exception e) {
			response.setStatus(500);
			writer.println(e.getMessage());
			e.printStackTrace();
		}finally{
			if(writer !=null ){
				writer.flush();
				writer.close();
			}
		}
		
	}
	
	public void doGetRelativePath() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter writer = getPrintWriter(request, response);
		
		NDirProcess dirProcess = new NDirProcessBean();
		try {
			ParamsTable params = getParams();
			String nDirId = params.getParameterAsString("nDirId");
			String name = params.getParameterAsString("name");
			
			if(StringUtil.isBlank(nDirId)) throw new Exception("传入的目录id参数为空！");
			Stack<NDir> dirs = (Stack<NDir>) dirProcess.getSuperiorNDirList(nDirId);
			dirs.pop();
			StringBuffer path = new StringBuffer();
			while(!dirs.isEmpty()){
				NDir dir = dirs.pop();
				path.append(dir.getName()+File.separator);
			}
			path.append(name);
			
			writer.println(path.toString());
		} catch (Exception e) {
			response.setStatus(500);
			writer.println(e.getMessage());
			e.printStackTrace();
		}finally{
			if(writer !=null ){
				writer.flush();
				writer.close();
			}
		}
	}
	
	
	public NUser getUser() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();

		NUser user = null;

		if (session != null)
			user = (NUser) session.getAttribute(NUser.SESSION_ATTRIBUTE_FRONT_USER);

		return user;
	}
	
	public ParamsTable getParams() {
		ParamsTable pm = ParamsTable.convertHTTP(ServletActionContext.getRequest());

		// put the page line count id to parameters table.
		if (pm.getParameter("_pagelines") == null)
			pm.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);

		return pm;
	}
	
	private PrintWriter getPrintWriter(HttpServletRequest request,
			HttpServletResponse response){
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html; charset=utf-8");
			return response.getWriter();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
