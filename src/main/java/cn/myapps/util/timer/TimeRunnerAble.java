package cn.myapps.util.timer;

import java.util.TimerTask;

import cn.myapps.core.task.ejb.Task;
import cn.myapps.core.task.ejb.TaskProcess;

public abstract class TimeRunnerAble extends TimerTask {
	
	public boolean cancel(Task task , TaskProcess tp) {
		try {
			tp.doCumulativeTask(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.cancel();
	}
}
