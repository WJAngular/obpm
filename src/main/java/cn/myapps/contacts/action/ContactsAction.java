package cn.myapps.contacts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.contacts.ejb.Application;
import cn.myapps.contacts.ejb.Department;
import cn.myapps.contacts.ejb.Node;
import cn.myapps.contacts.ejb.Role;
import cn.myapps.contacts.ejb.User;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.ProcessFactory;

public class ContactsAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3906611084717370757L;

	public ContactsAction() {
		super();
	}
	
	public String getAllUser(){
		List<Node> users = new ArrayList<Node>();
		WebUser webUser = getUser();
		try {
			UserProcess userPorcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			List<UserVO> user = (List<UserVO>)userPorcess.doQueryByHQL("from "+UserVO.class.getName()+" where domainid='"+webUser.getDomainid() +"' and status=1 and dimission=1 and userInfopublic = 1 and permission_type = 'public' order by NAME_LETTER,orderByNo");
			DepartmentProcess deptProcess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			for(UserVO userVO : user){
					User u = new User();
					u.setId(userVO.getId());
					u.setName(userVO.getName());
					u.setDept(userVO.getDefaultDepartment());
					DepartmentVO dept = (DepartmentVO) deptProcess.doView(userVO.getDefaultDepartment());
					if(dept!=null){	
						u.setDept(dept.getName());
					}else{
						u.setDept("");
					}
					if(userVO.isTelephonePublic()){
						u.setMobile(userVO.getTelephone());
					}
					if(userVO.isTelephonePublic2()){
						u.setMobile2(userVO.getTelephone2());
					}
					if(userVO.isEmailPublic()){
						u.setEmail(userVO.getEmail());
					}
					if(!StringUtil.isBlank(userVO.getAvatar())){
						JSONObject json = JSONObject.fromObject(userVO.getAvatar());
						u.setAvatar(json.getString("url"));
					}else{
						u.setAvatar("");
					}
					/**
					for (Iterator<DepartmentVO> iterator = process.queryByUser(userVO.getId()).iterator(); iterator.hasNext();){
						DepartmentVO departmentVO = iterator.next();
						u.setDept(departmentVO.getName());
					}
					**/
					/**
					DepartmentVO departmentVO = (DepartmentVO) process.doView(userVO.getDefaultDepartment());
					if(departmentVO !=null){
						u.setDept(departmentVO.getName());
					}
					**/
					users.add(u);
			}
			
			addActionResult(true, "", users);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getContacts(){
		List<Node> avatars = new ArrayList<Node>();
		WebUser webUser = getUser();
		try {
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			UserProcess userPorcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			Map<String,List<Node>> deptMap = new HashMap<String, List<Node>>();//key 部门id value 子部门集合
			List<DepartmentVO> departments = (List<DepartmentVO>) process.doQueryByHQL("from "+DepartmentVO.class.getName()+" vo where vo.domain.id='"+webUser.getDomainid()+"' order by vo.level desc,vo.orderByNo", 1, Integer.MAX_VALUE);
			List<UserVO> users = (List<UserVO>)userPorcess.doQueryByHQL("from "+UserVO.class.getName()+" where domainid='"+webUser.getDomainid() +"' and status=1 and dimission=1 and userInfopublic = 1 and permission_type = 'public' order by NAME_LETTER,orderByNo");
			
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
						if(userVO.isTelephonePublic()){
							u.setMobile(userVO.getTelephone());
						}
						if(userVO.isTelephonePublic2()){
							u.setMobile2(userVO.getTelephone2());
						}
						if(userVO.isEmailPublic()){
							u.setEmail(userVO.getEmail());
						}
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
	
	
	public String getApplicationAndRoleContactsTree(){
		List<Node> avatars = new ArrayList<Node>();
		WebUser webUser = getUser();
		ParamsTable params = getParams();
		try {
			String applictaionId = params.getParameterAsString("applictaionId");
			String roleId = params.getParameterAsString("roleId");
			
			if(StringUtil.isBlank(roleId) && StringUtil.isBlank(applictaionId)){  //初始化界面获取软件
				ApplicationProcess process = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
				Collection<ApplicationVO> apps = process.queryByDomain(webUser.getDomainid());
				
				Application app;
				for(ApplicationVO appVO : apps){
					if(appVO.isActivated()){
						app = new Application();
						app.setId(appVO.getId());
						app.setName(appVO.getName());
						avatars.add(app);
					}
				}
			}else if (!StringUtil.isBlank(applictaionId) && StringUtil.isBlank(roleId)){ //根据软件获取角色
				RoleProcess roleProcess = (RoleProcess)ProcessFactory.createProcess(RoleProcess.class);
				Collection<RoleVO> roles = roleProcess.getRolesByApplication(applictaionId);
				
				Role role ;
				for(RoleVO roleVO : roles){
					if(roleVO.getStatus() ==  RoleVO.STATUS_VALID){
						role = new Role();
						role.setId(roleVO.getId());
						role.setName(roleVO.getName());
						avatars.add(role);
					}
				}
			}else if(!StringUtil.isBlank(roleId)){  //获取角色下的成员
				UserProcess userProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
                Collection<UserVO> users = userProcess.queryByRole(roleId);
                DepartmentProcess deptProcess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			
				for(UserVO userVO : users){
					if(userVO.getDimission() == 0) 
						continue;
					User u = new User();
					u.setId(userVO.getId());
					u.setName(userVO.getName());
					DepartmentVO dept = (DepartmentVO) deptProcess.doView(userVO.getDefaultDepartment());
					if(dept!=null){
						u.setDept(dept.getName());
					}else{
						u.setDept("");
					}
					if(userVO.isTelephonePublic()){
						u.setMobile(userVO.getTelephone());
					}
					if(userVO.isTelephonePublic2()){
						u.setMobile2(userVO.getTelephone2());
					}
					if(userVO.isEmailPublic()){
						u.setEmail(userVO.getEmail());
					}
					if(!StringUtil.isBlank(userVO.getAvatar())){
						JSONObject json = JSONObject.fromObject(userVO.getAvatar());
						u.setAvatar(json.getString("url"));
					}else{
						u.setAvatar("");
					}
					
					avatars.add(u);
			  }
			}
			addActionResult(true, "", avatars);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		
		return SUCCESS;
		
	}
	
	public String getContactsBySearch(){
		List<Node> users = new ArrayList<Node>();
		WebUser webUser = getUser();
		ParamsTable params = getParams();
		
		String keyWord = params.getParameterAsString("keyWord");
			
			try {
				UserProcess userProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
                DepartmentProcess deptProcess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);

				List<UserVO> user = (List<UserVO>)userProcess.doQueryByHQL("from "+UserVO.class.getName()+" where domainid='"+webUser.getDomainid() +"' and status=1 and dimission=1 and userInfopublic = 1 AND (name_letter like '%" + keyWord + "%' OR name like '%" + keyWord + "%' OR loginno like '%" + keyWord + "%' OR telephone like '%" + keyWord + "%')  order by NAME_LETTER,orderByNo");
				
				for(UserVO userVO : user){
					User u = new User();
					u.setId(userVO.getId());
					u.setName(userVO.getName());
					DepartmentVO dept = (DepartmentVO) deptProcess.doView(userVO.getDefaultDepartment());
					if(dept!=null){
						u.setDept(dept.getName());
					}else{
						u.setDept("");
					}
					if(userVO.isTelephonePublic()){
						u.setMobile(userVO.getTelephone());
					}
					if(userVO.isTelephonePublic2()){
						u.setMobile2(userVO.getTelephone2());
					}
					if(userVO.isEmailPublic()){
						u.setEmail(userVO.getEmail());
					}
					if(!StringUtil.isBlank(userVO.getAvatar())){
						JSONObject json = JSONObject.fromObject(userVO.getAvatar());
						u.setAvatar(json.getString("url"));
					}else{
						u.setAvatar("");
					}
					users.add(u);
			}
			addActionResult(true, "", users);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String getContactsTree(){
		List<Node> avatars = new ArrayList<Node>();
		WebUser webUser = getUser();
		ParamsTable params = getParams();
		try {
			String parentId = params.getParameterAsString("parentId");
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			UserProcess userProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			
			if(StringUtil.isBlank(parentId)){
				DepartmentVO departmentVO = process.getRootDepartmentByApplication("", webUser.getDomainid());
				Department d = new Department();
				d.setId(departmentVO.getId());
				d.setName(departmentVO.getName());
				avatars.add(d);
			}else{
				DepartmentVO parent = (DepartmentVO) process.doView(parentId);
				Collection<DepartmentVO> departments = (Collection<DepartmentVO>) process.getDatasByParent(parentId);
				//Collection<UserVO> users =parent.getUsers();
				Collection<UserVO> users = userProcess.queryByDepartment(parentId,true);
				
				for(UserVO userVO : users){
					if(userVO.getDimission() == 0 || userVO.getPermissionType().equals(UserVO.PERMISSION_TYPE_PRIVATE)) 
						continue;
					User u = new User();
					u.setId(userVO.getId());
					u.setName(userVO.getName());
					if(userVO.isTelephonePublic()){
						u.setMobile(userVO.getTelephone());
					}
					if(userVO.isTelephonePublic2()){
						u.setMobile2(userVO.getTelephone2());
					}
					if(userVO.isEmailPublic()){
						u.setEmail(userVO.getEmail());
					}
					if(!StringUtil.isBlank(userVO.getAvatar())){
						JSONObject json = JSONObject.fromObject(userVO.getAvatar());
						u.setAvatar(json.getString("url"));
					}else{
						u.setAvatar("");
					}
					
					u.setDept(parent.getName());
					
					avatars.add(u);
				}
				
				for (Iterator<DepartmentVO> iterator = departments.iterator(); iterator.hasNext();) {
					DepartmentVO departmentVO = iterator.next();
					Department d = new Department();
					d.setId(departmentVO.getId());
					d.setName(departmentVO.getName());
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
	
	public String addFavoriteContact(){
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			WebUser user = getUser();
			ParamsTable params = getParams();
			String userId = params.getParameterAsString("userId");
			UserVO userVO = (UserVO) userProcess.doView(user.getId());
			if(StringUtil.isBlank(userVO.getFavoriteContacts())){
				userVO.setFavoriteContacts(userId);
			}else{
				userVO.setFavoriteContacts(userVO.getFavoriteContacts()+";"+userId);
			}
			userProcess.doUpdate(userVO);
			
			addActionResult(true, "添加成功", null);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String removeFavoriteContact(){
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			WebUser user = getUser();
			ParamsTable params = getParams();
			String userId = params.getParameterAsString("userId");
			UserVO userVO = (UserVO) userProcess.doView(user.getId());
			if(userVO.getFavoriteContacts().indexOf(userId)==0){
				userVO.setFavoriteContacts(userVO.getFavoriteContacts().replace(userId, ""));
			}else{
				userVO.setFavoriteContacts(userVO.getFavoriteContacts().replace(";"+userId, ""));
			}
			
			userProcess.doUpdate(userVO);
			addActionResult(true, "移除成功", null);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String getFavoriteContacts(){
		try {
			List<User> list = new ArrayList<User>();
			DepartmentProcess deptProcess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			WebUser user = getUser();
			UserVO vo = (UserVO) userProcess.doView(user.getId());
			
			if(!StringUtil.isBlank(vo.getFavoriteContacts())){
				String[] ids = vo.getFavoriteContacts().split(";");
				StringBuilder cdt = new StringBuilder();
				for (int i = 0; i < ids.length; i++) {
					cdt.append("'").append(ids[i]).append("'").append(",");
				}
				cdt.setLength(cdt.length()-1);
				List<UserVO> users = (List<UserVO>)userProcess.doQueryByHQL("from "+UserVO.class.getName()+" where domainid='"+user.getDomainid() +"' and status=1 and dimission=1 and userInfopublic = 1 and permission_type = 'public' and id in("+cdt.toString()+") order by NAME_LETTER,ORDERBYNO");
				for(UserVO userVO : users){
					User u = new User();
					u.setId(userVO.getId());
					u.setName(userVO.getName());
					if(userVO.isTelephonePublic()){
						u.setMobile(userVO.getTelephone());
					}
					if(userVO.isTelephonePublic2()){
						u.setMobile2(userVO.getTelephone2());
					}
					if(userVO.isEmailPublic()){
						u.setEmail(userVO.getEmail());
					}
					DepartmentVO dept = (DepartmentVO) deptProcess.doView(userVO.getDefaultDepartment());
					if(dept!=null){
						u.setDept(dept.getName());
					}else{
						u.setDept("");
					}
					if(!StringUtil.isBlank(userVO.getAvatar())){
						JSONObject json = JSONObject.fromObject(userVO.getAvatar());
						u.setAvatar(json.getString("url"));
					}else{
						u.setAvatar("");
					}
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
	
	public String isFavoriteContact(){
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			WebUser user = getUser();
			ParamsTable params = getParams();
			String userId = params.getParameterAsString("userId");
			UserVO userVO = (UserVO) userProcess.doView(user.getId());
			if(userVO.getFavoriteContacts() !=null && userVO.getFavoriteContacts().indexOf(userId)>=0){
				addActionResult(true, "", true);
			}else{
				addActionResult(true, "", false);
			}
			
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 获取用户头像
	 * @return
	 */
	public String getAvatar(){
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			ParamsTable params = getParams();
			String userId = params.getParameterAsString("id");
			UserVO userVO = (UserVO) userProcess.doView(userId);
			if(userVO != null){
				String avatar = "";
				if(!StringUtil.isBlank(userVO.getAvatar())){
					JSONObject json = JSONObject.fromObject(userVO.getAvatar());
					avatar = json.getString("url");
				}
				addActionResult(true, "", avatar);
			}else{
				addActionResult(false, "找不到指定用户", null);
			}
			
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 获取角色下或部门下人数
	 * @return
	 */
	  public String getRoleOrDeptUserCounts(){
			try {
				ParamsTable params = getParams();
				Integer type = params.getParameterAsInteger("type");        // 数据类型
				String id = params.getParameterAsString("id");              // 查询Id
				UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				Long userCounts  = null ;
				userCounts  = userProcess.getCountsByDepartment(id);
				
				if(type == Node.TYPE_DEPT){ // 查询部门下的人数
					userCounts  = userProcess.getCountsByDepartment(id);
				}else if(type == Node.TYPE_ROLE){ // 查询角色下的人数
					userCounts = userProcess.getCountsByRole(id);
				}
				addActionResult(true, "", userCounts);
			} catch (Exception e) {
				addActionResult(false, e.getMessage(), null);
				e.printStackTrace();
			}
			return SUCCESS;
		}

	  
	  

}
