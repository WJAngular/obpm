package cn.myapps.core.workflow.notification.ejb;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.timer.Job;
import cn.myapps.util.timer.Schedule;

public class NotificationJob extends Job {
	public final static Logger LOG = Logger.getLogger(NotificationJob.class);
	
	public final static Object _lock = new Object();

	public void run() {
		synchronized (_lock) {
			try {
				LOG.info("********************* Notification Job Start ********************");
				ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory
						.createProcess(ApplicationProcess.class);
				Collection<ApplicationVO> applications = applicationProcess.doSimpleQuery(null);
				try {
					for (Iterator<ApplicationVO> iterator = applications.iterator(); iterator.hasNext();) {
						ApplicationVO application = (ApplicationVO) iterator.next();
						if (application.testDB()) {
							try {
								NotificationProcess process = (NotificationProcess) ProcessFactory.createRuntimeProcess(
										NotificationProcess.class, application.getId());
								process.notifyOverDueAuditors();
							} catch (Exception e) {
								continue;
							} finally {
								PersistenceUtils.closeSessionAndConnection();
							}
						}
					}
				} catch (Exception e) {
					LOG.error("Notification Job Error: ", e);
				}
	
				LOG.info("********************* Notification Job End ********************");
			} catch (Exception e) {
				LOG.error("Notification Job Error: ", e);
			} finally {
				try {
					PersistenceUtils.closeSessionAndConnection();
				} catch (Exception e) {
					LOG.error("Notification Job Error: ", e);
				}
			}
		}
	}

	public static void main(String[] args) {
		Schedule.registerJob(new NotificationJob(), new Date());
	}
}
