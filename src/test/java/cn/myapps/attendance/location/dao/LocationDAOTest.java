package cn.myapps.attendance.location.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.attendance.base.dao.DaoManager;
import cn.myapps.attendance.base.schema.SchemaHelper;
import cn.myapps.attendance.location.ejb.Location;
import cn.myapps.attendance.util.ConnectionManager;
import cn.myapps.util.sequence.Sequence;

public class LocationDAOTest {
	
	LocationDAO dao;
	
	
	@Before
	public void before() throws Exception{
		new SchemaHelper().init();
		dao = (LocationDAO) DaoManager.getLocationDAO(ConnectionManager.getConnection());
	}
	
	@After
	public void after() throws Exception{
		ConnectionManager.closeConnection();
	}
	
	@Test
	public void test() throws Exception {
		
		String id = Sequence.getSequence();
		Location vo = new Location();
		vo.setName("testcase");
		vo.setLongitude(11.123456);
		vo.setLatitude(42.123456);
		vo.setDomainid("123");
		vo.setId(id);
		dao.create(vo);
		
		Location po = (Location) dao.find(id);
		Assert.assertNotNull(po);
		
		po.setName("update");
		dao.update(po);
		po = (Location) dao.find(id);
		Assert.assertNotSame(vo.getName(), po.getName());
		
		dao.remove(id);
		po = (Location) dao.find(id);
		Assert.assertNull(po);
		
	}
	
	

}
