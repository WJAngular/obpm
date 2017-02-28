package cn.myapps.km.permission.action;


import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.permission.ejb.ManagePermission;
import cn.myapps.km.permission.ejb.ManagePermissionProcess;
import cn.myapps.km.permission.ejb.ManagePermissionProcessBean;
import cn.myapps.km.permission.ejb.PermissionHelper;
import cn.myapps.km.util.StringUtil;

public class ManagePermissionAction extends AbstractRunTimeAction<ManagePermission> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1102905049882273055L;
	/**
	 * 资源类型
	 */
	private String resourceType;
	/**
	 * 资源ID
	 */
	private String resource;
	

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public ManagePermissionAction() {
		super();
		setContent(new ManagePermission());
		
	}
	
	public String doList() {
		ManagePermissionProcess process = (ManagePermissionProcess)getProcess();
		try {
			DataPackage<ManagePermission> datas = process.doQueryByResource(getResource());
			setDatas(datas);
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
		}
		return SUCCESS;
	}
	
	public String doSave() {
		ManagePermissionProcess process = (ManagePermissionProcess)getProcess();
		try {
			ManagePermission permission = (ManagePermission) getContent();
			if(StringUtil.isBlank(permission.getId())){
				process.doCreate(permission);
			}else{
				process.doUpdate(permission);
			}
			PermissionHelper.cleanMnangePermission();
			addActionMessage("保存成功！");
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}

	public String doNew() {
		try {
			ManagePermission permission  =  new ManagePermission();
			permission.setResourceType(resourceType);
			permission.setResource(resource);
			permission.setScope(ManagePermission.SCOPE_USER);
			permission.setDomainId(getUser().getDomainid());
			setContent(permission);
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
		}
		return SUCCESS;
	}
	
	public String doDelete(){
		ManagePermissionProcess process = (ManagePermissionProcess)getProcess();
		try {
			for (int i = 0; i < _selects.length; i++) {
				if(StringUtil.isBlank(_selects[i])) continue;
					process.doRemove(_selects[i]);
			}
			PermissionHelper.cleanMnangePermission();
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
				ManagePermission permission = (ManagePermission) getProcess().doView(id);
				setContent(permission);
			}
		} catch (Exception e) {
			addFieldError("1", e.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}


	@Override
	public NRunTimeProcess<ManagePermission> getProcess() {
		// TODO Auto-generated method stub
		return new ManagePermissionProcessBean();
	}

}
