package cn.myapps.core.expimp.imp.util;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface ImpOperator<E> extends Remote {
	public Collection<E> writeDataToDB(Collection<E> datas, String applicationId)
			throws Exception;

	public Collection<E> writeXmlToDB(String xmlStr, String applicationId)
			throws RemoteException;
}
