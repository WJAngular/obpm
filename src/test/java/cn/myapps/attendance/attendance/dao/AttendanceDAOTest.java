package cn.myapps.attendance.attendance.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.attendance.attendance.ejb.Attendance;
import cn.myapps.attendance.base.dao.DaoManager;
import cn.myapps.attendance.base.schema.SchemaHelper;
import cn.myapps.attendance.util.ConnectionManager;
import cn.myapps.util.sequence.Sequence;

public class AttendanceDAOTest {
	
	AttendanceDAO dao;
	
	
	@Before
	public void before() throws Exception{
		new SchemaHelper().init();
		dao = (AttendanceDAO) DaoManager.getAttendanceDAO(ConnectionManager.getConnection());
	}
	
	@After
	public void after() throws Exception{
		ConnectionManager.closeConnection();
	}
	
	@Test
	public void test() throws Exception {
		
		String id = Sequence.getSequence();
		Attendance vo = new Attendance();
		vo.setUserName("testcase");
		vo.setDomainid("123");
		vo.setId(id);
		dao.create(vo);
		
		Attendance po = (Attendance) dao.find(id);
		Assert.assertNotNull(po);
		
		po.setUserName("update");
		dao.update(po);
		po = (Attendance) dao.find(id);
		Assert.assertNotSame(vo.getUserName(), po.getUserName());
		
		dao.remove(id);
		po = (Attendance) dao.find(id);
		Assert.assertNull(po);
		
	}
	
	

}
