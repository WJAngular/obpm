package cn.myapps.util.converter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.search.IndexBuilder;
import cn.myapps.km.search.IndexBundle;
import cn.myapps.km.util.Config;

public class ConverterRunner {
	private static String keyLock = "KeyLock";
	
	private static final int MAX_THREAD = 10;//最大swf文件转换线程数

	private Queue<ConverterBundle> queue = new ConcurrentLinkedQueue<ConverterBundle>();
	
	ExecutorService pool = Executors.newFixedThreadPool(MAX_THREAD); //线程池

	private Object waitForJobsMonitor = new Object();

	private Thread thread = new ConverterThread();

	private boolean isWaitForJobs = false;
	
	private IndexBundle indexBundle;

	private static ConverterRunner converter = new ConverterRunner();
	
	private static final Logger log = Logger.getLogger(ConverterRunner.class);
	
	ConverterRunner(){
		
	}
	
	public static ConverterRunner getInstance(){
		synchronized (keyLock){
			if(converter == null){
				converter = new ConverterRunner();
				converter.isWaitForJobs = true;
			}
			return converter;
		}
	}
	
	public void convert(){
		if(!thread.isAlive()){
			thread.start();
		}
	}
	
	public void addBundle(ConverterBundle bundle){
		if(isReadableFile(bundle.getType()) && Config.previewEnabled()){
			queue.add(bundle);
			kickThread();
		}
	}
	
	private void kickThread() {
		if (!this.thread.isInterrupted()) {
			synchronized (waitForJobsMonitor) {
				waitForJobsMonitor.notifyAll();
			}
		}
	}

	public void setIndexBundle(IndexBundle indexBundle) {
		this.indexBundle = indexBundle;
	}

	public class ConverterThread extends Thread {

		public void run() {
			while (true) {
				synchronized (waitForJobsMonitor) {
					while (!queue.isEmpty()) {
						// 获取并移除此队列的头，如果此队列为空，则返回 null
						ConverterBundle bundle =  queue.poll();
						if (bundle != null) {
							try {
								Thread runner = new Runner(bundle);
								pool.execute(runner);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					if (queue.isEmpty()) {
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
	
	/**
	 * SWF文件转换线程
	 * @author xiuwei
	 *
	 */
	public class Runner extends Thread {
		
		private ConverterBundle bundle;
		

		public Runner(ConverterBundle bundle) {
			super();
			this.bundle = bundle;
		}

		public void setBundle(ConverterBundle bundle) {
			this.bundle = bundle;
		}

		public void run() {
			try {
				ConverterUtil.createSWF(bundle.getUuid(), bundle.getSource(),  bundle.getType());
				
				//建立索引
				//if (false) {
				//if(!NFile.TYPE_JPEG.equalsIgnoreCase(bundle.getType()) && !NFile.TYPE_JPG.equalsIgnoreCase(bundle.getType()) && !NFile.TYPE_PNG.equalsIgnoreCase(bundle.getType()) && !NFile.TYPE_GIF.equalsIgnoreCase(bundle.getType())){
				if(bundle.getFile() !=null){	
					IndexBuilder builder = IndexBuilder.getInstance();
					builder.setRealPath(indexBundle.getRealPath());
					builder.setNdiskid(indexBundle.getNdiskid());
					builder.addNFile(bundle.getFile());
					builder.setUpdate(false);//为新建索引
					builder.buildIndex();
				}
				//}
//				Thread.sleep(5000);
//				System.out.println("#Thread[name:]"+this.getName()+" [id:]"+this.getId()+" has been executed!!");
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					//PersistenceUtils.closeConnection();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 是否为可阅读文档类型
	 * 
	 * @param type
	 * @return
	 */
	public boolean isReadableFile(String type) {
		boolean readable = false;
		if (NFile.TYPE_DOC.equalsIgnoreCase(type)
				|| NFile.TYPE_DOCX.equalsIgnoreCase(type)
				|| NFile.TYPE_PDF.equalsIgnoreCase(type)
				|| NFile.TYPE_XLS.equalsIgnoreCase(type)
				|| NFile.TYPE_XLSX.equalsIgnoreCase(type)
				|| NFile.TYPE_PPT.equalsIgnoreCase(type)
				|| NFile.TYPE_PPTX.equalsIgnoreCase(type)
				|| NFile.TYPE_WPS.equalsIgnoreCase(type)
				|| NFile.TYPE_TXT.equalsIgnoreCase(type)
				|| NFile.TYPE_HTML.equalsIgnoreCase(type)
				|| NFile.TYPE_HTM.equalsIgnoreCase(type)
				|| NFile.TYPE_DPS.equalsIgnoreCase(type)
				|| NFile.TYPE_ET.equalsIgnoreCase(type)
				|| NFile.TYPE_POT.equalsIgnoreCase(type)
				|| NFile.TYPE_PPS.equalsIgnoreCase(type)
				|| NFile.TYPE_RTF.equalsIgnoreCase(type)
				|| NFile.TYPE_JPEG.equalsIgnoreCase(type)
				|| NFile.TYPE_JPG.equalsIgnoreCase(type)
				|| NFile.TYPE_PNG.equalsIgnoreCase(type)
				|| NFile.TYPE_GIF.equalsIgnoreCase(type)) {
			readable = true;
		}
		return readable;

	}
	
	public static void main(String[] args) {
		
	}
	
}
