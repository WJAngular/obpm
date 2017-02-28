package cn.myapps.core.workflow.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.action.FormHelper;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.Type;
import cn.myapps.core.workflow.utility.NameList;
import cn.myapps.core.workflow.utility.NameNode;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.web.DWRHtmlUtils;

public class StateMachineUtil {

	public Collection<Node> getFirstNodeList(Document doc) throws Exception {
		return StateMachine.getFirstNodeList(doc);
	}
	
	public Collection<Node> getFirstNodeList(String docid, String flowid) throws Exception {
		return StateMachine.getFirstNodeList(docid, flowid);
	}

	public String getFirstNodeListByFlowid(Document doc, String divid) throws Exception {
		Collection<Node> cols = null;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		cols = StateMachine.getFirstNodeList(doc);
		if (cols != null) {
			for (Iterator<Node> iter = cols.iterator(); iter.hasNext();) {
				ManualNode startNode = (ManualNode) iter.next();
				map.put(startNode.id, startNode.name);
			}
		}
		String[] str = new String[10];
		return DWRHtmlUtils.createHtmlStr(map, divid, str);
	}

	public String getFirstNodeListByDocidAndFlowid(String docid, String flowid, String divid, HttpServletRequest request) throws Exception {
		
		WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		Document doc = (Document) webUser.getFromTmpspace(docid);
		if(doc != null){
			IRunner runner = JavaScriptFactory.getInstance(request.getSession().getId(), doc
					.getApplicationid());
			runner.initBSFManager(doc, new ParamsTable(), webUser,
					new ArrayList<ValidateMessage>());
		}
		
		Collection<Node> cols = null;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		BillDefiProcess process = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		BillDefiVO flowVO = (BillDefiVO) process.doView(flowid);

		cols = StateMachine.getFirstNodeList(docid, flowVO);
		if (cols != null) {
			for (Iterator<Node> iter = cols.iterator(); iter.hasNext();) {
				ManualNode startNode = (ManualNode) iter.next();
				map.put(startNode.id, startNode.name);
			}
		}

		String[] str = new String[10];
		return DWRHtmlUtils.createHtmlStrWithRadio(map, divid, str);
	}

	/**
	 * 获取ID与负责人的映射
	 * 
	 * @SuppressWarnings JsonUtil.toCollectio不支持泛型
	 * @param node
	 * @param domainid
	 * @param applicationid
	 * @return ID与负责人的映射
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, BaseUser> getPrincipalMap(Document doc,ParamsTable params, Node nextnode, String domainid,
			String applicationid,BaseUser auditor) throws Exception {
		LinkedHashMap<String, BaseUser> userMap = new LinkedHashMap<String, BaseUser>();
		if (!(nextnode instanceof ManualNode)) {
			return userMap;
		}
		// 提交信息
		String submitTo = null;
		// json转化后的提交信息
		Collection<Object> submitInfo = null;
		// 标识用户是否指定审批人
		String isToPerson = "";
		// 某一节点下的用户指定的审批人列表
		Collection<Object> selectuserlist = null;
		String flowId ="";

		if (params != null) {
			submitTo = params.getParameterAsString("submitTo");
			if (submitTo != null && !("").equals(submitTo)) {
				submitInfo = JsonUtil.toCollection(submitTo);
				for (Iterator<Object> iterator = submitInfo.iterator(); iterator.hasNext();) {
					Map<String, String> tmpmap = (Map<String, String>) iterator.next();
					String nodeid = (String) tmpmap.get("nodeid");
					if (nodeid != "" && nodeid != null && nodeid.equals(nextnode.id)) {
						// 获取用户操作“是否指定审批人”true||false
						isToPerson = (String) tmpmap.get("isToPerson");
						// 获取用户指定的审批人列表
						selectuserlist = (Collection<Object>) JsonUtil.toCollection((String) tmpmap.get("userids"));
					} else
						continue;
				}
			}
		}

		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		RoleProcess roleProcess = (RoleProcess) ProcessFactory.createProcess(RoleProcess.class);
		DepartmentProcess deptProcess = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		// 用户指定审批人
		if (isToPerson.endsWith("true")) {
			for (Iterator<Object> iterator = selectuserlist.iterator(); iterator.hasNext();) {
				Object userObj = iterator.next();
				UserVO user = null;
				if (userObj instanceof String) {
					user = (UserVO) userProcess.doView((String) userObj);
				} else {
					user = (UserVO) userObj;
				}
				if (user != null) {
					userMap.put(user.getId(), user);
				}
			}
		} else {// 用户不指定审批人，默认提交给该节点下所有审批人
			ManualNode mNode = (ManualNode) nextnode;
			
			switch (mNode.actorEditMode) {
			case ManualNode.ACTOR_EDIT_MODE_CODE:
				if (StringUtil.isBlank(mNode.actorListScript)) {
					return userMap;
				}
				if(doc.getState() !=null){
					flowId = doc.getState().getFlowid();
				}else if(params !=null && !StringUtil.isBlank(params.getParameterAsString("_flowId"))){
					flowId = params.getParameterAsString("_flowId");
				}
				IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), applicationid);

				// 重新注册一些公共工具类 start
				if (params != null && auditor!=null && auditor instanceof WebUser) {
					
//					Document doc = (Document) request.getAttribute("content");
					runner.initBSFManager(doc, params, (WebUser)auditor, new ArrayList());
				}
				// ----------------- end

				StringBuffer label = new StringBuffer();
				label.append("ManualNode(").append(flowId).append(nextnode.id).append(").").append(StringUtil.dencodeHTML(nextnode.name)).append(
						".auditorListScript");
				Object obj = runner.run(label.toString(), StringUtil.dencodeHTML(mNode.actorListScript));

				// 兼容多种返回值，BaseUser,Collection<BaseUser>,BaseUser[],Collection<String>,NativeArray
				if (obj == null) {
					throw new Exception(label + " can not return a NULL! ");
				} else if (BaseUser.class.isAssignableFrom(obj.getClass())) {
					BaseUser user = (BaseUser) obj;
					userMap.put(user.getId(), user);
				} else if (obj instanceof UserVO) {
					UserVO user = (UserVO) obj;
					userMap.put(user.getId(), user);
				} else {
					Collection userList = new ArrayList(); // 用户列表
					if (obj instanceof Collection) {
						userList = (Collection) obj;
					} else if (obj instanceof UserVO[]) {
						userList = Arrays.asList((UserVO[]) obj);
					}
					for (Iterator iterator = userList.iterator(); iterator.hasNext();) {
						Object userObj = iterator.next();
						UserVO user = null;
						if (userObj instanceof String) {
							user = (UserVO) userProcess.doView((String) userObj);
						} else {
							user = (UserVO) userObj;
						}
						if (user != null) {
							userMap.put(user.getId(), user);
						}
					}
				}
				break;
			case ManualNode.ACTOR_EDIT_MODE_DESIGN:
				NameList nameList = NameList.parser(mNode.namelist);
				Collection<NameNode> nameNodeList = nameList.toNameNodeCollection();
				for (Iterator iter = nameNodeList.iterator(); iter.hasNext();) {
					NameNode nameNode = (NameNode) iter.next();
					String actorId = nameNode.getId();

					UserVO user = null;
					switch (nameNode.getType()) {
					case Type.TYPE_USER:
						user = (UserVO) userProcess.doView(actorId);
						userMap.put(user.getId(), user);
						break;
					case Type.TYPE_ROLE:
						RoleVO roleVO = (RoleVO) roleProcess.doView(actorId);
						if(roleVO != null && RoleVO.STATUS_VALID ==roleVO.getStatus()){
							Collection roleUsers = roleVO.getUsersByDomain(domainid);
							for (Iterator iterator = roleUsers.iterator(); iterator.hasNext();) {
								user = (UserVO) iterator.next();
								if(user.getDimission()==1){
									userMap.put(user.getId(), user);
								}
							}
						}
						break;
					case Type.TYPE_DEPARTMENT:
						DepartmentVO dept = (DepartmentVO) deptProcess.doView(actorId);
						if(dept != null){
							Collection deptUsers = dept.getUsers();
							for (Iterator iterator = deptUsers.iterator(); iterator.hasNext();) {
								user = (UserVO) iterator.next();
								userMap.put(user.getId(), user);
							}
						}
						break;
					default:
						break;
					}
				}
				break;
				//用户设计模式
			case ManualNode.ACTOR_EDIT_MODE_USER_DESIGN:
				NameList userList = NameList.parser(mNode.userList);
				Collection<NameNode> nameNodes = userList.toNameNodeCollection();
				for (Iterator<NameNode> iter = nameNodes.iterator(); iter.hasNext();) {
					NameNode nameNode = (NameNode) iter.next();
					String userId = nameNode.getId();

					UserVO user = null;
					switch (nameNode.getType()) {
					case Type.TYPE_USER:
						user = (UserVO) userProcess.doView(userId);
						userMap.put(user.getId(), user);
						break;
					case Type.TYPE_ROLE:
						RoleVO roleVO = (RoleVO) roleProcess.doView(userId);
						if(roleVO != null){
							Collection roleUsers = roleVO.getUsersByDomain(domainid);
							for (Iterator iterator = roleUsers.iterator(); iterator.hasNext();) {
								user = (UserVO) iterator.next();
								userMap.put(user.getId(), user);
							}
						}
						break;
					case Type.TYPE_DEPARTMENT:
						DepartmentVO dept = (DepartmentVO) deptProcess.doView(userId);
						if(dept != null){
							Collection deptUsers = dept.getUsers();
							for (Iterator iterator = deptUsers.iterator(); iterator.hasNext();) {
								user = (UserVO) iterator.next();
								userMap.put(user.getId(), user);
							}
						}
						break;
					default:
						break;
					}
				}
				break;
				//组织设计模式
			case ManualNode.ACTOR_EDIT_MODE_ORGANIZATION_DESIGN:
				if(ManualNode.ORG_AUDITOR.equals(mNode.orgField)){//流程提交者
					if(ManualNode.ORG_SCOPE_SUPERIOR.equals(mNode.orgScope)){
						loadSuperior(auditor, userMap);
					}else if(ManualNode.ORG_SCOPE_LOWER.equals(mNode.orgScope)){
						loadSubordinates(auditor, userMap);
					}else if(ManualNode.ORG_SCOPE_DEPT_DEFAULT.equals(mNode.orgScope)){
						loadUsersDefaultDeptUsers(auditor, userMap);
					}else if(ManualNode.ORG_SCOPE_DEPT_ALL_SUPERIOR.equals(mNode.orgScope)){
						loadAllSuperiorDeptUsers(auditor, userMap);
					}else if(ManualNode.ORG_SCOPE_DEPT_ALL_LOWER.equals(mNode.orgScope)){
						loadAllSubordinateDeptUsers(auditor, userMap);
					}else if(ManualNode.ORG_SCOPE_DEPT_LINE_SUPERIOR.equals(mNode.orgScope)){
						loadLineSuperiorDeptUsers(auditor, userMap);
					}else if(ManualNode.ORG_SCOPE_DEPT_LINE_LOWER.equals(mNode.orgScope)){
						loadLineSubordinateDeptUsers(auditor, userMap);
					}
					
					
				}else if(ManualNode.ORG_AUTHOR.equals(mNode.orgField)){//表单作者
					BaseUser author = doc.getAuthor();
					if(ManualNode.ORG_SCOPE_SUPERIOR.equals(mNode.orgScope)){
						loadSuperior(author, userMap);
					}else if(ManualNode.ORG_SCOPE_LOWER.equals(mNode.orgScope)){
						loadSubordinates(author, userMap);
					}else if(ManualNode.ORG_SCOPE_DEPT_DEFAULT.equals(mNode.orgScope)){
						loadUsersDefaultDeptUsers(author, userMap);
					}else if(ManualNode.ORG_SCOPE_DEPT_ALL_SUPERIOR.equals(mNode.orgScope)){
						loadAllSuperiorDeptUsers(author, userMap);
					}else if(ManualNode.ORG_SCOPE_DEPT_ALL_LOWER.equals(mNode.orgScope)){
						loadAllSubordinateDeptUsers(author, userMap);
					}else if(ManualNode.ORG_SCOPE_DEPT_LINE_SUPERIOR.equals(mNode.orgScope)){
						loadLineSuperiorDeptUsers(author, userMap);
					}else if(ManualNode.ORG_SCOPE_DEPT_LINE_LOWER.equals(mNode.orgScope)){
						loadLineSubordinateDeptUsers(author, userMap);
					}
					
				}else if(ManualNode.ORG_INITIATOR.equals(mNode.orgField)){//流程发起人
					
					if(doc.getState()!=null){
							String initiatorID = doc.getInitiator();
							UserVO initiator = (UserVO) userProcess.doView(initiatorID);
							if(initiator != null){
								if(ManualNode.ORG_SCOPE_SELF.equals(mNode.orgScope)){
									userMap.put(initiator.getId(), initiator);
								}else if(ManualNode.ORG_SCOPE_SUPERIOR.equals(mNode.orgScope)){
									loadSuperior(initiator, userMap);
								}else if(ManualNode.ORG_SCOPE_LOWER.equals(mNode.orgScope)){
									loadSubordinates(initiator, userMap);
								}else if(ManualNode.ORG_SCOPE_DEPT_DEFAULT.equals(mNode.orgScope)){
									loadUsersDefaultDeptUsers(initiator, userMap);
								}else if(ManualNode.ORG_SCOPE_DEPT_ALL_SUPERIOR.equals(mNode.orgScope)){
									loadAllSuperiorDeptUsers(initiator, userMap);
								}else if(ManualNode.ORG_SCOPE_DEPT_ALL_LOWER.equals(mNode.orgScope)){
									loadAllSubordinateDeptUsers(initiator, userMap);
								}else if(ManualNode.ORG_SCOPE_DEPT_LINE_SUPERIOR.equals(mNode.orgScope)){
									loadLineSuperiorDeptUsers(initiator, userMap);
								}else if(ManualNode.ORG_SCOPE_DEPT_LINE_LOWER.equals(mNode.orgScope)){
									loadLineSubordinateDeptUsers(initiator, userMap);
								}
							}
					}
					
				}else if(ManualNode.ORG_CURRUSER.equals(mNode.orgField)){//当前登录人
					if (params != null && params.getHttpRequest() != null) {
						HttpServletRequest request = params.getHttpRequest();
						WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
						userMap.put(webUser.getId(), webUser);
					}
					
				}
				//通过角色筛选条件过滤用户
				//mNode.orgRoleCondition = "(R11e4-c932-e010266a-b512-0b9a6eebd8cc|员工;)";
				if(!StringUtil.isBlank(mNode.orgRoleCondition)) {
					userMap = filterUserWithRoleCondition(userMap, mNode, domainid);
				}
				
				
				break;
			default:
				break;
			}
			
			if(!StringUtil.isBlank(mNode.roleCondition)){//根据筛选条件过滤审批人
				userMap = (LinkedHashMap<String, BaseUser>) filterUserWithCondition(userMap, mNode, doc,params);
			}
		}
		
		
		return userMap;

	}
	
	
	/**
	 * 根据角色筛选条件过滤用户
	 * @param userMap
	 * @param node
	 * @param domainid
	 * @return
	 * @throws Exception
	 */
	private static LinkedHashMap<String, BaseUser> filterUserWithRoleCondition(Map<String, BaseUser> userMap,ManualNode node,String domainid) throws Exception {
		LinkedHashMap<String, BaseUser> map = new LinkedHashMap<String, BaseUser>();
		if(StringUtil.isBlank(node.orgRoleCondition)) return null;
		
		RoleProcess roleProcess = (RoleProcess)ProcessFactory.createProcess(RoleProcess.class);
		
		//1.获取角色下的所有用户
		Collection<BaseUser> roleConditionUsers = new ArrayList<BaseUser>();
		
		NameList nameList = NameList.parser(node.orgRoleCondition);
		Collection<NameNode> nameNodeList = nameList.toNameNodeCollection();
		for (Iterator<NameNode> iter = nameNodeList.iterator(); iter.hasNext();) {
			NameNode nameNode = (NameNode) iter.next();
			RoleVO roleVO = (RoleVO) roleProcess.doView(nameNode.getId());
			if(roleVO != null && RoleVO.STATUS_VALID ==roleVO.getStatus()){
				roleConditionUsers.addAll(roleVO.getUsersByDomain(domainid));
			}
		}
		
		//2.角色下的所有用户与符合组织配置条件的用户列表合并，取交集。
		Collection<BaseUser> userList = userMap.values();
		userList.retainAll(roleConditionUsers);
		
		
		//3.取得的交集转换为map对象
		for (Iterator<BaseUser> iterator = userList.iterator(); iterator.hasNext();) {
			BaseUser u = iterator.next();
			map.put(u.getId(), u);
		}
		
		return map;
	}
	
	/**
	 * 根据过滤条件过滤审批人
	 * @param userMap
	 * @param node
	 * @param doc
	 * @param params
	 * @throws Exception
	 */
	private static Map<String, BaseUser> filterUserWithCondition(Map<String, BaseUser> userMap,ManualNode node,Document doc,ParamsTable params) throws Exception {
		BaseUser initiator = getInitiator(doc);
		Map<String, BaseUser> map = new LinkedHashMap<String, BaseUser>();
		if(initiator == null) return map;
		if(ManualNode.ROLE_CONDITION_INITIATOR_SUPERIOR.equals(node.roleCondition)){
			UserVO superior = initiator.getSuperior();
			if(superior != null && userMap.get(superior.getId()) != null){
				map.put(superior.getId(), superior);
			}
		}else if(ManualNode.ROLE_CONDITION_INITIATOR_DEP_SUPERIOR.equals(node.roleCondition)){
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			DepartmentVO dept = (DepartmentVO) process.doView(initiator.getDefaultDepartment());
			if(dept != null){
				Collection<DepartmentVO> deptList = process.getSuperiorDeptListExcludeCurrent(dept.getId());
				//Collection<DepartmentVO> deptList = process.doQueryAllSuperiorsByIndexCode(dept.getIndexCode());
				for(Iterator<DepartmentVO> it = deptList.iterator();it.hasNext();){
					DepartmentVO dep = it.next();
					Collection<UserVO> _userList = dep.getUsers();
					for(Iterator<UserVO> iter = _userList.iterator();iter.hasNext();){
						UserVO u = iter.next();
						if(u != null && userMap.get(u.getId()) != null){
							map.put(u.getId(), u);
						}
					}
				}
			}
		}else if(ManualNode.ROLE_CONDITION_CURRUSER_DEFAULT_DEPT.equals(node.roleCondition)){//审批人部门为提交人部门
			if (params != null && params.getHttpRequest() != null) {
				HttpServletRequest request = params.getHttpRequest();
				WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
				if(webUser !=null && !StringUtil.isBlank(webUser.getDefaultDepartment())){
					for (Iterator<Entry<String, BaseUser>> iterator = userMap.entrySet().iterator(); iterator.hasNext();) {
						Entry<String, BaseUser> entry = iterator.next();
						UserVO u = (UserVO)entry.getValue();
						for (Iterator<DepartmentVO> iter = u.getDepartments().iterator(); iter.hasNext();) {
							DepartmentVO dept = (DepartmentVO) iter.next();
							if (dept.getId() != null && dept.getId().equals(webUser.getDefaultDepartment())){
								map.put(u.getId(), u);
							}
						}
						
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 获取文档的流程发起人
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	private static UserVO getInitiator(Document doc) throws Exception{
		Collection<RelationHIS> colls = StateMachineHelper.getAllRelationHIS(doc.getId(), doc.getFlowid(),doc.getApplicationid());
		UserProcess userProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
		if(!colls.isEmpty()){
			RelationHIS his = colls.iterator().next();
			Collection<ActorHIS> actorhiss = his.getActorhiss();
			if(!actorhiss.isEmpty()){
				String initiatorID = actorhiss.iterator().next().getActorid();
				return (UserVO) userProcess.doView(initiatorID);
			}
		}
		
		return null;
	}
	
	
	/**
	 * 装载上级用户
	 * @param user
	 * @param userMap
	 * @throws Exception
	 */
	private static void loadSuperior(BaseUser user,LinkedHashMap<String, BaseUser> userMap)throws Exception{
		if(user.getSuperior() !=null){
			userMap.put(user.getSuperior().getId(), user.getSuperior());
		}
	}
	
	/**
	 * 装载下级用户
	 * @param user
	 * @param userMap
	 * @throws Exception
	 */
	private static void loadSubordinates(BaseUser user,LinkedHashMap<String, BaseUser> userMap)throws Exception{
		UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
		Collection<UserVO> lowerList = process.getUnderList(user.getId(), 1);
		for(Iterator<UserVO> iter = lowerList.iterator();iter.hasNext();){
			UserVO u = iter.next();
			userMap.put(u.getId(),u);
		}
	}
	
	/**
	 * 装载本部门的用户
	 * @param user
	 * @param userMap
	 * @throws Exception
	 */
	private static void loadUsersDefaultDeptUsers(BaseUser user,LinkedHashMap<String, BaseUser> userMap)throws Exception{
		DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
		DepartmentVO dept = (DepartmentVO) process.doView(user.getDefaultDepartment());
		if(dept != null){
			Collection<UserVO> _userList = dept.getUsers();
			for(Iterator<UserVO> iter = _userList.iterator();iter.hasNext();){
				UserVO u = iter.next();
				if(user.getDefaultDepartment().equals(u.getDefaultDepartment())){
					userMap.put(u.getId(),u);
				}
			}
		}
	}
	
	/**
	 * 装载直属上级部门用户
	 * @param user
	 * @param userMap
	 * @throws Exception
	 */
	private static void loadLineSuperiorDeptUsers(BaseUser user,LinkedHashMap<String, BaseUser> userMap)throws Exception{
		DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
		DepartmentVO dept = (DepartmentVO) process.doView(user.getDefaultDepartment());
		if(dept != null){
			DepartmentVO dep = dept.getSuperior();
			//DepartmentVO dep = process.findLineSuperiorByIndexCode(dept.getIndexCode());
			if(dep != null){
				Collection<UserVO> _userList = dep.getUsers();
				for(Iterator<UserVO> iter = _userList.iterator();iter.hasNext();){
					UserVO u = iter.next();
					userMap.put(u.getId(),u);
				}
			}
		}
	}
	
	/**
	 * 装载直属下级部门用户
	 * @param user
	 * @param userMap
	 * @throws Exception
	 */
	private static void loadLineSubordinateDeptUsers(BaseUser user,LinkedHashMap<String, BaseUser> userMap)throws Exception{
		DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
		DepartmentVO dept = (DepartmentVO) process.doView(user.getDefaultDepartment());
		if(dept != null){
			Collection<DepartmentVO> deptList = process.getDatasByParent(dept.getId());
			//Collection<DepartmentVO> deptList = process.doQueryLineSubordinatesByIndexCode(dept.getIndexCode());
			for(Iterator<DepartmentVO> it = deptList.iterator();it.hasNext();){
				DepartmentVO dep = it.next();
				Collection<UserVO> _userList = dep.getUsers();
				for(Iterator<UserVO> iter = _userList.iterator();iter.hasNext();){
					UserVO u = iter.next();
					userMap.put(u.getId(),u);
				}
			}
		}
	}
	
	/**
	 * 装载所有上级部门用户
	 * @param user
	 * @param userMap
	 * @throws Exception
	 */
	private static void loadAllSuperiorDeptUsers(BaseUser user,LinkedHashMap<String, BaseUser> userMap)throws Exception{
		DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
		DepartmentVO dept = (DepartmentVO) process.doView(user.getDefaultDepartment());
		if(dept != null){
			Collection<DepartmentVO> deptList = process.getSuperiorDeptListExcludeCurrent(dept.getId());
			//Collection<DepartmentVO> deptList = process.doQueryAllSuperiorsByIndexCode(dept.getIndexCode());
			for(Iterator<DepartmentVO> it = deptList.iterator();it.hasNext();){
				DepartmentVO dep = it.next();
				Collection<UserVO> _userList = dep.getUsers();
				for(Iterator<UserVO> iter = _userList.iterator();iter.hasNext();){
					UserVO u = iter.next();
					userMap.put(u.getId(),u);
				}
			}
		}
	}
	
	/**
	 * 装载所有下级部门用户
	 * @param user
	 * @param userMap
	 * @throws Exception
	 */
	private static void loadAllSubordinateDeptUsers(BaseUser user,LinkedHashMap<String, BaseUser> userMap)throws Exception{
		DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
		DepartmentVO dept = (DepartmentVO) process.doView(user.getDefaultDepartment());
		if(dept != null){
			Collection<DepartmentVO> deptList = process.getUnderDeptList(dept.getId(),Integer.MAX_VALUE,true);
			//Collection<DepartmentVO> deptList = process.doQueryAllSubordinatesByIndexCode(dept.getIndexCode());
			for(Iterator<DepartmentVO> it = deptList.iterator();it.hasNext();){
				DepartmentVO dep = it.next();
				Collection<UserVO> _userList = dep.getUsers();
				for(Iterator<UserVO> iter = _userList.iterator();iter.hasNext();){
					UserVO u = iter.next();
					userMap.put(u.getId(),u);
				}
			}
		}
	}

	/**
	 * 获取节点的负责人列表
	 * 
	 * @param node
	 * @param domainid
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public static Collection<BaseUser> getPrincipalList(Document doc,ParamsTable params, Node node, String domainid,
			String applicationid,BaseUser auditor) throws Exception {

		if (!StringUtil.isBlank(params.getParameterAsString("_subFlowApproverInfo"))
				&& !StringUtil.isBlank(params.getParameterAsString("_subFlowNodeId"))
				&& !StringUtil.isBlank(params.getParameterAsString("_position"))) {
			return getSubFlowApprover(doc,params, node, domainid, applicationid,auditor);
		}
		return getPrincipalMap(doc,params, node, domainid, applicationid,auditor).values();
	}

	/**
	 * 获取子流程第一个节点的处理人
	 * @param doc
	 * @param params
	 * @param node
	 * @param domainid
	 * @param applicationid
	 * @param auditor
	 * @return
	 * @throws Exception
	 */
	public static Collection<BaseUser> getSubFlowApprover(Document doc,ParamsTable params, Node node, String domainid,
			String applicationid ,BaseUser auditor) throws Exception {

		Collection<BaseUser> rtn = new ArrayList<BaseUser>();

		String _subFlowApproverInfo = params.getParameterAsString("_subFlowApproverInfo");
		String _subFlowNodeId = params.getParameterAsString("_subFlowNodeId");
		String _position = params.getParameterAsString("_position");

		if (!StringUtil.isBlank(_subFlowApproverInfo) && !StringUtil.isBlank(_subFlowNodeId)
				&& !StringUtil.isBlank(_position)) {

			Collection<Object> col = JsonUtil.toCollection(_subFlowApproverInfo);
			boolean hit = false;
			for (Iterator<Object> iter = col.iterator(); iter.hasNext();) {
				@SuppressWarnings("unchecked")
				Map<String, Object> item = (Map<String, Object>) iter.next();
				String nodeid = (String) item.get("nodeid");
				if (_subFlowNodeId.equals(nodeid)) {
					hit = true;
					 Object[] approvers = (Object[]) item.get("approver");
					 for(int i=0;i<approvers.length;i++){
						@SuppressWarnings("unchecked")
						Map<String, Object> approver = (Map<String, Object>)approvers[i];
						String position = String.valueOf(approver.get("position"));

						if (_position.equals(position)) {
							UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
							Object[] users = (Object[]) approver.get("userids");
							StringBuffer userids = new StringBuffer();
							for (int j = 0; j < users.length; j++) {
								userids.append("'").append(users[j].toString()).append("',");
							}
							if (userids.length() > 0) {
								userids.setLength(userids.length() - 1);
								String hql = "FROM " + UserVO.class.getName() + " WHERE id in(" + userids.toString()
										+ ")";
								Collection<UserVO> u = userProcess.doQueryByHQL(hql);
								if (u != null && !u.isEmpty())
									rtn.addAll(u);
							}

							break;
						}

					}
					break;
				}
			}
			if(!hit){
				rtn = getPrincipalMap(doc,params, node, domainid, applicationid,auditor).values();
			}
		}
		return rtn;
	}

	/**
	 * 获取节点的抄送人
	 * 
	 * @param params
	 * @param node
	 * @param domainid
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public static Collection<BaseUser> getCirculatorList(ParamsTable params, Document doc, Node node,
			String domainid, String applicationid) throws Exception {

		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		RoleProcess roleProcess = (RoleProcess) ProcessFactory.createProcess(RoleProcess.class);
		DepartmentProcess deptProcess = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		Collection<BaseUser> users = new ArrayList<BaseUser>();
		if (!(node instanceof ManualNode)) {
			return users;
		}

		String _circulatorInfo = params.getParameterAsString("_circulatorInfo");

		// 用户指定抄送人
		if (!StringUtil.isBlank(_circulatorInfo)) {
			users.addAll(getCirculatorsByParameter(params, node, domainid, applicationid));
		} else {// 用户不指定抄送人，默认抄送给该节点下所有抄送人
			ManualNode mNode = (ManualNode) node;
			switch (mNode.circulatorEditMode) {
			case ManualNode.CIRCULATOR_EDIT_MODE_CODE:
				if (StringUtil.isBlank(mNode.circulatorListScript)) {
					return users;
				}
				IRunner runner = JavaScriptFactory.getInstance("", applicationid);

				// 重新注册一些公共工具类 start
				if (params != null && params.getHttpRequest() != null) {
					HttpServletRequest request = params.getHttpRequest();
					// Document doc = (Document)request.getAttribute("content");
					WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
					runner.initBSFManager(doc, params, webUser, new ArrayList());// happy
				}
				// ----------------- end

				StringBuffer label = new StringBuffer();
				label.append("ManualNode(").append(node.id).append(").").append(node.name).append(
						".circulatorListScript");
				Object obj = runner.run(label.toString(), StringUtil.dencodeHTML(mNode.circulatorListScript));

				// 兼容多种返回值，BaseUser,Collection<BaseUser>,BaseUser[],Collection<String>,NativeArray
				if (BaseUser.class.isAssignableFrom(obj.getClass())) {
					BaseUser user = (BaseUser) obj;
					users.add(user);
				} else if (obj instanceof UserVO) {
					UserVO user = (UserVO) obj;
					users.add(user);
				} else {
					Collection userList = new ArrayList(); // 用户列表
					if (obj instanceof Collection) {
						userList = (Collection) obj;
					} else if (obj instanceof UserVO[]) {
						userList = Arrays.asList((UserVO[]) obj);
					}
					for (Iterator iterator = userList.iterator(); iterator.hasNext();) {
						Object userObj = iterator.next();
						UserVO user = null;
						if (userObj instanceof String) {
							user = (UserVO) userProcess.doView((String) userObj);
						} else {
							user = (UserVO) userObj;
						}
						if (user != null) {
							users.add(user);
						}
					}
				}
				break;
			case ManualNode.CIRCULATOR_EDIT_MODE_DESIGN:
				NameList nameList = NameList.parser(mNode.circulatorNamelist);
				Collection<NameNode> nameNodeList = nameList.toNameNodeCollection();
				if (nameNodeList == null)
					return users;
				for (Iterator iter = nameNodeList.iterator(); iter.hasNext();) {
					NameNode nameNode = (NameNode) iter.next();
					String actorId = nameNode.getId();

					UserVO user = null;
					switch (nameNode.getType()) {
					case Type.TYPE_USER:
						user = (UserVO) userProcess.doView(actorId);
						users.add(user);
						break;
					case Type.TYPE_ROLE:
						RoleVO roleVO = (RoleVO) roleProcess.doView(actorId);
						if(roleVO != null && RoleVO.STATUS_VALID == roleVO.getStatus()){
							Collection roleUsers = roleVO.getUsersByDomain(domainid);
							for (Iterator iterator = roleUsers.iterator(); iterator.hasNext();) {
								user = (UserVO) iterator.next();
								users.add(user);
							}
						}
						break;
					case Type.TYPE_DEPARTMENT:
						DepartmentVO dept = (DepartmentVO) deptProcess.doView(actorId);
						Collection deptUsers = dept.getUsers();
						for (Iterator iterator = deptUsers.iterator(); iterator.hasNext();) {
							user = (UserVO) iterator.next();
							users.add(user);
						}
						break;
					default:
						break;
					}
				}
				break;
				//用户设计模式
			case ManualNode.CIRCULATOR_EDIT_MODE_USER_DESIGN:
				NameList userList = NameList.parser(mNode.circulatorNamelistByUser);
				Collection<NameNode> nameNodes = userList.toNameNodeCollection();
				if (nameNodes == null)
					return users;
				for (Iterator<NameNode> iter = nameNodes.iterator(); iter.hasNext();) {
					NameNode nameNode = (NameNode) iter.next();
					String userId = nameNode.getId();
					UserVO user = null;
					switch (nameNode.getType()) {
					case Type.TYPE_USER:
						user = (UserVO) userProcess.doView(userId);
						users.add(user);
						break;
					case Type.TYPE_ROLE:
						RoleVO roleVO = (RoleVO) roleProcess.doView(userId);
						Collection roleUsers = roleVO.getUsersByDomain(domainid);
						for (Iterator iterator = roleUsers.iterator(); iterator.hasNext();) {
							user = (UserVO) iterator.next();
							users.add(user);
						}
						break;
					case Type.TYPE_DEPARTMENT:
						DepartmentVO dept = (DepartmentVO) deptProcess.doView(userId);
						Collection deptUsers = dept.getUsers();
						for (Iterator iterator = deptUsers.iterator(); iterator.hasNext();) {
							user = (UserVO) iterator.next();
							users.add(user);
						}
						break;
					default:
						break;
					}
				}
				break;
			default:
				break;
			}
		}
		return users;
	}

	/**
	 * 根据参数获取前台指定的抄送人
	 * 
	 * @param params
	 * @param node
	 * @param domainid
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public static Collection<BaseUser> getCirculatorsByParameter(ParamsTable params, Node node, String domainid,
			String applicationid) throws Exception {

		Collection<BaseUser> rtn = new ArrayList<BaseUser>();

		String _circulatorInfo = params.getParameterAsString("_circulatorInfo");

		if (!StringUtil.isBlank(_circulatorInfo)) {

			Collection<Object> col = JsonUtil.toCollection(_circulatorInfo);

			for (Iterator<Object> iter = col.iterator(); iter.hasNext();) {
				@SuppressWarnings("unchecked")
				Map<String, Object> item = (Map<String, Object>) iter.next();
					UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
					Object[] circulators = (Object[]) item.get("circulator");
					StringBuffer userids = new StringBuffer();
					for (int j = 0; j < circulators.length; j++) {
						userids.append("'").append(circulators[j].toString()).append("',");
					}
					if (userids.length() > 0) {
						userids.setLength(userids.length() - 1);
						String hql = "FROM " + UserVO.class.getName() + " WHERE id in(" + userids.toString() + ")";
						Collection<UserVO> u = userProcess.doQueryByHQL(hql);
						if (u != null && !u.isEmpty())
							rtn.addAll(u);
					}
			}
		}
		return rtn;
	}

	/**
	 * 获取节点的负责人ID列表
	 * 
	 * @param node
	 * @param domainid
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public static Collection<String> getPrincipalIdList(Document doc,ParamsTable params, Node node, String domainid,
			String applicationid,BaseUser auditor) throws Exception {
		return getPrincipalMap(doc,params, node, domainid, applicationid,auditor).keySet();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	/**
	 * 为流程的list生成一个有checkbox
	 * 
	 * @param moduleId
	 * @param divid
	 * @return
	 * @throws Exception
	 */

	public String getBillDefiNameCheckBox(String moduleId, String divid) throws Exception {
		BillDefiProcess fp = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		Collection<BillDefiVO> col = fp.getBillDefiByModule(moduleId);
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (col != null) {
			for (Iterator<BillDefiVO> iter = col.iterator(); iter.hasNext();) {
				BillDefiVO vo = (BillDefiVO) iter.next();
				map.put(vo.getId(), vo.getSubject());
			}
		}
		String[] str = new String[10];
		return DWRHtmlUtils.createFiledCheckbox(map, divid, str);
	}

	public String toFlowHtmlText(String docid, HttpServletRequest request) throws Exception {
		WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		Document doc = (Document) webUser.getFromTmpspace(docid);
		if (doc != null) {
			BillDefiVO flowVO = doc.getFlowVO();
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					flowVO.getApplicationid());
			doc = (Document) process.doView(docid);

			Form form = FormHelper.get_FormById(doc.getFormid());
			Activity flowAct = form.getActivityByType(ActivityType.WORKFLOW_PROCESS);
			String flowShowType = flowAct.getFlowShowType();

			StateMachineHelper helper = new StateMachineHelper(doc);
			return helper.toFlowHtmlText(doc, webUser, flowShowType, request);
		}

		return "";
	}
	
	public String toFlowDialogHtmlTextByState(String docid,String stateId,String applicationid, HttpServletRequest request) throws Exception {
		WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		Document doc = (Document) webUser.getFromTmpspace(docid);
		FlowStateRT instance = null;
		if(!StringUtil.isBlank(stateId)){
			FlowStateRTProcess stateProcess = (FlowStateRTProcess)ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class,applicationid);
			instance = ((FlowStateRT) stateProcess.doView(stateId));
		}
		if (doc != null && instance != null) {
			
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationid);
			doc = (Document) process.doView(docid);
			doc.setState(instance);

			Form form = FormHelper.get_FormById(doc.getFormid());
			Activity flowAct = form.getActivityByType(ActivityType.WORKFLOW_PROCESS);
			String flowShowType = flowAct.getFlowShowType();

			StateMachineHelper helper = new StateMachineHelper(doc);
			return helper.toFlowDialogHtmlText(doc,ParamsTable.convertHTTP(request), webUser, flowShowType);
		}

		return "";
	}
	
	public String toFlowHtmlTextByState(String docid,String stateId,String applicationid, HttpServletRequest request) throws Exception {
		WebUser webUser = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		Document doc = (Document) webUser.getFromTmpspace(docid);
		FlowStateRT instance = null;
		if(!StringUtil.isBlank(stateId)){
			FlowStateRTProcess stateProcess = (FlowStateRTProcess)ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class,applicationid);
			instance = ((FlowStateRT) stateProcess.doView(stateId));
		}
		if (doc != null && instance != null) {
			
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationid);
			doc = (Document) process.doView(docid);
			doc.setState(instance);
				
			//Form form = FormHelper.get_FormById(doc.getFormid());
			//Activity flowAct = form.getActivityByType(ActivityType.WORKFLOW_ADJUSTMENT);
			String flowShowType = "ST01";
			if(StringUtil.isBlank(instance.getFlowXML())){
				StateMachine.updateImage(instance);
			}
			StateMachineHelper helper = new StateMachineHelper(doc);
			return helper.toFlowHtmlText(doc, webUser, flowShowType, request);
		}

		return "";
	}
	
}
