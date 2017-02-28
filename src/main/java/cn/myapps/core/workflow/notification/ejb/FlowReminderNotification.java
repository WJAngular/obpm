package cn.myapps.core.workflow.notification.ejb;

import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.notification.ejb.sendmode.EmailMode;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSModeProxy;
import cn.myapps.core.workflow.notification.ejb.sendmode.WeixinMode;

/**
 * 流程催办通知
 * @author Happy
 *
 */
public class FlowReminderNotification extends Notification {
	
	private String reminderContent;
	
	public String getReminderContent() {
		return reminderContent;
	}

	public void setReminderContent(String reminderContent) {
		this.reminderContent = reminderContent;
	}

	protected void send(BaseUser responsible) throws Exception {
//		SummaryCfgVO summary = getSummaryCfg();
//		if (summary == null) return;

		SendMode sendMode = null; 
//		String content = summary.toSummay(getDocument(), getWebUser());
		for (int i = 0; i < sendModes.length; i++) {
			sendMode = sendModes[i];
			boolean flag = false;
			if (sendMode instanceof SMSModeProxy) {
				SMSModeProxy smsMode = (SMSModeProxy) sendMode;
				smsMode.setDomainid(responsible.getDomainid());
				smsMode.setReceiverUserId(responsible.getId());
				flag = smsMode.send(subject, reminderContent, responsible.getTelephone(), true);//(subject, summary, document, responsible, true);
			}else if (sendMode instanceof EmailMode) {
				String handleUrl = ((EmailMode)sendMode).getHandleUrl(document);
				String EmailContent = reminderContent + "</br><hr>" + "<a href='" + handleUrl + "'" +">" + "马上处理" + "</a>";
				flag = sendMode.send(subject,EmailContent,responsible);
			}else if(sendMode instanceof WeixinMode){
				flag = ((WeixinMode)sendMode).send(subject,reminderContent,responsible.getLoginno(),getDocument());
			} else {
				flag = sendMode.send(subject,reminderContent,responsible);
			}
			if (flag) setSended(true);
		}
	}

	
}
