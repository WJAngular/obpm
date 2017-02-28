/**
 * ApplicationServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public interface ApplicationServiceService extends javax.xml.rpc.Service {
	public java.lang.String getApplicationServiceAddress();

	public cn.myapps.webservice.client.ApplicationService getApplicationService()
			throws javax.xml.rpc.ServiceException;

	public cn.myapps.webservice.client.ApplicationService getApplicationService(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
