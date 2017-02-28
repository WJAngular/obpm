package cn.myapps.km.permission.dao;

import java.util.Collection;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.permission.ejb.ManagePermission;
import cn.myapps.km.permission.ejb.ManagePermissionItem;

public interface ManagePermissionDAO extends NRuntimeDAO {
	
	public Collection<ManagePermissionItem> queryAllPermissionItems() throws Exception;
	
	public DataPackage<ManagePermission> queryByResource(String resource) throws Exception;
}
