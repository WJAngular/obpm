package cn.myapps.core.permission.action;

import java.util.Collection;
import java.util.Iterator;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Environment;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.permission.ejb.PermissionProcessBean;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.privilege.operation.ejb.OperationProcess;
import cn.myapps.core.privilege.operation.ejb.OperationVO;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;


/**
 * @SuppressWarnings 此类不能使用泛型
 * @author Administrator
 * 
 */
public class PermissionBatchGrantAction extends ActionSupport implements Action {

	private static final long serialVersionUID = -304644165280079279L;

	private String applicationid;

	private Collection _roles;

	private Collection _grantedresources;

	private Collection _grantedoperations;

	private Collection _allresources;

	public Collection get_allresources() {
		return _allresources;
	}

	public void set_allresources(Collection _allresources) {
		this._allresources = _allresources;
	}

	public Collection get_alloperations() {
		return _alloperations;
	}

	public void set_alloperations(Collection _alloperations) {
		this._alloperations = _alloperations;
	}

	private Collection _alloperations;

	public Collection get_roles() {
		return _roles;
	}

	public void set_roles(Collection _roles) {
		this._roles = _roles;
	}

	public Collection get_grantedresources() {
		return _grantedresources;
	}

	public void set_grantedresources(Collection _resources) {
		this._grantedresources = _resources;
	}

	public Collection get_grantedoperations() {
		return _grantedoperations;
	}

	public void set_grantedoperations(Collection _operations) {
		this._grantedoperations = _operations;
	}

	public String getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	public String doBatchGrant() throws Exception {
		try {
			RoleProcess roleProcess = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			OperationProcess operationProcess = (OperationProcess) ProcessFactory
					.createProcess(OperationProcess.class);

			PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory
					.createProcess(PermissionProcess.class);

			ResourceProcess resourceProcess = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);

			try {
				if (_roles != null)
					for (Iterator iterator = _roles.iterator(); iterator
							.hasNext();) {
						String roleId = (String) iterator.next();
//						RoleVO roleVO = (RoleVO) roleProcess.doView(roleId);

						// 清空角色所有权限
						Collection<PermissionVO> datas = permissionProcess
								.doQueryByRole(roleId);
						if (datas != null) {
							for (Iterator<PermissionVO> iter = datas.iterator(); iter
									.hasNext();) {
								PermissionVO permission = iter.next();
								permissionProcess.doRemove(permission);

								PersistenceUtils.currentSession().flush();
								PersistenceUtils.currentSession().clear();
							}
						}

						// 设置菜单
//						ParamsTable params = new ParamsTable();
//						params.setParameter("application", applicationid);
//						Collection<ResourceVO> rs = (Collection<ResourceVO>) resourceProcess
//								.doQuery(params).datas;

						if (_allresources != null)
							for (Iterator iter = _allresources.iterator(); iter
									.hasNext();) {
								String resourceId = (String) iter.next();

//								ResourceVO resourceVO = (ResourceVO) resourceProcess
//										.doView(resourceId);
//								
//								OperationVO operationVO = operationProcess
//										.doViewByResource(resourceId, resourceId,
//												ResVO.MENU_TYPE);
								if(resourcePermissionType(resourceId)){
									createPermission4Resource(roleId,resourceId);
								}

								PersistenceUtils.currentSession().flush();
								PersistenceUtils.currentSession().clear();
							}

						// 设置视图、表单权限
						if (_alloperations != null)
							for (Iterator iter = _alloperations.iterator(); iter
									.hasNext();) {
								String operationTxt = (String) iter.next();
								// ResourceVO resourceVO =
								// (ResourceVO)resourceProcess.doView(operationId);

								String[] tmp = operationTxt.split("@");
								
								OperationVO operationVO = operationProcess
										.doViewByResource(tmp[2], tmp[1],
												tmp[0].equals("FORM") ? ResVO.FORM_TYPE
														: ResVO.VIEW_TYPE);
								if(operationPermissionType(tmp[2])){
									
								createPermission(
										tmp[1],
										tmp[2],
										operationVO.getName(),
										tmp[0].equals("FORM") ? ResVO.FORM_TYPE
												: ResVO.VIEW_TYPE,
										operationPermissionType(tmp[2]) ? PermissionVO.TYPE_ALLOW
												: PermissionVO.TYPE_FORBID,
										roleId,operationVO);
								}
								PersistenceUtils.currentSession().flush();
								PersistenceUtils.currentSession().clear();
								
							}
					}
				
				Environment.cleanPermissionMap();
				this.addActionMessage("{*[Save_Success]*}");
			} catch (Exception e) {
				addFieldError("doSave", e.getMessage());
				e.printStackTrace();
				throw e;
			}

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return INPUT;
		}
	}

	private void createPermission(String resourceId, String operationId, String resourceName,
			Integer resourceType, int permissionType, String roleId,OperationVO operationVO)
			throws Exception {
		PermissionProcess permissionProcess = new PermissionProcessBean();//(PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
		PermissionVO pv = new PermissionVO();
		pv.setId(Sequence.getSequence());
		pv.setResId(resourceId);
		pv.setResName(resourceName);
		pv.setResType(resourceType);
		pv.setOperationId(operationId);
		 pv.setOperationCode(operationVO.getCode());
		// 赋予禁止或者允许权限
		pv.setType(PermissionVO.TYPE_ALLOW);
		pv.setApplicationid(applicationid);
		pv.setRoleId(roleId);

		try {
			PersistenceUtils.beginTransaction();
			permissionProcess.doCreate(pv);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
		}
	}
	
	private void createPermission4Resource(String roleId,String resourceId) throws Exception {
		PermissionProcess permissionProcess = new PermissionProcessBean();//(PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
		PermissionVO pv = new PermissionVO();
		pv.setId(Sequence.getSequence());
		pv.setRoleId(roleId);
//		pv.setResourceId(resourceId);
		pv.setResId(resourceId);
		pv.setOperationId(resourceId);
		pv.setResName("");
		pv.setResType(ResVO.MENU_TYPE);
		 pv.setOperationCode(OperationVO.MENU_INVISIBLE);
		// 赋予禁止或者允许权限
		pv.setType(PermissionVO.TYPE_ALLOW);
		pv.setApplicationid(applicationid);
		try {
			PersistenceUtils.beginTransaction();
			permissionProcess.doCreate(pv);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
		}
	}

	private void createPermissionByResource(ResourceVO resourceVO,
			int permissionType, RoleVO role) throws Exception {
		PermissionProcess permissionProcess = new PermissionProcessBean();//(PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
		PermissionVO pv = new PermissionVO();
		pv.setId(Sequence.getSequence());
		pv.setResourceId(resourceVO.getId());
		pv.setResId(resourceVO.getId());
		pv.setResName(resourceVO.getDescription());
		pv.setResType(ResVO.MENU_TYPE);
		pv.setOperationId(resourceVO.getId());
		// pv.setOperationCode(operationVO.getCode());
		// 赋予禁止或者允许权限
		pv.setType(permissionType);

		pv.setApplicationid(applicationid);
		pv.setRoleId(role.getId());
		role.getPermission().add(pv);

		try {
			PersistenceUtils.beginTransaction();
			permissionProcess.doCreate(pv);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
		}
	}

	private boolean resourcePermissionType(String resourceId) {

		return _grantedresources != null
				&& _grantedresources.contains(resourceId);
	}

	private boolean operationPermissionType(String operationId) {

		boolean flag = false;
		if (_grantedoperations != null)
			for (Iterator iterator = _grantedoperations.iterator(); iterator
					.hasNext();) {
				String go = (String) iterator.next();
				if (go.indexOf(operationId) >= 0) {
					flag = true;
					break;
				}

			}
		return flag;
	}
}
