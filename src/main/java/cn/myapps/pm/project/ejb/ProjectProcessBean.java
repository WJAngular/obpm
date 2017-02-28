package cn.myapps.pm.project.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.base.dao.BaseDAO;
import cn.myapps.pm.base.dao.DaoManager;
import cn.myapps.pm.base.ejb.AbstractBaseProcessBean;
import cn.myapps.pm.project.dao.ProjectDAO;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;


public class ProjectProcessBean extends AbstractBaseProcessBean<Project>
		implements ProjectProcess {

	private static final long serialVersionUID = 3833895426776628591L;

	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getProjectDAO(getConnection());
	}

	public long countByName(String name) throws Exception {
		return ((ProjectDAO)getDAO()).countByName(name);
	}

	public void updateName(String name, String pk) throws Exception {
		Map<String,Object> items = new HashMap<String, Object>();
		items.put("NAME", name);
		((ProjectDAO)getDAO()).update(items, pk);
	}

	public Collection<Member> getMembersByProject(String projectId,String memberGroup)
			throws Exception {
		return ((ProjectDAO)getDAO()).queryMembersByProject(projectId,memberGroup);
	}

	public void addMembers(Collection<Member> addMember, String projectId)
			throws Exception {
		try {
			Collection<Member> members = new ArrayList<Member>();
			Collection<Member> allMember = getMembersByProject(projectId, "");
			
			// 避免在同一个项目里添加角色相同的成员
			for (Member add : addMember) {
				boolean find = false;
				for (Member all : allMember) {
					if (add.getUserId().equals(all.getUserId()) && add.getMemberType() == all.getMemberType()) {
						find = true;
						break;
					}
				}
				if (!find)
					members.add(add);
			}
			
			beginTransaction();
			((ProjectDAO)getDAO()).addMembers(members, projectId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}

	public void deleteMember(String userId, String projectId,int memberType) throws Exception {
		try {
			beginTransaction();
			((ProjectDAO)getDAO()).deleteMember(userId, projectId,memberType);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}

	public void setProjectManager(String userId, String projectId)
			throws Exception {
		try {
			beginTransaction();
			((ProjectDAO)getDAO()).setProjectManager(userId, projectId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}

	public void addFinishedTasksNum(String projectId) throws Exception {
		try {
			beginTransaction();
			((ProjectDAO)getDAO()).addFinishedTasksNum(projectId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}

	public void addTasksTotal(String projectId) throws Exception {
		try {
			beginTransaction();
			((ProjectDAO)getDAO()).addTasksTotal(projectId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}

	public void subtractFinishedTasksNum(String projectId) throws Exception {
		try {
			beginTransaction();
			((ProjectDAO)getDAO()).subtractFinishedTasksNum(projectId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}

	public void subtractTasksTotal(String projectId) throws Exception {
		try {
			beginTransaction();
			((ProjectDAO)getDAO()).subtractTasksTotal(projectId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}

	public Project doCreate(Project project, WebUser user) throws Exception {
		try {
			if(StringUtil.isBlank(project.getId())){
				project.setId(Sequence.getSequence());
			}
			Collection<Member> members = new ArrayList<Member>();
			Member member = new Member();
			member.setMemberType(Member.MEMBER_MANAGER);
			member.setUserId(user.getId());
			member.setUserName(user.getName());
			members.add(member);
			
			beginTransaction();
			((ProjectDAO)getDAO()).create(project);
			((ProjectDAO)getDAO()).addMembers(members, project.getId());
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return project;
	}

	public Collection<Project> doQueryMyProject(ParamsTable params, WebUser user)
			throws Exception {
		return ((ProjectDAO)getDAO()).queryMyProject(params, user);
	}


}
