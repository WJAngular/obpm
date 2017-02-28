/**
 * ApplicationService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

import java.util.Collection;

import cn.myapps.webservice.model.SimpleApplication;

public interface ApplicationService extends java.rmi.Remote {
	public Collection<SimpleApplication> searchApplicationsByName(java.lang.String name)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.ApplicationServiceFault;

	public cn.myapps.webservice.model.SimpleApplication searchApplicationByName(
			java.lang.String name) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.ApplicationServiceFault;

	public Collection<SimpleApplication> searchApplicationsByFilter(
			java.util.HashMap<?, ?> parameters)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.ApplicationServiceFault;

	public Collection<SimpleApplication> searchApplicationsByDomainAdmin(
			java.lang.String domainAdminId) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.ApplicationServiceFault;

	public Collection<SimpleApplication> searchApplicationsByDeveloper(
			java.lang.String developerId) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.ApplicationServiceFault;

	public boolean addApplication(java.lang.String userAccount,
			java.lang.String domainName, java.lang.String applicationId)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.ApplicationServiceFault;
}
