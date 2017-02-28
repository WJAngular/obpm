package cn.myapps.core.user.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.tools.ant.types.selectors.DepthSelector;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.contacts.ejb.Department;
import cn.myapps.contacts.ejb.Node;
import cn.myapps.contacts.ejb.User;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import flex.messaging.util.URLDecoder;

public class UserSelectAction extends ActionSupport {
	
	private static final long serialVersionUID = -1021941166716325951L;
	
	protected static final String ACTION_RESULT_KEY = "status";
	protected static final int ACTION_RESULT_VALUE_SUCCESS = 1;
	protected static final int ACTION_RESULT_VALUE_FAULT = 0;
	
	protected static final String ACTION_MESSAGE_KEY = "message";
	protected static final String ACTION_DATA_KEY = "data";
	
	
	protected ParamsTable params;
	
	//将会被Struts2序列化为JSON字符串的对象,用来作为控制层返回结果的容器
	protected Map<String, Object> dataMap = new HashMap<String, Object>();
	
	public ParamsTable getParams() {
		if (params == null) {
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());

			params.setSessionid(ServletActionContext.getRequest().getSession().getId());

			if (params.getParameter("_pagelines") == null){
				params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
			}
		}

		return params;
	}
	
	public WebUser getUser(){
		Map<String,Object> session = ActionContext.getContext().getSession();

		WebUser user = null;

		if (session == null || session.get(Web.SESSION_ATTRIBUTE_FRONT_USER) == null){
			if(isFromWeixin()){
				return getUserFromCookie();
			}else
				return null;
		}
		else{
			user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_FRONT_USER);
		}

		return user;
	}
	
	/**
     * 是否来自微信
     * @param request
     * @return
     */
    private boolean isFromWeixin() {
    	HttpServletRequest request = ServletActionContext.getRequest();
    	String userAgent = request.getHeader("user-agent");
    	if(userAgent.contains("MicroMessenger")) return true;
    	return false;
    }
    
    private WebUser getUserFromCookie(){
    	HttpServletRequest request = ServletActionContext.getRequest();
    	Cookie[] cookies = request.getCookies();
		
		if(cookies == null) return null;
 		
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if("WEB_USER_COOKIE_KEY".equals(cookie.getName())){
				String value = Security.decryptPassword(cookie.getValue());
				if(value !=null){
					JSONObject jo = JSONObject.fromObject(value);
					String loginno = jo.getString("loginNo");
					if(!StringUtil.isBlank(loginno)){
						UserProcess process;
						try {
							process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
							UserVO user = (UserVO)process.login(loginno);
							if(user !=null){
								WebUser webUser = new WebUser(user);
								request.getSession().setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);
								return webUser;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
				
			}
		}
    	
    	return null;
    }
	
	 /**
     * Struts2序列化指定属性时，必须有该属性的getter方法
     * @return
     */
    public Map<String, Object> getDataMap() {
        return dataMap;
    }
	/**
     * 添加Action处理结果
     * @param isSuccess
     * 		是否成功处理
     * @param message
     * 		返回消息
     * @param data
     * 		返回数据
     */
    public void addActionResult(boolean isSuccess,String message,Object data){
    	dataMap.put(ACTION_RESULT_KEY, isSuccess?ACTION_RESULT_VALUE_SUCCESS : ACTION_RESULT_VALUE_FAULT );
    	dataMap.put(ACTION_MESSAGE_KEY, message);
    	if(data != null){
    		dataMap.put(ACTION_DATA_KEY, data);
    	}
    }
	


	public String doListByDept() {

		try {
			ParamsTable params = getParams();
			String domain = params.getParameterAsString("domain");
			String departid = params.getParameterAsString("departid");

			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			this.getParams().setParameter("_pagelines", String.valueOf(pagelines));
			
			UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);

			DataPackage<UserVO> datas = process.queryByDeptForUserDialog(params, getUser(), ServletActionContext.getRequest(), null);
			
			Collection<WarpUser> users = new ArrayList<UserSelectAction.WarpUser>();
			for (Iterator<UserVO> iterator = datas.datas.iterator(); iterator.hasNext();) {
				UserVO u = iterator.next();
				WarpUser user = new WarpUser(u);
				users.add(user);
			}
			DataPackage<WarpUser> dp = new DataPackage<UserSelectAction.WarpUser>();
			dp.setDatas(users);
			dp.setLinesPerPage(datas.getLinesPerPage());
			dp.setPageNo(datas.getPageNo());
			dp.setRowCount(datas.getRowCount());

			addActionResult(true, "", dp);
			
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if (!(e instanceof OBPMValidateException)) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String doOnLineUserList() {
		try {
			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			this.getParams().setParameter("_pagelines", String.valueOf(pagelines));
			String domain = getParams().getParameterAsString("domain");

			DataPackage<WebUser> datas = OnlineUsers.doQueryByDomain(this.getParams(), domain);
			
			Collection<WarpUser> users = new ArrayList<UserSelectAction.WarpUser>();
			for (Iterator<WebUser> iterator = datas.datas.iterator(); iterator.hasNext();) {
				WebUser u = iterator.next();
				WarpUser user = new WarpUser(u);
				users.add(user);
			}
			DataPackage<WarpUser> dp = new DataPackage<UserSelectAction.WarpUser>();
			dp.setDatas(users);
			dp.setLinesPerPage(datas.getLinesPerPage());
			dp.setPageNo(datas.getPageNo());
			dp.setRowCount(datas.getRowCount());

			addActionResult(true, "", dp);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String doSearchUsers() {
		try {
			ParamsTable params = getParams();
			String sm_name = params.getParameterAsString("sm_name");
			sm_name = URLDecoder.decode(sm_name);
			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			this.getParams().setParameter("_pagelines", String.valueOf(pagelines));
			
			UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);

			DataPackage<UserVO> datas = ((UserProcess)process).queryForUserDialog(params, getUser(), ServletActionContext.getRequest(), null);
			
			Collection<WarpUser> users = new ArrayList<UserSelectAction.WarpUser>();
			for (Iterator<UserVO> iterator = datas.datas.iterator(); iterator.hasNext();) {
				UserVO u = iterator.next();
				WarpUser user = new WarpUser(u);
				users.add(user);
			}
			DataPackage<WarpUser> dp = new DataPackage<UserSelectAction.WarpUser>();
			dp.setDatas(users);
			dp.setLinesPerPage(datas.getLinesPerPage());
			dp.setPageNo(datas.getPageNo());
			dp.setRowCount(datas.getRowCount());

			addActionResult(true, "", dp);
			
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if (!(e instanceof OBPMValidateException)) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String getFavoriteContacts(){
		try {
			List<WarpUser> list = new ArrayList<WarpUser>();
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			DepartmentProcess deptProcess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			WebUser user = getUser();
			UserVO vo = (UserVO) userProcess.doView(user.getId());
			
			if(!StringUtil.isBlank(vo.getFavoriteContacts())){
				String[] ids = vo.getFavoriteContacts().split(";");
				StringBuilder cdt = new StringBuilder();
				for (int i = 0; i < ids.length; i++) {
					cdt.append("'").append(ids[i]).append("'").append(",");
				}
				cdt.setLength(cdt.length()-1);
				List<UserVO> users = (List<UserVO>)userProcess.doQueryByHQL("from "+UserVO.class.getName()+" where domainid='"+user.getDomainid() +"' and status=1 and dimission=1 and id in("+cdt.toString()+")");
				for(UserVO userVO : users){
					WarpUser u = new WarpUser(userVO);
					DepartmentVO dept = (DepartmentVO)deptProcess.doView(u.getDeptId());
					if(dept!=null) u.setDeptname(dept.getName());
					list.add(u);	
				}
			}
			addActionResult(true, "", list);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String getAllUser(){
		List<WarpUser> users = new ArrayList<WarpUser>();
		WebUser webUser = getUser();
		try {
			ParamsTable params = getParams();
			String sm_name = params.getParameterAsString("sm_name");
			String wherePart = StringUtil.isBlank(sm_name)? "": " and name like '%"+sm_name+"%'";
			DepartmentProcess deptProcess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			UserProcess userPorcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			List<UserVO> user = (List<UserVO>)userPorcess.doQueryByHQL("from "+UserVO.class.getName()+" where domainid='"+webUser.getDomainid() +"' and status=1 and dimission=1"+wherePart+" order by orderByNo");
			for(UserVO userVO : user){
				WarpUser u = new WarpUser(userVO);
				DepartmentVO dept = (DepartmentVO)deptProcess.doView(u.getDeptId());
				if(dept!=null) u.setDeptname(dept.getName());
				users.add(u);
			}
			addActionResult(true, "", users);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getUsersTree(){
		List<Node> avatars = new ArrayList<Node>();
		WebUser webUser = getUser();
		try {
			ParamsTable params = getParams();
			String sm_name = params.getParameterAsString("sm_name");
			String wherePart = StringUtil.isBlank(sm_name)? "": " and name like '%"+sm_name+"%'";
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			UserProcess userPorcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			Map<String,List<Node>> deptMap = new HashMap<String, List<Node>>();//key 部门id value 子部门集合
			List<DepartmentVO> departments = (List<DepartmentVO>) process.doQueryByHQL("from "+DepartmentVO.class.getName()+" vo where vo.domain.id='"+webUser.getDomainid()+"'  order by vo.level desc,vo.orderByNo", 1, Integer.MAX_VALUE);
			List<UserVO> users = (List<UserVO>)userPorcess.doQueryByHQL("from "+UserVO.class.getName()+" where domainid='"+webUser.getDomainid() +"' and status=1 and dimission=1"+wherePart+" order by orderByNo");
			
			for (Iterator<DepartmentVO> iterator = departments.iterator(); iterator.hasNext();) {
				DepartmentVO departmentVO = iterator.next();
				Department d = new Department();
				d.setId(departmentVO.getId());
				d.setName(departmentVO.getName());
				DepartmentVO s = departmentVO.getSuperior();
				
				for(UserVO userVO : users){
					if(userVO.getDefaultDepartment().equals(departmentVO.getId())){
						User u = new User();
						u.setId(userVO.getId());
						u.setName(userVO.getName());
						u.setLoginNo(userVO.getLoginno());
						u.setMobile(userVO.getTelephone());
						u.setEmail(userVO.getEmail());
						u.setDeptId(userVO.getDefaultDepartment());
						u.setDomainId(userVO.getDomainid());
						if(!StringUtil.isBlank(userVO.getAvatar())){
							JSONObject json = JSONObject.fromObject(userVO.getAvatar());
							u.setAvatar(json.getString("url"));
						}else{
							u.setAvatar("");
						}
						
						u.setDept(d.getName());
						d.addChildren(u);
						
						//users.remove(userVO);
					}
				}
				
				if(deptMap.get(d.getId()) !=null){
					d.addChildrens(deptMap.get(d.getId()));
				}
				
				if(s !=null){
					List<Node> list = deptMap.get(s.getId());
					if(list == null) {
						list = new ArrayList<Node>();
						deptMap.put(s.getId(), list);
					}
					list.add(d);
				}else{
					avatars.add(d);
				}
				
			}
			addActionResult(true, "", avatars);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		
		
		return SUCCESS;
		
	}
	
	public String getDepartmentsByParentId(){
		try {
			Collection<Map<String, String>> list = new ArrayList<Map<String, String>>();
			ParamsTable params = getParams();
			WebUser user = getUser();
			String parentId = params.getParameterAsString("parentId");
			String application = params.getParameterAsString("applicationId");
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			Collection<DepartmentVO> depts = new ArrayList<DepartmentVO>();
			if(StringUtil.isBlank(parentId)){
				depts.addAll(process.getDepartmentByLevel(0, application, user.getDomainid()));
			}else{
				depts.addAll(process.getDatasByParent(parentId));
			}
			for (Iterator<DepartmentVO> iterator = depts.iterator(); iterator.hasNext();) {
				DepartmentVO dept = iterator.next();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", dept.getId());
				map.put("name", dept.getName());
				map.put("parentId",parentId);
				
				list.add(map);
			}
			
			addActionResult(true, "", list);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public class WarpUser{
		private String id;
		
		private String name;
		
		private String loginNo;
		
		private String email;
		
		private String mobile;
		
		private String avatar;
		
		private String deptId;
		
		private String deptname;
		
		private String domainId;
		
		public WarpUser(BaseUser user){
			this.id = user.getId();
			this.name = user.getName();
			this.email = user.getEmail();
			this.mobile = user.getTelephone();
			if(!StringUtil.isBlank(user.getAvatar())){
				this.avatar = JSONObject.fromObject(user.getAvatar()).getString("url");
			}
			this.loginNo = user.getLoginno();
			this.deptId = user.getDefaultDepartment();
			this.domainId = user.getDomainid();
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLoginNo() {
			return loginNo;
		}

		public void setLoginNo(String loginNo) {
			this.loginNo = loginNo;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getDeptId() {
			return deptId;
		}

		public void setDeptId(String deptId) {
			this.deptId = deptId;
		}

		public String getDomainId() {
			return domainId;
		}

		public void setDomainId(String domainId) {
			this.domainId = domainId;
		}

		public String getDeptname() {
			return deptname;
		}

		public void setDeptname(String deptname) {
			this.deptname = deptname;
		}
		
		
	}

}
