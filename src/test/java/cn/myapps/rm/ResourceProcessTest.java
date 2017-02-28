package cn.myapps.rm;

import org.junit.Before;
import org.junit.Test;

import cn.myapps.rm.base.init.Initiator;
import cn.myapps.rm.resource.ejb.Resource;
import cn.myapps.rm.resource.ejb.ResourceProcess;
import cn.myapps.rm.resource.ejb.ResourceProcessBean;
import cn.myapps.util.sequence.Sequence;

public class ResourceProcessTest {
	
	ResourceProcess process = new ResourceProcessBean();
	
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
			Resource p = new Resource();
			p.setId(id);
			p.setName("测试项目");
			
			process.doCreate(p);
			
			Resource p2 = (Resource) process.doView(id);
			assert(p.getName().equals(p2.getName()));
			
			p2.setName("update");
			process.doUpdate(p2);
			
			Resource p3 = (Resource) process.doView(id);
			assert("update".equals(p3.getName()));
			
			process.doRemove(id);
			Resource p4 = (Resource) process.doView(id);
			assert(p4==null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

}
