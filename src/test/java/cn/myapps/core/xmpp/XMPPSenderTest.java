package cn.myapps.core.xmpp;

import junit.framework.TestCase;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.xmpp.notification.PendingNotification;

public class XMPPSenderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testProcessNotification() throws Exception {
		for (int i = 0; i < 1; i++) {
			PendingVO valueObject = new PendingVO();
			valueObject.setId("pending001" + i);
			valueObject.setFormid("form001" + i);
			valueObject.setSummary("测试待办" + i);
			valueObject.setApplicationid("app001" + i);

//			XMPPNotification notification = new PendingNotification(valueObject, PendingNotification.ACTION_CREATE);
//			XMPPSender.getInstance().processNotification(notification);
		}
	}

}
