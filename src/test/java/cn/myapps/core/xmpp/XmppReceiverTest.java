package cn.myapps.core.xmpp;

import junit.framework.TestCase;

public class XmppReceiverTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testStart() {
		XMPPReceiver receiver = new XMPPReceiver();
		receiver.start();
	}

}
