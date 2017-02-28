/**
 * DomainService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public interface DomainService extends java.rmi.Remote {
	public java.lang.Object[] searchDomainsByDomainAdmin(
			java.lang.String domainAdminId) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DomainServiceFault;

	public cn.myapps.webservice.model.SimpleDomain searchDomainByName(
			java.lang.String name) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DomainServiceFault;
}
