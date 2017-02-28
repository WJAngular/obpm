package cn.myapps.core.user.action;

import java.util.Collection;

import junit.framework.TestCase;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;

public class UserActionTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(UserActionTest.class);
	}

	/*
	 * public void testDoSave() throws Exception{ HashMap params = new
	 * HashMap(); params.put("id",new String[]{"1164039651830001"}); UserAction
	 * action = new UserAction(); action.getContext().setParameters(params);
	 * 
	 * DepartmentProcess dp =
	 * (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
	 * try { action.doView();//new UserVO(); UserVO vo =
	 * (UserVO)action.getContent(); vo.setName("Jerry Jarod 2"); Collection dpts
	 * = dp.doSimpleQuery(null);
	 * vo.getDepartments().remove(vo.getDepartments().toArray()[0]);
	 * 
	 * action.setContent(vo); String rslt = action.doSave(); } catch (Exception
	 * e) { e.printStackTrace(); } PersistenceUtils.closeSession(); }
	 */

	public void testGetUserHasMail() throws Exception {
		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		Collection<UserVO> colls = up.doQueryHasMail(null);
		System.out.println(colls);
	}
}
