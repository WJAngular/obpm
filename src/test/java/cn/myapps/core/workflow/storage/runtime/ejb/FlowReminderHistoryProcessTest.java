package cn.myapps.core.workflow.storage.runtime.ejb;

import org.junit.Assert;
import org.junit.Test;

import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

public class FlowReminderHistoryProcessTest {
	
	
	
	@Test
	public void crud() throws Exception{
		
		String appid = "11e4-c931-e8152d8f-b512-0b9a6eebd8cc";
		String domainid = "11e1-81e2-37f74759-9124-47aada6b7467";

		FlowReminderHistoryProcess process = new FlowReminderHistoryProcessBean(appid);
		
		String id = Sequence.getSequence();
		
		FlowReminderHistory h = new FlowReminderHistory();
		h.setId(id);
		h.setApplicationid(appid);
		h.setDomainid(domainid);
		h.setContent("test");
		
		process.doCreate(h);
		
		FlowReminderHistory h2 = (FlowReminderHistory) process.doView(id);
		
		Assert.assertNotNull(h2);
		
		h2.setContent("update");
		
		process.doUpdate(h2);
		
		
		FlowReminderHistory h3 = (FlowReminderHistory) process.doView(id);
		
		Assert.assertEquals(h3.getContent(), "update");
		
		process.doRemove(id);
		
		FlowReminderHistory h4 = (FlowReminderHistory) process.doView(id);
		
		Assert.assertNull(h4);
	}

}
