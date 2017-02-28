package cn.myapps.core.xmpp;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.XMPPConnection;

import cn.myapps.core.user.ejb.BaseUser;

/**
 * XMPP 消息发送器
 * 
 * @author znicholas
 * 
 */
public class XMPPSender {
	public final static Logger LOG = Logger.getLogger(XMPPSender.class);

	private static XMPPSender singleton; // 单例
	private static final Object LOCK = new Object();
	private Queue<XMPPNotification> packetQueue = new ConcurrentLinkedQueue<XMPPNotification>();

	private Object waitForJobsMonitor = new Object();

	private Thread thread = new NotificationThread();

	private boolean isWaitForJobs = false;

	private XMPPSender() throws Exception {
		// 启动线程
		thread.start();
	}

	public static XMPPSender getInstance() throws Exception {
		// Synchronize on LOCK to ensure that we don't end up creating
		// two singletons.
		synchronized (LOCK) {
			if (null == singleton) {
				XMPPSender sender = new XMPPSender();
				singleton = sender;
			}
		}
		return singleton;
	}

	private XMPPConnection connectToServer(String name, String password) throws Exception {
		XMPPConnection connection = XMPPConnectionPool.getInstance().getConnection();

		if (!connection.isConnected()) {
			connection.connect();
		}
		if (!connection.isAuthenticated()) {
			connection.login(name, password);
		}

		return connection;
	}

	/**
	 * 提醒处理
	 * 
	 * @param notification
	 */
	public void processNotification(XMPPNotification notification) {
		if(!XMPPConfig.getInstance().isEnable()) return;
		packetQueue.add(notification);
		kickThread();
	}

	/**
	 * 提醒处理
	 * 
	 * @param notification
	 * @param from
	 *            发送者
	 * @param to
	 *            接收者
	 */
	public void processNotification(XMPPNotification notification, BaseUser from, BaseUser to) {
		if(!XMPPConfig.getInstance().isEnable()) return;
		notification.setSender(from);
		notification.addReceiver(to);

		packetQueue.add(notification);
		kickThread();
	}

	/**
	 * 唤醒等待的线程
	 */
	private void kickThread() {
		if (!this.thread.isInterrupted()) {
			synchronized (waitForJobsMonitor) {
				waitForJobsMonitor.notifyAll();
			}
		}
	}

	/**
	 * 提醒发送的线程
	 * 
	 * @author Nicholas
	 * 
	 */
	private class NotificationThread extends Thread {
		public void run() {
			while (true) {
				synchronized (waitForJobsMonitor) {
					if (!packetQueue.isEmpty()) {
						// 获取并移除此队列的头，如果此队列为空，则返回 null
						XMPPConnection connection = null;
						XMPPNotification notification = (XMPPNotification) packetQueue.poll();
						try {
							connection = connectToServer(notification.getSenderName(),notification.getSenderPassword());
							XMPPNotification clone = (XMPPNotification) notification.getClone(connection.getServiceName());
							connection.sendPacket(clone);
						} catch (Exception e) {
							LOG.error("Send Notification failed, " + e.getLocalizedMessage());
						} finally {
							XMPPConnectionPool.getInstance().freeConnection(connection);
						}
					}
					if (packetQueue.isEmpty()) {
						isWaitForJobs = true;
					} else {
						isWaitForJobs = false;
					}
				}

				// 等待新的作业
				if (isWaitForJobs) {
					synchronized (waitForJobsMonitor) {
						try {
							if (waitForJobsMonitor != null) {
								waitForJobsMonitor.wait();
							}
						} catch (InterruptedException e) {
							LOG.warn("XMPPSender.SendPacketThread", e);
						}
					}
				}
			}
		}
	}
}
