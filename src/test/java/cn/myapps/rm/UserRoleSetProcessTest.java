package cn.myapps.rm;

import org.junit.Before;
import org.junit.Test;

import cn.myapps.rm.base.init.Initiator;
import cn.myapps.rm.role.ejb.UserRoleSet;
import cn.myapps.rm.role.ejb.UserRoleSetProcess;
import cn.myapps.rm.role.ejb.UserRoleSetProcessBean;
import cn.myapps.util.sequence.Sequence;

public class UserRoleSetProcessTest {
	
	UserRoleSetProcess process = new UserRoleSetProcessBean();
	
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
			UserRoleSet p = new UserRoleSet();
			p.setId(id);
			p.setUserId("123-abc");
			
			process.doCreate(p);
			
			UserRoleSet p2 = (UserRoleSet) process.doView(id);
			assert(p.getUserId().equals(p2.getUserId()));
			
			p2.setUserId("update");
			process.doUpdate(p2);
			
			UserRoleSet p3 = (UserRoleSet) process.doView(id);
			assert("update".equals(p3.getUserId()));
			
			process.doRemove(id);
			UserRoleSet p4 = (UserRoleSet) process.doView(id);
			assert(p4==null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

}
