package cn.myapps.util.message;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.shortmessage.received.ejb.ReceivedMessageProcess;
import cn.myapps.core.shortmessage.received.ejb.ReceivedMessageVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSModeProxy;
import cn.myapps.util.ProcessFactory;

public class ShortMessageUtil {

	public DataPackage<ReceivedMessageVO> queryReplyById(String id) throws Exception {
		ReceivedMessageProcess process = (ReceivedMessageProcess) ProcessFactory
				.createProcess(ReceivedMessageProcess.class);
		if (process != null)
			return process.queryByDocId(id);
		else
			return null;
	}

	public SMSModeProxy getSender(WebUser user) {
		return new SMSModeProxy(user);
	}
}
