/**
 * DepartmentService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public interface DepartmentService extends java.rmi.Remote {
	public cn.myapps.webservice.model.SimpleDepartment createDepartment(
			cn.myapps.webservice.model.SimpleDepartment dep)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DepartmentServiceFault;

	public void updateDepartment(cn.myapps.webservice.model.SimpleDepartment dep)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DepartmentServiceFault;

	public cn.myapps.webservice.model.SimpleDepartment getDepartment(
			java.lang.String pk) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DepartmentServiceFault;

	public void deleteDepartment(java.lang.String pk)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DepartmentServiceFault;

	public void upateSuperior(cn.myapps.webservice.model.SimpleDepartment dep,
			cn.myapps.webservice.model.SimpleDepartment superDep)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DepartmentServiceFault;
}
