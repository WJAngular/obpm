package cn.myapps.core.permission.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.constans.Environment;
import cn.myapps.core.permission.dao.PermissionDAO;
import cn.myapps.core.privilege.operation.ejb.OperationProcess;
import cn.myapps.core.privilege.operation.ejb.OperationVO;
import cn.myapps.core.privilege.res.ejb.ResProcess;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class PermissionProcessBean extends
		AbstractDesignTimeProcessBean<PermissionVO> implements
		PermissionProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8675975273714552185L;

	protected IDesignTimeDAO<PermissionVO> getDAO() throws Exception {
		return (PermissionDAO) DAOFactory.getDefaultDAO(PermissionVO.class
				.getName());
	}

	public PermissionVO getAppDomain_Cache() throws Exception {
		return null;
	}

	public PermissionVO findByResouceAndUser(String resourceId, String userId)
			throws Exception {
		return ((PermissionDAO) getDAO()).findByResouceAndUser(resourceId,
				userId);
	}

	public PermissionVO getPermissionByName(String name) throws Exception {
		return ((PermissionDAO) getDAO()).getPermissionByName(name);
	}

	public Collection<PermissionVO> doQueryByRoleIdAndResName(String roleId,
			String resName) throws Exception {

		return ((PermissionDAO) getDAO()).queryByRoleIdAndResName(roleId, resName);
	}

	public Collection<PermissionVO> doQueryByRole(String roleId, int resType)
			throws Exception {
		return ((PermissionDAO) getDAO()).queryByRole(roleId, resType);
	}

	public Collection<PermissionVO> doQueryByRole(String roleId)
			throws Exception {
		return ((PermissionDAO) getDAO()).queryByRole(roleId);
	}

	public Collection<PermissionVO> doQueryByRoleAndResource(String roleId,
			String resourceId) throws Exception {
		return ((PermissionDAO) getDAO()).queryByRoleAndResource(roleId, resourceId);
	}

	public Collection<String> getResourceIdsByRole(String roleId)
			throws Exception {

		return ((PermissionDAO) getDAO()).getResourceIdsByRole(roleId);
	}

	public Collection<String> getOperatonIdsByResourceAndRole(
			String resourceid, String roleId) throws Exception {
		return ((PermissionDAO) getDAO()).getOperatonIdsByResourceAndRole(resourceid, roleId);
	}

	/**
	 * 获取角色权限配置, JSON格式： {resourceid: [{'operationid': 操作ID1,
	 * 'resourcetype':资源类型1, 'resourcename':资源名称1, 'allow': 是否允许使用操作},
	 * {'operationid': 操作ID2, 'resourcetype':资源类型2, 'resourcename':资源名称2,
	 * 'allow': 是否允许使用操作}]}
	 * 
	 * @return
	 * @throws Exception
	 */
	/*
	public String getPermissionJSONByRole(String roleid) throws Exception {
		Collection<String> resourceIds = getResourceIdsByRole(roleid);
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		if (resourceIds != null && !resourceIds.isEmpty()) {
			for (Iterator<String> iterator = resourceIds.iterator(); iterator
					.hasNext();) {
				String resourceId = iterator.next();

				Collection<PermissionVO> permissionList = doQueryByRoleAndResource(
						roleid, resourceId);
				if (!StringUtil.isBlank(resourceId)) {
					builder.append("'").append(resourceId).append("':");
					if (permissionList != null && !permissionList.isEmpty()) {
						builder.append("[");
						for (Iterator<PermissionVO> iterator2 = permissionList
								.iterator(); iterator2.hasNext();) {
							PermissionVO permissionVO = iterator2.next();
							builder.append("{");
							builder.append("'resourcename':'").append(
									permissionVO.getResName()).append("',");
							builder.append("'resourcetype':").append(
									permissionVO.getResType()).append(",");
							builder.append("'operationid':'").append(
									permissionVO.getOperationId()).append("',");
							builder.append("'allow':");
							if (PermissionVO.TYPE_ALLOW == permissionVO
									.getType()) {
								builder.append(true);
							} else {
								builder.append(false);
							}
							builder.append("},");
						}
						builder.deleteCharAt(builder.lastIndexOf(","));
						builder.append("]");
					}
					builder.append(",");
				}
			}
			builder.deleteCharAt(builder.lastIndexOf(","));
		}
		builder.append("}");

		return builder.toString();
	}
	*/
	/**
	 * 获取角色权限配置, JSON格式： {resourceid: [{'operationid': 操作ID1,
	 * 'resourcetype':资源类型1, 'resourcename':资源名称1, 'allow': 是否允许使用操作},
	 * {'operationid': 操作ID2, 'resourcetype':资源类型2, 'resourcename':资源名称2,
	 * 'allow': 是否允许使用操作}]}
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getPermissionJSONByRole(String roleid) throws Exception {
		StringBuilder builder = new StringBuilder();
		Collection<PermissionVO> permissionList = doQueryByRole(roleid);
		builder.append("{");
		String resourceId = null;
		for (Iterator<PermissionVO> iterator = permissionList.iterator(); iterator.hasNext();) {
			PermissionVO permissionVO = iterator.next();
			if(resourceId ==null){
				resourceId = permissionVO.getResId();
				builder.append("\"").append(resourceId).append("\":");
				builder.append("[");
			}else if(!resourceId.equals(permissionVO.getResId())){
				builder.deleteCharAt(builder.lastIndexOf(","));
				builder.append("],");
				resourceId = permissionVO.getResId();
				builder.append("\"").append(resourceId).append("\":");
				builder.append("[");
			}
			builder.append("{");
			builder.append("\"resourcename\":\"").append(
					permissionVO.getResName()).append("\",");
			builder.append("\"resourcetype\":").append(
					permissionVO.getResType()).append(",");
			builder.append("\"operationid\":\"").append(
					permissionVO.getOperationId()).append("\",");
			builder.append("\"allow\":");
			if (PermissionVO.TYPE_ALLOW == permissionVO
					.getType()) {
				builder.append(true);
			} else {
				builder.append(false);
			}
			builder.append("},");
			
			
		}
		if(!permissionList.isEmpty()){
			builder.deleteCharAt(builder.lastIndexOf(","));
			builder.append("]");
		}
		//builder.deleteCharAt(builder.lastIndexOf(","));
		builder.append("}");

		return builder.toString();
	}


	public boolean check(Collection<RoleVO> roles, String resId,String operationId,int operationCode,int resType,String applicationId) throws Exception {
		if (roles != null && !roles.isEmpty()) {
			for (Iterator<RoleVO> iterator = roles.iterator(); iterator
					.hasNext();) {
				RoleVO role = iterator.next();
				boolean isAllow = check(role.getId(), resId, operationId,operationCode,resType,applicationId);
				if (isAllow) {
					return true;
				}
			}
			return false;
		}

		return PermissionVO.DEFAULT;
		
	}
	
	public boolean check(String[] roles, String resId,String operationId,int operationCode,int resType,String applicationId,boolean defalut) throws Exception {
		if (roles != null && roles.length > 0) {
			for (int i = 0; i < roles.length; i++) {
				boolean isAllow = check(roles[i], resId, operationId,operationCode,resType,applicationId);
				if (isAllow) {
					return true;
				}
			}
			return false;
		}
		return defalut;
	}
	
	public boolean check(String[] roles, String resId,String operationId,int operationCode,int resType,String applicationId) throws Exception {
		
		return check(roles, resId, operationId, operationCode, resType,applicationId, PermissionVO.DEFAULT);
	}
	
	public boolean check(String roleId, String resId,String operationId,int operationCode,int resType,String applicationId) throws Exception {
		
		if(ResVO.FOLDER_TYPE == resType){
			operationId = getFileOperationId(operationCode,resType,applicationId);
		}
		
		if(Environment.PERMISSIONS.isEmpty()){
			Environment.initPermissionMap();
		}
		if(Environment.PERMISSIONS.get(roleId+"_"+resId+"_"+operationId+"_"+operationCode) !=null){
			return true;
		}

		return false;
	}
	
	private String getFileOperationId(int operationCode,int resType,String applicationId){
		if(Environment.OPERATIONS.isEmpty()){
			Environment.initOperationMap();
		}
		return Environment.OPERATIONS.get(String.valueOf(operationCode)+String.valueOf(resType)+applicationId);
	}


	@SuppressWarnings("unchecked")
	public void grantAuth(Map<String, Object> permissionMap, ParamsTable params1)
			throws Exception {
		OperationProcess operationProcess = (OperationProcess) ProcessFactory
				.createProcess(OperationProcess.class);
		String roleid = params1.getParameterAsString("roleid");
		String applicationid = params1.getParameterAsString("applicationid");
		List<String> roleList = new ArrayList<String>();
		roleList.add(roleid);
		String rolesSelected = params1.getParameterAsString("rolesSelected");
		if (rolesSelected != null) {
			String[] roles = rolesSelected.split(";");
			for (int i = 0; i < roles.length; i++) {
				if (!StringUtil.isBlank(roles[i])) {
					roleList.add(roles[i]);
				}
			}
		}

		try {
			PersistenceUtils.beginTransaction();
			// 清空角色所有权限
//			for (int i = 0; i < roleList.size(); i++) {
//				ParamsTable params = new ParamsTable();
//				params.setParameter("s_role_id", roleList.get(i));
//				DataPackage<PermissionVO> datas = getDAO().query(params);
//				if (datas.rowCount > 0) {
//					for (Iterator<PermissionVO> iter = datas.datas.iterator(); iter
//							.hasNext();) {
//						PermissionVO permission = iter.next();
//						getDAO().remove(permission);
//					}
//				}
//			}

			if (permissionMap != null && !permissionMap.isEmpty()) {
				for (int k = 0; k < roleList.size(); k++) {
					// 重新为角色赋权
					for (Iterator<Entry<String, Object>> iterator = permissionMap
							.entrySet().iterator(); iterator.hasNext();) {
						Entry<String, Object> entry = iterator.next();
						String resourceid = entry.getKey();
						Object[] operationPermissions = (Object[]) entry
								.getValue();

						// 遍历资源选择的操作
						for (int i = 0; i < operationPermissions.length; i++) {
							Map operationPermission = (Map) operationPermissions[i];

							Integer resourcetype = (Integer) operationPermission
									.get("resourcetype");
							String resourcename = (String) operationPermission
									.get("resourcename");
							String operationid = (String) operationPermission
									.get("operationid");
							boolean allow = (Boolean) operationPermission
									.get("allow");

							OperationVO operationVO = operationProcess
									.doViewByResource(operationid, resourceid,
											resourcetype);
							

							
							
							if (operationVO == null || operationVO.getCode() == null) {
								System.out.println("operationVO is null");
							}
							
							
							PermissionVO pv = new PermissionVO();
							pv.setId(Sequence.getSequence());
							pv.setResId(resourceid);
							pv.setResName(resourcename);
							pv.setResType(Integer.valueOf(resourcetype));
							pv.setOperationId(operationVO.getId());
							pv.setOperationCode(operationVO.getCode());
							if (allow) { // 赋予禁止或者允许权限
								pv.setType(PermissionVO.TYPE_ALLOW);
							} else {
								pv.setType(PermissionVO.TYPE_FORBID);
							}

							pv.setApplicationid(applicationid);
							pv.setRoleId(roleList.get(k));
							doCreateOrUpdate(pv);
						}
					}
				}
			}
			Environment.cleanPermissionMap();
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * 创建或更新PermissionVO
	 * @param vo
	 * @throws Exception
	 */
	private void doCreateOrUpdate(PermissionVO vo) throws Exception{
		ParamsTable params = new ParamsTable();
		params.setParameter("s_role_id", vo.getRoleId());
		params.setParameter("s_res_id", vo.getResId());
		params.setParameter("s_operation_id", vo.getOperationId());
		params.setParameter("s_operationcode", vo.getOperationCode());
		DataPackage<PermissionVO> data = getDAO().query(params);
		if(data != null && data.rowCount > 0){
			for(Iterator<PermissionVO> it = data.datas.iterator();it.hasNext();){
				PermissionVO pvo = it.next();
				vo.setId(pvo.getId());
				getDAO().update(vo);
				break;
			}
		}else{
			getDAO().create(vo);
		}
	}
	
	/**
	 * 授权
	 */
	public void grantAuth(String[] _selectsResources, ParamsTable params1)
			throws Exception {
		try {
			ResProcess rprocess = (ResProcess) ProcessFactory
					.createProcess(ResProcess.class);
			OperationProcess operationProcess = (OperationProcess) ProcessFactory
					.createProcess(OperationProcess.class);

			String roleid = params1.getParameterAsString("roleid");
			String applicationid = params1
					.getParameterAsString("applicationid");

			PersistenceUtils.beginTransaction();
			// 清空选中资源的所有权限
			for (int i = 0; i < _selectsResources.length; i++) {
				ParamsTable params = new ParamsTable();
				params.setParameter("s_RES_ID", _selectsResources[i]);
				params.setParameter("s_role_id", roleid);

				DataPackage<PermissionVO> datas = getDAO().query(params);

				if (datas.rowCount > 0) {
					for (Iterator<PermissionVO> iterator = datas.datas
							.iterator(); iterator.hasNext();) {
						PermissionVO permission = iterator.next();
						getDAO().remove(permission);
					}
				}
			}

			// 重新添加选中资源权限
			for (int i = 0; i < _selectsResources.length; i++) {
				String resourceid = _selectsResources[i];
				String operationidstr = params1.getParameterAsString(resourceid
						+ "_selects");
				String type = params1.getParameterAsString(resourceid
						+ "_resourcesType");
				ResVO resourcesVO = (ResVO) rprocess.doView(resourceid);

				String[] operationids = operationidstr.split(";");
				for (int j = 0; j < operationids.length; j++) {
					String operaiontid = operationids[j];
					OperationVO operationVO = (OperationVO) operationProcess
							.doView(operaiontid);

					PermissionVO pv = new PermissionVO();
					pv.setId(Sequence.getSequence());
					pv.setResId(resourceid);
					pv.setResName(resourcesVO.getName());
					pv.setOperationId(operaiontid);
					pv.setOperationCode(operationVO.getCode());

					if (type != null) {
						// 设置允许或禁止
						pv.setType(Integer.parseInt(type));
					}
					pv.setApplicationid(applicationid);
					pv.setRoleId(roleid);
					getDAO().create(pv);
				}
			}

			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * 取消授权
	 */
	public void removeAuth(String[] _selectsResources, ParamsTable params1)
			throws Exception {
		try {
			PersistenceUtils.beginTransaction();

			String roleid = params1.getParameterAsString("roleid");
			RoleProcess roleProcess = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			RoleVO roleVO = (RoleVO) roleProcess.doView(roleid);

			Collection<PermissionVO> permissions = roleVO.getPermission();

			for (int i = 0; i < _selectsResources.length; i++) {
				String resourcesid = _selectsResources[i];
				for (Iterator<PermissionVO> iterator = permissions.iterator(); iterator
						.hasNext();) {
					PermissionVO permission = iterator.next();
					if (permission.getResId() != null
							&& permission.getResId().equals(resourcesid)) {
						getDAO().remove(permission);
					}
				}
			}

			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}
}
