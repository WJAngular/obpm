package cn.myapps.util.timer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.task.ejb.Task;
import cn.myapps.core.task.ejb.TaskConstants;
import cn.myapps.core.task.ejb.TaskProcess;
import cn.myapps.util.ProcessFactory;

public class TimerRunner {
	public final static Map<Task, TimeRunnerAble> runningList = new HashMap<Task, TimeRunnerAble>();

	private static Timer timer = new Timer();

	// public TimerRunner(TimeRunnerAble aRunnerable) {
	//
	// }
	//
	public static void registerTimerTask(TimeRunnerAble runnerAble,
			Date firstTime, long period) {
		timer.schedule(runnerAble, firstTime, period);
	}

	public static void registerTimerTask(TimeRunnerAble runnerAble, long delay,
			long period) {
		timer.schedule(runnerAble, delay, period);
	}

	public static void unregisterAllTimerTask() {
		timer.cancel();
		timer = null;
	}

	// 注册自动服务
	public static void registerJSService(String application) {
		try {
			IRunner runner = JavaScriptFactory.getInstance(null, application);
			runner.initBSFManager(null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static TimeRunnerAble createTimeRunnerAble(final Task task,
			final TaskProcess tp) {

		return new TimeRunnerAble() {
			int timesCount = 0;

			int currTotal = task.getTotalRuntimes();

			public void run() {
				try {
					ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
					ApplicationVO application = (ApplicationVO) applicationProcess.doView(task.getApplicationid());
					if(application==null || !application.isActivated()){
						return;
					}
					
					String terminateScript = task.getTerminateScript(); // 停止条件
					int total = currTotal;
	
					// 达到运行次数则停止
					if (task.getRuntimes() == timesCount
							&& task.getPeriod() == TaskConstants.REAPET_TYPE_NOTREAPET) {
						this.cancel(task, tp);
						TimerRunner.runningList.remove(task);
					}

					// 符合停止条件则停止
					registerJSService(task.getApplicationid());

					StringBuffer label = new StringBuffer();
					label.append("Task(").append(task.getId()).append(
							")." + task.getName()).append(".TerminateScript");

					IRunner runner = JavaScriptFactory.getInstance(null, task
							.getApplicationid());
					Object terminateObj = runner.run(label.toString(),
							terminateScript);
					if (terminateObj instanceof Boolean) {
						if (((Boolean) terminateObj).booleanValue()) {
							this.cancel(task, tp);
							TimerRunner.runningList.remove(task);
						}
					}
					if (task.isExecuteAble()) {
						timesCount++;

						try {
							task.execute();

							total += timesCount;
							task.setTotalRuntimes(total);

							tp.doCumulativeTask(task);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						PersistenceUtils.closeSessionAndConnection();
					} catch (Exception e) {
					}
				}
			}
		};
	}
}
