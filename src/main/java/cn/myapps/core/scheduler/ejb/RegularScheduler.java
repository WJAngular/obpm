package cn.myapps.core.scheduler.ejb;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

/**
 * 常规任务调度器
 * @author Happy
 *
 */
final class RegularScheduler implements Scheduler {
	
	private boolean start  = false;//调度器是否已经启动
	private static Object LOCK = new Object();
	private static final int THREAD_POOL_SIZE = 5;//线程池大小
	private static final long INTERVAL = 1*60*1000L;//查询任务的时间间隔
	
	private boolean isWaitForJobs = false;

	private Queue<TriggerVO> queue = new ConcurrentLinkedQueue<TriggerVO>();//任务队列
	
	//ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE); //执行任务的线程池
	ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);


	/* (non-Javadoc)
	 * @see cn.myapps.core.scheduler.ejb.Scheduler#addTrigger(cn.myapps.core.scheduler.ejb.TriggerVO)
	 */
	public void addTrigger(TriggerVO trigger) throws Exception {
		try {
			TriggerProcess process = (TriggerProcess)ProcessFactory.createProcess(TriggerProcess.class);
			
			if((new Date().getTime()+10*60*1000L)-trigger.getDeadline()>=0){//10分钟内需要触发的任务直接放到任务队列中
				trigger.setId(Sequence.getSequence());
				trigger.setState(TriggerVO.STATE_STAND_BY);
				process.doCreate(trigger);
				queue.add(trigger);
				kickThread();
			}else{
				process.doCreate(trigger);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

	/* (non-Javadoc)
	 * @see cn.myapps.core.scheduler.ejb.Scheduler#deleteTrigger(java.lang.String)
	 */
	public void cancelTrigger(String token) throws Exception {
		try {
			TriggerProcess process = (TriggerProcess)ProcessFactory.createProcess(TriggerProcess.class);
			process.removeByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see cn.myapps.core.scheduler.ejb.Scheduler#isShutdown()
	 */
	public boolean isShutdown() throws Exception {
		return !this.start;
	}

	/* (non-Javadoc)
	 * @see cn.myapps.core.scheduler.ejb.Scheduler#isStart()
	 */
	public boolean isStart() throws Exception {
		return this.start;
	}

	public void shutdown() throws Exception {
		
	}

	/* (non-Javadoc)
	 * @see cn.myapps.core.scheduler.ejb.Scheduler#start()
	 */
	public void start() throws Exception {
		try {
			if(!isStart()){
				init();
				queryJobs();
				schedule();
			}
			
			this.start = true;
		} catch (Exception e) {
			throw e;
		}

	}
	
	/**
	 * 唤醒任务队列派发线程
	 */
	private void kickThread() {
		synchronized (LOCK) {
			LOCK.notifyAll();
		}
	}
	
	/**
	 * 初始化
	 */
	private void init(){
		try {
			TriggerProcess process = (TriggerProcess)ProcessFactory.createProcess(TriggerProcess.class);
			//初始化触发器的状态，主要目的是还原非正常关闭服务器情况下状态标记就绪的触发器的状态
			process.updateStandbyState2WaitingState();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * 获取达到触发时机的任务，并添加到任务队列中
	 */
	private void queryJobs(){
		TimerTask task = new TimerTask() {
			
			public void run() {
				try {
					TriggerProcess process = (TriggerProcess)ProcessFactory.createProcess(TriggerProcess.class);
					Collection<TriggerVO> list = process.getStandbyTrigger(INTERVAL);
					for (Iterator<TriggerVO> iterator = list.iterator(); iterator
							.hasNext();) {
						TriggerVO triggerVO = iterator.next();
						triggerVO.setState(TriggerVO.STATE_STAND_BY);
						process.doUpdate(triggerVO);
						queue.add(triggerVO);//添加到任务队列
					}
					
					kickThread();
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					try {
						PersistenceUtils.closeSessionAndConnection();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		Timer timer = new Timer(RegularScheduler.class.getName());
		timer.schedule(task, 1000, INTERVAL);
		
	}
	
	/**
	 * 调度任务队列中的任务
	 */
	private void schedule(){
		new Thread(new Runnable() {
			
			public void run() {
				while (true) {
					synchronized (LOCK) {
						while (!queue.isEmpty()) {
							// 获取并移除此队列的头，如果此队列为空，则返回 null
							TriggerVO trigger =  queue.poll();
							if (trigger != null) {
								try {
									Thread executor = new TriggerExecutor(trigger);
									pool.schedule(executor, trigger.getExecuteDelay(), TimeUnit.MILLISECONDS);
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
						synchronized (LOCK) {
							try {
								if(LOCK != null) {
									LOCK.wait();
								}
							} catch (InterruptedException e) {
							}
						}
					}
				}
			}
		}).start();
	}
	
	
	 /**
	  * 执行触发器任务的线程
	 * @author Happy
	 *
	 */
	private class TriggerExecutor extends Thread {
		
		private TriggerVO trigger;
		

		public TriggerExecutor(TriggerVO trigger) {
			super();
			this.trigger = trigger;
		}

		public void run() {
			try {
				Thread.sleep(1000);
				TriggerProcess process = (TriggerProcess)ProcessFactory.createProcess(TriggerProcess.class);
				if(!process.isCancel(trigger.getId())){
					trigger.getJob().execute();
					process.doRemove(trigger.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					PersistenceUtils.closeSessionAndConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	

}
