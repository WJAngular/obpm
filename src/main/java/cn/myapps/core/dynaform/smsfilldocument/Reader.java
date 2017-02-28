package cn.myapps.core.dynaform.smsfilldocument;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.shortmessage.received.ejb.ReceivedMessageProcess;
import cn.myapps.core.shortmessage.received.ejb.ReceivedMessageVO;
import cn.myapps.util.ProcessFactory;

class Reader implements Runnable {

	private final static Logger log = Logger.getLogger(Reader.class);

	private Vector<ReceivedMessageVO> receiveQueue;

	static boolean stop = false;

	Reader(Vector<ReceivedMessageVO> receiveQueue) {
		this.receiveQueue = receiveQueue;
	}

	public void run() {
		try {
			ReceivedMessageProcess process = (ReceivedMessageProcess) ProcessFactory
					.createProcess(ReceivedMessageProcess.class);
			while (!Reader.stop) {
				try {
					Collection<ReceivedMessageVO> list = process.doQueryUnReadMessage();
					if (list != null && !list.isEmpty()) {
						log.debug("Has ReceivedMessage!!");
						for (Iterator<ReceivedMessageVO> iterator = list.iterator(); iterator
								.hasNext();) {
							//ReceivedMessageVO rcVO = (ReceivedMessageVO) iterator.next();
							ReceivedMessageVO rcVO = iterator.next();
							rcVO.setStatus(1);
							process.doUpdate(rcVO);
							receiveQueue.add(rcVO);
						}
					}
				} catch (Exception e) {
					log.warn(e.getMessage());
				} finally {
					PersistenceUtils.closeSession();
				}
				if (receiveQueue.size() > 10) {
					Thread.sleep(60000);
				}
				Thread.sleep(60000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isStop() {
		return stop;
	}

	public static synchronized void setStop(boolean stop) {
		Reader.stop = stop;
	}

}
