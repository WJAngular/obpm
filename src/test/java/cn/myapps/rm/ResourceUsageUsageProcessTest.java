package cn.myapps.rm;

import org.junit.Before;
import org.junit.Test;

import cn.myapps.rm.base.init.Initiator;
import cn.myapps.rm.usage.ejb.ResourceUsage;
import cn.myapps.rm.usage.ejb.ResourceUsageProcess;
import cn.myapps.rm.usage.ejb.ResourceUsageProcessBean;
import cn.myapps.util.sequence.Sequence;

public class ResourceUsageUsageProcessTest {
	
	ResourceUsageProcess process = new ResourceUsageProcessBean();
	
	@Before
	public void before(){
		try {
			new Initiator().init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test(){
		try {
			String id = Sequence.getSequence();
			ResourceUsage p = new ResourceUsage();
			p.setId(id);
			p.setUser("happy");
			
			process.doCreate(p);
			
			ResourceUsage p2 = (ResourceUsage) process.doView(id);
			assert(p.getUser().equals(p2.getUser()));
			
			p2.setUser("update");
			process.doUpdate(p2);
			
			ResourceUsage p3 = (ResourceUsage) process.doView(id);
			assert("update".equals(p3.getUser()));
			
			process.doRemove(id);
			ResourceUsage p4 = (ResourceUsage) process.doView(id);
			assert(p4==null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

}
