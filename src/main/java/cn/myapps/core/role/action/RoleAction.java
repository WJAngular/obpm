package cn.myapps.core.role.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

//TODO: 分出一个RoleRuntimeAction供前台使用，另外拼接HTML字符串的逻辑应该由JSP完成
public class RoleAction extends BaseAction<RoleVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2005136705776969715L;

	private String jsonStr;

	private String checkedList;
	
	private String tempRoles;

	/**
	 * 顶级菜单ID列表
	 */
	private Collection<String> _topresourcelist;

	public Collection<String> get_topresourcelist() {
		RoleVO role = (RoleVO) getContent();
		_topresourcelist = new HashSet<String>();
		
		try {
			ResourceProcess process = (ResourceProcess)ProcessFactory.createProcess(ResourceProcess.class);
		if (role.getId() != null && role.getId().trim().length() > 0) {
			Collection<PermissionVO> col = role.getPermission();
			Iterator<PermissionVO> it = col.iterator();
			while (it.hasNext()) {
				PermissionVO per = (PermissionVO) it.next();
				if (!StringUtil.isBlank(per.getResourceId())) {
					ResourceVO currentResource = (ResourceVO) process.doView(per.getResourceId());
					while (currentResource.getSuperior() != null) {
						currentResource = currentResource.getSuperior();
					}
					_topresourcelist.add(currentResource.getId());
				}
			}
		}
		} catch (ClassNotFoundException e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _topresourcelist;
	}

	public void set_topresourcelist(Collection<String> _topresourcelist) {
		this._topresourcelist = _topresourcelist;
	}

	public String doSelectAjax() throws Exception {
		return SUCCESS;
	}

	/**
	 * @SuppressWarnings 工厂方法创建的业务类，无法使用泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public RoleAction() throws Exception {
		super(ProcessFactory.createProcess(RoleProcess.class), new RoleVO());
	}

	/**
	 * 保存
	 */
	public String doSave() {

		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory
					.createProcess(PermissionProcess.class);
			RoleVO tempRoleVO = (RoleVO) (this.getContent());
			boolean flag = false;
			String tempname = tempRoleVO.getName();
			RoleVO role = ((RoleProcess) process).doViewByName(tempname, application);
			
			if(tempRoleVO.getRoleNo() != null && !tempRoleVO.getRoleNo().equals("")){
				ParamsTable p = new ParamsTable();
				p.setParameter("t_roleno", tempRoleVO.getRoleNo());
				Collection<RoleVO> rlvo_list = ((RoleProcess) process)
						.doSimpleQuery(p, tempRoleVO.getApplicationid());
				if (!rlvo_list.isEmpty()) {
					RoleVO rlvo = rlvo_list.iterator().next();
					if (!rlvo.getId().equals(tempRoleVO.getId())) {
						throw new OBPMValidateException("编号已存在！");
					}
				}
			}

			if (role != null) {
				if (tempRoleVO.getId() == null || tempRoleVO.getId().trim().length() <= 0) {// 判断新建不能重名
					this.addFieldError("1", "{*[core.role.exist]*}");
					flag = true;
				} else if (!tempRoleVO.getId().trim().equalsIgnoreCase(role.getId())) {// 修改不能重名
					this.addFieldError("1", "{*[core.role.exist]*}");
					flag = true;
				}
			}

			if (!flag) {
				if (tempRoleVO.getId() != null) {
					Collection<PermissionVO> permissions = permissionProcess.doQueryByRole(tempRoleVO.getId());
					Set<PermissionVO> set = new HashSet<PermissionVO>();
					for (Iterator<PermissionVO> it = permissions.iterator(); it.hasNext();) {
						set.add(it.next());
					}
					DataPackage<UserVO> users = userProcess.doQueryByRoleId(tempRoleVO.getId());
					tempRoleVO.setPermission(set);
					tempRoleVO.setUsers(users.getDatas());
					this.setContent(tempRoleVO);
				}
				return super.doSave();
			} else {
				return INPUT;
			}
		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/** 保存并新建 */
	public String doSaveAndNew() {
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory
					.createProcess(PermissionProcess.class);
			RoleVO tempRoleVO = (RoleVO) (this.getContent());
			boolean flag = false;
			String tempname = tempRoleVO.getName();
			RoleVO role = ((RoleProcess) process).doViewByName(tempname, application);

			if (role != null) {
				if (tempRoleVO.getId() == null || tempRoleVO.getId().trim().length() <= 0) {// 判断新建不能重名
					this.addFieldError("1", "{*[core.role.exist]*}");
					flag = true;
				} else if (!tempRoleVO.getId().trim().equalsIgnoreCase(role.getId())) {// 修改不能重名
					this.addFieldError("1", "{*[core.role.exist]*}");
					flag = true;
				}
			}

			if (!flag) {
				if (tempRoleVO.getId() == null || tempRoleVO.getId().equals("")) {
					process.doCreate(tempRoleVO);
				} else {
					Collection<PermissionVO> permissions = permissionProcess.doQueryByRole(tempRoleVO.getId());
					Set<PermissionVO> set = new HashSet<PermissionVO>();
					for (Iterator<PermissionVO> it = permissions.iterator(); it.hasNext();) {
						set.add(it.next());
					}
					DataPackage<UserVO> users = userProcess.doQueryByRoleId(tempRoleVO.getId());
					tempRoleVO.setPermission(set);
					tempRoleVO.setUsers(users.getDatas());
					this.setContent(tempRoleVO);
					process.doUpdate(tempRoleVO);
				}
				this.addActionMessage("{*[Save_Success]*}");
				setContent(new RoleVO());
				return SUCCESS;
			} else {
				return INPUT;
			}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String getNodes() {
		try {
			String applicationid = getApplication();
			String JSONStr = getRoleJSON(applicationid);
			

			this.setJsonStr(JSONStr);
		} catch (OBPMValidateException e) {
			addFieldError("getNodes", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}

	public String getRoleJSON(String application) throws Exception {
		StringBuffer builder = new StringBuffer();
		ApplicationProcess actionProcess = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
		RoleProcess rp = (RoleProcess) process;
		ApplicationVO appvo = (ApplicationVO) actionProcess.doView(application);
		if (application == null || application.trim().length() <= 0) {
			builder.append("[");
			builder.append(getNodeJSON(appvo));
			builder.deleteCharAt(builder.lastIndexOf(","));
			builder.append("]");
		} else {
			Collection<RoleVO> all = new ArrayList<RoleVO>();
			all.addAll(rp.getRolesByApplication(application));
			builder.append("[");
			for (Iterator<RoleVO> iter = all.iterator(); iter.hasNext();) {
				ValueObject vo = (ValueObject) iter.next();
				builder.append(getNodeJSON(vo));
			}
			builder.deleteCharAt(builder.lastIndexOf(","));
			builder.append("]");
		}

		return "";
	}

	public String doRolesList() {
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = this.getParams();
			params.setParameter("_pagelines", 10);
			params.setParameter("_orderby", "name");

			String applicationId = params.getParameterAsString("application");
			String domainId = params.getParameterAsString("domainid");
			
			html.append("<div>");
			//根据 applicationId 对软件用户进行筛选，若applicationId为空，则显示所有用户（兼容旧数据）
			if(applicationId.equalsIgnoreCase("null") || StringUtil.isBlank(applicationId) || applicationId == "" ){
				ApplicationHelper ah = new ApplicationHelper();
				Collection<ApplicationVO> appList = null;
				if(domainId.equalsIgnoreCase("null") || StringUtil.isBlank(domainId) || domainId == "" ){
					appList = ah.getAppList();
				} else {
					appList = ah.getListByDomainId(domainId);
				}
				for(ApplicationVO applicationVO : appList) {
					if (!applicationVO.isActivated()) {
						continue;
					}
					params.setParameter("application", applicationVO.getId());
					params.setParameter("t_status", RoleVO.STATUS_VALID);
					Collection<RoleVO> datas = this.process.doSimpleQuery(this.getParams());
					html.append("<font style='font-weight:bold;'>" + applicationVO.getName() + "</font>");
					for (Iterator<RoleVO> iter = datas.iterator(); iter.hasNext();) {
						RoleVO tempRole = (RoleVO) iter.next();
						html.append("<div id='" + tempRole.getId() + "' class='list_div' title='" + tempRole.getName()
								+ "' onclick='getUserListByRole(jQuery(this))'>");
						html.append("<img id='img_" + tempRole.getId() + "' class='selectImg_right' src='"
								+ Environment.getInstance().getContextPath() + "/resource/images/right_2.gif'/>");
						html.append(tempRole.getName());
						html.append("</div>");
					}
		
				}
			}else{
				ApplicationProcess applicationProcess = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
				ApplicationVO applicationVO = (ApplicationVO)applicationProcess.doView(applicationId);
				params.setParameter("application", applicationId);
				params.setParameter("t_status", RoleVO.STATUS_VALID);
				Collection<RoleVO> datas = this.process.doSimpleQuery(this.getParams());
				html.append("<font style='font-weight:bold;'>" + applicationVO.getName() + "</font>");
				for (Iterator<RoleVO> iter = datas.iterator(); iter.hasNext();) {
					RoleVO tempRole = (RoleVO) iter.next();
					html.append("<div id='" + tempRole.getId() + "' class='list_div' title='" + tempRole.getName()
							+ "' onclick='getUserListByRole(jQuery(this))'>");
					html.append("<img id='img_" + tempRole.getId() + "' class='selectImg_right' src='"
							+ Environment.getInstance().getContextPath() + "/resource/images/right_2.gif'/>");
					html.append(tempRole.getName());
					html.append("</div>");
				}
			}
			
			html.append("</div>");

			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public Map<String, String> get_departmentList(String application) throws Exception {
		DepartmentProcess dp = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		Collection<DepartmentVO> dc = dp.doSimpleQuery(null, application);

		Map<String, String> dm = ((DepartmentProcess) dp).deepSearchDepartmentTree(dc, null, null, 0);
		return dm;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
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

		if (node instanceof RoleVO) {
			RoleVO role = (RoleVO) node;
			id = "R" + role.getId();
			name = role.getName();
			icon = ctxPath + "/resource/images/dtree/group.gif";
		}

		if (checkedList == null) {
			checkedList = "";
		}

		JSONbuffer.append("{");
		JSONbuffer.append("text:'" + name + "',");
		JSONbuffer.append("id:'" + id + "',");
		JSONbuffer.append("icon:'" + icon + "',");
		JSONbuffer.append("checked:" + (checkedList.indexOf(id) != -1));
		JSONbuffer.append("},");
		return JSONbuffer.toString();
	}

	public void getPageDiv(StringBuffer html, int currentPage, int pageCount, String url) {
		html.append("<div style='padding:5px;border-bottom:1px solid gray;'>");
		if (currentPage > 1) {
			html.append("<a style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav(\"" + url + "&_currpage=1\")'>{*[FirstPage]*}</a>&nbsp;");
			html.append("<a style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav(\"" + url + "&_currpage=" + (currentPage - 1)
					+ "\")'>{*[PrevPage]*}</a>&nbsp;");
		}

		if (currentPage < pageCount) {
			html.append("<a style='cursor: pointer;color:#316AC5;' onclick='javascript:doLeftPageNav(\"" + url + "&_currpage=" + (currentPage + 1)
					+ "\")'>{*[NextPage]*}</a>&nbsp;");
			html.append("<a style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav(\"" + url + "&_currpage=" + pageCount
					+ "\")'>{*[EndPage]*}</a>&nbsp;");
		}

		html.append("{*[InPage]*}").append(currentPage).append("{*[Page]*}/{*[Total]*}").append(pageCount).append(
				"{*[Pages]*}&nbsp;");
		html.append("</div>");
	}
	
	/**
	 * 角色列表
	 */
	@SuppressWarnings("unchecked")
	public String doList(){
		ParamsTable params = this.getParams();
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
		params.removeParameter("tempRoles");
		params.setParameter("_orderby", "name");
		getContext().getParameters().put("tempRoles", tempRoles);
		return super.doList();
	}
	
	public void doRolesListByApplicationJSON(){
		JSONObject result = new JSONObject();
			try {
				ParamsTable params = this.getParams();
				String applicationId = params.getParameterAsString("application"); 
				
				ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
				RoleProcess roleProcess = (RoleProcess) ProcessFactory.createProcess((RoleProcess.class));
				
				ApplicationVO appVO = (ApplicationVO) applicationProcess.doView(applicationId);
				Collection<RoleVO> rolesByApplication = roleProcess.getRolesByApplication(appVO.getId());
				
				Iterator<RoleVO> iterator = rolesByApplication.iterator();
				JSONObject Json = new JSONObject();
				JSONArray roleList = new JSONArray();
				JSONObject roleJson ;
				while(iterator.hasNext()){
					RoleVO roleVO = iterator.next();
					roleJson = new JSONObject();
					roleJson.put("roleId", roleVO.getId());
					roleJson.put("roleName", roleVO.getName());
					roleList.add(roleJson);
				}
				Json.put("role", roleList);
				result.put("state", 1);
				result.put("message", "");
				result.put("data", Json);
				
			}catch (Exception e) {
				result.put("state", 0);
				result.put("message", e.getLocalizedMessage());
				result.put("data", null);
				e.printStackTrace();
			}
			
			try {
				HttpServletResponse response = ServletActionContext.getResponse();
				
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(result.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	public String getTempRoles() {
		return tempRoles;
	}

	public void setTempRoles(String tempRoles) {
		this.tempRoles = tempRoles;
	}
	
	

	
}
