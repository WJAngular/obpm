package cn.myapps.webservice.fault;

import java.rmi.RemoteException;

public class SecurityServiceFault extends RemoteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6751262468825862967L;
	
	public SecurityServiceFault (String message) {
		super(message);
	}

}
