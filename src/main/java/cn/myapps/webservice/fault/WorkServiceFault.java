package cn.myapps.webservice.fault;

import java.rmi.RemoteException;

public class WorkServiceFault extends RemoteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2125037483612634118L;

	public WorkServiceFault(String message) {
		super(message);
	}

}
