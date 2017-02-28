package cn.myapps.km.baike.upload.action;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.util.Log;

import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.disk.action.NFileAction;
import cn.myapps.km.disk.ejb.IFile;
import cn.myapps.km.disk.ejb.NDisk;
import cn.myapps.km.disk.ejb.NDiskProcess;
import cn.myapps.km.disk.ejb.NDiskProcessBean;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.disk.ejb.NFileProcess;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

public class UploadFileAction extends NFileAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2206585545421571770L;
	
	private String jsonFileInfo;
	
	

	public String getJsonFileInfo() {
		return jsonFileInfo;
	}


	public void setJsonFileInfo(String jsonFileInfo) {
		this.jsonFileInfo = jsonFileInfo;
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
				String jsonFileInfo = "";
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
					jsonFileInfo += ",{\"id\":\"" + file.getId() + "\",\"name\":\"" + file.getName() + "\"}";
					((NFileProcess)getProcess()).createFile(file, params, getUser());
				}
				if (jsonFileInfo.trim().length()>0) {
					jsonFileInfo = jsonFileInfo.substring(1, jsonFileInfo.length());
				}
				jsonFileInfo = "[" + jsonFileInfo + "]";
				setJsonFileInfo(jsonFileInfo);
				System.out.println(jsonFileInfo);
			}
			
			addActionMessage("保存成功");
			
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}
	
}
