package cn.myapps.webservice.fault;

import java.rmi.RemoteException;

public class WorkFlowProcessServiceFault extends RemoteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9153766841824452720L;
	
	public WorkFlowProcessServiceFault(String message) {
		super(message);
	}

}
