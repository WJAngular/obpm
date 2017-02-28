/**
 * UserServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public class UserServiceSoapBindingStub extends org.apache.axis.client.Stub
		implements cn.myapps.webservice.client.UserService {
	private java.util.Vector<Object> cachedSerClasses = new java.util.Vector<Object>();
	private java.util.Vector<Object> cachedSerQNames = new java.util.Vector<Object>();
	private java.util.Vector<Object> cachedSerFactories = new java.util.Vector<Object>();
	private java.util.Vector<Object> cachedDeserFactories = new java.util.Vector<Object>();

	static org.apache.axis.description.OperationDesc[] _operations;

	static {
		_operations = new org.apache.axis.description.OperationDesc[8];
		_initOperationDesc1();
	}

	private static void _initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("createUser");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "user"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("urn:model.webservice.myapps.cn",
						"SimpleUser"),
				cn.myapps.webservice.model.SimpleUser.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"urn:model.webservice.myapps.cn", "SimpleUser"));
		oper.setReturnClass(cn.myapps.webservice.model.SimpleUser.class);
		oper.setReturnQName(new javax.xml.namespace.QName("",
				"createUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://client.webservice.myapps.cn", "fault"),
				"cn.myapps.webservice.fault.UserServiceFault",
				new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn",
						"UserServiceFault"), true));
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getUser");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "pk"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"urn:model.webservice.myapps.cn", "SimpleUser"));
		oper.setReturnClass(cn.myapps.webservice.model.SimpleUser.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://client.webservice.myapps.cn", "fault"),
				"cn.myapps.webservice.fault.UserServiceFault",
				new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn",
						"UserServiceFault"), true));
		_operations[1] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("validateUser");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "domainName"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "userAccount"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "userPassword"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "userType"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "int"), int.class,
				false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"urn:model.webservice.myapps.cn", "SimpleUser"));
		oper.setReturnClass(cn.myapps.webservice.model.SimpleUser.class);
		oper.setReturnQName(new javax.xml.namespace.QName("",
				"validateUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://client.webservice.myapps.cn", "fault"),
				"cn.myapps.webservice.fault.UserServiceFault",
				new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn",
						"UserServiceFault"), true));
		_operations[2] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("updateUser");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "user"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("urn:model.webservice.myapps.cn",
						"SimpleUser"),
				cn.myapps.webservice.model.SimpleUser.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://client.webservice.myapps.cn", "fault"),
				"cn.myapps.webservice.fault.UserServiceFault",
				new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn",
						"UserServiceFault"), true));
		_operations[3] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("deleteUser");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "pk"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://client.webservice.myapps.cn", "fault"),
				"cn.myapps.webservice.fault.UserServiceFault",
				new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn",
						"UserServiceFault"), true));
		_operations[4] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("deleteUser");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "user"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("urn:model.webservice.myapps.cn",
						"SimpleUser"),
				cn.myapps.webservice.model.SimpleUser.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://client.webservice.myapps.cn", "fault"),
				"cn.myapps.webservice.fault.UserServiceFault",
				new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn",
						"UserServiceFault"), true));
		_operations[5] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("setRoleSet");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "user"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("urn:model.webservice.myapps.cn",
						"SimpleUser"),
				cn.myapps.webservice.model.SimpleUser.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "roles"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://client.webservice.myapps.cn",
						"ArrayOf_soapenc_string"), java.lang.String[].class,
				false, false);
		oper.addParameter(param);
		oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://client.webservice.myapps.cn", "fault"),
				"cn.myapps.webservice.fault.UserServiceFault",
				new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn",
						"UserServiceFault"), true));
		_operations[6] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("setDepartmentSet");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "user"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("urn:model.webservice.myapps.cn",
						"SimpleUser"),
				cn.myapps.webservice.model.SimpleUser.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "deps"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://client.webservice.myapps.cn",
						"ArrayOf_soapenc_string"), java.lang.String[].class,
				false, false);
		oper.addParameter(param);
		oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://client.webservice.myapps.cn", "fault"),
				"cn.myapps.webservice.fault.UserServiceFault",
				new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn",
						"UserServiceFault"), true));
		_operations[7] = oper;

	}

	public UserServiceSoapBindingStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public UserServiceSoapBindingStub(java.net.URL endpointURL,
			javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public UserServiceSoapBindingStub(javax.xml.rpc.Service service)
			throws org.apache.axis.AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service)
				.setTypeMappingVersion("1.2");
		java.lang.Class<?> cls;
		javax.xml.namespace.QName qName;
		javax.xml.namespace.QName qName2;
		java.lang.Class<?> beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
		java.lang.Class<?> beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
		/*
		 * java.lang.Class enumsf =
		 * org.apache.axis.encoding.ser.EnumSerializerFactory.class;
		 * java.lang.Class enumdf =
		 * org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
		 * java.lang.Class arraysf =
		 * org.apache.axis.encoding.ser.ArraySerializerFactory.class;
		 * java.lang.Class arraydf =
		 * org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
		 * java.lang.Class simplesf =
		 * org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
		 * java.lang.Class simpledf =
		 * org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
		 * java.lang.Class simplelistsf =
		 * org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
		 * java.lang.Class simplelistdf =
		 * org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
		 */
		qName = new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn", "ArrayOf_soapenc_string");
		cachedSerQNames.add(qName);
		cls = java.lang.String[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName(
				"http://schemas.xmlsoap.org/soap/encoding/", "string");
		qName2 = null;
		cachedSerFactories
				.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(
						qName, qName2));
		cachedDeserFactories
				.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn",
				"UserServiceFault");
		cachedSerQNames.add(qName);
		cls = cn.myapps.webservice.fault.UserServiceFault.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("urn:model.webservice.myapps.cn",
				"SimpleUser");
		cachedSerQNames.add(qName);
		cls = cn.myapps.webservice.model.SimpleUser.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

	}

	protected org.apache.axis.client.Call createCall()
			throws java.rmi.RemoteException {
		try {
			org.apache.axis.client.Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			java.util.Enumeration<Object> keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				java.lang.String key = (java.lang.String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call
							.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
					_call
							.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						java.lang.Class<?> cls = (java.lang.Class<?>) cachedSerClasses
								.get(i);
						javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames
								.get(i);
						java.lang.Object x = cachedSerFactories.get(i);
						if (x instanceof Class<?>) {
							java.lang.Class<?> sf = (java.lang.Class<?>) cachedSerFactories
									.get(i);
							java.lang.Class<?> df = (java.lang.Class<?>) cachedDeserFactories
									.get(i);
							_call
									.registerTypeMapping(cls, qName, sf, df,
											false);
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories
									.get(i);
							org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories
									.get(i);
							_call
									.registerTypeMapping(cls, qName, sf, df,
											false);
						}
					}
				}
			}
			return _call;
		} catch (java.lang.Throwable _t) {
			throw new org.apache.axis.AxisFault(
					"Failure trying to get the Call object", _t);
		}
	}

	public cn.myapps.webservice.model.SimpleUser createUser(
			cn.myapps.webservice.model.SimpleUser user)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn", "createUser"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call
					.invoke(new java.lang.Object[] { user });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (cn.myapps.webservice.model.SimpleUser) _resp;
				} catch (java.lang.Exception _exception) {
					return (cn.myapps.webservice.model.SimpleUser) org.apache.axis.utils.JavaUtils
							.convert(_resp,
									cn.myapps.webservice.model.SimpleUser.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.UserServiceFault) {
					throw (cn.myapps.webservice.fault.UserServiceFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public cn.myapps.webservice.model.SimpleUser getUser(java.lang.String pk)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn", "getUser"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call
					.invoke(new java.lang.Object[] { pk });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (cn.myapps.webservice.model.SimpleUser) _resp;
				} catch (java.lang.Exception _exception) {
					return (cn.myapps.webservice.model.SimpleUser) org.apache.axis.utils.JavaUtils
							.convert(_resp,
									cn.myapps.webservice.model.SimpleUser.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.UserServiceFault) {
					throw (cn.myapps.webservice.fault.UserServiceFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public cn.myapps.webservice.model.SimpleUser validateUser(
			java.lang.String domainName, java.lang.String userAccount,
			java.lang.String userPassword, int userType)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[2]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn", "validateUser"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					domainName, userAccount, userPassword,
					Integer.valueOf(userType) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (cn.myapps.webservice.model.SimpleUser) _resp;
				} catch (java.lang.Exception _exception) {
					return (cn.myapps.webservice.model.SimpleUser) org.apache.axis.utils.JavaUtils
							.convert(_resp,
									cn.myapps.webservice.model.SimpleUser.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.UserServiceFault) {
					throw (cn.myapps.webservice.fault.UserServiceFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public void updateUser(cn.myapps.webservice.model.SimpleUser user)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[3]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn", "updateUser"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call
					.invoke(new java.lang.Object[] { user });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			}
			extractAttachments(_call);
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.UserServiceFault) {
					throw (cn.myapps.webservice.fault.UserServiceFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public void deleteUser(java.lang.String pk)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[4]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn", "deleteUser"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call
					.invoke(new java.lang.Object[] { pk });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			}
			extractAttachments(_call);
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.UserServiceFault) {
					throw (cn.myapps.webservice.fault.UserServiceFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public void deleteUser(cn.myapps.webservice.model.SimpleUser user)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[5]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn", "deleteUser"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call
					.invoke(new java.lang.Object[] { user });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			}
			extractAttachments(_call);
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.UserServiceFault) {
					throw (cn.myapps.webservice.fault.UserServiceFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public void setRoleSet(cn.myapps.webservice.model.SimpleUser user,
			java.lang.String[] roles) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[6]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn", "setRoleSet"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					user, roles });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			}
			extractAttachments(_call);
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.UserServiceFault) {
					throw (cn.myapps.webservice.fault.UserServiceFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public void setDepartmentSet(cn.myapps.webservice.model.SimpleUser user,
			java.lang.String[] deps) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.UserServiceFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[7]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://client.webservice.myapps.cn", "setDepartmentSet"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					user, deps });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			}
			extractAttachments(_call);
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.UserServiceFault) {
					throw (cn.myapps.webservice.fault.UserServiceFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

}
