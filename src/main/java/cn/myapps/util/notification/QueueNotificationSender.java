package cn.myapps.util.notification;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

/**
 * 单线程队列通知提醒发送器
 * @author Happy
 *
 */
public class QueueNotificationSender implements NotificationSender {
	
	private static final Logger log = Logger.getLogger(QueueNotificationSender.class);
	/**
	 * 待发送的短信队列
	 */
	private Queue<Message> queue = new ConcurrentLinkedQueue<Message>();
	
	
	private Object LOCK = new Object();
	
	private Thread thread = new SenderThread();

	@Override
	public void putMessage(Message message) {
		queue.add(message);
		
		if (!this.thread.isInterrupted()) {
		synchronized (LOCK) {
			if(!queue.isEmpty()){
				LOCK.notifyAll();
				log.info("消息通知发送器线程恢复到活动状态");
				}
			}
		}

	}

	@Override
	public void start() {
		if (!thread.isAlive()) {
			thread.start();
			log.info("消息通知发送器线程切换到启动状态");
		}
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * 消息发送线程
	 * @author Happy
	 *
	 */
	class SenderThread extends Thread{

		@Override
		public void run() {
			while (true) {
				synchronized (LOCK) {
					if (!queue.isEmpty()) {

						Message message = queue.poll();
						if (message != null) {
							message.send();
						}

					}else{
						try {
							log.info("消息通知发送器线程切换到休眠状态");
							LOCK.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
	}

}
