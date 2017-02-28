package cn.myapps.km.permission.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.log.ejb.LogsProcess;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.permission.ejb.IFileAccess;
import cn.myapps.km.permission.ejb.Permission;
import cn.myapps.km.permission.ejb.PermissionProcess;
import cn.myapps.km.permission.ejb.PermissionProcessBean;
import cn.myapps.km.util.StringUtil;

public class PermissionAction extends AbstractRunTimeAction<Permission> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1696364935840444962L;
	
	String _fileId;
	
	int _fileType;
	
	String isSuccessSave;
	
	private String startDate;
	private String endDate;
	private String permissionId;
	

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String get_fileId() {
		return _fileId;
	}

	public void set_fileId(String _fileId) {
		this._fileId = _fileId;
	}

	public int get_fileType() {
		return _fileType;
	}

	public void set_fileType(int _fileType) {
		this._fileType = _fileType;
	}

	public String getIsSuccessSave() {
		return isSuccessSave;
	}

	public void setIsSuccessSave(String isSuccessSave) {
		this.isSuccessSave = isSuccessSave;
	}

	public PermissionAction() {
		super();
		setContent(new Permission());
		
	}
	
	public String doList() {
		try {
			PermissionProcess process = (PermissionProcess)getProcess();
			DataPackage<Permission> datas = process.doQueryByFile(get_fileId());
			setDatas(datas);
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
		}
		return SUCCESS;
	}
	
	public String doSave() {
		try {
			Permission permission = (Permission) getContent();
			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(!StringUtil.isBlank(getStartDate())){
				permission.setStartDate(format.parse(getStartDate()));
			}
			if(!StringUtil.isBlank(getEndDate())){
				permission.setEndDate(format.parse(getEndDate()));
			}
			
			if(permission.getReadMode()==0){
				permission.setReadMode(IFileAccess.READ_MODE_FALSE);
			}
			if(permission.getDownloadMode()==0){
				permission.setDownloadMode(IFileAccess.DOWNLOAD_MODE_FALSE);
			}
			if(permission.getWriteMode()==0){
				permission.setWriteMode(IFileAccess.WRITE_MODE_FALSE);
			}
			
			String scope = permission.getScope();
			String permissionId = permission.getId();
			if (scope.equals("user") || scope.equals("role") || scope.equals("dept")) {
				String[] ownerIds = permission.getOwnerIds().split(";");
				String[] ownerNames = permission.getOwnerNames().split(";");
				for (int i=0; i<ownerIds.length; i++) {
					permission.setOwnerIds(ownerIds[i]);
					if(scope.equals("user")){
						permission.setUserId(ownerIds[i]);
					}
					if(scope.equals("role")){
						permission.setRoleId(ownerIds[i]);
					}
					if(scope.equals("dept")){
						permission.setDeptId(ownerIds[i]);
					}
					permission.setOwnerNames(ownerNames[i]);
					if(StringUtil.isBlank(permissionId)){
						permission.setId(permissionId);
						Permission newPermission = ((PermissionProcess)getProcess()).doQueryByFileAndOwner(permission.getFileId(), permission.getOwnerIds());
						if (newPermission==null) {
							getProcess().doCreate(permission);
						} else {
							
							if (permission.getReadMode() > newPermission.getReadMode() || permission.getDownloadMode() > newPermission.getDownloadMode()) {
								if (permission.getReadMode() > newPermission.getReadMode()) {
									newPermission.setReadMode(permission.getReadMode());
								}
								
								if (permission.getDownloadMode() > newPermission.getDownloadMode()) {
									newPermission.setDownloadMode(permission.getDownloadMode());
								}
								
								getProcess().doUpdate(newPermission);
							}
						}
					}else {
						getProcess().doUpdate(permission);
					}
				}
			}else if(scope.equals("deptAndrole")){
				String[] deptIds = permission.getDeptId().split(";");
				String[] roleIds = permission.getRoleId().split(";");
				String[] ownerNames = permission.getOwnerNames().split(";");
				for (int i=0; i<deptIds.length; i++) {
					for (int j=0; j<roleIds.length; j++) {
						permission.setOwnerIds(deptIds[i]+";"+roleIds[j]);
						permission.setDeptId(deptIds[i]);
						permission.setRoleId(roleIds[j]);
						permission.setOwnerNames(ownerNames[i]+";"+ownerNames[deptIds.length+j]);
						if(StringUtil.isBlank(permissionId)) {
							permission.setId(permissionId);
							Permission newPermission = ((PermissionProcess)getProcess()).doQueryByFileAndOwner(permission.getFileId(), permission.getOwnerIds());
							if (newPermission==null) {
								getProcess().doCreate(permission);
							}else {
								
								if (permission.getReadMode() > newPermission.getReadMode() || permission.getDownloadMode() > newPermission.getDownloadMode()) {
									if (permission.getReadMode() > newPermission.getReadMode()) {
										newPermission.setReadMode(permission.getReadMode());
									}
									
									if (permission.getDownloadMode() > newPermission.getDownloadMode()) {
										newPermission.setDownloadMode(permission.getDownloadMode());
									}
									
									getProcess().doUpdate(newPermission);
								}
							}
						}else {
							getProcess().doUpdate(permission);
						}
					}
				}
			}
			
			set_fileId(permission.getFileId());
			set_fileType(permission.getFileType());
			
			isSuccessSave = "true";
			addActionMessage("保存成功！");
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			return INPUT;
		}
		return SUCCESS;

	}

	public String doNew() {
		try {
			Permission permission = new Permission();
			permission.setFileType(get_fileType());
			permission.setFileId(get_fileId());
			permission.setScope(IFileAccess.SCOPE_USER);
			setContent(permission);
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
		}
		return SUCCESS;
	}
	
	public String doDelete(){
		try {
			for (int i = 0; i < _selects.length; i++) {
				if(StringUtil.isBlank(_selects[i])) continue;
				getProcess().doRemove(_selects[i]);
			}
			addActionMessage("删除成功！");
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
		}
		return SUCCESS;
	}
	
	public String doRemove(){
		try {
			getProcess().doRemove(permissionId);
			
			addActionMessage("删除成功！");
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
		}
		return SUCCESS;
	}
	
	public String doEdit(){
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			if(!StringUtil.isBlank(id)){
				Permission permission = (Permission) getProcess().doView(id);
				if(permission !=null){
					if(permission.getReadMode()==IFileAccess.READ_MODE_FALSE){
						permission.setReadMode(0);
					}
					if(permission.getDownloadMode()==IFileAccess.DOWNLOAD_MODE_FALSE){
						permission.setDownloadMode(0);
					}
					if(permission.getWriteMode()==IFileAccess.WRITE_MODE_FALSE){
						permission.setWriteMode(0);
					}
				}
				setContent(permission);
			}
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}


	public String doQuery() throws Exception{
		DataPackage<Permission> datas = ((PermissionProcess)getProcess()).doQuery(_fileId, getParams());
		setDatas(datas);
		return SUCCESS;
	}
	
	@Override
	public NRunTimeProcess<Permission> getProcess() {
		// TODO Auto-generated method stub
		return new PermissionProcessBean();
	}

}
