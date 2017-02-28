/**
 * DocumentService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public interface DocumentService extends java.rmi.Remote {
	public void createDocumentByGuest(java.lang.String formName,
			java.util.HashMap<?, ?> parameters, java.lang.String applicationId)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DocumentServiceFault;

	public void updateDocumentByGuest(java.lang.String documentId,
			java.util.HashMap<?, ?> parameters, java.lang.String applicationId)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DocumentServiceFault;

	public void createDocumentByDomainUser(java.lang.String formName,
			java.util.HashMap<?, ?> parameters, java.lang.String domainUserId,
			java.lang.String applicationId) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DocumentServiceFault;

	public void updateDocumentByDomainUser(java.lang.String documentId,
			java.util.HashMap<?, ?> parameters, java.lang.String domainUserId,
			java.lang.String applicationId) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DocumentServiceFault;

	public java.lang.Object[] searchDocumentsByFilter(
			java.lang.String formName, java.util.HashMap<?, ?> parameters,
			java.lang.String applicationId, java.lang.String domainId)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DocumentServiceFault;

	public java.lang.Object[] searchDocumentsByFilter(
			java.lang.String formName, java.util.HashMap<?, ?> parameters,
			java.lang.String applicationId) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DocumentServiceFault;

	public cn.myapps.webservice.model.SimpleDocument searchDocumentByFilter(
			java.lang.String formName, java.util.HashMap<?, ?> parameters,
			java.lang.String applicationId, java.lang.String domainId)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.DocumentServiceFault;
}
