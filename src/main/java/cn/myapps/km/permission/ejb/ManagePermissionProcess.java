package cn.myapps.km.permission.ejb;

import java.util.Collection;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;

public interface ManagePermissionProcess extends NRunTimeProcess<ManagePermission> {
	
	public Collection<ManagePermissionItem> doQueryAllPermissionItems() throws Exception;
	
	public DataPackage<ManagePermission> doQueryByResource(String resource) throws Exception;
	

}
