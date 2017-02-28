package cn.myapps.km.permission.ejb;

import java.util.Collection;

import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.permission.dao.ManagePermissionDAO;
import cn.myapps.km.util.Sequence;
import cn.myapps.km.util.StringUtil;

public class ManagePermissionProcessBean extends AbstractBaseProcessBean<ManagePermission>
		implements ManagePermissionProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161963512904713460L;

	public NRuntimeDAO getDAO() throws Exception {
		return DaoManager.getManagePermissionDAO(getConnection());
	}

	public void doCreate(NObject no) throws Exception {
		ManagePermission permission = (ManagePermission) no;
		
		try {
			beginTransaction();
			
			String id = permission.getId();
			if(StringUtil.isBlank(id)){
				id = Sequence.getSequence();
				permission.setId(id);
			}
			
			getDAO().create(permission);
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	public void doUpdate(NObject no) throws Exception {
		ManagePermission permission = (ManagePermission) no;
		
		try {
			beginTransaction();
			
			getDAO().update(permission);
			
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
		
		return getDAO().find(id);
	}

	public Collection<ManagePermissionItem> doQueryAllPermissionItems() throws Exception {
		
		return ((ManagePermissionDAO)getDAO()).queryAllPermissionItems();
	}

	public DataPackage<ManagePermission> doQueryByResource(String resource)
			throws Exception {
		return ((ManagePermissionDAO)getDAO()).queryByResource(resource);
	}

}
