package cn.myapps.core.department.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.util.json.JsonUtil;

public class DepartmentFrontAction extends DepartmentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7896437538357702035L;

	public DepartmentFrontAction() throws Exception {
		super();
	}

	@Override
	public String getSubNodes() throws Exception {
		String pid = getParams().getParameterAsString("pid");
		Collection<DepartmentVO> subDepts = ((DepartmentProcess) process)
				.getUnderDeptList(pid, 1);

		Collection<Map<String, String>> nodeList = new ArrayList<Map<String, String>>();
		// id,pid,name,url,title,target,icon,iconOpen,open,checked,page
		String contextPath = getParams().getContextPath();
		for (Iterator<DepartmentVO> iterator = subDepts.iterator(); iterator
				.hasNext();) {
			DepartmentVO dept = iterator.next();
			Map<String, String> node = new HashMap<String, String>();
			node.put("id", dept.getId());
			node.put("pid", pid);
			node.put("name", dept.getName());
			node.put("url", "");
			node.put("title", dept.getId());
			node.put("target", "");
			node.put("icon", contextPath + "/resource/images/dtree/dept.gif");
			node.put("iconOpen", contextPath
					+ "/resource/images/dtree/dept.gif");
			node.put("open", "");
			node.put("checked", "");
			node.put("page", contextPath + "/portal/department/subNodes.action");

			nodeList.add(node);
		}
		setJSONStr(JsonUtil.collection2Json(nodeList));

		return SUCCESS;
	}
	
}
