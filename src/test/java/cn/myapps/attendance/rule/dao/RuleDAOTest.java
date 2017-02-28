package cn.myapps.attendance.rule.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.attendance.base.dao.DaoManager;
import cn.myapps.attendance.base.schema.SchemaHelper;
import cn.myapps.attendance.rule.ejb.Rule;
import cn.myapps.attendance.util.ConnectionManager;
import cn.myapps.util.sequence.Sequence;

public class RuleDAOTest {
	
	RuleDAO dao;
	
	
	@Before
	public void before() throws Exception{
		new SchemaHelper().init();
		dao = (RuleDAO) DaoManager.getRuleDAO(ConnectionManager.getConnection());
	}
	
	@After
	public void after() throws Exception{
		ConnectionManager.closeConnection();
	}
	
	@Test
	public void test() throws Exception {
		
		String id = Sequence.getSequence();
		Rule vo = new Rule();
		vo.setName("testcase");
		vo.setDomainid("123");
		vo.setId(id);
		dao.create(vo);
		
		Rule po = (Rule) dao.find(id);
		Assert.assertNotNull(po);
		
		po.setName("update");
		dao.update(po);
		po = (Rule) dao.find(id);
		Assert.assertNotSame(vo.getName(), po.getName());
		
		dao.remove(id);
		po = (Rule) dao.find(id);
		Assert.assertNull(po);
		
	}
	
	

}
