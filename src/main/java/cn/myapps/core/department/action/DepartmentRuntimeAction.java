package cn.myapps.core.department.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.tree.Node;
import cn.myapps.util.http.ResponseUtil;
import cn.myapps.util.json.JsonUtil;


/**
 * @see cn.myapps.base.action.BaseAction DepartmentAction class.
 * @author Darvense
 * @since JDK1.4
 */
public class DepartmentRuntimeAction extends DepartmentAction {
	private static final Logger LOG = Logger.getLogger(DepartmentRuntimeAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -3096692954428344339L;

	public DepartmentRuntimeAction() throws Exception {
		super();
	}

	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}

	/**
	 * 显示部门树
	 * 
	 * @throws Exception
	 */
	public void departTree() {

		ParamsTable params = getParams();
		String parentid = params.getParameterAsString("parentid");
		String domain = params.getParameterAsString("domain");
		try {
			if (parentid == null || "".equals(parentid)) {
				Collection<DepartmentVO> depts = ((DepartmentProcess) process).getDepartmentByLevel(0,
						getApplication(), domain);
				for (Iterator<DepartmentVO> ite = depts.iterator(); ite.hasNext();) {
					DepartmentVO dept = ite.next();
					if(dept.getValid() == 1 && (dept.getSuperior() == null || (dept.getSuperior() != null && dept.getSuperior().getValid() == 1))){
						Node node = new Node();
						node.setId(dept.getId());
						node.setData(dept.getName());
						node.addAttr("name", dept.getName());
						if (((DepartmentProcess) process).getChildrenCount(dept.getId()) > 0) {
							node.setState(Node.STATE_CLOSED);
						}
						childNodes.add(node);
					}
				}
				ResponseUtil
						.setJsonToResponse(ServletActionContext.getResponse(), JsonUtil.collection2Json(childNodes));
			} else {
				Collection<DepartmentVO> depts = ((DepartmentProcess) process).getDatasByParent(parentid);
				for (Iterator<DepartmentVO> ite = depts.iterator(); ite.hasNext();) {
					DepartmentVO dept = ite.next();
					if(dept.getValid() == 1 && (dept.getSuperior() == null || (dept.getSuperior() != null && dept.getSuperior().getValid() == 1))){
						Node node = new Node();
						node.setId(dept.getId());
						node.setData(dept.getName());
						node.addAttr("name", dept.getName());
						if (((DepartmentProcess) process).getChildrenCount(dept.getId()) > 0) {
							node.setState(Node.STATE_CLOSED);
						}
						childNodes.add(node);
					}
				}
				ResponseUtil
						.setJsonToResponse(ServletActionContext.getResponse(), JsonUtil.collection2Json(childNodes));
			}
		} catch (OBPMValidateException e) {
			LOG.error("departTree", e);
			this.addFieldError("1", e.getValidateMessage());
		} catch (Exception e) {
			LOG.error("departTree", e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
	}
}
