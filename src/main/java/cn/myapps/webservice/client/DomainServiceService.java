/**
 * DomainServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public interface DomainServiceService extends javax.xml.rpc.Service {
	public java.lang.String getDomainServiceAddress();

	public cn.myapps.webservice.client.DomainService getDomainService()
			throws javax.xml.rpc.ServiceException;

	public cn.myapps.webservice.client.DomainService getDomainService(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
