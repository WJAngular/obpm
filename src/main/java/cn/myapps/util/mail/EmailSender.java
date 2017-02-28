package cn.myapps.util.mail;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

public class EmailSender {
	
	private static String keyLock = "KeyLock";

	private Queue<Email> emailQueue = new ConcurrentLinkedQueue<Email>();

	private Object waitForJobsMonitor = new Object();

	private Thread thread = new SendMailThread();

	private boolean isWaitForJobs = false;

	private static EmailSender sender = new EmailSender();
	
	private static final Logger log = Logger.getLogger(EmailSender.class);

	
	private EmailSender() {
		// 将邮件发送线程设置成后台线程
		//thread.setDaemon(true);
	}

	public static EmailSender getInstance() {
		synchronized (keyLock) {
			if (sender == null) {
				sender = new EmailSender();
				sender.isWaitForJobs = true;
			}
			return sender;
		}
	}

	
	public void sendEmail() {
		if (!thread.isAlive()) {
			thread.start();
		}
	}


	/**
	 * 加入Email到发送队列
	 * 
	 * @param from
	 * @param to
	 * @param subject
	 * @param body
	 * @param host
	 * @param user
	 * @param password
	 * @param bbc
	 * @param validate
	 */
	public void addEmail(String from, String to, String subject, String body,
			String host, String user, String password, String bcc,
			boolean validate) {
		addEmail(from, to, subject, body, null, host, user, password, bcc, null, validate);
	}
	
	/**
	 * 加入Email到发送队列
	 * @param from
	 * @param to
	 * @param subject
	 * @param body
	 * @param attachFileNames
	 * @param host
	 * @param user
	 * @param password
	 * @param cc
	 * @param bcc
	 * @param validate
	 */
	public void addEmail(String from, String to, String subject, String body,String[] attachFileNames, 
			String host, String user, String password, String cc, String bcc, 
			 boolean validate) {
		Email email = new Email(from, to, subject, body, host, user, password,
				cc, bcc, attachFileNames, validate);
		this.addEmail(email);
	}

	/**
	 * 加入Email到发送队列
	 * 
	 * @param email
	 */
	public void addEmail(Email email) {
		emailQueue.add(email);
		kickThread();
	}

	public void clear() {
		emailQueue.clear();
	}

	private void kickThread() {
		if (!this.thread.isInterrupted()) {
			synchronized (waitForJobsMonitor) {
				waitForJobsMonitor.notifyAll();
			}
		}
	}


	/**
	 * 邮件发送的线程
	 * 
	 * @author Nicholas
	 * 
	 */
	public class SendMailThread extends Thread {

		public void run() {
			while (true) {
				synchronized (waitForJobsMonitor) {
					if (!emailQueue.isEmpty()) {
						// 获取并移除此队列的头，如果此队列为空，则返回 null
						Email email = (Email) emailQueue.poll();
						// Email不为空时，发邮件
						if (email != null) {
							boolean sendSuccess = false;
							if (email.isHaveAttachment()) {
								MultiEmailSender sender = new MultiEmailSender(email);
								sendSuccess = sender.sendMultiEmail();
							} else {
								SimpleEmailSender sender = new SimpleEmailSender(email, true);
								sendSuccess = sender.sendHtmlEmail();
							}
							if (sendSuccess) {
								log.info("Sent email to: " + email.getTo() + " success");
							}
						}
					}
					if (emailQueue.isEmpty()) {
						isWaitForJobs = true;
					} else {
						isWaitForJobs = false;
					}
				}

				// 等待新的作业
				if (isWaitForJobs) {
					synchronized (waitForJobsMonitor) {
						try {
							if(waitForJobsMonitor != null) {
								waitForJobsMonitor.wait();
							}
						} catch (InterruptedException e) {
							log.warn("EmailSender.SendMailThread: " + e.getMessage());
						}
					}
				}
			}
		}
	}
}
