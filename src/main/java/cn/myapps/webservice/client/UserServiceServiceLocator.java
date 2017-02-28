/**
 * UserServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public class UserServiceServiceLocator extends org.apache.axis.client.Service
		implements cn.myapps.webservice.client.UserServiceService {

	private static final long serialVersionUID = -2505492831664196880L;

	public UserServiceServiceLocator() {
	}

	public UserServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public UserServiceServiceLocator(java.lang.String wsdlLoc,
			javax.xml.namespace.QName sName)
			throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for UserService
	private java.lang.String UserService_address = "http://localhost:8082/obpm/services/UserService";

	public java.lang.String getUserServiceAddress() {
		return UserService_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String UserServiceWSDDServiceName = "UserService";

	public java.lang.String getUserServiceWSDDServiceName() {
		return UserServiceWSDDServiceName;
	}

	public void setUserServiceWSDDServiceName(java.lang.String name) {
		UserServiceWSDDServiceName = name;
	}

	public cn.myapps.webservice.client.UserService getUserService()
			throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(UserService_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getUserService(endpoint);
	}

	public cn.myapps.webservice.client.UserService getUserService(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			cn.myapps.webservice.client.UserServiceSoapBindingStub _stub = new cn.myapps.webservice.client.UserServiceSoapBindingStub(
					portAddress, this);
			_stub.setPortName(getUserServiceWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setUserServiceEndpointAddress(java.lang.String address) {
		UserService_address = address;
	}

	/**
	 * @SuppressWarnings getPort方法不支持泛型
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */
	@SuppressWarnings("unchecked")
	public java.rmi.Remote getPort(Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		try {
			if (cn.myapps.webservice.client.UserService.class
					.isAssignableFrom(serviceEndpointInterface)) {
				cn.myapps.webservice.client.UserServiceSoapBindingStub _stub = new cn.myapps.webservice.client.UserServiceSoapBindingStub(
						new java.net.URL(UserService_address), this);
				_stub.setPortName(getUserServiceWSDDServiceName());
				return _stub;
			}
		} catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException(
				"There is no stub implementation for the interface:  "
						+ (serviceEndpointInterface == null ? "null"
								: serviceEndpointInterface.getName()));
	}

	/**
	 * @SuppressWarnings getPort方法不支持泛型
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */
	@SuppressWarnings("unchecked")
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName,
			Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("UserService".equals(inputPortName)) {
			return getUserService();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn", "UserServiceService");
	}

	private java.util.HashSet<Object> ports = null;

	public java.util.Iterator<?> getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet<Object>();
			ports.add(new javax.xml.namespace.QName(
					"http://client.webservice.myapps.cn", "UserService"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName,
			java.lang.String address) throws javax.xml.rpc.ServiceException {

		if ("UserService".equals(portName)) {
			setUserServiceEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(
					" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName,
			java.lang.String address) throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}
