package cn.myapps.km.permission.ejb;

import java.util.Date;

import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.permission.dao.FileAccessDAO;
import cn.myapps.km.util.Sequence;
import cn.myapps.km.util.StringUtil;

public class FileAccessProcessBean extends AbstractBaseProcessBean<FileAccess>
		implements FileAccessProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1113281142718529883L;

	
	public NRuntimeDAO getDAO() throws Exception {
		return DaoManager.getFileAccessDAO(getConnection());
	}

	
	public void doCreate(NObject no) throws Exception {
		FileAccess fa = (FileAccess)no;
		try {
			beginTransaction();
			getDAO().create(fa);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}

	
	public void doUpdate(NObject no) throws Exception {
		FileAccess fa = (FileAccess)no;
		try {
			beginTransaction();
			getDAO().update(fa);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}

	
	public void doRemove(String pk) throws Exception {
		try {
			beginTransaction();
			getDAO().remove(pk);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}

	
	public NObject doView(String id) throws Exception {
		try {
			NObject no = getDAO().find(id);
			return no;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see cn.myapps.km.permission.ejb.FileAccessProcess#findByOwner(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public FileAccess findByOwner(String fileId, String ownerId, String scope)
			throws Exception {
		return ((FileAccessDAO)getDAO()).findByOwner(fileId, ownerId, scope);
	}

	
	public boolean checkPermission(String fileId, String scope, String ownerId,
			int permissionType) throws Exception {
		
		try {
			FileAccess acc = this.findByOwner(fileId, ownerId, scope);
			if (acc == null)
				return false;
			
			if(acc.getStartDate() !=null && acc.getEndDate() !=null){//授权时效判断
				if(acc.getStartDate().before(new Date()) && acc.getEndDate().after(new Date())){
					
				}else{
					return false;
				}
			}
			switch (permissionType) {
			case PermissionHelper.PERMISSION_TYPE_READ:
				if(FileAccess.READ_MODE_TRUE == acc.getReadMode()) return true;
				break;
			case PermissionHelper.PERMISSION_TYPE_WRITE:
				if(FileAccess.WRITE_MODE_TRUE == acc.getWriteMode()) return true;
				break;
			case PermissionHelper.PERMISSION_TYPE_DOWNLOAD:
				if(FileAccess.DOWNLOAD_MODE_TRUE == acc.getDownloadMode()) return true;
				break;
			case PermissionHelper.PERMISSION_TYPE_PRINT:
				if(FileAccess.PRINT_MODE_TRUE == acc.getPrintMode()) return true;
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return false;
	}
	
	/**
	 * @param permission
	 * @throws Exception
	 */
	public void doCreateByPermission(Permission permission) throws Exception {
		
		try {
			beginTransaction();
			//String[] ownerIds = permission.getOwnerIds().split(";");
			//for (int i = 0; i < ownerIds.length; i++) {
				String ownerId = permission.getOwnerIds();
				if(StringUtil.isBlank(ownerId)) return;
				FileAccess access = new FileAccess();
				access.setId(Sequence.getSequence());
				access.setFileId(permission.getFileId());
				access.setScope(permission.getScope());
				access.setOwnerId(ownerId);
				access.setStartDate(permission.getStartDate());
				access.setEndDate(permission.getEndDate());
				access.setReadMode(permission.getReadMode());
				access.setDownloadMode(permission.getDownloadMode());
				access.setWriteMode(permission.getWriteMode());
				access.setPrintMode(permission.getPrintMode());
				access.setPermissionId(permission.getId());
				
				doCreate(access);
			//}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @param permissionId
	 * @throws Exception
	 */
	public void doRemoveByPermission(String permissionId) throws Exception {
		((FileAccessDAO)getDAO()).removeByPermission(permissionId);
	}
	
	

}
