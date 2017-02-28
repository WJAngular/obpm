/**
 * RoleService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public interface RoleService extends java.rmi.Remote {
	public cn.myapps.webservice.model.SimpleRole getRole(java.lang.String pk)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.RoleServiceFault;

	public cn.myapps.webservice.model.SimpleRole createRole(
			cn.myapps.webservice.model.SimpleRole role)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.RoleServiceFault;

	public void updateRole(cn.myapps.webservice.model.SimpleRole role)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.RoleServiceFault;

	public void deleteRole(java.lang.String pk)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.RoleServiceFault;

	public void setPermissionSet(cn.myapps.webservice.model.SimpleRole role,
			java.lang.String[] resources) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.RoleServiceFault;
}
