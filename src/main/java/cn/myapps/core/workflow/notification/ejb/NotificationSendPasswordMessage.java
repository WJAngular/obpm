package cn.myapps.core.workflow.notification.ejb;

import java.util.Collection;
import java.util.Date;



import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.sysconfig.ejb.LoginConfig;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.notification.ejb.sendmode.PersonalMessageMode;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSModeProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.mail.EmailUtil;
import cn.myapps.util.property.PropertyUtil;
import cn.myapps.util.timer.Job;

public class NotificationSendPasswordMessage extends Job {

	@Override
	public void run() {
		PropertyUtil.reload("passwordLegal");
		String loginPasswordNotice = PropertyUtil.get(LoginConfig.LOGIN_UPDATE_NOTICE);
		String loginNoticeMethod = PropertyUtil.get(LoginConfig.LOGIN_NOTICE_METHOD);
		String loginNoticeAuthor = PropertyUtil.get(LoginConfig.LOGIN_NOTICE_AUTHOR);
		//String loginNoticeFrequency = PropertyUtil.get(LoginConfig.LOGIN_NOTICE_FREQUENCY);
		String loginNoticeContent = PropertyUtil.get(LoginConfig.LOGIN_NOTICE_CONTENT);
		String loginPasswordMaxage  = PropertyUtil.get(LoginConfig.LOGIN_PASSWOR_MAXAGE);
		long now = new Date().getTime();
		long passwordAgeNotice = Long.parseLong(loginPasswordNotice)*24*3600*1000;
		//long noticeFrequency = Long.parseLong(loginNoticeFrequency)*24*3600*1000;
		long passwordAge = Long.parseLong(loginPasswordMaxage)*24*3600*1000;
		String [] method = loginNoticeMethod.split(",");
		String content = loginNoticeContent;
		UserVO user=null;
		try {
		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		DomainProcess domainProcess = (DomainProcess)ProcessFactory.createProcess(DomainProcess.class);
		Collection<DomainVO> domains = domainProcess.queryDomainsByStatus(1);
		for(DomainVO domain:domains){
			Collection<UserVO> users = userProcess.queryByDomain(domain.getId());
			for(UserVO user1:users){
				
			
		if(loginNoticeAuthor.equals("1")){
		
		}else{
			
	
			
		    user = userProcess.getUserByLoginnoAndDoaminName(user1.getLoginno(), domain.getName());
		
		long lastPassTime = user.getLastModifyPasswordTime().getTime();
		
		
		
		if((lastPassTime+passwordAge)<now){
			//禁用该用户
			userProcess.updateUserLockFlag(user.getLoginno(), 0);
		}

			if((lastPassTime+passwordAgeNotice)<now){
		
				for(int i = 0; method.length>i;i++){
		if(method[i].equals("1")){
			WebUser tuser = new WebUser(user);

           SMSModeProxy sender = new SMSModeProxy(tuser);
			try {
				sender.send("", content, user.getTelephone(), null, false);	
			} catch (Exception e) {
				
				e.printStackTrace();
			}		
		}
		if(method[i].equals(" 2")){
			PersonalMessageMode personMessageMode = new PersonalMessageMode();
			personMessageMode.send("", content, user.getId());
				}
		if(method[i].equals(" 3")){
			EmailUtil eu = new EmailUtil();
			eu.sendEmailBySystemUserForTranspond(user.getEmail(), "", content);
				}
		      }	
			  }
			 }
		   }
		  }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
			}
		}
	}	
	 private static String decrypt(final String s){
		    return Security.decryptPassword(s);
		}

}
