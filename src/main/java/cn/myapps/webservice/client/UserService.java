/**
 * UserService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public interface UserService extends java.rmi.Remote {
	public cn.myapps.webservice.model.SimpleUser createUser(
			cn.myapps.webservice.model.SimpleUser user)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault;

	public cn.myapps.webservice.model.SimpleUser getUser(java.lang.String pk)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault;

	public cn.myapps.webservice.model.SimpleUser validateUser(
			java.lang.String domainName, java.lang.String userAccount,
			java.lang.String userPassword, int userType)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault;

	public void updateUser(cn.myapps.webservice.model.SimpleUser user)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault;

	public void deleteUser(java.lang.String pk)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault;

	public void deleteUser(cn.myapps.webservice.model.SimpleUser user)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault;

	public void setRoleSet(cn.myapps.webservice.model.SimpleUser user,
			java.lang.String[] roles) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault;

	public void setDepartmentSet(cn.myapps.webservice.model.SimpleUser user,
			java.lang.String[] deps) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault;
}
