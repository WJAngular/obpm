package cn.myapps.core.permission.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

/**
 * @SuppressWarnings 此类不能使用泛型
 * @author Administrator
 * 
 */
@SuppressWarnings("unchecked")
public class PermissionAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7968551832827739066L;

	private String[] _selectsResources;

	private List<Object> reslist = new ArrayList<Object>();

	private Collection<ResVO> innerResourceList = new ArrayList<ResVO>();
	
	private String permissionJSON;//回显时用
	
	private String savePermissionJSON;//保存时用
	
	public String getSavePermissionJSON() {
		return savePermissionJSON;
	}

	public void setSavePermissionJSON(String savePermissionJSON) {
		this.savePermissionJSON = savePermissionJSON;
	}
	
	public String getPermissionJSON() {
		return permissionJSON;
	}

	public void setPermissionJSON(String permissionJSON) {
		this.permissionJSON = permissionJSON;
	}

	public String[] get_selectsResources() {
		return _selectsResources;
	}

	public void set_selectsResources(String[] selectsResources) {
		_selectsResources = selectsResources;
	}

	public PermissionAction() throws Exception {
		super(ProcessFactory.createProcess(PermissionProcess.class), new PermissionVO());
	}

	public String doEdit() {
		return SUCCESS;
	}

	public Collection<ResVO> getInnerResourceList() {
		return innerResourceList;
	}

	public void setInnerResourceList(Collection<ResVO> innerResourceList) {
		this.innerResourceList = innerResourceList;
	}

	/**
	 * 列表
	 */
	public String doList() {
		try {
			String roleid = getParams().getParameterAsString("roleid");
			setPermissionJSON(((PermissionProcess)process).getPermissionJSONByRole(roleid));
//			ResProcess rprocess = (ResProcess) ProcessFactory.createProcess(ResProcess.class);
//			this.setDatas(rprocess.doQuery(getParams(), getUser()));
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}

	/**
	 * 保存
	 */
	public String doSave() {
		try {
			if (!StringUtil.isBlank(getSavePermissionJSON())){
				Map<String, Object> permissionMap = JsonUtil.toMap(getSavePermissionJSON());
				((PermissionProcess) process).grantAuth(permissionMap, this.getParams());
			}
			this.addActionMessage("{*[Save_Success]*}");
//			((PermissionProcess) process).grantAuth(_selectsResources, this.getParams());
		}catch (OBPMValidateException e) {
			this.addFieldError("doSave", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}

	/**
	 * 删除
	 */
	public String doDelete() {
		try {
			((PermissionProcess) process).removeAuth(_selectsResources, this.getParams());
		} catch (OBPMValidateException e) {
			this.addFieldError("doDelete", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}

	public List<Object> getReslist() {
		return reslist;
	}

	public void setReslist(List<Object> reslist) {
		this.reslist = reslist;
	}

}
