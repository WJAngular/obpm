package cn.myapps.init;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.DomainApplicationSet;
import cn.myapps.core.dynaform.smsfilldocument.FillDocumentJob;
import cn.myapps.util.CLoader;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.property.MultiLanguageProperty;
import cn.myapps.util.property.PropertyUtil;
import cn.myapps.util.timer.Job;
import cn.myapps.util.timer.Schedule;

/**
 * Execute this object to nitialize the system.
 */
public class InitSystem {

	private static boolean USE_SHORTMESSAGE_INTERFACE = false;
	static {
		try {
			String bool = PropertyUtil.getByPropName("shortmessage", "auto.received.job");
			USE_SHORTMESSAGE_INTERFACE = Boolean.parseBoolean(bool);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		init();
	}

	public static void init() throws InitializationException {
		cn.myapps.util.property.PropertyUtil.init();

		try {
			PersistenceUtils.beginTransaction();
		
			InitWorkingCalendar initWrkCld = new InitWorkingCalendar();
			initWrkCld.run();
	
			InitUserInfo initUser = new InitUserInfo();
			initUser.run();
	
			InitOperationInfo initOperation = new InitOperationInfo();
			initOperation.run();
	
			InitInstance initInst = new InitInstance();
			initInst.run();
	
			try {
				MultiLanguageProperty.init();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	
			if (USE_SHORTMESSAGE_INTERFACE) {
				try {
					Schedule.registerJob((Job) (CLoader.initReceiveJob().newInstance()), new Date());
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
				Timer timer = new Timer();
				Date time = new Date();
				timer.schedule(new FillDocumentJob(), new Date(time.getTime() + 60000));
				
			}
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			try {
				PersistenceUtils.rollbackTransaction();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		dataUpgrade();
	}
	
	private static void dataUpgrade(){
		Connection conn = null;
		try {
			Session session = PersistenceUtils.currentSession();
			conn = session.connection();
			Statement stmt1 = conn.createStatement();
			try {
				stmt1.executeQuery("select count(*) from t_domain_application_set");
				stmt1.close();
			} catch (Exception e) {
				return;
			}
			ApplicationProcess process = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
			String querySql = "select * from t_domain_application_set";
			Statement stmt2 = conn.createStatement();
			ResultSet rs = stmt2.executeQuery(querySql);
			List<DomainApplicationSet> list = new ArrayList<DomainApplicationSet>();
			while(rs.next()){
				DomainApplicationSet domainApplicationSet = new DomainApplicationSet();
				domainApplicationSet.setApplicationId(rs.getString("APPLICATIONID"));
				domainApplicationSet.setDomainId(rs.getString("DOMAINID"));
				list.add(domainApplicationSet);
			}
			stmt2.close();
			for (Iterator<DomainApplicationSet> iterator = list.iterator(); iterator.hasNext();) {
				DomainApplicationSet domainApplicationSet = (DomainApplicationSet) iterator
						.next();
				process.createDomainApplicationSet(domainApplicationSet);
				//session.flush();
			}
			
			Statement stmt3 = conn.createStatement();
			Transaction trans = session.beginTransaction();
			try {
				stmt3.execute("delete from  t_domain_application_set ");
				trans.commit();
			} catch (Exception e) {
				trans.rollback();
			}
			stmt3.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(conn !=null) conn.close();
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
			}
		}
	}
}
