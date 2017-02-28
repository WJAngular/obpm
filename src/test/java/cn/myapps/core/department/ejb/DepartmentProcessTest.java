package cn.myapps.core.department.ejb;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import cn.myapps.base.AbstractRuntimeProcessBeanTest;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

public class DepartmentProcessTest extends AbstractRuntimeProcessBeanTest {
	DepartmentProcess process;

	public DepartmentProcessTest() throws Exception {
		super();
		process = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);
	}

	@Test
	public void testDoCreateFindRemove() throws Exception {

		DepartmentVO vo = new DepartmentVO();

		String id = Sequence.getSequence();
		vo.setId(id);
		vo.setName("testdepartment");

		/**
		 * 应该把Runtime、Designtime、Authtime分开， Applicationid在Authtine是没有用的，
		 * 只需要和Domainid有关。
		 */
		vo.setApplicationid(getApplicationId());
		vo.setDomainid("domainid");
		vo.setCode("code");

		// 测试新建
		process.doCreate(vo);

		DepartmentVO po = (DepartmentVO) process.doView(id);

		assertEquals(vo.getId(), po.getId());
		assertEquals(vo.getName(), po.getName());
		assertEquals(vo.getDomainid(), po.getDomainid());
		assertEquals(vo.getCode(), po.getCode());

		// 测试重复新建
		Exception err = null;
		try {
			process.doCreate(vo);
		} catch (Exception e) {
			err = e;
		}

		assertNotNull(err);

		vo.setName("newname");

		// 测试更新
		process.doUpdate(vo);

		po = (DepartmentVO) process.doView(id);

		assertNotSame(vo.getName(), po.getName());

		// 测试删除
		process.doRemove(new String[] { vo.getId() });

		po = (DepartmentVO) process.doView(id);

		assertNull(po);
	}

	@Test
	public void testGetDatasByParent() throws Exception {

		DepartmentVO parentVO = new DepartmentVO();

		String parentId = Sequence.getSequence();
		parentVO.setId(parentId);
		parentVO.setName("parentdepartment");

		parentVO.setApplicationid(getApplicationId());
		parentVO.setDomainid("domainid");
		parentVO.setCode("parentcode");
		parentVO.setLevel(0);

		// 测试新建
		process.doCreate(parentVO);

		DepartmentVO subVO = new DepartmentVO();

		String subId = Sequence.getSequence();
		subVO.setId(subId);
		subVO.setName("subdepartment");

		subVO.setApplicationid(getApplicationId());
		subVO.setDomainid("domainid");
		subVO.setCode("subcode");
		
		//Level本来应该是通过计算出来，而不是直接赋值
		subVO.setLevel(1);
		subVO.setSuperior(parentVO);
		
		process.doCreate(subVO);

		Collection vos = process.getDatasByParent(parentId);
		
		//测试getChildrenCount
		int count = (int) process.getChildrenCount(parentId);

		assertEquals(1, vos.size());
		
		assertEquals(1, count);

		Exception err = null;
		try {
			process.doRemove(parentVO.getId());
		} catch (Exception e) {
			err = e;
		}
		assertNotNull(err);

		//测试getUpperDeptIds
		Collection<String> ids = process.getUpperDeptIds(subVO.getId());
		assertEquals(1,ids.size());
		
		process.doRemove(subVO.getId());
		process.doRemove(parentVO.getId());
	}

	@Test
	public void testDeepSearchDepartmentTree() {
	}

	@Test
	public void testGetUnderDeptListString() {
	}

	@Test
	public void testGetUnderDeptListStringInt() {
	}

	@Test
	public void testGetSuperiorDeptList() {
	}

	@Test
	public void testGetDepartmentByLevel() throws Exception {
	}

	/**
	 * 命名和传入的参数都有问题，Application并不起任何作用！！
	 * domain创建时会自动创建根部门
	 * @throws Exception
	 */
	@Test
	public void testGetRootDepartmentByApplication() throws Exception {
		DomainVO domainVO = new DomainVO();
		domainVO.setId(Sequence.getSequence());

		DomainProcess domainProcess = (DomainProcess)ProcessFactory.createProcess(DomainProcess.class);
		domainProcess.doCreate(domainVO);
		
		DepartmentVO dept = (DepartmentVO)process.getRootDepartmentByApplication(getApplicationId(), domainVO.getId());
		assertNotNull(dept);
	}

	@Test
	public void testGetDepartmentByName() throws Exception {
		DomainVO domainVO = new DomainVO();
		domainVO.setId(Sequence.getSequence());

		DomainProcess domainProcess = (DomainProcess)ProcessFactory.createProcess(DomainProcess.class);
		domainProcess.doCreate(domainVO);
		
		DepartmentVO vo = new DepartmentVO();

		String id = Sequence.getSequence();
		vo.setId(id);
		vo.setName("testdepartment");

		vo.setApplicationid(getApplicationId());
		vo.setCode("code");
		vo.setLevel(0);
		
		vo.setDomain(domainVO);
		vo.setDomainid(domainVO.getId());
		
		// 测试新建
		process.doCreate(vo);
		
		Collection depts = process.getDepartmentByName("testdepartment", domainVO.getId());
		assertTrue(depts.size()==1);

	
		//测试queryByDomain
		depts = process.queryByDomain(domainVO.getId());
		assertTrue(depts.size()==2);//创建domain时，自动创建了一个根部门，加上新建的部门，所以有2个。
	}

	@Test
	public void testGetDepartmentByCode() throws Exception {
		DomainVO domainVO = new DomainVO();
		domainVO.setId(Sequence.getSequence());

		DomainProcess domainProcess = (DomainProcess)ProcessFactory.createProcess(DomainProcess.class);
		domainProcess.doCreate(domainVO);
		
		DepartmentVO vo = new DepartmentVO();

		String id = Sequence.getSequence();
		vo.setId(id);
		vo.setName("testdepartment");

		vo.setApplicationid(getApplicationId());
		vo.setCode("code");
		vo.setLevel(0);
		
		vo.setDomain(domainVO);
		vo.setDomainid(domainVO.getId());
		
		// 测试新建
		process.doCreate(vo);
		
		Collection depts = process.getDepartmentByCode("code", domainVO.getId());
		assertTrue(depts.size()==1);
	}

	@Ignore
	public void testGetSuperiorDeptListExcludeCurrent() {
	}

	@Ignore
	public void testGetUnderDeptListStringIntBoolean() {
	}

	@Ignore
	public void testQueryByUser() throws Exception {
	}

}
