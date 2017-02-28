/**
 * DocumentServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public class DocumentServiceServiceLocator extends org.apache.axis.client.Service implements
		cn.myapps.webservice.client.DocumentServiceService {

	private static final long serialVersionUID = -8329390036941880410L;

	public DocumentServiceServiceLocator() {
	}
	
	public DocumentServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public DocumentServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName)
			throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for DocumentService
	private java.lang.String DocumentService_address = "http://localhost:8080/obpm/services/DocumentService";

	public java.lang.String getDocumentServiceAddress() {
		return DocumentService_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String DocumentServiceWSDDServiceName = "DocumentService";

	public java.lang.String getDocumentServiceWSDDServiceName() {
		return DocumentServiceWSDDServiceName;
	}

	public void setDocumentServiceWSDDServiceName(java.lang.String name) {
		DocumentServiceWSDDServiceName = name;
	}

	public cn.myapps.webservice.client.DocumentService getDocumentService() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(DocumentService_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getDocumentService(endpoint);
	}

	public cn.myapps.webservice.client.DocumentService getDocumentService(String loginno, String password, String domain)
			throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(DocumentService_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getDocumentService(loginno, password, domain, endpoint);
	}

	public cn.myapps.webservice.client.DocumentService getDocumentService(java.net.URL portAddress)
			throws javax.xml.rpc.ServiceException {
		try {
			cn.myapps.webservice.client.DocumentServiceSoapBindingStub _stub = new cn.myapps.webservice.client.DocumentServiceSoapBindingStub(
					portAddress, this);
			_stub.setPortName(getDocumentServiceWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public cn.myapps.webservice.client.DocumentService getDocumentService(String loginno, String password,
			String domain, java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			cn.myapps.webservice.client.DocumentServiceSoapBindingStub _stub = new cn.myapps.webservice.client.DocumentServiceSoapBindingStub(
					portAddress, this);
			_stub.setLoginno(loginno);
			_stub.setPassword(password);
			_stub.setDomain(domain);

			_stub.setPortName(getDocumentServiceWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setDocumentServiceEndpointAddress(java.lang.String address) {
		DocumentService_address = address;
	}

	/**
	 * @SuppressWarnings getPort方法不支持泛型 For the given interface, get the stub
	 *                   implementation. If this service has no port for the
	 *                   given interface, then ServiceException is thrown.
	 */
	@SuppressWarnings("unchecked")
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (cn.myapps.webservice.client.DocumentService.class.isAssignableFrom(serviceEndpointInterface)) {
				cn.myapps.webservice.client.DocumentServiceSoapBindingStub _stub = new cn.myapps.webservice.client.DocumentServiceSoapBindingStub(
						new java.net.URL(DocumentService_address), this);
				_stub.setPortName(getDocumentServiceWSDDServiceName());
				return _stub;
			}
		} catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
				+ (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * @SuppressWarnings getPort方法不支持泛型 For the given interface, get the stub
	 *                   implementation. If this service has no port for the
	 *                   given interface, then ServiceException is thrown.
	 */
	@SuppressWarnings("unchecked")
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("DocumentService".equals(inputPortName)) {
			return getDocumentService();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "DocumentServiceService");
	}

	private java.util.HashSet<Object> ports = null;

	public java.util.Iterator<?> getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet<Object>();
			ports.add(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "DocumentService"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName, java.lang.String address)
			throws javax.xml.rpc.ServiceException {

		if ("DocumentService".equals(portName)) {
			setDocumentServiceEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address)
			throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}
