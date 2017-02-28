/**
 * TeemLinkSMS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.client;

public interface TeemLinkSMS extends java.rmi.Remote {
	public int sendMsg(java.lang.String memberCode, java.lang.String memberPWD,
			java.lang.String smsNumber, java.lang.String smsContent)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.NotEoughBalanceExpection,
			cn.myapps.webservice.fault.AuthorizationException,
			cn.myapps.webservice.fault.InternalExpcetion;

	public int sendMsg(java.lang.String memberCode, java.lang.String memberPWD,
			java.lang.String smsNumber, java.lang.String smsContent,
			java.lang.String replyCode) throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.NotEoughBalanceExpection,
			cn.myapps.webservice.fault.AuthorizationException,
			cn.myapps.webservice.fault.InternalExpcetion;

	public cn.myapps.webservice.model.SimpleReceive receiveMsg(
			java.lang.String memberCode, java.lang.String memberPWD)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.AuthorizationException,
			cn.myapps.webservice.fault.InternalExpcetion;

	public cn.myapps.webservice.model.SimpleReceive[] receiveMsgs(
			java.lang.String memberCode, java.lang.String memberPWD)
			throws java.rmi.RemoteException,
			cn.myapps.webservice.fault.AuthorizationException,
			cn.myapps.webservice.fault.InternalExpcetion;
}
