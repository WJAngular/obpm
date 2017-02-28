package cn.myapps.core.task.action;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;


import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.task.ejb.Task;
import cn.myapps.core.task.ejb.TaskConstants;
import cn.myapps.core.task.ejb.TaskProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.timer.TimeRunnerAble;
import cn.myapps.util.timer.TimerRunner;

public class TaskAction extends BaseAction<Task> {

	private String rTime;

	private String rDate;
	
	//任务运行状态(0表示停止,1表示运行)
	private int runState = 0;

	public int getRunState() {
		return runState;
	}

	public void setRunState(int runState) {
		this.runState = runState;
	}

	private static final long serialVersionUID = 2948439801638083317L;

	private static Map<Integer, String> _TASKTYPE = new TreeMap<Integer, String>();

	private static Map<Integer, String> _STARTUPTYPE = new TreeMap<Integer, String>();

	private static Map<Integer, String> _REAPETTYPE = new TreeMap<Integer, String>();

	static {
		_TASKTYPE.put(Integer.valueOf(TaskConstants.TASK_TYPE_SCRIPT), "{*[Script]*}");

		_STARTUPTYPE.put(Integer.valueOf(TaskConstants.STARTUP_TYPE_MANUAL), "{*[cn.myapps.core.task.manual]*}");
		_STARTUPTYPE.put(Integer.valueOf(TaskConstants.STARTUP_TYPE_AUTO), "{*[Auto]*}");
		_STARTUPTYPE.put(Integer.valueOf(TaskConstants.STARTUP_TYPE_BANNED), "{*[cn.myapps.core.task.banned]*}");

		_REAPETTYPE = TaskConstants.getRepeatTypeList();
	}

	public Map<Integer, String> get_TASKTYPE() {
		return _TASKTYPE;
	}

	public Map<Integer, String> get_STARTUPTYPE() {
		return _STARTUPTYPE;
	}

	public Map<Integer, String> get_REAPETTYPE() {
		return _REAPETTYPE;
	}

	/**
	 * 默认构造方法
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TaskAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(TaskProcess.class), new Task());
	}

	/**
	 * 保存运行时所以的数据
	 */
	public String doSave() {
		try {
			this.setDate();
			Task task = getTask();
			task.setModifyTime(new Date());
			if (task.getStartupType() == TaskConstants.STARTUP_TYPE_BANNED) {
				this.stopTask();
			}
			String msg = super.doSave();
			if(task.getStartupType() == TaskConstants.STARTUP_TYPE_AUTO){
				this.stopTask();
				this.doStart();
			}
			return msg;
		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	/**
	 * 保存并新建
	 */
	public String doSaveAndNew() {
		try {
			Task task = getTask();
			task.setModifyTime(new Date());
			if (task.getName().equals("")) {
				throw new OBPMValidateException(
						"{*[page.name.notexist]*}");
			}
			this.setDate();
			if (task.getStartupType() == TaskConstants.STARTUP_TYPE_BANNED) {
				this.stopTask();
			}

			String msg = super.doSave();
			if(task.getStartupType() == TaskConstants.STARTUP_TYPE_AUTO){
				this.stopTask();
				this.doStart();
			}
			this.setContent(new Task());
			return msg;
		}  catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	public String doEdit() {
		try {
			Map<?, ?> params = getContext().getParameters();

			String id = ((String[]) params.get("id"))[0];
			Task bi = (Task) process.doView(id);
			// 获取所有运行时列表
			Collection<Task> runningList = TimerRunner.runningList.keySet();
			if(runningList != null){
			for (Iterator<Task> iter = runningList.iterator(); iter.hasNext();) {
				Task task = (Task) iter.next();
				if ((task.getId()).equals(id)) {
					this.setRunState(1);
					break;
				}
			}
			}
			setContent(bi);
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}
	
	public String doDelete() {
		try {
			Collection<Task> runningList = TimerRunner.runningList.keySet();
			if (_selects != null && runningList !=null){
				for (int i = 0; i < _selects.length; i++) {
					String id = _selects[i];
					if(!StringUtil.isBlank(id)){
						Task task = (Task) process.doView(id);
						for (Iterator<Task> iter = runningList.iterator(); iter.hasNext();) {
							Task t = (Task) iter.next();
							if (t.getId().equals(task.getId())) {
								TimeRunnerAble job = (TimeRunnerAble) TimerRunner.runningList.get(t);
								TimerRunner.runningList.remove(t);
								job.cancel(t, (TaskProcess) this.process);
								break;
							}
						}
					}
				}
			
				process.doRemove(_selects);
			}

			addActionMessage("{*[delete.successful]*}");
			return SUCCESS;
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			if (e.getMessage() !=null && e.getMessage().indexOf("Could not execute JDBC batch update") > -1) {
				addFieldError("", "{*[Resource.has.been.cited]*}");
			} else {
				LOG.error(this.getClass().getName() + "doDelete", e);
				this.setRuntimeException(new OBPMRuntimeException("{*[Error]*}!{*[Resource.has.been.cited.try.again]*}!",e));
				e.printStackTrace();
			}
			return INPUT;
		}
	}
	
	/**
	 * 开始运行
	 * 
	 * @return
	 * @throws Exception
	 */

	public String doStart() throws Exception {
		try {
			this.setDate();
			this.startTask();
			this.setRunState(1);
			addActionMessage("{*[core.task.started]*}");
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 结束
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doStop() throws Exception {
		try {
			this.setDate();
			this.stopTask();
			addActionMessage("{*[cn.myapps.core.task.stop_success]*}");
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	private void startTask() throws Exception {
		boolean flag = false;

		// 获取所有运行时列表
		Collection<Task> runningList = TimerRunner.runningList.keySet();
		boolean temp = false;

		for (Iterator<Task> iter = runningList.iterator(); iter.hasNext();) {
			Task task = (Task) iter.next();
			if ((task.getId()).equals(getTask().getId())) {
				temp = true;
				break;
			}
		}
		
		if (!temp || runningList.size() == 0) {
			flag = true;
		}
		
		int taskType = getTask().getStartupType();
		if (flag && taskType != TaskConstants.STARTUP_TYPE_BANNED) {
			TimeRunnerAble job = TimerRunner.createTimeRunnerAble(getTask(), (TaskProcess) this.process);

			TimerRunner.registerTimerTask(job, new Date(), 60 * 1000);

			TimerRunner.runningList.put(getTask(), job);
		} else {
			throw new OBPMValidateException("{*[core.task.error.runing]*}");
		}
	}
	
	private void stopTask() {
		Set<Task> runningList = TimerRunner.runningList.keySet();
		this.setRunState(0);
		for (Iterator<Task> iter = runningList.iterator(); iter.hasNext();) {
			Task task = (Task) iter.next();
			// 以id判断runningList的task与当前task是否相等
			if ((task.getId()).equals(getTask().getId())) {
				Object obj = TimerRunner.runningList.get(task);
				long delay = 1000L;
				if (obj != null && delay > 0) {
					TimerRunner.runningList.remove(task);
					TimeRunnerAble job = (TimeRunnerAble) obj;
					job.cancel(task, (TaskProcess) this.process);
					break;
				}
			}
		}
	}

	// 设置日期
	private void setDate() throws Exception {
		Date date = new Date();
		try {
			int period = getTask().getPeriod();
			if (period == TaskConstants.REPEAT_TYPE_DAILY
					|| period == TaskConstants.REPEAT_TYPE_WEEKLY
					|| period == TaskConstants.REPEAT_TYPE_MONTHLY) {
				// 当为每天，每周，每月的时候。选择运行日期的时候
				SimpleDateFormat formater = new SimpleDateFormat();
				formater.applyPattern("HH:mm:ss");
				if (rTime.equals("")) {
					throw new OBPMValidateException("{*[core.task.choosetime]*}");
				}
				String dateStr = rTime;
				date = formater.parse(dateStr);
			} else if (period == TaskConstants.REPEAT_TYPE_NONE) {
				// 当为不重复的时候。
				if (rDate.equals("")) {
					throw new OBPMValidateException(
							"{*[cn.myapps.core.task.select_running_time]*}");
				} else if (rTime.equals("")) {
					throw new OBPMValidateException("{*[core.task.choosetime]*}");
				}
				String dateStr = rDate + " " + rTime;
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				date = formatter.parse(dateStr);
			} else if (period == TaskConstants.REPEAT_TYPE_DAILY_MINUTES
					|| period == TaskConstants.REPEAT_TYPE_DAILY_HOURS) {
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				date = formatter.parse(formatter.format(date));
			} else {
				// 当为立即的时候
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				date = formatter.parse(formatter.format(date));
			}
		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			throw e;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			throw e;
		}
		// 把时间放入到运行的那里去
		getTask().setRunningTime(date);
	}

	private Task getTask() {
		return ((Task) this.getContent());
	}

	public Collection<Integer> get_dayOfWeek() {
		Task vo = (Task) getContent();
		return vo != null ? vo.getDaysOfWeek() : null;
	}

	public void set_dayOfWeek(Collection<String> _dayOfWeek) {
		((Task) getContent()).getDaysOfWeek().clear();
		for (Iterator<String> iterator = _dayOfWeek.iterator(); iterator.hasNext();) {
			String day = (String) iterator.next();
			((Task) getContent()).getDaysOfWeek().add(Integer.valueOf(day));
		}
	}

	public String getRTime() {
		return rTime;
	}

	public void setRTime(String time) {
		rTime = time;
	}

	public String getRDate() {
		return rDate;
	}

	public void setRDate(String date) {
		rDate = date;
	}

	/**
	 * 返回所属模块主键(module id)
	 * 
	 * @return 所属模块主键(module id)
	 */
	public String get_moduleid() {
		Task task = (Task) getContent();
		if (task.getModule() != null) {
			return task.getModule().getId();
		}
		return null;
	}

	/**
	 * Set模块主键(module id)
	 * 
	 * @param _moduleid
	 *            模块主键
	 */
	public void set_moduleid(String _moduleid) {
		Task task = (Task) getContent();
		if (_moduleid != null) {
			ModuleProcess mp;
			try {
				mp = (ModuleProcess) ProcessFactory.createProcess((ModuleProcess.class));
				ModuleVO module = (ModuleVO) mp.doView(_moduleid);
				task.setModule(module);
			}catch (OBPMValidateException e) {
				addFieldError("", e.getValidateMessage());
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}
	}
	
	public String doList() {
		ParamsTable params = getParams();
		int lines = 10;
		Cookie[] cookies = ServletActionContext.getRequest().getCookies();
		for(Cookie cookie : cookies){
			if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
				lines = Integer.parseInt(cookie.getValue());
			}
		    cookie.getName();
		    cookie.getValue();
		}
		params.removeParameter("_pagelines");
		params.setParameter("_pagelines", lines);
		try {
			this.validateQueryParams();
			datas = this.process.doQuery(getParams(), getUser());
		}catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}

}
