package cn.myapps.core.department.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.web.DWRHtmlUtils;

public class DepartmentHelper {

	public Collection<DepartmentVO> getDepartmentList(String domainid) throws Exception {
		DepartmentProcess dp = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		return dp.queryByDomain(domainid);
	}

	public Collection<UserVO> getUsersByDptIdAndRoleId(String deptid, String roleid) throws Exception {
		UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		if (roleid != null && roleid.trim().length() > 0)
			return up.queryByDptIdAndRoleId(deptid, roleid);
		else
			return up.queryByDepartment(deptid);

	}

	/**
	 * 生成树选择表单
	 * 
	 * @param selectedList
	 * @return
	 * @throws Exception
	 */
	public String getExpandPath(String selectedList) throws Exception {
		StringBuffer buffer = new StringBuffer();

		// DepartmentProcess dp = (DepartmentProcess) ProcessFactory
		// .createProcess(DepartmentProcess.class);
		RoleProcess rp = (RoleProcess) ProcessFactory.createProcess(RoleProcess.class);
		// UserProcess up = (UserProcess) ProcessFactory
		// .createProcess(UserProcess.class);

		if (selectedList != null && selectedList.trim().length() > 0) {
			String[] selecteds = selectedList.split(";");
			if (selecteds.length > 0) {
				buffer.append("[");
				for (int i = 0; i < selecteds.length; i++) {
					char prefix = selecteds[i].charAt(0);
					String id = selecteds[i].substring(1, selecteds[i].length());
					switch (prefix) {
					// case 'D':
					// DepartmentVO dept = (DepartmentVO) dp.doView(id);
					// buffer.append("\"" + getPathStr(dept, false) + "\"");
					// break;

					case 'R':
						RoleVO role = (RoleVO) rp.doView(id);

						buffer.append("\"" + getPathStr(role.getApplicationid(), true) + "\"");
						break;

					// case 'U':
					// UserVO user = (UserVO) up.doView(id);
					// buffer.append(getPathStr(user.getDepartments(), true));
					// break;
					default:
						break;
					}
					buffer.append(",");

				}
				buffer.deleteCharAt(buffer.lastIndexOf(","));
				buffer.append("]");
			}
		}
		return buffer.toString();
	}

	private String getPathStr(String applicationid, boolean includeCurrent) throws Exception {
		StringBuffer buffer = new StringBuffer();
		RoleProcess process = (RoleProcess) ProcessFactory.createProcess(RoleProcess.class);
		Collection<RoleVO> roles = process.getRolesByApplication(applicationid);
		if (roles != null && !roles.isEmpty()) {
			for (Iterator<RoleVO> iterator = roles.iterator(); iterator.hasNext();) {
				Object obj = (Object) iterator.next();
				buffer.append("/D" + obj.toString());
			}
		}
		if (includeCurrent) {
			buffer.append("/A" + applicationid);
		}
		return buffer.toString();
	}

	public String getDepartmentAll(String domain, String targetFieldName, String defValue) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		DepartmentProcess ph = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		Collection<DepartmentVO> deptList = ph.queryByDomain(domain);
		if (deptList != null && deptList.size() > 0) {
			for (Iterator<DepartmentVO> iterator = deptList.iterator(); iterator.hasNext();) {
				DepartmentVO vo = (DepartmentVO) iterator.next();
				map.put(vo.getId(), vo.getName());
			}
		}
		return DWRHtmlUtils.createOptions(map, targetFieldName, defValue);
	}

	public String queryDeptNamesByIdString(String idString) throws Exception {
		DepartmentProcess ph = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		String[] idArray = idString.split(",");
		StringBuffer deptNameString = new StringBuffer();
		if (idArray.length > 0) {
			for (int i = 0; i < idArray.length; i++) {
				String id = idArray[i];
				DepartmentVO dept = (DepartmentVO) ph.doView(id);
				deptNameString.append(dept.getName() + ",");
			}
			if (deptNameString.length() > 0) {
				deptNameString.deleteCharAt(deptNameString.lastIndexOf(","));
			}
		}
		return deptNameString.toString();
	}
	
	public Collection<DepartmentVO> getDepartmentListByUser(WebUser user) throws Exception {
		Collection<DepartmentVO> list = new ArrayList<DepartmentVO>();
		
		DepartmentProcess dp = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		
		list.addAll(dp.getSuperiorDeptList(user.getDefaultDepartment()));
		list.addAll(dp.getUnderDeptList(user.getDefaultDepartment()));
		
		return list;

	}
}
