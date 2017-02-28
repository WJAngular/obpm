/**
 * SMSServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public class SMSServiceSoapBindingStub extends org.apache.axis.client.Stub
		implements cn.myapps.webservice.client.TeemLinkSMS {

	private java.util.Vector<Object> cachedSerClasses = new java.util.Vector<Object>();
	private java.util.Vector<Object> cachedSerQNames = new java.util.Vector<Object>();
	private java.util.Vector<Object> cachedSerFactories = new java.util.Vector<Object>();
	private java.util.Vector<Object> cachedDeserFactories = new java.util.Vector<Object>();

	static org.apache.axis.description.OperationDesc[] _operations;

	static {
		_operations = new org.apache.axis.description.OperationDesc[4];
		_initOperationDesc1();
	}

	private static void _initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("sendMsg");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "memberCode"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "memberPWD"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "smsNumber"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "smsContent"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "sendMsgReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper
				.addFault(new org.apache.axis.description.FaultDesc(
						new javax.xml.namespace.QName(
								"http://localhost:8080/sms/services/SMSService",
								"fault"),
						"cn.teemlink.sms.webservice.exception.NotEoughBalanceExpection",
						new javax.xml.namespace.QName(
								"http://exception.webservice.sms.teemlink.cn",
								"NotEoughBalanceExpection"), true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://localhost:8080/sms/services/SMSService",
						"fault"),
				"cn.teemlink.sms.webservice.exception.AuthorizationException",
				new javax.xml.namespace.QName(
						"http://exception.webservice.sms.teemlink.cn",
						"AuthorizationException"), true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://localhost:8080/sms/services/SMSService",
						"fault"),
				"cn.teemlink.sms.webservice.exception.InternalExpcetion",
				new javax.xml.namespace.QName(
						"http://exception.webservice.sms.teemlink.cn",
						"InternalExpcetion"), true));
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("sendMsg");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "memberCode"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "memberPWD"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "smsNumber"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "smsContent"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "replyCode"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "sendMsgReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper
				.addFault(new org.apache.axis.description.FaultDesc(
						new javax.xml.namespace.QName(
								"http://localhost:8080/sms/services/SMSService",
								"fault"),
						"cn.teemlink.sms.webservice.exception.NotEoughBalanceExpection",
						new javax.xml.namespace.QName(
								"http://exception.webservice.sms.teemlink.cn",
								"NotEoughBalanceExpection"), true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://localhost:8080/sms/services/SMSService",
						"fault"),
				"cn.teemlink.sms.webservice.exception.AuthorizationException",
				new javax.xml.namespace.QName(
						"http://exception.webservice.sms.teemlink.cn",
						"AuthorizationException"), true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://localhost:8080/sms/services/SMSService",
						"fault"),
				"cn.teemlink.sms.webservice.exception.InternalExpcetion",
				new javax.xml.namespace.QName(
						"http://exception.webservice.sms.teemlink.cn",
						"InternalExpcetion"), true));
		_operations[1] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("receiveMsg");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "memberCode"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "memberPWD"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"urn:model.webservice.sms.teemlink.cn", "SimpleReceive"));
		oper.setReturnClass(cn.myapps.webservice.model.SimpleReceive.class);
		oper.setReturnQName(new javax.xml.namespace.QName("",
				"receiveMsgReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://localhost:8080/sms/services/SMSService",
						"fault"),
				"cn.teemlink.sms.webservice.exception.AuthorizationException",
				new javax.xml.namespace.QName(
						"http://exception.webservice.sms.teemlink.cn",
						"AuthorizationException"), true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://localhost:8080/sms/services/SMSService",
						"fault"),
				"cn.teemlink.sms.webservice.exception.InternalExpcetion",
				new javax.xml.namespace.QName(
						"http://exception.webservice.sms.teemlink.cn",
						"InternalExpcetion"), true));
		_operations[2] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("receiveMsgs");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "memberCode"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "memberPWD"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://localhost:8080/sms/services/SMSService",
				"ArrayOf_tns2_SimpleReceive"));
		oper.setReturnClass(cn.myapps.webservice.model.SimpleReceive[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("",
				"receiveMsgsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://localhost:8080/sms/services/SMSService",
						"fault"),
				"cn.teemlink.sms.webservice.exception.AuthorizationException",
				new javax.xml.namespace.QName(
						"http://exception.webservice.sms.teemlink.cn",
						"AuthorizationException"), true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(
						"http://localhost:8080/sms/services/SMSService",
						"fault"),
				"cn.teemlink.sms.webservice.exception.InternalExpcetion",
				new javax.xml.namespace.QName(
						"http://exception.webservice.sms.teemlink.cn",
						"InternalExpcetion"), true));
		_operations[3] = oper;

	}

	public SMSServiceSoapBindingStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public SMSServiceSoapBindingStub(java.net.URL endpointURL,
			javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public SMSServiceSoapBindingStub(javax.xml.rpc.Service service)
			throws org.apache.axis.AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service)
				.setTypeMappingVersion("1.2");

		javax.xml.namespace.QName qName;
		javax.xml.namespace.QName qName2;
		java.lang.Class<?> cls;
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
				"http://exception.webservice.sms.teemlink.cn",
				"AuthorizationException");
		cachedSerQNames.add(qName);
		cls = cn.myapps.webservice.fault.AuthorizationException.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(
				"http://exception.webservice.sms.teemlink.cn",
				"InternalExpcetion");
		cachedSerQNames.add(qName);
		cls = cn.myapps.webservice.fault.InternalExpcetion.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(
				"http://exception.webservice.sms.teemlink.cn",
				"NotEoughBalanceExpection");
		cachedSerQNames.add(qName);
		cls = cn.myapps.webservice.fault.NotEoughBalanceExpection.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(
				"http://localhost:8080/sms/services/SMSService",
				"ArrayOf_tns2_SimpleReceive");
		cachedSerQNames.add(qName);
		cls = cn.myapps.webservice.model.SimpleReceive[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName(
				"urn:model.webservice.sms.teemlink.cn", "SimpleReceive");
		qName2 = null;
		cachedSerFactories
				.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(
						qName, qName2));
		cachedDeserFactories
				.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(
				"urn:model.webservice.sms.teemlink.cn", "SimpleReceive");
		cachedSerQNames.add(qName);
		cls = cn.myapps.webservice.model.SimpleReceive.class;
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

	public int sendMsg(java.lang.String memberCode, java.lang.String memberPWD,
			java.lang.String smsNumber, java.lang.String smsContent)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.NotEoughBalanceExpection,
			cn.myapps.webservice.fault.AuthorizationException,
			cn.myapps.webservice.fault.InternalExpcetion {
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
				"http://webservice.sms.teemlink.cn", "sendMsg"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					memberCode, memberPWD, smsNumber, smsContent });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((java.lang.Integer) _resp).intValue();
				} catch (java.lang.Exception _exception) {
					return ((java.lang.Integer) org.apache.axis.utils.JavaUtils
							.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.NotEoughBalanceExpection) {
					throw (cn.myapps.webservice.fault.NotEoughBalanceExpection) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.AuthorizationException) {
					throw (cn.myapps.webservice.fault.AuthorizationException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.InternalExpcetion) {
					throw (cn.myapps.webservice.fault.InternalExpcetion) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public int sendMsg(java.lang.String memberCode, java.lang.String memberPWD,
			java.lang.String smsNumber, java.lang.String smsContent,
			java.lang.String replyCode) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.NotEoughBalanceExpection,
			cn.myapps.webservice.fault.AuthorizationException,
			cn.myapps.webservice.fault.InternalExpcetion {
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
				"http://webservice.sms.teemlink.cn", "sendMsg"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					memberCode, memberPWD, smsNumber, smsContent, replyCode });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((java.lang.Integer) _resp).intValue();
				} catch (java.lang.Exception _exception) {
					return ((java.lang.Integer) org.apache.axis.utils.JavaUtils
							.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.NotEoughBalanceExpection) {
					throw (cn.myapps.webservice.fault.NotEoughBalanceExpection) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.AuthorizationException) {
					throw (cn.myapps.webservice.fault.AuthorizationException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.InternalExpcetion) {
					throw (cn.myapps.webservice.fault.InternalExpcetion) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public cn.myapps.webservice.model.SimpleReceive receiveMsg(
			java.lang.String memberCode, java.lang.String memberPWD)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.AuthorizationException,
			cn.myapps.webservice.fault.InternalExpcetion {
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
				"http://webservice.sms.teemlink.cn", "receiveMsg"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					memberCode, memberPWD });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (cn.myapps.webservice.model.SimpleReceive) _resp;
				} catch (java.lang.Exception _exception) {
					return (cn.myapps.webservice.model.SimpleReceive) org.apache.axis.utils.JavaUtils
							.convert(
									_resp,
									cn.myapps.webservice.model.SimpleReceive.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.AuthorizationException) {
					throw (cn.myapps.webservice.fault.AuthorizationException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.InternalExpcetion) {
					throw (cn.myapps.webservice.fault.InternalExpcetion) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public cn.myapps.webservice.model.SimpleReceive[] receiveMsgs(
			java.lang.String memberCode, java.lang.String memberPWD)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.AuthorizationException,
			cn.myapps.webservice.fault.InternalExpcetion {
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
				"http://webservice.sms.teemlink.cn", "receiveMsgs"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					memberCode, memberPWD });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (cn.myapps.webservice.model.SimpleReceive[]) _resp;
				} catch (java.lang.Exception _exception) {
					return (cn.myapps.webservice.model.SimpleReceive[]) org.apache.axis.utils.JavaUtils
							.convert(
									_resp,
									cn.myapps.webservice.model.SimpleReceive[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.AuthorizationException) {
					throw (cn.myapps.webservice.fault.AuthorizationException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof cn.myapps.webservice.fault.InternalExpcetion) {
					throw (cn.myapps.webservice.fault.InternalExpcetion) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

}
