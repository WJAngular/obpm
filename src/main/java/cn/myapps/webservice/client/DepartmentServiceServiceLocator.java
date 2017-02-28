/**
 * DepartmentServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public class DepartmentServiceServiceLocator extends
		org.apache.axis.client.Service implements
		cn.myapps.webservice.client.DepartmentServiceService {

	private static final long serialVersionUID = 2307552088257131999L;

	public DepartmentServiceServiceLocator() {
	}

	public DepartmentServiceServiceLocator(
			org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public DepartmentServiceServiceLocator(java.lang.String wsdlLoc,
			javax.xml.namespace.QName sName)
			throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for DepartmentService
	private java.lang.String DepartmentService_address = "http://localhost:8082/obpm/services/DepartmentService";

	public java.lang.String getDepartmentServiceAddress() {
		return DepartmentService_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String DepartmentServiceWSDDServiceName = "DepartmentService";

	public java.lang.String getDepartmentServiceWSDDServiceName() {
		return DepartmentServiceWSDDServiceName;
	}

	public void setDepartmentServiceWSDDServiceName(java.lang.String name) {
		DepartmentServiceWSDDServiceName = name;
	}

	public cn.myapps.webservice.client.DepartmentService getDepartmentService()
			throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(DepartmentService_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getDepartmentService(endpoint);
	}

	public cn.myapps.webservice.client.DepartmentService getDepartmentService(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			cn.myapps.webservice.client.DepartmentServiceSoapBindingStub _stub = new cn.myapps.webservice.client.DepartmentServiceSoapBindingStub(
					portAddress, this);
			_stub.setPortName(getDepartmentServiceWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setDepartmentServiceEndpointAddress(java.lang.String address) {
		DepartmentService_address = address;
	}

	/**
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */

	/**
	 * @SuppressWarnings getPort方法不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public java.rmi.Remote getPort(Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		try {
			if (cn.myapps.webservice.client.DepartmentService.class
					.isAssignableFrom(serviceEndpointInterface)) {
				cn.myapps.webservice.client.DepartmentServiceSoapBindingStub _stub = new cn.myapps.webservice.client.DepartmentServiceSoapBindingStub(
						new java.net.URL(DepartmentService_address), this);
				_stub.setPortName(getDepartmentServiceWSDDServiceName());
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
		if ("DepartmentService".equals(inputPortName)) {
			return getDepartmentService();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn",
				"DepartmentServiceService");
	}

	private java.util.HashSet<Object> ports = null;

	public java.util.Iterator<?> getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet<Object>();
			ports.add(new javax.xml.namespace.QName(
					"http://client.webservice.myapps.cn", "DepartmentService"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName,
			java.lang.String address) throws javax.xml.rpc.ServiceException {

		if ("DepartmentService".equals(portName)) {
			setDepartmentServiceEndpointAddress(address);
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
