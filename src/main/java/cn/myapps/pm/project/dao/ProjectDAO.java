package cn.myapps.pm.project.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.base.dao.BaseDAO;
import cn.myapps.pm.project.ejb.Member;
import cn.myapps.pm.project.ejb.Project;

public interface ProjectDAO extends BaseDAO {
	
	/**
	 * 根据项目名称查询项目总数
	 * @param name
	 * 		项目名称
	 * @return
	 * 		项目总数
	 * @throws Exception
	 */
	public long countByName(String name) throws Exception;
	
	/**
	 * 查询项目的成员集合
	 * @param projectId
	 * 		项目主键
	 * @return
	 * @throws Exception
	 */
	public Collection<Member> queryMembersByProject(String projectId,String memberGroup) throws Exception;
	
	/**
	 * 添加项目成员
	 * @param members
	 * 		成员集合
	 * @param projectId
	 * 		项目主键
	 * @throws Exception
	 */
	public void addMembers(Collection<Member> members,String projectId) throws Exception;
	
	/**
	 * 删除项目成员
	 * @param userId
	 * 		用户主键
	 * @param projectId
	 * 		项目主键
	 * @param memberType
	 *      成员类型
	 * @throws Exception
	 */
	public void deleteMember(String userId,String projectId,int memberType) throws Exception;
	
	/**
	 * 设置用户为项目经理
	 * @param userId
	 * 		用户主键
	 * @param projectId
	 * 		项目主键
	 * @throws Exception
	 */
	public void setProjectManager(String userId,String projectId) throws Exception;
	
	/**
	 * 增加一个任务总数
	 * @param projectId
	 * 		项目主键
	 * @throws Exception
	 */
	public void addTasksTotal(String projectId)throws Exception;
	
	/**
	 * 增加一个已完成任务数
	 * @param projectId
	 * 		项目主键
	 * @throws Exception
	 */
	public void addFinishedTasksNum(String projectId)throws Exception;
	
	/**
	 * 减少一个任务总数
	 * @param projectId
	 * 		项目主键
	 * @throws Exception
	 */
	public void subtractTasksTotal(String projectId)throws Exception;
	
	/**
	 * 减少一个已完成任务总数
	 * @param projectId
	 * 		项目主键
	 * @throws Exception
	 */
	public void subtractFinishedTasksNum(String projectId)throws Exception;
	
	
	/**
	 * 查询我的项目
	 * @param params
	 * 		参数
	 * @param user
	 * 		当前用户对象
	 * @return
	 * 		项目集合
	 * @throws Exception
	 */
	public Collection<Project> queryMyProject(ParamsTable params,WebUser user) throws Exception;

}
