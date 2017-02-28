package cn.myapps.km.search;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import cn.myapps.km.disk.ejb.NFile;

public class IndexBuilder {
	private static String keyLock = "KeyLock";
	
	private static final int MAX_THREAD = 10;//最大建立索引线程数
	
	ExecutorService pool = Executors.newFixedThreadPool(MAX_THREAD); //线程池

	private Queue<NFile> nfileQueue = new ConcurrentLinkedQueue<NFile>();

	private Object waitForJobsMonitor = new Object();

	private Thread thread = new BuildIndexThread();

	private boolean isWaitForJobs = false;

	private static IndexBuilder builder = new IndexBuilder();
	
	private static final Logger log = Logger.getLogger(IndexBuilder.class);
	
	private String realPath;
	
	private String ndiskid;
	
	private boolean isUpdate;
	
	private IndexBuilder(){
		
	}
	
	public static IndexBuilder getInstance(){
		synchronized (keyLock){
			if(builder == null){
				builder = new IndexBuilder();
				builder.isWaitForJobs = true;
			}
			return builder;
		}
	}
	
	public void buildIndex(){
		if(!thread.isAlive()){
			thread.start();
		}
	}
	
	public void addNFile(NFile nfile){
		nfileQueue.add(nfile);
		kickThread();
	}
	
	private void kickThread() {
		if (!this.thread.isInterrupted()) {
			synchronized (waitForJobsMonitor) {
				waitForJobsMonitor.notifyAll();
			}
		}
	}
	
	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	
	public String getNdiskid() {
		return ndiskid;
	}

	public void setNdiskid(String ndiskid) {
		this.ndiskid = ndiskid;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * 建立索引的线程
	 */
	public class BuildIndexThread extends Thread {

		public void run() {
			while (true) {
				synchronized (waitForJobsMonitor) {
					if (!nfileQueue.isEmpty()) {
						// 获取并移除此队列的头，如果此队列为空，则返回 null
						NFile nfile = (NFile) nfileQueue.poll();
						
						if (nfile != null) {
							try {
								Thread runner = new Runner(nfile, realPath, ndiskid);
								pool.execute(runner);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					if (nfileQueue.isEmpty()) {
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
							log.warn("IndexBuilder.buildindexThread: " + e.getMessage());
						}
					}
				}
			}
		}
	}
	
	public class Runner extends Thread {
			
		private NFile nfile;
		
		private String realpath;
		
		private String ndiskid;
		

		public Runner(NFile nfile, String realpath, String ndiskid) {
			super();
			this.nfile = nfile;
			this.realpath = realpath;
			this.ndiskid = ndiskid;
		}

		public void setNfile(NFile nfile) {
			this.nfile = nfile;
		}

		public void setRealpath(String realpath) {
			this.realpath = realpath;
		}

		public void setNdiskid(String ndiskid) {
			this.ndiskid = ndiskid;
		}

		public void run() {
			try {
				if (nfile.getState()==1) {
					SearchEngine.addPublicDiskIndex(nfile, realpath, ndiskid, isUpdate);
				}
				else {
					SearchEngine.addPrivateDiskIndex(nfile, realpath, ndiskid, isUpdate);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
