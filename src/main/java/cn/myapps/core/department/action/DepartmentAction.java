package cn.myapps.core.department.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentException;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.tree.Node;
import cn.myapps.core.user.action.OnlineUsers;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.xmpp.XMPPSender;
import cn.myapps.core.xmpp.notification.ContactNotification;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.ResponseUtil;
import cn.myapps.util.json.JsonUtil;


/**
 * @see cn.myapps.base.action.BaseAction DepartmentAction class.
 * @author Darvense
 * @since JDK1.4
 */
public class DepartmentAction extends BaseAction<DepartmentVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3873288664483872273L;

	// private List<FieldExtendsVO> fieldExtendses;// 扩展字段集合

	/**
	 * @uml.property name="_superiorid"
	 */
	private String _superiorid;

	/**
	 * @uml.property name="_deptSelect"
	 */
	private Collection<DepartmentVO> _deptSelect;

	// public Collection departments = null;

	/**
	 * @uml.property name="jSONStr"
	 */
	private String JSONStr;

	/**
	 * @uml.property name="checkedList"
	 */
	private String checkedList;
	protected Collection<Node> childNodes = new ArrayList<Node>();

	public String getSuperiorid() {
		return _superiorid;
	}

	public Collection<Node> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(Collection<Node> childNodes) {
		this.childNodes = childNodes;
	}

	/**
	 * Get the _superiorid
	 * 
	 * @return _superiorid
	 * @uml.property name="_superiorid"
	 */
	public String get_superiorid() {
		DepartmentVO content = (DepartmentVO) this.getContent();
		if (content.getSuperior() != null) {
			_superiorid = content.getSuperior().getId();
		} else {
			_superiorid = null;
		}

		return _superiorid;
	}

	/**
	 * Set _superiorid.
	 * 
	 * @param _superiorid
	 * @throws Exception
	 * @uml.property name="_superiorid"
	 */
	public void set_superiorid(String _superiorid) throws Exception {
		this._superiorid = _superiorid;
		if (_superiorid != null && _superiorid.trim().length() > 0) {
			DepartmentVO content = (DepartmentVO) this.getContent();
			DepartmentVO superior = (DepartmentVO) process.doView(_superiorid);
			if (superior != null) {
				content.setSuperior(superior);
				content.setLevel(superior.getLevel() + 1);
			}
		}
	}

	/**
	 * 
	 * DepartmentAction structure function.
	 * 
	 * @SuppressWarnings 工厂方法无法使用泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public DepartmentAction() throws Exception {
		super(ProcessFactory.createProcess(DepartmentProcess.class),
				new DepartmentVO());
	}

	/**
	 * Get the _departments.
	 * 
	 * @return _departments
	 * @throws Exception
	 */
	public Map<String, String> get_departments() throws Exception {
		DepartmentProcess dp = (DepartmentProcess) process;

		ParamsTable params = getParams();
		String domainId = params.getParameterAsString("domain");

		Collection<DepartmentVO> dc = dp.queryByDomain(domainId);
		Map<String, String> dm = ((DepartmentProcess) process)
				.deepSearchDepartmentTree(dc, null, getContent().getId(), 0);

		if (dm.isEmpty()) {
			dm.put("", "{*[none]*}");
		}

		return dm;

	}

	/**
	 * Select department.
	 * 
	 * @return If the action execution was successful.return "SUCCESS".Show an
	 *         success view .
	 *         <p>
	 *         If the action execution was a failure. return "ERROR".Show an
	 *         error view, possibly asking the user to retry entering data.
	 *         <p>
	 *         The "INPUT" is also used if the given input params are invalid,
	 *         meaning the user should try providing input again.
	 * 
	 * @throws Exception
	 */
	public String doSelect() throws Exception {
		_deptSelect = ((DepartmentProcess) process).getDepartmentByLevel(0,
				getApplication(), getUser().getDomainid());
		return SUCCESS;
	}

	public String doSelectAjax() throws Exception {
		return SUCCESS;
	}

	/**
	 * Get the _deptSelect.
	 * 
	 * @return _deptSelect
	 * @uml.property name="_deptSelect"
	 */
	public Collection<DepartmentVO> get_deptSelect() {
		return _deptSelect;
	}

	/**
	 * Set the _deptSelect
	 * 
	 * @param select
	 * @uml.property name="_deptSelect"
	 */
	public void set_deptSelect(Collection<DepartmentVO> select) {
		_deptSelect = select;
	}

	/**
	 * Delete a DepartmentVO.
	 * 
	 * @return If the action execution was successful.return "SUCCESS".Show an
	 *         success view .
	 *         <p>
	 *         If the action execution was a failure. return "ERROR".Show an
	 *         error view, possibly asking the user to retry entering data.
	 *         <p>
	 *         The "INPUT" is also used if the given input params are invalid,
	 *         meaning the user should try providing input again.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String doDelete() {
		try {
			String ErrorField = "";
			if (_selects != null) {
				Map request = (Map) ActionContext.getContext().get("request");
				request.put("operation", "delete");
				ParamsTable params = getParams();
				params.getParameter("id");
				// 获取上级部门id
				String parentid = params.getParameterAsString("parentid");
				if (parentid != null && !"".equals(parentid)) {
					this._superiorid = parentid;
				}
				for (int i = 0; i < _selects.length; i++) {
					String id = _selects[i];
					try {
						process.doRemove(id);
					}catch (OBPMValidateException e) {
						if(e.getCause() instanceof DepartmentException){
							DepartmentVO department = (DepartmentVO)(process.doView(id));
							//重构相同的错误信息
							if(ErrorField.indexOf(e.getValidateMessage())!=-1){
								ErrorField=ErrorField.substring(0, ErrorField.indexOf(e.getValidateMessage()))+"、\""+department.getName()+"\""+ErrorField.substring(ErrorField.indexOf(e.getValidateMessage()));
							}else{
								ErrorField = "\""+department.getName()+"\""+e.getValidateMessage() + "," + ErrorField;
							}
						}else{
							this.addFieldError("", e.getValidateMessage());
							return INPUT;
						}
					}catch (Exception e) {
						this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
						e.printStackTrace();
						return INPUT;
					}
				}

				if (!ErrorField.equals("")) {
					if (ErrorField.endsWith(",")) {
						ErrorField = ErrorField.substring(0, ErrorField
								.length() - 1);
					}
					this.addFieldError("1", ErrorField);
					return INPUT;
				}
				addActionMessage("{*[cn.myapps.core.domain.department.delete_successful]*}");
			}

		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		/**
		 * 增加了xmpp的消息发送,此消息将发送到obpm-spark的各个客户端
		 */
		sendNotification();
		return SUCCESS;

	}

	/**
	 * xmpp消息发送,部门的增删改将触发此动作
	 * 
	 * 通知所有的obpm-spark客户端更新企业联系人列表
	 * 
	 * @author keezzm
	 * @date 2011-08-17
	 * @last modified by keezzm
	 */
	private void sendNotification() {
		try {
			// 发送XMPP信息
			ContactNotification notification = ContactNotification
					.newInstance(ContactNotification.ACTION_UPDATE);
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			/**
			 * 默认发送者为admin
			 */
			notification.setSender(superUserProcess.getDefaultAdmin());
			/**
			 * 添加接收者,为所有在线用户
			 */
			DataPackage<WebUser> dataPackage = OnlineUsers
					.doQuery(new ParamsTable());
			Collection<WebUser> users = dataPackage.getDatas();
			for (Iterator<WebUser> iterator = users.iterator(); iterator
					.hasNext();) {
				WebUser webUser = iterator.next();
				notification.addReceiver(webUser);
			}
			XMPPSender.getInstance().processNotification(notification);
		} catch (OBPMValidateException e) {
			LOG.warn("XMPP Notification Error", e);
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			LOG.warn("XMPP Notification Error", e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
	}

	/**
	 * 删除树中选择的部门
	 * 
	 * @throws Exception
	 */
	public void doTreeDelete() throws Exception {
		String errorField = "";
		ParamsTable params = getParams();
		String domain = params.getParameterAsString("domain");
		String departmentids = params.getParameterAsString("departmentids");// 选中的部门id
		Boolean isRootdep = false;// 是否具有根节点
		Boolean isHaveChild = false;// 是否有子部门
		Boolean isHaveUser = false;// 是否有用户
		if (departmentids != null) {
			Collection<DepartmentVO> rootdepts = ((DepartmentProcess) process)
					.getDepartmentByLevel(0, getApplication(), domain);
			// departmentids.substring(0,departmentids.lastIndexOf(";"));
			String departmentid[] = departmentids.split(";");
			for (String id : departmentid) {
				for (Iterator<DepartmentVO> ite = rootdepts.iterator(); ite
						.hasNext();) {
					DepartmentVO dept = ite.next();
					if (id.equals(dept.getId())) {
						isRootdep = true;
					}
				}
			}

			if (isRootdep) { // 存在根节点
				HttpServletResponse response = ServletActionContext
						.getResponse();
				response.setContentType("text/xml;charset=utf-8");
				PrintWriter out;
				out = response.getWriter();
				out.print("rootdepartment");
				out.flush();
				out.close();
			} else {
				for (String id : departmentid) {
					if (((DepartmentProcess) process).getChildrenCount(id) > 0) {
						isHaveChild = true;
					}
				}
				if (isHaveChild) {// 存在子节点
					HttpServletResponse response = ServletActionContext
							.getResponse();
					response.setContentType("text/xml;charset=utf-8");
					PrintWriter out;
					out = response.getWriter();
					out.print("hasChild");
					out.flush();
					out.close();
				} else {
					for (String id : departmentid) {
						DepartmentVO department = (DepartmentVO) ((DepartmentProcess) process)
								.doView(id);
						// if (department != null && !"".equals(department)) {
						// if (department.getUsers().size() > 0) {
						// isHaveUser = true;
						// }
						// }
						if (department != null && department.getUsers() != null) {
							if (department.getUsers().size() > 0) {
								isHaveUser = true;
							}
						}
					}
					if (isHaveUser) {// 存在用户
						HttpServletResponse response = ServletActionContext
								.getResponse();
						response.setContentType("text/xml;charset=utf-8");
						PrintWriter out;
						out = response.getWriter();
						out.print("hasUser");
						out.flush();
						out.close();
					} else {// 不存在根节点，子节点，用户，则进行部门删除
						try {
							for (String id : departmentid) {
								process.doRemove(id);
							}
						}catch (OBPMValidateException e) {
							if(e.getCause() instanceof DepartmentException){
								addFieldError(errorField, e.getValidateMessage() + ","
										+ errorField);
							}else{
								this.addFieldError("", e.getValidateMessage());
							}
						}catch (Exception e) {
							this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
							e.printStackTrace();
						}
					}

				}
			}

		}

	}

	/**
	 * Save tempDepartment.
	 * 
	 * @return If the action execution was successful.return "SUCCESS".Show an
	 *         success view .
	 *         <p>
	 *         If the action execution was a failure. return "ERROR".Show an
	 *         error view, possibly asking the user to retry entering data.
	 *         <p>
	 *         The "INPUT" is also used if the given input params are invalid,
	 *         meaning the user should try providing input again.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String doSave() {
		try {
			ParamsTable params = getParams();
			String domainId = params.getParameterAsString("domain");

			// 获取扩展字段
			// FieldExtendsProcess fieldExtendsProcess = (FieldExtendsProcess)
			// ProcessFactory
			// .createProcess(FieldExtendsProcess.class);
			// fieldExtendses =
			// fieldExtendsProcess.queryFieldExtendsByTable(domainId,
			// FieldExtendsVO.TABLE_DEPT);

			Map request = (Map) ActionContext.getContext().get("request");
			request.put("operation", "save");
			DepartmentVO tempDepartment = (DepartmentVO) (this.getContent());
			tempDepartment.setApplicationid("");
			
			if(tempDepartment.getValid() == 0 && !tempDepartment.getUsers().isEmpty()){
				StringBuffer usernames = new StringBuffer();
				for(Iterator<UserVO> it = tempDepartment.getUsers().iterator(); it.hasNext();){
					UserVO tempUser = it.next();
					if(tempUser.getDefaultDepartment().equals(tempDepartment.getId())){
						usernames.append(tempUser.getName()).append(",");
					}
				}
				if(usernames.length() > 1){
					usernames.setLength(usernames.length() - 1);
				}
				if(!StringUtil.isBlank(usernames.toString())){
					throw new OBPMValidateException("该部门下（" + usernames + "）已设置该部门为默认部门，请您先到用户列表把对应的用户的默认部门更改后再进行此操作");
				}
			}

			boolean flag = false;
			String tempname = tempDepartment.getName();
			DepartmentVO superior = tempDepartment.getSuperior();
			Collection<DepartmentVO> departments = ((DepartmentProcess) process)
					.getDepartmentByName(tempname, domainId);
			for (Iterator<DepartmentVO> iter = departments.iterator(); iter
					.hasNext();) {
				DepartmentVO dp = (DepartmentVO) iter.next();

				if (tempDepartment.getId() == null) {
					if (superior != null
							&& dp.getSuperior() != null
							&& dp.getSuperior().getId()
									.equals(superior.getId())) {
						this.addFieldError("1", "{*[cn.myapps.core.domain.department.exist]*}");
						flag = true;
					} else if (superior == null && dp.getSuperior() == null) {
						this.addFieldError("1", "{*[cn.myapps.core.domain.department.exist]*}");
						flag = true;
					}
				} else {
					if (!tempDepartment.getId().equals(dp.getId())
							&& superior != null
							&& dp.getSuperior() != null
							&& dp.getSuperior().getId()
									.equals(superior.getId())) {
						this.addFieldError("1", "{*[cn.myapps.core.domain.department.exist]*}");
						flag = true;
					} else if (!tempDepartment.getId().equals(dp.getId())
							&& superior == null && dp.getSuperior() == null) {
						this.addFieldError("1", "{*[cn.myapps.core.domain.department.exist]*}");
						flag = true;
					}

				}

			}
			if (!flag) {

				if (domainId != null && domainId.trim().length() > 0) {
					DomainProcess dp = (DomainProcess) ProcessFactory
							.createProcess(DomainProcess.class);
					DomainVO domain = (DomainVO) dp.doView(domainId);
					tempDepartment.setDomain(domain);
					tempDepartment.setDomainid(domain.getId());

				}
			}

			else {
				return INPUT;
			}
			setContent(tempDepartment);

		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		String rtn = super.doSave();
		if(SUCCESS.equals(rtn)){
			/**
			 * 增加了xmpp的消息发送,此消息将发送到obpm-spark的各个客户端
			 */
			sendNotification();
		}
		return rtn;
	}

	/**
	 * @SuppressWarnings Servlet API不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public String doList() {
		try {
			ParamsTable params = getParams();
			int lines = 10;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					lines = Integer.parseInt(cookie.getValue());
				}
			    cookie.getName();
			    cookie.getValue();
			}
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);
			String domain = params.getParameterAsString("domain");
			if (params.getParameter("application") != null) {
				params.removeParameter("application");
			}
			if(StringUtil.isBlank( params.getParameterAsString("_orderby"))){
				   params.setParameter("_orderby", "orderByNo;id");
			}

			// 获取可见的扩展字段
			// FieldExtendsProcess fieldExtendsProcess = (FieldExtendsProcess)
			// ProcessFactory
			// .createProcess(FieldExtendsProcess.class);
			// fieldExtendses =
			// fieldExtendsProcess.queryFieldExtendsByTableAndEnabel(domain,
			// FieldExtendsVO.TABLE_DEPT,
			// true);
			// 注意扩展字段的位置
			this.validateQueryParams();
			if (domain != null && domain.trim().length() > 0) {
				params.setParameter("t_domain.id", domain);

				WebUser user = getUser();
				setDatas(process.doQuery(params, user));
			}

			String id = params.getParameterAsString("t_superior.id");
			Map request = (Map) ActionContext.getContext().get("request");
			request.put("parentid", id);
			// // 设置要显示在列表中的扩展字段的值
			// addFieldExtendsValue();
			setApplication("");
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}

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
			node.put("page", contextPath + "/core/department/subNodes.action");

			nodeList.add(node);
		}
		setJSONStr(JsonUtil.collection2Json(nodeList));

		return SUCCESS;
	}

	public String getNodes() throws Exception {
		ParamsTable params = getParams();
		String parentid = params.getParameterAsString("parent");
		parentid = parentid.substring(1, parentid.length());
		DepartmentVO parent = (DepartmentVO) ((DepartmentProcess) process)
				.doView(parentid);
		String JSONStr = getDeptsJSON(parent);
		setJSONStr(JSONStr);

		return SUCCESS;
	}

	private String getDeptsJSON(DepartmentVO parent) throws Exception {
		DepartmentProcess dp = (DepartmentProcess) process;
		StringBuffer JSONbuffer = new StringBuffer();
		if (parent == null) {
			DepartmentVO root = dp.getRootDepartmentByApplication(
					getApplication(), getUser().getDomainid());
			JSONbuffer.append("[");
			JSONbuffer.append(getNodeJSON(root));
			JSONbuffer.deleteCharAt(JSONbuffer.lastIndexOf(","));
			JSONbuffer.append("]");
		} else {
			Collection<ValueObject> all = new ArrayList<ValueObject>();
			all.addAll(dp.getDatasByParent(parent.getId()));
			all.addAll(parent.getUsers());
			JSONbuffer.append("[");
			for (Iterator<ValueObject> iter = all.iterator(); iter.hasNext();) {
				ValueObject vo = iter.next();
				JSONbuffer.append(getNodeJSON(vo));
			}
			JSONbuffer.deleteCharAt(JSONbuffer.lastIndexOf(","));
			JSONbuffer.append("]");
		}
		return JSONbuffer.toString();
	}

	private String getNodeJSON(ValueObject node) throws Exception {
		StringBuffer JSONbuffer = new StringBuffer();
		String ctxPath = ServletActionContext.getRequest().getContextPath();
		if (ctxPath.equals("/")) {
			ctxPath = "";
		}
		String id = "";
		String name = "";
		String icon = "";
		// boolean isLeaf = true;
		// long start = System.currentTimeMillis();
		if (node instanceof UserVO) {
			UserVO user = (UserVO) node;
			id = "U" + user.getId();
			name = user.getName();
			icon = ctxPath + "/resource/images/dtree/user.gif";
		} else if (node instanceof DepartmentVO) {
			DepartmentVO dept = (DepartmentVO) node;
			id = "D" + dept.getId();
			name = dept.getName();
			icon = ctxPath + "/resource/images/dtree/dept.gif";
			// isLeaf = isLeaf(dept);
		} else if (node instanceof RoleVO) {
			RoleVO role = (RoleVO) node;
			id = "R" + role.getId();
			name = role.getName();
			icon = ctxPath + "/resource/images/dtree/group.gif";
		}
		JSONbuffer.append("{");
		JSONbuffer.append("text:'" + name + "',");
		JSONbuffer.append("id:'" + id + "',");
		// JSONbuffer.append("leaf:" + isLeaf + ",");
		JSONbuffer.append("icon:'" + icon + "',");
		JSONbuffer.append("checked:" + (checkedList.indexOf(id) != -1));
		JSONbuffer.append("},");
		return JSONbuffer.toString();
	}

	/**
	 * is leaf or not
	 * 
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	/*
	 * private boolean isLeaf(DepartmentVO parent) throws Exception { boolean
	 * rtn = true; DepartmentProcess dp = (DepartmentProcess) ProcessFactory
	 * .createProcess(DepartmentProcess.class); long count =
	 * dp.getChildrenCount(parent.getId()) + parent.getRoles().size(); if (count
	 * > 0) { rtn = false; } return rtn; }
	 */

	/**
	 * @return the jSONStr
	 * @uml.property name="jSONStr"
	 */
	public String getJSONStr() {
		return JSONStr;
	}

	/**
	 * @param jSONStr
	 *            the jSONStr to set
	 * @uml.property name="jSONStr"
	 */
	public void setJSONStr(String str) {
		JSONStr = str;
	}

	/**
	 * @return the checkedList
	 * @uml.property name="checkedList"
	 */
	public String getCheckedList() {
		return checkedList;
	}

	/**
	 * @param checkedList
	 *            the checkedList to set
	 * @uml.property name="checkedList"
	 */
	public void setCheckedList(String checkedList) {
		this.checkedList = checkedList;
	}

	/**
	 * Save tempDepartment.
	 * 
	 * @SuppressWarnings Servlet API不支持泛型
	 * @return If the action execution was successful.return "SUCCESS".Show an
	 *         success view .
	 *         <p>
	 *         If the action execution was a failure. return "ERROR".Show an
	 *         error view, possibly asking the user to retry entering data.
	 *         <p>
	 *         The "INPUT" is also used if the given input params are invalid,
	 *         meaning the user should try providing input again.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String doSaveAndNew() {
		try {
			ParamsTable params = getParams();
			String domainId = params.getParameterAsString("domain");

			// 获取扩展字段
			// FieldExtendsProcess fieldExtendsProcess = (FieldExtendsProcess)
			// ProcessFactory
			// .createProcess(FieldExtendsProcess.class);
			// fieldExtendses =
			// fieldExtendsProcess.queryFieldExtendsByTable(domainId,
			// FieldExtendsVO.TABLE_DEPT);

			Map request = (Map) ActionContext.getContext().get("request");
			request.put("operation", "save");
			DepartmentVO tempDepartment = (DepartmentVO) (this.getContent());

			if(tempDepartment.getValid() == 0 && !tempDepartment.getUsers().isEmpty()){
				StringBuffer usernames = new StringBuffer();
				for(Iterator<UserVO> it = tempDepartment.getUsers().iterator(); it.hasNext();){
					UserVO tempUser = it.next();
					if(tempUser.getDefaultDepartment().equals(tempDepartment.getId())){
						usernames.append(tempUser.getName()).append(",");
					}
				}
				if(usernames.length() > 1){
					usernames.setLength(usernames.length() - 1);
				}
				if(!StringUtil.isBlank(usernames.toString())){
					throw new OBPMValidateException("该部门下（" + usernames + "）已设置该部门为默认部门，请您先到用户列表把对应的用户的默认部门更改后再进行此操作");
				}
			}
			
			boolean flag = false;
			String tempname = tempDepartment.getName();
			DepartmentVO superior = tempDepartment.getSuperior();
			Collection<DepartmentVO> departments = ((DepartmentProcess) process)
					.getDepartmentByName(tempname, domainId);
			for (Iterator<DepartmentVO> iter = departments.iterator(); iter
					.hasNext();) {
				DepartmentVO dp = (DepartmentVO) iter.next();

				if (tempDepartment.getId() == null) {
					if (superior != null
							&& dp.getSuperior() != null
							&& dp.getSuperior().getId()
									.equals(superior.getId())) {
						this.addFieldError("1", "{*[cn.myapps.core.domain.department.exist]*}");
						flag = true;
					} else if (superior == null && dp.getSuperior() == null) {
						this.addFieldError("1", "{*[cn.myapps.core.domain.department.exist]*}");
						flag = true;
					}
				} else {
					if (!tempDepartment.getId().equals(dp.getId())
							&& superior != null
							&& dp.getSuperior() != null
							&& dp.getSuperior().getId()
									.equals(superior.getId())) {
						this.addFieldError("1", "{*[cn.myapps.core.domain.department.exist]*}");
						flag = true;
					} else if (!tempDepartment.getId().equals(dp.getId())
							&& superior == null && dp.getSuperior() == null) {
						this.addFieldError("1", "{*[cn.myapps.core.domain.department.exist]*}");
						flag = true;
					}
				}
			}
			if (!flag) {

				if (domainId != null && domainId.trim().length() > 0) {
					DomainProcess dp = (DomainProcess) ProcessFactory
							.createProcess(DomainProcess.class);
					DomainVO domain = (DomainVO) dp.doView(domainId);
					tempDepartment.setDomain(domain);
					tempDepartment.setDomainid(domain.getId());

				}
			} else {
				return INPUT;
			}

			if (tempDepartment.getId() == null
					|| tempDepartment.getId().equals("")) {
				process.doCreate(tempDepartment);
			} else {
				process.doUpdate(tempDepartment);
			}
			this.setContent(new DepartmentVO());
			this.addActionMessage("{*[cn.myapps.core.domain.department.save_success]*}");

			/**
			 * 增加了xmpp的消息发送,此消息将发送到obpm-spark的各个客户端
			 */
			sendNotification();
			return SUCCESS;

		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}

	/**
	 * 鏄剧ず閮ㄩ棬鏍�
	 * 
	 * @throws Exception
	 */
	public void departTree() throws Exception {

		ParamsTable params = getParams();
		String parentid = params.getParameterAsString("parentid");
		String domain = params.getParameterAsString("domain");
		try {
			if (parentid == null || "".equals(parentid)) {
				Collection<DepartmentVO> depts = ((DepartmentProcess) process)
						.getDepartmentByLevel(0, getApplication(), domain);
				for (Iterator<DepartmentVO> ite = depts.iterator(); ite
						.hasNext();) {
					DepartmentVO dept = ite.next();
					if(dept.getValid() == 1){
						Node node = new Node();
						node.setId(dept.getId());
						node.setData(dept.getName());
						node.addAttr("name", dept.getName());
						if (((DepartmentProcess) process).getChildrenCount(dept
								.getId()) > 0) {
							node.setState(Node.STATE_CLOSED);
						}
						childNodes.add(node);
					}
				}
				ResponseUtil.setJsonToResponse(ServletActionContext
						.getResponse(), JsonUtil.collection2Json(childNodes));
			} else {
				Collection<DepartmentVO> depts = ((DepartmentProcess) process)
						.getDatasByParent(parentid);
				for (Iterator<DepartmentVO> ite = depts.iterator(); ite
						.hasNext();) {
					DepartmentVO dept = ite.next();
					if(dept.getValid() == 1){
						Node node = new Node();
						node.setId(dept.getId());
						node.setData(dept.getName());
						node.addAttr("name", dept.getName());
						if (((DepartmentProcess) process).getChildrenCount(dept
								.getId()) > 0) {
							node.setState(Node.STATE_CLOSED);
						}
						childNodes.add(node);
					}
				}
				ResponseUtil.setJsonToResponse(ServletActionContext
						.getResponse(), JsonUtil.collection2Json(childNodes));
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
	}
	
	
	public void getDepartmentTreeByParent() throws Exception {

		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		ParamsTable params = getParams();
		String parentid = params.getParameterAsString("parentid");
		String domain = params.getParameterAsString("domain");
		String _departmentids = params.getParameterAsString("_departmentids");
		try {
			if (parentid == null || "".equals(parentid)) {
				Collection<DepartmentVO> depts = ((DepartmentProcess) process)
						.getDepartmentByLevel(0, getApplication(), domain);
				for (Iterator<DepartmentVO> ite = depts.iterator(); ite
						.hasNext();) {
					DepartmentVO dept = ite.next();
					if(dept.getValid() == 1){
						Map<String, Object> node = new HashMap<String, Object>();
						node.put("id", dept.getId());
						node.put("text", dept.getName());
						node.put("type", "root");
						Map<String, Object> state = new HashMap<String, Object>();
						if (((DepartmentProcess) process).getChildrenCount(dept
								.getId()) > 0) {
							//state.put("opened", true);
							node.put("children", true);
						}else{
							state.put("opened", false);
						}
						if(_departmentids != null && _departmentids.indexOf(dept.getId())>=0){
							state.put("selected", true);
						}else{
							state.put("selected", false);
						}
						node.put("state", state);
						
						list.add(node);
					}
				}
				ResponseUtil.setJsonToResponse(ServletActionContext
						.getResponse(), JsonUtil.collection2Json(list));
			} else {
				Collection<DepartmentVO> depts = ((DepartmentProcess) process)
						.getDatasByParent(parentid);
				for (Iterator<DepartmentVO> ite = depts.iterator(); ite
						.hasNext();) {
					DepartmentVO dept = ite.next();
					if(dept.getValid() == 1){
						Map<String, Object> node = new HashMap<String, Object>();
						node.put("id", dept.getId());
						node.put("text", dept.getName());
						
						Map<String, Object> state = new HashMap<String, Object>();
						if (((DepartmentProcess) process).getChildrenCount(dept
								.getId()) > 0) {
							//state.put("opened", true);
							node.put("children", true);
						}else{
							state.put("opened", false);
						}
						if(_departmentids != null && _departmentids.indexOf(dept.getId())>=0){
							state.put("selected", true);
						}else{
							state.put("selected", false);
						}
						node.put("state", state);
						list.add(node);
					}
				}
				ResponseUtil.setJsonToResponse(ServletActionContext
						.getResponse(), JsonUtil.collection2Json(list));
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
	}

	/**
	 * 显示添加页面
	 */
	public String doNew() {
		try {
			// ParamsTable params = getParams();
			// String domain = params.getParameterAsString("domain");
			// 获取扩展字段
			// FieldExtendsProcess fieldExtendsProcess = (FieldExtendsProcess)
			// ProcessFactory
			// .createProcess(FieldExtendsProcess.class);
			// fieldExtendses =
			// fieldExtendsProcess.queryFieldExtendsByTable(domain,
			// FieldExtendsVO.TABLE_DEPT);
			return super.doNew();
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 显示修改页面
	 */
	public String doEdit() {
		// try {
		// ParamsTable params = getParams();
		// String domain = params.getParameterAsString("domain");
		// // 获取扩展字段
		// FieldExtendsProcess fieldExtendsProcess = (FieldExtendsProcess)
		// ProcessFactory
		// .createProcess(FieldExtendsProcess.class);
		// fieldExtendses = fieldExtendsProcess.queryFieldExtendsByTable(domain,
		// FieldExtendsVO.TABLE_DEPT);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return super.doEdit();
	}

	// public List<FieldExtendsVO> getFieldExtendses() {
	// return fieldExtendses;
	// }
	//
	// public void setFieldExtendses(List<FieldExtendsVO> fieldExtendses) {
	// this.fieldExtendses = fieldExtendses;
	// }

	// public String toJSON() {
	// return JsonUtil.collection2Json(childNodes);
	// }
	//	

	// /**
	// * 设置要显示在列表中的扩展字段的值
	// */
	// @SuppressWarnings("unchecked")
	// private void addFieldExtendsValue() {
	// try {
	// // 获取当前人员列表集合
	// Collection departmentCollection = getDatas().getDatas();
	//
	// // 设置扩展字段值
	// for (Object object : departmentCollection) {
	// DepartmentVO departmentVO = (DepartmentVO) object;
	//
	// for (FieldExtendsVO field : fieldExtendses) {
	// // 获取扩展字段名
	// String fieldName = field.getName();
	// String firstLetter = fieldName.substring(0, 1);
	// fieldName = fieldName.replaceFirst(firstLetter,
	// firstLetter.toUpperCase());
	//
	// // 根据字段名，动态执行相应的get方法获取该字段的值
	// Method method = DepartmentVO.class.getMethod("get" + fieldName);
	// Object result = method.invoke(departmentVO);
	// if (result != null)
	// departmentVO.getFieldExtendsValues().add(result.toString());
	// else
	// departmentVO.getFieldExtendsValues().add("");
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
