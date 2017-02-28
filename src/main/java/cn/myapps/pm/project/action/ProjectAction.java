package cn.myapps.pm.project.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.base.action.BaseAction;
import cn.myapps.pm.project.ejb.Member;
import cn.myapps.pm.project.ejb.Project;
import cn.myapps.pm.project.ejb.ProjectProcess;
import cn.myapps.pm.project.ejb.ProjectProcessBean;
import cn.myapps.pm.util.json.JsonUtil;
import cn.myapps.util.StringUtil;

public class ProjectAction extends BaseAction<Project> {
	
	private static final long serialVersionUID = 3936861257468915570L;
	
	public ProjectAction() {
		super();
		content = new Project();
		process = new ProjectProcessBean();
	}

	public String doCreate(){
		WebUser user = getUser();
		
		Project project = (Project) getContent();
		project.setCreator(user.getName());
		
		project.setCreatorId(user.getId());
		project.setDomainid(user.getDomainid());
		project.setManager(user.getName());
		project.setManagerId(user.getId());
		project.setCreateDate(new Date());
		try {
			long count = ((ProjectProcess)process).countByName(project.getName());
			if(count>0){
				throw new OBPMValidateException("项目名已存在");
			}
			project = (Project) ((ProjectProcess)process).doCreate(project,user);
			addActionResult(true, "添加成功", project);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}

	public String doSave(){
		Project project = (Project) getContent();
		try {
			Project old = (Project)process.doView(project.getId());
			if(old !=null && !old.getName().equals(project.getName())){
				long count = ((ProjectProcess)process).countByName(project.getName());
				if(count>0){
					throw new OBPMValidateException("项目名已存在");
				}
			}
			((ProjectProcess)process).updateName(project.getName(), project.getId());
			addActionResult(true, "保存成功", null);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
		
	}
	
	public String doView(){
		try {
			ParamsTable params  = getParams();
			String id = params.getParameterAsString("id");
			Project project = (Project)process.doView(id);
			addActionResult(true, null, project);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
		}
		
		return SUCCESS;
	}
	
	public String doQueryMyProject(){
		ParamsTable params  = getParams();
		WebUser user = getUser();
		try {
			Collection<Project> datas = ((ProjectProcess)process).doQueryMyProject(params, user);
			addActionResult(true, null, datas);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	}
	
	public String doQuery(){
		ParamsTable params  = getParams();
		WebUser user = getUser();
		try {
			Collection<Project> datas = ((ProjectProcess)process).doSimpleQuery(params, user);
			addActionResult(true, null, datas);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	}
	
	public String doRemove(){
		
		try {
			for(String pk : get_selects()){
				process.doRemove(pk);
			}
			addActionResult(true, "删除成功", null);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
		
	}
	
	/**
	 * 添加项目成员
	 * @return
	 */
	public String doAddMembers(){
		
		try {
			Collection<Member> members = new ArrayList<Member>();
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			String _members = params.getParameterAsString("members");
			String memberType = params.getParameterAsString("memberType");
			if(!StringUtil.isBlank(_members)){
				members.addAll((Collection<? extends Member>) JsonUtil.toCollection(_members, Member.class));
			}
			((ProjectProcess) process).addMembers(members, id);
			addActionResult(true, "添加成功", process.doView(id));
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 移除项目成员
	 * @return
	 */
	public String doRemoveMember(){
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			String userId = params.getParameterAsString("userId");
			int memberType = params.getParameterAsInteger("memberType");
			((ProjectProcess) process).deleteMember(userId,id,memberType);
			addActionResult(true, "删除成功", process.doView(id));
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
		
	}
	
	/**
	 * 设置项目经理
	 * @return
	 */
	public String doSetProjectManager(){
		
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			String userId = params.getParameterAsString("userId");
			
			((ProjectProcess) process).setProjectManager(userId, id);
			addActionResult(true, "设置成功", process.doView(id));
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	/**
	 *  通过ProjectId获取项目下的所有成员
	 * @return
	 */
	public String doGetMembersByProject(){
		
		
		try {
			ParamsTable params  = getParams();
			String projectId= params.getParameterAsString("projectId");		
			String memberGroup = params.getParameterAsString("memberGroup");	
			
			Collection<Member> membersList = ((ProjectProcess) process).getMembersByProject(projectId,memberGroup);
			addActionResult(true, "获取成功",membersList );
			
			
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
		
	}
	
	
	/**
	 *  通过ProjectId获取项目,修改是否微信通知
	 * @return
	 */
	public String doUpdateNotification(){
		try {
			ParamsTable params = getParams();
			String projectId = params.getParameterAsString("projectId");
			int notification ;
			if(!StringUtil.isBlank(params.getParameterAsString("notification"))){
				notification = params.getParameterAsInteger("notification");
			}else{
				notification = 0;
			}
			Project project = (Project)process.doView(projectId);
			
			if(notification == 1){
				project.setNotification(true);
			}else if(notification == 0){
				project.setNotification(false);
			}
			
			((ProjectProcess) process).doUpdate(project);
			
			addActionResult(true, "设置成功", null);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}

}
