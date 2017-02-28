package cn.myapps.km.permission.ejb;

import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.permission.dao.PermissionDAO;
import cn.myapps.km.util.Sequence;
import cn.myapps.km.util.StringUtil;

public class PermissionProcessBean extends AbstractBaseProcessBean<Permission>
		implements PermissionProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161963512904713460L;

	@Override
	public NRuntimeDAO getDAO() throws Exception {
		return DaoManager.getPermissionDAO(getConnection());
	}

	@Override
	public void doCreate(NObject no) throws Exception {
		Permission permission = (Permission) no;
		DirAccessProcess dProcess = new DirAccessProcessBean();
		FileAccessProcess fProcess = new FileAccessProcessBean();
		
		try {
			beginTransaction();
			
			String id = permission.getId();
			if(StringUtil.isBlank(id)){
				id = Sequence.getSequence();
				permission.setId(id);
			}
			
			if(Permission.FILE_TYPE_DIR == permission.getFileType()){
				dProcess.doCreateByPermission(permission);
			}else if(Permission.FILE_TYPE_FILE == permission.getFileType()){
				fProcess.doCreateByPermission(permission);
			}
			
			getDAO().create(permission);
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public void doUpdate(NObject no) throws Exception {
		Permission permission = (Permission) no;
		DirAccessProcess dProcess = new DirAccessProcessBean();
		FileAccessProcess fProcess = new FileAccessProcessBean();
		
		try {
			beginTransaction();
			
			if(Permission.FILE_TYPE_DIR == permission.getFileType()){
				dProcess.doRemoveByPermission(permission.getId());
				dProcess.doCreateByPermission(permission);
			}else if(Permission.FILE_TYPE_FILE == permission.getFileType()){
				fProcess.doRemoveByPermission(permission.getId());
				fProcess.doCreateByPermission(permission);
			}
			
			getDAO().update(permission);
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public void doRemove(String pk) throws Exception {
		DirAccessProcess dProcess = new DirAccessProcessBean();
		FileAccessProcess fProcess = new FileAccessProcessBean();
		
		try {
			beginTransaction();
			
			Permission permission = (Permission) doView(pk);
			
			if (permission == null) return;
			
			if(Permission.FILE_TYPE_DIR == permission.getFileType()){
				dProcess.doRemoveByPermission(permission.getId());
			}else if(Permission.FILE_TYPE_FILE == permission.getFileType()){
				fProcess.doRemoveByPermission(permission.getId());
			}
			
			getDAO().remove(pk);
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public NObject doView(String id) throws Exception {
		
		return getDAO().find(id);
	}

	public DataPackage<Permission> doQueryByFile(String fileId) throws Exception {
		// TODO Auto-generated method stub
		return ((PermissionDAO)getDAO()).queryByFile(fileId);
	}

	public DataPackage<Permission> doQuery(String fileId, ParamsTable params) throws Exception {
		String scope = params.getParameterAsString("_scope");
		String ownerName = params.getParameterAsString("_ownerName");
		Integer readOnly = params.getParameterAsInteger("_readOnly");
		Integer download = params.getParameterAsInteger("_download");
		if(readOnly == null){
			readOnly = 0;
		}
		if(download == null){
			download = 0;
		}
		return  ((PermissionDAO)getDAO()).query(fileId, scope, ownerName, readOnly, download);
	}

	/**
	 * 
	 * @param fileId
	 * 			文件ID
	 * @param ownerId
	 * 			权限拥有者ID（用户ID 或 角色ID）
	 * @return
	 * @throws Exception
	 */
	public Permission doQueryByFileAndOwner(String fileId, String ownerId)
			throws Exception {
		// TODO Auto-generated method stub
		 return ((PermissionDAO)getDAO()).findByFileAndOwner(fileId, ownerId);
	}

}
