package cn.myapps.core.user.ejb;

import junit.framework.TestCase;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.util.ProcessFactory;

public class UserProcessBeanTest extends TestCase {
	UserProcess userProcess;
	DepartmentProcess deptProcess;

	protected void setUp() throws Exception {
		userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		deptProcess = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public UserProcess getUserProcess() {
		return userProcess;
	}

	public void setUserProcess(UserProcess userProcess) {
		this.userProcess = userProcess;
	}

	public DepartmentProcess getDeptProcess() {
		return deptProcess;
	}

	public void setDeptProcess(DepartmentProcess deptProcess) {
		this.deptProcess = deptProcess;
	}

	public void testQueryUserIdsByName() throws Exception{
		String userIds = userProcess.queryUserIdsByName("d1dev", "01b98ffe-bf66-0a40-a94c-4e7e79fc0c51");
		assertNotNull(userIds);
	}

//	public void testDoCreateValueObject() throws Exception {
//		// UserVO userVO = new UserVO();
//		// userVO.setId("U3");
//		DepartmentVO deptVO = (DepartmentVO) deptProcess.doView("01b99002-a23c-6280-9d8b-8a0df3feb314");
//		UserVO userVO = (UserVO) userProcess.doView("U3");
//
//		Set set = new HashSet();
//		UserDepartmentSet userDepartmentSet = new UserDepartmentSet(userVO.getId(), deptVO.getId());
//		set.add(userDepartmentSet);
//		// userVO.setUserDepartmentSets(set);
//		userVO.getUserDepartmentSets().clear();
//
//		userProcess.doUpdate(userVO);
//	}

	public void testDoRemoveString() {
		fail("Not yet implemented");
	}

	public void testDoRemoveStringArray() {
		fail("Not yet implemented");
	}

	public void testDoUpdateValueObject() {
		fail("Not yet implemented");
	}

	public void testDoView() {
		fail("Not yet implemented");
	}

}
