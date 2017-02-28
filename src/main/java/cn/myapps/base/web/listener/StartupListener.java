package cn.myapps.base.web.listener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.commons.io.IOUtils;

import net.sf.json.JSONObject;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.form.ejb.WordFieldIsEdit;
import cn.myapps.core.dynaform.form.ejb.WordFieldIsEditJob;
import cn.myapps.core.scheduler.ejb.SchedulerFactory;
import cn.myapps.core.security.action.LoginHelper;
import cn.myapps.core.sysconfig.ejb.KmConfig;
import cn.myapps.core.sysconfig.ejb.SysConfigProcess;
import cn.myapps.core.sysconfig.ejb.SysConfigProcessBean;
import cn.myapps.core.task.ejb.Task;
import cn.myapps.core.task.ejb.TaskConstants;
import cn.myapps.core.task.ejb.TaskProcess;
import cn.myapps.core.versions.util.VersionsUtil;
import cn.myapps.core.workflow.notification.ejb.NotificationConstant;
import cn.myapps.core.workflow.notification.ejb.NotificationJob;
import cn.myapps.core.workflow.notification.ejb.NotificationSendPasswordMessage;
import cn.myapps.init.InitSystem;
import cn.myapps.km.util.NDataSource;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;
import cn.myapps.util.timer.Schedule;
import cn.myapps.util.timer.TimeRunnerAble;
import cn.myapps.util.timer.TimerRunner;

import com.jamonapi.MonitorFactory;
import com.jamonapi.proxy.MonProxyFactory;

public class StartupListener extends HttpServlet implements
		ServletContextListener {
	// Notification that the web application is ready to process requests

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TaskProcess getTaskProcess() throws ClassNotFoundException {
		return (TaskProcess) ProcessFactory.createProcess(TaskProcess.class);
	}

	public void contextInitialized(ServletContextEvent sce) {

		try {

			// 关闭性能监控
			MonitorFactory.disable();
			MonProxyFactory.enableAll(false);
			// 初始化应用真实路径
			Environment evt = Environment.getInstance();
			evt.setContextPath(sce.getServletContext().getContextPath());
			evt.setApplicationRealPath(sce.getServletContext()
							.getRealPath("/"));
			
			/**初始化系统文件系统编码**/
			//System.setProperty("file.encoding", "UTF-8"); 强制设置编码会导致mysql驱动无法搜索到中文表单，因此屏蔽。
			
			/**初始化用户以及资源URL**/
			InitSystem.init();
			//初始化MR数据库表结构
			//new cn.myapps.mr.base.schema.SchemaHelper().init();
			
			//初始化PM数据库表结构
			new cn.myapps.pm.base.schema.SchemaHelper().init();
			new cn.myapps.attendance.base.schema.SchemaHelper().init();
			
			//初始化QM数据库表结构
			new cn.myapps.qm.base.schema.SchemaHelper().init();

			//初始化KM系统数据库表结构
			SysConfigProcess sysConfigProcess = new SysConfigProcessBean();
			KmConfig kmConfig = sysConfigProcess.getKmConfig();
			if(kmConfig != null && kmConfig.getEnable()){
				try {
					NDataSource.reLoadDataSource();
					new cn.myapps.km.base.init.Initiator().init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			// 已废弃
			sce.getServletContext().setAttribute(Environment.class.getName(),
					evt);

			//记录版本信息
			VersionsUtil.doRecord();
			
			// 流程催办提醒任务, 30分钟运行一次
			PropertyUtil.reload("notification");//如果此应用的notification配置为true时,注册过期通知的任务 2013-12-13
			if("true".equals(PropertyUtil.get("Enable"))){
				Schedule.registerJob(new NotificationJob(),
						NotificationConstant.JOB_PEIROD,
						NotificationConstant.JOB_PEIROD);
			}
			
			//word控件是否可编辑处理任务
			Schedule.registerJob(new WordFieldIsEditJob(),
					WordFieldIsEdit.JOB_PEIROD,
					WordFieldIsEdit.JOB_PEIROD);
			
             //发送密码提示信息
			
			Schedule.registerJob(new NotificationSendPasswordMessage(),
					1000,
					24*3600*1000);

			//读取任务配置文件
			if("true".equals(PropertyUtil.get("task-enable"))){
				// 获取所有任务
				Collection<Task> taskList = getTaskProcess().doSimpleQuery(null);
				PersistenceUtils.closeSession(); // 会影响到定时任务中的自动启动任务
				for (Iterator<Task> iter = taskList.iterator(); iter.hasNext();) {
					// Task task = (Task) iter.next();
					Task task = iter.next();
					if (!(task.getStartupType() == TaskConstants.STARTUP_TYPE_AUTO)) {
						continue;
					}
	
					long delay = 60 * 1000;
	
					TimerRunner.registerJSService(task.getApplicationid());
	
					// 根据任务内容新建任务
					TimeRunnerAble job = TimerRunner.createTimeRunnerAble(task,
							getTaskProcess());
	
					// 把运行的任务放进runningList map中
					TimerRunner.runningList.put(task, job);
					TimerRunner.registerTimerTask(job, new Date(), delay);
					
				}
			}
			
			//启动任务调度器
			SchedulerFactory.getScheduler().start();
			
			setupUEditorConfig();
			
			
			String applicationRealPath = evt.getApplicationRealPath();
			if(!StringUtil.isBlank(applicationRealPath)){
				String path = null;
				if(applicationRealPath.endsWith("/") || applicationRealPath.endsWith("\\")){
					path = applicationRealPath+ "WEB-INF/license.lic";
				}else {
					path = applicationRealPath+ "/WEB-INF/license.lic";
				}
				File f = new File(path);
				if(f.exists()){
					String _info = IOUtils.readLines(new FileInputStream(f)).get(0);
					try {
						String cfg = _info.substring(_info.length()-8)+_info.substring(0, _info.length()-8);
						LoginHelper.licConfig = JSONObject.fromObject(Security.decryptPassword(cfg));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setupUEditorConfig(){
		Environment env = Environment.getInstance();
		String contextPath = env.getContextPath();
		String templatePath = env.getRealPath("/WEB-INF/config.json");
		String configPath = env.getRealPath("/portal/share/component/htmlEditor/ueditor/jsp/config.json");
		BufferedReader reader = null;
		FileReader fr = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fr = new FileReader(templatePath);
			reader = new BufferedReader(fr);
			StringBuffer configString = new StringBuffer();
			String buffer = "";
			while((buffer=reader.readLine())!=null){
				configString.append(buffer);
			}
			reader.close();
			fr.close();
			if(configString.length()<=0) return;
			
			JSONObject config = JSONObject.fromObject(configString.toString());
			config.put("imageUrlPrefix", contextPath);
			config.put("videoUrlPrefix", contextPath);
			config.put("fileUrlPrefix", contextPath);
			
			fw = new FileWriter(configPath);
			bw = new BufferedWriter(fw);
			bw.write(config.toString());
			
			bw.close();
			fw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(bw!=null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
