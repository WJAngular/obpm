package cn.myapps.rm;

import org.junit.Before;
import org.junit.Test;

import cn.myapps.rm.base.init.Initiator;
import cn.myapps.rm.role.ejb.Role;
import cn.myapps.rm.role.ejb.RoleProcess;
import cn.myapps.rm.role.ejb.RoleProcessBean;
import cn.myapps.util.sequence.Sequence;

public class RoleProcessTest {
	
	RoleProcess process = new RoleProcessBean();
	
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
			Role p = new Role();
			p.setId(id);
			p.setName("测试角色");
			
			process.doCreate(p);
			
			Role p2 = (Role) process.doView(id);
			assert(p.getName().equals(p2.getName()));
			
			p2.setName("update");
			process.doUpdate(p2);
			
			Role p3 = (Role) process.doView(id);
			assert("update".equals(p3.getName()));
			
			process.doRemove(id);
			Role p4 = (Role) process.doView(id);
			assert(p4==null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

}
