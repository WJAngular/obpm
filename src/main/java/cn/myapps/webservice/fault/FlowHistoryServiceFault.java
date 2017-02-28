package cn.myapps.webservice.fault;

import java.rmi.RemoteException;

public class FlowHistoryServiceFault extends RemoteException {

	private static final long serialVersionUID = 1L;

	public FlowHistoryServiceFault(String message) {
		super(message);
	}

}
